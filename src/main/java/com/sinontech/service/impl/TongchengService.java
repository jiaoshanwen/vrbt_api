package com.sinontech.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sinontech.service.RedisService;
import com.sinontech.service.UserOrderLogService;
import com.sinontech.service.inter.OrderService;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.yd.YdInterface;

import net.sf.json.JSONObject;

/**
 * 移动同程
 * @author Administrator
 *
 */
@Service("tongchengService")
public class TongchengService implements OrderService {

	@Autowired
	private RedisService redisService;

	@Resource(name = "userOrderLogService")
	private UserOrderLogService userOrderLogService;
	
	@Override
	public JSONObject sendOrderSms(PageData orderInfo, PageData productInfo) throws Exception {
		JSONObject result = new JSONObject();
		String phone = orderInfo.getString("phone");
		String contentId = orderInfo.getString("contentId");
		String resultStr = YdInterface.sendMsg(phone, contentId);
		//{"transId":"202207151027982303","resCode":"000000","resMsg":"success"}
		JSONObject ydResult = JSONObject.fromObject(resultStr);
		String code = ydResult.getString("resCode");
		String msg = ydResult.getString("resMsg");
		if(code.equals("000000")) {
			String transId = ydResult.getString("transId");
			String oid = ydResult.getString("oid");
			result.put("code", "0");
			result.put("msg", "成功");
			result.put("transId", transId);
			result.put("oid", oid);
		}else {
			result.put("code", code);
			result.put("msg", msg);
		}
		return result;
	}

	@Override
	public JSONObject orderProduct(PageData orderInfo, PageData productInfo, PageData orderLog) throws Exception {
		JSONObject result = new JSONObject();
		//移动同程
		String backUrl = orderInfo.getString("returnUrl");
		String transId = orderInfo.getString("transId");
		String oid = orderInfo.getString("oid");
		String phone = orderInfo.getString("phone");
		String smsCode = orderInfo.getString("smsCode");
		String sessionid = redisService.get(oid);
		if(StringUtils.isBlank(sessionid)) {
			this.redisService.put(oid, oid, 60L);
			PageData query = new PageData();
			query.put("ORDER_NO", oid);
			PageData order = userOrderLogService.findByOrderNo(query);
			if(order!=null) {
				orderLog = order;
			}
			String resultStr = YdInterface.submitSmsCode(phone, smsCode, transId,oid);
			JSONObject resultJson = JSONObject.fromObject(resultStr);
			String code = resultJson.getString("resCode");
			if(code.equals("000000")) {
				orderLog.put("ORDER_NO",oid);
				result.put("order_no", oid);
				result.put("code", "0");
				result.put("msg", "提交成功");
			}else {
				result.put("code", code);
				result.put("msg", resultJson.getString("resMsg"));
			}
			orderLog.put("ORDER_TYPE", "8");//移动同程
			orderLog.put("BACK_URL", backUrl);//保存回调地址
		}else {
			result.put("code", "0003");
			result.put("msg", "重复提交");
		}
		return result;
	}

	@Override
	public JSONObject queryOrderStatus(PageData queryInfo) throws Exception {
		//移动同程
		JSONObject result = new JSONObject();
		String orderNo = queryInfo.getString("orderNo");
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
		//移动同程
		JSONObject result = new JSONObject();
		String userPhone = queryInfo.getString("phone");
		String salesCode = productInfo.getString("SALES_CODE");
		String yresult = YdInterface.queryVideoCrbtMonthState(userPhone, salesCode);
		JSONObject json = JSONObject.fromObject(yresult);
		String resultCode = json.getString("code");
		result.put("msg", json.get("msg"));
		if(resultCode.equals("000000")) {
			List<JSONObject> packages = new ArrayList<JSONObject>();
			result.put("code", "0");
			result.put("msg", "成功");
			String status = json.getString("status");
			if(status.equals("1")) {
				JSONObject returnPack = new JSONObject();
				returnPack.put("packageId", salesCode);
				packages.add(returnPack);
			}
			result.put("packages", packages);
		}else {
			result.put("code", json.get("returnCode"));
		}
		return result;
	}

}
