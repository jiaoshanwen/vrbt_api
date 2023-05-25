package com.sinontech.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sinontech.service.ApplicationSalesService;
import com.sinontech.service.inter.OrderService;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.imusic.Configuration;
import com.sinontech.tools.imusic.VolteInterface;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 爱音乐自费产品（api方式）
 * @author Administrator
 *
 */
@Service("musicApiService")
public class MusicApiService implements OrderService {
	
	@Resource(name = "applicationSalesService")
	private ApplicationSalesService applicationSalesService;

	@Override
	public JSONObject sendOrderSms(PageData orderInfo, PageData productInfo) throws Exception {
		JSONObject result = new JSONObject();
		String phone = orderInfo.getString("phone");
		//爱音乐自计费产品
		String salesId = productInfo.getString("SALES_ID");
		PageData header = applicationSalesService.getSaleHeader(salesId);
		if(productInfo.getString("SALES_CODE").equals("135999999999999000271")) {
			header.put("channelId", Configuration.YHY_CHANNELID);
		}
		String resultStr = VolteInterface.ismpOrderLaunched(phone, productInfo.getString("SALES_CODE"), "http://ayy.sinontech.com/api/back/ismpSyn/"+header.getString("id"), header);
		JSONObject ydResult = JSONObject.fromObject(resultStr);
		String code = ydResult.getString("res_code");
		String msg = ydResult.getString("res_message");
		if(code.equals("0")) {
			result.put("code", "0");
			result.put("msg", "成功");
			result.put("order_no", ydResult.getString("orderNo"));
		}else {
			result.put("code", code);
			result.put("msg", msg);
		}
		return result;
	}

	@Override
	public JSONObject orderProduct(PageData orderInfo, PageData productInfo, PageData orderLog) throws Exception {
		//订购爱音乐自计费产品
		JSONObject result = new JSONObject();
		String salesId = productInfo.getString("SALES_ID");
		PageData header = applicationSalesService.getSaleHeader(salesId);
		if(productInfo.getString("SALES_CODE").equals("135999999999999000271")) {
			header.put("channelId", Configuration.YHY_CHANNELID);
		}
		String orderNo = orderInfo.getString("orderNo");
		String verifyCode = orderInfo.getString("smsCode");
		String backUrl = orderInfo.getString("returnUrl");
		String phone = orderInfo.getString("phone");
		
		String orderResult = VolteInterface.ismpConfirmOrder(phone, orderNo, verifyCode, header);
		JSONObject json = JSONObject.fromObject(orderResult);
		String code = json.getString("res_code");
		result.put("msg", json.getString("res_message"));
		if(code.equals("0")||code.equals("4")) {
			result.put("code", "0");
			orderLog.put("ORDER_NO", orderNo);
			orderLog.put("BACK_URL", backUrl);//保存回调地址
		}else {
			result.put("code", code);
		}
		orderLog.put("ORDER_TYPE", "6");//电信产品
		return result;
	}

	@Override
	public JSONObject queryOrderStatus(PageData queryInfo) throws Exception {
		//电信10元卓越包
		JSONObject result = new JSONObject();
		String orderNo = queryInfo.getString("orderNo");
		String userPhone = queryInfo.getString("phone");
		String salesId = queryInfo.getString("SALES_ID");
		PageData header = applicationSalesService.getSaleHeader(salesId);
		String query_h5_order = VolteInterface.query_h5_order(orderNo, userPhone,header);
		JSONObject json = JSONObject .fromObject(query_h5_order);
		String code = json.getString("code");
		if(code.equals("0000")) {
			result.put("code", "0");
			result.put("msg", json.getString("message"));
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
			result.put("code", code);
			result.put("msg", json.getString("message"));
		}
		return result;
	}

	@Override
	public JSONObject queryUserPackage(PageData queryInfo,PageData productInfo) throws Exception {
		//电信爱音乐产品
		JSONObject result = new JSONObject();
		String userPhone = queryInfo.getString("phone");
		String salesId = productInfo.getString("SALES_ID");
		String salesCode = productInfo.getString("SALES_CODE");
		PageData header = applicationSalesService.getSaleHeader(salesId);
		String cresult = VolteInterface.querypackagelist(userPhone, salesCode,header);
		JSONObject userPackageListResp = JSONObject.fromObject(cresult);
		JSONObject json = userPackageListResp.getJSONObject("UserPackageListResp");
		String code = json.getString("res_code");
		if(code.equals("0000")) {
			code = "0";
		}
		result.put("code", code);
		result.put("msg", json.get("res_message"));
		if(json.containsKey("user_package_list")) {
			List<JSONObject> packages = new ArrayList<JSONObject>();
			JSONArray list = new JSONArray();
			JSONObject packageList = json.getJSONObject("user_package_list");
			String up = packageList.getString("user_package");
			if(up.startsWith("[")) {
				list = packageList.getJSONArray("user_package");
			}else {
				JSONObject op = JSONObject.fromObject(up);
				list.add(op);
			}
			if(list!=null&&list.size()>0) {
				for(int i=0;i<list.size();i++) {
					JSONObject pack = list.getJSONObject(i);
					JSONObject returnPack = new JSONObject();
					returnPack.put("packageId", pack.get("package_id"));
					returnPack.put("downNum", pack.get("count_down_num"));
					returnPack.put("orderTime", pack.get("order_time"));
					returnPack.put("unsubscribeTime", pack.get("unsubscribe_time"));
					returnPack.put("status", pack.get("status"));
					packages.add(returnPack);
				}
			}
			result.put("packages", packages);
		}
		return result;
	}
	
}
