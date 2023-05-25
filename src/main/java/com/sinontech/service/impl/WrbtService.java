package com.sinontech.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.sinontech.service.UserOrderLogService;
import com.sinontech.service.inter.OrderService;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.wrbt.WrbtCInterface;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 沃音乐受理
 * @author Administrator
 *
 */
@Service("wrbtService")
public class WrbtService implements OrderService {

	
	@Resource(name = "userOrderLogService")
	private UserOrderLogService userOrderLogService;
	
	@Override
	public JSONObject sendOrderSms(PageData orderInfo, PageData productInfo) throws Exception {
		JSONObject result = new JSONObject();
		result.put("code", "300");
		result.put("msg", "此产品无此功能");
		return result;
	}

	@Override
	public JSONObject orderProduct(PageData orderInfo, PageData productInfo, PageData orderLog) throws Exception {
		//订购沃音乐产品
		JSONObject result = new JSONObject();
		String phone = orderInfo.getString("phone");
		String backUrl = orderInfo.getString("returnUrl");
		String openOrderLanched = WrbtCInterface.onePointProductMon(phone, productInfo.getString("SALES_CODE"), "1", backUrl);
		JSONObject json = JSONObject.fromObject(openOrderLanched);
		String code = json.getString("returnCode");
		result.put("msg", json.getString("description"));
		if(code.equals("0000")) {
			result.put("code", "0");
			result.put("order_no", json.getString("orderId"));
			result.put("fee_url", json.getString("url"));
			orderLog.put("ORDER_NO", json.getString("orderId"));
		}else {
			result.put("code", json.getString("returnCode"));
		}
		orderLog.put("ORDER_TYPE", "7");//电信产品
		return result;
	}

	@Override
	public JSONObject queryOrderStatus(PageData queryInfo) throws Exception {
		//沃音乐产品
		JSONObject result = new JSONObject();
		String orderNo = queryInfo.getString("queryInfo");
		PageData query = new PageData();
		query.put("ORDER_NO", orderNo);
		PageData order = userOrderLogService.findByOrderNo(query);
		if(order!=null) {
			String resultCode = order.getString("RESULT_CODE");
			if(resultCode.equals("0")) {
				result.put("code", "0");
				result.put("msg", "受理成功");
			}else if(resultCode.equals("")) {
				result.put("code", "001");
				result.put("msg", "受理中");
			}else {
				result.put("code", "002");
				result.put("msg", "受理失败");
			}
		}else {
			result.put("code", "003");
			result.put("msg", "订单不存在");
		}
		return result;
	}

	@Override
	public JSONObject queryUserPackage(PageData queryInfo,PageData productInfo) throws Exception {
		//沃音乐
		JSONObject result = new JSONObject();
		String userPhone = queryInfo.getString("phone");
		String salesCode = productInfo.getString("SALES_CODE");
		
		String wresult = WrbtCInterface.qrySubedProductsNoToken(userPhone);
		JSONObject json = JSONObject.fromObject(wresult);
		String resultCode = json.getString("returnCode");
		result.put("msg", json.get("description"));
		if(resultCode.equals("000000")) {
			result.put("code", "0");
			result.put("msg", "成功");
		}else {
			result.put("code", json.get("returnCode"));
		}
		if(json.containsKey("subedProducts")) {
			List<JSONObject> packages = new ArrayList<JSONObject>();
			JSONArray list = json.getJSONArray("subedProducts");
			if(list!=null&&list.size()>0) {
				for(int i=0;i<list.size();i++) {
					JSONObject pack = list.getJSONObject(i);
					JSONObject returnPack = new JSONObject();
					returnPack.put("packageId", pack.get("productId"));
					returnPack.put("downNum", "0");
					returnPack.put("orderTime", pack.get("subTime"));
					returnPack.put("unsubscribeTime", pack.get("unSubTime"));
					returnPack.put("unSubscribeable", pack.get("unSubscribeable"));
					returnPack.put("status", pack.get("status"));
					String pId = pack.getString("productId");
					if(!StringUtils.isBlank(salesCode)) {
						if(salesCode.equals(pId)) {
							packages.add(returnPack);
							break;
						}
					}else {
						packages.add(returnPack);
					}
				}
			}
			result.put("packages", packages);
		}
		return result;
	}



}
