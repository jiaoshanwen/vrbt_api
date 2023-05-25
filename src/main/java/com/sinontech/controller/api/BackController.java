package com.sinontech.controller.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.asiainfo.womail.hsrc.Signer;
import com.sinontech.controller.base.BaseController;
import com.sinontech.service.ApplicationInfoService;
import com.sinontech.service.UserInfoService;
import com.sinontech.service.UserOrderLogService;
import com.sinontech.service.UserSubmitService;
import com.sinontech.tools.b2l.B2lInterface;
import com.sinontech.tools.common.AuthUtils;
import com.sinontech.tools.common.CommonHttpUtils;
import com.sinontech.tools.common.DateUtil;
import com.sinontech.tools.common.MD5;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.common.WrbtInterfaceInfo;
import com.sinontech.tools.hn.ShopInterface;

import net.sf.json.JSONObject;



/**
 * 
 * 回调
 * @author guchao
 *
 */
@Controller
@RequestMapping(value="/api/back")
public class BackController extends BaseController {
	
	
	@Autowired
	private ApplicationInfoService applicationInfoService;
	
	@Autowired
	private UserOrderLogService userOrderLogService;
	
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private UserSubmitService userSubmitService;
	
	
	
	
	@RequestMapping(value="/synZjqy", produces = "text/html;charset=utf-8")
	public @ResponseBody String synZjqy(HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		return result.toString();
	}
	
