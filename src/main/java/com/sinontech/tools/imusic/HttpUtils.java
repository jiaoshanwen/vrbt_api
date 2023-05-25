package com.sinontech.tools.imusic;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.log4j.Logger;

import com.sinontech.tools.common.PageData;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



public class HttpUtils {

	private static final Logger logger = Logger.getLogger(HttpUtils.class);

	public static String httpGetTool(List<NameValuePair> nvps, String urlPath,
			String methodName, String reusltType,PageData header) {
	    CloseableHttpClient http = HttpClients.createDefault();
		StringBuilder url = new StringBuilder();
		url.append(Configuration.BASEURL + urlPath + methodName
				+ reusltType);
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
		BufferedReader reader = null;
		try {
			RequestConfig config = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
			httpget.setConfig(config);
			setHeader(httpget, nvps,header);
			HttpResponse response = http.execute(httpget);
			HttpEntity entity = response.getEntity();
			logger.info("http response code = "+ response.getStatusLine().getStatusCode());
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
					reader = null;
				}
				if(http!=null) {
					http.close();
					http = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static String httpPostTool(List<NameValuePair> nvps, String urlPath,
			String methodName, String reusltType,PageData header) {
		
	    CloseableHttpClient http = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(Configuration.BASEURL + urlPath
				+ methodName + reusltType);
		UrlEncodedFormEntity entityform;
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = null;
		try {
			RequestConfig config = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
			httpPost.setConfig(config);
			entityform = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
			setHeader(httpPost, nvps,header);
			httpPost.setEntity(entityform);
			HttpResponse response = http.execute(httpPost);
			HttpEntity entity = response.getEntity();

			// 显示结果
			reader = new BufferedReader(new InputStreamReader(
					entity.getContent(), "UTF-8"));
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
	public static String httpPostTool(List<NameValuePair> requestNvps,List<NameValuePair> headerNvps, String urlPath,
			String methodName, String reusltType,PageData header) {
		
	    CloseableHttpClient http = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(Configuration.BASEURL + urlPath
				+ methodName + reusltType);
		UrlEncodedFormEntity entityform;
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = null;
		try {
			RequestConfig config = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
			httpPost.setConfig(config);
			entityform = new UrlEncodedFormEntity(requestNvps, HTTP.UTF_8);
			setHeader(httpPost, headerNvps,header);
			httpPost.setEntity(entityform);
			HttpResponse response = http.execute(httpPost);
			HttpEntity entity = response.getEntity();

			// 显示结果
			reader = new BufferedReader(new InputStreamReader(
					entity.getContent(), "UTF-8"));
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
//		System.out.println(sb.toString());
		return sb.toString();

	}
	public static void setHeader(HttpGet httpGet, List<NameValuePair> nvps,PageData header) {
		httpGet.setHeader("auth-deviceid", header.getString("appKey"));
		String channelId = header.getString("channelId");
		if(StringUtils.isBlank(channelId)) {
			channelId = Configuration.CHANNELID;
		}
		httpGet.setHeader("auth-channelid", channelId);
		java.text.SimpleDateFormat date = new SimpleDateFormat("yyyyMMddhhmmss");
		String timestamp = date.format(new Date());
		httpGet.setHeader("auth-timestamp", timestamp);
		httpGet.setHeader("auth-signature-method",
				Configuration.SIGNATUREMETHOD);

		StringBuffer sb = new StringBuffer();
		sb.append(header.getString("appKey")).append("&");
		sb.append(channelId).append("&");
		sb.append(timestamp).append("&");
		if (nvps != null && nvps.size() > 0) {
			for (NameValuePair n : nvps) {
				sb.append(n.getValue()).append("&");
				logger.info("请求参数："+n.getName()+":"+n.getValue());
			}
		}
		String auth_signature = AuthUtils.generateMac256Signature(
				header.getString("appSecret"), sb.substring(0, sb.length() - 1));
		httpGet.setHeader("auth-signature", auth_signature);
		logger.info("请求头部："+JSONArray.fromObject(httpGet.getAllHeaders()).toString());

	}

	public static void setHeader(HttpPost httpPost, List<NameValuePair> nvps,PageData header) {

		httpPost.setHeader("auth-deviceid", header.getString("appKey"));
		String channelId = header.getString("channelId");
		if(StringUtils.isBlank(channelId)) {
			channelId = Configuration.CHANNELID;
		}
		httpPost.setHeader("auth-channelid", channelId);
		java.text.SimpleDateFormat date = new SimpleDateFormat("yyyyMMddhhmmss");
		String timestamp = date.format(new Date());
		httpPost.setHeader("auth-timestamp", timestamp);
		httpPost.setHeader("auth-signature-method",Configuration.SIGNATUREMETHOD);

		StringBuffer sb = new StringBuffer();
		sb.append(header.getString("appKey")).append("&");
		sb.append(channelId).append("&");
		sb.append(timestamp).append("&");
		if (nvps != null && nvps.size() > 0) {
			for (NameValuePair n : nvps) {
				if(!n.getName().equals("ring_id")) {
					sb.append(n.getValue()).append("&");
				}
				logger.info("请求参数："+n.getName()+":"+n.getValue());
			}
		}
		String auth_signature = AuthUtils.generateMac256Signature(header.getString("appSecret"), sb.substring(0, sb.length() - 1));
		httpPost.setHeader("auth-signature", auth_signature);
		logger.info("请求头部："+JSONArray.fromObject(httpPost.getAllHeaders()).toString());
	}

}
