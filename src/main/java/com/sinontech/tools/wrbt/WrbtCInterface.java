package com.sinontech.tools.wrbt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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

import com.sinontech.tools.common.DateUtil;
import com.sinontech.tools.common.MD5;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class WrbtCInterface {
	public static String APPKEY = "3000011668";
	public static String APPSCREAT = "9474D90B53878140";
	public static String CHANNEL_ID = "3000011668";
	public static final String BASEURL = "http://api.10155.com";
	public static final String RESOURCEBASE = "https://listen.10155.com/listener";
	public static void main(String[] args) throws IOException {
		
//		File file = new File("E:\\phone.txt");
//		FileInputStream fis = null;
//		InputStreamReader isr = null;
//		BufferedReader br = null;
//		String text = null;
//		fis = new FileInputStream(file);
//		isr = new InputStreamReader(fis);
//		br = new BufferedReader(isr);
//		while ((text = br.readLine()) != null) {
//			String str = text.trim();
//			
//			String orderTime = "";
//			String jsonStr = getOrderList(str,"1");
//			JSONObject json = JSONObject.fromObject(jsonStr);
//			String returnCode = json.getString("returnCode");
//			if(returnCode.equals("0000")) {
//				JSONArray rings = json.getJSONArray("userDepotList");
//				for(int i=0;i<rings.size();i++) {
//					JSONObject ring = rings.getJSONObject(i);
//					String ringName = ring.getString("ringName");
//					if(ringName.indexOf("花小楼")>-1) {
//						orderTime = ring.getString("orderTime");
//					}
//				}
//			}
//			System.out.println(str+","+orderTime);
//		}
//		System.out.println(onePointProductMon("13282135056","4900491500","1","http://ayy.sinontech.com"));
//		System.out.println(sendVerifyCode("13067750310","10022","4900491500"));
//		System.out.println(qrySubedProductsNoToken("16739302201"));
		System.out.println(queryUserInfo("13067750310"));
//		System.out.println(getOrderList("15522357711","1"));
//		System.out.println(orderRingOnePointMon("13282135056","9096200000000155"));
//		System.out.println(delOrder("13017486490","90962000202112090882880"));
//		System.out.println(downloadBill(DateUtil.getDays(),"4900491500"));
//		System.out.println(sendVerifyCode("13067750310","10022","4900491500"));
//		List<String> list = new ArrayList<String>();
//		list.add("15608797238");
//		list.add("18589675911");
//		list.add("13268400108");
//		list.add("17590180674");
//		list.add("17586410958");
//		list.add("18599690617");
//		list.add("17520618727");
//		list.add("15597050112");
//		list.add("18579024521");
//		list.add("17588881934");
//		list.add("13192392619");
//		list.add("13208572937");
//		list.add("16620990470");
//		list.add("18682441303");
//		list.add("13229633992");
//		list.add("15611773917");
//		list.add("18690256196");
//		list.add("18570682984");
//		list.add("13085910013");
//		list.add("15607687756");
//		list.add("17528150075");
//		list.add("18655238235");
//		list.add("13157699158");
//		list.add("13022501599");
//		list.add("13079553281");
//		for(String phone:list){
//			System.out.println(unSubProductNoToken(phone,"4900491500"));
//		}
//		System.out.println(unSubProductNoToken("13282135056","4900491500"));
//		System.out.println(sendLoginCode("13067750310","欢迎使用沃音乐免流业务"));
//		System.out.println(codeLogin("13067750310","823843"));
//		String nowDate = DateUtil.getChar14();
//		String startDate = DateUtil.getBeforeDayDate("15","yyyyMMddHHmmss");
//		String startDate = "20220212000000";
//		String nowDate = "20220224000000";
//		System.out.println(queryRing("1","300",startDate,nowDate));
		
//		List<String> times = getDaysList("20210901","20220330");
//		for(String time:times) {
//			System.out.println(time);
//			String startDate = time+"000000";
//			String nowDate = time+"235959";
//			System.out.println(queryRing("1","300",startDate,nowDate));
//		}
//		String base = "https://listen.10155.com/listener";
//		String path = "/womusic-bucket/90115000/mv_vod/volte_mp4/20220318/1504711387464298498.mp4";
//		String contentid = "90962000202110214208290";
//		Long timestamp = System.currentTimeMillis();
//		String idm = CHANNEL_ID+"&SHITING&"+timestamp+"&"+APPSCREAT+"&"+contentid;
//		String digest = MD5.md5(idm).toUpperCase();
//		System.out.println(digest);
//		String url = base+path+"?channelid="+CHANNEL_ID+"&contentid="+contentid+"&id="+digest+"&timestamp="+timestamp;
//		System.out.println(url);
//		Map<String, Object> variableMap = new HashMap<String, Object>();
//		variableMap.put("effectiveTime", DateUtil.getDay());
//		variableMap.put("productName", "妙趣彩铃6元包月");
//		variableMap.put("productPrice", "6元/月");
//		variableMap.put("telPhone", "4006611850");
//		System.out.println(sendMsg("18658115567","211577",variableMap));
	}
	
	

public static List<String> getDaysList(String startDate, String endDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date dBegin = null;
        Date dEnd = null;
            try {
				dBegin = sdf.parse(startDate);
				dEnd = sdf.parse(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
          
        List<String> daysStrList = new ArrayList<String>();
        daysStrList.add(sdf.format(dBegin));
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(dEnd);
        while (dEnd.after(calBegin.getTime())) {
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            String dayStr = sdf.format(calBegin.getTime());
            daysStrList.add(dayStr);
        }
        return daysStrList;
}
	/**
	 * 发送短信（联通）
	 * @param cellNumber
	 * @param templateId 短信模板标识
	 * @param variableMap 短信内容变量，由调用方在模板申请时填写
	 * @return
	 */
	public static String sendMsg(String cellNumber,String templateId,Map<String, Object> variableMap) {
		try {
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("cellNumber",cellNumber);
			map.put("templateId",templateId);
			map.put("variableMap",variableMap);
			String timestamp = DateUtil.getChar14();
			String MD5sign="appkey"+APPKEY+"timestamp"+timestamp+APPSCREAT;
//			System.out.println("md5前："+MD5sign);
			String digest = MD5.md5(URLEncoder.encode(MD5sign, "utf-8")).toUpperCase();
//			System.out.println("md5后："+digest);
			String args = "appkey="+APPKEY+"&timestamp="+timestamp+"&digest="+digest;
			String query = JSONObject.fromObject(map).toString();
			JSONObject json = HttpUtils.postMethod(WrbtCInterface.BASEURL+"/abl/sms/sendMsg?"+args, query);
			return json.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static String queryRing(String pageStart,String pageEnd,String startDate,String endTime) {
		try {
			Map<String, String> map=new HashMap<String, String>();
			map.put("channelId",CHANNEL_ID);
			map.put("types","contentSpxl");
			map.put("pageStart",pageStart);
			map.put("pageEnd",pageEnd);
			map.put("startTime",startDate);
			map.put("endTime",endTime);
			String timestamp = DateUtil.getChar14();
			String MD5sign="appkey"+APPKEY+"timestamp"+timestamp+APPSCREAT;
//			System.out.println("md5前："+MD5sign);
			String digest = MD5.md5(URLEncoder.encode(MD5sign, "utf-8")).toUpperCase();
//			System.out.println("md5后："+digest);
			String args = "appkey="+APPKEY+"&timestamp="+timestamp+"&digest="+digest;
			String query = JSONObject.fromObject(map).toString();
			JSONObject json = HttpUtils.postMethod(WrbtCInterface.BASEURL+"/qk/sync/channelsync/query?"+args, query);
			return json.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 发送短信
	 * @param callNumber
	 * @param content
	 * @return
	 */
	public static String sendLoginCode(String callNumber,String content) {
		try {
			String timestamp = DateUtil.getChar14();
			String MD5sign="appkey"+APPKEY+"callNumber"+callNumber+"content"+URLEncoder.encode(content, "utf-8")+"timestamp"+timestamp+APPSCREAT;
//			System.out.println("md5前："+MD5sign);
			String digest = MD5.md5(MD5sign).toUpperCase();
//			System.out.println("md5后："+digest);
			String args = "appkey="+APPKEY+"&callNumber="+callNumber+"&content="+URLEncoder.encode(content, "utf-8")+"&timestamp="+timestamp+"&digest="+digest;
			JSONObject json = HttpUtils.getMethod(WrbtCInterface.BASEURL+"/v1/verifyCode/sendLoginCode?"+args);
			return json.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 校验登录验证码
	 * @param callNumber
	 * @param verifyCode
	 * @return
	 */
	public static String codeLogin(String callNumber,String verifyCode) {
		String timestamp = DateUtil.getChar14();
		String MD5sign="appkey"+APPKEY+"callNumber"+callNumber+"timestamp"+timestamp+"verifyCode"+verifyCode+APPSCREAT;
//		System.out.println("md5前："+MD5sign);
		String digest = MD5.md5(MD5sign).toUpperCase();
//		System.out.println("md5后："+digest);
		String args = "appkey="+APPKEY+"&callNumber="+callNumber+"&verifyCode="+verifyCode+"&timestamp="+timestamp+"&digest="+digest;
		JSONObject json = HttpUtils.getMethod(WrbtCInterface.BASEURL+"/v1/verifyCode/codeLogin?"+args);
		return json.toString();
	}
	/**
	 * 用户信息查询
	 * @param phone
	 * @return
	 */
	public static String queryUserInfo(String phone) {
		try {
			Map<String, String> map=new HashMap<String, String>();
			map.put("phoneNumber",phone);
			String timestamp = DateUtil.getChar14();
			String MD5sign="appkey"+APPKEY+"timestamp"+timestamp+APPSCREAT;
//			System.out.println("md5前："+MD5sign);
			String digest = MD5.md5(URLEncoder.encode(MD5sign, "utf-8")).toUpperCase();
//			System.out.println("md5后："+digest);
			String args = "appkey="+APPKEY+"&timestamp="+timestamp+"&digest="+digest;
			String query = JSONObject.fromObject(map).toString();
			JSONObject json = HttpUtils.postMethod(WrbtCInterface.BASEURL+"/fusionManagement/queryUserInfo?"+args, query);
			return json.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 视频彩铃包月业务订购接口：彩铃收费产品订购（包月）
	 * @param phone 用户电话号码
	 * @param productId 产品ID
	 * @param orderType 订购类型1：包月产品订购
	 * @param redirectUrl H5认证方式，接入二次确认成功之后前端页面跳转链接，
	 * @return
	 */
	public static String onePointProductMon(String phone,String productId,String orderType,String redirectUrl) {
		try {
			Map<String, String> map=new HashMap<String, String>();
			map.put("phoneNumber",phone);
			map.put("productId",productId);
			map.put("orderType",orderType);
			map.put("orderMethod","01");
			map.put("channelId",CHANNEL_ID);
			map.put("redirectUrl",redirectUrl);
			String timestamp = DateUtil.getChar14();
			String MD5sign="appkey"+APPKEY+"timestamp"+timestamp+APPSCREAT;
//			System.out.println("md5前："+MD5sign);
			String digest = MD5.md5(URLEncoder.encode(MD5sign, "utf-8")).toUpperCase();
//			System.out.println("md5后："+digest);
			String args = "appkey="+APPKEY+"&timestamp="+timestamp+"&digest="+digest;
			String query = JSONObject.fromObject(map).toString();
			JSONObject json = HttpUtils.postMethod(WrbtCInterface.BASEURL+"/fusionManagement/onePointProductMon?"+args, query);
			return json.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 退订产品(免用户授权)
	 * @param callNumber
	 * @return
	 */
	public static String unSubProductNoToken(String callNumber,String productId) {
		try {
			String timestamp = DateUtil.getChar14();
			String MD5sign="appkey"+APPKEY+"callNumber"+callNumber+"productId"+productId+"timestamp"+timestamp+APPSCREAT;
//			System.out.println("md5前："+MD5sign);
			String digest = MD5.md5(URLEncoder.encode(MD5sign, "utf-8")).toUpperCase();
//			System.out.println("md5后："+digest);
			String args = "appkey="+APPKEY+"&callNumber="+callNumber+"&productId="+productId+"&timestamp="+timestamp+"&digest="+digest;
			JSONObject json = HttpUtils.getMethod(WrbtCInterface.BASEURL+"/v1/product/unSubProductNoToken?"+args);
			return json.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 查询已订购产品(免用户授权)
	 * @param phone
	 * @return
	 */
	public static String qrySubedProductsNoToken(String callNumber) {
		try {
			String timestamp = DateUtil.getChar14();
			String MD5sign="appkey"+APPKEY+"callNumber"+callNumber+"timestamp"+timestamp+APPSCREAT;
//			System.out.println("md5前："+MD5sign);
			String digest = MD5.md5(URLEncoder.encode(MD5sign, "utf-8")).toUpperCase();
//			System.out.println("md5后："+digest);
			String args = "appkey="+APPKEY+"&callNumber="+callNumber+"&timestamp="+timestamp+"&digest="+digest;
			JSONObject json = HttpUtils.getMethod(WrbtCInterface.BASEURL+"/v1/product/qrySubedProductsNoToken?"+args);
			return json.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 *	 铃音删除：用于用户彩铃中心管理，供用户删除使用
	 * @param phone
	 * @param ringId
	 * @return
	 */
	public static String delOrder(String phone,String ringId) {
		try {
			Map<String, String> map=new HashMap<String, String>();
			map.put("phoneNumber",phone);
			map.put("ringId",ringId);
			map.put("channelId",CHANNEL_ID);
			String timestamp = DateUtil.getChar14();
			String MD5sign="appkey"+APPKEY+"timestamp"+timestamp+APPSCREAT;
//			System.out.println("md5前："+MD5sign);
			String digest = MD5.md5(URLEncoder.encode(MD5sign, "utf-8")).toUpperCase();
//			System.out.println("md5后："+digest);
			String args = "appkey="+APPKEY+"&timestamp="+timestamp+"&digest="+digest;
			String query = JSONObject.fromObject(map).toString();
			JSONObject json = HttpUtils.postMethod(WrbtCInterface.BASEURL+"/fusionManagement/delOrder?"+args, query);
			return json.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 获取用户订购铃音列表
	 * @param phone
	 * @param isVideo 0：音频  1：视频
	 * @return
	 */
	public static String getOrderList(String phone,String isVideo) {
		try {
			Map<String, String> map=new HashMap<String, String>();
			map.put("phoneNumber",phone);
			map.put("isVideo",isVideo);
			String timestamp = DateUtil.getChar14();
			String MD5sign="appkey"+APPKEY+"timestamp"+timestamp+APPSCREAT;
//			System.out.println("md5前："+MD5sign);
			String digest = MD5.md5(URLEncoder.encode(MD5sign, "utf-8")).toUpperCase();
//			System.out.println("md5后："+digest);
			String args = "appkey="+APPKEY+"&timestamp="+timestamp+"&digest="+digest;
			String query = JSONObject.fromObject(map).toString();
			JSONObject json = HttpUtils.postMethod(WrbtCInterface.BASEURL+"/fusionManagement/getOrderList?"+args, query);
			return json.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 彩铃一键设置（包月用户）
	 * @param phone
	 * @param ringId 铃音ID/铃音组ID
	 * @return
	 */
	public static String orderRingOnePointMon(String phone,String ringId) {
		try {
			Map<String, String> map=new HashMap<String, String>();
			map.put("phoneNumber",phone);
			map.put("ringId",ringId);
			String timestamp = DateUtil.getChar14();
			String MD5sign="appkey"+APPKEY+"timestamp"+timestamp+APPSCREAT;
//			System.out.println("md5前："+MD5sign);
			String digest = MD5.md5(URLEncoder.encode(MD5sign, "utf-8")).toUpperCase();
//			System.out.println("md5后："+digest);
			String args = "appkey="+APPKEY+"&timestamp="+timestamp+"&digest="+digest;
			String query = JSONObject.fromObject(map).toString();
			JSONObject json = HttpUtils.postMethod(WrbtCInterface.BASEURL+"/fusionManagement/settingRingOnePointMon?"+args, query);
			return json.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 订购关系数据核对：订购关系增量下载
	 * @param date 日期(yyyyMMdd),要查询的日期
	 * @param productId
	 * @return
	 */
	public static String downloadBill(String date,String productId) {
		try {
			Map<String, String> map=new HashMap<String, String>();
			map.put("date",date);
			map.put("productId",productId);
			map.put("channelId",CHANNEL_ID);
			String timestamp = DateUtil.getChar14();
			String MD5sign="appkey"+APPKEY+"timestamp"+timestamp+APPSCREAT;
//			System.out.println("md5前："+MD5sign);
			String digest = MD5.md5(URLEncoder.encode(MD5sign, "utf-8")).toUpperCase();
//			System.out.println("md5后："+digest);
			String args = "appkey="+APPKEY+"&timestamp="+timestamp+"&digest="+digest;
			String query = JSONObject.fromObject(map).toString();
			JSONObject json = HttpUtils.postMethod(WrbtCInterface.BASEURL+"/v1/ifdp/services/downloadBill?"+args, query);
			return json.toString();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 退订产品(需短信验证码)
	 * @param phone
	 * @param verifyCode
	 * @param productId
	 * @return
	 */
	public static String unSubProductWithVCode(String phone,String verifyCode,String productId) {
		try {
			String timestamp = DateUtil.getChar14();
			String MD5sign="appkey"+APPKEY+"callNumber"+phone+"productId"+productId+"timestamp"+timestamp+"verifyCode"+verifyCode+APPSCREAT;
//			System.out.println("md5前："+MD5sign);
			String digest = MD5.md5(URLEncoder.encode(MD5sign, "utf-8")).toUpperCase();
//			System.out.println("md5后："+digest);
			String args = "appkey="+APPKEY+"&callNumber="+phone+"&productId="+productId+"&timestamp="+timestamp+"&verifyCode="+verifyCode+"&digest="+digest;
			JSONObject json = HttpUtils.getMethod(WrbtCInterface.BASEURL+"/v1/product/unSubProductWithVCode?"+args);
			return json.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 发送短信验证码（退订产品需用户验证时用到）
	 * @param phone
	 * @param verifyType 验证类型, 表示此验证码对应接口 
	 *  10001 开通炫铃
		10002 注销炫铃
		10010 订购炫铃
		10021 订购产品
		10022 退订产品
		10023 购买产品(单次计费)
		10031 查询下载链接
	 * @param verifyParam 验证参数, 根据验证类型不同而定
		 * 	verifyType=10010时必须填炫铃Id
			verifyType=10021或10022时填49开头的产品Id
			verifyType=10031时填歌曲Id
	 * @return
	 */
	public static String sendVerifyCode(String phone,String verifyType,String verifyParam) {
		try {
			String timestamp = DateUtil.getChar14();
			String MD5sign="appkey"+APPKEY+"callNumber"+phone+"timestamp"+timestamp+"verifyParam"+verifyParam+"verifyType"+verifyType+APPSCREAT;
//			System.out.println("md5前："+MD5sign);
			String digest = MD5.md5(URLEncoder.encode(MD5sign, "utf-8")).toUpperCase();
//			System.out.println("md5后："+digest);
			String args = "appkey="+APPKEY+"&callNumber="+phone+"&timestamp="+timestamp+"&verifyType="+verifyType+"&verifyParam="+verifyParam+"&digest="+digest;
			JSONObject json = HttpUtils.getMethod(WrbtCInterface.BASEURL+"/v1/verifyCode/sendVerifyCode?"+args);
			return json.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
}
