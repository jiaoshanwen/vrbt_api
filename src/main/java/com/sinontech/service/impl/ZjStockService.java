package com.sinontech.service.impl;

import org.springframework.stereotype.Service;

import com.sinontech.service.inter.OrderService;
import com.sinontech.tools.b2l.B2lInterface;
import com.sinontech.tools.common.DateUtil;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.common.UuidUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 浙江存量业务受理
 * @author Administrator
 *
 */
@Service("zjStockService")
public class ZjStockService implements OrderService {

	@Override
	public JSONObject sendOrderSms(PageData orderInfo, PageData productInfo) throws Exception {
		//电信存量业务
		String phone = orderInfo.getString("phone");
		JSONObject result = new JSONObject();
		JSONObject checkJson = this.checkYX(orderInfo,productInfo);
		String checkCode = checkJson.getString("code");
		if(checkCode.equals("0")) {
			try {
				String ruleId = checkJson.getString("ruleId");
				String requestInfo = productInfo.getString("REQUEST_INFO");
				JSONObject requestJson = JSONObject.fromObject(requestInfo);
				JSONObject rulesList = requestJson.getJSONObject("productInfo");
				JSONObject product = rulesList.getJSONObject(ruleId);
				String productName = product.getString("productName");
				String salesCode = product.getString("productCode");
				String resultStr = B2lInterface.verifycodeSend(phone, productName, salesCode);
				JSONObject ydResult = JSONObject.fromObject(resultStr);
				String code = ydResult.getString("code");
				String msg = ydResult.getString("message");
				if(code.equals("0000")) {
					result.put("code", "0");
					result.put("msg", "成功");
				}else {
					result.put("code", code);
					result.put("msg", msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("code", "0002");
				result.put("msg", "无法受理");
			}
		}else {
			result.put("code", "0002");
			result.put("msg", "无法受理");
		}
		return result;
	}

	@Override
	public JSONObject orderProduct(PageData orderInfo, PageData productInfo, PageData orderLog) throws Exception {
		//电信存量业务（区分号码类型）
		JSONObject result = new JSONObject();
		String phone = orderInfo.getString("phone");
		String phoneCity = orderInfo.getString("phoneCity");
		JSONObject checkJson = this.checkYX(orderInfo,productInfo);
		String checkCode = checkJson.getString("code");
		if(checkCode.equals("0")) {
			try {
				String ruleId = checkJson.getString("ruleId");
				String requestInfo = productInfo.getString("REQUEST_INFO");
				JSONObject requestJson = JSONObject.fromObject(requestInfo);
				
				JSONObject rulesList = requestJson.getJSONObject("productInfo");
				JSONObject product = rulesList.getJSONObject(ruleId);
				String productName = product.getString("productName");
				String salesCode = product.getString("productCode");
				String discountCode = product.getString("discountCode");

				String verifyCode = orderInfo.getString("smsCode");
				String salesPhone = requestJson.getString("SALES_PHONE");
				String salesName = requestJson.getString("SALES_NAME");
				String orderNo = UuidUtil.get32UUID();
				String sequenceNo = UuidUtil.get32UUID();
				//保存到数据库
				orderInfo.put("orderNo", orderNo);
				orderInfo.put("sequenceNo", sequenceNo);
				String orderResult = B2lInterface.zhifuAndWxReseve(phone, phoneCity, productName, salesCode, discountCode, orderNo, sequenceNo, verifyCode, salesPhone, salesName);
				JSONObject json = JSONObject.fromObject(orderResult);
				String code = json.getString("code");
				result.put("msg", json.getString("message"));
				if(code.equals("0000")) {
					result.put("code", "0");
					String orderId = json.getJSONObject("data").getString("yxOrderNo");
					result.put("order_no", orderId);
					orderLog.put("ORDER_NO", orderId);
				}else {
					result.put("code", code);
				}
				orderLog.put("ORDER_TYPE", "14");
			} catch (Exception e) {
				e.printStackTrace();
				result.put("code", "0002");
				result.put("msg", "无法受理");
			}
		}else {
			result.put("code", "0002");
			result.put("msg", "无法受理");
		}
		return result;
	}

	@Override
	public JSONObject queryOrderStatus(PageData queryInfo) throws Exception {
		JSONObject result = new JSONObject();
		String orderNo = queryInfo.getString("orderNo");
		String orderStr = B2lInterface.yxOrderDetail(orderNo);
		JSONObject json = JSONObject .fromObject(orderStr);
		String code = json.getString("code");
		if(code.equals("0000")) {
			result.put("code", "0");
			result.put("msg", json.getString("message"));
			result.put("status", json.getJSONObject("data").getJSONObject("baseinfo").getString("status"));
		}else {
			result.put("code", code);
			result.put("msg", json.getString("message"));
		}
		return result;
	}

	@Override
	public JSONObject queryUserPackage(PageData queryInfo,PageData productInfo) throws Exception {
		JSONObject result = new JSONObject();
		result.put("code", "300");
		result.put("msg", "此产品无此功能");
		return result;
	}

	/**
	 * 检测存量产品是否可以订购
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public JSONObject checkYX(PageData data,PageData productInfo) throws Exception {
		JSONObject result = new JSONObject();
		String phone = data.getString("phone");
		if(productInfo!=null) {
			result.put("code", "0002");
			result.put("msg", "无法受理");
			String evtCollectTime = DateUtil.getTime();
			String requestInfo = productInfo.getString("REQUEST_INFO");
			JSONObject requestJson = JSONObject.fromObject(requestInfo);
			String salerNo = requestJson.getString("salerNo");
			String ruleIds = requestJson.getString("ruleId");
			String activityCode = requestJson.getString("ACTIVITY_CODE");
			String resultStr = B2lInterface.syncCollectContactInfoToAgent(phone, salerNo, evtCollectTime,activityCode);
			JSONObject resultJson = JSONObject.fromObject(resultStr);
			String code = resultJson.getString("code");
			if(code.equals("0000")) {
				JSONObject orderdata = resultJson.getJSONObject("data");
				String orderCode = orderdata.getString("code");
				JSONArray orderlist = orderdata.getJSONArray("orderList");
				if(orderCode.equals("1")&&orderlist.size()>0) {
					boolean ifcan = false;
					for(int i=0;i<orderlist.size();i++) {
						JSONObject order = orderlist.getJSONObject(i);
						String ruleId = order.getString("ruleId");
						if(ruleIds.contains(ruleId)) {
							ifcan = true;
							result.put("ruleId", ruleId);
						}
					}
					if(ifcan) {
						result.put("code", "0");
						result.put("msg", "可以受理");
					}
				}
			}
		}else {
			result.put("code", "0001");
			result.put("msg", "产品不存在");
		}
		return result;
	}

}