	/**
	 * ismp订购结果同步
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/ismpSyn/{channel}", produces = "text/html;charset=utf-8")
	public @ResponseBody String ismpSyn(@PathVariable("channel") String channel,HttpServletRequest request,HttpServletResponse response) {
//		PageData data = getPageDataJson();
		JSONObject result = new JSONObject();
		String mobile = request.getParameter("mobile");
		String productid = request.getParameter("productid");
		String state = request.getParameter("state");//状态 0 订购 1 退订(短代点播产品 0 成功 1 失败)
		String time = request.getParameter("time");
		String orderNo = request.getParameter("orderNo");
		String deviceNo = request.getParameter("deviceNo");
		String streamingNo = request.getParameter("streamingNo");
		String orderTime = request.getParameter("orderTime");
		String validTime = request.getParameter("validTime");
		String channelId = request.getParameter("channelId");
		
		String deviceid = request.getHeader("deviceid");
		String timestamp = request.getHeader("timestamp");
		String signature = request.getHeader("signature-hmacsha256");
		
		logger.info("爱音乐ismp回调参数");
		logger.info("channelId="+channelId);
		logger.info("mobile="+mobile);
		logger.info("productid="+productid);
		logger.info("state="+state);
		logger.info("time="+time);
		logger.info("orderNo="+orderNo);
		logger.info("deviceNo="+deviceNo);
		logger.info("streamingNo="+streamingNo);
		logger.info("orderTime="+orderTime);
		logger.info("validTime="+validTime);
		logger.info("timestamp="+timestamp);
		logger.info("signature="+signature);
		logger.info("deviceid="+deviceid);
		try {
			PageData queryApplication = new PageData();
			queryApplication.put("ID", channel);
			PageData applicationInfo = applicationInfoService.findById(queryApplication);
			if(applicationInfo==null) {
				result.put("code",  "1006");
				result.put("msg", "同步失败");
				return result.toString();
			}
			String str = timestamp+mobile+productid+state;
			String keyword = applicationInfo.getString("APP_SECRET");
			String auth_signature = AuthUtils.generateMac256Signature(keyword,str);
			
			if(!signature.equals(auth_signature)) {
				result.put("code", "1007");
				result.put("description", "签名失败");
				return result.toString();
			}
			if(channelId!=null&&channelId.equals("5825")&&state.equals("0")) {
				JSONObject orderResult = new JSONObject();
				orderResult.put("code", "0");
				orderResult.put("msg", "受理成功");
				PageData query = new PageData();
				query.put("ORDER_NO", orderNo);
				PageData order = userOrderLogService.findByOrderNo(query);
				if(order==null) {
					if(channelId.equals("126002120002")) {
						productid = "ht_"+productid;
					}
					PageData orderLog = new PageData();
					orderLog.put("ORDER_CODE", productid);
					orderLog.put("ORDER_NO", orderNo);
					orderLog.put("ORDER_TYPE", "6");//电信产品
					orderLog.put("C_ID", "26");
					orderLog.put("USER_PHONE", mobile);
					orderLog.put("ORDER_TIME", DateUtil.getTime());
					orderLog.put("ORDER_RESULT", orderResult.toString());
					orderLog.put("ERROR_CODE", "0");
					orderLog.put("RESULT_CODE", "0");// 成功
					orderLog.put("IS_ON", "1");// 成功
					userOrderLogService.save(orderLog);
					
					PageData saveUser = new PageData();
					saveUser.put("USER_PHONE", orderLog.get("USER_PHONE"));
					saveUser.put("ORDER_CODE", orderLog.get("ORDER_CODE"));
					saveUser.put("ORDER_MONTH", orderTime.substring(0, 7));
					saveUser.put("ORDER_TIME", orderTime);
					saveUser.put("ON_MONTH", 1);
					saveUser.put("IS_ON", "1");
					saveUser.put("C_ID", orderLog.get("C_ID"));
					saveUser.put("LOG_ID", orderLog.get("ID"));
					userInfoService.save(saveUser);
				}
			}
			if(productid.equals("135999999999999000256")&&state.equals("0")) {
				JSONObject orderResult = new JSONObject();
				orderResult.put("code", "0");
				orderResult.put("msg", "受理成功");
				PageData query = new PageData();
				query.put("ORDER_NO", orderNo);
				PageData order = userOrderLogService.findByOrderNo(query);
				if(order==null) {
					PageData orderLog = new PageData();
					orderLog.put("ORDER_CODE", productid);
					orderLog.put("ORDER_NO", orderNo);
					orderLog.put("ORDER_TYPE", "6");//电信产品
					orderLog.put("C_ID", "14");
					orderLog.put("USER_PHONE", mobile);
					orderLog.put("ORDER_TIME", DateUtil.getTime());
					orderLog.put("ORDER_RESULT", orderResult.toString());
					orderLog.put("ERROR_CODE", "0");
					orderLog.put("RESULT_CODE", "0");// 成功
					orderLog.put("IS_ON", "1");// 成功
					userOrderLogService.save(orderLog);
					
					PageData saveUser = new PageData();
					saveUser.put("USER_PHONE", orderLog.get("USER_PHONE"));
					saveUser.put("ORDER_CODE", orderLog.get("ORDER_CODE"));
					saveUser.put("ORDER_MONTH", orderTime.substring(0, 7));
					saveUser.put("ORDER_TIME", orderTime);
					saveUser.put("ON_MONTH", 1);
					saveUser.put("IS_ON", "1");
					saveUser.put("C_ID", orderLog.get("C_ID"));
					saveUser.put("LOG_ID", orderLog.get("ID"));
					userInfoService.save(saveUser);
				}
			}
			result.put("code", "000000");
			result.put("description", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",  "1006");
			result.put("msg", "同步失败");
			return result.toString();
		}
		return result.toString();
	}
	/**
	 * 沃音乐订购结果回调
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/wSyn", produces = "text/html;charset=utf-8")
	public @ResponseBody String wSyn(HttpServletRequest request,HttpServletResponse response) {
		PageData log = new PageData();
		log.put("C_ID", "0");
		log.put("ORDER_METHOD", "com.sinontech.controller.crud.wSyn");
		log.put("ORDER_TIME", DateUtil.getTime());
		try {
			String  timestamp = request.getHeader("timestamp");
			String  authorization = request.getHeader("Authorization");
			
			BufferedReader br = request.getReader();
		    String str, body = "";
		    while((str = br.readLine()) != null){
		        body += str;
		    }
			Signer.Response signResponse = Signer.create()
											.setAccessKey(WrbtInterfaceInfo.APPKEY)
											.setAccessSecret(WrbtInterfaceInfo.APPSCREAT)
											.setTimestamp(timestamp)
											.putData("POST_DATA", body).sign();
			String sign = signResponse.getSign();
			String myAuthorization = signResponse.getAuthorization();
			
			logger.info("接受：timestamp="+timestamp);
			logger.info("接受：authorization="+authorization);
			logger.info("接受：body="+body);
			
			logger.info("加密签名：sign="+sign);
			logger.info("验证字符串：myAuthorization="+myAuthorization);

			if(!authorization.equals(myAuthorization)) {
				logger.info("验证结果：false");
				log.put("BACK_INFO", "false");
				userOrderLogService.addLog(log);
				return "false:签名验证失败";
			}
			log.put("ORDER_INFO", body);
			JSONObject resultBody = JSONObject.fromObject(body);
			JSONObject notice = resultBody.getJSONObject("notice");
			String orderId = "";
			boolean orderflag = false;
			
			String msg = "订购失败";
			if(notice.containsKey("notify")) {
				JSONObject notify = notice.getJSONObject("notify");
				String returnCode = notify.getString("returnCode");
				orderId = notify.getString("id");
				if(returnCode.equals("000000")) {
					//订购成功
					orderflag = true;
					msg = "订购成功";
				}else {
					//订购失败
					msg = notify.getString("description");
				}
			}else {
				orderId = notice.getString("OrderId");
				if(notice.containsKey("Retn")&&notice.getString("Retn").equals("0000")) {
					//订购并设置成功
					orderflag = true;
				}else {
					//订购失败
				}
			}
			try {
				PageData pd = new PageData();
				pd.put("ORDER_NO", orderId);
				PageData order = userOrderLogService.findByOrderNo(pd);
				if(order!=null) {
					if(orderflag) {
						order.put("RESULT_CODE", "0");//成功
						order.put("IS_ON", "1");//成功
						//发送短信
						Map<String, Object> variableMap = new HashMap<String, Object>();
						variableMap.put("effectiveTime", DateUtil.getDay());
						variableMap.put("productName", "妙趣彩铃6元包月");
						variableMap.put("productPrice", "6元/月");
						variableMap.put("telPhone", "4006611850");
//						logger.info("发送短信接口："+WrbtInterfaceInfo.sendMsg(order.getString("USER_PHONE"), "211577", variableMap));
						
						//增加到成功用户信息表
						String orderTime = order.getString("ORDER_TIME");
						PageData saveUser = new PageData();
						saveUser.put("USER_PHONE", order.get("USER_PHONE"));
						saveUser.put("ORDER_CODE", order.get("ORDER_CODE"));
						saveUser.put("ORDER_MONTH", orderTime.substring(0, 7));
						saveUser.put("ORDER_TIME", orderTime);
						saveUser.put("ON_MONTH", 1);
						saveUser.put("IS_ON", "1");
						saveUser.put("C_ID", order.get("C_ID"));
						saveUser.put("LOG_ID", order.get("ID"));
						userInfoService.save(saveUser);
					}else {
						order.put("RESULT_CODE", "1");//失败
						order.put("IS_ON", "0");//失败
					}
					userOrderLogService.edit(order);
					
//					try {
//						
//						if(order.getString("C_ID").equals("18")) {
//							logger.info("回调联通结果给信息流");
//							String result = XxlUtils.backMsg(orderId, msg, order.getString("IS_ON"), DateUtil.getChar14());
//							logger.info("回调联通结果给信息流返回："+result);
//						}
//					} catch (Exception e) {
//						e.printStackTrace();
//						logger.info("回调信息流接口请求出错！");
//					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("订单更新出错");
			}
			log.put("BACK_INFO", "success");
			userOrderLogService.addLog(log);
			logger.info("验证结果：success");
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("验证结果：false");
			log.put("BACK_INFO", "false");
			try {
				userOrderLogService.addLog(log);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return "false:签名验证失败";
		}
	
	}
	
	@RequestMapping(value="/hnSyn", produces = "text/html;charset=utf-8")
	public @ResponseBody String hnSyn(HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		 BufferedReader reader=null;
		try {
			StringBuffer buffer = new StringBuffer();
            
             reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
             String line=null;
             while((line = reader.readLine())!=null){
                 buffer.append(line);
             }
	         String bf = new String(buffer.toString().getBytes(), "UTF-8");
		    
			JSONObject data = JSONObject.fromObject(bf);
			String orderNum = data.getString("orderNum");
			String registerStatus = data.getString("registerStatus");
			String orderStatus = data.getString("orderStatus");
			logger.info("湖南回调回调参数"+JSONObject.fromObject(data).toString());
			PageData pd = new PageData();
			pd.put("ORDER_NO", orderNum);
			PageData order = userOrderLogService.findByOrderNo(pd);
			if(order!=null) {
				if((registerStatus.equals("0")||registerStatus.equals("3"))&&orderStatus.equals("0")) {//成功
					order.put("RESULT_CODE", "0");//成功
					order.put("IS_ON", "1");//成功
					//增加到成功用户信息表
					String orderTime = order.getString("ORDER_TIME");
					PageData saveUser = new PageData();
					saveUser.put("USER_PHONE", order.get("USER_PHONE"));
					saveUser.put("ORDER_CODE", order.get("ORDER_CODE"));
					saveUser.put("ORDER_MONTH", orderTime.substring(0, 7));
					saveUser.put("ORDER_TIME", orderTime);
					saveUser.put("ON_MONTH", 1);
					saveUser.put("IS_ON", "1");
					saveUser.put("C_ID", order.get("C_ID"));
					saveUser.put("LOG_ID", order.get("ID"));
					userInfoService.save(saveUser);
				}else {
					order.put("RESULT_CODE", "1");//失败
					order.put("IS_ON", "0");//失败
				}
				userOrderLogService.edit(order);
				String url = order.getString("BACK_URL");
				if(StringUtils.isNotBlank(url)) {
					JSONObject json = backPost(url,data.toString());
					return json.toString();
				}
			}else {
				result.put("code",  "1006");
				result.put("msg", "同步失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",  "1006");
			result.put("msg", "同步失败");
			return result.toString();
		}finally {
			try {
				if(reader!=null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}
	
	
	@RequestMapping(value="/readSyn", produces = "text/html;charset=utf-8")
	public @ResponseBody String readSyn(HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		 BufferedReader reader=null;
		try {
			 String sign = request.getParameter("sign");
			 String instatus = request.getParameter("instatus");
			PageData pd = new PageData();
			pd.put("ORDER_NO", sign);
			PageData order = userOrderLogService.findByOrderNo(pd);
			if(order!=null) {
				if(instatus.equals("3")) {//成功
					order.put("RESULT_CODE", "0");//成功
					order.put("IS_ON", "1");//成功
					//增加到成功用户信息表
					String orderTime = order.getString("ORDER_TIME");
					PageData saveUser = new PageData();
					saveUser.put("USER_PHONE", order.get("USER_PHONE"));
					saveUser.put("ORDER_CODE", order.get("ORDER_CODE"));
					saveUser.put("ORDER_MONTH", orderTime.substring(0, 7));
					saveUser.put("ORDER_TIME", orderTime);
					saveUser.put("ON_MONTH", 1);
					saveUser.put("IS_ON", "1");
					saveUser.put("C_ID", order.get("C_ID"));
					saveUser.put("LOG_ID", order.get("ID"));
					userInfoService.save(saveUser);
				}else {
					order.put("RESULT_CODE", "1");//失败
					order.put("IS_ON", "0");//失败
				}
				userOrderLogService.edit(order);
				result.put("code", "000000");
				result.put("description", "成功");
			}else {
				result.put("code",  "1006");
				result.put("msg", "同步失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",  "1006");
			result.put("msg", "同步失败");
			return result.toString();
		}finally {
			try {
				if(reader!=null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result.toString();
	}
	/**
	 * 同程回调
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/ydSyn", produces = "text/html;charset=utf-8")
	public @ResponseBody String ydSyn(HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		 BufferedReader reader=null;
		 PageData log = new PageData();
			log.put("C_ID", "0");
			log.put("ORDER_METHOD", "com.sinontech.controller.crud.ydSyn");
			log.put("ORDER_TIME", DateUtil.getTime());
		try {
			 String code = request.getParameter("code");
			 String mobile = request.getParameter("mobile");
			 String msg = request.getParameter("msg");
			 String boxId = request.getParameter("boxId");
			 String oid = request.getParameter("oid");
			 String transId = request.getParameter("transId");
			 PageData body = new PageData();
			 body.put("code", code);
			 body.put("mobile", mobile);
			 body.put("msg", msg);
			 body.put("boxId", boxId);
			 body.put("id", oid);
			 body.put("transId", transId);
			 log.put("ORDER_INFO", JSONObject.fromObject(body).toString());
			PageData pd = new PageData();
			pd.put("ORDER_NO", oid);
			PageData order = userOrderLogService.findByOrderNo(pd);
			if(order!=null) {
				if(code.equals("1")) {//成功
					order.put("ERROR_CODE", "0");//成功
					order.put("RESULT_CODE", "0");//成功
					order.put("IS_ON", "1");//成功
					//增加到成功用户信息表
					String orderTime = order.getString("ORDER_TIME");
					PageData saveUser = new PageData();
					saveUser.put("USER_PHONE", order.get("USER_PHONE"));
					saveUser.put("ORDER_CODE", order.get("ORDER_CODE"));
					saveUser.put("ORDER_MONTH", orderTime.substring(0, 7));
					saveUser.put("ORDER_TIME", orderTime);
					saveUser.put("ON_MONTH", 1);
					saveUser.put("IS_ON", "1");
					saveUser.put("C_ID", order.get("C_ID"));
					saveUser.put("LOG_ID", order.get("ID"));
					userInfoService.save(saveUser);
				}else {
					order.put("RESULT_CODE", "1");//失败
					order.put("IS_ON", "0");//失败
				}
				userOrderLogService.edit(order);
				result.put("code", "000000");
				result.put("msg", "成功");
				String url = order.getString("BACK_URL");
				if(StringUtils.isNotBlank(url)) {
					body.put("orderType", "1");//订购状态11
					JSONObject json = backPost(url,JSONObject.fromObject(body).toString());
//					return json.toString();
				}
			}else {
				result.put("code",  "1004");
				result.put("msg", "同步失败：oid为空或不存在");
			}
			log.put("BACK_INFO", "success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",  "1006");
			result.put("msg", "同步失败");
			log.put("BACK_INFO", "false");
		}finally {
			try {
				if(reader!=null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			userOrderLogService.addLog(log);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result.toString();
	}
	/**
	 * 移动一小时状态回调
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/statusSyn", produces = "text/html;charset=utf-8")
	public @ResponseBody String canceSyn(HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		 BufferedReader reader=null;
		 PageData log = new PageData();
			log.put("C_ID", "0");
			log.put("ORDER_METHOD", "com.sinontech.controller.crud.statusSyn");
			log.put("ORDER_TIME", DateUtil.getTime());
		try {
			 String mobile = request.getParameter("mobile");
			 String state = request.getParameter("state");
			 String boxId = request.getParameter("boxId");
			 String oid = request.getParameter("oid");
			 PageData body = new PageData();
			 body.put("mobile", mobile);
			 body.put("boxId", boxId);
			 body.put("id", oid);
			 body.put("state", state);
			 log.put("ORDER_INFO", JSONObject.fromObject(body).toString());
			PageData pd = new PageData();
			pd.put("ORDER_NO", oid);
			PageData order = userOrderLogService.findByOrderNo(pd);
			if(order!=null) {
				if(state.equals("0")) {//退订
					order.put("IS_ON", "0");//失败
					userOrderLogService.edit(order);
					
					//保存一小时退订
					PageData oneLog = new PageData();
					oneLog.put("LOG_ID", order.getString("ID"));
					oneLog.put("USER_PHONE", mobile);
					oneLog.put("ORDER_NO", oid);
					oneLog.put("CANCE_TIME", DateUtil.getTime());
					oneLog.put("ORDER_CODE", order.getString("ORDER_CODE"));
					userOrderLogService.saveOneHour(oneLog);
					
					PageData user = userInfoService.findByOrderId(order.getString("ID"));
					if(user!=null) {
						user.put("IS_ON", "0");
						user.put("UPDATE_TIME", DateUtil.getTime());
						user.put("UN_TIME", DateUtil.getTime());
						userInfoService.edit(user);
					}
					
					String url = order.getString("BACK_URL");
					if(StringUtils.isNotBlank(url)) {
						body.put("orderType", "2");//退订状态同步
						JSONObject json = backPost(url,JSONObject.fromObject(body).toString());
					}
					
				}
				
				result.put("code", "000000");
				result.put("msg", "成功");
			}else {
				result.put("code",  "1004");
				result.put("msg", "同步失败：oid为空或不存在");
			}
			log.put("BACK_INFO", "success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",  "1006");
			result.put("msg", "同步失败");
			log.put("BACK_INFO", "false");
		}finally {
			try {
				if(reader!=null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			userOrderLogService.addLog(log);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result.toString();
	}
	/**
	 * 飞鹄回调
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/fhOrderSyn", produces = "text/html;charset=utf-8")
	public @ResponseBody String fhOrderSyn(HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		 BufferedReader reader=null;
		 PageData log = new PageData();
			log.put("C_ID", "0");
			log.put("ORDER_METHOD", "com.sinontech.controller.crud.fhOrderSyn");
			log.put("ORDER_TIME", DateUtil.getTime());
		try {
			PageData data = getPageDataJson();
			String id = data.getString("id");
			String orderNo = data.getString("orderNo");
			log.put("ORDER_INFO", JSONObject.fromObject(data).toString());
			PageData pd = new PageData();
			pd.put("ORDER_NO", orderNo);
			PageData order = userOrderLogService.findByOrderNo(pd);
			if(order!=null) {
				order.put("ERROR_CODE", "0");//成功
				order.put("RESULT_CODE", "0");//成功
				order.put("IS_ON", "1");//成功
				//增加到成功用户信息表
				String orderTime = order.getString("ORDER_TIME");
				PageData saveUser = new PageData();
				saveUser.put("USER_PHONE", order.get("USER_PHONE"));
				saveUser.put("ORDER_CODE", order.get("ORDER_CODE"));
				saveUser.put("ORDER_MONTH", orderTime.substring(0, 7));
				saveUser.put("ORDER_TIME", orderTime);
				saveUser.put("ON_MONTH", 1);
				saveUser.put("IS_ON", "1");
				saveUser.put("C_ID", order.get("C_ID"));
				saveUser.put("LOG_ID", order.get("ID"));
				userInfoService.save(saveUser);
				userOrderLogService.edit(order);
				result.put("code", "000000");
				result.put("msg", "成功");
				String url = order.getString("BACK_URL");
				if(StringUtils.isNotBlank(url)) {
					data.put("code", "0");//回调成功
					JSONObject json = backPost(url,JSONObject.fromObject(data).toString());
				}
			}else {
				result.put("code",  "1004");
				result.put("msg", "同步失败：订单不存在");
			}
			log.put("BACK_INFO", "success");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",  "1006");
			result.put("msg", "同步失败");
			log.put("BACK_INFO", "false");
		}finally {
			try {
				if(reader!=null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			userOrderLogService.addLog(log);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result.toString();
	}
	/**
	 * 请求过程数据回调
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/fhStepSyn", produces = "text/html;charset=utf-8")
	public @ResponseBody String fhStepSyn(HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		 BufferedReader reader=null;
		 PageData log = new PageData();
			log.put("C_ID", "0");
			log.put("ORDER_METHOD", "com.sinontech.controller.crud.fhStepSyn");
			log.put("ORDER_TIME", DateUtil.getTime());
		try {
			PageData data = getPageDataJson();
			log.put("ORDER_INFO", JSONObject.fromObject(data).toString());
			log.put("BACK_INFO", "success");
			result.put("code", "000000");
			result.put("msg", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",  "1006");
			result.put("msg", "同步失败");
			log.put("BACK_INFO", "false");
		}finally {
			try {
				if(reader!=null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		try {
			userOrderLogService.addLog(log);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return result.toString();
	}
	/**
	 * 存量报俊回调
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/orderSyn", produces = "text/html;charset=utf-8")
	public @ResponseBody String orderSyn(HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		JSONObject resultHeader = new JSONObject();
		 BufferedReader reader=null;
		try {
			PageData data = getPageDataJson();
			logger.info("b2l回调参数:"+data.toString());
			JSONObject json = JSONObject.fromObject(data);
			JSONObject header = json.getJSONObject("head");
			String srcsysid = header.getString("srcsysid");
			String clientip = header.getString("clientip");
			String inputtime = header.getString("inputtime");
			String sign = header.getString("sign");
			String mySign = MD5.md5(srcsysid+clientip+inputtime+B2lInterface.KEY);
			if(!mySign.equals(sign)) {
				resultHeader.put("result_code",  "-1");
				resultHeader.put("result_msg", "签名校验失败");
			}else {
				resultHeader.put("result_code",  "0");
				resultHeader.put("result_msg", "成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultHeader.put("result_code",  "-1");
			resultHeader.put("result_msg", "签名校验失败");
		}finally {
			try {
				if(reader!=null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		result.put("head", resultHeader);
		logger.info("b2l回调返回:"+result.toString());
		return result.toString();
	}
	/**
	 * 复杂加包 订单校验结果报竣回调
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/orderComplexSyn", produces = "text/html;charset=utf-8")
	public @ResponseBody String orderComplexSyn(HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		JSONObject resultHeader = new JSONObject();
		 BufferedReader reader=null;
		try {
			PageData data = getPageDataJson();
			logger.info("b2l复杂加包回调参数:"+data.toString());
			JSONObject json = JSONObject.fromObject(data);
			JSONObject header = json.getJSONObject("head");
			String srcsysid = header.getString("srcsysid");
			String clientip = header.getString("clientip");
			String inputtime = header.getString("inputtime");
			String sign = header.getString("sign");
			String mySign = MD5.md5(srcsysid+clientip+inputtime+B2lInterface.KEY);
			if(!mySign.equals(sign)) {
				resultHeader.put("result_code",  "-1");
				resultHeader.put("result_msg", "签名校验失败");
			}else {
				JSONObject body = json.getJSONObject("body");
				//是否成功创建订单（-1 失败，0成功）
				String status = body.getString("status");
				String outerOrderNo = body.getString("outerOrderNo");
				PageData queryOrder = new PageData();
				queryOrder.put("ORDER_NO", outerOrderNo);
				PageData order = userOrderLogService.findByOrderNo(queryOrder);
				if(status.equals("0")) {
					String yxOrderNo = body.getString("yxOrderNo");
					PageData zjOrder = new PageData();
					zjOrder.put("ORDER_NO", outerOrderNo);
					zjOrder.put("YX_ORDER_NO", yxOrderNo);
					zjOrder.put("ADD_TIME", DateUtil.getTime());
					userOrderLogService.saveZjOrder(zjOrder);
					order.put("ERROR_CODE", "0");
				}else {
					String msg = body.getString("msg");
					JSONObject returnResult = new JSONObject();
					returnResult.put("code", "-1");
					returnResult.put("msg", msg);
					order.put("ERROR_CODE", status);
					order.put("ORDER_RESULT", returnResult.toString());
				}
				userOrderLogService.edit(order);
				resultHeader.put("result_code",  "0");
				resultHeader.put("result_msg", "成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultHeader.put("result_code",  "-1");
			resultHeader.put("result_msg", "签名校验失败");
		}finally {
			try {
				if(reader!=null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		result.put("head", resultHeader);
		logger.info("b2l复杂加包回调返回:"+result.toString());
		return result.toString();
	}
	
	/**
	 * 湖南企业视频彩铃受理回调
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/hnStatusSyn", produces = "text/html;charset=utf-8")
	public @ResponseBody String hnStatusSyn(HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		try {
			logger.info("湖南企业视频彩铃回调参数:");
			JSONObject data = new JSONObject();
			
			String timestamp = request.getParameter("timestamp");
			String sign = request.getParameter("sign");
			String accNum = request.getParameter("accNum");
			String orderNo = request.getParameter("orderNo");
			String orderState = request.getParameter("orderState"); //状态 1订购成功  2订购失败
			String orderTime = request.getParameter("orderTime"); 
			String orderMsg = request.getParameter("orderMsg");
			
			data.put("timestamp", timestamp);
			data.put("accNum", accNum);
			data.put("orderNo", orderNo);
			data.put("orderState", orderState);
			data.put("orderTime", orderTime);
			data.put("orderMsg", orderMsg);
			
			String signStr = ShopInterface.NAME+accNum+timestamp+ShopInterface.KEY;
			String mySign = MD5.md5(signStr).toUpperCase();
			if(sign.equals(mySign)) {
				PageData pd = new PageData();
				pd.put("ORDER_NO", orderNo);
				PageData order = userOrderLogService.findByOrderNo(pd);
				if(order!=null) {
					if(orderState.equals("1")) {//成功
						order.put("RESULT_CODE", "0");//成功
						order.put("IS_ON", "1");//成功
						//增加到成功用户信息表
						String orderLogTime = order.getString("ORDER_TIME");
						PageData saveUser = new PageData();
						saveUser.put("USER_PHONE", order.get("USER_PHONE"));
						saveUser.put("ORDER_CODE", order.get("ORDER_CODE"));
						saveUser.put("ORDER_MONTH", orderLogTime.substring(0, 7));
						saveUser.put("ORDER_TIME", orderLogTime);
						saveUser.put("ON_MONTH", 1);
						saveUser.put("IS_ON", "1");
						saveUser.put("C_ID", order.get("C_ID"));
						saveUser.put("LOG_ID", order.get("ID"));
						userInfoService.save(saveUser);
					}else {
						order.put("RESULT_CODE", "1");//失败
						order.put("IS_ON", "0");//失败
					}
					userOrderLogService.edit(order);
					result.put("code", "0000");
					result.put("msg", "回调成功");
					String url = order.getString("BACK_URL");
					if(StringUtils.isNotBlank(url)) {
						JSONObject json = backPost(url,data.toString());
//						return json.toString();
					}
				}else {
					result.put("code",  "1006");
					result.put("msg", "同步失败");
				}
			}else {
				result.put("code",  "1006");
				result.put("msg", "同步失败-签名错误");
			}
			PageData log = new PageData();
			log.put("C_ID", "0");
			log.put("ORDER_METHOD", "com.sinontech.controller.crud.hnStatusSyn");
			log.put("ORDER_TIME", DateUtil.getTime());
			log.put("ORDER_INFO", JSONObject.fromObject(data).toString());
			log.put("BACK_INFO", "success");
			userOrderLogService.addLog(log);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",  "1006");
			result.put("msg", "同步失败");
			return result.toString();
		}
		return result.toString();
	}
	/**
	 * 用户是否回填验证码回调
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="/submitSyn", produces = "text/html;charset=utf-8")
	public @ResponseBody String submitSyn(HttpServletRequest request,HttpServletResponse response) {
		JSONObject result = new JSONObject();
		 BufferedReader reader=null;
		try {
			PageData data = getPageDataJson();
			String orderNo = data.getString("orderNo");
			String submitTime = data.getString("submitTime");
			PageData log = new PageData();
			log.put("ORDER_NO", orderNo);
			log.put("SUBMIT_TIME", submitTime);
			log.put("IS_SUBMIT", "1");
			log.put("ADD_TIME", DateUtil.getTime());
			userSubmitService.save(log);
			result.put("code", "000000");
			result.put("msg", "成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("code",  "1006");
			result.put("msg", "同步失败");
		}finally {
			try {
				if(reader!=null) {
					reader.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return result.toString();
	}
	private JSONObject backPost(String backUrl,String query) {
		return CommonHttpUtils.postMethod(backUrl, query);
	}
	/**
	 * 前台提示
	 * Description: 
	 * @Version2.0 2019-10-12 下午3:00:18 by 谷超（guchao@sinon.com）创建
	 * @param request
	 * @param response
	 * @return 
	 * String
	 */
	@RequestMapping("message")
	public @ResponseBody String message(HttpServletRequest request,HttpServletResponse response) {
		String message = (String)request.getAttribute("message");
		logger.info(message);
		return message;
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(format,true));
	}
}
