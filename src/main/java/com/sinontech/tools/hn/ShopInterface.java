package com.sinontech.tools.hn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.log4j.Logger;

import com.sinontech.tools.common.DateUtil;

import net.sf.json.JSONObject;

public class ShopInterface {
	private static final Logger logger = Logger.getLogger(ShopInterface.class);

	private final static String REQ_URL = "http://servicemp.114zhan.cn/UnifiedOrder/CreateOrder";
	public final static String NAME = "XW_01";
	public final static String KEY = "6aa1de599fe0f8b5b9181d93e69fae08";
//	private final static String CALLBACKURL = "http://hn.118100.cn/crbt/interceptorMessage/orderBack";
	private final static String CALLBACKURL = "http://ayy.sinontech.com/api/back/hnStatusSyn";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String userPhone = "15364071213";
//		String typeName = "视频彩铃";
//		String salesCode = "15367866886";
//		String areaId = "731";
//		String backUrl = "http://h5.sinontech.com:8085/act/act/addToneFreeOnProduct";
//		String customText = "视频彩铃是基于通信场景的5G娱乐应用。业务开通后，别人给您打电话时将看到一段高清短视频，展现个人与企业形象，观看免流量。您正在办理的是中国电信爱音乐的视频彩铃业务（18元/月），订购立即生效，不退订持续生效，可拨打4008308100或114退订。";
//		JSONObject result = order(userPhone,typeName,salesCode,backUrl,customText);
		checkhnphone("15367866886");
	}
	/**
	 * 
	 * @param userPhone 用户号码
	 * @param typeName 业务类型
	 * @param managerPhone 客户经理号码
	 * @param areaId 号码归属地
	 * @param backUrl 页面回调地址
	 * @param customText 页面自定义文本
	 * @return
	 */
	public static JSONObject order(String userPhone,String typeName,String managerPhone,String backUrl,String customText) {
		String checkhnphone = checkhnphone(userPhone);
		String areaCode=JSONObject.fromObject(checkhnphone).getString("areacode");
		if(areaCode.startsWith("0")){
			areaCode=areaCode.substring(1, areaCode.length());
		}
		String timestamp = DateUtil.getChar14();
		String signStr = NAME+userPhone+timestamp+KEY;
		String sign = MD5.md5(signStr).toUpperCase();
		JSONObject req = new JSONObject();
		req.put("name", NAME);
		req.put("timestamp", timestamp);
		req.put("sign", sign);
		req.put("orderType", "0");
		req.put("accNum", userPhone);
		req.put("typeName", typeName);
		req.put("lan_id", areaCode);
		req.put("offer_id", "0");
		req.put("prod_id", "0");
		req.put("sale_code", managerPhone);
		req.put("callbackUrl", CALLBACKURL);
		req.put("customText", customText);
		req.put("isDouble", "");
		req.put("productid", "");
		req.put("price", "");
		req.put("setdefault", "");
		req.put("PagebackUrl", backUrl);
		logger.info("请求参数："+req.toString());
		JSONObject json = postMethod(REQ_URL,req.toString());
		logger.info(json.toString());
		return json;
	}
	/**
	 * 查询湖南号码归属地市
	 * @param phone
	 * @return
	 */
	public static String checkhnphone(String phone ){
		String	url="http://218.77.121.75:8084/cms/webinface!ringACT.action?inuser=smstask&inpwd=smstask&phone="+phone+"&acname=get_phone_area";
		JSONObject result = postMethod(url,null);
		if(result==null) {
			 return "{}";
		}
		return result.toString();
	}
	public static JSONObject postMethod(String url, String query){
	    StringBuffer resultJson = new StringBuffer("");
	    URL restURL = null;
	    HttpURLConnection conn = null;
	    PrintStream ps = null;
	    BufferedReader br = null;
	    try {
			restURL = new URL(url);
			conn = (HttpURLConnection) restURL.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setDoOutput(true);
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			ps = new PrintStream(conn.getOutputStream());
			ps.print(query);
			ps.close();
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			String line = null;
			while((line = br.readLine()) != null ){
			  resultJson.append(line);
			}
			logger.info(resultJson);
			 return JSONObject.fromObject(resultJson.toString());
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
				// TODO Auto-generated catch block
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
