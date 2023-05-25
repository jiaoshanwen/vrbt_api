package com.sinontech.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;
import com.sinontech.service.ProductInfoService;
import com.sinontech.service.SmsSendService;
import com.sinontech.service.inter.OrderService;
import com.sinontech.tools.common.DateUtil;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.yd.GsYdInterface;
import com.sinontech.tools.yd.XjYdInterface;

import net.sf.json.JSONObject;
/**
 * 甘肃移动流量包产品
 * @author Administrator
 *
 */
@Service("gsMobileService")
public class GsMobileService implements OrderService {

	@Resource(name="productInfoService")
	private ProductInfoService productInfoService;
	
	@Override
	public JSONObject sendOrderSms(PageData orderInfo, PageData productInfo) throws Exception {
		JSONObject result = new JSONObject();
		String phone = orderInfo.getString("phone");
		String salesCode = productInfo.getString("SALES_CODE");
		JSONObject json = GsYdInterface.sendSms(phone, salesCode);
		String retCode = json.getString("retCode");
		String respMsg = json.getString("respMsg");
		result.put("msg", respMsg);
		result.put("code", retCode);
		if(retCode.equals("111000")) {
			result.put("code", "0");
			JSONObject data = json.getJSONObject("data");
			result.put("identifyingKey", data.getString("identifyingKey"));
		}
		return result;
	}

	@Override
	public JSONObject orderProduct(PageData orderInfo, PageData productInfo, PageData orderLog) throws Exception {
		//新疆移动特惠包
		JSONObject result = new JSONObject();
		String phone = orderInfo.getString("phone");
		String smsCode = orderInfo.getString("smsCode");
		String identifyingKey = orderInfo.getString("identifyingKey");
		String salesCode = productInfo.getString("SALES_CODE");
		JSONObject resultJson = GsYdInterface.submit(phone, salesCode, identifyingKey, smsCode);
		String retCode = resultJson.getString("retCode");
		String respMsg = resultJson.getString("respMsg");
		result.put("msg", respMsg);
		result.put("code", retCode);
		if(retCode.equals("111000")) {
			result.put("code", "0");
			JSONObject repData = resultJson.getJSONObject("data");
			JSONObject busiInfo = repData.getJSONObject("RSP_PARAM").getJSONObject("BUSI_INFO");
			if(busiInfo.size()==0) {
				result.put("code", "-1");
				result.put("msg", "受理失败");
			}else {
				String orderNo = busiInfo.getString("ORDER_ID");
				orderLog.put("ORDER_NO", orderNo);
				result.put("order_no", orderNo);
				orderLog.put("RESULT_CODE", "0");// 成功
				orderLog.put("IS_ON", "1");// 成功
			}
		}
		orderLog.put("ORDER_TYPE", "19");//甘肃移动特惠包
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
		String phone = queryInfo.getString("phone");
		String salesCode = productInfo.getString("SALES_CODE");
		JSONObject json = GsYdInterface.query(phone, salesCode);
		return json;
	}


}
