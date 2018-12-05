package com.zswy.server.wxlogin;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

public class WeiXinUtils {
	/**
	 * Get请求，方便到一个url接口来获取结果
	 * 
	 * @param url
	 * @return
	 */
	public static JSONObject doGetStr(String url) {
		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		try {
			HttpResponse response = defaultHttpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				String result = EntityUtils.toString(entity, "UTF-8");
				jsonObject = JSONObject.fromObject(result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	/**
	 * 获取access_token
	 * 
	 * @return
	 */
	/*public static AccessToken getAccessToken() {
		AccessToken accessToken = new AccessToken();
		String url = ProjectConst.ACCESS_TOKEN_URL.replace("APPID", ProjectConst.PROJECT_APPID).replace("APPSECRET", ProjectConst.PROJECT_APPSECRET);
		JSONObject jsonObject = doGetStr(url);
		if (jsonObject != null) {
			accessToken.setAccess_token(jsonObject.getString("access_token"));
			accessToken.setExpires_in(jsonObject.getInt("expires_in"));
		}
		return accessToken;
	}*/
}
