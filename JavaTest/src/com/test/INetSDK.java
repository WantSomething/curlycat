package com.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.test.NetSDKLib.*;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Memory;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

/**
 * SDK 接口简化封装
 */
public final class INetSDK {
	static NetSDKLib netsdkapi = NetSDKLib.NETSDK_INSTANCE;
	static NetSDKLib configapi = NetSDKLib.CONFIG_INSTANCE;
	
	public INetSDK() {

	}
	
    /************************************************************************
     ** 接口
     ***********************************************************************/
    //  JNA直接调用方法定义，cbDisConnect实际情况并不回调Java代码，仅为定义可以使用如下方式进行定义。 fDisConnect 回调
    public static boolean init(StdCallCallback cbDisConnect) {
    	return netsdkapi.CLIENT_Init(cbDisConnect, null);
    }
    
    //  JNA直接调用方法定义，SDK退出清理
    public static void cleanup() {
    	netsdkapi.CLIENT_Cleanup();
    }
    
    //  JNA直接调用方法定义，设置断线重连成功回调函数，设置后SDK内部断线自动重连, fHaveReConnect 回调
    public static void setAutoReconnect(StdCallCallback cbAutoConnect) {
    	netsdkapi.CLIENT_SetAutoReconnect(cbAutoConnect, null);
    }
    
    // 返回函数执行失败代码
    public static int getLastError() {
    	return netsdkapi.CLIENT_GetLastError();
    }

    // 设置连接设备超时时间和尝试次数
    public static void setConnectTime(int nWaitTime, int nTryTimes) {
    	netsdkapi.CLIENT_SetConnectTime(nWaitTime, nTryTimes);
    }

    // 设置登陆网络环境
    public static void setNetworkParam(NET_PARAM pNetParam) {
    	netsdkapi.CLIENT_SetNetworkParam(pNetParam);
    }

    // 获取SDK的版本信息
    public static int getSDKVersion() {
    	return netsdkapi.CLIENT_GetSDKVersion();
    }
    
    //  JNA直接调用方法定义，登陆接口
    public static NativeLong loginEx(String pchDVRIP, 
						    		int wDVRPort, 
						    		String pchUserName, 
						    		String pchPassword, 
						    		int nSpecCap,
						    		NET_DEVICEINFO lpDeviceInfo) {
    	IntByReference error = new IntByReference(0);
		return netsdkapi.CLIENT_LoginEx(pchDVRIP, wDVRPort, pchUserName, pchPassword, nSpecCap, null, lpDeviceInfo, error);
	}
    
    //  JNA直接调用方法定义，登陆扩展接口///////////////////////////////////////////////////
    //  nSpecCap 对应  EM_LOGIN_SPAC_CAP_TYPE 登陆类型
    public static NativeLong loginEx2(String pchDVRIP, 
						    		int wDVRPort, 
						    		String pchUserName, 
						    		String pchPassword, 
						    		int nSpecCap, 
						    		NET_DEVICEINFO_Ex lpDeviceInfo) {
    	IntByReference error = new IntByReference(0);
    	return netsdkapi.CLIENT_LoginEx2(pchDVRIP, wDVRPort, pchUserName, pchPassword, nSpecCap, null, lpDeviceInfo, error);
    }
    
    //  JNA直接调用方法定义，向设备注销
    public static boolean logout(NativeLong lLoginID) {
    	return netsdkapi.CLIENT_Logout(lLoginID);
    }
    
    // 获取配置
    // error 为设备返回的错误码： 0-成功 1-失败 2-数据不合法 3-暂时无法设置 4-没有权限
    public static boolean getNewDevConfig(NativeLong lLoginID , 
									    		String szCommand , 
									    		int nChannelID , 
									    		byte[] szOutBuffer , 
									    		int dwOutBufferSize , 
									    		int waiitime) {
    	IntByReference error = new IntByReference(0);
    	return netsdkapi.CLIENT_GetNewDevConfig(lLoginID, szCommand, nChannelID, szOutBuffer, dwOutBufferSize, error, waiitime);
    }
    
    // 设置配置
    public static boolean setNewDevConfig(NativeLong lLoginID , 
							    		String szCommand , 
							    		int nChannelID , 
							    		byte[] szInBuffer, 
							    		int dwInBufferSize, 									    
							    		int waittime ) {
    	IntByReference error = new IntByReference(0);
    	IntByReference restart = new IntByReference(0);
    	return netsdkapi.CLIENT_SetNewDevConfig(lLoginID, szCommand, nChannelID, szInBuffer, dwInBufferSize, error, restart, waittime);
    }
    
    // 解析查询到的配置信息
    public static boolean parseData(String szCommand, 
						    		byte[] szInBuffer, 
						    		Pointer lpOutBuffer, 
						    		int dwOutBufferSize, 
						    		Pointer pReserved) {
    	return netsdkapi.CLIENT_ParseData(szCommand, szInBuffer, lpOutBuffer, dwOutBufferSize, pReserved);
    }

    // 组成要设置的配置信息
    public static boolean packetData(String szCommand, 
						    		Pointer lpInBuffer, 
						    		int dwInBufferSize, 
						    		byte[] szOutBuffer, 
						    		int dwOutBufferSize) {
    	return netsdkapi.CLIENT_PacketData(szCommand, lpInBuffer, dwInBufferSize, szOutBuffer, dwOutBufferSize);
    }

    // 设置报警回调函数, fMessCallBack 回调
    public static void  setDVRMessCallBack(StdCallCallback cbMessage) {
    	netsdkapi.CLIENT_SetDVRMessCallBack(cbMessage, null);
    }
    
    // 向设备订阅报警--扩展
    public static boolean startListenEx(NativeLong lLoginID) {
    	return netsdkapi.CLIENT_StartListenEx(lLoginID);
    }

    /////////////////////////////////人脸识别接口/////////////////////////////////////////
    //人脸识别数据库信息操作（包括添加,修改和删除）
    // pstInParam指向NET_IN_OPERATE_FACERECONGNITIONDB类型的指针
    // pstOutParam指向NET_OUT_OPERATE_FACERECONGNITIONDB类型的指针
    public static boolean  operateFaceRecognitionDB(NativeLong lLoginID, 
										    		NET_IN_OPERATE_FACERECONGNITIONDB  pstInParam, 
										    		NET_OUT_OPERATE_FACERECONGNITIONDB pstOutParam, 
										    		int nWaitTime) {
    	return netsdkapi.CLIENT_OperateFaceRecognitionDB(lLoginID, pstInParam, pstOutParam, nWaitTime);
    }
    
    //按条件查询人脸识别结果 
    // pstInParam指向NET_IN_STARTFIND_FACERECONGNITION类型的指针
    // pstOutParam指向NET_OUT_STARTFIND_FACERECONGNITION类型的指针
    public static boolean  startFindFaceRecognition(NativeLong lLoginID,  
										    		NET_IN_STARTFIND_FACERECONGNITION  pstInParam, 
										    		NET_OUT_STARTFIND_FACERECONGNITION pstOutParam, 
										    		int nWaitTime) {
    	return netsdkapi.CLIENT_StartFindFaceRecognition(lLoginID, pstInParam, pstOutParam, nWaitTime);
    }
    
    //查找人脸识别结果:nFilecount:需要查询的条数, 返回值为媒体文件条数 返回值<nFilecount则相应时间段内的文件查询完毕(每次最多只能查询20条记录)
    // pstInParam指向NET_IN_DOFIND_FACERECONGNITION类型的指针
    // pstOutParam指向NET_OUT_DOFIND_FACERECONGNITION类型的指针
    public static boolean  doFindFaceRecognition(NET_IN_DOFIND_FACERECONGNITION  pstInParam, 
									    		NET_OUT_DOFIND_FACERECONGNITION pstOutParam, 
									    		int nWaitTime) {
    	return netsdkapi.CLIENT_DoFindFaceRecognition(pstInParam, pstOutParam, nWaitTime);
    }
    
    //结束查询
    public static boolean stopFindFaceRecognition(long lFindHandle) {
    	return netsdkapi.CLIENT_StopFindFaceRecognition(lFindHandle);
    }
    
    //人脸检测(输入一张大图,输入大图中被检测出来的人脸图片)
    // pstInParam指向NET_IN_DETECT_FACE类型的指针
    // pstOutParam指向NET_OUT_DETECT_FACE类型的指针
    public static boolean  detectFace(NativeLong lLoginID,  
						    		 NET_IN_DETECT_FACE  pstInParam, 
						    		 NET_OUT_DETECT_FACE pstOutParam, 
						    		 int nWaitTime) {
    	return netsdkapi.CLIENT_DetectFace(lLoginID, pstInParam, pstOutParam, nWaitTime);
    }
    
    //人脸识别人员组操作（包括添加,修改和删除）
    // pstInParam指向NET_IN_OPERATE_FACERECONGNITION_GROUP类型的指针
    // pstOutParam指向NET_OUT_OPERATE_FACERECONGNITION_GROUP类型的指针
    public static boolean  operateFaceRecognitionGroup(NativeLong lLoginID,  
										    		  NET_IN_OPERATE_FACERECONGNITION_GROUP  pstInParam, 
										    		  NET_OUT_OPERATE_FACERECONGNITION_GROUP pstOutParam, 
										    		  int nWaitTime) {
    	return netsdkapi.CLIENT_OperateFaceRecognitionGroup(lLoginID, pstInParam, pstOutParam, nWaitTime);
    }
    
