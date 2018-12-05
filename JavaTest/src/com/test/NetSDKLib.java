package com.test;

import java.awt.Point;
import java.util.Arrays;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Union;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.win32.StdCallLibrary.StdCallCallback;

/**
 * SDK JNA接口封装
 */
public interface NetSDKLib extends Library {
	NetSDKLib NETSDK_INSTANCE = (NetSDKLib)Native.loadLibrary(INetSDK.getLoadLibrary("dhnetsdk"), NetSDKLib.class);
	NetSDKLib CONFIG_INSTANCE = (NetSDKLib)Native.loadLibrary(INetSDK.getLoadLibrary("dhconfigsdk"), NetSDKLib.class);
	
    /************************************************************************
     ** 常量定义
     ***********************************************************************/
    public static final int NET_SERIALNO_LEN                      = 48;             // 设备序列号字符长度
    public static final int NET_CFG_Max_VideoColor                = 24;             // 每个通道最大视频输入颜色配置数量
    public static final int NET_CFG_Custom_Title_Len              = 1024;           // 自定义标题名称长度(扩充到1024)
    public static final int NET_CFG_Custom_TitleType_Len          = 32;             // 自定义标题类型长度
    public static final int NET_CFG_Max_Video_Widget_Cover        = 16;             // 编码区域覆盖最大数量
    public static final int NET_CFG_Max_Video_Widget_Custom_Title = 8;              // 编码物件自定义标题最大数量
    public static final int NET_CFG_Max_Video_Widget_Sensor_Info  = 2;              // 编码物件叠加传感器信息的最大数目
    public static final int NET_CFG_Max_Description_Num           = 4;              // 叠加区域描述信息的最大个数

    // 错误类型代号，对应CLIENT_GetLastError接口的返回值, 十进制
    public static final int NET_NOERROR                         =  0;               // 没有错误
    public static final int NET_ERROR                           = -1;               // 未知错误
    public static final int NET_SYSTEM_ERROR                    = (0x80000000|1);   // Windows系统出错
    public static final int NET_NETWORK_ERROR                   = (0x80000000|2);   // 网络错误，可能是因为网络超时
    public static final int NET_DEV_VER_NOMATCH                 = (0x80000000|3);   // 设备协议不匹配
    public static final int NET_INVALID_HANDLE                  = (0x80000000|4);   // 句柄无效
    public static final int NET_OPEN_CHANNEL_ERROR              = (0x80000000|5);   // 打开通道失败
    public static final int NET_CLOSE_CHANNEL_ERROR             = (0x80000000|6);   // 关闭通道失败
    public static final int NET_ILLEGAL_PARAM                   = (0x80000000|7);   // 用户参数不合法
    public static final int NET_SDK_INIT_ERROR                  = (0x80000000|8);   // SDK初始化出错
    public static final int NET_SDK_UNINIT_ERROR                = (0x80000000|9);   // SDK清理出错
    public static final int NET_RENDER_OPEN_ERROR               = (0x80000000|10);  // 申请render资源出错
    public static final int NET_DEC_OPEN_ERROR                  = (0x80000000|11);  // 打开解码库出错
    public static final int NET_DEC_CLOSE_ERROR                 = (0x80000000|12);  // 关闭解码库出错
    public static final int NET_MULTIPLAY_NOCHANNEL             = (0x80000000|13);  // 多画面预览中检测到通道数为0
    public static final int NET_TALK_INIT_ERROR                 = (0x80000000|14);  // 录音库初始化失败
    public static final int NET_TALK_NOT_INIT                   = (0x80000000|15);  // 录音库未经初始化
    public static final int NET_TALK_SENDDATA_ERROR             = (0x80000000|16);  // 发送音频数据出错
    public static final int NET_REAL_ALREADY_SAVING             = (0x80000000|17);  // 实时数据已经处于保存状态
    public static final int NET_NOT_SAVING                      = (0x80000000|18);  // 未保存实时数据
    public static final int NET_OPEN_FILE_ERROR                 = (0x80000000|19);  // 打开文件出错
    public static final int NET_PTZ_SET_TIMER_ERROR             = (0x80000000|20);  // 启动云台控制定时器失败
    public static final int NET_RETURN_DATA_ERROR               = (0x80000000|21);  // 对返回数据的校验出错
    public static final int NET_INSUFFICIENT_BUFFER             = (0x80000000|22);  // 没有足够的缓存
    public static final int NET_NOT_SUPPORTED                   = (0x80000000|23);  // 当前SDK未支持该功能
    public static final int NET_NO_RECORD_FOUND                 = (0x80000000|24);  // 查询不到录像
    public static final int NET_NOT_AUTHORIZED                  = (0x80000000|25);  // 无操作权限
    public static final int NET_NOT_NOW                         = (0x80000000|26);  // 暂时无法执行
    public static final int NET_NO_TALK_CHANNEL                 = (0x80000000|27);  // 未发现对讲通道
    public static final int NET_NO_AUDIO                        = (0x80000000|28);  // 未发现音频
    public static final int NET_NO_INIT                         = (0x80000000|29);  // 网络SDK未经初始化
    public static final int NET_DOWNLOAD_END                    = (0x80000000|30);  // 下载已结束
    public static final int NET_EMPTY_LIST                      = (0x80000000|31);  // 查询结果为空
    public static final int NET_ERROR_GETCFG_SYSATTR            = (0x80000000|32);  // 获取系统属性配置失败
    public static final int NET_ERROR_GETCFG_SERIAL             = (0x80000000|33);  // 获取序列号失败
    public static final int NET_ERROR_GETCFG_GENERAL            = (0x80000000|34);  // 获取常规属性失败
    public static final int NET_ERROR_GETCFG_DSPCAP             = (0x80000000|35);  // 获取DSP能力描述失败
    public static final int NET_ERROR_GETCFG_NETCFG             = (0x80000000|36);  // 获取网络配置失败
    public static final int NET_ERROR_GETCFG_CHANNAME           = (0x80000000|37);  // 获取通道名称失败
    public static final int NET_ERROR_GETCFG_VIDEO              = (0x80000000|38);  // 获取视频属性失败
    public static final int NET_ERROR_GETCFG_RECORD             = (0x80000000|39);  // 获取录象配置失败
    public static final int NET_ERROR_GETCFG_PRONAME            = (0x80000000|40);  // 获取解码器协议名称失败
    public static final int NET_ERROR_GETCFG_FUNCNAME           = (0x80000000|41);  // 获取232串口功能名称失败
    public static final int NET_ERROR_GETCFG_485DECODER         = (0x80000000|42);  // 获取解码器属性失败
    public static final int NET_ERROR_GETCFG_232COM             = (0x80000000|43);  // 获取232串口配置失败
    public static final int NET_ERROR_GETCFG_ALARMIN            = (0x80000000|44);  // 获取外部报警输入配置失败
    public static final int NET_ERROR_GETCFG_ALARMDET           = (0x80000000|45);  // 获取动态检测报警失败
    public static final int NET_ERROR_GETCFG_SYSTIME            = (0x80000000|46);  // 获取设备时间失败
    public static final int NET_ERROR_GETCFG_PREVIEW            = (0x80000000|47);  // 获取预览参数失败
    public static final int NET_ERROR_GETCFG_AUTOMT             = (0x80000000|48);  // 获取自动维护配置失败
    public static final int NET_ERROR_GETCFG_VIDEOMTRX          = (0x80000000|49);  // 获取视频矩阵配置失败
    public static final int NET_ERROR_GETCFG_COVER              = (0x80000000|50);  // 获取区域遮挡配置失败
    public static final int NET_ERROR_GETCFG_WATERMAKE          = (0x80000000|51);  // 获取图象水印配置失败
    public static final int NET_ERROR_SETCFG_GENERAL            = (0x80000000|55);  // 修改常规属性失败
    public static final int NET_ERROR_SETCFG_NETCFG             = (0x80000000|56);  // 修改网络配置失败
    public static final int NET_ERROR_SETCFG_CHANNAME           = (0x80000000|57);  // 修改通道名称失败
    public static final int NET_ERROR_SETCFG_VIDEO              = (0x80000000|58);  // 修改视频属性失败
    public static final int NET_ERROR_SETCFG_RECORD             = (0x80000000|59);  // 修改录象配置失败
    public static final int NET_ERROR_SETCFG_485DECODER         = (0x80000000|60);  // 修改解码器属性失败
    public static final int NET_ERROR_SETCFG_232COM             = (0x80000000|61);  // 修改232串口配置失败
    public static final int NET_ERROR_SETCFG_ALARMIN            = (0x80000000|62);  // 修改外部输入报警配置失败
    public static final int NET_ERROR_SETCFG_ALARMDET           = (0x80000000|63);  // 修改动态检测报警配置失败
    public static final int NET_ERROR_SETCFG_SYSTIME            = (0x80000000|64);  // 修改设备时间失败
    public static final int NET_ERROR_SETCFG_PREVIEW            = (0x80000000|65);  // 修改预览参数失败
    public static final int NET_ERROR_SETCFG_AUTOMT             = (0x80000000|66);  // 修改自动维护配置失败
    public static final int NET_ERROR_SETCFG_VIDEOMTRX          = (0x80000000|67);  // 修改视频矩阵配置失败
    public static final int NET_ERROR_SETCFG_COVER              = (0x80000000|68);  // 修改区域遮挡配置失败
    public static final int NET_ERROR_SETCFG_WATERMAKE          = (0x80000000|69);  // 修改图象水印配置失败
    public static final int NET_ERROR_SETCFG_WLAN               = (0x80000000|70);  // 修改无线网络信息失败
    public static final int NET_ERROR_SETCFG_WLANDEV            = (0x80000000|71);  // 选择无线网络设备失败
    public static final int NET_ERROR_SETCFG_REGISTER           = (0x80000000|72);  // 修改主动注册参数配置失败
    public static final int NET_ERROR_SETCFG_CAMERA             = (0x80000000|73);  // 修改摄像头属性配置失败
    public static final int NET_ERROR_SETCFG_INFRARED           = (0x80000000|74);  // 修改红外报警配置失败
    public static final int NET_ERROR_SETCFG_SOUNDALARM         = (0x80000000|75);  // 修改音频报警配置失败
    public static final int NET_ERROR_SETCFG_STORAGE            = (0x80000000|76);  // 修改存储位置配置失败
    public static final int NET_AUDIOENCODE_NOTINIT             = (0x80000000|77);  // 音频编码接口没有成功初始化
    public static final int NET_DATA_TOOLONGH                   = (0x80000000|78);  // 数据过长
    public static final int NET_UNSUPPORTED                     = (0x80000000|79);  // 设备不支持该操作
    public static final int NET_DEVICE_BUSY                     = (0x80000000|80);  // 设备资源不足
    public static final int NET_SERVER_STARTED                  = (0x80000000|81);  // 服务器已经启动
    public static final int NET_SERVER_STOPPED                  = (0x80000000|82);  // 服务器尚未成功启动
    public static final int NET_LISTER_INCORRECT_SERIAL         = (0x80000000|83);  // 输入序列号有误
    public static final int NET_QUERY_DISKINFO_FAILED           = (0x80000000|84);  // 获取硬盘信息失败
    public static final int NET_ERROR_GETCFG_SESSION            = (0x80000000|85);  // 获取连接Session信息
    public static final int NET_USER_FLASEPWD_TRYTIME           = (0x80000000|86);  // 输入密码错误超过限制次数
    public static final int NET_LOGIN_ERROR_PASSWORD            = (0x80000000|100); // 密码不正确
    public static final int NET_LOGIN_ERROR_USER                = (0x80000000|101); // 帐户不存在
    public static final int NET_LOGIN_ERROR_TIMEOUT             = (0x80000000|102); // 等待登录返回超时
    public static final int NET_LOGIN_ERROR_RELOGGIN            = (0x80000000|103); // 帐号已登录
    public static final int NET_LOGIN_ERROR_LOCKED              = (0x80000000|104); // 帐号已被锁定
    public static final int NET_LOGIN_ERROR_BLACKLIST           = (0x80000000|105); // 帐号已被列为黑名单
    public static final int NET_LOGIN_ERROR_BUSY                = (0x80000000|106); // 资源不足，系统忙
    public static final int NET_LOGIN_ERROR_CONNECT             = (0x80000000|107); // 登录设备超时，请检查网络并重试
    public static final int NET_LOGIN_ERROR_NETWORK             = (0x80000000|108); // 网络连接失败
    public static final int NET_LOGIN_ERROR_SUBCONNECT          = (0x80000000|109); // 登录设备成功，但无法创建视频通道，请检查网络状况
    public static final int NET_LOGIN_ERROR_MAXCONNECT          = (0x80000000|110); // 超过最大连接数
    public static final int NET_LOGIN_ERROR_PROTOCOL3_ONLY      = (0x80000000|111); // 只支持3代协议
    public static final int NET_LOGIN_ERROR_UKEY_LOST           = (0x80000000|112); // 未插入U盾或U盾信息错误
    public static final int NET_LOGIN_ERROR_NO_AUTHORIZED       = (0x80000000|113); // 客户端IP地址没有登录权限
    public static final int NET_LOGIN_ERROR_USER_OR_PASSOWRD    = (0x80000000|117); // 账号或密码错误 
    public static final int NET_RENDER_SOUND_ON_ERROR           = (0x80000000|120); // Render库打开音频出错
    public static final int NET_RENDER_SOUND_OFF_ERROR          = (0x80000000|121); // Render库关闭音频出错
    public static final int NET_RENDER_SET_VOLUME_ERROR         = (0x80000000|122); // Render库控制音量出错
    public static final int NET_RENDER_ADJUST_ERROR             = (0x80000000|123); // Render库设置画面参数出错
    public static final int NET_RENDER_PAUSE_ERROR              = (0x80000000|124); // Render库暂停播放出错
    public static final int NET_RENDER_SNAP_ERROR               = (0x80000000|125); // Render库抓图出错
    public static final int NET_RENDER_STEP_ERROR               = (0x80000000|126); // Render库步进出错
    public static final int NET_RENDER_FRAMERATE_ERROR          = (0x80000000|127); // Render库设置帧率出错
    public static final int NET_RENDER_DISPLAYREGION_ERROR      = (0x80000000|128); // Render库设置显示区域出错
    public static final int NET_GROUP_EXIST                     = (0x80000000|140); // 组名已存在
    public static final int NET_GROUP_NOEXIST                   = (0x80000000|141); // 组名不存在
    public static final int NET_GROUP_RIGHTOVER                 = (0x80000000|142); // 组的权限超出权限列表范围
    public static final int NET_GROUP_HAVEUSER                  = (0x80000000|143); // 组下有用户，不能删除
    public static final int NET_GROUP_RIGHTUSE                  = (0x80000000|144); // 组的某个权限被用户使用，不能出除
    public static final int NET_GROUP_SAMENAME                  = (0x80000000|145); // 新组名同已有组名重复
    public static final int NET_USER_EXIST                      = (0x80000000|146); // 用户已存在
    public static final int NET_USER_NOEXIST                    = (0x80000000|147); // 用户不存在
    public static final int NET_USER_RIGHTOVER                  = (0x80000000|148); // 用户权限超出组权限
    public static final int NET_USER_PWD                        = (0x80000000|149); // 保留帐号，不容许修改密码
    public static final int NET_USER_FLASEPWD                   = (0x80000000|150); // 密码不正确
    public static final int NET_USER_NOMATCHING                 = (0x80000000|151); // 密码不匹配
    public static final int NET_USER_INUSE                      = (0x80000000|152); // 账号正在使用中
    public static final int NET_ERROR_GETCFG_ETHERNET           = (0x80000000|300); // 获取网卡配置失败
    public static final int NET_ERROR_GETCFG_WLAN               = (0x80000000|301); // 获取无线网络信息失败
    public static final int NET_ERROR_GETCFG_WLANDEV            = (0x80000000|302); // 获取无线网络设备失败
    public static final int NET_ERROR_GETCFG_REGISTER           = (0x80000000|303); // 获取主动注册参数失败
    public static final int NET_ERROR_GETCFG_CAMERA             = (0x80000000|304); // 获取摄像头属性失败
    public static final int NET_ERROR_GETCFG_INFRARED           = (0x80000000|305); // 获取红外报警配置失败
    public static final int NET_ERROR_GETCFG_SOUNDALARM         = (0x80000000|306); // 获取音频报警配置失败
    public static final int NET_ERROR_GETCFG_STORAGE            = (0x80000000|307); // 获取存储位置配置失败
    public static final int NET_ERROR_GETCFG_MAIL               = (0x80000000|308); // 获取邮件配置失败
    public static final int NET_CONFIG_DEVBUSY                  = (0x80000000|309); // 暂时无法设置
    public static final int NET_CONFIG_DATAILLEGAL              = (0x80000000|310); // 配置数据不合法
    public static final int NET_ERROR_GETCFG_DST                = (0x80000000|311); // 获取夏令时配置失败
    public static final int NET_ERROR_SETCFG_DST                = (0x80000000|312); // 设置夏令时配置失败
    public static final int NET_ERROR_GETCFG_VIDEO_OSD          = (0x80000000|313); // 获取视频OSD叠加配置失败
    public static final int NET_ERROR_SETCFG_VIDEO_OSD          = (0x80000000|314); // 设置视频OSD叠加配置失败
    public static final int NET_ERROR_GETCFG_GPRSCDMA           = (0x80000000|315); // 获取CDMA\GPRS网络配置失败
    public static final int NET_ERROR_SETCFG_GPRSCDMA           = (0x80000000|316); // 设置CDMA\GPRS网络配置失败
    public static final int NET_ERROR_GETCFG_IPFILTER           = (0x80000000|317); // 获取IP过滤配置失败
    public static final int NET_ERROR_SETCFG_IPFILTER           = (0x80000000|318); // 设置IP过滤配置失败
    public static final int NET_ERROR_GETCFG_TALKENCODE         = (0x80000000|319); // 获取语音对讲编码配置失败
    public static final int NET_ERROR_SETCFG_TALKENCODE         = (0x80000000|320); // 设置语音对讲编码配置失败
    public static final int NET_ERROR_GETCFG_RECORDLEN          = (0x80000000|321); // 获取录像打包长度配置失败
    public static final int NET_ERROR_SETCFG_RECORDLEN          = (0x80000000|322); // 设置录像打包长度配置失败
    public static final int NET_DONT_SUPPORT_SUBAREA            = (0x80000000|323); // 不支持网络硬盘分区
    public static final int NET_ERROR_GET_AUTOREGSERVER         = (0x80000000|324); // 获取设备上主动注册服务器信息失败
    public static final int NET_ERROR_CONTROL_AUTOREGISTER      = (0x80000000|325); // 主动注册重定向注册错误
    public static final int NET_ERROR_DISCONNECT_AUTOREGISTER   = (0x80000000|326); // 断开主动注册服务器错误
    public static final int NET_ERROR_GETCFG_MMS                = (0x80000000|327); // 获取mms配置失败
    public static final int NET_ERROR_SETCFG_MMS                = (0x80000000|328); // 设置mms配置失败
    public static final int NET_ERROR_GETCFG_SMSACTIVATION      = (0x80000000|329); // 获取短信激活无线连接配置失败
    public static final int NET_ERROR_SETCFG_SMSACTIVATION      = (0x80000000|330); // 设置短信激活无线连接配置失败
    public static final int NET_ERROR_GETCFG_DIALINACTIVATION   = (0x80000000|331); // 获取拨号激活无线连接配置失败
    public static final int NET_ERROR_SETCFG_DIALINACTIVATION   = (0x80000000|332); // 设置拨号激活无线连接配置失败
    public static final int NET_ERROR_GETCFG_VIDEOOUT           = (0x80000000|333); // 查询视频输出参数配置失败
    public static final int NET_ERROR_SETCFG_VIDEOOUT           = (0x80000000|334); // 设置视频输出参数配置失败
    public static final int NET_ERROR_GETCFG_OSDENABLE          = (0x80000000|335); // 获取osd叠加使能配置失败
    public static final int NET_ERROR_SETCFG_OSDENABLE          = (0x80000000|336); // 设置osd叠加使能配置失败
    public static final int NET_ERROR_SETCFG_ENCODERINFO        = (0x80000000|337); // 设置数字通道前端编码接入配置失败
    public static final int NET_ERROR_GETCFG_TVADJUST           = (0x80000000|338); // 获取TV调节配置失败
    public static final int NET_ERROR_SETCFG_TVADJUST           = (0x80000000|339); // 设置TV调节配置失败
    public static final int NET_ERROR_CONNECT_FAILED            = (0x80000000|340); // 请求建立连接失败
    public static final int NET_ERROR_SETCFG_BURNFILE           = (0x80000000|341); // 请求刻录文件上传失败
    public static final int NET_ERROR_SNIFFER_GETCFG            = (0x80000000|342); // 获取抓包配置信息失败
    public static final int NET_ERROR_SNIFFER_SETCFG            = (0x80000000|343); // 设置抓包配置信息失败
    public static final int NET_ERROR_DOWNLOADRATE_GETCFG       = (0x80000000|344); // 查询下载限制信息失败
    public static final int NET_ERROR_DOWNLOADRATE_SETCFG       = (0x80000000|345); // 设置下载限制信息失败
    public static final int NET_ERROR_SEARCH_TRANSCOM           = (0x80000000|346); // 查询串口参数失败
    public static final int NET_ERROR_GETCFG_POINT              = (0x80000000|347); // 获取预制点信息错误
    public static final int NET_ERROR_SETCFG_POINT              = (0x80000000|348); // 设置预制点信息错误
    public static final int NET_SDK_LOGOUT_ERROR                = (0x80000000|349); // SDK没有正常登出设备
    public static final int NET_ERROR_GET_VEHICLE_CFG           = (0x80000000|350); // 获取车载配置失败
    public static final int NET_ERROR_SET_VEHICLE_CFG           = (0x80000000|351); // 设置车载配置失败
    public static final int NET_ERROR_GET_ATM_OVERLAY_CFG       = (0x80000000|352); // 获取atm叠加配置失败
    public static final int NET_ERROR_SET_ATM_OVERLAY_CFG       = (0x80000000|353); // 设置atm叠加配置失败
    public static final int NET_ERROR_GET_ATM_OVERLAY_ABILITY   = (0x80000000|354); // 获取atm叠加能力失败
    public static final int NET_ERROR_GET_DECODER_TOUR_CFG      = (0x80000000|355); // 获取解码器解码轮巡配置失败
    public static final int NET_ERROR_SET_DECODER_TOUR_CFG      = (0x80000000|356); // 设置解码器解码轮巡配置失败
    public static final int NET_ERROR_CTRL_DECODER_TOUR         = (0x80000000|357); // 控制解码器解码轮巡失败
    public static final int NET_GROUP_OVERSUPPORTNUM            = (0x80000000|358); // 超出设备支持最大用户组数目
    public static final int NET_USER_OVERSUPPORTNUM             = (0x80000000|359); // 超出设备支持最大用户数目
    public static final int NET_ERROR_GET_SIP_CFG               = (0x80000000|368); // 获取SIP配置失败
    public static final int NET_ERROR_SET_SIP_CFG               = (0x80000000|369); // 设置SIP配置失败
    public static final int NET_ERROR_GET_SIP_ABILITY           = (0x80000000|370); // 获取SIP能力失败
    public static final int NET_ERROR_GET_WIFI_AP_CFG           = (0x80000000|371); // 获取WIFI ap配置失败
    public static final int NET_ERROR_SET_WIFI_AP_CFG           = (0x80000000|372); // 设置WIFI ap配置失败
    public static final int NET_ERROR_GET_DECODE_POLICY         = (0x80000000|373); // 获取解码策略配置失败
    public static final int NET_ERROR_SET_DECODE_POLICY         = (0x80000000|374); // 设置解码策略配置失败
    public static final int NET_ERROR_TALK_REJECT               = (0x80000000|375); // 拒绝对讲
    public static final int NET_ERROR_TALK_OPENED               = (0x80000000|376); // 对讲被其他客户端打开
    public static final int NET_ERROR_TALK_RESOURCE_CONFLICIT   = (0x80000000|377); // 资源冲突
    public static final int NET_ERROR_TALK_UNSUPPORTED_ENCODE   = (0x80000000|378); // 不支持的语音编码格式
    public static final int NET_ERROR_TALK_RIGHTLESS            = (0x80000000|379); // 无权限
    public static final int NET_ERROR_TALK_FAILED               = (0x80000000|380); // 请求对讲失败
    public static final int NET_ERROR_GET_MACHINE_CFG           = (0x80000000|381); // 获取机器相关配置失败
    public static final int NET_ERROR_SET_MACHINE_CFG           = (0x80000000|382); // 设置机器相关配置失败
    public static final int NET_ERROR_GET_DATA_FAILED           = (0x80000000|383); // 设备无法获取当前请求数据
    public static final int NET_ERROR_MAC_VALIDATE_FAILED       = (0x80000000|384); // MAC地址验证失败
    public static final int NET_ERROR_GET_INSTANCE              = (0x80000000|385); // 获取服务器实例失败
    public static final int NET_ERROR_JSON_REQUEST              = (0x80000000|386); // 生成的json字符串错误
    public static final int NET_ERROR_JSON_RESPONSE             = (0x80000000|387); // 响应的json字符串错误
    public static final int NET_ERROR_VERSION_HIGHER            = (0x80000000|388); // 协议版本低于当前使用的版本
    public static final int NET_SPARE_NO_CAPACITY               = (0x80000000|389); // 热备操作失败, 容量不足
    public static final int NET_ERROR_SOURCE_IN_USE             = (0x80000000|390); // 显示源被其他输出占用
    public static final int NET_ERROR_REAVE                     = (0x80000000|391); // 高级用户抢占低级用户资源
    public static final int NET_ERROR_NETFORBID                 = (0x80000000|392); // 禁止入网
    public static final int NET_ERROR_GETCFG_MACFILTER          = (0x80000000|393); // 获取MAC过滤配置失败
    public static final int NET_ERROR_SETCFG_MACFILTER          = (0x80000000|394); // 设置MAC过滤配置失败
    public static final int NET_ERROR_GETCFG_IPMACFILTER        = (0x80000000|395); // 获取IP/MAC过滤配置失败
    public static final int NET_ERROR_SETCFG_IPMACFILTER        = (0x80000000|396); // 设置IP/MAC过滤配置失败
    public static final int NET_ERROR_OPERATION_OVERTIME        = (0x80000000|397); // 当前操作超时
    public static final int NET_ERROR_SENIOR_VALIDATE_FAILED    = (0x80000000|398); // 高级校验失败
    public static final int NET_ERROR_DEVICE_ID_NOT_EXIST       = (0x80000000|399); // 设备ID不存在
    public static final int NET_ERROR_UNSUPPORTED               = (0x80000000|400); // 不支持当前操作
    public static final int NET_ERROR_PROXY_DLLLOAD             = (0x80000000|401); // 代理库加载失败
    public static final int NET_ERROR_PROXY_ILLEGAL_PARAM       = (0x80000000|402); // 代理用户参数不合法
    public static final int NET_ERROR_PROXY_INVALID_HANDLE      = (0x80000000|403); // 代理句柄无效
    public static final int NET_ERROR_PROXY_LOGIN_DEVICE_ERROR  = (0x80000000|404); // 代理登入前端设备失败
    public static final int NET_ERROR_PROXY_START_SERVER_ERROR  = (0x80000000|405); // 启动代理服务失败

    // CLIENT_StartListenEx报警事件
    public static final int NET_ALARM_ALARM_EX 					= 0x2101;     		// 外部报警，数据字节数与设备报警通道个数相同，每个字节表示一个报警通道的报警状态，1为有报警，0为无报警。
    public static final int NET_VIDEOLOST_ALARM_EX 				= 0x2103; 			// 视频丢失报警，数据字节数与设备视频通道个数相同，每个字节表示一个视频通道的视频丢失报警状态，1为有报警，0为无报警。
    public static final int NET_SHELTER_ALARM_EX 				= 0x2104;   		// 视频遮挡报警，数据字节数与设备视频通道个数相同，每个字节表示一个视频通道的遮挡(黑屏)报警状态，1为有报警，0为无报警。
    public static final int NET_DISKFULL_ALARM_EX 				= 0x2106;  			// 硬盘满报警，数据为1个字节，1为有硬盘满报警，0为无报警。
    public static final int NET_DISKERROR_ALARM_EX 				= 0x2107; 			// 坏硬盘报警，数据为32个字节，每个字节表示一个硬盘的故障报警状态，1为有报警，0为无报警。
    public static final int NET_ALARM_ACC_POWEROFF              = 0x211E;           // ACC断电报警，数据为 DWORD 0：ACC通电 1：ACC断电 
    public static final int NET_ALARM_FRONTDISCONNECT           = 0x2132;           // 前端IPC断网报警(对应结构体 ALARM_FRONTDISCONNET_INFO)
    public static final int NET_ALARM_BATTERYLOWPOWER 			= 0x2134;      		// 电池电量低报警(对应结构体 ALARM_BATTERYLOWPOWER_INFO)
    public static final int NET_ALARM_TEMPERATURE 				= 0x2135;  			// 温度过高报警(对应结构体 ALARM_TEMPERATURE_INFO)
    public static final int NET_ALARM_STORAGE_FAILURE_EX        = 0x2163;           // 存储错误报警(对应结构体 ALARM_STORAGE_FAILURE_EX)
    public static final int NET_ALARM_TALKING_INVITE            = 0x2171;           // 设备请求对方发起对讲事件(对应结构体  ALARM_TALKING_INVITE_INFO)
    public static final int NET_ALARM_ALARM_EX2 				= 0x2175;    		// 本地报警事件(对应结构体ALARM_ALARM_INFO_EX2,对NET_ALARM_ALARM_EX升级)
    public static final int NET_EVENT_VIDEOABNORMALDETECTION    = 0x218e;           // 视频异常事件(对应ALARM_VIDEOABNORMAL_DETECTION_INFO)
    public static final int NET_ALARM_STORAGE_NOT_EXIST         = 0x3167;           // 存储组不存在事件(对应结构体 ALARM_STORAGE_NOT_EXIST_INFO)
    public static final int NET_ALARM_SCADA_DEV_ALARM           = 0x31a2;           // 检测采集设备报警事件(对应结构体 ALARM_SCADA_DEV_INFO)
    public static final int NET_ALARM_PARKING_CARD				= 0x31a4;			// 停车刷卡事件(对应结构体  ALARM_PARKING_CARD)
    public static final int NET_ALARM_VEHICLE_ACC               = 0x31a6;           // 车辆ACC报警事件(对应结构体 ALARM_VEHICLE_ACC_INFO)
    public static final int NET_ALARM_NEW_FILE                  = 0x31b3;           // 新文件事件(对应ALARM_NEW_FILE_INFO)
    public static final int NET_ALARM_HUMAM_NUMBER_STATISTIC    = 0x31cc;           // 人数量/客流量统计事件 (对应结构体 ALARM_HUMAN_NUMBER_STATISTIC_INFO)
    public static final int NET_ALARM_ARMMODE_CHANGE_EVENT      = 0x3175;			// 布撤防状态变化事件(对应结构体 ALARM_ARMMODE_CHANGE_INFO)
    public static final int NET_ALARM_ACCESS_CTL_EVENT          = 0x3181;           // 门禁事件(对应结构体 ALARM_ACCESS_CTL_EVENT_INFO)
    public static final int NET_URGENCY_ALARM_EX2               = 0x3182;           // 紧急报警EX2(对 NET_URGENCY_ALARM_EX 的升级,对应结构体 ALARM_URGENCY_ALARM_EX2, 人为触发的紧急事件, 一般处理是联动外部通讯功能请求帮助
    public static final int NET_ALARM_ALARMCLEAR                = 0x3187;           // 消警事件(对应结构体  ALARM_ALARMCLEAR_INFO )
    public static final int NET_ALARM_RCEMERGENCY_CALL          = 0x318b;  			// 紧急呼叫报警事件(对应结构体 ALARM_RCEMERGENCY_CALL_INFO)
    public static final int NET_ALARM_BUS_SHARP_ACCELERATE      = 0x31ae;           // 车辆急加速事件(对应结构体 ALARM_BUS_SHARP_ACCELERATE_INFO)
    public static final int NET_ALARM_BUS_SHARP_DECELERATE      = 0x31af;           // 车辆急减速事件(对应结构体 ALARM_BUS_SHARP_DECELERATE_INFO)
    public static final int NET_ALARM_HOTSPOT_WARNING           = 0X31d8;           // 热成像热点异常报警(对应结构体 ALARM_HOTSPOT_WARNING_INFO)
    public static final int NET_ALARM_COLDSPOT_WARNING          = 0X31d9;           // 热成像冷点异常报警(对应结构体 ALARM_COLDSPOT_WARNING_INFO)
    public static final int NET_ALARM_FIREWARNING_INFO          = 0X31da;           // 热成像火情报警信息上报(对应结构体 ALARM_FIREWARNING_INFO_DETAIL)
    public static final int NET_ALARM_RADAR_HIGH_SPEED          = 0x31df;           // 雷达监测超速报警事件 智能楼宇专用 (对应结构体 ALARM_RADAR_HIGH_SPEED_INFO)
    public static final int NET_ALARM_POLLING_ALARM             = 0x31e0;           // 设备巡检报警事件 智能楼宇专用 (对应结构体 ALARM_POLLING_ALARM_INFO)
    public static final int NET_ALARM_GPS_NOT_ALIGNED           = 0x321d;           // GPS未定位报警(对应结构体 ALARM_GPS_NOT_ALIGNED_INFO)
    public static final int NET_ALARM_VIDEOBLIND                = 0x323e;           // 视频遮挡事件(对应结构体 ALARM_VIDEO_BLIND_INFO)
    public static final int NET_ALARM_DRIVER_NOTCONFIRM         = 0x323f;           // 司机未按确认按钮报警事件(对应结构体 ALARM_DRIVER_NOTCONFIRM_INFO)
    public static final int NET_ALARM_FACEINFO_COLLECT          = 0x3240;           // 人脸信息录入事件(对应 ALARM_FACEINFO_COLLECT_INFO)
    public static final int NET_ALARM_HIGH_SPEED	            = 0x3241;			// 车辆超速报警事件(对应 ALARM_HIGH_SPEED_INFO )
    public static final int NET_ALARM_VIDEO_LOSS                = 0x3242;			// 视频丢失事件(对应 ALARM_VIDEO_LOSS_INFO )
    
    // 订阅Bus状态对应事件上报(CLIENT_AttachBusState)
    public static final int NET_ALARM_BUS_PASSENGER_CARD_CHECK  = 0x0009;           // 乘客刷卡事件(对应结构体 ALARM_PASSENGER_CARD_CHECK )
    
    // 帧类型掩码定义
    public static final int FRAME_TYPE_MOTION                   = 0x00000001;       // 动检帧
    
    // CLIENT_RealLoadPictureEx 智能抓图事件
    public static final int EVENT_IVS_ALL                       = 0x00000001;       // 订阅所有事件
    public static final int EVENT_IVS_CROSSLINEDETECTION        = 0x00000002;       // 警戒线事件(对应 DEV_EVENT_CROSSLINE_INFO)
    public static final int EVENT_IVS_CROSSREGIONDETECTION      = 0x00000003;       // 警戒区事件(对应 DEV_EVENT_CROSSREGION_INFO)
    public static final int EVENT_IVS_WANDERDETECTION           = 0x00000007;       // 徘徊事件(对应  DEV_EVENT_WANDER_INFO)
    public static final int EVENT_IVS_FIGHTDETECTION            = 0x0000000E;       // 斗殴事件(对应 DEV_EVENT_FIGHT_INFO)  
    public static final int EVENT_IVS_TRAFFICJUNCTION           = 0x00000017;       // 交通路口事件----老规则(对应 DEV_EVENT_TRAFFICJUNCTION_INFO)
    public static final int EVENT_IVS_TRAFFICGATE               = 0x00000018;       // 交通卡口事件----老规则(对应 DEV_EVENT_TRAFFICGATE_INFO)
    public static final int EVENT_IVS_FACEDETECT                = 0x0000001A;       // 人脸检测事件 (对应 DEV_EVENT_FACEDETECT_INFO)
    public static final int EVENT_IVS_TRAFFICJAM                = 0x0000001B;       // 交通拥堵事件(对应 DEV_EVENT_TRAFFICJAM_INFO)
    public static final int EVENT_IVS_TRAFFIC_RUNREDLIGHT       = 0x00000100;       // 交通违章-闯红灯事件(对应 DEV_EVENT_TRAFFIC_RUNREDLIGHT_INFO)
    public static final int EVENT_IVS_TRAFFIC_OVERLINE          = 0x00000101;       // 交通违章-压车道线事件(对应 DEV_EVENT_TRAFFIC_OVERLINE_INFO)
    public static final int EVENT_IVS_TRAFFIC_RETROGRADE        = 0x00000102;       // 交通违章-逆行事件(对应  DEV_EVENT_TRAFFIC_RETROGRADE_INFO)
    public static final int EVENT_IVS_TRAFFIC_TURNLEFT          = 0x00000103;       // 交通违章-违章左转(对应 DEV_EVENT_TRAFFIC_TURNLEFT_INFO)
    public static final int EVENT_IVS_TRAFFIC_TURNRIGHT         = 0x00000104;       // 交通违章-违章右转(对应 DEV_EVENT_TRAFFIC_TURNRIGHT_INFO)
    public static final int EVENT_IVS_TRAFFIC_UTURN             = 0x00000105;       // 交通违章-违章掉头(对应 DEV_EVENT_TRAFFIC_UTURN_INFO)
    public static final int EVENT_IVS_TRAFFIC_OVERSPEED         = 0x00000106;       // 交通违章-超速(对应 DEV_EVENT_TRAFFIC_OVERSPEED_INFO)
    public static final int EVENT_IVS_TRAFFIC_UNDERSPEED        = 0x00000107;       // 交通违章-低速(对应 DEV_EVENT_TRAFFIC_UNDERSPEED_INFO)
    public static final int EVENT_IVS_TRAFFIC_PARKING           = 0x00000108;       // 交通违章-违章停车(对应 DEV_EVENT_TRAFFIC_PARKING_INFO)
    public static final int EVENT_IVS_TRAFFIC_WRONGROUTE        = 0x00000109;       // 交通违章-不按车道行驶(对应 DEV_EVENT_TRAFFIC_WRONGROUTE_INFO)
    public static final int EVENT_IVS_TRAFFIC_CROSSLANE         = 0x0000010A;       // 交通违章-违章变道(对应 DEV_EVENT_TRAFFIC_CROSSLANE_INFO)
    public static final int EVENT_IVS_TRAFFIC_OVERYELLOWLINE    = 0x0000010B;       // 交通违章-压黄线 (对应 DEV_EVENT_TRAFFIC_OVERYELLOWLINE_INFO)
    public static final int EVENT_IVS_TRAFFIC_YELLOWPLATEINLANE = 0x0000010E;       // 交通违章-黄牌车占道事件(对应 DEV_EVENT_TRAFFIC_YELLOWPLATEINLANE_INFO)
    public static final int EVENT_IVS_TRAFFIC_PEDESTRAINPRIORITY = 0x0000010F;      // 交通违章-斑马线行人优先事件(对应 DEV_EVENT_TRAFFIC_PEDESTRAINPRIORITY_INFO)
    public static final int EVENT_IVS_TRAFFIC_NOPASSING         = 0x00000111;       // 交通违章-禁止通行事件(对应 DEV_EVENT_TRAFFIC_NOPASSING_INFO)
    public static final int EVENT_IVS_FACERECOGNITION           = 0x00000117;       // 人脸识别事件(对应 DEV_EVENT_FACERECOGNITION_INFO
    public static final int EVENT_IVS_TRAFFIC_MANUALSNAP        = 0x00000118;       // 交通手动抓拍事件(对应  DEV_EENT_TRAFFIC_MANUALSNAP_INFO)
    public static final int EVENT_IVS_TRAFFIC_FLOWSTATE         = 0x00000119;       // 交通流量统计事件(对应 DEV_EVENT_TRAFFIC_FLOW_STATE)
    public static final int EVENT_IVS_TRAFFIC_VEHICLEINROUTE    = 0x0000011B;       // 有车占道事件(对应 DEV_EVENT_TRAFFIC_VEHICLEINROUTE_INFO)
    public static final int EVENT_IVS_TRAFFIC_TOLLGATE          = 0x00000120;       // 交通违章--卡口事件----新规则(对应 DEV_EVENT_TRAFFICJUNCTION_INFO)
    public static final int EVENT_IVS_TRAFFIC_VEHICLEINBUSROUTE = 0x00000124;       // 交通违章--占用公交车道事件(对应 DEV_EVENT_TRAFFIC_VEHICLEINBUSROUTE_INFO)
    public static final int EVENT_IVS_TRAFFIC_BACKING           = 0x00000125;       // 交通违章--违章倒车事件(对应 DEV_EVENT_IVS_TRAFFIC_BACKING_INFO)
    public static final int EVENT_IVS_AUDIO_ABNORMALDETECTION   = 0x00000126;       // 声音异常检测(对应 DEV_EVENT_IVS_AUDIO_ABNORMALDETECTION_INFO)
    public static final int EVENT_IVS_TRAFFIC_RUNYELLOWLIGHT    = 0x00000127;       // 交通违章-闯黄灯事件(对应 DEV_EVENT_TRAFFIC_RUNYELLOWLIGHT_INFO)
    public static final int EVENT_IVS_CLIMBDETECTION            = 0x00000128;       // 攀高检测事件(对应 DEV_EVENT_IVS_CLIMB_INFO)
    public static final int EVENT_IVS_LEAVEDETECTION            = 0x00000129;       // 离岗检测事件(对应 DEV_EVENT_IVS_LEAVE_INFO)
    public static final int EVENT_IVS_TRAFFIC_PARKINGONYELLOWBOX = 0x0000012A;      // 交通违章--黄网格线抓拍事件(对应 DEV_EVENT_TRAFFIC_PARKINGONYELLOWBOX_INFO)
    public static final int EVENT_IVS_TRAFFIC_PARKINGSPACEPARKING = 0x0000012B;     // 车位有车事件(对应 DEV_EVENT_TRAFFIC_PARKINGSPACEPARKING_INFO )
    public static final int EVENT_IVS_TRAFFIC_PARKINGSPACENOPARKING = 0x0000012C;   // 车位无车事件(对应  DEV_EVENT_TRAFFIC_PARKINGSPACENOPARKING_INFO )
    public static final int EVENT_IVS_TRAFFIC_PEDESTRAIN        = 0x0000012D;       // 交通行人事件(对应 DEV_EVENT_TRAFFIC_PEDESTRAIN_INFO)
    public static final int EVENT_IVS_TRAFFIC_THROW             = 0x0000012E;       // 交通抛洒物品事件(对应 DEV_EVENT_TRAFFIC_THROW_INFO)
    public static final int EVENT_IVS_TRAFFIC_OVERSTOPLINE      = 0X00000137;       // 交通违章--压停止线事件(对应 DEV_EVENT_TRAFFIC_OVERSTOPLINE)
    public static final int EVENT_IVS_TRAFFIC_WITHOUT_SAFEBELT  = 0x00000138;       // 交通违章--交通未系安全带事件(对应 DEV_EVENT_TRAFFIC_WITHOUT_SAFEBELT)
    public static final int EVENT_IVS_TRAFFIC_PASSNOTINORDER    = 0x0000013C;       // 交通违章--未按规定依次通行(对应 DEV_EVENT_TRAFFIC_PASSNOTINORDER_INFO)
    public static final int EVENT_IVS_TRAFFIC_JAM_FORBID_INTO	= 0x00000163;       // 交通违章--车辆拥堵禁入事件(对应 DEV_EVENT_ALARM_JAMFORBIDINTO_INFO)
    public static final int EVENT_IVS_TRAFFIC_FCC               = 0x0000016B;       // 加油站提枪、挂枪事件(对应  DEV_EVENT_TRAFFIC_FCC_INFO)
    
    // CLIENT_GetNewDevConfig / CLIENT_SetNewDevConfig 配置项
    public static final String CFG_CMD_VIDEOWIDGET              = "VideoWidget";         // 视频编码物件配置(对应 NET_CFG_VideoWidget )
    public static final String CFG_CMD_ANALYSEMODULE            = "VideoAnalyseModule";  // 物体的检测模块配置(对应 CFG_ANALYSEMODULES_INFO)
    public static final String CFG_CMD_ANALYSERULE              = "VideoAnalyseRule";    // 视频分析规则配置(对应 CFG_ANALYSERULES_INFO)
    public static final String CFG_CMD_VIDEOINOPTIONS           = "VideoInOptions";      // 视频输入前端选项(对应CFG_VIDEO_IN_OPTIONS)
    public static final String CFG_CMD_RAINBRUSHMODE            = "RainBrushMode";       // 雨刷模式相关配置(对应CFG_RAINBRUSHMODE_INFO数组)
    public static final String CFG_CMD_RAINBRUSH                = "RainBrush";           // 雨刷配置(对应CFG_RAINBRUSH_INFO)
    public static final String CFG_CMD_ENCODE                   = "Encode";              // 图像通道属性配置(对应CFG_ENCODE_INFO)
    public static final String CFG_CMD_VIDEO_IN_ZOOM            = "VideoInZoom";         // 云台通道变倍配置(对应CFG_VIDEO_IN_ZOOM)
    public static final String CFG_CMD_REMOTEDEVICE				= "RemoteDevice";		 // 远程设备信息(对应  AV_CFG_RemoteDevice 数组, 通道无关)
    public static final String CFG_CMD_ANALYSESOURCE			= "VideoAnalyseSource";  // 视频分析资源配置(对应 CFG_ANALYSESOURCE_INFO)
    public static final String CFG_CMD_TRAFFICGLOBAL            = "TrafficGlobal";       // 智能交通全局配置(CFG_TRAFFICGLOBAL_INFO)
    public static final String CFG_CMD_RECORDMODE               = "RecordMode";          // 录像模式(对应  AV_CFG_RecordMode )
    public static final String CFG_CMD_ALARMLAMP                = "AlarmLamp";           // 警灯配置(对应 CFG_ALARMLAMP_INFO)
    public static final String CFG_CMD_ALARMOUT                 = "AlarmOut";            // 报警输出通道配置(对应  CFG_ALARMOUT_INFO )
    public static final String CFG_CMD_INTELLECTIVETRAFFIC      = "TrafficSnapshot";     // 智能交通抓拍(对应 CFG_TRAFFICSNAPSHOT_INFO )
    public static final String CFG_CMD_TRAFFICSNAPSHOT_MULTI    = "TrafficSnapshotNew" ; // 智能交通抓拍( CFG_TRAFFICSNAPSHOT_NEW_INFO )
    public static final String CFG_CMD_NTP       				= "NTP";     			 // 时间同步服务器(对应  CFG_NTP_INFO )
    public static final String CFG_CMD_THERMOMETRY_RULE         = "ThermometryRule";     // 热成像测温规则配置(对应 CFG_RADIOMETRY_RULE_INFO )
    public static final String CFG_CMD_ALARMINPUT               = "Alarm";               // 外部输入报警配置(对应 CFG_ALARMIN_INFO)

    // CLIENT_FileTransmit接口传输文件类型
    public static final int NET_DEV_BLACKWHITETRANS_START      = 0x0003;           // 开始发送黑白名单(对应结构体 DHDEV_BLACKWHITE_LIST_INFO)
    public static final int NET_DEV_BLACKWHITETRANS_SEND       = 0x0004;           // 发送黑白名单
    public static final int NET_DEV_BLACKWHITETRANS_STOP       = 0x0005;           // 停止发送黑白名单
    
    // 配置类型,对应CLIENT_GetDevConfig和CLIENT_SetDevConfig接口
    public static final int NET_DEV_DEVICECFG                   = 0x0001;           // 设备属性配置
    public static final int NET_DEV_NETCFG_EX                   = 0x005b;           // 网络扩展配置(对应结构体 NETDEV_NET_CFG_EX )
    public static final int NET_DEV_TIMECFG                     = 0x0008;           // DVR时间配置

    // 命令类型, 对应 CLIENT_QueryNewSystemInfo 接口
    public static final String CFG_CAP_CMD_DEVICE_STATE         = "trafficSnap.getDeviceStatus"; // 获取设备状态信息 (对应 CFG_CAP_TRAFFIC_DEVICE_STATUS)
    public static final String CFG_CAP_CMD_RECORDFINDER         = "RecordFinder.getCaps";        // 获取查询记录能力集, (对应结构体 CFG_CAP_RECORDFINDER_INFO)
    
    // 远程配置结构体相关常量                 
    public static final int NET_MAX_MAIL_ADDR_LEN              = 128;              // 邮件发(收)地址最大长度
    public static final int NET_MAX_MAIL_SUBJECT_LEN           = 64;               // 邮件主题最大长度
    public static final int NET_MAX_IPADDR_LEN                 = 16;               // IP地址字符串长度
    public static final int NET_MAX_IPADDR_LEN_EX              = 40;               // 扩展IP地址字符串长度, 支持IPV6

    public static final int NET_MAX_DEV_ID_LEN                 = 48;               // 机器编号最大长度
    public static final int NET_MAX_HOST_NAMELEN               = 64;               // 主机名长度,
    public static final int NET_MAX_HOST_PSWLEN                = 32;               // 密码长度
    public static final int NET_MAX_ETHERNET_NUM               = 2;                // 以太网口最大个数
    public static final int NET_MAX_ETHERNET_NUM_EX            = 10;               // 扩展以太网口最大个数
    public static final int NET_DEV_CLASS_LEN                  = 16;               // 设备类型字符串（如"IPC"）长度
    public static final int NET_N_WEEKS                        = 7;                // 一周的天数    
    public static final int NET_N_TSECT                        = 6;                // 通用时间段个数
    public static final int NET_N_REC_TSECT                    = 6;                // 录像时间段个数
    public static final int NET_N_COL_TSECT                    = 2;                // 颜色时间段个数            
    public static final int NET_N_ENCODE_AUX                   = 3;                // 扩展码流个数    
    public static final int NET_N_TALK                         = 1;                // 最多对讲通道个数
    public static final int NET_N_COVERS                       = 1;                // 遮挡区域个数    
    public static final int NET_N_CHANNEL                      = 16;               // 最大通道个数    
    public static final int NET_N_ALARM_TSECT                  = 2;                // 报警提示时间段个数
    public static final int NET_MAX_ALARMOUT_NUM               = 16;               // 报警输出口个数上限
    public static final int NET_MAX_AUDIO_IN_NUM               = 16;               // 音频输入口个数上限
    public static final int NET_MAX_VIDEO_IN_NUM               = 16;               // 视频输入口个数上限
    public static final int NET_MAX_ALARM_IN_NUM               = 16;               // 报警输入口个数上限
    public static final int NET_MAX_DISK_NUM                   = 16;               // 硬盘个数上限,暂定为16
    public static final int NET_MAX_DECODER_NUM                = 16;               // 解码器(485)个数上限    
    public static final int NET_MAX_232FUNCS                   = 10;               // 232串口功能个数上限
    public static final int NET_MAX_232_NUM                    = 2;                // 232串口个数上限
    public static final int NET_MAX_232_NUM_EX                 = 16;               // 扩展串口配置个数上限          
    public static final int NET_MAX_DECPRO_LIST_SIZE           = 100;              // 解码器协议列表个数上限
    public static final int NET_FTP_MAXDIRLEN                  = 240;              // FTP文件目录最大长度
    public static final int NET_MATRIX_MAXOUT                  = 16;               // 矩阵输出口最大个数
    public static final int NET_TOUR_GROUP_NUM                 = 6;                // 矩阵输出组最大个数
    public static final int NET_MAX_DDNS_NUM                   = 10;               // 设备支持的ddns服务器最大个数
    public static final int NET_MAX_SERVER_TYPE_LEN            = 32;               // ddns服务器类型,最大字符串长度
    public static final int NET_MAX_DOMAIN_NAME_LEN            = 256;              // ddns域名,最大字符串长度
    public static final int NET_MAX_DDNS_ALIAS_LEN             = 32;               // ddns服务器别名,最大字符串长度
    public static final int NET_MAX_DEFAULT_DOMAIN_LEN         = 60;               // ddns默认域名,最大字符串长度     
    public static final int NET_MOTION_ROW                     = 32;               // 动态检测区域的行数
    public static final int NET_MOTION_COL                     = 32;               // 动态检测区域的列数
    public static final int NET_STATIC_ROW                     = 32;               // 静态检测区域的行数
    public static final int NET_STATIC_COL                     = 32;               // 静态检测区域的列数
    public static final int NET_FTP_USERNAME_LEN               = 64;               // FTP配置,用户名最大长度
    public static final int NET_FTP_PASSWORD_LEN               = 64;               // FTP配置,密码最大长度
    public static final int NET_TIME_SECTION                   = 2;                // FTP配置,每天时间段个数
    public static final int NET_FTP_MAX_PATH                   = 240;              // FTP配置,文件路径名最大长度
    public static final int NET_FTP_MAX_SUB_PATH               = 128;              // FTP配置,文件路径名最大长度
    public static final int NET_INTERVIDEO_UCOM_CHANID         = 32;               // 平台接入配置,U网通通道ID
    public static final int NET_INTERVIDEO_UCOM_DEVID          = 32;               // 平台接入配置,U网通设备ID
    public static final int NET_INTERVIDEO_UCOM_REGPSW         = 16;               // 平台接入配置,U网通注册密码
    public static final int NET_INTERVIDEO_UCOM_USERNAME       = 32;               // 平台接入配置,U网通用户名
    public static final int NET_INTERVIDEO_UCOM_USERPSW        = 32;               // 平台接入配置,U网通密码
    public static final int NET_INTERVIDEO_NSS_IP              = 32;              // 平台接入配置,中兴力维IP
    public static final int NET_INTERVIDEO_NSS_SERIAL          = 32;               // 平台接入配置,中兴力维serial
    public static final int NET_INTERVIDEO_NSS_USER            = 32;               // 平台接入配置,中兴力维user
    public static final int NET_INTERVIDEO_NSS_PWD             = 50;              // 平台接入配置,中兴力维password
    public static final int NET_MAX_VIDEO_COVER_NUM            = 16;               // 遮挡区域最大个数
    public static final int NET_MAX_WATERMAKE_DATA             = 4096;             // 水印图片数据最大长度
    public static final int NET_MAX_WATERMAKE_LETTER           = 128;              // 水印文字最大长度
    public static final int NET_MAX_WLANDEVICE_NUM             = 10;               // 最多搜索出的无线设备个数
    public static final int NET_MAX_WLANDEVICE_NUM_EX          = 32;               // 最多搜索出的无线设备个数
    public static final int NET_MAX_ALARM_NAME                 = 64;               // 地址长度
    public static final int NET_MAX_REGISTER_SERVER_NUM        = 10;               // 主动注册服务器个数
    public static final int NET_SNIFFER_FRAMEID_NUM            = 6;                // 6个FRAME ID 选项
    public static final int NET_SNIFFER_CONTENT_NUM            = 4;                // 每个FRAME对应的4个抓包内容
    public static final int NET_SNIFFER_CONTENT_NUM_EX         = 8;                // 每个FRAME对应的8个抓包内容
    public static final int NET_SNIFFER_PROTOCOL_SIZE          = 20;               // 协议名字长度
    public static final int NET_MAX_PROTOCOL_NAME_LENGTH       = 20;               
    public static final int NET_SNIFFER_GROUP_NUM              = 4;                // 4组抓包设置
    public static final int NET_ALARM_OCCUR_TIME_LEN           = 40;               // 新的报警上传时间的长度
    public static final int NET_VIDEO_OSD_NAME_NUM             = 64;               // 叠加的名称长度,目前支持32个英文,16个中文
    public static final int NET_VIDEO_CUSTOM_OSD_NUM           = 8;               // 支持的自定义叠加的数目,不包含时间和通道
    public static final int NET_VIDEO_CUSTOM_OSD_NUM_EX        = 256;              // 支持的自定义叠加的数目,不包含时间和通道
    public static final int NET_CONTROL_AUTO_REGISTER_NUM      = 100;              // 支持定向主动注册服务器的个数
    public static final int NET_MMS_RECEIVER_NUM               = 100;              // 支持短信接收者的个数
    public static final int NET_MMS_SMSACTIVATION_NUM          = 100;              // 支持短信发送者的个数
    public static final int NET_MMS_DIALINACTIVATION_NUM       = 100;              // 支持拨号发送者的个数
    public static final int NET_MAX_ALARM_IN_NUM_EX            = 32;               // 报警输入口个数上限
    public static final int NET_MAX_IPADDR_OR_DOMAIN_LEN       = 64;               // IP地址字符串长度
    public static final int NET_MAX_CALLID                     = 32;               // 呼叫ID
    public static final int NET_MAX_FENCE_LINE_NUM             = 2;                // 围栏最大曲线数
    public static final int MAX_SMART_VALUE_NUM                = 30;               // 最多的smart信息个数
    public static final int NET_INTERVIDEO_AMP_DEVICESERIAL    = 48;               // 平台接入配置,天地阳光 设备序列号字符串长度
    public static final int NET_INTERVIDEO_AMP_DEVICENAME      = 16;               // 平台接入配置,天地阳光 设备名称字符串长度
    public static final int NET_INTERVIDEO_AMP_USER            = 32;               // 平台接入配置,天地阳光 注册用户名字符串长度
    public static final int NET_INTERVIDEO_AMP_PWD             = 32;               // 平台接入配置,天地阳光 注册密码字符串长度
    public static final int MAX_SUBMODULE_NUM                  = 32;               // 最多子模块信息个数
    public static final int NET_MAX_CARWAY_NUM                 = 8;                // 交通抓拍,最大车道数
    public static final int NET_MAX_SNAP_SIGNAL_NUM            = 3;                // 一个车道的最大抓拍张数
    public static final int NET_MAX_CARD_NUM                   = 128;              // 卡号的最大个数
    public static final int NET_MAX_CARDINFO_LEN               = 32;               // 每条卡号最长字符数
    public static final int NET_MAX_CONTROLER_NUM              = 64;               // 最大支持控制器数目
    public static final int NET_MAX_LIGHT_NUM                  = 32;               // 最多控制灯组数
    public static final int NET_MAX_SNMP_COMMON_LEN            = 64;               // snmp 读写数据长度
    public static final int NET_MAX_DDNS_STATE_LEN             = 128;              // DDNS 状态信息长度
    public static final int NET_MAX_PHONE_NO_LEN               = 16;               // 电话号码长度
    public static final int NET_MAX_MSGTYPE_LEN                = 32;               // 导航类型或短信息类型长度
    public static final int NET_MAX_MSG_LEN                    = 256;              // 导航和短信息长度
    public static final int NET_MAX_GRAB_INTERVAL_NUM          = 4;                // 多张图片抓拍个数
    public static final int NET_MAX_FLASH_NUM                  = 5;                // 最多支持闪光灯个数
    public static final int NET_MAX_ISCSI_PATH_NUM             = 64;               // ISCSI远程目录最大数量
    public static final int NET_MAX_WIRELESS_CHN_NUM           = 256;              // 无线路由最大信道数
    public static final int NET_PROTOCOL3_BASE                 = 100;              // 三代协议版本基数
    public static final int NET_PROTOCOL3_SUPPORT              = 11;               // 只支持3代协议
    public static final int NET_MAX_STAFF_NUM                  = 20;               // 浓缩视频配置信息中标尺数上限
    public static final int NET_MAX_CALIBRATEBOX_NUM           = 10;               // 浓缩视频配置信息中标定区域数上限
    public static final int NET_MAX_EXCLUDEREGION_NUM          = 10;               // 浓缩视频配置信息中排除区域数上限
    public static final int NET_MAX_POLYLINE_NUM               = 20;               // 浓缩视频配置信息中标尺线数
    public static final int NET_MAX_COLOR_NUM                  = 16;               // 最大颜色数目
    public static final int MAX_OBJFILTER_NUM                  = 16;              // 最大过滤种类个数
    public static final int NET_MAX_SYNOPSIS_STATE_NAME        = 64;               // 视频浓缩状态字符串长度
    public static final int NET_MAX_SYNOPSIS_QUERY_FILE_COUNT  = 10;               // 视频浓缩相关原始文件按照路径查找时文件个数上限
    public static final int NET_MAX_SSID_LEN                   = 36;              // SSID长度
    public static final int NET_MAX_APPIN_LEN                  = 16;               // PIN码长度
    public static final int NET_NETINTERFACE_NAME_LEN          = 260;              // 网口名称长度
    public static final int NET_NETINTERFACE_TYPE_LEN          = 260;             // 网络类型长度
    public static final int NET_MAX_CONNECT_STATUS_LEN         = 260;              // 连接状态字符串长度
    public static final int NET_MAX_MODE_LEN                   = 64;               // 3G支持的网络模式长度
    public static final int NET_MAX_MODE_NUM                   = 64;               // 3G支持的网络模式个数
    public static final int NET_MAX_COMPRESSION_TYPES_NUM      = 16;               // 视频编码格式最多种类个数
    public static final int NET_MAX_CAPTURE_SIZE_NUM           = 64;               // 视频分辨率个数
    public static final int NET_NODE_NAME_LEN                  = 64;               // 组织结构节点名称长度
    public static final int MAX_CALIBPOINTS_NUM                = 256;              // 支持最大标定点数
    public static final int NET_MAX_ATTR_NUM                   = 32;               // 显示单元属性最大数量
    public static final int NET_MAX_CLOUDCONNECT_STATE_LEN     = 128;             // 云注册连接状态信息长度
    public static final int NET_MAX_IPADDR_EX_LEN              = 128;              // 扩展IP地址最大长度
    public static final int MAX_EVENT_NAME                     = 128;              // 最长事件名
    public static final int NET_MAX_ETH_NAME                   = 64;              // 最大网卡名
    public static final int NET_N_SCHEDULE_TSECT               = 8;                // 时间表元素个数    
    public static final int NET_MAX_URL_NUM                    = 8;                // URL最大个数
    public static final int NET_MAX_LOWER_MITRIX_NUM           = 16;               // 最大下位矩阵数
    public static final int NET_MAX_BURN_CHANNEL_NUM           = 32;               // 最大刻录通道数
    public static final int NET_MAX_NET_STRORAGE_BLOCK_NUM     = 64;               // 最大远程存储区块数量
    public static final int NET_MAX_CASE_PERSON_NUM            = 32;               // 案件人员最大数量
    public static final int NET_MAX_MULTIPLAYBACK_CHANNEL_NUM  = 64;               // 最大多通道预览回放通道数
    public static final int NET_MAX_MULTIPLAYBACK_SPLIT_NUM    = 32;               // 最大多通道预览回放分割模式数
    public static final int NET_MAX_AUDIO_ENCODE_TYPE          = 64;               // 最大语音编码类型个数
    public static final int MAX_CARD_RECORD_FIELD_NUM          = 16;               // 卡号录像最大域数量
    public static final int NET_BATTERY_NUM_MAX                = 16;               // 最大电池数量    
    public static final int NET_POWER_NUM_MAX                  = 16;               // 最大电源数量        
    public static final int NET_MAX_AUDIO_PATH                 = 260;              // 最大音频文件路长度
    public static final int NET_MAX_DOORNAME_LEN               = 128;              // 最大门禁名称长度    
    public static final int NET_MAX_CARDPWD_LEN                = 64;               // 最大门禁名称长度    
    public static final int NET_MAX_FISHEYE_MOUNTMODE_NUM      = 4;                // 最大鱼眼安装模式个数
    public static final int NET_MAX_FISHEYE_CALIBRATEMODE_NUM  = 16;               // 最大鱼眼矫正模式个数
    public static final int NET_MAX_FISHEYE_EPTZCMD_NUM        = 64;               // 最大鱼眼电子云台操作个数   
    public static final int POINT_NUM_IN_PAIR                  = 2;                // 标定点对中的点数量
    public static final int MAX_POINT_PAIR_NUM                 = 128;              // 标定点最大数量
    public static final int CHANNEL_NUM_IN_POINT_GROUP         = 2;                // 标定点中的视频通道数
    public static final int MAX_POINT_GROUP_NUM                = 32;               // 标定点组最大数量, 每两个通道进行拼接需要一组标定点
    public static final int MAX_LANE_INFO_NUM                  = 32;               // 最大车道信息数
    public static final int MAX_LANE_DIRECTION_NUM             = 8;                // 车道方向总数
    public static final int NET_MAX_MONITORWALL_NUM            = 32;               // 电视墙最大数量
    public static final int NET_MAX_OPTIONAL_URL_NUM           = 8;                // 备用url最大数量
    public static final int NET_MAX_CAMERA_CHANNEL_NUM         = 1024;             // 最大摄像机通道数
    public static final int MAX_FILE_SUMMARY_NUM               = 32;               // 最大文件摘要数
    public static final int MAX_AUDIO_ENCODE_NUM               = 64;               // 最大支持音频编码个数

    public static final int MAX_FLASH_LIGHT_NUM                = 8;                // 最大支持的爆闪灯(闪光灯)个数
    public static final int MAX_STROBOSCOPIC_LIGHT_NUM         = 8;                // 最大支持的频闪灯个数
    public static final int MAX_MOSAIC_NUM					   = 8;				   // 最大支持的马赛克数量
    public static final int MAX_MOSAIC_CHANNEL_NUM			   = 256;			   // 支持马赛克叠加的最多通道数量
    public static final int MAX_FIREWARNING_INFO_NUM           = 4;                // 最大热成像着火点报警信息个数
    public static final int MAX_AXLE_NUM                       = 8;                // 最大车轴数量
    public static final int MAX_ACCESSDOOR_NUM                 = 128;              // 最大门数量 

    public static final int NET_MAX_BULLET_HOLES               = 10;               // 最大的弹孔数       

    public static final int MAX_NTP_SERVER                     = 4;                // 最大备用NTP服务器地址
    public static final int MAX_PLATE_NUM                      = 64;               // 每张图片中包含的最大车牌个数
    public static final int MAX_PREVIEW_CHANNEL_NUM            = 64;               // 最大导播预览的通道数量 
    public static final int MAX_ADDRESS_LEN                    = 256;              // 最大的地址长度

    public static final int MAX_EVENT_RESTORE_UUID			   = 36;			   // 事件重传uuid数组大小
    public static final int MAX_EVENT_RESTORE_CODE_NUM         = 8;			       // 最大事件重传类型个数
    public static final int MAX_EVENT_RESOTER_CODE_TYPE	       = 32;			   // 事件重传类型数组大小
    public static final int MAX_SNAP_TYPE                      = 3;                // 抓图类型数量
    public static final int MAX_MAINFORMAT_NUM                 = 4;                // 最大支持主码流类型数量

    public static final int CUSTOM_TITLE_LEN				   = 1024;			   // 自定义标题名称长度(扩充到1024)
    public static final int MAX_CUSTOM_TITLE_NUM    		   = 8;                // 编码物件自定义标题最大数量
    public static final int FORMAT_TYPE_LEN					   = 16;			   // 编码类型名最大长度

    public static final int MAX_CHANNEL_NAME_LEN			   = 256;     		   // 通道名称最大长度

    public static final int MAX_VIRTUALINFO_DOMAIN_LEN		   = 64;			   // 虚拟身份上网域名长度
    public static final int MAX_VIRTUALINFO_TITLE_LEN		   = 64;			   // 虚拟身份上网标题长度
    public static final int MAX_VIRTUALINFO_USERNAME_LEN	   = 32;			   // 虚拟身份用户名长度
    public static final int MAX_VIRTUALINFO_PASSWORD_LEN	   = 32;			   // 虚拟身份密码长度
    public static final int MAX_VIRTUALINFO_PHONENUM_LEN	   = 12;			   // 虚拟身份手机号长度
    public static final int MAX_VIRTUALINFO_IMEI_LEN	       = 16;			   // 虚拟身份国际移动设备标识长度
    public static final int MAX_VIRTUALINFO_IMSI_LEN	       = 16;			   // 虚拟身份国际移动用户识别码长度
    public static final int MAX_VIRTUALINFO_LATITUDE_LEN	   = 16;			   // 虚拟身份经度长度
    public static final int MAX_VIRTUALINFO_LONGITUDE_LEN	   = 16;			   // 虚拟身份纬度长度
    public static final int MAX_VIRTUALINFO_NUM				   = 1024;             // 最大虚拟身份信息个数

    public static final int MAX_CALL_ID_LEN					   = 64;			   // 呼叫ID长度
    
    public static final int MAX_REMOTEDEVICEINFO_IPADDR_LEN    = 128;      		   // 远程设备IP地址最大长度
    public static final int MAX_REMOTEDEVICEINFO_USERNAME_LEN  = 128;              // 远程设备用户名最大长度
    public static final int MAX_REMOTEDEVICEINFO_USERPSW_LENGTH = 128;             // 远程设备密码最大长度
    
    // 查询类型,对应CLIENT_QueryDevState接口
    public static final int NET_DEVSTATE_COMM_ALARM            = 0x0001;           // 查询普通报警状态(包括外部报警,视频丢失,动态检测)
    public static final int NET_DEVSTATE_SHELTER_ALARM         = 0x0002;           // 查询遮挡报警状态
    public static final int NET_DEVSTATE_RECORDING             = 0x0003;           // 查询录象状态
    public static final int NET_DEVSTATE_DISK                  = 0x0004;           // 查询硬盘信息
    public static final int NET_DEVSTATE_RESOURCE              = 0x0005;           // 查询系统资源状态
    public static final int NET_DEVSTATE_BITRATE               = 0x0006;           // 查询通道码流
    public static final int NET_DEVSTATE_CONN                  = 0x0007;           // 查询设备连接状态
    public static final int NET_DEVSTATE_PROTOCAL_VER          = 0x0008;           // 查询网络协议版本号,pBuf = int*
    public static final int NET_DEVSTATE_TALK_ECTYPE           = 0x0009;           // 查询设备支持的语音对讲格式列表,见结构体NETDEV_TALKFORMAT_LIST
    public static final int NET_DEVSTATE_SD_CARD               = 0x000A;           // 查询SD卡信息(IPC类产品)
    public static final int NET_DEVSTATE_BURNING_DEV           = 0x000B;           // 查询刻录机信息
    public static final int NET_DEVSTATE_BURNING_PROGRESS      = 0x000C;           // 查询刻录进度
    public static final int NET_DEVSTATE_PLATFORM              = 0x000D;           // 查询设备支持的接入平台
    public static final int NET_DEVSTATE_CAMERA                = 0x000E;           // 查询摄像头属性信息(IPC类产品),pBuf = NETDEV_CAMERA_INFO *,可以有多个结构体
    public static final int NET_DEVSTATE_SOFTWARE              = 0x000F;           // 查询设备软件版本信息  NETDEV_VERSION_INFO
    public static final int NET_DEVSTATE_LANGUAGE              = 0x0010;           // 查询设备支持的语音种类
    public static final int NET_DEVSTATE_DSP                   = 0x0011;           // 查询DSP能力描述(对应结构体NET_DEV_DSP_ENCODECAP)
    public static final int NET_DEVSTATE_OEM                   = 0x0012;           // 查询OEM信息
    public static final int NET_DEVSTATE_NET                   = 0x0013;           // 查询网络运行状态信息
    public static final int NET_DEVSTATE_TYPE                  = 0x0014;           // 查询设备类型
    public static final int NET_DEVSTATE_SNAP                  = 0x0015;           // 查询功能属性(IPC类产品)
    public static final int NET_DEVSTATE_RECORD_TIME           = 0x0016;           // 查询最早录像时间和最近录像时间
    public static final int NET_DEVSTATE_NET_RSSI              = 0x0017;           // 查询无线网络信号强度,见结构体NETDEV_WIRELESS_RSS_INFO
    public static final int NET_DEVSTATE_BURNING_ATTACH        = 0x0018;           // 查询附件刻录选项
    public static final int NET_DEVSTATE_BACKUP_DEV            = 0x0019;           // 查询备份设备列表
    public static final int NET_DEVSTATE_BACKUP_DEV_INFO       = 0x001a;           // 查询备份设备详细信息 NETDEV_BACKUP_INFO
    public static final int NET_DEVSTATE_BACKUP_FEEDBACK       = 0x001b;           // 备份进度反馈
    public static final int NET_DEVSTATE_ATM_QUERY_TRADE       = 0x001c;           // 查询ATM交易类型
    public static final int NET_DEVSTATE_SIP                   = 0x001d;           // 查询sip状态
    public static final int NET_DEVSTATE_VICHILE_STATE         = 0x001e;           // 查询车载wifi状态
    public static final int NET_DEVSTATE_TEST_EMAIL            = 0x001f;           // 查询邮件配置是否成功
    public static final int NET_DEVSTATE_SMART_HARD_DISK       = 0x0020;           // 查询硬盘smart信息
    public static final int NET_DEVSTATE_TEST_SNAPPICTURE      = 0x0021;           // 查询抓图设置是否成功
    public static final int NET_DEVSTATE_STATIC_ALARM          = 0x0022;           // 查询静态报警状态
    public static final int NET_DEVSTATE_SUBMODULE_INFO        = 0x0023;           // 查询设备子模块信息
    public static final int NET_DEVSTATE_DISKDAMAGE            = 0x0024;           // 查询硬盘坏道能力 
    public static final int NET_DEVSTATE_IPC                   = 0x0025;           // 查询设备支持的IPC能力, 见结构体NET_DEV_IPC_INFO
    public static final int NET_DEVSTATE_ALARM_ARM_DISARM      = 0x0026;           // 查询报警布撤防状态
    public static final int NET_DEVSTATE_ACC_POWEROFF_ALARM    = 0x0027;           // 查询ACC断电报警状态(返回一个DWORD, 1表示断电,0表示通电)
    public static final int NET_DEVSTATE_TEST_FTP_SERVER       = 0x0028;           // 测试FTP服务器连接
    public static final int NET_DEVSTATE_3GFLOW_EXCEED         = 0x0029;           // 查询3G流量超出阈值状态,(见结构体 NETDEV_3GFLOW_EXCEED_STATE_INFO)
    public static final int NET_DEVSTATE_3GFLOW_INFO           = 0x002a;           // 查询3G网络流量信息,见结构体 NET_DEV_3GFLOW_INFO
    public static final int NET_DEVSTATE_VIHICLE_INFO_UPLOAD   = 0x002b;           // 车载自定义信息上传(见结构体 ALARM_VEHICLE_INFO_UPLOAD)
    public static final int NET_DEVSTATE_SPEED_LIMIT           = 0x002c;           // 查询限速报警状态(见结构体ALARM_SPEED_LIMIT)
    public static final int NET_DEVSTATE_DSP_EX                = 0x002d;           // 查询DSP扩展能力描述(对应结构体 NET_DEV_DSP_ENCODECAP_EX)
    public static final int NET_DEVSTATE_3GMODULE_INFO         = 0x002e;           // 查询3G模块信息(对应结构体NET_DEV_3GMODULE_INFO)
    public static final int NET_DEVSTATE_MULTI_DDNS            = 0x002f;           // 查询多DDNS状态信息(对应结构体NET_DEV_MULTI_DDNS_INFO)
    public static final int NET_DEVSTATE_CONFIG_URL            = 0x0030;           // 查询设备配置URL信息(对应结构体NET_DEV_URL_INFO)
    public static final int NET_DEVSTATE_HARDKEY               = 0x0031;           // 查询HardKey状态（对应结构体NETDEV_HARDKEY_STATE)
    public static final int NET_DEVSTATE_ISCSI_PATH            = 0x0032;           // 查询ISCSI路径列表(对应结构体NETDEV_ISCSI_PATHLIST)
    public static final int NET_DEVSTATE_DLPREVIEW_SLIPT_CAP   = 0x0033;           // 查询设备本地预览支持的分割模式(对应结构体DEVICE_LOCALPREVIEW_SLIPT_CAP)
    public static final int NET_DEVSTATE_WIFI_ROUTE_CAP        = 0x0034;           // 查询无线路由能力信息(对应结构体NETDEV_WIFI_ROUTE_CAP)
    public static final int NET_DEVSTATE_ONLINE                = 0x0035;           // 查询设备的在线状态(返回一个DWORD, 1表示在线, 0表示断线)
    public static final int NET_DEVSTATE_PTZ_LOCATION          = 0x0036;           // 查询云台状态信息(对应结构体 NET_PTZ_LOCATION_INFO)
    public static final int NET_DEVSTATE_MONITOR_INFO          = 0x0037;           // 画面监控辅助信息(对应结构体NETDEV_MONITOR_INFO)
    public static final int NET_DEVSTATE_SUBDEVICE             = 0x0300;           // 查询子设备(电源, 风扇等)状态(对应结构体CFG_DEVICESTATUS_INFO)
    public static final int NET_DEVSTATE_RAID_INFO             = 0x0038;           // 查询RAID状态(对应结构体ALARM_RAID_INFO)  
    public static final int NET_DEVSTATE_TEST_DDNSDOMAIN       = 0x0039;           // 测试DDNS域名是否可用
    public static final int NET_DEVSTATE_VIRTUALCAMERA         = 0x003a;           // 查询虚拟摄像头状态(对应 NETDEV_VIRTUALCAMERA_STATE_INFO)
    public static final int NET_DEVSTATE_TRAFFICWORKSTATE      = 0x003b;           // 获取设备工作视频/线圈模式状态等(对应NETDEV_TRAFFICWORKSTATE_INFO)
    public static final int NET_DEVSTATE_ALARM_CAMERA_MOVE     = 0x003c;           // 获取摄像机移位报警事件状态(对应ALARM_CAMERA_MOVE_INFO)
    public static final int NET_DEVSTATE_ALARM                 = 0x003e;           // 获取外部报警状态(对应 NET_CLIENT_ALARM_STATE) 
    public static final int NET_DEVSTATE_VIDEOLOST             = 0x003f;           // 获取视频丢失报警状态(对应 NET_CLIENT_VIDEOLOST_STATE) 
    public static final int NET_DEVSTATE_MOTIONDETECT          = 0x0040;           // 获取动态监测报警状态(对应 NET_CLIENT_MOTIONDETECT_STATE)
    public static final int NET_DEVSTATE_DETAILEDMOTION        = 0x0041;           // 获取详细的动态监测报警状态(对应 NET_CLIENT_DETAILEDMOTION_STATE)
    public static final int NET_DEVSTATE_VEHICLE_INFO          = 0x0042;           // 获取车载自身各种硬件信息(对应 NETDEV_VEHICLE_INFO)
    public static final int NET_DEVSTATE_VIDEOBLIND            = 0x0043;           // 获取视频遮挡报警状态(对应 NET_CLIENT_VIDEOBLIND_STATE)
    public static final int NET_DEVSTATE_3GSTATE_INFO          = 0x0044;           // 查询3G模块相关信息(对应结构体NETDEV_VEHICLE_3GMODULE)
    public static final int NET_DEVSTATE_NETINTERFACE          = 0x0045;           // 查询网络接口信息(对应 NETDEV_NETINTERFACE_INFO)

    public static final int NET_DEVSTATE_PICINPIC_CHN          = 0x0046;           // 查询画中画通道号(对应DWORD数组)
    public static final int NET_DEVSTATE_COMPOSITE_CHN         = 0x0047;           // 查询融合屏通道信息(对应DH_COMPOSITE_CHANNEL数组)
    public static final int NET_DEVSTATE_WHOLE_RECORDING       = 0x0048;           // 查询设备整体录像状态(对应BOOL), 只要有一个通道在录像,即为设备整体状态为录像
    public static final int NET_DEVSTATE_WHOLE_ENCODING        = 0x0049;           // 查询设备整体编码状态(对应BOOL), 只要有一个通道在编码,即为设备整体状态为编码
    public static final int NET_DEVSTATE_DISK_RECORDE_TIME     = 0x004a;           // 查询设备硬盘录像时间信息(pBuf = DEV_DISK_RECORD_TIME*,可以有多个结构体)
    public static final int NET_DEVSTATE_BURNER_DOOR           = 0x004b;           // 是否已弹出刻录机光驱门(对应结构体 NET_DEVSTATE_BURNERDOOR)
    public static final int NET_DEVSTATE_GET_DATA_CHECK        = 0x004c;           // 查询光盘数据校验进度(对应 NET_DEVSTATE_DATA_CHECK)
    public static final int NET_DEVSTATE_ALARM_IN_CHANNEL      = 0x004f;           // 查询报警输入通道信息(对应NET_ALARM_IN_CHANNEL数组)
    public static final int NET_DEVSTATE_ALARM_CHN_COUNT       = 0x0050;           // 查询报警通道数(对应NET_ALARM_CHANNEL_COUNT)
    public static final int NET_DEVSTATE_PTZ_VIEW_RANGE        = 0x0051;           // 查询云台可视域状态(对应 NET_OUT_PTZ_VIEW_RANGE_STATUS	)
    public static final int NET_DEVSTATE_DEV_CHN_COUNT         = 0x0052;           // 查询设备通道信息(对应NET_DEV_CHN_COUNT_INFO)
    public static final int NET_DEVSTATE_RTSP_URL              = 0x0053;           // 查询设备支持的RTSP URL列表,见结构体DEV_RTSPURL_LIST
    public static final int NET_DEVSTATE_LIMIT_LOGIN_TIME      = 0x0054;           // 查询设备登录的在线超时时间,返回一个BTYE,（单位：分钟） ,0表示不限制,非零正整数表示限制的分钟数
    public static final int NET_DEVSTATE_GET_COMM_COUNT        = 0x0055;           // 获取串口数 见结构体NET_GET_COMM_COUNT
    public static final int NET_DEVSTATE_RECORDING_DETAIL      = 0x0056;           // 查询录象状态详细信息(pBuf = NET_RECORD_STATE_DETAIL*)
    public static final int NET_DEVSTATE_PTZ_PRESET_LIST       = 0x0057;           // 获取当前云台的预置点列表(对应结构NET_PTZ_PRESET_LIST)
    public static final int NET_DEVSTATE_EXTERNAL_DEVICE       = 0x0058;           // 外接设备信息(pBuf = NET_EXTERNAL_DEVICE*)
    public static final int NET_DEVSTATE_GET_UPGRADE_STATE     = 0x0059;           // 获取设备升级状态(对应结构 NETDEV_UPGRADE_STATE_INFO)
    public static final int NET_DEVSTATE_MULTIPLAYBACK_SPLIT_CAP = 0x005a;         // 获取多通道预览分割能力( 对应结构体 NET_MULTIPLAYBACK_SPLIT_CAP )
    public static final int NET_DEVSTATE_BURN_SESSION_NUM      = 0x005b;           // 获取刻录会话总数(pBuf = int*)
    public static final int NET_DEVSTATE_PROTECTIVE_CAPSULE    = 0X005c;           // 查询防护舱状态(对应结构体ALARM_PROTECTIVE_CAPSULE_INFO)
    public static final int NET_DEVSTATE_GET_DOORWORK_MODE     = 0X005d;           // 获取门锁控制模式( 对应 NET_GET_DOORWORK_MODE)
    public static final int NET_DEVSTATE_PTZ_ZOOM_INFO         = 0x005e;           // 查询云台获取光学变倍信息(对应 NET_OUT_PTZ_ZOOM_INFO )

    public static final int NET_DEVSTATE_POWER_STATE           = 0x0152;           // 查询电源状态(对应结构体NET_POWER_STATUS)
    public static final int NET_DEVSTATE_ALL_ALARM_CHANNELS_STATE  = 0x153;        // 查询报警通道状态(对应结构体 NET_CLIENT_ALARM_CHANNELS_STATE)
    public static final int NET_DEVSTATE_ALARMKEYBOARD_COUNT   = 0x0154;           // 查询串口上连接的报警键盘数(对应结构体NET_ALARMKEYBOARD_COUNT)
    public static final int NET_DEVSTATE_EXALARMCHANNELS       = 0x0155;           // 查询扩展报警模块通道映射关系(对应结构体 NET_EXALARMCHANNELS)
    public static final int NET_DEVSTATE_GET_BYPASS            = 0x0156;           // 查询通道旁路状态(对应结构体 NET_DEVSTATE_GET_BYPASS)
    public static final int NET_DEVSTATE_ACTIVATEDDEFENCEAREA  = 0x0157;           // 获取激活的防区信息(对应结构体 NET_ACTIVATEDDEFENCEAREA)
    public static final int NET_DEVSTATE_DEV_RECORDSET         = 0x0158;           // 查询设备记录集信息(对应 NET_CTRL_RECORDSET_PARAM)
    public static final int NET_DEVSTATE_DOOR_STATE            = 0x0159;           // 查询门禁状态(对应NET_DOOR_STATUS_INFO)
    public static final int NET_DEVSTATE_ANALOGALARM_CHANNELS  = 0x1560;           // 模拟量报警输入通道映射关系(对应NET_ANALOGALARM_CHANNELS)
    public static final int NET_DEVSTATE_GET_SENSORLIST        = 0x1561;           // 获取设备支持的传感器列表(对应 NET_SENSOR_LIST)
    public static final int NET_DEVSTATE_ALARM_CHANNELS        = 0x1562;           // 查询开关量报警模块通道映射关系(对应结构体 NET_ALARM_CHANNELS)
    																			   // 如果设备不支持查询扩展报警模块通道,可以用该功能查询扩展通道的逻辑通道号,并当做本地报警通道使用
    public static final int NET_DEVSTATE_GET_ALARM_SUBSYSTEM_ACTIVATESTATUS = 0x1563;  // 获取当前子系统启用状态( 对应 NET_GET_ALARM_SUBSYSTEM_ACTIVATE_STATUES)
    public static final int NET_DEVSTATE_AIRCONDITION_STATE    = 0x1564;           // 获取空调工作状态(对应 NET_GET_AIRCONDITION_STATE)
    public static final int NET_DEVSTATE_ALARMSUBSYSTEM_STATE  = 0x1565;           // 获取子系统状态(对应NET_ALARM_SUBSYSTEM_STATE)
    public static final int NET_DEVSTATE_ALARM_FAULT_STATE     = 0x1566;           // 获取故障状态(对应 NET_ALARM_FAULT_STATE_INFO)
    public static final int NET_DEVSTATE_DEFENCE_STATE         = 0x1567;           // 获取防区状态(对应 NET_DEFENCE_STATE_INFO, 和旁路状态变化事件、本地报警事件、报警信号源事件的状态描述有区别,不能混用,仅个别设备使用)
    public static final int NET_DEVSTATE_CLUSTER_STATE         = 0x1568;           // 获取集群状态(对应 NET_CLUSTER_STATE_INFO)
    public static final int NET_DEVSTATE_SCADA_POINT_LIST      = 0x1569;           // 获取点位表路径信息(对应 NET_SCADA_POINT_LIST_INFO)
    public static final int NET_DEVSTATE_SCADA_INFO            = 0x156a;           // 获取监测点位信息(对应 NET_SCADA_INFO)
    public static final int NET_DEVSTATE_SCADA_CAPS            = 0X156b;           // 获取SCADA能力集(对应 NET_SCADA_CAPS)
    public static final int NET_DEVSTATE_GET_CODEID_COUNT      = 0x156c;           // 获取对码成功的总条数(对应 NET_GET_CODEID_COUNT)
    public static final int NET_DEVSTATE_GET_CODEID_LIST       = 0x156d;           // 查询对码信息(对应 NET_GET_CODEID_LIST)
    public static final int NET_DEVSTATE_ANALOGALARM_DATA      = 0x156e;           // 查询模拟量通道数据(对应 NET_GET_ANALOGALARM_DATA)
    public static final int NET_DEVSTATE_VTP_CALLSTATE         = 0x156f;           // 获取视频电话呼叫状态(对应 NET_GET_VTP_CALLSTATE)
    public static final int NET_DEVSTATE_SCADA_INFO_BY_ID      = 0x1570;           // 通过设备、获取监测点位信息(对应 NET_SCADA_INFO_BY_ID)
    public static final int NET_DEVSTATE_SCADA_DEVICE_LIST     = 0x1571;           // 获取当前主机所接入的外部设备ID(对应 NET_SCADA_DEVICE_LIST)
    public static final int NET_DEVSTATE_DEV_RECORDSET_EX      = 0x1572;           // 查询设备记录集信息(带二进制数据)(对应NET_CTRL_RECORDSET_PARAM)
    public static final int NET_DEVSTATE_ACCESS_LOCK_VER       = 0x1573;           // 获取门锁软件版本号(对应 NET_ACCESS_LOCK_VER)
    public static final int NET_DEVSTATE_MONITORWALL_TVINFO    = 0x1574;           // 获取电视墙显示信息(对应 NET_CTRL_MONITORWALL_TVINFO)
    public static final int NET_DEVSTATE_GET_ALL_POS           = 0x1575;           // 获取所有用户可用Pos设备配置信息(对应 NET_POS_ALL_INFO)
    public static final int NET_DEVSTATE_GET_ROAD_LIST         = 0x1576;           // 获取城市及路段编码信息,哥伦比亚项目专用(对应 NET_ROAD_LIST_INFO)
    public static final int NET_DEVSTATE_GET_HEAT_MAP          = 0x1577;           // 获取热度统计信息(对应 NET_QUERY_HEAT_MAP)
    public static final int NET_DEVSTATE_GET_WORK_STATE        = 0x1578;           // 获取盒子工作状态信息(对应 NET_QUERY_WORK_STATE )
    public static final int NET_DEVSTATE_GET_WIRESSLESS_STATE  = 0x1579;           // 获取无线设备状态信息(对应 NET_GET_WIRELESS_DEVICE_STATE)
    public static final int NET_DEVSTATE_GET_REDUNDANCE_POWER_INFO = 0x157a;       // 获取冗余电源信息(对应 NET_GET_REDUNDANCE_POWER_INFO)

 
    // 查询设备信息类型, 对应接口 CLIENT_QueryDevInfo
    // 设备信息类型,对应CLIENT_QueryDevInfo接口
    public static final int NET_QUERY_DEV_STORAGE_NAMES                 = 0x01;                // 查询设备的存储模块名列表 , pInBuf=NET_IN_STORAGE_DEV_NAMES *, pOutBuf=NET_OUT_STORAGE_DEV_NAMES *
    public static final int NET_QUERY_DEV_STORAGE_INFOS                 = 0x02;                // 查询设备的存储模块信息列表, pInBuf=NET_IN_STORAGE_DEV_INFOS*, pOutBuf= NET_OUT_STORAGE_DEV_INFOS *
    public static final int NET_QUERY_RECENCY_JNNCTION_CAR_INFO         = 0x03;                // 查询最近的卡口车辆信息接口, pInBuf=NET_IN_GET_RECENCY_JUNCTION_CAR_INFO*, pOutBuf=NET_OUT_GET_RECENCY_JUNCTION_CAR_INFO*
    public static final int NET_QUERY_LANES_STATE                       = 0x04;                // 查询车道信息,pInBuf = NET_IN_GET_LANES_STATE , pOutBuf = NET_OUT_GET_LANES_STATE
    public static final int NET_QUERY_DEV_FISHEYE_WININFO               = 0x05;                // 查询鱼眼窗口信息 , pInBuf= NET_IN_FISHEYE_WININFO*, pOutBuf=NET_OUT_FISHEYE_WININFO *
    public static final int NET_QUERY_DEV_REMOTE_DEVICE_INFO            = 0x06;;               // 查询远程设备信息 , pInBuf= NET_IN_GET_DEVICE_INFO*, pOutBuf= NET_OUT_GET_DEVICE_INFO *
    public static final int NET_QUERY_SYSTEM_INFO                       = 0x07;                // 查询设备系统信息 , pInBuf= NET_IN_SYSTEM_INFO*, pOutBuf= NET_OUT_SYSTEM_INFO*
    public static final int NET_QUERY_REG_DEVICE_NET_INFO               = 0x08;                // 查询主动注册设备的网络连接 , pInBuf=NET_IN_REGDEV_NET_INFO * , pOutBuf=NET_OUT_REGDEV_NET_INFO *
    public static final int NET_QUERY_DEV_THERMO_GRAPHY_PRESET          = 0x09;                // 查询热成像预设信息 , pInBuf= NET_IN_THERMO_GET_PRESETINFO*, pOutBuf= NET_OUT_THERMO_GET_PRESETINFO *
    public static final int NET_QUERY_DEV_THERMO_GRAPHY_OPTREGION       = 0x0a;                // 查询热成像感兴趣区域信息,pInBuf= NET_IN_THERMO_GET_OPTREGION*, pOutBuf= NET_OUT_THERMO_GET_OPTREGION *
    public static final int NET_QUERY_DEV_THERMO_GRAPHY_EXTSYSINFO      = 0x0b;                // 查询热成像外部系统信息, pInBuf= NET_IN_THERMO_GET_EXTSYSINFO*, pOutBuf= NET_OUT_THERMO_GET_EXTSYSINFO *
    public static final int NET_QUERY_DEV_RADIOMETRY_POINT_TEMPER       = 0x0c;                // 查询测温点的参数值, pInBuf= NET_IN_RADIOMETRY_GETPOINTTEMPER*, pOutBuf= NET_OUT_RADIOMETRY_GETPOINTTEMPER *
    public static final int NET_QUERY_DEV_RADIOMETRY_TEMPER             = 0x0d;                // 查询测温项的参数值, pInBuf= NET_IN_RADIOMETRY_GETTEMPER*, pOutBuf= NET_OUT_RADIOMETRY_GETTEMPER *
    public static final int NET_QUERY_GET_CAMERA_STATE                  = 0x0e;                // 获取摄像机状态, pInBuf= NET_IN_GET_CAMERA_STATEINFO*, pOutBuf= NET_OUT_GET_CAMERA_STATEINFO *
    public static final int NET_QUERY_GET_REMOTE_CHANNEL_AUDIO_ENCODE   = 0x0f;                // 获取远程通道音频编码方式, pInBuf= NET_IN_GET_REMOTE_CHANNEL_AUDIO_ENCODEINFO*, pOutBuf= NET_OUT_GET_REMOTE_CHANNEL_AUDIO_ENCODEINFO *
    public static final int NET_QUERY_GET_COMM_PORT_INFO                = 0x10;                // 获取设备串口信息, pInBuf=NET_IN_GET_COMM_PORT_INFO* , pOutBuf=NET_OUT_GET_COMM_PORT_INFO* 
    public static final int NET_QUERY_GET_LINKCHANNELS                  = 0x11;                // 查询某视频通道的关联通道列表,pInBuf=NET_IN_GET_LINKCHANNELS* , pOutBuf=NET_OUT_GET_LINKCHANNELS*
    public static final int NET_QUERY_GET_VIDEOOUTPUTCHANNELS           = 0x12;                // 获取解码通道数量统计信息, pInBuf=NET_IN_GET_VIDEOOUTPUTCHANNELS*, pOutBuf=NET_OUT_GET_VIDEOOUTPUTCHANNELS*
    public static final int NET_QUERY_GET_VIDEOINFO                     = 0x13;                // 获取解码通道信息, pInBuf=NET_IN_GET_VIDEOINFO*, pOutBuf=NET_OUT_GET_VIDEOINFO*
    public static final int NET_QUERY_GET_ALLLINKCHANNELS               = 0x14;                // 查询全部视频关联通道列表,pInBuf=NET_IN_GET_ALLLINKCHANNELS* , pOutBuf=NET_OUT_GET_ALLLINKCHANNELS*
    public static final int NET_QUERY_VIDEOCHANNELSINFO                 = 0x15;                // 查询视频通道信息,pInBuf=NET_IN_GET_VIDEOCHANNELSINFO* , pOutBuf=NET_OUT_GET_VIDEOCHANNELSINFO*
    public static final int NET_QUERY_TRAFFICRADAR_VERSION              = 0x16;                // 查询雷达设备版本,pInBuf=NET_IN_TRAFFICRADAR_VERSION* , pOutBuf=NET_OUT_TRAFFICRADAR_VERSION*
    public static final int NET_QUERY_WORKGROUP_NAMES                   = 0x17;                // 查询所有的工作目录组名,pInBuf=NET_IN_WORKGROUP_NAMES* , pOutBuf=NET_OUT_WORKGROUP_NAMES*
    public static final int NET_QUERY_WORKGROUP_INFO                    = 0x18;                // 查询工作组信息,pInBuf=NET_IN_WORKGROUP_INFO* , pOutBuf=NET_OUT_WORKGROUP_INFO*
    public static final int NET_QUERY_WLAN_ACCESSPOINT                  = 0x19;                // 查询无线网络接入点信息,pInBuf=NET_IN_WLAN_ACCESSPOINT* , pOutBuf=NET_OUT_WLAN_ACCESSPOINT*
    public static final int NET_QUERY_GPS_INFO							= 0x1a;				   // 查询设备GPS信息,pInBuf=NET_IN_DEV_GPS_INFO* , pOutBuf=NET_OUT_DEV_GPS_INFO*
    public static final int NET_QUERY_IVS_REMOTE_DEVICE_INFO            = 0x1b;                // 查询IVS的前端设备所关联的远程设备信息, pInBuf = NET_IN_IVS_REMOTE_DEV_INFO*, pOutBuf = NET_OUT_IVS_REMOTE_DEV_INFO*
   
    /////////////////////////////////// 矩阵 ///////////////////////////////////////

    public static final int NET_MATRIX_INTERFACE_LEN          			= 16;          // 信号接口名称长度
    public static final int NET_MATRIX_MAX_CARDS             			= 128;         // 矩阵子卡最大数量
    public static final int NET_SPLIT_PIP_BASE               			= 1000;        // 分割模式画中画的基础值
    public static final int NET_MAX_SPLIT_MODE_NUM           			= 64;          // 最大分割模式数
    public static final int NET_MATRIX_MAX_CHANNEL_IN        			= 1500;        // 矩阵最大输入通道数
    public static final int NET_MATRIX_MAX_CHANNEL_OUT       			= 256;         // 矩阵最大输出通道数
    public static final int NET_DEVICE_NAME_LEN              			= 64;          // 设备名称长度
    public static final int NET_MAX_CPU_NUM                  			= 16;          // 最大CPU数量
    public static final int NET_MAX_FAN_NUM                  			= 16;          // 最大风扇数量
    public static final int NET_MAX_POWER_NUM                			= 16;          // 最大电源数量
    public static final int NET_MAX_BATTERY_NUM              			= 16;          // 最大电池数量
    public static final int NET_MAX_TEMPERATURE_NUM          			= 256;         // 最大温度传感器数量
    public static final int NET_MAX_ISCSI_NAME_LEN           			= 128;         // ISCSI名称长度
    public static final int NET_VERSION_LEN                  			= 64;          // 版本信息长度
    public static final int NET_MAX_STORAGE_PARTITION_NUM    			= 32;          // 存储分区最大数量
    public static final int NET_STORAGE_MOUNT_LEN            			= 64;          // 挂载点长度
    public static final int NET_STORAGE_FILE_SYSTEM_LEN      			= 16;          // 文件系统名称长度
    public static final int NET_MAX_MEMBER_PER_RAID          			= 32;          // RAID成员最大数量
    public static final int NET_DEV_ID_LEN_EX                			= 128;         // 设备ID最大长度
    public static final int NET_MAX_BLOCK_NUM                			= 32;          // 最大区块数量
    public static final int NET_MAX_SPLIT_WINDOW             			= 128;         // 最大分割窗口数量
    public static final int NET_FILE_TYPE_LEN                			= 64;          // 文件类型长度
    public static final int NET_DEV_ID_LEN                  			= 128;         // 设备ID最大长度
    public static final int NET_DEV_NAME_LEN                 			= 128;         // 设备名称最大长度
    public static final int NET_TSCHE_DAY_NUM                			= 8;           // 时间表第一维大小, 表示天数
    public static final int NET_TSCHE_SEC_NUM                			= 6;           // 时间表第二维大小, 表示时段数
    public static final int NET_SPLIT_INPUT_NUM              			= 256;         // 司法设备二级切换时第一级split支持的输入通道数

    public static final String NET_DEVICE_ID_LOCAL               		= "Local";     // 本地设备ID
    public static final String NET_DEVICE_ID_REMOTE              		= "Remote";    // 远程设备ID
    public static final String NET_DEVICE_ID_UNIQUE             		= "Unique";    // 设备内统一编号
    
    //其他定义
    public static final int NET_MAX_NAME_LEN                    = 16;   // 通用名字字符串长度
    public static final int NET_MAX_PERSON_ID_LEN               = 32;   // 人员id最大长度
    public static final int NET_MAX_PERSON_IMAGE_NUM            = 48;   // 每个人员对应的最大人脸图片数
    public static final int NET_MAX_PROVINCE_NAME_LEN           = 64;   // 省份名称最大长度
    public static final int NET_MAX_CITY_NAME_LEN               = 64;   // 城市名称最大长度
    public static final int NET_MAX_PERSON_NAME_LEN             = 64;   // 人员名字最大长度
    public static final int MAX_FACE_AREA_NUM                   = 8;    // 最大人脸区域个数
    public static final int MAX_PATH                            = 260;
    public static final int MAX_FACE_DB_NUM                     = 8;    // 最大人脸数据库个数
    public static final int MAX_GOURP_NUM                       = 128;  // 人脸库最大个数

    public static final int MAX_FIND_COUNT                      = 20;
    public static final int NET_MAX_POLYGON_NUM                 = 16;   // 多边形最大顶点个数
    public static final int NET_MAX_CANDIDATE_NUM               = 50;   // 人脸识别最大匹配数
    public static final int MAX_POLYGON_NUM                     = 20;   // 视频分析设备区域顶点个数上限
    public static final int MAX_CALIBRATEBOX_NUM                = 10;   // 智能分析校准框个数上限
    public static final int MAX_NAME_LEN                        = 128;  // 通用名字字符串长度
    public static final int MAX_EXCLUDEREGION_NUM               = 10;   // 智能分析检测区域中需要排除的区域个数上限
    public static final int MAX_OBJECT_LIST_SIZE                = 16;   // 视频分析设备支持的检测物体类型列表个数上限
    public static final int MAX_SPECIALDETECT_NUM               = 10;   // 智能分析特殊检测区域上限
    public static final int MAX_OBJECT_ATTRIBUTES_SIZE          = 16;   // 视频分析设备支持的检测物体属性类型列表个数上限
    public static final int MAX_CATEGORY_TYPE_NUMBER            = 128;  // 子类别类型数
    public static final int MAX_ANALYSE_MODULE_NUM              = 16;   // 视频分析设备最大检测模块个数
    public static final int MAX_LOG_PATH_LEN                    = 260;  // 日志路径名最大长度
    public static final int MAX_CHANNELNAME_LEN                 = 64;   // 最大通道名称长度
    public static final int MAX_VIDEO_CHANNEL_NUM               = 256;  // 最大通道数256
    public static final int MAX_PSTN_SERVER_NUM                 = 8;    // 最大报警电话服务器数
    public static final int MAX_TIME_SCHEDULE_NUM               = 8;    // 时间表元素个数
    public static final int MAX_REC_TSECT                       = 6;    // 录像时间段个数
    public static final int MAX_REC_TSECT_EX                    = 10;   // 录像时间段扩展个数
    public static final int MAX_CHANNEL_COUNT                   = 16;
    public static final int MAX_ACCESSCONTROL_NUM               = 8;    // 最大门禁操作的组合数
    public static final int MAX_DBKEY_NUM                       = 64;   // 数据库关键字最大值
    public static final int MAX_SUMMARY_LEN                     = 1024; // 叠加到JPEG图片的摘要信息最大长度
    public static final int WEEK_DAY_NUM                        = 7;    // 一周的天数
    public static final int NET_MAX_FACEDETECT_FEATURE_NUM      = 32;   // 人脸特征最大个数
    public static final int NET_MAX_OBJECT_LIST                 = 16;   // 智能分析设备检测到的物体ID个数上限    
    public static final int NET_MAX_RULE_LIST                   = 16;   // 智能分析设备规则个数上限
    public static final int NET_MAX_DETECT_REGION_NUM           = 20;   // 规则检测区域最大顶点数
    public static final int NET_MAX_DETECT_LINE_NUM             = 20;   // 规则检测线最大顶点数
    public static final int NET_MAX_TRACK_LINE_NUM              = 20;   // 物体运动轨迹最大顶点数
    public static final int NET_MACADDR_LEN                     = 40;   // MAC地址字符串长度
    public static final int NET_DEV_TYPE_LEN                    = 32;   // 设备型号字符串（如"IPC-F725"）长度
    public static final int NET_DEV_SERIALNO_LEN                = 48;   // 序列号字符串长度
    public static final int NET_MAX_URL_LEN                     = 128;  // URL字符串长度
    public static final int NET_MAX_STRING_LEN                  = 128;
    public static final int NET_MACHINE_NAME_NUM                = 64;   // 机器名称长度
    public static final int NET_USER_NAME_LENGTH_EX             = 16;   // 用户名长度
    public static final int NET_USER_NAME_LENGTH                = 8;    // 用户名长度
    public static final int NET_USER_PSW_LENGTH                 = 8;    // 用户密码长度
    public static final int NET_EVENT_NAME_LEN                  = 128;  // 事件名称长度
    public static final int NET_MAX_LANE_NUM                    = 8;    // 视频分析设备每个通道对应车道数上限
    public static final int MAX_DRIVING_DIR_NUM                 = 16;   // 车道行驶方向最大个数
    public static final int FLOWSTAT_ADDR_NAME                  = 16;   // 上下行地点名长
    public static final int NET_MAX_DRIVINGDIRECTION            = 256;  // 行驶方向字符串长度
    public static final int COMMON_SEAT_MAX_NUMBER              = 8;    // 默认检测最大座驾个数
    public static final int NET_MAX_ATTACHMENT_NUM              = 8;    // 最大车辆物件数量
    public static final int NET_MAX_ANNUUALINSPECTION_NUM       = 8;    // 最大年检标识位置
    public static final int NET_MAX_EVENT_PIC_NUM				= 6;    // 最大原始图片张数
    public static final int NET_COMMON_STRING_32                = 32;   // 通用字符串长度32
    public static final int NET_COMMON_STRING_16                = 16;   // 通用字符串长度16
    public static final int NET_COMMON_STRING_64                = 64;   // 通用字符串长度64
    public static final int NET_COMMON_STRING_128               = 128;  // 通用字符串长度128
    public static final int NET_COMMON_STRING_256               = 256;  // 通用字符串长度256
    public static final int NET_COMMON_STRING_512               = 512;  // 通用字符串长度512
    public static final int NET_COMMON_STRING_1024              = 1024; // 通用字符串长度1024
    public static final int NET_COMMON_STRING_2048              = 2048; // 通用字符串长度2048
    public static final int MAX_VIDEOSTREAM_NUM                 = 4;    // 最大码流个数
    public static final int MAX_VIDEO_COVER_NUM                 = 16;   // 最大遮挡区域个数
    public static final int MAX_VIDEO_IN_ZOOM                   = 32;   // 单通道最大变速配置个数
    public static final int NET_EVENT_CARD_LEN                  = 36;   // 卡片名称长度
    public static final int NET_EVENT_MAX_CARD_NUM              = 16;   // 事件上报信息包含最大卡片个数
    public static final int MAX_STATUS_NUM                      = 16;   // 交通设备状态最大个数
    public static final int NET_MAX_CHANMASK 					= 64;   // 通道掩码最大值
    public static final int NET_CHAN_NAME_LEN                   = 32;   // 通道名长度,DVR DSP能力限制,最多32字节 

    public static final int NET_NEW_MAX_RIGHT_NUM               = 1024; // 用户权限个数上限
    public static final int NET_MAX_GROUP_NUM                   = 20;   // 用户组个数上限
    public static final int NET_MAX_USER_NUM                    = 200;  // 用户个数上限
    public static final int NET_RIGHT_NAME_LENGTH               = 32;   // 权限名长度
    public static final int NET_MEMO_LENGTH                     = 32;   // 备注长度
    public static final int NET_NEW_USER_NAME_LENGTH            = 128;  // 用户名长度
    public static final int NET_NEW_USER_PSW_LENGTH             = 128;  // 密码   

    public static final int AV_CFG_Device_ID_Len				= 64;   // 设备ID长度
    public static final int AV_CFG_IP_Address_Len				= 32;   // IP 长度
    public static final int AV_CFG_Protocol_Len 				= 32;   // 协议名长度
    public static final int AV_CFG_User_Name_Len 				= 64;   // 用户名长度
    public static final int	AV_CFG_Password_Len 				= 64;   // 密码长度
    public static final int AV_CFG_Serial_Len					= 32;	// 序列号长度
    public static final int AV_CFG_Device_Class_Len				= 16;   // 设备类型长度
    public static final int AV_CFG_Device_Type_Len				= 32;	// 设备具体型号长度
    public static final int AV_CFG_Device_Name_Len				= 128;	// 机器名称
    public static final int AV_CFG_Address_Len					= 128;	// 机器部署地点
    public static final int AV_CFG_Max_Path						= 260;	// 路径长度
    public static final int AV_CFG_Group_Name_Len               = 64;   // 分组名称长度
    public static final int AV_CFG_DeviceNo_Len                 = 32;   // 设备编号长度
    public static final int AV_CFG_Group_Memo_Len               = 128;  // 分组说明长度
    public static final int AV_CFG_Max_Channel_Num              = 1024; // 最大通道数量
    public static final int MAX_DEVICE_NAME_LEN					= 64;   // 机器名称
    public static final int MAX_DEV_ID_LEN_EX					= 128;  // 设备ID最大长度
    public static final int MAX_PATH_STOR                       = 240;  // 远程目录的长度
    public static final int	MAX_REMOTE_DEV_NUM       			= 256;  // 最大远程设备数量
    public static final int NET_MAX_PLATE_NUMBER_LEN            = 32;   // 车牌字符长度    
    public static final int NET_MAX_AUTHORITY_LIST_NUM          = 16;   // 权限列表最大个数    
    public static final int NET_MAX_ALARMOUT_NUM_EX 			= 32;   //报警输出口个数上限扩展
    public static final int NET_MAX_VIDEO_IN_NUM_EX 			= 32;   //视频输入口个数上限扩展
    public static final int NET_MAX_SAERCH_IP_NUM               = 256;  // 最大搜索IP个数
    public static final int NET_MAX_POS_MAC_NUM                 = 8;    // 刷卡机Mac码最大长度
    public static final int NET_MAX_BUSCARD_NUM                 = 64;   // 公交卡号最大长度
    public static final int NET_STORAGE_NAME_LEN                = 128;  // 存储设备名称长度
    
    public static final int NET_MAX_DOOR_NUM               		= 32;   // 最大有权限门禁数目
    public static final int NET_MAX_TIMESECTION_NUM        		= 32;   // 最大有效时间段数目
    public static final int NET_MAX_CARDNAME_LEN           		= 64;   // 门禁卡命名最大长度
    public static final int NET_MAX_CARDNO_LEN             		= 32;   // 门禁卡号最大长度
    public static final int NET_MAX_USERID_LEN             		= 32;   // 门禁卡用户ID最大长度
    public static final int NET_MAX_IC_LEN				  		= 32;	// 身份证最大长度
    public static final int NET_MAX_QRCODE_LEN			  		= 128;	// QRCode 最大长度
    public static final int NET_MAX_CARD_INFO_LEN               = 256;  // 卡号信息最大长度
    public static final int NET_MAX_SIM_LEN                     = 16;   // SIM卡的值的最大长度
    public static final int NET_MAX_DISKNUM                     = 256;  // 最大硬盘个数
    public static final int MAX_FACE_DATA_NUM                	= 20;   // 人脸模版最大个数
    public static final int MAX_FINGERPRINT_NUM              	= 10;   // 最大指纹个数
    public static final int MAX_FACE_DATA_LEN                   = 2 * 1024; // 人脸模版数据最大长度
    public static final int MAX_COMMON_STRING_8              	= 8;    // 通用字符串长度8
    public static final int MAX_COMMON_STRING_16             	= 16;   // 通用字符串长度16
    public static final int MAX_COMMON_STRING_32             	= 32;   // 通用字符串长度32
    public static final int MAX_COMMON_STRING_64             	= 64;   // 通用字符串长度64
    public static final int MAX_USER_NAME_LEN                   = 128;  // 最大用户名长度
    public static final int MAX_ROOMNUM_COUNT					= 32;	// 房间最大个数
    public static final int MAX_FACE_COUTN						= 20;	// 人脸模板数据最大个数
    
    public static final int CFG_COMMON_STRING_16                = 16;   // 通用字符串长度16
    public static final int CFG_COMMON_STRING_32                = 32;   // 通用字符串长度16
    public static final int CFG_COMMON_STRING_256               = 256;  // 通用字符串长度256
    
    public static final int MAX_COILCONFIG          			= 3;    // 智能交通车检器线圈配置上限
    public static final int MAX_DETECTOR            			= 6;    // 智能交通车检器配置上限
    public static final int MAX_VIOLATIONCODE					= 16;   // 智能交通违章代码长度上限
    public static final int MAX_LANE_CONFIG_NUMBER              = 32;   // 车道最大个数
    public static final int MAX_VIOLATIONCODE_DESCRIPT          = 64;   // 智能交通违章代码长度上限
    public static final int MAX_ROADWAYNO           			= 128;  // 道路编号	由32个数字和字母构成
    public static final int MAX_PRIORITY_NUMBER                 = 256;  // 违章优先级包含违章最大个数
    public static final int MAX_DRIVINGDIRECTION          		= 256;  // 行驶方向字符串长度
    
    public static final int MAX_OSD_CUSTOM_SORT_NUM       		= 8;
    public static final int MAX_OSD_CUSTOM_SORT_ELEM_NUM  		= 8;
    public static final int MAX_OSD_CUSTOM_GENERAL_NUM    		= 8;
    public static final int MAX_OSD_ITEM_ATTR_NUM         		= 8;
    public static final int MAX_PRE_POX_STR_LEN           		= 32;
    public static final int MAX_OSD_CUSTOM_NAME_LEN       		= 32;
    public static final int MAX_OSD_CUSTOM_VALUE_LEN      		= 256;
    public static final int MAX_CONF_CHAR 						= 256;
    public static final int MAX_IVS_EVENT_NUM    				= 256;
    public static final int MAX_QUERY_USER_NUM   				= 4;    // 最大查询用户个数
    public static final int MAX_DEVICE_ADDRESS      			= 256;  // TrafficSnapshot智能交通设备地址
    public static final int MAX_STORAGE_NUM                 	= 8;    // 存储设备最大个数
    public static final int MAX_PARTITION_NUM               	= 8;    // 最大分区个数
    public static final int MAX_SCADA_POINT_LIST_INFO_NUM       = 256;  // 最大点位表路径个数
    public static final int MAX_SCADA_POINT_LIST_ALARM_INFO_NUM = 256;  // 最大点位表报警个数
    public static final int	MAX_LABEL_ARRAY						= 1024;
    
    public static final int	MAX_DELIVERY_FILE_NUM  				= 128;  // 最大投放文件数量
    public static final int	DELIVERY_FILE_URL_LEN  				= 128;  // 投放文件的URL长度
    
    public static final int MAX_COMMON_STRING_512               = 512;  // 通用字符串长度512
    public static final int	MAX_RFIDELETAG_CARDID_LEN		    = 16;	// RFID 电子车牌标签信息中卡号最大长度
    public static final int	MAX_RFIDELETAG_DATE_LEN		 	    = 16;	// RFID 电子车牌标签信息中时间最大长度
    public static final int MAX_REPEATENTERROUTE_NUM   			= 12;   //反潜路径个数  
    public static final int ECK_SCREEN_NUM_MAX                  = 8;    // 智能停车系统出入口机最大屏数量

    public static final int	NET_COUNTRY_LENGTH				    = 3;	// 国家缩写长度
    
    // 矩阵子卡类型, 多种类型可以组合
    public static final int NET_MATRIX_CARD_MAIN                = 0x10000000;   // 主卡
    public static final int NET_MATRIX_CARD_INPUT               = 0x00000001;   // 输入卡
    public static final int NET_MATRIX_CARD_OUTPUT              = 0x00000002;   // 输出卡
    public static final int NET_MATRIX_CARD_ENCODE              = 0x00000004;   // 编码卡
    public static final int NET_MATRIX_CARD_DECODE              = 0x00000008;   // 解码卡
    public static final int NET_MATRIX_CARD_CASCADE             = 0x00000010;   // 级联卡
    public static final int NET_MATRIX_CARD_INTELLIGENT         = 0x00000020;   // 智能卡
    public static final int NET_MATRIX_CARD_ALARM               = 0x00000040;   // 报警卡
    public static final int NET_MATRIX_CARD_RAID                = 0x00000080;   // 硬Raid卡
    public static final int NET_MATRIX_CARD_NET_DECODE          = 0x00000100;   // 网络解码卡
    
    public static final int RESERVED_TYPE_FOR_INTEL_BOX 		= 0x00000001;
    public static final int RESERVED_TYPE_FOR_COMMON   			= 0x00000010;
    
    /************************************************************************
     ** 结构体
     ***********************************************************************/
    // 设置登入时的相关参数
    public static class NET_PARAM  extends Structure
    {
        public int                    nWaittime;                // 等待超时时间(毫秒为单位)，为0默认5000ms
        public int                    nConnectTime;            	// 连接超时时间(毫秒为单位)，为0默认1500ms
        public int                    nConnectTryNum;           // 连接尝试次数，为0默认1次
        public int                    nSubConnectSpaceTime;    	// 子连接之间的等待时间(毫秒为单位)，为0默认10ms
        public int                    nGetDevInfoTime;        	// 获取设备信息超时时间，为0默认1000ms
        public int                    nConnectBufSize;        	// 每个连接接收数据缓冲大小(字节为单位)，为0默认250*1024
        public int                    nGetConnInfoTime;         // 获取子连接信息超时时间(毫秒为单位)，为0默认1000ms
        public int                    nSearchRecordTime;      	// 按时间查询录像文件的超时时间(毫秒为单位),为0默认为3000ms
        public int                    nsubDisconnetTime;      	// 检测子链接断线等待时间(毫秒为单位)，为0默认为60000ms
        public byte                   byNetType;                // 网络类型, 0-LAN, 1-WAN
        public byte                   byPlaybackBufSize;      	// 回放数据接收缓冲大小（M为单位），为0默认为4M
        public byte                   bDetectDisconnTime;       // 心跳检测断线时间(单位为秒),为0默认为60s,最小时间为2s
        public byte                   bKeepLifeInterval;        // 心跳包发送间隔(单位为秒),为0默认为10s,最小间隔为2s
        public int                    nPicBufSize;              // 实时图片接收缓冲大小（字节为单位），为0默认为2*1024*1024
        public byte[]                 bReserved = new byte[4];  // 保留字段字段
    }
    
    // 设备信息
    public static class NET_DEVICEINFO extends Structure {
        public byte[]              sSerialNumber = new byte[NET_SERIALNO_LEN];    // 序列号
        public byte                byAlarmInPortNum;         // DVR报警输入个数
        public byte                byAlarmOutPortNum;        // DVR报警输出个数
        public byte                byDiskNum;                // DVR硬盘个数
        public byte                byDVRType;                // DVR类型, 见枚举NET_DEV_DEVICE_TYPE
        public union 			   union = new union();
        public static class union extends Union {
            public byte                byChanNum;                // DVR通道个数
            public byte                byLeftLogTimes;           // 当登陆失败原因为密码错误时,通过此参数通知用户,剩余登陆次数,为0时表示此参数无效
        }
    }
    
    // 设备信息扩展///////////////////////////////////////////////////
    public static class NET_DEVICEINFO_Ex extends Structure {
    	 public byte[]     sSerialNumber = new byte[NET_SERIALNO_LEN];    // 序列号
    	 public int        byAlarmInPortNum;                              // DVR报警输入个数
    	 public int        byAlarmOutPortNum;                             // DVR报警输出个数
    	 public int        byDiskNum;                                     // DVR硬盘个数
    	 public int        byDVRType;                                     // DVR类型,见枚举NET_DEVICE_TYPE
    	 public int        byChanNum;                                     // DVR通道个数
    	 public byte       byLimitLoginTime;                              // 在线超时时间,为0表示不限制登陆,非0表示限制的分钟数
    	 public byte       byLeftLogTimes;                                // 当登陆失败原因为密码错误时,通过此参数通知用户,剩余登陆次数,为0时表示此参数无效
    	 public byte[]     bReserved = new byte[2];                       // 保留字节,字节对齐
    	 public int        byLockLeftTime;                                // 当登陆失败,用户解锁剩余时间（秒数）, -1表示设备未设置该参数
    	 public byte[]     Reserved = new byte[24];                       // 保留
    }
    
    // 对应接口 CLIENT_LoginEx2/////////////////////////////////////////////////////////
    public static class EM_LOGIN_SPAC_CAP_TYPE extends Structure {
    	public static final int EM_LOGIN_SPEC_CAP_TCP               = 0;    // TCP登陆, 默认方式
    	public static final int EM_LOGIN_SPEC_CAP_ANY               = 1;    // 无条件登陆
    	public static final int EM_LOGIN_SPEC_CAP_SERVER_CONN       = 2;    // 主动注册的登入
    	public static final int	EM_LOGIN_SPEC_CAP_MULTICAST         = 3;    // 组播登陆
    	public static final int	EM_LOGIN_SPEC_CAP_UDP               = 4;    // UDP方式下的登入
    	public static final int	EM_LOGIN_SPEC_CAP_MAIN_CONN_ONLY    = 6;    // 只建主连接下的登入
    	public static final int	EM_LOGIN_SPEC_CAP_SSL               = 7;    // SSL加密方式登陆

    	public static final int	EM_LOGIN_SPEC_CAP_INTELLIGENT_BOX   = 9;    // 登录智能盒远程设备
    	public static final int	EM_LOGIN_SPEC_CAP_NO_CONFIG         = 10;   // 登录设备后不做取配置操作
    	public static final int EM_LOGIN_SPEC_CAP_U_LOGIN           = 11;   // 用U盾设备的登入
    	public static final int	EM_LOGIN_SPEC_CAP_LDAP              = 12;   // LDAP方式登录
    	public static final int EM_LOGIN_SPEC_CAP_AD                = 13;   // AD（ActiveDirectory）登录方式
    	public static final int EM_LOGIN_SPEC_CAP_RADIUS            = 14;   // Radius 登录方式 
    	public static final int EM_LOGIN_SPEC_CAP_SOCKET_5          = 15;   // Socks5登陆方式
    	public static final int EM_LOGIN_SPEC_CAP_CLOUD             = 16;   // 云登陆方式
    	public static final int EM_LOGIN_SPEC_CAP_AUTH_TWICE        = 17;   // 二次鉴权登陆方式
    	public static final int EM_LOGIN_SPEC_CAP_TS                = 18;   // TS码流客户端登陆方式
    	public static final int	EM_LOGIN_SPEC_CAP_P2P               = 19;   // 为P2P登陆方式
    	public static final int	EM_LOGIN_SPEC_CAP_MOBILE            = 20;   // 手机客户端登陆
    }
    
    // 时间
    public static class NET_TIME extends Structure {
        public int                dwYear;                   // 年
        public int                dwMonth;                  // 月
        public int                dwDay;                    // 日
        public int                dwHour;                   // 时
        public int                dwMinute;                 // 分
        public int                dwSecond;                 // 秒
        
        public NET_TIME() {
            this.dwYear = 0;
            this.dwMonth = 0;
            this.dwDay = 0;
            this.dwHour = 0;
            this.dwMinute = 0;
            this.dwSecond = 0;
        }
        
        public void setTime(int year, int month, int day, int hour, int minute, int second) {
        	this.dwYear = year;
        	this.dwMonth= month;
        	this.dwDay= day;
        	this.dwHour=hour;
        	this.dwMinute=minute;
        	this.dwSecond=second;
        }
        
        public NET_TIME(NET_TIME other) {
            this.dwYear = other.dwYear;
            this.dwMonth = other.dwMonth;
            this.dwDay = other.dwDay;
            this.dwHour = other.dwHour;
            this.dwMinute = other.dwMinute;
            this.dwSecond = other.dwSecond;
        }
        
        //用于列表中显示
        public String toStringTime() {
            return  String.format("%02d/%02d/%02d %02d:%02d:%02d", dwYear, dwMonth, dwDay, dwHour, dwMinute, dwSecond);
        }
        
        public String toString() {
        	return String.format("%02d%02d%02d%02d%02d%02d", dwYear, dwMonth, dwDay, dwHour, dwMinute, dwSecond);
        }
    }

    public static class NET_TIME_EX extends Structure 
    {
        public int                dwYear;                    // 年
        public int                dwMonth;                   // 月
        public int                dwDay;                     // 日
        public int                dwHour;                    // 时
        public int                dwMinute;                  // 分
        public int                dwSecond;                  // 秒
        public int              dwMillisecond;               // 毫秒
        public int[]            dwReserved = new int[2];     // 保留字段
        
        public String toString() {
            return dwYear + "/" + dwMonth + "/" + dwDay + " " + dwHour + ":" + dwMinute + ":" + dwSecond;
        }

        //用于列表中显示
        public String toStringTime()
        {
            return  String.format("%02d/%02d/%02d %02d:%02d:%02d", dwYear, dwMonth, dwDay, dwHour, dwMinute, dwSecond);
        }

        //存储文件名使用
         public String toStringTitle()
        {
            return  String.format("Time_%02d%02d%02d_%02d%02d%02d", dwYear, dwMonth, dwDay, dwHour, dwMinute, dwSecond);
        }
    }
    
    // 区域
    public static class  NET_CFG_Rect extends Structure
    {
        public int            nStructSize;
        public int            nLeft;
        public int            nTop;
        public int            nRight;
        public int            nBottom;    
        
        public NET_CFG_Rect()
        {
            this.nStructSize = this.size();
        }
    }
    
    // 颜色
    public static class  NET_CFG_Color extends Structure
    {
        public int            nStructSize;
        public int            nRed;                            		// 红
        public int            nGreen;                            	// 绿
        public int            nBlue;                            	// 蓝
        public int            nAlpha;                            	// 透明
        
        public NET_CFG_Color()
        {
            this.nStructSize = this.size();
        }
    }

    // 编码物件-通道标题
    public static class  NET_CFG_VideoWidgetChannelTitle extends Structure
    {
        public int                nStructSize;
        public int            	  bEncodeBlend;                    // 叠加到主码流, 类型为BOOL, 取值0或者1
        public int            	  bEncodeBlendExtra1;              // 叠加到辅码流1, 类型为BOOL, 取值0或者1
        public int                bEncodeBlendExtra2;              // 叠加到辅码流2, 类型为BOOL, 取值0或者1
        public int            	  bEncodeBlendExtra3;              // 叠加到辅码流3, 类型为BOOL, 取值0或者1
        public int            	  bEncodeBlendSnapshot;            // 叠加到抓图, 类型为BOOL, 取值0或者1
        public NET_CFG_Color      stuFrontColor = new NET_CFG_Color();    // 前景色
        public NET_CFG_Color      stuBackColor = new NET_CFG_Color();    // 背景色
        public NET_CFG_Rect       stuRect = new NET_CFG_Rect();        // 区域, 坐标取值0~8191, 仅使用left和top值, 点(left,top)应和(right,bottom)设置成同样的点
        public int            bPreviewBlend;                    // 叠加到预览视频, 类型为BOOL， 取值0或者1
        
        public NET_CFG_VideoWidgetChannelTitle()
        {
            this.nStructSize = this.size();
        }
    }

    // 编码物件-时间标题
    public static class  NET_CFG_VideoWidgetTimeTitle extends Structure
    {
        public int                nStructSize;
        public int            bEncodeBlend;                        // 叠加到主码流, 类型为BOOL, 取值0或者1
        public int            bEncodeBlendExtra1;                    // 叠加到辅码流1, 类型为BOOL, 取值0或者1
        public int            bEncodeBlendExtra2;                    // 叠加到辅码流2, 类型为BOOL, 取值0或者1
        public int            bEncodeBlendExtra3;                    // 叠加到辅码流3, 类型为BOOL, 取值0或者1
        public int            bEncodeBlendSnapshot;                // 叠加到抓图, 类型为BOOL, 取值0或者1
        public NET_CFG_Color        stuFrontColor = new NET_CFG_Color();    // 前景色
        public NET_CFG_Color        stuBackColor = new NET_CFG_Color();    // 背景色
        public NET_CFG_Rect        stuRect = new NET_CFG_Rect();        // 区域, 坐标取值0~8191, 仅使用left和top值, 点(left,top)应和(right,bottom)设置成同样的点
        public int            bShowWeek;                            // 是否显示星期, 类型为BOOL, 取值0或者1
        public int            bPreviewBlend;                        // 叠加到预览视频, 类型为BOOL, 取值0或者1
        
        public NET_CFG_VideoWidgetTimeTitle()
        {
            this.nStructSize = this.size();
        }
    }
    
    // 编码物件-区域覆盖配置
    public static class  NET_CFG_VideoWidgetCover extends Structure
    {
        public int                nStructSize;    
        public int            bEncodeBlend;                    // 叠加到主码流, 类型为BOOL, 取值0或者1
        public int            bEncodeBlendExtra1;                // 叠加到辅码流1, 类型为BOOL, 取值0或者1
        public int            bEncodeBlendExtra2;                // 叠加到辅码流2, 类型为BOOL, 取值0或者1
        public int            bEncodeBlendExtra3;                // 叠加到辅码流3, 类型为BOOL, 取值0或者1
        public int            bEncodeBlendSnapshot;            // 叠加到抓图, 类型为BOOL, 取值0或者1
        public NET_CFG_Color        stuFrontColor = new NET_CFG_Color();        // 前景色
        public NET_CFG_Color        stuBackColor = new NET_CFG_Color();        // 背景色
        public NET_CFG_Rect        stuRect = new NET_CFG_Rect();            // 区域, 坐标取值0~8191
        public int            bPreviewBlend;                    // 叠加到预览视频, 类型为BOOL, 取值0或者1
        
        public NET_CFG_VideoWidgetCover()
        {
            this.nStructSize = this.size();
        }
    }
    
    public class EM_TITLE_TEXT_ALIGN
    {
        public static final int EM_TEXT_ALIGN_INVALID         = 0;     // 无效的对齐方式
        public static final int EM_TEXT_ALIGN_LEFT            = 1;       // 左对齐
        public static final int EM_TEXT_ALIGN_XCENTER        = 2;    // X坐标中对齐
        public static final int EM_TEXT_ALIGN_YCENTER        = 3;    // Y坐标中对齐
        public static final int EM_TEXT_ALIGN_CENTER        = 4;      // 居中
        public static final int EM_TEXT_ALIGN_RIGHT            = 5;       // 右对齐
        public static final int EM_TEXT_ALIGN_TOP            = 6;       // 按照顶部对齐
        public static final int EM_TEXT_ALIGN_BOTTOM        = 7;     // 按照底部对齐
        public static final int EM_TEXT_ALIGN_LEFTTOP        = 8;    // 按照左上角对齐
        public static final int EM_TEXT_ALIGN_CHANGELINE    = 9;      // 换行对齐
    }

    // 编码物件-自定义标题
    public static class  NET_CFG_VideoWidgetCustomTitle extends Structure
    {
        public int                nStructSize;
        public int            bEncodeBlend;                        // 叠加到主码流, 类型为BOOL, 取值0或者1 
        public int            bEncodeBlendExtra1;                    // 叠加到辅码流1, 类型为BOOL, 取值0或者1
        public int            bEncodeBlendExtra2;                    // 叠加到辅码流2, 类型为BOOL, 取值0或者1
        public int            bEncodeBlendExtra3;                    // 叠加到辅码流3, 类型为BOOL, 取值0或者1
        public int            bEncodeBlendSnapshot;                // 叠加到抓图, 类型为BOOL, 取值0或者1
        public NET_CFG_Color        stuFrontColor = new NET_CFG_Color();    // 前景色
        public NET_CFG_Color        stuBackColor = new NET_CFG_Color();    // 背景色
        public NET_CFG_Rect        stuRect = new NET_CFG_Rect();        // 区域, 坐标取值0~8191, 仅使用left和top值, 点(left,top)应和(right,bottom)设置成同样的点
        public byte[]            szText = new byte[NET_CFG_Custom_Title_Len];// 标题内容
        public int            bPreviewBlend;                    // 叠加到预览视频, 类型为BOOL, 取值0或者1
        public byte[]           szType = new byte[NET_CFG_Custom_TitleType_Len];// 标题类型 "Rtinfo" 实时刻录信息 "Custom" 自定义叠加、温湿度叠加 "Title" :片头信息 "Check"  校验码
                                                                // 地理信息 "Geography"  ATM卡号信息 "ATMCardInfo" 摄像机编号 "CameraID" 
        public int                  emTextAlign;                    // 标题对齐方式 (参见EM_TITLE_TEXT_ALIGN)
        
        public NET_CFG_VideoWidgetCustomTitle()
        {
            this.nStructSize = this.size();
        }
    }
    
    //  编码物件-叠加传感器信息-叠加内容描述
    public static class  NET_CFG_VideoWidgetSensorInfo_Description extends Structure
    {
        public int            nStructSize;
        public int            nSensorID;                        // 需要描述的传感器的ID(即模拟量报警通道号)
        public byte[]         szDevID =  new byte[CFG_COMMON_STRING_32];  // 设备ID
        public byte[]         szPointID = new byte[CFG_COMMON_STRING_32];// 测点ID
        public byte[]         szText = new byte[CFG_COMMON_STRING_256];  // 需要叠加的内容
        public NET_CFG_VideoWidgetSensorInfo_Description()
        {
            this.nStructSize = this.size();
        }
    }

    // 编码物件-叠加传感器信息
    public static class  NET_CFG_VideoWidgetSensorInfo extends Structure
    {
        public int            nStructSize;
        public int        bPreviewBlend;                    // 叠加到预览视频, 类型为BOOL, 取值0或者1
        public int        bEncodeBlend;                    // 叠加到主码流视频编码, 类型为BOOL, 取值0或者1
        public NET_CFG_Rect    stuRect = new NET_CFG_Rect();                        // 区域, 坐标取值0~8191
        public int            nDescriptionNum;                // 叠加区域描述数目
        public NET_CFG_VideoWidgetSensorInfo_Description[]  stuDescription = (NET_CFG_VideoWidgetSensorInfo_Description[])new NET_CFG_VideoWidgetSensorInfo_Description().toArray(NET_CFG_Max_Description_Num);// 叠加区域描述信息
        
        public NET_CFG_VideoWidgetSensorInfo()
        {
            this.nStructSize = this.size();
        }
    }

    // 视频编码物件配置
    public static class NET_CFG_VideoWidget extends Structure
    {
        public int                              nStructSize;
        public NET_CFG_VideoWidgetChannelTitle  stuChannelTitle = new NET_CFG_VideoWidgetChannelTitle();        // 通道标题
        public NET_CFG_VideoWidgetTimeTitle     stuTimeTitle = new NET_CFG_VideoWidgetTimeTitle();            // 时间标题
        public int                              nConverNum;              // 区域覆盖数量
        public NET_CFG_VideoWidgetCover[]       stuCovers = new NET_CFG_VideoWidgetCover[NET_CFG_Max_Video_Widget_Cover];                        // 覆盖区域
        public int                              nCustomTitleNum;         // 自定义标题数量
        public NET_CFG_VideoWidgetCustomTitle[] stuCustomTitle = new NET_CFG_VideoWidgetCustomTitle[NET_CFG_Max_Video_Widget_Custom_Title];    // 自定义标题
        public int                              nSensorInfo;             // 传感器信息叠加区域数目
        public NET_CFG_VideoWidgetSensorInfo[]  stuSensorInfo = new NET_CFG_VideoWidgetSensorInfo[NET_CFG_Max_Video_Widget_Sensor_Info];        // 传感器信息叠加区域信息
        public double                           fFontSizeScale;          //叠加字体大小放大比例
                                                                         //当fFontSizeScale≠0时,nFontSize不起作用
                                                                         //当fFontSizeScale=0时,nFontSize起作用
                                                                         //设备默认fFontSizeScale=1.0
                                                                         //如果需要修改倍数，修改该值
                                                                         //如果需要按照像素设置，则置该值为0，nFontSize的值生效
        public int                               nFontSize;              //叠加到主码流上的全局字体大小,单位 px.
                                                                         //和fFontSizeScale共同作用
        public int                               nFontSizeExtra1;        //叠加到辅码流1上的全局字体大小,单位 px
        public int                               nFontSizeExtra2;        //叠加到辅码流2上的全局字体大小,单位 px
        public int                               nFontSizeExtra3;        //叠加到辅码流3上的全局字体大小,单位 px
        public int                               nFontSizeSnapshot;      //叠加到抓图流上的全局字体大小, 单位 px
        public int                               nFontSizeMergeSnapshot; //叠加到抓图流上合成图片的字体大小,单位 px
        
        public NET_CFG_VideoWidget()
        {
            this.nStructSize = this.size();
            for (int i = 0; i < stuCustomTitle.length; i++) {
            	stuCustomTitle[i] = new NET_CFG_VideoWidgetCustomTitle();
			}
            
            for (int i = 0; i < stuCovers.length; i++) {
            	stuCovers[i] = new NET_CFG_VideoWidgetCover();
			}
            
            for (int i = 0; i < stuSensorInfo.length; i++) {
            	stuSensorInfo[i] = new NET_CFG_VideoWidgetSensorInfo();
			}
        }
    }
    
    // 报警事件类型 NET_EVENT_VIDEOABNORMALDETECTION 对应的数据描述信息
    public static class ALARM_VIDEOABNORMAL_DETECTION_INFO extends Structure
    {
        public int          dwSize;    
        public int          nChannelID;                     // 通道号
        public double       PTS;                            // 时间戳(单位是毫秒)
        public NET_TIME_EX  UTC;                            // 事件发生的时间
        public int             nEventID;                       // 事件ID
        public int          nEventAction;                   // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public int          nType;                          // 检测类型,0-视频丢失, 1-视频遮挡, 2-画面冻结, 3-过亮, 4-过暗, 5-场景变化
                                                            // 6-条纹检测 , 7-噪声检测 , 8-偏色检测 , 9-视频模糊检测 , 10-对比度异常检测
                                                            // 11-视频运动, 12-视频闪烁, 13-视频颜色, 14-虚焦检测, 15-过曝检测
        public int          nValue;                         // 检测值,值越高表示视频质量越差, GB30147定义
        public int          nOccurrenceCount;               // 规则被触发生次数
        
        public ALARM_VIDEOABNORMAL_DETECTION_INFO()
        {
            this.dwSize = this.size();
        }
    }
    
    // 停车发卡刷卡类型
    public static class NET_PARKING_CARD_TYPE extends Structure
    {
        public static final int NET_PARKING_CARD_TYPE_UNKNOWN = 0;
        public static final int NET_PARKING_CARD_TYPE_SEND = 1;   // 发卡
        public static final int NET_PARKING_CARD_TYPE_DETECT = 2; // 刷卡
    }
    
    // 报警事件类型 NET_ALARM_PARKING_CARD (停车刷卡事件)对应的数据描述信息
    public static class ALARM_PARKING_CARD extends Structure {
    	public int                   dwSize;
    	public int   				 emType;                       // 类型, 参考 NET_PARKING_CARD_TYPE
        public int                   dwCardNo;                     // 卡号
        public byte[]                szPlate = new byte[NET_COMMON_STRING_16]; // 车牌
        
        public ALARM_PARKING_CARD() {
        	this.dwSize = this.size();
        }
    }

    // 报警事件类型 NET_ALARM_NEW_FILE 对应的数据描述信息
    public static class ALARM_NEW_FILE_INFO extends Structure
    {
        public int      dwSize;
        public int      nChannel;                           // 抓图通道号
        public int      nEventID;                           // 事件ID
        public int      dwEvent;                            // 事件类型
        public int      FileSize;                           // 文件大小,单位是字节
        public int      nIndex;                             // 事件源通道
        public int      dwStorPoint;                        // 存储点
        public byte[]   szFileName = new byte[128];         // 文件名
        
        public ALARM_NEW_FILE_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 人数越上限类型
    public static class EM_UPPER_LIMIT_TYPE extends Structure
    {
        public static final int EM_UPPER_LIMIT_TYPE_UNKNOWN     = 0;  
        public static final int EM_UPPER_LIMIT_TYPE_ENTER_OVER  = 1; // 进入越上限
        public static final int EM_UPPER_LIMIT_TYPE_EXIT_OVER   = 2; // 出来越上限
        public static final int EM_UPPER_LIMIT_TYPE_INSIDE_OVER = 3; // 内部越上限    
    }
    

    // 事件类型 NET_ALARM_HUMAM_NUMBER_STATISTIC (人数量/客流量统计事件NumberStat对应的数据描述信息)
    public static class  ALARM_HUMAN_NUMBER_STATISTIC_INFO extends Structure
    {
        public double              PTS;                            // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                            // 事件发生的时间
        public int                 nEventAction;                   // 事件动作,0-事件持续, 1-表示事件开始, 2-表示事件结束;
        public int                 nNumber;                        // 区域内物体的个数
        public int                 nEnteredNumber;                 // 进入区域或者出入口内的物体个数
        public int                 nExitedNumber;                  // 出来区域或者出入口内的物体个数
        public int                 emUpperLimitType;               // 人数越上限类型,参见EM_UPPER_LIMIT_TYPE定义
        public byte[]              reserved = new byte[512];       // 预留       
    }
    
    /////////////////////////////////智能支持/////////////////////////////////
    //物体对应图片文件信息
    public static class NET_PIC_INFO extends Structure
    {
        public int dwOffSet;//文件在二进制数据块中的偏移位置,单位:字节
        public int dwFileLenth;//文件大小,单位:字节
        public short wWidth;//图片宽度,单位:像素
        public short wHeight;//图片高度,单位:像素
        public Pointer pszFilePath;//鉴于历史原因,该成员只在事件上报时有效， char *
                                   // 文件路径
                                   // 用户使用该字段时需要自行申请空间进行拷贝保存
        public byte bIsDetected;//图片是否算法检测出来的检测过的提交识别服务器时,
                                            //则不需要再时检测定位抠图,1:检测过的,0:没有检测过
        public byte[] bReserved = new byte[7];//12<--16
        public NET_POINT stuPoint;			  // 小图左上角在大图的位置，使用绝对坐标系				
    }

    // 人员类型
    public static class EM_PERSON_TYPE extends Structure
    {
        public static final int PERSON_TYPE_UNKNOWN = 0;  
        public static final int PERSON_TYPE_NORMAL = 1; //普通人员
        public static final int PERSON_TYPE_SUSPICION = 2; //嫌疑人员
    }

    // 证件类型
    public static class EM_CERTIFICATE_TYPE extends Structure
    {
        public static final int CERTIFICATE_TYPE_UNKNOWN = 0;  
        public static final int CERTIFICATE_TYPE_IC = 1; //身份证
        public static final int CERTIFICATE_TYPE_PASSPORT = 2; //护照
    }
    
    //人员信息
    public static class FACERECOGNITION_PERSON_INFO extends Structure
    {
        public byte[] szPersonName = new byte[NET_MAX_NAME_LEN];//姓名,此参数作废
        public short wYear;//出生年,作为查询条件时,此参数填0,则表示此参数无效
        public byte byMonth;//出生月,作为查询条件时,此参数填0,则表示此参数无效
        public byte byDay;//出生日,作为查询条件时,此参数填0,则表示此参数无效
        public byte[] szID = new byte[NET_MAX_PERSON_ID_LEN];//人员唯一标示(身份证号码,工号,或其他编号)
        public byte bImportantRank;//人员重要等级,1~10,数值越高越重要,作为查询条件时,此参数填0,则表示此参数无效
        public byte bySex;//性别,1-男,2-女,作为查询条件时,此参数填0,则表示此参数无效
        public short wFacePicNum;//图片张数
        public NET_PIC_INFO[] szFacePicInfo =  (NET_PIC_INFO[])new NET_PIC_INFO().toArray(NET_MAX_PERSON_IMAGE_NUM);//当前人员对应的图片信息
        public byte byType;//人员类型,详见EM_PERSON_TYPE
        public byte byIDType;//证件类型,详见EM_CERTIFICATE_TYPE
        public byte[] bReserved1 = new byte[2];//字节对齐
        public byte[] szProvince = new byte[NET_MAX_PROVINCE_NAME_LEN];//省份
        public byte[] szCity = new byte[NET_MAX_CITY_NAME_LEN];//城市
        public byte[] szPersonNameEx = new byte[NET_MAX_PERSON_NAME_LEN];//姓名,因存在姓名过长,16字节无法存放问题,故增加此参数,
        public byte[] szUID = new byte[NET_MAX_PERSON_ID_LEN];//人员唯一标识符,首次由服务端生成,区别于ID字段
                                                              // 修改,删除操作时必填
    	public byte[] szCountry = new byte[NET_COUNTRY_LENGTH];			// 国籍,符合ISO3166规范
    	public byte		 byIsCustomType;								// 人员类型是否为自定义: 0 使用Type规定的类型 1 自定义,使用szPersonName字段
    	public Pointer	 pszComment;									// 备注信息
    	public Pointer	 pszGroupID;									// 人员所属组ID
    	public Pointer	 pszGroupName;									// 人员所属组名
    	public Pointer	 pszFeatureValue;								// 人脸特征
    	public byte		 bGroupIdLen;									// pszGroupID的长度
    	public byte		 bGroupNameLen;									// pszGroupName的长度
    	public byte		 bFeatureValueLen;								// pszFeatureValue的长度
    	public byte[]    bReserved = new byte[5];
    }

    ///////////////////////////////////人脸识别模块相关结构体///////////////////////////////////////
    public static class NET_UID_CHAR extends Structure
    {
        public byte[] szUID = new byte[NET_MAX_PERSON_ID_LEN];//UID内容
    }
    
    //人脸识别数据库操作
    public static class EM_OPERATE_FACERECONGNITIONDB_TYPE
    {
        public static final int NET_FACERECONGNITIONDB_UNKOWN = 0; 
        public static final int NET_FACERECONGNITIONDB_ADD = 1;             //添加人员信息和人脸样本，如果人员已经存在，图片数据和原来的数据合并
        public static final int NET_FACERECONGNITIONDB_DELETE = 2;          //删除人员信息和人脸样本
        public static final int NET_FACERECONGNITIONDB_MODIFY = 3;          //修改人员信息和人脸样本,人员的UID标识必填
        public static final int NET_FACERECONGNITIONDB_DELETE_BY_UID = 4;   //通过UID删除人员信息和人脸样本
    }
    
    //CLIENT_OperateFaceRecognitionDB接口输入参数
    public static class NET_IN_OPERATE_FACERECONGNITIONDB extends Structure
    {
        public int dwSize;
        public int emOperateType;//操作类型, 取EM_OPERATE_FACERECONGNITIONDB_TYPE中的值
        public FACERECOGNITION_PERSON_INFO stPersonInfo;//人员信息
                                                        //emOperateType操作类型为ET_FACERECONGNITIONDB_DELETE_BY_UID时使用,stPeronInfo字段无效
        public int nUIDNum;//UID个数
        public Pointer stuUIDs;//人员唯一标识符,首次由服务端生成,区别于ID字段, NET_UID_CHAR *
        // 图片二进制数据
        public Pointer pBuffer;//缓冲地址, char *
        public int nBufferLen;//缓冲数据长度
        
        public NET_IN_OPERATE_FACERECONGNITIONDB()
        {
            this.dwSize = this.size();
        }
    }
    
    //CLIENT_OperateFaceRecognitionDB接口输出参数
    public static class NET_OUT_OPERATE_FACERECONGNITIONDB extends Structure
    {
        public int dwSize;
        
        public NET_OUT_OPERATE_FACERECONGNITIONDB()
        {
            this.dwSize = this.size();
        }
    }
    
    //人脸对比模式
    public static class EM_FACE_COMPARE_MODE extends Structure
    {
        public static final int NET_FACE_COMPARE_MODE_UNKOWN = 0;
        public static final int NET_FACE_COMPARE_MODE_NORMAL = 1; //正常
        public static final int NET_FACE_COMPARE_MODE_AREA = 2; //指定人脸区域组合区域
        public static final int  NET_FACE_COMPARE_MODE_AUTO = 3; //智能模式,算法根据人脸各个区域情况自动选取组合
    }
    
    //人脸区域
    public static class EM_FACE_AREA_TYPE extends Structure
    {
        public static final int NET_FACE_AREA_TYPE_UNKOWN = 0; 
        public static final int NET_FACE_AREA_TYPE_EYEBROW = 1; //眉毛
        public static final int NET_FACE_AREA_TYPE_EYE = 2; //眼睛
        public static final int NET_FACE_AREA_TYPE_NOSE= 3; //鼻子
        public static final int NET_FACE_AREA_TYPE_MOUTH = 4; //嘴巴
        public static final int NET_FACE_AREA_TYPE_CHEEK =5; //脸颊
    }
    
    public static class NET_FACE_MATCH_OPTIONS extends Structure
    {
        public int dwSize;
        public int nMatchImportant;//人员重要等级1~10,数值越高越重要,(查询重要等级大于等于此等级的人员)， 类型为unsigned int
        public int emMode;//人脸比对模式,详见EM_FACE_COMPARE_MODE, 取EM_FACE_COMPARE_MODE中的值
        public int nAreaNum;//人脸区域个数
        public int[] szAreas= new int[MAX_FACE_AREA_NUM];//人脸区域组合,emMode为NET_FACE_COMPARE_MODE_AREA时有效, 数组元素取EM_FACE_AREA_TYPE中的值
        public int nAccuracy;//识别精度(取值1~10,随着值增大,检测精度提高,检测速度下降。最小值为1表示检测速度优先,最大值为10表示检测精度优先。暂时只对人脸检测有效)
        public int nSimilarity;//相似度(必须大于该相识度才报告;百分比表示,1~100)
        public int nMaxCandidate;//报告的最大候选个数(根据相似度进行排序,取相似度最大的候选人数报告)
        
        public NET_FACE_MATCH_OPTIONS()
        {
            this.dwSize = this.size();
        }
    }
    
    //人脸识别人脸类型
    public static class EM_FACERECOGNITION_FACE_TYPE extends Structure
    {
        public static final int  EM_FACERECOGNITION_FACE_TYPE_UNKOWN = 0;
        public static final int  EM_FACERECOGNITION_FACE_TYPE_ALL = 1; //所有人脸
        public static final int  EM_FACERECOGNITION_FACE_TYPE_REC_SUCCESS=  2;//识别成功
        public static final int  EM_FACERECOGNITION_FACE_TYPE_REC_FAIL = 3;//识别失败
    }
    
    public static class NET_FACE_FILTER_CONDTION extends Structure
    {
        public int dwSize;
        public NET_TIME stStartTime;//开始时间
        public NET_TIME stEndTime;//结束时间
        public byte[] szMachineAddress = new byte[MAX_PATH];//地点,支持模糊匹配
        public int nRangeNum;//实际数据库个数
        public byte[] szRange = new byte[MAX_FACE_DB_NUM];//待查询数据库类型,详见EM_FACE_DB_TYPE
        public int emFaceType;//待查询人脸类型,详见EM_FACERECOGNITION_FACE_TYPE， 取EM_FACERECOGNITION_FACE_TYPE中的值
        public int nGroupIdNum;//人员组数
        public byte[] szGroupId = new byte[MAX_GOURP_NUM*NET_COMMON_STRING_64];//人员组ID
        public NET_TIME stBirthdayRangeStart;          // 生日起始时间
        public NET_TIME stBirthdayRangeEnd;            // 生日结束时间
        
        public NET_FACE_FILTER_CONDTION()
        {
            this.dwSize = this.size();
        }
    }
    
    //CLIENT_StartFindFaceRecognition接口输入参数
    public static class NET_IN_STARTFIND_FACERECONGNITION extends Structure
    {
        public int dwSize;
        public int bPersonEnable;//人员信息查询条件是否有效, BOOL类型，取值0或1
        public FACERECOGNITION_PERSON_INFO stPerson;//人员信息查询条件
        public NET_FACE_MATCH_OPTIONS stMatchOptions;//人脸匹配选项
        public NET_FACE_FILTER_CONDTION stFilterInfo;//查询过滤条件
        // 图片二进制数据
        public Pointer pBuffer;//缓冲地址, char *
        public int nBufferLen;//缓冲数据长度
        public int nChannelID;//通道号
        
        public NET_IN_STARTFIND_FACERECONGNITION()
        {
            this.dwSize = this.size();
        }
    }
    
    //CLIENT_StartFindFaceRecognition接口输出参数
    public static class NET_OUT_STARTFIND_FACERECONGNITION extends Structure
    {
        public int dwSize;
        public int nTotalCount;//返回的符合查询条件的记录个数
                               // -1表示总条数未生成,要推迟获取
                               // 使用CLIENT_AttachFaceFindState接口状态
        public long lFindHandle;//查询句柄
        public int nToken;//获取到的查询令牌
        
        public NET_OUT_STARTFIND_FACERECONGNITION()
        {
            this.dwSize = this.size();
        }
    }
    
    //CLIENT_DoFindFaceRecognition 接口输入参数
    public static class NET_IN_DOFIND_FACERECONGNITION extends Structure
    {
        public int dwSize;
        public long lFindHandle;//查询句柄
        public int nBeginNum;//查询起始序号
        public int nCount;//当前想查询的记录条数
    	public int	emDataType;	// 指定查询结果返回图片的格式. 参考  EM_NEEDED_PIC_RETURN_TYPE
        
        public NET_IN_DOFIND_FACERECONGNITION()
        {
            this.dwSize = this.size();
        }
    }
    
    // 查询结果返回图片的格式
    public static class EM_NEEDED_PIC_RETURN_TYPE extends Structure
    {
        public static final int EM_NEEDED_PIC_TYPE_UNKOWN = 0;            // 未知类型
        public static final int EM_NEEDED_PIC_TYPE_HTTP_URL = 1;          // 返回图片HTTP链接
        public static final int EM_NEEDED_PIC_TYPE_BINARY_DATA = 2;       // 返回图片二进制数据
        public static final int EM_NEEDED_PIC_TYPE_HTTP_AND_BINARY = 3;   // 返回二进制和HTTP链接
    }
    
    //候选人员信息
    public static class CANDIDATE_INFO extends Structure
    {
        public FACERECOGNITION_PERSON_INFO stPersonInfo;//人员信息
                                                        // 布控（黑名单）库,指布控库中人员信息；
                                                        // 历史库,指历史库中人员信息
                                                        // 报警库,指布控库的人员信息
        public byte bySimilarity;//和查询图片的相似度,百分比表示,1~100
        public byte byRange;//人员所属数据库范围,详见EM_FACE_DB_TYPE
        public byte[] byReserved1 = new byte[2];
        public NET_TIME stTime;//当byRange为历史数据库时有效,表示查询人员出现的时间
        public byte[] szAddress = new byte[MAX_PATH];//当byRange为历史数据库时有效,表示查询人员出现的地点
    	public int bIsHit; // BOOL类型,是否有识别结果,指这个检测出的人脸在库中有没有比对结果
    	public NET_PIC_INFO_EX3 stuSceneImage; // 人脸全景图
    	public int nChannelID;	// 通道号
        public byte[] byReserved = new byte[32];//保留字节
    }
    
    // 物体对应图片文件信息(包含图片路径)
    public static class NET_PIC_INFO_EX3 extends Structure
    {
        public int          dwOffSet;                       // 文件在二进制数据块中的偏移位置, 单位:字节
        public int          dwFileLenth;                    // 文件大小, 单位:字节
        public short        wWidth;                         // 图片宽度, 单位:像素
        public short        wHeight;                        // 图片高度, 单位:像素
    	public byte[]       szFilePath = new byte[64];      // 文件路径
        public byte         bIsDetected;                    // 图片是否算法检测出来的检测过的提交识别服务器时,
    	                                                    // 则不需要再时检测定位抠图,1:检测过的,0:没有检测过
        public byte[]       bReserved = new byte[11];       // 保留
    }
    
    //CLIENT_DoFindFaceRecognition接口输出参数
    public static class NET_OUT_DOFIND_FACERECONGNITION extends Structure
    {
        public int dwSize;
        public int nCadidateNum;//实际返回的候选信息结构体个数
        public CANDIDATE_INFO[] stCadidateInfo = (CANDIDATE_INFO[])new CANDIDATE_INFO().toArray(MAX_FIND_COUNT);//候选信息数组
        // 图片二进制数据
        public Pointer pBuffer;//缓冲地址, char *
        public int nBufferLen;//缓冲数据长度
        
        public NET_OUT_DOFIND_FACERECONGNITION()
        {
            this.dwSize = this.size();
        }
    }
    
    /////////////////////////////////智能支持/////////////////////////////////
    //CLIENT_DetectFace接口输入参数
    public static class NET_IN_DETECT_FACE extends Structure
    {
        public int dwSize;
        public NET_PIC_INFO stPicInfo;//大图信息
        // 图片二进制数据
        public Pointer pBuffer;//缓冲地址, char *
        public int nBufferLen;//缓冲数据长度
        
        public NET_IN_DETECT_FACE()
        {
            this.dwSize = this.size();
        }
    }
    
    //CLIENT_DetectFace接口输出参数
    public static class NET_OUT_DETECT_FACE extends Structure
    {
        public int dwSize;
        public Pointer pPicInfo;//检测出的人脸图片信息,由用户申请空间, NET_PIC_INFO*
        public int nMaxPicNum;//最大人脸图片信息个数
        public int nRetPicNum;//实际返回的人脸图片个数
        // 图片二进制数据
        public Pointer pBuffer;//缓冲地址,由用户申请空间,存放检测出的人脸图片数据, char *
        public int nBufferLen;//缓冲数据长度
        
        public NET_OUT_DETECT_FACE()
        {
            this.dwSize = this.size();
        }
    }
    
    // 人脸识别事件类型
    public static class EM_FACERECOGNITION_ALARM_TYPE extends Structure
    {
        public static final int NET_FACERECOGNITION_ALARM_TYPE_UNKOWN = 0;
        public static final int NET_FACERECOGNITION_ALARM_TYPE_ALL = 1; //黑白名单
        public static final int NET_FACERECOGNITION_ALARM_TYPE_BLACKLIST = 2; //黑名单
        public static final int NET_FACERECOGNITION_ALARM_TYPE_WHITELIST = 3; //白名单
    }
    
    //NET_FILE_QUERY_FACE对应的人脸识别服务查询参数
    public static class MEDIAFILE_FACERECOGNITION_PARAM extends Structure
    {
        public int dwSize;//结构体大小
        // 查询过滤条件
        public NET_TIME stStartTime;//开始时间
        public NET_TIME stEndTime;//结束时间
        public byte[] szMachineAddress = new byte[MAX_PATH];//地点,支持模糊匹配
        public int nAlarmType;//待查询报警类型,详见EM_FACERECOGNITION_ALARM_TYPE
        public int abPersonInfo;//人员信息是否有效, BOOL类型，取值0或1
        public FACERECOGNITION_PERSON_INFO stPersonInfo;//人员信息
        public int nChannelId;//通道号
        public int nGroupIdNum;//人员组数
        public byte[] szGroupId = new byte[MAX_GOURP_NUM*NET_COMMON_STRING_64];//人员组ID
        
        public MEDIAFILE_FACERECOGNITION_PARAM()
        {
            this.dwSize = this.size();
        }
    }
    
    // DH_MEDIA_QUERY_TRAFFICCAR对应的查询条件
    public static class MEDIA_QUERY_TRAFFICCAR_PARAM extends Structure 
    {
        public int                 nChannelID;                     // 通道号从0开始,-1表示查询所有通道
        public NET_TIME            StartTime;                      // 开始时间    
        public NET_TIME            EndTime;                        // 结束时间
        public int                 nMediaType;                     // 文件类型,0:任意类型, 1:jpg图片, 2:dav文件
        public int                 nEventType;                     // 事件类型,详见"智能分析事件类型", 0:表示查询任意事件,此参数废弃,请使用pEventTypes
        public byte[]              szPlateNumber = new byte[32];   // 车牌号, "\0"则表示查询任意车牌号
        public int                 nSpeedUpperLimit;               // 查询的车速范围; 速度上限 单位: km/h
        public int                 nSpeedLowerLimit;               // 查询的车速范围; 速度下限 单位: km/h 
        public int                 bSpeedLimit;                    // 是否按速度查询; TRUE:按速度查询,nSpeedUpperLimit和nSpeedLowerLimit有效。
        public int                 dwBreakingRule;                 // 违章类型：
                                                            	   // 当事件类型为 EVENT_IVS_TRAFFICGATE时
                                                            	   //        第一位:逆行;  第二位:压线行驶; 第三位:超速行驶; 
                                                                   //        第四位：欠速行驶; 第五位:闯红灯;
                                                                   // 当事件类型为 EVENT_IVS_TRAFFICJUNCTION
                                                                   //        第一位:闯红灯;  第二位:不按规定车道行驶;  
                                                                   //        第三位:逆行; 第四位：违章掉头;
                                                                   //        第五位:压线行驶;

        public byte[]              szPlateType=new byte[32];       // 车牌类型,"Unknown" 未知,"Normal" 蓝牌黑牌,"Yellow" 黄牌,"DoubleYellow" 双层黄尾牌,"Police" 警牌"Armed" 武警牌,
                                                            	   // "Military" 部队号牌,"DoubleMilitary" 部队双层,"SAR" 港澳特区号牌,"Trainning" 教练车号牌
                                                                   // "Personal" 个性号牌,"Agri" 农用牌,"Embassy" 使馆号牌,"Moto" 摩托车号牌,"Tractor" 拖拉机号牌,"Other" 其他号牌
        public byte[]              szPlateColor = new byte[16];    // 车牌颜色, "Blue"蓝色,"Yellow"黄色, "White"白色,"Black"黑色
        public byte[]              szVehicleColor = new byte[16];  // 车身颜色:"White"白色, "Black"黑色, "Red"红色, "Yellow"黄色, "Gray"灰色, "Blue"蓝色,"Green"绿色
        public byte[]              szVehicleSize = new byte[16];   // 车辆大小类型:"Light-duty":小型车;"Medium":中型车; "Oversize":大型车; "Unknown": 未知
        public int                 nGroupID;                       // 事件组编号(此值>=0时有效)
        public short               byLane;                         // 车道号(此值>=0时表示具体车道,-1表示所有车道,即不下发此字段)
        public byte                byFileFlag;                     // 文件标志, 0xFF-使用nFileFlagEx, 0-表示所有录像, 1-定时文件, 2-手动文件, 3-事件文件, 4-重要文件, 5-合成文件
        public byte                byRandomAccess;                 // 是否需要在查询过程中随意跳转,0-不需要,1-需要
        public int                 nFileFlagEx;                    // 文件标志, 按位表示: bit0-定时文件, bit1-手动文件, bit2-事件文件, bit3-重要文件, bit4-合成文件, bit5-黑名单图片 0xFFFFFFFF-所有录像
        public int                 nDirection;                     // 车道方向（车开往的方向）    0-北 1-东北 2-东 3-东南 4-南 5-西南 6-西 7-西北 8-未知 -1-所有方向
        public Pointer             szDirs;                         // 工作目录列表,一次可查询多个目录,为空表示查询所有目录。目录之间以分号分隔,如“/mnt/dvr/sda0;/mnt/dvr/sda1”,szDirs==null 或"" 表示查询所有
        public Pointer             pEventTypes;                    // 待查询的事件类型数组指针,事件类型,详见"智能分析事件类型",若为NULL则认为查询所有事件（缓冲需由用户申请）
        public int                 nEventTypeNum;                  // 事件类型数组大小
        public Pointer             pszDeviceAddress;               // 设备地址, NULL表示该字段不起作用
        public Pointer             pszMachineAddress;              // 机器部署地点, NULL表示该字段不起作用
        public Pointer             pszVehicleSign;                 // 车辆标识, 例如 "Unknown"-未知, "Audi"-奥迪, "Honda"-本田... NULL表示该字段不起作用
    	public short               wVehicleSubBrand;               // 车辆子品牌 需要通过映射表得到真正的子品牌 映射表详见开发手册
        public short               wVehicleYearModel;              // 车辆品牌年款 需要通过映射表得到真正的年款 映射表详见开发手册
        public int[]               bReserved = new int[28];        // 保留字段
    }
    
    // DH_MEDIA_QUERY_TRAFFICCAR_EX对应的查询条件
    public static class MEDIA_QUERY_TRAFFICCAR_PARAM_EX extends Structure
    {
        public int               dwSize;
        public MEDIA_QUERY_TRAFFICCAR_PARAM stuParam;                  // 基本查询参数
        
        public MEDIA_QUERY_TRAFFICCAR_PARAM_EX() {
        	this.dwSize = this.size();
        }
    }
    
    // DH_MEDIA_QUERY_TRAFFICCAR查询出来的media文件信息
    public static class MEDIAFILE_TRAFFICCAR_INFO extends Structure
    {
        public int				   ch;                                 // 通道号
        public byte[]              szFilePath = new byte[128];         // 文件路径
        public int        		   size;                               // 文件长度
        public NET_TIME            starttime;                          // 开始时间
        public NET_TIME            endtime;                            // 结束时间
        public int                 nWorkDirSN;                         // 工作目录编号                                    
        public byte                nFileType;                          // 文件类型  1：jpg图片
        public byte                bHint;                              // 文件定位索引
        public byte                bDriveNo;                           // 磁盘号
        public byte                bReserved2;
        public int                 nCluster;                           // 簇号
        public byte                byPictureType;                      // 图片类型, 0-普通, 1-合成, 2-抠图
        public byte[]              bReserved = new byte[3];            // 保留字段

        //以下是交通车辆信息
        public byte[]              szPlateNumber = new byte[32];       // 车牌号码
        public byte[]              szPlateType = new byte[32];         // 号牌类型"Unknown" 未知; "Normal" 蓝牌黑牌; "Yellow" 黄牌; "DoubleYellow" 双层黄尾牌
                                                                       // "Police" 警牌; "Armed" 武警牌; "Military" 部队号牌; "DoubleMilitary" 部队双层
                                                                       // "SAR" 港澳特区号牌; "Trainning" 教练车号牌; "Personal" 个性号牌; "Agri" 农用牌
                                                                       // "Embassy" 使馆号牌; "Moto" 摩托车号牌; "Tractor" 拖拉机号牌; "Other" 其他号牌
        public byte[]              szPlateColor = new byte[16];        // 车牌颜色:"Blue","Yellow", "White","Black"
        public byte[]              szVehicleColor = new byte[16];      // 车身颜色:"White", "Black", "Red", "Yellow", "Gray", "Blue","Green"
        public int                 nSpeed;                             // 车速,单位 Km/H
        public int                 nEventsNum;                         // 关联的事件个数
        public int[]               nEvents = new int[32];              // 关联的事件列表,数组值表示相应的事件,详见"智能分析事件类型"        
        public int                 dwBreakingRule;                     // 具体违章类型掩码,第一位:闯红灯; 第二位:不按规定车道行驶;
                                                                       // 第三位:逆行; 第四位：违章掉头;否则默认为:交通路口事件
        public byte[]              szVehicleSize = new byte[16];       // 车辆大小类型:"Light-duty":小型车;"Medium":中型车; "Oversize":大型车
        public byte[]              szChannelName = new byte[NET_CHAN_NAME_LEN];    // 本地或远程的通道名称
        public byte[]              szMachineName = new byte[NET_MAX_NAME_LEN];     // 本地或远程设备名称

        public int                 nSpeedUpperLimit;                   // 速度上限 单位: km/h
        public int                 nSpeedLowerLimit;                   // 速度下限 单位: km/h    
        public int                 nGroupID;                           // 事件里的组编号
        public byte                byCountInGroup;                     // 一个事件组内的抓拍张数
        public byte                byIndexInGroup;                     // 一个事件组内的抓拍序号
        public byte                byLane;                             // 车道,参见MEDIA_QUERY_TRAFFICCAR_PARAM
        public byte[]              bReserved1 = new byte[21];          // 保留
        public NET_TIME            stSnapTime;                         // 抓拍时间
        public int                 nDirection;                         // 车道方向,参见MEDIA_QUERY_TRAFFICCAR_PARAM
        public byte[]              szMachineAddress = new byte[MAX_PATH]; // 机器部署地点
    }
    
    // DH_MEDIA_QUERY_TRAFFICCAR_EX查询出来的文件信息
    public static class MEDIAFILE_TRAFFICCAR_INFO_EX extends Structure
    {
        public int               dwSize;
        public MEDIAFILE_TRAFFICCAR_INFO stuInfo;  									 // 基本信息
        public byte[]            szDeviceAddr = new byte[NET_COMMON_STRING_256];     // 设备地址
        public byte[]            szVehicleSign = new byte[NET_COMMON_STRING_32];     // 车辆标识, 例如 "Unknown"-未知, "Audi"-奥迪, "Honda"-本田...
        public byte[]            szCustomParkNo = new byte[NET_COMMON_STRING_64];    // 自定义车位号（停车场用）
        public short             wVehicleSubBrand;                      			 // 车辆子品牌，需要通过映射表得到真正的子品牌
        public short             wVehicleYearModel;                      			 // 车辆年款，需要通过映射表得到真正的年款
    	public NET_TIME			 stuEleTagInfoUTC;									 // 对应电子车牌标签信息中的过车时间(ThroughTime)
    	public int[] 			 emFalgLists = new int[EM_RECORD_SNAP_FLAG_TYPE.FLAG_TYPE_MAX];		// 录像或抓图文件标志, 参考 EM_RECORD_SNAP_FLAG_TYPE
    	public int               nFalgCount;										 // 标志总数 
        
        public MEDIAFILE_TRAFFICCAR_INFO_EX() { 
        	this.dwSize = this.size();
        }
    }

    public static class NET_PIC_INFO_EX extends Structure
    {
        public int dwSize;//结构体大小
        public int dwFileLenth;//文件大小,单位:字节
        public byte[] szFilePath = new byte[MAX_PATH];//文件路径
        
        public NET_PIC_INFO_EX()
        {
            this.dwSize = this.size();
        }
    }
    
    //区域；各边距按整长8192的比例
    public static class NET_RECT extends Structure
    {
        public int left;
        public int top;
        public int right;
        public int bottom;
    }
    

	 // 时间段结构                                                                
	 public static class NET_TSECT extends Structure
	 {
	    public int             bEnable;        // 当表示录像时间段时,按位表示四个使能,从低位到高位分别表示动检录象、报警录象、普通录象、动检和报警同时发生才录像
	    public int             iBeginHour;
	    public int             iBeginMin;
	    public int             iBeginSec;
	    public int             iEndHour;
	    public int             iEndMin;
	    public int             iEndSec;
	 } 
    

    public static class DH_RECT extends Structure
    {
    	public NativeLong left;
    	public NativeLong top;
    	public NativeLong right;
    	public NativeLong bottom;
    }
    
    //二维空间点
    public static class NET_POINT extends Structure
    {
        public short nx;
        public short ny;
    }
    
    public static class NET_CANDIDAT_PIC_PATHS extends Structure
    {
        public int dwSize;//结构体大小
        public int nFileCount;//实际文件个数
        public NET_PIC_INFO_EX[] stFiles = (NET_PIC_INFO_EX[])new NET_PIC_INFO_EX().toArray(NET_MAX_PERSON_IMAGE_NUM);//文件信息
        
        public NET_CANDIDAT_PIC_PATHS()
        {
            this.dwSize = this.size();
        }
    }
    
    //颜色类型
    public static class EM_COLOR_TYPE extends Structure
    {   
        public static final int NET_COLOR_TYPE_RED = 0;//红色
        public static final int NET_COLOR_TYPE_YELLOW = 1;//黄色
        public static final int NET_COLOR_TYPE_GREEN = 2; //绿色
        public static final int NET_COLOR_TYPE_CYAN = 3; //青色
        public static final int NET_COLOR_TYPE_BLUE = 4; //蓝色
        public static final int NET_COLOR_TYPE_PURPLE = 5; //紫色
        public static final int NET_COLOR_TYPE_BLACK = 6; //黑色
        public static final int NET_COLOR_TYPE_WHITE = 7; //白色
        public static final int NET_COLOR_TYPE_MAX = 8; 
    }
    
    //视频分析物体信息结构体
    public static class NET_MSG_OBJECT extends Structure
    {
        public int nObjectID;//物体ID,每个ID表示一个唯一的物体
        public byte[] szObjectType = new byte[128];//物体类型
        public int nConfidence;//置信度(0~255),值越大表示置信度越高
        public int nAction;//物体动作:1:Appear2:Move3:Stay
        public DH_RECT BoundingBox;//包围盒
        public NET_POINT Center;//物体型心
        public int nPolygonNum;//多边形顶点个数
        public NET_POINT[] Contour = (NET_POINT[])new NET_POINT().toArray(NET_MAX_POLYGON_NUM);//较精确的轮廓多边形
        public int rgbaMainColor;//表示车牌、车身等物体主要颜色；按字节表示,分别为红、绿、蓝和透明度,例如:RGB值为(0,255,0),透明度为0时,其值为0x00ff0000.
        public byte[] szText = new byte[128];//物体上相关的带0结束符文本,比如车牌,集装箱号等等
                                                                // "ObjectType"为"Vehicle"或者"Logo"时（尽量使用Logo。Vehicle是为了兼容老产品）表示车标,支持：
                                                                // "Unknown"未知 
                                                                // "Audi" 奥迪
                                                                // "Honda" 本田
                                                                // "Buick" 别克
                                                                // "Volkswagen" 大众
                                                                // "Toyota" 丰田
                                                                // "BMW" 宝马
                                                                // "Peugeot" 标致
                                                                // "Ford" 福特
                                                                // "Mazda" 马自达
                                                                // "Nissan" 尼桑
                                                                // "Hyundai" 现代
                                                                // "Suzuki" 铃木
                                                                // "Citroen" 雪铁龙
                                                                // "Benz" 奔驰
                                                                // "BYD" 比亚迪
                                                                // "Geely" 吉利
                                                                // "Lexus" 雷克萨斯
                                                                // "Chevrolet" 雪佛兰
                                                                // "Chery" 奇瑞
                                                                // "Kia" 起亚
                                                                // "Charade" 夏利
                                                                // "DF" 东风
                                                                // "Naveco" 依维柯
                                                                // "SGMW" 五菱
                                                                // "Jinbei" 金杯
                                                                // "JAC" 江淮
                                                                // "Emgrand" 帝豪
                                                                // "ChangAn" 长安
                                                                // "Great Wall" 长城
                                                                // "Skoda" 斯柯达
                                                                // "BaoJun" 宝骏
                                                                // "Subaru" 斯巴鲁
                                                                // "LandWind" 陆风
                                                                // "Luxgen" 纳智捷
                                                                // "Renault" 雷诺
                                                                // "Mitsubishi" 三菱
                                                                // "Roewe" 荣威
                                                                // "Cadillac" 凯迪拉克
                                                                // "MG" 名爵
                                                                // "Zotye" 众泰
                                                                // "ZhongHua" 中华
                                                                // "Foton" 福田
                                                                // "SongHuaJiang" 松花江
                                                                // "Opel" 欧宝
                                                                // "HongQi" 一汽红旗
                                                                // "Fiat" 菲亚特
                                                                // "Jaguar" 捷豹
                                                                // "Volvo" 沃尔沃
                                                                // "Acura" 讴歌
                                                                // "Porsche" 保时捷
                                                                // "Jeep" 吉普
                                                                // "Bentley" 宾利
                                                                // "Bugatti" 布加迪
                                                                // "ChuanQi" 传祺
                                                                // "Daewoo" 大宇
                                                                // "DongNan" 东南
                                                                // "Ferrari" 法拉利
                                                                // "Fudi" 福迪
                                                                // "Huapu" 华普
                                                                // "HawTai" 华泰
                                                                // "JMC" 江铃
                                                                // "JingLong" 金龙客车
                                                                // "JoyLong" 九龙
                                                                // "Karry" 开瑞
                                                                // "Chrysler" 克莱斯勒
                                                                // "Lamborghini" 兰博基尼
                                                                // "RollsRoyce" 劳斯莱斯
                                                                // "Linian" 理念
                                                                // "LiFan" 力帆
                                                                // "LieBao" 猎豹
                                                                // "Lincoln" 林肯
                                                                // "LandRover" 路虎
                                                                // "Lotus" 路特斯
                                                                // "Maserati" 玛莎拉蒂
                                                                // "Maybach" 迈巴赫
                                                                // "Mclaren" 迈凯轮
                                                                // "Youngman" 青年客车
                                                                // "Tesla" 特斯拉
                                                                // "Rely" 威麟
                                                                // "Lsuzu" 五十铃
                                                                // "Yiqi" 一汽
                                                                // "Infiniti" 英菲尼迪
                                                                // "YuTong" 宇通客车
                                                                // "AnKai" 安凯客车
                                                                // "Canghe" 昌河
                                                                // "HaiMa" 海马
                                                                // "Crown" 丰田皇冠
                                                                // "HuangHai" 黄海
                                                                // "JinLv" 金旅客车
                                                                // "JinNing" 精灵
                                                                // "KuBo" 酷博
                                                                // "Europestar" 莲花
                                                                // "MINI" 迷你
                                                                // "Gleagle" 全球鹰
                                                                // "ShiDai" 时代
                                                                // "ShuangHuan" 双环
                                                                // "TianYe" 田野
                                                                // "WeiZi" 威姿
                                                                // "Englon" 英伦
                                                                // "ZhongTong" 中通客车
                                                                // "Changan" 长安轿车
                                                                // "Yuejin" 跃进
                                                                // "Taurus" 金牛星
                                                                // "Alto" 奥拓
                                                                // "Weiwang" 威旺
                                                                // "Chenglong" 乘龙
                                                                // "Haige" 海格
                                                                // "Shaolin" 少林客车
                                                                // "Beifang" 北方客车
                                                                // "Beijing" 北京汽车
                                                                // "Hafu" 哈弗
        public byte[] szObjectSubType = new byte[64];//物体子类别,根据不同的物体类型,可以取以下子类型：
                                                                             // Vehicle Category:"Unknown"  未知,"Motor" 机动车,"Non-Motor":非机动车,"Bus": 公交车,"Bicycle" 自行车,"Motorcycle":摩托车,"PassengerCar":客车,
                                                                             // "LargeTruck":大货车,    "MidTruck":中货车,"SaloonCar":轿车,"Microbus":面包车,"MicroTruck":小货车,"Tricycle":三轮车,    "Passerby":行人                                                    
                                                                             // Plate Category："Unknown" 未知,"Normal" 蓝牌黑牌,"Yellow" 黄牌,"DoubleYellow" 双层黄尾牌,"Police" 警牌"Armed" 武警牌,
                                                                             // "Military" 部队号牌,"DoubleMilitary" 部队双层,"SAR" 港澳特区号牌,"Trainning" 教练车号牌
                                                                             // "Personal" 个性号牌,"Agri" 农用牌,"Embassy" 使馆号牌,"Moto" 摩托车号牌,"Tractor" 拖拉机号牌,"Other" 其他号牌
                                                                             // HumanFace Category:"Normal" 普通人脸,"HideEye" 眼部遮挡,"HideNose" 鼻子遮挡,"HideMouth" 嘴部遮挡
        public short wSubBrand;   // 车辆子品牌 需要通过映射表得到真正的子品牌 映射表详见开发手册
        public byte byReserved1;
        public byte bPicEnble;//是否有物体对应图片文件信息, 类型小bool, 取值0或者1
        public NET_PIC_INFO stPicInfo;//物体对应图片信息
        public byte bShotFrame;//是否是抓拍张的识别结果,  类型小bool, 取值0或者1
        public byte bColor;//物体颜色(rgbaMainColor)是否可用, 类型小bool, 取值0或者1
        public byte byReserved2;
        public byte byTimeType;//时间表示类型,详见EM_TIME_TYPE说明
        public NET_TIME_EX stuCurrentTime;//针对视频浓缩,当前时间戳（物体抓拍或识别时,会将此识别智能帧附在一个视频帧或jpeg图片中,此帧所在原始视频中的出现时间）
        public NET_TIME_EX stuStartTime;//开始时间戳（物体开始出现时）
        public NET_TIME_EX stuEndTime;//结束时间戳（物体最后出现时）
        public DH_RECT stuOriginalBoundingBox;//包围盒(绝对坐标)
        public DH_RECT stuSignBoundingBox;//车标坐标包围盒
        public int dwCurrentSequence;//当前帧序号（抓下这个物体时的帧）
        public int dwBeginSequence;//开始帧序号（物体开始出现时的帧序号）
        public int dwEndSequence;//结束帧序号（物体消逝时的帧序号）
        public long nBeginFileOffse;//开始时文件偏移,单位:字（物体开始出现时,视频帧在原始视频文件中相对于文件起始处的偏移）
        public long nEndFileOffset;//结束时文件偏移,单位:字节（物体消逝时,视频帧在原始视频文件中相对于文件起始处的偏移）
        public byte[] byColorSimilar = new byte[EM_COLOR_TYPE.NET_COLOR_TYPE_MAX];//物体颜色相似度,取值范围：0-100,数组下标值代表某种颜色,详见 EM_COLOR_TYPE
        public byte[] byUpperBodyColorSimilar = new byte[EM_COLOR_TYPE.NET_COLOR_TYPE_MAX];//上半身物体颜色相似度(物体类型为人时有效)
        public byte[] byLowerBodyColorSimilar = new byte[EM_COLOR_TYPE.NET_COLOR_TYPE_MAX];//下半身物体颜色相似度(物体类型为人时有效)
        public int nRelativeID;//相关物体ID
        public byte[] szSubText = new byte[20];//"ObjectType"为"Vehicle"或者"Logo"时,表示车标下的某一车系,比如奥迪A6L,由于车系较多,SDK实现时透传此字段,设备如实填写。
        public short wBrandYear;   // 车辆品牌年款 需要通过映射表得到真正的年款 映射表详见开发手册
        
        public NET_MSG_OBJECT()
        {
        	if(INetSDK.getOsName().equals("win")) {
                // 强制采用最大四字节对其
                setAlignType(ALIGN_GNUC);
        	}
        }
    }
    
    //NET_FILE_QUERY_FACE对应的人脸识别服务FINDNEXT查询返回参数
    public static class MEDIAFILE_FACERECOGNITION_INFO extends Structure
    {
        public int dwSize;//结构体大小
        public int bGlobalScenePic;//全景图是否存在, BOOL类型，取值0或1
        public NET_PIC_INFO_EX stGlobalScenePic;//全景图片文件路径
        public NET_MSG_OBJECT stuObject;//目标人脸物体信息
        public NET_PIC_INFO_EX stObjectPic;//目标人脸文件路径
        public int nCandidateNum;//当前人脸匹配到的候选对象数量
        public CANDIDATE_INFO[] stuCandidates = (CANDIDATE_INFO[])new CANDIDATE_INFO().toArray(NET_MAX_CANDIDATE_NUM);//当前人脸匹配到的候选对象信息
        public NET_CANDIDAT_PIC_PATHS[] stuCandidatesPic = (NET_CANDIDAT_PIC_PATHS[])new NET_CANDIDAT_PIC_PATHS().toArray(NET_MAX_CANDIDATE_NUM);//当前人脸匹配到的候选对象图片文件路径
        public NET_TIME stTime;//报警发生时间
        public byte[] szAddress = new byte[MAX_PATH];//报警发生地点
        public int nChannelId;//通道号
        
        public MEDIAFILE_FACERECOGNITION_INFO()
        {
            this.dwSize = this.size();
        }
    }
    
    //每个视频输入通道对应的所有事件规则：缓冲区pRuleBuf填充多个事件规则信息，每个事件规则信息内容为CFG_RULE_INFO+"事件类型对应的规则配置结构体"。
    public static class CFG_ANALYSERULES_INFO extends Structure
    {
        public int nRuleCount;//事件规则个数
        public Pointer pRuleBuf;//每个视频输入通道对应的视频分析事件规则配置缓冲, char *
        public int nRuleLen;//缓冲大小
    }
    
    // 规则通用信息
    public static class CFG_RULE_COMM_INFO extends Structure
    {
    	public byte 		bRuleId;							// 规则编号
    	public int   		emClassType;						// 规则所属的场景, EM_SCENE_TYPE
    	public byte[] 		bReserved = new byte[512];			// 保留字节
    }
    
    public static class CFG_RULE_INFO extends Structure
    {
        public int dwRuleType;//事件类型，详见dhnetsdk.h中"智能分析事件类型"
        public int nRuleSize;//该事件类型规则配置结构体大小
    	public CFG_RULE_COMM_INFO  stuRuleCommInfo;					// 规则通用信息
    }
    
    //区域顶点信息
    public static class CFG_POLYGON extends Structure
    {
        public int nX;//0~8191
        public int nY;
    }
    
    //区域信息
    public static class CFG_REGION extends Structure
    {
        public int nPointNum;
        public CFG_POLYGON[] stuPolygon = (CFG_POLYGON[])new CFG_POLYGON().toArray(MAX_POLYGON_NUM);
    }
    
    public static class CFG_SIZE_Attribute extends Union
    {
        public float nWidth;//宽
        public float nArea;//面积
    }
    
    //Size
    public static class CFG_SIZE extends Structure
    {
        public CFG_SIZE_Attribute attr;
        public float nHeight;//高
    }
    
    //校准框信息
    public static class CFG_CALIBRATEBOX_INFO extends Structure
    {
        public CFG_POLYGON stuCenterPoint;//校准框中心点坐标(点的坐标归一化到[0,8191]区间)
        public float fRatio;//相对基准校准框的比率(比如1表示基准框大小，0.5表示基准框大小的一半)
    }
    
    //尺寸过滤器
    public static class CFG_SIZEFILTER_INFO extends Structure
    {
        public int nCalibrateBoxNum;//校准框个数
        public CFG_CALIBRATEBOX_INFO[] stuCalibrateBoxs = (CFG_CALIBRATEBOX_INFO[])new CFG_CALIBRATEBOX_INFO().toArray(MAX_CALIBRATEBOX_NUM);//校准框(远端近端标定模式下有效)
        public byte bMeasureModeEnable;//计量方式参数是否有效， 类型bool, 取值0或1
        public byte bMeasureMode;//计量方式,0-像素，不需要远端、近端标定,1-实际长度，单位：米,2-远端近端标定后的像素
        public byte bFilterTypeEnable;//过滤类型参数是否有效， 类型bool, 取值0或1
        // ByArea,ByRatio仅作兼容，使用独立的ByArea和ByRatio选项代替 2012/03/06
        public byte bFilterType;//过滤类型:0:"ByLength",1:"ByArea",2"ByWidthHeight"
        public byte[] bReserved = new byte[2];//保留字段
        public byte bFilterMinSizeEnable;//物体最小尺寸参数是否有效， 类型bool, 取值0或1
        public byte bFilterMaxSizeEnable;//物体最大尺寸参数是否有效， 类型bool, 取值0或1
        public CFG_SIZE stuFilterMinSize;//物体最小尺寸"ByLength"模式下表示宽高的尺寸，"ByArea"模式下宽表示面积，高无效(远端近端标定模式下表示基准框的宽高尺寸)。
        public CFG_SIZE stuFilterMaxSize;//物体最大尺寸"ByLength"模式下表示宽高的尺寸，"ByArea"模式下宽表示面积，高无效(远端近端标定模式下表示基准框的宽高尺寸)。
        public byte abByArea;//类型bool, 取值0或1
        public byte abMinArea;//类型bool, 取值0或1
        public byte abMaxArea;//类型bool, 取值0或1
        public byte abMinAreaSize;//类型bool, 取值0或1
        public byte abMaxAreaSize;//类型bool, 取值0或1
        public byte bByArea;//是否按面积过滤通过能力ComplexSizeFilter判断是否可用， 类型bool, 取值0或1
        public byte[] bReserved1 = new byte[2];
        public float nMinArea;//最小面积
        public float nMaxArea;//最大面积
        public CFG_SIZE stuMinAreaSize;//最小面积矩形框尺寸"计量方式"为"像素"时，表示最小面积矩形框的宽高尺寸；"计量方式"为"远端近端标定模式"时，表示基准框的最小宽高尺寸；
        public CFG_SIZE stuMaxAreaSize;//最大面积矩形框尺寸,同上
        public byte abByRatio;//类型bool, 取值0或1
        public byte abMinRatio;//类型bool, 取值0或1
        public byte abMaxRatio;//类型bool, 取值0或1
        public byte abMinRatioSize;//类型bool, 取值0或1
        public byte abMaxRatioSize;//类型bool, 取值0或1
        public byte bByRatio;//是否按宽高比过滤通过能力ComplexSizeFilter判断是否可用， 类型bool, 取值0或1
        public byte[] bReserved2 = new byte[2];
        public double dMinRatio;//最小宽高比
        public double dMaxRatio;//最大宽高比
        public CFG_SIZE stuMinRatioSize;//最小宽高比矩形框尺寸，最小宽高比对应矩形框的宽高尺寸。
        public CFG_SIZE stuMaxRatioSize;//最大宽高比矩形框尺寸，同上
        public int nAreaCalibrateBoxNum;//面积校准框个数
        public CFG_CALIBRATEBOX_INFO[] stuAreaCalibrateBoxs = (CFG_CALIBRATEBOX_INFO[])new CFG_CALIBRATEBOX_INFO().toArray(MAX_CALIBRATEBOX_NUM);//面积校准框
        public int nRatioCalibrateBoxs;//宽高校准框个数
        public CFG_CALIBRATEBOX_INFO[] stuRatioCalibrateBoxs = (CFG_CALIBRATEBOX_INFO[])new CFG_CALIBRATEBOX_INFO().toArray(MAX_CALIBRATEBOX_NUM);//宽高校准框
        public byte abBySize;//长宽过滤使能参数是否有效， 类型bool, 取值0或1
        public byte bBySize;//长宽过滤使能， 类型bool, 取值0或1
    }
    
    //各种物体特定的过滤器
    public static class CFG_OBJECT_SIZEFILTER_INFO extends Structure
    {
        public byte[] szObjectType = new byte[MAX_NAME_LEN];//物体类型
        public CFG_SIZEFILTER_INFO stSizeFilter;//对应的尺寸过滤器
    }
    
    //特殊区域的属性类型
    public static class EM_SEPCIALREGION_PROPERTY_TYPE extends Structure
    {
         public static final int EM_SEPCIALREGION_PROPERTY_TYPE_HIGHLIGHT = 1;//高亮，键盘检测区域具有此特性
         public static final int EM_SEPCIALREGION_PROPERTY_TYPE_REGULARBLINK = 2; //规律的闪烁，插卡区域具有此特性
         public static final int EM_SEPCIALREGION_PROPERTY_TYPE_IREGULARBLINK = 3; //不规律的闪烁，屏幕区域具有此特性
         public static final int EM_SEPCIALREGION_PROPERTY_TYPE_NUM = 4; 
    }
    
    //特殊检测区，是指从检测区中区分出来，有特殊检测属性的区域
    public static class CFG_SPECIALDETECT_INFO extends Structure
    {
        public int nDetectNum;//检测区域顶点数
        public CFG_POLYGON[] stDetectRegion = (CFG_POLYGON[])new CFG_POLYGON().toArray(MAX_POLYGON_NUM);//检测区域
        public int nPropertyNum;//特殊检测区属性个数
        public int[] nPropertys = new int[EM_SEPCIALREGION_PROPERTY_TYPE.EM_SEPCIALREGION_PROPERTY_TYPE_NUM];//特殊检测区属性
    }
    
    //各类物体的子类型
    public static class CFG_CATEGORY_TYPE extends Structure
    {
        public static final int CFG_CATEGORY_TYPE_UNKNOW = 0; //未知类型
         //车型相关子类别
         public static final int CFG_CATEGORY_VEHICLE_TYPE_MOTOR = 1; //"Motor"机动车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_NON_MOTOR = 2; //"Non-Motor"非机动车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_BUS = 3; //"Bus"公交车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_BICYCLE = 4; //"Bicycle"自行车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_MOTORCYCLE = 5; //"Motorcycle"摩托车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_UNLICENSEDMOTOR = 6; //"UnlicensedMotor":无牌机动车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_LARGECAR = 7; //"LargeCar"大型汽车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_MICROCAR = 8; //"MicroCar"小型汽车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_EMBASSYCAR = 9; //"EmbassyCar"使馆汽车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_MARGINALCAR = 10; //"MarginalCar"领馆汽车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_AREAOUTCAR = 11; //"AreaoutCar"境外汽车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_FOREIGNCAR = 12; //"ForeignCar"外籍汽车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_DUALTRIWHEELMOTORCYCLE = 13; //"DualTriWheelMotorcycle"两、三轮摩托车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_LIGHTMOTORCYCLE = 14; //"LightMotorcycle"轻便摩托车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_EMBASSYMOTORCYCLE = 15 ; //"EmbassyMotorcycle"使馆摩托车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_MARGINALMOTORCYCLE = 16; //"MarginalMotorcycle"领馆摩托车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_AREAOUTMOTORCYCLE = 17; //"AreaoutMotorcycle"境外摩托车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_FOREIGNMOTORCYCLE = 18; //"ForeignMotorcycle"外籍摩托车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_FARMTRANSMITCAR = 19; //"FarmTransmitCar"农用运输车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_TRACTOR = 20; //"Tractor"拖拉机
         public static final int CFG_CATEGORY_VEHICLE_TYPE_TRAILER = 21; //"Trailer"挂车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_COACHCAR = 22; //"CoachCar"教练汽车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_COACHMOTORCYCLE = 23; //"CoachMotorcycle"教练摩托车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_TRIALCAR = 24; //"TrialCar"试验汽车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_TRIALMOTORCYCLE = 25; //"TrialMotorcycle"试验摩托车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_TEMPORARYENTRYCAR = 26; //"TemporaryEntryCar"临时入境汽车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_TEMPORARYENTRYMOTORCYCLE = 27; //"TemporaryEntryMotorcycle"临时入境摩托车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_TEMPORARYSTEERCAR = 28; //"TemporarySteerCar"临时行驶车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_PASSENGERCAR = 29; //"PassengerCar"客车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_LARGETRUCK = 30; //"LargeTruck"大货车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_MIDTRUCK =31 ; //"MidTruck"中货车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_SALOONCAR = 32; //"SaloonCar"轿车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_MICROBUS = 33; //"Microbus"面包车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_MICROTRUCK = 34; //"MicroTruck"小货车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_TRICYCLE = 35; //"Tricycle"三轮车
         public static final int CFG_CATEGORY_VEHICLE_TYPE_PASSERBY = 36; //"Passerby"行人
         //车牌相关子类别
         public static final int CFG_CATEGORY_PLATE_TYPE_NORMAL = 37; //"Normal"蓝牌黑字
         public static final int CFG_CATEGORY_PLATE_TYPE_YELLOW = 38; //"Yellow"黄牌
         public static final int CFG_CATEGORY_PLATE_TYPE_DOUBLEYELLOW = 39; //"DoubleYellow"双层黄尾牌
         public static final int CFG_CATEGORY_PLATE_TYPE_POLICE = 40; //"Police"警牌
         public static final int CFG_CATEGORY_PLATE_TYPE_ARMED = 41; //"Armed"武警牌
         public static final int CFG_CATEGORY_PLATE_TYPE_MILITARY = 42; //"Military"部队号牌
         public static final int CFG_CATEGORY_PLATE_TYPE_DOUBLEMILITARY = 43; //"DoubleMilitary"部队双层
         public static final int CFG_CATEGORY_PLATE_TYPE_SAR = 44; //"SAR"港澳特区号牌
         public static final int CFG_CATEGORY_PLATE_TYPE_TRAINNING = 45; //"Trainning"教练车号牌
         public static final int CFG_CATEGORY_PLATE_TYPE_PERSONAL = 46; //"Personal"个性号牌
         public static final int CFG_CATEGORY_PLATE_TYPE_AGRI = 47; //"Agri"农用牌
         public static final int CFG_CATEGORY_PLATE_TYPE_EMBASSY = 48; //"Embassy"使馆号牌
         public static final int CFG_CATEGORY_PLATE_TYPE_MOTO = 49; //"Moto"摩托车号牌
         public static final int CFG_CATEGORY_PLATE_TYPE_TRACTOR = 50; //"Tractor"拖拉机号牌
         public static final int CFG_CATEGORY_PLATE_TYPE_OFFICIALCAR = 51; //"OfficialCar"公务车
         public static final int CFG_CATEGORY_PLATE_TYPE_PERSONALCAR = 52; //"PersonalCar"私家车
         public static final int CFG_CATEGORY_PLATE_TYPE_WARCAR = 53; //"WarCar"军用
         public static final int CFG_CATEGORY_PLATE_TYPE_OTHER = 54; //"Other"其他号牌
         public static final int CFG_CATEGORY_PLATE_TYPE_CIVILAVIATION = 55; //"Civilaviation"民航号牌
         public static final int CFG_CATEGORY_PLATE_TYPE_BLACK = 56; //"Black"黑牌
    }
    
    //不同区域各种类型物体的检测模块配置
    public static class CFG_MODULE_INFO extends Structure
    {
        // 信息
        public byte[] szObjectType = new byte[MAX_NAME_LEN];//默认物体类型,详见"支持的检测物体类型列表"
        public byte bSnapShot;//是否对识别物体抓图，类型bool，取值0或1
        public byte bSensitivity;//灵敏度,取值1-10，值越小灵敏度越低
        public byte bMeasureModeEnable;//计量方式参数是否有效，类型bool，取值0或1
        public byte bMeasureMode;//计量方式,0-像素，不需要远端、近端标定,1-实际长度，单位：米,2-远端近端标定后的像素
        public int nDetectRegionPoint;//检测区域顶点数
        public CFG_POLYGON[] stuDetectRegion = (CFG_POLYGON[])new CFG_POLYGON().toArray(MAX_POLYGON_NUM);//检测区域
        public int nTrackRegionPoint;//跟踪区域顶点数
        public CFG_POLYGON[] stuTrackRegion = (CFG_POLYGON[])new CFG_POLYGON().toArray(MAX_POLYGON_NUM);//跟踪区域
        public byte bFilterTypeEnable;//过滤类型参数是否有效，类型bool，取值0或1
        // ByArea,ByRatio仅作兼容使枚懒⒌腂yArea和ByRatio选项代替 2012/03/06
        public byte nFilterType;//过滤类型:0:"ByLength",1:"ByArea",2:"ByWidthHeight",3:"ByRatio":
        public byte bBackgroudEnable;//区域的背景类型参数是否有效，类型bool，取值0或1
        public byte bBackgroud;//区域的背景类型,0-普通类型,1-高光类型
        public byte abBySize;//长宽过滤使能参数是否有效，类型bool，取值0或1
        public byte bBySize;//长宽过滤使能，类型bool，取值0或1
        public byte bFilterMinSizeEnable;//物体最小尺寸参数是否有效，类型bool，取值0或1
        public byte bFilterMaxSizeEnable;//物体最大尺寸参数是否有效，类型bool，取值0或1
        public CFG_SIZE stuFilterMinSize;//物体最小尺寸"ByLength"模式下表示宽高的尺寸，"ByArea"模式下宽表示面积，高无效。
        public CFG_SIZE stuFilterMaxSize;//物体最大尺寸"ByLength"模式下表示宽高的尺寸，"ByArea"模式下宽表示面积，高无效。
        public int nExcludeRegionNum;//排除区域数
        public CFG_REGION[] stuExcludeRegion = (CFG_REGION[])new CFG_REGION().toArray(MAX_EXCLUDEREGION_NUM);//排除区域
        public int nCalibrateBoxNum;//校准框个数
        public CFG_CALIBRATEBOX_INFO[] stuCalibrateBoxs = (CFG_CALIBRATEBOX_INFO[])new CFG_CALIBRATEBOX_INFO().toArray(MAX_CALIBRATEBOX_NUM);//校准框(远端近端标定模式下有效)
        public byte bAccuracy;//检测精度是否有效，类型bool，取值0或1
        public byte byAccuracy;//检测精度
        public byte bMovingStep;//算法移动步长是否有效，类型bool，取值0或1
        public byte byMovingStep;//算法移动步长
        public byte bScalingFactor;//算法缩放因子是否有效，类型bool，取值0或1
        public byte byScalingFactor;//算法缩放因子
        public byte[] bReserved2 = new byte[1];//保留字段
        public byte abDetectBalance;//漏检和误检平衡参数是否有效，类型bool，取值0或1
        public int nDetectBalance;//漏检和误检平衡0-折中模式(默认)1-漏检更少2-误检更少
        public byte abByRatio;//类型bool，取值0或1
        public byte abMinRatio;;//类型bool，取值0或1
        public byte abMaxRatio;;//类型bool，取值0或1
        public byte abMinAreaSize;;//类型bool，取值0或1
        public byte abMaxAreaSize;;//类型bool，取值0或1
        public byte bByRatio;//是否按宽高比过滤通过能力ComplexSizeFilter判断是否可用可以和nFilterType复用，类型bool，取值0或1
        public byte[] bReserved1 = new byte[2];
        public double dMinRatio;//最小宽高比
        public double dMaxRatio;//最大宽高比
        public CFG_SIZE stuMinAreaSize;//最小面积矩形框尺寸"计量方式"为"像素"时，表示最小面积矩形框的宽高尺寸；"计量方式"为"远端近端标定模式"时，表示基准框的最小宽高尺寸；
        public CFG_SIZE stuMaxAreaSize;//最大面积矩形框尺寸,同上
        public byte abByArea;//类型bool，取值0或1
        public byte abMinArea;//类型bool，取值0或1
        public byte abMaxArea;//类型bool，取值0或1
        public byte abMinRatioSize;//类型bool，取值0或1
        public byte abMaxRatioSize;//类型bool，取值0或1
        public byte bByArea;//是否按面积过滤通过能力ComplexSizeFilter判断是否可用可以和nFilterType复用，类型bool，取值0或1
        public byte[] bReserved3 = new byte[2];
        public float nMinArea;//最小面积
        public float nMaxArea;//最大面积
        public CFG_SIZE stuMinRatioSize;//最小宽高比矩形框尺寸，最小宽高比对应矩形框的宽高尺寸。
        public CFG_SIZE stuMaxRatioSize;//最大宽高比矩形框尺寸，同上
        public int nAreaCalibrateBoxNum;//面积校准框个数
        public CFG_CALIBRATEBOX_INFO[] stuAreaCalibrateBoxs = (CFG_CALIBRATEBOX_INFO[])new CFG_CALIBRATEBOX_INFO().toArray(MAX_CALIBRATEBOX_NUM);//面积校准框
        public int nRatioCalibrateBoxs;//比例校准框个数
        public CFG_CALIBRATEBOX_INFO[] stuRatioCalibrateBoxs = (CFG_CALIBRATEBOX_INFO[])new CFG_CALIBRATEBOX_INFO().toArray(MAX_CALIBRATEBOX_NUM);//比例校准框个数
        public byte bAntiDisturbance;//是否开启去扰动模块，类型bool，取值0或1
        public byte bBacklight;//是否有逆光，类型bool，取值0或1
        public byte bShadow;//是否有阴影，类型bool，取值0或1
        public byte bContourAssistantTrack;//是否开启轮廓辅助跟踪，例：在人脸识别时可以通过跟踪人体来辅助识别脸，类型bool，取值0或1
        public int nPtzPresetId;//云台预置点，0~255，0表示固定场景，忽略预置点。大于0表示在此预置点时模块有效
        public int nObjectFilterNum;//物体特定的过滤器个数
        public CFG_OBJECT_SIZEFILTER_INFO[] stObjectFilter= (CFG_OBJECT_SIZEFILTER_INFO[])new CFG_OBJECT_SIZEFILTER_INFO().toArray(MAX_OBJECT_LIST_SIZE);//物体特定的过滤器信息
        public int abObjectImageSize; //BOOL类型，取值0或1
        public CFG_SIZE stObjectImageSize;//保证物体图像尺寸相同,单位是像素,不支持小数，取值：>=0,0表示自动调整大小
        public int nSpecailDetectNum;//特殊检测区域个数
        public CFG_SPECIALDETECT_INFO[] stSpecialDetectRegions= (CFG_SPECIALDETECT_INFO[])new CFG_SPECIALDETECT_INFO().toArray(MAX_SPECIALDETECT_NUM);//特殊检测区信息
        public int nAttribute;//需要识别物体的属性个数， 类型为unsigned int
        public byte[] szAttributes = new byte[MAX_OBJECT_ATTRIBUTES_SIZE*MAX_NAME_LEN];//需要识别物体的属性列表，“Category”
        public int abPlateAnalyseMode;//nPlateAnalyseMode是否有效, BOOL类型，取值0或1
        public int nPlateAnalyseMode;//车牌识别模式，0-只识别车头牌照1-只识别车尾牌照2-车头牌照优先（场景中大部分车均是车头牌照）3-车尾牌照优先（场景中大部分车均是车尾牌照）
        //szAttributes属性存在"Category"时生效
        public int nCategoryNum;//需要识别物体的子类型总数
        public int[] emCategoryType= new int[MAX_CATEGORY_TYPE_NUMBER];//子类型信息, 元素取CFG_CATEGORY_TYPE中的值
        public byte[] szSceneType = new byte[CFG_COMMON_STRING_16];		// 检测区参数用于的场景类型
    }
    
    public static class CFG_ANALYSEMODULES_INFO extends Structure
    {
        public int nMoudlesNum;//检测模块数
        public CFG_MODULE_INFO[] stuModuleInfo= (CFG_MODULE_INFO[])new CFG_MODULE_INFO().toArray(MAX_ANALYSE_MODULE_NUM);//每个视频输入通道对应的各种类型物体的检测模块配置
    }
    
    // CLIENT_FindGroupInfo接口输入参数
    public static class NET_IN_FIND_GROUP_INFO extends Structure
    {
        public int dwSize;
        public byte[] szGroupId = new byte[NET_COMMON_STRING_64];//人员组ID,唯一标识一组人员,为空表示查询全部人员组信息
        
        public NET_IN_FIND_GROUP_INFO()
        {
            this.dwSize = this.size();
        }
    }
    
    // 人脸数据类型
    public static class EM_FACE_DB_TYPE extends Structure
    {
        public static final int NET_FACE_DB_TYPE_UNKOWN = 0; 
        public static final int NET_FACE_DB_TYPE_HISTORY = 1; //历史数据库,存放的是检测出的人脸信息,一般没有包含人脸对应人员信息
        public static final int NET_FACE_DB_TYPE_BLACKLIST = 2;//黑名单数据库
        public static final int NET_FACE_DB_TYPE_WHITELIST = 3; //白名单数据库,废弃
        public static final int NET_FACE_DB_TYPE_ALARM = 4;//报警库
    }
    
    // 人员组信息
    public static class NET_FACERECONGNITION_GROUP_INFO extends Structure
    {
        public int dwSize;
        public int emFaceDBType;//人员组类型,详见EM_FACE_DB_TYPE, 取值为EM_FACE_DB_TYPE中的值
        public byte[] szGroupId = new byte[NET_COMMON_STRING_64];//人员组ID,唯一标识一组人员(不可修改,添加操作时无效)
        public byte[] szGroupName = new byte[NET_COMMON_STRING_128];//人员组名称
        public byte[] szGroupRemarks = new byte[NET_COMMON_STRING_256];//人员组备注信息
        public int nGroupSize;//当前组内人员数
        public int	nSimilarity;				   					  // 库相似度阈值，人脸比对高于阈值认为匹配成功
        public int	nRetChnCount;									  // 实际返回的通道号个数
        public int[] nChannel = new int[NET_MAX_CAMERA_CHANNEL_NUM];  // 当前组绑定到的视频通道号列表
        
        public NET_FACERECONGNITION_GROUP_INFO()
        {
            this.dwSize = this.size();
        }
    }
    
    // CLIENT_FindGroupInfo接口输出参数
    public static class NET_OUT_FIND_GROUP_INFO extends Structure
    {
        public int dwSize;
        public Pointer pGroupInfos;//人员组信息,由用户申请空间， 指向NET_FACERECONGNITION_GROUP_INFO的指针
        public int nMaxGroupNum;//当前申请的数组大小
        public int nRetGroupNum;//设备返回的人员组个数
        
        public NET_OUT_FIND_GROUP_INFO()
        {
            this.dwSize = this.size();
        }
    }

    // 人员组操作枚举
    public static class EM_OPERATE_FACERECONGNITION_GROUP_TYPE extends Structure
    {
        public static final int NET_FACERECONGNITION_GROUP_UNKOWN = 0;
        public static final int NET_FACERECONGNITION_GROUP_ADD = 1; //添加人员组信息
        public static final int NET_FACERECONGNITION_GROUP_MODIFY = 2; //修改人员组信息
        public static final int NET_FACERECONGNITION_GROUP_DELETE = 3; //删除人员组信息
    }
    
    // CLIENT_OperateFaceRecognitionGroup接口输入参数
    public static class NET_IN_OPERATE_FACERECONGNITION_GROUP extends Structure
    {
        public int dwSize;
        public int emOperateType;//操作类型, 取值为EM_OPERATE_FACERECONGNITION_GROUP_TYPE中的值
        public Pointer pOPerateInfo;//相关操作信息，指向void *
        
        public NET_IN_OPERATE_FACERECONGNITION_GROUP()
        {
            this.dwSize = this.size();
        }
    }
    
    // CLIENT_OperateFaceRecognitionGroup接口输出参数
    public static class NET_OUT_OPERATE_FACERECONGNITION_GROUP extends Structure
    {
        public int dwSize;
        public byte[] szGroupId = new byte[NET_COMMON_STRING_64];//新增记录的人员组ID,唯一标识一组人员
        
        public NET_OUT_OPERATE_FACERECONGNITION_GROUP()
        {
            this.dwSize = this.size();
        }
    }
    
    // CLIENT_SetGroupInfoForChannel接口输入参数
    public static class NET_IN_SET_GROUPINFO_FOR_CHANNEL extends Structure
    {
        public int dwSize;
        public int nChannelID;//通道号
        public int nGroupIdNum;//人员组数
        public byte[] szGroupId = new byte[MAX_GOURP_NUM*NET_COMMON_STRING_64];//人员组ID
        
        public NET_IN_SET_GROUPINFO_FOR_CHANNEL()
        {
            this.dwSize = this.size();
        }
    }
    
    // CLIENT_SetGroupInfoForChannel接口输出参数
    public static class NET_OUT_SET_GROUPINFO_FOR_CHANNEL extends Structure
    {
        public int dwSize;
        
        public NET_OUT_SET_GROUPINFO_FOR_CHANNEL()
        {
            this.dwSize = this.size();
        }
    }
    
    // 人脸查询状态信息回调函数, lAttachHandle是CLIENT_AttachFaceFindState的返回值
    public static class NET_CB_FACE_FIND_STATE extends Structure
    {
        public int dwSize;
        public int nToken;//视频浓缩任务数据库主键ID
        public int nProgress;//正常取值范围：0-100,-1,表示查询token不存在(当订阅一个不存在或结束的查询时)
        public int nCurrentCount;//目前符合查询条件的人脸数量
        
        public NET_CB_FACE_FIND_STATE()
        {
            this.dwSize = this.size();
        }
    }
    
    //CLIENT_AttachFaceFindState接口输入参数
    public static class NET_IN_FACE_FIND_STATE extends Structure
    {
        public int dwSize;//结构体大小,必须填写
        public int nTokenNum;//查询令牌数,为0时,表示订阅所有的查询任务
        public Pointer nTokens;//查询令牌, 指向int的指针
        public StdCallCallback cbFaceFindState; //回调函数 fFaceFindState 回调
        public NativeLong dwUser;//用户数据
        
        public NET_IN_FACE_FIND_STATE()
        {
            this.dwSize = this.size();
        }
    }
    
    //CLIENT_AttachFaceFindState接口输入参数
    public static class NET_OUT_FACE_FIND_STATE extends Structure
    {
        public int dwSize;
        
        public NET_OUT_FACE_FIND_STATE()
        {
            this.dwSize = this.size();
        }
    }
    
    // SDK全局日志打印信息
    public static class LOG_SET_PRINT_INFO extends Structure
    {
        public int dwSize;
        public int bSetFilePath;//是否重设日志路径, BOOL类型，取值0或1
        public byte[] szLogFilePath = new byte[MAX_LOG_PATH_LEN];//日志路径(默认"./sdk_log/sdk_log.log")
        public int bSetFileSize;//是否重设日志文件大小, BOOL类型，取值0或1
        public int nFileSize;//每个日志文件的大小(默认大小10240),单位:比特, 类型为unsigned int
        public int bSetFileNum;//是否重设日志文件个数, BOOL类型，取值0或1
        public int nFileNum;//绕接日志文件个数(默认大小10), 类型为unsigned int
        public int bSetPrintStrategy;//是否重设日志打印输出策略, BOOL类型，取值0或1
        public int nPrintStrategy;//日志输出策略,0:输出到文件(默认); 1:输出到窗口, 类型为unsigned int
        
        public LOG_SET_PRINT_INFO()
        {
            this.dwSize = this.size();
        }
    }
    
    // media文件查询条件
    public static class EM_FILE_QUERY_TYPE extends Structure
    {
        public static final int NET_FILE_QUERY_TRAFFICCAR = 0; //交通车辆信息
        public static final int NET_FILE_QUERY_ATM = 1; //ATM信息
        public static final int NET_FILE_QUERY_ATMTXN = 2; //ATM交易信息
        public static final int NET_FILE_QUERY_FACE = 3; //人脸信息 MEDIAFILE_FACERECOGNITION_PARAM和MEDIAFILE_FACERECOGNITION_INFO
        public static final int NET_FILE_QUERY_FILE = 4; //文件信息对应 NET_IN_MEDIA_QUERY_FILE 和 NET_OUT_MEDIA_QUERY_FILE
        public static final int NET_FILE_QUERY_TRAFFICCAR_EX = 5; //交通车辆信息,扩展NET_FILE_QUERY_TRAFFICCAR,支持更多的字段
        public static final int NET_FILE_QUERY_FACE_DETECTION = 6; //人脸检测事件信息MEDIAFILE_FACE_DETECTION_PARAM和 MEDIAFILE_FACE_DETECTION_INFO
    }
    
    // 查询跳转条件
    public static class NET_FINDING_JUMP_OPTION_INFO extends Structure
    {
        public int dwSize;
        public int nOffset;//查询结果偏移量,是相对于当前查询的第一条查询结果的位置偏移
        
        public NET_FINDING_JUMP_OPTION_INFO()
        {
            this.dwSize = this.size();
        }
    }
    
    // 云台联动类型
    public static class CFG_LINK_TYPE extends Structure
    {
        public static final int LINK_TYPE_NONE = 0; //无联动
        public static final int LINK_TYPE_PRESET = 1; //联动预置点
        public static final int LINK_TYPE_TOUR = 2; //联动巡航
        public static final int LINK_TYPE_PATTERN = 3; //联动轨迹
    }

    // 联动云台信息
    public static class CFG_PTZ_LINK extends Structure
    {
        public int emType;//联动类型, 取值为CFG_LINK_TYPE中的值
        public int nValue;//联动取值分别对应预置点号，巡航号等等
    }

    // 联动云台信息扩展
    public static class CFG_PTZ_LINK_EX extends Structure
    {
        public int emType;//联动类型, 取值为CFG_LINK_TYPE中的值
        public int nParam1;//联动参数1
        public int nParam2;//联动参数2
        public int nParam3;//联动参数3
        public int nChannelID;//所联动云台通道
    }

    // RGBA信息
    public static class CFG_RGBA extends Structure
    {
        public int nRed;
        public int nGreen;
        public int nBlue;
        public int nAlpha;
    }

    // 事件标题内容结构体
    public static class CFG_EVENT_TITLE extends Structure
    {
        public byte[] szText = new byte[MAX_CHANNELNAME_LEN];
        public CFG_POLYGON stuPoint;//标题左上角坐标,采用0-8191相对坐标系
        public CFG_SIZE stuSize;//标题的宽度和高度,采用0-8191相对坐标系，某项或者两项为0表示按照字体自适应宽高
        public CFG_RGBA stuFrontColor;//前景颜色
        public CFG_RGBA stuBackColor;//背景颜色
    }

    // 邮件附件类型
    public static class CFG_ATTACHMENT_TYPE extends Structure
    {
        public static final int ATTACHMENT_TYPE_PIC = 0; //图片附件
        public static final int ATTACHMENT_TYPE_VIDEO = 1; //视频附件
        public static final int ATTACHMENT_TYPE_NUM = 2; //附件类型总数
    }

    // 分割模式
    public static class CFG_SPLITMODE extends Structure
    {
        public static final int SPLITMODE_1 =1;//1画面
        public static final int SPLITMODE_2 =2;//2画面
        public static final int SPLITMODE_4 =4;//4画面
        public static final int SPLITMODE_6 =6;//6画面
        public static final int SPLITMODE_8 =8;//8画面
        public static final int SPLITMODE_9 =9;//9画面
        public static final int SPLITMODE_12 =12;//12画面
        public static final int SPLITMODE_16 =16;//16画面
        public static final int SPLITMODE_20 =20;//20画面
        public static final int SPLITMODE_25 =25;//25画面
        public static final int SPLITMODE_36 =36;//36画面
        public static final int SPLITMODE_64 =64;//64画面
        public static final int SPLITMODE_144 =144;//144画面
        public static final int SPLITMODE_PIP =1000;//画中画分割模式基础值
        public static final int SPLITMODE_PIP1 =SPLITMODE_PIP+1;//画中画模式, 1个全屏大画面+1个小画面窗口
        public static final int SPLITMODE_PIP3 =SPLITMODE_PIP+3;//画中画模式, 1个全屏大画面+3个小画面窗口
        public static final int SPLITMODE_FREE =SPLITMODE_PIP*2;//自由开窗模式，可以自由创建、关闭窗口，自由设置窗口位置和Z轴次序
        public static final int SPLITMODE_COMPOSITE_1 = SPLITMODE_PIP * 3 + 1;  // 融合屏成员1分割
        public static final int SPLITMODE_COMPOSITE_4 = SPLITMODE_PIP * 3 + 4;  // 融合屏成员4分割
        public static final int SPLITMODE_EOF = SPLITMODE_COMPOSITE_4+1; //结束标识
    }

    // 轮巡联动配置
    public static class CFG_TOURLINK extends Structure
    {
        public int bEnable;//轮巡使能, BOOL类型，取值0或1
        public int emSplitMode;//轮巡时的分割模式,取值范围为CFG_SPLITMODE中的值
        public int[] nChannels = new int[MAX_VIDEO_CHANNEL_NUM];//轮巡通道号列表
        public int nChannelCount;//轮巡通道数量
    }

    // 门禁操作类型
    public static class EM_CFG_ACCESSCONTROLTYPE extends Structure
    {
        public static final int EM_CFG_ACCESSCONTROLTYPE_NULL = 0;//不做操作
        public static final int EM_CFG_ACCESSCONTROLTYPE_AUTO = 1; //自动
        public static final int EM_CFG_ACCESSCONTROLTYPE_OPEN = 2; //开门
        public static final int EM_CFG_ACCESSCONTROLTYPE_CLOSE = 3; //关门
        public static final int EM_CFG_ACCESSCONTROLTYPE_OPENALWAYS = 4; //永远开启
        public static final int EM_CFG_ACCESSCONTROLTYPE_CLOSEALWAYS = 5; //永远关闭
    }

    // 邮件详细内容
    public static class CFG_MAIL_DETAIL extends Structure
    {
        public int emAttachType;//附件类型, 取值范围为CFG_ATTACHMENT_TYPE中的值
        public int nMaxSize;//文件大小上限，单位kB
        public int nMaxTimeLength;//最大录像时间长度，单位秒，对video有效
    }

    // 语音呼叫发起方
    public static class EM_CALLER_TYPE extends Structure
    {
        public static final int EM_CALLER_DEVICE = 0;//设备发起
    }

    // 呼叫协议
    public static class EM_CALLER_PROTOCOL_TYPE extends Structure
    {
        public static final int EM_CALLER_PROTOCOL_CELLULAR = 0;//手机方式
    }

    // 语音呼叫联动信息
    public static class CFG_TALKBACK_INFO extends Structure
    {
        public int bCallEnable;//语音呼叫使能, BOOL类型，取值0或1
        public int emCallerType;//语音呼叫发起方, 取值范围为EM_CALLER_TYPE中的值
        public int emCallerProtocol;//语音呼叫协议, 取值范围为EM_CALLER_PROTOCOL_TYPE中的值
    }

    // 电话报警中心联动信息
    public static class CFG_PSTN_ALARM_SERVER extends Structure
    {
        public int bNeedReport;//是否上报至电话报警中心, BOOL类型，取值0或1
        public int nServerCount;//电话报警服务器个数
        public byte[] byDestination = new byte[MAX_PSTN_SERVER_NUM];//上报的报警中心下标,详见配置CFG_PSTN_ALARM_CENTER_INFO
    }

    // 时间表信息
    public static class CFG_TIME_SCHEDULE extends Structure
    {
        public int bEnableHoliday;//是否支持节假日配置，默认为不支持，除非获取配置后返回为TRUE，不要使能假日配置, BOOL类型，取值0或1
        public CFG_TIME_SECTION[] stuTimeSection = (CFG_TIME_SECTION[])new CFG_TIME_SECTION().toArray(MAX_TIME_SCHEDULE_NUM*MAX_REC_TSECT);//第一维前7个元素对应每周7天，第8个元素对应节假日，每天最多6个时间段
    }

    // 报警联动信息
    public static class CFG_ALARM_MSG_HANDLE extends Structure
    {
        //能力
        public byte abRecordMask;//类型bool, 取值0或者1
        public byte abRecordEnable;//类型bool, 取值0或者1
        public byte abRecordLatch;//类型bool, 取值0或者1
        public byte abAlarmOutMask;//类型bool, 取值0或者1
        public byte abAlarmOutEn;//类型bool, 取值0或者1
        public byte abAlarmOutLatch;//类型bool, 取值0或者1
        public byte abExAlarmOutMask;//类型bool, 取值0或者1
        public byte abExAlarmOutEn;//类型bool, 取值0或者1
        public byte abPtzLinkEn;//类型bool, 取值0或者1
        public byte abTourMask;//类型bool, 取值0或者1
        public byte abTourEnable;//类型bool, 取值0或者1
        public byte abSnapshot;//类型bool, 取值0或者1
        public byte abSnapshotEn;//类型bool, 取值0或者1
        public byte abSnapshotPeriod;//类型bool, 取值0或者1
        public byte abSnapshotTimes;//类型bool, 取值0或者1
        public byte abTipEnable;//类型bool, 取值0或者1
        public byte abMailEnable;//类型bool, 取值0或者1
        public byte abMessageEnable;//类型bool, 取值0或者1
        public byte abBeepEnable;//类型bool, 取值0或者1
        public byte abVoiceEnable;//类型bool, 取值0或者1
        public byte abMatrixMask;//类型bool, 取值0或者1
        public byte abMatrixEnable;//类型bool, 取值0或者1
        public byte abEventLatch;//类型bool, 取值0或者1
        public byte abLogEnable;//类型bool, 取值0或者1
        public byte abDelay;//类型bool, 取值0或者1
        public byte abVideoMessageEn;//类型bool, 取值0或者1
        public byte abMMSEnable;//类型bool, 取值0或者1
        public byte abMessageToNetEn;//类型bool, 取值0或者1
        public byte abTourSplit;//类型bool, 取值0或者1
        public byte abSnapshotTitleEn;//类型bool, 取值0或者1
        public byte abChannelCount;//类型bool, 取值0或者1
        public byte abAlarmOutCount;//类型bool, 取值0或者1
        public byte abPtzLinkEx;//类型bool, 取值0或者1
        public byte abSnapshotTitle;//类型bool, 取值0或者1
        public byte abMailDetail;//类型bool, 取值0或者1
        public byte abVideoTitleEn;//类型bool, 取值0或者1
        public byte abVideoTitle;//类型bool, 取值0或者1
        public byte abTour;//类型bool, 取值0或者1
        public byte abDBKeys;//类型bool, 取值0或者1
        public byte abJpegSummary;//类型bool, 取值0或者1
        public byte abFlashEn;//类型bool, 取值0或者1
        public byte abFlashLatch;//类型bool, 取值0或者1
        //信息
        public int nChannelCount;//设备的视频通道数
        public int nAlarmOutCount;//设备的报警输出个数
        public int[] dwRecordMask = new int[MAX_CHANNEL_COUNT];//录像通道掩码(按位)
        public int bRecordEnable;//录像使能, BOOL类型，取值0或1
        public int nRecordLatch;//录像延时时间(秒)
        public int[] dwAlarmOutMask = new int[MAX_CHANNEL_COUNT];//报警输出通道掩码
        public int bAlarmOutEn;//报警输出使能, BOOL类型，取值0或1
        public int nAlarmOutLatch;//报警输出延时时间(秒)
        public int[] dwExAlarmOutMask = new int[MAX_CHANNEL_COUNT];//扩展报警输出通道掩码
        public int bExAlarmOutEn;//扩展报警输出使能, BOOL类型，取值0或1
        public CFG_PTZ_LINK [] stuPtzLink = (CFG_PTZ_LINK [])new CFG_PTZ_LINK().toArray(MAX_VIDEO_CHANNEL_NUM);//云台联动项
        public int bPtzLinkEn;//云台联动使能, BOOL类型，取值0或1
        public int[] dwTourMask = new int[MAX_CHANNEL_COUNT];//轮询通道掩码
        public int bTourEnable;//轮询使能, BOOL类型，取值0或1
        public int[] dwSnapshot = new int[MAX_CHANNEL_COUNT];//快照通道号掩码
        public int bSnapshotEn;//快照使能, BOOL类型，取值0或1
        public int nSnapshotPeriod;//连拍周期(秒)
        public int nSnapshotTimes;//连拍次数
        public int bTipEnable;//本地消息框提示, BOOL类型，取值0或1
        public int bMailEnable;//发送邮件，如果有图片，作为附件, BOOL类型，取值0或1
        public int bMessageEnable;//上传到报警服务器, BOOL类型，取值0或1
        public int bBeepEnable;//蜂鸣, BOOL类型，取值0或1
        public int bVoiceEnable;//语音提示, BOOL类型，取值0或1
        public int[] dwMatrixMask = new int[MAX_CHANNEL_COUNT];//联动视频矩阵通道掩码
        public int bMatrixEnable;//联动视频矩阵, BOOL类型，取值0或1
        public int nEventLatch;//联动开始延时时间(秒)，0－15
        public int bLogEnable;//是否记录日志, BOOL类型，取值0或1
        public int nDelay;//设置时先延时再生效，单位为秒
        public int bVideoMessageEn;//叠加提示字幕到视频。叠加的字幕包括事件类型，通道号，秒计时。BOOL类型，取值0或1
        public int bMMSEnable;//发送彩信使能, BOOL类型，取值0或1
        public int bMessageToNetEn;//消息上传给网络使能, BOOL类型，取值0或1
        public int nTourSplit;//轮巡时的分割模式0:1画面;
        public int bSnapshotTitleEn;//是否叠加图片标题, BOOL类型，取值0或1
        public int nPtzLinkExNum;//云台配置数
        public CFG_PTZ_LINK_EX[] stuPtzLinkEx = (CFG_PTZ_LINK_EX[])new CFG_PTZ_LINK_EX().toArray(MAX_VIDEO_CHANNEL_NUM);//扩展云台信息
        public int nSnapTitleNum;//图片标题内容数
        public CFG_EVENT_TITLE[] stuSnapshotTitle = (CFG_EVENT_TITLE[])new CFG_EVENT_TITLE().toArray(MAX_VIDEO_CHANNEL_NUM);//图片标题内容
        public CFG_MAIL_DETAIL stuMailDetail;//邮件详细内容
        public int bVideoTitleEn;//是否叠加视频标题，主要指主码流, BOOL类型，取值0或1
        public int nVideoTitleNum;//视频标题内容数目
        public CFG_EVENT_TITLE[] stuVideoTitle = (CFG_EVENT_TITLE[])new CFG_EVENT_TITLE().toArray(MAX_VIDEO_CHANNEL_NUM);//视频标题内容
        public int nTourNum;//轮询联动数目
        public CFG_TOURLINK[] stuTour = (CFG_TOURLINK[])new CFG_TOURLINK().toArray(MAX_VIDEO_CHANNEL_NUM);//轮询联动配置
        public int nDBKeysNum;//指定数据库关键字的有效数
        public byte[] szDBKeys = new byte[MAX_DBKEY_NUM*MAX_CHANNELNAME_LEN];//指定事件详细信息里需要写到数据库的关键字
        public byte[] byJpegSummary = new byte[MAX_SUMMARY_LEN];//叠加到JPEG图片的摘要信息
        public int bFlashEnable;//是否使能补光灯, BOOL类型，取值0或1
        public int nFlashLatch;//补光灯延时时间(秒),延时时间范围：10,300]
        public byte abAudioFileName;//bool类型，取值0或1
        public byte abAlarmBellEn;//bool类型，取值0或1
        public byte abAccessControlEn;//bool类型，取值0或1
        public byte abAccessControl;//bool类型，取值0或1
        public byte[] szAudioFileName = new byte[MAX_PATH];//联动语音文件绝对路径
        public int bAlarmBellEn;//警号使能, BOOL类型，取值0或1
        public int bAccessControlEn;//门禁使能, BOOL类型，取值0或1
        public int dwAccessControl;//门禁组数
        public int[] emAccessControlType = new int[MAX_ACCESSCONTROL_NUM];//门禁联动操作信息, 元素取值范围为EM_CFG_ACCESSCONTROLTYPE中的值
        public byte abTalkBack;//bool类型，取值0或1
        public CFG_TALKBACK_INFO stuTalkback;//语音呼叫联动信息
        public byte abPSTNAlarmServer;//bool类型，取值0或1
        public CFG_PSTN_ALARM_SERVER stuPSTNAlarmServer;//电话报警中心联动信息
        public CFG_TIME_SCHEDULE stuTimeSection;//事件响应时间表
        public byte abAlarmBellLatch;//bool类型，取值0或1
        public int nAlarmBellLatch;//警号输出延时时间(10-300秒)
    }

    // 时间段信息
    public static class CFG_TIME_SECTION extends Structure
    {
        public int dwRecordMask;//录像掩码，按位分别为动态检测录像、报警录像、定时录像、Bit3~Bit15保留、Bit16动态检测抓图、Bit17报警抓图、Bit18定时抓图
        public int nBeginHour;
        public int nBeginMin;
        public int nBeginSec;
        public int nEndHour;
        public int nEndMin;
        public int nEndSec;
    }

    // 事件类型EVENT_IVS_FACERECOGNITION(人脸识别)对应的规则配置
    public static class CFG_FACERECOGNITION_INFO extends Structure
    {
    // 信息
        public byte[] szRuleName = new byte[MAX_NAME_LEN];//规则名称,不同规则不能重名
        public byte bRuleEnable;//规则使能,bool类型，取值0或1
        public byte[] bReserved = new byte[2];//保留字段
        public int nObjectTypeNum;//相应物体类型个数
        public byte[] szObjectTypes = new byte[MAX_OBJECT_LIST_SIZE*MAX_NAME_LEN];//相应物体类型列表
        public int nPtzPresetId;//云台预置点编号0~65535
        public byte bySimilarity;//相似度，必须大于该相识度才报告(1~100)
        public byte byAccuracy;//识别精度(取值1~10，随着值增大，检测精度提高，检测速度下降。最小值为1表示检测速度优先，最大值为10表示检测精度优先)
        public byte byMode;//对比模式,0-正常,1-指定人脸区域组合,
        public byte byImportantRank;//查询重要等级大于等于此等级的人员(1~10,数值越高越重要)
        public int nAreaNum;//区域数
        public byte[] byAreas = new byte[8];//人脸区域组合,0-眉毛，1-眼睛，2-鼻子，3-嘴巴，4-脸颊(此参数在对比模式为1时有效)
        public int nMaxCandidate;//报告的最大匹配图片个数
        public CFG_ALARM_MSG_HANDLE stuEventHandler;//报警联动
        public CFG_TIME_SECTION[] stuTimeSection = (CFG_TIME_SECTION[])new CFG_TIME_SECTION().toArray(WEEK_DAY_NUM*MAX_REC_TSECT_EX);//事件响应时间段
    }
    
    // 大类业务方案    
    public static class EM_CLASS_TYPE extends Structure
    {
        public static final int EM_CLASS_UNKNOWN =0;//未知业务
        public static final int EM_CLASS_VIDEO_SYNOPSIS =1;//视频浓缩
        public static final int EM_CLASS_TRAFFIV_GATE =2;//卡口
        public static final int EM_CLASS_ELECTRONIC_POLICE =3;//电警
        public static final int EM_CLASS_SINGLE_PTZ_PARKING =4;//单球违停
        public static final int EM_CLASS_PTZ_PARKINBG =5;//主从违停
        public static final int EM_CLASS_TRAFFIC =6;//交通事件"Traffic"
        public static final int EM_CLASS_NORMAL =7;//通用行为分析"Normal"
        public static final int EM_CLASS_PRISON =8;//监所行为分析"Prison"
        public static final int EM_CLASS_ATM =9;//金融行为分析"ATM"
        public static final int EM_CLASS_METRO =10;//地铁行为分析
        public static final int EM_CLASS_FACE_DETECTION =11;//人脸检测"FaceDetection"
        public static final int EM_CLASS_FACE_RECOGNITION =12;//人脸识别"FaceRecognition"
        public static final int EM_CLASS_NUMBER_STAT =13;//人数统计"NumberStat"
        public static final int EM_CLASS_HEAT_MAP =14;//热度图"HeatMap"
        public static final int EM_CLASS_VIDEO_DIAGNOSIS =15;//视频诊断"VideoDiagnosis"
        public static final int EM_CLASS_VIDEO_ENHANCE =16;//视频增强
        public static final int EM_CLASS_SMOKEFIRE_DETECT =17;//烟火检测
        public static final int EM_CLASS_VEHICLE_ANALYSE =18;//车辆特征识别"VehicleAnalyse"
        public static final int EM_CLASS_PERSON_FEATURE =19;//人员特征识别
    }
    
    // 智能报警事件公共信息
    public static class EVENT_INTELLI_COMM_INFO extends Structure
    {
        public int 			emClassType;				//智能事件所属大类， 取值为  EM_CLASS_TYPE 中的值
        public int			nPresetID;					// 该事件触发的预置点，对应该设置规则的预置点
    	public byte[]       bReserved = new byte[124];  // 保留字节,留待扩展.
    }
    
    // 事件类型EVENT_IVS_FACERECOGNITION(人脸识别)对应的数据块描述信息
    public static class DEV_EVENT_FACERECOGNITION_INFO extends Structure
    {
        public int nChannelID;//通道号
        public byte[] szName = new byte[128];//事件名称
        public int nEventID;//事件ID
        public NET_TIME_EX UTC;//事件发生的时间
        public NET_MSG_OBJECT stuObject;//检测到的物体
        public int nCandidateNum;//当前人脸匹配到的候选对象数量
        public CANDIDATE_INFO[] stuCandidates = (CANDIDATE_INFO[])new CANDIDATE_INFO().toArray(NET_MAX_CANDIDATE_NUM);//当前人脸匹配到的候选对象信息
        public byte bEventAction;//事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束; 
        public byte byImageIndex;//图片的序号,同一时间内(精确到秒)可能有多张图片,从0开始
        public byte[] byReserved1 = new byte[2];//对齐
        public int bGlobalScenePic;//全景图是否存在, 类型为BOOL, 取值为0或者1
        public NET_PIC_INFO stuGlobalScenePicInfo;//全景图片信息
        public byte[] szSnapDevAddress = new byte[MAX_PATH];//抓拍当前人脸的设备地址,如：滨康路37号
        public int nOccurrenceCount;//事件触发累计次数， 类型为unsigned int
        public EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;//智能事件公共信息
    	public NET_FACE_DATA stuFaceData;	// 人脸数据
        public byte[] bReserved = new byte[300];//保留字节,留待扩展. 
    }
    
    // 人脸数据
    public static class NET_FACE_DATA extends Structure
    {
    	public int 				emSex;							// 性别, 参考 EM_DEV_EVENT_FACEDETECT_SEX_TYPE
    	public int        		nAge;							// 年龄,-1表示该字段数据无效
    	public int        		nFeatureValidNum;           	// 人脸特征数组有效个数,与 emFeature 结合使用
    	public int[]    		emFeature = new int[NET_MAX_FACEDETECT_FEATURE_NUM];   // 人脸特征数组,与 nFeatureValidNum 结合使用, 参考 EM_DEV_EVENT_FACEDETECT_FEATURE_TYPE
    	public int				emRace;							// 种族, 参考 EM_RACE_TYPE
    	public int				emEye;							// 眼睛状态, 参考 EM_EYE_STATE_TYPE
    	public int				emMouth;						// 嘴巴状态, 参考 EM_MOUTH_STATE_TYPE
    	public int 				emMask;							// 口罩状态, 参考 EM_MASK_STATE_TYPE
    	public int				emBeard;						// 胡子状态, 参考 EM_BEARD_STATE_TYPE
    	public int				nAttractive;					// 魅力值, -1表示无效, 0未识别，识别时范围1-100,得分高魅力高
    	public byte[]           bReserved = new byte[128];      // 保留字节,留待扩展.
    } 
    
    //人脸检测对应性别类型
    public static class EM_DEV_EVENT_FACEDETECT_SEX_TYPE extends Structure
    {
        public static final int EM_DEV_EVENT_FACEDETECT_SEX_TYPE_UNKNOWN = 0; //未知
        public static final int EM_DEV_EVENT_FACEDETECT_SEX_TYPE_MAN = 1; //男性
        public static final int EM_DEV_EVENT_FACEDETECT_SEX_TYPE_WOMAN = 2; //女性
    }

    //人脸检测对应人脸特征类型
    public static class EM_DEV_EVENT_FACEDETECT_FEATURE_TYPE extends Structure
    {
        public static final int EM_DEV_EVENT_FACEDETECT_FEATURE_TYPE_UNKNOWN = 0; //未知
        public static final int EM_DEV_EVENT_FACEDETECT_FEATURE_TYPE_WEAR_GLASSES = 1; //戴眼镜
        public static final int EM_DEV_EVENT_FACEDETECT_FEATURE_TYPE_SMILE = 2; //微笑
        public static final int EM_DEV_EVENT_FACEDETECT_FEATURE_TYPE_ANGER = 3; //愤怒
        public static final int EM_DEV_EVENT_FACEDETECT_FEATURE_TYPE_SADNESS = 4; //悲伤
        public static final int EM_DEV_EVENT_FACEDETECT_FEATURE_TYPE_DISGUST = 5; //厌恶
        public static final int EM_DEV_EVENT_FACEDETECT_FEATURE_TYPE_FEAR = 6; //害怕
        public static final int EM_DEV_EVENT_FACEDETECT_FEATURE_TYPE_SURPRISE = 7; //惊讶
        public static final int EM_DEV_EVENT_FACEDETECT_FEATURE_TYPE_NEUTRAL = 8; //正常
        public static final int EM_DEV_EVENT_FACEDETECT_FEATURE_TYPE_LAUGH = 9; //大笑
    }
    
    // 种族类型
    public static class EM_RACE_TYPE extends Structure
    {
    	public static final int EM_RACE_UNKNOWN = 0;			// 未知
    	public static final int EM_RACE_NODISTI = 1;			// 未识别
    	public static final int EM_RACE_YELLOW = 2;				// 黄种人
    	public static final int EM_RACE_BLACK = 3;				// 黑人
    	public static final int EM_RACE_WHITE = 4;				// 白人
    } 
    
    // 眼睛状态
    public static class EM_EYE_STATE_TYPE extends Structure
    {
    	public static final int EM_EYE_STATE_UNKNOWN = 0;		// 未知
    	public static final int EM_EYE_STATE_NODISTI = 1;		// 未识别
    	public static final int EM_EYE_STATE_CLOSE = 2;		    // 闭眼
    	public static final int EM_EYE_STATE_OPEN = 3;			// 睁眼
    } 

    // 嘴巴状态
    public static class EM_MOUTH_STATE_TYPE extends Structure
    {
    	public static final int EM_MOUTH_STATE_UNKNOWN = 0;		// 未知
    	public static final int EM_MOUTH_STATE_NODISTI = 1;		// 未识别
    	public static final int EM_MOUTH_STATE_CLOSE = 2;		// 闭嘴
    	public static final int EM_MOUTH_STATE_OPEN = 3;		// 张嘴
    } 

    // 口罩状态
    public static class EM_MASK_STATE_TYPE extends Structure
    {
    	public static final int EM_MASK_STATE_UNKNOWN = 0;		// 未知
    	public static final int EM_MASK_STATE_NODISTI = 1;		// 未识别
    	public static final int EM_MASK_STATE_NOMASK = 2;		// 没戴口罩
    	public static final int EM_MASK_STATE_WEAR = 3;			// 戴口罩
    } 

    // 胡子状态
    public static class EM_BEARD_STATE_TYPE extends Structure
    {
    	public static final int EM_BEARD_STATE_UNKNOWN = 0;		// 未知
    	public static final int EM_BEARD_STATE__NODISTI = 1;	// 未识别
    	public static final int EM_BEARD_STATE_NOBEARD = 2;		// 没胡子
    	public static final int EM_BEARD_STATE_HAVEBEARD = 3;	// 有胡子
    } 

    // 事件文件的文件标签类型
    public static class EM_EVENT_FILETAG extends Structure
    {
        public static final int NET_ATMBEFOREPASTE = 1; //ATM贴条前
        public static final int NET_ATMAFTERPASTE = 2;  //ATM贴条后
    }

    // 事件对应文件信息
    public static class NET_EVENT_FILE_INFO extends Structure
    {
        public byte bCount;//当前文件所在文件组中的文件总数
        public byte bIndex;//当前文件在文件组中的文件编号(编号1开始)
        public byte bFileTag;//文件标签,具体说明见枚举类型 EM_EVENT_FILETAG
        public byte bFileType;//文件类型,0-普通1-合成2-抠图
        public NET_TIME_EX stuFileTime;//文件时间
        public int nGroupId;//同一组抓拍文件的唯一标识
    }

    // 多人脸检测信息
    public static class NET_FACE_INFO extends Structure
    {
        public int nObjectID;//物体ID,每个ID表示一个唯一的物体
        public byte[] szObjectType = new byte[128];//物体类型
        public int nRelativeID;//Relative与另一张图片ID相同,表示这张人脸抠图是从大图中取出的
        public DH_RECT BoundingBox;//包围盒
        public NET_POINT Center;//物体型心
    }

    //事件类型EVENT_IVS_FACEDETECT(人脸检测事件)对应的数据块描述信息
    public static class DEV_EVENT_FACEDETECT_INFO extends Structure
    {
        public int nChannelID;//通道号
        public byte[] szName = new byte[128];//事件名称
        public byte[] bReserved1 = new byte[4];//字节对齐
        public double PTS;//时间戳(单位是毫秒)
        public NET_TIME_EX UTC;//事件发生的时间
        public int nEventID;//事件ID
        public NET_MSG_OBJECT stuObject;//检测到的物体
        public NET_EVENT_FILE_INFO stuFileInfo;//事件对应文件信息
        public byte bEventAction;//事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[] reserved = new byte[2];//保留字节
        public byte byImageIndex;//图片的序号,同一时间内(精确到秒)可能有多张图片,从0开始
        public int nDetectRegionNum;//规则检测区域顶点数
        public NET_POINT[] DetectRegion = (NET_POINT[])new NET_POINT().toArray(NET_MAX_DETECT_REGION_NUM);//规则检测区域
        public int dwSnapFlagMask;//抓图标志(按位),具体见NET_RESERVED_COMMON
        public byte[] szSnapDevAddress = new byte[MAX_PATH];//抓拍当前人脸的设备地址,如：滨康路37号
        public int nOccurrenceCount;//事件触发累计次数, 类型为unsigned int
        public int emSex;//性别, 取值为EM_DEV_EVENT_FACEDETECT_SEX_TYPE中的值
        public int nAge;//年龄,-1表示该字段数据无效
        public int nFeatureValidNum;//人脸特征数组有效个数,与emFeature结合使用, 类型为unsigned int
        public int[] emFeature = new int[NET_MAX_FACEDETECT_FEATURE_NUM];//人脸特征数组,与nFeatureValidNum, 取值为EM_DEV_EVENT_FACEDETECT_FEATURE_TYPE中的值
        public int nFacesNum;//指示stuFaces有效数量
        public NET_FACE_INFO[] stuFaces = (NET_FACE_INFO[])new NET_FACE_INFO().toArray(10);//多张人脸时使用,此时没有Object
        public EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;//智能事件公共信息
        public byte[] bReserved = new byte[892];//保留字节,留待扩展
    }
    
    // 事件类型EVENT_IVS_TRAFFICJAM(交通拥堵事件)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFICJAM_INFO extends Structure {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[128];                     // 事件名称
        public byte[]              bReserved1 = new byte[4];                   // 字节对齐
        public double              PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public int                 nLane;                                      // 对应车道号
        public NET_EVENT_FILE_INFO stuFileInfo;                                // 事件对应文件信息               
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte                bJamLenght;                                 // 表示拥堵长度(总车道长度百分比）0-100
        public byte                reserved;                                   // 保留字节
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public NET_TIME_EX         stuStartJamTime;                            // 开始停车时间
        public int                 nSequence;                                  // 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束(bEventAction=2时此参数有效)
        public int                 nAlarmIntervalTime;                         // 报警时间间隔,单位:秒。(此事件为连续性事件,在收到第一个此事件之后,若在超过间隔时间后未收到此事件的后续事件,则认为此事件异常结束了)
        public int                 dwSnapFlagMask;                             // 抓图标志(按位),具体见NET_RESERVED_COMMON    
        public NET_RESOLUTION_INFO stuResolution;                              // 对应图片的分辨率
        public int                 nJamRealLength;                             // 表实际的拥堵长度,单位米
        public byte[]              bReserved = new byte[1008];                 // 保留字节,留待扩展.
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 交通车辆信息  
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
    }
    
    // 车辆行驶方向
    public static class NET_FLOWSTAT_DIRECTION extends Structure
    {
        public static final int DRIVING_DIR_UNKNOW   = 0 ;     //兼容之前
        public static final int DRIVING_DIR_APPROACH = 1 ;     //上行,即车辆离设备部署点越来越近
        public static final int DRIVING_DIR_LEAVE    = 2 ;     //下行,即车辆离设备部署点越来越远
    }
    
    //车辆流量统计车辆行驶方向信息 
    public static class NET_TRAFFIC_FLOWSTAT_INFO_DIR extends Structure
    {
        public int                         emDrivingDir;      //行驶方向 (参见NET_FLOWSTAT_DIRECTION)
        public byte[]                      szUpGoing = new byte[FLOWSTAT_ADDR_NAME];      //上行地点 
        public byte[]                      szDownGoing = new byte[FLOWSTAT_ADDR_NAME];    //下行地点 
        public byte[]                      reserved= new byte[32];                       //保留字节
        
    }
    
    public static class NET_TRAFFIC_JAM_STATUS extends Structure
    {
         public static final int JAM_STATUS_UNKNOW = 0; //未知
         public static final int JAM_STATUS_CLEAR  = 1; //通畅
         public static final int JAM_STATUS_JAMMED = 2; //拥堵
    }


    public static class  NET_TRAFFIC_FLOW_STATE  extends Structure
    {
        public int                             nLane;                          // 车道号
        public int                             dwState;                        // 状态值
                                                                               // 1- 流量过大
                                                                               // 2- 流量过大恢复
                                                                               // 3- 正常
                                                                               // 4- 流量过小
                                                                               // 5- 流量过小恢复
        public int                             dwFlow;                         // 流量值, 单位: 辆
        public int                             dwPeriod;                       // 流量值对应的统计时间
        public NET_TRAFFIC_FLOWSTAT_INFO_DIR   stTrafficFlowDir;               // 车道方向信息
        public int                             nVehicles;                      // 通过车辆总数
        public float                           fAverageSpeed;                  // 平均车速,单位km/h
        public float                           fAverageLength;                 // 平均车长,单位米
        public float                           fTimeOccupyRatio;               // 时间占有率,即单位时间内通过断面的车辆所用时间的总和占单位时间的比例
        public float                           fSpaceOccupyRatio;              // 空间占有率,即按百分率计量的车辆长度总和除以时间间隔内车辆平均行驶距离
        public float                           fSpaceHeadway;                  // 车头间距,相邻车辆之间的距离,单位米/辆
        public float                           fTimeHeadway;                   // 车头时距,单位秒/辆
        public float                           fDensity;                       // 车辆密度,每公里的车辆数,单位辆/km
        public int                             nOverSpeedVehicles;             // 超速车辆数
        public int                             nUnderSpeedVehicles;            // 低速车辆数
        public int                             nLargeVehicles;                 // 大车交通量(9米<车长<12米),辆/单位时间
        public int                             nMediumVehicles;                // 中型车交通量(6米<车长<9米),辆/单位时间
        public int                             nSmallVehicles;                 // 小车交通量(4米<车长<6米),辆/单位时间,
        public int                             nMotoVehicles;                  // 摩托交通量(微型车,车长<4米),辆/单位时间,
        public int                             nLongVehicles;                  // 超长交通量(车长>=12米),辆/单位时间,
        public int                             nVolume;                        // 交通量, 辆/单位时间, 某时间间隔通过车道、道路或其他通道上一点的车辆数,常以1小时计, 
        public int                             nFlowRate;                      // 流率小车当量,辆/小时, 车辆通过车道、道路某一断面或某一路段的当量小时流量
        public int                             nBackOfQueue;                   // 排队长度,单位：米, 从信号交叉口停车线到上游排队车辆末端之间的距离
        public int                             nTravelTime;                    // 旅行时间,单位：秒, 车辆通过某一条道路所用时间。包括所有停车延误
        public int                             nDelay;                         // 延误,单位：秒,驾驶员、乘客或行人花费的额外的行程时间
        public byte[]                          byDirection = new byte[MAX_DRIVING_DIR_NUM]; // 车道方向,详见NET_ROAD_DIRECTION
        public byte                            byDirectionNum;                 // 车道行驶方向个数
        public byte[]                          reserved1 = new byte[3];        // 字节对齐
        public int          				   emJamState;                     // 道路拥挤状况 (参见 NET_TRAFFIC_JAM_STATUS )
        //  按车辆类型统计交通量
        public int                             nPassengerCarVehicles;          // 客车交通量(辆/单位时间)
        public int                             nLargeTruckVehicles;            // 大货车交通量(辆/单位时间)
        public int                             nMidTruckVehicles;              // 中货车交通量(辆/单位时间)
        public int                             nSaloonCarVehicles;             // 轿车交通量(辆/单位时间)    
        public int                             nMicrobusVehicles;              // 面包车交通量(辆/单位时间)
        public int                             nMicroTruckVehicles;            // 小货车交通量(辆/单位时间)
        public int                             nTricycleVehicles;              // 三轮车交通量(辆/单位时间)
        public int                             nMotorcycleVehicles;            // 摩托车交通量(辆/单位时间)
        public int                             nPasserbyVehicles;              // 行人交通量(辆/单位时间)
        public int							   emRank;						   // 道路等级, 参考  NET_TRAFFIC_ROAD_RANK
        public int							   nState;						   // 流量状态
																			   // 1- 流量过大(拥堵)
																			   // 2- 流量过大恢复(略堵)
																			   // 3- 正常
																			   // 4- 流量过小(通畅)
																			   // 5- 流量过小恢复(良好)
        public int                            bOccupyHeadCoil;                 // 车头虚拟线圈是否被占用 TURE表示占用，FALSE表示未占用
        public int                            bOccupyTailCoil;                 // 车尾虚拟线圈是否被占用 TURE表示占用，FALSE表示未占用
        public int                            bStatistics;                     // 流量数据是否有效 TURE表示有效，FALSE表示无效

        public int							  nLeftVehicles;				   // 左转车辆总数,单位:分钟
        public int							  nRightVehicles;				   // 右转车辆总数,单位:分钟
        public int							  nStraightVehicles;			   // 直行车辆总数,单位:分钟
        public int							  nUTurnVehicles;				   // 掉头车辆总数,单位:分钟
        public NET_POINT                      stQueueEnd;                      // 每个车道的最后一辆车坐标,采用8192坐标系
        public double						  dBackOfQueue;					   // 排队长度,单位：米, 从信号交叉口停车线到上游排队车辆末端之间的距离
        public int							  dwPeriodByMili;				   // 流量值的毫秒时间,值不超过60000,和dwPeriod一起使用,流量值总时间:dwPeriod*60*1000+dwPeriodByMili(单位：毫秒)
        public int							  nBusVehicles;					   // 公交车交通量(辆/单位时间)
        public int							  nMPVVehicles;					   // MPV交通量(辆/单位时间)
        public int							  nMidPassengerCarVehicles;		   // 中客车交通量(辆/单位时间)
        public int							  nMiniCarriageVehicles;		   // 微型轿车交通量(辆/单位时间)
        public int							  nOilTankTruckVehicles;		   // 油罐车交通量(辆/单位时间)
        public int							  nPickupVehicles;				   // 皮卡车交通量(辆/单位时间)
        public int							  nSUVVehicles;					   // SUV交通量(辆/单位时间)
        public int							  nSUVorMPVVehicles;			   // SUV或者MPV交通量(辆/单位时间)
        public int							  nTankCarVehicles;				   // 槽罐车交通量(辆/单位时间)
        public int							  nUnknownVehicles;				   // 未知车辆交通量(辆/单位时间)
        public byte[]                         reserved = new byte[724];        // 保留字节
    }

    //事件类型 EVENT_IVS_TRAFFIC_FLOWSTATE(交通流量事件)对应数据块描述信息
    public static class DEV_EVENT_TRAFFIC_FLOW_STATE extends Structure
    {
        public int                       nChannelID;                             // 通道号
        public byte[]                    szName = new byte[NET_EVENT_NAME_LEN];  // 事件名称
        public byte[]                    bReserved1 = new byte[8];               // 字节对齐
        public int                       PTS;                                    // 时间戳(单位是毫秒)
        public NET_TIME_EX               UTC;                                    // 事件发生的时间
        public int                       nEventID;                               // 事件ID
        public int                       nSequence;                              // 序号
        public int                       nStateNum;                              // 流量状态数量
        public NET_TRAFFIC_FLOW_STATE[]  stuStates = (NET_TRAFFIC_FLOW_STATE[])new NET_TRAFFIC_FLOW_STATE().toArray(NET_MAX_LANE_NUM);             // 流量状态, 每个车道对应数组中一个元素
    	public EVENT_INTELLI_COMM_INFO   stuIntelliCommInfo;                 		// 智能事件公共信息
        public byte[]                    bReserved = new byte[892];            // 保留字节
    }
    
    // 图片分辨率
    public static class NET_RESOLUTION_INFO extends Structure
    {
        public short snWidth;//宽
        public short snHight;//高
    }

    public static class EM_COMMON_SEAT_TYPE extends Structure
    {
        public static final int COMMON_SEAT_TYPE_UNKNOWN = 0;//未识别
        public static final int COMMON_SEAT_TYPE_MAIN = 1;//主驾驶
        public static final int COMMON_SEAT_TYPE_SLAVE = 2;//副驾驶
    }

    // 违规状态
    public static class EVENT_COMM_STATUS extends Structure
    {
        public byte bySmoking;//是否抽烟
        public byte byCalling;//是否打电话
        public byte[] szReserved = new byte[14];//预留字段
    }

    public static class NET_SAFEBELT_STATE extends Structure
    {
        public static final int SS_NUKNOW = 0;//未知
        public static final int SS_WITH_SAFE_BELT = 1;//已系安全带
        public static final int SS_WITHOUT_SAFE_BELT = 2;//未系安全带
    }

    //遮阳板状态
    public static class NET_SUNSHADE_STATE extends Structure
    {
        public static final int SS_NUKNOW_SUN_SHADE = 0;//未知
        public static final int SS_WITH_SUN_SHADE = 1;//有遮阳板
        public static final int SS_WITHOUT_SUN_SHADE = 2;//无遮阳板
    }

    // 驾驶位违规信息
    public static class EVENT_COMM_SEAT extends Structure
    {
        public int bEnable;//是否检测到座驾信息, 类型BOOL, 取值0或者1
        public int emSeatType;//座驾类型,0:未识别;1:主驾驶; 取值为EM_COMMON_SEAT_TYPE中的值
        public EVENT_COMM_STATUS stStatus;//违规状态
        public int emSafeBeltStatus;//安全带状态, 取值为NET_SAFEBELT_STATE中的值
        public int emSunShadeStatus;//遮阳板状态, 取值为NET_SUNSHADE_STATE中的值
        public byte[] szReserved = new byte[24];//预留字节
    }

    public static class EM_COMM_ATTACHMENT_TYPE extends Structure
    {       
        public static final int COMM_ATTACHMENT_TYPE_UNKNOWN = 0;// 未知类型
        public static final int COMM_ATTACHMENT_TYPE_FURNITURE = 1;// 摆件   
        public static final int COMM_ATTACHMENT_TYPE_PENDANT = 2;// 挂件    
        public static final int COMM_ATTACHMENT_TYPE_TISSUEBOX = 3;// 纸巾盒
        public static final int COMM_ATTACHMENT_TYPE_DANGER = 4;// 危险品
    }

    // 车辆物件
    public static class EVENT_COMM_ATTACHMENT extends Structure
    {       
        public int emAttachmentType;//物件类型, 取值为EM_COMM_ATTACHMENT_TYPE中的值
        public NET_RECT stuRect;//坐标
        public byte[] bReserved = new byte[20];//预留字节
    }
    
    //NTP校时状态
    public static class EM_NTP_STATUS extends Structure
    {
        public static final int NET_NTP_STATUS_UNKNOWN = 0;
        public static final int NET_NTP_STATUS_DISABLE = 1;
        public static final int NET_NTP_STATUS_SUCCESSFUL = 2;
        public static final int NET_NTP_STATUS_FAILED = 3;
    }
    
    // 交通抓图图片信息
    public static class EVENT_PIC_INFO extends Structure
    {
    	public int                       nOffset;                // 原始图片偏移，单位字节
    	public int                       nLength;                // 原始图片长度，单位字节
    }

    public static class EVENT_COMM_INFO extends Structure
    {
        public int emNTPStatus;//NTP校时状态, 取值为EM_NTP_STATUS中的值
        public int nDriversNum;//驾驶员信息数
        public Pointer pstDriversInfo;//驾驶员信息数据，类型为 NET_MSG_OBJECT_EX*
        public Pointer pszFilePath;//本地硬盘或者sd卡成功写入路径,为NULL时,路径不存在， 类型为char *
        public Pointer pszFTPPath;//设备成功写到ftp服务器的路径， 类型为char *
        public Pointer pszVideoPath;//当前接入需要获取当前违章的关联视频的FTP上传路径， 类型为char *
        public EVENT_COMM_SEAT[] stCommSeat = (EVENT_COMM_SEAT[])new EVENT_COMM_SEAT().toArray(COMMON_SEAT_MAX_NUMBER);//驾驶位信息
        public int nAttachmentNum;//车辆物件个数
        public EVENT_COMM_ATTACHMENT[] stuAttachment = (EVENT_COMM_ATTACHMENT[])new EVENT_COMM_ATTACHMENT().toArray(NET_MAX_ATTACHMENT_NUM);//车辆物件信息
        public int nAnnualInspectionNum;//年检标志个数
        public NET_RECT[] stuAnnualInspection = (NET_RECT[])new NET_RECT().toArray(NET_MAX_ANNUUALINSPECTION_NUM);//年检标志
        public float fHCRatio; // HC所占比例，单位：% 
        public float fNORatio; // NO所占比例，单位：% 
        public float fCOPercent; // CO所占百分比，单位：% 取值0~100
        public float fCO2Percent; // CO2所占百分比，单位：% 取值0~100     
        public float fLightObscuration; // 不透光度，单位：% 取值0~100
        public int nPictureNum;// 原始图片张数
        public EVENT_PIC_INFO[] stuPicInfos = (EVENT_PIC_INFO[])new EVENT_PIC_INFO().toArray(NET_MAX_EVENT_PIC_NUM);// 原始图片信息
        public float fTemperature;                                           // 温度值,单位摄氏度
        public int nHumidity;                                                // 相对湿度百分比值   
        public float fPressure;                                              // 气压值,单位Kpa
        public float fWindForce;                                             // 风力值,单位m/s
        public int nWindDirection;                                           // 风向,单位度,范围:[0,360]
        public float fRoadGradient;                                          // 道路坡度值,单位度
        public float fAcceleration;                                          // 加速度值,单位:m/s2   
        public NET_RFIDELETAG_INFO			stuRFIDEleTagInfo;				 // RFID 电子车牌标签信息
        public byte[] bReserved = new byte[704];//预留字节
        public byte[] szCountry = new byte[20];//国家
    }
    
 // RFID 电子车牌标签信息
    public static class NET_RFIDELETAG_INFO extends Structure
    {
    	public byte[]					szCardID = new byte[MAX_RFIDELETAG_CARDID_LEN];	// 卡号
    	public int						nCardType;										// 卡号类型, 0:交通管理机关发行卡, 1:新车出厂预装卡
    	public int						emCardPrivince;									// 卡号省份, 对应   EM_CARD_PROVINCE
    	public byte[]					szPlateNumber = new byte[NET_MAX_PLATE_NUMBER_LEN];			// 车牌号码
    	public byte[]					szProductionDate = new byte[MAX_RFIDELETAG_DATE_LEN];		// 出厂日期
    	public int						emCarType;										// 车辆类型, 对应  EM_CAR_TYPE
    	public int						nPower;											// 功率,单位：千瓦时，功率值范围0~254；255表示该车功率大于可存储的最大功率值
    	public int						nDisplacement;									// 排量,单位：百毫升，排量值范围0~254；255表示该车排量大于可存储的最大排量值
    	public int						nAntennaID;										// 天线ID，取值范围:1~4
    	public int						emPlateType;									// 号牌种类, 对应  EM_PLATE_TYPE
    	public byte[]					szInspectionValidity = new byte[MAX_RFIDELETAG_DATE_LEN];	// 检验有效期，年-月
    	public int						nInspectionFlag;								// 逾期未年检标志, 0:已年检, 1:逾期未年检
    	public int						nMandatoryRetirement;							// 强制报废期，从检验有效期开始，距离强制报废期的年数
    	public int						emCarColor;										// 车身颜色, 对应  EM_CAR_COLOR_TYPE
    	public int						nApprovedCapacity;								// 核定载客量，该值<0时：无效；此值表示核定载客，单位为人
    	public int						nApprovedTotalQuality;							// 此值表示总质量，单位为百千克；该值<0时：无效；该值的有效范围为0~0x3FF，0x3FF（1023）表示数据值超过了可存储的最大值
    	public NET_TIME_EX				stuThroughTime;									// 过车时间
    	public int						emUseProperty;									// 使用性质, 对应  EM_USE_PROPERTY_TYPE
    	public byte[]					szPlateCode = new byte[MAX_COMMON_STRING_8];	// 发牌代号，UTF-8编码
    	public byte[]					szPlateSN = new byte[MAX_COMMON_STRING_16];		// 号牌号码序号，UTF-8编码
    	public byte[]               	bReserved = new byte[104];		                // 保留字节,留待扩展.
    } 
    
    // 车检器冗余信息
    public static class NET_SIG_CARWAY_INFO_EX extends Structure
    {
        public byte[] byRedundance = new byte[8];//由车检器产生抓拍信号冗余信息
        public byte[] bReserved = new byte[120];//保留字段
    }
    
    // 颜色RGBA
    public static class NET_COLOR_RGBA extends Structure
    {
        public int nRed;//红
        public int nGreen;//绿
        public int nBlue;//蓝
        public int nAlpha;//透明
    }

    // TrafficCar 交通车辆信息
    public static class DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO extends Structure
    {
        public byte[] szPlateNumber = new byte[32];//车牌号码
        public byte[] szPlateType = new byte[32];//号牌类型参见VideoAnalyseRule中车牌类型定义
        public byte[] szPlateColor = new byte[32];//车牌颜色"Blue","Yellow",
        public byte[] szVehicleColor = new byte[32];//车身颜色"White",
        public int nSpeed;//速度单位Km/H
        public byte[] szEvent = new byte[64];//触发的相关事件参见事件列表Event
        public byte[] szViolationCode = new byte[32];//违章代码详见TrafficGlobal.ViolationCode
        public byte[] szViolationDesc = new byte[64];//违章描述
        public int nLowerSpeedLimit;//速度下限
        public int nUpperSpeedLimit;//速度上限
        public int nOverSpeedMargin;//限高速宽限值单位：km/h
        public int nUnderSpeedMargin;//限低速宽限值单位：km/h
        public int nLane;//车道参见事件列表EventList中卡口和路口事件。
        public int nVehicleSize;//车辆大小,-1表示未知,否则按位
        // 第0位:"Light-duty", 小型车
        // 第1位:"Medium", 中型车
        // 第2位:"Oversize", 大型车
        // 第3位:"Minisize", 微型车
        // 第4位:"Largesize", 长车
        public float fVehicleLength;//车辆长度单位米
        public int nSnapshotMode;//抓拍方式0-未分类,1-全景,2-近景,4-同向抓拍,8-反向抓拍,16-号牌图像
        public byte[] szChannelName = new byte[32];//本地或远程的通道名称,可以是地点信息来源于通道标题配置ChannelTitle.Name
        public byte[] szMachineName = new byte[256];//本地或远程设备名称来源于普通配置General.MachineName
        public byte[] szMachineGroup = new byte[256];//机器分组或叫设备所属单位默认为空,用户可以将不同的设备编为一组,便于管理,可重复。
        public byte[] szRoadwayNo = new byte[64];//道路编号
        public byte[] szDrivingDirection = new byte[3*NET_MAX_DRIVINGDIRECTION];//
                                                                                // 行驶方向 , "DrivingDirection" : ["Approach", "上海", "杭州"],
                                                                                // "Approach"-上行,即车辆离设备部署点越来越近；"Leave"-下行,
                                                                                // 即车辆离设备部署点越来越远,第二和第三个参数分别代表上行和
                                                                                // 下行的两个地点
        public Pointer szDeviceAddress;//设备地址,OSD叠加到图片上的,来源于配 置TrafficSnapshot.DeviceAddress,'\0'结束
        public byte[] szVehicleSign = new byte[32];//车辆标识,例如
        public NET_SIG_CARWAY_INFO_EX stuSigInfo;//由车检器产生抓拍信号冗余信息
        public Pointer szMachineAddr;//设备部署地点
        public float fActualShutter;//当前图片曝光时间,单位为毫秒
        public byte byActualGain;//当前图片增益,范围为0~100
        public byte byDirection;//车道方向,0-南向北1-西南向东北2-西向东
        public byte[] byReserved = new byte[2];
        public Pointer szDetailedAddress;//详细地址,作为szDeviceAddress的补充
        public byte[] szDefendCode = new byte[NET_COMMON_STRING_64];//图片防伪码
        public int nTrafficBlackListID;//关联黑名单数据库记录默认主键ID,0,无效；>0,黑名单数据记录
        public NET_COLOR_RGBA stuRGBA;//车身颜色RGBA
        public NET_TIME stSnapTime;//抓拍时间
        public int nRecNo;//记录编号
        public byte[] szCustomParkNo= new byte[NET_COMMON_STRING_32+1];// 自定义车位号（停车场用）
        public byte[] byReserved1 = new byte[3];
        public int nDeckNo;//车板位号
        public int nFreeDeckCount;//空闲车板数量
        public int nFullDeckCount;//占用车板数量
        public int nTotalDeckCount;//总共车板数量
        public byte[] szViolationName = new byte[64];//违章名称
        public int nWeight;//车重(单位Kg), 类型为unsigned int
       
        public byte[]      szCustomRoadwayDirection = new byte[32];		// 自定义车道方向,byDirection为9时有效
        public byte        byPhysicalLane; // 物理车道号,取值0到5
        public byte[]      byReserved2 = new byte[3];
        public int 		   emMovingDirection; // 车辆行驶方向 EM_TRAFFICCAR_MOVE_DIRECTION
        public NET_TIME	stuEleTagInfoUTC;					// 对应电子车牌标签信息中的过车时间(ThroughTime)
        public byte[]      bReserved = new byte[552]; // 保留字节,留待扩展.
    }

    // 事件类型EVENT_IVS_TRAFFIC_PARKING(交通违章停车事件)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFIC_PARKING_INFO extends Structure
    {
        public int nChannelID;//通道号
        public byte[] szName = new byte[128];//事件名称
        public byte[] bReserved1 = new byte[4];//字节对齐
        public double PTS;//时间戳(单位是毫秒)
        public NET_TIME_EX UTC;//事件发生的时间
        public int nEventID;//事件ID
        public NET_MSG_OBJECT stuObject;//检测到的物体
        public NET_MSG_OBJECT stuVehicle;//车身信息
        public int nLane;//对应车道号
        public NET_EVENT_FILE_INFO stuFileInfo;//事件对应文件信息
        public byte bEventAction;//事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[] reserved = new byte[2];//保留字节
        public byte byImageIndex;//图片的序号,同一时间内(精确到秒)可能有多张图片,从0开始
        public NET_TIME_EX stuStartParkingTime;//开始停车时间
        public int nSequence;//表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束(bEventAction=2时此参数有效)
        public int nAlarmIntervalTime;//报警时间间隔,单位:秒。(此事件为连续性事件,在收到第一个此事件之后,若在超过间隔时间后未收到此事件的后续事件,则认为此事件异常结束了)
        public int nParkingAllowedTime;//允许停车时长,单位：秒。
        public int nDetectRegionNum;//规则检测区域顶点数
        public NET_POINT[] DetectRegion = (NET_POINT[])new NET_POINT().toArray(NET_MAX_DETECT_REGION_NUM);//规则检测区域
        public int dwSnapFlagMask;//抓图标志(按位),具体见NET_RESERVED_COMMON
        public NET_RESOLUTION_INFO stuResolution;//对应图片的分辨率
        public int bIsExistAlarmRecord;//true:有对应的报警录像;false:无对应的报警录像, 类型为BOOL, 取值为0或1
        public int dwAlarmRecordSize;//录像大小
        public byte[] szAlarmRecordPath = new byte[NET_COMMON_STRING_256];//录像路径
        public byte[] szFTPPath = new byte[NET_COMMON_STRING_256];//FTP路径
        public EVENT_INTELLI_COMM_INFO stuIntelliCommInfo;//智能事件公共信息
        public byte byPreAlarm;	// 是否为违章预警图片,0 违章停车事件1 预警事件(预警触发后一定时间，车辆还没有离开，才判定为违章)由于此字段会导致事件含义改变，必须和在平台识别预警事件后，才能有此字段, 
        public byte[]  bReserved2 = new byte[3]; // 保留字节,留待扩展.
        public NET_GPS_INFO stuGPSInfo;   // GPS信息 车载定制
        public byte[] bReserved = new byte[228];//保留字节,留待扩展.
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;//交通车辆信息
        public EVENT_COMM_INFO stCommInfo;//公共信息
    }
    
    //停车场信息
    public static class DEV_TRAFFIC_PARKING_INFO extends Structure
    {
        public int           nFeaturePicAreaPointNum;                                                        // 特征图片区点个数
        public NET_POINT[]   stFeaturePicArea = (NET_POINT[])new NET_POINT().toArray(NET_MAX_POLYGON_NUM);   // 特征图片区信息
        public byte[]        bReserved = new byte[572];                                                      // 保留字节
    }
    
    //事件类型 EVENT_IVS_TRAFFIC_PARKINGSPACEPARKING(车位有车事件)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFIC_PARKINGSPACEPARKING_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[NET_EVENT_NAME_LEN];      // 事件名称
        public byte[]              bReserved1 = new byte[8];                   // 字节对齐
        public int                 PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public int                 nLane;                                      // 对应车道号
        public NET_MSG_OBJECT      stuObject;                                  // 检测到的物体
        public NET_MSG_OBJECT      stuVehicle;                                 // 车身信息
        public NET_EVENT_FILE_INFO stuFileInfo;                                // 事件对应文件信息
        
        public int                 nSequence;                                  // 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束    
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              byReserved = new byte[2];
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 dwSnapFlagMask;                             // 抓图标志(按位),具体见NET_RESERVED_COMMON    
        public NET_RESOLUTION_INFO  stuResolution;                             // 对应图片的分辨率
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 交通车辆信息
        public int                 nParkingSpaceStatus;                        // 车位状态,0-占用,1-空闲,2-压线
        public DEV_TRAFFIC_PARKING_INFO stTrafficParingInfo;                   // 停车场信息
        public byte                byPlateTextSource;                          // 车牌识别来源, 0:本地算法识别,1:后端服务器算法识别
        public byte[]              bReserved = new byte[379];                  // 保留字节 
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
    }

    // 事件类型 EVENT_IVS_TRAFFIC_PARKINGSPACENOPARKING(车位无车事件)对应的数据块描述信息
    // 由于历史原因,如果要处理卡口事件,DEV_EVENT_TRAFFICJUNCTION_INFO和EVENT_IVS_TRAFFICGATE要一起处理,以防止有视频电警和线圈电警同时接入平台的情况发生
    // 另外EVENT_IVS_TRAFFIC_TOLLGATE只支持新卡口事件的配置
    public static class DEV_EVENT_TRAFFIC_PARKINGSPACENOPARKING_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[NET_EVENT_NAME_LEN];      // 事件名称
        public byte[]              bReserved1 = new byte[8];                   // 字节对齐
        public int                 PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public int                 nLane;                                      // 对应车道号
        public NET_MSG_OBJECT       stuObject;                                 // 检测到的物体
        public NET_MSG_OBJECT       stuVehicle;                                // 车身信息
        public NET_EVENT_FILE_INFO  stuFileInfo;                               // 事件对应文件信息
        
        public int                 nSequence;                                  // 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              byReserved = new byte[2];
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 dwSnapFlagMask;                             // 抓图标志(按位),具体见 NET_RESERVED_COMMON    
        public NET_RESOLUTION_INFO  stuResolution;                             // 对应图片的分辨率
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 交通车辆信息
        public DEV_TRAFFIC_PARKING_INFO stTrafficParingInfo;                   // 停车场信息
        public byte                byPlateTextSource;                          // 车牌识别来源, 0:本地算法识别,1:后端服务器算法识别
        public byte[]              bReserved = new byte[383];                  // 保留字节
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
    }
    
    //事件类型 EVENT_IVS_TRAFFIC_PEDESTRAIN(交通行人事件)对应数据块描述信息
    public static class DEV_EVENT_TRAFFIC_PEDESTRAIN_INFO extends Structure {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[NET_EVENT_NAME_LEN];      // 事件名称
        public byte[]              bReserved1 = new byte[8];                   // 字节对齐
        public int                 PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public NET_EVENT_FILE_INFO stuFileInfo;                                // 事件对应文件信息
        public NET_RESOLUTION_INFO stuResolution;                              // 对应图片的分辨率
        public int                 dwSnapFlagMask;                             // 抓图标志(按位),0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout" 
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              bReserved2 = new byte[2];
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 nLane;                                      // 对应车道号
        public NET_MSG_OBJECT      stuObject;                                  // 检测到的物体
        public EVENT_INTELLI_COMM_INFO     stuIntelliCommInfo;                 // 智能事件公共信息
        public byte[]              bReserved = new byte[892];                 // 保留字节
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
    }

    //事件类型 EVENT_IVS_TRAFFIC_THROW(交通抛洒物品事件)对应数据块描述信息
    public static class DEV_EVENT_TRAFFIC_THROW_INFO extends Structure {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[NET_EVENT_NAME_LEN];      // 事件名称
        public byte[]              bReserved1 = new byte[8];                   // 字节对齐
        public int                 PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public NET_EVENT_FILE_INFO stuFileInfo;                                // 事件对应文件信息
        public NET_RESOLUTION_INFO stuResolution;                              // 对应图片的分辨率
        public int                 dwSnapFlagMask;                             // 抓图标志(按位),0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout" 
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              bReserved2 = new byte[2];
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 nLane;                                      // 对应车道号
        public NET_MSG_OBJECT      stuObject;                                  // 检测到的物体
        public EVENT_INTELLI_COMM_INFO     stuIntelliCommInfo;                 // 智能事件公共信息
        public EVENT_TRAFFIC_CAR_PART_INFO stuTrafficCarPartInfo;              // 交通车辆部分信息
        public NET_GPS_INFO        stuGPSInfo;                                 // GPS信息 车载定制
        public byte[]              bReserved = new byte[340];                  // 保留字节
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
    }
    
    // 交通车辆部分信息
    public class EVENT_TRAFFIC_CAR_PART_INFO extends Structure
    {
        public byte[]              szMachineName = new byte[128];               // 本地或远程设备名称    来源于普通配置General.MachineName    
        public byte[]              szRoadwayNo = new byte[32];                  // 道路编号
        public byte[]              bReserved = new byte[352];                   // 保留字节
    }
    
    // 事件上报携带卡片信息
    public static class EVENT_CARD_INFO extends Structure
    {
        public byte[]              szCardNumber = new byte[NET_EVENT_CARD_LEN];// 卡片序号字符串
        public byte[]              bReserved = new byte[32];                   // 保留字节,留待扩展.
    }
    
    // 车辆方向信息
    public static class EM_VEHICLE_DIRECTION extends Structure
    {
        public static final int    NET_VEHICLE_DIRECTION_UNKOWN = 0;           // 未知
        public static final int    NET_VEHICLE_DIRECTION_HEAD   = 1;           // 车头    
        public static final int    NET_VEHICLE_DIRECTION_TAIL   = 2;           // 车尾  
    }
    
    // 开闸状态
    public static class EM_OPEN_STROBE_STATE extends Structure
    {
        public static final int    NET_OPEN_STROBE_STATE_UNKOWN = 0;           // 未知状态
        public static final int    NET_OPEN_STROBE_STATE_CLOSE  = 1;           // 关闸
        public static final int    NET_OPEN_STROBE_STATE_AUTO   = 2;           // 自动开闸    
        public static final int    NET_OPEN_STROBE_STATE_MANUAL = 3;           // 手动开闸
    }
    
    public static class RESERVED_PARA extends Structure
    {
        public int   				dwType;         						   //pData的数据类型
                                											   //当[dwType]为 RESERVED_TYPE_FOR_INTEL_BOX 时,pData 对应为结构体 RESERVED_DATA_INTEL_BOX 的地址                    
                                											   //当[dwType]为 RESERVED_TYPE_FOR_COMMON 时,[pData]对应为结构体 NET_RESERVED_COMMON 的结构体地址
                                											   //当[dwType]为 RESERVED_TYPE_FOR_PATH 时,[pData]对应结构体NET_RESERVED_PATH的结构体地址
        public Pointer   			pData;          						   //数据,由用户申请内存，大小参考对应的结构体
    }
    
    public static class NET_RESERVED_COMMON extends Structure
    {
        public int                  dwStructSize;
        public Pointer   			pIntelBox;             					   // 对应 结构体 RESERVED_DATA_INTEL_BOX*, 兼容  RESERVED_TYPE_FOR_INTEL_BOX
        public int                  dwSnapFlagMask;     					   // 抓图标志(按位),0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"
    
        public NET_RESERVED_COMMON() {
        	this.dwStructSize = this.size();
        }
    }
    
    // 事件类型 EVENT_IVS_TRAFFICJUNCTION 交通路口老规则事件/视频电警上的交通卡口老规则事件对应的数据块描述信息
    // 由于历史原因,如果要处理卡口事件,DEV_EVENT_TRAFFICJUNCTION_INFO 和 EVENT_IVS_TRAFFICGATE要一起处理
    // 以防止有视频电警和线圈电警同时接入平台的情况发生, 另外EVENT_IVS_TRAFFIC_TOLLGATE只支持新卡口事件的配置
    public static class DEV_EVENT_TRAFFICJUNCTION_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[NET_EVENT_NAME_LEN];      // 事件名称
        public byte                byMainSeatBelt;                             // 主驾驶座,系安全带状态,1-系安全带,2-未系安全带
        public byte                bySlaveSeatBelt;                            // 副驾驶座,系安全带状态,1-系安全带,2-未系安全带
        public byte                byVehicleDirection;                         // 当前被抓拍到的车辆是车头还是车尾,具体请见 EM_VEHICLE_DIRECTION
        public byte                byOpenStrobeState;                          // 开闸状态,具体请见 EM_OPEN_STROBE_STATE 
        public double              PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public NET_MSG_OBJECT      stuObject;                                  // 检测到的物体
        public int                 nLane;                                      // 对应车道号
        public int                 dwBreakingRule;                             // 违反规则掩码,第一位:闯红灯; 
                                                                               // 第二位:不按规定车道行驶;
                                                                               // 第三位:逆行; 第四位：违章掉头;
                                                                               // 第五位:交通堵塞; 第六位:交通异常空闲
                                                                               // 第七位:压线行驶; 否则默认为:交通路口事件
        public NET_TIME_EX         RedLightUTC;                                // 红灯开始UTC时间
        public NET_EVENT_FILE_INFO stuFileInfo;                                // 事件对应文件信息
        public int                 nSequence;                                  // 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
        public int                 nSpeed;                                     // 车辆实际速度Km/h                 
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte                byDirection;                                // 路口方向,1-表示正向,2-表示反向
        public byte                byLightState;                               // LightState表示红绿灯状态:0 未知,1 绿灯,2 红灯,3 黄灯
        public byte                byReserved;                                 // 保留字节
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public NET_MSG_OBJECT      stuVehicle;                                 // 车身信息
        public int                 dwSnapFlagMask;                             // 抓图标志(按位),具体见 NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"   
        public NET_RESOLUTION_INFO stuResolution;                              // 对应图片的分辨率
        public byte[]              szRecordFile = new byte[NET_COMMON_STRING_128];// 报警对应的原始录像文件信息
    	public EVENT_JUNCTION_CUSTOM_INFO   stuCustomInfo;                     // 自定义信息
        public byte                byPlateTextSource;                          // 车牌识别来源, 0:本地算法识别,1:后端服务器算法识别
    	public byte[]              bReserved1 = new byte[3];                   // 保留字节,留待扩展.
    	public NET_GPS_INFO        stuGPSInfo;                                 // GPS信息 车载定制
        public byte[]              bReserved = new byte[196];                  // 保留字节,留待扩展.
        public int                 nTriggerType;                               // TriggerType:触发类型,0车检器,1雷达,2视频
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 交通车辆信息
        public int                 dwRetCardNumber;                            // 卡片个数
        public EVENT_CARD_INFO[]   stuCardInfo = (EVENT_CARD_INFO[])new EVENT_CARD_INFO().toArray(NET_EVENT_MAX_CARD_NUM);// 卡片信息   
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
    }    
    
    //卡口事件专用定制上报内容，定制需求增加到Custom下
    public static class EVENT_JUNCTION_CUSTOM_INFO extends Structure
    {
        public EVENT_CUSTOM_WEIGHT_INFO    stuWeightInfo;      				  // 原始图片信息
        public byte[]					   bReserved = new byte[60];		  // 预留字节 
    }
    
    //建委地磅定制称重信息
    public static class EVENT_CUSTOM_WEIGHT_INFO extends Structure
    {
    	public int        dwRoughWeight;                    				// 毛重,车辆满载货物重量。单位KG
    	public int        dwTareWeight;                     				// 皮重,空车重量。单位KG
    	public int        dwNetWeight;                      				// 净重,载货重量。单位KG
    	public byte[]	  bReserved = new byte[28];					   	 	// 预留字节 
    }
    
    // 事件类型 EVENT_IVS_TRAFFICGATE(交通卡口老规则事件/线圈电警上的交通卡口老规则事件)对应的数据块描述信息
    // 由于历史原因,如果要处理卡口事件,DEV_EVENT_TRAFFICJUNCTION_INFO和EVENT_IVS_TRAFFICGATE要一起处理,以防止有视频电警和线圈电警同时接入平台的情况发生
    // 另外 EVENT_IVS_TRAFFIC_TOLLGATE 只支持新卡口事件的配置
    public static class DEV_EVENT_TRAFFICGATE_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[NET_EVENT_NAME_LEN];      // 事件名称
        public byte                byOpenStrobeState;                          // 开闸状态,具体请见EM_OPEN_STROBE_STATE
        public byte                bReserved1[] = new byte[3];                 // 字节对齐
        public double              PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public NET_MSG_OBJECT      stuObject;                                  // 检测到的物体
        public int                 nLane;                                      // 对应车道号
        public int                 nSpeed;                                     // 车辆实际速度Km/h
        public int                 nSpeedUpperLimit;                           // 速度上限 单位：km/h
        public int                 nSpeedLowerLimit;                           // 速度下限 单位：km/h 
        public int                 dwBreakingRule;                             // 违反规则掩码,第一位:逆行; 
                                                                               // 第二位:压线行驶; 第三位:超速行驶; 
                                                                               // 第四位：欠速行驶; 第五位:闯红灯;第六位:穿过路口(卡口事件)
                                                                               // 第七位: 压黄线; 第八位: 有车占道; 第九位: 黄牌占道;否则默认为:交通卡口事件
        public NET_EVENT_FILE_INFO stuFileInfo;                                // 事件对应文件信息
        public NET_MSG_OBJECT      stuVehicle;                                 // 车身信息
        public byte                szManualSnapNo[] = new byte[64];            // 手动抓拍序号
        public int                 nSequence;                                  // 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束; 
        public byte[]              byReserved = new byte[3];                   // 保留字节
        public byte[]              szSnapFlag = new byte[16];                  // 设备产生的抓拍标识
        public byte                bySnapMode;                                 // 抓拍方式,0-未分类 1-全景 2-近景 4-同向抓拍 8-反向抓拍 16-号牌图像
        public byte                byOverSpeedPercentage;                      // 超速百分比
        public byte                byUnderSpeedingPercentage;                  // 欠速百分比
        public byte                byRedLightMargin;                           // 红灯容许间隔时间,单位：秒
        public byte                byDriveDirection;                           // 行驶方向,0-上行(即车辆离设备部署点越来越近),1-下行(即车辆离设备部署点越来越远)
        public byte[]              szRoadwayNo = new byte[32];                 // 道路编号
        public byte[]              szViolationCode = new byte[16];             // 违章代码
        public byte[]              szViolationDesc = new byte[128];            // 违章描述
        public NET_RESOLUTION_INFO stuResolution;                              // 对应图片的分辨率
        public byte[]              szVehicleType= new byte[32];                // 车辆大小类型 Minisize"微型车,"Light-duty"小型车,"Medium"中型车,
                                                                               // "Oversize"大型车,"Huge"超大车,"Largesize"长车 "Unknown"未知
        public byte                byVehicleLenth;                             // 车辆长度, 单位米
        public byte                byLightState;                               // LightState表示红绿灯状态:0 未知,1 绿灯,2 红灯,3 黄灯
        public byte                byReserved1;                                // 保留字节,留待扩展
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 nOverSpeedMargin;                           // 限高速宽限值    单位：km/h 
        public int                 nUnderSpeedMargin;                          // 限低速宽限值    单位：km/h 
        public byte[]              szDrivingDirection = new byte[3*NET_MAX_DRIVINGDIRECTION]; //
                                                                               // "DrivingDirection" : ["Approach", "上海", "杭州"],行驶方向
                                                                               // "Approach"-上行,即车辆离设备部署点越来越近；"Leave"-下行,
                                                                               // 即车辆离设备部署点越来越远,第二和第三个参数分别代表上行和
                                                                               // 下行的两个地点,UTF-8编码
        public byte[]              szMachineName = new byte[256];              // 本地或远程设备名称
        public byte[]              szMachineAddress = new byte[256];           // 机器部署地点、道路编码
        public byte[]              szMachineGroup = new byte[256];             // 机器分组、设备所属单位
        public int                 dwSnapFlagMask;                             // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"   
        public NET_SIG_CARWAY_INFO_EX stuSigInfo;                              // 由车检器产生抓拍信号冗余信息
        public byte[]              szFilePath = new byte[MAX_PATH];            // 文件路径
        public NET_TIME_EX         RedLightUTC;                                // 红灯开始UTC时间
        public Pointer             szDeviceAddress;                            // 设备地址,OSD叠加到图片上的,来源于配置TrafficSnapshot.DeviceAddress,'\0'结束
        public float               fActualShutter;                             // 当前图片曝光时间,单位为毫秒
        public byte                byActualGain;                               // 当前图片增益,范围为0~100
        public byte                byDirection;                                // 0-南向北 1-西南向东北 2-西向东 3-西北向东南 4-北向南 5-东北向西南 6-东向西 7-东南向西北 8-未知
        public byte                bReserve;                                   // 保留字节, 字节对齐
        public byte                bRetCardNumber;                             // 卡片个数
        public EVENT_CARD_INFO[]   stuCardInfo = (EVENT_CARD_INFO[])new EVENT_CARD_INFO().toArray(NET_EVENT_MAX_CARD_NUM);// 卡片信息
        public byte[]              szDefendCode = new byte[NET_COMMON_STRING_64];           // 图片防伪码
        public int                 nTrafficBlackListID;                         // 关联黑名单数据库记录默认主键ID, 0,无效；> 0,黑名单数据记录
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
        public byte[]              bReserved = new byte[452];                  // 保留字节,留待扩展.
    }
    
    //事件类型EVENT_IVS_TRAFFIC_RUNREDLIGHT(交通-闯红灯事件)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFIC_RUNREDLIGHT_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[128];                     // 事件名称
        public byte[]              bReserved1 = new byte[4];                   // 字节对齐
        public double              PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public int                 nLane;                                      // 对应车道号
        public NET_MSG_OBJECT      stuObject;                                  // 车牌信息
        public NET_MSG_OBJECT      stuVehicle;                                 // 车身信息
        public NET_EVENT_FILE_INFO stuFileInfo;                                // 事件对应文件信息 
        public int                 nLightState;                                // 红绿灯状态 0:未知 1：绿灯 2:红灯 3:黄灯
        public int                 nSpeed;                                     // 车速,km/h
        public int                 nSequence;                                  // 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              byReserved = new byte[2];
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 dwSnapFlagMask;                             // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"    
        public NET_TIME_EX         stRedLightUTC;                              // 红灯开始时间
        public NET_RESOLUTION_INFO stuResolution;                              // 对应图片的分辨率
        public byte                byRedLightMargin;                           // 红灯容许间隔时间,单位：秒
        public byte[]              byAlignment = new byte[3];                  // 字节对齐
        public int                 nRedLightPeriod;                            // 表示红灯周期时间,单位毫秒
        public NET_GPS_INFO        stuGPSInfo;                                 // GPS信息 车载定制
        public byte[]              bReserved = new byte[928];                  // 保留字节
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 交通车辆信息
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
    }
    
    //事件类型EVENT_IVS_TRAFFIC_OVERLINE(交通-压线事件)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFIC_OVERLINE_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[128];                     // 事件名称
        public byte[]              bReserved1 = new byte[4];                   // 字节对齐
        public double              PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public int                 nLane;                                      // 对应车道号
        public NET_MSG_OBJECT      stuObject;                                  // 车牌信息
        public NET_MSG_OBJECT      stuVehicle;                                 // 车身信息
        public NET_EVENT_FILE_INFO stuFileInfo;                                // 事件对应文件信息
        public int                 nSequence;                                  // 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
        public int                 nSpeed;                                     // 车辆实际速度,Km/h
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              byReserved = new byte[2];
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 dwSnapFlagMask;                             // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"    
        public NET_RESOLUTION_INFO stuResolution;                              // 对应图片的分辨率
        public NET_GPS_INFO        stuGPSInfo;                                 // GPS信息 车载定制
        public byte[]              bReserved = new byte[968];                  // 保留字节
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 交通车辆信息
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
    } 
    
    // 事件类型EVENT_IVS_TRAFFIC_RETROGRADE(交通-逆行事件)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFIC_RETROGRADE_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[128];                     // 事件名称
        public byte[]              bReserved1 = new byte[4];                   // 字节对齐
        public double              PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public int                 nLane;                                      // 对应车道号
        public NET_MSG_OBJECT      stuObject;                                  // 车牌信息
        public NET_MSG_OBJECT      stuVehicle;                                 // 车身信息
        public NET_EVENT_FILE_INFO  stuFileInfo;                               // 事件对应文件信息
        public int                 nSequence;                                  // 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
        public int                 nSpeed;                                     // 车辆实际速度,Km/h
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              byReserved = new byte[2];
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 dwSnapFlagMask;                             // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"    
        public NET_RESOLUTION_INFO  stuResolution;                             // 对应图片的分辨率
        public int                bIsExistAlarmRecord;                         // rue:有对应的报警录像; false:无对应的报警录像
        public int                dwAlarmRecordSize;                           // 录像大小
        public byte[]             szAlarmRecordPath = new byte[NET_COMMON_STRING_256];    // 录像路径
        public EVENT_INTELLI_COMM_INFO      intelliCommInfo;                   // 智能事件公共信息
        public NET_GPS_INFO        stuGPSInfo;                                 // GPS信息 车载定制
        public byte[]              bReserved = new byte[484];                  // 保留字节
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 交通车辆信息
        public int                 nDetectNum;                                 // 规则检测区域顶点数
        public NET_POINT[]         DetectRegion = (NET_POINT[])new NET_POINT().toArray(NET_MAX_DETECT_REGION_NUM); // 规则检测区域     
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
    }
    
    //事件类型EVENT_IVS_TRAFFIC_OVERSPEED(交通超速事件)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFIC_OVERSPEED_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[128];                     // 事件名称
        public byte[]              bReserved1 = new byte[4];                   // 字节对齐
        public double              PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public int                 nLane;                                      // 对应车道号
        public NET_MSG_OBJECT      stuObject;                                  // 检测到的物体
        public NET_MSG_OBJECT      stuVehicle;                                 // 车身信息
        public NET_EVENT_FILE_INFO stuFileInfo;                                // 事件对应文件信息
        public int                 nSpeed;                                     // 车辆实际速度Km/h
        public int                 nSpeedUpperLimit;                           // 速度上限 单位：km/h
        public int                 nSpeedLowerLimit;                           // 速度下限 单位：km/h 
        public int                 nSequence;                                  // 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;    
        public byte[]              byReserved = new byte[2];
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 dwSnapFlagMask;                             // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"     
        public NET_RESOLUTION_INFO stuResolution;                              // 对应图片的分辨率
        public byte[]              szFilePath = new byte[MAX_PATH];            // 文件路径
        public EVENT_INTELLI_COMM_INFO     stuIntelliCommInfo;                 // 智能事件公共信息
        public NET_GPS_INFO        stuGPSInfo;                                 // GPS信息  车载定制
        public byte[]                bReserved = new byte[576];                // 保留字节
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 交通车辆信息
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
    }
    
    //事件类型EVENT_IVS_TRAFFIC_UNDERSPEED(交通欠速事件)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFIC_UNDERSPEED_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[128];                     // 事件名称
        public byte[]              bReserved2 = new byte[4];                   // 字节对齐
        public double              PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public int                 nLane;                                      // 对应车道号
        public NET_MSG_OBJECT      stuObject;                                  // 检测到的物体
        public NET_MSG_OBJECT      stuVehicle;                                 // 车身信息
        public NET_EVENT_FILE_INFO stuFileInfo;                                // 事件对应文件信息
        public int                 nSpeed;                                     // 车辆实际速度Km/h
        public int                 nSpeedUpperLimit;                           // 速度上限 单位：km/h
        public int                 nSpeedLowerLimit;                           // 速度下限 单位：km/h 
        public int                 nSequence;                                  // 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              bReserved1 = new byte[2];                   // 对齐
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 nUnderSpeedingPercentage;                   // 欠速百分比
        public int               dwSnapFlagMask;                               // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"     
        public NET_RESOLUTION_INFO  stuResolution;                             // 对应图片的分辨率
        public EVENT_INTELLI_COMM_INFO     stuIntelliCommInfo;                 // 智能事件公共信息
        public NET_GPS_INFO        stuGPSInfo;                                 // GPS信息 车载定制
        public byte[]              bReserved = new byte[832];                  // 保留字节
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 交通车辆信息
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
    }
    
    //事件类型EVENT_IVS_TRAFFIC_WRONGROUTE(交通违章-不按车道行驶)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFIC_WRONGROUTE_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[128];                     // 事件名称
        public byte[]              bReserved1 = new byte[4];                   // 字节对齐
        public double              PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public NET_MSG_OBJECT      stuObject;                                  // 检测到的物体
        public NET_MSG_OBJECT      stuVehicle;                                 // 车身信息
        public int                 nLane;                                      // 对应车道号
        public NET_EVENT_FILE_INFO stuFileInfo;                                // 事件对应文件信息               
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              byReserved = new byte[2];
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 nSpeed;                                     // 车辆实际速度,km/h
        public int                 dwSnapFlagMask;                             // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"     
        public NET_RESOLUTION_INFO stuResolution;                              // 对应图片的分辨率
        public NET_GPS_INFO        stuGPSInfo;                                 // GPS信息 车载定制
        public byte[]              bReserved = new byte[972];                  // 保留字节,留待扩展.
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 交通车辆信息
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
    }
    
	// 事件类型 EVENT_IVS_TRAFFIC_TURNLEFT(交通-违章左转)对应的数据块描述信息
	public static class DEV_EVENT_TRAFFIC_TURNLEFT_INFO extends Structure
	{
	    public int                 		nChannelID;                                 	// 通道号
	    public byte[]                	szName = new byte[128];                         // 事件名称
	    public byte[]                	bReserved1 = new byte[4];                       // 字节对齐
	    public double              		PTS;                                        	// 时间戳(单位是毫秒)
	    public NET_TIME_EX         		UTC ;                        					// 事件发生的时间
	    public int                 		nEventID;                                   	// 事件ID
	    public int                 		nLane;                                      	// 对应车道号
	    public NET_MSG_OBJECT       	stuObject;               						// 车牌信息
	    public NET_MSG_OBJECT       	stuVehicle;              						// 车身信息
	    public NET_EVENT_FILE_INFO  	stuFileInfo;        							// 事件对应文件信息
	    public int                 		nSequence;                                  	// 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
	    public int                 		nSpeed;                                     	// 车辆实际速度,Km/h
	    public byte                		bEventAction;                               	// 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
	    public byte[]                	byReserved = new byte[2];
	    public byte                		byImageIndex;                               	// 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
	    public int               		dwSnapFlagMask;                             	// 抓图标志(按位),具体见  NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"     
	    public NET_RESOLUTION_INFO  	stuResolution;     							 	// 对应图片的分辨率
	    public NET_GPS_INFO        		stuGPSInfo;                                		// GPS信息 车载定制
	    public byte[]                	bReserved = new byte[968];                      // 保留字节
	    public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;   						// 交通车辆信息
	    public EVENT_COMM_INFO     		stCommInfo;             						// 公共信息
	} 
	
	// 事件类型 EVENT_IVS_TRAFFIC_TURNRIGHT (交通-违章右转)对应的数据块描述信息
	public static class DEV_EVENT_TRAFFIC_TURNRIGHT_INFO extends Structure
	{
	    public int                 		nChannelID;                                 	// 通道号
	    public byte[]                	szName = new byte[128];                         // 事件名称
	    public byte[]                	bReserved1 = new byte[4];                       // 字节对齐
	    public double              		PTS;                                        	// 时间戳(单位是毫秒)
	    public NET_TIME_EX         		UTC;                                        	// 事件发生的时间
	    public int                 		nEventID;                                   	// 事件ID
	    public int                 		nLane;                                      	// 对应车道号
	    public NET_MSG_OBJECT       	stuObject;                                  	// 车牌信息
	    public NET_MSG_OBJECT       	stuVehicle;                                 	// 车身信息
	    public NET_EVENT_FILE_INFO  	stuFileInfo;                                	// 事件对应文件信息
	    public int                 		nSequence;                                  	// 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
	    public int                 		nSpeed;                                     	// 车辆实际速度,Km/h
	    public byte                		bEventAction;                               	// 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
	    public byte[]                	byReserved = new byte[2];
	    public byte                		byImageIndex;                               	// 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
	    public int               		dwSnapFlagMask;                             	// 抓图标志(按位),具体见  NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"     
	    public NET_RESOLUTION_INFO  	stuResolution;                              	// 对应图片的分辨率
	    public NET_GPS_INFO        		stuGPSInfo;                                 	// GPS信息 车载定制
	    public byte[]                	bReserved = new byte[968];                      // 保留字节
	    public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 			// 交通车辆信息
	    public EVENT_COMM_INFO     		stCommInfo;                                 	// 公共信息
	} 
	
	// 事件类型EVENT_IVS_TRAFFIC_UTURN(违章调头事件)对应的数据块描述信息
	public static class DEV_EVENT_TRAFFIC_UTURN_INFO extends Structure
	{
		public int                 		nChannelID;                                 	// 通道号
	    public byte[]                	szName = new byte[128];                         // 事件名称
	    public byte[]                	bReserved1 = new byte[4];                       // 字节对齐
	    public double              		PTS;                                        	// 时间戳(单位是毫秒)
	    public NET_TIME_EX         		UTC;                                        	// 事件发生的时间
	    public int                 		nEventID;                                   	// 事件ID
	    public int                 		nLane;                                      	// 对应车道号
	    public NET_MSG_OBJECT       	stuObject;                                  	// 车牌信息
	    public NET_MSG_OBJECT       	stuVehicle;                                 	// 车身信息
	    public NET_EVENT_FILE_INFO  	stuFileInfo;                                	// 事件对应文件信息
	    public int                 		nSequence;                                  	// 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
	    public int                 		nSpeed;                                     	// 车辆实际速度,Km/h
	    public byte                		bEventAction;                               	// 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
	    public byte[]                	byReserved = new byte[2];
	    public byte                		byImageIndex;                               	// 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
	    public int               		dwSnapFlagMask;                             	// 抓图标志(按位),具体见  NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"     
	    public NET_RESOLUTION_INFO  	stuResolution;                              	// 对应图片的分辨率
	    public NET_GPS_INFO        		stuGPSInfo;                                     // GPS信息 车载定制
	    public byte[]                	bReserved = new byte[968];                      // 保留字节
	    public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 			// 交通车辆信息
	    public EVENT_COMM_INFO     		stCommInfo;                                 	// 公共信息
	}
	
	//事件类型 EVENT_IVS_TRAFFIC_RUNYELLOWLIGHT(交通违章-闯黄灯事件)对应数据块描述信息
	public static class DEV_EVENT_TRAFFIC_RUNYELLOWLIGHT_INFO extends Structure
	{
		public int                 		nChannelID;                                 	// 通道号
	    public byte[]                	szName = new byte[128];                         // 事件名称
	    public byte[]                	bReserved1 = new byte[4];                       // 字节对齐
	    public double              		PTS;                                        	// 时间戳(单位是毫秒)
	    public NET_TIME_EX         		UTC;                                        	// 事件发生的时间
	    public int                 		nEventID;                                   	// 事件ID
	    public int                 		nLane;                                      	// 对应车道号
	    public NET_MSG_OBJECT       	stuObject;                                  	// 车牌信息
	    public NET_MSG_OBJECT       	stuVehicle;                                 	// 车身信息
	    public NET_EVENT_FILE_INFO  	stuFileInfo;                                	// 事件对应文件信息
	    public int                 		nLightState;                                	// 红绿灯状态 0:未知 1：绿灯 2:红灯 3:黄灯
	    public int                 		nSpeed;                                     	// 车速,km/h
	    public int                 		nSequence;                                  	// 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
	    public byte                		bEventAction;                               	// 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
	    public byte[]                	byReserved = new byte[2];
	    public byte                		byImageIndex;                               	// 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
	    public int               		dwSnapFlagMask;                             	// 抓图标志(按位),具体见  NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"    
	    public NET_TIME_EX         		stYellowLightUTC;                           	// 黄灯开始时间
	    public int        				nYellowLightPeriod;                         	// 黄灯周期间隔时间,单位秒
	    public NET_RESOLUTION_INFO  	stuResolution;                              	// 对应图片的分辨率
	    public byte               		byRedLightMargin;                           	// 黄灯容许间隔时间,单位：秒
	    public byte[]                	szSourceDevice = new byte[MAX_PATH];            // 事件源设备唯一标识,字段不存在或者为空表示本地设备
	    public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 			// 交通车辆信息
	    public byte[]                	bReserved = new byte[1024];                     // 保留字节
	    public EVENT_COMM_INFO     		stCommInfo;                                 	// 公共信息

	} 
    
    //事件类型EVENT_IVS_TRAFFIC_OVERYELLOWLINE(交通违章-压黄线)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFIC_OVERYELLOWLINE_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[128];                     // 事件名称
        public byte[]              bReserved1 = new byte[4];                   // 字节对齐
        public double              PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public NET_MSG_OBJECT      stuObject;                                  // 检测到的物体
        public NET_MSG_OBJECT      stuVehicle;                                 // 车身信息
        public int                 nLane;                                      // 对应车道号
        public NET_EVENT_FILE_INFO stuFileInfo;                                // 事件对应文件信息 
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              byReserved = new byte[2];
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 nSpeed;                                     // 车辆实际速度,km/h
        public int                 dwSnapFlagMask;                             // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"     
        public NET_RESOLUTION_INFO stuResolution;                              // 对应图片的分辨率
        public int                 bIsExistAlarmRecord;                        // bool 类型： 1:有对应的报警录像; 0:无对应的报警录像
        public int                 dwAlarmRecordSize;                          // 录像大小
        public byte[]              szAlarmRecordPath = new byte[NET_COMMON_STRING_256]; // 录像路径
        public EVENT_INTELLI_COMM_INFO     stuIntelliCommInfo;                 // 智能事件公共信息
        public byte[]              bReserved = new byte[532];                  // 保留字节,留待扩展.
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 交通车辆信息

        public int                 nDetectNum;                                 // 规则检测区域顶点数
        public NET_POINT[]         DetectRegion = (NET_POINT[])new NET_POINT().toArray(NET_MAX_DETECT_REGION_NUM); // 规则检测区域    
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
    }
    
    //事件类型EVENT_IVS_TRAFFIC_YELLOWPLATEINLANE(交通违章-黄牌车占道事件)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFIC_YELLOWPLATEINLANE_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[128];                     // 事件名称
        public byte[]              bReserved1 = new byte[4];                   // 字节对齐
        public double              PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public NET_MSG_OBJECT      stuObject;                                  // 检测到的物体
        public NET_MSG_OBJECT      stuVehicle;                                 // 车身信息
        public int                 nLane;                                      // 对应车道号
        public NET_EVENT_FILE_INFO stuFileInfo;                                // 事件对应文件信息               
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              byReserved = new byte[2];     
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 nSpeed;                                     // 车辆实际速度,km/h
        public int                 dwSnapFlagMask;                             // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"     
        public NET_RESOLUTION_INFO stuResolution;                              // 对应图片的分辨率
        public byte[]              bReserved = new byte[1016];                 // 保留字节,留待扩展.
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 交通车辆信息
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
    }

    //事件类型 EVENT_IVS_TRAFFIC_VEHICLEINROUTE(有车占道事件)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFIC_VEHICLEINROUTE_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[128];                     // 事件名称
        public byte[]              bReserved1 = new byte[4];                   // 字节对齐
        public double              PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public NET_MSG_OBJECT      stuObject;                                  // 检测到的物体
        public NET_MSG_OBJECT      stuVehicle;                                 // 车身信息
        public int                 nLane;                                      // 对应车道号
        public int                 nSequence;                                  // 抓拍序号,如3-2-1/0,1表示抓拍正常结束,0表示抓拍异常结束
        public int                 nSpeed;                                     // 车速
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 表示交通车辆的数据库记录
        public NET_EVENT_FILE_INFO  stuFileInfo;                               // 事件对应文件信息               
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              byReserved0 = new byte[2];
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 dwSnapFlagMask;                             // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"     
        public NET_RESOLUTION_INFO  stuResolution;                             // 对应图片的分辨率
        public EVENT_INTELLI_COMM_INFO     stuIntelliCommInfo;                 // 智能事件公共信息
        public byte[]              byReserved = new byte[884];           
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
    }
    
    //事件类型EVENT_IVS_TRAFFIC_CROSSLANE(交通违章-违章变道)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFIC_CROSSLANE_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[128];                                // 事件名称
        public byte[]              bReserved1 = new byte[4];                              // 字节对齐
        public double              PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public NET_MSG_OBJECT      stuObject;                                  // 检测到的物体
        public NET_MSG_OBJECT      stuVehicle;                                 // 车身信息
        public int                 nLane;                                      // 对应车道号
        public NET_EVENT_FILE_INFO stuFileInfo;                                // 事件对应文件信息               
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              byReserved = new byte[2];       
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
         public int                nSpeed;                                     // 车辆实际速度,km/h
        public int                 dwSnapFlagMask;                               // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"     
        public NET_RESOLUTION_INFO  stuResolution;                             // 对应图片的分辨率
    	public EVENT_INTELLI_COMM_INFO     stuIntelliCommInfo;                 // 智能事件公共信息
        public NET_GPS_INFO        stuGPSInfo;                                 // GPS信息 车载定制
    	public byte[]              bReserved = new byte[836];                   // 保留字节,留待扩展.留待扩展.
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stuTrafficCar;                // 交通车辆信息
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
    }
     
    // 事件类型EVENT_IVS_TRAFFIC_NOPASSING(交通违章-禁止通行事件)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFIC_NOPASSING_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[NET_EVENT_NAME_LEN];      // 事件名称
        public int                 nTriggerType;                               // TriggerType:触发类型,0车检器,1雷达,2视频
        public int                 PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public int                 UTCMS;                                      // 
        public int                 nMark;                                      // 底层产生的触发抓拍帧标记
        public int                 nSequence;                                  // 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
        public NET_EVENT_FILE_INFO  stuFileInfo;                               // 事件对应文件信息
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 交通车辆信息
        public int               dwSnapFlagMask;                               // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"     
        public NET_RESOLUTION_INFO  stuResolution;                             // 对应图片的分辨率
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public byte[]              byReserved1 = new byte[3];
        public int                 nLane;                                      // 对应车道号
        public NET_MSG_OBJECT      stuObject;                                  // 检测到的物体
        public NET_MSG_OBJECT      stuVehicle;                                 // 车身信息
        public int                 nFrameSequence;                             // 视频分析帧序号
        public int                 nSource;                                    // 视频分析的数据源地址   
        public NET_GPS_INFO        stuGPSInfo;                                 // GPS信息 车载定制
        public byte[]              byReserved = new byte[984];                // 保留字节
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
    }
    
    //事件类型 EVENT_IVS_TRAFFIC_PEDESTRAINPRIORITY(斑马线行人优先事件)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFIC_PEDESTRAINPRIORITY_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[128];                     // 事件名称
        public byte[]              bReserved1 = new byte[4];                   // 字节对齐
        public double              PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public NET_MSG_OBJECT       stuObject;                                 // 检测到的物体
        public NET_MSG_OBJECT       stuVehicle;                                // 车身信息
        public NET_EVENT_FILE_INFO  stuFileInfo;                               // 事件对应文件信息
        public int                 nLane;                                      // 对应车道号
        public double              dInitialUTC;                                // 事件初始UTC时间    UTC为事件的UTC (1970-1-1 00:00:00)秒数。
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              byReserved = new byte[2];
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 dwSnapFlagMask;                             // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"     
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 表示交通车辆的数据库记录
        public NET_RESOLUTION_INFO  stuResolution;                             // 对应图片的分辨率
        public NET_GPS_INFO        stuGPSInfo;                                 // GPS信息 车载定制
        public byte[]              bReserved = new byte[984];                  // 保留字节,留待扩展.
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息

    }
    
    //事件类型 EVENT_IVS_TRAFFIC_VEHICLEINBUSROUTE(占用公交车道事件)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFIC_VEHICLEINBUSROUTE_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[128];                     // 事件名称
        public byte[]              bReserved1 = new byte[4];                   // 字节对齐
        public double              PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public NET_MSG_OBJECT       stuObject;                                 // 检测到的物体
        public NET_MSG_OBJECT       stuVehicle;                                // 车身信息
        public NET_EVENT_FILE_INFO  stuFileInfo;                               // 事件对应文件信息
        public int                 nLane;                                      // 对应车道号
        public int                 nSequence;                                  // 抓拍序号,如3-2-1/0,1表示抓拍正常结束,0表示抓拍异常结束
        public int                 nSpeed;                                     // 车速,km/h
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              byReserved = new byte[2];
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int               dwSnapFlagMask;                               // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"     
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 表示交通车辆的数据库记录
        public NET_RESOLUTION_INFO stuResolution;                              // 对应图片的分辨率
        public NET_GPS_INFO        stuGPSInfo;                                 // GPS信息 车载定制
        public byte[]              bReserved = new byte[980];                  // 保留字节,留待扩展.
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息

    }
    
   //事件类型 EVENT_IVS_TRAFFIC_BACKING(违章倒车事件)对应的数据块描述信息
    public static class DEV_EVENT_IVS_TRAFFIC_BACKING_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[128];                     // 事件名称
        public byte[]              bReserved1 = new byte[4];                   // 字节对齐
        public double              PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public NET_MSG_OBJECT       stuObject;                                 // 检测到的物体
        public NET_MSG_OBJECT       stuVehicle;                                // 车身信息
        public NET_EVENT_FILE_INFO  stuFileInfo;                               // 事件对应文件信息
        public int                 nLane;                                      // 对应车道号
        public int                 nSequence;                                  // 抓拍序号,如3-2-1/0,1表示抓拍正常结束,0表示抓拍异常结束
        public int                 nSpeed;                                     // 车速,km/h
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              byReserved = new byte[2];
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 dwSnapFlagMask;                             // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"     
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 表示交通车辆的数据库记录
        public NET_RESOLUTION_INFO  stuResolution;                             // 对应图片的分辨率
        public EVENT_INTELLI_COMM_INFO     stuIntelliCommInfo;                 // 智能事件公共信息
        public NET_GPS_INFO        stuGPSInfo;                                 // GPS信息 车载定制
        public byte[]              bReserved = new byte[848];                  // 保留字节,留待扩展.
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息

    }
    
    // GPS信息
    public static class NET_GPS_INFO extends Structure
    {
        public int                  nLongitude;         	    // 经度(单位是百万分之一度)
                                                                // 西经：0 - 180000000				实际值应为: 180*1000000 – dwLongitude
                                                                // 东经：180000000 - 360000000		实际值应为: dwLongitude – 180*1000000
                                                                // 如: 300168866应为（300168866 - 180*1000000）/1000000 即东经120.168866度
        public int					nLatidude;                  // 纬度(单位是百万分之一度)
                                                                // 南纬：0 - 90000000				实际值应为: 90*1000000 – dwLatidude
                                                                // 北纬：90000000 – 180000000		实际值应为: dwLatidude – 90*1000000
    															// 如: 120186268应为 (120186268 - 90*1000000)/1000000 即北纬30. 186268度
        public double              dbAltitude;                  // 高度,单位为米
        public double              dbSpeed;                     // 速度,单位km/H
        public double              dbBearing;                   // 方向角,单位°
        public byte[]              bReserved = new byte[8];     // 保留字段
		
		public NET_GPS_INFO()
        {
        	if(INetSDK.getOsName().equals("win")) {
                // 强制采用最大四字节对齐
                setAlignType(ALIGN_GNUC);
        	}
        }
    }

 // 事件类型 EVENT_IVS_TRAFFIC_OVERSTOPLINE (压停车线事件)对应的数据块描述信息
 public static class DEV_EVENT_TRAFFIC_OVERSTOPLINE extends Structure
 {
     public int                     nChannelID;                     // 通道号
     public byte[]                  szName = new byte[128];         // 事件名称
     public int                     nTriggerType;                   // TriggerType:触发类型,0车检器,1雷达,2视频
     public int                     PTS;                            // 时间戳(单位是毫秒)
     public NET_TIME_EX             UTC;                            // 事件发生的时间
     public int                     nEventID;                       // 事件ID
     public int                     nSequence;                      // 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
     public byte                    byEventAction;                  // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
     public byte                    byImageIndex;                   // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
     public byte[]                  byReserved1 = new byte[2];
     public int                     nLane;                          // 对应车道号
     public NET_MSG_OBJECT          stuObject;                      // 检测到的物体
     public NET_MSG_OBJECT          stuVehicle;                     // 车身信息
     public NET_EVENT_FILE_INFO     stuFileInfo;                    // 事件对应文件信息
     public int                     nMark;                          // 底层产生的触发抓拍帧标记
     public int                     nFrameSequence;                 // 视频分析帧序号
     public int                     nSource;                        // 视频分析的数据源地址
     public int                     dwSnapFlagMask;                 // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"     
     public NET_RESOLUTION_INFO     stuResolution;                  // 对应图片的分辨率
     public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stuTrafficCar;        // 交通车辆信息
     public int                     nSpeed;                         // 车辆实际速度,Km/h
     public NET_GPS_INFO            stuGPSInfo;                     // GPS信息 车载定制
     public byte[]                  byReserved = new byte[984];    // 保留字节
     public EVENT_COMM_INFO         stCommInfo;                     // 公共信息

 }

 //事件类型 EVENT_IVS_TRAFFIC_PARKINGONYELLOWBOX(黄网格线抓拍事件)对应的数据块描述信息
 public static class DEV_EVENT_TRAFFIC_PARKINGONYELLOWBOX_INFO extends Structure
 {
     public int                 nChannelID;                                 // 通道号
     public byte[]              szName = new byte[128];                     // 事件名称
     public byte[]              bReserved1 = new byte[8];                   // 字节对齐
     public int                 PTS;                                        // 时间戳(单位是毫秒)
     public NET_TIME_EX         UTC;                                        // 事件发生的时间
     public int                 nEventID;                                   // 事件ID
     public int                 nLane;                                      // 对应车道号
     public NET_MSG_OBJECT      stuObject;                                  // 检测到的物体
     public NET_MSG_OBJECT      stuVehicle;                                 // 车身信息
     public NET_EVENT_FILE_INFO stuFileInfo;                                // 事件对应文件信息

     public int                 nInterval1;                                 // 第二张和第一张的延时时间,单位秒
     public int                 nInterval2;                                 // 第三张和第二张的延时时间,单位秒
     public int                 nFollowTime;                                // 跟随时间,如果一辆车与前一辆车进入黄网格的时间差小于此值,就认为是跟车进入,跟车进入情况下如果停车则不算违章

     public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
     public byte[]              byReserved = new byte[2];
     public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
     public int                 dwSnapFlagMask;                             // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"     
     public NET_RESOLUTION_INFO stuResolution;                              // 对应图片的分辨率
     public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 交通车辆信息
     public NET_GPS_INFO        stuGPSInfo;                                 // GPS信息 车载定制
     public byte[]              bReserved = new byte[984];                  // 保留字节
     public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息

 }

 // 事件类型EVENT_IVS_TRAFFIC_WITHOUT_SAFEBELT(交通未系安全带事件事件)对应的数据块描述信息
 public static class  DEV_EVENT_TRAFFIC_WITHOUT_SAFEBELT extends Structure
 {
     public int                     nChannelID;                     // 通道号
     public byte[]                  szName = new byte[128];      // 事件名称
     public int                     nTriggerType;                   // TriggerType:触发类型,0车检器,1雷达,2视频
     public int                     PTS;                            // 时间戳(单位是毫秒)
     public NET_TIME_EX             UTC;                            // 事件发生的时间
     public int                     nEventID;                       // 事件ID
     public int                     nSequence;                      // 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
     public byte                    byEventAction;                  // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;    public byte                    byReserved1[2];
     public byte                    byImageIndex;                   // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
     public byte[]                  byReserved1 = new byte[2];
     public NET_EVENT_FILE_INFO     stuFileInfo;                    // 事件对应文件信息
     public int                     nLane;                          // 对应车道号
     public int                     nMark;                          // 底层产生的触发抓拍帧标记
     public int                     nFrameSequence;                 // 视频分析帧序号
     public int                     nSource;                        // 视频分析的数据源地址
     public NET_MSG_OBJECT          stuObject;                      // 检测到的物体
     public NET_MSG_OBJECT          stuVehicle;                     // 车身信息
     public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stuTrafficCar;        // 交通车辆信息
     public int                     nSpeed;                         // 车辆实际速度,Km/h
     public int      				emMainSeat;                     // 主驾驶座位安全带状态   参考 NET_SAFEBELT_STATE
     public int      				emSlaveSeat;                    // 副驾驶座位安全带状态 参考 NET_SAFEBELT_STATE
     public int                     dwSnapFlagMask;                 // 抓图标志(按位),具体见NET_RESERVED_COMMON , 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"    
     public NET_RESOLUTION_INFO     stuResolution;                  // 对应图片的分辨率
     public NET_GPS_INFO            stuGPSInfo;                     // GPS信息 车载定制
     public byte[]                  byReserved = new byte[984];    // 保留字节
     public EVENT_COMM_INFO         stCommInfo;                     // 公共信息

 }

 //事件类型EVENT_IVS_TRAFFIC_JAM_FORBID_INTO(交通拥堵禁入事件)对应的数据块描述信息
 public static class DEV_EVENT_ALARM_JAMFORBIDINTO_INFO extends Structure
 {
 	 public int                 nChannelID;                                 // 通道号
     public byte[]              szName = new byte[NET_EVENT_NAME_LEN];      // 事件名称
     public byte[]              bReserved1 = new byte[4];                   // 字节对齐
     public int                 PTS;                                        // 时间戳(单位是毫秒)
     public NET_TIME_EX         UTC;                                        // 事件发生的时间
     public int                 nEveID;                                     // 事件ID
     ///////////////////////////////以上为公共字段//////////////////////////////
 	 public NET_EVENT_FILE_INFO stuFileInfo;                               	// 事件对应文件信息 
 	 public int					nMark;										// 底层产生的触发抓拍帧标记
 	 public int					nSource;									// 视频分析的数据源地址
 	 public int					nSequence;									// 表示抓拍序号,如3-2-1/0,1表示抓拍正常结束,0表示抓拍异常结束
 	 public int					nFrameSequence;								// 帧序号
 	 public int					nLane;										// 车道号
 	 public byte                byImageIndex;                   			// 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
 	 public NET_MSG_OBJECT      stuObject;                      			// 检测到的物体
     public NET_GPS_INFO        stuGPSInfo;                                 // GPS信息 车载定制
 	 public byte[]              bReserved = new byte[984];                	// 保留字节
 	 public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stuTrafficCar;                // 交通车辆信息
 	 public EVENT_COMM_INFO     stCommInfo;                     			// 公共信息
     public NET_MSG_OBJECT      stuVehicle;                                 // 车身信息
     public NET_RESOLUTION_INFO stuResolution;                              // 对应图片的分辨率
 }

 //事件类型EVENT_IVS_TRAFFIC_PASSNOTINORDER(交通-未按规定依次通过)对应的数据块描述信息
 public static class DEV_EVENT_TRAFFIC_PASSNOTINORDER_INFO extends Structure
 {
     public int                 nChannelID;                                 // 通道号
     public byte[]              szName = new byte[NET_EVENT_NAME_LEN];      // 事件名称
     public byte[]              bReserved1 = new byte[4];                   // 字节对齐
     public double              PTS;                                        // 时间戳(单位是毫秒)
     public NET_TIME_EX         UTC;                                        // 事件发生的时间
     public int                 nEventID;                                   // 事件ID
     public int                 nLane;                                      // 对应车道号
     public NET_MSG_OBJECT      stuObject;                                  // 车牌信息
     public NET_MSG_OBJECT      stuVehicle;                                 // 车身信息
     public NET_EVENT_FILE_INFO stuFileInfo;                                // 事件对应文件信息 
     public int                 nSequence;                                  // 表示抓拍序号,如3,2,1,1表示抓拍结束,0表示异常结束
     public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
     public byte[]              byReserved = new byte[2];
     public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
     public int                 dwSnapFlagMask;                             // 抓图标志(按位),具体见NET_RESERVED_COMMON , 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"    
     public NET_RESOLUTION_INFO  stuResolution;                             // 对应图片的分辨率
     public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 交通车辆信息
     public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
     public NET_GPS_INFO        stuGPSInfo;                                 // GPS信息 车载定制
     public byte[]              bReserved = new byte[984];                  // 保留字节
 }
    
    //事件类型EVENT_IVS_TRAFFIC_MANUALSNAP(交通手动抓拍事件)对应的数据块描述信息
    public static class DEV_EVENT_TRAFFIC_MANUALSNAP_INFO extends Structure
    {
        public int                 nChannelID;                                 // 通道号
        public byte[]              szName = new byte[128];                     // 事件名称
        public byte[]              bReserved1 = new byte[4];                   // 字节对齐
        public double              PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                        // 事件发生的时间
        public int                 nEventID;                                   // 事件ID
        public int                 nLane;                                      // 对应车道号
        public byte[]              szManualSnapNo = new byte[64];              // 手动抓拍序号 
        public NET_MSG_OBJECT      stuObject;                                  // 检测到的物体
        public NET_MSG_OBJECT      stuVehicle;                                 // 检测到的车身信息
        public DEV_EVENT_TRAFFIC_TRAFFICCAR_INFO stTrafficCar;                 // 表示交通车辆的数据库记录
        public NET_EVENT_FILE_INFO stuFileInfo;                                // 事件对应文件信息
        public byte                bEventAction;                               // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte                byOpenStrobeState;                          // 开闸状态, 具体请见 EM_OPEN_STROBE_STATE
        public byte[]              byReserved = new byte[1];
        public byte                byImageIndex;                               // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 dwSnapFlagMask;                             // 抓图标志(按位),具体见NET_RESERVED_COMMON , 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"    
        public NET_RESOLUTION_INFO stuResolution;                              // 对应图片的分辨率
        public byte[]              bReserved = new byte[1016];                 // 保留字节,留待扩展.
        public EVENT_COMM_INFO     stCommInfo;                                 // 公共信息
    }
    
    // 事件类型 EVENT_IVS_CROSSLINEDETECTION(警戒线事件)对应的数据块描述信息
    public static class DEV_EVENT_CROSSLINE_INFO extends Structure {
        public int                 nChannelID;                         // 通道号
        public byte[]              szName = new byte[128];             // 事件名称
        public byte[]              bReserved1 = new byte[4];           // 字节对齐
        public double              PTS;                                // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                // 事件发生的时间
        public int                 nEventID;                           // 事件ID
        public NET_MSG_OBJECT      stuObject;                          // 检测到的物体
        public NET_EVENT_FILE_INFO stuFileInfo;                        // 事件对应文件信息
        public NET_POINT[]         DetectLine = (NET_POINT[])new NET_POINT().toArray(NET_MAX_DETECT_LINE_NUM);// 规则检测线
        public int                 nDetectLineNum;                     // 规则检测线顶点数
        public NET_POINT[]         TrackLine = (NET_POINT[])new NET_POINT().toArray(NET_MAX_TRACK_LINE_NUM);   // 物体运动轨迹
        public int                 nTrackLineNum;                      // 物体运动轨迹顶点数
        public byte                bEventAction;                       // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte                bDirection;                         // 表示入侵方向, 0-由左至右, 1-由右至左
        public byte                byReserved;
        public byte                byImageIndex;                       // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 dwSnapFlagMask;                     // 抓图标志(按位),具体见  NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout" 
        public int                 nSourceIndex;                       // 事件源设备上的index,-1表示数据无效,-1表示数据无效
        public byte[]              szSourceDevice = new byte[MAX_PATH];           // 事件源设备唯一标识,字段不存在或者为空表示本地设备
        public int        nOccurrenceCount;                   		   // 事件触发累计次数, 类型为unsigned int
        public EVENT_INTELLI_COMM_INFO     stuIntelliCommInfo;         // 智能事件公共信息
        public byte[]              bReserved = new byte[476];          // 保留字节,留待扩展.
    }
    
    // 事件类型 EVENT_IVS_CROSSREGIONDETECTION(警戒区事件)对应的数据块描述信息
    public static class DEV_EVENT_CROSSREGION_INFO extends Structure {
        public int                 nChannelID;                         // 通道号
        public byte[]              szName = new byte[128];             // 事件名称
        public byte[]              bReserved1 = new byte[4];           // 字节对齐
        public double              PTS;                                // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                // 事件发生的时间
        public int                 nEventID;                           // 事件ID
        public NET_MSG_OBJECT      stuObject;                          // 检测到的物体
        public NET_EVENT_FILE_INFO stuFileInfo;                        // 事件对应文件信息
        public NET_POINT[]         DetectRegion = (NET_POINT[])new NET_POINT().toArray(NET_MAX_DETECT_REGION_NUM); // 规则检测区域
        public int                 nDetectRegionNum;                   // 规则检测区域顶点数
        public NET_POINT[]         TrackLine = (NET_POINT[])new NET_POINT().toArray(NET_MAX_TRACK_LINE_NUM);   // 物体运动轨迹
        public int                 nTrackLineNum;                      // 物体运动轨迹顶点数
        public byte                bEventAction;                       // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte                bDirection;                         // 表示入侵方向, 0-进入, 1-离开,2-出现,3-消失
        public byte                bActionType;                        // 表示检测动作类型,0-出现 1-消失 2-在区域内 3-穿越区域
        public byte                byImageIndex;                       // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 dwSnapFlagMask;                     // 抓图标志(按位),具体见NET_RESERVED_COMMON , 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"    
        public int                 nSourceIndex;                       // 事件源设备上的index,-1表示数据无效
        public byte[]              szSourceDevice = new byte[MAX_PATH];// 事件源设备唯一标识,字段不存在或者为空表示本地设备
        public int        		   nOccurrenceCount;                   // 事件触发累计次数, unsigned int 类型
        public byte[]              bReserved = new byte[536];          // 保留字节,留待扩展.
        public int                 nObjectNum;                         // 检测到的物体个数
        public NET_MSG_OBJECT[]    stuObjectIDs = (NET_MSG_OBJECT[]) new NET_MSG_OBJECT().toArray(NET_MAX_OBJECT_LIST);   // 检测到的物体
        public int                 nTrackNum;                          // 轨迹数(与检测到的物体个数  nObjectNum 对应)
        public NET_POLY_POINTS[]   stuTrackInfo = (NET_POLY_POINTS[]) new NET_POLY_POINTS().toArray(NET_MAX_OBJECT_LIST);   // 轨迹信息(与检测到的物体对应)
    	public EVENT_INTELLI_COMM_INFO     stuIntelliCommInfo;         // 智能事件公共信息
    }
    
    // 事件类型 EVENT_IVS_WANDERDETECTION(徘徊事件)对应的数据块描述信息
    public static class DEV_EVENT_WANDER_INFO extends Structure {
        public int                 nChannelID;                         // 通道号
        public byte[]              szName = new byte[128];             // 事件名称
        public byte[]              bReserved1 = new byte[4];           // 字节对齐
        public double              PTS;                                // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                // 事件发生的时间
        public int                 nEventID;                           // 事件ID
        public NET_EVENT_FILE_INFO  stuFileInfo;                       // 事件对应文件信息
        public byte                bEventAction;                       // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              byReserved = new byte[2];           // 保留字节
        public byte                byImageIndex;                       // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 nObjectNum;                         // 检测到的物体个数
        public NET_MSG_OBJECT[]    stuObjectIDs = (NET_MSG_OBJECT[]) new NET_MSG_OBJECT().toArray(NET_MAX_OBJECT_LIST);   // 检测到的物体
        public int                 nTrackNum;                          // 轨迹数(与检测到的物体个数对应)
        public NET_POLY_POINTS[]   stuTrackInfo = (NET_POLY_POINTS[]) new NET_POLY_POINTS().toArray(NET_MAX_OBJECT_LIST);   // 轨迹信息(与检测到的物体对应)
        public int                 nDetectRegionNum;                   // 规则检测区域顶点数
        public NET_POINT[]         DetectRegion = (NET_POINT[])new NET_POINT().toArray(NET_MAX_DETECT_REGION_NUM);    // 规则检测区域
        public int                 dwSnapFlagMask;                     // 抓图标志(按位),具体见NET_RESERVED_COMMON , 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"    
        public int                 nSourceIndex;                       // 事件源设备上的index,-1表示数据无效
        public byte[]              szSourceDevice = new byte[MAX_PATH]; // 事件源设备唯一标识,字段不存在或者为空表示本地设备
        public int        		   nOccurrenceCount;                   // 事件触发累计次数, unsigned int 类型
        public EVENT_INTELLI_COMM_INFO     stuIntelliCommInfo;         // 智能事件公共信息
        public byte[]              bReserved =  new byte[624];         // 保留字节,留待扩展.
    }
    
    //事件类型 EVENT_IVS_LEAVEDETECTION(离岗检测事件)对应数据块描述信息
    public static class DEV_EVENT_IVS_LEAVE_INFO extends Structure {
        public int                 nChannelID;                         // 通道号
        public byte[]              szName = new byte[128];             // 事件名称
        public byte[]              bReserved1 = new byte[4];           // 字节对齐
        public double              PTS;                                // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                // 事件发生的时间
        public int                 nEventID;                           // 事件ID
        public NET_MSG_OBJECT      stuObject;                          // 检测到的物体
        public NET_EVENT_FILE_INFO stuFileInfo;                        // 事件对应文件信息
        public NET_RESOLUTION_INFO stuResolution;                      // 对应图片的分辨率
        public int                 nDetectRegionNum;                   // 规则检测区域顶点数
        public NET_POINT[]         DetectRegion = (NET_POINT[])new NET_POINT().toArray(NET_MAX_DETECT_REGION_NUM);// 规则检测区域
        public byte                bEventAction;                       // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;    
        public byte                byImageIndex;                       // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
    	public EVENT_INTELLI_COMM_INFO     stuIntelliCommInfo;         // 智能事件公共信息
    	public byte[]              bReserved = new byte[894];          // 保留字节
    }

    //事件类型 EVENT_IVS_AUDIO_ABNORMALDETECTION(声音异常检测)对应数据块描述信息
    public static class DEV_EVENT_IVS_AUDIO_ABNORMALDETECTION_INFO extends Structure {
        public int                 nChannelID;                         // 通道号
        public byte[]              szName = new byte[128];             // 事件名称
        public byte[]              bReserved1 = new byte[4];           // 字节对齐
        public double              PTS;                                // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                // 事件发生的时间
        public int                 nEventID;                           // 事件ID
        public NET_EVENT_FILE_INFO  stuFileInfo;                       // 事件对应文件信息
        public int                 nDecibel;                           // 声音强度
        public int                 nFrequency;                         // 声音频率
        public byte                bEventAction;                       // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              byReserved = new byte[2];
        public byte                byImageIndex;                       // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 dwSnapFlagMask;                     // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout" 
        public NET_RESOLUTION_INFO stuResolution;                      // 对应图片的分辨率
        public byte[]              bReserved = new byte[1024];         // 保留字节,留待扩展.
    }
    
    //事件类型 EVENT_IVS_CLIMBDETECTION(攀高检测事件)对应数据块描述信息
    public static class DEV_EVENT_IVS_CLIMB_INFO extends Structure {
        public int                 nChannelID;                         // 通道号
        public byte[]              szName = new byte[128];             // 事件名称
        public byte[]              bReserved1 = new byte[4];           // 字节对齐
        public double              PTS;                                // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                // 事件发生的时间
        public int                 nEventID;                           // 事件ID
        public NET_MSG_OBJECT      stuObject;                          // 检测到的物体
        public NET_EVENT_FILE_INFO stuFileInfo;                        // 事件对应文件信息
        public NET_RESOLUTION_INFO stuResolution;                      // 对应图片的分辨率
        public int                 nDetectLineNum;                     // 规则检测线顶点数
        public NET_POINT[]         DetectLine = (NET_POINT[])new NET_POINT().toArray(NET_MAX_DETECT_LINE_NUM);         // 规则检测线
        public byte                bEventAction;                       // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte                byImageIndex;                       // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int        		   nOccurrenceCount;                   // 事件触发累计次数, unsigned int
        public EVENT_INTELLI_COMM_INFO     stuIntelliCommInfo;         // 智能事件公共信息
        public byte[]              bReserved = new byte[890];          // 保留字节
    }
    
    // 事件类型 EVENT_IVS_FIGHTDETECTION(斗殴事件)对应的数据块描述信息
    public static class DEV_EVENT_FIGHT_INFO extends Structure {
        public int                 nChannelID;                         // 通道号
        public byte[]              szName = new byte[128];             // 事件名称
        public byte[]              bReserved1 = new byte[4];           // 字节对齐
        public double              PTS;                                // 时间戳(单位是毫秒)
        public NET_TIME_EX         UTC;                                // 事件发生的时间
        public int                 nEventID;                           // 事件ID
        public int                 nObjectNum;                         // 检测到的物体个数
        public NET_MSG_OBJECT[]    stuObjectIDs = (NET_MSG_OBJECT[])new NET_MSG_OBJECT().toArray(NET_MAX_OBJECT_LIST);   // 检测到的物体列表
        public NET_EVENT_FILE_INFO  stuFileInfo;                       // 事件对应文件信息
        public byte                bEventAction;                       // 事件动作,0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
        public byte[]              byReserved = new byte[2];           // 保留字节
        public byte                byImageIndex;                       // 图片的序号, 同一时间内(精确到秒)可能有多张图片, 从0开始
        public int                 nDetectRegionNum;                   // 规则检测区域顶点数
        public NET_POINT[]         DetectRegion = (NET_POINT[]) new NET_POINT().toArray(NET_MAX_DETECT_REGION_NUM);    // 规则检测区域
        
        public int                 dwSnapFlagMask;                     // 抓图标志(按位),具体见NET_RESERVED_COMMON, 0位:"*",1位:"Timing",2位:"Manual",3位:"Marked",4位:"Event",5位:"Mosaic",6位:"Cutout"     
        public int                 nSourceIndex;                       // 事件源设备上的index,-1表示数据无效
        public byte[]              szSourceDevice = new byte[MAX_PATH]; // 事件源设备唯一标识,字段不存在或者为空表示本地设备
        public int                 nOccurrenceCount;                   // 事件触发累计次数, unsigned int 类型
        public EVENT_INTELLI_COMM_INFO     stuIntelliCommInfo;         // 智能事件公共信息
        public byte[]              bReserved = new byte[492];          // 保留字节,留待扩展.
    }
    
    // 加油类型
    public static class EM_REFUEL_TYPE extends Structure {
        public static final int 	EM_REFUEL_TYPE_UNKNOWN = 0;								// unknown
        public static final int		EM_REFUEL_TYPE_NINETY_EIGHT = 1;						// "98#"
        public static final int		EM_REFUEL_TYPE_NINETY_SEVEN = 2;						// "97#"
        public static final int		EM_REFUEL_TYPE_NINETY_FIVE  = 3;						// "95#"
        public static final int		EM_REFUEL_TYPE_NINETY_THREE = 4;                        // "93#"
        public static final int		EM_REFUEL_TYPE_NINETY = 5;								// "90#"
        public static final int		EM_REFUEL_TYPE_TEN 	= 6;								// "10#"
        public static final int		EM_REFUEL_TYPE_FIVE = 7;								// "5#"
        public static final int		EM_REFUEL_TYPE_ZERO = 8; 								// "0#"
        public static final int		EM_REFUEL_TYPE_NEGATIVE_TEN = 9;						// "-10#"
        public static final int		EM_REFUEL_TYPE_NEGATIVE_TWENTY = 10;					// "-20#"
        public static final int		EM_REFUEL_TYPE_NEGATIVE_THIRTY_FIVE = 11;				// "-35#"
        public static final int		EM_REFUEL_TYPE_NEGATIVE_FIFTY = 12;						// "-50#"   	
    }

    // 车辆抓拍图片信息
    public static class DEV_EVENT_TRAFFIC_FCC_IMAGE extends Structure {
        public int              dwOffSet; // 图片文件在二进制数据块中的偏移位置, 单位:字节
        public int              dwLength; // 图片大小, 单位:字节
        public short            wWidth;   // 图片宽度, 单位:像素
        public short            wHeight;  // 图片高度, 单位:像素
    }

    // 车辆抓图信息
    public static class DEV_EVENT_TRAFFIC_FCC_OBJECT extends Structure {
    	public DEV_EVENT_TRAFFIC_FCC_IMAGE	stuImage; // 车辆抓拍图片信息
    }

    // 事件类型  EVENT_IVS_TRAFFIC_FCC 加油站提枪、挂枪事件
    public static class DEV_EVENT_TRAFFIC_FCC_INFO extends Structure {
    	public int              nChannelID;                                 // 通道号
        public byte[]           szName = new byte[NET_EVENT_NAME_LEN];      // 事件名称
        public int				nTriggerID;									// 触发类型: 1表示提枪, 2表示挂枪
        public double           PTS;                                        // 时间戳(单位是毫秒)
        public NET_TIME_EX      UTC;                                        // 事件发生的时间
        public int              nEventID;                                   // 事件ID
        ///////////////////////////////以上为公共字段//////////////////////////////

    	public int				nNum;										// 加油枪号
    	public int				nLitre;										// 加油升数,单位 0.01升
    	public int   			emType;										// 加油类型: 取值范围{"90#","93#","10#","-20#"}, 具体参考 EM_REFUEL_TYPE
    	public int				dwMoney;									// 加油金额,单位 0.01元
    	public byte[]		    szText = new byte[NET_COMMON_STRING_16];	// 车牌号
    	public byte[]			szTime = new byte[NET_COMMON_STRING_32];	// 事件发生时间: "2016-05-23 10:31:17"
    	public DEV_EVENT_TRAFFIC_FCC_OBJECT	stuObject;						// 车辆抓图信息
    	public byte[]			bReserved = new byte[1024];					// 保留字节,留待扩展
    }
    
    // 区域或曲线顶点信息
    public static class NET_POLY_POINTS extends Structure
    {
        public int         nPointNum;                               	// 顶点数
        public NET_POINT[] stuPoints = (NET_POINT[])new NET_POINT().toArray(NET_MAX_DETECT_REGION_NUM);     // 顶点信息
    }
    
    
    // 抓图参数结构体
    public static class SNAP_PARAMS extends Structure
    {
        public int     Channel;                       // 抓图的通道
        public int     Quality;                       // 画质；1~6
        public int     ImageSize;                     // 画面大小；0：QCIF,1：CIF,2：D1
        public int     mode;                          // 抓图模式；-1:表示停止抓图, 0：表示请求一帧, 1：表示定时发送请求, 2：表示连续请求
        public int     InterSnap;                     // 时间单位秒；若mode=1表示定时发送请求时
                                                      // 只有部分特殊设备(如：车载设备)支持通过该字段实现定时抓图时间间隔的配置
                                                      // 建议通过 CFG_CMD_ENCODE 配置的stuSnapFormat[nSnapMode].stuVideoFormat.nFrameRate字段实现相关功能
        public int     CmdSerial;                     // 请求序列号，有效值范围 0~65535，超过范围会被截断为 unsigned short
        public int[]   Reserved = new int[4];
    }
    
    // 对应CLIENT_StartSearchDevices接口
    public static class DEVICE_NET_INFO_EX extends Structure
    {
        public int iIPVersion;//4 for IPV4, 6 for IPV6
        public byte[] szIP = new byte[64];//IPIPV4形如"192.168.0.1"
        public int nPort;//tcp端口
        public byte[] szSubmask = new byte[64];//子网掩码IPV6无子网掩码
        public byte[] szGateway = new byte[64];//网关
        public byte[] szMac = new byte[NET_MACADDR_LEN];//MAC地址
        public byte[] szDeviceType = new byte[NET_DEV_TYPE_LEN];//设备类型
        public byte byManuFactory;//目标设备的生产厂商,具体参考EM_IPC_TYPE类
        public byte byDefinition;//1-标清2-高清
        public byte bDhcpEn;//Dhcp使能状态,true-开,false-关, 类型为bool, 取值0或者1
        public byte byReserved1;//字节对齐
        public byte[] verifyData = new byte[88];//校验数据通过异步搜索回调获取(在修改设备IP时会用此信息进行校验)
        public byte[] szSerialNo = new byte[NET_DEV_SERIALNO_LEN];//序列号
        public byte[] szDevSoftVersion = new byte[NET_MAX_URL_LEN];//设备软件版本号
        public byte[] szDetailType = new byte[NET_DEV_TYPE_LEN];//设备型号
        public byte[] szVendor = new byte[NET_MAX_STRING_LEN];//OEM客户类型
        public byte[] szDevName = new byte[NET_MACHINE_NAME_NUM];//设备名称
        public byte[] szUserName = new byte[NET_USER_NAME_LENGTH_EX];//登陆设备用户名（在修改设备IP时需要填写）
        public byte[] szPassWord = new byte[NET_USER_NAME_LENGTH_EX];//登陆设备密码（在修改设备IP时需要填写）
        public short nHttpPort;//HTTP服务端口号, unsigned short类型
        public short wVideoInputCh;//视频输入通道数
        public short wRemoteVideoInputCh;//远程视频输入通道数
        public short wVideoOutputCh;//视频输出通道数
        public short wAlarmInputCh;//报警输入通道数
        public short wAlarmOutputCh;//报警输出通道数
        public int   bNewWordLen;                                         // TRUE使用新密码字段szNewPassWord, BOOL类型
        public byte[]   szNewPassWord = new byte[NET_COMMON_STRING_64];   // 登陆设备密码（在修改设备IP时需要填写）
        public byte		byInitStatus;							          // 设备初始化状态，按位确定初始化状态
    																      // bit0~1：0-老设备，没有初始化功能 1-未初始化账号 2-已初始化账户
    																      // bit2~3：0-老设备，保留 1-公网接入未使能 2-公网接入已使能
    																      // bit4~5：0-老设备，保留 1-手机直连未使能 2-手机直连使能
 
    	public byte		byPwdResetWay;							 		  // 支持密码重置方式：按位确定密码重置方式，只在设备有初始化账号时有意义															
    																	  // bit0-支持预置手机号 bit1-支持预置邮箱 bit2-支持文件导出
    	public byte		bySpecialAbility;								  // 设备初始化能力，按位确定初始化能力
        public byte[]   szNewDetailType = new byte[NET_COMMON_STRING_64]; // 设备型号 
    	public int		bNewUserName;									  // TRUE表示使用新用户名(szNewUserName)字段. BOOL类型
    	public byte[]	szNewUserName = new byte[NET_COMMON_STRING_64];	  // 登陆设备用户名（在修改设备IP时需要填写）
    	public byte[]   cReserved = new byte[41];
    }
    
    // 视频输入通道信息
    public static class NET_VIDEO_INPUTS extends Structure {
        public int                       dwSize;
        public byte[]                    szChnName = new byte[64];                  // 通道名称
        public int                        bEnable;                                // 使能
        public byte[]                   szControlID = new byte[128];            // 控制ID
        public byte[]                   szMainStreamUrl = new byte[MAX_PATH];   // 主码流url地址 
        public byte[]                   szExtraStreamUrl = new byte[MAX_PATH];  // 辅码流url地址
        public int                      nOptionalMainUrlCount;                  // 备用主码流地址数量
        public byte[]                   szOptionalMainUrls = new byte[8*MAX_PATH];  // 备用主码流地址列表
        public int                      nOptionalExtraUrlCount;                 // 备用辅码流地址数量
        public byte[]                   szOptionalExtraUrls = new byte[8*MAX_PATH]; // 备用辅码流地址列表
        
        public NET_VIDEO_INPUTS()
        {
            this.dwSize = this.size();
        }
    }
    
    // 远程设备信息
    public static class NET_REMOTE_DEVICE extends Structure {
        public int                       dwSize;
        public int                       bEnable;                          // 使能
        public byte[]                    szIp     =  new byte[16];         // IP
        public byte[]                    szUser = new byte[8];             // 用户名, 建议使用szUserEx
        public byte[]                    szPwd     = new byte[8];          // 密码, 建议使用szPwdEx
        public int                       nPort;                            // 端口
        public int                       nDefinition;                      // 清晰度, 0-标清, 1-高清
        public int                       emProtocol;                       // 协议类型  NET_DEVICE_PROTOCOL
        public byte[]                    szDevName = new byte[64];         // 设备名称
        public int                       nVideoInputChannels;              // 视频输入通道数
        public int                       nAudioInputChannels;              // 音频输入通道数
        public byte[]                    szDevClass = new byte[32];        // 设备类型, 如IPC, DVR, NVR等
        public byte[]                    szDevType = new byte[32];         // 设备具体型号, 如IPC-HF3300
        public int                       nHttpPort;                        // Http端口
        public int                       nMaxVideoInputCount;              // 视频输入通道最大数
        public int                       nRetVideoInputCount;              // 返回实际通道个数
        public Pointer                   pstuVideoInputs;                  // 视频输入通道信息 NET_VIDEO_INPUTS*
        public byte[]                    szMachineAddress = new byte[256]; // 设备部署地
        public byte[]                    szSerialNo = new byte[48];        // 设备序列号
        public int                       nRtspPort;                        // Rtsp端口

        /*以下用于新平台扩展*/
        public byte[]                    szUserEx = new byte[32];          // 用户名
        public byte[]                    szPwdEx = new byte[32];           // 密码
        
        public NET_REMOTE_DEVICE() 
        {
            this.dwSize = this.size();
        }
    }
    
    // 可用的显示源信息
    public static class NET_MATRIX_CAMERA_INFO extends Structure 
    {
        public int                      dwSize;
        public byte                     szName[] = new byte[128];          // 名称
        public byte                     szDevID[] = new byte[128];         // 设备ID
        public byte                     szszControlID[] = new byte[128];   // 控制ID
        public int                      nChannelID;                        // 通道号, DeviceID设备内唯一
        public int                      nUniqueChannel;                    // 设备内统一编号的唯一通道号
        public int                      bRemoteDevice;                     // 是否远程设备
        public NET_REMOTE_DEVICE        stuRemoteDevice;                   // 远程设备信息
        public int                      emStreamType;                      // 视频码流类型  NET_STREAM_TYPE
        public int                      emChannelType;                     // 通道类型应 NET_LOGIC_CHN_TYPE
               
        public NET_MATRIX_CAMERA_INFO() {
            this.dwSize = this.size();
            stuRemoteDevice = new NET_REMOTE_DEVICE();
        }
    }
    
    // CLIENT_MatrixGetCameras接口的输入参数
    public static class NET_IN_MATRIX_GET_CAMERAS extends Structure {
        public int dwSize; 
        
        public NET_IN_MATRIX_GET_CAMERAS() {
            this.dwSize = this.size();
        }
    }
        
    // CLIENT_MatrixGetCameras接口的输出参数
    public static class NET_OUT_MATRIX_GET_CAMERAS extends Structure {
        public int                        dwSize;                    
        public Pointer                    pstuCameras;            // 显示源信息数组, 用户分配内存  NET_MATRIX_CAMERA_INFO
        public int                        nMaxCameraCount;        // 显示源数组大小
        public int                        nRetCameraCount;        // 返回的显示源数量
        
        public NET_OUT_MATRIX_GET_CAMERAS() {
            this.dwSize = this.size();
        }
    }
    
    // CLIENT_SnapPictureToFile 接口输入参数
    public static class NET_IN_SNAP_PIC_TO_FILE_PARAM extends Structure {
        public int                         dwSize;                    // 结构体大小
        public SNAP_PARAMS                 stuParam;                  // 抓图参数, 其中mode字段仅一次性抓图, 不支持定时或持续抓图; 除了车载DVR, 其他设备仅支持每秒一张的抓图频率

        public byte[]                      szFilePath = new byte[MAX_PATH];// 写入文件的地址
        
        public NET_IN_SNAP_PIC_TO_FILE_PARAM() {
            this.dwSize = this.size();
            this.stuParam = new SNAP_PARAMS();
        }
    }
    
    //  CLIENT_SnapPictureToFile 接口输出参数
    public static class NET_OUT_SNAP_PIC_TO_FILE_PARAM extends Structure {
        public int                        dwSize;                    
        public Pointer                    szPicBuf;               // 图片内容,用户分配内存
        public int                        dwPicBufLen;            // 图片内容内存大小, 单位:字节
        public int                        dwPicBufRetLen;         // 返回的图片大小, 单位:字节
        
        public NET_OUT_SNAP_PIC_TO_FILE_PARAM() {
            this.dwSize = this.size();
        }
        
        public NET_OUT_SNAP_PIC_TO_FILE_PARAM(int nMaxBuf) {
            this.dwSize = this.size();
            this.dwPicBufLen = nMaxBuf;
            Memory mem = new Memory(nMaxBuf);
            mem.clear();
            this.szPicBuf = mem;
        }
    }
    
    // 录像文件信息
    public static class NET_RECORDFILE_INFO extends Structure {
        public int                        ch;                         // 通道号
        public byte[]                     filename = new byte[124];   // 文件名
        public int                        framenum;                   // 文件总帧数
        public int                        size;                       // 文件长度
        public NET_TIME                   starttime = new NET_TIME(); // 开始时间
        public NET_TIME                   endtime = new NET_TIME();   // 结束时间
        public int                        driveno;                    // 磁盘号(区分网络录像和本地录像的类型,0－127表示本地录像,其中64表示光盘1,128表示网络录像)
        public int                        startcluster;               // 起始簇号
        public byte                       nRecordFileType;            // 录象文件类型  0：普通录象；1：报警录象；2：移动检测；3：卡号录象；4：图片, 5: 智能录像,255:所有录像
        public byte                       bImportantRecID;            // 0:普通录像 1:重要录像
        public byte                       bHint;                      // 文件定位索引(nRecordFileType==4<图片>时,bImportantRecID<<8 +bHint ,组成图片定位索引 )
        public byte                       bRecType;                   // 0-主码流录像 1-辅码1流录像 2-辅码流2 3-辅码流3录像
        
        public static class ByValue extends NET_RECORDFILE_INFO implements Structure.ByValue { }
        public static class ByReference extends NET_RECORDFILE_INFO implements Structure.ByReference { }
    }
    
    // 录像查询类型
    public static class EM_QUERY_RECORD_TYPE extends Structure {
        public static final int            EM_RECORD_TYPE_ALL              = 0;            // 所有录像
        public static final int            EM_RECORD_TYPE_ALARM            = 1;            // 外部报警录像
        public static final int            EM_RECORD_TYPE_MOTION_DETECT    = 2;            // 动态检测报警录像
        public static final int            EM_RECORD_TYPE_ALARM_ALL        = 3;            // 所有报警录像
        public static final int            EM_RECORD_TYPE_CARD             = 4;            // 卡号查询
        public static final int            EM_RECORD_TYPE_CONDITION        = 5;            // 按条件查询
        public static final int            EM_RECORD_TYPE_JOIN             = 6;            // 组合查询
        public static final int            EM_RECORD_TYPE_CARD_PICTURE     = 8;            // 按卡号查询图片,HB-U、NVS等使用
        public static final int            EM_RECORD_TYPE_PICTURE          = 9;            // 查询图片,HB-U、NVS等使用
        public static final int            EM_RECORD_TYPE_FIELD            = 10;           // 按字段查询
        public static final int            EM_RECORD_TYPE_INTELLI_VIDEO    = 11;           // 智能录像查询
        public static final int            EM_RECORD_TYPE_NET_DATA         = 15;           // 查询网络数据,金桥网吧等使用
        public static final int            EM_RECORD_TYPE_TRANS_DATA       = 16;           // 查询透明串口数据录像
        public static final int            EM_RECORD_TYPE_IMPORTANT        = 17;           // 查询重要录像
        public static final int            EM_RECORD_TYPE_TALK_DATA        = 18;           // 查询录音文件
        
        public static final int            EM_RECORD_TYPE_INVALID          = 256;          // 无效的查询类型
    }
    
    // 语言种类
    public static class NET_LANGUAGE_TYPE extends Structure
    {
        public static final int NET_LANGUAGE_ENGLISH = 0; //英文
        public static final int NET_LANGUAGE_CHINESE_SIMPLIFIED = NET_LANGUAGE_ENGLISH+1; //简体中文
        public static final int NET_LANGUAGE_CHINESE_TRADITIONAL = NET_LANGUAGE_CHINESE_SIMPLIFIED+1; //繁体中文
        public static final int NET_LANGUAGE_ITALIAN = NET_LANGUAGE_CHINESE_TRADITIONAL+1; //意大利文
        public static final int NET_LANGUAGE_SPANISH = NET_LANGUAGE_ITALIAN+1; //西班牙文
        public static final int NET_LANGUAGE_JAPANESE = NET_LANGUAGE_SPANISH+1; //日文版
        public static final int NET_LANGUAGE_RUSSIAN = NET_LANGUAGE_JAPANESE+1; //俄文版
        public static final int NET_LANGUAGE_FRENCH = NET_LANGUAGE_RUSSIAN+1; //法文版
        public static final int NET_LANGUAGE_GERMAN = NET_LANGUAGE_FRENCH+1; //德文版
        public static final int NET_LANGUAGE_PORTUGUESE = NET_LANGUAGE_GERMAN+1; //葡萄牙语
        public static final int NET_LANGUAGE_TURKEY = NET_LANGUAGE_PORTUGUESE+1; //土尔其语
        public static final int NET_LANGUAGE_POLISH = NET_LANGUAGE_TURKEY+1; //波兰语
        public static final int NET_LANGUAGE_ROMANIAN = NET_LANGUAGE_POLISH+1; //罗马尼亚
        public static final int NET_LANGUAGE_HUNGARIAN = NET_LANGUAGE_ROMANIAN+1; //匈牙利语
        public static final int NET_LANGUAGE_FINNISH = NET_LANGUAGE_HUNGARIAN+1; //芬兰语
        public static final int NET_LANGUAGE_ESTONIAN = NET_LANGUAGE_FINNISH+1; //爱沙尼亚语
        public static final int NET_LANGUAGE_KOREAN = NET_LANGUAGE_ESTONIAN+1; //韩语
        public static final int NET_LANGUAGE_FARSI = NET_LANGUAGE_KOREAN+1; //波斯语
        public static final int NET_LANGUAGE_DANSK = NET_LANGUAGE_FARSI+1; //丹麦语
        public static final int NET_LANGUAGE_CZECHISH = NET_LANGUAGE_DANSK+1; //捷克文
        public static final int NET_LANGUAGE_BULGARIA = NET_LANGUAGE_CZECHISH+1; //保加利亚文
        public static final int NET_LANGUAGE_SLOVAKIAN = NET_LANGUAGE_BULGARIA+1; //斯洛伐克语
        public static final int NET_LANGUAGE_SLOVENIA = NET_LANGUAGE_SLOVAKIAN+1; //斯洛文尼亚文
        public static final int NET_LANGUAGE_CROATIAN = NET_LANGUAGE_SLOVENIA+1; //克罗地亚语
        public static final int NET_LANGUAGE_DUTCH = NET_LANGUAGE_CROATIAN+1; //荷兰语
        public static final int NET_LANGUAGE_GREEK = NET_LANGUAGE_DUTCH+1; //希腊语
        public static final int NET_LANGUAGE_UKRAINIAN = NET_LANGUAGE_GREEK+1; //乌克兰语
        public static final int NET_LANGUAGE_SWEDISH = NET_LANGUAGE_UKRAINIAN+1; //瑞典语
        public static final int NET_LANGUAGE_SERBIAN = NET_LANGUAGE_SWEDISH+1; //塞尔维亚语
        public static final int NET_LANGUAGE_VIETNAMESE = NET_LANGUAGE_SERBIAN+1; //越南语
        public static final int NET_LANGUAGE_LITHUANIAN = NET_LANGUAGE_VIETNAMESE+1; //立陶宛语
        public static final int NET_LANGUAGE_FILIPINO = NET_LANGUAGE_LITHUANIAN+1; //菲律宾语
        public static final int NET_LANGUAGE_ARABIC = NET_LANGUAGE_FILIPINO+1; //阿拉伯语
        public static final int NET_LANGUAGE_CATALAN = NET_LANGUAGE_ARABIC+1; //加泰罗尼亚语
        public static final int NET_LANGUAGE_LATVIAN = NET_LANGUAGE_CATALAN+1; //拉脱维亚语
        public static final int NET_LANGUAGE_THAI = NET_LANGUAGE_LATVIAN+1; //泰语
        public static final int NET_LANGUAGE_HEBREW = NET_LANGUAGE_THAI+1; //希伯来语
        public static final int NET_LANGUAGE_Bosnian = NET_LANGUAGE_HEBREW+1; //波斯尼亚文
    }
    
    // 区域信息
    public static class CFG_RECT extends Structure
    {
        public int nLeft;
        public int nTop;
        public int nRight;
        public int nBottom;
    }

    // 视频输入夜晚特殊配置选项，在晚上光线较暗时自动切换到夜晚的配置参数
    public static class CFG_VIDEO_IN_NIGHT_OPTIONS extends Structure
    {
        public byte bySwitchMode;//已废弃,使用CFG_VIDEO_IN_OPTIONS里面的bySwitchMode
        //0-不切换，总是使用白天配置；1-根据亮度切换；2-根据时间切换；3-不切换，总是使用夜晚配置；4-使用普通配置
        public byte byProfile;//当前使用的配置文件.
                              // 0-白天
                              // 1-晚上
                              // 2-Normal
                              // 0，1,2都为临时配置，使图像生效，便于查看图像调试效果，不点击确定，离开页面不保存至设备。
                              ///3-非临时配置，点击确定后保存至设备，与SwitchMode结合使用，根据SwitchMode决定最终生效的配置。
                              // SwitchMode=0，Profile=3，设置白天配置到设备；
                              // SwitchMode=1，Profile=3，则设置夜晚配置到设备
                              // SwitchMode=2，Profile=3，根据日出日落时间段切换，白天时间段使用白天配置，夜晚时间段使用夜晚配置，保存至设备；
                              // SwitchMode=4，Profile=3；使用普通配置，保存至设备
        public byte byBrightnessThreshold;//亮度阈值0~100
        public byte bySunriseHour;//大致日出和日落时间，日落之后日出之前，将采用夜晚特殊的配置。
        public byte bySunriseMinute;//00:00:00 ~ 23:59:59
        public byte bySunriseSecond;
        public byte bySunsetHour;
        public byte bySunsetMinute;
        public byte bySunsetSecond;
        public byte byGainRed;//红色增益调节，白平衡为"Custom"模式下有效0~100
        public byte byGainBlue;//绿色增益调节，白平衡为"Custom"模式下有效0~100
        public byte byGainGreen;//蓝色增益调节，白平衡为"Custom"模式下有效0~100
        public byte byExposure;//曝光模式；取值范围取决于设备能力集：0-自动曝光，1-曝光等级1，2-曝光等级2…n-1最大曝光等级数n带时间上下限的自动曝光n+1自定义时间手动曝光 (n==byExposureEn）
        public float fExposureValue1;//自动曝光时间下限或者手动曝光自定义时间,毫秒为单位，取值0.1ms~80ms
        public float fExposureValue2;//自动曝光时间上限,毫秒为单位，取值0.1ms~80ms
        public byte byWhiteBalance;//白平衡,0-"Disable", 1-"Auto", 2-"Custom", 3-"Sunny", 4-"Cloudy", 5-"Home", 6-"Office", 7-"Night", 8-"HighColorTemperature", 9-"LowColorTemperature", 10-"AutoColorTemperature", 11-"CustomColorTemperature"
        public byte byGain;//0~100,GainAuto为true时表示自动增益的上限，否则表示固定的增益值
        public byte bGainAuto;//自动增益, 类型为bool, 取值0或1
        public byte bIrisAuto;//自动光圈, 类型为bool, 取值0或1
        public float fExternalSyncPhase;//外同步的相位设置0~360
        public byte byGainMin;//增益下限
        public byte byGainMax;//增益上限
        public byte byBacklight;//背光补偿：取值范围取决于设备能力集：0-关闭1-启用2-指定区域背光补偿
        public byte byAntiFlicker;//防闪烁模式0-Outdoor1-50Hz防闪烁 2-60Hz防闪烁
        public byte byDayNightColor;//日/夜模式；0-总是彩色，1-根据亮度自动切换，2-总是黑白
        public byte byExposureMode;//曝光模式调节曝光等级为自动曝光时有效，取值：0-默认自动，1-增益优先，2-快门优先
        public byte byRotate90;//0-不旋转，1-顺时针90°，2-逆时针90°
        public byte bMirror;//镜像, 类型为bool, 取值0或1
        public byte byWideDynamicRange;//宽动态值0-关闭，1~100-为真实范围值
        public byte byGlareInhibition;//强光抑制0-关闭，1~100为范围值
        public CFG_RECT stuBacklightRegion = new CFG_RECT();//背光补偿区域
        public byte byFocusMode;//0-关闭，1-辅助聚焦，2-自动聚焦
        public byte bFlip;//翻转, 类型为bool, 取值0或1
        public byte[] reserved = new byte[74];//保留
    }

    // 闪光灯配置
    public static class CFG_FLASH_CONTROL extends Structure
    {
        public byte byMode;//工作模式，0-禁止闪光，1-始终闪光，2-自动闪光
        public byte byValue;//工作值,0-0us,1-64us, 2-128us, 3-192...15-960us
        public byte byPole;//触发模式,0-低电平1-高电平 2-上升沿 3-下降沿
        public byte byPreValue;//亮度预设值区间0~100
        public byte byDutyCycle;//占空比,0~100
        public byte byFreqMultiple;//倍频,0~10
        public byte[] reserved = new byte[122];//保留
    }

    // 抓拍参数特殊配置
    public static class CFG_VIDEO_IN_SNAPSHOT_OPTIONS extends Structure
    {
        public byte byGainRed;//红色增益调节，白平衡为"Custom"模式下有效0~100
        public byte byGainBlue;//绿色增益调节，白平衡为"Custom"模式下有效0~100
        public byte byGainGreen;//蓝色增益调节，白平衡为"Custom"模式下有效0~100
        public byte byExposure;//曝光模式；取值范围取决于设备能力集：0-自动曝光，1-曝光等级1，2-曝光等级2…n-1最大曝光等级数n带时间上下限的自动曝光n+1自定义时间手动曝光 (n==byExposureEn）
        public float fExposureValue1;//自动曝光时间下限或者手动曝光自定义时间,毫秒为单位，取值0.1ms~80ms
        public float fExposureValue2;//自动曝光时间上限,毫秒为单位，取值0.1ms~80ms  
        public byte byWhiteBalance;//白平衡,0-"Disable", 1-"Auto", 2-"Custom", 3-"Sunny", 4-"Cloudy", 5-"Home", 6-"Office", 7-"Night", 8-"HighColorTemperature", 9-"LowColorTemperature", 10-"AutoColorTemperature", 11-"CustomColorTemperature"
        public byte byColorTemperature;//色温等级,白平衡为"CustomColorTemperature"模式下有效
        public byte bGainAuto;//自动增益, 类型为bool, 取值0或1
        public byte byGain;//增益调节,GainAuto为true时表示自动增益的上限，否则表示固定的增益值
        public byte[] reversed = new byte[112];//保留
    }

    // 鱼眼镜头配置
    public static class CFG_FISH_EYE extends Structure
    {
        public CFG_POLYGON stuCenterPoint;//鱼眼圆心坐标,范围[0,8192]
        public int nRadius;//鱼眼半径大小,范围[0,8192], 类型为unsigned int
        public float fDirection;//镜头旋转方向,旋转角度[0,360.0]
        public byte byPlaceHolder;//镜头安装方式1顶装，2壁装；3地装,默认1
        public byte byCalibrateMode;//鱼眼矫正模式,详见CFG_CALIBRATE_MODE枚举值
        public byte[] reversed = new byte[31];//保留
    }

    public static class CFG_VIDEO_IN_NORMAL_OPTIONS extends Structure
    {
        public byte byGainRed;//红色增益调节，白平衡为"Custom"模式下有效0~100
        public byte byGainBlue;//绿色增益调节，白平衡为"Custom"模式下有效0~100
        public byte byGainGreen;//蓝色增益调节，白平衡为"Custom"模式下有效0~100
        public byte byExposure;//曝光模式；取值范围取决于设备能力集：0-自动曝光，1-曝光等级1，2-曝光等级2…n-1最大曝光等级数n带时间上下限的自动曝光n+1自定义时间手动曝光 (n==byExposureEn）
        public float fExposureValue1;//自动曝光时间下限或者手动曝光自定义时间,毫秒为单位，取值0.1ms~80ms
        public float fExposureValue2;//自动曝光时间上限,毫秒为单位，取值0.1ms~80ms
        public byte byWhiteBalance;//白平衡,0-"Disable", 1-"Auto", 2-"Custom", 3-"Sunny", 4-"Cloudy", 5-"Home", 6-"Office", 7-"Night", 8-"HighColorTemperature", 9-"LowColorTemperature", 10-"AutoColorTemperature", 11-"CustomColorTemperature"
        public byte byGain;//0~100,GainAuto为true时表示自动增益的上限，否则表示固定的增益值
        public byte bGainAuto;//自动增益, 类型为bool, 取值0或1
        public byte bIrisAuto;//自动光圈, 类型为bool, 取值0或1
        public float fExternalSyncPhase;//外同步的相位设置0~360
        public byte byGainMin;//增益下限
        public byte byGainMax;//增益上限
        public byte byBacklight;//背光补偿：取值范围取决于设备能力集：0-关闭1-启用2-指定区域背光补偿
        public byte byAntiFlicker;//防闪烁模式0-Outdoor1-50Hz防闪烁 2-60Hz防闪烁
        public byte byDayNightColor;//日/夜模式；0-总是彩色，1-根据亮度自动切换，2-总是黑白
        public byte byExposureMode;//曝光模式调节曝光等级为自动曝光时有效，取值：0-默认自动，1-增益优先，2-快门优先
        public byte byRotate90;//0-不旋转，1-顺时针90°，2-逆时针90°
        public byte bMirror;//镜像, 类型为bool, 取值0或1
        public byte byWideDynamicRange;//宽动态值0-关闭，1~100-为真实范围值
        public byte byGlareInhibition;//强光抑制0-关闭，1~100为范围值
        public CFG_RECT stuBacklightRegion;//背光补偿区域
        public byte byFocusMode;//0-关闭，1-辅助聚焦，2-自动聚焦
        public byte bFlip;//翻转, 类型为bool, 取值0或1
        public byte[] reserved = new byte[74];//保留
    }

    // 视频输入前端选项
    public static class CFG_VIDEO_IN_OPTIONS extends Structure
    {
        public byte byBacklight;//背光补偿：取值范围取决于设备能力集：0-关闭1-启用2-指定区域背光补偿
        public byte byDayNightColor;//日/夜模式；0-总是彩色，1-根据亮度自动切换，2-总是黑白
        public byte byWhiteBalance;//白平衡,0-"Disable", 1-"Auto", 2-"Custom", 3-"Sunny", 4-"Cloudy", 5-"Home", 6-"Office", 7-"Night", 8-"HighColorTemperature", 9-"LowColorTemperature", 10-"AutoColorTemperature", 11-"CustomColorTemperature"
        public byte byColorTemperature;//色温等级,白平衡为"CustomColorTemperature"模式下有效
        public byte bMirror;//镜像, 类型为bool, 取值0或1
        public byte bFlip;//翻转, 类型为bool, 取值0或1
        public byte bIrisAuto;//自动光圈, 类型为bool, 取值0或1
        public byte bInfraRed;//根据环境光自动开启红外补偿灯, 类型为bool, 取值0或1
        public byte byGainRed;//红色增益调节，白平衡为"Custom"模式下有效0~100
        public byte byGainBlue;//绿色增益调节，白平衡为"Custom"模式下有效0~100
        public byte byGainGreen;//蓝色增益调节，白平衡为"Custom"模式下有效0~100
        public byte byExposure;//曝光模式；取值范围取决于设备能力集：0-自动曝光，1-曝光等级1，2-曝光等级2…n-1最大曝光等级数n带时间上下限的自动曝光n+1自定义时间手动曝光 (n==byExposureEn）
        public float fExposureValue1;//自动曝光时间下限或者手动曝光自定义时间,毫秒为单位，取值0.1ms~80ms
        public float fExposureValue2;//自动曝光时间上限,毫秒为单位，取值0.1ms~80ms
        public byte bGainAuto;//自动增益, 类型为bool, 取值0或1
        public byte byGain;//增益调节,GainAuto为true时表示自动增益的上限，否则表示固定的增益值
        public byte bySignalFormat;//信号格式,0-Inside(内部输入)1-BT656 2-720p 3-1080p  4-1080i  5-1080sF
        public byte byRotate90;//0-不旋转，1-顺时针90°，2-逆时针90°
        public float fExternalSyncPhase;//外同步的相位设置 0~360   
        public byte byExternalSync;//外部同步信号输入,0-内部同步 1-外部同步
        public byte bySwitchMode;//0-不切换，总是使用白天配置；1-根据亮度切换；2-根据时间切换；3-不切换，总是使用夜晚配置；4-使用普通配置
        public byte byDoubleExposure;//双快门,0-不启用，1-双快门全帧率，即图像和视频只有快门参数不同，2-双快门半帧率，即图像和视频快门及白平衡参数均不同
        public byte byWideDynamicRange;//宽动态值
        public CFG_VIDEO_IN_NIGHT_OPTIONS stuNightOptions;//夜晚参数
        public CFG_FLASH_CONTROL stuFlash;//闪光灯配置
        public CFG_VIDEO_IN_SNAPSHOT_OPTIONS stuSnapshot;//抓拍参数,双快门时有效
        public CFG_FISH_EYE stuFishEye;//鱼眼镜头
        public byte byFocusMode;//0-关闭，1-辅助聚焦，2-自动聚焦
        public byte[] reserved = new byte[28];//保留
        public byte byGainMin;//增益下限
        public byte byGainMax;//增益上限
        public byte byAntiFlicker;//防闪烁模式 0-Outdoor 1-50Hz防闪烁 2-60Hz防闪烁
        public byte byExposureMode;//曝光模式调节曝光等级为自动曝光时有效，取值：0-默认自动，1-增益优先，2-快门优先,4-手动
        public byte byGlareInhibition;//强光抑制0-关闭，1~100为范围值
        public CFG_RECT stuBacklightRegion;//背光补偿区域
        public CFG_VIDEO_IN_NORMAL_OPTIONS stuNormalOptions;//普通参数
    }
    
    // 通用云台控制命令
    public static class NET_PTZ_ControlType extends Structure
    {
        public static final int NET_PTZ_UP_CONTROL = 0;//上
        public static final int NET_PTZ_DOWN_CONTROL = NET_PTZ_UP_CONTROL+1; //下
        public static final int NET_PTZ_LEFT_CONTROL = NET_PTZ_DOWN_CONTROL+1; //左
        public static final int NET_PTZ_RIGHT_CONTROL = NET_PTZ_LEFT_CONTROL+1; //右
        public static final int NET_PTZ_ZOOM_ADD_CONTROL = NET_PTZ_RIGHT_CONTROL+1; //变倍+
        public static final int NET_PTZ_ZOOM_DEC_CONTROL = NET_PTZ_ZOOM_ADD_CONTROL+1; //变倍-
        public static final int NET_PTZ_FOCUS_ADD_CONTROL = NET_PTZ_ZOOM_DEC_CONTROL+1; //调焦+
        public static final int NET_PTZ_FOCUS_DEC_CONTROL = NET_PTZ_FOCUS_ADD_CONTROL+1; //调焦-
        public static final int NET_PTZ_APERTURE_ADD_CONTROL = NET_PTZ_FOCUS_DEC_CONTROL+1; //光圈+
        public static final int NET_PTZ_APERTURE_DEC_CONTROL = NET_PTZ_APERTURE_ADD_CONTROL+1; //光圈-
        public static final int NET_PTZ_POINT_MOVE_CONTROL = NET_PTZ_APERTURE_DEC_CONTROL+1; //转至预置点
        public static final int NET_PTZ_POINT_SET_CONTROL = NET_PTZ_POINT_MOVE_CONTROL+1; //设置
        public static final int NET_PTZ_POINT_DEL_CONTROL = NET_PTZ_POINT_SET_CONTROL+1; //删除
        public static final int NET_PTZ_POINT_LOOP_CONTROL = NET_PTZ_POINT_DEL_CONTROL+1; //点间巡航
        public static final int NET_PTZ_LAMP_CONTROL = NET_PTZ_POINT_LOOP_CONTROL+1; //灯光雨刷
    }

    // 云台控制扩展命令
    public static class NET_EXTPTZ_ControlType extends Structure
    {
        public static final int NET_EXTPTZ_LEFTTOP = 0x20;//左上
        public static final int NET_EXTPTZ_RIGHTTOP = NET_EXTPTZ_LEFTTOP+1; //右上
        public static final int NET_EXTPTZ_LEFTDOWN = NET_EXTPTZ_RIGHTTOP+1; //左下
        public static final int NET_EXTPTZ_RIGHTDOWN = NET_EXTPTZ_LEFTDOWN+1; //右下
        public static final int NET_EXTPTZ_ADDTOLOOP = NET_EXTPTZ_RIGHTDOWN+1; //加入预置点到巡航巡航线路预置点值
        public static final int NET_EXTPTZ_DELFROMLOOP = NET_EXTPTZ_ADDTOLOOP+1; //删除巡航中预置点巡航线路预置点值
        public static final int NET_EXTPTZ_CLOSELOOP = NET_EXTPTZ_DELFROMLOOP+1; //清除巡航巡航线路
        public static final int NET_EXTPTZ_STARTPANCRUISE = NET_EXTPTZ_CLOSELOOP+1; //开始水平旋转
        public static final int NET_EXTPTZ_STOPPANCRUISE = NET_EXTPTZ_STARTPANCRUISE+1; //停止水平旋转
        public static final int NET_EXTPTZ_SETLEFTBORDER = NET_EXTPTZ_STOPPANCRUISE+1; //设置左边界
        public static final int NET_EXTPTZ_SETRIGHTBORDER = NET_EXTPTZ_SETLEFTBORDER+1; //设置右边界
        public static final int NET_EXTPTZ_STARTLINESCAN = NET_EXTPTZ_SETRIGHTBORDER+1; //开始线扫
        public static final int NET_EXTPTZ_CLOSELINESCAN = NET_EXTPTZ_STARTLINESCAN+1; //停止线扫
        public static final int NET_EXTPTZ_SETMODESTART = NET_EXTPTZ_CLOSELINESCAN+1; //设置模式开始模式线路
        public static final int NET_EXTPTZ_SETMODESTOP = NET_EXTPTZ_SETMODESTART+1; //设置模式结束模式线路
        public static final int NET_EXTPTZ_RUNMODE = NET_EXTPTZ_SETMODESTOP+1; //运行模式模式线路
        public static final int NET_EXTPTZ_STOPMODE = NET_EXTPTZ_RUNMODE+1; //停止模式模式线路
        public static final int NET_EXTPTZ_DELETEMODE = NET_EXTPTZ_STOPMODE+1; //清除模式模式线路
        public static final int NET_EXTPTZ_REVERSECOMM = NET_EXTPTZ_DELETEMODE+1; //翻转命令
        public static final int NET_EXTPTZ_FASTGOTO = NET_EXTPTZ_REVERSECOMM+1; //快速定位水平坐标(8192)垂直坐标(8192)变倍(4)
        public static final int NET_EXTPTZ_AUXIOPEN = NET_EXTPTZ_FASTGOTO+1; //辅助开关开辅助点
        public static final int NET_EXTPTZ_AUXICLOSE = NET_EXTPTZ_AUXIOPEN+1; //辅助开关关辅助点
        public static final int NET_EXTPTZ_OPENMENU = 0x36;//打开球机菜单
        public static final int NET_EXTPTZ_CLOSEMENU = NET_EXTPTZ_OPENMENU+1; //关闭菜单
        public static final int NET_EXTPTZ_MENUOK = NET_EXTPTZ_CLOSEMENU+1; //菜单确定
        public static final int NET_EXTPTZ_MENUCANCEL = NET_EXTPTZ_MENUOK+1; //菜单取消
        public static final int NET_EXTPTZ_MENUUP = NET_EXTPTZ_MENUCANCEL+1; //菜单上
        public static final int NET_EXTPTZ_MENUDOWN = NET_EXTPTZ_MENUUP+1; //菜单下
        public static final int NET_EXTPTZ_MENULEFT = NET_EXTPTZ_MENUDOWN+1; //菜单左
        public static final int NET_EXTPTZ_MENURIGHT = NET_EXTPTZ_MENULEFT+1; //菜单右
        public static final int NET_EXTPTZ_ALARMHANDLE = 0x40;//报警联动云台parm1：报警输入通道；parm2：报警联动类型1-预置点2-线扫3-巡航；parm3：联动值，如预置点号
        public static final int NET_EXTPTZ_MATRIXSWITCH = 0x41;//矩阵切换parm1：监视器号(视频输出号)；parm2：视频输入号；parm3：矩阵号
        public static final int NET_EXTPTZ_LIGHTCONTROL= NET_EXTPTZ_MATRIXSWITCH+1; //灯光控制器
        public static final int NET_EXTPTZ_EXACTGOTO = NET_EXTPTZ_LIGHTCONTROL+1; //三维精确定位parm1：水平角度(0~3600)；parm2：垂直坐标(0~900)；parm3：变倍(1~128)
        public static final int NET_EXTPTZ_RESETZERO = NET_EXTPTZ_EXACTGOTO+1; //三维定位重设零位
        public static final int NET_EXTPTZ_MOVE_ABSOLUTELY = NET_EXTPTZ_RESETZERO+1; //绝对移动控制命令，param4对应结构PTZ_CONTROL_ABSOLUTELY
        public static final int NET_EXTPTZ_MOVE_CONTINUOUSLY = NET_EXTPTZ_MOVE_ABSOLUTELY+1; //持续移动控制命令，param4对应结构PTZ_CONTROL_CONTINUOUSLY
        public static final int NET_EXTPTZ_GOTOPRESET = NET_EXTPTZ_MOVE_CONTINUOUSLY+1; //云台控制命令，以一定速度转到预置位点，parm4对应结构PTZ_CONTROL_GOTOPRESET
        public static final int NET_EXTPTZ_SET_VIEW_RANGE = 0x49;//设置可视域(param4对应结构PTZ_VIEW_RANGE_INFO)
        public static final int NET_EXTPTZ_FOCUS_ABSOLUTELY = 0x4A;//绝对聚焦(param4对应结构PTZ_FOCUS_ABSOLUTELY)
        public static final int NET_EXTPTZ_HORSECTORSCAN = 0x4B;//水平扇扫(param4对应PTZ_CONTROL_SECTORSCAN,param1、param2、param3无效)
        public static final int NET_EXTPTZ_VERSECTORSCAN = 0x4C;//垂直扇扫(param4对应PTZ_CONTROL_SECTORSCAN,param1、param2、param3无效)
        public static final int NET_EXTPTZ_SET_ABS_ZOOMFOCUS = 0x4D;//设定绝对焦距、聚焦值,param1为焦距,范围:0,255],param2为聚焦,范围:[0,255],param3、param4无效
        public static final int NET_EXTPTZ_SET_FISHEYE_EPTZ = 0x4E;//控制鱼眼电子云台，param4对应结构PTZ_CONTROL_SET_FISHEYE_EPTZ
        public static final int NET_EXTPTZ_UP_TELE = 0x70;    //上 + TELE param1=速度(1-8)，下同
        public static final int NET_EXTPTZ_DOWN_TELE = NET_EXTPTZ_UP_TELE+1; //下 + TELE
        public static final int NET_EXTPTZ_LEFT_TELE = NET_EXTPTZ_DOWN_TELE+1; //左 + TELE
        public static final int NET_EXTPTZ_RIGHT_TELE = NET_EXTPTZ_LEFT_TELE+1; //右 + TELE
        public static final int NET_EXTPTZ_LEFTUP_TELE = NET_EXTPTZ_RIGHT_TELE+1; //左上 + TELE
        public static final int NET_EXTPTZ_LEFTDOWN_TELE = NET_EXTPTZ_LEFTUP_TELE+1; //左下 + TELE
        public static final int NET_EXTPTZ_TIGHTUP_TELE = NET_EXTPTZ_LEFTDOWN_TELE+1; //右上 + TELE
        public static final int NET_EXTPTZ_RIGHTDOWN_TELE = NET_EXTPTZ_TIGHTUP_TELE+1; //右下 + TELE
        public static final int NET_EXTPTZ_UP_WIDE = NET_EXTPTZ_RIGHTDOWN_TELE+1; // 上 + WIDEparam1=速度(1-8)，下同
        public static final int NET_EXTPTZ_DOWN_WIDE = NET_EXTPTZ_UP_WIDE+1; //下 + WIDE
        public static final int NET_EXTPTZ_LEFT_WIDE = NET_EXTPTZ_DOWN_WIDE+1; //左 + WIDE
        public static final int NET_EXTPTZ_RIGHT_WIDE = NET_EXTPTZ_LEFT_WIDE+1; //右 + WIDE
        public static final int NET_EXTPTZ_LEFTUP_WIDE = NET_EXTPTZ_RIGHT_WIDE+1; //左上 + WIDE
        public static final int NET_EXTPTZ_LEFTDOWN_WIDE = NET_EXTPTZ_LEFTUP_WIDE+1; //左下 + WIDE
        public static final int NET_EXTPTZ_TIGHTUP_WIDE = NET_EXTPTZ_LEFTDOWN_WIDE+1; //右上 + WIDE
        public static final int NET_EXTPTZ_RIGHTDOWN_WIDE = NET_EXTPTZ_TIGHTUP_WIDE+1; //右下 + WIDE
        public static final int NET_EXTPTZ_TOTAL = NET_EXTPTZ_RIGHTDOWN_WIDE+1; //最大命令值
    }

    // 雨刷工作模式
    public static class EM_CFG_RAINBRUSHMODE_MODE extends Structure
    {
        public static final int EM_CFG_RAINBRUSHMODE_MODE_UNKNOWN = 0; //未知
        public static final int EM_CFG_RAINBRUSHMODE_MODE_MANUAL = EM_CFG_RAINBRUSHMODE_MODE_UNKNOWN+1; //手动模式
        public static final int EM_CFG_RAINBRUSHMODE_MODE_TIMING = EM_CFG_RAINBRUSHMODE_MODE_MANUAL+1; //定时模式
    }

    // 雨刷使能电平模式
    public static class EM_CFG_RAINBRUSHMODE_ENABLEMODE extends Structure
    {
        public static final int EM_CFG_RAINBRUSHMODE_ENABLEMODE_UNKNOWN = 0; //未知
        public static final int EM_CFG_RAINBRUSHMODE_ENABLEMODE_LOW = EM_CFG_RAINBRUSHMODE_ENABLEMODE_UNKNOWN+1; //低电平有效（常闭）
        public static final int EM_CFG_RAINBRUSHMODE_ENABLEMODE_HIGH = EM_CFG_RAINBRUSHMODE_ENABLEMODE_LOW+1; //高电平有效（常开）
    }

    // 雨刷模式相关配置(对应 CFG_RAINBRUSHMODE_INFO 命令)
    public static class CFG_RAINBRUSHMODE_INFO extends Structure
    {
        public int emMode;//雨刷工作模式, 取值为EM_CFG_RAINBRUSHMODE_MODE中的值
        public int emEnableMode;//雨刷使能电平模式, 取值为EM_CFG_RAINBRUSHMODE_ENABLEMODE中的值
        public int nPort;//雨刷使用的IO端口,-1表示未接入设备,-2表示该字段无效（设备未传送该字段）
    }

    public static class CFG_RAINBRUSH_INFO extends Structure
    {
        public byte bEnable;//雨刷使能, 类型为bool, 取值0或1
        public byte bSpeedRate;//雨刷速度,1:快速;2:中速;3:慢速
        public byte[] bReserved = new byte[2];//保留对齐
        public CFG_TIME_SECTION[] stuTimeSection = (CFG_TIME_SECTION[])new CFG_TIME_SECTION().toArray(WEEK_DAY_NUM*MAX_REC_TSECT);// 事件响应时间段
    }

    // 控制类型，对应CLIENT_ControlDevice接口
    public static class CtrlType extends Structure
    {
        public static final int CTRLTYPE_CTRL_REBOOT = 0;//重启设备
        public static final int CTRLTYPE_CTRL_SHUTDOWN = CTRLTYPE_CTRL_REBOOT+1; //关闭设备
        public static final int CTRLTYPE_CTRL_DISK = CTRLTYPE_CTRL_SHUTDOWN+1; //硬盘管理
        public static final int CTRLTYPE_KEYBOARD_POWER =3;//网络键盘
        public static final int CTRLTYPE_KEYBOARD_ENTER = CTRLTYPE_KEYBOARD_POWER+1; 
        public static final int CTRLTYPE_KEYBOARD_ESC = CTRLTYPE_KEYBOARD_ENTER+1; 
        public static final int CTRLTYPE_KEYBOARD_UP = CTRLTYPE_KEYBOARD_ESC+1; 
        public static final int CTRLTYPE_KEYBOARD_DOWN = CTRLTYPE_KEYBOARD_UP+1; 
        public static final int CTRLTYPE_KEYBOARD_LEFT = CTRLTYPE_KEYBOARD_DOWN+1; 
        public static final int CTRLTYPE_KEYBOARD_RIGHT = CTRLTYPE_KEYBOARD_LEFT+1; 
        public static final int CTRLTYPE_KEYBOARD_BTN0 = CTRLTYPE_KEYBOARD_RIGHT+1; 
        public static final int CTRLTYPE_KEYBOARD_BTN1 = CTRLTYPE_KEYBOARD_BTN0+1; 
        public static final int CTRLTYPE_KEYBOARD_BTN2 = CTRLTYPE_KEYBOARD_BTN1+1; 
        public static final int CTRLTYPE_KEYBOARD_BTN3 = CTRLTYPE_KEYBOARD_BTN2+1; 
        public static final int CTRLTYPE_KEYBOARD_BTN4 = CTRLTYPE_KEYBOARD_BTN3+1; 
        public static final int CTRLTYPE_KEYBOARD_BTN5 = CTRLTYPE_KEYBOARD_BTN4+1; 
        public static final int CTRLTYPE_KEYBOARD_BTN6 = CTRLTYPE_KEYBOARD_BTN5+1; 
        public static final int CTRLTYPE_KEYBOARD_BTN7 = CTRLTYPE_KEYBOARD_BTN6+1; 
        public static final int CTRLTYPE_KEYBOARD_BTN8 = CTRLTYPE_KEYBOARD_BTN7+1; 
        public static final int CTRLTYPE_KEYBOARD_BTN9 = CTRLTYPE_KEYBOARD_BTN8+1; 
        public static final int CTRLTYPE_KEYBOARD_BTN10 = CTRLTYPE_KEYBOARD_BTN9+1; 
        public static final int CTRLTYPE_KEYBOARD_BTN11 = CTRLTYPE_KEYBOARD_BTN10+1; 
        public static final int CTRLTYPE_KEYBOARD_BTN12 = CTRLTYPE_KEYBOARD_BTN11+1; 
        public static final int CTRLTYPE_KEYBOARD_BTN13 = CTRLTYPE_KEYBOARD_BTN12+1; 
        public static final int CTRLTYPE_KEYBOARD_BTN14 = CTRLTYPE_KEYBOARD_BTN13+1; 
        public static final int CTRLTYPE_KEYBOARD_BTN15 = CTRLTYPE_KEYBOARD_BTN14+1; 
        public static final int CTRLTYPE_KEYBOARD_BTN16 = CTRLTYPE_KEYBOARD_BTN15+1; 
        public static final int CTRLTYPE_KEYBOARD_SPLIT = CTRLTYPE_KEYBOARD_BTN16+1; 
        public static final int CTRLTYPE_KEYBOARD_ONE = CTRLTYPE_KEYBOARD_SPLIT+1; 
        public static final int CTRLTYPE_KEYBOARD_NINE = CTRLTYPE_KEYBOARD_ONE+1; 
        public static final int CTRLTYPE_KEYBOARD_ADDR = CTRLTYPE_KEYBOARD_NINE+1; 
        public static final int CTRLTYPE_KEYBOARD_INFO = CTRLTYPE_KEYBOARD_ADDR+1; 
        public static final int CTRLTYPE_KEYBOARD_REC = CTRLTYPE_KEYBOARD_INFO+1; 
        public static final int CTRLTYPE_KEYBOARD_FN1 = CTRLTYPE_KEYBOARD_REC+1; 
        public static final int CTRLTYPE_KEYBOARD_FN2 = CTRLTYPE_KEYBOARD_FN1+1; 
        public static final int CTRLTYPE_KEYBOARD_PLAY = CTRLTYPE_KEYBOARD_FN2+1; 
        public static final int CTRLTYPE_KEYBOARD_STOP = CTRLTYPE_KEYBOARD_PLAY+1; 
        public static final int CTRLTYPE_KEYBOARD_SLOW = CTRLTYPE_KEYBOARD_STOP+1; 
        public static final int CTRLTYPE_KEYBOARD_FAST = CTRLTYPE_KEYBOARD_SLOW+1; 
        public static final int CTRLTYPE_KEYBOARD_PREW = CTRLTYPE_KEYBOARD_FAST+1; 
        public static final int CTRLTYPE_KEYBOARD_NEXT = CTRLTYPE_KEYBOARD_PREW+1; 
        public static final int CTRLTYPE_KEYBOARD_JMPDOWN = CTRLTYPE_KEYBOARD_NEXT+1; 
        public static final int CTRLTYPE_KEYBOARD_JMPUP = CTRLTYPE_KEYBOARD_JMPDOWN+1; 
        public static final int CTRLTYPE_KEYBOARD_10PLUS = CTRLTYPE_KEYBOARD_JMPUP+1; 
        public static final int CTRLTYPE_KEYBOARD_SHIFT = CTRLTYPE_KEYBOARD_10PLUS+1; 
        public static final int CTRLTYPE_KEYBOARD_BACK = CTRLTYPE_KEYBOARD_SHIFT+1; 
        public static final int CTRLTYPE_KEYBOARD_LOGIN = CTRLTYPE_KEYBOARD_BACK+1;//新网络键盘功能
        public static final int CTRLTYPE_KEYBOARD_CHNNEL = CTRLTYPE_KEYBOARD_LOGIN+1;//切换视频通道
        public static final int CTRLTYPE_TRIGGER_ALARM_IN =100;//触发报警输入
        public static final int CTRLTYPE_TRIGGER_ALARM_OUT = CTRLTYPE_TRIGGER_ALARM_IN+1; //触发报警输出
        public static final int CTRLTYPE_CTRL_MATRIX = CTRLTYPE_TRIGGER_ALARM_OUT+1; //矩阵控制
        public static final int CTRLTYPE_CTRL_SDCARD = CTRLTYPE_CTRL_MATRIX+1; //SD卡控制(IPC产品)参数同硬盘控制
        public static final int CTRLTYPE_BURNING_START = CTRLTYPE_CTRL_SDCARD+1; //刻录机控制，开始刻录
        public static final int CTRLTYPE_BURNING_STOP = CTRLTYPE_BURNING_START+1; //刻录机控制，结束刻录
        public static final int CTRLTYPE_BURNING_ADDPWD = CTRLTYPE_BURNING_STOP+1; //刻录机控制，叠加密码(以'\0'为结尾的字符串，最大长度8位)
        public static final int CTRLTYPE_BURNING_ADDHEAD = CTRLTYPE_BURNING_ADDPWD+1; //刻录机控制，叠加片头(以'\0'为结尾的字符串，最大长度1024字节，支持分行，行分隔符'\n')
        public static final int CTRLTYPE_BURNING_ADDSIGN = CTRLTYPE_BURNING_ADDHEAD+1; //刻录机控制，叠加打点到刻录信息(参数无)
        public static final int CTRLTYPE_BURNING_ADDCURSTOMINFO = CTRLTYPE_BURNING_ADDSIGN+1; //刻录机控制，自定义叠加(以'\0'为结尾的字符串，最大长度1024字节，支持分行，行分隔符'\n')
        public static final int CTRLTYPE_CTRL_RESTOREDEFAULT = CTRLTYPE_BURNING_ADDCURSTOMINFO+1; //恢复设备的默认设置
        public static final int CTRLTYPE_CTRL_CAPTURE_START = CTRLTYPE_CTRL_RESTOREDEFAULT+1; //触发设备抓图
        public static final int CTRLTYPE_CTRL_CLEARLOG = CTRLTYPE_CTRL_CAPTURE_START+1; //清除日志
        public static final int CTRLTYPE_TRIGGER_ALARM_WIRELESS =200;//触发无线报警(IPC产品)
        public static final int CTRLTYPE_MARK_IMPORTANT_RECORD = CTRLTYPE_TRIGGER_ALARM_WIRELESS+1; //标识重要录像文件
        public static final int CTRLTYPE_CTRL_DISK_SUBAREA = CTRLTYPE_MARK_IMPORTANT_RECORD+1; //网络硬盘分区
        public static final int CTRLTYPE_BURNING_ATTACH = CTRLTYPE_CTRL_DISK_SUBAREA+1; //刻录机控制，附件刻录.
        public static final int CTRLTYPE_BURNING_PAUSE = CTRLTYPE_BURNING_ATTACH+1; //刻录暂停
        public static final int CTRLTYPE_BURNING_CONTINUE = CTRLTYPE_BURNING_PAUSE+1; //刻录继续
        public static final int CTRLTYPE_BURNING_POSTPONE = CTRLTYPE_BURNING_CONTINUE+1; //刻录顺延
        public static final int CTRLTYPE_CTRL_OEMCTRL = CTRLTYPE_BURNING_POSTPONE+1; //报停控制
        public static final int CTRLTYPE_BACKUP_START = CTRLTYPE_CTRL_OEMCTRL+1; //设备备份开始
        public static final int CTRLTYPE_BACKUP_STOP = CTRLTYPE_BACKUP_START+1; //设备备份停止
        public static final int CTRLTYPE_VIHICLE_WIFI_ADD = CTRLTYPE_BACKUP_STOP+1; //车载手动增加WIFI配置
        public static final int CTRLTYPE_VIHICLE_WIFI_DEC = CTRLTYPE_VIHICLE_WIFI_ADD+1; //车载手动删除WIFI配置
        public static final int CTRLTYPE_BUZZER_START = CTRLTYPE_VIHICLE_WIFI_DEC+1; //蜂鸣器控制开始
        public static final int CTRLTYPE_BUZZER_STOP = CTRLTYPE_BUZZER_START+1; //蜂鸣器控制结束
        public static final int CTRLTYPE_REJECT_USER = CTRLTYPE_BUZZER_STOP+1; //剔除用户
        public static final int CTRLTYPE_SHIELD_USER = CTRLTYPE_REJECT_USER+1; //屏蔽用户
        public static final int CTRLTYPE_RAINBRUSH = CTRLTYPE_SHIELD_USER+1; //智能交通,雨刷控制
        public static final int CTRLTYPE_MANUAL_SNAP = CTRLTYPE_RAINBRUSH+1; //智能交通,手动抓拍(对应结构体MANUAL_SNAP_PARAMETER)
        public static final int CTRLTYPE_MANUAL_NTP_TIMEADJUST = CTRLTYPE_MANUAL_SNAP+1; //手动NTP校时
        public static final int CTRLTYPE_NAVIGATION_SMS = CTRLTYPE_MANUAL_NTP_TIMEADJUST+1; //导航信息和短消息
        public static final int CTRLTYPE_CTRL_ROUTE_CROSSING = CTRLTYPE_NAVIGATION_SMS+1; //路线点位信息
        public static final int CTRLTYPE_BACKUP_FORMAT = CTRLTYPE_CTRL_ROUTE_CROSSING+1; //格式化备份设备
        public static final int CTRLTYPE_DEVICE_LOCALPREVIEW_SLIPT = CTRLTYPE_BACKUP_FORMAT+1; //控制设备端本地预览分割(对应结构体DEVICE_LOCALPREVIEW_SLIPT_PARAMETER)
        public static final int CTRLTYPE_CTRL_INIT_RAID = CTRLTYPE_DEVICE_LOCALPREVIEW_SLIPT+1; //RAID初始化
        public static final int CTRLTYPE_CTRL_RAID = CTRLTYPE_CTRL_INIT_RAID+1; //RAID操作
        public static final int CTRLTYPE_CTRL_SAPREDISK = CTRLTYPE_CTRL_RAID+1; //热备盘操作
        public static final int CTRLTYPE_WIFI_CONNECT = CTRLTYPE_CTRL_SAPREDISK+1; //手动发起WIFI连接(对应结构体WIFI_CONNECT)
        public static final int CTRLTYPE_WIFI_DISCONNECT = CTRLTYPE_WIFI_CONNECT+1; //手动断开WIFI连接(对应结构体WIFI_CONNECT)
        public static final int CTRLTYPE_CTRL_ARMED = CTRLTYPE_WIFI_DISCONNECT+1; //布撤防操作
        public static final int CTRLTYPE_CTRL_IP_MODIFY = CTRLTYPE_CTRL_ARMED+1; //修改前端IP(对应结构体 NET_CTRL_IPMODIFY_PARAM)
        public static final int CTRLTYPE_CTRL_WIFI_BY_WPS = CTRLTYPE_CTRL_IP_MODIFY+1; //wps连接wifi(对应结构体NET_CTRL_CONNECT_WIFI_BYWPS)
        public static final int CTRLTYPE_CTRL_FORMAT_PATITION = CTRLTYPE_CTRL_WIFI_BY_WPS+1; //格式化分区(对应结构体NET_FORMAT_PATITION)
        public static final int CTRLTYPE_CTRL_EJECT_STORAGE = CTRLTYPE_CTRL_FORMAT_PATITION+1; //手动卸载设备(对应结构体NET_EJECT_STORAGE_DEVICE)
        public static final int CTRLTYPE_CTRL_LOAD_STORAGE = CTRLTYPE_CTRL_EJECT_STORAGE+1; //手动装载设备(对应结构体NET_LOAD_STORAGE_DEVICE)
        public static final int CTRLTYPE_CTRL_CLOSE_BURNER = CTRLTYPE_CTRL_LOAD_STORAGE+1; //关闭刻录机光驱门(对应结构体NET_CTRL_BURNERDOOR)一般需要等6
        public static final int CTRLTYPE_CTRL_EJECT_BURNER = CTRLTYPE_CTRL_CLOSE_BURNER+1; //弹出刻录机光驱门(对应结构体NET_CTRL_BURNERDOOR)一般需要等4秒
        public static final int CTRLTYPE_CTRL_CLEAR_ALARM = CTRLTYPE_CTRL_EJECT_BURNER+1; //消警(对应结构体NET_CTRL_CLEAR_ALARM)
        public static final int CTRLTYPE_CTRL_MONITORWALL_TVINFO = CTRLTYPE_CTRL_CLEAR_ALARM+1; //电视墙信息显示(对应结构体NET_CTRL_MONITORWALL_TVINFO)
        public static final int CTRLTYPE_CTRL_START_VIDEO_ANALYSE = CTRLTYPE_CTRL_MONITORWALL_TVINFO+1; //开始视频智能分析(对应结构体NET_CTRL_START_VIDEO_ANALYSE)
        public static final int CTRLTYPE_CTRL_STOP_VIDEO_ANALYSE = CTRLTYPE_CTRL_START_VIDEO_ANALYSE+1; //停止视频智能分析(对应结构体NET_CTRL_STOP_VIDEO_ANALYSE)
        public static final int CTRLTYPE_CTRL_UPGRADE_DEVICE = CTRLTYPE_CTRL_STOP_VIDEO_ANALYSE+1; //控制启动设备升级,由设备独立完成升级过程,不需要传输升级文件
        public static final int CTRLTYPE_CTRL_MULTIPLAYBACK_CHANNALES = CTRLTYPE_CTRL_UPGRADE_DEVICE+1; //切换多通道预览回放的通道(对应结构体NET_CTRL_MULTIPLAYBACK_CHANNALES)
        public static final int CTRLTYPE_CTRL_SEQPOWER_OPEN = CTRLTYPE_CTRL_MULTIPLAYBACK_CHANNALES+1; //电源时序器打开开关量输出口(对应NET_CTRL_SEQPOWER_PARAM)
        public static final int CTRLTYPE_CTRL_SEQPOWER_CLOSE = CTRLTYPE_CTRL_SEQPOWER_OPEN+1; //电源时序器关闭开关量输出口(对应NET_CTRL_SEQPOWER_PARAM)
        public static final int CTRLTYPE_CTRL_SEQPOWER_OPEN_ALL = CTRLTYPE_CTRL_SEQPOWER_CLOSE+1; //电源时序器打开开关量输出口组(对应NET_CTRL_SEQPOWER_PARAM)
        public static final int CTRLTYPE_CTRL_SEQPOWER_CLOSE_ALL = CTRLTYPE_CTRL_SEQPOWER_OPEN_ALL+1; //电源时序器关闭开关量输出口组(对应NET_CTRL_SEQPOWER_PARAM)
        public static final int CTRLTYPE_CTRL_PROJECTOR_RISE = CTRLTYPE_CTRL_SEQPOWER_CLOSE_ALL+1; //投影仪上升(对应NET_CTRL_PROJECTOR_PARAM)
        public static final int CTRLTYPE_CTRL_PROJECTOR_FALL = CTRLTYPE_CTRL_PROJECTOR_RISE+1; //投影仪下降(对应NET_CTRL_PROJECTOR_PARAM)
        public static final int CTRLTYPE_CTRL_PROJECTOR_STOP = CTRLTYPE_CTRL_PROJECTOR_FALL+1; //投影仪停止(对应NET_CTRL_PROJECTOR_PARAM)
        public static final int CTRLTYPE_CTRL_INFRARED_KEY = CTRLTYPE_CTRL_PROJECTOR_STOP+1; //红外按键(对应NET_CTRL_INFRARED_KEY_PARAM)
        public static final int CTRLTYPE_CTRL_START_PLAYAUDIO = CTRLTYPE_CTRL_INFRARED_KEY+1; //设备开始播放音频文件(对应结构体NET_CTRL_START_PLAYAUDIO)
        public static final int CTRLTYPE_CTRL_STOP_PLAYAUDIO = CTRLTYPE_CTRL_START_PLAYAUDIO+1; //设备停止播放音频文件
        public static final int CTRLTYPE_CTRL_START_ALARMBELL = CTRLTYPE_CTRL_STOP_PLAYAUDIO+1; //开启警号(对应结构体 NET_CTRL_ALARMBELL )
        public static final int CTRLTYPE_CTRL_STOP_ALARMBELL = CTRLTYPE_CTRL_START_ALARMBELL+1; //关闭警号(对应结构体 NET_CTRL_ALARMBELL )
        public static final int CTRLTYPE_CTRL_ACCESS_OPEN = CTRLTYPE_CTRL_STOP_ALARMBELL+1; //门禁控制-开门(对应结构体 NET_CTRL_ACCESS_OPEN)
        public static final int CTRLTYPE_CTRL_SET_BYPASS = CTRLTYPE_CTRL_ACCESS_OPEN+1; //设置旁路功能(对应结构体NET_CTRL_SET_BYPASS)
        public static final int CTRLTYPE_CTRL_RECORDSET_INSERT = CTRLTYPE_CTRL_SET_BYPASS+1; //添加记录，获得记录集编号(对应NET_CTRL_RECORDSET_INSERT_PARAM)
        public static final int CTRLTYPE_CTRL_RECORDSET_UPDATE = CTRLTYPE_CTRL_RECORDSET_INSERT+1; //更新某记录集编号的记录(对应NET_CTRL_RECORDSET_PARAM)
        public static final int CTRLTYPE_CTRL_RECORDSET_REMOVE = CTRLTYPE_CTRL_RECORDSET_UPDATE+1; //根据记录集编号删除某记录(对应NET_CTRL_RECORDSET_PARAM)
        public static final int CTRLTYPE_CTRL_RECORDSET_CLEAR = CTRLTYPE_CTRL_RECORDSET_REMOVE+1; //清除所有记录集信息(对应NET_CTRL_RECORDSET_PARAM)
        public static final int CTRLTYPE_CTRL_ACCESS_CLOSE = CTRLTYPE_CTRL_RECORDSET_CLEAR+1; //门禁控制-关门(对应结构体NET_CTRL_ACCESS_CLOSE)
        public static final int CTRLTYPE_CTRL_ALARM_SUBSYSTEM_ACTIVE_SET = CTRLTYPE_CTRL_ACCESS_CLOSE+1; //报警子系统激活设置(对应结构体NET_CTRL_ALARM_SUBSYSTEM_SETACTIVE)
        public static final int CTRLTYPE_CTRL_FORBID_OPEN_STROBE = CTRLTYPE_CTRL_ALARM_SUBSYSTEM_ACTIVE_SET+1; //禁止设备端开闸(对应结构体NET_CTRL_FORBID_OPEN_STROBE)
        public static final int CTRLTYPE_CTRL_OPEN_STROBE = CTRLTYPE_CTRL_FORBID_OPEN_STROBE+1; //开启道闸(对应结构体 NET_CTRL_OPEN_STROBE)
        public static final int CTRLTYPE_CTRL_TALKING_REFUSE = CTRLTYPE_CTRL_OPEN_STROBE+1; //对讲拒绝接听(对应结构体NET_CTRL_TALKING_REFUSE)
        public static final int CTRLTYPE_CTRL_ARMED_EX = CTRLTYPE_CTRL_TALKING_REFUSE+1; //布撤防操作(对应结构体CTRL_ARM_DISARM_PARAM_EX),对CTRL_ARM_DISARM_PARAM升级，建议用这个
        public static final int CTRLTYPE_CTRL_NET_KEYBOARD =400;//网络键盘控制(对应结构体NET_CTRL_NET_KEYBOARD)
        public static final int CTRLTYPE_CTRL_AIRCONDITION_OPEN = CTRLTYPE_CTRL_NET_KEYBOARD+1; //打开空调(对应结构体NET_CTRL_OPEN_AIRCONDITION)
        public static final int CTRLTYPE_CTRL_AIRCONDITION_CLOSE = CTRLTYPE_CTRL_AIRCONDITION_OPEN+1; //关闭空调(对应结构体NET_CTRL_CLOSE_AIRCONDITION)
        public static final int CTRLTYPE_CTRL_AIRCONDITION_SET_TEMPERATURE = CTRLTYPE_CTRL_AIRCONDITION_CLOSE+1; //设定空调温度(对应结构体NET_CTRL_SET_TEMPERATURE)
        public static final int CTRLTYPE_CTRL_AIRCONDITION_ADJUST_TEMPERATURE = CTRLTYPE_CTRL_AIRCONDITION_SET_TEMPERATURE+1; //调节空调温度(对应结构体NET_CTRL_ADJUST_TEMPERATURE)
        public static final int CTRLTYPE_CTRL_AIRCONDITION_SETMODE = CTRLTYPE_CTRL_AIRCONDITION_ADJUST_TEMPERATURE+1; //设置空调工作模式(对应结构体NET_CTRL_ADJUST_TEMPERATURE)
        public static final int CTRLTYPE_CTRL_AIRCONDITION_SETWINDMODE = CTRLTYPE_CTRL_AIRCONDITION_SETMODE+1; //设置空调送风模式(对应结构体NET_CTRL_AIRCONDITION_SETMODE)
        public static final int CTRLTYPE_CTRL_RESTOREDEFAULT_EX  = CTRLTYPE_CTRL_AIRCONDITION_SETWINDMODE+1;//恢复设备的默认设置新协议(对应结构体NET_CTRL_RESTORE_DEFAULT)
                                                                                                  // 恢复配置优先使用该枚举，如果接口失败，
                                                                                                  // 且CLIENT_GetLastError返回NET_UNSUPPORTED,再尝试使用NET_CTRL_RESTOREDEFAULT恢复配置
        public static final int CTRLTYPE_CTRL_NOTIFY_EVENT = CTRLTYPE_CTRL_RESTOREDEFAULT_EX+1; //向设备发送事件(对应结构体NET_NOTIFY_EVENT_DATA)
        public static final int CTRLTYPE_CTRL_SILENT_ALARM_SET = CTRLTYPE_CTRL_NOTIFY_EVENT+1; //无声报警设置
        public static final int CTRLTYPE_CTRL_START_PLAYAUDIOEX = CTRLTYPE_CTRL_SILENT_ALARM_SET+1; //设备开始语音播报(对应结构体NET_CTRL_START_PLAYAUDIOEX)
        public static final int CTRLTYPE_CTRL_STOP_PLAYAUDIOEX = CTRLTYPE_CTRL_START_PLAYAUDIOEX+1; //设备停止语音播报
        public static final int CTRLTYPE_CTRL_CLOSE_STROBE = CTRLTYPE_CTRL_STOP_PLAYAUDIOEX+1; //关闭道闸(对应结构体 NET_CTRL_CLOSE_STROBE)
        public static final int CTRLTYPE_CTRL_SET_ORDER_STATE = CTRLTYPE_CTRL_CLOSE_STROBE+1; //设置车位预定状态(对应结构体NET_CTRL_SET_ORDER_STATE)
        public static final int CTRLTYPE_CTRL_RECORDSET_INSERTEX = CTRLTYPE_CTRL_SET_ORDER_STATE+1; //添加记录，获得记录集编号(对应NET_CTRL_RECORDSET_INSERT_PARAM)
        public static final int CTRLTYPE_CTRL_RECORDSET_UPDATEEX = CTRLTYPE_CTRL_RECORDSET_INSERTEX+1; //更新某记录集编号的记录(对应NET_CTRL_RECORDSET_PARAM)
        public static final int CTRLTYPE_CTRL_CAPTURE_FINGER_PRINT = CTRLTYPE_CTRL_RECORDSET_UPDATEEX+1; //指纹采集(对应结构体NET_CTRL_CAPTURE_FINGER_PRINT)
        public static final int CTRLTYPE_CTRL_ECK_LED_SET = CTRLTYPE_CTRL_CAPTURE_FINGER_PRINT+1; //停车场出入口控制器LED设置(对应结构体NET_CTRL_ECK_LED_SET_PARAM)
        public static final int CTRLTYPE_CTRL_ECK_IC_CARD_IMPORT = CTRLTYPE_CTRL_ECK_LED_SET+1; //智能停车系统出入口机IC卡信息导入(对应结构体NET_CTRL_ECK_IC_CARD_IMPORT_PARAM)
        public static final int CTRLTYPE_CTRL_ECK_SYNC_IC_CARD = CTRLTYPE_CTRL_ECK_IC_CARD_IMPORT+1; //智能停车系统出入口机IC卡信息同步指令，收到此指令后，设备删除原有IC卡信息(对应结构体NET_CTRL_ECK_SYNC_IC_CARD_PARAM)
        public static final int CTRLTYPE_CTRL_LOWRATEWPAN_REMOVE = CTRLTYPE_CTRL_ECK_SYNC_IC_CARD+1; //删除指定无线设备(对应结构体NET_CTRL_LOWRATEWPAN_REMOVE)
        public static final int CTRLTYPE_CTRL_LOWRATEWPAN_MODIFY = CTRLTYPE_CTRL_LOWRATEWPAN_REMOVE+1; //修改无线设备信息(对应结构体NET_CTRL_LOWRATEWPAN_MODIFY)
        public static final int CTRLTYPE_CTRL_ECK_SET_PARK_INFO = CTRLTYPE_CTRL_LOWRATEWPAN_MODIFY+1; //智能停车系统出入口机设置车位信息(对应结构体NET_CTRL_ECK_SET_PARK_INFO_PARAM)
        public static final int CTRLTYPE_CTRL_VTP_DISCONNECT = CTRLTYPE_CTRL_ECK_SET_PARK_INFO+1; //挂断视频电话(对应结构体NET_CTRL_VTP_DISCONNECT)
        public static final int CTRLTYPE_CTRL_UPDATE_FILES = CTRLTYPE_CTRL_VTP_DISCONNECT+1; //远程投放多媒体文件更新(对应结构体NET_CTRL_UPDATE_FILES)
        public static final int CTRLTYPE_CTRL_MATRIX_SAVE_SWITCH = CTRLTYPE_CTRL_UPDATE_FILES+1; //保存上下位矩阵输出关系(对应结构体NET_CTRL_MATRIX_SAVE_SWITCH)
        public static final int CTRLTYPE_CTRL_MATRIX_RESTORE_SWITCH = CTRLTYPE_CTRL_MATRIX_SAVE_SWITCH+1; //恢复上下位矩阵输出关系(对应结构体NET_CTRL_MATRIX_RESTORE_SWITCH)
        public static final int CTRLTYPE_CTRL_VTP_DIVERTACK = CTRLTYPE_CTRL_MATRIX_RESTORE_SWITCH+1; //呼叫转发响应(对应结构体NET_CTRL_VTP_DIVERTACK)
        public static final int CTRLTYPE_CTRL_RAINBRUSH_MOVEONCE = CTRLTYPE_CTRL_VTP_DIVERTACK+1; //雨刷来回刷一次，雨刷模式配置为手动模式时有效(对应结构体NET_CTRL_RAINBRUSH_MOVEONCE)
        public static final int CTRLTYPE_CTRL_RAINBRUSH_MOVECONTINUOUSLY = CTRLTYPE_CTRL_RAINBRUSH_MOVEONCE+1; //雨刷来回循环刷，雨刷模式配置为手动模式时有效(对应结构体NET_CTRL_RAINBRUSH_MOVECONTINUOUSLY)
        public static final int CTRLTYPE_CTRL_RAINBRUSH_STOPMOVE = CTRLTYPE_CTRL_RAINBRUSH_MOVECONTINUOUSLY+1; //雨刷停止刷，雨刷模式配置为手动模式时有效(对应结构体NET_CTRL_RAINBRUSH_STOPMOVE)
        public static final int CTRLTYPE_CTRL_ALARM_ACK = CTRLTYPE_CTRL_RAINBRUSH_STOPMOVE+1; //报警事件确认(对应结构体NET_CTRL_ALARM_ACK)
                                                                                              // NET_CTRL_ALARM_ACK 该操作切勿在报警回调接口中调用
        public static final int CTRLTYPE_CTRL_RECORDSET_IMPORT = CTRLTYPE_CTRL_ALARM_ACK + 1; // 批量导入记录集信息(对应 NET_CTRL_RECORDSET_PARAM )
        public static final int CTRLTYPE_CTRL_DELIVERY_FILE = CTRLTYPE_CTRL_RECORDSET_IMPORT + 1; // 向视频输出口投放视频和图片文件, 楼宇对讲使用，同一时间投放(对应 NET_CTRL_DELIVERY_FILE )
                                                                                    // 以下命令只在 CLIENT_ControlDeviceEx 上有效
        public static final int CTRLTYPE_CTRL_THERMO_GRAPHY_ENSHUTTER = 0x10000;//设置热成像快门启用/禁用,pInBuf= NET_IN_THERMO_EN_SHUTTER*, pOutBuf= NET_OUT_THERMO_EN_SHUTTER * 
        public static final int CTRLTYPE_CTRL_RADIOMETRY_SETOSDMARK = CTRLTYPE_CTRL_THERMO_GRAPHY_ENSHUTTER+1; //设置测温项的osd为高亮,pInBuf=NET_IN_RADIOMETRY_SETOSDMARK*,pOutBuf= NET_OUT_RADIOMETRY_SETOSDMARK * 
        public static final int CTRLTYPE_CTRL_AUDIO_REC_START_NAME = CTRLTYPE_CTRL_RADIOMETRY_SETOSDMARK+1; //开启音频录音并得到录音名,pInBuf = NET_IN_AUDIO_REC_MNG_NAME *, pOutBuf = NET_OUT_AUDIO_REC_MNG_NAME *
        public static final int CTRLTYPE_CTRL_AUDIO_REC_STOP_NAME = CTRLTYPE_CTRL_AUDIO_REC_START_NAME+1; //关闭音频录音并返回文件名称,pInBuf = NET_IN_AUDIO_REC_MNG_NAME *, pOutBuf = NET_OUT_AUDIO_REC_MNG_NAME *
        public static final int CTRLTYPE_CTRL_SNAP_MNG_SNAP_SHOT = CTRLTYPE_CTRL_AUDIO_REC_STOP_NAME+1; //即时抓图(又名手动抓图),pInBuf  =NET_IN_SNAP_MNG_SHOT *, pOutBuf = NET_OUT_SNAP_MNG_SHOT *
        public static final int CTRLTYPE_CTRL_LOG_STOP = CTRLTYPE_CTRL_SNAP_MNG_SNAP_SHOT+1; //强制同步缓存数据到数据库并关闭数据库,pInBuf = NET_IN_LOG_MNG_CTRL *, pOutBuf = NET_OUT_LOG_MNG_CTRL *
        public static final int CTRLTYPE_CTRL_LOG_RESUME = CTRLTYPE_CTRL_LOG_STOP+1; //恢复数据库,pInBuf = NET_IN_LOG_MNG_CTRL *, pOutBuf = NET_OUT_LOG_MNG_CTRL *
    }

    // 视频压缩格式
    public static class CFG_VIDEO_COMPRESSION extends Structure
    {
        public static final int VIDEO_FORMAT_MPEG4 = 0; //MPEG4
        public static final int VIDEO_FORMAT_MS_MPEG4 = VIDEO_FORMAT_MPEG4+1; //MS-MPEG4
        public static final int VIDEO_FORMAT_MPEG2 = VIDEO_FORMAT_MS_MPEG4+1; //MPEG2
        public static final int VIDEO_FORMAT_MPEG1 = VIDEO_FORMAT_MPEG2+1; //MPEG1
        public static final int VIDEO_FORMAT_H263 = VIDEO_FORMAT_MPEG1+1; //H.263
        public static final int VIDEO_FORMAT_MJPG = VIDEO_FORMAT_H263+1; //MJPG
        public static final int VIDEO_FORMAT_FCC_MPEG4 = VIDEO_FORMAT_MJPG+1; //FCC-MPEG4
        public static final int VIDEO_FORMAT_H264 = VIDEO_FORMAT_FCC_MPEG4+1; //H.264
        public static final int VIDEO_FORMAT_H265 = VIDEO_FORMAT_H264+1; //H.265
    }

    // 码流控制模式
    public static class CFG_BITRATE_CONTROL extends Structure
    {
        public static final int BITRATE_CBR = 0;              //固定码流
        public static final int BITRATE_VBR = BITRATE_CBR+1; //可变码流
    }

    // H264 编码级别
    public static class CFG_H264_PROFILE_RANK extends Structure
    {
        public static final int PROFILE_BASELINE = 1;//提供I/P帧，仅支持progressive(逐行扫描)和CAVLC
        public static final int PROFILE_MAIN = PROFILE_BASELINE+1; //提供I/P/B帧，支持progressiv和interlaced，提供CAVLC或CABAC
        public static final int PROFILE_EXTENDED = PROFILE_MAIN+1; //提供I/P/B/SP/SI帧，仅支持progressive(逐行扫描)和CAVLC
        public static final int PROFILE_HIGH = PROFILE_EXTENDED+1; //即FRExt，Main_Profile基础上新增：8x8intraprediction(8x8帧内预测), custom 
                                                                   // quant(自定义量化), lossless video coding(无损视频编码), 更多的yuv格式
    }

    // 画质
    public static class CFG_IMAGE_QUALITY extends Structure
    {
        public static final int IMAGE_QUALITY_Q10 = 1;//图像质量10%
        public static final int IMAGE_QUALITY_Q30 = IMAGE_QUALITY_Q10+1; //图像质量30%
        public static final int IMAGE_QUALITY_Q50 = IMAGE_QUALITY_Q30+1; //图像质量50%
        public static final int IMAGE_QUALITY_Q60 = IMAGE_QUALITY_Q50+1; //图像质量60%
        public static final int IMAGE_QUALITY_Q80 = IMAGE_QUALITY_Q60+1; //图像质量80%
        public static final int IMAGE_QUALITY_Q100 = IMAGE_QUALITY_Q80+1; //图像质量100%
    }

    // 视频格式
    public static class CFG_VIDEO_FORMAT extends Structure
    {
        // 能力
        public byte abCompression;// 类型为bool, 取值0或1
        public byte abWidth;// 类型为bool, 取值0或1
        public byte abHeight;// 类型为bool, 取值0或1
        public byte abBitRateControl;// 类型为bool, 取值0或1
        public byte abBitRate;// 类型为bool, 取值0或1
        public byte abFrameRate;// 类型为bool, 取值0或1
        public byte abIFrameInterval;// 类型为bool, 取值0或1
        public byte abImageQuality;// 类型为bool, 取值0或1
        public byte abFrameType;// 类型为bool, 取值0或1
        public byte abProfile;// 类型为bool, 取值0或1
        // 信息
        public int emCompression;//视频压缩格式, 取值为CFG_VIDEO_COMPRESSION中的值
        public int nWidth;//视频宽度
        public int nHeight;//视频高度
        public int emBitRateControl;//码流控制模式, 取值为CFG_BITRATE_CONTROL中的值
        public int nBitRate;//视频码流(kbps)
        public float nFrameRate;//视频帧率
        public int nIFrameInterval;//I帧间隔(1-100)，比如50表示每49个B帧或P帧，设置一个I帧。
        public int emImageQuality;//图像质量, 取值为CFG_IMAGE_QUALITY中的值
        public int nFrameType;//打包模式，0－DHAV，1－"PS"
        public int emProfile;//H.264编码级别, 取值为CFG_H264_PROFILE_RANK中的值
    }

    // 音频编码模式
    public static class CFG_AUDIO_FORMAT extends Structure
    {
        public static final int  AUDIO_FORMAT_G711A = 0; //G711a
        public static final int  AUDIO_FORMAT_PCM = AUDIO_FORMAT_G711A+1; //PCM
        public static final int  AUDIO_FORMAT_G711U = AUDIO_FORMAT_PCM+1; //G711u
        public static final int  AUDIO_FORMAT_AMR = AUDIO_FORMAT_G711U+1; //AMR
        public static final int  AUDIO_FORMAT_AAC = AUDIO_FORMAT_AMR+1; //AAC
    }

    // 音频格式
    public static class CFG_AUDIO_ENCODE_FORMAT extends Structure
    {
        // 能力
        public byte abCompression;// 类型为bool, 取值0或1
        public byte abDepth;// 类型为bool, 取值0或1
        public byte abFrequency;// 类型为bool, 取值0或1
        public byte abMode;// 类型为bool, 取值0或1
        public byte abFrameType;// 类型为bool, 取值0或1
        public byte abPacketPeriod;// 类型为bool, 取值0或1
        // 信息
        public int emCompression;//音频压缩模式，取值为CFG_AUDIO_FORMAT中的值
        public int nDepth;//音频采样深度
        public int nFrequency;//音频采样频率
        public int nMode;//音频编码模式
        public int nFrameType;//音频打包模式,0-DHAV,1-PS
        public int nPacketPeriod;//音频打包周期,ms
    }

    // 视频编码参数
    public static class CFG_VIDEOENC_OPT extends Structure
    {
        // 能力
        public byte abVideoEnable;// 类型为bool, 取值0或1
        public byte abAudioEnable;// 类型为bool, 取值0或1
        public byte abSnapEnable;// 类型为bool, 取值0或1
        public byte abAudioAdd;//音频叠加能力, 类型为bool, 取值0或1
        public byte abAudioFormat;// 类型为bool, 取值0或1
        // 信息
        public int bVideoEnable;//视频使能, 类型为BOOL, 取值0或者1
        public CFG_VIDEO_FORMAT stuVideoFormat;//视频格式
        public int bAudioEnable;//音频使能, 类型为BOOL, 取值0或者1
        public int bSnapEnable;//定时抓图使能, 类型为BOOL, 取值0或者1
        public int bAudioAddEnable;//音频叠加使能, 类型为BOOL, 取值0或者1
        public CFG_AUDIO_ENCODE_FORMAT stuAudioFormat;//音频格式
    }

    // 遮挡信息
    public static class CFG_COVER_INFO extends Structure
    {
        // 能力
        public byte abBlockType;// 类型为bool, 取值0或1
        public byte abEncodeBlend;// 类型为bool, 取值0或1
        public byte abPreviewBlend;// 类型为bool, 取值0或1
        // 信息
        public CFG_RECT stuRect = new CFG_RECT();//覆盖的区域坐标
        public CFG_RGBA stuColor = new CFG_RGBA();//覆盖的颜色
        public int nBlockType;//覆盖方式；0－黑块，1－马赛克
        public int nEncodeBlend;//编码级遮挡；1－生效，0－不生效
        public int nPreviewBlend;//预览遮挡；1－生效，0－不生效
    }

    // 多区域遮挡配置
    public static class CFG_VIDEO_COVER extends Structure
    {
        public int nTotalBlocks;//支持的遮挡块数
        public int nCurBlocks;//已设置的块数
        public CFG_COVER_INFO[] stuCoverBlock = (CFG_COVER_INFO[])new CFG_COVER_INFO().toArray(MAX_VIDEO_COVER_NUM);// 覆盖的区域    
    }

    // OSD信息
    public static class CFG_OSD_INFO extends Structure
    {
        // 能力
        public byte abShowEnable;// 类型为bool, 取值0或1
        // 信息
        public CFG_RGBA stuFrontColor = new CFG_RGBA();//前景颜色
        public CFG_RGBA stuBackColor = new CFG_RGBA();//背景颜色
        public CFG_RECT stuRect = new CFG_RECT();//矩形区域
        public int bShowEnable;//显示使能, 类型为BOOL, 取值0或者1
    }

    // 画面颜色属性
    public static class CFG_COLOR_INFO extends Structure
    {
        public int nBrightness;//亮度(0-100)
        public int nContrast;//对比度(0-100)
        public int nSaturation;//饱和度(0-100)
        public int nHue;//色度(0-100)
        public int nGain;//增益(0-100)
        public int bGainEn;//增益使能, 类型为BOOL, 取值0或者1
    }

    // 图像通道属性信息
    public static class CFG_ENCODE_INFO extends Structure
    {
        public int nChannelID;//通道号(0开始),获取时，该字段有效；设置时，该字段无效
        public byte[] szChnName = new byte[MAX_CHANNELNAME_LEN];//无效字段
        public CFG_VIDEOENC_OPT[] stuMainStream = (CFG_VIDEOENC_OPT[])new CFG_VIDEOENC_OPT().toArray(MAX_VIDEOSTREAM_NUM);    // 主码流，0－普通录像，1-动检录像，2－报警录像
    	public int nValidCountMainStream;                      // 主码流数组中有效的个数
        public CFG_VIDEOENC_OPT[] stuExtraStream = (CFG_VIDEOENC_OPT[])new CFG_VIDEOENC_OPT().toArray(MAX_VIDEOSTREAM_NUM);    // 辅码流，0－辅码流1，1－辅码流2，2－辅码流3
    	public int nValidCountExtraStream;             		   // 辅码流数组中有效的个数
        public CFG_VIDEOENC_OPT[] stuSnapFormat = (CFG_VIDEOENC_OPT[])new CFG_VIDEOENC_OPT().toArray(MAX_VIDEOSTREAM_NUM);    // 抓图，0－普通抓图，1－动检抓图，2－报警抓图
        public int nValidCountSnapFormat;              		   // 抓图数组中有效的个数
        public int dwCoverAbilityMask;//无效字段
        public int dwCoverEnableMask;//无效字段
        public CFG_VIDEO_COVER stuVideoCover;//无效字段
        public CFG_OSD_INFO stuChnTitle;//无效字段
        public CFG_OSD_INFO stuTimeTitle;//无效字段
        public CFG_COLOR_INFO stuVideoColor;//无效字段
        public int emAudioFormat;//无效字段, 取值为CFG_AUDIO_FORMAT中的值
        public int nProtocolVer;//协议版本号,只读,获取时，该字段有效；设置时，该字段无效
    }

    // 设备软件版本信息,高16位表示主版本号,低16位表示次版本号
    public static class NET_VERSION_INFO extends Structure
    {
        public int dwSoftwareVersion;
        public int dwSoftwareBuildDate;
        public int dwDspSoftwareVersion;
        public int dwDspSoftwareBuildDate;
        public int dwPanelVersion;
        public int dwPanelSoftwareBuildDate;
        public int dwHardwareVersion;
        public int dwHardwareDate;
        public int dwWebVersion;
        public int dwWebBuildDate;
    }
    
    // 设备软件版本信息,对应CLIENT_QueryDevState接口
    public static class NETDEV_VERSION_INFO extends Structure
    {
        public byte[]       szDevSerialNo = new byte[NET_DEV_SERIALNO_LEN];         // 序列号
        public int          byDevType;                                 						  // 设备类型,见枚举  NET_DEVICE_TYPE
        public byte[]       szDevType = new byte[NET_DEV_TYPE_LEN];                 // 设备详细型号,字符串格式,可能为空
        public int          nProtocalVer;                              						  // 协议版本号
        public byte[]       szSoftWareVersion = new byte[NET_MAX_URL_LEN];
        public int          dwSoftwareBuildDate;
        public byte[]       szPeripheralSoftwareVersion = new byte[NET_MAX_URL_LEN];// 从片版本信息,字符串格式,可能为空
        public int          dwPeripheralSoftwareBuildDate;
        public byte[]       szGeographySoftwareVersion = new byte[NET_MAX_URL_LEN]; // 地理信息定位芯片版本信息,字符串格式,可能为空
        public int          dwGeographySoftwareBuildDate;
        public byte[]       szHardwareVersion = new byte[NET_MAX_URL_LEN];
        public int          dwHardwareDate;
        public byte[]       szWebVersion = new byte[NET_MAX_URL_LEN];
        public int          dwWebBuildDate;
        public byte[]       reserved = new byte[256];
    }
    
    // 设备类型
    public static class NET_DEVICE_TYPE extends Structure
    {
        public static final int NET_PRODUCT_NONE = 0;
        public static final int NET_DVR_NONREALTIME_MACE = 1;     // 非实时MACE
        public static final int NET_DVR_NONREALTIME	= 2;          // 非实时
        public static final int NET_NVS_MPEG1 = 3;                // 网络视频服务器
        public static final int NET_DVR_MPEG1_2 = 4;              // MPEG1二路录像机
        public static final int NET_DVR_MPEG1_8 = 5;              // MPEG1八路录像机
        public static final int NET_DVR_MPEG4_8 = 6;              // MPEG4八路录像机
        public static final int NET_DVR_MPEG4_16 = 7;             // MPEG4十六路录像机
        public static final int NET_DVR_MPEG4_SX2 = 8;            // LB系列录像机
        public static final int NET_DVR_MEPG4_ST2 = 9;            // GB系列录像机
        public static final int NET_DVR_MEPG4_SH2 = 10;            // HB系列录像机               10
        public static final int NET_DVR_MPEG4_GBE = 11;            // GBE系列录像机
        public static final int NET_DVR_MPEG4_NVSII = 12;          // II代网络视频服务器
        public static final int NET_DVR_STD_NEW = 13;              // 新标准配置协议
        public static final int NET_DVR_DDNS = 14;                 // DDNS服务器
        public static final int NET_DVR_ATM = 15;                  // ATM机
        public static final int NET_NB_SERIAL = 16;                // 二代非实时NB系列机器
        public static final int NET_LN_SERIAL = 17;                // LN系列产品
        public static final int NET_BAV_SERIAL = 18;               // BAV系列产品
        public static final int NET_SDIP_SERIAL = 19;              // SDIP系列产品
        public static final int NET_IPC_SERIAL = 20;               // IPC系列产品                20
        public static final int NET_NVS_B = 21;                    // NVS B系列
        public static final int NET_NVS_C = 22;                    // NVS H系列
        public static final int NET_NVS_S = 23;                    // NVS S系列
        public static final int NET_NVS_E = 24;                    // NVS E系列
        public static final int NET_DVR_NEW_PROTOCOL = 25;         // 从QueryDevState中查询设备类型,以字符串格式
        public static final int NET_NVD_SERIAL = 26;               // 解码器
        public static final int NET_DVR_N5 = 27;                   // N5
        public static final int NET_DVR_MIX_DVR = 28;              // 混合DVR
        public static final int NET_SVR_SERIAL = 29;               // SVR系列
        public static final int NET_SVR_BS = 30;                   // SVR-BS                     30
        public static final int NET_NVR_SERIAL = 31;               // NVR系列
        public static final int NET_DVR_N51 = 32;                  // N51
        public static final int NET_ITSE_SERIAL = 33;              // ITSE 智能分析盒
        public static final int NET_ITC_SERIAL = 34;               // 智能交通像机设备
        public static final int NET_HWS_SERIAL = 35;               // 雷达测速仪HWS
        public static final int NET_PVR_SERIAL = 36;               // 便携式音视频录像机
        public static final int NET_IVS_SERIAL = 37;               // IVS（智能视频服务器系列）
        public static final int NET_IVS_B = 38;                    // 通用智能视频侦测服务器
        public static final int NET_IVS_F = 39;                    // 人脸识别服务器
        public static final int NET_IVS_V = 40;                    // 视频质量诊断服务器         40
        public static final int NET_MATRIX_SERIAL = 41;            // 矩阵
        public static final int NET_DVR_N52 = 42;                  // N52
        public static final int NET_DVR_N56 = 43;                  // N56
        public static final int NET_ESS_SERIAL = 44;               // ESS
        public static final int NET_IVS_PC = 45;                   // 人数统计服务器
        public static final int NET_PC_NVR = 46;                   // pc-nvr
        public static final int NET_DSCON = 47;                    // 大屏控制器
        public static final int NET_EVS = 48;                      // 网络视频存储服务器
        public static final int NET_EIVS = 49;                     // 嵌入式智能分析视频系统
        public static final int NET_DVR_N6 = 50;                   // DVR-N6       50
        public static final int NET_UDS = 51;                      // 万能解码器
        public static final int NET_AF6016 = 52;                   // 银行报警主机
        public static final int NET_AS5008 = 53;                   // 视频网络报警主机
        public static final int NET_AH2008 = 54;                   // 网络报警主机
        public static final int NET_A_SERIAL = 55;                 // 报警主机系列
        public static final int NET_BSC_SERIAL = 56;               // 门禁系列产品
        public static final int NET_NVS_SERIAL = 57;               // NVS系列产品
        public static final int NET_VTO_SERIAL = 58;              // VTO系列产品
        public static final int NET_VTNC_SERIAL = 59;              // VTNC系列产品
        public static final int NET_TPC_SERIAL = 60;               // TPC系列产品, 即热成像设备  60
        public static final int NET_ASM_SERIAL = 61;               // 无线中继设备
        public static final int NET_VTS_SERIAL = 62;               // 管理机
    }

    // DSP能力描述,对应CLIENT_GetDevConfig接口
    public static class NET_DSP_ENCODECAP extends Structure
    {
        public int dwVideoStandardMask;//视频制式掩码,按位表示设备能够支持的视频制式
        public int dwImageSizeMask;//分辨率掩码,按位表示设备能够支持的分辨率设置
        public int dwEncodeModeMask;//编码模式掩码,按位表示设备能够支持的编码模式设置
        public int dwStreamCap;    // 按位表示设备支持的多媒体功能,
                                // 第一位表示支持主码流
                                // 第二位表示支持辅码流1
                                // 第三位表示支持辅码流2
                                // 第五位表示支持jpg抓图
        public int[] dwImageSizeMask_Assi = new int[8];//表示主码流为各分辨率时,支持的辅码流分辨率掩码。
        public int dwMaxEncodePower;//DSP支持的最高编码能力
        public short wMaxSupportChannel;//每块DSP支持最多输入视频通道数
        public short wChannelMaxSetSync;//DSP每通道的最大编码设置是否同步；0：不同步,1：同步
    }

    // 系统信息
    public static class NET_DEV_SYSTEM_ATTR_CFG extends Structure
    {
        public int dwSize;
        /* 下面是设备的只读部分 */
        public NET_VERSION_INFO stVersion;
        public NET_DSP_ENCODECAP stDspEncodeCap;//DSP能力描述
        public byte[] szDevSerialNo = new byte[NET_DEV_SERIALNO_LEN];//序列号
        public byte byDevType;//设备类型,见枚举NET_DEVICE_TYPE
        public byte[] szDevType = new byte[NET_DEV_TYPE_LEN];//设备详细型号,字符串格式,可能为空
        public byte byVideoCaptureNum;//视频口数量
        public byte byAudioCaptureNum;//音频口数量
        public byte byTalkInChanNum;//对讲输入接口数量
        public byte byTalkOutChanNum;//对讲输出接口数量
        public byte byDecodeChanNum;//NSP
        public byte byAlarmInNum;//报警输入口数
        public byte byAlarmOutNum;//报警输出口数
        public byte byNetIONum;//网络口数
        public byte byUsbIONum;//USB口数量
        public byte byIdeIONum;//IDE数量
        public byte byComIONum;//串口数量
        public byte byLPTIONum;//并口数量
        public byte byVgaIONum;//NSP
        public byte byIdeControlNum;//NSP
        public byte byIdeControlType;//NSP
        public byte byCapability;//NSP,扩展描述
        public byte byMatrixOutNum;//视频矩阵输出口数
        /* 下面是设备的可写部分 */
        public byte byOverWrite;//硬盘满处理方式(覆盖、停止)
        public byte byRecordLen;//录象打包长度
        public byte byDSTEnable;//是否实行夏令时1-实行0-不实行
        public short wDevNo;//设备编号,用于遥控
        public byte byVideoStandard;//视频制式:0-PAL,1-NTSC
        public byte byDateFormat;//日期格式
        public byte byDateSprtr;//日期分割符(0：".",1："-",2："/")
        public byte byTimeFmt;//时间格式(0-24小时,1－12小时)
        public byte byLanguage;//枚举值详见NET_LANGUAGE_TYPE

        public NET_DEV_SYSTEM_ATTR_CFG()
        {
            this.dwSize = this.size();
        }
    }

    // 入侵方向
    public static class EM_MSG_OBJ_PERSON_DIRECTION extends Structure
    {
        public static final int EM_MSG_OBJ_PERSON_DIRECTION_UNKOWN = 0; //未知方向
        public static final int EM_MSG_OBJ_PERSON_DIRECTION_LEFT_TO_RIGHT = EM_MSG_OBJ_PERSON_DIRECTION_UNKOWN+1; //从左向右
        public static final int EM_MSG_OBJ_PERSON_DIRECTION_RIGHT_TO_LEFT = EM_MSG_OBJ_PERSON_DIRECTION_LEFT_TO_RIGHT+1; //从右向左
    }

    // 视频分析物体信息扩展结构体
    public static class NET_MSG_OBJECT_EX extends Structure
    {
        public int dwSize;
        public int nObjectID;//物体ID,每个ID表示一个唯一的物体
        public byte[] szObjectType = new byte[128];//物体类型
        public int nConfidence;//置信度(0~255),值越大表示置信度越高
        public int nAction;//物体动作:1:Appear2:Move3:Stay 4:Remove 5:Disappear 6:Split 7:Merge 8:Rename
        public DH_RECT BoundingBox;//包围盒
        public NET_POINT Center;//物体型心
        public int nPolygonNum;//多边形顶点个数
        public NET_POINT[] Contour = (NET_POINT[])new NET_POINT().toArray(NET_MAX_POLYGON_NUM);// 较精确的轮廓多边形
        public int rgbaMainColor;//表示车牌、车身等物体主要颜色；按字节表示,分别为红、绿、蓝和透明度,例如:RGB值为(0,255,0),透明度为0时,其值为0x00ff0000.
        public byte[] szText = new byte[128];//同NET_MSG_OBJECT相应字段
        public byte[] szObjectSubType = new byte[64];//物体子类别,根据不同的物体类型,可以取以下子类型：
        // 同NET_MSG_OBJECT相应字段
        public byte[] byReserved1 = new byte[3];
        public byte bPicEnble;//是否有物体对应图片文件信息, 类型为bool, 取值0或1
        public NET_PIC_INFO stPicInfo;//物体对应图片信息
        public byte bShotFrame;//是否是抓拍张的识别结果, 类型为bool, 取值0或1
        public byte bColor;//物体颜色(rgbaMainColor)是否可用, 类型为bool, 取值0或1
        public byte bLowerBodyColor;//下半身颜色(rgbaLowerBodyColor)是否可用
        public byte byTimeType;//时间表示类型,详见EM_TIME_TYPE说明
        public NET_TIME_EX stuCurrentTime;//针对视频浓缩,当前时间戳（物体抓拍或识别时,会将此识别智能帧附在一个视频帧或jpeg图片中,此帧所在原始视频中的出现时间）
        public NET_TIME_EX stuStartTime;//开始时间戳（物体开始出现时）
        public NET_TIME_EX stuEndTime;//结束时间戳（物体最后出现时）
        public DH_RECT stuOriginalBoundingBox;//包围盒(绝对坐标)
        public DH_RECT stuSignBoundingBox;//车标坐标包围盒
        public int dwCurrentSequence;//当前帧序号（抓下这个物体时的帧）
        public int dwBeginSequence;//开始帧序号（物体开始出现时的帧序号）
        public int dwEndSequence;//结束帧序号（物体消逝时的帧序号）
        public long nBeginFileOffset;//开始时文件偏移,单位:字节（物体开始出现时,视频帧在原始视频文件中相对于文件起始处的偏移）
        public long nEndFileOffset;//结束时文件偏移,单位:字节（物体消逝时,视频帧在原始视频文件中相对于文件起始处的偏移）
        public byte[] byColorSimilar = new byte[EM_COLOR_TYPE.NET_COLOR_TYPE_MAX];//物体颜色相似度,取值范围：0-100,数组下标值代表某种颜色,详见EM_COLOR_TYPE
        public byte[] byUpperBodyColorSimilar = new byte[EM_COLOR_TYPE.NET_COLOR_TYPE_MAX];//上半身物体颜色相似度(物体类型为人时有效)
        public byte[] byLowerBodyColorSimilar = new byte[EM_COLOR_TYPE.NET_COLOR_TYPE_MAX];//下半身物体颜色相似度(物体类型为人时有效)
        public int nRelativeID;//相关物体ID
        public byte[] szSubText = new byte[20];//"ObjectType"为"Vehicle"或者"Logo"时,表示车标下的某一车系,比如奥迪A6L,由于车系较多,SDK实现时透传此字段,设备如实填写。
        public int nPersonStature;//入侵人员身高,单位cm
        public int emPersonDirection;//人员入侵方向, 取值为EM_MSG_OBJ_PERSON_DIRECTION中的值
        public int rgbaLowerBodyColor;//使用方法同rgbaMainColor,物体类型为人时有效

        public NET_MSG_OBJECT_EX()
        {
            this.dwSize = this.size();
            
        	if(INetSDK.getOsName().equals("win")) {
                // 强制采用最大四字节对其
                setAlignType(ALIGN_GNUC);
        	}
        }
    }
    
    // 视频分析物体信息扩展结构体,扩展版本2
    public static class NET_MSG_OBJECT_EX2 extends Structure
    {
        public int dwSize;
        public int nObjectID;//物体ID,每个ID表示一个唯一的物体
        public byte[] szObjectType = new byte[128];//物体类型
        public int nConfidence;//置信度(0~255),值越大表示置信度越高
        public int nAction;//物体动作:1:Appear2:Move3:Stay 4:Remove 5:Disappear 6:Split 7:Merge 8:Rename
        public DH_RECT BoundingBox;//包围盒
        public NET_POINT Center;//物体型心
        public int nPolygonNum;//多边形顶点个数
        public NET_POINT[] Contour = (NET_POINT[])new NET_POINT().toArray(NET_MAX_POLYGON_NUM);//较精确的轮廓多边形
        public int rgbaMainColor;//表示车牌、车身等物体主要颜色；按字节表示,分别为红、绿、蓝和透明度,例如:RGB值为(0,255,0),透明度为0时,其值为0x00ff0000.
        public byte[] szText = new byte[128];//同NET_MSG_OBJECT相应字段
        public byte[] szObjectSubType = new byte[64];//物体子类别,根据不同的物体类型,可以取以下子类型：
        // 同NET_MSG_OBJECT相应字段
        public byte[] byReserved1 = new byte[3];
        public byte bPicEnble;//是否有物体对应图片文件信息, 类型为bool, 取值0或者1
        public NET_PIC_INFO stPicInfo;//物体对应图片信息
        public byte bShotFrame;//是否是抓拍张的识别结果, 类型为bool, 取值0或者1
        public byte bColor;//物体颜色(rgbaMainColor)是否可用, 类型为bool, 取值0或者1
        public byte bLowerBodyColor;//下半身颜色(rgbaLowerBodyColor)是否可用
        public byte byTimeType;//时间表示类型,详见EM_TIME_TYPE说明
        public NET_TIME_EX stuCurrentTime;//针对视频浓缩,当前时间戳（物体抓拍或识别时,会将此识别智能帧附在一个视频帧或jpeg图片中,此帧所在原始视频中的出现时间）
        public NET_TIME_EX stuStartTime;//开始时间戳（物体开始出现时）
        public NET_TIME_EX stuEndTime;//结束时间戳（物体最后出现时）
        public DH_RECT stuOriginalBoundingBox;//包围盒(绝对坐标)
        public DH_RECT stuSignBoundingBox;//车标坐标包围盒
        public int dwCurrentSequence;//当前帧序号（抓下这个物体时的帧）
        public int dwBeginSequence;//开始帧序号（物体开始出现时的帧序号）
        public int dwEndSequence;//结束帧序号（物体消逝时的帧序号）
        public long nBeginFileOffset;//开始时文件偏移,单位:字节（物体开始出现时,视频帧在原始视频文件中相对于文件起始处的偏移）
        public long nEndFileOffset;//结束时文件偏移,单位:字节（物体消逝时,视频帧在原始视频文件中相对于文件起始处的偏移）
        public byte[] byColorSimilar = new byte[EM_COLOR_TYPE.NET_COLOR_TYPE_MAX];//物体颜色相似度,取值范围：0-100,数组下标值代表某种颜色,详见EM_COLOR_TYPE
        public byte[] byUpperBodyColorSimilar = new byte[EM_COLOR_TYPE.NET_COLOR_TYPE_MAX];//上半身物体颜色相似度(物体类型为人时有效)
        public byte[] byLowerBodyColorSimilar = new byte[EM_COLOR_TYPE.NET_COLOR_TYPE_MAX];//下半身物体颜色相似度(物体类型为人时有效)
        public int nRelativeID;//相关物体ID
        public byte[] szSubText = new byte[20];//"ObjectType"为"Vehicle"或者"Logo"时,表示车标下的某一车系,比如奥迪A6L,由于车系较多,SDK实现时透传此字段,设备如实填写。
        public int nPersonStature;//入侵人员身高,单位cm
        public int emPersonDirection;//人员入侵方向, 取值为EM_MSG_OBJ_PERSON_DIRECTION中的值
        public int rgbaLowerBodyColor;//使用方法同rgbaMainColor,物体类型为人时有效
        //视频浓缩额外信息
        public int nSynopsisSpeed;//浓缩速度域值,共分1~10共十个档位,5表示浓缩后只保留5以上速度的物体。是个相对单位
        // 为0时,该字段无效
        public int nSynopsisSize;//浓缩尺寸域值,共分1~10共十个档位,3表示浓缩后只保留3以上大小的物体。是个相对单位
        // 为0时,该字段无效
        public int bEnableDirection;//为True时,对物体运动方向做过滤, 类型为BOOL, 取值0或者1
        // 为False时,不对物体运动方向做过滤,
        public NET_POINT stuSynopsisStartLocation;//浓缩运动方向,起始坐标点,点的坐标归一化到[0,8192)区间,bEnableDirection为True时有效
        public NET_POINT stuSynopsisEndLocation;//浓缩运动方向,终止坐标点,点的坐标归一化到[0,8192)区间,bEnableDirection为True时有效
        public byte[] byReserved = new byte[2048];//扩展字节
        
        public NET_MSG_OBJECT_EX2()
        {
            this.dwSize = this.size();
            
        	if(INetSDK.getOsName().equals("win")) {
                // 强制采用最大四字节对其
                setAlignType(ALIGN_GNUC);
        	}
        }
    }
    
    // 设备协议类型
    public static class NET_DEVICE_PROTOCOL extends Structure
    {
        public static final int NET_PROTOCOL_PRIVATE2 = 0; //私有2代协议
        public static final int NET_PROTOCOL_PRIVATE3 = NET_PROTOCOL_PRIVATE2+1; //私有3代协议
        public static final int NET_PROTOCOL_ONVIF = NET_PROTOCOL_PRIVATE3+1; //Onvif
        public static final int NET_PROTOCOL_VNC = NET_PROTOCOL_ONVIF+1; //虚拟网络计算机
        public static final int NET_PROTOCOL_TS = NET_PROTOCOL_VNC+1; //标准TS
        public static final int NET_PROTOCOL_PRIVATE = 100;//私有协议
        public static final int NET_PROTOCOL_AEBELL = NET_PROTOCOL_PRIVATE+1; //美电贝尔
        public static final int NET_PROTOCOL_PANASONIC = NET_PROTOCOL_AEBELL+1; //松下
        public static final int NET_PROTOCOL_SONY = NET_PROTOCOL_PANASONIC+1; //索尼
        public static final int NET_PROTOCOL_DYNACOLOR = NET_PROTOCOL_SONY+1; //Dynacolor
        public static final int NET_PROTOCOL_TCWS = NET_PROTOCOL_DYNACOLOR+1; //天城威视
        public static final int NET_PROTOCOL_SAMSUNG = NET_PROTOCOL_TCWS+1; //三星
        public static final int NET_PROTOCOL_YOKO = NET_PROTOCOL_SAMSUNG+1; //YOKO
        public static final int NET_PROTOCOL_AXIS = NET_PROTOCOL_YOKO+1; //安讯视
        public static final int NET_PROTOCOL_SANYO = NET_PROTOCOL_AXIS+1; //三洋
        public static final int NET_PROTOCOL_BOSH = NET_PROTOCOL_SANYO+1; //Bosch
        public static final int NET_PROTOCOL_PECLO = NET_PROTOCOL_BOSH+1; //Peclo
        public static final int NET_PROTOCOL_PROVIDEO = NET_PROTOCOL_PECLO+1; //Provideo
        public static final int NET_PROTOCOL_ACTI = NET_PROTOCOL_PROVIDEO+1; //ACTi
        public static final int NET_PROTOCOL_VIVOTEK = NET_PROTOCOL_ACTI+1; //Vivotek
        public static final int NET_PROTOCOL_ARECONT = NET_PROTOCOL_VIVOTEK+1; //Arecont
        public static final int NET_PROTOCOL_PRIVATEEH = NET_PROTOCOL_ARECONT+1; //PrivateEH
        public static final int NET_PROTOCOL_IMATEK = NET_PROTOCOL_PRIVATEEH+1; //IMatek
        public static final int NET_PROTOCOL_SHANY = NET_PROTOCOL_IMATEK+1; //Shany
        public static final int NET_PROTOCOL_VIDEOTREC = NET_PROTOCOL_SHANY+1; //动力盈科
        public static final int NET_PROTOCOL_URA = NET_PROTOCOL_VIDEOTREC+1; //Ura
        public static final int NET_PROTOCOL_BITICINO = NET_PROTOCOL_URA+1; //Bticino
        public static final int NET_PROTOCOL_ONVIF2 = NET_PROTOCOL_BITICINO+1; //Onvif协议类型,同NET_PROTOCOL_ONVIF
        public static final int NET_PROTOCOL_SHEPHERD = NET_PROTOCOL_ONVIF2+1; //视霸
        public static final int NET_PROTOCOL_YAAN = NET_PROTOCOL_SHEPHERD+1; //亚安
        public static final int NET_PROTOCOL_AIRPOINT = NET_PROTOCOL_YAAN+1; //Airpop
        public static final int NET_PROTOCOL_TYCO = NET_PROTOCOL_AIRPOINT+1; //TYCO
        public static final int NET_PROTOCOL_XUNMEI = NET_PROTOCOL_TYCO+1; //讯美
        public static final int NET_PROTOCOL_HIKVISION = NET_PROTOCOL_XUNMEI+1; //海康
        public static final int NET_PROTOCOL_LG = NET_PROTOCOL_HIKVISION+1; //LG
        public static final int NET_PROTOCOL_AOQIMAN = NET_PROTOCOL_LG+1; //奥奇曼
        public static final int NET_PROTOCOL_BAOKANG = NET_PROTOCOL_AOQIMAN+1; //宝康
        public static final int NET_PROTOCOL_WATCHNET = NET_PROTOCOL_BAOKANG+1; //Watchnet
        public static final int NET_PROTOCOL_XVISION = NET_PROTOCOL_WATCHNET+1; //Xvision
        public static final int NET_PROTOCOL_FUSITSU = NET_PROTOCOL_XVISION+1; //富士通
        public static final int NET_PROTOCOL_CANON = NET_PROTOCOL_FUSITSU+1; //Canon
        public static final int NET_PROTOCOL_GE = NET_PROTOCOL_CANON+1; //GE
        public static final int NET_PROTOCOL_Basler = NET_PROTOCOL_GE+1; //巴斯勒
        public static final int NET_PROTOCOL_Patro = NET_PROTOCOL_Basler+1; //帕特罗
        public static final int NET_PROTOCOL_CPKNC = NET_PROTOCOL_Patro+1; //CPPLUSK系列
        public static final int NET_PROTOCOL_CPRNC = NET_PROTOCOL_CPKNC+1; //CPPLUSR系列
        public static final int NET_PROTOCOL_CPUNC = NET_PROTOCOL_CPRNC+1; //CPPLUSU系列
        public static final int NET_PROTOCOL_CPPLUS = NET_PROTOCOL_CPUNC+1; //CPPLUSIPC
        public static final int NET_PROTOCOL_XunmeiS = NET_PROTOCOL_CPPLUS+1; //讯美s,实际协议为Onvif
        public static final int NET_PROTOCOL_GDDW = NET_PROTOCOL_XunmeiS+1; //广东电网
        public static final int NET_PROTOCOL_PSIA = NET_PROTOCOL_GDDW+1; //PSIA
        public static final int NET_PROTOCOL_GB2818 = NET_PROTOCOL_PSIA+1; //GB2818
        public static final int NET_PROTOCOL_GDYX = NET_PROTOCOL_GB2818+1; //GDYX
        public static final int NET_PROTOCOL_OTHER = NET_PROTOCOL_GDYX+1; //由用户自定义
    }
    
    // 雨刷来回循环刷,雨刷模式配置为手动模式时有效(对应命令 CTRLTYPE_CTRL_RAINBRUSH_MOVECONTINUOUSLY)
    public static class NET_CTRL_RAINBRUSH_MOVECONTINUOUSLY extends Structure
    {
        public int dwSize;
        public int nChannel;//表示雨刷的索引
        public int nInterval;//雨刷间隔
        
        public NET_CTRL_RAINBRUSH_MOVECONTINUOUSLY()
        {
            this.dwSize = this.size();
        }
    }

    // 雨刷停止刷,雨刷模式配置为手动模式时有效(对应命令 CTRLTYPE_CTRL_RAINBRUSH_STOPMOVE)
    public static class NET_CTRL_RAINBRUSH_STOPMOVE extends Structure
    {
        public int dwSize;
        public int nChannel;//表示雨刷的索引
        
        public NET_CTRL_RAINBRUSH_STOPMOVE()
        {
            this.dwSize = this.size();
        }
    }

    // 雨刷来回刷一次，雨刷模式配置为手动模式时有效(对应命令 CTRLTYPE_CTRL_RAINBRUSH_MOVEONCE)
    public static class NET_CTRL_RAINBRUSH_MOVEONCE extends Structure
    {
        public int dwSize;
        public int nChannel;//表示雨刷的索引
        
        public NET_CTRL_RAINBRUSH_MOVEONCE()
        {
            this.dwSize = this.size();
        }
    }
    
    // DSP能力描述，扩展类型，对应CLIENT_QueryDevState接口
    public static class NET_DEV_DSP_ENCODECAP extends Structure
    {
        public int dwVideoStandardMask; //视频制式掩码，按位表示设备能够支持的视频制式
        public int dwImageSizeMask;		//分辨率掩码，按位表示设备能够支持的分辨率
        public int dwEncodeModeMask;	//编码模式掩码，按位表示设备能够支持的编码模式
        public int dwStreamCap;			//按位表示设备支持的多媒体功能，
                               			// 第一位表示支持主码流
                               			// 第二位表示支持辅码流1
                               			// 第三位表示支持辅码流2
                               			// 第五位表示支持jpg抓图
        public int[] dwImageSizeMask_Assi = new int[32];//表示主码流为各分辨率时，支持的辅码流分辨率掩码。
        public int dwMaxEncodePower;	//DSP支持的最高编码能力
        public short wMaxSupportChannel;//每块DSP支持最多输入视频通道数
        public short wChannelMaxSetSync;//DSP每通道的最大编码设置是否同步；0：不同步，1：同步
        public byte[] bMaxFrameOfImageSize = new byte[32];//不同分辨率下的最大采集帧率，与dwVideoStandardMask按位对应
        public byte bEncodeCap;			//标志，配置时要求符合下面条件，否则配置不能生效；
                               			// 0：主码流的编码能力+辅码流的编码能力 <= 设备的编码能力，
                               			// 1：主码流的编码能力+辅码流的编码能力 <= 设备的编码能力，
                               			// 辅码流的编码能力 <= 主码流的编码能力，
                               			// 辅码流的分辨率 <= 主码流的分辨率，
                               			// 主码流和辅码流的帧率 <= 前端视频采集帧率
                               			// 2：N5的计算方法
                               			// 辅码流的分辨率 <= 主码流的分辨率
                               			// 查询支持的分辨率和相应最大帧率
        public byte[] reserved = new byte[95];
    }
    
    //云台控制坐标单元
    public static class PTZ_SPACE_UNIT extends Structure
    {
        public int nPositionX;//云台水平运动位置,有效范围：0,3600]
        public int nPositionY;//云台垂直运动位置,有效范围：-1800,1800]
        public int nZoom;//云台光圈变动位置,有效范围：0,128]
        public byte[] szReserve = new byte[32];//预留32字节
    }

    //云台控制速度单元
    public static class PTZ_SPEED_UNIT extends Structure
    {
        public float fPositionX;//云台水平方向速率,归一化到-1~1
        public float fPositionY;//云台垂直方向速率,归一化到-1~1
        public float fZoom;//云台光圈放大倍率,归一化到0~1
        public byte[] szReserve = new byte[32];//预留32字节
    }

    //持续控制云台对应结构
    public static class PTZ_CONTROL_CONTINUOUSLY extends Structure
    {
        public PTZ_SPEED_UNIT 	  stuSpeed;							//云台运行速度
        public int 				  nTimeOut;							//连续移动超时时间,单位为秒
        public byte[] 			  szReserve = new byte[64];			//预留64字节
    }

    //绝对控制云台对应结构
    public static class PTZ_CONTROL_ABSOLUTELY extends Structure
    {
        public PTZ_SPACE_UNIT 	 stuPosition;						//云台绝对移动位置
        public PTZ_SPEED_UNIT	 stuSpeed;							//云台运行速度
        public byte[] 			 szReserve = new byte[64];			//预留64字节
    }

    //带速度转动到预置位点云台控制对应结构
    public static class PTZ_CONTROL_GOTOPRESET extends Structure
    {
        public int 				nPresetIndex;						//预置位索引
        public PTZ_SPEED_UNIT 	stuSpeed;							//云台运行速度
        public byte[] 			szReserve = new byte[64];			//预留64字节
    }

    //设置云台可视域信息
    public static class PTZ_VIEW_RANGE_INFO extends Structure
    {
        public int 				nStructSize;
        public int 				nAzimuthH;							//水平方位角度,0~3600,单位:度
        
        public PTZ_VIEW_RANGE_INFO()
        {
            this.nStructSize = this.size();
        }
    }

    //云台绝对聚焦对应结构
    public static class PTZ_FOCUS_ABSOLUTELY extends Structure
    {
        public int 			   dwValue;					 //云台聚焦位置,取值范围(0~8191)
        public int 			   dwSpeed;					 //云台聚焦速度,取值范围(0~7)
        public byte[]		   szReserve = new byte[64]; //预留64字节
    }

    // 云台控制-扇扫对应结构
    public static class PTZ_CONTROL_SECTORSCAN extends Structure
    {
        public int 			 nBeginAngle;				//起始角度,范围:-180,180]
        public int			 nEndAngle;					//结束角度,范围:-180,180]
        public int 			 nSpeed;					//速度,范围:0,255]
        public byte[]		 szReserve = new byte[64];  //预留64字节
    }

    // 控制鱼眼电子云台信息
    public static class PTZ_CONTROL_SET_FISHEYE_EPTZ extends Structure
    {
        public int 			 dwSize;					//结构体大小
        public int 			 dwWindowID;				//进行EPtz控制的窗口编号
        public int 			 dwCommand;					//电子云台命令
        public int 			 dwParam1;					//命令对应参数1
        public int 			 dwParam2;					//命令对应参数2
        public int 			 dwParam3;					//命令对应参数3
        public int 			 dwParam4;					//命令对应参数4
    }
    
    // 变倍设置基本信息单元
    public static class CFG_VIDEO_IN_ZOOM_UNIT extends Structure
    {
        public int nSpeed;//变倍速率(0~7)
        public int bDigitalZoom;//是否数字变倍, 类型为BOOL, 取值0或者1
        public int nZoomLimit;//当前速率下最大变倍上限(0~13)。
    }

    // 单通道变倍设置基本信息
    public static class CFG_VIDEO_IN_ZOOM extends Structure
    {
        public int nChannelIndex;//通道号
        public int nVideoInZoomRealNum;//配置使用个数
        public CFG_VIDEO_IN_ZOOM_UNIT[] stVideoInZoomUnit = (CFG_VIDEO_IN_ZOOM_UNIT[])new CFG_VIDEO_IN_ZOOM_UNIT().toArray(MAX_VIDEO_IN_ZOOM);//通道变速配置单元信息
    }

    // 设备状态
    public static class CFG_TRAFFIC_DEVICE_STATUS extends Structure 
    {
        public byte[]                 szType = new byte[MAX_PATH];          // 设备类型 支持："Radar","Detector","SigDetector","StroboscopicLamp"," FlashLamp"
        public byte[]                 szSerialNo = new byte[MAX_PATH];      // 设备编号
        public byte[]                 szVendor = new byte[MAX_PATH];        // 生产厂商
        public int                    nWokingState;                         // 工作状态 0-故障,1-正常工作
    	public byte				      byLightState;						    // RS485灯的亮灭状态，Type 为"DhrsStroboscopicLamp"或者"DhrsSteadyLamp"时有效
																		    // 0-未知, 1-灯亮, 2-灯灭
    	public byte[]			      byReserved = new byte[3];             // 预留字节
    }
    
    // 获取设备工作状态是否正常 (对应命令 CFG_CAP_CMD_DEVICE_STATE )
    public static class CFG_CAP_TRAFFIC_DEVICE_STATUS extends Structure
    {
        public int                          nStatus;                        // stuStatus 实际个数
        public CFG_TRAFFIC_DEVICE_STATUS[]  stuStatus = (CFG_TRAFFIC_DEVICE_STATUS[]) new CFG_TRAFFIC_DEVICE_STATUS().toArray(MAX_STATUS_NUM);
    }
    
    // 视频输入通道
    public static class CFG_RemoteDeviceVideoInput extends Structure
    {
    	public int				bEnable;
    	public byte[]			szName = new byte[MAX_DEVICE_NAME_LEN];
    	public byte[]			szControlID = new byte[MAX_DEV_ID_LEN_EX];
    	public byte[]			szMainUrl = new byte[MAX_PATH];	// 主码流url地址
    	public byte[]			szExtraUrl = new byte[MAX_PATH]; // 辅码流url地址
    	public int				nServiceType; // 服务类型, 0-TCP, 1-UDP, 2-MCAST, -1-AUTO
    }
    
    // 远程设备
    public static class AV_CFG_RemoteDevice extends Structure
    {
    	public int 				nStructSize;
    	public int 				bEnable; // 使能
    	public byte[]			szID = new byte[AV_CFG_Device_ID_Len]; // 设备ID
    	public byte[]			szIP = new byte[AV_CFG_IP_Address_Len];// 设备IP
    	public int 				nPort; // 端口
    	public byte[]			szProtocol = new byte[AV_CFG_Protocol_Len]; // 协议类型
    	public byte[]			szUser = new byte[AV_CFG_User_Name_Len]; // 用户名
    	public byte[]			szPassword = new byte[AV_CFG_Password_Len]; // 密码
    	public byte[]			szSerial = new byte[AV_CFG_Serial_Len];	// 设备序列号
    	public byte[]			szDevClass = new byte[AV_CFG_Device_Class_Len]; // 设备类型
    	public byte[]			szDevType = new byte[AV_CFG_Device_Type_Len]; // 设备型号
    	public byte[]			szName = new byte[AV_CFG_Device_Name_Len]; // 机器名称
    	public byte[]			szAddress =  new byte[AV_CFG_Address_Len]; // 机器部署地点
    	public byte[]			szGroup = new byte[AV_CFG_Group_Name_Len]; // 机器分组
    	public int 				nDefinition; // 清晰度, 0-标清, 1-高清
    	public int 				nVideoChannel; // 视频输入通道数
    	public int 				nAudioChannel; // 音频输入通道数
    	public int             	nRtspPort; // Rtsp端口号
    	public byte[]           szVendor = new byte[MAX_PATH]; // 设备接入类型
    	public Pointer 			pVideoInput; // 视频输入通道，用户申请nMaxVideoInputs个CFG_RemoteDeviceVideoInput空间
    	public int              nMaxVideoInputs;
    	public int              nRetVideoInputs;
    	public int				nHttpPort; // http端口号
    	
    	/* 以下3项为国际接入方式相关  */
    	public int 				bGB28181; // 是否有国际接入方式
    	public int				nDevLocalPort; // 设备本地端口
    	public byte[]			szDeviceNo = new byte[AV_CFG_DeviceNo_Len]; // 设备编号
    	
    	public AV_CFG_RemoteDevice() {
        	this.nStructSize = this.size();
    	}
    }
    
    // 录像模式
    public static class AV_CFG_RecordMode extends Structure
    {
    	public int			nStructSize;
    	public int			nMode;							// 录像模式, 0-自动录像，1-手动录像，2-关闭录像
        public int			nModeExtra1;					// 辅码流录像模式, 0-自动录像，1-手动录像，2-关闭录像
        public int			nModeExtra2;					// 辅码流2录像模式, 0-自动录像，1-手动录像，2-关闭录像
        
        public AV_CFG_RecordMode() {
        	this.nStructSize = this.size();
        }
    }
    
    // 视频分析资源类型
    public static class CFG_VIDEO_SOURCE_TYPE extends Structure {
    	public static final int CFG_VIDEO_SOURCE_REALSTREAM = 0; // 实时流
    	public static final int CFG_VIDEO_SOURCE_FILESTREAM = 1; // 文件流
    }
    
    // 分析源文件类型
    public static class CFG_SOURCE_FILE_TYPE extends Structure {
    	public static final int CFG_SOURCE_FILE_UNKNOWN = 0; // 未知类型
    	public static final int CFG_SOURCE_FILE_RECORD = 1; // 录像文件
    	public static final int CFG_SOURCE_FILE_PICTURE = 2; // 图片文件
    }
    
    // 视频分析源文件信息
    public static class CFG_SOURCE_FILE_INFO extends Structure {
    	public byte[]				szFilePath = new byte[MAX_PATH];// 文件路径
    	public int 					emFileType; // 文件类型，详见 CFG_SOURCE_FILE_TYPE
    }
    
    // 每个视频输入通道对应的视频分析资源配置信息
    public static class CFG_ANALYSESOURCE_INFO extends Structure {
    	public byte					bEnable; // 视频分析使能   1-使能， 0-禁用
    	public int					nChannelID;	// 智能分析的前端视频通道号
    	public int					nStreamType;// 智能分析的前端视频码流类型，0:抓图码流; 1:主码流; 2:子码流1; 3:子码流2; 4:子码流3; 5:物体流
    	public byte[]				szRemoteDevice = new byte[MAX_NAME_LEN];// 设备名
    	public byte					abDeviceInfo; // 设备信息是否有效 ; 1-有效，0-无效
    	public AV_CFG_RemoteDevice  stuDeviceInfo; // 设备信息
    	public int                  emSourceType; // 视频分析源类型，详见  CFG_VIDEO_SOURCE_TYPE
    	public CFG_SOURCE_FILE_INFO stuSourceFile; // 当视频分析源类型为 CFG_VIDEO_SOURCE_FILESTREAM 时，有效
    } 
    
    public static class CFG_OVERSPEED_INFO extends Structure {
    	public int[]	nSpeedingPercentage = new int[2];                        // 超速百分比区间要求区间不能重叠。有效值为0,正数,-1，-1表示无穷大值
        // 如果是欠速：要求区间不能重叠。有效值为0,正数,-1，-1表示无穷大值，欠速百分比的计算方式：限低速-实际车速/限低速
    	public byte[]   szCode = new byte[MAX_VIOLATIONCODE];                     // 违章代码
    	public byte[]   szDescription = new byte[MAX_VIOLATIONCODE_DESCRIPT];     // 违章描述
    }
    
    // 违章代码配置表
    public static class VIOLATIONCODE_INFO extends Structure {
        public byte[]	szRetrograde = new byte[MAX_VIOLATIONCODE];			                   // 逆行
    	public byte[]	szRetrogradeDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];               // 违章描述信息
        public byte[]	szRetrogradeShowName = new byte[MAX_VIOLATIONCODE_DESCRIPT];           // 显示名称

    	public byte[]	szRetrogradeHighway = new byte[MAX_VIOLATIONCODE];		               // 逆行-高速公路
    	public byte[]	szRetrogradeHighwayDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];		   // 违章描述信息

    	public byte[]	szRunRedLight = new byte[MAX_VIOLATIONCODE];			               // 闯红灯
    	public byte[]	szRunRedLightDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];			   // 违章描述信息

    	public byte[]	szCrossLane = new byte[MAX_VIOLATIONCODE];				               // 违章变道
    	public byte[]	szCrossLaneDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];			       // 违章描述信息
        public byte[]	szCrossLaneShowName = new byte[MAX_VIOLATIONCODE_DESCRIPT];            // 违章变道显示名称

    	public byte[]	szTurnLeft = new byte[MAX_VIOLATIONCODE];				               // 违章左转
    	public byte[]   szTurnLeftDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];				   // 违章描述信息

    	public byte[]   szTurnRight = new byte[MAX_VIOLATIONCODE];				               // 违章右转
    	public byte[]   szTurnRightDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];				   // 违章描述信息

    	public byte[]   szU_Turn = new byte[MAX_VIOLATIONCODE];				                   // 违章掉头
    	public byte[]   szU_TurnDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];				   // 违章描述信息
        public byte[]   szU_TurnShowName = new byte[MAX_VIOLATIONCODE_DESCRIPT];               // 显示信息

    	public byte[]   szJam = new byte[MAX_VIOLATIONCODE];					                // 交通拥堵
    	public byte[]   szJamDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];					    // 违章描述信息

    	public byte[]   szParking = new byte[MAX_VIOLATIONCODE];				                // 违章停车
    	public byte[]   szParkingDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];		 	      	// 违章描述信息
        public byte[]   szParkingShowName = new byte[MAX_VIOLATIONCODE_DESCRIPT];               // 违章停车显示名称

    	// 超速 和 超速比例 只需且必须有一个配置
    	public byte[]    szOverSpeed = new byte[MAX_VIOLATIONCODE];				                // 超速
    	public byte[]    szOverSpeedDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];				// 违章描述信息
    	public CFG_OVERSPEED_INFO[]  stOverSpeedConfig = (CFG_OVERSPEED_INFO[])new CFG_OVERSPEED_INFO().toArray(5);                                               // 超速比例代码

    	// 超速(高速公路) 和 超速比例(高速公路) 只需且必须有一个配置
    	public byte[]    szOverSpeedHighway = new byte[MAX_VIOLATIONCODE];		                // 超速-高速公路
    	public byte[]    szOverSpeedHighwayDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];	      	// 超速-违章描述信息
    	public CFG_OVERSPEED_INFO[] stOverSpeedHighwayConfig = (CFG_OVERSPEED_INFO[])new CFG_OVERSPEED_INFO().toArray(5);                                 // 超速比例代码

    	// 欠速 和 欠速比例 只需且必须有一个配置
    	public byte[]    szUnderSpeed = new byte[MAX_VIOLATIONCODE];	                        // 欠速
    	public byte[]    szUnderSpeedDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];	            // 违章描述信息
    	public CFG_OVERSPEED_INFO[] stUnderSpeedConfig = (CFG_OVERSPEED_INFO[]) new CFG_OVERSPEED_INFO().toArray(5);                                              // 欠速配置信息是一个数组，不同的欠速比违章代码不同，为空表示违章代码不区分超速比

    	public byte[]    szOverLine = new byte[MAX_VIOLATIONCODE];				                // 压线
    	public byte[]    szOverLineDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];			    	// 违章描述信息
        public byte[]    szOverLineShowName = new byte[MAX_VIOLATIONCODE_DESCRIPT];             // 压线显示名称

    	public byte[]    szOverYellowLine = new byte[MAX_VIOLATIONCODE];	                    // 压黄线
    	public byte[]    szOverYellowLineDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];	    	// 违章描述信息

    	public byte[]    szYellowInRoute = new byte[MAX_VIOLATIONCODE];			                // 黄牌占道
    	public byte[]    szYellowInRouteDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];			// 黄牌占道违章描述信息

    	public byte[]    szWrongRoute = new byte[MAX_VIOLATIONCODE];			                // 不按车道行驶
    	public byte[]    szWrongRouteDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];				// 不按车道行驶违章描述信息

    	public byte[]    szDrivingOnShoulder = new byte[MAX_VIOLATIONCODE];		                // 路肩行驶
    	public byte[]    szDrivingOnShoulderDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];		// 路肩行驶违章描述信息

    	public byte[]    szPassing = new byte[MAX_VIOLATIONCODE];                               // 正常行驶
    	public byte[]    szPassingDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];               	// 正常行驶违章描述信息

    	public byte[]    szNoPassing = new byte[MAX_VIOLATIONCODE];                             // 禁止行驶
    	public byte[]    szNoPassingDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT]; 				// 禁止行驶违章描述信息

    	public byte[]    szFakePlate = new byte[MAX_VIOLATIONCODE];                             // 套牌
    	public byte[]    szFakePlateDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT]; 				// 套牌违章描述信息
    	
    	public byte[]    szParkingSpaceParking = new byte[MAX_VIOLATIONCODE];                   // 车位有车
    	public byte[]    szParkingSpaceParkingDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT]; 		// 车位有车违章描述信息、

    	public byte[]    szParkingSpaceNoParking = new byte[MAX_VIOLATIONCODE];                 // 车位无车
    	public byte[]    szParkingSpaceNoParkingDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT]; 	// 车位无车违章描述信息

        public byte[]    szWithoutSafeBelt = new byte[MAX_VIOLATIONCODE];                       // 不系安全带
        public byte[]    szWithoutSafeBeltShowName = new byte[MAX_VIOLATIONCODE_DESCRIPT];      // 不系安全带显示名称
        public byte[]    szWithoutSafeBeltDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT]; 	        // 不系安全带违章描述信息

        public byte[]    szDriverSmoking = new byte[MAX_VIOLATIONCODE];                         // 驾驶员抽烟
        public byte[]    szDriverSmokingShowName = new byte[MAX_VIOLATIONCODE_DESCRIPT];        // 驾驶员抽烟显示名称
        public byte[]    szDriverSmokingDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT]; 	        // 驾驶员抽烟带违章描述信息

        public byte[]    szDriverCalling = new byte[MAX_VIOLATIONCODE];                         // 驾驶员打电话
        public byte[]    szDriverCallingShowName = new byte[MAX_VIOLATIONCODE_DESCRIPT];        // 驾驶员打电话显示名称
        public byte[]    szDriverCallingDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT]; 	        // 驾驶员打电话违章描述信息

        public byte[]    szBacking = new byte[MAX_VIOLATIONCODE];                               // 违章倒车
        public byte[]    szBackingShowName = new byte[MAX_VIOLATIONCODE_DESCRIPT];	            // 违章倒车显示名称
        public byte[]    szBackingDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];                  // 违章倒车描述信息

        public byte[]    szVehicleInBusRoute = new byte[MAX_VIOLATIONCODE];                     // 违章占道
        public byte[]    szVehicleInBusRouteShowName = new byte[MAX_VIOLATIONCODE_DESCRIPT];    // 违章占道显示名称
        public byte[]    szVehicleInBusRouteDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];        // 违章占道描述信息

        public byte[]    szPedestrianRunRedLight = new byte[MAX_VIOLATIONCODE];                 // 行人闯红灯
        public byte[]    szPedestrianRunRedLightShowName = new byte[MAX_VIOLATIONCODE_DESCRIPT];// 行人闯红灯显示名称
        public byte[]    szPedestrianRunRedLightDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];    // 行人闯红灯描述信息
        
        public byte[]    szPassNotInOrder = new byte[MAX_VIOLATIONCODE];                        // 未按规定依次通行
        public byte[]    szPassNotInOrderShowName = new byte[MAX_VIOLATIONCODE_DESCRIPT];       // 未按规定依次通行显示名称
        public byte[]    szPassNotInOrderDesc = new byte[MAX_VIOLATIONCODE_DESCRIPT];           // 未按规定依次通行描述信息
    }
    
    // 违章抓拍时间配置表
    public static class TIME_SCHEDULE_INFO extends Structure {
    	public int                	 bEnable;                                              // 是否启用时间表
        public CFG_TIME_SECTION[]    stuTimeSchedule = (CFG_TIME_SECTION[])new CFG_TIME_SECTION().toArray(WEEK_DAY_NUM*MAX_REC_TSECT);         // 时间表
    }
    
    // 违章抓拍自定义时间配置
    public static class VIOLATION_TIME_SCHEDULE extends Structure 
    {
        public int                abTrafficGate;                  // 是否携带交通卡口信息
        public TIME_SCHEDULE_INFO  stTrafficGate;                  // 交通卡口时间配置

        public int                abTrafficJunction;              // 是否携带交通路口信息
        public TIME_SCHEDULE_INFO  stTrafficJunction;              // 交通路口时间配置

        public int                abTrafficTollGate;              // 是否携带新交通卡口信息
        public TIME_SCHEDULE_INFO  stTrafficTollGate;              // 新交通卡口时间配置

        public int                abTrafficRunRedLight;           // 是否携带交通闯红灯信息
        public TIME_SCHEDULE_INFO  stTrafficRunRedLight;           // 交通闯红灯时间配置
        
        public int                abTrafficRunYellowLight;        // 是否携带交通闯黄灯信息
        public TIME_SCHEDULE_INFO  stTrafficRunYellowLight;        // 交通闯黄灯时间配置

        public int                abTrafficOverLine;              // 是否携带交通压线信息
        public TIME_SCHEDULE_INFO  stTrafficOverLine;              // 交通压线时间配置

        public int                abTrafficOverYellowLine;        // 是否携带交通压黄线信息
        public TIME_SCHEDULE_INFO  stTrafficOverYellowLine;        // 交通压黄线时间配置

        public int                abTrafficRetrograde;            // 是否携带交通逆行信息
        public TIME_SCHEDULE_INFO  stTrafficRetrograde;            // 交通逆行时间配置

        public int                abTrafficTurnLeft;              // 是否携带交通违章左转信息
        public TIME_SCHEDULE_INFO  stTrafficTurnLeft;              // 交通违章左转时间配置

        public int                abTrafficTurnRight;             // 是否携带交通违章右转信息
        public TIME_SCHEDULE_INFO  stTrafficTurnRight;             // 交通路口违章右转类型

        public int                abTrafficU_Turn;                // 是否携带交通违章掉头信息
        public TIME_SCHEDULE_INFO  stTrafficU_Turn;                // 交通违章掉头时间配置

        public int                abTrafficCrossLane;             // 是否携带交通违章变道信息
        public TIME_SCHEDULE_INFO  stTrafficCrossLane;             // 交通违章变道时间配置

        public int                abTrafficParking;               // 是否携带交通违章停车信息
        public TIME_SCHEDULE_INFO  stTrafficParking;               // 交通违章停车时间配置

        public int                abTrafficJam;                   // 是否携带交通拥堵信息
        public TIME_SCHEDULE_INFO  stTrafficJam;                   // 交通拥堵时间配置

        public int                abTrafficIdle;                  // 是否携带交通交通空闲信息
        public TIME_SCHEDULE_INFO  stTrafficIdle;                  // 交通交通空闲时间配置

        public int                abTrafficWaitingArea;           // 是否携带交通违章驶入待行区信息
        public TIME_SCHEDULE_INFO  stTrafficWaitingArea;           // 交通违章驶入待行区时间配置

        public int                abTrafficUnderSpeed;            // 是否携带交通欠速信息
        public TIME_SCHEDULE_INFO  stTrafficUnderSpeed;            // 交通欠速时间配置

        public int                abTrafficOverSpeed;             // 是否携带交通超速信息
        public TIME_SCHEDULE_INFO  stTrafficOverSpeed;             // 交通超速时间配置

        public int                abTrafficWrongRoute;            // 是否携带交通不按车道行驶信息
        public TIME_SCHEDULE_INFO  stTrafficWrongRoute;            // 交通不按车道行驶时间配置

        public int                abTrafficYellowInRoute;         // 是否携带交通黄牌占道信息
        public TIME_SCHEDULE_INFO  stTrafficYellowInRoute;         // 交通黄牌占道时间配置

        public int                abTrafficVehicleInRoute;        // 是否携带交通有车占道信息
        public TIME_SCHEDULE_INFO  stTrafficVehicleInRoute;        // 交通有车占道时间配置

        public int                abTrafficControl;               // 是否携带交通交通管制信息
        public TIME_SCHEDULE_INFO  stTrafficControl;               // 交通交通管制时间配置

        public int                abTrafficObjectAlarm;           // 是否携带交通指定类型抓拍信息
        public TIME_SCHEDULE_INFO  stTrafficObjectAlarm;           // 交通指定类型抓拍时间配置

        public int                abTrafficAccident;              // 是否携带交通交通事故信息
        public TIME_SCHEDULE_INFO  stTrafficAccident;              // 交通交通事故时间配置

        public int                abTrafficStay;                  // 是否携带交通交通停留/滞留信息
        public TIME_SCHEDULE_INFO  stTrafficStay;                  // 交通交通停留/滞留时间配置

        public int                abTrafficPedestrainPriority;    // 是否携带交通斑马线行人优先信息
        public TIME_SCHEDULE_INFO  stTrafficPedestrainPriority;    // 交通斑马线行人优先时间配置

        public int                abTrafficPedestrain;            // 是否携带交通交通行人事件信息
        public TIME_SCHEDULE_INFO  stTrafficPedestrain;            // 交通交通行人事件时间配置

        public int                abTrafficThrow;                 // 是否携带交通交通抛洒物品事件信息
        public TIME_SCHEDULE_INFO  stTrafficThrow;                 // 交通交通抛洒物品事件时间配置

        public int                abTrafficVehicleInBusRoute;     // 是否携带交通违章占道信息
        public TIME_SCHEDULE_INFO  stTrafficVehicleInBusRoute;     // 交通违章占道时间配置

        public int                abTrafficBacking;               // 是否携带交通违章倒车信息
        public TIME_SCHEDULE_INFO  stTrafficBacking;               // 交通违章倒车时间配置

        public int                abTrafficOverStopLine;          // 是否携带交通压停止线信息
        public TIME_SCHEDULE_INFO  stTrafficOverStopLine;          // 交通压停止线时间配置

        public int                abTrafficParkingOnYellowBox;    // 是否携带交通黄网格线抓拍信息
        public TIME_SCHEDULE_INFO  stTrafficParkingOnYellowBox;    // 交通黄网格线抓拍时间配置

        public int                abTrafficParkingSpaceParking;   // 是否携带交通车位有车信息
        public TIME_SCHEDULE_INFO  stTrafficParkingSpaceParking;   // 交通车位有车时间配置

        public int                abTrafficParkingSpaceNoParking; // 是否携带交通车位无车信息
        public TIME_SCHEDULE_INFO  stTrafficParkingSpaceNoParking; // 交通车位无车时间配置

        public int                abTrafficParkingSpaceOverLine;  // 是否携带交通车位有车压线信息
        public TIME_SCHEDULE_INFO  stTrafficParkingSpaceOverLine;  // 交通车位有车压线时间配置

        public int                abParkingSpaceDetection;        // 是否携带交通多停车位状态检测信息
        public TIME_SCHEDULE_INFO  stParkingSpaceDetection;        // 交通多停车位状态检测时间配置

        public int                abTrafficRestrictedPlate;       // 是否携带交通受限车牌信息
        public TIME_SCHEDULE_INFO  stTrafficRestrictedPlate;       // 交通受限车牌时间配置

        public int                abTrafficWithoutSafeBelt;       // 是否携带交通不系安全带信息
        public TIME_SCHEDULE_INFO  stTrafficWithoutSafeBelt;       // 交通不系安全带时间配置

        public int                abTrafficNoPassing;             // 是否携带交通禁行信息
        public TIME_SCHEDULE_INFO  stTrafficNoPassing;             // 交通禁行时间配置

        public int                abVehicleAnalyse;               // 是否携带交通车辆特征检测分析信息
        public TIME_SCHEDULE_INFO  stVehicleAnalyse;               // 交通车辆特征时间配置

        public int                abCrossLineDetection;           // 是否携带交通警戒线信息
        public TIME_SCHEDULE_INFO  stCrossLineDetection;           // 交通警戒线时间配置

        public int                abCrossFenceDetection;          // 是否携带交通穿越围栏信息
        public TIME_SCHEDULE_INFO  stCrossFenceDetection;          // 交通穿越围栏时间配置

        public int                abCrossRegionDetection;         // 是否携带交通警戒区信息
        public TIME_SCHEDULE_INFO  stCrossRegionDetection;         // 交通警戒区时间配置

        public int                abPasteDetection;               // 是否携带交通ATM贴条信息
        public TIME_SCHEDULE_INFO  stPasteDetection;               // 交通ATM贴条时间配置

        public int                abLeftDetection;                // 是否携带交通物品遗留信息
        public TIME_SCHEDULE_INFO  stLeftDetection;                // 交通物品遗留时间配置

        public int                abPreservation;                 // 是否携带交通物品保全信息
        public TIME_SCHEDULE_INFO  stPreservation;                 // 交通物品保全时间配置

        public int                abTakenAwayDetection;           // 是否携带交通物品搬移信息
        public TIME_SCHEDULE_INFO  stTakenAwayDetection;           // 交通物品搬移时间配置

        public int                abStayDetection;                // 是否携带交通停留/滞留信息
        public TIME_SCHEDULE_INFO  stStayDetection;                // 交通停留/滞留时间配置

        public int                abParkingDetection;             // 是否携带交通非法停车信息
        public TIME_SCHEDULE_INFO  stParkingDetection;             // 交通非法停车时间配置

        public int                abWanderDetection;              // 是否携带交通徘徊信息
        public TIME_SCHEDULE_INFO  stWanderDetection;              // 交通徘徊时间配置

        public int                abMoveDetection;                // 是否携带交通运动信息
        public TIME_SCHEDULE_INFO  stMoveDetection;                // 交通运动时间配置

        public int                abTailDetection;                // 是否携带交通尾随信息
        public TIME_SCHEDULE_INFO  stTailDetection;                // 交通尾随时间配置

        public int                abRioterDetection;              // 是否携带交通聚集信息
        public TIME_SCHEDULE_INFO  stRioterDetection;              // 交通聚集时间配置

        public int                abFightDetection;               // 是否携带交通打架信息
        public TIME_SCHEDULE_INFO  stFightDetection;               // 交通打架时间配置

        public int                abRetrogradeDetection;          // 是否携带交通逆行信息
        public TIME_SCHEDULE_INFO  stRetrogradeDetection;          // 交通逆行时间配置

        public int                abFireDetection;                // 是否携带交通火焰信息
        public TIME_SCHEDULE_INFO  stFireDetection;                // 交通火焰时间配置

        public int                abSmokeDetection;               // 是否携带交通烟雾信息
        public TIME_SCHEDULE_INFO  stSmokeDetection;               // 交通烟雾时间配置

        public int                abNumberStat;                   // 是否携带交通数量统计信息
        public TIME_SCHEDULE_INFO  stNumberStat;                   // 交通数量统计时间配置

        public int                abVideoAbnormalDetection;       // 是否携带交通视频异常信息
        public TIME_SCHEDULE_INFO  stVideoAbnormalDetection;       // 交通视频异常时间配置

        public int                abPrisonerRiseDetection;        // 是否携带看守所囚犯起身检测信息
        public TIME_SCHEDULE_INFO  stPrisonerRiseDetection;        // 看守所囚犯起身检测时间配置

        public int                abFaceDetection;                // 是否携带人脸检测信息
        public TIME_SCHEDULE_INFO  stFaceDetection;                // 人脸检测时间配置

        public int                abFaceRecognition;              // 是否携带人脸识别信息
        public TIME_SCHEDULE_INFO  stFaceRecognition;              // 人脸识别时间配置

        public int                abDensityDetection;             // 是否携带密集度检测信息
        public TIME_SCHEDULE_INFO  stDensityDetection;             // 密集度检测时间配置

        public int                abQueueDetection;               // 是否携带排队检测信息
        public TIME_SCHEDULE_INFO  stQueueDetection;               // 排队检测时间配置

        public int                abClimbDetection;               // 是否携带攀高检测信息
        public TIME_SCHEDULE_INFO  stClimbDetection;               // 攀高时间配置

        public int                abLeaveDetection;               // 是否携带离岗检测信息
        public TIME_SCHEDULE_INFO  stLeaveDetection;               // 离岗时间配置

        public int                abVehicleOnPoliceCar;           // 是否携带车载警车信息
        public TIME_SCHEDULE_INFO  stVehicleOnPoliceCar;           // 车载警车时间配置

        public int                abVehicleOnBus;                 // 是否携带车载公交信息
        public TIME_SCHEDULE_INFO  stVehicleOnBus;                 // 车载公交时间配置

        public int                abVehicleOnSchoolBus;           // 是否携带车载校车信息
        public TIME_SCHEDULE_INFO  stVehicleOnSchoolBus;           // 车载校车时间配置  
    }
    
    // 交通全局配置对应图片命名格式参数配置
    public static class TRAFFIC_NAMING_FORMAT extends Structure {
    	public byte[]               szFormat = new byte[CFG_COMMON_STRING_256];            // 图片格式
    }
    
    // CFG_NET_TIME 时间
    public static class CFG_NET_TIME extends Structure {
    	public int                 	nStructSize;
        public int					dwYear;					// 年
        public int					dwMonth;				// 月
        public int					dwDay;					// 日
        public int					dwHour;					// 时
        public int					dwMinute;				// 分
        public int					dwSecond;				// 秒
        
        public CFG_NET_TIME() {
        	this.nStructSize = this.size();
        }
    }
    
    // PERIOD_OF_VALIDITY
    public static class PERIOD_OF_VALIDITY extends Structure {
        public CFG_NET_TIME            stBeginTime;                    // 标定开始时间 
        public CFG_NET_TIME            stEndTime;                      // 标定到期时间
    }
    
    // 交通全局配置对应标定相关配置
    public static class TRAFFIC_CALIBRATION_INFO extends Structure {
    	public byte[]               szUnit = new byte[CFG_COMMON_STRING_256];              // 标定单位
    	public byte[]               szCertificate = new byte[CFG_COMMON_STRING_256];       // 标定证书
        public PERIOD_OF_VALIDITY   stPeriodOfValidity;                         // 标定有效期
    }
    
    // TRAFFIC_EVENT_CHECK_MASK
    public static class TRAFFIC_EVENT_CHECK_MASK extends Structure {
        public int                abTrafficGate;                  // 是否携带交通卡口信息
        public int                 nTrafficGate;                   // 交通卡口检测模式掩码

        public int                abTrafficJunction;              // 是否携带交通路口信息
        public int                 nTrafficJunction;               // 交通路口检测模式掩码

        public int                abTrafficTollGate;              // 是否携带新交通卡口信息
        public int                 nTrafficTollGate;               // 新交通卡口检测模式掩码

        public int                abTrafficRunRedLight;           // 是否携带交通闯红灯信息
        public int                 nTrafficRunRedLight;            // 交通闯红灯检测模式掩码
        
        public int                abTrafficRunYellowLight;        // 是否携带交通闯黄灯信息
        public int                 nTrafficRunYellowLight;         // 交通闯黄灯检测模式掩码

        public int                abTrafficOverLine;              // 是否携带交通压线信息
        public int                 nTrafficOverLine;               // 交通压线检测模式掩码

        public int                abTrafficOverYellowLine;        // 是否携带交通压黄线信息
        public int                 nTrafficOverYellowLine;         // 交通压黄线检测模式掩码

        public int                abTrafficRetrograde;            // 是否携带交通逆行信息
        public int                 nTrafficRetrograde;             // 交通逆行检测模式掩码

        public int                abTrafficTurnLeft;              // 是否携带交通违章左转信息
        public int                 nTrafficTurnLeft;               // 交通违章左转检测模式掩码

        public int                abTrafficTurnRight;             // 是否携带交通违章右转信息
        public int                 nTrafficTurnRight;              // 交通路口违章右转类型

        public int                abTrafficU_Turn;                // 是否携带交通违章掉头信息
        public int                 nTrafficU_Turn;                 // 交通违章掉头检测模式掩码

        public int                abTrafficCrossLane;             // 是否携带交通违章变道信息
        public int                 nTrafficCrossLane;              // 交通违章变道检测模式掩码

        public int                abTrafficParking;               // 是否携带交通违章停车信息
        public int                 nTrafficParking;                // 交通违章停车检测模式掩码

        public int                abTrafficJam;                   // 是否携带交通拥堵信息
        public int                 nTrafficJam;                    // 交通拥堵检测模式掩码

        public int                abTrafficIdle;                  // 是否携带交通交通空闲信息
        public int                 nTrafficIdle;                   // 交通交通空闲检测模式掩码

        public int                abTrafficWaitingArea;           // 是否携带交通违章驶入待行区信息
        public int                 nTrafficWaitingArea;            // 交通违章驶入待行区检测模式掩码

        public int                abTrafficUnderSpeed;            // 是否携带交通欠速信息
        public int                 nTrafficUnderSpeed;             // 交通欠速检测模式掩码

        public int                abTrafficOverSpeed;             // 是否携带交通超速信息
        public int                 nTrafficOverSpeed;              // 交通超速检测模式掩码

        public int                abTrafficWrongRoute;            // 是否携带交通不按车道行驶信息
        public int                 nTrafficWrongRoute;             // 交通不按车道行驶检测模式掩码

        public int                abTrafficYellowInRoute;         // 是否携带交通黄牌占道信息
        public int                 nTrafficYellowInRoute;          // 交通黄牌占道检测模式掩码

        public int                abTrafficVehicleInRoute;        // 是否携带交通有车占道信息
        public int                 nTrafficVehicleInRoute;         // 交通有车占道检测模式掩码

        public int                abTrafficControl;               // 是否携带交通交通管制信息
        public int                 nTrafficControl;                // 交通交通管制检测模式掩码

        public int                abTrafficObjectAlarm;           // 是否携带交通指定类型抓拍信息
        public int                 nTrafficObjectAlarm;            // 交通指定类型抓拍检测模式掩码

        public int                abTrafficAccident;              // 是否携带交通交通事故信息
        public int                 nTrafficAccident;               // 交通交通事故检测模式掩码

        public int                abTrafficStay;                  // 是否携带交通交通停留/滞留信息
        public int                 nTrafficStay;                   // 交通交通停留/滞留检测模式掩码

        public int                abTrafficPedestrainPriority;    // 是否携带交通斑马线行人优先信息
        public int                 nTrafficPedestrainPriority;     // 交通斑马线行人优先检测模式掩码

        public int                abTrafficPedestrain;            // 是否携带交通交通行人事件信息
        public int                 nTrafficPedestrain;             // 交通交通行人事件检测模式掩码

        public int                abTrafficThrow;                 // 是否携带交通交通抛洒物品事件信息
        public int                 nTrafficThrow;                  // 交通交通抛洒物品事件检测模式掩码

        public int                abTrafficVehicleInBusRoute;     // 是否携带交通违章占道信息
        public int                 nTrafficVehicleInBusRoute;      // 交通违章占道检测模式掩码

        public int                abTrafficBacking;               // 是否携带交通违章倒车信息
        public int                 nTrafficBacking;                // 交通违章倒车检测模式掩码

        public int                abTrafficOverStopLine;          // 是否携带交通压停止线信息
        public int                 nTrafficOverStopLine;           // 交通压停止线检测模式掩码

        public int                abTrafficParkingOnYellowBox;    // 是否携带交通黄网格线抓拍信息
        public int                 nTrafficParkingOnYellowBox;     // 交通黄网格线抓拍检测模式掩码

        public int                abTrafficParkingSpaceParking;   // 是否携带交通车位有车信息
        public int                 nTrafficParkingSpaceParking;    // 交通车位有车检测模式掩码

        public int                abTrafficParkingSpaceNoParking; // 是否携带交通车位无车信息
        public int                 nTrafficParkingSpaceNoParking;  // 交通车位无车检测模式掩码

        public int                abTrafficParkingSpaceOverLine;  // 是否携带交通车位有车压线信息
        public int                 nTrafficParkingSpaceOverLine;   // 交通车位有车压线检测模式掩码

        public int                abParkingSpaceDetection;        // 是否携带交通多停车位状态检测信息
        public int                 nParkingSpaceDetection;         // 交通多停车位状态检测检测模式掩码

        public int                abTrafficRestrictedPlate;       // 是否携带交通受限车牌信息
        public int                 nTrafficRestrictedPlate;        // 交通受限车牌检测模式掩码

        public int                abTrafficWithoutSafeBelt;       // 是否携带交通不系安全带信息
        public int                 nTrafficWithoutSafeBelt;        // 交通不系安全带检测模式掩码

        public int                abTrafficNoPassing;             // 是否携带交通禁行信息
        public int                 nTrafficNoPassing;              // 交通禁行检测模式掩码

        public int                abVehicleAnalyse;               // 是否携带交通车辆特征检测分析信息
        public int                 nVehicleAnalyse;                // 交通车辆特征检测模式掩码

        public int                abCrossLineDetection;           // 是否携带交通警戒线信息
        public int                 nCrossLineDetection;            // 交通警戒线检测模式掩码

        public int                abCrossFenceDetection;          // 是否携带交通穿越围栏信息
        public int                 nCrossFenceDetection;           // 交通穿越围栏检测模式掩码

        public int                abCrossRegionDetection;         // 是否携带交通警戒区信息
        public int                 nCrossRegionDetection;          // 交通警戒区检测模式掩码

        public int                abPasteDetection;               // 是否携带交通ATM贴条信息
        public int                 nPasteDetection;                // 交通ATM贴条检测模式掩码

        public int                abLeftDetection;                // 是否携带交通物品遗留信息
        public int                 nLeftDetection;                 // 交通物品遗留检测模式掩码

        public int                abPreservation;                 // 是否携带交通物品保全信息
        public int                 nPreservation;                  // 交通物品保全检测模式掩码

        public int                abTakenAwayDetection;           // 是否携带交通物品搬移信息
        public int                 nTakenAwayDetection;            // 交通物品搬移检测模式掩码

        public int                abStayDetection;                // 是否携带交通停留/滞留信息
        public int                 nStayDetection;                 // 交通停留/滞留检测模式掩码

        public int                abParkingDetection;             // 是否携带交通非法停车信息
        public int                 nParkingDetection;              // 交通非法停车检测模式掩码

        public int                abWanderDetection;              // 是否携带交通徘徊信息
        public int                 nWanderDetection;               // 交通徘徊检测模式掩码

        public int                abMoveDetection;                // 是否携带交通运动信息
        public int                 nMoveDetection;                 // 交通运动检测模式掩码

        public int                abTailDetection;                // 是否携带交通尾随信息
        public int                 nTailDetection;                 // 交通尾随检测模式掩码

        public int                abRioterDetection;              // 是否携带交通聚集信息
        public int                 nRioterDetection;               // 交通聚集检测模式掩码

        public int                abFightDetection;               // 是否携带交通打架信息
        public int                 nFightDetection;                // 交通打架检测模式掩码

        public int                abRetrogradeDetection;          // 是否携带交通逆行信息
        public int                 nRetrogradeDetection;           // 交通逆行检测模式掩码

        public int                abFireDetection;                // 是否携带交通火焰信息
        public int                 nFireDetection;                 // 交通火焰检测模式掩码

        public int                abSmokeDetection;               // 是否携带交通烟雾信息
        public int                 nSmokeDetection;                // 交通烟雾检测模式掩码

        public int                abNumberStat;                   // 是否携带交通数量统计信息
        public int                 nNumberStat;                    // 交通数量统计检测模式掩码

        public int                abVideoAbnormalDetection;       // 是否携带交通视频异常信息
        public int                 nVideoAbnormalDetection;        // 交通视频异常检测模式掩码

        public int                abPrisonerRiseDetection;        // 是否携带看守所囚犯起身检测信息
        public int                 nPrisonerRiseDetection;         // 看守所囚犯起身检测检测模式掩码

        public int                abFaceDetection;                // 是否携带人脸检测信息
        public int                 nFaceDetection;                 // 人脸检测检测模式掩码

        public int                abFaceRecognition;              // 是否携带人脸识别信息
        public int                 nFaceRecognition;               // 人脸识别检测模式掩码

        public int                abDensityDetection;             // 是否携带密集度检测信息
        public int                 nDensityDetection;              // 密集度检测检测模式掩码

        public int                abQueueDetection;               // 是否携带排队检测信息
        public int                 nQueueDetection;                // 排队检测检测模式掩码

        public int                abClimbDetection;               // 是否携带攀高检测信息
        public int                 nClimbDetection;                // 攀高检测模式掩码

        public int                abLeaveDetection;               // 是否携带离岗检测信息
        public int                 nLeaveDetection;                // 离岗检测模式掩码

        public int                abVehicleOnPoliceCar;           // 是否携带车载警车信息
        public int                 nVehicleOnPoliceCar;            // 车载警车检测模式掩码

        public int                abVehicleOnBus;                 // 是否携带车载公交信息
        public int                 nVehicleOnBus;                  // 车载公交检测模式掩码

        public int                abVehicleOnSchoolBus;           // 是否携带车载校车信息
        public int                 nVehicleOnSchoolBus;            // 车载校车检测模式掩码  
        
    }
    
    // 交通全局配置对应灯组状态配置
    public static class ENABLE_LIGHT_STATE_INFO extends Structure {
    	public int 				bEnable;      // 是否启动应用层收到的灯组状态给底层
    }
    
    // 车道检测类型
    public static class EM_CHECK_TYPE extends Structure {
    	public int EM_CHECK_TYPE_UNKNOWN = 0;             // 不识别的检测类型
    	public int EM_CHECK_TYPE_PHYSICAL = 1;            // 物理检测
    	public int EM_CHECK_TYPE_VIDEO = 2;               // 视频检测
    }
    
    // TRAFFIC_EVENT_CHECK_INFO
    public static class TRAFFIC_EVENT_CHECK_INFO extends Structure {
        public int       abTrafficGate;                  // 是否携带交通卡口信息
        public int       emTrafficGate;                  // 交通卡口检测类型 EM_CHECK_TYPE

        public int       abTrafficJunction;              // 是否携带交通路口信息
        public int       emTrafficJunction;              // 交通路口检测类型

        public int       abTrafficTollGate;              // 是否携带新交通卡口信息
        public int       emTrafficTollGate;              // 新交通卡口检测类型

        public int       abTrafficRunRedLight;           // 是否携带交通闯红灯信息
        public int       emTrafficRunRedLight;           // 交通闯红灯检测类型
        
        public int       abTrafficRunYellowLight;        // 是否携带交通闯黄灯信息
        public int       emTrafficRunYellowLight;        // 交通闯黄灯检测类型

        public int       abTrafficOverLine;              // 是否携带交通压线信息
        public int       emTrafficOverLine;              // 交通压线检测类型

        public int       abTrafficOverYellowLine;        // 是否携带交通压黄线信息
        public int       emTrafficOverYellowLine;        // 交通压黄线检测类型

        public int       abTrafficRetrograde;            // 是否携带交通逆行信息
        public int       emTrafficRetrograde;            // 交通逆行检测类型

        public int       abTrafficTurnLeft;              // 是否携带交通违章左转信息
        public int       emTrafficTurnLeft;              // 交通违章左转检测类型

        public int       abTrafficTurnRight;             // 是否携带交通违章右转信息
        public int       emTrafficTurnRight;             // 交通路口违章右转类型

        public int       abTrafficU_Turn;                // 是否携带交通违章掉头信息
        public int       emTrafficU_Turn;                // 交通违章掉头检测类型

        public int       abTrafficCrossLane;             // 是否携带交通违章变道信息
        public int       emTrafficCrossLane;             // 交通违章变道检测类型

        public int       abTrafficParking;               // 是否携带交通违章停车信息
        public int       emTrafficParking;               // 交通违章停车检测类型

        public int       abTrafficJam;                   // 是否携带交通拥堵信息
        public int       emTrafficJam;                   // 交通拥堵检测类型

        public int       abTrafficIdle;                  // 是否携带交通交通空闲信息
        public int       emTrafficIdle;                  // 交通交通空闲检测类型

        public int       abTrafficWaitingArea;           // 是否携带交通违章驶入待行区信息
        public int       emTrafficWaitingArea;           // 交通违章驶入待行区检测类型

        public int       abTrafficUnderSpeed;            // 是否携带交通欠速信息
        public int       emTrafficUnderSpeed;            // 交通欠速检测类型

        public int       abTrafficOverSpeed;             // 是否携带交通超速信息
        public int       emTrafficOverSpeed;             // 交通超速检测类型

        public int       abTrafficWrongRoute;            // 是否携带交通不按车道行驶信息
        public int       emTrafficWrongRoute;            // 交通不按车道行驶检测类型

        public int       abTrafficYellowInRoute;         // 是否携带交通黄牌占道信息
        public int       emTrafficYellowInRoute;         // 交通黄牌占道检测类型

        public int       abTrafficVehicleInRoute;        // 是否携带交通有车占道信息
        public int       emTrafficVehicleInRoute;        // 交通有车占道检测类型

        public int       abTrafficControl;               // 是否携带交通交通管制信息
        public int       emTrafficControl;               // 交通交通管制检测类型

        public int       abTrafficObjectAlarm;           // 是否携带交通指定类型抓拍信息
        public int       emTrafficObjectAlarm;           // 交通指定类型抓拍检测类型

        public int       abTrafficAccident;              // 是否携带交通交通事故信息
        public int       emTrafficAccident;              // 交通交通事故检测类型

        public int       abTrafficStay;                  // 是否携带交通交通停留/滞留信息
        public int       emTrafficStay;                  // 交通交通停留/滞留检测类型

        public int       abTrafficPedestrainPriority;    // 是否携带交通斑马线行人优先信息
        public int       emTrafficPedestrainPriority;    // 交通斑马线行人优先检测类型

        public int       abTrafficPedestrain;            // 是否携带交通交通行人事件信息
        public int       emTrafficPedestrain;            // 交通交通行人事件检测类型

        public int       abTrafficThrow;                 // 是否携带交通交通抛洒物品事件信息
        public int       emTrafficThrow;                 // 交通交通抛洒物品事件检测类型

        public int       abTrafficVehicleInBusRoute;     // 是否携带交通违章占道信息
        public int       emTrafficVehicleInBusRoute;     // 交通违章占道检测类型

        public int       abTrafficBacking;               // 是否携带交通违章倒车信息
        public int       emTrafficBacking;               // 交通违章倒车检测类型

        public int       abTrafficOverStopLine;          // 是否携带交通压停止线信息
        public int       emTrafficOverStopLine;          // 交通压停止线检测类型

        public int       abTrafficParkingOnYellowBox;    // 是否携带交通黄网格线抓拍信息
        public int       emTrafficParkingOnYellowBox;    // 交通黄网格线抓拍检测类型

        public int       abTrafficParkingSpaceParking;   // 是否携带交通车位有车信息
        public int       emTrafficParkingSpaceParking;   // 交通车位有车检测类型

        public int       abTrafficParkingSpaceNoParking; // 是否携带交通车位无车信息
        public int       emTrafficParkingSpaceNoParking; // 交通车位无车检测类型

        public int       abTrafficParkingSpaceOverLine;  // 是否携带交通车位有车压线信息
        public int       emTrafficParkingSpaceOverLine;  // 交通车位有车压线检测类型

        public int       abParkingSpaceDetection;        // 是否携带交通多停车位状态检测信息
        public int       emParkingSpaceDetection;        // 交通多停车位状态检测检测类型

        public int       abTrafficRestrictedPlate;       // 是否携带交通受限车牌信息
        public int       emTrafficRestrictedPlate;       // 交通受限车牌检测类型

        public int       abTrafficWithoutSafeBelt;       // 是否携带交通不系安全带信息
        public int       emTrafficWithoutSafeBelt;       // 交通不系安全带检测类型

        public int       abTrafficNoPassing;             // 是否携带交通禁行信息
        public int       emTrafficNoPassing;             // 交通禁行检测类型

        public int       abVehicleAnalyse;               // 是否携带交通车辆特征检测分析信息
        public int       emVehicleAnalyse;               // 交通车辆特征检测类型

        public int       abCrossLineDetection;           // 是否携带交通警戒线信息
        public int       emCrossLineDetection;           // 交通警戒线检测类型

        public int       abCrossFenceDetection;          // 是否携带交通穿越围栏信息
        public int       emCrossFenceDetection;          // 交通穿越围栏检测类型

        public int       abCrossRegionDetection;         // 是否携带交通警戒区信息
        public int       emCrossRegionDetection;         // 交通警戒区检测类型

        public int       abPasteDetection;               // 是否携带交通ATM贴条信息
        public int       emPasteDetection;               // 交通ATM贴条检测类型

        public int       abLeftDetection;                // 是否携带交通物品遗留信息
        public int       emLeftDetection;                // 交通物品遗留检测类型

        public int       abPreservation;                 // 是否携带交通物品保全信息
        public int       emPreservation;                 // 交通物品保全检测类型

        public int       abTakenAwayDetection;           // 是否携带交通物品搬移信息
        public int       emTakenAwayDetection;           // 交通物品搬移检测类型

        public int       abStayDetection;                // 是否携带交通停留/滞留信息
        public int       emStayDetection;                // 交通停留/滞留检测类型

        public int       abParkingDetection;             // 是否携带交通非法停车信息
        public int       emParkingDetection;             // 交通非法停车检测类型

        public int       abWanderDetection;              // 是否携带交通徘徊信息
        public int       emWanderDetection;              // 交通徘徊检测类型

        public int       abMoveDetection;                // 是否携带交通运动信息
        public int       emMoveDetection;                // 交通运动检测类型

        public int       abTailDetection;                // 是否携带交通尾随信息
        public int       emTailDetection;                // 交通尾随检测类型

        public int       abRioterDetection;              // 是否携带交通聚集信息
        public int       emRioterDetection;              // 交通聚集检测类型

        public int       abFightDetection;               // 是否携带交通打架信息
        public int       emFightDetection;               // 交通打架检测类型

        public int       abRetrogradeDetection;          // 是否携带交通逆行信息
        public int       emRetrogradeDetection;          // 交通逆行检测类型

        public int       abFireDetection;                // 是否携带交通火焰信息
        public int       emFireDetection;                // 交通火焰检测类型

        public int       abSmokeDetection;               // 是否携带交通烟雾信息
        public int       emSmokeDetection;               // 交通烟雾检测类型

        public int       abNumberStat;                   // 是否携带交通数量统计信息
        public int       emNumberStat;                   // 交通数量统计检测类型

        public int       abVideoAbnormalDetection;       // 是否携带交通视频异常信息
        public int       emVideoAbnormalDetection;       // 交通视频异常检测类型

        public int       abPrisonerRiseDetection;        // 是否携带看守所囚犯起身检测信息
        public int       emPrisonerRiseDetection;        // 看守所囚犯起身检测检测类型

        public int       abFaceDetection;                // 是否携带人脸检测信息
        public int       emFaceDetection;                // 人脸检测检测类型

        public int       abFaceRecognition;              // 是否携带人脸识别信息
        public int       emFaceRecognition;              // 人脸识别检测类型

        public int       abDensityDetection;             // 是否携带密集度检测信息
        public int       emDensityDetection;             // 密集度检测检测类型

        public int       abQueueDetection;               // 是否携带排队检测信息
        public int       emQueueDetection;               // 排队检测检测类型

        public int       abClimbDetection;               // 是否携带攀高检测信息
        public int       emClimbDetection;               // 攀高检测类型

        public int       abLeaveDetection;               // 是否携带离岗检测信息
        public int       emLeaveDetection;               // 离岗检测类型

        public int       abVehicleOnPoliceCar;           // 是否携带车载警车信息
        public int       emVehicleOnPoliceCar;           // 车载警车检测类型

        public int       abVehicleOnBus;                 // 是否携带车载公交信息
        public int       emVehicleOnBus;                 // 车载公交检测类型

        public int       abVehicleOnSchoolBus;           // 是否携带车载校车信息
        public int       emVehicleOnSchoolBus;           // 车载校车检测类型  

    	public int		 abStandUpDetection;		     // 是否携带学生起立信息
    	public int		 emStandUpDetection;		     // 学生起立检测类型    	
    }
    
    // MixModeConfig中关于车道配置信息
    public static class MIX_MODE_LANE_INFO extends Structure {
    	 public  int                nLaneNum;                           // 车道配置个数
    	 public  TRAFFIC_EVENT_CHECK_INFO[]   stCheckInfo = (TRAFFIC_EVENT_CHECK_INFO[]) new TRAFFIC_EVENT_CHECK_INFO().toArray(MAX_LANE_CONFIG_NUMBER);     // 车道配置对应事件检测信息
    }
    
    // MixModeConfig 混合模式违章配置
    public static class MIX_MODE_CONFIG extends Structure {
    	public int                         bLaneDiffEnable;                    // 是否按车道区分
    	public MIX_MODE_LANE_INFO          stLaneInfo;
    	public TRAFFIC_EVENT_CHECK_INFO    stCheckInfo;
    	
    }
    
    // CFG_CMD_TRAFFICGLOBAL 交通全局配置配置表
    public static class CFG_TRAFFICGLOBAL_INFO extends Structure 
    {
    	public VIOLATIONCODE_INFO     stViolationCode;                            // 违章代码配置表                          
        public int                    bEnableRedList;                             // 使能红名单检测，使能后，名单内车辆违章不上报

        public int                    abViolationTimeSchedule;                    // 是否携带违章抓拍自定义时间配置
        public VIOLATION_TIME_SCHEDULE stViolationTimeSchedule;                   // 违章抓拍自定义时间配置
        
        public int                    abEnableBlackList;                          // 是否携带使能黑名单检测信息
        public int                    bEnableBlackList;                           // 使能黑名单检测

        public int                    abPriority;                                 // 是否携带违章优先级参数
        public int            		  nPriority;                                  // 违章优先级个数
        public byte[]                 szPriority = new byte[MAX_PRIORITY_NUMBER*CFG_COMMON_STRING_256]; // 违章优先级, 0为最高优先级    

        public int                    abNamingFormat;                             // 是否携带图片命名格式参数
        public TRAFFIC_NAMING_FORMAT  stNamingFormat;                             	  // 图片命名格式参数配置

        public int                    abVideoNamingFormat;                        // 是否携带录像命名格式参数
        public TRAFFIC_NAMING_FORMAT  stVideoNamingFormat;                        // 录像命名格式参数配置

        public int                    abCalibration;                              // 是否携带标定信息
        public TRAFFIC_CALIBRATION_INFO stCalibration;                             // 标定信息
        
        public int                    abAddress;                                  // 是否携带查询地址参数
        public byte[]                 szAddress = new byte[CFG_COMMON_STRING_256];           // 查询地址，UTF-8编码

        public int                    abTransferPolicy;                           // 是否携带传输策略参数
        public int      			  emTransferPolicy;                           // 传输策略, EM_TRANSFER_POLICY

        public int                    abSupportModeMaskConfig;                    // 是否携带违章掩码 
        public TRAFFIC_EVENT_CHECK_MASK stSupportModeMaskConfig;                   // 违章类型支持的检测模式掩码配置

        public int                    abIsEnableLightState;                       // 是否携带灯组状态
        public ENABLE_LIGHT_STATE_INFO stIsEnableLightState;                       // 交通全局配置对应图片命名格式参数配置
        
        public int                    abMixModeInfo;                              // 是否含有混合模式配置
        public MIX_MODE_CONFIG         stMixModeInfo;                              // 混合模式配置
    }
    
    // 手动抓拍参数
    public static class MANUAL_SNAP_PARAMETER extends Structure
    {
    	public int                   nChannel;               			// 抓图通道,从0开始
    	public byte[]                bySequence = new byte[64];	        // 抓图序列号字符串
    	public byte[]                byReserved = new byte[60];         // 保留字段
    }
    
    // 视频统计小计信息
    public static class NET_VIDEOSTAT_SUBTOTAL extends Structure
    {
    	 public int                 nTotal;                         // 设备运行后人数统计总数
         public int                 nHour;                          // 小时内的总人数
         public int                 nToday;                         // 当天的总人数
         public int                 nOSD;							// 统计人数,用于OSD显示, 可手动清除
         public byte[]              reserved = new byte[252];
    }

    // 视频统计摘要信息
    public static class NET_VIDEOSTAT_SUMMARY extends Structure
    {
    	public int                     nChannelID;                 	// 通道号
        public byte[]                  szRuleName = new byte[32];	// 规则名称
        public NET_TIME_EX             stuTime;                    	// 统计时间
        public NET_VIDEOSTAT_SUBTOTAL  stuEnteredSubtotal;         	// 进入小计
        public NET_VIDEOSTAT_SUBTOTAL  stuExitedSubtotal;          	// 出去小计
        public byte[]                  reserved = new byte[512];
    }

    // CLIENT_AttachVideoStatSummary 入参
    public static class NET_IN_ATTACH_VIDEOSTAT_SUM extends Structure
    {
    	 public int                   	dwSize;
         public int                     nChannel;                    // 视频通道号         
         public StdCallCallback   				cbVideoStatSum;              // 视频统计摘要信息回调, fVideoStatSumCallBack 回调
         public NativeLong              dwUser;                      // 用户数据                  
         
         public NET_IN_ATTACH_VIDEOSTAT_SUM()
         {
        	 this.dwSize = this.size();
         }
    }
    // CLIENT_AttachVideoStatSummary 出参
    public static class NET_OUT_ATTACH_VIDEOSTAT_SUM extends Structure
    {
    	public int 					dwSize;
    	
    	public NET_OUT_ATTACH_VIDEOSTAT_SUM()
    	{
    		this.dwSize = this.size();
    	}
   
    }

    // 接口(CLIENT_StartFindNumberStat)输入参数
    public static class NET_IN_FINDNUMBERSTAT extends Structure 
    {
        public int                 dwSize;                     // 此结构体大小
        public int                 nChannelID;                 // 要进行查询的通道号
        public NET_TIME            stStartTime;                // 开始时间 暂时精确到小时
        public NET_TIME            stEndTime;                  // 结束时间 暂时精确到小时
        public int                 nGranularityType;           // 查询粒度0:分钟,1:小时,2:日,3:周,4:月,5:季,6:年
        public int                 nWaittime;                  // 等待接收数据的超时时间
        public int                 nPlanID;                    // 计划ID,仅球机有效,从1开始
        
        public NET_IN_FINDNUMBERSTAT() {
        	this.dwSize = this.size();
        }
    }

    // 接口(CLIENT_StartFindNumberStat)输出参数
    public static class NET_OUT_FINDNUMBERSTAT extends Structure 
    {
        public int               dwSize;                     // 此结构体大小
        public int               dwTotalCount;               // 符合此次查询条件的结果总条数
        
        public NET_OUT_FINDNUMBERSTAT() {
        	this.dwSize = this.size();
		}
    }


    // 接口(CLIENT_DoFindNumberStat)输入参数
    public static class NET_IN_DOFINDNUMBERSTAT extends Structure 
    {
        public int               dwSize;                     // 此结构体大小
        public int        		 nBeginNumber;               // [0, totalCount-1], 查询起始序号,表示从beginNumber条记录开始,取count条记录返回; 
        public int        		 nCount;                     // 每次查询的流量统计条数
        public int               nWaittime;                  // 等待接收数据的超时时间            
        
        public NET_IN_DOFINDNUMBERSTAT() {
        	this.dwSize = this.size();
		}
    }

    public static class NET_NUMBERSTAT extends Structure 
    {
        public int      dwSize;
        public int      nChannelID;                           	  //统计通道号
        public byte[]   szRuleName = new byte[NET_CHAN_NAME_LEN]; //规则名称
        public NET_TIME stuStartTime;                        	  //开始时间
        public NET_TIME stuEndTime;                          	  //结束时间
        public int      nEnteredSubTotal;                    	  //进入人数小计
        public int      nExitedSubtotal;                     	  //出去人数小计
        public int      nAvgInside;                          	  //平均保有人数(除去零值)
        public int      nMaxInside;                         	  //最大保有人数
        public int      nEnteredWithHelmet;                		  //戴安全帽进入人数小计
        public int      nEnteredWithoutHelmet;                	  //不戴安全帽进入人数小计
        public int      nExitedWithHelmet;                        //戴安全帽出去人数小计
        public int      nExitedWithoutHelmet;                     //不戴安全帽出去人数小计
        
        public NET_NUMBERSTAT() {
        	this.dwSize = this.size();
        }
        
        public static class ByReference extends NET_NUMBERSTAT implements Structure.ByReference { }
    }

    // 接口(CLIENT_DoFindNumberStat)输出参数
    public static class NET_OUT_DOFINDNUMBERSTAT extends Structure 
    {
        public int                          dwSize;                // 此结构体大小
        public int                 			nCount;                // 查询返回人数统计信息个数
        public Pointer   					pstuNumberStat;        // 返回人数统计信息数组, NET_NUMBERSTAT 类型 
        public int                 			nBufferLen;            // 用户申请的内存大小,以NET_NUMBERSTAT中的dwsize大小为单位
    
        public NET_OUT_DOFINDNUMBERSTAT() {
        	this.dwSize = this.size();
        }
    }
    
    public static class CONNECT_STATE extends Structure
    {
        public static final int CONNECT_STATE_UNCONNECT = 0;
        public static final int CONNECT_STATE_CONNECTING = 1;
        public static final int CONNECT_STATE_CONNECTED = 2;
        public static final int CONNECT_STATE_ERROR = 255;
    }

    // 虚拟摄像头状态查询
    public static class NET_DEV_VIRTUALCAMERA_STATE_INFO extends Structure
    {
        public int 				 nStructSize;							 //结构体大小
        public int 				 nChannelID;							 //通道号
        public int 			 	 emConnectState;						 //连接状态, 取值范围为CONNECT_STATE中的值
        public int 				 uiPOEPort;								 //此虚拟摄像头所连接的POE端口号,0表示不是POE连接, 类型为unsigned int
        public byte[] 			 szDeviceName = new byte[64];			 //设备名称
        public byte[] 			 szDeviceType = new byte[128];			 //设备类型
        public byte[] 			 szSystemType = new byte[128];			 //系统版本
        public byte[] 			 szSerialNo = new byte[NET_SERIALNO_LEN];//序列号
        public int				 nVideoInput;							 //视频输入
        public int 				 nAudioInput;							 //音频输入
        public int     			 nAlarmOutput;							 //外部报警
        
        public NET_DEV_VIRTUALCAMERA_STATE_INFO()
        {
        	this.nStructSize = this.size();
        }
    }
    
    // 录像文件类型
    public static class NET_RECORD_TYPE extends Structure
    {
    	public final static int NET_RECORD_TYPE_ALL = 0; 		 // 所有录像
    	public final static int NET_RECORD_TYPE_NORMAL = 1; 	 // 普通录像
    	public final static int NET_RECORD_TYPE_ALARM = 2; 		 // 外部报警录像
    	public final static int NET_RECORD_TYPE_MOTION = 3; 	 // 动检报警录像
    }
    
    // 对讲方式
    public static class EM_USEDEV_MODE extends Structure
    {
    	public static final int NET_TALK_CLIENT_MODE 	  = 0;   // 设置客户端方式进行语音对讲
    	public static final int NET_TALK_SERVER_MODE 	  = 1;   // 设置服务器方式进行语音对讲
    	public static final int NET_TALK_ENCODE_TYPE 	  = 2;   // 设置语音对讲编码格式(对应 NETDEV_TALKDECODE_INFO)
    	public static final int NET_ALARM_LISTEN_MODE 	  = 3;   // 设置报警订阅方式
    	public static final int NET_CONFIG_AUTHORITY_MODE = 4;   // 设置通过权限进行配置管理
    	public static final int NET_TALK_TALK_CHANNEL 	  = 5;   // 设置对讲通道(0~MaxChannel-1)
    	public static final int NET_RECORD_STREAM_TYPE	  = 6;   // 设置待查询及按时间回放的录像码流类型(0-主辅码流,1-主码流,2-辅码流)  
    	public static final int NET_TALK_SPEAK_PARAM      = 7;   // 设置语音对讲喊话参数,对应结构体 NET_SPEAK_PARAM
    	public static final int NET_RECORD_TYPE           = 8;   // 设置按时间录像回放及下载的录像文件类型(详见  NET_RECORD_TYPE)
    	public static final int NET_TALK_MODE3            = 9;   // 设置三代设备的语音对讲参数, 对应结构体 NET_TALK_EX
    	public static final int NET_PLAYBACK_REALTIME_MODE = 10; // 设置实时回放功能(0-关闭,1开启)
    	public static final int NET_TALK_TRANSFER_MODE    = 11;  // 设置语音对讲是否为转发模式, 对应结构体 NET_TALK_TRANSFER_PARAM
    	public static final int NET_TALK_VT_PARAM         = 12;  // 设置VT对讲参数, 对应结构体 NET_VT_TALK_PARAM
    	public static final int NET_TARGET_DEV_ID         = 13;  // 设置目标设备标示符, 用以查询新系统能力(非0-转发系统能力消息)
    }
    
    // 语音编码类型
    public static class NET_TALK_CODING_TYPE extends Structure 
    {
    	public static final int NET_TALK_DEFAULT = 0;            // 无头PCM
    	public static final int NET_TALK_PCM = 1;                // 带头PCM
    	public static final int	NET_TALK_G711a = 2;              // G711a
    	public static final int NET_TALK_AMR = 3;                // AMR
    	public static final int	NET_TALK_G711u = 4;              // G711u
    	public static final int	NET_TALK_G726 = 5;               // G726
    	public static final int	NET_TALK_G723_53 = 6;            // G723_53
    	public static final int NET_TALK_G723_63 = 7;            // G723_63
    	public static final int	NET_TALK_AAC = 8;                // AAC
    	public static final int	NET_TALK_OGG = 9;                // OGG
    	public static final int	NET_TALK_G729 = 10;              // G729
    	public static final int	NET_TALK_MPEG2 = 11;             // MPEG2
    	public static final int	NET_TALK_MPEG2_Layer2 = 12;      // MPEG2-Layer2
    	public static final int	NET_TALK_G722_1 = 13;            // G.722.1
    	public static final int	NET_TALK_ADPCM = 21;             // ADPCM
		public static final int	NET_TALK_MP3   = 22;             // MP3
    }
   
    // 设备支持的语音对讲类型
    public static class NETDEV_TALKFORMAT_LIST extends Structure 
    {
    	public int 						 nSupportNum;                                                  				    // 个数
        public NETDEV_TALKDECODE_INFO[] type = (NETDEV_TALKDECODE_INFO[])new NETDEV_TALKDECODE_INFO().toArray(64);   // 编码类型
        
        public byte[] reserved = new byte[64];
    }
    
    // 语音编码信息  
    public static class NETDEV_TALKDECODE_INFO extends Structure 
    {
    	public int                 encodeType;                       // 编码类型, encodeType对应NET_TALK_CODING_TYPE
        public int                 nAudioBit;                        // 位数,如8或16, 目前只能是16
        public int                 dwSampleRate;                     // 采样率,如8000或16000, 目前只能是16000
        public int                 nPacketPeriod;                    // 打包周期, 单位ms, 目前只能是25
        public byte[]    		   reserved = new byte[60];    
    }
    
    // 语音对讲喊话参数
    public static class NET_SPEAK_PARAM extends Structure 
    {
    	public int 				  dwSize;                     		// 结构体大小
        public int 				  nMode;                      		// 0：对讲（默认模式）,1：喊话；从喊话切换到对讲要重新设置
        public int 				  nSpeakerChannel;           		// 扬声器通道号,喊话时有效
        public boolean 			  bEnableWait;            			// 开启对讲时是否等待设备的响应,默认不等待.TRUE:等待;FALSE:不等待
                                               						// 超时时间由CLIENT_SetNetworkParam设置,对应NET_PARAM的nWaittime字段
        public NET_SPEAK_PARAM()
	    {
	    	this.dwSize = this.size();
	    }
    }
    
    // 是否开启语音对讲的转发模式
    public static class NET_TALK_TRANSFER_PARAM extends Structure 
    {
    	public int 				 dwSize;
        public boolean 			 bTransfer;                 	   // 是否开启语音对讲转发模式, TRUE: 开启转发
        
        public NET_TALK_TRANSFER_PARAM()
	    {
	    	this.dwSize = this.size();
	    }
    }
    
    // 预览类型,对应CLIENT_RealPlayEx接口
    public static class NET_RealPlayType extends Structure
    {
    	public static final int NET_RType_Realplay   = 0; 		// 实时预览
    	public static final int NET_RType_Multiplay  = 1; 		// 多画面预览
    	public static final int NET_RType_Realplay_0 = 2; 		// 实时监视-主码流 ,等同于NET_RType_Realplay
    	public static final int NET_RType_Realplay_1 = 3; 		// 实时监视-从码流1
    	public static final int NET_RType_Realplay_2 = 4; 		// 实时监视-从码流2
    	public static final int NET_RType_Realplay_3 = 5; 		// 实时监视-从码流3    
    	public static final int NET_RType_Multiplay_1 = 6;		// 多画面预览－1画面
    	public static final int NET_RType_Multiplay_4 = 7; 		// 多画面预览－4画面
    	public static final int NET_RType_Multiplay_8 = 8; 		// 多画面预览－8画面
    	public static final int NET_RType_Multiplay_9 = 9; 		// 多画面预览－9画面
    	public static final int NET_RType_Multiplay_16 = 10; 	// 多画面预览－16画面
    	public static final int NET_RType_Multiplay_6 = 11;  	// 多画面预览－6画面
    	public static final int NET_RType_Multiplay_12 = 12; 	// 多画面预览－12画面
    	public static final int NET_RType_Multiplay_25 = 13;	// 多画面预览－25画面
    	public static final int NET_RType_Multiplay_36 = 14; 	// 多画面预览－36画面
    	public static final int NET_RType_Realplay_Test = 255;  // 带宽测试码流
    }
    
    // 回调视频数据帧的帧参数结构体
    public static class tagVideoFrameParam extends Structure
    {
        public byte                  encode;                 // 编码类型
        public byte                  frametype;              // I = 0, P = 1, B = 2...
        public byte                  format;                 // PAL - 0, NTSC - 1
        public byte                  size;                   // CIF - 0, HD1 - 1, 2CIF - 2, D1 - 3, VGA - 4, QCIF - 5, QVGA - 6 ,
                                                             // SVCD - 7,QQVGA - 8, SVGA - 9, XVGA - 10,WXGA - 11,SXGA - 12,WSXGA - 13,UXGA - 14,WUXGA - 15, LFT - 16, 720 - 17, 1080 - 18 ,1_3M-19
    												 		 // 2M-20, 5M-21;当size=255时，成员变量width,height 有效
        public int                   fourcc;                 // 如果是H264编码则总为0，否则值为*( DWORD*)"DIVX"，即0x58564944
        public short				 width;					 // 宽，单位是像素，当size=255时有效
    	public short				 height;				 // 高，单位是像素，当size=255时有效
        public NET_TIME              struTime;               // 时间信息
    }
    
    // 回调音频数据帧的帧参数结构体
    public static class tagCBPCMDataParam extends Structure 
    {
        public byte                channels;                // 声道数
        public byte                samples;                 // 采样 0 - 8000, 1 - 11025, 2 - 16000, 3 - 22050, 4 - 32000, 5 - 44100, 6 - 48000
        public byte                depth;                   // 采样深度 取值8或者16等。直接表示
        public byte                param1;                  // 0 - 指示无符号,1-指示有符号
        public int                 reserved;                // 保留
    }
    
    // 视频监视断开事件类型
    public static class EM_REALPLAY_DISCONNECT_EVENT_TYPE extends Structure 
    {
        public static final int DISCONNECT_EVENT_REAVE 		= 0;                 // 表示高级用户抢占低级用户资源
        public static final int DISCONNECT_EVENT_NETFORBID  = 1;                 // 禁止入网
        public static final int DISCONNECT_EVENT_SUBCONNECT = 2;                 // 动态子链接断开
    }
    
    // 电池电压过低报警
    public static class ALARM_BATTERYLOWPOWER_INFO extends Structure
    {
	    public int dwSize;			//结构体大小
	    public int nAction;			//0:开始1:停止
	    public int nBatteryLeft;	//剩余电量百分比,单位%
	    public NET_TIME stTime;		//事件发生时间
	    public int nChannelID;		//通道号,标识子设备电池,从0开始
	    
	    public ALARM_BATTERYLOWPOWER_INFO()
	    {
	    	this.dwSize = this.size();
	    }
    }
    
    // 温度过高报警
    public static class ALARM_TEMPERATURE_INFO extends Structure
    {
	    public int      dwSize;					//结构体大小
	    public byte[]   szSensorName = new byte[NET_MACHINE_NAME_NUM];//温度传感器名称
	    public int      nChannelID;			    //通道号
	    public int      nAction;				//0:开始1:停止
	    public float    fTemperature;			//当前温度值,单位摄氏度
	    public NET_TIME stTime;					//事件发生时间
	    
	    public ALARM_TEMPERATURE_INFO()
	    {
	    	this.dwSize = this.size();
	    }
    }
    
    // 普通报警信息
    public static class NET_CLIENT_STATE_EX extends Structure 
    {
        public int                channelcount;
        public int                alarminputcount;
        public byte[]             alarm       		= new byte[32];        // 外部报警
        public byte[]       	  motiondection 	= new byte[32];        // 动态检测
        public byte[]             videolost 		= new byte[32];        // 视频丢失
        public byte[]             bReserved 		= new byte[32];
    } 
   
    // 视频遮挡报警状态信息对应结构体
    public static class NET_CLIENT_VIDEOBLIND_STATE extends Structure
    {
	    public int dwSize;
	    public int channelcount;
	    public int[] dwAlarmState = new int[NET_MAX_CHANMASK];//每一个int按位表示32通道的报警状态,0-表示无报警,1-表示有报警
	    
	    public NET_CLIENT_VIDEOBLIND_STATE()
	    {
	    	this.dwSize = this.size();
	    }
    }
    
    // 视频丢失报警状态信息对应结构体
    public static class NET_CLIENT_VIDEOLOST_STATE extends Structure
    {
	    public int	    dwSize;
	    public int	    channelcount;
	    public int[]	dwAlarmState = new int[NET_MAX_CHANMASK];//每一个int按位表示32通道的报警状态（只有dwAlarmState[0]有效）,0-表示无报警,1-表示有报警
	    
	    public NET_CLIENT_VIDEOLOST_STATE()
	    {
	    	this.dwSize = this.size();
	    }
    }
    
    // 门禁开门 CLIENT_ControlDevice接口的 CTRLTYPE_CTRL_ACCESS_OPEN 命令参数
    public static class NET_CTRL_ACCESS_OPEN extends Structure {
    	public int          dwSize;
    	public int          nChannelID;            					  // 通道号(0开始)
        public Pointer      szTargetID;			    				  // 转发目标设备ID,为NULL表示不转发
        public byte[]       szUserID = new byte[MAX_COMMON_STRING_32];//远程用户ID
    	public int			emOpenDoorType;                			  // 开门方式, 参考 EM_OPEN_DOOR_TYPE
        
        public NET_CTRL_ACCESS_OPEN() {
        	this.dwSize = this.size();
        }
    }    
    
    // 门禁控制--开门方式
    public static class EM_OPEN_DOOR_TYPE extends Structure
    {
    	public static final int EM_OPEN_DOOR_TYPE_UNKNOWN = 0;
    	public static final int EM_OPEN_DOOR_TYPE_REMOTE = 1;			// 远程开门
    	public static final int EM_OPEN_DOOR_TYPE_LOCAL_PASSWORD = 2;	// 本地密码开门
    	public static final int EM_OPEN_DOOR_TYPE_LOCAL_CARD = 3;		// 本地刷卡开门
    	public static final int EM_OPEN_DOOR_TYPE_LOCAL_BUTTON = 4;		// 本地按钮开门
    }
    
    // 门禁关门CLIENT_ControlDevice接口的 CTRLTYPE_CTRL_ACCESS_CLOSE 命令参数
    public static class NET_CTRL_ACCESS_CLOSE extends Structure
    {
	    public int dwSize;
	    public int nChannelID;//通道号(0开始)
	    
        public NET_CTRL_ACCESS_CLOSE() {
        	this.dwSize = this.size();
        }
    }
    
	 // 门禁状态类型
	 public static class EM_NET_DOOR_STATUS_TYPE extends Structure
	 {
		 public static final int EM_NET_DOOR_STATUS_UNKNOWN=0;
		 public static final int EM_NET_DOOR_STATUS_OPEN=EM_NET_DOOR_STATUS_UNKNOWN+1; 	//门打开
		 public static final int EM_NET_DOOR_STATUS_CLOSE=EM_NET_DOOR_STATUS_OPEN+1; 	//门关闭
		 public static final int EM_NET_DOOR_STATUS_BREAK=EM_NET_DOOR_STATUS_CLOSE+1; 	//门异常打开
	 }

	 // 门禁状态信息(CLIENT_QueryDevState 接口输入参数)
	 public static class NET_DOOR_STATUS_INFO extends Structure
	 {
		 public int dwSize;
		 public int nChannel;//门禁通道号
		 public int emStateType;//门禁状态信息, 对应枚举EM_NET_DOOR_STATUS_TYPE
		 
    	public NET_DOOR_STATUS_INFO() {
    		this.dwSize = this.size();
    	}
	 }

    // 开启道闸参数(对应 CTRLTYPE_CTRL_OPEN_STROBE 命令)
    public static class NET_CTRL_OPEN_STROBE extends Structure 
    {
    	public int dwSize;
    	public int nChannelId;                      // 通道号 
    	public byte[] szPlateNumber = new byte[64]; // 车牌号码
 
    	public NET_CTRL_OPEN_STROBE() {
    		this.dwSize = this.size();
    	}
    }
    
    // 关闭道闸参数(对应  CTRLTYPE_CTRL_CLOSE_STROBE 命令)
    public static class NET_CTRL_CLOSE_STROBE extends Structure 
    {
    	public int	dwSize;
    	public int	nChannelId; // 通道号
    	
    	public NET_CTRL_CLOSE_STROBE() {
    		this.dwSize = this.size();
    	}
    }
    
    // 报警状态 (对应   CTRLTYPE_TRIGGER_ALARM_OUT 命令)
    public static class ALARMCTRL_PARAM extends Structure 
    {
    	public int	dwSize;
    	public int	nAlarmNo; // 报警通道号,从0开始
    	public int	nAction; // 1：触发报警,0：停止报警
    	
    	public ALARMCTRL_PARAM() {
    		this.dwSize = this.size();
    	}
    }
    
    // 查询 IVS 前端设备入参
    public static class NET_IN_IVS_REMOTE_DEV_INFO extends Structure 
    {
        public int                     dwSize;                         // 该结构体大小   
        public int                     nChannel;                       // 通道号
        
        public NET_IN_IVS_REMOTE_DEV_INFO() {
        	this.dwSize = this.size();
        }
    }
    
    // 查询 IVS 前端设备出参
    public static class NET_OUT_IVS_REMOTE_DEV_INFO extends Structure   
    {
        public int                   dwSize;                         	// 该结构体大小 
        public int     			     nPort;								// 端口
        public byte[]				 szIP = new byte[64];	            // 设备IP
        public byte[]				 szUser = new byte[64];	            // 用户名
    	public byte[]				 szPassword = new byte[64];         // 密码    
        public byte[]				 szAddress = new byte[128];	        // 机器部署地点
    
        public NET_OUT_IVS_REMOTE_DEV_INFO() {
        	this.dwSize = this.size();
        }
    }
    
    // 传感器感应方式枚举类型
    public static class NET_SENSE_METHOD extends Structure
    {
    	public static final int NET_SENSE_UNKNOWN = -1;//未知类型
    	public static final int NET_SENSE_DOOR	= 0; //门磁
    	public static final int NET_SENSE_PASSIVEINFRA = 1; //被动红外
    	public static final int NET_SENSE_GAS = 2; //气感
    	public static final int NET_SENSE_SMOKING = 3; //烟感
    	public static final int	NET_SENSE_WATER = 4; //水感
    	public static final int	NET_SENSE_ACTIVEFRA = 5; //主动红外
    	public static final int	NET_SENSE_GLASS = 6; //玻璃破碎
    	public static final int	NET_SENSE_EMERGENCYSWITCH = 7; //紧急开关
    	public static final int	NET_SENSE_SHOCK = 8; //震动
    	public static final int	NET_SENSE_DOUBLEMETHOD = 9; //双鉴(红外+微波)
    	public static final int	NET_SENSE_THREEMETHOD = 10; //三技术
    	public static final int	NET_SENSE_TEMP = 11; //温度
    	public static final int	NET_SENSE_HUMIDITY = 12; //湿度
    	public static final int	NET_SENSE_WIND = 13; //风速
    	public static final int	NET_SENSE_CALLBUTTON = 14; //呼叫按钮
    	public static final int	NET_SENSE_GASPRESSURE = 15; //气体压力
    	public static final int	NET_SENSE_GASCONCENTRATION = 16; //燃气浓度
    	public static final int	NET_SENSE_GASFLOW = 17; //气体流量
    	public static final int	NET_SENSE_OTHER = 18; //其他
    	public static final int	NET_SENSE_OIL = 19; //油量检测,汽油、柴油等车辆用油检测
    	public static final int	NET_SENSE_MILEAGE = 20; //里程数检测
    	public static final int	NET_SENSE_URGENCYBUTTON = 21; //紧急按钮
    	public static final int	NET_SENSE_STEAL = 22; //盗窃
    	public static final int	NET_SENSE_PERIMETER = 23; //周界
    	public static final int	NET_SENSE_PREVENTREMOVE = 24; //防拆
    	public static final int	NET_SENSE_DOORBELL = 25; //门铃
    	public static final int	NET_SENSE_ALTERVOLT = 26; //交流电压传感器
    	public static final int	NET_SENSE_DIRECTVOLT = 27; //直流电压传感器
    	public static final int	NET_SENSE_ALTERCUR = 28; //交流电流传感器
    	public static final int	NET_SENSE_DIRECTCUR = 29; //直流电流传感器
    	public static final int	NET_SENSE_RSUGENERAL = 30; //高新兴通用模拟量4~20mA或0~5V
    	public static final int	NET_SENSE_RSUDOOR = 31; //高新兴门禁感应
    	public static final int	NET_SENSE_RSUPOWEROFF = 32; //高新兴断电感应
    	public static final int	NET_SENSE_TEMP1500 = 33;//1500温度传感器
    	public static final int	NET_SENSE_TEMPDS18B20 = 34;//DS18B20温度传感器
    	public static final int	NET_SENSE_HUMIDITY1500 = 35; //1500湿度传感器
    	public static final int	NET_SENSE_NUM = 36; //枚举类型总数
    }
    
    //-------------------------------报警属性---------------------------------
	// 云台联动
	public static class NET_PTZ_LINK extends Structure
	{
		public int iType;//0-None,1-Preset,2-Tour,3-Pattern
		public int iValue;
	}

	////////////////////////////////HDVR专用//////////////////////////////////
    // 报警联动扩展结构体
    public static class NET_MSG_HANDLE_EX extends Structure
    {
        /* 消息处理方式,可以同时多种处理方式,包括
         * 0x00000001 - 报警上传
         * 0x00000002 - 联动录象
         * 0x00000004 - 云台联动
         * 0x00000008 - 发送邮件
         * 0x00000010 - 本地轮巡
         * 0x00000020 - 本地提示
         * 0x00000040 - 报警输出
         * 0x00000080 - Ftp上传
         * 0x00000100 - 蜂鸣
         * 0x00000200 - 语音提示
         * 0x00000400 - 抓图
        */
		/*当前报警所支持的处理方式,按位掩码表示*/
		public int dwActionMask;
		/*触发动作,按位掩码表示,具体动作所需要的参数在各自的配置中体现*/
		public int dwActionFlag;
		/*报警触发的输出通道,报警触发的输出,为1表示触发该输出*/
		public byte[] byRelAlarmOut = new byte[NET_MAX_ALARMOUT_NUM_EX];
		public int dwDuration;/*报警持续时间*/
		/*联动录象*/
		public byte[] byRecordChannel = new byte[NET_MAX_VIDEO_IN_NUM_EX];/*报警触发的录象通道,为1表示触发该通道*/
		public int dwRecLatch;/*录象持续时间*/
		/*抓图通道*/
		public byte[] bySnap = new byte[NET_MAX_VIDEO_IN_NUM_EX];
		/*轮巡通道*/
		public byte[] byTour = new byte[NET_MAX_VIDEO_IN_NUM_EX];/*轮巡通道0-31路*/
		/*云台联动*/
		public NET_PTZ_LINK[] struPtzLink = (NET_PTZ_LINK[])new NET_PTZ_LINK().toArray(NET_MAX_VIDEO_IN_NUM_EX);
		public int dwEventLatch;/*联动开始延时时间,s为单位,范围是0~15,默认值是0*/
		/*报警触发的无线输出通道,报警触发的输出,为1表示触发该输出*/
		public byte[] byRelWIAlarmOut = new byte[NET_MAX_ALARMOUT_NUM_EX];
		public byte bMessageToNet;
		public byte bMMSEn;/*短信报警使能*/
		public byte bySnapshotTimes;/*短信发送抓图张数*/
		public byte bMatrixEn;/*!<矩阵使能*/
		public int dwMatrix;/*!<矩阵掩码*/
		public byte bLog;/*!<日志使能,目前只有在WTN动态检测中使用*/
		public byte bSnapshotPeriod;/*!<抓图帧间隔,每隔多少帧抓一张图片,一定时间内抓拍的张数还与抓图帧率有关。0表示不隔帧,连续抓拍。*/
		public byte[] byTour2 = new byte[NET_MAX_VIDEO_IN_NUM_EX];/*轮巡通道32-63路*/
		public byte byEmailType;/*<0,图片附件,1,录像附件>*/
		public byte byEmailMaxLength;/*<附件录像时的最大长度,单位MB>*/
		public byte byEmailMaxTime;/*<附件是录像时最大时间长度,单位秒>*/
		public byte[] byReserved = new byte[475];
    }

    public static class EM_NET_DEFENCE_AREA_TYPE extends Structure
    {
    	public static final int EM_NET_DEFENCE_AREA_TYPE_UNKNOW = 0; //未知
    	public static final int EM_NET_DEFENCE_AREA_TYPE_INTIME = 1; //即时防区
    	public static final int EM_NET_DEFENCE_AREA_TYPE_DELAY = 2; //延时防区
    	public static final int EM_NET_DEFENCE_AREA_TYPE_FULLDAY = 3; //24小时防区
    	public static final int EM_NET_DEFENCE_AREA_TYPE_Follow = 4; //跟随防区
    	public static final int EM_NET_DEFENCE_AREA_TYPE_MEDICAL = 5; //医疗紧急防区
    	public static final int EM_NET_DEFENCE_AREA_TYPE_PANIC = 6; //恐慌防区
    	public static final int EM_NET_DEFENCE_AREA_TYPE_FIRE = 7; //火警防区
    	public static final int EM_NET_DEFENCE_AREA_TYPE_FULLDAYSOUND = 8; //24小时有声防区
    	public static final int EM_NET_DEFENCE_AREA_TYPE_FULLDATSLIENT = 9; //24小时无声防区
    	public static final int EM_NET_DEFENCE_AREA_TYPE_ENTRANCE1 = 10; //出入防区1
    	public static final int EM_NET_DEFENCE_AREA_TYPE_ENTRANCE2 = 11; //出入防区2
    	public static final int EM_NET_DEFENCE_AREA_TYPE_INSIDE = 12; //内部防区
    	public static final int EM_NET_DEFENCE_AREA_TYPE_OUTSIDE = 13; //外部防区
    	public static final int EN_NET_DEFENCE_AREA_TYPE_PEOPLEDETECT = 14; //人员检测防区
    }

    // 本地报警事件(对NET_ALARM_ALARM_EX升级)
    public static class ALARM_ALARM_INFO_EX2 extends Structure
    {
    	public int dwSize;
    	public int nChannelID;						//通道号
    	public int nAction;							//0:开始, 1:停止
    	public NET_TIME stuTime;					//报警事件发生的时间
    	public int emSenseType;						//传感器类型, 取值范围为  NET_SENSE_METHOD 中的值
    	public NET_MSG_HANDLE_EX stuEventHandler;	//联动信息
    	public int emDefenceAreaType;				//防区类型, 取值类型为EM_NET_DEFENCE_AREA_TYPE中的值
    	public int nEventID;					    //事件ID
        public byte[]     szName = new byte[NET_COMMON_STRING_32];  // 通道名称               
        public int nCount;                 			// 事件发生次数
        
    	public ALARM_ALARM_INFO_EX2() {
    		this.dwSize = this.size();
    	}
    }
    
    // 布撤防状态变化事件的信息
    public static class ALARM_ARMMODE_CHANGE_INFO extends Structure
    {
        public int                 dwSize;
        public NET_TIME            stuTime;        // 报警事件发生的时间
        public int                 bArm;           // 变化后的状态,对应  NET_ALARM_MODE
        public int 			       emSceneMode;    // 情景模式，对应  NET_SCENE_MODE
        public int                 dwID;           // ID号, 遥控器编号或键盘地址, emTriggerMode为NET_EM_TRIGGER_MODE_NET类型时为0
        public int       		   emTriggerMode;  // 触发方式,对应  NET_EM_TRIGGER_MODE
        
        public ALARM_ARMMODE_CHANGE_INFO() {
        	this.dwSize = this.size();
        }
    }
    
    // 布撤防模式
    public static class NET_ALARM_MODE extends Structure 
    {
        public static final int NET_ALARM_MODE_UNKNOWN    = -1;          // 未知
        public static final int NET_ALARM_MODE_DISARMING  = 0;           // 撤防
        public static final int NET_ALARM_MODE_ARMING	  = 1;           // 布防
        public static final int NET_ALARM_MODE_FORCEON	  = 2;           // 强制布防
        public static final int NET_ALARM_MODE_PARTARMING = 3;           // 部分布防
    }
    
    // 布撤防场景模式
    public static class NET_SCENE_MODE extends Structure 
    {
    	public static final int NET_SCENE_MODE_UNKNOWN   = 0;            // 未知场景
    	public static final int NET_SCENE_MODE_OUTDOOR   = 1;            // 外出模式
    	public static final int NET_SCENE_MODE_INDOOR    = 2;            // 室内模式
    	public static final int NET_SCENE_MODE_WHOLE     = 3;            // 全局模式
    	public static final int NET_SCENE_MODE_RIGHTNOW  = 4;            // 立即模式
    	public static final int NET_SCENE_MODE_SLEEPING  = 5;            // 就寝模式
    	public static final int NET_SCENE_MODE_CUSTOM    = 6;            // 自定义模式
    }
    
    // 触发方式
    public static class NET_EM_TRIGGER_MODE extends Structure 
    { 
    	public static final int NET_EM_TRIGGER_MODE_UNKNOWN 		= 0;
    	public static final int NET_EM_TRIGGER_MODE_NET			    = 1;   // 网络用户(平台或Web)
    	public static final int NET_EM_TRIGGER_MODE_KEYBOARD		= 2;   // 键盘
    	public static final int NET_EM_TRIGGER_MODE_REMOTECONTROL	= 3;   // 遥控器
    }
    
    // 紧急救助事件详情
    public static class ALARM_RCEMERGENCY_CALL_INFO extends Structure 
    {
    	public int                       dwSize;
	    public int                       nAction;                // -1:未知 0:开始 1:停止
	    public int                  	 emType;                 // 紧急类型,对应 EM_RCEMERGENCY_CALL_TYPE
	    public NET_TIME                  stuTime;                // 事件发生时间
	    public int   					 emMode;                 // 报警方式，对应 EM_RCEMERGENCY_MODE_TYPE
	    public int                       dwID;                   // 用于标示不同的紧急事件(只有emMode是遥控器类型时有效, 表示遥控器的编号, 0表示无效ID)
	    
	    public ALARM_RCEMERGENCY_CALL_INFO() {
	    	this.dwSize = this.size();
	    }
    }
    
    // 紧急救助事件类型
    public static class EM_RCEMERGENCY_CALL_TYPE extends Structure 
    {
    	public static final int EM_RCEMERGENCY_CALL_UNKNOWN   = 0;
    	public static final int EM_RCEMERGENCY_CALL_FIRE	  = 1;             // 火警
    	public static final int EM_RCEMERGENCY_CALL_DURESS	  = 2;             // 胁迫
    	public static final int EM_RCEMERGENCY_CALL_ROBBER	  = 3;             // 匪警
    	public static final int EM_RCEMERGENCY_CALL_MEDICAL	  = 4;             // 医疗
    	public static final int EM_RCEMERGENCY_CALL_EMERGENCY = 5;             // 紧急
    }
    
    // 报警方式
    public static class EM_RCEMERGENCY_MODE_TYPE extends Structure 
    {
    	public static final int EM_RCEMERGENCY_MODE_UNKNOWN          = 0;
    	public static final int EM_RCEMERGENCY_MODE_KEYBOARD		 = 1;       // 键盘
    	public static final int EM_RCEMERGENCY_MODE_WIRELESS_CONTROL = 2;       // 遥控器
    }
    
    /////////////////////////////////////////////////////
    ////////用户信息管理对应接口CLIENT_QueryUserInfoNew/////////
    // 用户信息表
    public static class USER_MANAGE_INFO_NEW extends Structure {
    	public int 						dwSize; 																				 // 结构体大小
    	public int 						dwRightNum;  																			 // 权限信息有效个数
    	public OPR_RIGHT_NEW[] 			rightList = (OPR_RIGHT_NEW[])new OPR_RIGHT_NEW().toArray(NET_NEW_MAX_RIGHT_NUM); 		 // 权限信息，有效个数由 dwRightNum 成员决定, 用户权限个数上限NET_NEW_MAX_RIGHT_NUM = 1024
    	public int 						dwGroupNum; 																			 // 用户组信息有效个数
    	public USER_GROUP_INFO_NEW[] 	groupList = (USER_GROUP_INFO_NEW[])new USER_GROUP_INFO_NEW().toArray(NET_MAX_GROUP_NUM);  // 用户组信息，此参数废弃，请使用groupListEx
    	public int 						dwUserNum;  																			  // 用户数
    	public USER_INFO_NEW[] 			userList = (USER_INFO_NEW[])new USER_INFO_NEW().toArray(NET_MAX_USER_NUM); 				  // 用户列表， 用户个数上限NET_MAX_USER_NUM=200
    	public int 						dwFouctionMask; 																		  // 掩码： 0x00000001 - 支持用户复用， 0x00000002 - 密码修改需要校验
    	public byte 					byNameMaxLength;  																		  // 支持的用户名最大长度
    	public byte 					byPSWMaxLength; 																		  // 支持的密码最大长度
    	public byte[] 					byReserve = new byte[254];
    	public USER_GROUP_INFO_EX2[]    groupListEx = (USER_GROUP_INFO_EX2[])new USER_GROUP_INFO_EX2().toArray(NET_MAX_GROUP_NUM); // 用户组信息扩展, 用户组个数上限NET_MAX_GROUP_NUM=20
    	
    	public USER_MANAGE_INFO_NEW() {
    		this.dwSize = this.size();
    	}  	
    }
    
    // 权限信息
    public static class OPR_RIGHT_NEW extends Structure {
    	public int 						dwSize;										  //结构体大小
    	public int 						dwID; 										  //权限ID，每个 权限都有各自的ID
    	public byte[] 					name = new byte[NET_RIGHT_NAME_LENGTH]; 	  //名称 权限名长度 NET_RIGHT_NAME_LENGTH=32
    	public byte[] 					memo = new byte[NET_MEMO_LENGTH];			  //说明备注长度NET_MEMO_LENGTH=32
    	
    	public OPR_RIGHT_NEW() {
    		this.dwSize = this.size();
    	}
    }
    
    // 用户组信息
    public static class USER_GROUP_INFO_NEW extends Structure {
    	public int 						dwSize;
        public int 						dwID; 											// 用户组ID， 每个用户组都有各自的ID
        public byte[] 					name = new byte[NET_USER_NAME_LENGTH_EX]; 		// 用户组名称/NET_USER_NAME_LENGTH_EX=16
        public int 						dwRightNum; 									// 用户组权限有效个数
        public int[] 					rights = new int[NET_NEW_MAX_RIGHT_NUM];		// 用户组支持权限数组
        public byte[] 					memo = new byte[NET_MEMO_LENGTH]; 				// 用户组备注说明
        public USER_GROUP_INFO_NEW() {
    		this.dwSize = this.size();
    	}
    }
    
    // 用户组信息扩展，用户组名加长
    public static class USER_GROUP_INFO_EX2 extends Structure {
    	public int 						 dwSize; 										// 结构体大小
        public int 						 dwID; 											// ID
        public byte[] 					 name = new byte[NET_NEW_USER_NAME_LENGTH];     // 用户名 长度NET_NEW_USER_NAME_LENGTH=128
        public int 						 dwRightNum;  									// 权限数量
        public int[] 					 rights = new int[NET_NEW_MAX_RIGHT_NUM]; 		// 用户权限 个数上限 NET_NEW_MAX_RIGHT_NUM = 1024
        public byte[]					 memo = new byte[NET_MEMO_LENGTH]; 				// 说明， 备注长度NET_MEMO_LENGTH=32
        
        public USER_GROUP_INFO_EX2() {
    		this.dwSize = this.size();
    	}
    }
    
    // 用户信息结构体
    public static class USER_INFO_NEW extends Structure {
    	public int 						dwSize; 									   // 结构体大小
        public int 						dwID; 										   // 用户ID
        public int 						dwGroupID; 									   // 用户组组ID
        public byte[] 					name = new byte[NET_NEW_USER_NAME_LENGTH];     // 用户名称，长度NET_NEW_USER_NAME_LENGTH=128
        public byte[] 					passWord = new byte[NET_NEW_USER_PSW_LENGTH];  // 用户密码，NET_NEW_USER_PSW_LENGTH=128
        public int 						dwRightNum;  								   // 用户权限有效个数
        public int[] 					rights = new int[NET_NEW_MAX_RIGHT_NUM];       // 用户支持权限数组，个数上限 NET_NEW_MAX_RIGHT_NUM = 1024
        public byte[] 					memo = new byte[NET_MEMO_LENGTH]; 			   // 用户备注说明， 备注长度NET_MEMO_LENGTH=32
        public int 						dwFouctionMask;          					   // 掩码,0x00000001 - 支持用户复用
        public NET_TIME 				stuTime;           							   // 最后修改时间
        public byte 					byIsAnonymous;         					       // 是否可以匿名登录, 0:不可匿名登录, 1: 可以匿名登录
        public byte[] 					byReserve = new byte[7];  					   // 保留字节
        
        public USER_INFO_NEW() {
    		this.dwSize = this.size();
    	}
    }
    
    
    //------------------------白名单相关结构体-------------------------
    // CLIENT_FindRecord接口输入参数
    public static class NET_IN_FIND_RECORD_PARAM extends Structure {
        public int                       dwSize;          							 // 结构体大小
        public int                       emType;          							 // 待查询记录类型,emType对应  EM_NET_RECORD_TYPE
        public Pointer                   pQueryCondition;							 // 查询类型对应的查询条件 =1时，是白名单账户记录, 查询条件对应 FIND_RECORD_TRAFFICREDLIST_CONDITION 结构体,记录信息对应 NET_TRAFFIC_LIST_RECORD 结构体    
        
        public NET_IN_FIND_RECORD_PARAM() {
        	this.dwSize = this.size();
        }
    }
    
    // 交通黑白名单账户记录查询条件
    public static class FIND_RECORD_TRAFFICREDLIST_CONDITION extends Structure {
    	public int          dwSize;
        public byte[]       szPlateNumber = new byte[NET_MAX_PLATE_NUMBER_LEN];      // 车牌号
        public byte[]       szPlateNumberVague = new byte[NET_MAX_PLATE_NUMBER_LEN]; // 车牌号码模糊查询
        public int          nQueryResultBegin;                          			 // 第一个条返回结果在查询结果中的偏移量 
        public boolean      bRapidQuery;       										 // 是否快速查询, TRUE:为快速,快速查询时不等待所有增、删、改操作完成。默认为非快速查询
        
        public FIND_RECORD_TRAFFICREDLIST_CONDITION() {
        	this.dwSize = this.size();
        }
    }
    
    // 交通流量记录查询条件
    public static class FIND_RECORD_TRAFFICFLOW_CONDITION extends Structure {
    	public int                     dwSize;
    	public int                     abChannelId;                      // 通道号查询条件是否有效     
    	public int                     nChannelId;                       // 通道号
    	public int                     abLane;                           // 车道号查询条件是否有效
    	public int                     nLane;                            // 车道号
    	public int                     bStartTime;                       // 开始时间查询条件是否有效   
    	public NET_TIME                stStartTime;                      // 开始时间
    	public int					   bEndTime;                         // 结束时间查询条件是否有效
    	public NET_TIME                stEndTime;                        // 结束时间 
        public int                     bStatisticsTime;                  // 查询是否为统计时间,为BOOL类型，bStartTime及bEndTime均为1
    	public FIND_RECORD_TRAFFICFLOW_CONDITION() {
    		this.dwSize = this.size();
    	}
    }
    
    // 门禁出入记录查询条件
    public static class FIND_RECORD_ACCESSCTLCARDREC_CONDITION_EX extends Structure {
    	public int                     dwSize;
    	public int                     bCardNoEnable;                    // 启用卡号查询, 为BOOL类型
        public byte[]                  szCardNo = new byte[NET_MAX_CARDNO_LEN];      // 卡号
        public int                     bTimeEnable;                      // 启用时间段查询, 为BOOL类型
        public NET_TIME                stStartTime;                      // 起始时间
        public NET_TIME                stEndTime;                        // 结束时间
        
        public FIND_RECORD_ACCESSCTLCARDREC_CONDITION_EX() {
        	this.dwSize = this.size();
        }
    }
    
    // 开门方式(门禁事件,门禁出入记录,实际的开门方式)
    public static class NET_ACCESS_DOOROPEN_METHOD {
        public static final int NET_ACCESS_DOOROPEN_METHOD_UNKNOWN = 0;
        public static final int NET_ACCESS_DOOROPEN_METHOD_PWD_ONLY = 1;                    // 密码开锁
        public static final int NET_ACCESS_DOOROPEN_METHOD_CARD = 2;                        // 刷卡开锁
        public static final int NET_ACCESS_DOOROPEN_METHOD_CARD_FIRST = 3;                  // 先刷卡后密码开锁
        public static final int NET_ACCESS_DOOROPEN_METHOD_PWD_FIRST = 4;                   // 先密码后刷卡开锁
        public static final int NET_ACCESS_DOOROPEN_METHOD_REMOTE = 5;                      // 远程开锁,如通过室内机或者平台对门口机开锁
        public static final int NET_ACCESS_DOOROPEN_METHOD_BUTTON = 6;                      // 开锁按钮进行开锁
        public static final int NET_ACCESS_DOOROPEN_METHOD_FINGERPRINT = 7;                 // 指纹开锁
        public static final int NET_ACCESS_DOOROPEN_METHOD_PWD_CARD_FINGERPRINT = 8;        // 密码+刷卡+指纹组合开锁
        public static final int NET_ACCESS_DOOROPEN_METHOD_PWD_FINGERPRINT = 10;	        // 密码+指纹组合开锁
        public static final int NET_ACCESS_DOOROPEN_METHOD_CARD_FINGERPRINT = 11;	       	// 刷卡+指纹组合开锁
        public static final int NET_ACCESS_DOOROPEN_METHOD_PERSONS = 12;                	// 多人开锁
        public static final int NET_ACCESS_DOOROPEN_METHOD_KEY = 13;                    	// 钥匙开门
        public static final int NET_ACCESS_DOOROPEN_METHOD_COERCE_PWD = 14;             	// 胁迫密码开门
        public static final int NET_ACCESS_DOOROPEN_METHOD_FACE_RECOGNITION = 16;       	// 人脸识别开门
    }
    
    // 卡类型
    public static class NET_ACCESSCTLCARD_TYPE {
        public static final int NET_ACCESSCTLCARD_TYPE_UNKNOWN = -1;
        public static final int NET_ACCESSCTLCARD_TYPE_GENERAL = 0;                     // 一般卡
        public static final int NET_ACCESSCTLCARD_TYPE_VIP     = 1;                     // VIP卡
        public static final int NET_ACCESSCTLCARD_TYPE_GUEST   = 2;                     // 来宾卡
        public static final int NET_ACCESSCTLCARD_TYPE_PATROL  = 3;                     // 巡逻卡
        public static final int NET_ACCESSCTLCARD_TYPE_BLACKLIST = 4;                   // 黑名单卡
        public static final int NET_ACCESSCTLCARD_TYPE_CORCE	= 5;                    // 胁迫卡
        public static final int NET_ACCESSCTLCARD_TYPE_POLLING  = 6;                    // 巡检卡
        public static final int NET_ACCESSCTLCARD_TYPE_MOTHERCARD = 0xff;           	// 母卡
    }
    
    // 门禁刷卡记录记录集信息
    public static class NET_RECORDSET_ACCESS_CTL_CARDREC extends Structure {
    	public int           		dwSize;
        public int             		nRecNo;                                 // 记录集编号,只读
        public byte[]            	szCardNo = new byte[NET_MAX_CARDNO_LEN];// 卡号
        public byte[]            	szPwd = new byte[NET_MAX_CARDPWD_LEN];  // 密码
        public NET_TIME        		stuTime;                                // 刷卡时间
        public int            		bStatus;                                // 刷卡结果,TRUE表示成功,FALSE表示失败
        public int    				emMethod;                 				// 开门方式 NET_ACCESS_DOOROPEN_METHOD
        public int             		nDoor;                                  // 门号,即CFG_CMD_ACCESS_EVENT配置CFG_ACCESS_EVENT_INFO的数组下标
        public byte[]            	szUserID = new byte[NET_MAX_USERID_LEN]; // 用户ID
        public int             		nReaderID;                              // 读卡器ID (废弃,不再使用)
    	public byte[]				szSnapFtpUrl = new byte[MAX_PATH];		// 开锁抓拍上传的FTP地址
    	public byte[]            	szReaderID = new byte[NET_COMMON_STRING_32];// 读卡器ID													// 开门并上传抓拍照片,在记录集记录存储地址,成功才有
        public int       			emCardType;                 // 卡类型 NET_ACCESSCTLCARD_TYPE
        public int                  nErrorCode;                 // 开门失败的原因,仅在bStatus为FALSE时有效
                                                                // 0x00 没有错误
                                                                // 0x10 未授权
                                                                // 0x11 卡挂失或注销
                                                                // 0x12 没有该门权限
                                                                // 0x13 开门模式错误
                                                                // 0x14 有效期错误
                                                                // 0x15 防反潜模式
                                                                // 0x16 胁迫报警未打开
                                                                // 0x17 门常闭状态
                                                                // 0x18 AB互锁状态
                                                                // 0x19 巡逻卡
                                                                // 0x1A 设备处于闯入报警状态
                                                                // 0x20 时间段错误
                                                                // 0x21 假期内开门时间段错误
                                                                // 0x30 需要先验证有首卡权限的卡片
                                                                // 0x40 卡片正确,输入密码错误
                                                                // 0x41 卡片正确,输入密码超时
                                                                // 0x42 卡片正确,输入指纹错误
                                                                // 0x43 卡片正确,输入指纹超时
                                                                // 0x44 指纹正确,输入密码错误
                                                                // 0x45 指纹正确,输入密码超时
                                                                // 0x50 组合开门顺序错误
                                                                // 0x51 组合开门需要继续验证
                                                                // 0x60 验证通过,控制台未授权
        public byte[]           	szRecordURL = new byte[NET_COMMON_STRING_128];// 刷卡录像的地址
        public int            		nNumbers;                   // 抓图的张数
    	public int  				emAttendanceState;          // 考勤状态 ,参考  NET_ATTENDANCESTATE
        public int   				emDirection;                // 开门方向, 参考  NET_ENUM_DIRECTION_ACCESS_CTL
        public NET_RECORDSET_ACCESS_CTL_CARDREC() {
        	this.dwSize = this.size();
        }
    }
    
    //考勤状态
    public static class NET_ATTENDANCESTATE extends Structure {
        public static final int NET_ATTENDANCESTATE_UNKNOWN = 0;
        public static final int NET_ATTENDANCESTATE_SIGNIN	= 1;                   //签入
        public static final int NET_ATTENDANCESTATE_GOOUT   = 2;                   //外出
        public static final int NET_ATTENDANCESTATE_GOOUT_AND_RETRUN = 3;          //外出归来
        public static final int NET_ATTENDANCESTATE_SIGNOUT = 4;                   // 签出
        public static final int NET_ATTENDANCESTATE_WORK_OVERTIME_SIGNIN = 5;      // 加班签到
        public static final int NET_ATTENDANCESTATE_WORK_OVERTIME_SIGNOUT = 6;     // 加班签出
    }
    
    // 开门方向
    public static class NET_ENUM_DIRECTION_ACCESS_CTL extends Structure {
    	public static final int NET_ENUM_DIRECTION_UNKNOWN = 0; 
    	public static final int NET_ENUM_DIRECTION_ENTRY   = 1;                     // 进门             
    	public static final int NET_ENUM_DIRECTION_EXIT    = 2;                     // 出门
    }
   
    // 记录集类型
    public static class EM_NET_RECORD_TYPE extends Structure {
        public static final int NET_RECORD_UNKNOWN = 0;
        public static final int NET_RECORD_TRAFFICREDLIST = 1; 					 // 交通白名单账户记录, 查询条件对应 FIND_RECORD_TRAFFICREDLIST_CONDITION 结构体,记录信息对应 NET_TRAFFIC_LIST_RECORD 结构体    
        public static final int NET_RECORD_TRAFFICBLACKLIST = 2;  				 // 交通黑名单账号记录,查询条件对应 FIND_RECORD_TRAFFICREDLIST_CONDITION 结构体,记录信息对应 NET_TRAFFIC_LIST_RECORD 结构体       
        public static final int NET_RECORD_BURN_CASE = 3;      					 // 刻录案件记录,查询条件对应 FIND_RECORD_BURN_CASE_CONDITION 结构体,记录信息对应 NET_BURN_CASE_INFO 结构体
        public static final int NET_RECORD_ACCESSCTLCARD = 4;  					 // 门禁卡,查询条件对应 FIND_RECORD_ACCESSCTLCARD_CONDITION 结构体,记录信息对应 NET_RECORDSET_ACCESS_CTL_CARD 结构体
        public static final int NET_RECORD_ACCESSCTLPWD = 5;      				 // 门禁密码,查询条件对应 FIND_RECORD_ACCESSCTLPWD_CONDITION 结构体,记录信息对应 NET_RECORDSET_ACCESS_CTL_PWD
        public static final int NET_RECORD_ACCESSCTLCARDREC = 6; 				 // 门禁出入记录（必须同时按卡号和时间段查询,建议用 NET_RECORD_ACCESSCTLCARDREC_EX 查询）,查询条件对应 FIND_RECORD_ACCESSCTLCARDREC_CONDITION 结构体,记录信息对应 NET_RECORDSET_ACCESS_CTL_CARDREC 结构体 
        public static final int NET_RECORD_ACCESSCTLHOLIDAY = 7; 				 // 假日记录集,查询条件对应 FIND_RECORD_ACCESSCTLHOLIDAY_CONDITION 结构体,记录信息对应 NET_RECORDSET_HOLIDAY 结构体
        public static final int NET_RECORD_TRAFFICFLOW_STATE = 8;  				 // 查询交通流量记录,查询条件对应 FIND_RECORD_TRAFFICFLOW_CONDITION 结构体,记录信息对应 NET_RECORD_TRAFFIC_FLOW_STATE 结构体
        public static final int NET_RECORD_VIDEOTALKLOG = 9;    				 // 通话记录,查询条件对应 FIND_RECORD_VIDEO_TALK_LOG_CONDITION 结构体,记录信息对应 NET_RECORD_VIDEO_TALK_LOG 结构体
        public static final int NET_RECORD_REGISTERUSERSTATE = 10;  			 // 状态记录,查询条件对应 FIND_RECORD_REGISTER_USER_STATE_CONDITION 结构体,记录信息对应 NET_RECORD_REGISTER_USER_STATE 结构体
        public static final int NET_RECORD_VIDEOTALKCONTACT = 11;  				 // 联系人记录,查询条件对应 FIND_RECORD_VIDEO_TALK_CONTACT_CONDITION 结构体,记录信息对应 NET_RECORD_VIDEO_TALK_CONTACT 结构体
        public static final int NET_RECORD_ANNOUNCEMENT = 12;					 // 公告记录,查询条件对应 FIND_RECORD_ANNOUNCEMENT_CONDITION 结构体,记录信息对应 NET_RECORD_ANNOUNCEMENT_INFO 结构体    														
        public static final int NET_RECORD_ALARMRECORD = 13; 					 // 报警记录,查询条件对应 FIND_RECORD_ALARMRECORD_CONDITION 结构体,记录信息对应 NET_RECORD_ALARMRECORD_INFO 结构体
        public static final int NET_RECORD_COMMODITYNOTICE = 14;  				 // 下发商品记录,查询条件对应 FIND_RECORD_COMMODITY_NOTICE_CONDITION 结构体,记录信息对应 NET_RECORD_COMMODITY_NOTICE 结构体                                                          
        public static final int NET_RECORD_HEALTHCARENOTICE = 15;  				 // 就诊信息记录,查询条件对应 FIND_RECORD_HEALTH_CARE_NOTICE_CONDITION 结构体,记录信息对应 NET_RECORD_HEALTH_CARE_NOTICE 结构体
        public static final int NET_RECORD_ACCESSCTLCARDREC_EX = 16; 			 // 门禁出入记录(可选择部分条件查询,建议替代NET_RECORD_ACCESSCTLCARDREC),查询条件对应 FIND_RECORD_ACCESSCTLCARDREC_CONDITION_EX 结构体,记录信息对应 NET_RECORDSET_ACCESS_CTL_CARDREC 结构体
        public static final int NET_RECORD_GPS_LOCATION = 17;  					 // GPS位置信息记录, 只实现import和clear,记录信息对应 NET_RECORD_GPS_LOCATION_INFO 结构体
        public static final int NET_RECORD_RESIDENT = 18;      					 // 公租房租户信息,查询条件对应 FIND_RECORD_RESIDENT_CONDTION结构体, 记录信息对应 NET_RECORD_RESIDENT_INFO 结构体
        public static final int NET_RECORD_SENSORRECORD = 19;   			 	 // 监测量数据记录,查询条件对应 FIND_RECORD_SENSORRECORD_CONDITION 结构体,记录信息对应 NET_RECORD_SENSOR_RECORD 结构体      
        public static final int NET_RECORD_ACCESSQRCODE = 20;  					 // 开门二维码记录集,记录信息对应 NET_RECORD_ACCESSQRCODE_INFO结构体
    }
 
    //交通黑白名单记录信息
    public static class NET_TRAFFIC_LIST_RECORD extends Structure {
		public int                      dwSize; 	
		public int                	  	nRecordNo;                                		 // 之前查询到的记录号
		public byte[]      			  	szMasterOfCar = new byte[NET_MAX_NAME_LEN];        // 车主姓名
		public byte[]      			  	szPlateNumber = new byte[NET_MAX_PLATE_NUMBER_LEN];// 车牌号码 
		public int          			emPlateType;                               		 // 车牌类型,对应EM_NET_PLATE_TYPE
		public int          			emPlateColor;                              		 // 车牌颜色 ，对应EM_NET_PLATE_COLOR_TYPE
		public int          			emVehicleType;                             		 // 车辆类型 ，对应EM_NET_VEHICLE_TYPE
		public int        			  	emVehicleColor;                         		     // 车身颜色，对应EM_NET_VEHICLE_COLOR_TYPE
		public NET_TIME                 stBeginTime;                       				 // 开始时间
		public NET_TIME                 stCancelTime;                       				 // 撤销时间
		public int                      nAuthrityNum;                       				 // 权限个数
		public NET_AUTHORITY_TYPE[]  	stAuthrityTypes = (NET_AUTHORITY_TYPE[])new NET_AUTHORITY_TYPE().toArray(NET_MAX_AUTHORITY_LIST_NUM); // 权限列表 , 白名单仅有
		public int           		  	emControlType;                    			     // 布控类型 ,黑名单仅有，对应EM_NET_TRAFFIC_CAR_CONTROL_TYPE
		  
		public static class ByReference extends NET_TRAFFIC_LIST_RECORD implements Structure.ByReference {}
		public static class ByValue extends NET_TRAFFIC_LIST_RECORD implements Structure.ByValue {}
		  
		public NET_TRAFFIC_LIST_RECORD() {
			this.dwSize = this.size();
		}
    }
    
    // 交通流量记录
    public static class NET_RECORD_TRAFFIC_FLOW_STATE extends Structure {
        public int                       dwSize;
        public int                       nRecordNum;                 // 记录编号
        public int                       nChannel;                   // 通道号   
        public int                       nLane;                      // 车道号
        public int                       nVehicles;                  // 通过车辆总数
        public float                     fAverageSpeed;              // 平均车速,单位km/h
        public float                     fTimeOccupyRatio;           // 时间占有率,即单位时间内通过断面的车辆所用时间的总和占单位时间的比例
        public float                     fSpaceOccupyRatio;          // 空间占有率,即按百分率计量的车辆长度总和除以时间间隔内车辆平均行驶距离
        public float                     fSpaceHeadway;              // 车头间距,相邻车辆之间的距离,单位米/辆
        public float                     fTimeHeadway;               // 车头时距,单位秒/辆
        public int                       nLargeVehicles;             // 大车交通量(9米<车长<12米),辆/单位时间
        public int                       nMediumVehicles;            // 中型车交通量(6米<车长<9米),辆/单位时间
        public int                       nSmallVehicles;             // 小车交通量(4米<车长<6米),辆/单位时间,
        public float                     fBackOfQueue;               // 排队长度,单位：米, 从信号交叉口停车线到上游排队车辆末端之间的距离
        
        public NET_RECORD_TRAFFIC_FLOW_STATE() {
        	this.dwSize = this.size();
        }
    }
 
    //权限列表 , 白名单仅有
    public static class NET_AUTHORITY_TYPE extends Structure {
    	  public int                     dwSize; 
    	  public int              		 emAuthorityType;                 		 //权限类型，对应EM_NET_AUTHORITY_TYPE
    	  public boolean         		 bAuthorityEnable;                 		 //权限使能
    	  
    	  public NET_AUTHORITY_TYPE() {
    		  this.dwSize = this.size();
    	  }
    }

    //权限类型
    public static class EM_NET_AUTHORITY_TYPE extends Structure {
    	public static final int     	NET_AUTHORITY_UNKNOW = 0;
    	public static final int		    NET_AUTHORITY_OPEN_GATE = 1;             //开闸权限
    }
    
    // CLIENT_FindRecord接口输出参数
    public static class NET_OUT_FIND_RECORD_PARAM extends Structure {
    	 public int                     dwSize;          						// 结构体大小
    	 public long                    lFindeHandle; 						    // 查询记录句柄,唯一标识某次查询
    	 
    	 public NET_OUT_FIND_RECORD_PARAM() {
    		 this.dwSize = this.size();
    	 }
    }
    
    // CLIENT_FindNextRecord接口输入参数
    public static class NET_IN_FIND_NEXT_RECORD_PARAM extends Structure {
        public int                      dwSize;          						// 结构体大小
        public long              		lFindeHandle;    						// 查询句柄
        public int                      nFileCount;      						// 当前想查询的记录条数
        
        public NET_IN_FIND_NEXT_RECORD_PARAM() {
        	this.dwSize = this.size();
        }
    }
    
    //CLIENT_FindNextRecord接口输出参数
    public static class NET_OUT_FIND_NEXT_RECORD_PARAM extends Structure {
        public int                     dwSize;          						// 结构体大小
        public Pointer                 pRecordList;     				   	 	// 记录列表,用户分配内存，对应 交通黑白名单记录信息 NET_TRAFFIC_LIST_RECORD
        public int                     nMaxRecordNum;   						// 列表记录数
        public int                     nRetRecordNum;   						// 查询到的记录条数,当查询到的条数小于想查询的条数时,查询结束
        
        public NET_OUT_FIND_NEXT_RECORD_PARAM() {
        	this.dwSize = this.size();
        }
    }
    
    // CLIENT_QueryRecordCount接口输入参数
    public static class NET_IN_QUEYT_RECORD_COUNT_PARAM extends Structure
    {
	    public int dwSize;//结构体大小
	    public long lFindeHandle;//查询句柄
	    
        public NET_IN_QUEYT_RECORD_COUNT_PARAM() {
        	this.dwSize = this.size();
        }
    }
    
    // CLIENT_QueryRecordCount接口输出参数
    public static class NET_OUT_QUEYT_RECORD_COUNT_PARAM extends Structure
    {
	    public int dwSize;//结构体大小
	    public int nRecordCount;//设备返回的记录条数
	    
        public NET_OUT_QUEYT_RECORD_COUNT_PARAM() {
        	this.dwSize = this.size();
        }
    }
    
    // CLIENT_OperateTrafficList接口输入参数,
    public static class NET_IN_OPERATE_TRAFFIC_LIST_RECORD extends Structure {
        public int                       dwSize;
        public int                       emOperateType;  					 // emOperateType对应EM_RECORD_OPERATE_TYPE
        public int                       emRecordType;    					 // 要操作记录信息类型,emRecordType对应EM_NET_RECORD_TYPE
        public Pointer                   pstOpreateInfo;  				    // 对应 添加NET_INSERT_RECORD_INFO/ 删除NET_REMOVE_RECORD_INFO / 修改NET_UPDATE_RECORD_INFO
        
        public NET_IN_OPERATE_TRAFFIC_LIST_RECORD() {
        	this.dwSize = this.size();
        }
    }
    // 添加
    public static class NET_INSERT_RECORD_INFO extends Structure {
        public int                       			dwSize;
        public NET_TRAFFIC_LIST_RECORD.ByReference  pRecordInfo = new NET_TRAFFIC_LIST_RECORD.ByReference();      		// 记录内容信息
                    
        public NET_INSERT_RECORD_INFO () {
        	this.dwSize = this.size();
        }
    }
    // 删除
    public static class NET_REMOVE_RECORD_INFO extends Structure {
        public int                      dwSize;
        public int                      nRecordNo;      			    	 // 之前查询到的记录号，对应NET_TRAFFIC_LIST_RECORD里的nRecordNo
        
        public NET_REMOVE_RECORD_INFO() {
        	this.dwSize = this.size();
        }
    }
    // 修改
    public static class NET_UPDATE_RECORD_INFO extends Structure{
        public int                  			    dwSize;
        public NET_TRAFFIC_LIST_RECORD.ByReference 	pRecordInfo;    	   // 记录内容信息 ，对应  NET_TRAFFIC_LIST_RECORD
        
        public NET_UPDATE_RECORD_INFO() {
        	this.dwSize = this.size();
        }
    }
    
    // 黑白名单操作类型
    public static class EM_RECORD_OPERATE_TYPE extends Structure {
        public static final int NET_TRAFFIC_LIST_INSERT = 0;               // 增加记录操作
        public static final int NET_TRAFFIC_LIST_UPDATE = 1;               // 更新记录操作
        public static final int NET_TRAFFIC_LIST_REMOVE = 2;               // 删除记录操作
        public static final int NET_TRAFFIC_LIST_MAX = 3;
    }
    
    // CLIENT_OperateTrafficList接口输出参数,现阶段实现的操作接口中,只有返回nRecordNo的操作,stRetRecord暂时不可用,是null
    public static class NET_OUT_OPERATE_TRAFFIC_LIST_RECORD extends Structure {
        public int                     dwSize;
        public int                     nRecordNo;        //记录号 
        
        public NET_OUT_OPERATE_TRAFFIC_LIST_RECORD() {
        	this.dwSize = this.size();
        }
    }
    
    // 记录集操作参数
    public static class NET_CTRL_RECORDSET_PARAM extends Structure {
        public int               dwSize;
        public int 			     emType;                         // 记录集信息类型,对应EM_NET_RECORD_TYPE
        public Pointer           pBuf;                           // 新增\更新\查询\导入时,为记录集信息缓存,详见EM_NET_RECORD_TYPE注释
                                                                 // 删除时,为记录编号(int型)
        public int               nBufLen;                        // 记录集信息缓存大小
        
        public NET_CTRL_RECORDSET_PARAM() {
        	this.dwSize = this.size();
        }
    }
    
    // 黑白名单上传
    public static class NETDEV_BLACKWHITE_LIST_INFO extends Structure {
        public byte[]        						  szFile = new byte[MAX_PATH_STOR];      // 黑白名单文件路径
        public int                                    nFileSize;            				 // 升级文件大小
        public byte                  			      byFileType;         					 // 当前文件类型,0-黑名单,1-白名单 
        public byte                   			      byAction;            					 // 动作,0-覆盖,1-追加
        public byte[]      		   					  byReserved = new byte[126];            // 保留
    }
    
    // GPS信息(车载设备)
    public static class GPS_Info extends Structure {
        public NET_TIME           revTime;                          // 定位时间
        public byte[]             DvrSerial = new byte[50];         // 设备序列号
        public double             longitude;                     	// 经度(单位是百万分之度,范围0-360度)
        public double             latidude;                     	// 纬度(单位是百万分之度,范围0-180度)
        public double             height;                      	    // 高度(米)
        public double             angle;                        	// 方向角(正北方向为原点,顺时针为正)
        public double             speed;                        	// 速度(单位是海里,speed/1000*1.852公里/小时)
        public short              starCount;                     	// 定位星数,无符号
        public int           	  antennaState;                 	// 天线状态(true 好,false 坏)
        public int                orientationState;              	// 定位状态(true 定位,false 不定位)
        
        public static class ByValue extends GPS_Info implements Structure.ByValue { }
    }
    
    // 报警状态信息
    public static class ALARM_STATE_INFO extends Structure {
        public int                nAlarmCount;                       // 发生的报警事件个数
        public int[]              nAlarmState = new int[128];        // 发生的报警事件类型
        public byte[]             byRserved   = new byte[128];       // 保留字节
        
        public static class ByValue extends ALARM_STATE_INFO implements Structure.ByValue { }
    }
    
    // 对应CLIENT_SearchDevicesByIPs接口
    public static class DEVICE_IP_SEARCH_INFO extends Structure {
        public int               dwSize;                                    		 		// 结构体大小
        public int               nIpNum;                                				    // 当前搜索的IP个数
        public byte[]            szIP        = new byte[NET_MAX_SAERCH_IP_NUM*64];         // 具体待搜索的IP信息
        
        public DEVICE_IP_SEARCH_INFO() {
        	this.dwSize = this.size();
        }
    }
    
    // CLIENT_UploadRemoteFile 接口输入参数(上传文件到设备)
    public static class NET_IN_UPLOAD_REMOTE_FILE extends Structure {
        public int               dwSize;
        public String            pszFileSrc;                     	// 源文件路径
        public String         	 pszFileDst;                     	// 目标文件路径
        public String         	 pszFolderDst;                   	// 目标文件夹路径：可为NULL, NULL时设备使用默认路径
        public int          	 nPacketLen;                     	// 文件分包大小(字节): 0表示不分包
        
        public NET_IN_UPLOAD_REMOTE_FILE(){
        	this.dwSize = this.size();
        }
    } 
    
    // CLIENT_UploadRemoteFile 接口输出参数(上传文件到设备)
    public static class NET_OUT_UPLOAD_REMOTE_FILE extends Structure {
        public int               dwSize;
        
        public NET_OUT_UPLOAD_REMOTE_FILE() {
        	this.dwSize = this.size();
        }
    }
    
    // CLIENT_ParkingControlAttachRecord()接口输入参数
    public static class NET_IN_PARKING_CONTROL_PARAM extends Structure {
        public int                              dwSize;
        public StdCallCallback						    cbCallBack;                 // 数据回调函数,fParkingControlRecordCallBack 回调
        public NativeLong                       dwUser;                     // 用户定义参数
        
        public NET_IN_PARKING_CONTROL_PARAM() {
        	this.dwSize = this.size();
        }
    } 

    // CLIENT_ParkingControlDetachRecord()接口输出参数
    public static class NET_OUT_PARKING_CONTROL_PARAM extends Structure {
        public int    							dwSize;
        
        public NET_OUT_PARKING_CONTROL_PARAM() {
        	this.dwSize = this.size();
        }
    }
    
    // 过车记录信息
    public static class NET_CAR_PASS_ITEM extends Structure {
        public int                      dwSize; 
        public NET_TIME                 stuTime;          // 过车时间
        public int                      dwCardNo;         // 卡号
        public int      			    emCardType;       // 智能停车系统出入口机IC卡用户类型,对应 NET_ECK_IC_CARD_USER_TYPE
        public int     				    emFlag;           // 过车记录类型，对应 NET_ECK_CAR_PASS_FLAG
        
        public static class ByReference extends NET_CAR_PASS_ITEM implements Structure.ByReference {}
        
        public NET_CAR_PASS_ITEM(){
        	this.dwSize = this.size();
        }
    }
    
    // 智能停车系统出入口机IC卡用户类型
    public static class NET_ECK_IC_CARD_USER_TYPE extends Structure {
    	public static final int NET_ECK_IC_CARD_USER_UNKNOWN     = 0;
    	public static final int NET_ECK_IC_CARD_USER_ALL         = 1;               // 全部类型
    	public static final int NET_ECK_IC_CARD_USER_TEMP		 = 2;               // 临时用户
    	public static final int NET_ECK_IC_CARD_USER_LONG		 = 3;               // 长期用户
    	public static final int NET_ECK_IC_CARD_USER_ADMIN		 = 4;               // 管理员
    	public static final int NET_ECK_IC_CARD_USER_BLACK_LIST  = 5;               // 黑名单
    }
    
    // 智能停车系统出入口机异常过车记录类型
    public static class NET_ECK_CAR_PASS_FLAG extends Structure {
    	public static final int NET_ECK_CAR_PASS_FLAG_NORMAL   = 0;                 // 正常
    	public static final int NET_ECK_CAR_PASS_FLAG_ABNORMAL = 1;                 // 异常
    	public static final int NET_ECK_CAR_PASS_FLAG_ALL      = 2;                 // 全部
    } 

    // CLIENT_ParkingControlStartFind接口输入参数******************
    public static class NET_IN_PARKING_CONTROL_START_FIND_PARAM extends Structure {
        public int                      dwSize;          // 结构体大小
        public int                      bSearchCount;    // 查询记录调试是否有效
        public int                      dwSearchCount;   // 查询记录条数, 数值范围1~100
        public int                      bBegin;          // 查询开始时间是否有效
        public NET_TIME                 stuBegin;        // 查询开始时间
        public int                      bEnd;            // 查询结束时间是否有效
        public NET_TIME                 stuEnd;          // 查询结束时间
        public int                      bCardType;       // 卡类型是否有效
        public int 					    emCardType;      // 卡类型,对应 NET_ECK_IC_CARD_USER_TYPE
        public int                      bFlag;           // 过车标记是否有效
        public int                      emFlag;          // 过车标记，对应 NET_ECK_CAR_PASS_FLAG
        
        public NET_IN_PARKING_CONTROL_START_FIND_PARAM() {
        	this.dwSize = this.size();
        }
    } 

    // CLIENT_ParkingControlStartFind接口输出参数
    public static class NET_OUT_PARKING_CONTROL_START_FIND_PARAM extends Structure {
        public int                     dwSize;          // 结构体大小
        public int                     dwTotalCount;    // 符合此次查询条件的结果总条数
        
        public NET_OUT_PARKING_CONTROL_START_FIND_PARAM(){
        	this.dwSize = this.size();
        }
    }
    
    // CLIENT_ParkingControlDoFind接口输入参数*******************
    public static class NET_IN_PARKING_CONTROL_DO_FIND_PARAM extends Structure {
        public int                     dwSize;          // 结构体大小
        public int                     dwFileCount;     // 当前想查询的记录条数
        
        public NET_IN_PARKING_CONTROL_DO_FIND_PARAM(){
        	this.dwSize = this.size();
        }
    } 

    // CLIENT_ParkingControlDoFind接口输出参数
    public static class NET_OUT_PARKING_CONTROL_DO_FIND_PARAM extends Structure{
        public int                             dwSize;          // 结构体大小
        public NET_CAR_PASS_ITEM.ByReference   pstuRecordList = new NET_CAR_PASS_ITEM.ByReference();  // 记录列表,用户分配内存
        public int                     		   nMaxRecordNum;   // 列表记录数
        public int                     	 	   nRetRecordNum;   // 查询到的记录条数,当查询到的条数小于想查询的条数时,查询结束
        
        public NET_OUT_PARKING_CONTROL_DO_FIND_PARAM(){
        	this.dwSize = this.size();
        }
    } 
    
    // CLIENT_ParkingControlAttachParkInfo()接口输入参数
    public static class NET_IN_PARK_INFO_PARAM extends Structure 
    {
        public int                             dwSize;
        public NET_PARK_INFO_FILTER            stuFilter;
        public StdCallCallback			       cbCallBack;        // 数据回调函数,fParkInfoCallBack 回调
        public NativeLong                      dwUser;            // 用户定义参数
        
        public NET_IN_PARK_INFO_PARAM() {
        	this.dwSize = this.size();
        }
    } 
    
    // CLIENT_ParkingControlAttachParkInfo()接口输出参数
    public static class NET_OUT_PARK_INFO_PARAM extends Structure
    {
        public int    				dwSize;
        
        public NET_OUT_PARK_INFO_PARAM() {
        	this.dwSize = this.size();
        }
    }
    
    // 车位检测器信息查询条件
    public static class NET_PARK_INFO_FILTER extends Structure
    {
        public int           dwSize; 
        public int           dwNum;                               // 车位检测器类型数量
        public int[] 		 emType = new int[NET_ECK_PARK_DETECTOR_TYPE.NET_ECK_PARK_DETECTOR_TYPE_ALL];   // 车位检测器类型
        
        public NET_PARK_INFO_FILTER() {
        	this.dwSize = this.size();
        }
    } 
    
    // 车位检测器类型
    public static class NET_ECK_PARK_DETECTOR_TYPE extends Structure
    {
        public static final int NET_ECK_PARK_DETECTOR_TYPE_SONIC  = 0;         // 超声波探测器
        public static final int NET_ECK_PARK_DETECTOR_TYPE_CAMERA = 1;         // 相机检测器
        public static final int NET_ECK_PARK_DETECTOR_TYPE_ALL	  = 2;
    } 
    
    // 车位信息
    public static class NET_PARK_INFO_ITEM extends Structure
    {
        public int                 dwSize; 
        public byte[]              szParkNo = new byte[NET_COMMON_STRING_32];   // 车位号
        public int  			   emState;                         			// 车位状态,对应  NET_ECK_PARK_STATE
        public int                 dwScreenIndex;                   			// 车位号显示对应的诱导屏分屏号
        public int                 dwFreeParkNum;                   			// 屏号显示的当前空余车位数目
        
        public NET_PARK_INFO_ITEM(){
        	this.dwSize = this.size();
        }
    }
    
    // 智能停车系统车位状态
    public static class NET_ECK_PARK_STATE extends Structure
    {
        public static final int NET_ECK_PARK_STATE_UNKOWN = 0;
        public static final int NET_ECK_PARK_STATE_PARK   = 1;       // 车位有车
        public static final int NET_ECK_PARK_STATE_NOPARK = 2;       // 车位无车
    } 
    
    // 智能停车系统出入口机设置车位信息 参数 DH_CTRL_ECK_SET_PARK_INFO
    public static class NET_CTRL_ECK_SET_PARK_INFO_PARAM extends Structure
    {
        public int           	dwSize;
        public int              nScreenNum;                                     // 屏数量, 不超过 ECK_SCREEN_NUM_MAX
        public int[]            nScreenIndex = new int[ECK_SCREEN_NUM_MAX];     // 屏号, 每个元素表示屏序号
        public int[]            nFreeParkNum = new int[ECK_SCREEN_NUM_MAX];     // 对应屏管理下的空余车位数
                                                                				// 长度和下标与nScreenIndex一致,每个元素表示对应屏号下的空余车位
        public NET_CTRL_ECK_SET_PARK_INFO_PARAM(){
        	this.dwSize = this.size();
        }
    }
    
    // CLIENT_PowerControl接口输入参数(电视墙电源控制)
    public static class NET_IN_WM_POWER_CTRL extends Structure
    {
        public int              	dwSize;
        public int                  nMonitorWallID;             // 电视墙序号
        public String          		pszBlockID;                 // 区块ID, NULL/""-所有区块
        public int                  nTVID;                      // 显示单元序号, -1表示区块中所有显示单元
        public int                  bPowerOn;                   // 是否打开电源
        
        public NET_IN_WM_POWER_CTRL() {
        	this.dwSize = this.size();
        }
    } 

    // CLIENT_PowerControl接口输出参数(电视墙电源控制)
    public static class NET_OUT_WM_POWER_CTRL extends Structure
    {
        public int                 dwSize;
        
        public NET_OUT_WM_POWER_CTRL() {
        	this.dwSize = this.size();
        }
    } 
    
    // CLIENT_LoadMonitorWallCollection接口输入参数(载入电视墙预案)
    public static class NET_IN_WM_LOAD_COLLECTION extends Structure
    {
        public int                dwSize;
        public int                nMonitorWallID;             // 电视墙序号
        public String         	  pszName;                    // 预案名称
        
        public NET_IN_WM_LOAD_COLLECTION() {
        	this.dwSize = this.size();
        }
    } 

    // CLIENT_LoadMonitorWallCollection接口输出参数(载入电视墙预案)
    public static class NET_OUT_WM_LOAD_COLLECTION extends Structure
    {
        public int               dwSize;
        
        public NET_OUT_WM_LOAD_COLLECTION() {
        	this.dwSize = this.size();
        }
    } 

    // CLIENT_SaveMonitorWallCollection接口输入参数(保存电视墙预案)
    public static class NET_IN_WM_SAVE_COLLECTION extends Structure
    {
        public int               dwSize;
        public int               nMonitorWallID;             // 电视墙序号
        public String            pszName;                    // 预案名称
        public String            pszControlID;               // 控制id
        
        public NET_IN_WM_SAVE_COLLECTION() {
        	this.dwSize = this.size();
        }
    } 

    // CLIENT_SaveMonitorWallCollection接口输出参数(保存电视墙预案)
    public static class NET_OUT_WM_SAVE_COLLECTION extends Structure
    {
        public int               dwSize;
        
        public NET_OUT_WM_SAVE_COLLECTION() {
        	this.dwSize = this.size();
        }
    }
    
    // 分割模式
    public static class NET_SPLIT_MODE extends Structure
    {
        public static final int NET_SPLIT_1 = 1;                                   // 1画面
        public static final int NET_SPLIT_2 = 2;                                   // 2画面
        public static final int NET_SPLIT_4 = 4;                                   // 4画面
        public static final int NET_SPLIT_6 = 6;                                   // 6画面
        public static final int NET_SPLIT_8 = 8;                                   // 8画面
        public static final int NET_SPLIT_9 = 9;                                   // 9画面
        public static final int NET_SPLIT_12 = 12;                                 // 12画面
        public static final int NET_SPLIT_16 = 16;                                 // 16画面
        public static final int NET_SPLIT_20 = 20;                                 // 20画面
        public static final int NET_SPLIT_25 = 25;                                 // 25画面
        public static final int NET_SPLIT_36 = 36;                                 // 36画面
        public static final int NET_SPLIT_64 = 64;                                 // 64画面
        public static final int NET_SPLIT_144 = 144;                               // 144画面
        public static final int NET_PIP_1 = NET_SPLIT_PIP_BASE + 1;                // 画中画模式, 1个全屏大画面+1个小画面窗口
        public static final int NET_PIP_3 = NET_SPLIT_PIP_BASE + 3;                // 画中画模式, 1个全屏大画面+3个小画面窗口
        public static final int NET_SPLIT_FREE = NET_SPLIT_PIP_BASE * 2;           // 自由开窗模式,可以自由创建、关闭窗口,自由设置窗口位置和Z轴次序
        public static final int NET_COMPOSITE_SPLIT_1 = NET_SPLIT_PIP_BASE * 3 + 1;// 融合屏成员1分割
        public static final int NET_COMPOSITE_SPLIT_4 = NET_SPLIT_PIP_BASE * 3 + 4;// 融合屏成员4分割
    }
    
    // 区块窗口信息
    public static class NET_WINDOW_COLLECTION extends Structure
    {
        public int               dwSize;
        public int               nWindowID;                      // 窗口ID
        public int               bWndEnable;                     // 窗口是否有效
        public DH_RECT          stuRect;                        // 窗口区域, 自由分割模式下有效
        public int               bDirectable;                    // 坐标是否满足直通条件
        public int               nZOrder;                        // 窗口Z次序
        public int               bSrcEnable;                     // 显示源是否有效
        public byte[]            szDeviceID = new byte[NET_DEV_ID_LEN_EX]; // 设备ID
        public int               nVideoChannel;                  // 视频通道号
        public int               nVideoStream;                   // 视频码流类型
        public int               nAudioChannel;                  // 音频通道
        public int               nAudioStream;                   // 音频码流类型
        public int               nUniqueChannel;                 // 设备内统一编号的唯一通道号       
        
        public NET_WINDOW_COLLECTION() {
        	this.dwSize = this.size();
        }
    } 
    
    // 区块收藏
    public static class NET_BLOCK_COLLECTION extends Structure
    {
        public int                	 			  dwSize;
        public int        			  			  emSplitMode;                      						  // 分割模式，对应  NET_SPLIT_MODE
        public NET_WINDOW_COLLECTION[] 			  stuWnds       = (NET_WINDOW_COLLECTION[])new NET_WINDOW_COLLECTION().toArray(NET_MAX_SPLIT_WINDOW);  // 窗口信息数组
        public int                   			  nWndsCount;                    							  // 窗口数量
        public byte[]                			  szName        = new byte[NET_DEVICE_NAME_LEN];    		  // 收藏夹名称
        public int                   			  nScreen;                       							  // 输出通道号, 包括拼接屏
        public byte[]                  			  szCompositeID = new byte[NET_DEV_ID_LEN_EX]; 				  // 拼接屏ID    
        public Pointer  		  				  pstuWndsEx;                  							      // 窗口信息数组指针 DH_WINDOW_COLLECTION[] , 由用户分配内存. 当stuWnds数组大小不够用时可以使用
        public int                  			  nMaxWndsCountEx;               							  // 最大窗口数量, 用户填写. pstuWndsEx数组的元素个数
        public int                  			  nRetWndsCountEx;               							  // 返回窗口数量
        
        public NET_BLOCK_COLLECTION() {
        	this.dwSize = this.size();
        }      
    }

    // 电视墙显示单元
   public static class NET_MONITORWALL_OUTPUT extends Structure
    {
        public int               dwSize;
        public byte[]            szDeviceID = new byte[NET_DEV_ID_LEN];          // 设备ID, 本机时为""
        public int               nChannel;                           		     // 通道号
        public byte[]            szName 	= new byte[NET_DEV_NAME_LEN];        // 屏幕名称
        
        public NET_MONITORWALL_OUTPUT() {
        	this.dwSize = this.size();
        }
    } 

    // 电视墙显示区块
    public static class NET_MONITORWALL_BLOCK extends Structure
    {
        public int               dwSize;
        public byte[]            szName 	   = new byte[NET_DEV_NAME_LEN];   // 区块名称
        public byte[]            szCompositeID = new byte[NET_DEV_ID_LEN];     // 拼接屏ID
        public byte[]            szControlID   = new byte[NET_DEV_ID_LEN];     // 控制ID
        public int               nSingleOutputWidth;             			   // 单个显示单元所占的网格列数
        public int               nSingleOutputHeight;            			   // 单个显示单元所占的网格行数
        public DH_RECT           stuRect;                        			   // 区域坐标
        public NET_TSECT[]       stuPowerSchedule = new NET_TSECT[NET_TSCHE_DAY_NUM * NET_TSCHE_SEC_NUM]; // 开机时间表, 第一维各元素表示周日~周六和节假日
        public Pointer			 pstuOutputs;                				   // 显示单元数组 NET_MONITORWALL_OUTPUT[] , 用户分配内存
        public int               nMaxOutputCount;                			   // 显示单元数组大小, 用户填写
        public int               nRetOutputCount;                			   // 返回的显示单元数量
        
        public NET_MONITORWALL_BLOCK() {
        	this.dwSize = this.size();
        }
    } 

    // 电视墙配置
    public static class NET_MONITORWALL extends Structure
    {
        public int                dwSize;
        public byte[]             szName = new byte[NET_DEV_NAME_LEN];      // 名称
        public int                nGridLine;                      			// 网格行数
        public int                nGridColume;                   		 	// 网格列数
        public Pointer   		  pstuBlocks;              		            // 显示区块数组 NET_MONITORWALL_BLOCK[] , 用户分配内存
        public int                nMaxBlockCount;                 			// 显示区块数组大小, 用户填写
        public int                nRetBlockCount;                 			// 返回的显示区块数量
        public int                bDisable;                       			// 是否禁用, 0-该电视墙有效, 1-该电视墙无效
        public byte[]             szDesc = new byte[NET_COMMON_STRING_256]; // 电视墙描述信息
        
        public NET_MONITORWALL() {
        	this.dwSize = this.size();
        }
    } 

    // 电视墙预案
    public static class NET_MONITORWALL_COLLECTION extends Structure
    {
        public int                	  dwSize;
        public byte[]                 szName 	  = new byte[NET_DEVICE_NAME_LEN];    			// 电视墙预案名称
        public NET_BLOCK_COLLECTION[] stuBlocks	  = (NET_BLOCK_COLLECTION[])new NET_BLOCK_COLLECTION().toArray(NET_MAX_BLOCK_NUM);// 区块数组
        public int                 	  nBlocksCount;                  							// 区块数量
        public byte[]                 szControlID = new byte[NET_DEV_ID_LEN_EX]; 				// 控制ID
        public NET_MONITORWALL        stuMonitorWall;                							// 电视墙配置
        
        public NET_MONITORWALL_COLLECTION() {
        	this.dwSize = this.size();
        }
    } 

    // CLIENT_GetMonitorWallCollections接口输入参数(获取电视墙预案信息)
    public static class NET_IN_WM_GET_COLLECTIONS extends Structure
    {
        public int                	dwSize;
        public int                  nMonitorWallID;                // 电视墙ID
        
        public NET_IN_WM_GET_COLLECTIONS() {
        	this.dwSize = this.size();
        }
    } 

    // CLIENT_GetMonitorWallCollections接口输出参数(获取电视墙预案信息)
    public static class NET_OUT_WM_GET_COLLECTIONS extends Structure
    {
        public int                   dwSize;    
        public Pointer			   	 pCollections;  		      // 电视墙预案数组, 对应   NET_MONITORWALL_COLLECTION 指针
        public int                   nMaxCollectionsCount;   	  // 电视墙预案数组大小
        public int                   nCollectionsCount;      	  // 电视墙预案数量
        
        public NET_OUT_WM_GET_COLLECTIONS() {
        	this.dwSize = this.size();
        }
    } 
    
    // 级联权限验证信息
    public static class NET_CASCADE_AUTHENTICATOR extends Structure
    {
        public int                  dwSize;
        public byte[]               szUser	   = new byte[NET_NEW_USER_NAME_LENGTH];    // 用户名
        public byte[]               szPwd  	   = new byte[NET_NEW_USER_PSW_LENGTH];     // 密码
        public byte[]               szSerialNo = new byte[NET_SERIALNO_LEN];        	// 设备序列号
        
        public NET_CASCADE_AUTHENTICATOR() {
        	this.dwSize = this.size();
        }
    }
    

    public static class EM_SRC_PUSHSTREAM_TYPE extends Structure
    {   
        public static final int EM_SRC_PUSHSTREAM_AUTO			= 0;       // 设备端根据码流头自动识别，默认值
        public static final int EM_SRC_PUSHSTREAM_HIKVISION		= 1;       // 海康私有码流
        public static final int EM_SRC_PUSHSTREAM_PS			= 2;       // PS流
        public static final int EM_SRC_PUSHSTREAM_TS			= 3;       // TS流
        public static final int EM_SRC_PUSHSTREAM_SVAC			= 4;       // SVAC码流
    }
    
    // 显示源
    public static class NET_SPLIT_SOURCE extends Structure
    {
        public int               	dwSize;
        public int                  bEnable;                                // 使能
        public byte[]               szIp 	= new byte[NET_MAX_IPADDR_LEN]; // IP, 空表示没有设置
        public byte[]               szUser  = new byte[NET_USER_NAME_LENGTH];// 用户名, 建议使用szUserEx
        public byte[]               szPwd 	= new byte[NET_USER_PSW_LENGTH]; // 密码, 建议使用szPwdEx
        public int                  nPort;                                  // 端口
        public int                  nChannelID;                             // 通道号
        public int                  nStreamType;                            // 视频码流, -1-自动, 0-主码流, 1-辅码流1, 2-辅码流2, 3-辅码流3, 4-snap, 5-预览
        public int                  nDefinition;                            // 清晰度, 0-标清, 1-高清
        public int  				emProtocol;                             // 协议类型,对应   NET_DEVICE_PROTOCOL
        public byte[]               szDevName  = new byte[NET_DEVICE_NAME_LEN]; // 设备名称
        public int                  nVideoChannel;                          // 视频输入通道数
        public int                  nAudioChannel;                          // 音频输入通道数
        //--------------------------------------------------------------------------------------
        // 以下只对解码器有效
        public int                 bDecoder;                                // 是否是解码器
        public byte                byConnType;                              // -1: auto, 0：TCP；1：UDP；2：组播
        public byte                byWorkMode;                              // 0：直连；1：转发
        public short               wListenPort;                             // 指示侦听服务的端口,转发时有效; byConnType为组播时,则作为多播端口
        public byte[]              szDevIpEx  = new byte[NET_MAX_IPADDR_OR_DOMAIN_LEN]; // szDevIp扩展,前端DVR的IP地址(可以输入域名)
        public byte                bySnapMode;                              // 抓图模式(nStreamType==4时有效) 0：表示请求一帧,1：表示定时发送请求
        public byte                byManuFactory;                           // 目标设备的生产厂商, 具体参考EM_IPC_TYPE类
        public byte                byDeviceType;                            // 目标设备的设备类型, 0:IPC
        public byte                byDecodePolicy;                          // 目标设备的解码策略, 0:兼容以前
                                                                    	    // 1:实时等级高 2:实时等级中
                                                                    	    // 3:实时等级低 4:默认等级
                                                                    	    // 5:流畅等级高 6:流畅等级中
                                                                    	    // 7:流畅等级低
        //--------------------------------------------------------------------------------------
        public int                dwHttpPort;                               // Http端口号, 0-65535
        public int                dwRtspPort;                               // Rtsp端口号, 0-65535
        public byte[]             szChnName  = new byte[NET_DEVICE_NAME_LEN]; // 远程通道名称, 只有读取到的名称不为空时才可以修改该通道的名称
        public byte[]             szMcastIP  = new byte[NET_MAX_IPADDR_LEN];  // 多播IP地址, byConnType为组播时有效
        public byte[]             szDeviceID = new byte[NET_DEV_ID_LEN_EX];   // 设备ID, ""-null, "Local"-本地通道, "Remote"-远程通道, 或者填入具体的RemoteDevice中的设备ID
        public int                bRemoteChannel;                           // 是否远程通道(只读)
        public int        		  nRemoteChannelID;                         // 远程通道ID(只读), bRemoteChannel=TRUE时有效
        public byte[]             szDevClass = new byte[NET_DEV_TYPE_LEN];  // 设备类型, 如IPC, DVR, NVR等
        public byte[]             szDevType  = new byte[NET_DEV_TYPE_LEN];  // 设备具体型号, 如IPC-HF3300
        public byte[]             szMainStreamUrl = new byte[MAX_PATH];     // 主码流url地址, byManuFactory为DH_IPC_OTHER时有效
        public byte[]             szExtraStreamUrl = new byte[MAX_PATH];    // 辅码流url地址, byManuFactory为DH_IPC_OTHER时有效
        public int                nUniqueChannel;                           // 设备内统一编号的唯一通道号, 只读
        public NET_CASCADE_AUTHENTICATOR  stuCascadeAuth;                   // 级联认证信息, 设备ID为"Local/Cascade/SerialNo"时有效, 其中SerialNo是设备序列号
        public int                nHint;                                    // 0-普通视频源, 1-报警视频源
        public int                nOptionalMainUrlCount;                    // 备用主码流地址数量
        public byte[]             szOptionalMainUrls = new byte[NET_MAX_OPTIONAL_URL_NUM * MAX_PATH];  // 备用主码流地址列表
        public int                nOptionalExtraUrlCount;                   // 备用辅码流地址数量
        public byte[]             szOptionalExtraUrls = new byte[NET_MAX_OPTIONAL_URL_NUM * MAX_PATH]; // 备用辅码流地址列表
        //--------------------------------------------------------------------------------------
        //协议后续添加字段
        public int                nInterval;                                // 轮巡时间间隔   单位：秒
        public byte[]             szUserEx = new byte[NET_NEW_USER_NAME_LENGTH]; // 用户名
        public byte[]             szPwdEx  = new byte[NET_NEW_USER_PSW_LENGTH];  // 密码
        public int  			  emPushStream;          			        // 推流方式的码流类型,只有byConnType为TCP-Push或UDP-Push才有该字段,对应  EM_SRC_PUSHSTREAM_TYPE
        
        public NET_SPLIT_SOURCE() {
        	this.dwSize = this.size();
        }
    } 
    
    // 矩阵子卡信息
    public static class NET_MATRIX_CARD extends Structure
    {
        public int               dwSize;
        public int               bEnable;                                // 是否有效
        public int               dwCardType;                             // 子卡类型
        public byte[]            szInterface = new byte[NET_MATRIX_INTERFACE_LEN];   // 信号接口类型, "CVBS", "VGA", "DVI"...
        public byte[]            szAddress   = new byte[NET_MAX_IPADDR_OR_DOMAIN_LEN]; // 设备ip或域名, 无网络接口的子卡可以为空
        public int               nPort;                                  // 端口号, 无网络接口的子卡可以为0
        public int               nDefinition;                            // 清晰度, 0=标清, 1=高清
        public int               nVideoInChn;                            // 视频输入通道数
        public int               nAudioInChn;                            // 音频输入通道数
        public int               nVideoOutChn;                           // 视频输出通道数
        public int               nAudioOutChn;                           // 音频输出通道数
        public int               nVideoEncChn;                           // 视频编码通道数
        public int               nAudioEncChn;                           // 音频编码通道数
        public int               nVideoDecChn;                           // 视频解码通道数
        public int               nAudioDecChn;                           // 音频解码通道数
        public int               nStauts;                                // 状态: -1-未知, 0-正常, 1-无响应, 2-网络掉线, 3-冲突, 4-正在升级, 5-链路状态异常, 6-子板背板未插好, 7-程序版本出错
        public int               nCommPorts;                             // 串口数
        public int               nVideoInChnMin;                         // 视频输入通道号最小值
        public int               nVideoInChnMax;                         // 视频输入通道号最大值
        public int               nAudioInChnMin;                         // 音频输入通道号最小值
        public int               nAudioInChnMax;                         // 音频输入通道号最大值
        public int               nVideoOutChnMin;                        // 视频输出通道号最小值
        public int               nVideoOutChnMax;                        // 视频输出通道号最大值
        public int               nAudioOutChnMin;                        // 音频输出通道号最小值
        public int               nAudioOutChnMax;                        // 音频输出通道号最大值    
        public int               nVideoEncChnMin;                        // 视频编码通道号最小值
        public int               nVideoEncChnMax;                        // 视频编码通道号最大值
        public int               nAudioEncChnMin;                        // 音频编码通道号最小值
        public int               nAudioEncChnMax;                        // 音频编码通道号最大值
        public int               nVideoDecChnMin;                        // 视频解码通道号最小值
        public int               nVideoDecChnMax;                        // 视频解码通道号最大值
        public int               nAudioDecChnMin;                        // 音频解码通道号最小值
        public int               nAudioDecChnMax;                        // 音频解码通道号最大值
        public int               nCascadeChannels;                       // 级联通道数
        public int               nCascadeChannelBitrate;                 // 级联通道带宽, 单位Mbps
        public int               nAlarmInChnCount;                       // 报警输入通道数
        public int               nAlarmInChnMin;                         // 报警输入通道号最小值
        public int               nAlarmInChnMax;                         // 报警输入通道号最大值
        public int               nAlarmOutChnCount;                      // 报警输出通道数
        public int               nAlarmOutChnMin;                        // 报警输入通道号最小值
        public int               nAlarmOutChnMax;                        // 报警输入通道号最大值
        public int               nVideoAnalyseChnCount;                  // 智能分析通道数
        public int               nVideoAnalyseChnMin;                    // 智能分析通道号最小值
        public int               nVideoAnalyseChnMax;                    // 智能分析通道号最大值
        public int               nCommPortMin;                           // 串口号最小值
        public int               nCommPortMax;                           // 串口号最大值
        public byte[]            szVersion 	   = new byte[NET_COMMON_STRING_32];    // 版本信息
        public NET_TIME          stuBuildTime;                           // 编译时间
        public byte[]            szBIOSVersion = new byte[NET_COMMON_STRING_64];    // BIOS版本号
        public byte[]			 szMAC         = new byte[NET_MACADDR_LEN];			// MAC地址
        
        public NET_MATRIX_CARD() {
        	this.dwSize = this.size();
        }
    } 
    
    // 矩阵子卡列表
    public static class NET_MATRIX_CARD_LIST extends Structure
    {
        public int               dwSize;
        public int               nCount;                                 				  // 子卡数量
        public NET_MATRIX_CARD[] stuCards = new NET_MATRIX_CARD[NET_MATRIX_MAX_CARDS];    // 子卡列表
        
        public NET_MATRIX_CARD_LIST() {
        	this.dwSize = this.size();
        	for(int i = 0; i < NET_MATRIX_MAX_CARDS; i++) {
        		stuCards[i] = new NET_MATRIX_CARD();
        	}
        }
    } 
    
    // CLIENT_FindFramInfo 接口输入参数
    public static class NET_IN_FIND_FRAMEINFO_PRAM extends Structure
    {
        public int                 dwSize;                   // 结构体大小 
        public boolean             abFileName;               // 文件名是否作为有效的查询条件,若文件名有效,则不用填充文件信息（stRecordInfo）
        public byte[]              szFileName = new byte[MAX_PATH];     // 文件名
        public NET_RECORDFILE_INFO stuRecordInfo;            // 文件信息
        public int                 dwFramTypeMask;           // 帧类型掩码,详见“帧类型掩码定义”
        
        public NET_IN_FIND_FRAMEINFO_PRAM() {
        	this.dwSize = this.size();
        }
    }

    // CLIENT_FindFramInfo 接口输出参数
    public static class NET_OUT_FIND_FRAMEINFO_PRAM extends Structure
    {
        public int                 dwSize;                 // 结构体大小 
        public long          	   lFindHandle;            // 文件查找句柄
        
        public NET_OUT_FIND_FRAMEINFO_PRAM() {
        	this.dwSize = this.size();
        }
    }
    
    // CLIENT_FileStreamClearTags / CLIENT_FileStreamSetTags 接口输入参数
    public static class NET_IN_FILE_STREAM_TAGS_INFO extends Structure
    {
    	public int				   dwSize;					// 结构体大小 
    	public int				   nArrayCount;				// 标签数组个数
    	public Pointer			   pstuTagInfo;  			// 标签数组，各项内容关系为"且", 用户分配内存,大小为sizeof( NET_FILE_STREAM_TAG_INFO )*nArrayCount						
    
        public NET_IN_FILE_STREAM_TAGS_INFO() {
        	this.dwSize = this.size();
        }
    } 

    // CLIENT_FileStreamClearTags / CLIENT_FileStreamSetTags 接口输出参数
    public static class NET_OUT_FILE_STREAM_TAGS_INFO extends Structure
    {
    	public int				  dwSize;			       // 结构体大小 
    	
    	public NET_OUT_FILE_STREAM_TAGS_INFO() {
    		this.dwSize = this.size();
    	}
    } 
    
    // 标签数组
    public static class NET_FILE_STREAM_TAG_INFO extends Structure
    {
    	public int				dwSize;									// 结构体大小 
    	public NET_TIME			stuTime;								// 标签时间
    	public byte[]			szContext = new byte[NET_COMMON_STRING_64];		// 标签内容，中文必须使用utf8编码
    	public byte[]			szUserName = new byte[NET_COMMON_STRING_32];		// 用户名，中文必须使用utf8编码，EVS定制增加
    	public byte[]			szChannelName = new byte[NET_COMMON_STRING_64];   // 通道名称，中文必须使用utf8编码，EVS定制增加
    	
    	public NET_FILE_STREAM_TAG_INFO() {
    		this.dwSize = this.size();
    	}
    } 
    
    // CLIENT_FileStreamGetTags 接口输入参数
    public static class NET_IN_FILE_STREAM_GET_TAGS_INFO extends Structure
    {
    	public int			   dwSize;					// 结构体大小 
    	
    	public NET_IN_FILE_STREAM_GET_TAGS_INFO() {
    		this.dwSize = this.size();
    	}
    } 

    // CLIENT_FileStreamGetTags 接口输出参数
    public static class NET_OUT_FILE_STREAM_GET_TAGS_INFO extends Structure
    {
    	public int			  dwSize;					// 结构体大小 
    	public int			  nMaxNumber;				// 标签数组最大个数
    	public int			  nRetTagsCount;	        // 实际返回的标签信息个数
    	public Pointer	 	  pstuTagInfo; 				// 标签数组  NET_FILE_STREAM_TAG_INFO_EX
    	
    	public NET_OUT_FILE_STREAM_GET_TAGS_INFO() {
    		this.dwSize = this.size();
    	}
    }  

	// 查询到的标签信息
    public static class NET_FILE_STREAM_TAG_INFO_EX extends Structure
	{
	    public int				dwSize;										// 结构体大小 
	 	public NET_TIME			stuTime;									// 标签所对于视频的时间，精确到秒
	 	public int				nMillisecond;								// 毫秒
	 	public int				nSequence;									// 视频序列号
	 	public byte[]			szContext = new byte[NET_COMMON_STRING_64];	// 标签内容，中文必须使用utf8编码
	 	public NET_TIME			stuStartTime;								// 录像文件开始时间
	 	public NET_TIME			stuEndTime;									// 录像文件结束时间
	 	public int				emType;										// 文件类型,对应   NET_FILE_STREAM_TYPE
	 	public byte[]			szUserName = new byte[NET_COMMON_STRING_32];	   // 用户名，中文必须使用utf8编码，EVS定制增加
	 	public byte[]			szChannelName = new byte[NET_COMMON_STRING_64];  // 通道名称，中文必须使用utf8编码，EVS定制增加
	 	
	 	public NET_FILE_STREAM_TAG_INFO_EX() {
	 		this.dwSize = this.size();
	 	}
	} 
	 
	// 文件类型
	public static class NET_FILE_STREAM_TYPE extends Structure
	{
	 	public static final int NET_FILE_STREAM_TYPE_UNKNOWN   = 0;			// 未知
	 	public static final int NET_FILE_STREAM_TYPE_NORMAL    = 1;			// 普通
	 	public static final int NET_FILE_STREAM_TYPE_ALARM     = 2;			// 报警
	 	public static final int NET_FILE_STREAM_TYPE_DETECTION = 3;			// 动检
	} 

	// 一屏幕的分割模式信息， CLIENT_GetSplitMode/CLIENT_SetSplitMode参数
	public static class NET_SPLIT_MODE_INFO extends Structure
	{
	    public int               dwSize;
	    public int               emSplitMode;            // 分割模式, NET_SPLIT_MODE
	    public int               nGroupID;               // 分组序号
	    public int               dwDisplayType;          // 显示类型；具体见DH_SPLIT_DISPLAY_TYPE（注释各模式下显示内容由"PicInPic"决定, 各模式下显示内容按NVD旧有规则决定（即DisChn字段决定）。兼容,没有这一个项时,默认为普通显示类型,即"General"）
	
	    public NET_SPLIT_MODE_INFO() {
	    	this.dwSize = this.size();
	    }
	} 
	
	// 分割能力， CLIENT_GetSplitCaps 参数
	public static class NET_SPLIT_CAPS extends Structure
	{
	    public int               dwSize;
	    public int               nModeCount;                                     // 支持的分割模式数量
	    public int[]             emSplitMode = new int[NET_MAX_SPLIT_MODE_NUM];  // 支持的分割模式, 见 NET_SPLIT_MODE
	    public int               nMaxSourceCount;                        // 最大显示源配置数
	    public int               nFreeWindowCount;                       // 支持的最大自由开窗数目
	    public int               bCollectionSupported;                   // 是否支持区块收藏, BOOL类型，0或1
	    public int               dwDisplayType;                          // 掩码表示多个显示类型,具体见NET_SPLIT_DISPLAY_TYPE（注释各模式下显示内容由"PicInPic"决定, 各模式下显示内容按NVD旧有规则决定（即DisChn字段决定）。兼容,没有这一个项时,默认为普通显示类型,即"General"）
	    public int               nPIPModeCount;                          // 画中画支持的分割模式数量
	    public int[]             emPIPSplitMode = new int[NET_MAX_SPLIT_MODE_NUM];  // 画中画支持的分割模式, 见 NET_SPLIT_MODE
	    public int[]             nInputChannels = new int[NET_SPLIT_INPUT_NUM];     // 支持的输入通道
	    public int               nInputChannelCount;                     // 支持的输入通道个数, 0表示没有输入通道限制
	    public int               nBootModeCount;                         // 启动分割模式数量
	    public int[]             emBootMode = new int[NET_MAX_SPLIT_MODE_NUM];      // 支持的启动默认画面分割模式, 见 NET_SPLIT_MODE
	    
	    public NET_SPLIT_CAPS() {
	    	this.dwSize = this.size();
	    }
	}
	
	// (设置显示源, 支持同时设置多个窗口)CLIENT_SplitSetMultiSource 接口的输入参数
	public static class NET_IN_SPLIT_SET_MULTI_SOURCE extends Structure
	{
	    public int              	dwSize;
	    public int  				emCtrlType;         // 视频输出控制方式,见 EM_VIDEO_OUT_CTRL_TYPE
	    public int              	nChannel;           // 视频输出逻辑通道号, emCtrlType为EM_VIDEO_OUT_CTRL_CHANNEL时有效
	    public String            	pszCompositeID;     // 拼接屏ID, emCtrlType为EM_VIDEO_OUT_CTRL_COMPOSITE_ID时有效
	    public int               	bSplitModeEnable;   // 是否改变分割模式, BOOL类型，0或1
	    public int               	emSplitMode;        // 分割模式, bSplitModeEnable=TRUE时有效,见 NET_SPLIT_MODE
	    public int               	nGroupID;           // 分割分组号, bSplitModeEnable=TRUE时有效
	    public Pointer             	pnWindows;          // 窗口号数组 int[],由用户申请内存，大小为sizeof(int)*nWindowCount
	    public int               	nWindowCount;       // 窗口数量
	    public Pointer	   		    pstuSources;        // 视频源信息, 分别对应每个窗口, 数量同窗口数  NET_SPLIT_SOURCE[] ,由用户申请内存，大小为sizeof(DH_SPLIT_SOURCE)*nWindowCount
	
	    public NET_IN_SPLIT_SET_MULTI_SOURCE() {
	    	this.dwSize = this.size();
	    }
	} 
	
	// (设置显示源, 支持同时设置多个窗口) CLIENT_SplitSetMultiSource 接口的输出参数
	public static class NET_OUT_SPLIT_SET_MULTI_SOURCE extends Structure
	{
	    public int                  dwSize;
	    
	    public NET_OUT_SPLIT_SET_MULTI_SOURCE() {
	    	this.dwSize = this.size();
	    }
	}
	
	// (下位矩阵切换) CLIENT_MatrixSwitch 输入参数
	public static class NET_IN_MATRIX_SWITCH extends Structure
	{
	    public int           dwSize;
	    public int           emSplitMode;                // 分割模式,见 NET_SPLIT_MODE
	    public Pointer       pnOutputChannels;           // 输出通道, 可同时指定多个输出通道一起切换, 内容一致
														 // 由用户申请内存 int[] ，大小为sizeof(int)*nOutputChannelCount
	    public int           nOutputChannelCount;        // 输出通道数
	    public Pointer       pnInputChannels;            // 输入通道, 每个分割窗口一个对应一个输入通道
														 // 由用户申请内存 int[] ，大小为sizeof(int)*nInputChannelCount
	    public int           nInputChannelCount;         // 输入通道数
	    
	    public NET_IN_MATRIX_SWITCH() {
	    	this.dwSize = this.size();
	    }
	}
	
	// (下位矩阵切换) CLIENT_MatrixSwitch 输出参数
	public static class NET_OUT_MATRIX_SWITCH extends Structure
	{
	    public int            dwSize;
	    
	    public NET_OUT_MATRIX_SWITCH() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 刻录模式
	public static class NET_BURN_MODE extends Structure
	{
	    public static final int BURN_MODE_SYNC 	= 0;                    // 同步
	    public static final int BURN_MODE_TURN 	= 1;                    // 轮流
	    public static final int BURN_MODE_CYCLE = 2;                    // 循环
	}
	
	// 刻录流格式
	public static class NET_BURN_RECORD_PACK extends Structure
	{
		public static final int BURN_PACK_DHAV = 0;                     // DHAV
		public static final int BURN_PACK_PS   = 1;                     // PS
		public static final int BURN_PACK_ASF  = 2;                     // ASF
		public static final int BURN_PACK_MP4  = 3;                     // MP4
		public static final int BURN_PACK_TS   = 4;                     // TS
	}
	
	// 刻录扩展模式
	public static class NET_BURN_EXTMODE extends Structure
	{
		public static final int BURN_EXTMODE_UNKNOWN = 0;			    // 未知
		public static final int BURN_EXTMODE_NORMAL  = 1;               // 正常刻录
		public static final int BURN_EXTMODE_NODISK  = 2;               // 无盘刻录
	}
	
	// (开始刻录) CLIENT_StartBurn 接口输入参数
	public static class NET_IN_START_BURN extends Structure
	{
	    public int             dwSize;
	    public int             dwDevMask;                             // 刻录设备掩码, 按位表示多个刻录设备组合
	    public int[]           nChannels = new int[NET_MAX_BURN_CHANNEL_NUM];    // 刻录通道数组
	    public int             nChannelCount;                         // 刻录通道数
	    public int             emMode;                                // 刻录模式,见  NET_BURN_MODE
	    public int             emPack;                                // 刻录流格式,见  NET_BURN_RECORD_PACK
	    public int   		   emExtMode;                             // 刻录扩展模式, 见  NET_BURN_EXTMODE
	    
	    public NET_IN_START_BURN() {
	    	this.dwSize = this.size();
	    }
	}
	
	// (开始刻录)CLIENT_StartBurn 接口输出参数
	public static class NET_OUT_START_BURN extends Structure
	{
	    public int               dwSize;
	    
	    public NET_OUT_START_BURN() {
	    	this.dwSize = this.size();
	    }
	} 
	
	// (打开会话)CLIENT_StartBurnSession 接口输入参数
	public static class NET_IN_START_BURN_SESSION extends Structure
	{
	    public int              dwSize;
	    public int              nSessionID;                         // 会话ID
	    
	    public NET_IN_START_BURN_SESSION() {
	    	this.dwSize = this.size();
	    }
	}
	
	// (打开会话)CLIENT_StartBurnSession 接口输出参数
	public static class NET_OUT_START_BURN_SESSION extends Structure
	{
	    public int               dwSize;
	    
	    public NET_OUT_START_BURN_SESSION() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 雷达监测超速报警事件 智能楼宇专用 ( NET_ALARM_RADAR_HIGH_SPEED )
	public static class ALARM_RADAR_HIGH_SPEED_INFO extends Structure
	{
	    public NET_TIME_EX             stuTime;  // 事件发生时间
	    public float                   fSpeed;                       // 速度(单位:km/h)
		public byte[]				   szPlateNumber = new byte[16]; // 车牌
	    public byte[]                  byReserved = new byte[1008];  // 预留字段 
	}
	
	// 设备巡检报警事件 智网专用 ( NET_ALARM_POLLING_ALARM )
	public static class ALARM_POLLING_ALARM_INFO extends Structure
	{
	    public NET_TIME_EX             stuTime;  // 事件发生时间
	    public byte[]                  byReserved = new byte[1024];           // 预留字段
	}
	
	// 门禁事件 ALARM_ACCESS_CTL_EVENT
	public static class ALARM_ACCESS_CTL_EVENT_INFO extends Structure {
	    public int                      dwSize;
	    public int                      nDoor;                              // 门通道号
	    public byte[]                   szDoorName = new byte[NET_MAX_DOORNAME_LEN];    // 门禁名称
	    public NET_TIME                 stuTime;                            // 报警事件发生的时间
	    public int   					emEventType;                        // 门禁事件类型 参考 NET_ACCESS_CTL_EVENT_TYPE
	    public int                      bStatus;                            // 刷卡结果,TRUE表示成功,FALSE表示失败
	    public int      				emCardType;                         // 卡类型, 参考 NET_ACCESSCTLCARD_TYPE
	    public int   					emOpenMethod;                       // 开门方式, 参考 NET_ACCESS_DOOROPEN_METHOD
	    public byte[]                   szCardNo = new byte[NET_MAX_CARDNO_LEN];        // 卡号
	    public byte[]                   szPwd = new byte[NET_MAX_CARDPWD_LEN];          // 密码
	    public byte[]                   szReaderID = new byte[NET_COMMON_STRING_32];    // 门读卡器ID
	    public byte[]                   szUserID = new byte[NET_COMMON_STRING_64];      // 开门用户
	    public byte[]                   szSnapURL = new byte[NET_COMMON_STRING_256];    // 抓拍照片存储地址
	    public int                      nErrorCode;                         // 开门失败的原因,仅在bStatus为FALSE时有效
	                                                                    // 0x00 没有错误
	                                                                    // 0x10 未授权
	                                                                    // 0x11 卡挂失或注销
	                                                                    // 0x12 没有该门权限
	                                                                    // 0x13 开门模式错误
	                                                                    // 0x14 有效期错误
	                                                                    // 0x15 防反潜模式
	                                                                    // 0x16 胁迫报警未打开
	                                                                    // 0x17 门常闭状态
	                                                                    // 0x18 AB互锁状态
	                                                                    // 0x19 巡逻卡
	                                                                    // 0x1A 设备处于闯入报警状态
	                                                                    // 0x20 时间段错误
	                                                                    // 0x21 假期内开门时间段错误
	                                                                    // 0x30 需要先验证有首卡权限的卡片
	                                                                    // 0x40 卡片正确,输入密码错误
	                                                                    // 0x41 卡片正确,输入密码超时
	                                                                    // 0x42 卡片正确,输入指纹错误
	                                                                    // 0x43 卡片正确,输入指纹超时
	                                                                    // 0x44 指纹正确,输入密码错误
	                                                                    // 0x45 指纹正确,输入密码超时
	                                                                    // 0x50 组合开门顺序错误
	                                                                    // 0x51 组合开门需要继续验证
	                                                                    // 0x60 验证通过,控制台未授权
	    public int                       nPunchingRecNo;                // 刷卡记录集中的记录编号
		public int						 nNumbers;					    // 抓图张数
	    public int					     emStatus;		                // 卡状态     NET_ACCESSCTLCARD_STATE
		public ALARM_ACCESS_CTL_EVENT_INFO() {
			super();
			this.dwSize = this.size();
		}
		@Override
		public String toString() {
			return "ALARM_ACCESS_CTL_EVENT_INFO [dwSize=" + dwSize + ", nDoor="
					+ nDoor + ", szDoorName=" + Arrays.toString(szDoorName)
					+ ", stuTime=" + stuTime + ", emEventType=" + emEventType
					+ ", bStatus=" + bStatus + ", emCardType=" + emCardType
					+ ", emOpenMethod=" + emOpenMethod + ", szCardNo="
					+ Arrays.toString(szCardNo) + ", szPwd="
					+ Arrays.toString(szPwd) + ", szReaderID="
					+ Arrays.toString(szReaderID) + ", szUserID="
					+ Arrays.toString(szUserID) + ", szSnapURL="
					+ Arrays.toString(szSnapURL) + ", nErrorCode=" + nErrorCode
					+ ", nPunchingRecNo=" + nPunchingRecNo + "]";
		}
	}
	
	// 消警事件
	public static class ALARM_ALARMCLEAR_INFO extends Structure
	{
		public int                    dwSize;
	    public int             		  nChannelID;                   // 通道号
	    public NET_TIME        		  stuTime;                      // 报警事件发生的时间
	    public int                    bEventAction;                 // 事件动作，0表示脉冲事件,1表示持续性事件开始,2表示持续性事件结束;
		public ALARM_ALARMCLEAR_INFO() {
			this.dwSize = this.size();
		}
		@Override
		public String toString() {
			return "ALARM_ALARMCLEAR_INFO [dwSize=" + dwSize + ", nChannelID="
					+ nChannelID + ", stuTime=" + stuTime + ", bEventAction="
					+ bEventAction + "]";
		}
	}
	
	public static class NET_ALARM_TYPE
	{
		public static final int NET_ALARM_LOCAL = 0;                //开关量防区的报警事件(对应 NET_ALARM_ALARM_EX2 事件)
		public static final int NET_ALARM_ALARMEXTENDED = 1;        //扩展模块报警事件(对应 NET_ALARM_ALARMEXTENDED 事件)
		public static final int NET_ALARM_TEMP = 2;                 //温度报警事件(对应 NET_ALARM_TEMPERATURE 事件)
		public static final int NET_ALARM_URGENCY = 3;              //紧急报警事件(对应 NET_URGENCY_ALARM_EX 事件)
		public static final int NET_ALARM_RCEMERGENCYCALL = 4;      //紧急呼叫报警事件(对应 NET_ALARM_RCEMERGENCY_CALL 事件)
		public static final int NET_ALARM_ALL = 5;                  //所有报警事件
	}
	
	// CLIENT_ControlDevice 接口的 NET_CTRL_CLEAR_ALARM 命令参数
	public static class NET_CTRL_CLEAR_ALARM extends Structure	{
		public int               	dwSize;
		public int                 	nChannelID;             // 防区通道号, -1 表示所有通道
		public int       			emAlarmType;            // 事件类型(支持的类型较少,建议用nEventType字段) NET_ALARM_TYPE
	    public String               szDevPwd;               // 登陆设备的密码,如不使用加密消警,直接赋值为NULL
	    public int                  bEventType;             // 表示是否启用nEventType字段, TRUE:nEventType代替emAlarmType字段, FALSE:沿用emAlarmType字段,忽略nEventType字段
	    public int                  nEventType;             // 事件类型, 对应 fMessCallBack 回调来上的lCommand字段, 即CLIENT_StartListenEx接口获得事件类型
	                                                		// 比如DH_ALARM_ALARM_EX2表示本地报警事件
	   public NET_CTRL_CLEAR_ALARM() {
		   this.dwSize = this.size();
	   }
	   @Override
	   public String toString() {
		   return "NET_CTRL_CLEAR_ALARM [dwSize=" + dwSize + ", nChannelID="
				+ nChannelID + ", emAlarmType=" + emAlarmType + ", szDevPwd="
				+ szDevPwd + ", bEventType=" + bEventType + ", nEventType="
				+ nEventType + "]";
	   }
	}
	
	// CLIENT_ControlDevice接口的 CTRLTYPE_CTRL_START_ALARMBELL / CTRLTYPE_CTRL_STOP_ALARMBELL命令参数
	public static class NET_CTRL_ALARMBELL extends Structure
	{
	    public int                    dwSize;
	    public int                    nChannelID;                   // 通道号(0开始)       
	    
	    public NET_CTRL_ALARMBELL(){
	    	this.dwSize = this.size();
	    }
	}
	
	// 警灯配置(对应 CFG_CMD_ALARMLAMP)
	public static class CFG_ALARMLAMP_INFO extends Structure
	{
	     public int     	         emAlarmLamp;                  // 警灯状态,参考  EM_ALARMLAMP_MODE
	}
	
	// 警灯状态
	public static class EM_ALARMLAMP_MODE extends Structure
	{
	    public static final int EM_ALARMLAMP_MODE_UNKNOWN = -1;    // 未知
	    public static final int EM_ALARMLAMP_MODE_OFF = 0;         // 灭
	    public static final int EM_ALARMLAMP_MODE_ON = 1;          // 亮
	    public static final int EM_ALARMLAMP_MODE_BLINK = 2;       // 闪烁
	}
	
	// 发送的通知类型,对应CLIENT_SendNotifyToDev接口
	public static class NET_EM_NOTIFY_TYPE extends Structure
	{
	    public static final int NET_EM_NOTIFY_PATROL_STATUS = 1;   // 发送巡更通知 (对应结构体 NET_IN_PATROL_STATUS_INFO, NET_OUT_PATROL_STATUS_INFO )
	} 
	
	// 巡更状态
	public static class NET_EM_PATROL_STATUS extends Structure
	{
		public static final int NET_EM_PATROL_STATUS_UNKNOWN = 0;  // 未知状态
		public static final int NET_EM_PATROL_STATUS_BEGIN = 1;    // 巡更开始
		public static final int NET_EM_PATROL_STATUS_END = 2;      // 巡更结束
		public static final int NET_EM_PATROL_STATUS_FAIL = 3;     // 巡更失败
	}
	
	// CLIENT_SendNotifyToDev 入参 (对应枚举 NET_EM_NOTIFY_PATROL_STATUS)
	public static class NET_IN_PATROL_STATUS_INFO extends Structure
	{
	    public int                    dwSize;                       // 结构体大小
	    public int                    emPatrolStatus;               // 巡更状态,参考  NET_EM_PATROL_STATUS
	    
	    public NET_IN_PATROL_STATUS_INFO() {
	    	this.dwSize = this.size();
	    }
	}
	
	// CLIENT_SendNotifyToDev 出参 (对应枚举 NET_EM_NOTIFY_PATROL_STATUS)
	public static class NET_OUT_PATROL_STATUS_INFO extends Structure
	{
	    public int                    dwSize;                       // 结构体大小
	    
	    public NET_OUT_PATROL_STATUS_INFO() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 报警事件类型  NET_ALARM_TALKING_INVITE (设备请求对方发起对讲事件)对应的数据描述信息
	public static class ALARM_TALKING_INVITE_INFO extends Structure
	{
	    public int                    dwSize;
	    public int                    emCaller;                                    // 设备希望的对讲发起方,取值参考   EM_TALKING_CALLER
	    public NET_TIME               stuTime;                   // 事件触发时间
	    public byte[]                 szCallID = new byte[NET_COMMON_STRING_64];   // 呼叫惟一标识符
	    public int                    nLevel;                         			   // 表示所呼叫设备所处层级
	    public TALKINGINVITE_REMOTEDEVICEINFO       stuRemoteDeviceInfo;   // 远端设备信息
		
	    public ALARM_TALKING_INVITE_INFO() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 对讲发起方
	public static class EM_TALKING_CALLER extends Structure
	{
	    public static final int EM_TALKING_CALLER_UNKNOWN  = 0;                   // 未知发起方
	    public static final int EM_TALKING_CALLER_PLATFORM = 1;                   // 对讲发起方为平台
	}
	
	// Invite事件远程设备协议
	public static class TALKINGINVITE_REMOTEDEVICE_PROTOCOL extends Structure
	{
		public static final int EM_TALKINGINVITE_REMOTEDEVICE_PROTOCOL_UNKNOWN   = 0;    // 未知
		public static final int EM_TALKINGINVITE_REMOTEDEVICE_PROTOCOL_HIKVISION = 1;    // 海康
	}
	
	// Invite事件远端设备信息
	public static class TALKINGINVITE_REMOTEDEVICEINFO extends Structure 
    {
	    public byte[]		         szIP = new byte[MAX_REMOTEDEVICEINFO_IPADDR_LEN];	        // 设备IP
		public int	                 nPort;					                                    // 端口
		public int                   emProtocol;                                                // 协议类型,取值参考 EM_TALKINGINVITE_REMOTEDEVICE_PROTOCOL
		public byte[]		         szUser = new byte[MAX_REMOTEDEVICEINFO_USERNAME_LEN];	    // 用户名
		public byte[]		         szPassword = new byte[MAX_REMOTEDEVICEINFO_USERPSW_LENGTH];// 密码
		public byte[]                szReverse = new byte[1024];                                // 保留字段	
    }
	
	// IO控制命令,对应 CLIENT_QueryIOControlState 接口  和  CLIENT_IOControl 接口
	public static class NET_IOTYPE extends Structure
	{
	    public static final int NET_ALARMINPUT = 1;                             // 控制报警输入,对应结构体为  ALARM_CONTROL
	    public static final int NET_ALARMOUTPUT = 2;                            // 控制报警输出，对应结构体为  ALARM_CONTROL
	    public static final int NET_DECODER_ALARMOUT = 3;                       // 控制报警解码器输出，对应结构体为  DECODER_ALARM_CONTROL
	    public static final int NET_WIRELESS_ALARMOUT = 5;                      // 控制无线报警输出，对应结构体为  ALARM_CONTROL
	    public static final int NET_ALARM_TRIGGER_MODE = 7;                     // 报警触发方式（手动,自动,关闭）,使用  TRIGGER_MODE_CONTROL 结构体
	} 
	
	// 报警IO控制
	public static class ALARM_CONTROL extends Structure 
	{
	    public short       			index;                    		// 端口序号
	    public short       			state;                   	    // 端口状态，0 - 关闭，1 - 打开
	} 
	
	// 报警解码器控制
	public static class DECODER_ALARM_CONTROL extends Structure
	{
	    public int                  decoderNo;               		// 报警解码器号,从0开始
	    public short      			alarmChn;                		// 报警输出口,从0开始
	    public short      		    alarmState;             	    // 报警输出状态；1：打开,0：关闭
	} 
    
	// 触发方式
	public static class TRIGGER_MODE_CONTROL extends Structure
	{
	    public short      			index;                    		// 端口序号
	    public short       			mode;                     		// 触发方式(0关闭1手动2自动);不设置的通道,sdk默认将保持原来的设置。
	    public byte[]     			bReserved = new byte[28];            
	} 
	
	// 报警输出通道的状态的配置, 对应 命令  CFG_CMD_ALARMOUT
	public static class CFG_ALARMOUT_INFO extends Structure		
	{
		public int					nChannelID;									 // 报警通道号(0开始)
		public byte[]				szChnName = new byte[MAX_CHANNELNAME_LEN];	 // 报警通道名称
		public byte[]				szOutputType = new byte[MAX_NAME_LEN];		 // 输出类型, 用户自定义
		public int					nOutputMode;								 // 输出模式, 0-自动报警, 1-强制报警, 2-关闭报警
	    public int                  nPulseDelay;                                 // 脉冲模式输出时间, 单位为秒(0-255秒)
	    public int                  nSlot;                                       // 根地址, 0表示本地通道, 1表示连接在第一个串口上的扩展通道, 2、3...以此类推, -1表示无效
	    public int                  nLevel1;                                     // 第一级级联地址, 表示连接在第nSlot串口上的第nLevel1个探测器或仪表, 从0开始, -1表示无效
	    public byte                 abLevel2;                                    // 类型为bool, 表示nLevel2字段是否存在
	    public int                  nLevel2;                                     // 第二级级联地址, 表示连接在第nLevel1个的仪表上的探测器序号, 从0开始
	}
	
	// 检测采集设备报警事件, 对应事件类型 NET_ALARM_SCADA_DEV_ALARM
	public static class ALARM_SCADA_DEV_INFO extends Structure
	{
	    public int                   dwSize;
	    public int                   nChannel;                                       // 通道号
	    public NET_TIME              stuTime;                       // 事件发生的时间
	    public byte[]                szDevName = new byte[NET_COMMON_STRING_64];     // 故障设备名称
	    public byte[]                szDesc = new byte[NET_COMMON_STRING_256];       // 报警描述
	    public int                   nAction;                            			 // -1:未知 0:脉冲 1:开始 2:停止
	    public byte[]                szID = new byte[NET_COMMON_STRING_32];          // 点位ID, 目前使用16字节
	    public byte[]                szSensorID = new byte[NET_COMMON_STRING_32];    // 探测器ID, 目前使用16字节
	    public byte[]                szDevID = new byte[NET_COMMON_STRING_32];       // 设备ID, 目前使用16字节
	    public byte[]                szPointName = new byte[NET_COMMON_STRING_64];   // 点位名,与点表匹配
	    public int                   nAlarmFlag;                         			 // 0:开始, 1:结束
	    
	    public ALARM_SCADA_DEV_INFO() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 点位类型
	public static class EM_NET_SCADA_POINT_TYPE extends Structure
	{
	    public static final int EM_NET_SCADA_POINT_TYPE_UNKNOWN = 0;                  // 未知
		public static final int EM_NET_SCADA_POINT_TYPE_ALL		= 1;                  // 所有类型
		public static final int EM_NET_SCADA_POINT_TYPE_YC      = 2;                  // 遥测 模拟量输入
		public static final int EM_NET_SCADA_POINT_TYPE_YX		= 3;                  // 遥信 开关量输入
		public static final int EM_NET_SCADA_POINT_TYPE_YT		= 4;                  // 遥调 模拟量输出
		public static final int EM_NET_SCADA_POINT_TYPE_YK		= 5;                  // 遥控 开关量输出
	} 
	
	// CLIENT_SCADAAttachInfo()接口输入参数
	public static class NET_IN_SCADA_ATTACH_INFO extends Structure
	{
	    public int                          dwSize;
	    public StdCallCallback				cbCallBack;                 // 数据回调函数, fSCADAAttachInfoCallBack 回调
	    public int         					emPointType;                // 点位类型,取值参考  EM_NET_SCADA_POINT_TYPE
	    public long                   		dwUser;                     // 用户定义参数
	    
	    public NET_IN_SCADA_ATTACH_INFO() {
	    	this.dwSize = this.size();
	    }
	} 
	
	// CLIENT_SCADAAttachInfo()接口输出参数
	public static class NET_OUT_SCADA_ATTACH_INFO extends Structure
	{
	    public int    					   dwSize;
	    
	    public NET_OUT_SCADA_ATTACH_INFO() {
	    	this.dwSize = this.size();
	    }
	} 
	
	// 监测点位信息列表
	public static class NET_SCADA_NOTIFY_POINT_INFO_LIST extends Structure
	{
	    public int                           	dwSize;
	    public int                              nList;                        // 监测点位信息个数
	    public NET_SCADA_NOTIFY_POINT_INFO[]    stuList = (NET_SCADA_NOTIFY_POINT_INFO[])new NET_SCADA_NOTIFY_POINT_INFO().toArray(MAX_SCADA_POINT_LIST_INFO_NUM); // 监测点位信息
	
	    public NET_SCADA_NOTIFY_POINT_INFO_LIST() {
	    	this.dwSize = this.size();
	    }
	} 
	
	// 监测点位信息
	public static class NET_SCADA_NOTIFY_POINT_INFO extends Structure
	{
	    public int                       		dwSize; 
	    public byte[]                        	szDevName = new byte[NET_COMMON_STRING_64];     // 设备名称,与getInfo获取的名称一致
	    public int     							emPointType;                        			// 点位类型,取值参考 EM_NET_SCADA_POINT_TYPE
	    public byte[]                        	szPointName = new byte[NET_COMMON_STRING_64];   // 点位名,与点位表的取值一致
	    public float                       		fValue;                            				// Type为YC时为浮点数
	    public int                         		nValue;                             			// Type为YX时为整数
	    public byte[]                        	szFSUID = new byte[NET_COMMON_STRING_64];       // 现场监控单元ID(Field Supervision Unit), 即设备本身
	    public byte[]                        	szID = new byte[NET_COMMON_STRING_64];          // 点位ID
	    public byte[]                        	szSensorID = new byte[NET_COMMON_STRING_64];    // 探测器ID
	    public NET_TIME_EX                 		stuCollectTime;             // 采集时间
	    
	    public NET_SCADA_NOTIFY_POINT_INFO() {
	    	this.dwSize = this.size();
	    }
	} 
	
	public static class CFG_TRAFFICSNAPSHOT_NEW_INFO extends Structure
	{
		public int									nCount;											// 有效成员个数
		public CFG_TRAFFICSNAPSHOT_INFO[]	stInfo = (CFG_TRAFFICSNAPSHOT_INFO[])new CFG_TRAFFICSNAPSHOT_INFO().toArray(8);		// 交通抓拍表数组
	}
	
	// CFG_CMD_INTELLECTIVETRAFFIC
	public static class CFG_TRAFFICSNAPSHOT_INFO extends Structure
	{
		public byte[]                   szDeviceAddress = new byte[MAX_DEVICE_ADDRESS];   	// 设备地址	UTF-8编码，256字节
		public int                      nVideoTitleMask;                      			  	// OSD叠加类型掩码	从低位到高位分别表示：0-时间 1-地点 2-车牌3-车长 4-车速 5-限速6-大车限速 7-小车限速8-超速 9-违法代码10-车道号 11-车身颜色 12-车牌类型 13-车牌颜色14-红灯点亮时间 15-违章类型 16-雷达方向 17-设备编号 18-标定到期时间 19-车型 20-行驶方向
		public int                      nRedLightMargin;                      			  	// 红灯冗余间隔时间	红灯开始的一段时间内，车辆通行不算闯红灯，单位：秒
		public float				    fLongVehicleLengthLevel;              			  	// 超长车长度最小阈值	单位：米，包含
		public float[]					arfLargeVehicleLengthLevel = new float[2];        	// 大车长度阈值	单位：米，包含小值
		public float[]					arfMediumVehicleLengthLevel = new float[2];       	// 中型车长度阈值	单位：米，包含小值
		public float[]					arfSmallVehicleLengthLevel = new float[2];        	// 小车长度阈值	单位：米，包含小值
		public float					fMotoVehicleLengthLevel;              			  	// 摩托车长度最大阈值	单位：米，不包含
		public BREAKINGSNAPTIMES_INFO   stBreakingSnapTimes; 								// 违章抓拍张数
		public DETECTOR_INFO[]          arstDetector = (DETECTOR_INFO[])new DETECTOR_INFO().toArray(MAX_DETECTOR);     // 车检器配置，下标是车道号
		public int 					 	nCarType;			               	   				// 抓拍车辆类型	0-大小车都抓拍1-抓拍小车2-抓拍大车3-大小车都不抓拍
		public int						nMaxSpeed;			               	   				// 当测得的速度超过最大速度时，则以最大速度计	0~255km/h
		public int						nFrameMode;		               	   					// 帧间隔模式	1-速度自适应（超过速度上限取0间隔，低于速度下限取2间隔，中间取1间隔）2-由联动参数决定
		public int[]                    arnAdaptiveSpeed = new int[2];                  	// 速度自适应下限和上限
		public CFG_ALARM_MSG_HANDLE     stuEventHandler;	    							// 交通抓拍联动参数
		public int                      abSchemeRange;                        				// BOOL类型,TRUE:方案针对相机,以车到0的值为准;FALSE:方案针对车道。不可以修改此字段数据, 只内部使用
	    public int                      nVideoTitleMask1;					   				// 从低位到高位分别表示：0-车标 1-红灯结束时间 2-设备制造厂商 3-小车低限速 4-大车低限速 5-小车高限速 6-大车高限速 7-设备工作模式 8-通用自定义 9-车道自定义 10-抓拍触发源 11-停车场区域12-车辆类型(面包车、轿车等等) 13-中车低限速 14-中车高限速 15-道路方向 16-GPS信息
	    public int				        nMergeVideoTitleMask;                 				// 合成图片OSD叠加类型掩码	参照nVideoTitleMask字段
	    public int				        nMergeVideoTitleMask1;				  				// 合成图片OSD叠加类型掩码	参照nVideoTitleMask1字段
	    public int                      nTriggerSource;					   					// 触发源掩码 0-RS232 1-RS485 2-IO 3-Video 4-Net
	    public int						nSnapMode;							   				// 抓拍模式 0-全部抓拍 1-超速抓拍 2-逆向抓拍 3-PK模式
	    public int                      nWorkMode;							   				// 工作模式 0-自动模式，1-线圈抓拍模式，2-线圈抓拍识别，3-视频抓拍，4-视频识别, 5-混合抓拍（带识别）
	    public int                      nCarThreShold;						   				// 车长阈值  区分大小车长的阈值，单位: cm
	    public int                      nSnapType;							   				// 抓拍或抓录选择 0-正常抓拍模式 1-视频抓拍模式 2-黑屏快抓模式
	    public int[]                    nCustomFrameInterval = new int[3];			   		// 自定义抓拍帧间隔 第一个元素指车速小于速度自适应下限时的抓拍帧间隔，依次类推
	    public int                      nKeepAlive;				           					// 与雷达、车检器的默认保活周期 单位秒
	    public OSD_INFO				    stOSD;												// 原始图片OSD参数配置
	    public OSD_INFO                 stMergeOSD;                       				 	// 合成图片OSD参数配置
	    public CFG_NET_TIME             stValidUntilTime;									// 标定到期时间，指该时间点之前抓拍照片有效
	    public RADAR_INFO               stRadar;
	    public byte[]                   szRoadwayCode = new byte[MAX_ROADWAYNO];        	// 道路代码
	    public int                      nVideoTitleMask2;					   				// 原始图片OSD叠加类型掩码2 从低位到高位分别表示：0-国别 1-尾气数据    
	    public int				        nMergeVideoTitleMask2;				   				// 合成图片OSD叠加类型掩码2 参照nVideoTitleMask2字段
		public int                      nParkType;                            				// 出入口类型，0-默认( 兼容以前，不区分出口/入口 )，1-入口相机， 2-出口相机
	}
	
	// 车检器配置
	public static class DETECTOR_INFO extends Structure
	{
	    public int                		nDetectBreaking;                   		 			// 违章类型掩码	从低位到高位依次是：0-正常1-闯红灯2-压线3-逆行4-欠速5-超速6-有车占道
	                                                         		   		 				// 7-黄牌占道 8-闯黄灯 9-违章占公交车道 10-不系安全带 11-驾驶员抽烟 12-驾驶员打电话
	    public COILCONFIG_INFO[]    	arstCoilCfg = (COILCONFIG_INFO[])new COILCONFIG_INFO().toArray(MAX_COILCONFIG);    // 线圈配置数组
		public int				    	nRoadwayNumber;				      		 			// 车道号	1-16 ; 0表示不启用
		public int                  	nRoadwayDirection;                		 			// 车道方向（车开往的方向）	0-南向北 1-西南向东北 2-东 3-西北向东南 4-北向南 5-东北向西南 6-东向西 7-东南向西北 8-忽略
		public int                  	nRedLightCardNum;                 		 			// 卡口图片序号	表示将电警中的某一张图片作为卡口图片（共三张），0表示不采用，1~3,表示采用对应序号的图片
		public int                  	nCoilsNumber;                     		 			// 线圈个数	1-3
		public int                  	nOperationType;                   		 			// 业务模式	0-卡口电警1-电警2-卡口
		public int[]                	arnCoilsDistance = new int[3];    		 			// 两两线圈的间隔	范围0-1000，单位为厘米
		public int                  	nCoilsWidth;                      		 			// 每个线圈的宽度	0~200cm
		public int[]                	arnSmallCarSpeedLimit = new int[2];      			// 小型车辆速度下限和上限	0~255km/h，不启用大小车限速时作为普通车辆限速
		public int[]					arnBigCarSpeedLimit = new int[2];        			// 大型车辆速度下限和上限	0~255km/h，启用大小车限速时有效
		public int				    	nOverSpeedMargin;			     	     			// 限高速宽限值	单位：km/h
		public int                  	nBigCarOverSpeedMargin;           	     			// 大车限高速宽限值	单位：km/h，启用大小车限速时有效
		public int				    	nUnderSpeedMargin;			     		 			// 限低速宽限值	单位：km/h
		public int                  	nBigCarUnderSpeedMargin;          		 			// 大车限低速宽限值	单位：km/h，启用大小车限速时有效
		public byte                 	bSpeedLimitForSize;               		 			// bool类型,是否启用大小车限速
		public byte			        	bMaskRetrograde;				     	 			// bool类型,逆行是否视为违章行为
		public byte[]               	byReserved = new byte[2];                			// 保留对齐
		public byte[]            		szDrivingDirection = new byte[3*MAX_DRIVINGDIRECTION]; // "DrivingDirection" : ["Approach", "上海", "杭州"],行驶方向
														     				 				// "Approach"-上行，即车辆离设备部署点越来越近；"Leave"-下行，
															 				 				// 即车辆离设备部署点越来越远，第二和第三个参数分别代表上行和
															 				 				// 下行的两个地点，UTF-8编码
		public int                  	nOverPercentage;                  		 			// 超速百分比，超过限速百分比后抓拍
		public int                  	nCarScheme;                       		 			// 具体的方案Index,具体方案含义参参考打包环境local.png;根据CFG_TRAFFICSNAPSHOT_INFO.abSchemeRange字段区分作用范围
		public int                  	nSigScheme;                       		 			// 同上，非卡口使用
		public int                  	bEnable;                          					// BOOL类型,车道是否有效，只有有效时才抓拍
		public int[]			    	nYellowSpeedLimit = new int[2];			 			//黄牌车限速上限和下限 范围0~255km/h
		public int				    	nRoadType;						 		 			//工作路段 0 普通公路 1 高速公路
		public int				    	nSnapMode;						 		 			//抓拍模式 0-全部抓拍 1-超速抓拍 2-逆向抓拍（鄞州项目用）
		public int                  	nDelayMode;						 		 			//延时抓拍方案 0-使DelaySnapshotDistance，1-使用DelayTime
	    public int                  	nDelayTime;						 		 			//延时抓拍时间 闯红灯第三张抓拍位置距离最后一个线圈的时间，单位毫秒
	    public int				    	nTriggerMode;					 		 			//触发模式 0-入线圈触发 1-出线圈触发 2-出入都抓拍 3-关闭
	    public int			        	nErrorRange;						 	 			//速度误差值，进线圈2与进线圈3之间的速度误差值，若实际误差大于或等于该值，视速度无效，否则取平均速度 0-20
	    public double			    	dSpeedCorrection;				 		 			//速度校正系数，即速度值为测出的值乘以该系数 
	    public int[]                	nDirection = new int[2];                 			//相对车道方向需要上报车辆行驶方向,nDirection[0] 0--空 1--正向 ; nDirection[1] 0--空 1--反向	
	    public byte[]               	szCustomParkNo = new byte[CFG_COMMON_STRING_32 + 1];// 自定义车位号（停车场用）
	    public byte[]               	btReserved = new byte[3];
	}
	
	// 线圈配置
	public static class COILCONFIG_INFO extends Structure
	{
		public int                 		nDelayFlashID;                 						// 延时闪光灯序号	每个线圈对应的延时闪关灯序号，范围0~5，0表示不延时任何闪光灯
		public int                 		nFlashSerialNum;               						// 闪光灯序号	范围0~5，0表示不打开闪光灯（鄞州项目用）
		public int                 		nRedDirection;                 						// 红灯方向	每个线圈对应的红灯方向：0-不关联,1-左转红灯,2-直行红灯,3-右转红灯,4-待左,5-待直,6-待右, 只在电警中有效
		public int                 		nTriggerMode ;                 						// 线圈触发模式	触发模式：0-入线圈触发1-出线圈触发（鄞州项目用）
		public int				   		nFlashSerialNum2;				 					//多抓第二张对应闪光灯序号 范围0~5，0表示不打开闪光灯
		public int				   		nFlashSerialNum3;				 					//多抓第三张对应闪光灯序号 范围0~5，0表示不打开闪光灯
	}
	
	// 违章抓拍张数
	public static class BREAKINGSNAPTIMES_INFO extends Structure
	{
		public int               		nNormal;                          					// 正常
		public int               		nRunRedLight;                     					// 闯红灯
		public int			      		nOverLine;											// 压线
		public int				  		nOverYellowLine;			  	    				// 压黄线
		public int				  		nRetrograde;			    	    				// 逆向
		public int				  		nUnderSpeed;					    				// 欠速
		public int				  		nOverSpeed;											// 超速
		public int			      		nWrongRunningRoute;									// 有车占道
		public int			      		nYellowInRoute;										// 黄牌占道
		public int		    	  		nSpecialRetrograde;									// 特殊逆行
		public int			      		nTurnLeft;											// 违章左转
		public int			      		nTurnRight;											// 违章右转
		public int			      		nCrossLane;											// 违章变道
		public int			      		nU_Turn;						    				// 违章调头
		public int			      		nParking;						    				// 违章停车
		public int               		nWaitingArea;										// 违章进入待行区
		public int			      		nWrongRoute;					   				    // 不按车道行驶		
		public int               		nParkingSpaceParking;             					// 车位有车
		public int               		nParkingSpaceNoParking;           					// 车位无车
		public int               		nRunYellowLight;									// 闯黄灯
		public int               		nStay;												// 违章停留
		public int               		nPedestrainPriority;	            				// 斑马线行人优先违章
		public int               		nVehicleInBusRoute;               					// 违章占道
		public int               		nBacking;                         					// 违章倒车
		public int				  		nOverStopLine;										// 压停止线
		public int               		nParkingOnYellowBox;           						// 黄网格线停车	
		public int               		nRestrictedPlate;									// 受限车牌	
		public int               		nNoPassing;											// 禁行	
		public int               		nWithoutSafeBelt;                					// 不系安全带
		public int               		nDriverSmoking;                   					// 驾驶员抽烟
		public int               		nDriverCalling;                   					// 驾驶员打电话
		public int               		nPedestrianRunRedLight;           					// 行人闯红灯
		public int               		nPassNotInOrder;                  					// 未按规定依次通行
	}

	//OSD属性
	public static class OSD_INFO extends Structure
	{
		public BLACK_REGION_INFO		stBackRegionInfo;									//OSD黑边属性
		public int						nOSDAttrScheme;		    							//OSD属性配置方案 0=未知 , 1=全体OSD项共用属性 , 2=每个OSD项一个属性
		public OSD_ATTR_SCHEME			stOSDAttrScheme;									//OSD属性配置方案内容
		public OSD_CUSTOM_SORT[]     	stOSDCustomSorts = (OSD_CUSTOM_SORT[])new OSD_CUSTOM_SORT().toArray(MAX_OSD_CUSTOM_SORT_NUM);    //OSD叠加内容自定义排序
		public int                 		nOSDCustomSortNum;
		public int                 		nRedLightTimeDisplay;  					 			//OSD红灯时间配置 0=未知,1=违法最后一张,2=所有张
		public byte                		cSeperater;             							//OSD不同项之间的分隔符
		public byte[]		        	bReserved = new byte[3];           					//字节对齐
		public byte[]                	szOSDOrder = new byte[MAX_CONF_CHAR];    
		public int                 		nOSDContentScheme;      							//0=未知, 1=Mask , 2=CustomizeSort
		public OSD_CUSTOM_INFO     		stOSDCustomInfo;        							//OSD自定义项
	}
	
	//OSD黑边
	public static class BLACK_REGION_INFO extends Structure
	{
		public int 						nHeight;											//黑边高度 取值范围：0 ~ ( 8192-原图片高度)
		public int 						nOSDPosition;										//黑边位置 0=未知 , 1=顶部 , 2=底部
	}
	
	//OSD属性配置方案内容
	public static class OSD_ATTR_SCHEME extends Structure 
	{
	    public OSD_WHOLE_ATTR 			stWholeAttr;										//全体OSD项共用属性
	}
	
	//全体OSD项共用属性
	public static class OSD_WHOLE_ATTR extends Structure
	{
	    public int        				bPositionAsBlackRegion;     						//BOOL类型,位置是否同黑边相同,true时，下面的Position无效,BOOL类型
	    public CFG_RECT    				stPostion;                  						//位置,不能超过图片范围
	    public int        				bNewLine;                   						//BOOL类型,超出矩形范围是否换行,bPositionAsBlackRegion为true时有效,BOOL类型
	    public int        				bLoneVehicle;               						//BOOL类型,车辆信息独立显示,true 一行显示一辆车信息,false 允许多辆车信息显示在一行,BOOL类型
	}
	
	//OSD叠加内容自定义排序
	public static class OSD_CUSTOM_SORT extends Structure
	{
	    public OSD_CUSTOM_ELEMENT[]   	stElements = (OSD_CUSTOM_ELEMENT[])new OSD_CUSTOM_ELEMENT().toArray(MAX_OSD_CUSTOM_SORT_ELEM_NUM);     //具体叠加元素
	    public int                  	nElementNum;
	}
	
	//OSD具体叠加元素
	public static class OSD_CUSTOM_ELEMENT extends Structure
	{
	    public int  					nNameType;                          				//名称类型,	0:szName字段含义参照szOSDOrder字段定义的项
	                                             											//          1:"Name"字段表示自定义项，无需解析
	    public byte[] 					szName = new byte[MAX_OSD_CUSTOM_VALUE_LEN];        // 该项名称
	    public byte[] 					szPrefix = new byte[MAX_PRE_POX_STR_LEN];      		// 叠加前缀字符串	
	    public byte[] 					szPostfix = new byte[MAX_PRE_POX_STR_LEN];     		//叠加后缀字符串
	    public int  					nSeperaterCount;                    				//后面添加分隔符个数
	    
	}
	
	//OSD自定义项
	public static class OSD_CUSTOM_INFO extends Structure
	{
	    public OSD_CUSTOM_GENERAL_INFO[]  stGeneralInfos = (OSD_CUSTOM_GENERAL_INFO[])new OSD_CUSTOM_GENERAL_INFO().toArray(MAX_OSD_CUSTOM_GENERAL_NUM);     //具体叠加元素
	    public int                        nGeneralInfoNum;
	}
	
	public static class OSD_CUSTOM_GENERAL_INFO extends Structure
	{
	    public int    					bEnable;            							//BOOL类型,是否叠加
	}
	
	public static class RADAR_INFO extends Structure
	{
	    public int     					nAngle;                 						//角度,用于修正雷达探头安装的角度造成的速度误差,范围[0,90]
	    public int     					nAntiJammingValue;      						//抗干扰门槛值
	    public int     					nComeInValue;           						//来向进入门槛值,取值范围[0,65535]
	    public int     					nComeOutValue;          						//来向离开门槛值
	    public int     					nDelayTime;             						//雷达延时,单位ms，范围[0,255]
	    public int   					nDetectBreaking;        						//违章类型掩码,从低位到高位依次是:
	                                    												//0-正常,1-闯红灯, 2-压线, 3-逆行,4-欠速
	                                    												//5-超速,6-有车占道,7-黄牌占道,8-闯黄灯,9-违章占公交车道
	    public int     					nDetectMode;            						//检测模式  0-车头检测 1-车尾检测 2-双向检测
	    public int     					nInstallMode;           						//雷达安装方式  0-侧面安装 1-上方安装
	    public int     					nLevel;                 						//灵敏度,0级灵敏度最高,范围[0,5]
	    public int     					nMultiTargetFilter;     						//多目标过滤模式,0-正常 1-过滤
	    public int     					nWentEndValue;          						//去向信号结束门槛值
	    public int     					nWentInValue;           						//去向进入门槛值
	    public int     					nWentOutValue;          						//去向离开门槛值
	}
	
	// 串口状态     		 
	public static class NET_COMM_STATE extends Structure
	{
	    public int        				uBeOpened; 										// 串口是否打开,0:未打开 1:打开. 
	    public int        				uBaudRate;										// 波特率, 1~8分别表示 1200 2400  4800 9600 19200 38400 57600 115200
	    public int        				uDataBites;										// 数据位，4~8表示4位~8位  
	    public int        				uStopBits;										// 停止位, 232串口 ： 数值0 代表停止位1; 数值1 代表停止位1.5; 数值2 代表停止位2.    485串口 ： 数值1 代表停止位1; 数值2 代表停止位2.
	    public int        				uParity;										// 检验, 0：无校验，1：奇校验；2：偶校验; 
	    public byte[]                	bReserved = new byte[32];
	}
	

	// 门禁卡记录查询条件
	public static class FIND_RECORD_ACCESSCTLCARD_CONDITION extends Structure
	{
	    public int                      dwSize;
	    public byte[]                   szCardNo = new byte[NET_MAX_CARDNO_LEN];       // 卡号
	    public byte[]                   szUserID = new byte[NET_MAX_USERID_LEN];       // 用户ID
	    public int                      bIsValid;                         			   // 是否有效, 1:有效, 0:无效 , boolean类型，为1或者0
	    public int                  	abCardNo;                         			   // 卡号查询条件是否有效,针对成员 szCardNo,boolean类型，为1或者0
	    public int                  	abUserID;                         			   // 用户ID查询条件是否有效,针对成员 szUserID, boolean类型，为1或者0
	    public int                  	abIsValid;                        			   // IsValid查询条件是否有效,针对成员 bIsValid, boolean类型，为1或者0
	    
	    public FIND_RECORD_ACCESSCTLCARD_CONDITION() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 门禁卡记录集信息
	public static class NET_RECORDSET_ACCESS_CTL_CARD extends Structure
	{
	    public int           		  dwSize;
	    public int             		  nRecNo;                                 		   // 记录集编号,只读
	    public NET_TIME        		  stuCreateTime = new NET_TIME();                  // 创建时间
	    public byte[]            	  szCardNo = new byte[NET_MAX_CARDNO_LEN];         // 卡号
	    public byte[]            	  szUserID = new byte[NET_MAX_USERID_LEN];         // 用户ID, 设备暂不支持
	    public int       			  emStatus;                 					   // 卡状态   NET_ACCESSCTLCARD_STATE
	    public int        			  emType;                        			       // 卡类型   NET_ACCESSCTLCARD_TYPE
	    public byte[]            	  szPsw = new byte[NET_MAX_CARDPWD_LEN];           // 卡密码
	    public int             		  nDoorNum;                               		   // 有效的门数目;
	    public int[]              	  sznDoors = new int[NET_MAX_DOOR_NUM];            // 有权限的门序号,即CFG_CMD_ACCESS_EVENT配置的数组下标
	    public int             		  nTimeSectionNum;                        		   // 有效的的开门时间段数目
	    public int[]             	  sznTimeSectionNo = new int[NET_MAX_TIMESECTION_NUM];  // 开门时间段索引,即CFG_ACCESS_TIMESCHEDULE_INFO的数组下标
	    public int             		  nUserTime;                              		   // 使用次数,仅当来宾卡时有效
	    public NET_TIME        		  stuValidStartTime = new NET_TIME();              // 开始有效期, 设备暂不支持时分秒
	    public NET_TIME        		  stuValidEndTime = new NET_TIME();                // 结束有效期, 设备暂不支持时分秒
	    public int            	  	  bIsValid;                               		   // 是否有效,1有效; 0无效, boolean类型，为1或者0
	    public NET_ACCESSCTLCARD_FINGERPRINT_PACKET stuFingerPrintInfo;			   	   // 下发指纹数据信息，仅为兼容性保留，请使用 stuFingerPrintInfoEx, 如果使用，内部的 pPacketData，请初始化
	    public int            	  	  bFirstEnter;                            		   // 是否拥有首卡权限, boolean类型，为1或者0
	    public byte[]            	  szCardName = new byte[NET_MAX_CARDNAME_LEN];     // 卡命名
	    public byte[]            	  szVTOPosition = new byte[NET_COMMON_STRING_64];  // 门口机关联位置
	    public int            	  	  bHandicap;                              		   // 是否为残疾人卡, boolean类型，为1或者0
	    public int            	  	  bEnableExtended;                        		   // 启用成员 stuFingerPrintInfoEx, boolean类型，为1或者0
	    public NET_ACCESSCTLCARD_FINGERPRINT_PACKET_EX     stuFingerPrintInfoEx;	   // 指纹数据信息, 如果使用，内部的 pPacketData，请初始化
	    public int             		  nFaceDataNum;                          		   // 人脸数据个数不超过20
	    public byte[]            	  szFaceData = new byte[MAX_FACE_DATA_NUM * MAX_FACE_DATA_LEN];	// 人脸模版数据
		public byte[]				  szDynamicCheckCode = new byte[MAX_COMMON_STRING_16];			// 动态校验码。
																					   // VTO等设备会保存此校验码，以后每次刷卡都以一定的算法生成新校验码并写入IC卡中，同时更新VTO设备的校验码，只有卡号和此校验码同时验证通过时才可开门。
		public int             		  nRepeatEnterRouteNum;                            // 反潜路径个数
		public int[]            	  arRepeatEnterRoute = new int[MAX_REPEATENTERROUTE_NUM]; // 反潜路径
		public int            		  nRepeatEnterRouteTimeout;                        // 反潜超时时间
		public int            		  bNewDoor;                                        // 是否启动新开门授权字段，TRUE表示使用nNewDoorNum和nNewDoors字段下发开门权限, BOOL类型
		public int             		  nNewDoorNum;                                     // 有效的门数目;
		public int[]             	  nNewDoors = new int[MAX_ACCESSDOOR_NUM];         // 有权限的门序号,即CFG_CMD_ACCESS_EVENT配置的数组下标
		public int             		  nNewTimeSectionNum;                              // 有效的的开门时间段数目
		public int[]             	  nNewTimeSectionNo = new int[MAX_ACCESSDOOR_NUM]; // 开门时间段索引,即CFG_ACCESS_TIMESCHEDULE_INFO的数组下标
		public byte[]				  szCitizenIDNo = new byte[MAX_COMMON_STRING_32];  // 身份证号码																			   // 缺点：目前方案只支持一卡刷一个设备。
		
		public NET_RECORDSET_ACCESS_CTL_CARD() {
			this.dwSize = this.size();
		}
	}
	
	// 卡状态
	public static class NET_ACCESSCTLCARD_STATE extends Structure
	{
	    public static final int NET_ACCESSCTLCARD_STATE_UNKNOWN = -1;
	    public static final int NET_ACCESSCTLCARD_STATE_NORMAL = 0;                 // 正常
	    public static final int NET_ACCESSCTLCARD_STATE_LOSE   = 0x01;              // 挂失
	    public static final int NET_ACCESSCTLCARD_STATE_LOGOFF = 0x02;              // 注销
	    public static final int NET_ACCESSCTLCARD_STATE_FREEZE = 0x04;              // 冻结
	    public static final int NET_ACCESSCTLCARD_STATE_ARREARAGE = 0x08;           // 欠费
	    public static final int NET_ACCESSCTLCARD_STATE_OVERDUE = 0x10;             // 逾期
	    public static final int NET_ACCESSCTLCARD_STATE_PREARREARAGE = 0x20;        // 预欠费(还是可以开门,但有语音提示)
	}
	
	// 指纹数据，只用于下发信息
	public static class NET_ACCESSCTLCARD_FINGERPRINT_PACKET extends Structure
	{
	    public int   			 dwSize; 
	    public int     			 nLength;        			// 单个数据包长度,单位字节
	    public int     			 nCount;         			// 包个数
	    public Pointer   		 pPacketData;    			// 所有指纹数据包，用户申请内存并填充，长度为 nLength*nCount
	    	
	    public NET_ACCESSCTLCARD_FINGERPRINT_PACKET() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 指纹数据扩展，可用于下发和获取信息
	public static class NET_ACCESSCTLCARD_FINGERPRINT_PACKET_EX extends Structure
	{
	    public int     			 nLength;        // 单个数据包长度,单位字节
	    public int     			 nCount;         // 包个数
	    public Pointer   		 pPacketData;    // 所有指纹数据包, 用户申请内存,大小至少为nLength * nCount
	    public int     		     nPacketLen;     // pPacketData 指向内存区的大小，用户填写
	    public int     			 nRealPacketLen; // 返回给用户实际指纹总大小
	    public byte[]    		 byReverseed = new byte[1024]; //保留大小
	}
	

	// 查询记录能力集能力集
	public static class CFG_CAP_RECORDFINDER_INFO extends Structure
	{
		public int nMaxPageSize;//最大分页条数
	}
	
	// 时间同步服务器配置
	public static class CFG_NTP_INFO extends Structure
	{
		public int					bEnable;									// 使能开关,BOOL类型
		public byte[]				szAddress = new byte[MAX_ADDRESS_LEN];		// IP地址或网络名
		public int					nPort;										// 端口号
		public int					nUpdatePeriod;								// 更新周期，单位为分钟
		public int 					emTimeZoneType;								// 时区, 参考 EM_CFG_TIME_ZONE_TYPE
		public byte[]				szTimeZoneDesc = new byte[MAX_NAME_LEN];	// 时区描述
	    public int                  nSandbyServerNum;                           // 实际备用NTP服务器个数
	    public CFG_NTP_SERVER[]     stuStandbyServer = (CFG_NTP_SERVER[])new CFG_NTP_SERVER().toArray(MAX_NTP_SERVER);  // 备选NTP服务器地址
	}
	
	// NTP服务器
	public static class CFG_NTP_SERVER extends Structure
	{
	    public int                	bEnable;   									// BOOL类型
	    public byte[]				szAddress = new byte[MAX_ADDRESS_LEN];		// IP地址或网络名
		public int					nPort;										// 端口号
	}
	
	// 时区定义(NTP)
	public static class EM_CFG_TIME_ZONE_TYPE extends Structure
	{
		public static final int EM_CFG_TIME_ZONE_0 = 0;								// {0, 0*3600,"GMT+00:00"}
		public static final int EM_CFG_TIME_ZONE_1 = 1;								// {1, 1*3600,"GMT+01:00"}
		public static final int EM_CFG_TIME_ZONE_2 = 2;								// {2, 2*3600,"GMT+02:00"}
		public static final int EM_CFG_TIME_ZONE_3 = 3;								// {3, 3*3600,"GMT+03:00"}
		public static final int EM_CFG_TIME_ZONE_4 = 4;								// {4, 3*3600+1800,"GMT+03:30"}
		public static final int EM_CFG_TIME_ZONE_5 = 5;								// {5, 4*3600,"GMT+04:00"}
		public static final int EM_CFG_TIME_ZONE_6 = 6;								// {6, 4*3600+1800,"GMT+04:30"}
		public static final int EM_CFG_TIME_ZONE_7 = 7;								// {7, 5*3600,"GMT+05:00"}
		public static final int EM_CFG_TIME_ZONE_8 = 8;								// {8, 5*3600+1800,"GMT+05:30"}
		public static final int EM_CFG_TIME_ZONE_9 = 9;								// {9, 5*3600+1800+900,"GMT+05:45"}
		public static final int EM_CFG_TIME_ZONE_10 = 10;							// {10, 6*3600,"GMT+06:00"}
		public static final int EM_CFG_TIME_ZONE_11 = 11;							// {11, 6*3600+1800,"GMT+06:30"}
		public static final int EM_CFG_TIME_ZONE_12 = 12;							// {12, 7*3600,"GMT+07:00"}
		public static final int EM_CFG_TIME_ZONE_13 = 13;							// {13, 8*3600,"GMT+08:00"}
		public static final int EM_CFG_TIME_ZONE_14 = 14;							// {14, 9*3600,"GMT+09:00"}
		public static final int EM_CFG_TIME_ZONE_15 = 15;							// {15, 9*3600+1800,"GMT+09:30"}
		public static final int EM_CFG_TIME_ZONE_16 = 16;							// {16, 10*3600,"GMT+10:00"}
		public static final int EM_CFG_TIME_ZONE_17 = 17;							// {17, 11*3600,"GMT+11:00"}
		public static final int EM_CFG_TIME_ZONE_18 = 18;							// {18, 12*3600,"GMT+12:00"}
		public static final int EM_CFG_TIME_ZONE_19 = 19;							// {19, 13*3600,"GMT+13:00"}
		public static final int EM_CFG_TIME_ZONE_20 = 20;							// {20, -1*3600,"GMT-01:00"}
		public static final int EM_CFG_TIME_ZONE_21 = 21;							// {21, -2*3600,"GMT-02:00"}
		public static final int EM_CFG_TIME_ZONE_22 = 22;							// {22, -3*3600,"GMT-03:00"}
		public static final int EM_CFG_TIME_ZONE_23 = 23;							// {23, -3*3600-1800,"GMT-03:30"}
		public static final int EM_CFG_TIME_ZONE_24 = 24;							// {24, -4*3600,"GMT-04:00"}
		public static final int EM_CFG_TIME_ZONE_25 = 25;							// {25, -5*3600,"GMT-05:00"}
		public static final int EM_CFG_TIME_ZONE_26 = 26;							// {26, -6*3600,"GMT-06:00"}
		public static final int EM_CFG_TIME_ZONE_27 = 27;							// {27, -7*3600,"GMT-07:00"}
		public static final int EM_CFG_TIME_ZONE_28 = 28;							// {28, -8*3600,"GMT-08:00"}
		public static final int EM_CFG_TIME_ZONE_29 = 29;							// {29, -9*3600,"GMT-09:00"}
		public static final int EM_CFG_TIME_ZONE_30 = 30;							// {30, -10*3600,"GMT-10:00"}
		public static final int EM_CFG_TIME_ZONE_31 = 31;							// {31, -11*3600,"GMT-11:00"}
		public static final int EM_CFG_TIME_ZONE_32 = 32;							// {32, -12*3600,"GMT-12:00"}
	}

	// 录像信息对应 CLIENT_FindFileEx 接口的 NET_FILE_QUERY_FILE 命令 查询条件
	// 目前支持通过路径查询
	public static class NET_IN_MEDIA_QUERY_FILE extends Structure  
	{
	    public int               	dwSize;                 // 结构体大小
	    public String            	szDirs;                 // 工作目录列表,一次可查询多个目录,为空表示查询所有目录。目录之间以分号分隔,如“/mnt/dvr/sda0;/mnt/dvr/sda1”,szDirs==null 或"" 表示查询所有
	    public int                  nMediaType;             // 文件类型,0:查询任意类型,1:查询jpg图片,2:查询dav
	    public int                  nChannelID;             // 通道号从0开始,-1表示查询所有通道
	    public NET_TIME             stuStartTime;           // 开始时间    
	    public NET_TIME             stuEndTime;             // 结束时间
	    public int[]                nEventLists = new int[MAX_IVS_EVENT_NUM]; // 事件类型列表,参见智能分析事件类型
	    public int                  nEventCount;            // 事件总数
	    public byte                 byVideoStream;          // 视频码流 0-未知 1-主码流 2-辅码流1 3-辅码流2 4-辅码流3 
	    public byte[]               bReserved 	= new byte[3];           // 字节对齐
	    public int[]     			emFalgLists = new int[EM_RECORD_SNAP_FLAG_TYPE.FLAG_TYPE_MAX]; // 录像或抓图文件标志, 不设置标志表示查询所有文件, 参考 EM_RECORD_SNAP_FLAG_TYPE
	    public int                  nFalgCount;             // 标志总数
	    public NET_RECORD_CARD_INFO stuCardInfo;            // 卡号录像信息, emFalgLists包含卡号录像时有效
	    public int                  nUserCount;             // 用户名有效个数
	    public byte[]               szUserName  = new byte[MAX_QUERY_USER_NUM * NET_NEW_USER_NAME_LENGTH]; // 用户名
	    public int 					emResultOrder;          // 查询结果排序方式, 参考 EM_RESULT_ORDER_TYPE
	    
	    public NET_IN_MEDIA_QUERY_FILE() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 录像信息对应 CLIENT_FindFileEx 接口的 NET_FILE_QUERY_FILE 命令 查询结果
	public static class NET_OUT_MEDIA_QUERY_FILE extends Structure  
	{
	    public int                 	 dwSize;                 // 结构体大小
	    public int                 	 nChannelID;             // 通道号从0开始,-1表示查询所有通道
	    public NET_TIME            	 stuStartTime;           // 开始时间    
	    public NET_TIME            	 stuEndTime;             // 结束时间
	    public int        		   	 nFileSize;              // 文件长度
	    public byte                	 byFileType;             // 文件类型 1:jpg图片, 2: dav
	    public byte                	 byDriveNo;              // 该字段已废弃,后续开发使用 nDriveNo成员
	    public byte                	 byPartition;            // 分区号
	    public byte                	 byVideoStream;          // 视频码流 0-未知 1-主码流 2-辅码流1 3-辅码流 4-辅码流 
	    public int        		   	 nCluster;               // 簇号
	    public byte[]              	 szFilePath = new byte[MAX_PATH];   // 文件路径
	    public int[]               	 nEventLists = new int[MAX_IVS_EVENT_NUM]; // 关联的事件列表,事件类型列表,参见智能分析事件类型
	    public int                 	 nEventCount;            //事件总数
	    public int[]  			   	 emFalgLists = new int[EM_RECORD_SNAP_FLAG_TYPE.FLAG_TYPE_MAX]; // 录像或抓图文件标志, 参考  EM_RECORD_SNAP_FLAG_TYPE
	    public int                 	 nFalgCount;             //标志总数
	    public int        		   	 nDriveNo;               // 磁盘号

	    //频浓缩文件相关信息
	    public byte[]			   	 szSynopsisPicPath = new byte[NET_COMMON_STRING_512]; // 预处理文件提取到的快照	文件路径
						                                                                  // 支持HTTP URL表示:"http://www.dahuate.com/1.jpg"
						                                                                  // 支持FTP URL表示: "ftp://ftp.dahuate.com/1.jpg"
						                                                                  // 支持服务器本地路径 
						                                                                  // a)"C:/pic/1.jpg" 
						                                                                  // b)"/mnt//2010/8/11/dav/15:40:50.jpg"
	    public int                 	 nSynopsisMaxTime;                      			  // 支持浓缩视频最大时间长度,单位 秒
	    public int                 	 nSynopsisMinTime;                     				  // 支持浓缩视频最小时间长度,单位 秒
	   
	    //文件摘要信息
	    public int                 	 nFileSummaryNum;                                     // 文件摘要信息数
	    public NET_FILE_SUMMARY_INFO[]   stFileSummaryInfo = (NET_FILE_SUMMARY_INFO[])new NET_FILE_SUMMARY_INFO().toArray(MAX_FILE_SUMMARY_NUM);   // 文件摘要信息    
	    
	    public NET_OUT_MEDIA_QUERY_FILE() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 卡号录像信息
	public static class NET_RECORD_CARD_INFO extends Structure
	{
	    public int               	 dwSize;
	    public int                   nType;                                          // 类型, 0-Card, 1-Field
	    public byte[]                szCardNo = new byte[NET_MAX_CARD_INFO_LEN];     // 卡号
	    public int   				 emTradeType;                                    // 交易类型, 参考 EM_ATM_TRADE_TYPE
	    public byte[]                szAmount = new byte[NET_COMMON_STRING_64];      // 交易金额, 空字符串表示不限金额
	    public int                   nError;                                         // 错误码, 0-所有错误, 1-吞钞, 2-吞卡
	    public int                   nFieldCount;                                    // 域数量, 按域查询时有效
	    public byte[]                szFields  = new byte[MAX_CARD_RECORD_FIELD_NUM * NET_COMMON_STRING_256];   // 域信息, 按域查询时有效
	    public byte[]				 szChange  = new byte[NET_COMMON_STRING_32];	 // 零钱
	    
	    public NET_RECORD_CARD_INFO() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 文件摘要信息
	public static class NET_FILE_SUMMARY_INFO extends Structure 
	{
	    public byte[]	 			 szKey = new byte[NET_COMMON_STRING_64];          // 摘要名称
	    public byte[] 	 			 szValue = new byte[NET_COMMON_STRING_512];       // 摘要内容
	    public byte[] 				 bReserved = new byte[256];                       // 保留字段   
	}
	
	// 录像或抓图文件标志
	public static class EM_RECORD_SNAP_FLAG_TYPE extends Structure
	{
	    public static final int FLAG_TYPE_TIMING  = 0;                  //定时文件
	    public static final int FLAG_TYPE_MANUAL  = 1;                  //手动文件
	    public static final int FLAG_TYPE_MARKED  = 2;                  //重要文件
	    public static final int FLAG_TYPE_EVENT   = 3;                  //事件文件
	    public static final int FLAG_TYPE_MOSAIC  = 4;                  //合成图片
	    public static final int FLAG_TYPE_CUTOUT  = 5;                  //抠图图片
	    public static final int FLAG_TYPE_LEAVE_WORD  = 6;              //留言文件
	    public static final int FLAG_TYPE_TALKBACK_LOCAL_SIDE  = 7;     //对讲本地方文件
	    public static final int FLAG_TYPE_TALKBACK_REMOTE_SIDE = 8;     //对讲远程方文件
	    public static final int FLAG_TYPE_SYNOPSIS_VIDEO  = 9;          //浓缩视频
	    public static final int FLAG_TYPE_ORIGINAL_VIDEO  = 10;         //原始视频
	    public static final int FLAG_TYPE_PRE_ORIGINAL_VIDEO = 11;      //已经预处理的原始视频
	    public static final int FLAG_TYPE_BLACK_PLATE  = 12;            //黑名单图片
	    public static final int FLAG_TYPE_ORIGINAL_PIC = 13;            //原始图片
	    public static final int FLAG_TYPE_CARD = 14;                    //卡号录像
	    public static final int FLAG_TYPE_MAX  = 128; 
	}
	
	// 交易类型
	public static class EM_ATM_TRADE_TYPE extends Structure
	{
		public static final int ATM_TRADE_ALL = 0;                      // 所有类型
		public static final int ATM_TRADE_ENQUIRY  = 1;                 // 查询
		public static final int ATM_TRADE_WITHDRAW = 2;                 // 取款
		public static final int ATM_TRADE_MODIFY_PASSWORD = 3;          // 修改密码
		public static final int ATM_TRADE_TRANSFER = 4;                 // 转账
		public static final int ATM_TRADE_DEPOSIT  = 5;                 // 存款
		public static final int ATM_TRADE_CARDLESS_ENQUIRY = 6;         // 无卡查询
		public static final int ATM_TRADE_CARDLESS_DEPOSIT = 7;         // 无卡存款
		public static final int ATM_TRADE_OTHER = 8;                    // 其他
	}
	
	// 查询结果排序方式
	public static class EM_RESULT_ORDER_TYPE extends Structure
	{
		public static final int EM_RESULT_ORDER_UNKNOWN = 0;            // 未知
		public static final int EM_RESULT_ORDER_ASCENT_BYTIME  = 1;     // 按时间升序排序
		public static final int EM_RESULT_ORDER_DESCENT_BYTIME = 2;     // 按时间降序排序   
	}
	
	// CLIENT_ControlDevice 接口的 CTRLTYPE_CTRL_START_VIDEO_ANALYSE 命令参数, 开始视频智能分析
	public static class NET_CTRL_START_VIDEO_ANALYSE extends Structure
	{
	    public int               dwSize; 
	    public int               nChannelId;             				// 通道号  
	    
	    public NET_CTRL_START_VIDEO_ANALYSE() {
	    	this.dwSize = this.size();
	    }
	}
	
	// CLIENT_ControlDevice 接口的 CTRLTYPE_CTRL_STOP_VIDEO_ANALYSE 命令参数, 停止视频智能分析
	public static class NET_CTRL_STOP_VIDEO_ANALYSE extends Structure
	{
	    public int          	dwSize; 
	    public int              nChannelId;             			  // 通道号  
	    
	    public NET_CTRL_STOP_VIDEO_ANALYSE() {
	    	this.dwSize = this.size();
	    }
	}
	
	// CLIENT_AttachVideoAnalyseState 接口输入参数
	public static class NET_IN_ATTACH_VIDEOANALYSE_STATE extends Structure
	{
	    public int              	dwSize;
	    public int                  nChannleId;            			// 通道号
	    public StdCallCallback      cbVideoAnalyseState;   			// 视频分析状态回调函数,fVideoAnalyseState 回调
	    public NativeLong           dwUser;                			// 用户信息 
	    
	    public NET_IN_ATTACH_VIDEOANALYSE_STATE() {
	    	this.dwSize = this.size();
	    }
	}
	
	// CLIENT_AttachVideoAnalyseState 接口输出参数
	public static class NET_OUT_ATTACH_VIDEOANALYSE_STATE extends Structure
	{
	    public int              	dwSize;
	    public long           		lAttachHandle;         			// 分析进度句柄,唯一标识某一通道的分析进度
	    
	    public NET_OUT_ATTACH_VIDEOANALYSE_STATE() {
	    	this.dwSize = this.size();
	    }
	}
	
	public static class NET_VIDEOANALYSE_STATE extends Structure
	{
	    public int            	   dwSize;
	    public int            	   dwProgress;                        			  // 分析进度,0-100
	    public byte[]              szState = new byte[NET_COMMON_STRING_64];      // 通道状态,Running"：运行,"Stop"：停止,"NoStart"：未启动,"Failed"：失败,"Successed"：成功
	    public byte[]              szFailedCode = new byte[NET_COMMON_STRING_64]; // 错误码
	    
	    public NET_VIDEOANALYSE_STATE() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 热成像火情报警信息上报事件, 对应事件  NET_ALARM_FIREWARNING_INFO
	public static class ALARM_FIREWARNING_INFO_DETAIL extends Structure 
	{
	    public int                       nChannel;                                             // 对应视频通道号
	    public int                       nWarningInfoCount;                                    // 报警信息个数
	    public NET_FIREWARNING_INFO[]    stuFireWarningInfo 
	    								 = new NET_FIREWARNING_INFO[MAX_FIREWARNING_INFO_NUM]; // 具体报警信息
	    public byte[]                    reserved = new byte[256];
		
		public ALARM_FIREWARNING_INFO_DETAIL() {
			for(int i = 0; i < stuFireWarningInfo.length; i++) { 
				stuFireWarningInfo[i] = new NET_FIREWARNING_INFO();
			}
		}
	}
	
	//热成像火情报警信息
	public static class NET_FIREWARNING_INFO extends Structure
	{
	    public int                 nPresetId;                          // 预置点编号	从测温规则配置 CFG_RADIOMETRY_RULE_INFO 中选择
	    public NET_RECT            stuBoundingBox;                     // 着火点矩形框	
	    public int                 nTemperatureUnit;                   // 温度单位(当前配置的温度单位),见 NET_TEMPERATURE_UNIT
	    public float               fTemperature;                       // 最高点温度值	同帧检测和差分检测提供
	    public int                 nDistance;                          // 着火点距离,单位米 0表示无效
	    public GPS_POINT           stuGpsPoint;                        // 着火点经纬度
	    public PTZ_POSITION_UNIT   stuPTZPosition;                     // 云台运行信息
	    public float               fAltitude;                          // 高度(单位：米)
	    public byte[]              reserved = new byte[208];
	}
	
	// 着火点经纬度
	public static class GPS_POINT extends Structure
	{
	    public int         		  dwLongitude;                         // 经度(单位是百万分之度,范围0-360度)如东经120.178274度表示为300178274
	    public int         		  dwLatidude;                          // 纬度(单位是百万分之度,范围0-180度)如北纬30.183382度表示为120183382
	                                                				   // 经纬度的具体转换方式可以参考结构体  NET_WIFI_GPS_INFO 中的注释
	}
	
	//云台控制坐标单元
	public static class PTZ_POSITION_UNIT extends Structure
	{
	    public int                 nPositionX;                        // 云台水平方向角度,归一化到-1~1
	    public int                 nPositionY;                        // 云台垂直方向角度,归一化到-1~1
	    public int                 nZoom;                             // 云台光圈放大倍率,归一化到 0~1
	    public byte[]              szReserve = new byte[32];          // 预留32字节
	}
	
	// 搜索到的地点信息
	public static class NET_WIFI_GPS_INFO extends Structure
	{
		public int	 			  emPositioningResult;			      // 定位结果, 参考   NET_GPS_POSITION_RESULT
		public int 				  nLongitude;				 		  // 经度(单位是百万分之一度)
																	  // 西经：0 - 180000000				实际值应为: 180*1000000 – dwLongitude
																	  // 东经：180000000 - 360000000		实际值应为: dwLongitude – 180*1000000
																	  // 如: 300168866应为（300168866 - 180*1000000）/1000000 即东经120.168866度
		public int				  nLatidude;				     	  // 纬度(单位是百万分之一度)
																	  // 南纬：0 - 90000000				实际值应为: 90*1000000 – dwLatidude
																	  // 北纬：90000000 – 180000000		实际值应为: dwLatidude – 90*1000000
																	  // 如: 120186268应为 (120186268 - 90*1000000)/1000000 即北纬30. 186268度
		public int				  nSpeed;					 		  // 速度, 单位千分之一km/H
		public byte[]			  reserved = new byte[112];	 		  // 保留字段
	} 
	
	// 定位结果
	public static class NET_GPS_POSITION_RESULT extends Structure
	{			
		public static final int NET_GPS_POSITION_RESULT_UNKNOWN = 0;  // 未知
		public static final int NET_GPS_POSITION_RESULT_FAILED = 1;	  // 有GPS数据,但定位失败,此时定位数据无意义
		public static final int NET_GPS_POSITION_RESULT_SUCCEED = 2;  // 有GPS数据,且定位成功,此时定位数据有意义
	} 
	
	// 温度单位
	public static class NET_TEMPERATURE_UNIT extends Structure
	{
		public static final int NET_TEMPERATURE_UNIT_UNKNOWN = 0;
		public static final int NET_TEMPERATURE_UNIT_CENTIGRADE = 1;  // 摄氏度
		public static final int NET_TEMPERATURE_UNIT_FAHRENHEIT = 2;  // 华氏度
	} 
	
	// 测温规则配置结构, 对应命令  CFG_CMD_THERMOMETRY_RULE
	public static class CFG_RADIOMETRY_RULE_INFO extends Structure
	{
	    public int                         nCount;                 				  // 规则个数
	    public CFG_RADIOMETRY_RULE[]       stRule = new CFG_RADIOMETRY_RULE[512]; // 测温规则
	    
	    public CFG_RADIOMETRY_RULE_INFO() {
	    	for(int i = 0; i < stRule.length; i++) {
	    		stRule[i] = new CFG_RADIOMETRY_RULE();
	    	}
	    }
	} 
	
	// 测温规则
	public static class CFG_RADIOMETRY_RULE extends Structure
	{
		public int                            bEnable;                // 测温使能, BOOL类型
		public int                            nPresetId;              // 预置点编号
		public int                            nRuleId;                // 规则编号
		public byte[]                         szName = new byte[128]; // 自定义名称
		public int                            nMeterType;             // 测温模式的类型，见 NET_RADIOMETRY_METERTYPE
		public CFG_POLYGON[]                  stCoordinates = new CFG_POLYGON[64];      // 测温点坐标	使用相对坐标体系，取值均为0~8191
		public int                         	  nCoordinateCnt;         // 测温点坐标实际个数
		public int                            nSamplePeriod;          // 温度采样周期	单位 : 秒
		public CFG_RADIOMETRY_ALARMSETTING[]  stAlarmSetting = new CFG_RADIOMETRY_ALARMSETTING[64];     // 测温点报警设置
		public int                            nAlarmSettingCnt;       // 测温点报警设置实际个数
		public CFG_RADIOMETRY_LOCALPARAM      stLocalParameters;      // 本地参数配置
		
		public CFG_RADIOMETRY_RULE() {
			for(int i = 0; i < stCoordinates.length; i++) {
				stCoordinates[i] = new CFG_POLYGON();
			}
			
			for(int i = 0; i < stAlarmSetting.length; i++) {
				stAlarmSetting[i] = new CFG_RADIOMETRY_ALARMSETTING();
			}
		}
	} 
	
	// 测温点报警设置
	public static class CFG_RADIOMETRY_ALARMSETTING extends Structure
	{
		public int                         nId;                    // 报警唯一编号	报警编号统一编码
		public int                         bEnable;                // 是否开启该点报警, BOOL类型
		public int                         nResultType;            // 测温报警结果类型，见 CFG_STATISTIC_TYPE，可取值：
	                                                        	   // 点测温：具体值，
	                                                        	   // 线测温：最大, 最小, 平均
	                                                        	   // 区域测温：最大, 最小, 平均, 标准, 中间, ISO
		public int                         nAlarmCondition;        // 报警条件，见 CFG_COMPARE_RESULT
		public float                       fThreshold;             // 报警阈值温度	浮点数
		public float                       fHysteresis;            // 温度误差，浮点数，比如0.1 表示正负误差在0.1范围内
		public int                         nDuration;              // 阈值温度持续时间	单位：秒
	} 
	
	// 测温规则本地参数配置
	public static class CFG_RADIOMETRY_LOCALPARAM extends Structure
	{
		public int                         bEnable;                // 是否启用本地配置, BOOL类型
		public float                       fObjectEmissivity;      // 目标辐射系数	浮点数 0~1
		public int                         nObjectDistance;        // 目标距离	
		public int                         nRefalectedTemp;        // 目标反射温度
	} 
	
	// 统计量类型
	public static class CFG_STATISTIC_TYPE extends Structure
	{
		public static final int CFG_STATISTIC_TYPE_UNKNOWN = 0;
		public static final int CFG_STATISTIC_TYPE_VAL = 1; 	   // 具体值
		public static final int CFG_STATISTIC_TYPE_MAX = 2; 	   // 最大
		public static final int CFG_STATISTIC_TYPE_MIN = 3; 	   // 最小
		public static final int CFG_STATISTIC_TYPE_AVR = 4; 	   // 平均
		public static final int CFG_STATISTIC_TYPE_STD = 5; 	   // 标准
		public static final int CFG_STATISTIC_TYPE_MID = 6; 	   // 中间
		public static final int CFG_STATISTIC_TYPE_ISO = 7; 	   // ISO
	} 
	
	// 比较运算结果
	public static class CFG_COMPARE_RESULT extends Structure
	{
		public static final int CFG_COMPARE_RESULT_UNKNOWN = 0;
		public static final int CFG_COMPARE_RESULT_BELOW = 1; 	  // 低于
		public static final int CFG_COMPARE_RESULT_MATCH = 2; 	  // 匹配
		public static final int CFG_COMPARE_RESULT_ABOVE = 3; 	  // 高于
	} 
	
	// 记录集新增操作(insert)参数
	public static class NET_CTRL_RECORDSET_INSERT_PARAM extends Structure
	{
	    public int                             dwSize;
	    public NET_CTRL_RECORDSET_INSERT_IN    stuCtrlRecordSetInfo;       // 记录集信息(用户填写)
	    public NET_CTRL_RECORDSET_INSERT_OUT   stuCtrlRecordSetResult;     // 记录集信息(设备返回)
	    
	    public NET_CTRL_RECORDSET_INSERT_PARAM() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 记录集新增操作(insert)输入参数
	public static class NET_CTRL_RECORDSET_INSERT_IN extends Structure
	{
	    public int          		 dwSize;
	    public int 	 				 emType;                             // 记录集信息类型, 取值参考  EM_NET_RECORD_TYPE
	    public Pointer          	 pBuf;                               // 记录集信息缓存,详见EM_NET_RECORD_TYPE注释，由用户申请内存.
		public int             		 nBufLen;                            // 记录集信息缓存大小,大小参照记录集信息类型对应的结构体
	
		public NET_CTRL_RECORDSET_INSERT_IN() {
			this.dwSize = this.size();
		}
	}
	
	// 记录集新增操作(insert)输出参数
	public static class NET_CTRL_RECORDSET_INSERT_OUT extends Structure
	{
	    public int           		dwSize;
	    public int             		nRecNo;                             // 记录编号(新增insert时设备返回)
	    
	    public NET_CTRL_RECORDSET_INSERT_OUT() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 门禁密码记录查询条件
	public static class FIND_RECORD_ACCESSCTLPWD_CONDITION extends Structure
	{
	    public int                     dwSize;
	    public byte[]                  szUserID = new byte[NET_MAX_USERID_LEN];      // 用户ID
	    
	    public FIND_RECORD_ACCESSCTLPWD_CONDITION() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 门禁密码记录集信息
	public static class NET_RECORDSET_ACCESS_CTL_PWD extends Structure
	{
	    public int           		  dwSize;
	    public int             		  nRecNo;                                 		      // 记录集编号,只读
	    public NET_TIME        		  stuCreateTime;                          			  // 创建时间
	    public byte[]            	  szUserID = new byte[NET_MAX_USERID_LEN];            // 用户ID, 设备暂不支持
	    public byte[]            	  szDoorOpenPwd = new byte[NET_MAX_CARDPWD_LEN];      // 开门密码
	    public byte[]            	  szAlarmPwd = new byte[NET_MAX_CARDPWD_LEN];         // 报警密码
	    public int             		  nDoorNum;                               			  // 有效的的门数目
	    public int[]             	  sznDoors = new int[NET_MAX_DOOR_NUM];               // 有权限的门序号,即 CFG_CMD_ACCESS_EVENT 配置CFG_ACCESS_EVENT_INFO的数组下标
	    public byte[]            	  szVTOPosition = new byte[NET_COMMON_STRING_64];     // 门口机关联位置
	    public int             		  nTimeSectionNum;                        			  // 开门时间段个数
	    public int[]                  nTimeSectionIndex = new int[NET_MAX_TIMESECTION_NUM];  // 开门时间段索引,是个数组，每个元素与sznDoors中的门对应
	    public int             		  bNewDoor;                               			  // BOOL类型, 是否启动新开门授权字段，TRUE表示使用 nNewDoorNum 和 nNewDoors 字段下发开门权限
	    public int             		  nNewDoorNum;                            			  // 有效的门数目;
	    public int[]                  nNewDoors = new int[MAX_ACCESSDOOR_NUM];            // 有权限的门序号,即 CFG_CMD_ACCESS_EVENT 配置的数组下标
	    public int             		  nNewTimeSectionNum;                    			  // 有效的的开门时间段数目
	    public int[]             	  nNewTimeSectionNo = new int[MAX_ACCESSDOOR_NUM];    // 开门时间段索引,即 CFG_ACCESS_TIMESCHEDULE_INFO 的数组下标
	    
	    public NET_RECORDSET_ACCESS_CTL_PWD() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 开门二维码记录集信息
	public static class NET_RECORD_ACCESSQRCODE_INFO extends Structure
	{
		public int                     dwSize;                                   // 结构体大小
		public int                     nRecNo;                                   // 记录集编号,只读
		public byte[]				   szQRCode = new byte[NET_MAX_QRCODE_LEN];  // 二维码
		public int					   nLeftTimes;                               // 剩余的有效次数
		public NET_TIME        		   stuStartTime;                             // 有效期开始时间
	    public NET_TIME        		   stuEndTime;                               // 有效期截止时间
	    
	    public NET_RECORD_ACCESSQRCODE_INFO(){
	    	this.dwSize = this.size();
	    }
	}
	
	// 查询盒子工作状态, 对应命令  NET_DEVSTATE_GET_WORK_STATE
	public static class NET_QUERY_WORK_STATE extends Structure
	{
	    public int                    dwSize;                            // 保留字段
	    public NET_WORKSTATE          stuWorkState;                      // 运行状态
	    
	    public NET_QUERY_WORK_STATE() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 设备工作状态
	public static class NET_WORKSTATE extends Structure
	{
	    public int                     bOnline;                           // 设备是否在线,BOOL类型
	    public byte[]                  szFirmwareVersion = new byte[NET_COMMON_STRING_128];  // 固件版本号
	    public float                   fTemperature;                      // 温度值, 单位摄氏度
	    public float                   fPowerDissipation;                 // 功耗, 单位W
	    public int                     nUtilizationOfCPU;                 // CPU 使用率
	    public int                     nStorageNum;                       // 存储设备个数
	    public NET_STORAGE_INFO[]      stuStorages = new NET_STORAGE_INFO[MAX_STORAGE_NUM];      // 存储设备信息
	    public int					   nUpTimeLast;						 // 上次上电时间, 单位: 秒
	    public int					   nUpTimeTotal;				     // 总共上电时间, 单位: 秒
	    public double				   dbMemInfoTotal;					 // 总内存大小, 单位: 字节
	    public double				   dbMemInfoFree;				     // 剩余内存大小, 单位: 字节
	    public byte[]				   byReserved1 = new byte[4];	     // 字节对齐，非保留字节
	    public byte[]				   szDevType = new byte[32];		 // 设备型号
	    public NET_RESOURCE_STATE	   stuResourceStat;					 // 网络资源
	    public byte[]                  byReserved = new byte[8];         // 保留字节
	    
	    public NET_WORKSTATE() {
	    	for(int i = 0; i < MAX_STORAGE_NUM; i++) {
	    		stuStorages[i] = new NET_STORAGE_INFO();
	    	}
	    }
	}
	
	// 存储设备信息
	public static class NET_STORAGE_INFO extends Structure
	{
	    public int      			    emState;                           // 存储设备状态, ENUM_STORAGE_STATE
	    public int                      nPartitonNum;                      // 分区个数
	    public NET_PARTITION_INFO[]     stuPartions = new NET_PARTITION_INFO[MAX_PARTITION_NUM];    // 分区信息
	    public byte[]                   byReserved = new byte[128];                   // 保留字段
	    
	    public NET_STORAGE_INFO() {
	    	for(int i = 0; i < MAX_PARTITION_NUM; i++) {
	    		stuPartions[i] = new NET_PARTITION_INFO();
	    	}
	    }
	}
	
	// 网络资源
	public static class NET_RESOURCE_STATE extends Structure
	{
		public int						nIPChanneIn;					// IP通道接入速度, 单位: kbps
		public int						nNetRemain;						// 网络接收剩余能力, 单位: kbps
		public int						nNetCapability;					// 网络接收总能力, 单位: kbps
		public int						nRemotePreview;					// 远程预览能力, 单位: kbps
		public int						nRmtPlayDownload;				// 远程回放及下载能力, 单位: kbps
		public int						nRemoteSendRemain;				// 远程发送剩余能力, 单位: kbps
		public int						nRemoteSendCapability;			// 远程发送总能力, 单位: kbps	
		public byte[]                   byReserved = new byte[32];      // 保留字节
	} 
	
	// 存储设备分区信息
	public static class NET_PARTITION_INFO extends Structure
	{
	    public double                    dbTotalBytes;                   // 分区总空间
	    public double                    dbUsedBytes;                    // 分区使用的空间
	    public int                       bError;                         // 是否异常, BOOL类型
	    public byte[]                    byReserved = new byte[64];      // 保留字段
	}
	
	// CLIENT_GetSelfCheckInfo 输入参数
	public static class NET_IN_GET_SELTCHECK_INFO extends Structure
	{
	    public int               		dwSize;                  				// 用户使用该结构体时,dwSize 需赋值为 sizeof (NET_IN_GET_SELTCHECK_INFO)
	    
	    public NET_IN_GET_SELTCHECK_INFO() {
	    	this.dwSize = this.size();
	    }
	} 
	
	// 设备自检信息
	public static class NET_SELFCHECK_INFO extends Structure
	{
	    public int             		  dwSize;
	    public int               	  nAlarmIn;                 // 报警输入通道数
	    public int               	  nAlarmOut;                // 报警输出通道数  
	    public NET_TIME          	  stuTime;                  // 上报时间
	    public byte[]                 szPlateNo = new byte[NET_MAX_PLATE_NUMBER_LEN]; // 车牌
	    public byte[]                 szICCID = new byte[NET_MAX_SIM_LEN];  // SIM卡号,建议使用szICCIDExInfo字段
	    public byte              	  byOrientation;            // 定位状态,0-未定位,1-定位 
	    public byte              	  byACCState;               // ACC 状态,0-关闭,1-打开
	    public byte               	  byConstantElecState;      // 常电状态,0-正常连接,1-断开,2-欠压,3-高压
	    public byte              	  byAntennaState;           // 通信信号状态,0-正常,1-未知故障,2-未接,3-短路
	    
	    // 外部设备状态
	    public byte              	  byReportStation;          // 报站器状态,0-未接,1-正常,2-异常
	    public byte              	  byControlScreen;          // 调度屏状态,0-未接,1-正常,2-异常
	    public byte              	  byPOS;                    // POS机状态,0-未接,1-正常,2-异常
	    public byte              	  byCoinBox;                // 投币箱状态,0-未接,1-正常,2-异常
	    
	    // 能力集
	    public int              	  bTimerSnap;               // 定时抓图,TRUE-支持,FALSE-不支持, BOOL类型
	    public int              	  bElectronEnclosure;       // 电子围栏,TRUE-支持,FALSE-不支持, BOOL类型
	    public int              	  bTeleUpgrade;             // 远程升级,TRUE-支持,FALSE-不支持, BOOL类型  
	   
	    public int               	  nHddNum;                  // 硬盘个数
	    public NET_HDD_STATE[]        stuHddStates = new NET_HDD_STATE[NET_MAX_DISKNUM]; // 硬盘状态
	    
	    public Pointer 				  pChannleState;           // 通道状态,是一个 NET_CHANNLE_STATE 数组, 
	                                                		   // CLIENT_AttachMission接口,NET_MISSION_TYPE_SELFCHECK类型,回调函数,内存由SDK申请,SDK释放
	                                                		   // CLIENT_GetSelfCheckInfo接口,出参,内存由用户申请,用户释放,大小为sizeof(NET_CHANNLE_STATE)*nChannelMax
	    public int               	  nChannleNum;             // 实际上报的通道个数
	    public int               	  nChannelMax;             // CLIENT_GetSelfCheckInfo接口,pChannleState内存的最大NET_CHANNLE_STATE个数
	    public int         			  emConnState;             // PAD/DVR连接状态, 参考 NET_PAD_CONNECT_STATE
	    public int                    emHomeState;             // Home键状态， 参考  NET_HOME_STATE
		public byte[]              	  szICCIDExInfo = new byte[NET_COMMON_STRING_256];  // SIM卡号扩展信息，用于字段扩展使用
		public byte              	  by3GState;                // 3G/4G状态, 0-未连接，1-连接，2-模块未找到
		public byte              	  byWifiState;              // Wifi状态, 0-未连接，1-连接，2-模块未找到
		public byte             	  byGpsState;               // Gps状态, 0-未连接，1-连接，2-模块未找到
		public byte              	  byBlackBoxState;          // BlackBox状态, 0-未接，1-正常
		public int               	  nCpuUsage;                // CPU使用百分比, 单位%
		public int               	  nTemperature;             // 设备内部温度, 摄氏度
		
		public NET_SELFCHECK_INFO() {
			this.dwSize = this.size();
			
			for(int i = 0; i < NET_MAX_DISKNUM; i++) {
				stuHddStates[i] = new NET_HDD_STATE();
			}
		}
	}
	
	// 硬盘状态
	public static class NET_HDD_STATE extends Structure
	{
	    public int             		  dwSize; 
	    public int               	  nState;                   // 硬盘状态,0-正常,1-错误   
	    public double            	  dbTotalSize;              // 硬盘总容量,字节为单位
	    public NET_PARTITION_STATE[]  stuPartitions = new NET_PARTITION_STATE[NET_MAX_STORAGE_PARTITION_NUM]; // 分区状态
	    public int               	  nPartitionNum;            // 分区数
	    
	    public NET_HDD_STATE() {
	    	this.dwSize = this.size();
	    	
	    	for(int i = 0; i < NET_MAX_STORAGE_PARTITION_NUM; i++) {
	    		stuPartitions[i] = new NET_PARTITION_STATE();
	    	}
	    }
	}
	
	// 分区状态
	public static class NET_PARTITION_STATE extends Structure
	{
	    public int             		 dwSize;
	    public int               	 nStatus;                  // 分区状态,0-正常,1-错误 
	    public double            	 dbTotalSize;              // 分区总容量,字节为单位
	    public double            	 dbRemainSize;             // 剩余容量,字节为单位
	    
	    public NET_PARTITION_STATE() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 扩展网络配置结构体
	public static class NETDEV_NET_CFG_EX extends Structure
	{ 
	    public int               	dwSize; 
	    public byte[]               sDevName = new byte[NET_MAX_NAME_LEN];  // 设备主机名
	    public short                wTcpMaxConnectNum;                  	// TCP最大连接数
	    public short                wTcpPort;                           	// TCP帧听端口
	    public short                wUdpPort;                           	// UDP侦听端口
	    public short                wHttpPort;                          	// HTTP端口号
	    public short                wHttpsPort;                         	// HTTPS端口号
	    public short                wSslPort;                           	// SSL端口号
	    public int                  nEtherNetNum;                      	 	// 以太网口数
	    public NET_ETHERNET_EX[]    stEtherNet = new NET_ETHERNET_EX[NET_MAX_ETHERNET_NUM_EX]; // 以太网口
	    public NET_REMOTE_HOST      struAlarmHost;                      	// 报警服务器
	    public NET_REMOTE_HOST      struLogHost;                        	// 日志服务器
	    public NET_REMOTE_HOST      struSmtpHost;                       	// SMTP服务器
	    public NET_REMOTE_HOST      struMultiCast;                      	// 多播组
	    public NET_REMOTE_HOST      struNfs;                            	// NFS服务器
	    public NET_REMOTE_HOST      struPppoe;                          	// PPPoE服务器
	    public byte[]               sPppoeIP = new byte[NET_MAX_IPADDR_LEN]; // PPPoE注册返回的IP
	    public NET_REMOTE_HOST      struDdns;                           	// DDNS服务器
	    public byte[]               sDdnsHostName = new byte[NET_MAX_HOST_NAMELEN]; // DDNS主机名
	    public NET_REMOTE_HOST      struDns;                            	// DNS服务器
	    public NET_MAIL_CFG         struMail;                           	// 邮件配置
	    public byte[]               bReserved = new byte[128];          	// 保留字节
	    
	    public NETDEV_NET_CFG_EX() {
	    	this.dwSize = this.size();
	    	
	    	for(int i = 0; i < NET_MAX_ETHERNET_NUM_EX; i++) {
	    		stEtherNet[i] = new NET_ETHERNET_EX();
	    	}
	    }
	} 
		
	// 以太网扩展配置
	public static class NET_ETHERNET_EX extends Structure
	{
		public byte[]                sDevIPAddr = new byte[NET_MAX_IPADDR_LEN];      // DVR IP 地址
		public byte[]                sDevIPMask = new byte[NET_MAX_IPADDR_LEN];      // DVR IP 地址掩码
		public byte[]                sGatewayIP = new byte[NET_MAX_IPADDR_LEN];      // 网关地址

	    /*
	     * 1：10Mbps 全双工
	     * 2：10Mbps 自适应
	     * 3：10Mbps 半双工
	     * 4：100Mbps 全双工
	     * 5：100Mbps 自适应
	     * 6：100Mbps 半双工
	     * 7：自适应
	     */
	    // 为了扩展将DWORD拆成四个
		public byte                dwNetInterface;                     				// NSP
	    public byte                bTranMedia;                         				// 0：有线,1：无线
	    public byte                bValid;                             				// 按位表示,第一位：1：有效 0：无效；第二位：0：DHCP关闭 1：DHCP使能；第三位：0：不支持DHCP 1：支持DHCP
	    public byte                bDefaultEth;                        				// 是否作为默认的网卡 1：默认 0：非默认
	    public byte[]              byMACAddr = new byte[NET_MACADDR_LEN];           // MAC地址,只读
	    public byte                bMode;                              				// 网卡所处模式, 0:绑定模式, 1:负载均衡模式, 2:多址模式, 3:容错模式
	    public byte[]              bReserved1 = new byte[3];                        // 字节对齐
	    public byte[]              szEthernetName = new byte[NET_MAX_NAME_LEN];     // 网卡名,只读
	    public byte[]              bReserved = new byte[12];                        // 保留字节   
	} 
	

	// 远程主机配置
	public static class NET_REMOTE_HOST extends Structure
	{
	    public byte                byEnable;                           				// 连接使能
	    public byte                byAssistant;                        				// 目前只对于PPPoE服务器有用,0：在有线网卡拨号；1：在无线网卡上拨号
	    public short               wHostPort;                         				// 远程主机 端口
	    public byte[]              sHostIPAddr = new byte[NET_MAX_IPADDR_LEN];      // 远程主机 IP 地址        
	    public byte[]              sHostUser = new byte[NET_MAX_HOST_NAMELEN];      // 远程主机 用户名
	    public byte[]              sHostPassword = new byte[NET_MAX_HOST_PSWLEN];   // 远程主机 密码
	} 
	
	// 邮件配置
	public static class NET_MAIL_CFG extends Structure
	{
		public byte[]               sMailIPAddr = new byte[NET_MAX_IPADDR_LEN];     // 邮件服务器IP地址
		public short                wMailPort;                          			// 邮件服务器端口
	    public short                wReserved;                         				// 保留
	    public byte[]               sSenderAddr = new byte[NET_MAX_MAIL_ADDR_LEN];  // 发送地址
	    public byte[]               sUserName = new byte[NET_MAX_NAME_LEN];         // 用户名
	    public byte[]               sUserPsw = new byte[NET_MAX_NAME_LEN];          // 用户密码
	    public byte[]               sDestAddr = new byte[NET_MAX_MAIL_ADDR_LEN];    // 目的地址
	    public byte[]               sCcAddr = new byte[NET_MAX_MAIL_ADDR_LEN];      // 抄送地址
	    public byte[]               sBccAddr = new byte[NET_MAX_MAIL_ADDR_LEN];     // 暗抄地址
	    public byte[]               sSubject = new byte[NET_MAX_MAIL_SUBJECT_LEN];  // 标题
	} 
	
	// 向视频输出口投放视频和图片文件, CLIENT_ControlDevice接口的 CTRLTYPE_CTRL_DELIVERY_FILE 命令参数 
	public static class NET_CTRL_DELIVERY_FILE extends Structure 
	{
	    public int                  	 dwSize;                             // 结构体大小
	    public int                    	 nPort;                              // 视频输出口
	    public int 						 emPlayMode;                         // 播放类型, 参考  EM_VIDEO_PLAY_MODE_TYPE
	    public NET_TIME                	 stuStartPlayTime;                   // 开始播放的时间
	    public NET_TIME                	 stuStopPlayTime;                    // 结束播放的时间，emPlayMode为 EM_VIDEO_PLAY_MODE_TYPE_REPEAT 时，此值有效    
	    public int                     	 nFileCount;                         // 投放的文件个数
	    public NET_DELIVERY_FILE_INFO[]  stuFileInfo = new NET_DELIVERY_FILE_INFO[MAX_DELIVERY_FILE_NUM]; // 投放的文件信息
	    
	    public NET_CTRL_DELIVERY_FILE() {
	    	this.dwSize = this.size();
	    }
	}

	// 投放文件信息
	public static class NET_DELIVERY_FILE_INFO extends Structure
	{
	    public int   				emFileType;                                  // 文件类型, 参考 EM_DELIVERY_FILE_TYPE
	    public byte[]               szFileURL = new byte[DELIVERY_FILE_URL_LEN]; // 文件的资源地址
	    public int                  nImageSustain;                               // 每张图片停留多长时间，单位秒 (emFileType为 EM_DELIVERY_FILE_TYPE_IMAGE 时此字段有效)
	    public byte[]               byReserved = new byte[1024];                 // 保留字节
	}
	
	// 视频播放模式
	public static class EM_VIDEO_PLAY_MODE_TYPE extends Structure
	{
	    public static final int EM_VIDEO_PLAY_MODE_TYPE_UNKNOWN = 0;              // 未知
	    public static final int EM_VIDEO_PLAY_MODE_TYPE_ONCE = 1;                 // 播放一次
	    public static final int EM_VIDEO_PLAY_MODE_TYPE_REPEAT = 2;               // 循环播放
	}
	
	// 投放的文件类型
	public static class EM_DELIVERY_FILE_TYPE extends Structure
	{
		public static final int EM_DELIVERY_FILE_TYPE_UNKNOWN = 0;                // 未知
		public static final int EM_DELIVERY_FILE_TYPE_VIDEO = 1;                  // 视频
		public static final int EM_DELIVERY_FILE_TYPE_IMAGE = 2;                  // 图片
	}
	
	// CLIENT_ControlDevice接口的 CTRLTYPE_CTRL_START_PLAYAUDIO 命令参数
	public static class NET_CTRL_START_PLAYAUDIO extends Structure
	{
	    public int               dwSize;
	    public byte[]            szAudioPath = new byte[NET_MAX_AUDIO_PATH];
	    
	    public NET_CTRL_START_PLAYAUDIO() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 公告记录信息查询条件
	public static class FIND_RECORD_ANNOUNCEMENT_CONDITION extends Structure
	{
		public int                  dwSize;
		public int                  bTimeEnable;                      // 启用时间段查询, BOOL类型
		public NET_TIME             stStartTime;                      // 起始时间
		public NET_TIME             stEndTime;                        // 结束时间
		
		public FIND_RECORD_ANNOUNCEMENT_CONDITION() {
			this.dwSize = this.size();
		}
	}
	
	//公告记录信息
	public static class NET_RECORD_ANNOUNCEMENT_INFO extends Structure
	{
		public int					dwSize;									
		public int					nRecNo;									   		    // 记录集编号,只读
		public NET_TIME             stuCreateTime;                          			// 创建时间
		public NET_TIME				stuIssueTime;										// 公告发布时间
		public byte[]				szAnnounceTitle = new byte[NET_COMMON_STRING_64];	// 公告标题
		public byte[]				szAnnounceContent = new byte[NET_COMMON_STRING_256];//公告内容
		public byte[]				szAnnounceDoor = new byte[NET_COMMON_STRING_16];	//公告要发送的房间号
		public NET_TIME				stuExpireTime;										//公告过期的时间
		public int			 		emAnnounceState;									//公告的状态 , 参考 NET_ANNOUNCE_STATE
		public int		 			emAnnounceReadFlag;									//公告是否已经浏览, 参考 NET_ANNOUNCE_READFLAG
		
		public NET_RECORD_ANNOUNCEMENT_INFO() {
			this.dwSize = this.size();
		}
	}
	
	//公告的状态
	public static class NET_ANNOUNCE_STATE extends Structure
	{
		public static final int NET_ANNOUNCE_STATE_UNSENDED = 0;    //初始状态(未发送)
		public static final int NET_ANNOUNCE_STATE_SENDED = 1;		//已经发送
		public static final int NET_ANNOUNCE_STATE_EXPIRED = 2;		//已经过期
		public static final int NET_ANNOUNCE_STATE_UNKNOWN = 3;		//未知
	}
	
	//公告是否已经浏览
	public static class NET_ANNOUNCE_READFLAG extends Structure
	{
		public static final int NET_ANNOUNCE_READFLAG_UNREADED = 0; //未读
		public static final int NET_ANNOUNCE_READFLAG_READED = 1;	//已读
		public static final int NET_ANNOUNCE_READFLAG_UNKNOWN = 2;  //未知
	}
	
	// 开始实时监视并指定回调数据格式入参
	public static class NET_IN_REALPLAY_BY_DATA_TYPE extends Structure
	{
	    public int               		dwSize;                 // 结构体大小
	    public int                  	nChannelID;             // 通道编号
	    public Pointer              	hWnd;                   // 窗口句柄, HWND类型
	    public int     					rType;                  // 码流类型 ，参考  NET_RealPlayType  
	    public fRealDataCallBackEx  	cbRealData;             // 数据回调函数
	    public int   					emDataType;             // 回调的数据类型，参考 EM_REAL_DATA_TYPE
	    public long           			dwUser;                 // 用户数据, Win32时，要改成NativeLong类型
	    public String         			szSaveFileName;         // 转换后的文件名
	    
	    public NET_IN_REALPLAY_BY_DATA_TYPE() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 开始实时监视并指定回调数据格式出参
	public static class NET_OUT_REALPLAY_BY_DATA_TYPE extends Structure
	{
	    public int               		dwSize;                 // 结构体大小  
	    
	    public NET_OUT_REALPLAY_BY_DATA_TYPE() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 实时监视回调数据类型
	public static class EM_REAL_DATA_TYPE extends Structure
	{
	    public static final int EM_REAL_DATA_TYPE_PRIVATE = 0;       // 私有码流
	    public static final int EM_REAL_DATA_TYPE_GBPS = 1;          // 国标PS码流
	    public static final int EM_REAL_DATA_TYPE_TS = 2;            // TS码流
	    public static final int EM_REAL_DATA_TYPE_MP4 = 3;           // MP4文件(从回调函数出来的是私有码流数据,参数dwDataType值为0)
	    public static final int EM_REAL_DATA_TYPE_H264 = 4;          // 裸H264码流
	}
	
	// 事件类型 NET_ALARM_HIGH_SPEED (车辆超速报警事件)对应的数据块描述信息
	public static class ALARM_HIGH_SPEED_INFO extends Structure
	{
		public int                        nAction;                   	// 事件动作,1表示持续性事件开始,2表示持续性事件结束;
		public NET_TIME_EX			      stuTime;						// 事件发生的时间
		public double				      dbPTS;						// 时间戳(单位是毫秒)	
		public NET_GPS_STATUS_INFO 		  stGPSStatusInfo;              // GPS信息
		public int						  nSpeedLimit;					// 车连限速值km/h
		public int						  nCurSpeed;					// 当前车辆速度km/h
		public int						  nMaxSpeed;					// 最高速度Km/h
		public byte[]                	  byReserved = new byte[508];   // 保留字节    
	} 
	
	// GPS状态信息
	public static class NET_GPS_STATUS_INFO extends Structure
	{
		public NET_TIME                revTime;                        // 定位时间
		public byte[]                  DvrSerial = new byte[50];       // 设备序列号
		public byte[]                  byRserved1 = new byte[6];       // 对齐字节
		public double                  longitude;                      // 经度(单位是百万分之度,范围0-360度)
		public double                  latidude;                       // 纬度(单位是百万分之度,范围0-180度)
		public double                  height;                         // 高度(米)
		public double                  angle;                          // 方向角(正北方向为原点,顺时针为正)
		public double                  speed;                          // 速度(单位km/H)
		public short                   starCount;                      // 定位星数, emDateSource为 EM_DATE_SOURCE_GPS时有效
		public byte[]                  byRserved2 = new byte[2];       // 对齐字节
		public int   				   antennaState;                   // 天线状态, 参考  NET_THREE_STATUS_BOOL, emDateSource为 EM_DATE_SOURCE_GPS时有效    
		public int   				   orientationState;               // 定位状态, 参考  NET_THREE_STATUS_BOOL
		public int                     workStae;                       // 工作状态(0=未定位,1=非差分定位,2=差分定位,3=无效PPS,6=正在估算 
	                                                                   // emDateSource为 EM_DATE_SOURCE_GPS时有效
		public int                     nAlarmCount;                    // 发生的报警位置个数
		public int[]                   nAlarmState = new int[128];     // 发生的报警位置,值可能多个, emDateSource为 EM_DATE_SOURCE_GPS时有效
		public byte                    bOffline;                       // 0-实时 1-补传 
		public byte                    bSNR;                           // GPS信噪比,表示GPS信号强度,值越大,信号越强 范围：0~100,0表示不可用	
		public byte[]                  byRserved3 = new byte[2];       // 对齐字节
		public int          		   emDateSource;                   // 数据来源, 参考 EM_DATE_SOURCE
		public byte[]                  byRserved = new byte[124];      // 保留字节
	} 
	
	//三态布尔类型
	public static class NET_THREE_STATUS_BOOL extends Structure
	{
		public static final int BOOL_STATUS_FALSE  = 0; 
		public static final int BOOL_STATUS_TRUE   = 1;
	    public static final int BOOL_STATUS_UNKNOWN = 2;  //未知
	}
	
	// 数据来源
	public static class EM_DATE_SOURCE extends Structure
	{
	    public static final int EM_DATE_SOURCE_GPS = 0;                // GPS 
	    public static final int EM_DATE_SOURCE_INERTIALNAVIGATION = 1; // 惯性导航数据
	}
	
	// Gps定位信息
	public static class NET_GPS_LOCATION_INFO extends Structure
	{
		public GPS_Info	        	stuGpsInfo;               		// GPS信息
		public ALARM_STATE_INFO     stuAlarmStateInfo;        		// 报警状态信息
		public int				    nTemperature;		      		// 温度(单位:0.1摄氏度)
		public int					nHumidity;				  		// 湿度(单位:0.1%)
		public int					nIdleTime;				 		// 怠速时长(单位:秒)
		public int        			nMileage;				  		// 里程(单位:0.1km)
		public int					nVoltage;				  		// 设置电压值(单位:0.1伏)
		public byte[]			    byReserved = new byte[1024];  
	}
	
	// 事件类型 NET_ALARM_VIDEO_LOSS (视频丢失事件)对应的数据块描述信息
	public static class ALARM_VIDEO_LOSS_INFO extends Structure
	{
		public int                  nAction;                   		// 事件动作,1表示持续性事件开始,2表示持续性事件结束;
		public int					nChannelID;						// 通道号
		public double				dbPTS;							// 时间戳(单位是毫秒)	
		public byte[]				byReserved1 = new byte[4];		// 字节对齐
		public NET_TIME_EX			stuTime;						// 事件发生的时间
		public byte[]               byReserved = new byte[512];     // 保留字节    
	} 
	
	//报警事件类型 NET_ALARM_BUS_SHARP_ACCELERATE(车辆急加速事件)对应的数据描述信息
	public static class ALARM_BUS_SHARP_ACCELERATE_INFO extends Structure
	{
	    public int                  dwSize;
	    public NET_GPS_STATUS_INFO  stuGPSStatusInfo;       // GPS信息
	    public NET_TIME_EX			stuTime;				// 事件发生的时间
	    
	    public ALARM_BUS_SHARP_ACCELERATE_INFO() {
	    	this.dwSize = this.size();
	    }
	} 
	
	//报警事件类型 NET_ALARM_BUS_SHARP_DECELERATE(车辆急减速事件)对应的数据描述信息
	public static class ALARM_BUS_SHARP_DECELERATE_INFO extends Structure
	{
	    public int                  dwSize;
	    public NET_GPS_STATUS_INFO  stuGPSStatusInfo;       // GPS信息
	    public NET_TIME_EX			stuTime;				// 事件发生的时间
	    
	    public ALARM_BUS_SHARP_DECELERATE_INFO() {
	    	this.dwSize = this.size();
	    }
	} 
	
	// GPS未定位报警(NET_ALARM_GPS_NOT_ALIGNED)
	public static class ALARM_GPS_NOT_ALIGNED_INFO extends Structure
	{
		public int                 nAction;                        // 事件动作,0表示脉冲事件,1表示报警开始,2表示报警结束;  
		public NET_TIME_EX         stuTime;                        // 事件发生的时间
		public byte[]              byReserved = new byte[1024];    // 保留字节 
	}
	
	// 前端断网报警信息, 对应  NET_ALARM_FRONTDISCONNECT
	public static class ALARM_FRONTDISCONNET_INFO extends Structure
	{
	    public int                dwSize;                           // 结构体大小
	    public int                nChannelID;                       // 通道号
	    public int                nAction;                          // 0:开始 1:停止
	    public NET_TIME           stuTime;                          // 事件发生时间
	    public byte[]             szIpAddress = new byte[MAX_PATH]; // 前端IPC的IP地址
	    
	    public ALARM_FRONTDISCONNET_INFO() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 存储错误报警, 对应  NET_ALARM_STORAGE_FAILURE_EX
	public static class ALARM_STORAGE_FAILURE_EX extends Structure
	{
	    public int               dwSize;
	    public int               nAction;                            		  // 0:开始 1:停止
	    public byte[]            szName = new byte[NET_EVENT_NAME_LEN];       // 事件名称
	    public byte[]            szDevice = new byte[NET_STORAGE_NAME_LEN];   // 存储设备名称
	    public byte[]            szGroup = new byte[NET_STORAGE_NAME_LEN];    // 存储组名称
	    public byte[]            szPath = new byte[MAX_PATH];                 // 路径
	    public int    			 emError;                            		  // 错误类型, 参考   EM_STORAGE_ERROR
	    public int               nPhysicNo;                          		  // 硬盘所在槽编码, 从1开始
	    public NET_TIME_EX	     stuTime;									  // 事件发生的时间
	    
	    public ALARM_STORAGE_FAILURE_EX() {
	    	this.dwSize = this.size();
	    }
	} 
	
	// 存储组不存在事件信息, 对应  NET_ALARM_STORAGE_NOT_EXIST
	public static class ALARM_STORAGE_NOT_EXIST_INFO extends Structure
	{
	    public int              dwSize;
	    public int              nAction;                            		  // 0:开始 1:停止
	    public byte[]           szGroup = new byte[NET_STORAGE_NAME_LEN];     // 在录像或抓图存储点中设置但不存在的组
	    public NET_TIME         stuTime;                           			  // 事件触发时间
	    
	    public ALARM_STORAGE_NOT_EXIST_INFO() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 车辆ACC报警事件, 对应事件类型  NET_ALARM_VEHICLE_ACC
	public static class ALARM_VEHICLE_ACC_INFO extends Structure
	{
	    public int                   dwSize;
	    public int                   nACCStatus;                         // ACC状态, 0:无效, 1:开启, 2:关闭 
	    public int                   nAction;                            // 事件动作, 0:Start, 1:Stop
	    public NET_GPS_STATUS_INFO   stuGPSStatusInfo;                   // GPS信息
	    public int                   nConstantElectricStatus;            // 常电状态, 0:未知, 1:连接, 2:断开                            
	    public NET_TIME_EX		     stuTime;							 // 事件发生的时间
	    
	    public ALARM_VEHICLE_ACC_INFO() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 事件类型 NET_ALARM_VIDEOBLIND(视频遮挡事件)对应的数据块描述信息
	public static class ALARM_VIDEO_BLIND_INFO extends Structure
	{
		public int                   nAction;                   		// 事件动作,1表示持续性事件开始,2表示持续性事件结束;
		public int					 nChannelID;						// 通道号
		public double				 dbPTS;							    // 时间戳(单位是毫秒)
		public NET_TIME_EX			 stuTime;						    // 事件发生的时间
		public int					 nEventID;						    // 事件ID
		public byte[]                byReserved = new byte[512];       	// 保留字节    
	} 
	
	// 紧急事件(对应 NET_URGENCY_ALARM_EX2, 对原有的 NET_URGENCY_ALARM_EX 类型的升级, 指人为触发的紧急事件, 一般处理是联动外部通讯功能请求帮助)
	public static class ALARM_URGENCY_ALARM_EX2 extends Structure
	{
	    public int           	   dwSize;
	    public NET_TIME            stuTime;                     		// 事件产生的时间
	    public int           	   nID;                         		// 用于标识不同的紧急事件
	    
	    public ALARM_URGENCY_ALARM_EX2() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 事件类型 NET_ALARM_DRIVER_NOTCONFIRM (司机未按确认按钮报警事件)对应的数据块描述信息
	public static class ALARM_DRIVER_NOTCONFIRM_INFO extends Structure
	{
		public int                  nAction;                   			// 事件动作,1表示持续性事件开始,2表示持续性事件结束;
		public NET_TIME_EX			stuTime;							// 事件发生的时间
		public double				dbPTS;								// 时间戳(单位是毫秒)
		public byte[]               byReserved = new byte[512];			// 保留字节    
	} 
	
	// CLIENT_AttachBusState, 订阅Bus状态输入参结构
	public static class NET_IN_BUS_ATTACH extends Structure
	{
	    public int                   dwSize;
	    public fBusStateCallBack     cbBusState;                         // 状态回调函数
	    public NativeLong            dwUser;                             // 用户数据
	    
	    public NET_IN_BUS_ATTACH() {
	    	this.dwSize = this.size();
	    }
	}
	
	// CLIENT_AttachBusState, 订阅Bus状态输出参结构
	public static class NET_OUT_BUS_ATTACH extends Structure
	{
	    public int                dwSize;
	    
	    public NET_OUT_BUS_ATTACH() {
	    	this.dwSize = this.size();
	    }
	}
	
	// 事件类型 NET_ALARM_BUS_PASSENGER_CARD_CHECK (乘客刷卡事件)对应的数据描述信息
	public static class ALARM_PASSENGER_CARD_CHECK extends Structure
	{
	    public int                     bEventConfirm;                  			   // 是否需要回复, BOOL类型
	    public byte[]                  szCardNum = new byte[NET_MAX_BUSCARD_NUM];  // 公交卡号
	    public NET_GPS_STATUS_INFO     stuGPS;                        			   // GPS信息
	    public NET_TIME_EX             UTC;                           			   // 刷卡时间
	    public int                     nTime;                          			   // UTC整型
	    public int    				   emType;                 					   // 刷卡类型, 参考  EM_PASSENGER_CARD_CHECK_TYPE
	    public byte[]                  szMac = new byte[NET_MAX_POS_MAC_NUM];      // 刷卡机Mac码 (默认"0000",兼容老设备)
	    public byte[]                  reserved = new byte[1012];                  // 预留
	} 
	
	public static class EM_PASSENGER_CARD_CHECK_TYPE extends Structure
	{
	    public static final int EM_PASSENGER_CARD_CHECK_TYPE_UNKOWN = 0;            // 未知
	    public static final int EM_PASSENGER_CARD_CHECK_TYPE_SIGNIN = 1;            // 签到/上车
	    public static final int EM_PASSENGER_CARD_CHECK_TYPE_SIGNOUT = 2;           // 签出/下车
	    public static final int EM_PASSENGER_CARD_CHECK_TYPE_NORMAL = 3;            // 正常刷卡，不区分上下车
	}
	
	// CLIENT_AttachEventRestore 接口输入参数
	public static class NET_IN_ATTACH_EVENT_RESTORE extends Structure
	{
	    public int                dwSize;											//结构体大小
	    public byte[] 			  szUuid = new byte[MAX_EVENT_RESTORE_UUID];		//客户端惟一标识
	    
	    public NET_IN_ATTACH_EVENT_RESTORE() {
	    	this.dwSize = this.size();
	    }
	}
	
	public static class GPS_TEMP_HUMIDITY_INFO extends Structure
	{
		public double              dTemperature;               			// 温度值(摄氏度),实际值的1000倍,如30.0摄氏度表示为30000
		public double              dHumidity;                  			// 湿度值(%),实际值的1000倍,如30.0%表示为30000
		public byte[]              bReserved = new byte[128];           // 保留字节
		
		public static class ByValue extends GPS_Info implements Structure.ByValue { }
	}
	

	// 事件类型 NET_ALARM_FACEINFO_COLLECT (人脸信息录入事件)对应的数据块描述信息
	public static class ALARM_FACEINFO_COLLECT_INFO extends Structure
	{
		
		public int                nAction;                   				// 事件动作,1表示持续性事件开始,2表示持续性事件结束;
		public NET_TIME_EX		  stuTime;									// 事件发生的时间
		public double			  dbPTS;									// 时间戳(单位是毫秒)
		public byte[]			  szUserID = new byte[NET_MAX_USERID_LEN];	// 用户ID
		public byte[]             byReserved = new byte[512];       		// 保留字节    
	} 
	
	// 人脸信息记录操作类型, 接口  CLIENT_FaceInfoOpreate
	public static class EM_FACEINFO_OPREATE_TYPE extends Structure
	{
		public static final int EM_FACEINFO_OPREATE_ADD = 0;				// 添加, pInbuf = NET_IN_ADD_FACE_INFO , pOutBuf = NET_OUT_ADD_FACE_INFO
		public static final int EM_FACEINFO_OPREATE_GET = 1;				// 获取, pInBuf = NET_IN_GET_FACE_INFO , pOutBuf = NET_OUT_GET_FACE_INFO
		public static final int EM_FACEINFO_OPREATE_UPDATE = 2;				// 更新, pInbuf = NET_IN_UPDATE_FACE_INFO , pOutBuf = NET_OUT_UPDATE_FACE_INFO
		public static final int EM_FACEINFO_OPREATE_REMOVE = 3;				// 删除, pInbuf = NET_IN_REMOVE_FACE_INFO , pOutBuf = NET_OUT_REMOVE_FACE_INFO
		public static final int EM_FACEINFO_OPREATE_CLEAR = 4;				// 清除, pInbuf = NET_IN_CLEAR_FACE_INFO, pOutBuf = NET_OUT_CLEAR_FACE_INFO
	} 
	
	// 添加人脸记录信息输入参数
	public static class NET_IN_ADD_FACE_INFO extends Structure
	{
		public int 					 dwSize;
		public byte[]				 szUserID = new byte[NET_MAX_USERID_LEN];	// 用户ID
		public NET_FACE_RECORD_INFO	 stuFaceInfo;								// 人脸数据
		
		public NET_IN_ADD_FACE_INFO() {
			this.dwSize = this.size();
		}
	} 
	
	// 人脸信息
	public static class NET_FACE_RECORD_INFO extends Structure
	{
		public byte[]		 		szUserName = new byte[MAX_USER_NAME_LEN];						// 用户名
		public int			 		nRoom;														    // 房间个数
		public NET_FACE_ROOMNO[]	szRoomNo = (NET_FACE_ROOMNO[])new NET_FACE_ROOMNO().toArray(MAX_ROOMNUM_COUNT); // 房间号列表
		public int 			 		nFaceData;														// 人脸模板数据个数
		public NET_FACE_FACEDATA[]	szFaceData = (NET_FACE_FACEDATA[])new NET_FACE_FACEDATA().toArray(MAX_FACE_COUTN);// 人脸模板数据
		public byte[]        		byReserved = new byte[512];                                    // 保留字节
	} 
	
	public static class NET_FACE_ROOMNO extends Structure {
		public byte[] roomNo = new byte[NET_COMMON_STRING_16];   // 房间号
	}
	
	public static class NET_FACE_FACEDATA extends Structure {
		public byte[] faceData = new byte[MAX_FACE_DATA_LEN];    // 人脸数据
	}
	
	// 添加人脸记录信息输出参数
	public static class NET_OUT_ADD_FACE_INFO extends Structure
	{
		public int 			dwSize;
		
		public NET_OUT_ADD_FACE_INFO() {
			this.dwSize = this.size();
		}
	} 
	
	// 获取人脸记录信息输入参数
	public static class NET_IN_GET_FACE_INFO extends Structure
	{
		public int 			dwSize;
		public byte[]		szUserID = new byte[NET_MAX_USERID_LEN];	// 用户ID
		
		public NET_IN_GET_FACE_INFO() {
			this.dwSize = this.size();
		}
	} 
	
	// 获取人脸记录信息输出参数
	public static class NET_OUT_GET_FACE_INFO extends Structure
	{
		public int 					dwSize;
		public int 					nFaceData;														// 人脸模板数据个数
		public NET_FACE_FACEDATA[]	szFaceData = (NET_FACE_FACEDATA[])new NET_FACE_FACEDATA().toArray(MAX_FACE_COUTN);		// 人脸模板数据
		
		public NET_OUT_GET_FACE_INFO() {
			this.dwSize = this.size();
		}
	} 
	
	// 更新人脸记录信息输入参数
	public static class NET_IN_UPDATE_FACE_INFO extends Structure
	{
		public int 					  dwSize;
		public byte[]				  szUserID = new byte[NET_MAX_USERID_LEN];			  // 用户ID
		public NET_FACE_RECORD_INFO	  stuFaceInfo;							    		  // 人脸数据
		
		public NET_IN_UPDATE_FACE_INFO() {
			this.dwSize = this.size();
		}
	} 
	
	// 更新人脸记录信息输出参数
	public static class NET_OUT_UPDATE_FACE_INFO extends Structure 
	{
		public int 			dwSize;
		
		public NET_OUT_UPDATE_FACE_INFO() {
			this.dwSize = this.size();
		}
	} 
	
	// 删除人脸记录信息输入参数
	public static class NET_IN_REMOVE_FACE_INFO extends Structure
	{
		public int 			dwSize;
		public byte[]		szUserID = new byte[NET_MAX_USERID_LEN];	// 用户ID
		
		public NET_IN_REMOVE_FACE_INFO() {
			this.dwSize = this.size();
		}
	} 

	// 删除人脸记录信息输出参数
	public static class NET_OUT_REMOVE_FACE_INFO extends Structure
	{
		public int 			dwSize;
		
		public NET_OUT_REMOVE_FACE_INFO() {
			this.dwSize = this.size();
		}
	} 

	// 清除人脸记录信息输入参数
	public static class NET_IN_CLEAR_FACE_INFO extends Structure
	{
		public int 			dwSize;
		
		public NET_IN_CLEAR_FACE_INFO() {
			this.dwSize = this.size();
		}
	} 

	// 清除人脸记录信息输出参数
	public static class NET_OUT_CLEAR_FACE_INFO extends Structure 
	{
		public int 			dwSize;
		
		public NET_OUT_CLEAR_FACE_INFO() {
			this.dwSize = this.size();
		}
	} 
	
    /***********************************************************************
     ** 回调
     ***********************************************************************/
    //JNA StdCallCallback方法定义,断线回调
    public interface fDisConnect extends StdCallCallback {
        public void invoke(NativeLong lLoginID, String pchDVRIP, int nDVRPort, NativeLong dwUser);
    }

    // 网络连接恢复回调函数原形
    public interface fHaveReConnect extends StdCallCallback {
        public void invoke(NativeLong lLoginID, String pchDVRIP, int nDVRPort, NativeLong dwUser);
    }
    
    // 消息回调函数原形(pBuf内存由SDK内部申请释放)
    public interface fMessCallBack extends StdCallCallback{
        public boolean invoke(int lCommand , NativeLong lLoginID , Pointer pStuEvent , int dwBufLen , String strDeviceIP ,  NativeLong nDevicePort , NativeLong dwUser);
    }
    
    public interface fFaceFindState extends StdCallCallback {
        // pstStates 指向NET_CB_FACE_FIND_STATE的指针
        public void invoke(NativeLong lLoginID, NativeLong lAttachHandle, Pointer pstStates, int nStateNum, NativeLong dwUser);
    }
    
    // 智能分析数据回调;nSequence表示上传的相同图片情况，为0时表示是第一次出现，为2表示最后一次出现或仅出现一次，为1表示此次之后还有
    // int nState = *(int*) reserved 表示当前回调数据的状态, 为0表示当前数据为实时数据，为1表示当前回调数据是离线数据，为2时表示离线数据传送结束
    // pAlarmInfo 对应智能事件信息, pBuffer 对应智能图片信息, dwBufSize 智能图片信息大小
    public interface fAnalyzerDataCallBack extends StdCallCallback {
        public int invoke(NativeLong lAnalyzerHandle, int dwAlarmType, Pointer pAlarmInfo, Pointer pBuffer, int dwBufSize, Pointer dwUser, int nSequence, Pointer reserved);
    }
    
    // 抓图回调函数原形(pBuf内存由SDK内部申请释放)
    // EncodeType 编码类型，10：表示jpeg图片      0：mpeg4    CmdSerial : 操作流水号，同步抓图的情况下用不上 
    public interface fSnapRev extends StdCallCallback{
        public void invoke( NativeLong lLoginID ,Pointer pBuf, int RevLen, int EncodeType, NativeLong CmdSerial, NativeLong dwUser);
    }
    
    // 异步搜索设备回调(pDevNetInfo内存由SDK内部申请释放)
    public interface fSearchDevicesCB extends StdCallCallback{
        public void invoke(Pointer pDevNetInfo, Pointer pUserData);
    }
    
    // 按时间回放进度回调函数原形
    public interface fTimeDownLoadPosCallBack extends StdCallCallback {    
        public void invoke(NativeLong lPlayHandle, int dwTotalSize, int dwDownLoadSize, int index, NET_RECORDFILE_INFO.ByValue recordfileinfo, NativeLong dwUser);
    } 
    
    // 回放数据回调函数原形
    public interface fDataCallBack extends StdCallCallback {
        public int invoke(NativeLong lRealHandle, int dwDataType, Pointer pBuffer, int dwBufSize, NativeLong dwUser);
    }
    
    // 回放进度回调函数原形
    public interface fDownLoadPosCallBack extends StdCallCallback {
    	public void invoke(NativeLong lPlayHandle, int dwTotalSize, int dwDownLoadSize, NativeLong dwUser);
    }
    
    // 视频统计摘要信息回调函数原形，lAttachHandle 是 CLIENT_AttachVideoStatSummary 返回值
    public interface fVideoStatSumCallBack extends StdCallCallback {
    	public void invoke(NativeLong lAttachHandle, NET_VIDEOSTAT_SUMMARY pBuf, int dwBufLen, NativeLong dwUser);
    }
    
    // 用户自定义的数据回调   lTalkHandle是CLIENT_StartTalkEx的返回值 
    // byAudioFlag：   0表示是本地录音库采集的音频数据 ，  1表示收到的设备发过来的音频数据
    public interface pfAudioDataCallBack extends StdCallCallback {
    	public void invoke(NativeLong lTalkHandle, Pointer pDataBuf, int dwBufSize, byte byAudioFlag, NativeLong dwUser);
    }

    // lHandle是文件传输句柄 ，nTransType是文件传输类型，nState是文件传输状态，
    public interface fTransFileCallBack extends StdCallCallback {
    	public void invoke(NativeLong lHandle, int nTransType, int nState, int nSendSize, int nTotalSize, NativeLong  dwUser);
    }    
    
    // GPS信息订阅回调--扩展
    public interface fGPSRevEx extends StdCallCallback { 
    	public void invoke(NativeLong lLoginID, GPS_Info.ByValue GpsInfo, ALARM_STATE_INFO.ByValue stAlarmInfo, NativeLong dwUserData, Pointer reserved);
    }
    
    // GPS信息订阅回调--扩展2
    public interface fGPSRevEx2 extends StdCallCallback { 
    	public void invoke(NativeLong lLoginID, NET_GPS_LOCATION_INFO lpData, Pointer dwUserData, Pointer reserved);
    }
    
    // 实时监视数据回调函数--扩展(pBuffer内存由SDK内部申请释放)
    // lRealHandle实时监视           dwDataType: 0-原始数据   1-帧数据    2-yuv数据   3-pcm音频数据
    // pBuffer对应BYTE*
    // param:当类型为0(原始数据)和2(YUV数据) 时为0。当回调的数据类型为1时param为一个tagVideoFrameParam结构体指针。
    // param:当数据类型是3时,param也是一个tagCBPCMDataParam结构体指针 
    public interface fRealDataCallBackEx extends StdCallCallback {
    	public void invoke(NativeLong lRealHandle, int dwDataType, Pointer pBuffer, int dwBufSize, int param, NativeLong dwUser);
    }

    // 视频监视断开回调函数, (param内存由SDK内部申请释放 )
    // lOperateHandle监控句柄   dwEventType对应EM_REALPLAY_DISCONNECT_EVENT_TYPE   param对应void*,事件参数
    public interface fRealPlayDisConnect extends StdCallCallback {
    	public void invoke(NativeLong lOperateHandle, int dwEventType, Pointer param, NativeLong dwUser);
    }
    
    // 订阅过车记录数据回调函数原型     lAttachHandle为CLIENT_ParkingControlAttachRecord返回值
    public interface fParkingControlRecordCallBack extends StdCallCallback {
    	public void invoke(NativeLong lLoginID, NativeLong lAttachHandle, NET_CAR_PASS_ITEM pInfo, int nBufLen, NativeLong dwUser);
    }
    
    // 订阅车位信息回调函数原型
    public interface fParkInfoCallBack extends StdCallCallback {
    	public void invoke(NativeLong lLoginID, NativeLong lAttachHandle, NET_PARK_INFO_ITEM pInfo, int nBufLen, NativeLong dwUser);
    }
  
    // 订阅监测点位信息回调函数原型
    public interface fSCADAAttachInfoCallBack extends StdCallCallback {
    	public void invoke(NativeLong lLoginID, NativeLong lAttachHandle, NET_SCADA_NOTIFY_POINT_INFO_LIST pInfo, int nBufLen, NativeLong dwUser);
    }
    
    // 透明串口回调函数原形(pBuffer内存由SDK内部申请释放)) 
    public interface fTransComCallBack extends StdCallCallback {
    	public void invoke(NativeLong lLoginID, NativeLong lTransComChannel, Pointer pBuffer, int dwBufSize, NativeLong dwUser);
    }
    
    //视频分析进度状态实时回调函数
    public interface fVideoAnalyseState extends StdCallCallback {
    	public int invoke(NativeLong lAttachHandle, NET_VIDEOANALYSE_STATE pAnalyseStateInfos, NativeLong dwUser, Pointer pReserved);
    }
    
    // 侦听服务器回调函数原形
    public interface fServiceCallBack extends StdCallCallback {
    	public int invoke(NativeLong lHandle, String pIp, int wPort, NativeLong lCommand, Pointer pParam, int dwParamLen, long dwUserData);
    }
    
    //订阅Bus状态回调函数原型
    public interface fBusStateCallBack extends StdCallCallback {
    	public void invoke(NativeLong lAttachHandle, int lCommand, Pointer pBuf, int dwBufLen, NativeLong dwUser);
    }

    // GPS温湿度信息订阅回调
    public interface fGPSTempHumidityRev extends StdCallCallback {
    	public void invoke(NativeLong lLoginID, GPS_TEMP_HUMIDITY_INFO.ByValue GpsTHInfo, NativeLong dwUserData);
    }

    
    /************************************************************************
     ** 接口
     ***********************************************************************/
    //  JNA直接调用方法定义，cbDisConnect 实际情况并不回调Java代码，仅为定义可以使用如下方式进行定义。 fDisConnect 回调
    public boolean CLIENT_Init(StdCallCallback cbDisConnect, NativeLong dwUser);
    
    //  JNA直接调用方法定义，SDK退出清理
    public void CLIENT_Cleanup();
    
    //  JNA直接调用方法定义，设置断线重连成功回调函数，设置后SDK内部断线自动重连, fHaveReConnect 回调
    public void CLIENT_SetAutoReconnect(StdCallCallback cbAutoConnect, NativeLong dwUser);
    
    // 返回函数执行失败代码
    public int CLIENT_GetLastError();

    // 设置连接设备超时时间和尝试次数
    public void CLIENT_SetConnectTime(int nWaitTime, int nTryTimes);

    // 设置登陆网络环境
    public void CLIENT_SetNetworkParam(NET_PARAM pNetParam);

    // 获取SDK的版本信息
    public int CLIENT_GetSDKVersion();
    
    //  JNA直接调用方法定义，登陆接口
    public NativeLong CLIENT_LoginEx(String pchDVRIP, int wDVRPort, String pchUserName, String pchPassword, int nSpecCap, Pointer pCapParam, NET_DEVICEINFO lpDeviceInfo, IntByReference error/*= 0*/);
    
    //  JNA直接调用方法定义，登陆扩展接口///////////////////////////////////////////////////
    //  nSpecCap 对应  EM_LOGIN_SPAC_CAP_TYPE 登陆类型
    public NativeLong CLIENT_LoginEx2(String pchDVRIP, int wDVRPort, String pchUserName, String pchPassword, int nSpecCap, Pointer pCapParam, NET_DEVICEINFO_Ex lpDeviceInfo, IntByReference error/*= 0*/);
    
    //  JNA直接调用方法定义，向设备注销
    public boolean CLIENT_Logout(NativeLong lLoginID);
    
    // 获取配置
    // error 为设备返回的错误码： 0-成功 1-失败 2-数据不合法 3-暂时无法设置 4-没有权限
    public boolean CLIENT_GetNewDevConfig(NativeLong lLoginID , String szCommand , int nChannelID , byte[] szOutBuffer , int dwOutBufferSize , IntByReference error , int waiitime);
    
    // 设置配置
    public boolean CLIENT_SetNewDevConfig(NativeLong lLoginID , String szCommand , int nChannelID , byte[] szInBuffer, int dwInBufferSize, IntByReference error, IntByReference restart, int waittime );
    
    // 解析查询到的配置信息
    public boolean CLIENT_ParseData(String szCommand, byte[] szInBuffer, Pointer lpOutBuffer, int dwOutBufferSize, Pointer pReserved);

    // 组成要设置的配置信息
    public boolean CLIENT_PacketData(String szCommand, Pointer lpInBuffer, int dwInBufferSize, byte[] szOutBuffer, int dwOutBufferSize);

    // 设置报警回调函数, fMessCallBack 回调
    public void  CLIENT_SetDVRMessCallBack(StdCallCallback cbMessage , NativeLong dwUser);
    
    // 向设备订阅报警--扩展
    public boolean  CLIENT_StartListenEx(NativeLong lLoginID);

    /////////////////////////////////人脸识别接口/////////////////////////////////////////
    //人脸识别数据库信息操作（包括添加,修改和删除）
    // pstInParam指向NET_IN_OPERATE_FACERECONGNITIONDB类型的指针
    // pstOutParam指向NET_OUT_OPERATE_FACERECONGNITIONDB类型的指针
    public boolean  CLIENT_OperateFaceRecognitionDB(NativeLong lLoginID, NET_IN_OPERATE_FACERECONGNITIONDB pstInParam, NET_OUT_OPERATE_FACERECONGNITIONDB pstOutParam, int nWaitTime);
    
    //按条件查询人脸识别结果 
    // pstInParam指向NET_IN_STARTFIND_FACERECONGNITION类型的指针
    // pstOutParam指向NET_OUT_STARTFIND_FACERECONGNITION类型的指针
    public boolean  CLIENT_StartFindFaceRecognition(NativeLong lLoginID, NET_IN_STARTFIND_FACERECONGNITION pstInParam, NET_OUT_STARTFIND_FACERECONGNITION pstOutParam, int nWaitTime);
    
    //查找人脸识别结果:nFilecount:需要查询的条数, 返回值为媒体文件条数 返回值<nFilecount则相应时间段内的文件查询完毕(每次最多只能查询20条记录)
    // pstInParam指向NET_IN_DOFIND_FACERECONGNITION类型的指针
    // pstOutParam指向NET_OUT_DOFIND_FACERECONGNITION类型的指针
    public boolean  CLIENT_DoFindFaceRecognition(final NET_IN_DOFIND_FACERECONGNITION pstInParam, NET_OUT_DOFIND_FACERECONGNITION pstOutParam, int nWaitTime);
    
    //结束查询
    public boolean  CLIENT_StopFindFaceRecognition(long lFindHandle);
    
    //人脸检测(输入一张大图,输入大图中被检测出来的人脸图片)
    // pstInParam指向NET_IN_DETECT_FACE类型的指针
    // pstOutParam指向NET_OUT_DETECT_FACE类型的指针
    public boolean  CLIENT_DetectFace(NativeLong lLoginID, final NET_IN_DETECT_FACE pstInParam, NET_OUT_DETECT_FACE pstOutParam, int nWaitTime);
    
    //人脸识别人员组操作（包括添加,修改和删除）
    // pstInParam指向NET_IN_OPERATE_FACERECONGNITION_GROUP类型的指针
    // pstOutParam指向NET_OUT_OPERATE_FACERECONGNITION_GROUP类型的指针
    public boolean  CLIENT_OperateFaceRecognitionGroup(NativeLong lLoginID, final NET_IN_OPERATE_FACERECONGNITION_GROUP pstInParam, NET_OUT_OPERATE_FACERECONGNITION_GROUP pstOutParam, int nWaitTime);
    
    //查询人脸识别人员组信息
    // pstInParam指向NET_IN_FIND_GROUP_INFO类型的指针
    // pstOutParam指向NET_OUT_FIND_GROUP_INFO类型的指针
    public boolean  CLIENT_FindGroupInfo(NativeLong NativeLong, final NET_IN_FIND_GROUP_INFO pstInParam, NET_OUT_FIND_GROUP_INFO pstOutParam, int nWaitTime);
    
    //布控通道人员组信息
    // pstInParam指向NET_IN_SET_GROUPINFO_FOR_CHANNEL类型的指针
    // pstOutParam指向NET_OUT_SET_GROUPINFO_FOR_CHANNEL类型的指针
    public boolean CLIENT_SetGroupInfoForChannel(NativeLong lLoginID, final NET_IN_SET_GROUPINFO_FOR_CHANNEL pstInParam, NET_OUT_SET_GROUPINFO_FOR_CHANNEL pstOutParam, int nWaitTime);
    
    //订阅人脸查询状态
    // pstInParam指向NET_IN_FACE_FIND_STATE类型的指针
    // pstOutParam指向NET_OUT_FACE_FIND_STATE类型的指针
    public NativeLong CLIENT_AttachFaceFindState(NativeLong lLoginID, final NET_IN_FACE_FIND_STATE pstInParam, NET_OUT_FACE_FIND_STATE pstOutParam, int nWaitTime);
    
    //取消订阅人脸查询状态,lAttachHandle为CLIENT_AttachFaceFindState返回的句柄
    public boolean CLIENT_DetachFaceFindState(NativeLong lAttachHandle);
    
    // 打开日志功能
    // pstLogPrintInfo指向LOG_SET_PRINT_INFO的指针
    public boolean CLIENT_LogOpen(LOG_SET_PRINT_INFO pstLogPrintInfo);

    // 关闭日志功能
    public boolean CLIENT_LogClose();
    
    // 获取符合查询条件的文件总数
    // reserved为void *
    public boolean CLIENT_GetTotalFileCount(NativeLong lFindHandle, IntByReference pTotalCount,  Pointer reserved, int waittime);
    
    // 设置查询跳转条件
    // reserved为void *
    public boolean  CLIENT_SetFindingJumpOption(NativeLong lFindHandle, NET_FINDING_JUMP_OPTION_INFO pOption, Pointer reserved, int waittime);
    
    // 按查询条件查询文件
    // pQueryCondition为void *, 具体类型根据emType的类型确定,对应 EM_FILE_QUERY_TYPE
    // reserved为void *, 具体类型根据emType的类型确定
    public NativeLong CLIENT_FindFileEx(NativeLong lLoginID, int emType, Pointer pQueryCondition, Pointer reserved, int waittime);
    
    // 查找文件:nFilecount:需要查询的条数, 返回值为媒体文件条数 返回值<nFilecount则相应时间段内的文件查询完毕
    // pMediaFileInfo为void *
    // reserved为void *
    public int CLIENT_FindNextFileEx(NativeLong lFindHandle, int nFilecount, Pointer pMediaFileInfo, int maxlen, Pointer reserved, int waittime);
    
    // 结束录像文件查找
    public boolean CLIENT_FindCloseEx(NativeLong lFindHandle);
    
    // 实时上传智能分析数据－图片(扩展接口,bNeedPicFile表示是否订阅图片文件,Reserved类型为RESERVED_PARA) 
    // bNeedPicFile为BOOL类型，取值范围为0或者1, fAnalyzerDataCallBack回调
    public NativeLong CLIENT_RealLoadPictureEx(NativeLong lLoginID, int nChannelID, int dwAlarmType, int bNeedPicFile, StdCallCallback cbAnalyzerData, Pointer dwUser, Pointer Reserved);
    
    // 停止上传智能分析数据－图片
    public boolean CLIENT_StopLoadPic(NativeLong lAnalyzerHandle);
    
    // 设置抓图回调函数, fSnapRev回调
    public void CLIENT_SetSnapRevCallBack(StdCallCallback OnSnapRevMessage, NativeLong dwUser);
    
    // 抓图请求扩展接口
    public boolean CLIENT_SnapPictureEx(NativeLong lLoginID, SNAP_PARAMS stParam, IntByReference reserved);
    
    // 异步搜索局域网内IPC、NVS等设备, fSearchDevicesCB回调
    public NativeLong CLIENT_StartSearchDevices(StdCallCallback cbSearchDevices, Pointer pUserData, String szLocalIp);
    
    // 停止异步搜索局域网内IPC、NVS等设备
    public boolean CLIENT_StopSearchDevices(NativeLong lSearchHandle);
    
    // 同步跨网段搜索设备IP (pIpSearchInfo内存由用户申请释放)
    // szLocalIp为本地IP，可不做输入, fSearchDevicesCB回调
    public boolean CLIENT_SearchDevicesByIPs(DEVICE_IP_SEARCH_INFO pIpSearchInfo, StdCallCallback cbSearchDevices, NativeLong dwUserData, String szLocalIp, int dwWaitTime);

    // 开始实时监视
    // rType  : NET_RealPlayType    返回监控句柄
    public NativeLong CLIENT_RealPlayEx(NativeLong lLoginID, int nChannelID, Pointer hWnd, int rType);
    
    // 停止实时预览--扩展     lRealHandle为CLIENT_RealPlayEx的返回值
    public boolean CLIENT_StopRealPlayEx(NativeLong lRealHandle);
    
    // 开始实时监视支持设置码流回调接口     rType  : NET_RealPlayType   返回监控句柄
    // cbRealData 对应 fRealDataCallBackEx 回调
    // cbDisconnect 对应 fRealPlayDisConnect 回调
    public NativeLong CLIENT_StartRealPlay(NativeLong lLoginID, int nChannelID, Pointer hWnd, int rType, StdCallCallback cbRealData, StdCallCallback cbDisconnect, NativeLong dwUser, int dwWaitTime);

    // 停止实时预览
    public boolean CLIENT_StopRealPlay(NativeLong lRealHandle);
    
    // 设置实时监视数据回调函数扩展接口    lRealHandle监控句柄,fRealDataCallBackEx 回调
    public boolean CLIENT_SetRealDataCallBackEx(NativeLong lRealHandle, StdCallCallback cbRealData, NativeLong dwUser, int dwFlag);
    
    // 设置图象流畅性
    // 将要调整图象的等级(0-6),当level为0时，图象最流畅；当level为6时，图象最实时。Level的默认值为3。注意：直接解码下有效 
    public boolean CLIENT_AdjustFluency(NativeLong lRealHandle, int nLevel);
    

    // 保存数据为文件,lRealHandle为CLIENT_RealPlayEx的返回值,pchFileName为实时监视保存文件名 
    public boolean CLIENT_SaveRealData(NativeLong lRealHandle, String pchFileName);
    
    // 结束保存数据为文件,lRealHandle为CLIENT_RealPlayEx的返回值
    public boolean CLIENT_StopSaveRealData(NativeLong lRealHandle);     

    // 打开声音
    public boolean CLIENT_OpenSound(NativeLong hPlayHandle);
    
    // 关闭声音
    public boolean CLIENT_CloseSound();
    
    // 获取所有有效显示源
    // pInParam  对应  NET_IN_MATRIX_GET_CAMERAS
    // pOutParam 对应  NET_OUT_MATRIX_GET_CAMERAS
    public boolean CLIENT_MatrixGetCameras(NativeLong lLoginID, NET_IN_MATRIX_GET_CAMERAS pInParam, NET_OUT_MATRIX_GET_CAMERAS pOutParam, int nWaitTime);

    // 抓图同步接口,将图片数据直接返回给用户
    public boolean CLIENT_SnapPictureToFile(NativeLong lLoginID, final NET_IN_SNAP_PIC_TO_FILE_PARAM pInParam, NET_OUT_SNAP_PIC_TO_FILE_PARAM pOutParam, int nWaitTime);
    
    // 查询时间段内的所有录像文件
    // nRecordFileType 录像类型 0:所有录像  1:外部报警  2:动态监测报警  3:所有报警  4:卡号查询   5:组合条件查询   6:录像位置与偏移量长度   8:按卡号查询图片(目前仅HB-U和NVS特殊型号的设备支持)  9:查询图片(目前仅HB-U和NVS特殊型号的设备支持)  10:按字段查询    15:返回网络数据结构(金桥网吧)  16:查询所有透明串数据录像文件
    // nriFileinfo 返回的录像文件信息，是一个 NET_RECORDFILE_INFO 结构数组 
    // maxlen 是 nriFileinfo缓冲的最大长度(单位字节，建议在(100~200)*sizeof(NET_RECORDFILE_INFO)之间) 
    // filecount返回的文件个数，属于输出参数最大只能查到缓冲满为止的录像记录; 
    // bTime 是否按时间查(目前无效) 
    public boolean CLIENT_QueryRecordFile(NativeLong lLoginID, int nChannelId, int nRecordFileType, NET_TIME tmStart, NET_TIME tmEnd, String pchCardid, NET_RECORDFILE_INFO[] stFileInfo, int maxlen, IntByReference filecount, int waittime, boolean bTime);
    
    // 查询时间段内是否有录像文件   bResult输出参数，true有录像，false没录像
    public boolean CLIENT_QueryRecordTime(NativeLong lLoginID, int nChannelId, int nRecordFileType, NET_TIME tmStart, NET_TIME tmEnd, String pchCardid, IntByReference bResult, int waittime);
    
    // 通过时间下载录像--扩展
    // nRecordFileType 对应 EM_QUERY_RECORD_TYPE
    // cbTimeDownLoadPos 对应 fTimeDownLoadPosCallBack 回调
    // fDownLoadDataCallBack 对应 fDataCallBack 回调
    public NativeLong CLIENT_DownloadByTimeEx(
            NativeLong lLoginID, 
            int nChannelId, 
            int nRecordFileType, 
            NET_TIME tmStart, 
            NET_TIME tmEnd, 
            String sSavedFileName, 
            StdCallCallback cbTimeDownLoadPos, 
            NativeLong dwUserData, 
            StdCallCallback fDownLoadDataCallBack, 
            NativeLong dwDataUser, 
            Pointer pReserved);
    
    // 停止录像下载
    public boolean CLIENT_StopDownload(NativeLong lFileHandle);
    
    // 私有云台控制扩展接口,支持三维快速定位
    public boolean CLIENT_DHPTZControlEx(NativeLong lLoginID, int nChannelID, int dwPTZCommand, int lParam1, int lParam2, int lParam3, int dwStop);
   
    // 云台控制扩展接口,支持三维快速定位,鱼眼
    // dwStop类型为BOOL, 取值0或者1
    // dwPTZCommand取值为NET_EXTPTZ_ControlType中的值或者是NET_PTZ_ControlType中的值
    public boolean CLIENT_DHPTZControlEx2(NativeLong lLoginID, int nChannelID, int dwPTZCommand, int lParam1, int lParam2, int lParam3, int dwStop, Pointer param4);
       
    // 设备控制(param内存由用户申请释放)  emType对应 枚举 CtrlType
    public boolean CLIENT_ControlDevice(NativeLong lLoginID, int emType, Pointer param, int waittime);
    
    // 设备控制扩展接口，兼容 CLIENT_ControlDevice (pInBuf, pOutBuf内存由用户申请释放)
    // emType的取值为CtrlType中的值
    public boolean CLIENT_ControlDeviceEx(NativeLong lLoginID, int emType, Pointer pInBuf, Pointer pOutBuf, int nWaitTime);
    
    // 查询配置信息(lpOutBuffer内存由用户申请释放)
    public boolean CLIENT_GetDevConfig(NativeLong lLoginID, int dwCommand, int lChannel, Pointer lpOutBuffer, int dwOutBufferSize, IntByReference lpBytesReturned,int waittime);

    // 设置配置信息(lpInBuffer内存由用户申请释放)
    public boolean CLIENT_SetDevConfig(NativeLong lLoginID, int dwCommand, int lChannel, Pointer lpInBuffer, int dwInBufferSize, int waittime);
    
    // 查询设备状态(pBuf内存由用户申请释放)
    // pBuf指向char *,输出参数
    // pRetLen指向int *;输出参数，实际返回的数据长度，单位字节
    public boolean CLIENT_QueryDevState(NativeLong lLoginID, int nType, Pointer pBuf, int nBufLen, IntByReference pRetLen, int waittime);
    
    // 获取设备能力接口
    // pInBuf指向void*，输入参数结构体指针       pOutBuf指向void*，输出参数结构体指针
    public boolean CLIENT_GetDevCaps(NativeLong lLoginID, int nType, Pointer pInBuf, Pointer pOutBuf, int nWaitTime);
    
    // 停止订阅报警
    public boolean CLIENT_StopListen(NativeLong lLoginID);
    
    // 新系统能力查询接口，查询系统能力信息(以Json格式，具体见配置SDK)(szOutBuffer内存由用户申请释放)
    // szCommand: 对应命令查看上文
    // szOutBuffer: 获取到的信息, 通过 CLIENT_ParseData 解析
    // error 指向 int * ： 错误码大于0表示设备返回的，小于0表示缓冲不够或数据校验引起的
    public boolean CLIENT_QueryNewSystemInfo(NativeLong lLoginID, String szCommand, int nChannelID, byte[] szOutBuffer, int dwOutBufferSize, IntByReference error, int waittime);
    
    // 订阅视频统计摘要信息
    public NativeLong CLIENT_AttachVideoStatSummary(NativeLong lLoginID, NET_IN_ATTACH_VIDEOSTAT_SUM pInParam, NET_OUT_ATTACH_VIDEOSTAT_SUM pOutParam, int nWaitTime);

    // 取消订阅视频统计摘要信息，lAttachHandle为CLIENT_AttachVideoStatSummary的返回值
    public boolean CLIENT_DetachVideoStatSummary(NativeLong lAttachHandle);
    
    // 开始查询视频统计信息/获取人数统计信息
    public NativeLong CLIENT_StartFindNumberStat(NativeLong lLoginID, NET_IN_FINDNUMBERSTAT pstInParam, NET_OUT_FINDNUMBERSTAT pstOutParam);

    // 继续查询视频统计/继续查询人数统计
    public int CLIENT_DoFindNumberStat(NativeLong lFindHandle, NET_IN_DOFINDNUMBERSTAT pstInParam, NET_OUT_DOFINDNUMBERSTAT pstOutParam);

    // 结束查询视频统计/结束查询人数统计
    public boolean CLIENT_StopFindNumberStat(NativeLong lFindHandle);
   
    // 设置语音对讲模式,客户端方式还是服务器方式
    // emType : 方式类型 参照 EM_USEDEV_MODE
    public boolean CLIENT_SetDeviceMode(NativeLong lLoginID, int emType, Pointer pValue);
    
    ///////////////// 录像回放相关接口 ///////////////////////
    // 按时间方式回放--扩展接口 
    // cbDownLoadPos 对应 fDownLoadPosCallBack 回调
    // fDownLoadDataCallBack 对应 fDataCallBack 回调
    public NativeLong CLIENT_PlayBackByTimeEx(NativeLong lLoginID, int nChannelID, NET_TIME lpStartTime, NET_TIME lpStopTime, Pointer hWnd, 
								    		StdCallCallback cbDownLoadPos, NativeLong dwPosUser, 
								    		StdCallCallback fDownLoadDataCallBack, NativeLong dwDataUser);
    // 停止录像回放接口
    public boolean CLIENT_StopPlayBack(NativeLong lPlayHandle);
    
    // 获取回放OSD时间
    public boolean CLIENT_GetPlayBackOsdTime(NativeLong lPlayHandle, NET_TIME lpOsdTime, NET_TIME lpStartTime, NET_TIME lpEndTime);

    // 暂停或恢复录像回放
    // bPause: 1 - 暂停	0 - 恢复 
    public boolean CLIENT_PausePlayBack(NativeLong lPlayHandle, int bPause);
    
    // 快进录像回放
    public boolean CLIENT_FastPlayBack(NativeLong lPlayHandle);

    // 慢进录像回放
    public boolean CLIENT_SlowPlayBack(NativeLong lPlayHandle);
 
    // 恢复正常回放速度
    public boolean CLIENT_NormalPlayBack(NativeLong lPlayHandle);
    
    // 查询设备当前时间
    public boolean CLIENT_QueryDeviceTime(NativeLong lLoginID, NET_TIME pDeviceTime, int waittime);
    
    // 设置设备当前时间
    public boolean CLIENT_SetupDeviceTime(NativeLong lLoginID, NET_TIME pDeviceTime);
    
    // 获得亮度、色度、对比度、饱和度的参数      
    // param1/param2/param3/param4 四个参数范围0~255
  	public boolean CLIENT_ClientGetVideoEffect(NativeLong lPlayHandle, byte[] nBrightness, byte[] nContrast, byte[] nHue, byte[] nSaturation);

  	// 设置亮度、色度、对比度、饱和度的参数    
  	// nBrightness/nContrast/nHue/nSaturation四个参数为 unsigned byte 范围0~255
  	public boolean CLIENT_ClientSetVideoEffect(NativeLong lPlayHandle, byte nBrightness, byte nContrast, byte nHue, byte nSaturation);    

	//------------------------用户管理-----------------------
	// 查询用户信息--最大支持64通道设备  
	// pReserved指向void*  
	public boolean CLIENT_QueryUserInfoNew(NativeLong lLoginID, USER_MANAGE_INFO_NEW info, Pointer pReserved, int nWaittime);
	
	// 设置用户信息接口--操作设备用户--最大支持64通道设备
	// opParam指向void*           subParam指向void*   
	// pReserved指向void*       
	// opParam（设置用户信息的输入缓冲）和subParam（设置用户信息的辅助输入缓冲）对应结构体类型USER_GROUP_INFO_NEW或USER_INFO_NEW
	public boolean CLIENT_OperateUserInfoNew(NativeLong lLoginID, int nOperateType, Pointer opParam, Pointer subParam, Pointer pReserved, int nWaittime);
	
	
	//----------------------语音对讲--------------------------
	// 向设备发起语音对讲请求          pfcb是用户自定义的数据回调接口, pfAudioDataCallBack 回调
	public NativeLong CLIENT_StartTalkEx(NativeLong lLoginID, StdCallCallback pfcb, NativeLong dwUser);
	
	// 停止语音对讲        lTalkHandle语音对讲句柄，是CLIENT_StartTalkEx的返回 值
    public boolean CLIENT_StopTalkEx(NativeLong lTalkHandle);

    // 启动本地录音功能(只在Windows平台下有效)，录音采集出来的音频数据通过CLIENT_StartTalkEx的回调函数回调给用户，对应操作是CLIENT_RecordStopEx
    // lLoginID是CLIENT_Login的返回值 
    public boolean CLIENT_RecordStartEx(NativeLong lLoginID);

    // 停止本地录音(只在Windows平台下有效)，对应操作是CLIENT_RecordStartEx。
    public boolean CLIENT_RecordStopEx(NativeLong lLoginID);
    
    // 向设备发送用户的音频数据，这里的数据可以是从CLIENT_StartTalkEx的回调接口中回调出来的数据
    public NativeLong CLIENT_TalkSendData(NativeLong lTalkHandle, Pointer pSendBuf, int dwBufSize);
    
    // 解码音频数据扩展接口(只在Windows平台下有效)    pAudioDataBuf是要求解码的音频数据内容 
    public void CLIENT_AudioDec(Pointer pAudioDataBuf, int dwBufSize);
    public boolean CLIENT_AudioDecEx(NativeLong lTalkHandle, Pointer pAudioDataBuf, int dwBufSize);
    
    //-------------------白名单-------------------------
    // 按查询条件查询记录          pInParam查询记录参数        pOutParam返回查询句柄  
    // 可以先调用本接口获得查询句柄，再调用  CLIENT_FindNextRecord函数获取记录列表，查询完毕可以调用CLIENT_FindRecordClose关闭查询句柄。 
    public boolean CLIENT_FindRecord(NativeLong lLoginID, NET_IN_FIND_RECORD_PARAM pInParam, NET_OUT_FIND_RECORD_PARAM pOutParam, int waittime);
    
    // 查找记录:nFilecount:需要查询的条数, 返回值为媒体文件条数 返回值小于nFilecount则相应时间段内的文件查询完毕
    public boolean CLIENT_FindNextRecord(NET_IN_FIND_NEXT_RECORD_PARAM pInParam, NET_OUT_FIND_NEXT_RECORD_PARAM pOutParam, int waittime);
    
    // 结束记录查找,lFindHandle是CLIENT_FindRecord的返回值 
    public boolean CLIENT_FindRecordClose(NativeLong lFindHandle);
    
    // 查找记录条数,pInParam与pOutParam内存由用户申请释放
    public boolean CLIENT_QueryRecordCount(NET_IN_QUEYT_RECORD_COUNT_PARAM pInParam, NET_OUT_QUEYT_RECORD_COUNT_PARAM pOutParam, int waittime);
    
    // 黑白名单操作 ,pstOutParam = null;
    public boolean CLIENT_OperateTrafficList(NativeLong lLoginID ,  NET_IN_OPERATE_TRAFFIC_LIST_RECORD pstInParam, NET_OUT_OPERATE_TRAFFIC_LIST_RECORD pstOutParam , int waittime);
    
    // 文件上传控制接口，白名单上传需要三个步骤配合使用，CLIENT_FileTransmit的 NET_DEV_BLACKWHITETRANS_START、  NET_DEV_BLACKWHITETRANS_SEND、   NET_DEV_BLACKWHITETRANS_STOP，如下所示
    // fTransFileCallBack 回调
    public NativeLong CLIENT_FileTransmit(NativeLong lLoginID, int nTransType, Pointer szInBuf, int nInBufLen, StdCallCallback cbTransFile, NativeLong dwUserData, int waittime);    

  	// 查询设备信息
  	public boolean CLIENT_QueryDevInfo(NativeLong lLoginID, int nQueryType, Pointer pInBuf, Pointer pOutBuf, Pointer pReservedL, int nWaitTime);
  	
  	// ------------------车载GPS------------------------- 	
  	// 设置GPS订阅回调函数--扩展, fGPSRevEx 回调
  	public void CLIENT_SetSubcribeGPSCallBackEX(StdCallCallback OnGPSMessage, NativeLong dwUser);
  	
    // 设置GPS订阅回调函数--扩展2， fGPSRevEx2 回调
  	public void CLIENT_SetSubcribeGPSCallBackEX2(StdCallCallback OnGPSMessage, NativeLong dwUser);
  	
  	// GPS信息订阅       
  	// bStart:表明是订阅还是取消          InterTime:订阅时间内GPS发送频率(单位秒)
  	// KeepTime:订阅持续时间(单位秒) 值为-1时,订阅时间为极大值,可视为永久订阅     
  	// 订阅时间内GPS发送频率(单位秒)
  	public boolean CLIENT_SubcribeGPS (NativeLong lLoginID, int bStart, int KeepTime, int InterTime);
	
    // 同步文件上传, 只适用于小文件
  	public boolean CLIENT_UploadRemoteFile(NativeLong lLoginID, NET_IN_UPLOAD_REMOTE_FILE pInParam, NET_OUT_UPLOAD_REMOTE_FILE pOutParam, int nWaitTime);

    // 过车记录订阅
  	public NativeLong CLIENT_ParkingControlAttachRecord(NativeLong lLoginID, NET_IN_PARKING_CONTROL_PARAM pInParam, NET_OUT_PARKING_CONTROL_PARAM pOutParam, int nWaitTime);
  	
  	// 取消过车记录订阅
  	public boolean CLIENT_ParkingControlDetachRecord(NativeLong lAttachHandle);
 
    // 开始过车记录查询
  	public NativeLong CLIENT_ParkingControlStartFind(NativeLong lLoginID, NET_IN_PARKING_CONTROL_START_FIND_PARAM pInParam, NET_OUT_PARKING_CONTROL_START_FIND_PARAM pOutParam, int waittime);

  	// 获取过车记录
  	public boolean CLIENT_ParkingControlDoFind(NativeLong lFindeHandle, NET_IN_PARKING_CONTROL_DO_FIND_PARAM pInParam, NET_OUT_PARKING_CONTROL_DO_FIND_PARAM pOutParam, int waittime);

  	// 结束过车记录查询
  	public boolean CLIENT_ParkingControlStopFind(NativeLong lFindHandle);
  	
  	// 车位状态订阅,pInParam与pOutParam内存由用户申请释放
  	public NativeLong CLIENT_ParkingControlAttachParkInfo(NativeLong lLoginID, NET_IN_PARK_INFO_PARAM pInParam, NET_OUT_PARK_INFO_PARAM pOutParam, int nWaitTime);

  	// 取消车位状态订阅
  	public boolean CLIENT_ParkingControlDetachParkInfo(NativeLong lAttachHandle);
  	
  	// 电源控制,pInParam与pOutParam内存由用户申请释放
  	public boolean CLIENT_PowerControl(NativeLong lLoginID, NET_IN_WM_POWER_CTRL pInParam, NET_OUT_WM_POWER_CTRL pOutParam, int nWaitTime);

  	// 载入/保存预案,pInParam与pOutParam内存由用户申请释放
  	public boolean CLIENT_LoadMonitorWallCollection(NativeLong lLoginID, NET_IN_WM_LOAD_COLLECTION pInParam, NET_OUT_WM_LOAD_COLLECTION pOutParam, int nWaitTime);
  	public boolean CLIENT_SaveMonitorWallCollection(NativeLong lLoginID, NET_IN_WM_SAVE_COLLECTION pInParam, NET_OUT_WM_SAVE_COLLECTION pOutParam, int nWaitTime);
  	
  	// 获取电视墙预案,pInParam与pOutParam内存由用户申请释放
  	public boolean CLIENT_GetMonitorWallCollections(NativeLong lLoginID, NET_IN_WM_GET_COLLECTIONS pInParam, NET_OUT_WM_GET_COLLECTIONS pOutParam, int nWaitTime);

  	// 查询/设置显示源(pstuSplitSrc内存由用户申请释放),  nWindow为-1表示所有窗口 ; pstuSplitSrc 对应 NET_SPLIT_SOURCE 指针
  	public boolean CLIENT_GetSplitSource(NativeLong lLoginID, int nChannel, int nWindow, NET_SPLIT_SOURCE[] pstuSplitSrc, int nMaxCount, IntByReference pnRetCount, int nWaitTime);
  	public boolean CLIENT_SetSplitSource(NativeLong lLoginID, int nChannel, int nWindow, NET_SPLIT_SOURCE pstuSplitSrc, int nSrcCount, int nWaitTime);

  	// 设置显示源, 支持同时设置多个窗口(pInparam, pOutParam内存由用户申请释放)
  	public boolean CLIENT_SplitSetMultiSource(NativeLong lLoginID, NET_IN_SPLIT_SET_MULTI_SOURCE pInParam, NET_OUT_SPLIT_SET_MULTI_SOURCE pOutParam, int nWaitTime);
  	
  	// 查询矩阵子卡信息(pstuCardList内存由用户申请释放)
  	public boolean CLIENT_QueryMatrixCardInfo(NativeLong lLoginID, NET_MATRIX_CARD_LIST pstuCardList, int nWaitTime);
  	
    // 开始查找录像文件帧信息(pInParam, pOutParam内存由用户申请释放)
  	public boolean CLIENT_FindFrameInfo(NativeLong lLoginID, NET_IN_FIND_FRAMEINFO_PRAM pInParam, NET_OUT_FIND_FRAMEINFO_PRAM pOutParam, int nWaitTime);

  	// 获取标签信息
  	public boolean CLIENT_FileStreamGetTags(NativeLong lFindHandle, NET_IN_FILE_STREAM_GET_TAGS_INFO pInParam, NET_OUT_FILE_STREAM_GET_TAGS_INFO pOutParam, int nWaitTime);

  	// 设置标签信息
  	public boolean CLIENT_FileStreamSetTags(NativeLong lFindHandle, NET_IN_FILE_STREAM_TAGS_INFO pInParam, NET_OUT_FILE_STREAM_TAGS_INFO pOutParam, int nWaitTime);

    // 查询/设置分割模式(pstuSplitInfo内存由用户申请释放)
  	public boolean CLIENT_GetSplitMode(NativeLong lLoginID, int nChannel, NET_SPLIT_MODE_INFO pstuSplitInfo, int nWaitTime);
  	public boolean CLIENT_SetSplitMode(NativeLong lLoginID, int nChannel, NET_SPLIT_MODE_INFO pstuSplitInfo, int nWaitTime);

  	// 查询分割能力(pstuCaps内存由用户申请释放)
  	public boolean CLIENT_GetSplitCaps(NativeLong lLoginID, int nChannel, NET_SPLIT_CAPS pstuCaps, int nWaitTime);	

	// 下位矩阵切换(pInparam, pOutParam内存由用户申请释放)
	public boolean CLIENT_MatrixSwitch(NativeLong lLoginID, NET_IN_MATRIX_SWITCH pInParam, NET_OUT_MATRIX_SWITCH pOutParam, int nWaitTime);

	// 打开刻录会话, 返回刻录会话句柄,pstInParam与pstOutParam内存由用户申请释放
	public NativeLong CLIENT_StartBurnSession(NativeLong lLoginID, NET_IN_START_BURN_SESSION pstInParam, NET_OUT_START_BURN_SESSION pstOutParam, int nWaitTime);

	// 关闭刻录会话
	public boolean CLIENT_StopBurnSession(NativeLong lBurnSession);
	
	//------------有盘/无盘刻录----lBurnSession 是 CLIENT_StartBurnSession返回的句柄//	
	// 开始刻录,pstInParam与pstOutParam内存由用户申请释放
	public boolean CLIENT_StartBurn(NativeLong lBurnSession, NET_IN_START_BURN pstInParam, NET_OUT_START_BURN pstOutParam, int nWaitTime);
	// 停止刻录
	public boolean CLIENT_StopBurn(NativeLong lBurnSession);
	
	// 监听刻录状态,pstInParam与pstOutParam内存由用户申请释放
//	public NativeLong CLIENT_AttachBurnState(NativeLong lLoginID, NET_IN_ATTACH_STATE pstInParam, NET_OUT_ATTACH_STATE pstOutParam, int nWaitTime);

	// 取消监听刻录状态,lAttachHandle是CLIENT_AttachBurnState返回值
	public boolean CLIENT_DetachBurnState(NativeLong lAttachHandle);
	
  	// 下载指定的智能分析数据 - 图片, fDownLoadPosCallBack 回调
  	// emType 参考 EM_FILE_QUERY_TYPE
  	public NativeLong CLIENT_DownloadMediaFile(NativeLong lLoginID, int emType, Pointer lpMediaFileInfo, String sSavedFileName, StdCallCallback cbDownLoadPos, Pointer dwUserData,  Pointer reserved);

  	// 停止下载数据
  	public boolean CLIENT_StopDownloadMediaFile(NativeLong lFileHandle);

  	// 下发通知到设备 接口, 以emNotifyType来区分下发的通知类型, pInParam 和 pOutParam 都由用户来分配和释放, emNotifyType对应结构体 NET_EM_NOTIFY_TYPE
  	public boolean CLIENT_SendNotifyToDev(NativeLong lLoginID, int emNotifyType, Pointer pInParam, Pointer pOutParam, int nWaitTime);

  	// 查询IO状态(pState内存由用户申请释放,根据emType对应的类型找到相应的结构体，进而确定申请内存大小), emType 参考 NET_IOTYPE
  	public boolean CLIENT_QueryIOControlState(NativeLong lLoginID, int emType, 
  	                                           Pointer pState, int maxlen, IntByReference nIOCount, int waittime);
  	
  	// IO控制(pState内存由用户申请释放,根据emType对应的类型找到相应的结构体，进而确定申请内存大小),emType 参考 NET_IOTYPE
  	public boolean CLIENT_IOControl(NativeLong lLoginID, int emType, Pointer pState, int maxlen);
  	
  	// 订阅监测点位信息,pInParam与pOutParam内存由用户申请释放
  	public NativeLong CLIENT_SCADAAttachInfo(NativeLong lLoginID, NET_IN_SCADA_ATTACH_INFO pInParam, NET_OUT_SCADA_ATTACH_INFO pOutParam, int nWaitTime);

  	// 取消监测点位信息订阅
  	public boolean CLIENT_SCADADetachInfo(NativeLong lAttachHandle);
  	
  	// 创建透明串口通道,TransComType高2个字节表示串口序号,低2个字节表示串口类型,目前类型支持 0：串口(232), 1:485
  	// baudrate 串口的波特率，1~8分别表示1200，2400，4800，9600，19200，38400，57600，115200 
  	// databits 串口的数据位 4~8表示4位~8位 
  	// stopbits 串口的停止位   232串口 ： 数值0 代表停止位1; 数值1 代表停止位1.5; 数值2 代表停止位2.    485串口 ： 数值1 代表停止位1; 数值2 代表停止位2.
  	// parity 串口的检验位，0：无校验，1：奇校验；2：偶校验; 
  	// cbTransCom 串口数据回调，回调出前端设备发过来的信息
  	// fTransComCallBack 回调
  	public NativeLong CLIENT_CreateTransComChannel(NativeLong lLoginID, int TransComType, int baudrate, int databits, int stopbits, int parity, StdCallCallback cbTransCom, NativeLong dwUser);

  	// 透明串口发送数据(pBuffer内存由用户申请释放)
  	public boolean CLIENT_SendTransComData(NativeLong lTransComChannel, byte[] pBuffer, int dwBufSize);

  	// 释放通明串口通道
  	public boolean CLIENT_DestroyTransComChannel(NativeLong lTransComChannel);

  	// 查询透明串口状态(pCommState内存由用户申请释放), TransComType 低2个字节表示串口类型， 0:串口(232)， 1:485口；高2个字节表示串口通道号，从0开始 
  	public boolean CLIENT_QueryTransComParams(NativeLong lLoginID, int TransComType, NET_COMM_STATE pCommState, int nWaitTime);

  	// 订阅智能分析进度（适用于视频分析源为录像文件时）,pstInParam与pstOutParam内存由用户申请释放
  	public boolean CLIENT_AttachVideoAnalyseState(NativeLong lLoginID, NET_IN_ATTACH_VIDEOANALYSE_STATE pstInParam, NET_OUT_ATTACH_VIDEOANALYSE_STATE pstOutParam, int nWaittime);

  	// 停止订阅
  	public boolean CLIENT_DetachVideoAnalyseState(NativeLong lAttachHandle);
  	
  	// 抓图, hPlayHandle为监视或回放句柄
  	public boolean CLIENT_CapturePicture(NativeLong hPlayHandle, String pchPicFileName);
  	
  	// 获取设备自检信息,pInParam与pOutParam内存由用户申请释放
  	public boolean CLIENT_GetSelfCheckInfo(NativeLong lLoginID, NET_IN_GET_SELTCHECK_INFO pInParam, NET_SELFCHECK_INFO pOutParam, int nWaitTime);

  	// 主动注册功能,启动服务；nTimeout参数已无效 . 
  	// cbListen对象为  fServiceCallBack 子类 
  	public NativeLong CLIENT_ListenServer(String ip, int port, int nTimeout, StdCallCallback cbListen, NativeLong dwUserData);
  	
  	// 停止服务
  	public boolean CLIENT_StopListenServer(NativeLong lServerHandle);
  	
	// 指定回调数据类型 实施监视(预览), 数据回调函数 cbRealData 中得到的码流类型为 emDataType 所指定的类型
  	public NativeLong CLIENT_RealPlayByDataType(NativeLong lLoginID, NET_IN_REALPLAY_BY_DATA_TYPE pstInParam, NET_OUT_REALPLAY_BY_DATA_TYPE pstOutParam, int dwWaitTime);

  	/************************************************************************/
  	/*                            BUS订阅                                   */
  	/************************************************************************/
  	// 订阅Bus状态,pstuInBus与pstuOutBus内存由用户申请释放
  	public NativeLong CLIENT_AttachBusState(NativeLong lLoginID, NET_IN_BUS_ATTACH pstuInBus, NET_OUT_BUS_ATTACH pstuOutBus, int nWaitTime);

  	// 停止订阅Bus状态,lAttachHandle是CLIENT_AttachBusState返回值
  	public boolean CLIENT_DetachBusState(NativeLong lAttachHandle);
  
   //订阅事件重传,pInParam内存由用户申请释放
   public NativeLong CLIENT_AttachEventRestore(NativeLong lLoginID, NET_IN_ATTACH_EVENT_RESTORE pInParam, int nWaitTime);

   // 停止订阅事件重传,pInParam内存由用户申请释放
   public boolean CLIENT_DetachEventRestore(NativeLong lAttachHandle);
   
   // 设置GPS温湿度订阅回调函数, fGPSTempHumidityRev
   public void CLIENT_SetSubcribeGPSTHCallBack(StdCallCallback OnGPSMessage, NativeLong dwUser);

   // GPS温湿度信息订阅, bStart为BOOL类型
   public boolean CLIENT_SubcribeGPSTempHumidity(NativeLong lLoginID, int bStart,  int InterTime, Point Reserved);
   
   /**************************************************************************************
   *   Funcname: CLIENT_FaceInfoOpreate
   *   Purpose:人脸信息记录操作函数
   *   InputParam:	LLONG		:lLoginID		// 登陆句柄
   *   InputParam:     EM_FACEINFO_OPREATE_TYPE: emType //操作类型
   *   InputParam:	void*		:pInParam	// 接口输入参数, 资源由用户维护
   *   OutputParam:	void*		:pOutParam	// 接口输出参数, 资源由用户维护
   *   InputParam:	int			:nWaitTime	// 等待超时时间
   *   Return:		BOOL
   *   Created:		%2017%:%08%:%30%  
   *   Revision Record:    date:author:modify sth
   **************************************************************************************/
   public boolean CLIENT_FaceInfoOpreate(NativeLong lLoginID, int emType, Pointer pInParam, Pointer pOutParam, int nWaitTime);

}
