package com.sinontech.tools.common;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import net.sf.json.JSONObject;

public class XxlUtils {
	private static String POSTPORT = "https://xwcard.sinontech.com";
	private static String CHANNLNAME = "sinon";
	private static String CHANNELPWD = "sinon!@#";
	
	public static void main(String[] args){
//		System.out.println(backMsg("1220422148965625811","订购成功","1","2022-04-22"));
		System.out.println(getAreaByPhone("19956019692"));
	}
	public static String getAreaByPhone(String mobile) {
		JSONObject json = new JSONObject();
		json.put("mobile",mobile);
		json.put("channlName", CHANNLNAME);
		json.put("channelPwd", CHANNELPWD);
		String result = post(POSTPORT,json.toString(), "/ring-api/openapi/getAreaByPhone");
		return result;
	}
	/**
	 * 联通回调
	 * @param orderNum 订单号
	 * @param orderResult 结果描述
	 * @param orderStatus 结果1成功2失败
	 * @param orderTime 订购时间
	 * @param phone 订购号码
	 * @return
	 */
	public static String backMsg(String orderNum,String orderResult,String orderStatus,String orderTime) {
		JSONObject json = new JSONObject();
		json.put("orderNum",orderNum);
		json.put("orderResult", orderResult);
		json.put("orderStatus", orderStatus);
		json.put("orderTime", orderTime);
		String result = post(POSTPORT,json.toString(), "/ring-api/userring/duo/ltzyback");
		return result;
	}
	protected static String post(String QWREQUEST_IP, String param,
			String function) {
		String url = QWREQUEST_IP + function;
		// System.err.println(url);
		PostMethod postMethod = new PostMethod(url);
		String result = null;
		int status = 0;
		try {
			System.out.println("请求参数"+param.toString());
			postMethod.setRequestHeader("Content-Type","application/json");
			RequestEntity requestEntity = new ByteArrayRequestEntity(param.getBytes("utf-8"), "utf-8");
			postMethod.setRequestEntity(requestEntity);
			HttpClient httpClient = new HttpClient();
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
			
			httpClient.getParams().setContentCharset("UTF-8");
			status = httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
			result = result.trim();
			System.out.println(function + "," + status + "," + result);
			System.out.println("结束后：：："+result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}
}
