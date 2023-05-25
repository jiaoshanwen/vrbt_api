package com.sinontech.tools.yd;

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
import org.apache.log4j.Logger;

import com.sinontech.tools.common.DateUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 新疆移动产品受理
 * @author Administrator
 *
 */
public class XjYdInterface {
	private static final Logger logger = Logger.getLogger(XjYdInterface.class);

//	生产环境地址：http://36.189.241.29:81/oppf
//	测试环境地址：http://36.189.241.29:8081/oppf
	public static String APPID = "542979";
	public static String APPKEY = "a66992540731a4d7c10458606bc1fb5c";
	//省别编码 固定XINJ
	public static String PROVINCECODE = "XINJ";

	//员工所在地州编码，对于省集中的省份，填写：INTF
	public static String TRADEEPARCHYCODE = "INTF";
	//员工所在业务区编码。填写：INTF
	public static String TRADECITYCODE = "INTF";
	// 员工所在的部门(渠道标识)TRADEDEPARTID
	public static String TRADEDEPARTID = "9F2E6";
	//渠道工号
	public static String TRADESTAFFID = "IT000059";
	
	public static String CHANNELTYPEID = "l";

	public static String TRADEDEPARTNAME = "1";

	public static String TRADESTAFFNAME = "ITF";

	public static String ROUTEEPARCHYCODE = "0871";

	public static String TRADEDEPARTPASSWD = "1111";
	
	public static final String BASEURL = "http://36.189.241.29:81/oppf";
	public static void main(String[] args) {
//		System.out.println(sendMsg("15299499980","999001610233898"));
//		System.out.println(queryDetailUnionInfo("15299499980","999001610233898"));
		System.out.println(unionCaseAcceptAcceptOrder("15299499980","999001610233898","0","W"));
		//{"transId":"202207151027982303","resCode":"000000","resMsg":"success"}

//		System.out.println(checkVerifyCode("15299499980","795687"));
//		System.out.println(loadCheckChange("15299499980"));
		
//		System.out.println(offerOrder("15299499980","20215209","0","D"));

	}
	
	