    //查询人脸识别人员组信息
    // pstInParam指向NET_IN_FIND_GROUP_INFO类型的指针
    // pstOutParam指向NET_OUT_FIND_GROUP_INFO类型的指针
    public static boolean  findGroupInfo(NativeLong NativeLong,  
									    		NET_IN_FIND_GROUP_INFO  pstInParam, 
									    		NET_OUT_FIND_GROUP_INFO pstOutParam, 
									    		int nWaitTime) {
    	return netsdkapi.CLIENT_FindGroupInfo(NativeLong, pstInParam, pstOutParam, nWaitTime);
    }
    
    //布控通道人员组信息
    // pstInParam指向NET_IN_SET_GROUPINFO_FOR_CHANNEL类型的指针
    // pstOutParam指向NET_OUT_SET_GROUPINFO_FOR_CHANNEL类型的指针
    public static boolean setGroupInfoForChannel(NativeLong lLoginID,  
									    		NET_IN_SET_GROUPINFO_FOR_CHANNEL  pstInParam, 
									    		NET_OUT_SET_GROUPINFO_FOR_CHANNEL pstOutParam, 
									    		int nWaitTime) {
    	return netsdkapi.CLIENT_SetGroupInfoForChannel(lLoginID, pstInParam, pstOutParam, nWaitTime);
    }
    
    //订阅人脸查询状态
    // pstInParam指向NET_IN_FACE_FIND_STATE类型的指针
    // pstOutParam指向NET_OUT_FACE_FIND_STATE类型的指针
    public static NativeLong attachFaceFindState(NativeLong lLoginID,  
									    		NET_IN_FACE_FIND_STATE pstInParam, 
									    		NET_OUT_FACE_FIND_STATE pstOutParam, 
									    		int nWaitTime) {
    	return netsdkapi.CLIENT_AttachFaceFindState(lLoginID, pstInParam, pstOutParam, nWaitTime);
    }
    
    //取消订阅人脸查询状态,lAttachHandle为CLIENT_AttachFaceFindState返回的句柄
    public static boolean detachFaceFindState(NativeLong lAttachHandle) {
    	return netsdkapi.CLIENT_DetachFaceFindState(lAttachHandle);
    }
    
    // 打开日志功能
    // pstLogPrintInfo指向LOG_SET_PRINT_INFO的指针
    public static boolean logOpen(LOG_SET_PRINT_INFO pstLogPrintInfo) {
    	return netsdkapi.CLIENT_LogOpen(pstLogPrintInfo);
    }

    // 关闭日志功能
    public static boolean logClose() {
    	return netsdkapi.CLIENT_LogClose();
    }
    
    // 获取符合查询条件的文件总数
    // reserved为void *
    public static boolean getTotalFileCount(NativeLong lFindHandle, IntByReference pTotalCount, int waittime) {
    	return netsdkapi.CLIENT_GetTotalFileCount(lFindHandle, pTotalCount, null, waittime);
    }
    
    // 设置查询跳转条件
    // reserved为void *
    public static boolean setFindingJumpOption(NativeLong lFindHandle, 
								    		  NET_FINDING_JUMP_OPTION_INFO pOption, 
								    		  int waittime) {
    	return netsdkapi.CLIENT_SetFindingJumpOption(lFindHandle, pOption, null, waittime);
    }
    
    // 按查询条件查询文件
    // pQueryCondition为void *, 具体类型根据emType的类型确定,对应 EM_FILE_QUERY_TYPE
    // reserved为void *, 具体类型根据emType的类型确定
    public static NativeLong findFileEx(NativeLong lLoginID, int emType, Pointer pQueryCondition, int waittime) {
    	return netsdkapi.CLIENT_FindFileEx(lLoginID, emType, pQueryCondition, null, waittime);
    }
    
    // 查找文件:nFilecount:需要查询的条数, 返回值为媒体文件条数 返回值<nFilecount则相应时间段内的文件查询完毕
    // pMediaFileInfo为void *
    // reserved为void *
    public static int findNextFileEx(NativeLong lFindHandle, int nFilecount, Pointer pMediaFileInfo, int maxlen, int waittime) {
    	return netsdkapi.CLIENT_FindNextFileEx(lFindHandle, nFilecount, pMediaFileInfo, maxlen, null, waittime);
    }
    
    // 结束录像文件查找
    public static boolean findCloseEx(NativeLong lFindHandle) {
    	return netsdkapi.CLIENT_FindCloseEx(lFindHandle);
    }
    
    // 实时上传智能分析数据－图片(扩展接口,bNeedPicFile表示是否订阅图片文件,Reserved类型为RESERVED_PARA) 
    // bNeedPicFile为BOOL类型，取值范围为0或者1, fAnalyzerDataCallBack 回调
    public static NativeLong realLoadPictureEx(NativeLong lLoginID, 
										    		 int nChannelID, 
										    		 int dwAlarmType, 
										    		 int bNeedPicFile, 
										    		 StdCallCallback cbAnalyzerData) {
    	return netsdkapi.CLIENT_RealLoadPictureEx(lLoginID, nChannelID, dwAlarmType, bNeedPicFile, cbAnalyzerData, null, null);
    }
    
    // 停止上传智能分析数据－图片
    public static boolean stopLoadPic(NativeLong lAnalyzerHandle) {
    	return netsdkapi.CLIENT_StopLoadPic(lAnalyzerHandle);
    }
    
    // 设置抓图回调函数, fSnapRev 回调
    public static void setSnapRevCallBack(StdCallCallback OnSnapRevMessage) {
    	netsdkapi.CLIENT_SetSnapRevCallBack(OnSnapRevMessage, null);
    }
    
    // 抓图请求扩展接口
    public static boolean snapPictureEx(NativeLong lLoginID, SNAP_PARAMS stParam) {
    	IntByReference reserved = new IntByReference(0);
    	return netsdkapi.CLIENT_SnapPictureEx(lLoginID, stParam, reserved);
    }
    
    // 异步搜索局域网内IPC、NVS等设备, fSearchDevicesCB 回调
    public static NativeLong startSearchDevices(StdCallCallback cbSearchDevices, String szLocalIp) {
    	return netsdkapi.CLIENT_StartSearchDevices(cbSearchDevices, null, szLocalIp);
    }
    
    // 停止异步搜索局域网内IPC、NVS等设备
    public static boolean stopSearchDevices(NativeLong lSearchHandle) {
    	return netsdkapi.CLIENT_StopSearchDevices(lSearchHandle);
    }
    
    // 同步跨网段搜索设备IP (pIpSearchInfo内存由用户申请释放)
    // szLocalIp为本地IP，可不做输入, fSearchDevicesCB 回调
    public static boolean searchDevicesByIPs(DEVICE_IP_SEARCH_INFO pIpSearchInfo, 
    												StdCallCallback cbSearchDevices, 
										    		String szLocalIp, 
										    		int dwWaitTime) {
    	return netsdkapi.CLIENT_SearchDevicesByIPs(pIpSearchInfo, cbSearchDevices, null, szLocalIp, dwWaitTime);
    }

    // 开始实时监视
    // rType  : NET_RealPlayType    返回监控句柄
    public static NativeLong realPlayEx(NativeLong lLoginID, int nChannelID, Pointer hWnd, int rType) {
    	return netsdkapi.CLIENT_RealPlayEx(lLoginID, nChannelID, hWnd, rType);
    }
    
    // 停止实时预览--扩展     lRealHandle为CLIENT_RealPlayEx的返回值
    public static boolean stopRealPlayEx(NativeLong lRealHandle) {
    	return netsdkapi.CLIENT_StopRealPlayEx(lRealHandle);
    }
    
    // 开始实时监视支持设置码流回调接口     rType  : NET_RealPlayType   返回监控句柄
    // cbRealData 对应 fRealDataCallBackEx 回调
    // cbDisconnect 对应 fRealPlayDisConnect 回调
    public static NativeLong startRealPlay(NativeLong lLoginID, 
									    		  int nChannelID, 
									    		  Pointer hWnd, 
									    		  int rType, 
									    		  StdCallCallback cbRealData, 
									    		  StdCallCallback cbDisconnect, 
									    		  int dwWaitTime) {
    	return netsdkapi.CLIENT_StartRealPlay(lLoginID, nChannelID, hWnd, rType, cbRealData, cbDisconnect, null, dwWaitTime);
    }

    // 停止实时预览
    public static boolean stopRealPlay(NativeLong lRealHandle) {
    	return netsdkapi.CLIENT_StopRealPlay(lRealHandle);
    }
    
