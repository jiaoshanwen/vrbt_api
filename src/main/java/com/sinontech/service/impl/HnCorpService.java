package com.sinontech.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sinontech.service.UserOrderLogService;
import com.sinontech.service.inter.OrderService;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.hn.ShopInterface;

import net.sf.json.JSONObject;
/**
 * 湖南企业视频彩铃
 * @author Administrator
 *
 */
@Service("hnCorpService")
public class HnCorpService implements OrderService {
	
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
		//湖南企业视频彩铃
		String requestInfo = productInfo.getString("REQUEST_INFO");
		JSONObject requestJson = JSONObject.fromObject(requestInfo);
		String managerPhone = requestJson.getString("MANAGER_PHONE");
		String typeName = requestJson.getString("TYPE_NAME");
		String customText = requestJson.getString("CUSTOM_TEXT");
		String backUrl = orderInfo.getString("returnUrl");
		String pageUrl = orderInfo.getString("pageUrl");
		String phone = orderInfo.getString("phone");
		JSONObject resultJson = ShopInterface.order(phone, typeName, managerPhone, pageUrl, customText);
		String code = resultJson.getString("code");
		result.put("msg", resultJson.getString("msg"));
		if(code.equals("0000")) {
			code = "0";
			String orderNum = resultJson.getString("orderNo");
			String orderPage = resultJson.getString("url");
			orderLog.put("ORDER_NO", orderNum);
			result.put("order_no", orderNum);
			result.put("fee_url", orderPage);
		}
		result.put("code", code);
		orderLog.put("ORDER_TYPE", "15");//湖南企业视频彩铃
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
		JSONObject result = new JSONObject();
		result.put("code", "300");
		result.put("msg", "此产品无此功能");
		return result;
	}



}
