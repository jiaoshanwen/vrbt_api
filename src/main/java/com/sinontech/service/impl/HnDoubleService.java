package com.sinontech.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sinontech.service.UserOrderLogService;
import com.sinontech.service.inter.OrderService;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.hn.HnVrbtInterface;

import net.sf.json.JSONObject;

/**
 * 湖南6+10
 * @author Administrator
 *
 */
@Service("hnDoubleService")
public class HnDoubleService implements OrderService {
	
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
		JSONObject result = new JSONObject();
		String phone = orderInfo.getString("phone");
		String backUrl = orderInfo.getString("returnUrl");
		String pageUrl = orderInfo.getString("pageUrl");
		String resultStr = HnVrbtInterface.acceptOrder(phone, productInfo.getString("SALES_CODE"),pageUrl);
		JSONObject resultJson = JSONObject.fromObject(resultStr);
		String code = resultJson.getString("code");
		result.put("code", code);
		result.put("msg", resultJson.getString("msg"));
		if(code.equals("0")) {
			String orderNum = resultJson.getString("orderNum");
			String orderPage = resultJson.getString("orderPage");
			orderLog.put("ORDER_NO", orderNum);
			result.put("order_no", orderNum);
			result.put("fee_url", orderPage);
		}
		orderLog.put("ORDER_TYPE", "10");//湖南6+10订购
		orderLog.put("BACK_URL", backUrl);//保存回调地址
		return result;
	}
	
	@Override
	public JSONObject queryOrderStatus(PageData queryInfo) throws Exception {
		JSONObject result = new JSONObject();
		String orderNo = queryInfo.getString("orderNo");
		PageData querOrder = new PageData();
		querOrder.put("ORDER_NO", orderNo);
		PageData order = userOrderLogService.findByOrderNo(querOrder);
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
		String userPhone = queryInfo.getString("phone");
		JSONObject result = new JSONObject();
		String resultStr = HnVrbtInterface.queryVideoStatus(userPhone);
		JSONObject resultJson = JSONObject.fromObject(resultStr);
		String code = resultJson.getString("code");
		result.put("code", code);
		if(code.equals("0")) {
			result.put("msg", "成功");
			List<JSONObject> packages = new ArrayList<JSONObject>();
			String offerId = resultJson.getString("offer_id");
			if(offerId.equals("9016116")) {
				JSONObject returnPack = new JSONObject();
				returnPack.put("packageId", offerId);
				packages.add(returnPack);
			}
			result.put("packages", packages);
		}else {
			result.put("msg", "无产品");
		}
		return result;
	}


}
