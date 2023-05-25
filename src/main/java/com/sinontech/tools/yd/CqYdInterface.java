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

/**
 * 重庆移动产品包
 * @author Administrator
 *
 */
public class CqYdInterface {
	
	private static final Logger logger = Logger.getLogger(CqYdInterface.class);

	public static String CHANNEL_ID = "2221";
	public static final String BASEURL = "https://www.yidong77.com";
	
	public static void main(String[] s) throws Exception {
		//{"data":"20230331133956503573","resCode":"000000","resMsg":"success"}
//		System.out.println(sendMsg("13996887878"));
//		System.out.println(submitSmsCode("13996887878","792619","20230331133956503573"));
//		System.out.println(queryVideoCrbtMonthState("13996887878","698039035100000058"));
	}

	/**
	 * 发送订购短信
	 * @param mobile
	 * @param contentId
	 * @return {"data":"20230331133956503573","resCode":"000000","resMsg":"success"}
	 */
	public static String sendMsg(String mobile) {
		Long start = System.currentTimeMillis();
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("mobile", mobile));
		nvps.add(new BasicNameValuePair("channel", CHANNEL_ID));
		String returnStr = HttpUtils.httpPostTool(CqYdInterface.BASEURL+"/dq/sendSms.htm",nvps);
		logger.info("发短信接口消耗时间："+(System.currentTimeMillis()-start)/1000);
		logger.info("接口返回："+returnStr);
		return returnStr;
	}
	/**
	 * 提交短信验证码并订购
	 * @param mobile
	 * @param smsCode
	 * @param transId
	 * @return {"resCode":"000000","resMsg":"success"}
	 */
	public static String submitSmsCode(String mobile,String smsCode,String oid) {
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("mobile", mobile));
		nvps.add(new BasicNameValuePair("channel", CHANNEL_ID));
		nvps.add(new BasicNameValuePair("code", smsCode));
		nvps.add(new BasicNameValuePair("smsOid", oid));
		String returnStr = HttpUtils.httpPostTool(CqYdInterface.BASEURL+"/dq/order.htm",nvps);
		logger.info("接口返回："+returnStr);
		return returnStr;
	}
}
