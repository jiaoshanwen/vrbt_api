package com.sinontech.tools.yd;

import org.apache.log4j.Logger;

import com.sinontech.tools.common.DateUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 甘肃移动产品受理
 * @author Administrator
 *
 */
public class GsYdInterface {
	
	private static final Logger logger = Logger.getLogger(GsYdInterface.class);

	public static String BUSICODE = "1100";

	public static String APP_ID = "F9AC1C8143E1136FE0537D0BA8C0FA83";
	public static String APP_SECRET = "F9AC336163D516A1E0537D0BA8C0B9C5";
	public static String REQUEST_IP = "47.92.109.52";
	public static final String BASEURL = "https://wap.gs.10086.cn/capability/api/v1";
	
	
	public static void main(String[] args) {
		//{"data":{"identifyingKey":"a112cdb374b849a09d625eeb3bebeaf9"},"execute_times":196,"respMsg":"成功","retCode":"111000"}

//		System.out.println(sendSms("19514305883","HD1000000073"));
		System.out.println(submit("19514305883","HD1000000073","906ef2f081d0448dbf22b7c83a7360dd","561546"));
//		System.out.println(query("19514305883","HD1000000073"));
	}
	
	/**
	 * 发送订购短信
	 * @param mobile
	 * @param contentId
	 * @return
	 */
	public static JSONObject sendSms(String phone,String offerCode) {
		JSONObject requestJs = new JSONObject();
		String reqTime = DateUtil.getChar14();
		JSONObject header = new JSONObject();
		header.put("appId", APP_ID);
		header.put("cmd", "DEL100025");
		header.put("reqTime", reqTime);
		header.put("userIp", REQUEST_IP);
		JSONObject body = new JSONObject();
		body.put("phoneNum", phone);
		body.put("busiType", "1");
		body.put("offerCode", offerCode);
		header = MacUtils.sort(header);
		body = MacUtils.sort(body);
		String data = MacUtils.toJson(body, header);
		String sign = MacUtils.hmacsha256(APP_SECRET, data);
		header.put("sign", sign);
		requestJs.put("header", header);
		requestJs.put("body", body);
		System.out.println(requestJs.toString());
		logger.info("甘肃移动请求参数："+requestJs.toString());
		JSONObject json = HttpUtils.postMethod(BASEURL,requestJs.toString());
		logger.info("甘肃移动请求返回："+json.toString());
		return json;
	}
	/**
	 * 业务受理
	 * @param phone
	 * @param offerCode
	 * @return
	 */
	public static JSONObject submit(String phone,String offerCode,String identifyingKey,String identifyingCode) {
		JSONObject requestJs = new JSONObject();
		String reqTime = DateUtil.getChar14();
		JSONObject header = new JSONObject();
		header.put("appId", APP_ID);
		header.put("cmd", "DEL100023");
		header.put("reqTime", reqTime);
		header.put("userIp", REQUEST_IP);
		JSONObject body = new JSONObject();
		body.put("identifyingKey", identifyingKey);
		body.put("identifyingCode", identifyingCode);
		body.put("offerCode", offerCode);
		body.put("operCode", "1");
		body.put("phoneNum", phone);
		JSONObject commonInfo = new JSONObject();
		commonInfo.put("accessNum", phone);
		commonInfo.put("actType", "1");
		commonInfo.put("busiCode", BUSICODE);
		commonInfo.put("countyId", "");
		commonInfo.put("districtId", "");
		body.put("commonInfo", commonInfo);
		header = MacUtils.sort(header);
		body = MacUtils.sort(body);
		String data = MacUtils.toJson(body, header);
		String sign = MacUtils.hmacsha256(APP_SECRET, data);
		header.put("sign", sign);
		requestJs.put("header", header);
		requestJs.put("body", body);
		System.out.println(requestJs.toString());
		logger.info("甘肃移动请求参数："+requestJs.toString());
		JSONObject json = HttpUtils.postMethod(BASEURL,requestJs.toString());
		logger.info("甘肃移动请求返回："+json.toString());
		return json;
	}
	/**
	 * 查询产品状态
	 * @param phone
	 * @param offerCode
	 * @return
	 */
	public static JSONObject query(String phone,String offerCode) {
		JSONObject resultJSON = new JSONObject();
		JSONObject requestJs = new JSONObject();
		String reqTime = DateUtil.getChar14();
		JSONObject header = new JSONObject();
		header.put("appId", APP_ID);
		header.put("cmd", "QRY100006");
		header.put("reqTime", reqTime);
		header.put("userIp", REQUEST_IP);
		JSONObject body = new JSONObject();
		body.put("phoneNum", phone);
		body.put("offerCode", offerCode);
		header = MacUtils.sort(header);
		body = MacUtils.sort(body);
		String data = MacUtils.toJson(body, header);
		String sign = MacUtils.hmacsha256(APP_SECRET, data);
		header.put("sign", sign);
		requestJs.put("header", header);
		requestJs.put("body", body);
		System.out.println(requestJs.toString());
		logger.info("甘肃移动请求参数："+requestJs.toString());
		JSONObject json = HttpUtils.postMethod(BASEURL,requestJs.toString());
		String retCode = json.getString("retCode");
		String respMsg = json.getString("respMsg");
		if(retCode.equals("111000")) {
			JSONObject repData = json.getJSONObject("data");
			JSONObject busiInfo = repData.getJSONObject("RSP_PARAM").getJSONObject("BUSI_INFO");
			if(busiInfo.size()==0) {
				resultJSON.put("code", "-1");
				resultJSON.put("msg", "无产品");
			}else {
				JSONArray offerList = busiInfo.getJSONArray("OFFER_INFO_LIST");
				if(offerList!=null&&offerList.size()>0) {
					JSONObject packageS = new JSONObject();
					JSONObject offerInfo = offerList.getJSONObject(0).getJSONObject("OFFER_INFO");
					String offerId = offerInfo.getString("OFFER_ID");
					String offerName = offerInfo.getString("OFFER_NAME");
					packageS.put("offerId", offerId);
					packageS.put("offerName", offerName);
					packageS.put("offerCode", offerCode);
					resultJSON.put("packages", packageS);
					resultJSON.put("code", "0");
					resultJSON.put("msg", "成功");
				}else {
					resultJSON.put("code", "-1");
					resultJSON.put("msg", "无产品");
				}
			}
		}else {
			resultJSON.put("code", retCode);
			resultJSON.put("msg", respMsg);
		}
		logger.info("甘肃移动请求返回："+json.toString());
		return resultJSON;
	}
}
