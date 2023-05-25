package com.sinontech.tools.common;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;





import net.sf.json.JSONObject;

public class Security {
	/**
	 * 
	 * 加密
	 * 
	 * @param src
	 *            数据源
	 * 
	 * @param key
	 *            密钥，长度必须是8的倍数
	 * 
	 * @return 返回加密后的数据
	 * 
	 * @throws Exception
	 * 
	 */
	public static final String PASSWORD_CRYPT_KEY = "123456789";// 密钥
	public final static String DES = "DES";

	public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成加密操作
		Cipher cipher = Cipher.getInstance(DES);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
		// 现在，获取数据并加密
		// 正式执行加密操作
		return cipher.doFinal(src);
	}

	/**
	 * 
	 * 解密
	 * 
	 * @param src
	 *            数据源
	 * 
	 * @param key
	 *            密钥，长度必须是8的倍数
	 * 
	 * @return 返回解密后的原始数据
	 * 
	 * @throws Exception
	 * 
	 */

	public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
		// DES算法要求有一个可信任的随机数源
		SecureRandom sr = new SecureRandom();
		// 从原始密匙数据创建一个DESKeySpec对象
		DESKeySpec dks = new DESKeySpec(key);
		// 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
		// 一个SecretKey对象
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
		SecretKey securekey = keyFactory.generateSecret(dks);
		// Cipher对象实际完成解密操作
		Cipher cipher = Cipher.getInstance(DES);
		// 用密匙初始化Cipher对象
		cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
		// 现在，获取数据并解密
		// 正式执行解密操作
		return cipher.doFinal(src);
	}

	/**
	 * 
	 * 密码解密
	 * 
	 * @param data
	 * 
	 * @return
	 * 
	 * @throws Exception
	 * 
	 */

	public final static String decrypt(String data) {
		try {
			return new String(decrypt(hex2byte(data.getBytes()),
					PASSWORD_CRYPT_KEY.getBytes()),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * explain:自定义appkey解密
	 * author:zhuwq
	 * date:2017-12-1下午2:22:23
	 */
	public final static String decrypt(String data,String appkey) {
		try {
			return new String(decrypt(hex2byte(data.getBytes()),
					appkey.getBytes()),"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * 
	 * 密码加密
	 * 
	 * @param password
	 * 
	 * @return
	 * 
	 * @throws Exception
	 * 
	 */

	public final static String encrypt(String password) {
		try {
			return byte2hex(encrypt(password.getBytes("utf-8"), PASSWORD_CRYPT_KEY
					.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public final static String encrypt(String key,String password) {
		try {
			return byte2hex(encrypt(password.getBytes("utf-8"), key.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 
	 * 二行制转字符串
	 * 
	 * @param b
	 * 
	 * @return
	 * 
	 */

	private static String byte2hex(byte[] b) {
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs.toUpperCase();
	}

	private static byte[] hex2byte(byte[] b) {
		if ((b.length % 2) != 0)
			throw new IllegalArgumentException("长度不是偶数");
		byte[] b2 = new byte[b.length / 2];
		for (int n = 0; n < b.length; n += 2) {
			String item = new String(b, n, 2);
			b2[n / 2] = (byte) Integer.parseInt(item, 16);
		}

		return b2;
	}
	public static void main(String[] s) {
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("login_name", "sinonwh");
//		jsonObject.put("login_pwd", "sinonwh!@#");
		// 查询集团
//		 jsonObject.put("CIRCLE_ID","61692243bf464b42bed5088fcd5b0484");
		 //封装的post请求，
//		 String result = post("http://47.92.107.181:8080/interfaces/",jsonObject.toString(),
//				 "three/circles/findByAll");
//		 String result = post("http://127.0.0.1:8080/xwh/interfaces/",
//		 jsonObject.toString(), "three/circles/findByAll");
//		 three_circles_findByAll,three_members_listAllBy,three_rings_listRing,three_members_deleteMember,three_members_sendSmsNoticeByPhone,three_members_refreshMemberState,three_rings_ringSet,three_rings_listSuccessRing,three_rings_ringdel,three_members_getMemberSumBy,three_circles_saveOrRing,three_members_addmember,three_rings_ringSaveSingle
		 //解密
//		 System.err.println("fsdf=="+decode(result));
//		 System.out.println("fsdf=="+decode(result));
		// 查询成员
		// jsonObject.put("CIRCLE_ID","您的集团ID");
		// //封装的post请求，
		// String result = post("您的分配请求地址",
		// jsonObject.toString(), "three/members/listAllBy");
		// //解密
		// System.err.println(decode(result));
//		 查询铃音
//		 jsonObject.put("CIRCLE_ID","61692243bf464b42bed5088fcd5b0484");
//		 //封装的post请求，
//		 String result = post("http://47.92.107.181:8080/interfaces/",
//		 jsonObject.toString(), "three/rings/listRing");
//		 //解密
//		 System.err.println(result);
		// 删除成员
		// jsonObject.put("CIRCLE_ID","您的集团ID");
		// jsonObject.put("PHONE", "13429191960");
		// //封装的post请求，
		// String result = post("您的分配请求地址",
		// jsonObject.toString(), "three/members/deleteMember");
		// //解密
		// System.err.println(decode(result));
		// 下发短信接口
		// jsonObject.put("PHONE", "13429191960");
		// //封装的post请求，
		// String result = post("您的分配请求地址",
		// jsonObject.toString(), "three/members/sendSmsNoticeByPhone");
		// //解密
		// System.err.println(decode(result));
		// 成员刷新接口
		// jsonObject.put("CIRCLEMEMBER_ID",
		// "af3940d961364e0782c858388c7e4b80");
		// //封装的post请求，
		// String result = post("您的分配请求地址",
		// jsonObject.toString(), "three/members/refreshMemberState");
		// //解密
		// System.err.println(decode(result));
		// 铃音设置接口
		// jsonObject.put("CIRCLEMEMBER_ID",
		// "af3940d961364e0782c858388c7e4b80");
		// jsonObject.put("CIRCLE_ID", "您的集团ID");
		// jsonObject.put("CIRCLERING_ID", "9c687c9b1a8a44aebffccee4d087b331");
		// //封装的post请求，
		// String result = post("您的分配请求地址",
		// jsonObject.toString(), "three/rings/ringSet");
		// //解密
		// System.err.println(decode(result));
		// 查询集团下已经审核通过铃音接口
		// jsonObject.put("PHONE_TYPE", "2");
		// jsonObject.put("CIRCLE_ID", "您的集团ID");
		// //封装的post请求，
		// String result = post("您的分配请求地址",
		// jsonObject.toString(), "three/rings/listSuccessRing");
		// //解密
		// System.err.println(decode(result));
		// 铃音删除接口
		// jsonObject.put("PHONE_TYPE", "2");
		// jsonObject.put("CIRCLE_ID", "您的集团ID");
		// jsonObject.put("CIRCLERING_ID", "9c687c9b1a8a44aebffccee4d087b331");
		// //封装的post请求，
		// String result = post("您的分配请求地址",
		// jsonObject.toString(), "three/rings/ringdel");
		// //解密
		// System.err.println(decode(result));
		// 成员开通数统计接口
		// Map<String, String> map=new HashMap<String, String>();
		// map.put("CIRCLE_ID", "您的集团ID");
		// map.put("PHONE_TYPE", "2");
		// jsonObject.put("pd", map);
		// //封装的post请求，
		// String result = post("您的分配请求地址",
		// jsonObject.toString(), "three/members/getMemberSumBy/");
		// //解密
		// System.err.println(decode(result));

		// 集团创建
//		 Map<String, String> map=new HashMap<String, String>();
//		 map.put("CIRCLE_NAME", "刘测试1122");
//		 map.put("ADMIN_NAME", "张三");
//		 map.put("ADMIN_PHONE", "13758133121");
//		 map.put("CMCC_STATUS", "0");
//		 // map.put("QUALIFICATIONURL",
////		 "http://47.92.147.88:8080/upload/uploadFiles/file/2017-12-05/986ba4e3cd354b709c8b3ec188d21ede.png");
//		 // map.put("KNOWPAGEURI",
////		 "http://47.92.147.88:8080/upload/uploadFiles/file/2017-12-05/986ba4e3cd354b709c8b3ec188d21ede.png");
//		 map.put("MEMBER_PHONE", "13312345678\r\n13312345677\r\n3312345676\r\n13758133121");
//		 jsonObject.put("producttype", "ring");
//		 jsonObject.put("circlename","刘测试1122");
//		 jsonObject.put("circleman","张三");
//		 jsonObject.put("circletel","13758133121");
//		 jsonObject.put("pd", map);
		 //封装的post请求，
		 
//		String result = post("http://192.168.23.3:8080/xwh/interfaces/",jsonObject.toString(), "three/circles/saveOrRing/");

//		 String result = post("http://47.92.107.181:8080/interfaces/",jsonObject.toString(), "three/circles/saveOrRing/");
		// //解密
		// //{"code":"0","msg":"订购成功","CIRCLE_ID":"6ff741a51d23453aa999425dab25ca48","CIRCLE_NAME":"we","ADMIN_PHONE":"15856719133","pdmember":[{"parea":"安徽 宿州市","phone":"15856719133","des":"正常","code":"0","ptype":"2"},{"parea":"安徽 宿州市","phone":"15856719100","des":"正常","code":"0","ptype":"2"}],"ORDER_CIRCLE_ID":"22278"}
//		System.err.println(result);
//		// 添加员工接口
//		jsonObject.put("CIRCLE_ID", "4116262e5c94439fbc02838165a2fff5");
//		jsonObject.put("MEMBER_PHONE", "13357122007\r\n13357122000");
//		// 封装的post请求，
//		String result = post("http://127.0.0.1:8080/xwh/interfaces/",
//				jsonObject.toString(), "three/members/addmember");
//		// 解密
//		//200,{"code":"-1","codeMsg":"你的号码都存在问题","pd":[],"pdmember":[{"parea":"安徽 宿州市","phone":"15856719167","des":"15856719167号码已存在于本系统！","code":"-1","ptype":"2"},{"parea":"江苏 南京市","phone":"18551842550","des":"18551842550号码已存在于本系统！","code":"-1","ptype":"3"}]}
//		System.err.println(result);
		
		// 添加铃音接口
//		jsonObject.put("CIRCLE_ID", "4116262e5c94439fbc02838165a2fff5");
//		jsonObject.put("RINGNAME", "测试接口11");
//		jsonObject.put("RING_CONTENT", "铃音内容接口11");
//		jsonObject.put("RINGURL", "http://47.92.147.88/uploadFiles/file/2017-12-13/3d80831667d043c88f55ae535aaafd4d.mp3");
//		jsonObject.put("PHONE_TYPES", "2");
////		// 封装的post请求，
//		String result = post("http://127.0.0.1:8080/xwh/interfaces/",
//				jsonObject.toString(), "three/rings/ringSaveSingle");
//		// 解密
//		System.err.println(result);
		String str= "8B7897901CCF031407AE528160584D06";
		System.out.println(Security.decrypt(str, Security.PASSWORD_CRYPT_KEY));
	}

	public static String decode(String result) {
		String encrypt = Security.decrypt(result, "*&()^&**");
		return encrypt;
	}

	protected static String post(String QWREQUEST_IP, String param,
			String function) {
		String url = QWREQUEST_IP + function;
		// System.err.println(url);
		PostMethod postMethod = new PostMethod(url);
		String result = null;
		int status = 0;
		try {
			String encrypt = Security.encrypt("*&()^&**", param);
			System.out.println("开始前：：："+encrypt);
			postMethod.addParameter("param", encrypt);
			postMethod.addParameter("loginname", "13456990694");
			HttpClient httpClient = new HttpClient();
			httpClient.getHttpConnectionManager().getParams()
					.setConnectionTimeout(10000);
			httpClient.getParams().setContentCharset("UTF-8");
			status = httpClient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
			result = result.trim();
			System.out.println(function + "," + status + "," + result);
			System.out.println("结束后：：："+result);
			result= Security.decrypt(result, "*&()^&**");
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