    // 设置实时监视数据回调函数扩展接口    lRealHandle监控句柄,fRealDataCallBackEx 回调
    public static boolean setRealDataCallBackEx(NativeLong lRealHandle, StdCallCallback cbRealData, int dwFlag) {
    	return netsdkapi.CLIENT_SetRealDataCallBackEx(lRealHandle, cbRealData, null, dwFlag);
    }
    
    // 设置图象流畅性
    // 将要调整图象的等级(0-6),当level为0时，图象最流畅；当level为6时，图象最实时。Level的默认值为3。注意：直接解码下有效 
    public static boolean adjustFluency(NativeLong lRealHandle, int nLevel) {
    	return netsdkapi.CLIENT_AdjustFluency(lRealHandle, nLevel);
    }   

    // 保存数据为文件,lRealHandle为CLIENT_RealPlayEx的返回值,pchFileName为实时监视保存文件名 
    public static boolean saveRealData(NativeLong lRealHandle, String pchFileName) {
    	return netsdkapi.CLIENT_SaveRealData(lRealHandle, pchFileName);
    }
    
    // 结束保存数据为文件,lRealHandle为CLIENT_RealPlayEx的返回值
    public static boolean stopSaveRealData(NativeLong lRealHandle) {
    	return netsdkapi.CLIENT_StopSaveRealData(lRealHandle);
    }

    // 打开声音
    public static boolean openSound(NativeLong hPlayHandle) {
    	return netsdkapi.CLIENT_OpenSound(hPlayHandle);
    }
    
    // 关闭声音
    public static boolean closeSound() {
    	return netsdkapi.CLIENT_CloseSound();
    }
    
    // 获取所有有效显示源
    // pInParam  对应  NET_IN_MATRIX_GET_CAMERAS
    // pOutParam 对应  NET_OUT_MATRIX_GET_CAMERAS
    public static boolean matrixGetCameras(NativeLong lLoginID, 
									    		  NET_IN_MATRIX_GET_CAMERAS pInParam, 
									    		  NET_OUT_MATRIX_GET_CAMERAS pOutParam, 
									    		  int nWaitTime) {
    	return netsdkapi.CLIENT_MatrixGetCameras(lLoginID, pInParam, pOutParam, nWaitTime);
    }

    // 抓图同步接口,将图片数据直接返回给用户
    public static boolean snapPictureToFile(NativeLong lLoginID,  
									    		  NET_IN_SNAP_PIC_TO_FILE_PARAM  pInParam, 
									    		  NET_OUT_SNAP_PIC_TO_FILE_PARAM pOutParam, 
									    		  int nWaitTime) {
    	return netsdkapi.CLIENT_SnapPictureToFile(lLoginID, pInParam, pOutParam, nWaitTime);
    }
    
    // 查询时间段内的所有录像文件
    // nRecordFileType 录像类型 0:所有录像  1:外部报警  2:动态监测报警  3:所有报警  4:卡号查询   5:组合条件查询   6:录像位置与偏移量长度   8:按卡号查询图片(目前仅HB-U和NVS特殊型号的设备支持)  9:查询图片(目前仅HB-U和NVS特殊型号的设备支持)  10:按字段查询    15:返回网络数据结构(金桥网吧)  16:查询所有透明串数据录像文件
    // nriFileinfo 返回的录像文件信息，是一个 NET_RECORDFILE_INFO 结构数组 
    // maxlen 是 nriFileinfo缓冲的最大长度(单位字节，建议在(100~200)*sizeof(NET_RECORDFILE_INFO)之间) 
    // filecount返回的文件个数，属于输出参数最大只能查到缓冲满为止的录像记录; 
    // bTime 是否按时间查(目前无效) 
    public static boolean queryRecordFile(NativeLong lLoginID, 
							    		 int nChannelId, 
							    		 int nRecordFileType, 
							    		 NET_TIME tmStart, 
							    		 NET_TIME tmEnd, 
							    		 String pchCardid, 
							    		 NET_RECORDFILE_INFO[] stFileInfo, 
							    		 int maxlen, 
							    		 IntByReference filecount, 
							    		 int waittime, 
							    		 boolean bTime) {
    	return netsdkapi.CLIENT_QueryRecordFile(lLoginID, nChannelId, nRecordFileType, tmStart, tmEnd, pchCardid, stFileInfo, maxlen, filecount, waittime, bTime);
    }
    
    // 查询时间段内是否有录像文件   bResult输出参数，true有录像，false没录像
    public static boolean queryRecordTime(NativeLong lLoginID, 
									    		int nChannelId, 
									    		int nRecordFileType, 
									    		NET_TIME tmStart, 
									    		NET_TIME tmEnd, 
									    		String pchCardid, 
									    		IntByReference bResult, 
									    		int waittime) {
    	return netsdkapi.CLIENT_QueryRecordTime(lLoginID, nChannelId, nRecordFileType, tmStart, tmEnd, pchCardid, bResult, waittime);
    }
    
    // 通过时间下载录像--扩展
    // nRecordFileType 对应 EM_QUERY_RECORD_TYPE
    // cbTimeDownLoadPos 对应 fTimeDownLoadPosCallBack 回调
    // fDownLoadDataCallBack 对应 fDataCallBack 回调
    public static NativeLong downloadByTimeEx(NativeLong lLoginID, 
										            int nChannelId, 
										            int nRecordFileType, 
										            NET_TIME tmStart, 
										            NET_TIME tmEnd, 
										            String sSavedFileName, 
										            StdCallCallback cbTimeDownLoadPos, 
										            StdCallCallback fDownLoadDataCallBack, 
										            Pointer pReserved) {
    	return netsdkapi.CLIENT_DownloadByTimeEx(lLoginID, nChannelId, nRecordFileType, tmStart, tmEnd, sSavedFileName, cbTimeDownLoadPos, null, fDownLoadDataCallBack, null, pReserved);
    }
    
    // 停止录像下载
    public static boolean stopDownload(NativeLong lFileHandle) {
    	return netsdkapi.CLIENT_StopDownload(lFileHandle);
    }

    // 云台控制扩展接口,支持三维快速定位,鱼眼
    // dwStop类型为BOOL, 取值0或者1
    // dwPTZCommand取值为NET_EXTPTZ_ControlType中的值或者是NET_PTZ_ControlType中的值
    public static boolean PTZControlEx2(NativeLong lLoginID, 
									    		int nChannelID, 
									    		int dwPTZCommand, 
									    		int lParam1, 
									    		int lParam2, 
									    		int lParam3, 
									    		int dwStop, 
									    		Pointer param4) {
    	return netsdkapi.CLIENT_DHPTZControlEx2(lLoginID, nChannelID, dwPTZCommand, lParam1, lParam2, lParam3, dwStop, param4);
    }
       
    // 设备控制(param内存由用户申请释放)  emType对应 枚举 CtrlType
    public static boolean controlDevice(NativeLong lLoginID, int emType, Pointer param, int waittime) {
    	return netsdkapi.CLIENT_ControlDevice(lLoginID, emType, param, waittime);
    }
    
    // 设备控制扩展接口，兼容 CLIENT_ControlDevice (pInBuf, pOutBuf内存由用户申请释放)
    // emType的取值为CtrlType中的值
    public static boolean controlDeviceEx(NativeLong lLoginID, int emType, Pointer pInBuf, Pointer pOutBuf, int nWaitTime) {
    	return netsdkapi.CLIENT_ControlDeviceEx(lLoginID, emType, pInBuf, pOutBuf, nWaitTime);
    }
    
    // 查询配置信息(lpOutBuffer内存由用户申请释放)
    public static boolean getDevConfig(NativeLong lLoginID, 
								    		 int dwCommand, 
								    		 int lChannel, 
								    		 Pointer lpOutBuffer, 
								    		 int dwOutBufferSize, 
								    		 IntByReference lpBytesReturned,
								    		 int waittime) {
    	return netsdkapi.CLIENT_GetDevConfig(lLoginID, dwCommand, lChannel, lpOutBuffer, dwOutBufferSize, lpBytesReturned, waittime);
    }

    // 设置配置信息(lpInBuffer内存由用户申请释放)
    public static boolean setDevConfig(NativeLong lLoginID, 
								    		 int dwCommand, 
								    		 int lChannel, 
								    		 Pointer lpInBuffer, 
								    		 int dwInBufferSize, 
								    		 int waittime) {
    	return netsdkapi.CLIENT_SetDevConfig(lLoginID, dwCommand, lChannel, lpInBuffer, dwInBufferSize, waittime);
    }
    
    // 查询设备状态(pBuf内存由用户申请释放)
    // pBuf指向char *,输出参数
    // pRetLen指向int *;输出参数，实际返回的数据长度，单位字节
    public static boolean queryDevState(NativeLong lLoginID, 
								    		  int nType, 
								    		  Pointer pBuf, 
								    		  int nBufLen, 
								    		  IntByReference pRetLen, 
								    		  int waittime) {
    	return netsdkapi.CLIENT_QueryDevState(lLoginID, nType, pBuf, nBufLen, pRetLen, waittime);
    }
    
