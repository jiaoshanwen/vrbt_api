package com.sinontech.tools.imusic;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import sun.misc.BASE64Encoder;

public class AuthUtils {

	private static final String HMAC_SHA1 = "HmacSHA1";
	private static final Logger logger = Logger.getLogger(AuthUtils.class);

	public static String generateMacSignature(String secret, String data) {
		logger.debug("generateDate is:" + data);
		logger.debug("secret is:" + secret);
		byte[] byteHMAC = null;

		try {
			Mac mac = Mac.getInstance(HMAC_SHA1);
			SecretKey secretKey = new SecretKeySpec(secret.getBytes("utf-8"), HMAC_SHA1);

			mac.init(secretKey);
			byteHMAC = mac.doFinal(data.getBytes("utf-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String result = new BASE64Encoder().encode(byteHMAC);
		logger.debug("generateMacSignature is:" + result);
		return result;
	}

	public static String generateMac256Signature(String secret, String data) {
		byte[] byteHMAC = null;
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			SecretKey secretKey = new SecretKeySpec(secret.getBytes("utf-8"), "HmacSHA256");
			mac.init(secretKey);
			byteHMAC = mac.doFinal(data.getBytes("utf-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String result = new BASE64Encoder().encode(byteHMAC);
		return result;
	}
	public static void main(String[] s) {
		String timestamp = "20221025173219";
		String mobile = "13362273197";
		String productid = "135999999999999000207";
		String state = "2";
		String str = timestamp+mobile+productid+state;
		String keyword = "p#K!HjW7deOoTyk%v2Dahw6DQcHUSyFx";
		System.out.println("加密前："+str);
		String auth_signature = AuthUtils.generateMac256Signature(keyword,str);
		System.out.println("加密后："+auth_signature);
		//b+rWZ8lpp2ncXqJqPM/q9OIIuzUJ3zEW0SlVqVXM2JQ=
	}
}
