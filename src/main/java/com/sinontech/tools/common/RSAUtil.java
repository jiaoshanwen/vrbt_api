package com.sinontech.tools.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;



public class RSAUtil {
	public static String ALI_PRIVATEKEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMVsyfhkD/6y2ykjf10lna5VU4OwyEi/OGfhXHokWQapbk167+jgfWia+900B6mxsHIqJeYkhdo+ZPwK5Zmeqmy89PdqQr/qavYNgLIp5vzCHTqO2iSwcw8xG3R5UqHc+SVBqnX1js4zv9lBrp7AugH8SZLM83eUkcyZSBSXAEzvAgMBAAECgYEAuA/WOZOo6fB8rd0fiItieD+rDjNKd7B3+Tm+C05KBhD++pOsivSJV3ZvbWtt3YMBm166WNgiRzg21FmgeFnaTsVvib5OOhLvKomE6nchEsQ39wlXiDVOxyUAp6IFYEtcAWIOcHiTXT/K0Kz+4Voo+dkOLLZ+sMJOnIWdBqUhO2ECQQDoFddy9u4oevcbOzGGOODwnsSa2jQY4Ampd+XeiJFxKmDLOBXf/ODW7usBWMdfMEWoG1kcW5p43Nwsjl0Pqb5/AkEA2cSlCdPkwe+9A1fMoix5Ff1dMtcHSN11qGMYNPcHD+/hYEmqSQeW8paMWoX43IT2pXQzLRQRnETN80HTPT8ZkQJAJX3ZxyWixYFPx+NhFbi8hcJwKj/TqK0QgoLu7GsNa8WXy1xtFDkKrU39QsNvua3XoteAJreZlarAGqaI3A3kywJAWw/k55gox4RFqLk1Q/eO5sgdCd5rGGgrBC/z9mn6FQr9VrNXNtSqpWSY6l2tdCFRtlxB7i6/MnZ/HKLA/5J8UQJAbFgNq3C+hdRQ6+Ix7y2FTr2ZXB5bmhFRB0tr6vUs8mkz8TUkvj1auz5V3FieEHtfvg5UOhD+NRA18GEosFRsFg==";
//	public static String ALI_PUBLICKEY ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDB1BP+AIZOZHsOJ2K0S11jZnLcS6IUN870hl+ehsBclS6YUFNNUEFrbAlTFQ0SPZUhmJZkcWgw+Y7Aklxbtd8UVKRiH2mXD1I6s8HIeWmfgojWFEx10OGY0i0viBOG7ewHJNkFWLc2ofdG8GAyYbOheIpRKdxod3aYn6tvDxGWAQIDAQAB";
	public static String ALI_PUBLICKEY ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDFbMn4ZA/+stspI39dJZ2uVVODsMhIvzhn4Vx6JFkGqW5Neu/o4H1omvvdNAepsbByKiXmJIXaPmT8CuWZnqpsvPT3akK/6mr2DYCyKeb8wh06jtoksHMPMRt0eVKh3PklQap19Y7OM7/ZQa6ewLoB/EmSzPN3lJHMmUgUlwBM7wIDAQAB";
	/**
     * 获取RSA公私钥匙对
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair getKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }
 
    /**
     * 获取公钥(base64编码)
     * @param keyPair
     * @return
     */
    public static String getPublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return byte2Base64(bytes);
    }
 
    /**
     * 获取私钥(Base64编码)
     * @param keyPair
     * @return
     */
    public static String getPrivateKey(KeyPair keyPair){
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return byte2Base64(bytes);
    }
 
    /**
     * 将Base64编码后的公钥转换成PublicKey对象
     * @param pubStr
     * @return
     * @throws Exception
     */
    public static PublicKey string2PublicKey(String pubStr) throws Exception{
        byte[] keyBytes = base642Byte(pubStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }
 
    /**
     * 将Base64编码后的私钥转换成PrivateKey对象
     * @param priStr
     * @return
     * @throws Exception
     */
    public static PrivateKey string2PrivateKey(String priStr) throws Exception{
        byte[] keyBytes = base642Byte(priStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }
 
    /**
     * 公钥加密
     * @param content
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }
 
    /**
     * 私钥解密
     * @param content
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception{
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytes = cipher.doFinal(content);
        return bytes;
    }
 
    /**
     * 字节数组转Base64编码
     * @param bytes
     * @return
     */
    public static String byte2Base64(byte[] bytes){
        return Base64.getMimeEncoder().encodeToString(bytes);
    }
 
    /**
     * Base64编码转数组
     * @param base64Key
     * @return
     * @throws IOException
     */
    public static byte[] base642Byte(String base64Key) throws IOException {
        return Base64.getMimeDecoder().decode(base64Key);
    }
    /**
     * 解密
     * @param miwen
     * @param privateKeyStr
     * @return 
     * @author zhuwq
     * @time 2019年10月9日下午5:35:19
     */
    public static String decrypt(String miwen, String privateKeyStr) {
	    // 将Base64编码后的私钥转换成PrivateKey对象
	    // 加密后的内容Base64解码
	    // 用私钥解密
	    try {
	        PrivateKey privateKey = RSAUtil.string2PrivateKey(privateKeyStr);
	        byte[] base642Byte = RSAUtil.base642Byte(miwen);
	        byte[] privateDecrypt = RSAUtil.privateDecrypt(base642Byte, privateKey);
	        String msg = new String(privateDecrypt);
//	        System.out.println("解密后的明文: " + msg);
	        return msg;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
    /**
     * 加密
     * @param message
     * @param publicKeyStr
     * @return 
     * @author zhuwq
     * @time 2019年10月9日下午5:35:29
     */
    public static String encrypt(String message, String publicKeyStr) {
	    try {
	        // 将Base64编码后的公钥转换成PublicKey对象
	        PublicKey publicKey = RSAUtil.string2PublicKey(publicKeyStr);
	        // 用公钥加密
	        byte[] publicEncrypt = RSAUtil.publicEncrypt(message.getBytes(), publicKey);
	        // 加密后的内容Base64编码
	        String byte2Base64 = RSAUtil.byte2Base64(publicEncrypt);
//	        System.out.println(byte2Base64);
	        return byte2Base64;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
    
    /**
     * 解密
     * @param miwen
     * @param privateKeyStr
     * @return 
     * @author zhuwq
     * @time 2019年10月9日下午5:35:19
     */
    public static String decrypt(String miwen) {
	    // 将Base64编码后的私钥转换成PrivateKey对象
	    // 加密后的内容Base64解码
	    // 用私钥解密
	    try {
	        PrivateKey privateKey = RSAUtil.string2PrivateKey(ALI_PRIVATEKEY);
	        byte[] base642Byte = RSAUtil.base642Byte(miwen);
	        byte[] privateDecrypt = RSAUtil.privateDecrypt(base642Byte, privateKey);
	        String msg = new String(privateDecrypt);
//	        System.out.println("解密后的明文: " + msg);
	        return msg;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
    /**
     * 加密
     * @param message
     * @param publicKeyStr
     * @return 
     * @author zhuwq
     * @time 2019年10月9日下午5:35:29
     */
    public static String encrypt(String message) {
	    try {
	        // 将Base64编码后的公钥转换成PublicKey对象
	        PublicKey publicKey = RSAUtil.string2PublicKey(ALI_PUBLICKEY);
	        // 用公钥加密
	        byte[] publicEncrypt = RSAUtil.publicEncrypt(message.getBytes("UTF-8"), publicKey);
	        // 加密后的内容Base64编码
	        String byte2Base64 = RSAUtil.byte2Base64(publicEncrypt);
//	        System.out.println(byte2Base64);
	        return byte2Base64;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return null;
	}
    public static void main(String[] args) throws UnsupportedEncodingException {
//		String msg = RSAUtil.encrypt("测试加密",ALI_PUBLICKEY);
//		System.out.println(msg);
//		msg = "kOytjLswVHk9/IrqVRz67gJRn1wv+cvIaMw4jB/yUcqfXtrVdtUcyfbNEhlVGjdDPeizeIe2PdFRStfL9RYOgTNGHH/uQi5EGG2NgM6AeKSC02uHJu9XCzptLpjYyRfTfDew16O4QpWXpq0G2ZvyXBdIFB3WxpxIMgCjjKOAie4=";
		String result = "ioxs7OR/xvpx5wDH8GzUez7gxuZNeCqwLOl6UEkg/aPeUyrvGPsayxeHuq75jTbtUOIGDZ0AknlN94J2lDJ9nUASuZ2JTiDi/r1yrHkrfm5qOSvxO3L4cmXE+54xEgtoaiQGWcM0ZwIDcMg0A9ONtlOmhz+ZBU/Kx9VddpY/fT4=";
		String dresult = URLEncoder.encode(result,"utf-8");
		System.out.println(dresult);
		result = URLDecoder.decode(dresult,"utf-8");// GBK解码
		System.out.println(result);

		String encrypt = RSAUtil.decrypt(result,ALI_PRIVATEKEY);
		System.out.println(encrypt);
//		String encrypt = "HjUE/e3OYkDtDoV+8/HL18l6qg8HX8jEnVHmwRTVD2paBxk7vAHdSaq8VMh+/UzOGT4BwRA6TvGD4iqlyVe213Cm4vuoiMBpjl0stBWoDdXSpWmaX9dNVwuYV/fblE154A9zdFO7XKWg2r7fTsMIVBa9IThSgHKyuEsloZO2Gz8i04o1AtNIs0qXyi2EcXsGtu+3r8osH4AWT9R/nLbLjQ4PfcFmy8xgNEZm4ltwxrPv8Y8B1qN0NKI5ZwIuto53lT7EnfS5L3VX4/RUCvlrZyfpaTd0qpDXbxY57YxgNHMR62OlF/I6jmxMSRz47gVKz8wN4sO2lImzJ2jXoBpIuQ==";
//		try {
//			KeyPair decrypt = RSAUtil.getKeyPair();
//			System.out.println(getPublicKey(decrypt));
//			System.out.println(getPrivateKey(decrypt));
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
	
		
	}
}