    // 获取设备能力接口
    // pInBuf指向void*，输入参数结构体指针       pOutBuf指向void*，输出参数结构体指针
    public static boolean getDevCaps(NativeLong lLoginID, 
							    		    int nType, 
							    		    Pointer pInBuf, 
							    		    Pointer pOutBuf, 
							    		    int nWaitTime) {
		return netsdkapi.CLIENT_GetDevCaps(lLoginID, nType, pInBuf, pOutBuf, nWaitTime);
	}
    
    // 停止订阅报警
    public static boolean stopListen(NativeLong lLoginID) {
    	return netsdkapi.CLIENT_StopListen(lLoginID);
    }
    
    // 新系统能力查询接口，查询系统能力信息(以Json格式，具体见配置SDK)(szOutBuffer内存由用户申请释放)
    // szCommand: 对应命令查看上文
    // szOutBuffer: 获取到的信息, 通过 CLIENT_ParseData 解析
    // error 指向 int * ： 错误码大于0表示设备返回的，小于0表示缓冲不够或数据校验引起的
    public static boolean queryNewSystemInfo(NativeLong lLoginID, 
								    		String szCommand, 
								    		int nChannelID, 
								    		byte[] szOutBuffer, 
								    		int dwOutBufferSize, 									    	
								    		int waittime) {
    	IntByReference error = new IntByReference(0);
    	return netsdkapi.CLIENT_QueryNewSystemInfo(lLoginID, szCommand, nChannelID, szOutBuffer, dwOutBufferSize, error, waittime);
    }
    
    // 订阅视频统计摘要信息
    public static NativeLong attachVideoStatSummary(NativeLong lLoginID, 
									    		  NET_IN_ATTACH_VIDEOSTAT_SUM  pInParam, 
									    		  NET_OUT_ATTACH_VIDEOSTAT_SUM pOutParam, 
									    		  int nWaitTime) {
    	return netsdkapi.CLIENT_AttachVideoStatSummary(lLoginID, pInParam, pOutParam, nWaitTime);
    }

    // 取消订阅视频统计摘要信息，lAttachHandle为CLIENT_AttachVideoStatSummary的返回值
    public static boolean detachVideoStatSummary(NativeLong lAttachHandle) {
    	return netsdkapi.CLIENT_DetachVideoStatSummary(lAttachHandle);
    }
    
    // 开始查询视频统计信息/获取人数统计信息
    public static NativeLong startFindNumberStat(NativeLong lLoginID, 
									    		NET_IN_FINDNUMBERSTAT  pstInParam, 
									    		NET_OUT_FINDNUMBERSTAT pstOutParam) {
    	return netsdkapi.CLIENT_StartFindNumberStat(lLoginID, pstInParam, pstOutParam);
    }

    // 继续查询视频统计/继续查询人数统计
    public static int doFindNumberStat(NativeLong lFindHandle, 
						    		  NET_IN_DOFINDNUMBERSTAT  pstInParam, 
						    		  NET_OUT_DOFINDNUMBERSTAT pstOutParam) {
    	return netsdkapi.CLIENT_DoFindNumberStat(lFindHandle, pstInParam, pstOutParam);
    }

    // 结束查询视频统计/结束查询人数统计
    public static boolean stopFindNumberStat(NativeLong lFindHandle) {
    	return netsdkapi.CLIENT_StopFindNumberStat(lFindHandle);
    }
   
    // 设置语音对讲模式,客户端方式还是服务器方式
    // emType : 方式类型 参照 EM_USEDEV_MODE
    public static boolean setDeviceMode(NativeLong lLoginID, int emType, Pointer pValue) {
    	return netsdkapi.CLIENT_SetDeviceMode(lLoginID, emType, pValue);
    }
    
    ///////////////// 录像回放相关接口 ///////////////////////
    // 按时间方式回放--扩展接口 
    // cbDownLoadPos 对应 fDownLoadPosCallBack 回调
    // fDownLoadDataCallBack 对应 fDataCallBack 回调
    public static NativeLong playBackByTimeEx(NativeLong lLoginID, 
								    		int nChannelID, 
								    		NET_TIME lpStartTime, 
								    		NET_TIME lpStopTime, 
								    		Pointer hWnd, 
								    		StdCallCallback cbDownLoadPos,  
								    		StdCallCallback fDownLoadDataCallBack) {
		return netsdkapi.CLIENT_PlayBackByTimeEx(lLoginID, nChannelID, lpStartTime, lpStopTime, hWnd, cbDownLoadPos, null, fDownLoadDataCallBack, null);
	}
    // 停止录像回放接口
    public static boolean stopPlayBack(NativeLong lPlayHandle) {
    	return netsdkapi.CLIENT_StopPlayBack(lPlayHandle);
    }
    
    // 获取回放OSD时间
    public static boolean getPlayBackOsdTime(NativeLong lPlayHandle, 
								    		NET_TIME lpOsdTime, 
								    		NET_TIME lpStartTime, 
								    		NET_TIME lpEndTime) {
    	return netsdkapi.CLIENT_GetPlayBackOsdTime(lPlayHandle, lpOsdTime, lpStartTime, lpEndTime);
    }

    // 暂停或恢复录像回放
    // bPause: 1 - 暂停	0 - 恢复 
    public static boolean pausePlayBack(NativeLong lPlayHandle, int bPause) {
    	return netsdkapi.CLIENT_PausePlayBack(lPlayHandle, bPause);
    }
    
    // 快进录像回放
    public static boolean CLIENT_FastPlayBack(NativeLong lPlayHandle) {
    	return netsdkapi.CLIENT_FastPlayBack(lPlayHandle);
    }

    // 慢进录像回放
    public static boolean slowPlayBack(NativeLong lPlayHandle) {
    	return netsdkapi.CLIENT_SlowPlayBack(lPlayHandle);
    }
 
    // 恢复正常回放速度
    public static boolean normalPlayBack(NativeLong lPlayHandle) {
    	return netsdkapi.CLIENT_NormalPlayBack(lPlayHandle);
    }
    
    // 查询设备当前时间
    public static boolean queryDeviceTime(NativeLong lLoginID, NET_TIME pDeviceTime, int waittime) {
    	return netsdkapi.CLIENT_QueryDeviceTime(lLoginID, pDeviceTime, waittime);
    }
    
    // 设置设备当前时间
    public static boolean setupDeviceTime(NativeLong lLoginID, NET_TIME pDeviceTime) {
    	return netsdkapi.CLIENT_SetupDeviceTime(lLoginID, pDeviceTime);
    }
    
    // 获得亮度、色度、对比度、饱和度的参数      
    // param1/param2/param3/param4 四个参数范围0~255
  	public static boolean clientGetVideoEffect(NativeLong lPlayHandle, 
								  			 byte[] param1, 
								  			 byte[] param2, 
								  			 byte[] param3, 
								  			 byte[] param4) {
  		return netsdkapi.CLIENT_ClientGetVideoEffect(lPlayHandle, param1, param2, param3, param4);
  	}

  	// 设置亮度、色度、对比度、饱和度的参数    
  	// nBrightness/nContrast/nHue/nSaturation四个参数为 unsigned byte 范围0~255
  	public static boolean clientSetVideoEffect(NativeLong lPlayHandle, 
								  			  byte nBrightness, 
								  			  byte nContrast, 
								  			  byte nHue, 
								  			  byte nSaturation) {
  		return netsdkapi.CLIENT_ClientSetVideoEffect(lPlayHandle, nBrightness, nContrast, nHue, nSaturation);
  	}

	//------------------------用户管理-----------------------
	// 查询用户信息--最大支持64通道设备  
	// pReserved指向void*  
	public static boolean queryUserInfoNew(NativeLong lLoginID, 
										 USER_MANAGE_INFO_NEW info,
										 int nWaittime) {
		return netsdkapi.CLIENT_QueryUserInfoNew(lLoginID, info, null, nWaittime);
	}
	
	// 设置用户信息接口--操作设备用户--最大支持64通道设备
	// opParam指向void*           subParam指向void*   
	// pReserved指向void*       
	// opParam（设置用户信息的输入缓冲）和subParam（设置用户信息的辅助输入缓冲）对应结构体类型USER_GROUP_INFO_NEW或USER_INFO_NEW
	public static boolean operateUserInfoNew(NativeLong lLoginID, 
											int nOperateType, 
											Pointer opParam, 
											Pointer subParam, 
											int nWaittime) {
		return netsdkapi.CLIENT_OperateUserInfoNew(lLoginID, nOperateType, opParam, subParam, null, nWaittime);
	}
	
	//----------------------语音对讲--------------------------
	// 向设备发起语音对讲请求          pfcb是用户自定义的数据回调接口, pfAudioDataCallBack 回调
	public static NativeLong startTalkEx(NativeLong lLoginID, StdCallCallback pfcb) {
		return netsdkapi.CLIENT_StartTalkEx(lLoginID, pfcb, null);
	}
	
	// 停止语音对讲        lTalkHandle语音对讲句柄，是CLIENT_StartTalkEx的返回 值
    public static boolean stopTalkEx(NativeLong lTalkHandle) {
    	return netsdkapi.CLIENT_StopTalkEx(lTalkHandle);
    }

