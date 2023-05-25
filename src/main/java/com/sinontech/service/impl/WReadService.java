package com.sinontech.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinontech.service.RedisService;
import com.sinontech.service.inter.OrderService;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.wrbt.WReadInterface;

import net.sf.json.JSONObject;

@Service("wReadService")
public class WReadService implements OrderService {

	
	@Autowired
	private RedisService redisService;
	
	@Override
	public JSONObject sendOrderSms(PageData orderInfo, PageData productInfo) throws Exception {
		JSONObject result = new JSONObject();
		result.put("code", "300");
		result.put("msg", "此产品无此功能");
		return result;
	}

	@Override
	public JSONObject orderProduct(PageData orderInfo, PageData productInfo, PageData orderLog) throws Exception {
		//订购沃阅读产品
		JSONObject result = new JSONObject();

		String backUrl = orderInfo.getString("returnUrl");
		String phone = orderInfo.getString("phone");
		String channlidkey = orderInfo.getString("wokey");
		String auto = redisService.get("wreaduthorization");
		if(auto==null) {
			String autoResult = WReadInterface.getAuthorization();
			JSONObject autoJson = JSONObject.fromObject(autoResult);
			String code = autoJson.getString("code");
			if(code.equals("0000")) {
				auto = autoJson.getString("key_type")+" "+autoJson.getString("key");
				redisService.put("wreaduthorization", auto, Long.valueOf(autoJson.getString("expires_in")));
			}
		}
		String orderResult = WReadInterface.getOrder(phone, productInfo.getString("SALES_CODE"), backUrl, WReadInterface.BACKURL,channlidkey, auto);
		JSONObject json = JSONObject.fromObject(orderResult);
		String code = json.getString("code");
		if(code.equals("0000")) {
			result.put("code", "0");
			JSONObject message = json.getJSONObject("message");
			String sign = message.getString("sign");
			String torder = message.getString("torder");
			//存入redis
			redisService.put(sign, torder, null);
			result.put("order_no", sign);
			result.put("fee_url", message.getString("requrl"));
			orderLog.put("ORDER_NO", sign);
		}else {
			result.put("code", json.getString("code"));
			result.put("msg", json.getString("message"));
		}
		orderLog.put("ORDER_TYPE", "12");//沃阅读产品
		return result;
	}

	@Override
	public JSONObject queryOrderStatus(PageData queryInfo) throws Exception {
		//沃音乐产品
		JSONObject result = new JSONObject();
		String orderNo = queryInfo.getString("queryInfo");
		String auto = redisService.get("wreaduthorization");
		if(auto==null) {
			String autoResult = WReadInterface.getAuthorization();
			JSONObject autoJson = JSONObject.fromObject(autoResult);
			String code = autoJson.getString("code");
			if(code.equals("0000")) {
				auto = autoJson.getString("key_type")+" "+autoJson.getString("key");
				redisService.put("wreaduthorization", auto, Long.valueOf(autoJson.getString("expires_in")));
			}
		}
		String sign = orderNo;
		String toOrder = redisService.get(sign);
		String queryResult = WReadInterface.queryOrder(toOrder, sign, auto);
		JSONObject json = JSONObject.fromObject(queryResult);
		String code = json.getString("code");
		if(code.equals("0000")) {
			JSONObject message = json.getJSONObject("message");
			String instatus = message.getString("instatus");
			if(instatus.equals("0")) {
				result.put("msg", "申请临时订单成功");
				result.put("code", "001");
			}else if(instatus.equals("1")) {
				result.put("msg", "打开网页");
				result.put("code", "001");
			}else if(instatus.equals("2")) {
				result.put("msg", "用户订购");
				result.put("code", "001");
			}else if(instatus.equals("3")) {
				result.put("code", "0");
				result.put("msg", "订购成功");
			}else if(instatus.equals("4")) {
				result.put("code", "002");
				result.put("msg", "订购失败");
			}else {
				result.put("code", "002");
				result.put("msg", "查询失败");
			}
		}else {
			result.put("code", code);
			result.put("msg", json.getString("message"));
		}
		return result;
	}

	@Override
	public JSONObject queryUserPackage(PageData queryInfo,PageData productInfo) throws Exception {
		//沃阅读产品
		JSONObject result = new JSONObject();
		String userPhone = queryInfo.getString("phone");
		String salesCode = productInfo.getString("SALES_CODE");

		String auto = redisService.get("wreaduthorization");
		if(auto==null) {
			String autoResult = WReadInterface.getAuthorization();
			JSONObject autoJson = JSONObject.fromObject(autoResult);
			String code = autoJson.getString("code");
			if(code.equals("0000")) {
				auto = autoJson.getString("key_type")+" "+autoJson.getString("key");
				redisService.put("wreaduthorization", auto, Long.valueOf(autoJson.getString("expires_in")));
			}
		}
		String queryResult = WReadInterface.getPkgOrderedStatus(userPhone, salesCode,auto);
		JSONObject json = JSONObject.fromObject(queryResult);
		String code = json.getString("code");
		if(code.equals("0000")) {
			List<JSONObject> packages = new ArrayList<JSONObject>();
			result.put("code", "0");
			result.put("msg", "成功");
			JSONObject message = json.getJSONObject("message");
			if(!message.isEmpty()) {
				String instatus = message.getString("status");
				if(instatus.equals("3")) {
					JSONObject returnPack = new JSONObject();
					returnPack.put("packageId", message.get("productpkgindex"));
					packages.add(returnPack);
				}
				result.put("packages", packages);
			}else {
				result.put("code", "0001");
				result.put("msg", "产品不存在");
			}
		}else {
			result.put("code", code);
			result.put("msg", json.getString("message"));
		}
		return result;
	}



}
