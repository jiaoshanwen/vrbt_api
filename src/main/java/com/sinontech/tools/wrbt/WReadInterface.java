package com.sinontech.tools.wrbt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;


import net.sf.json.JSONObject;

public class WReadInterface {
	
	public static String CHANNELID = "15796169";
	public static String CLIENTID = "zj_xinwangzhuoxin";
	public static String CLIENTSECRET = "rV0SpM6uC7NUwgyioj";
	public static String CLIENTSOURCE = "11";
	public static final String BASEURL = "https://pcc.woread.com.cn";
	public static final String BACKURL = "http://ayy.sinontech.com/api/back/readSyn";//订购结果异步通知接口
	
	public static void main(String[] args) {
//		System.out.println(getAuthorization());
		String auto = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJjbGllbnRTb3VyY2UiOiIxMSIsInN1YiI6Im51bGwiLCJjbGllbnRJZCI6InpqX3hpbndhbmd6aHVveGluIiwicmVmcmVzaF9kYXRlIjoxNjU4OTEyNDE4LCJhcHBJZCI6InpqX3hpbndhbmd6aHVveGluIiwiY2xpZW50U291cmNlU2lnbiI6ImY5YmJmMDNhMTU3NzQxMzkxYzBjY2FmYWVjMWZjYjljIiwiaXNzIjoiaXJlYWQud28uY29tLmNuIiwiZXhwIjoxNjU2OTI1MjE4LCJpYXQiOjE2NTYzMjA0MTgsImp0aSI6InpqX3hpbndhbmd6aHVveGluOi0xNGU1YjViMy1mMWRiLTQzNzctOWQ2NS1mY2VkMTFkNGQxNWQifQ.Szp5vmbbFzV8iD7ZZRibvwYm8ufNDOCI3VKepesvIos";
//		System.out.println(getOrder("17681819638","7517","http://h5.sinontech.com:8085/act/act/indexH5Call","http://h5.sinontech.com:8085/act/act/indexH5Call",auto));
//		System.out.println(queryOrder("43d45b4b76a844699f66d1a87789b45cdc1fce9f785e4033bd350377518473e2","bed98e6d6204458c8c1c3b546b1aad7ce2f94dd9856f4bc9834891f19f4b5913",auto));
//		System.out.println(unsubscribeWithoutValidateCode("13067750310","7517",auto));
		System.out.println(getPkgOrderedStatus("13067750310","7517",auto));
//		String ss = "1111,222";
//		String[] sa = ss.split(",");
//		System.out.println(sa);
	}
	
	
	public static String getAuthorization(){
		String args = "clientSource="+CLIENTSOURCE+"&clientId="+CLIENTID+"&clientSecret="+CLIENTSECRET;
		JSONObject json = HttpUtils.getMethod(WReadInterface.BASEURL+"/oauth/client/key?"+args);
		return json.toString();
	}
	/**
	 * 下单
	 * @param phone
	 * @param productpkgid
	 * @param redirectUrl
	 * @param bakurl
	 * @param authorizationClient
	 * @return
	 */
	public static String getOrder(String phone,String productpkgid,String redirectUrl,String bakurl,String channlidkey,String authorizationClient) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("productpkgid",productpkgid);
		map.put("returl",redirectUrl);
		map.put("bakurl",bakurl);
		map.put("channlidkey",channlidkey);
		String query = JSONObject.fromObject(map).toString();
		System.out.println("请求json:"+query);
		JSONObject json = HttpUtils.postMethod(WReadInterface.BASEURL+"/cappay/rest/order/package/beforeordervac/"+CLIENTSOURCE+"/"+phone+"/"+CHANNELID, query,authorizationClient);
		return json.toString();
	}
	/**
	 * 查询订单
	 * @param torder 下单接口返回
	 * @param sign	  下单接口返回
	 * @param authorizationClient
	 * @return
	 */
	public static String queryOrder(String torder,String sign,String authorizationClient) {
		JSONObject json = HttpUtils.postMethod(WReadInterface.BASEURL+
				"/cappay/rest/order/package/validateOrder/"+torder+"/"+sign, "",authorizationClient);
		return json.toString();
	}
	/**
	 * 查询用户产品订购状态
	 * @param phone	号码
	 * @param productpkgid 产品id
	 * @param authorizationClient
	 * @return
	 */
	public static String getPkgOrderedStatus(String phone,String productpkgid,String authorizationClient){
		JSONObject json = HttpUtils.getMethod(WReadInterface.BASEURL+
				"/cappay/rest/order/package/getPkgOrderedStatus/"+CLIENTSOURCE+"/"+CHANNELID+"/"+phone+"/"+productpkgid,authorizationClient);
		return json.toString();
	}
	/**
	 * 退订产品
	 * @param phone 号码	
	 * @param productpkgid 产品id
	 * @param authorizationClient
	 * @return
	 */
	public static String unsubscribeWithoutValidateCode(String phone,String productpkgid,String authorizationClient){
		String args = "channelid="+CHANNELID+"&productid="+productpkgid;
		JSONObject json = HttpUtils.getMethod(WReadInterface.BASEURL+
				"/cappay/rest/unsubscribe/unsubscribeWithoutValidateCode/"+CLIENTSOURCE+"/"+phone+"?"+args,authorizationClient);
		return json.toString();
	}
	public static String httpPostTool(String url,List<NameValuePair> nvps,String authorizationClient) {
		CloseableHttpClient http = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		UrlEncodedFormEntity entityform;
		StringBuffer sb = new StringBuffer();
		try {
			entityform = new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8);
			httpPost.setHeader("AuthorizationClient", authorizationClient);
			httpPost.setEntity(entityform);
			HttpResponse response = http.execute(httpPost);
			HttpEntity entity = response.getEntity();
			// 显示结果
			BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			if (reader != null) {
				reader.close();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();

	}
}
