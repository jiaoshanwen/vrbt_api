package com.sinontech.tools.flyh;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import com.sinontech.tools.common.CommonHttpUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class FhAIInterface {
	
	private static final Logger logger = Logger.getLogger(FhAIInterface.class);

	public static final String BASEURL = "http://ctcrbt.feihu.net.cn:8001";
	private static final String AGENTKEY = "d8c6b25906fa4ee19a1ba2a1ebecdac5";	
	

	/**
	 * 下单受理
	 * @param phone
	 * @return
	 */
	public static String doSubscribe(String phone,String accessUrl) {
		JSONObject json = CommonHttpUtils.postMethod(FhAIInterface.BASEURL+"/Ai/doSubscribe?mdn="+phone+"&access_url="+accessUrl+"&agentKey="+AGENTKEY,"");
		logger.info(json.toString());
		return json.toString();
	}
	/**
	 * 查询用户套餐
	 * @param phone
	 * @return 
	 */
	public static String query(String phone) {
		JSONObject json = CommonHttpUtils.postMethod(FhAIInterface.BASEURL+"/Ai/Query?mdn="+phone,"");
		logger.info(json.toString());
		return json.toString();
	}
	
	/**
	 *查询订单状态
	 * @param phone
	 * @param orderNo
	 * @return
	 */
	public static String orderQuery(String phone,String orderNo) {
		JSONObject json = CommonHttpUtils.postMethod(FhAIInterface.BASEURL+"/Ai/Order?mdn="+phone+"&order_no="+orderNo,"");
		logger.info(json.toString());
		return json.toString();
	}
	
	public static void main(String args[]) {
//		System.out.println(FhAIInterface.doSubscribe("17316908160", "www.baidu.com"));
		System.out.println(FhAIInterface.orderQuery("17316908160","3c504f1cfb5c48d9a1154a7e7247adfe"));
	}
}