    // 启动本地录音功能(只在Windows平台下有效)，录音采集出来的音频数据通过CLIENT_StartTalkEx的回调函数回调给用户，对应操作是CLIENT_RecordStopEx
    // lLoginID是CLIENT_Login的返回值 
    public static boolean recordStartEx(NativeLong lLoginID) {
    	return netsdkapi.CLIENT_RecordStartEx(lLoginID);
    }

    // 停止本地录音(只在Windows平台下有效)，对应操作是CLIENT_RecordStartEx。
    public static boolean recordStopEx(NativeLong lLoginID) {
    	return netsdkapi.CLIENT_RecordStopEx(lLoginID);
    }
    
    // 向设备发送用户的音频数据，这里的数据可以是从CLIENT_StartTalkEx的回调接口中回调出来的数据
    public static NativeLong talkSendData(NativeLong lTalkHandle, Pointer pSendBuf, int dwBufSize) {
    	return netsdkapi.CLIENT_TalkSendData(lTalkHandle, pSendBuf, dwBufSize);
    }
    
    // 解码音频数据扩展接口(只在Windows平台下有效)    pAudioDataBuf是要求解码的音频数据内容 
    public static void audioDec(Pointer pAudioDataBuf, int dwBufSize) {
    	netsdkapi.CLIENT_AudioDec(pAudioDataBuf, dwBufSize);
    }
    
    public static boolean audioDecEx(NativeLong lTalkHandle, Pointer pAudioDataBuf, int dwBufSize) {
    	return netsdkapi.CLIENT_AudioDecEx(lTalkHandle, pAudioDataBuf, dwBufSize);
    }
    
    //-------------------白名单-------------------------
    // 按查询条件查询记录          pInParam查询记录参数        pOutParam返回查询句柄  
    // 可以先调用本接口获得查询句柄，再调用  CLIENT_FindNextRecord函数获取记录列表，查询完毕可以调用CLIENT_FindRecordClose关闭查询句柄。 
    public static boolean FindRecord(NativeLong lLoginID, 
						    		NET_IN_FIND_RECORD_PARAM pInParam, 
						    		NET_OUT_FIND_RECORD_PARAM pOutParam, 
						    		int waittime) {
    	return netsdkapi.CLIENT_FindRecord(lLoginID, pInParam, pOutParam, waittime);
    }
    
    // 查找记录:nFilecount:需要查询的条数, 返回值为媒体文件条数 返回值小于nFilecount则相应时间段内的文件查询完毕
    public static boolean FindNextRecord(NET_IN_FIND_NEXT_RECORD_PARAM pInParam, 
							    		NET_OUT_FIND_NEXT_RECORD_PARAM pOutParam, 
							    		int waittime) {
    	return netsdkapi.CLIENT_FindNextRecord(pInParam, pOutParam, waittime);
    }
    
    // 结束记录查找,lFindHandle是CLIENT_FindRecord的返回值 
    public static boolean FindRecordClose(NativeLong lFindHandle) {
    	return netsdkapi.CLIENT_FindRecordClose(lFindHandle);
    }
    
    // 查找记录条数,pInParam与pOutParam内存由用户申请释放
    public static boolean QueryRecordCount(NET_IN_QUEYT_RECORD_COUNT_PARAM pInParam, 
							    		 NET_OUT_QUEYT_RECORD_COUNT_PARAM pOutParam, 
							    		 int waittime) {
    	return netsdkapi.CLIENT_QueryRecordCount(pInParam, pOutParam, waittime);
    }
    
    // 黑白名单操作 ,pstOutParam = null;
    public static boolean OperateTrafficList(NativeLong lLoginID,  
								    		NET_IN_OPERATE_TRAFFIC_LIST_RECORD  pstInParam, 
								    		NET_OUT_OPERATE_TRAFFIC_LIST_RECORD pstOutParam , 
								    		int waittime) {
    	return netsdkapi.CLIENT_OperateTrafficList(lLoginID, pstInParam, pstOutParam, waittime);
    }
    
    // 文件上传控制接口，白名单上传需要三个步骤配合使用，CLIENT_FileTransmit的 NET_DEV_BLACKWHITETRANS_START、  NET_DEV_BLACKWHITETRANS_SEND、   NET_DEV_BLACKWHITETRANS_STOP，如下所示
    // fTransFileCallBack 回调
    public static NativeLong FileTransmit(NativeLong lLoginID, 
							    		int nTransType, 
							    		Pointer szInBuf, 
							    		int nInBufLen, 
							    		StdCallCallback cbTransFile, 
							    		int waittime)  {
    	return netsdkapi.CLIENT_FileTransmit(lLoginID, nTransType, szInBuf, nInBufLen, cbTransFile, null, waittime);
    }

  	// 查询设备信息
  	public static boolean QueryDevInfo(NativeLong lLoginID, 
						  			 int nQueryType, 
						  			 Pointer pInBuf, 
						  		 	 Pointer pOutBuf,
						  			 int nWaitTime) {
  		return netsdkapi.CLIENT_QueryDevInfo(lLoginID, nQueryType, pInBuf, pOutBuf, null, nWaitTime);
  	}
  	 	
  	// 设置GPS订阅回调函数--扩展, fGPSRevEx 回调
  	public static void SetSubcribeGPSCallBackEX(StdCallCallback OnGPSMessage) {
  		netsdkapi.CLIENT_SetSubcribeGPSCallBackEX(OnGPSMessage, null);
  	}
  	
  	// GPS信息订阅       
  	// bStart:表明是订阅还是取消          InterTime:订阅时间内GPS发送频率(单位秒)
  	// KeepTime:订阅持续时间(单位秒) 值为-1时,订阅时间为极大值,可视为永久订阅     
  	public static boolean SubcribeGPS (NativeLong lLoginID, int bStart, int KeepTime, int InterTime) {
  		return netsdkapi.CLIENT_SubcribeGPS(lLoginID, bStart, KeepTime, InterTime);
  	}
	
    // 同步文件上传, 只适用于小文件
  	public static boolean UploadRemoteFile(NativeLong lLoginID, 
							  			  NET_IN_UPLOAD_REMOTE_FILE  pInParam, 
							  			  NET_OUT_UPLOAD_REMOTE_FILE pOutParam, 
							  			  int nWaitTime) {
  		return netsdkapi.CLIENT_UploadRemoteFile(lLoginID, pInParam, pOutParam, nWaitTime);
  	}

    // 过车记录订阅
  	public static NativeLong ParkingControlAttachRecord(NativeLong lLoginID, 
											  			NET_IN_PARKING_CONTROL_PARAM  pInParam, 
											  			NET_OUT_PARKING_CONTROL_PARAM pOutParam, 
											  			int nWaitTime) {
  		return netsdkapi.CLIENT_ParkingControlAttachRecord(lLoginID, pInParam, pOutParam, nWaitTime);
  	}
  	
  	// 取消过车记录订阅
  	public static boolean ParkingControlDetachRecord(NativeLong lAttachHandle) {
  		return netsdkapi.CLIENT_ParkingControlDetachRecord(lAttachHandle);
  	}
 
    // 开始过车记录查询
  	public static NativeLong ParkingControlStartFind(NativeLong lLoginID, 
										  			NET_IN_PARKING_CONTROL_START_FIND_PARAM  pInParam, 
										  			NET_OUT_PARKING_CONTROL_START_FIND_PARAM pOutParam, 
										  			int waittime) {
  		return netsdkapi.CLIENT_ParkingControlStartFind(lLoginID, pInParam, pOutParam, waittime);
  	}

  	// 获取过车记录
  	public static boolean ParkingControlDoFind(NativeLong lFindeHandle, 
								  			 NET_IN_PARKING_CONTROL_DO_FIND_PARAM pInParam, 
								  			 NET_OUT_PARKING_CONTROL_DO_FIND_PARAM pOutParam, 
								  			 int waittime) {
  		return netsdkapi.CLIENT_ParkingControlDoFind(lFindeHandle, pInParam, pOutParam, waittime);
  	}

  	// 结束过车记录查询
  	public static boolean ParkingControlStopFind(NativeLong lFindHandle) {
  		return netsdkapi.CLIENT_ParkingControlStopFind(lFindHandle);
  	}
  	
  	// 车位状态订阅,pInParam与pOutParam内存由用户申请释放
  	public static NativeLong ParkingControlAttachParkInfo(NativeLong lLoginID, 
											  			NET_IN_PARK_INFO_PARAM  pInParam, 
											  			NET_OUT_PARK_INFO_PARAM pOutParam, 
											  			int nWaitTime) {
  		return netsdkapi.CLIENT_ParkingControlAttachParkInfo(lLoginID, pInParam, pOutParam, nWaitTime);
  	}

  	// 取消车位状态订阅
  	public static boolean ParkingControlDetachParkInfo(NativeLong lAttachHandle) {
  		return netsdkapi.CLIENT_ParkingControlDetachParkInfo(lAttachHandle);
  	}
  	
