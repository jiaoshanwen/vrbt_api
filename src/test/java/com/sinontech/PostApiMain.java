package com.sinontech;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

import com.sinontech.tools.common.RSAUtil;
import com.sinontech.tools.common.Security;

import net.sf.json.JSONObject;



public class PostApiMain {
	

	private static final String LOGIN_NAME="sinon_xmt";
	private static final String LOGIN_PWD="xxl_ZJ519";
//	private static final String POSTPORT="http://ayy.sinontech.com";
	private static final String POSTPORT="http://192.168.1.183:8081";
//	private static final String privatekey="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1M6ConLzEcPe1aI5H9kaET54u\r\nl3gEGIaGjCqVV4QDShYYk/w4uUUiTo+LNNRJt3g4w6bOcenZentyxxnj+OLZiMI3\r\na6ClUUimlqwT4vqzPxmBm+jp0lq2LDpUTuvzV+QgFTktXgyA/l9TiQZiU8kvNBEU\r\nyQwNfRSaW0ZN5UukrwIDAQAB";
	private static final String privatekey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzXjRsMfI+KKc55XsdzUfBWnc6CAut/p34nDoR2dz9iJX9g0BrfoifjCAymx2G1qrCyJEe6Zx4Hr4JRx2DrIlnM7vkQbQ52QWhXg9IUsTxeGmb1yBLJXm88l11EAj844T35gijNNxGLzU5ZN5ayVV7k+wnuWNUeIIprGQYToMQqVCcBPm/2SEYNDviECh3zPkJPdSh7faisri6OQisFCp1/y+ZO+dl1pWeaxP/1XmSRpY6CXLmphTcBz7zP7acnrawqn+nQgKOKzNILwyEd6NDmMPkxgcoKIWIgnh4mIKlrgpksFRwb+XWJpKzmu1dXZhegUjwF/VsAoW/158PKwhOwIDAQAB";
	
	private static final String AGENT_ID = "1";
	

	public static void main(String[] s) {
//		Thread b= new Thread(new Runnable() {
//            @Override
//            public void run() {
//            	System.out.println(testHttp());	
//            }
//        }, "线程1");
//        b.start();
//        Thread c= new Thread(new Runnable() {
//            @Override
//            public void run() {
//            	System.out.println(testHttp());	
//            }
//        }, "线程2");
//        c.start();
		testHttp();
	}
	private static String testHttp() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("phone","15384083230");
		jsonObject.put("productCode","1a07d764ed9b4f3782ffc5707b4b9471");
//		jsonObject.put("smsCode","368772");
		jsonObject.put("orderNo","66e033c197c24496aa525636c8718e85");
//		jsonObject.put("transId","202207251007958603");
//		jsonObject.put("oid","1658718221522266813");
		//4900491500
//		jsonObject.put("productId","135999999999999000130");
//		jsonObject.put("ringId","90962000202110214208390");
//		jsonObject.put("returnUrl","https://xwcard.sinontech.com/ring-api/userring/duo/dxhnback");
//		jsonObject.put("pageUrl","http://h5.sinontech.com:8085/act/act/indexH5Call");
		
//		jsonObject.put("mobile","15268108147");
//		jsonObject.put("boxId","698039035100000058");
//		jsonObject.put("contentId","699429T3545");
//		jsonObject.put("price","0");
//		jsonObject.put("openStatus","000000");
//		jsonObject.put("openMessage","订购成功");
//		jsonObject.put("openTime","2022-07-14 00:00:00");
//		jsonObject.put("setStatus","000000");
//		jsonObject.put("setMessage","设置成功");
//		
		JSONObject json=new JSONObject();
		json.put("data",  Security.encrypt("123456789",jsonObject.toString()));
		long currentTimeMillis = System.currentTimeMillis();
		String sign=LOGIN_PWD+"|"+currentTimeMillis;
		json.put("sign",RSAUtil.encrypt(sign,privatekey));
		json.put("timestamp",currentTimeMillis);
		json.put("loginname", LOGIN_NAME);
		System.out.println(json.toString());
		String result = postwring(POSTPORT,json.toString(), "/api/crbt/queryOrderStatus");
		return result;
	}
	
	protected static String postwring(String QWREQUEST_IP, String param,
			String function) {
		String url = QWREQUEST_IP + function;
		// System.err.println(url);
		PostMethod postMethod = new PostMethod(url);
		String result = null;
		int status = 0;
		try {
			System.out.println("请求参数"+param.toString());
			postMethod.addRequestHeader("Content-Type", "application/json");
			RequestEntity requestEntity = new ByteArrayRequestEntity(param.getBytes(), "utf-8");
			postMethod.setRequestEntity(requestEntity);
			HttpClient httpClient = new HttpClient();
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
			httpClient.getParams().setContentCharset("UTF-8");
			
			status = httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
			result = result.trim();
			System.out.println(function + "," + status + "," + result);
			System.out.println("结束后：：："+result);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return result;
	}

}
