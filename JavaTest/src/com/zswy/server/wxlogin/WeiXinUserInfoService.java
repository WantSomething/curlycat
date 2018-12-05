package com.zswy.server.wxlogin;

public interface WeiXinUserInfoService {
	/**
	 * 获取到微信个人用户的信息
	 * 
	 * @param accessToken
	 * @param openId
	 * @return
	 */
	WeiXinUser getUserInfo(String accessToken, String openId);

	/**
	 * 用于获取网页授权后的信息字段，其中主要是获取openId及accesstoken
	 * 
	 * @param code
	 *            授权码
	 * @return
	 */
	AccessToken getAccessTokenInfo(String code);


}
