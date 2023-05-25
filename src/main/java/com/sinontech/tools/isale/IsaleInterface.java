package com.sinontech.tools.isale;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import com.sinontech.tools.common.ReaderXmlForSAX;
import com.sinontech.tools.imusic.VolteInterface;

import net.sf.json.JSONObject;

public class IsaleInterface {
	
	private static final Logger logger = Logger.getLogger(IsaleInterface.class);

	public static String SID = "heartthrob";
	public static String SIDPWD = "xwzxZJ135#";
	public static final String BASEURL = "https://www.118320.com";
	public static final String SMSURL = "http://172.26.101.255:8899";
	
	/**
	 * 发短信
	 * @param phone
	 * @param mess
	 * @return
	 */
	public static String sendMess(String phone, String mess) {
		URL url = null;
		InputStream is = null;
		String returnValue = null;
		try {
			mess = URLEncoder.encode(mess,"utf-8");
			String poiendpointnt = SMSURL+ "/cms/webinface!sendMsg.action?inuser=zjvideoring&inpwd=sinon135&momt=2&phone=" + phone + "&msg="+ mess;
			url = new URL(poiendpointnt);
			is = url.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null) {
				returnValue = line;
			}
			logger.info("发短信返回："+returnValue);
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnValue;
	}
	
	/**
	 *  isale心动会员受理
	 * @param phone
	 * @param areaCode
	 * @param promNbr
	 * @return
	 */
	public static JSONObject createSaleOrder(String phone,String areaCode,String promNbr){
		PostMethod postMethod = new PostMethod(IsaleInterface.BASEURL+"/spservice/createSaleOrderHeartthrob.do");
		HttpClient httpClient = new HttpClient();
		String result = "";
		int status = 0;
		try {
			postMethod.addParameter("sid", SID);
			postMethod.addParameter("sidpwd", SIDPWD);
			postMethod.addParameter("phone", phone);
			postMethod.addParameter("areaCode", areaCode);
			postMethod.addParameter("promNbr", promNbr);

			httpClient.getHttpConnectionManager().getParams()
					.setConnectionTimeout(3000);
			httpClient.getParams().setContentCharset("UTF-8");
			status = httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
			logger.info("result:"+result);
			Map<String,String> map = ReaderXmlForSAX.parse(result);
			return JSONObject.fromObject(map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
				httpClient.getHttpConnectionManager().closeIdleConnections(0);
			}
		}
		return null;
	}
	public static JSONObject createSaleOrderComfortBa(String phone,String areaCode,String promNbr){
		PostMethod postMethod = new PostMethod(IsaleInterface.BASEURL+"/spservice/createSaleOrderComfortBagController.do");
		HttpClient httpClient = new HttpClient();
		String result = "";
		int status = 0;
		try {
			postMethod.addParameter("sid", SID);
			postMethod.addParameter("sidpwd", SIDPWD);
			postMethod.addParameter("phone", phone);
			postMethod.addParameter("areaCode", areaCode);
			postMethod.addParameter("promNbr", promNbr);

			httpClient.getHttpConnectionManager().getParams()
					.setConnectionTimeout(3000);
			httpClient.getParams().setContentCharset("UTF-8");
			status = httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
			logger.info("result:"+result);
			Map<String,String> map = ReaderXmlForSAX.parse(result);
			return JSONObject.fromObject(map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
				httpClient.getHttpConnectionManager().closeIdleConnections(0);
			}
		}
		return null;
	}
	/**
	 * isale查询工单状态
	 * @param tradeOrdNum
	 * @return
	 */
	public static JSONObject queryTradeOrderStatus(String tradeOrdNum){
		PostMethod postMethod = new PostMethod(IsaleInterface.BASEURL+"/spservice/queryTradeOrderStatus.do");
		HttpClient httpClient = new HttpClient();
		String result = "";
		int status = 0;
		try {
			postMethod.addParameter("sid", SID);
			postMethod.addParameter("sidpwd", SIDPWD);
			postMethod.addParameter("tradeOrdNum", tradeOrdNum);

			httpClient.getHttpConnectionManager().getParams()
					.setConnectionTimeout(3000);
			httpClient.getParams().setContentCharset("UTF-8");
			status = httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
			logger.info("result:"+result);
			Map<String,String> map = ReaderXmlForSAX.parse(result);
			return JSONObject.fromObject(map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
				httpClient.getHttpConnectionManager().closeIdleConnections(0);
			}
		}
		return null;
	}
}
