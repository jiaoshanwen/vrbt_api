package com.sinontech.tools.hn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.sinontech.tools.common.MD5;
import com.sinontech.tools.flyh.FhInterface;

import net.sf.json.JSONObject;

public class HnVrbtInterface {
	
	private static final Logger logger = Logger.getLogger(HnVrbtInterface.class);

	private static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDG9AV0EVzyd/1piU0mHOBK92NuqNFlcvAmplN3ydShY4lJycUzd1FsaC4OO7MPMv0URsKXsMlycxgCN88eZo2W+9PiJrtHTo9inN9B9k05R0yFKET8w3ZUobFjSo/yIBeez6jy467tMpgA3ufBkKn9MuTwKt7xp504FE9yh/1rGQIDAQAB";
	private static String SID = "xw_api";
	private static String SIDPWD = "xxl_XW328";
	private static String BASE_URL = "http://hn.118100.cn/crbt/api";
	

	public static String acceptOrder(String phone,String ringCode,String backUrl) throws Exception {
		Map<String, String> map=new HashMap<String, String>();
		map.put("phone", phone);
		map.put("ringCode", ringCode);
		map.put("backUrl", backUrl);
		map.put("sid",SID);
		map.put("sidpwd",SIDPWD);
		String MD5sign="";
		map=MD5.sortMapByKey(map);
		for (Map.Entry<String, String> entry:map.entrySet()) {
			MD5sign+=(entry.getKey()+entry.getValue());
		}
		String sign = MD5.md5Up(MD5sign);
		JSONObject fromObject = JSONObject.fromObject(map);
		fromObject.put("sign", sign);
		String encrypt = RSAEncrypt.encrypt(fromObject.toString(),PUBLIC_KEY);
		logger.info("加密后："+encrypt);
		String url=BASE_URL+"/user/acceptOrder?sign="+encrypt+"&sid="+SID;
		logger.info(url);
		String post = getMethod(url);
		logger.info(post);
		return post;
	}
	/**
	 * 发送订购短信
	 * @param phone
	 * @throws Exception
	 */
	public static String sendOrderMsg(String phone) throws Exception {
		Map<String, String> map=new HashMap<String, String>();
		map.put("mobile", phone);
		map.put("sid",SID);
		map.put("sidpwd",SIDPWD);
		String MD5sign="";
		map=MD5.sortMapByKey(map);
		for (Map.Entry<String, String> entry:map.entrySet()) {
			MD5sign+=(entry.getKey()+entry.getValue());
		}
		String sign = MD5.md5Up(MD5sign);
		JSONObject fromObject = JSONObject.fromObject(map);
		fromObject.put("sign", sign);
		String encrypt = RSAEncrypt.encrypt(fromObject.toString(),PUBLIC_KEY);
		logger.info("加密后："+encrypt);
		String url=BASE_URL+"/video/sendSms?sign="+encrypt+"&sid="+SID;
		logger.info(url);
		String post = getMethod(url);
		logger.info(post);
		return post;
	}
	/**
	 * 一键双开
	 * @param phone
	 * @throws Exception
	 */
	public static String openAndOrder(String phone,String ringCode,String smsCode) throws Exception {
		Map<String, String> map=new HashMap<String, String>();
		map.put("phone", phone);
		map.put("ringCode", ringCode);
		map.put("smsCode", smsCode);
		map.put("sid",SID);
		map.put("sidpwd",SIDPWD);
		String MD5sign="";
		map=MD5.sortMapByKey(map);
		for (Map.Entry<String, String> entry:map.entrySet()) {
			MD5sign+=(entry.getKey()+entry.getValue());
		}
		String sign = MD5.md5Up(MD5sign);
		JSONObject fromObject = JSONObject.fromObject(map);
		fromObject.put("sign", sign);
		String encrypt = RSAEncrypt.encrypt(fromObject.toString(),PUBLIC_KEY);
		logger.info("加密后："+encrypt);
		String url=BASE_URL+"/user/saveOperaRO?sign="+encrypt+"&sid="+SID;
		logger.info(url);
		String post = getMethod(url);
		logger.info(post);
		return post;
	}
	/**
	 * 开通心动会员
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	public static String updateCrbt(String phone,String smsCode) throws Exception {
		Map<String, String> map=new HashMap<String, String>();
		map.put("mobile", phone);
		map.put("smsCode", smsCode);
		map.put("sid",SID);
		map.put("sidpwd",SIDPWD);
		String MD5sign="";
		map=MD5.sortMapByKey(map);
		for (Map.Entry<String, String> entry:map.entrySet()) {
			MD5sign+=(entry.getKey()+entry.getValue());
		}
		String sign = MD5.md5Up(MD5sign);
		JSONObject fromObject = JSONObject.fromObject(map);
		fromObject.put("sign", sign);
		String encrypt = RSAEncrypt.encrypt(fromObject.toString(),PUBLIC_KEY);
		logger.info("加密后："+encrypt);
		String url=BASE_URL+"/user/updateCrbt?sign="+encrypt+"&sid="+SID;
		logger.info(url);
		String post = getMethod(url);
		logger.info(post);
		return post;
	}
	/**
	 * 查询用户视频彩铃状态
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	public static String queryVideoStatus(String phone) throws Exception {
		Map<String, String> map=new HashMap<String, String>();
		map.put("mobile", phone);
		map.put("sid",SID);
		map.put("sidpwd",SIDPWD);
		String MD5sign="";
		map=MD5.sortMapByKey(map);
		for (Map.Entry<String, String> entry:map.entrySet()) {
			MD5sign+=(entry.getKey()+entry.getValue());
		}
		String sign = MD5.md5Up(MD5sign);
		JSONObject fromObject = JSONObject.fromObject(map);
		fromObject.put("sign", sign);
		String encrypt = RSAEncrypt.encrypt(fromObject.toString(),PUBLIC_KEY);
		logger.info("加密后："+encrypt);
		String url=BASE_URL+"/video/queryVideoStatus?sign="+encrypt+"&sid="+SID;
		logger.info(url);
		String post = getMethod(url);
		logger.info(post);
		return post;
	}
	
	public static String ringList(String phone) throws Exception {
		Map<String, String> map=new HashMap<String, String>();
		map.put("mobile", phone);
		map.put("sid",SID);
		map.put("sidpwd",SIDPWD);
		String MD5sign="";
		map=MD5.sortMapByKey(map);
		for (Map.Entry<String, String> entry:map.entrySet()) {
			MD5sign+=(entry.getKey()+entry.getValue());
		}
		String sign = MD5.md5Up(MD5sign);
		JSONObject fromObject = JSONObject.fromObject(map);
		fromObject.put("sign", sign);
		String encrypt = RSAEncrypt.encrypt(fromObject.toString(),PUBLIC_KEY);
		logger.info("加密后："+encrypt);
		String url=BASE_URL+"/video/ringList?sign="+encrypt+"&sid="+SID;
		logger.info(url);
		String post = getMethod(url);
		logger.info(post);
		return post;
	}
	public static String getMethod(String url){
		 URL restURL = null;
		 HttpURLConnection conn = null;
		 BufferedReader br = null;
	    try {
			restURL = new URL(url);
			conn = (HttpURLConnection) restURL.openConnection();
			conn.setRequestMethod("GET"); // POST GET PUT DELETE
			conn.setRequestProperty("Accept", "application/json");
			conn.setConnectTimeout(40000);
			conn.setReadTimeout(40000);
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			String line = null;
			while((line = br.readLine()) != null ){
			  return line;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
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
