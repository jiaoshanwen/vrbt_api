package com.sinontech.controller.server;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinontech.controller.base.ExceptionController;
import com.sinontech.service.ProductInfoService;
import com.sinontech.tools.common.AliTTS;
import com.sinontech.tools.common.PageData;

import net.sf.json.JSONObject;



@Controller
@ResponseBody
@RequestMapping(value="/server")
public class BusnessController extends ExceptionController {
	
	
	
	@Resource(name="productInfoService")
	private ProductInfoService productInfoService;
	
	/**
	 * 健康检查
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/health", produces = "text/html;charset=utf-8")
	public  String health(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String host = request.getServerName();
		Integer port = request.getServerPort();
		return "我是心跳，来自:"+host+",端口："+port;
	}
	/**
	 * 发送订购短信验证码
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/sendOrderSms", produces = "text/html;charset=utf-8")
	public  String sendOrderSms(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = getPageDataJson();
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.sendOrderSms(data);
		} catch (Exception e) {
			logger.info(e.toString());
			e.printStackTrace();
			request.setAttribute("data",data);
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
	public  String orderProduct(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = getPageDataJson();
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.orderProduct(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
			request.setAttribute("data",data);
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
	public  String queryOrderStatus(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = getPageDataJson();
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.queryOrderStatus(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
			request.setAttribute("data",data);
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
	public  String queryUserPackage(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = getPageDataJson();
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.queryUserPackage(data);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.toString());
			request.setAttribute("data",data);
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
	public  String synMobileLog(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = getPageDataJson();
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.synMobileLog(data);
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
	 * 检查省份是否可以受理 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/checkProvince", produces = "text/html;charset=utf-8")
	public  String checkProvince(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = getPageDataJson();
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.checkProvince(data);
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
	 * 检测优享会员是否可以订购
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/checkYX", produces = "text/html;charset=utf-8")
	public  String checkYX(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = getPageDataJson();
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.checkYX(data);
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
	 * 业务退订（沃音乐和爱音乐）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/canceProduct", produces = "text/html;charset=utf-8")
	public  String canceProduct(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = getPageDataJson();
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
	 * 获取用户视频库（沃音乐和爱音乐）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/queryUserVideo", produces = "text/html;charset=utf-8")
	public  String queryUserVideo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = getPageDataJson();
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.queryUserVideo(data);
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
	 * 免费权益订购视频（爱音乐和沃音乐）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/orderVideo", produces = "text/html;charset=utf-8")
	public  String orderVideo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = getPageDataJson();
		JSONObject result = new JSONObject();
		try {
			result = productInfoService.orderVideo(data);
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
	public  String delVideo(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = getPageDataJson();
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
	 * 增加视频设置（爱音乐）
	 * @param data
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/addRingset", produces = "text/html;charset=utf-8")
	public  String addRingset(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PageData data = getPageDataJson();
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
	 * 信息提示
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("message")
	public  String message(HttpServletRequest request,HttpServletResponse response) {
		String message = (String)request.getAttribute("message");
		logger.info(message);
		return message;
	}
	/**
	 * 监控电话告警
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("tel")
	public  String tel(HttpServletRequest request,HttpServletResponse response) {
		String name = request.getParameter("name");
		JSONObject json = new JSONObject();
     	json.put("name", name);
     	System.out.println(AliTTS.callPhoneWhenSystemWarn("TTS_269670008","17316908160",json.toString()));
		return "ok";
	}
}
