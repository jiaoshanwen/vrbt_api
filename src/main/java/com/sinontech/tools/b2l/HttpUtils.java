package com.sinontech.tools.b2l;



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

import com.sinontech.tools.common.MD5;



public class HttpUtils {

	private static final Logger logger = Logger.getLogger(HttpUtils.class);

	public static JSONObject postMethod(String url, String query,String tradeOrderSeq){
	    StringBuffer resultJson = new StringBuffer("");
	    URL restURL = null;
	    HttpURLConnection conn = null;
	    PrintStream ps = null;
	    BufferedReader br = null;
	    try {
	    	System.out.println("请求地址："+url);
	    	logger.info("请求参数："+query);
			restURL = new URL(url);
			conn = (HttpURLConnection) restURL.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("appCode", B2lInterface.APPCODE);
			logger.info("appCode:"+B2lInterface.APPCODE);
			conn.setRequestProperty("signType", "MD5");
			logger.info("signType:MD5");
			conn.setRequestProperty("tradeOrderSeq", tradeOrderSeq);
			logger.info("tradeOrderSeq:"+tradeOrderSeq);
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			logger.info("Content-Type:application/json");
			String sign= MD5.md5(tradeOrderSeq+B2lInterface.APPCODE+"MD5"+B2lInterface.APPKEY);
			logger.info("sign:"+sign);
			conn.setRequestProperty("sign", sign);
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
			logger.info("接口返回："+resultJson.toString());
		    return JSONObject.fromObject(resultJson.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
			logger.info("异常1："+e.toString());
		} catch (ProtocolException e) {
			e.printStackTrace();
			logger.info("异常2："+e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			logger.info("异常3："+e.toString());
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
	
}
