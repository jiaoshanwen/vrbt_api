package com.sinontech.tools.common;



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



public class CommonHttpUtils {

	private static final Logger logger = Logger.getLogger(CommonHttpUtils.class);

	public static JSONObject postMethod(String url, String query){
	    StringBuffer resultJson = new StringBuffer("");
	    URL restURL = null;
	    HttpURLConnection conn = null;
	    PrintStream ps = null;
	    BufferedReader br = null;
	    try {
	    	logger.info("请求地址："+url);
	    	logger.info("body参数："+query);
			restURL = new URL(url);
			conn = (HttpURLConnection) restURL.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setDoOutput(true);
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			ps = new PrintStream(conn.getOutputStream());
			ps.print(query);
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			String line = null;
			while((line = br.readLine()) != null ){
			  resultJson.append(line);
			}
			 return JSONObject.fromObject(resultJson.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br!=null) {
					br.close();
					br = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(ps!=null) {
				ps.close();
				ps = null;
			}
			if(conn!=null) {
				conn.disconnect();
				conn = null;
			}
		}
	    return null;
	  }
		public static JSONObject getMethod(String url){
		 String result = "";
		 URL restURL = null;
		 HttpURLConnection conn = null;
		 BufferedReader br = null;
	    try {
	    	logger.info("请求地址："+url);
			restURL = new URL(url);
			conn = (HttpURLConnection) restURL.openConnection();
			conn.setRequestMethod("GET"); // POST GET PUT DELETE
			conn.setRequestProperty("Accept", "application/json");
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			while((line = br.readLine()) != null ){
			  result = line;
			}
			return JSONObject.fromObject(result);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(br!=null) {
					br.close();
					br = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(conn!=null) {
				conn.disconnect();
				conn = null;
			}
		}
	    return null;
	  }
}
