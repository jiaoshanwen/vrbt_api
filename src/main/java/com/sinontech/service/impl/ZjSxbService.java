package com.sinontech.service.impl;

import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sinontech.service.SmsSendService;
import com.sinontech.service.UserOrderLogService;
import com.sinontech.service.inter.OrderService;
import com.sinontech.tools.common.DateUtil;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.isale.IsaleInterface;

import net.sf.json.JSONObject;

/**
 * 浙江舒享包
 * @author Administrator
 *
 */
@Service("zjSxbService")
public class ZjSxbService implements OrderService {

	@Resource(name="smsSendService")
	private SmsSendService smsSendService;
	
	@Resource(name = "userOrderLogService")
	private UserOrderLogService userOrderLogService;
	
	@Override
	public JSONObject sendOrderSms(PageData orderInfo, PageData productInfo) throws Exception {
		JSONObject result = new JSONObject();
		String phone = orderInfo.getString("phone");
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<6;i++){
			String rand=String.valueOf(r.nextInt(10));
			sb.append(rand);
		}
		JSONObject verifySendSms = smsSendService.verifySendSms(phone);
		if(verifySendSms.getString("code").equals("0")) {
			String sendMsg = "【业务认证】尊敬的用户，您的验证码为"+sb.toString()+"，请勿泄露给他人，如非本人操作，请忽略此信息【爱音乐】";
			String sendResult = IsaleInterface.sendMess(phone, sendMsg);
			if(sendResult.equals("ok")) {
				PageData sms=new PageData();
				sms.put("USER_PHONE", phone);
				sms.put("ADD_TIME", DateUtil.getTime());
				sms.put("SMS_CODE", sb.toString());
				sms.put("SMS_STATUS", "A");
				sms.put("ERROR_CODE", sendMsg);
				smsSendService.save(sms);
				
				result.put("code", "0");
				result.put("msg", "成功");
			}else {
				result.put("code", "0002");
				result.put("msg", "短信发送失败");
			}
		}else {
			result.put("code", verifySendSms.getString("code"));
			result.put("msg", verifySendSms.getString("msg"));
		}
		return result;
	}

	@Override
	public JSONObject orderProduct(PageData orderInfo, PageData productInfo, PageData orderLog) throws Exception {
		JSONObject result = new JSONObject();
		String cooperateId = orderInfo.getString("cooperateId");
		String phone = orderInfo.getString("phone");
		String smsCode = orderInfo.getString("smsCode");
		String areaCode = orderInfo.getString("areaCode");
		JSONObject verifySms = smsSendService.verifySms(phone, smsCode);
		if(verifySms.getString("code").equals("0")) {
			JSONObject intResult = IsaleInterface.createSaleOrderComfortBa(phone, areaCode, productInfo.getString("SALES_CODE"));
			String code =  intResult.getString("returncode");
			result.put("code", intResult.getString("returncode"));
			result.put("msg", intResult.getString("description"));
			if(code.equals("0")) {
				String orderNum = intResult.getString("orderNum");
				// 1 表示6+6元  2表示 6元
				String type = intResult.getString("type");
				orderLog.put("ORDER_NO", orderNum);
				result.put("order_no", orderNum);
				result.put("type", type);
				if(type.equals("1")) {
					//增加isale心动会员订单记录
					PageData orderIsaleLog = new PageData();
					orderIsaleLog.put("ORDER_NO", orderNum+"_xd");
					orderIsaleLog.put("ORDER_TYPE", "9");//isale订购
					orderIsaleLog.put("C_ID", cooperateId);
					orderIsaleLog.put("USER_PHONE", phone);
					orderIsaleLog.put("ORDER_TIME", DateUtil.getTime());
					orderIsaleLog.put("ORDER_RESULT", result.toString());
					orderIsaleLog.put("ERROR_CODE", result.get("code"));
					orderIsaleLog.put("ORDER_CODE", "7360110000100254");
					userOrderLogService.save(orderIsaleLog);
				}
			}
		}else {
			result.put("code", verifySms.getString("code"));
			result.put("msg", verifySms.getString("msg"));
		}
		orderLog.put("ORDER_TYPE", "9");//isale订购
		return result;
	}

	@Override
	public JSONObject queryOrderStatus(PageData queryInfo) {
		JSONObject result = new JSONObject();
		String orderNo = queryInfo.getString("orderNo");
		JSONObject json = IsaleInterface.queryTradeOrderStatus(orderNo);
		result.put("code", json.getString("returncode"));
		result.put("msg", json.getString("description"));
		return result;
	}

	@Override
	public JSONObject queryUserPackage(PageData queryInfo,PageData productInfo) {
		JSONObject result = new JSONObject();
		result.put("code", "300");
		result.put("msg", "此产品无查询功能");
		return result;
	}


}
