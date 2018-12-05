package com.zswy.server.wxlogin;

import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

@Service
public class WeiXinUserInfoImlp implements WeiXinUserInfoService {

	/**
	 * 获取微信用户的信息
	 * 
	 * @param accessToken
	 * @param openId
	 * @return
	 */
	@Override
	public WeiXinUser getUserInfo(String accessToken, String openId) {
		WeiXinUser weixinUserInfo = null;
		// 拼接获取用户信息接口的请求地址
		String requestUrl = ProjectConst.GET_PAGEUSER_URL.replace("ACCESS_TOKEN", accessToken).replace("OPENID",
				openId);
		// 获取用户信息(返回的是Json格式内容)
		JSONObject jsonObject = WeiXinUtils.doGetStr(requestUrl);

		if (null != jsonObject) {
			try {
				// 封装获取到的用户信息
				weixinUserInfo = new WeiXinUser();
				// 用户的标识
				weixinUserInfo.setOpenId(jsonObject.getString("openid"));
				// 昵称
				weixinUserInfo.setNickname(jsonObject.getString("nickname"));
				// 用户的性别（1是男性，2是女性，0是未知）
				weixinUserInfo.setSex(jsonObject.getInt("sex"));
				// 用户所在国家
				weixinUserInfo.setCountry(jsonObject.getString("country"));
				// 用户所在省份
				weixinUserInfo.setProvince(jsonObject.getString("province"));
				// 用户所在城市
				weixinUserInfo.setCity(jsonObject.getString("city"));
				// 用户头像
				weixinUserInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
			} catch (Exception e) {
				if (0 == weixinUserInfo.getSubscribe()) {
					System.out.println("用户并没有关注本公众号");
				} else {
					int errorCode = jsonObject.getInt("errcode");
					String errorMsg = jsonObject.getString("errmsg");
					System.out.println("由于" + errorCode + "错误码；错误信息为：" + errorMsg + "；导致获取用户信息失败");
				}
			}
		}
		return weixinUserInfo;
	}

	/**
	 * 进行用户授权，获取到需要的授权字段，比如openId
	 * 
	 * @param code
	 *            识别得到用户id必须的一个值 得到网页授权凭证和用户id
	 * @return
	 */
	@Override
	public AccessToken getAccessTokenInfo(String code) {
		AccessToken accesstoken = new AccessToken();
		// 自己的配置appid（公众号进行查阅）
		String appid = ProjectConst.PROJECT_APPID;
		// 自己的配置APPSECRET;（公众号进行查阅）
		String appsecret = ProjectConst.PROJECT_APPSECRET;
		// 拼接用户授权接口信息
		String requestUrl = ProjectConst.GET_WEBAUTH_URL.replace("APPID", appid).replace("SECRET", appsecret)
				.replace("CODE", code);
		// 存储获取到的授权字段信息
		try {
			JSONObject OpenidJSONO = WeiXinUtils.doGetStr(requestUrl);
			// OpenidJSONO可以得到的内容：access_token expires_in refresh_token openid
			// scope
			String openid = String.valueOf(OpenidJSONO.get("openid"));
			String access_token = String.valueOf(OpenidJSONO.get("access_token"));
			// 用户保存的作用域
			String scope = String.valueOf(OpenidJSONO.get("scope"));
			String refresh_token = String.valueOf(OpenidJSONO.get("refresh_token"));
			int expires_in = Integer.parseInt(String.valueOf(OpenidJSONO.get("expires_in")));
			accesstoken.setAccess_token(access_token);
			accesstoken.setOpenid(openid);
			accesstoken.setExpires_in(expires_in);
			accesstoken.setRefresh_token(refresh_token);
			accesstoken.setScope(scope);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accesstoken;
	}

}
