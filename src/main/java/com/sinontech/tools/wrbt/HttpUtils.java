package com.sinontech.tools.wrbt;



import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;



public class HttpUtils {

	private static final Logger logger = Logger.getLogger(HttpUtils.class);

	public static String httpGetTool(List<NameValuePair> nvps, String urlPath) {
		HttpClient http = HttpClients.createDefault();
		StringBuilder url = new StringBuilder();
		url.append(urlPath);
		if (null != nvps && nvps.size() > 0) {
			url.append("?");
			for (int i = 0; i < nvps.size(); i++) {
				try {
					url.append(nvps.get(i).getName()).append("=").append(
							URLEncoder.encode(nvps.get(i).getValue(),"utf-8")
							).append("&");
				} catch (UnsupportedEncodingException e) {
				
					e.printStackTrace();
				}
			}
			
			if(url.toString().endsWith("&")){
				url.deleteCharAt(url.lastIndexOf("&"));
			}
		}
		HttpGet httpget = new HttpGet(url.toString());
		StringBuffer sb = new StringBuffer();
		try {
			HttpResponse response = http.execute(httpget);
			HttpEntity entity = response.getEntity();
			logger.info("http response code = "
					+ response.getStatusLine().getStatusCode());
			// 显示结果
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent(), "UTF-8"));
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

	public static String httpPostTool(List<NameValuePair> nvps, String urlPath) {
		
		HttpClient http =  new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(urlPath);
		UrlEncodedFormEntity entityform;
		StringBuffer sb = new StringBuffer();
		try {
			entityform = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
			httpPost.setEntity(entityform);
			HttpResponse response = http.execute(httpPost);
			HttpEntity entity = response.getEntity();
			logger.info("http response code = "
					+ response.getStatusLine().getStatusCode());

			// 显示结果
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					entity.getContent(), "UTF-8"));
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
//		System.out.println(sb.toString());
		return sb.toString();

	}
	public static JSONObject postMethod(String url, String query){
	    StringBuffer resultJson = new StringBuffer("");
	    URL restURL = null;
	    HttpURLConnection conn = null;
	    try {
			restURL = new URL(url);
			conn = (HttpURLConnection) restURL.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setDoOutput(true);
			PrintStream ps = new PrintStream(conn.getOutputStream());
			ps.print(query);
			ps.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			String line;
			while((line = br.readLine()) != null ){
			  resultJson.append(line);
			}
			br.close();
			 return JSONObject.fromObject(resultJson.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(conn!=null) {
				conn.disconnect();
			}
		}
	    return null;
	  }
	public static JSONObject postMethod(String url, String query,String authorizationClient){
		System.out.println(url);
	    StringBuffer resultJson = new StringBuffer("");
	    URL restURL = null;
	    HttpURLConnection conn = null;
	    try {
			restURL = new URL(url);
			conn = (HttpURLConnection) restURL.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			//设置头部验证参数
			conn.addRequestProperty("AuthorizationClient", authorizationClient);
			conn.setDoOutput(true);
			PrintStream ps = new PrintStream(conn.getOutputStream());
			ps.print(query);
			ps.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			String line;
			while((line = br.readLine()) != null ){
			  resultJson.append(line);
			}
			br.close();
			 return JSONObject.fromObject(resultJson.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(conn!=null) {
				conn.disconnect();
			}
		}
	    return null;
	  }
		public static JSONObject getMethod(String url){
		 String result = "";
	    try {
//	    	System.out.println("请求地址："+url);
			URL restURL = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
			conn.setRequestMethod("GET"); // POST GET PUT DELETE
			conn.setRequestProperty("Accept", "application/json");
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			while((line = br.readLine()) != null ){
//			  System.out.println(line);
			  result = line;
			}
			br.close();
			return JSONObject.fromObject(result);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return null;
	  }
		public static JSONObject getMethod(String url,String authorizationClient){
			 String result = "";
		    try {
		    	System.out.println("请求地址："+url);
				URL restURL = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) restURL.openConnection();
				conn.setRequestMethod("GET"); // POST GET PUT DELETE
				conn.setRequestProperty("Accept", "application/json");
				
				conn.addRequestProperty("AuthorizationClient", authorizationClient);
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line = "";
				while((line = br.readLine()) != null ){
//				  System.out.println(line);
				  result = line;
				}
				br.close();
				return JSONObject.fromObject(result);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (ProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    return null;
		  }
}
