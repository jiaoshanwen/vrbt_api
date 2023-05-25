package com.sinontech.controller.api;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinontech.controller.base.ExceptionController;
import com.sinontech.service.ProductInfoService;
import com.sinontech.tools.common.CommonHttpUtils;
import com.sinontech.tools.common.Const;
import com.sinontech.tools.common.PageData;

import net.sf.json.JSONObject;



/**
 * 
 * 产品订购
 * @author guchao
 *
 */
@Controller
@RequestMapping(value="/api/crbt")
public class OrderController extends ExceptionController {
	@Autowired
	public ProductInfoService productInfoService;
	
	
	/**
	 * 发送订购短信验证码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/sendOrderSms", produces = "text/html;charset=utf-8")
	public @ResponseBody String sendOrderSms(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = (PageData)request.getAttribute("data");
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.sendOrderSms(data);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "404");
			result.put("msg", "操作异常");
			throw e;
		}
		return result.toString();
	}
	/**
	 * 下单受理
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/orderProduct", produces = "text/html;charset=utf-8")
	public @ResponseBody String orderProduct(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = (PageData)request.getAttribute("data");
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.orderProduct(data);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "404");
			result.put("msg", "操作异常");
			throw e;
		}
		return result.toString();
	}
	/**
	 * 查询订单状态
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryOrderStatus", produces = "text/html;charset=utf-8")
	public @ResponseBody String queryOrderStatus(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = (PageData)request.getAttribute("data");
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.queryOrderStatus(data);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "404");
			result.put("msg", "操作异常");
			throw e;
		}
		return result.toString();
	}
	/**
	 * 获取用户的产品包
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryUserPackage", produces = "text/html;charset=utf-8")
	public @ResponseBody String queryUserPackage(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = (PageData)request.getAttribute("data");
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.queryUserPackage(data);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "404");
			result.put("msg", "操作异常");
			throw e;
		}
		return result.toString();
	}
	/**
	 * 同步移动订购记录
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/synMobileLog", produces = "text/html;charset=utf-8")
	public @ResponseBody String synMobileLog(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = (PageData)request.getAttribute("data");
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.synMobileLog(data);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "404");
			result.put("msg", "操作异常");
			throw e;
		}
		return result.toString();
	}
	/**
	 * 检查省份是否可以受理 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/checkProvince", produces = "text/html;charset=utf-8")
	public @ResponseBody String checkProvince(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = (PageData)request.getAttribute("data");
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.checkProvince(data);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "404");
			result.put("msg", "操作异常");
			throw e;
		}
		return result.toString();
	}
	/**
	 * 检测优享会员是否可以订购
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/checkYX", produces = "text/html;charset=utf-8")
	public @ResponseBody String checkYX(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = (PageData)request.getAttribute("data");
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.checkYX(data);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "404");
			result.put("msg", "操作异常");
			throw e;
		}
		return result.toString();
	}
	/**
	 * 使用包月产品权益免费订购视频铃音接口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/addToneFreeOnProduct", produces = "text/html;charset=utf-8")
	public @ResponseBody String addToneFreeOnProduct(HttpServletRequest request,HttpServletResponse response) {
		PageData data = (PageData)request.getAttribute("data");
		JSONObject result = new JSONObject();
		
		try {
			result = productInfoService.orderVideo(data);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code", "404");
			result.put("msg", "操作异常");
		}
		return result.toString();
	}
	/**
	 * 业务退订（沃音乐和爱音乐）
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/canceUserProduct", produces = "text/html;charset=utf-8")
	public @ResponseBody String canceProduct(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = (PageData)request.getAttribute("data");
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.canceProduct(data);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("data",data);
			result.put("code", "404");
			result.put("msg", "操作异常");
			throw e;
		}
		return result.toString();
	}
	
	/**
	 * 增加视频设置（爱音乐）
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/addRingset", produces = "text/html;charset=utf-8")
	public @ResponseBody String addRingset(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = (PageData)request.getAttribute("data");
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.addRingset(data);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("data",data);
			result.put("code", "404");
			result.put("msg", "操作异常");
			throw e;
		}
		return result.toString();
	}
	/**
	 * 删除订购视频（爱音乐和沃音乐）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/delVideo", produces = "text/html;charset=utf-8")
	public @ResponseBody String delVideo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = (PageData)request.getAttribute("data");
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.delVideo(data);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("data",data);
			result.put("code", "404");
			result.put("msg", "操作异常");
			throw e;
		}
		return result.toString();
	}
	/**
	 * 信息提示
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("message")
	public @ResponseBody String message(HttpServletRequest request,HttpServletResponse response) {
		String message = (String)request.getAttribute("message");
		logger.info(message);
		return message;
	}
}
