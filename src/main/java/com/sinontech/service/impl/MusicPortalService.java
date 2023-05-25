package com.sinontech.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;
import com.sinontech.service.ApplicationSalesService;
import com.sinontech.service.inter.OrderService;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.imusic.Configuration;
import com.sinontech.tools.imusic.VolteInterface;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 爱音乐poral方式受理
 * @author Administrator
 *
 */
@Service("musicPortalService")
public class MusicPortalService implements OrderService {

	@Resource(name = "applicationSalesService")
	private ApplicationSalesService applicationSalesService;
	
	@Override
	public JSONObject sendOrderSms(PageData orderInfo, PageData productInfo) throws Exception {
		JSONObject result = new JSONObject();
		result.put("code", "300");
		result.put("msg", "此产品无此功能");
		return result;
	}

	/**
	 * 请求参数需要增加省份和城市
	 */
	@Override
	public JSONObject orderProduct(PageData orderInfo, PageData productInfo, PageData orderLog) throws Exception {
		//订购电信产品
		JSONObject result = new JSONObject();
		String backUrl = orderInfo.getString("returnUrl");
		String ringId = orderInfo.getString("ringId");
		String phone = orderInfo.getString("phone");
		String phoneProvince = orderInfo.getString("phoneProvince");
		String phoneCity = orderInfo.getString("phoneCity");

		String salesId = productInfo.getString("SALES_ID");
		PageData header = applicationSalesService.getSaleHeader(salesId);
//		if(phoneProvince.contains("广东")&&phoneCity.contains("惠州")) {
//			header.put("channelId", Configuration.HZ_CHANNELID);
//		}
		if(productInfo.containsKey("REQUEST_INFO")&&StringUtil.isNotEmpty(productInfo.getString("REQUEST_INFO"))) {
			String requestInfo = productInfo.getString("REQUEST_INFO");
			JSONObject requestJson = JSONObject.fromObject(requestInfo);
			if(requestJson.containsKey("channelId")) {
				header.put("channelId", requestJson.getString("channelId"));
			}
		}
		String salesCode = productInfo.getString("SALES_CODE");
		if(salesCode.indexOf("ht_")>-1) {
			salesCode = salesCode.replace("ht_", "");
		}
		String openOrderLanched = VolteInterface.openOrderLanched(phone, salesCode,ringId, backUrl,header);
		JSONObject json = JSONObject.fromObject(openOrderLanched);
		String code = json.getString("res_code");
		result.put("code", json.getString("res_code"));
		result.put("msg", json.getString("res_message"));
		if(code.equals("0")) {
			result.put("order_no", json.getString("order_no"));
			result.put("fee_url", json.getString("fee_url"));
			orderLog.put("ORDER_NO", json.getString("order_no"));
			orderLog.put("BACK_URL", backUrl);//保存回调地址
		}
		orderLog.put("ORDER_TYPE", "6");//电信产品
	
		return result;
	}

	@Override
	public JSONObject queryOrderStatus(PageData queryInfo) throws Exception {
		//电信爱音乐产品
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
			result.put("fee_type", json.getString("fee_type"));
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
		if(salesCode.indexOf("ht_")>-1) {
			salesCode = salesCode.replace("ht_", "");
		}
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
