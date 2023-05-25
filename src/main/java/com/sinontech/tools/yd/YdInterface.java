package com.sinontech.tools.yd;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

public class YdInterface {
	
	private static final Logger logger = Logger.getLogger(YdInterface.class);

	public static String CHANNEL_ID = "3021";
	public static final String BASEURL = "https://www.yidong77.com";
	
	

	/**
	 * 发送订购短信
	 * @param mobile
	 * @param contentId
	 * @return
	 */
	public static String sendMsg(String mobile,String contentId) {
		String args = "mobile="+mobile+"&contentId="+contentId+"&channel="+CHANNEL_ID;
		logger.info(args);
		Long start = System.currentTimeMillis();
		JSONObject json = HttpUtils.getMethod(YdInterface.BASEURL+"/toneif/sendSms.htm?"+args);
		logger.info("发短信接口消耗时间："+(System.currentTimeMillis()-start)/1000);
		logger.info(json.toString());
		return json.toString();
	}
	/**
	 * 提交短信验证码并订购
	 * @param mobile
	 * @param smsCode
	 * @param transId
	 * @return
	 */
	public static String submitSmsCode(String mobile,String smsCode,String transId,String oid) {
		String args = "mobile="+mobile+"&smsCode="+smsCode+"&transId="+transId+"&channel="+CHANNEL_ID+"&oid="+oid;
		Long start = System.currentTimeMillis();
		JSONObject json = HttpUtils.getMethod(YdInterface.BASEURL+"/toneif/submitSmsCode.htm?"+args);
		logger.info("提交短信验证码接口消耗时间："+(System.currentTimeMillis()-start)/1000);
		return json.toString();
	}
	/**
	 * 获取彩铃列表
	 * @param pageCode 页码，大于等于1
	 * @param pageSize 每页条数
	 * @param type 视频彩铃类型：1  风景   2 民族
	 * @param boxId 视频彩铃订阅包编号，目前只有一个彩铃包，可不传
	 * @return
	 */
	public static String selectVideoList(String pageCode,String pageSize,String type,String boxId) {
		String args = "pageCode="+pageCode+"&pageSize="+pageSize+"&type="+type+"&channel="+CHANNEL_ID+"&boxId="+boxId;
		JSONObject json = HttpUtils.getMethod(YdInterface.BASEURL+"/toneif/selectVideoList.htm?"+args);
		return json.toString();
	}
	
	/**
	 * 获取token
	 * @param mobile
	 * @return
	 */
	public static String queryToken(String mobile) {
		String args = "mobile="+mobile+"&channel="+CHANNEL_ID;
		JSONObject json = HttpUtils.getMethod(YdInterface.BASEURL+"/toneif/queryToken.htm?"+args);
		return json.toString();
	}
	/**
	 * 查询视频彩铃包月包订购状态
	 * @param mobile
	 * @param token 登录凭证
	 * @param boxId 订阅包ID
	 * @return
	 * 
	 */
	public static String queryVideoCrbtMonthState(String mobile,String boxId) {
		JSONObject result = new JSONObject();
		String tokenStr = queryToken(mobile);
		JSONObject tokenJson = JSONObject.fromObject(tokenStr);
		String resCode = tokenJson.getString("resCode");
		if(tokenJson.containsKey("token")) {
			String token = tokenJson.getString("token");
			String args = "mobile="+mobile+"&channel="+CHANNEL_ID+"&token="+token+"&boxId="+boxId;
			JSONObject json = HttpUtils.getMethod(YdInterface.BASEURL+"/toneif/queryOrderState.htm?"+args);
			result.put("code", json.getString("resCode"));
			result.put("msg", json.getString("resMsg"));
			result.put("status", json.getString("status"));
		}else {
			result.put("code", "001");
			result.put("msg", "token获取失败");
		}
		return result.toString();
	}
}