  	// 电源控制,pInParam与pOutParam内存由用户申请释放
  	public static boolean PowerControl(NativeLong lLoginID, 
						  			 NET_IN_WM_POWER_CTRL  pInParam, 
						  			 NET_OUT_WM_POWER_CTRL pOutParam, 
						  			 int nWaitTime) {
  		return netsdkapi.CLIENT_PowerControl(lLoginID, pInParam, pOutParam, nWaitTime);
  	}

  	// 载入预案,pInParam与pOutParam内存由用户申请释放
  	public static boolean LoadMonitorWallCollection(NativeLong lLoginID, 
									  			  NET_IN_WM_LOAD_COLLECTION  pInParam, 
									  			  NET_OUT_WM_LOAD_COLLECTION pOutParam, 
									  			  int nWaitTime) {
  		return netsdkapi.CLIENT_LoadMonitorWallCollection(lLoginID, pInParam, pOutParam, nWaitTime);
  	}
  	
  	// 保存预案,pInParam与pOutParam内存由用户申请释放
  	public static boolean SaveMonitorWallCollection(NativeLong lLoginID, 
										  		  NET_IN_WM_SAVE_COLLECTION  pInParam, 
										  		  NET_OUT_WM_SAVE_COLLECTION pOutParam, 
										  		  int nWaitTime) {
  		return netsdkapi.CLIENT_SaveMonitorWallCollection(lLoginID, pInParam, pOutParam, nWaitTime);
  	}
  	
  	// 获取电视墙预案,pInParam与pOutParam内存由用户申请释放
  	public static boolean GetMonitorWallCollections(NativeLong lLoginID, 
									  			  NET_IN_WM_GET_COLLECTIONS  pInParam, 
									  			  NET_OUT_WM_GET_COLLECTIONS pOutParam, 
									  			  int nWaitTime) {
  		return netsdkapi.CLIENT_GetMonitorWallCollections(lLoginID, pInParam, pOutParam, nWaitTime);
  	}

  	// 查询显示源(pstuSplitSrc内存由用户申请释放),  nWindow为-1表示所有窗口 ; pstuSplitSrc 对应 NET_SPLIT_SOURCE 指针
  	public static boolean GetSplitSource(NativeLong lLoginID, 
							  			int nChannel, 
							  			int nWindow, 
							  			NET_SPLIT_SOURCE[] pstuSplitSrc, 
							  			int nMaxCount, 
							  			IntByReference pnRetCount, 
							  			int nWaitTime) {
  		return netsdkapi.CLIENT_GetSplitSource(lLoginID, nChannel, nWindow, pstuSplitSrc, nMaxCount, pnRetCount, nWaitTime);
  	}
  	
  	// 设置显示源(pstuSplitSrc内存由用户申请释放),  nWindow为-1表示所有窗口 ; pstuSplitSrc 对应 NET_SPLIT_SOURCE 指针
  	public static boolean SetSplitSource(NativeLong lLoginID, 
							  			int nChannel, 
							  			int nWindow, 
							  			NET_SPLIT_SOURCE pstuSplitSrc, 
							  			int nSrcCount, 
							  			int nWaitTime) {
  		return netsdkapi.CLIENT_SetSplitSource(lLoginID, nChannel, nWindow, pstuSplitSrc, nSrcCount, nWaitTime);
  	}

  	// 设置显示源, 支持同时设置多个窗口(pInparam, pOutParam内存由用户申请释放)
  	public static boolean SplitSetMultiSource(NativeLong lLoginID, 
								  			NET_IN_SPLIT_SET_MULTI_SOURCE  pInParam, 
								  			NET_OUT_SPLIT_SET_MULTI_SOURCE pOutParam, 
								  			int nWaitTime) {
  		return netsdkapi.CLIENT_SplitSetMultiSource(lLoginID, pInParam, pOutParam, nWaitTime);
  	}
  	
  	// 查询矩阵子卡信息(pstuCardList内存由用户申请释放)
  	public static boolean QueryMatrixCardInfo(NativeLong lLoginID, NET_MATRIX_CARD_LIST pstuCardList, int nWaitTime) {
  		return netsdkapi.CLIENT_QueryMatrixCardInfo(lLoginID, pstuCardList, nWaitTime);
  	}
  	
    // 开始查找录像文件帧信息(pInParam, pOutParam内存由用户申请释放)
  	public static boolean FindFrameInfo(NativeLong lLoginID, 
						  			  NET_IN_FIND_FRAMEINFO_PRAM  pInParam, 
						  			  NET_OUT_FIND_FRAMEINFO_PRAM pOutParam, 
						  			  int nWaitTime) {
  		return netsdkapi.CLIENT_FindFrameInfo(lLoginID, pInParam, pOutParam, nWaitTime);
  	}

  	// 获取标签信息
  	public static boolean FileStreamGetTags(NativeLong lFindHandle, 
							  			  NET_IN_FILE_STREAM_GET_TAGS_INFO  pInParam, 
							  			  NET_OUT_FILE_STREAM_GET_TAGS_INFO pOutParam, 
							  			  int nWaitTime) {
  		return netsdkapi.CLIENT_FileStreamGetTags(lFindHandle, pInParam, pOutParam, nWaitTime);
  	}

  	// 设置标签信息
  	public static boolean FileStreamSetTags(NativeLong lFindHandle, 
							  			  NET_IN_FILE_STREAM_TAGS_INFO  pInParam, 
							  			  NET_OUT_FILE_STREAM_TAGS_INFO pOutParam, 
							  			  int nWaitTime) {
  		return netsdkapi.CLIENT_FileStreamSetTags(lFindHandle, pInParam, pOutParam, nWaitTime);
  	}

    // 查询分割模式(pstuSplitInfo内存由用户申请释放)
  	public static boolean GetSplitMode(NativeLong lLoginID, int nChannel, NET_SPLIT_MODE_INFO pstuSplitInfo, int nWaitTime) {
  		return netsdkapi.CLIENT_GetSplitMode(lLoginID, nChannel, pstuSplitInfo, nWaitTime);
  	}
  	
  	// 设置分割模式(pstuSplitInfo内存由用户申请释放)
  	public static boolean SetSplitMode(NativeLong lLoginID, int nChannel, NET_SPLIT_MODE_INFO pstuSplitInfo, int nWaitTime) {
  		return netsdkapi.CLIENT_SetSplitMode(lLoginID, nChannel, pstuSplitInfo, nWaitTime);
  	}

  	// 查询分割能力(pstuCaps内存由用户申请释放)
  	public static boolean GetSplitCaps(NativeLong lLoginID, int nChannel, NET_SPLIT_CAPS pstuCaps, int nWaitTime) {
  		return netsdkapi.CLIENT_GetSplitCaps(lLoginID, nChannel, pstuCaps, nWaitTime);
  	}

	// 下位矩阵切换(pInparam, pOutParam内存由用户申请释放)
	public static boolean MatrixSwitch(NativeLong lLoginID, 
									 NET_IN_MATRIX_SWITCH  pInParam, 
									 NET_OUT_MATRIX_SWITCH pOutParam, 
									 int nWaitTime) {
		return netsdkapi.CLIENT_MatrixSwitch(lLoginID, pInParam, pOutParam, nWaitTime);
	}

	// 打开刻录会话, 返回刻录会话句柄,pstInParam与pstOutParam内存由用户申请释放
	public static NativeLong StartBurnSession(NativeLong lLoginID, 
											NET_IN_START_BURN_SESSION pstInParam, 
											NET_OUT_START_BURN_SESSION pstOutParam, 
											int nWaitTime) {
		return netsdkapi.CLIENT_StartBurnSession(lLoginID, pstInParam, pstOutParam, nWaitTime);
	}

	// 关闭刻录会话
	public static boolean StopBurnSession(NativeLong lBurnSession) {
		return netsdkapi.CLIENT_StopBurnSession(lBurnSession);
	}
	
	//------------有盘/无盘刻录----lBurnSession 是 CLIENT_StartBurnSession返回的句柄//	
	// 开始刻录,pstInParam与pstOutParam内存由用户申请释放
	public static boolean StartBurn(NativeLong lBurnSession, 
								   NET_IN_START_BURN  pstInParam, 
								   NET_OUT_START_BURN pstOutParam, 
								   int nWaitTime) {
		return netsdkapi.CLIENT_StartBurn(lBurnSession, pstInParam, pstOutParam, nWaitTime);
	}
	
	// 停止刻录
	public static boolean StopBurn(NativeLong lBurnSession) {
		return netsdkapi.CLIENT_StopBurn(lBurnSession);
	}
	
  	// 下载指定的智能分析数据 - 图片, fDownLoadPosCallBack 回调
  	// emType 参考 EM_FILE_QUERY_TYPE
  	public static NativeLong DownloadMediaFile(NativeLong lLoginID, 
								  			 int emType, 
								  			 Pointer lpMediaFileInfo, 
								  			 String sSavedFileName, 
								  			StdCallCallback cbDownLoadPos ) {
		return netsdkapi.CLIENT_DownloadMediaFile(lLoginID, emType, lpMediaFileInfo, sSavedFileName, cbDownLoadPos, null, null);
	}

  	// 停止下载数据
  	public static boolean StopDownloadMediaFile(NativeLong lFileHandle) {
  		return netsdkapi.CLIENT_StopDownloadMediaFile(lFileHandle);
  	}

