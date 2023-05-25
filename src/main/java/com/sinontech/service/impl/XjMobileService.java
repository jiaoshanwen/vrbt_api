package com.sinontech.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;
import com.sinontech.service.ProductInfoService;
import com.sinontech.service.SmsSendService;
import com.sinontech.service.inter.OrderService;
import com.sinontech.tools.common.DateUtil;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.yd.XjYdInterface;

import net.sf.json.JSONObject;
/**
 * 新疆移动流量包产品
 * @author Administrator
 *
 */
@Service("xjMobileService")
public class XjMobileService implements OrderService {

	@Resource(name="productInfoService")
	private ProductInfoService productInfoService;
	
	@Override
	public JSONObject sendOrderSms(PageData orderInfo, PageData productInfo) throws Exception {
		JSONObject result = new JSONObject();
		String phone = orderInfo.getString("phone");
		String salesCode = productInfo.getString("SALES_CODE");
		result = XjYdInterface.sendMsg(phone, salesCode);
		return result;
	}

	@Override
	public JSONObject orderProduct(PageData orderInfo, PageData productInfo, PageData orderLog) throws Exception {
		//新疆移动特惠包
		JSONObject result = new JSONObject();
		String phone = orderInfo.getString("phone");
		String smsCode = orderInfo.getString("smsCode");
		String salesCode = productInfo.getString("SALES_CODE");
		JSONObject checkResult = XjYdInterface.checkVerifyCode(phone, smsCode);
		String ccode = checkResult.getString("code");
		if(ccode.equals("0")) {
			if(productInfo.containsKey("REQUEST_INFO")&&StringUtil.isNotEmpty(productInfo.getString("REQUEST_INFO"))) {
				boolean checkFlag = true;
				String requestInfo = productInfo.getString("REQUEST_INFO");
				JSONObject requestJson = JSONObject.fromObject(requestInfo);
				if(requestJson.containsKey("vipPhones")) {
					String vipPhones = requestJson.getString("vipPhones");
					if(vipPhones.indexOf(phone)>-1) {
						checkFlag = false;
					}
				}
				if(checkFlag) {
					if(requestJson.containsKey("days")) {
						String days = requestJson.getString("days");
						JSONObject checkDays = XjYdInterface.checkPersonOpenDate(phone, days);
						String dayCode = checkDays.getString("code");
						if(!dayCode.equals("0")) {
							result.put("code", "1103");
							result.put("msg", "入网天数校验不通过");
							PageData log = new PageData();
							log.put("C_ID", orderInfo.getString("cooperateId"));
							log.put("ORDER_METHOD", "com.sinontech.service.crbt.orderProduct");
							log.put("ORDER_INFO", JSONObject.fromObject(orderInfo).toString());
							log.put("ORDER_TIME", DateUtil.getTime());
							log.put("BACK_INFO", result.toString());
							productInfoService.addLog(log);
							return result;
						}
					}
					if(requestJson.containsKey("age")) {
						String age = requestJson.getString("age");
						JSONObject checkAge = XjYdInterface.checkCustAge(phone, age);
						String ageCode = checkAge.getString("code");
						if(!ageCode.equals("0")) {
							result.put("code", "1103");
							result.put("msg", "用户年龄校验不通过");
							PageData log = new PageData();
							log.put("C_ID", orderInfo.getString("cooperateId"));
							log.put("ORDER_METHOD", "com.sinontech.service.crbt.orderProduct");
							log.put("ORDER_INFO", JSONObject.fromObject(orderInfo).toString());
							log.put("ORDER_TIME", DateUtil.getTime());
							log.put("BACK_INFO", result.toString());
							productInfoService.addLog(log);
							return result;
						}
					}
					if(requestJson.containsKey("minAge")) {
						String age = requestJson.getString("minAge");
						JSONObject checkAge = XjYdInterface.checkCustAge(phone, age);
						String ageCode = checkAge.getString("code");
						if(ageCode.equals("0")) {
							result.put("code", "1103");
							result.put("msg", "用户年龄校验不通过");
							PageData log = new PageData();
							log.put("C_ID", orderInfo.getString("cooperateId"));
							log.put("ORDER_METHOD", "com.sinontech.service.crbt.orderProduct");
							log.put("ORDER_INFO", JSONObject.fromObject(orderInfo).toString());
							log.put("ORDER_TIME", DateUtil.getTime());
							log.put("BACK_INFO", result.toString());
							productInfoService.addLog(log);
							return result;
						}
					}
				}
			}
			
			JSONObject orderResult = XjYdInterface.offerOrder(phone, salesCode, "0", "D");
			String ocode = orderResult.getString("code");
			result.put("code", ocode);
			result.put("msg", orderResult.getString("msg"));
			if(ocode.equals("0")) {
				String orderNo = orderResult.getString("orderNo");
				orderLog.put("ORDER_NO", orderNo);
				result.put("order_no", orderNo);
				orderLog.put("RESULT_CODE", "0");// 成功
				orderLog.put("IS_ON", "1");// 成功
			}
		}else {
			result.put("code", ccode);
			result.put("msg", checkResult.getString("msg"));
		}
		orderLog.put("ORDER_TYPE", "18");//新疆移动特惠包
		return result;
	}

	@Override
	public JSONObject queryOrderStatus(PageData queryInfo) throws Exception {
		JSONObject result = new JSONObject();
		result.put("code", "300");
		result.put("msg", "此产品无此功能");
		return result;
	}

	@Override
	public JSONObject queryUserPackage(PageData queryInfo,PageData productInfo) throws Exception {
		JSONObject result = new JSONObject();
		result.put("code", "300");
		result.put("msg", "此产品无此功能");
		return result;
	}


}
