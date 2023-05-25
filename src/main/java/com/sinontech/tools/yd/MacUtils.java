package com.sinontech.tools.yd;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import net.sf.json.JSONObject;


/**
 * @Type MacUtils.java
 * @Desc mac签名示例
 * @author
 * @date
 * @version
 */
public class MacUtils {
	
	private static Logger logger = Logger.getLogger(MacUtils.class);
	private static final String HMAC_ALGORITHM = "HmacSHA256";

	/**
	 * 数据加密
	 * @param appSecretKey App应用秘钥
	 * @param data 要加密的数据
	 * @return
	 */
	public static String hmacsha256(String appSecretKey, String data) {
		Mac mac = null;
		byte[] doFinal = null;
		try {
			mac = Mac.getInstance(HMAC_ALGORITHM);
			//先对排序后的字符串进行MD5
			byte[] dataBytes = DigestUtils.md5(data);
			//对appSecretKey进行MD5,得到密钥
			SecretKey secretkey = new SecretKeySpec(DigestUtils.md5(appSecretKey), HMAC_ALGORITHM);
			mac.init(secretkey);
			//HmacSHA256加密
			doFinal = mac.doFinal(dataBytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e) {

		}
		String checksum = Hex.encodeHexString(doFinal).toLowerCase();
		return checksum;
	}

	/**
	 * 组装排序后的报文
	 * @param jo1 body报文
	 * @param jo2 header报文
	 * @return
	 */
	public static String toJson(JSONObject jo1, JSONObject jo2) {
		JSONObject jo = new JSONObject();
		jo.put("body", jo1);
		jo.put("header", jo2);
		return jo.toString();
	}

	/**
	 * json对象排序
	 * @param json
	 * @return
	 */
	public static JSONObject sort(JSONObject json) {
		Set<String> k = json.keySet();
		JSONObject result = new JSONObject();
		TreeMap<String, Object> hm = new TreeMap<String, Object>();
		for (String str : k) {
			hm.put(str, json.get(str));
		}
		Set<String> kMap = hm.keySet();
		for (String s : kMap) {
			result.put(s, json.get(s));
		}
		return result;
	}
}