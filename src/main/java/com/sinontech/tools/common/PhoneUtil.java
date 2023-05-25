package com.sinontech.tools.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;


import net.sf.json.JSONObject;

public class PhoneUtil {
	
	private static final String LOGIN_NAME="sinonVedioRing";
	private static final String LOGIN_PWD="bSSdl7av9q";
	private static final String QUERYPORT="https://apixnh.xnhkfpt.com/api/sms/getphonecity";
	
	
	
	public static String queryPhoneInfo(String phone) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("phone", phone);
		
		PostMethod postMethod = new PostMethod(QUERYPORT);
		String result = null;
		int status = 0;
		try {
			String encrypt = Security.encrypt(LOGIN_PWD,jsonObject.toString());
			postMethod.addParameter("param", encrypt);
			postMethod.addParameter("login_name", LOGIN_NAME);
			HttpClient httpClient = new HttpClient();
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
			httpClient.getParams().setContentCharset("UTF-8");
			status = httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
			if(result!=null){
				return result;
			}else{
				return "0001";
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return null;
	}
	public static void main(String args[]) {
		String result = queryPhoneInfo("19351121181");
		System.out.println(result);
	}
}
