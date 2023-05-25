package com.sinontech.tools.common;

import java.security.MessageDigest;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class MD5 {

	public static String md5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes("utf-8"));
			byte b[] = md.digest();

			int i;

			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			str = buf.toString();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return str;
	}
	public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                new Comparator<String>() {

                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }});
                    sortMap.putAll(map);

        return sortMap;
    }
	public static String md5Up(String str){
		return md5(str).toUpperCase();
	}
	public static void main(String[] args) {
//		System.out.println(md5("31119@qq.com"+"123456"));
		System.out.println(md5("1"));
	}
}
