package com.sinontech.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sinontech.service.inter.OrderService;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.flyh.FhInterface;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 飞鹄心享包
 * @author Administrator
 *
 */
@Service("diyFhService")
public class DiyFhService implements OrderService {

	@Override
	public JSONObject sendOrderSms(PageData orderInfo, PageData productInfo) throws Exception {
		JSONObject result = new JSONObject();
		result.put("code", "300");
		result.put("msg", "此产品无此功能");
		return result;
	}

	@Override
	public JSONObject orderProduct(PageData orderInfo, PageData productInfo, PageData orderLog) throws Exception {
		//diy飞鹄
		JSONObject result = new JSONObject();
		String backUrl = orderInfo.getString("returnUrl");
		String phone = orderInfo.getString("phone");
		String resultStr = FhInterface.doSubscribeAfterDIY(phone,backUrl);
		JSONObject resultJson = JSONObject.fromObject(resultStr);
		String code = resultJson.getString("code");
		JSONObject json = JSONObject.fromObject(resultJson.get("msg"));
		String orderCode = json.getString("res_code");
		result.put("code", orderCode);
		result.put("msg", json.getString("res_message"));
		if(code.equals("0")) {
			if(orderCode.equals("0")) {
				result.put("order_no", json.getString("order_no"));
				result.put("fee_url", json.getString("fee_url"));
				orderLog.put("ORDER_NO", json.getString("order_no"));
			}else if(orderCode.equals("0000")) {
				result.put("code", "0002");
				result.put("msg", "用户已经订购此产品");
			}
		}
		orderLog.put("ORDER_TYPE", "13");//电信diy产品
		orderLog.put("BACK_URL", backUrl);//保存回调地址
		return result;
	}

	@Override
	public JSONObject queryOrderStatus(PageData queryInfo) throws Exception {
		//diy飞鹄
		String orderNo = queryInfo.getString("orderNo");
		String userPhone = queryInfo.getString("phone");
		JSONObject result = new JSONObject();
		String qresult = FhInterface.orderQuery(userPhone, orderNo);
		JSONObject resultJson = JSONObject.fromObject(qresult);
		String code = resultJson.getString("code");
		JSONObject json = JSONObject.fromObject(resultJson.get("msg"));
		String orderCode = json.getString("code");
		result.put("msg", json.getString("message"));
		if (code.equals("0")&&orderCode.equals("0000")) {
			result.put("code", "0");
			result.put("order_no", json.getString("order_no"));
			result.put("verify_type", json.getString("verify_type"));
			result.put("order_status", json.getString("order_status"));
			result.put("is_openaccount", json.getString("is_openaccount"));
			result.put("price", json.getString("price"));
			result.put("product_id", json.getString("product_id"));
			result.put("mobile", json.getString("mobile"));
			result.put("return_url", json.getString("return_url"));
			result.put("order_time", json.getString("order_time"));
			result.put("product_name", json.getString("product_name"));
		}else {
			result.put("code", orderCode);
		}
		return result;
	}

	@Override
	public JSONObject queryUserPackage(PageData queryInfo,PageData productInfo) throws Exception {
		//diy飞鹄
		JSONObject result = new JSONObject();
		String userPhone = queryInfo.getString("phone");
		String salesCode = productInfo.getString("SALES_CODE");
		String diyResult = FhInterface.diyQuery(userPhone);
		JSONObject resultJson = JSONObject.fromObject(diyResult);
		String code = resultJson.getString("code");
		JSONObject diyJson = JSONObject.fromObject(resultJson.get("msg"));
		String orderCode = diyJson.getString("code");
		result.put("msg", diyJson.getString("message"));
		if(code.equals("0")&&orderCode.equals("0000")) {
			orderCode = "0";
		}
		result.put("code", orderCode);
		result.put("msg", diyJson.get("message"));
		if(diyJson.containsKey("data")) {
			List<JSONObject> packages = new ArrayList<JSONObject>();
			JSONArray list = diyJson.getJSONArray("data");
			if(list!=null&&list.size()>0) {
				for(int i=0;i<list.size();i++) {
					JSONObject pack = list.getJSONObject(i);
					JSONObject returnPack = new JSONObject();
					String packageId = pack.getString("package_id");
					if(packageId.equals(salesCode)) {
						returnPack.put("packageId", pack.get("package_id"));
						returnPack.put("orderTime", pack.get("order_time"));
						returnPack.put("unsubscribeTime", pack.get("unsubscribe_time"));
						returnPack.put("status", pack.get("status"));
						packages.add(returnPack);
					}
				}
			}
			result.put("packages", packages);
		}
		return result;
	}
}
