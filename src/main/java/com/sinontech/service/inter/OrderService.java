package com.sinontech.service.inter;

import com.sinontech.tools.common.PageData;

import net.sf.json.JSONObject;

public interface OrderService {
	/**
	 * 发送订购短信
	 * @param orderInfo
	 * @param productInfo
	 * @param orderLog
	 * @return
	 * @throws Exception
	 */
	JSONObject sendOrderSms(PageData orderInfo,PageData productInfo) throws Exception;
	/**
	 * 提交受理
	 * @param orderInfo
	 * @param productInfo
	 * @param orderLog
	 * @return
	 * @throws Exception
	 */
	JSONObject orderProduct(PageData orderInfo,PageData productInfo,PageData orderLog) throws Exception;
	/**
	 * 查询订单状态
	 * @param queryInfo
	 * @return
	 * @throws Exception
	 */
	JSONObject queryOrderStatus(PageData queryInfo) throws Exception;
	/**
	 * 查询用户产品包
	 * @param queryInfo
	 * @return
	 * @throws Exception
	 */
	JSONObject queryUserPackage(PageData queryInfo,PageData productInfo) throws Exception;

}