  	// 下发通知到设备 接口, 以emNotifyType来区分下发的通知类型, pInParam 和 pOutParam 都由用户来分配和释放, emNotifyType对应结构体 NET_EM_NOTIFY_TYPE
  	public static boolean SendNotifyToDev(NativeLong lLoginID, 
								  			int emNotifyType, 
								  			Pointer pInParam, 
								  			Pointer pOutParam, 
								  			int nWaitTime) {
  		return netsdkapi.CLIENT_SendNotifyToDev(lLoginID, emNotifyType, pInParam, pOutParam, nWaitTime);
  	}

  	// 查询IO状态(pState内存由用户申请释放,根据emType对应的类型找到相应的结构体，进而确定申请内存大小), emType 参考 NET_IOTYPE
  	public static boolean QueryIOControlState(NativeLong lLoginID, 
  											    int emType, 
  	                                            Pointer pState, 
  	                                            int maxlen, 
  	                                            IntByReference nIOCount, 
  	                                            int waittime) {
  		return netsdkapi.CLIENT_QueryIOControlState(lLoginID, emType, pState, maxlen, nIOCount, waittime);
  	}
  	
  	// IO控制(pState内存由用户申请释放,根据emType对应的类型找到相应的结构体，进而确定申请内存大小),emType 参考 NET_IOTYPE
  	public static boolean IOControl(NativeLong lLoginID, int emType, Pointer pState, int maxlen) {
  		return netsdkapi.CLIENT_IOControl(lLoginID, emType, pState, maxlen);
  	}
  	
  	// 订阅监测点位信息,pInParam与pOutParam内存由用户申请释放
  	public static NativeLong SCADAAttachInfo(NativeLong lLoginID, 
								  			NET_IN_SCADA_ATTACH_INFO pInParam, 
								  			NET_OUT_SCADA_ATTACH_INFO pOutParam, 
								  			int nWaitTime) {
  		return netsdkapi.CLIENT_SCADAAttachInfo(lLoginID, pInParam, pOutParam, nWaitTime);
  	}

  	// 取消监测点位信息订阅
  	public static boolean SCADADetachInfo(NativeLong lAttachHandle) {
  		return netsdkapi.CLIENT_SCADADetachInfo(lAttachHandle);
  	}
  	
  	// 创建透明串口通道,TransComType高2个字节表示串口序号,低2个字节表示串口类型,目前类型支持 0：串口(232), 1:485
  	// baudrate 串口的波特率，1~8分别表示1200，2400，4800，9600，19200，38400，57600，115200 
  	// databits 串口的数据位 4~8表示4位~8位 
  	// stopbits 串口的停止位   232串口 ： 数值0 代表停止位1; 数值1 代表停止位1.5; 数值2 代表停止位2.    485串口 ： 数值1 代表停止位1; 数值2 代表停止位2.
  	// parity 串口的检验位，0：无校验，1：奇校验；2：偶校验; 
  	// cbTransCom 串口数据回调，回调出前端设备发过来的信息
  	// fTransComCallBack 回调
  	public static NativeLong CreateTransComChannel(NativeLong lLoginID, 
									  			 int TransComType, 
									  			 int baudrate, 
									  			 int databits, 
									  			 int stopbits, 
									  			 int parity, 
									  			 StdCallCallback cbTransCom) {
  		return netsdkapi.CLIENT_CreateTransComChannel(lLoginID, TransComType, baudrate, databits, stopbits, parity, cbTransCom, null);
  	}

  	// 透明串口发送数据(pBuffer内存由用户申请释放)
  	public static boolean SendTransComData(NativeLong lTransComChannel, byte[] pBuffer, int dwBufSize) {
  		return netsdkapi.CLIENT_SendTransComData(lTransComChannel, pBuffer, dwBufSize);
  	}

  	// 释放通明串口通道
  	public static boolean DestroyTransComChannel(NativeLong lTransComChannel) {
  		return netsdkapi.CLIENT_DestroyTransComChannel(lTransComChannel);
  	}

  	// 查询透明串口状态(pCommState内存由用户申请释放), TransComType 低2个字节表示串口类型， 0:串口(232)， 1:485口；高2个字节表示串口通道号，从0开始 
  	public static boolean QueryTransComParams(NativeLong lLoginID, int TransComType, NET_COMM_STATE pCommState, int nWaitTime) {
  		return netsdkapi.CLIENT_QueryTransComParams(lLoginID, TransComType, pCommState, nWaitTime);
  	}

  	// 订阅智能分析进度（适用于视频分析源为录像文件时）,pstInParam与pstOutParam内存由用户申请释放
  	public static boolean AttachVideoAnalyseState(NativeLong lLoginID, 
									  			NET_IN_ATTACH_VIDEOANALYSE_STATE  pstInParam, 
									  			NET_OUT_ATTACH_VIDEOANALYSE_STATE pstOutParam, 
									  			int nWaittime) {
  		return netsdkapi.CLIENT_AttachVideoAnalyseState(lLoginID, pstInParam, pstOutParam, nWaittime);
  	}

  	// 停止订阅
  	public static boolean DetachVideoAnalyseState(NativeLong lAttachHandle) {
  		return netsdkapi.CLIENT_DetachVideoAnalyseState(lAttachHandle);
  	}
  	
  	// 抓图, hPlayHandle为监视或回放句柄
  	public static boolean CapturePicture(NativeLong hPlayHandle, String pchPicFileName) {
  		return netsdkapi.CLIENT_CapturePicture(hPlayHandle, pchPicFileName);
  	}
  	
  	/***************************************************************************************************
  	 *     CLIENT_GetNewDevConfig CLIENT_ParseData 和 CLIENT_PacketData  CLIENT_SetNewDevConfig 封装        *
  	 ***************************************************************************************************/
  	/**
	 * 获取多个配置
	 * @param hLoginHandle 登陆句柄
	 * @param nChn 通道号，-1 表示全通道
	 * @param strCmd 配置名称
	 * @param cmdObjects 配置对应的结构体对象
	 * @return 成功返回实际获取到的配置个数
	 */
	public static int GetDevConfig(NativeLong hLoginHandle, int nChn, String strCmd, Structure[] cmdObjects)
	{
		IntByReference error = new IntByReference(0);
		int nBufferLen = 2*1024*1024;
	    byte[] strBuffer = new byte[nBufferLen];
	    
	    if(!netsdkapi.CLIENT_GetNewDevConfig(hLoginHandle, strCmd , nChn, strBuffer, nBufferLen, error, 3000))
	    {
	    	System.err.printf("Get %s Config Failed!Last Error = %x\n" , strCmd , netsdkapi.CLIENT_GetLastError());
	    	return -1;
	    }
	    
	    IntByReference retLength = new IntByReference(0);
	    int memorySize = cmdObjects.length * cmdObjects[0].size();
	    Pointer objectsPointer = new Memory(memorySize);
	    objectsPointer.clear(memorySize);
	    
	    SetStructArrToPointerData(cmdObjects, objectsPointer);
	    
		if (!configapi.CLIENT_ParseData(strCmd, strBuffer, objectsPointer, memorySize, retLength.getPointer())) {		     		
     		System.err.println("Parse " + strCmd + " Config Failed!");
     		return -1;
		}
		
		GetPointerDataToStructArr(objectsPointer, cmdObjects);
		
		return (retLength.getValue() / cmdObjects[0].size());
	}

	/**
	 * 获取单个配置
	 * @param hLoginHandle 登陆句柄
	 * @param nChn 通道号，-1 表示全通道
	 * @param strCmd 配置名称
	 * @param cmdObject 配置对应的结构体对象
	 * @return 成功返回 true 
	 */
	public static boolean GetDevConfig(NativeLong hLoginHandle, int nChn, String strCmd, Structure cmdObject) {
		boolean result = false;
		IntByReference error = new IntByReference(0);
		int nBufferLen = 2*1024*1024;
	    byte[] strBuffer = new byte[nBufferLen];
	   
	    if(netsdkapi.CLIENT_GetNewDevConfig( hLoginHandle, strCmd , nChn, strBuffer, nBufferLen,error,3000) )
	    {  
	    	cmdObject.write();
			if (configapi.CLIENT_ParseData(strCmd, strBuffer, cmdObject.getPointer(),
					cmdObject.size(), null))
	     	{
				cmdObject.read();
	     		result = true;
	     	}
	     	else
	     	{
	     		System.err.println("Parse " + strCmd + " Config Failed!");
	     		result = false;
		 	}
	    }
		else
		{
			 System.err.printf("Get %s Config Failed!Last Error = %x\n" , strCmd , netsdkapi.CLIENT_GetLastError());
			 result = false;
		}
			
	    return result;
	}
	
