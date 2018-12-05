package com.test;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.swing.JWindow;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;

public class MyTest2 {

	static NetSDKLib netsdkApi = NetSDKLib.NETSDK_INSTANCE;
	static NetSDKLib configApi = NetSDKLib.CONFIG_INSTANCE;

	private NativeLong loginHandle;

	private static class DisconnectCallback implements NetSDKLib.fDisConnect {
		private static DisconnectCallback instance = new DisconnectCallback();

		private DisconnectCallback() {
		}

		public static DisconnectCallback getInstance() {
			return instance;
		}

		public void invoke(NativeLong lLoginID, String pchDVRIP, int nDVRPort, NativeLong dwUser) {
			System.out.printf("Device[%s:%d] Disconnect!\n", pchDVRIP, nDVRPort);
		}
	}

	private static class HaveReconnectCallback implements NetSDKLib.fHaveReConnect {
		private static HaveReconnectCallback instance = new HaveReconnectCallback();

		private HaveReconnectCallback() {
		}

		public static HaveReconnectCallback getInstance() {
			return instance;
		}

		public void invoke(NativeLong lLoginID, String pchDVRIP, int nDVRPort, NativeLong dwUser) {
			System.out.printf("Device[%s:%d] HaveReconnected!\n", pchDVRIP, nDVRPort);
		}
	}

	public void initTest() {
		// 初始化SDK库
		netsdkApi.CLIENT_Init(DisconnectCallback.getInstance(), null);

		// 设置断线自动重练功能
		netsdkApi.CLIENT_SetAutoReconnect(HaveReconnectCallback.getInstance(), null);

		// 向设备登入
		final int nSpecCap = 0; /// login device by TCP
		final IntByReference error = new IntByReference();
		final String address = "192.168.124.69";
		final int port = 37777;
		final String usrname = "admin";
		final String password = "admin123";
		final NetSDKLib.NET_DEVICEINFO deviceInfo = new NetSDKLib.NET_DEVICEINFO();

		loginHandle = netsdkApi.CLIENT_LoginEx(address, (short) port, usrname, password, nSpecCap, null, deviceInfo,
				error);
		if (loginHandle.longValue() == 0) {
			System.err.printf("Login Device [%s:%d] Failed ! Last Error[%x]\n", address, port,
					netsdkApi.CLIENT_GetLastError());
			return;
		}

		System.out.printf("Login Device [%s:%d] Success. \n", address, port);
	}

	public static class SnapCallback implements NetSDKLib.fSnapRev {
		private static String potoUrl = "";
		private static SnapCallback instance = new SnapCallback();

		private SnapCallback() {
		}

		public static SnapCallback getInstance() {
			return instance;
		}

		public void invoke(NativeLong lLoginID, Pointer pBuf, int RevLen, int EncodeType, NativeLong CmdSerial,
				NativeLong dwUser) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyMMddHHmmss");
			String fileName = "AsyncSnapPicture_" + dateFormat.format(new Date()) + "_" + CmdSerial.intValue() + ".jpg";

			// 保存图片到本地文件
			INetSDK.savePicture(pBuf, RevLen, "E:/" + fileName);
			potoUrl = "E:/" + fileName;
			System.out.println("--> Get Picture " + new File(potoUrl).getAbsolutePath());

			// 成功生成图片之后, 进行通知
			synchronized (SnapCallback.class) {
				SnapCallback.class.notify();
			}
		}

		public String getPotoUrl() {
			return potoUrl;
		}
	}

	/**
	 * 异步方式抓图 ：适用于 IPC, 球机等非智能交通、停车场设备 建议抓拍的频率不要超过1s
	 */
	public synchronized void asyncSnapPicture() {
		if (loginHandle.longValue() == 0) {
			return;
		}
		/// 设置抓图回调: 图片主要在 SnapCallback.getInstance() invoke. 中返回
		netsdkApi.CLIENT_SetSnapRevCallBack(SnapCallback.getInstance(), null);

		NetSDKLib.SNAP_PARAMS snapParam = new NetSDKLib.SNAP_PARAMS();
		snapParam.Channel = 0; // 抓图通道
		snapParam.mode = 0; // 表示请求一帧
		snapParam.CmdSerial = serialNum++; // 请求序列号，有效值范围 0~65535，超过范围会被截断

		/// 触发抓图动作
		if (!netsdkApi.CLIENT_SnapPictureEx(loginHandle, snapParam, null)) {
			System.err.printf("CLIENT_SnapPictureEx Failed ! Last Error[%x]\n", netsdkApi.CLIENT_GetLastError());
			return;
		}

		// 以下保证图片数据的生成
		try {
			synchronized (SnapCallback.class) {
				SnapCallback.class.wait(3000L); // 默认等待3s,
												// 防止设备断线时抓拍回调没有被触发，而导致死等
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("--> " + Thread.currentThread().getName() + " CLIENT_SnapPictureEx Success."
				+ System.currentTimeMillis());
	}

	private int serialNum = 1;

	public void centerWindow(Container window) {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = window.getSize().width;
		int h = window.getSize().height;
		int x = (dim.width - w) / 2;
		int y = (dim.height - h) / 2;
		window.setLocation(x, y);
	}

	public void realPlayByDataType() {

		final JWindow wnd = new JWindow();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.height /= 2;
		screenSize.width /= 2;
		wnd.setSize(screenSize);
		centerWindow(wnd);
		wnd.setVisible(true);

		String fileName = "Video_" + UUID.randomUUID().toString() + ".dav";
		String videoUrl ="E:/" + fileName;

		int channel = 0; // 预览通道号
		int playType = NetSDKLib.NET_RealPlayType.NET_RType_Realplay; // 实时预览
		NativeLong lRealHandle = netsdkApi.CLIENT_RealPlayEx(loginHandle, channel, Native.getComponentPointer(wnd),
				playType);
		if (lRealHandle.longValue() != 0) {
			System.out.println("RealPlayByDataType Succeed!");
		} else {
			System.err.println("RealPlayByDataType Failed!" + netsdkApi.CLIENT_GetLastError());
			wnd.setVisible(false);
			return;
		}

		// System.out.println("开始录制视频");
		netsdkApi.CLIENT_SaveRealData(lRealHandle, videoUrl);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 停止预览
		netsdkApi.CLIENT_StopSaveRealData(lRealHandle);
		// System.out.println("停止录制视频");
		wnd.setVisible(false);
		System.out.println("视频地址：" + videoUrl);
	}

	public void runTest() {
		System.out.println("Run Test");
		String str = "";
		try {
			asyncSnapPicture();
			str = SnapCallback.getInstance().getPotoUrl();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(str);
		//realPlayByDataType();//录制视频
	}

	public void endTest() {
		if (loginHandle.longValue() != 0) {
			netsdkApi.CLIENT_Logout(loginHandle);
			loginHandle.setValue(0);
		}

		/// 清理资源, 只需调用一次
		netsdkApi.CLIENT_Cleanup();

		System.out.println("See You...");
	}

	public static void main(String[] args) {
		MyTest2 demo = new MyTest2();
		demo.initTest();
		demo.runTest();
		demo.endTest();
	}
}
