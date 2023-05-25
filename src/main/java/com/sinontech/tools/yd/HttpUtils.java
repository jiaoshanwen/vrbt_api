package com.sinontech.tools.yd;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;



public class HttpUtils {

	private static final Logger logger = Logger.getLogger(HttpUtils.class);
	
	public static String httpPostTool( String url,List<NameValuePair> nvps) {
	    CloseableHttpClient http = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		UrlEncodedFormEntity entityform;
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = null;
		try {
			RequestConfig config = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
			httpPost.setConfig(config);
			entityform = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
			httpPost.setEntity(entityform);
			HttpResponse response = http.execute(httpPost);
			HttpEntity entity = response.getEntity();
			// 显示结果
			reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if(http!=null) {
					http.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();

	}
	public static JSONObject postMethod(String url, String query){
		System.out.println(url);
		System.out.println(query);
	    StringBuffer resultJson = new StringBuffer("");
	    URL restURL = null;
	    HttpURLConnection conn = null;
	    try {
	    	logger.info("请求地址："+url);
	    	logger.info("请求参数："+query);
			restURL = new URL(url);
			conn = (HttpURLConnection) restURL.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Accept", "application/json");
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
