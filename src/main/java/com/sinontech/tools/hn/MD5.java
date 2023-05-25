package com.sinontech.tools.hn;

import java.security.MessageDigest;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MD5 {

	public static String md5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
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
	
	public static String md5Up(String str){
		return md5(str).toUpperCase();
	}
	public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                new Comparator<String>() {

                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }});
                    sortMap.putAll(map);

        return sortMap;
    }
	public static void main(String[] args) {
		Map<String, String> map=new HashMap<String, String>();
		map.put("c", "3");
		map.put("a", "1");
		map.put("e", "5");
		map.put("b", "2");
		
		map.put("d", "4");
		
		map=MD5.sortMapByKey(map);
		for (Map.Entry<String, String> entry:map.entrySet()) {
			System.out.println(entry.getKey()+"---"+entry.getValue());
			
		}
//		System.out.println(md5("mj1"));
//		System.out.println(md5Up("mj1"));
	}
}
