package com.zswy.server.wxlogin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zswy.server.conmmom.util.StrUtil;

import net.sf.json.JSONObject;

@Controller
public class WeiXinUserInfoController {
	
	@Autowired
	private WeiXinUserInfoService userService;

	/**
	 * 进行网页授权，便于获取到用户的绑定的内容
	 * 
	 * @param request
	 * @param session
	 * @param map
	 * @return
	 */
	//https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=http://localhost:8080/orderSys/tologin/userinfo.do&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect
	//成功后跳转页面
	@RequestMapping("/tologin/userinfo")
	public void check(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		// 首先判断一下session中，是否有保存着的当前用户的信息，有的话，就不需要进行重复请求信息
		WeiXinUser weiXinUser = null;
		if (session.getAttribute("currentUser") != null) {
			weiXinUser = (WeiXinUser) session.getAttribute("currentUser");
		} else {
			/**
			 * 进行获取openId，必须的一个参数，这个是当进行了授权页面的时候，再重定向了我们自己的一个页面的时候，
			 * 会在request页面中，新增这个字段信息，要结合这个ProjectConst.Get_WEIXINPAGE_Code这个常量思考
			 */
			String code = request.getParameter("code");
			try {
				// 得到当前用户的信息(具体信息就看weixinUser这个javabean)
				weiXinUser = getWeiXinUser(session, code);
				// 将获取到的用户信息，放入到session中
				session.setAttribute("currentUser", weiXinUser);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//map.put("weiXinUser", weiXinUser);
		response.sendRedirect("http://localhost:8080/orderSys/index.html?userinfo=");
	}

	@RequestMapping("/tologin")
	public void redirect(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		JSONObject OpenidJSONO = WeiXinUtils.doGetStr("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9c636504ad6b39b3&redirect_uri=http://localhost:8080/orderSys/tologin/userinfo.do&response_type=code&scope=snsapi_userinfo&state=1#wechat_redirect");
		try {
			String code = String.valueOf(OpenidJSONO.get("code"));
			if(StrUtil.isNullEmpty(code)){
				response.sendRedirect("http://localhost:8080/orderSys/index.html");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect("http://localhost:8080/orderSys/index.html");
		}
	}
	/**
	 * 获取用户的openId
	 * 
	 * @param session
	 * @param code
	 * @return 返回封装的微信用户的对象
	 */
	private WeiXinUser getWeiXinUser(HttpSession session, String code) {
		AccessToken accessToken = new AccessToken();
		String openId = "";
		if (code != null) {
			// 调用根据用户的code得到需要的授权信息
			accessToken = userService.getAccessTokenInfo(code);
		} else {
			
		}
		// 获取基础刷新的接口访问凭证（目前还没明白为什么用authInfo.get("AccessToken");这里面的access_token就不行）
		//String accessToken = WeiXinUtils.getAccessToken().getAccess_token();
		// 获取到微信用户的信息
		WeiXinUser userinfo = userService.getUserInfo(accessToken.getAccess_token(), openId);
		return userinfo;
	}

}