	/**
	 * 设置多个配置
	 * @param hLoginHandle 登陆句柄
	 * @param nChn 通道号，-1 表示全通道
	 * @param strCmd 配置名称
	 * @param cmdObjects 配置对应的结构体对象
	 * @return 成功返回 true
	 */
	public static boolean SetDevConfigArr(NativeLong hLoginHandle, int nChn, String strCmd, Structure[] cmdObjects) {
        boolean result = false;
    	int nBufferLen = 2*1024*1024;
        byte szBuffer[] = new byte[nBufferLen];
        for(int i=0; i<nBufferLen; i++)szBuffer[i]=0;
    	IntByReference error = new IntByReference(0);
    	IntByReference restart = new IntByReference(0); 

	    int memorySize = cmdObjects.length * cmdObjects[0].size();
	    Pointer objectsPointer = new Memory(memorySize);
	    objectsPointer.clear(memorySize);
        
	    SetStructArrToPointerData(cmdObjects, objectsPointer);
        
		if (configapi.CLIENT_PacketData(strCmd, objectsPointer, memorySize, szBuffer, nBufferLen))
        {
        	if( netsdkapi.CLIENT_SetNewDevConfig(hLoginHandle, strCmd , nChn , szBuffer, nBufferLen, error, restart, 3000))
        	{
        		result = true;
        	}
        	else
        	{
        		 System.err.printf("Set %s Config Failed! Last Error = %x\n" , strCmd , netsdkapi.CLIENT_GetLastError());
	        	 result = false;
        	}
        }
        else
        {
        	System.err.println("Packet " + strCmd + " Config Failed!");
         	result = false;
        }
        
        return result;
    }
	
	/**
	 * 设置单个配置
	 * @param hLoginHandle 登陆句柄
	 * @param nChn 通道号，-1 表示全通道
	 * @param strCmd 配置名称
	 * @param cmdObject 配置对应的结构体对象
	 * @return 成功返回 true
	 */
	public static boolean SetDevConfig(NativeLong hLoginHandle, int nChn, String strCmd, Structure cmdObject) {
        boolean result = false;
    	int nBufferLen = 2*1024*1024;
        byte szBuffer[] = new byte[nBufferLen];
        for(int i=0; i<nBufferLen; i++)szBuffer[i]=0;
    	IntByReference error = new IntByReference(0);
    	IntByReference restart = new IntByReference(0); 

		cmdObject.write();
		if (configapi.CLIENT_PacketData(strCmd, cmdObject.getPointer(), cmdObject.size(),
				szBuffer, nBufferLen))
        {	
			cmdObject.read();
        	if( netsdkapi.CLIENT_SetNewDevConfig(hLoginHandle, strCmd , nChn , szBuffer, nBufferLen, error, restart, 3000))
        	{
        		result = true;
        	}
        	else
        	{
        		 System.err.printf("Set %s Config Failed! Last Error = %x\n" , strCmd , netsdkapi.CLIENT_GetLastError());
	        	 result = false;
        	}
        }
        else
        {
        	System.err.println("Packet " + strCmd + " Config Failed!");
         	result = false;
        }
        
        return result;
    }
	
	public static void GetPointerData(Pointer pNativeData, Structure pJavaStu)
	{
		GetPointerDataToStruct(pNativeData, 0, pJavaStu);
	}

	public static void GetPointerDataToStruct(Pointer pNativeData, long OffsetOfpNativeData, Structure pJavaStu) {
		pJavaStu.write();
		Pointer pJavaMem = pJavaStu.getPointer();
		pJavaMem.write(0, pNativeData.getByteArray(OffsetOfpNativeData, pJavaStu.size()), 0,
				pJavaStu.size());
		pJavaStu.read();
	}
	
	public static void GetPointerDataToStructArr(Pointer pNativeData, Structure []pJavaStuArr) {
		long offset = 0;
		for (int i=0; i<pJavaStuArr.length; ++i)
		{
			GetPointerDataToStruct(pNativeData, offset, pJavaStuArr[i]);
			offset += pJavaStuArr[i].size();
		}
	}
	
	/**
	 * 将结构体数组拷贝到内存
	 * @param pNativeData 
	 * @param pJavaStuArr
	 */
	public static void SetStructArrToPointerData(Structure []pJavaStuArr, Pointer pNativeData) {
		long offset = 0;
		for (int i = 0; i < pJavaStuArr.length; ++i) {
			SetStructDataToPointer(pJavaStuArr[i], pNativeData, offset);
			offset += pJavaStuArr[i].size();
		}
	}
	
	public static void SetStructDataToPointer(Structure pJavaStu, Pointer pNativeData, long OffsetOfpNativeData){
		pJavaStu.write();
		Pointer pJavaMem = pJavaStu.getPointer();
		pNativeData.write(OffsetOfpNativeData, pJavaMem.getByteArray(0, pJavaStu.size()), 0, pJavaStu.size());
	}
	
	public static void ByteArrToStructure(byte[] pNativeData, Structure pJavaStu) {
		pJavaStu.write();
		Pointer pJavaMem = pJavaStu.getPointer();
		pJavaMem.write(0, pNativeData, 0, pJavaStu.size());
		pJavaStu.read();
	}

	public static void ByteArrZero(byte[] dst) {
		// 清零
		for (int i = 0; i < dst.length; ++i) {
			dst[i] = 0;
		}
	}

	//byte转换为byte[2]数组
	public static byte[] getByteArray(byte b){
		byte[] array = new byte[8];
		for (int i = 0; i < 8; i++) {
			array[i] = (byte) ((b & (1 << i)) > 0 ? 1:0);		
		}
		
		return array;
	}
	
	public static void StringToByteArr(String src, byte[] dst) {
		try {
			byte[] GBKBytes = src.getBytes("GBK");
			for (int i = 0; i < GBKBytes.length; i++) {
				dst[i] = (byte) GBKBytes[i];
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public static long GetFileSize(String filePath)
	{
		File f = new File(filePath);
		if (f.exists() && f.isFile()) {
			return f.length();
		}
		else
		{
			return 0;
		}
	}
	
	public static boolean ReadAllFileToMemory(String file, Memory mem)
	{
		if (mem != Memory.NULL)
		{
			long fileLen = GetFileSize(file);
			if (fileLen <= 0)
			{
				return false;
			}
			
			try {
				File infile = new File(file);
				if (infile.canRead())
				{
					FileInputStream in = new FileInputStream(infile);
					int buffLen = 1024; 
					byte[] buffer = new byte[buffLen];
					long currFileLen = 0;
					int readLen = 0;
					while (currFileLen < fileLen)
					{
						readLen = in.read(buffer);
						mem.write(currFileLen, buffer, 0, readLen);
						currFileLen += readLen;
					}
					
					in.close();
					return true;
				}
		        else
		        {
		        	System.err.println("Failed to open file %s for read!!!\n");
		            return false;
		        }
			}catch (Exception e)
			{
				System.err.println("Failed to open file %s for read!!!\n");
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	public static void savePicture(Pointer pBuf, int dwBufSize, String sDstFile)
	{
        try
        {
        	DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(sDstFile)));
        	out.write(pBuf.getByteArray(0, dwBufSize), 0, dwBufSize);
        	out.close();
        } catch (Exception e){
        	e.printStackTrace();
        }
	}
	
	public static void savePicture(Pointer pBuf, int dwBufOffset, int dwBufSize, String sDstFile)
	{
        try
        {
        	DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(sDstFile)));
        	out.write(pBuf.getByteArray(dwBufOffset, dwBufSize), 0, dwBufSize);
        	out.close();
        } catch (Exception e){
        	e.printStackTrace();
        }
	}
	
	// 读取本地图片到byte[]
	public static byte[] readPictureToByteArray(String filename) {
		File file = new File(filename);
		if(!file.exists()) {
			System.err.println("picture is not exist!");
			return null;
		}
		
		ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream((int)file.length());
		BufferedInputStream byteInStream = null;
		try {
			byteInStream = new BufferedInputStream(new FileInputStream(file));
			byte[] buf = new byte[1024];
			int len = 0;
			while((len = byteInStream.read(buf)) != -1) {
				byteOutStream.write(buf, 0, len);
			}
		} catch (FileNotFoundException e) {		
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				byteInStream.close();
				byteOutStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return byteOutStream.toByteArray();
	}
	
	// 将一位数组转为二维数组
	public static byte[][] ByteArrToByteArrArr(byte[] byteArr, int count, int length) {
		if(count * length != byteArr.length) {
			System.err.println(count * length + " != " + byteArr.length);
			return null ;
		}
		byte[][] byteArrArr = new byte[count][length];
		
		for(int i = 0; i < count; i++) {
			System.arraycopy(byteArr, i * length, byteArrArr[i], 0, length);
		}
		
		return byteArrArr;
	}
	
	// 获取操作平台信息
	public static String getLoadLibrary(String library) {
		String path = "";
		String os = System.getProperty("os.name");
		if(os.toLowerCase().startsWith("win")) {
			path = "./libs/";
		} else if(os.toLowerCase().startsWith("linux")) {
			path = "";
		}

		return (path + library);
	}
		
	public static String getOsName() {
		String osName = "";
		String os = System.getProperty("os.name");
		if(os.toLowerCase().startsWith("win")) {
			osName = "win";
		} else if(os.toLowerCase().startsWith("linux")) {
			osName = "linux";
		}
		
		return osName;
	}
	
}