	/**
	 * 发送校验短信
	 * @param mobile
	 * @param offerId
	 * @return
	 */
	public static JSONObject sendMsg(String mobile,String offerId) {
		JSONObject returnJson = new JSONObject();
		JSONObject query = new JSONObject();
		query.put("ACCESS_NUM", mobile);
		if(!offerId.startsWith("999")) {
			offerId = "1300"+offerId;
		}
		query.put("OFFER_ID", offerId);
		String result = submit("XINJ_UNHT_checkVerifyCodeNew",query.toString());
		JSONObject resultJson = JSONObject.fromObject(result);
		String respCode = resultJson.getString("respCode");
		String respDesc = resultJson.getString("respDesc");
		if(respCode.equals("0")) {
			JSONObject resultData = resultJson.getJSONObject("result");
			String resultCode = resultData.getString("X_RESULTCODE");
			String resultInfo = resultData.getString("X_RESULTINFO");
			if(resultCode.equals("0")) {
				returnJson.put("code", "0");
				returnJson.put("msg", "成功");
			}else {
				returnJson.put("code", resultCode);
				returnJson.put("msg", resultInfo);
			}
		}else {
			returnJson.put("code", respCode);
			returnJson.put("msg", respDesc);
		}
		return returnJson;
	}
	/**
	 * 用户短信验证码效验接口
	 * @param mobile
	 * @param smsCode
	 * @return
	 */
	public static JSONObject checkVerifyCode(String mobile,String smsCode) {
		JSONObject returnJson = new JSONObject();
		JSONObject query = new JSONObject();
		query.put("ACCESS_NUM", mobile);
		query.put("VERIFY_CODE", smsCode);
		String result = submit("XINJ_UNHT_checkVerifyCode",query.toString());
		JSONObject resultJson = JSONObject.fromObject(result);
		String respCode = resultJson.getString("respCode");
		String respDesc = resultJson.getString("respDesc");
		if(respCode.equals("0")) {
			JSONObject resultData = resultJson.getJSONObject("result");
			String resultCode = resultData.getString("X_RESULTCODE");
			String resultInfo = resultData.getString("X_RESULTINFO");
			if(resultCode.equals("0")) {
				JSONObject data = resultData.getJSONObject("OUTDATA").getJSONObject("DATAS");
				String checkCode = data.getString("RESULT_CODE");
				String checkInfo = data.getString("RESULT_INFO");
				if(checkCode.equals("0")) {
					returnJson.put("code", "0");
					returnJson.put("msg", "成功");
				}else {
					returnJson.put("code",checkCode);
					returnJson.put("msg", checkInfo);
				}
			}else {
				returnJson.put("code", resultCode);
				returnJson.put("msg", resultInfo);
			}
		}else {
			returnJson.put("code", respCode);
			returnJson.put("msg", respDesc);
		}
		return returnJson;
	}
	/**
	 * 用户入网天数校验接口
	 * @param mobile
	 * @param days
	 * @return
	 */
	public static JSONObject checkPersonOpenDate(String mobile,String days) {
		JSONObject returnJson = new JSONObject();
		JSONObject query = new JSONObject();
		query.put("ACCESS_NUM", mobile);
		query.put("OPEN_DAYS", days);
		String result = submit("XINJ_UNHQ_CheckPersonOpenDate",query.toString());
		JSONObject resultJson = JSONObject.fromObject(result);
		String respCode = resultJson.getString("respCode");
		String respDesc = resultJson.getString("respDesc");
		if(respCode.equals("0")) {
			JSONObject resultData = resultJson.getJSONObject("result");
			String resultCode = resultData.getString("X_RESULTCODE");
			String resultInfo = resultData.getString("X_RESULTINFO");
			if(resultCode.equals("0")) {
				JSONObject data = resultData.getJSONObject("OUTDATA");
				String checkResult = data.getString("CHECK_RESULT");
				if(checkResult.equals("TRUE")) {
					returnJson.put("code", "0");
					returnJson.put("msg", "校验通过");
				}else {
					returnJson.put("code", "-1");
					returnJson.put("msg", "入网天数校验不通过");
				}
			}else {
				returnJson.put("code", resultCode);
				returnJson.put("msg", resultInfo);
			}
		}else {
			returnJson.put("code", respCode);
			returnJson.put("msg", respDesc);
		}
		return returnJson;
	}
	/**
	 * 校验用户年龄
	 * @param mobile
	 * @param age(此值大于等于客户年龄时返回 1，此值小于客户年龄时返回 0)
	 * @return
	 */
	public static JSONObject checkCustAge(String mobile,String age) {
		JSONObject returnJson = new JSONObject();
		JSONObject query = new JSONObject();
		query.put("ACCESS_NUM", mobile);
		query.put("CHECK_AGE_VALUE", age);
		String result = submit("XINJ_UNHT_checkCustAge",query.toString());
		JSONObject resultJson = JSONObject.fromObject(result);
		String respCode = resultJson.getString("respCode");
		String respDesc = resultJson.getString("respDesc");
		if(respCode.equals("0")) {
			JSONObject resultData = resultJson.getJSONObject("result");
			String resultCode = resultData.getString("X_RESULTCODE");
			String resultInfo = resultData.getString("X_RESULTINFO");
			if(resultCode.equals("0")) {
				JSONObject data = resultData.getJSONObject("OUTDATA");
				String checkResult = data.getString("CHECK_RESULT");
				if(checkResult.equals("1")) {
					returnJson.put("code", "0");
					returnJson.put("msg", "校验通过");
				}else {
					returnJson.put("code", "-1");
					returnJson.put("msg", "用户年龄校验不通过");
				}
			}else {
				returnJson.put("code", resultCode);
				returnJson.put("msg", resultInfo);
			}
		}else {
			returnJson.put("code", respCode);
			returnJson.put("msg", respDesc);
		}
		return returnJson;
	}
	/**
	 * 订购前进行用户优惠办理效验 
	 * @param mobile
	 * 校验类型，0 业务受理前 1 业务受理后 为空则不做校验 初始化时填 0
	 * @return
	 */
	public static String loadCheckChange(String mobile) {
		JSONObject query = new JSONObject();
		query.put("ACCESS_NUM", mobile);
		query.put("ACCEPT_TAG", "0");
		String resutl = submit("XINJ_UNHQ_loadCheckChange",query.toString());
		return resutl;
	}
	/**
	 *  用户优惠办理接口 
	 * @param mobile
	 * @param productId
	 * @param action 操作状态: 0：新增，1：删除，2：修改
	 * @param offerType 订单类型 P:产品,D:优惠,S:服务
	 * @return
	 */
	public static JSONObject offerOrder(String mobile,String productId,String action,String offerType) {
		JSONObject returnJson = new JSONObject();
		JSONObject query = new JSONObject();
		query.put("ACCESS_NUM", mobile);
		JSONArray offerList = new JSONArray();
		JSONObject offer = new JSONObject();
		offer.put("OFFER_CODE", productId);
		offer.put("ACTION", action);
		offer.put("OFFER_TYPE", offerType);
		offerList.add(offer);
		query.put("OFFER_LIST", offerList);
		String result = submit("XINJ_UNHT_offerOrder",query.toString());
		JSONObject resultJson = JSONObject.fromObject(result);
		String respCode = resultJson.getString("respCode");
		String respDesc = resultJson.getString("respDesc");
		if(respCode.equals("0")) {
			JSONObject resultData = resultJson.getJSONObject("result");
			String resultCode = resultData.getString("X_RESULTCODE");
			String resultInfo = resultData.getString("X_RESULTINFO");
			if(resultCode.equals("0")) {
				JSONObject data = resultData.getJSONObject("OUTDATA");
				String orderId = data.getString("ORDER_ID");
				returnJson.put("code", "0");
				returnJson.put("msg", "成功");
				returnJson.put("orderNo", orderId);
			}else {
				returnJson.put("code", resultCode);
				returnJson.put("msg", resultInfo);
			}
		}else {
			returnJson.put("code", respCode);
			returnJson.put("msg", respDesc);
		}
		return returnJson;
	}
	/**
	 * 一事一案商品详情查询
	 * @param mobile
	 * @param offerId
	 * @return
	 */
	public static JSONObject queryDetailUnionInfo(String mobile,String offerId) {
		JSONObject returnJson = new JSONObject();
		JSONObject query = new JSONObject();
		query.put("ACCESS_NUM", mobile);
		query.put("OFFER_ID", offerId);
		String result = submit("XINJ_UNHQ_queryDetailUnionInfo",query.toString());
		JSONObject resultJson = JSONObject.fromObject(result);
		String respCode = resultJson.getString("respCode");
		String respDesc = resultJson.getString("respDesc");
		if(respCode.equals("0")) {
			JSONObject resultData = resultJson.getJSONObject("result");
			String resultCode = resultData.getString("X_RESULTCODE");
			String resultInfo = resultData.getString("X_RESULTINFO");
			if(resultCode.equals("0")) {
				JSONObject data = resultData.getJSONObject("OUTDATA").getJSONObject("SALE_OFFER_DATA");
				returnJson.put("code", "0");
				returnJson.put("msg", "成功");
				returnJson.put("data", data);
			}else {
				returnJson.put("code", resultCode);
				returnJson.put("msg", resultInfo);
			}
		}else {
			returnJson.put("code", respCode);
			returnJson.put("msg", respDesc);
		}
		return returnJson;
	}
	/**
	 * 一事一案业务受理接口
	 * @param mobile
	 * @param unionType 一事一案类型，0：纯赠送类
	 * @param offerId 一事一案营销 OFFER_ID
	 * @param channelTag 外围渠道标识，电渠为 W 渠道 APP 为 w
	 * @return
	 */
	public static JSONObject unionCaseAcceptAcceptOrder(String mobile,String offerId,String unionType,String channelTag) {
		JSONObject queryJson = queryDetailUnionInfo(mobile,offerId);
		String queryCode = queryJson.getString("code");
		if(!queryCode.equals("0")) {
			return queryJson;
		}
		JSONObject queryData = queryJson.getJSONObject("data");
		
		String offerName = queryData.getString("OFFER_NAME");
		String offerCode = queryData.getString("OFFER_CODE");
		String relId = "";
		if(queryData.containsKey("OFFER_GIFT_LIST")) {
			JSONArray giftList = queryData.getJSONArray("OFFER_GIFT_LIST");
			if(giftList!=null&&giftList.size()>0) {
				JSONObject rels = giftList.getJSONObject(0);
				relId = rels.getString("REL_ID");
			}
		}
		JSONObject returnJson = new JSONObject();
		JSONObject query = new JSONObject();
		query.put("ACCESS_NUM", mobile);
		query.put("UNION_TYPE", unionType);
		query.put("UNION_OFFER_ID", offerId);
		query.put("UNION_OFFER_NAME", offerName);
		query.put("SALE_OFFER_CODE", offerCode);
		query.put("CHANNEL_TAG", channelTag);
		query.put("REL_ID", relId);
		String result = submit("XINJ_UNHQ_unionCaseAcceptAcceptOrder",query.toString());
		JSONObject resultJson = JSONObject.fromObject(result);
		String respCode = resultJson.getString("respCode");
		String respDesc = resultJson.getString("respDesc");
		if(respCode.equals("0")) {
			JSONObject resultData = resultJson.getJSONObject("result");
			String resultCode = resultData.getString("X_RESULTCODE");
			String resultInfo = resultData.getString("X_RESULTINFO");
			if(resultCode.equals("0")) {
				JSONObject data = resultData.getJSONObject("OUTDATA");
				String orderId = data.getString("ORDER_ID");
				returnJson.put("code", "0");
				returnJson.put("msg", "成功");
				returnJson.put("orderNo", orderId);
			}else {
				returnJson.put("code", resultCode);
				returnJson.put("msg", resultInfo);
			}
		}else {
			returnJson.put("code", respCode);
			returnJson.put("msg", respDesc);
		}
		return returnJson;
	}
	private static String submit(String method,String jsonStr) {
		String flowId = DateUtil.sdfdetail();
		String args = "method="+method+"&appId="+APPID+"&format=json&appKey="+APPKEY+"&status=1&flowId="+flowId+"&provinceCode="+PROVINCECODE+"&"
				+ "channelTypeId="+CHANNELTYPEID+"&tradeEparchyCode="+TRADEEPARCHYCODE+"&tradeCityCode="+TRADECITYCODE+"&tradeDepartId="+TRADEDEPARTID+"&"
				+ "tradeStaffId="+TRADESTAFFID+"&tradeDepartName="+TRADEDEPARTNAME+"&tradeStaffName="+TRADESTAFFNAME+"&routeEparchyCode="+ROUTEEPARCHYCODE+"&"
				+ "tradeDepartPassWd="+TRADEDEPARTPASSWD;
		logger.info("新疆请求参数："+args);
		JSONObject json = HttpUtils.postMethod(BASEURL+"?"+args,jsonStr);
		System.out.println(json.toString());
		return json.toString();
	}
}
