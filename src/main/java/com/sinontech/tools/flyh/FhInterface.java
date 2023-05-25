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

public class FhInterface {
	
	private static final Logger logger = Logger.getLogger(FhInterface.class);

	public static final String BASEURL = "http://ctcrbt.feihu.net.cn:8001";
	private static final String AGENTKEY = "ccd01fbbad154239ba7b8b9e9899b7de";	
	

	/**
	 * 下单受理
	 * @param phone
	 * @return
	 */
	public static String doSubscribeAfterDIY(String phone,String accessUrl) {
		JSONObject json = CommonHttpUtils.postMethod(FhInterface.BASEURL+"/diy/diysubscribe?mdn="+phone+"&access_url="+accessUrl+"&agentKey="+AGENTKEY,"");
		logger.info(json.toString());
		return json.toString();
	}
	/**
	 * 查询用户套餐
	 * @param phone
	 * @return 
	 */
	public static String diyQuery(String phone) {
		JSONObject json = CommonHttpUtils.postMethod(FhInterface.BASEURL+"/xvideo/DiyQuery?mdn="+phone,"");
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
		JSONObject json = CommonHttpUtils.postMethod(FhInterface.BASEURL+"/xvideo/Order?mdn="+phone+"&order_no="+orderNo,"");
		logger.info(json.toString());
		return json.toString();
	}
}
