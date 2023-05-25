package com.sinontech.tools.b2l;

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

import com.sinontech.tools.common.DateUtil;
import com.sinontech.tools.common.MD5;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 浙江存量复杂加包
 * @author Administrator
 *
 */
public class B2lComInterface {
	
	private static final Logger logger = Logger.getLogger(B2lComInterface.class);

//	测试：http://122.229.8.198:9001
//	生产：http://122.229.17.201:9001
//测试环境：appCode：ZJWXZX appKye：ZJWXZX_PST%@
//生产环境：appCode：ZJWXZX appKye：ZJWXZX_PROD%@

public static final String BASEURL = "http://122.229.17.201:9001";
public static final String SRCSYSID = "XINWZ";
public static final String APPCODE = "ZJWXZX";
public static final String APPKEY = "ZJWXZX_PROD%@";
public static final String KEY = "b4f97cc9a3d0730982d522469fce10a4";
public static final String CLIENTIP = "47.92.77.102";
public static final String CHANNELCODE = "QD00083";
public static final String EVENTCODE = "EVTD000004220";



public static void main(String[] s) throws Exception {
	String phone = "";
	String evtCollectTime = DateUtil.getTime();

	String salerNo = "Y33010090588";
	String activityCode = "MKT051413";
	String resultStr = B2lComInterface.syncCollectContactInfoToAgent(phone, salerNo, evtCollectTime,activityCode);
	System.out.println(resultStr);
}



/**
 * 存量业务短信获取接口
 * @param phone
 * @return
 */
public static JSONObject verifycodeSend(String phone,String productName,String productId) {
	JSONObject requestJSON = new JSONObject();
	requestJSON.put("channel", APPCODE);
	requestJSON.put("serviceCode", "verifycodeSend");
	String inputtime = DateUtil.getChar14();
	JSONObject header = new JSONObject();
	header.put("srcsysid", SRCSYSID);
	header.put("clientip", CLIENTIP);
	header.put("inputtime", inputtime);
	String signStr = SRCSYSID+CLIENTIP+inputtime+KEY;
	String sign = MD5.md5(signStr);
	header.put("sign", sign);
	requestJSON.put("head", header);
	JSONObject body = new JSONObject();
	body.put("productName", productName);
	body.put("productId", productId);
	body.put("phone", phone);
	requestJSON.put("body", body);
	String tradeOrderSeq = System.currentTimeMillis()+phone;
	JSONObject json = HttpUtils.postMethod(B2lComInterface.BASEURL+"/b2i-api/apicontroller/yunxiaoServiceCall/verifycodeSend",requestJSON.toString(),tradeOrderSeq);
	logger.info("复杂加包发短信接口返回："+json.toString());
	return json;
}
/**
 * 受理
 * @param phone 号码
 * @param phoneArea 号码地市
 * @param productName 产品名称
 * @param productId 产品id
 * @param discountCode 优惠包名称 （简单加包业务必填）
 * @param orderNo
 * @param sequenceNo
 * @return
 */
public static JSONObject zhifuAndWxReseve(String phone,String phoneArea,String productName,String productId,String discountCode,
		String orderNo,String sequenceNo,String verifyCode,String salesPhone,String salesName) {
	JSONObject requestJSON = new JSONObject();
	requestJSON.put("channel", APPCODE);
	requestJSON.put("serviceCode", "zhifuAndWxReseve");
	String inputtime = DateUtil.getChar14();
	JSONObject header = new JSONObject();
	header.put("srcsysid", SRCSYSID);
	header.put("clientip", CLIENTIP);
	header.put("inputtime", inputtime);
	String signStr = SRCSYSID+CLIENTIP+inputtime+KEY;
	String sign = MD5.md5(signStr);
	header.put("sign", sign);
	requestJSON.put("head", header);
	JSONObject body = new JSONObject();
	body.put("orderNo", orderNo);
	body.put("sequenceNo", sequenceNo);
	body.put("productName", productName);
	body.put("productId", productId);
	body.put("discountCode", discountCode);
	body.put("verifyCode", verifyCode);
	body.put("customerPhone", phone);
	body.put("c3Name", phoneArea);
	body.put("salesPhone", salesPhone);
	body.put("salesName", salesName);
	requestJSON.put("body", body);
	String tradeOrderSeq = System.currentTimeMillis()+phone;
	JSONObject json = HttpUtils.postMethod(B2lComInterface.BASEURL+"/b2i-api/apicontroller/yunxiaoServiceCall/zhifubAndWxReseve",requestJSON.toString(),tradeOrderSeq);
	logger.info("复杂加包受理接口返回："+json.toString());
	return json;
}
/**
 * 订单详情查询
 * @param orderId
 * @return
 */
public static String yxOrderDetail(String orderId) {
	JSONObject requestJSON = new JSONObject();
	requestJSON.put("channel", APPCODE);
	requestJSON.put("serviceCode", "zhifuAndWxReseve");
	String inputtime = DateUtil.getChar14();
	JSONObject header = new JSONObject();
	header.put("srcsysid", SRCSYSID);
	header.put("clientip", CLIENTIP);
	header.put("inputtime", inputtime);
	String signStr = SRCSYSID+CLIENTIP+inputtime+KEY;
	String sign = MD5.md5(signStr);
	header.put("sign", sign);
	requestJSON.put("head", header);
	JSONObject body = new JSONObject();
	body.put("orderId", orderId);
	requestJSON.put("body", body);
	String tradeOrderSeq = System.currentTimeMillis()+orderId;
	JSONObject json = HttpUtils.postMethod(B2lComInterface.BASEURL+"/b2i-api/apicontroller/yunxiaoServiceCall/yxOrderDetail",requestJSON.toString(),tradeOrderSeq);
	logger.info(json.toString());
	return json.toString();
}

public static String prodList(String phone) {
	JSONObject requestJSON = new JSONObject();
	requestJSON.put("channel", APPCODE);
	requestJSON.put("serviceCode", "QRYPRODINSTLIST");
	requestJSON.put("accNum", phone);
	String inputtime = DateUtil.getChar14();
	JSONObject header = new JSONObject();
	header.put("srcsysid", SRCSYSID);
	header.put("clientip", CLIENTIP);
	header.put("inputtime", inputtime);
	String signStr = SRCSYSID+CLIENTIP+inputtime+KEY;
	String sign = MD5.md5(signStr);
	header.put("sign", sign);
	requestJSON.put("head", header);
	String tradeOrderSeq = System.currentTimeMillis()+phone;
	JSONObject json = HttpUtils.postMethod(B2lComInterface.BASEURL+"/b2i-api/apicontroller/uopCustBaseService/prodList",requestJSON.toString(),tradeOrderSeq);
	logger.info(json.toString());
	return json.toString();
}

/**
 * 校验是否可以办理
 * @param phone
 * @param salerNo
 * @param evtCollectTime
 * @param activityCode
 * @return
 */
public static String syncCollectContactInfoToAgent(String phone,String salerNo,String evtCollectTime,String activityCode) {
	JSONObject result = new JSONObject();
	String phoneStr = prodList(phone);
	JSONObject phoneJSON = JSONObject.fromObject(phoneStr);
	String phoneCode = phoneJSON.getString("code");
	if(phoneCode.equals("0000")) {
		JSONObject cdata = phoneJSON.getJSONObject("data");
		String resultCode  = cdata.getString("resultCode");
		if(resultCode.equals("0")) {
			JSONObject resultObject = cdata.getJSONArray("resultObject").getJSONObject(0);
			String areaId = resultObject.getString("areaId");
			String crmRowId = resultObject.getString("crmRowId");
			String custId = resultObject.getString("custId");
			JSONObject requestJSON = new JSONObject();
			requestJSON.put("channel", APPCODE);
			requestJSON.put("serviceCode", "SYNCCOLLECTCONTACTINFOTOAGENT");
			requestJSON.put("channelCode", CHANNELCODE);
			requestJSON.put("eventCode", EVENTCODE);
			requestJSON.put("accNbr", phone);
			requestJSON.put("integrationId", crmRowId);
			requestJSON.put("custId", custId);
			requestJSON.put("lanId", areaId);
			requestJSON.put("salerNo", salerNo);
			requestJSON.put("evtCollectTime", evtCollectTime);
			JSONObject evtContent = new JSONObject();
			evtContent.put("CPCP_GRATIFICATION", "");
			evtContent.put("CPCP_USER_ADDRESS", "");
			evtContent.put("CPCP_ACTIVITY_CODE", activityCode);
			requestJSON.put("evtContent", "\""+evtContent.toString()+"\"");
			String inputtime = DateUtil.getChar14();
			JSONObject header = new JSONObject();
			header.put("srcsysid", SRCSYSID);
			header.put("clientip", CLIENTIP);
			header.put("inputtime", inputtime);
			String signStr = SRCSYSID+CLIENTIP+inputtime+KEY;
			String sign = MD5.md5(signStr);
			header.put("sign", sign);
			requestJSON.put("head", header);
			String tradeOrderSeq = System.currentTimeMillis()+phone;
			result = HttpUtils.postMethod(B2lComInterface.BASEURL+"/b2i-api/apicontroller/iqcbroadbandController/syncCollectContactInfoToAgent",requestJSON.toString(),tradeOrderSeq);
			System.out.println(result.toString());
		}else {
			result.put("code", "-1");
			result.put("message", "无法受理");
		}
	}else {
		result.put("code", "-1");
		result.put("message", "无法受理");
	}
	return result.toString();
}

public static String yxOrderList(String phone,String salerNo,String evtCollectTime) {
	JSONObject result = new JSONObject();
	String phoneStr = prodList(phone);
	JSONObject phoneJSON = JSONObject.fromObject(phoneStr);
	String phoneCode = phoneJSON.getString("code");
	if(phoneCode.equals("0000")) {
		JSONObject cdata = phoneJSON.getJSONObject("data");
		String resultCode  = cdata.getString("resultCode");
		if(resultCode.equals("0")) {
			JSONObject resultObject = cdata.getJSONArray("resultObject").getJSONObject(0);
			String areaId = resultObject.getString("areaId");
			String crmRowId = resultObject.getString("crmRowId");
			String custId = resultObject.getString("custId");
			JSONObject requestJSON = new JSONObject();
			requestJSON.put("channel", APPCODE);
			requestJSON.put("serviceCode", "SYNCCOLLECTCONTACTINFOTOAGENT");
			requestJSON.put("channelCode", CHANNELCODE);
			requestJSON.put("eventCode", EVENTCODE);
			requestJSON.put("accNbr", phone);
			requestJSON.put("integrationId", crmRowId);
			requestJSON.put("custId", custId);
			requestJSON.put("lanId", areaId);
			requestJSON.put("salerNo", salerNo);
			requestJSON.put("evtCollectTime", evtCollectTime);
			JSONObject evtContent = new JSONObject();
			evtContent.put("CPCP_GRATIFICATION", "");
			evtContent.put("CPCP_USER_ADDRESS", "");
			requestJSON.put("evtContent", evtContent);
			String inputtime = DateUtil.getChar14();
			JSONObject header = new JSONObject();
			header.put("srcsysid", SRCSYSID);
			header.put("clientip", CLIENTIP);
			header.put("inputtime", inputtime);
			String signStr = SRCSYSID+CLIENTIP+inputtime+KEY;
			String sign = MD5.md5(signStr);
			header.put("sign", sign);
			requestJSON.put("head", header);
			String tradeOrderSeq = System.currentTimeMillis()+phone;
			result = HttpUtils.postMethod(B2lComInterface.BASEURL+"/b2i-api/apicontroller/yunxiaoServiceCall/yxOrderList",requestJSON.toString(),tradeOrderSeq);
			logger.info(result.toString());
		}else {
			result.put("code", "-1");
			result.put("message", "无法受理");
		}
	}else {
		result.put("code", "-1");
		result.put("message", "无法受理");
	}
	return result.toString();
}
}
