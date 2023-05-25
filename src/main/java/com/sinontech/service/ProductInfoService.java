package com.sinontech.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;
import com.sinontech.dao.DaoSupport;
import com.sinontech.listen.AppContextBean;
import com.sinontech.service.inter.OrderService;
import com.sinontech.tools.b2l.B2lInterface;
import com.sinontech.tools.common.DateUtil;
import com.sinontech.tools.common.Page;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.common.UuidUtil;
import com.sinontech.tools.common.XxlUtils;
import com.sinontech.tools.flyh.FhInterface;
import com.sinontech.tools.hn.HnVrbtInterface;
import com.sinontech.tools.hn.ShopInterface;
import com.sinontech.tools.imusic.Configuration;
import com.sinontech.tools.imusic.VolteInterface;
import com.sinontech.tools.isale.IsaleInterface;
import com.sinontech.tools.wrbt.WReadInterface;
import com.sinontech.tools.wrbt.WrbtCInterface;
import com.sinontech.tools.yd.XjYdInterface;
import com.sinontech.tools.yd.YdInterface;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Service("productInfoService")
public class ProductInfoService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	
	@Resource(name="smsSendService")
	private SmsSendService smsSendService;
	
	@Resource(name = "userOrderLogService")
	private UserOrderLogService userOrderLogService;
	
	@Resource(name = "applicationSalesService")
	private ApplicationSalesService applicationSalesService;
	
	@Resource(name = "prosaleService")
	private ProsaleService prosaleService;
	
	
	@Resource(name = "blackListService")
	private BlackListService blackListService;
	
	@Autowired
	private RedisService redisService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private VipInfoService vipInfoService;
	
	/**
	 * 发送订购短信
	 * @param phone
	 * @param productCode
	 * @return
	 * @throws Exception 
	 */
	public JSONObject sendOrderSms(PageData data) throws Exception {
		JSONObject result = new JSONObject();
		String cooperateId = data.getString("cooperateId");
		String productCode = data.getString("productCode");
		String phone = data.getString("phone");
		PageData productInfo = this.findByProductCode(productCode);
		Long useTime = 0L;
		if(productInfo!=null) {
			String phoneProvince = "";
			String phoneCity = "";
			String ptype = "";
			String areaResult = XxlUtils.getAreaByPhone(phone);
			JSONObject phoneJson = JSONObject.fromObject(areaResult);
			String pcode = phoneJson.getString("status");
			if(pcode.equals("200")) {
				JSONObject payload = phoneJson.getJSONObject("payload");
				String system_code = payload.getString("system_code");
				if(system_code.equals("0")) {
					phoneProvince = payload.getString("provinceName");
					phoneCity = payload.getString("cityName");
					ptype = payload.getString("brand");//1联通，2移动3电信
				}
			}
			if(StringUtils.isBlank(phoneProvince)) {
				result.put("code", "0002");
				result.put("msg", "号码归属地查询失败");
				PageData log = new PageData();
				log.put("C_ID", data.getString("cooperateId"));
				log.put("ORDER_METHOD", "com.sinontech.service.crbt.sendOrderSms");
				log.put("ORDER_INFO", JSONObject.fromObject(data).toString());
				log.put("ORDER_TIME", DateUtil.getTime());
				log.put("BACK_INFO", result.toString());
				addLog(log);
				return result;
			}
			if(!checkIfCan(phoneProvince,productInfo.getString("SALES_CODE"),phoneCity,phone)) {
				result.put("code", "1101");
				result.put("msg", "该省不允许办理");
				PageData log = new PageData();
				log.put("C_ID", data.getString("cooperateId"));
				log.put("ORDER_METHOD", "com.sinontech.service.crbt.sendOrderSms");
				log.put("ORDER_INFO", JSONObject.fromObject(data).toString());
				log.put("ORDER_TIME", DateUtil.getTime());
				log.put("BACK_INFO", result.toString());
				addLog(log);
				return result;
			}
			PageData que = new PageData();
			que.put("phone", phone);
			if(blackListService.queryExist(que)) {
				result.put("code", "1102");
				result.put("msg", "黑名单用户不能办理");
				PageData log = new PageData();
				log.put("C_ID", data.getString("cooperateId"));
				log.put("ORDER_METHOD", "com.sinontech.service.crbt.sendOrderSms");
				log.put("ORDER_INFO", JSONObject.fromObject(data).toString());
				log.put("ORDER_TIME", DateUtil.getTime());
				log.put("BACK_INFO", result.toString());
				addLog(log);
				return result;
			}
			data.put("provinceName", phoneProvince);
			data.put("cityName", phoneCity);
			Long start = System.currentTimeMillis();
			Long end = System.currentTimeMillis();
			
			OrderService service = AppContextBean.getBean(productInfo.getString("IMPL_NAME"), OrderService.class);
			result = service.sendOrderSms(data, productInfo);
			end = System.currentTimeMillis();
			useTime = (end - start)/1000;
		}else {
			result.put("code", "0001");
			result.put("msg", "产品不存在");
		}
		PageData log = new PageData();
		log.put("C_ID", cooperateId);
		log.put("ORDER_METHOD", "com.sinontech.service.crbt.sendOrderSms");
		log.put("ORDER_INFO", JSONObject.fromObject(data).toString());
		log.put("ORDER_TIME", DateUtil.getTime());
		log.put("BACK_INFO", result.toString());
		log.put("USE_TIME", useTime+"");
		addLog(log);
		return result;
	}
	/**
	 * 订购产品
	 * @param phone
	 * @param productCode
	 * @param smsCode
	 * @return
	 * @throws Exception 
	 */
	public JSONObject orderProduct(PageData pd) throws Exception {
		JSONObject result = new JSONObject();
		String cooperateId = pd.getString("cooperateId");
		String phone = pd.getString("phone");
		String productCode = pd.getString("productCode");
		PageData orderLog = new PageData();
		PageData productInfo = this.findByProductCode(productCode);
		Long useTime = 0L;
		if(productInfo!=null) {
			String phoneProvince = "";
			String phoneCity = "";
			String areaResult = XxlUtils.getAreaByPhone(phone);
			JSONObject phoneJson = JSONObject.fromObject(areaResult);
			String pcode = phoneJson.getString("status");
			if(pcode.equals("200")) {
				JSONObject payload = phoneJson.getJSONObject("payload");
				String system_code = payload.getString("system_code");
				if(system_code.equals("0")) {
					phoneProvince = payload.getString("provinceName");
					phoneCity = payload.getString("cityName");
//					ptype = payload.getString("brand");//1联通，2移动3电信
				}
			}
			if(StringUtils.isBlank(phoneProvince)) {
				result.put("code", "0002");
				result.put("msg", "号码归属地查询失败");
				PageData log = new PageData();
				log.put("C_ID", cooperateId);
				log.put("ORDER_METHOD", "com.sinontech.service.crbt.orderProduct");
				log.put("ORDER_INFO", JSONObject.fromObject(pd).toString());
				log.put("ORDER_TIME", DateUtil.getTime());
				log.put("BACK_INFO", result.toString());
				addLog(log);
				return result;
			}
			if(!checkIfCan(phoneProvince,productInfo.getString("SALES_CODE"),phoneCity,phone)) {
				result.put("code", "1101");
				result.put("msg", "该省不允许办理");
				PageData log = new PageData();
				log.put("C_ID", cooperateId);
				log.put("ORDER_METHOD", "com.sinontech.service.crbt.orderProduct");
				log.put("ORDER_INFO", JSONObject.fromObject(pd).toString());
				log.put("ORDER_TIME", DateUtil.getTime());
				log.put("BACK_INFO", result.toString());
				addLog(log);
				return result;
			}
			PageData que = new PageData();
			que.put("phone", phone);
			if(blackListService.queryExist(que)) {
				result.put("code", "1102");
				result.put("msg", "黑名单用户不能办理");
				PageData log = new PageData();
				log.put("C_ID", cooperateId);
				log.put("ORDER_METHOD", "com.sinontech.service.crbt.orderProduct");
				log.put("ORDER_INFO", JSONObject.fromObject(pd).toString());
				log.put("ORDER_TIME", DateUtil.getTime());
				log.put("BACK_INFO", result.toString());
				addLog(log);
				return result;
			}
			Long start = System.currentTimeMillis();
			Long end = System.currentTimeMillis();
			orderLog.put("ORDER_CODE", productInfo.getString("SALES_CODE"));
			pd.put("phoneProvince", phoneProvince);
			pd.put("phoneCity", phoneCity);
			OrderService service = AppContextBean.getBean(productInfo.getString("IMPL_NAME"), OrderService.class);
			result = service.orderProduct(pd, productInfo, orderLog);
			end = System.currentTimeMillis();
			useTime = (end - start)/1000;
		}else {
			result.put("code", "0001");
			result.put("msg", "产品不存在");
		}
		orderLog.put("C_ID", cooperateId);
		orderLog.put("USER_PHONE", phone);
		
		orderLog.put("ORDER_TIME", DateUtil.getTime());
		orderLog.put("ORDER_RESULT", result.toString());
		orderLog.put("ERROR_CODE", result.get("code"));
		userOrderLogService.save(orderLog);
		if(orderLog.containsKey("IS_ON")&&orderLog.getString("IS_ON").equals("1")) {
			// 增加到成功用户信息表
			String orderTime = orderLog.getString("ORDER_TIME");
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
		PageData log = new PageData();
		log.put("C_ID", cooperateId);
		log.put("ORDER_METHOD", "com.sinontech.service.crbt.orderProduct");
		log.put("ORDER_INFO", JSONObject.fromObject(pd).toString());
		log.put("ORDER_TIME", DateUtil.getTime());
		log.put("BACK_INFO", result.toString());
		log.put("USE_TIME", useTime+"");
		addLog(log);
		return result;
	}
	/**
	 * 查询订单状态
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public JSONObject queryOrderStatus(PageData pd) throws Exception {
		JSONObject result = new JSONObject();
		String productCode = pd.getString("productCode");
		PageData productInfo = this.findByProductCode(productCode);
		if(productInfo!=null) {
			pd.put("SALES_ID", productInfo.getString("SALES_ID"));
			OrderService service = AppContextBean.getBean(productInfo.getString("IMPL_NAME"), OrderService.class);
			result = service.queryOrderStatus(pd);
		}else {
			result.put("code", "0001");
			result.put("msg", "产品不存在");
		}
		return result;
	}
	
	/**
	 * 查询用户订购包
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public JSONObject queryUserPackage(PageData pd) throws Exception {
		JSONObject result = new JSONObject();
		String productCode = pd.getString("productCode");
		PageData productInfo = this.findByProductCode(productCode);
		if(productInfo!=null) {
			OrderService service = AppContextBean.getBean(productInfo.getString("IMPL_NAME"), OrderService.class);
			result = service.queryUserPackage(pd, productInfo);
		}else {
			result.put("code", "0001");
			result.put("msg", "产品不存在");
		}
		return result;
	}
	/**
	 * 同步移动订购记录
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public JSONObject synMobileLog(PageData data) throws Exception {
		JSONObject returnResult = new JSONObject();
		
		JSONObject openResult = new JSONObject();
		JSONObject setResult = new JSONObject();
		String cooperateId = data.getString("cooperateId");
		//开户号码
		String mobile = data.getString("mobile");
		//订阅包id
		String boxId = data.getString("boxId");
		//设置视频内容id
		String contentId = data.getString("contentId");
		//价格
		String price = data.getString("price");
		//订购状态码：000000：成功，其他失败
		String openStatus = data.getString("openStatus");
		//订购状态描述
		String openMessage = data.getString("openMessage");
		
		openResult.put("code", openStatus);
		openResult.put("msg", openMessage);
		
		//订购时间
		String openTime = data.getString("openTime");
		//设置视频状态码：000000：成功，其他失败
		String setStatus = data.getString("setStatus");
		//设置视频状态描述
		String setMessage = data.getString("setMessage");
		
		setResult.put("code", setStatus);
		setResult.put("msg", setMessage);
		setResult.put("contentId", contentId);
		setResult.put("price", price);
		
		//保存订购日志
		PageData orderLog = new PageData();
		orderLog.put("ORDER_TYPE", "8");//移动订购
		orderLog.put("ORDER_CODE", boxId);
		orderLog.put("C_ID", cooperateId);
		orderLog.put("USER_PHONE", mobile);
		orderLog.put("ORDER_TIME", DateUtil.getTime());
		orderLog.put("ORDER_RESULT", openResult.toString());
		orderLog.put("ERROR_CODE", openResult.get("code"));
		if(openStatus.equals("000000")) {
			orderLog.put("RESULT_CODE", "0");//成功
			orderLog.put("IS_ON", "1");//成功
			orderLog.put("ERROR_CODE", "0");
		}else {
			orderLog.put("RESULT_CODE", "1");//失败
			orderLog.put("IS_ON", "0");//失败
			orderLog.put("ERROR_CODE", openResult.get("code"));
		}
		userOrderLogService.save(orderLog);
		if(openStatus.equals("000000")) {
			PageData saveUser = new PageData();
			saveUser.put("USER_PHONE", orderLog.get("USER_PHONE"));
			saveUser.put("ORDER_CODE", orderLog.get("ORDER_CODE"));
			saveUser.put("ORDER_MONTH", openTime.substring(0, 7));
			saveUser.put("ORDER_TIME", openTime);
			saveUser.put("ON_MONTH", 1);
			saveUser.put("IS_ON", "1");
			saveUser.put("C_ID", orderLog.get("C_ID"));
			saveUser.put("LOG_ID", orderLog.get("ID"));
			userInfoService.save(saveUser);
		}
		
		PageData log = new PageData();
		log.put("C_ID", cooperateId);
		log.put("ORDER_METHOD", "com.sinontech.service.crbt.orderProduct");
		log.put("ORDER_INFO", JSONObject.fromObject(data).toString());
		log.put("ORDER_TIME", openTime);
		log.put("BACK_INFO", openResult.toString());
		addLog(log);
		if(!StringUtils.isBlank(setStatus)) {
			PageData setlog = new PageData();
			setlog.put("C_ID", cooperateId);
			setlog.put("ORDER_METHOD", "com.sinontech.service.crbt.ydset");
			setlog.put("ORDER_INFO", JSONObject.fromObject(data).toString());
			setlog.put("ORDER_TIME", openTime);
			setlog.put("BACK_INFO", setResult.toString());
			addLog(setlog);
		}
		returnResult.put("code", "0");
		returnResult.put("msg","成功");
		return returnResult;
	}
	
	/**
	 * 检查屏蔽省份
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public JSONObject checkProvince(PageData data) throws Exception {
		JSONObject result = new JSONObject();
		String productCode = data.getString("productCode");
		String phone = data.getString("phone");
		PageData productInfo = this.findByProductCode(productCode);
		if(productInfo!=null) {
			String phoneProvince = "";
			String phoneCity = "";
			String areaResult = XxlUtils.getAreaByPhone(phone);
			JSONObject phoneJson = JSONObject.fromObject(areaResult);
			String pcode = phoneJson.getString("status");
			if(pcode.equals("200")) {
				JSONObject payload = phoneJson.getJSONObject("payload");
				String system_code = payload.getString("system_code");
				if(system_code.equals("0")) {
					phoneProvince = payload.getString("provinceName");
					phoneCity = payload.getString("cityName");
				}else {
					result.put("code", "0002");
					result.put("msg", "号码归属地查询失败");
				}
			}else {
				result.put("code", "0002");
				result.put("msg", "号码归属地查询失败");
			}
			if(StringUtils.isBlank(phoneProvince)) {
				result.put("code", "0002");
				result.put("msg", "号码归属地查询失败");
				return result;
			}
			if(!checkIfCan(phoneProvince,productInfo.getString("SALES_CODE"),phoneCity,phone)) {
				result.put("code", "1101");
				result.put("msg", "该省不允许办理");
			}else {
				result.put("code", "0");
				result.put("msg", "省份未屏蔽");
			}
		}else {
			result.put("code", "0001");
			result.put("msg", "产品不存在");
		}
		return result;
	}
	/**
	 * 检查号码是否可以订购
	 * @param phone
	 * @param salesCode
	 * @return
	 */
	public boolean checkIfCan(String phoneProvince,String salesCode,String city,String phone) {
		PageData query = new PageData();
		query.put("salesCode", salesCode);
		query.put("vipInfo", phone);
		boolean phoneResult = this.vipInfoService.chekIfVip(query);
		query.put("vipInfo", city);
		boolean cityResult = this.vipInfoService.chekIfVip(query);
		boolean closeResult = prosaleService.queryStatusByCondition(phoneProvince, salesCode);
		return phoneResult||cityResult||closeResult;
	}
	/**
	 * 检测优享会员是否可以订购
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public JSONObject checkYX(PageData data) throws Exception {
		JSONObject result = new JSONObject();
		String productCode = data.getString("productCode");
		String phone = data.getString("phone");
		PageData productInfo = this.findByProductCode(productCode);
		if(productInfo!=null) {
			result.put("code", "0002");
			result.put("msg", "无法受理");
			String evtCollectTime = DateUtil.getTime();
			String requestInfo = productInfo.getString("REQUEST_INFO");
			JSONObject requestJson = JSONObject.fromObject(requestInfo);
			String salerNo = requestJson.getString("salerNo");
			String ruleIds = requestJson.getString("ruleId");
			String activityCode = requestJson.getString("ACTIVITY_CODE");
			String resultStr = B2lInterface.syncCollectContactInfoToAgent(phone, salerNo, evtCollectTime,activityCode);
			JSONObject resultJson = JSONObject.fromObject(resultStr);
			String code = resultJson.getString("code");
			if(code.equals("0000")) {
				JSONObject orderdata = resultJson.getJSONObject("data");
				String orderCode = orderdata.getString("code");
				JSONArray orderlist = orderdata.getJSONArray("orderList");
				if(orderCode.equals("1")&&orderlist.size()>0) {
					boolean ifcan = false;
					for(int i=0;i<orderlist.size();i++) {
						JSONObject order = orderlist.getJSONObject(i);
						String ruleId = order.getString("ruleId");
						if(ruleIds.contains(ruleId)) {
							ifcan = true;
						}
					}
					if(ifcan) {
						result.put("code", "0");
						result.put("msg", "可以受理");
					}
				}
			}
		}else {
			result.put("code", "0001");
			result.put("msg", "产品不存在");
		}
		return result;
	}
	
	/**
	 * 业务退订（沃音乐和爱音乐）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public JSONObject canceProduct(PageData pd) throws Exception {
		JSONObject result = new JSONObject();
		String cooperateId = pd.getString("cooperateId");
		String phone = pd.getString("phone");
		String productCode = pd.getString("productCode");
		PageData productInfo = this.findByProductCode(productCode);
		Long useTime = 0L;
		if(productInfo!=null) {
			Long start = System.currentTimeMillis();
			Long end = System.currentTimeMillis();
			String productType = productInfo.getString("PRODUCT_TYPE");
			String salesCode = productInfo.getString("SALES_CODE");
			//浙江isale心动会员订购
			if(productType.equals("4")||productType.equals("9")) {//订购电信产品
				if(salesCode.indexOf("_")>-1) {
					salesCode = salesCode.split("_")[1];
				}
				String salesId = productInfo.getString("SALES_ID");
				PageData header = applicationSalesService.getSaleHeader(salesId);
				String openOrderLanched = VolteInterface.unsubscribebyemp(phone, salesCode,header);
				JSONObject json = JSONObject.fromObject(openOrderLanched);
				JSONObject response = json.getJSONObject("BasicJTResponse");
				result.put("code", response.get("res_code"));
				result.put("msg", response.get("res_message"));
			}else if(productType.equals("5")) {//订购沃音乐产品
				String openOrderLanched = WrbtCInterface.unSubProductNoToken(phone,salesCode);
				JSONObject resulJson = JSONObject.fromObject(openOrderLanched);
				String resultCode = resulJson.getString("returnCode");
				result.put("msg", resulJson.get("description"));
				if(resultCode.equals("000000")) {
					result.put("code", "0");
					result.put("msg", "处理成功");
				}else {
					result.put("code", resulJson.get("returnCode"));
				}
			}else {
				result.put("code", "0001");
				result.put("msg", "产品不存在");
			}
			end = System.currentTimeMillis();
			useTime = (end - start)/1000;
		}else {
			result.put("code", "0001");
			result.put("msg", "产品不存在");
		}
		PageData log = new PageData();
		log.put("C_ID", StringUtils.isBlank(cooperateId)?"0":cooperateId);
		log.put("ORDER_METHOD", "com.sinontech.service.crbt.canceProduct");
		log.put("ORDER_INFO", JSONObject.fromObject(pd).toString());
		log.put("ORDER_TIME", DateUtil.getTime());
		log.put("BACK_INFO", result.toString());
		log.put("USE_TIME", useTime+"");
		addLog(log);
		return result;
	}
	/**
	 * 获取用户视频库（沃音乐和爱音乐）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public JSONObject queryUserVideo(PageData pd) throws Exception {
		JSONObject result = new JSONObject();
		String phone = pd.getString("phone");
		String phoneStr = XxlUtils.getAreaByPhone(phone);
		JSONObject phoneJson = JSONObject.fromObject(phoneStr);
		String code = phoneJson.getString("status");
		if(code.equals("200")) {
			JSONObject payload = phoneJson.getJSONObject("payload");
			String ptype = payload.getString("brand");//1联通，2移动3电信
			//浙江isale心动会员订购
			if(ptype.equals("3")) {//订购电信产品
				List<PageData> ringLists = new ArrayList<PageData>();
				String salesId = "20";
				PageData header = applicationSalesService.getSaleHeader(salesId);
				String ringResult = VolteInterface.browsepersonallib(phone, "1", "1", "100", header);
				JSONObject json = JSONObject.fromObject(ringResult);
				JSONObject returnJson = new JSONObject();
				returnJson.put("code","0");
				returnJson.put("msg", "成功");
				if(json.containsKey("toneList")) {
					JSONArray list = json.getJSONArray("toneList");
					if(list!=null&&list.size()>0) {
						for(int i=0;i<list.size();i++) {
							json = list.getJSONObject(i);
							String ringId = json.getString("ringId");
							PageData a = new PageData();
							a.put("ringId", ringId);
							a.put("ringName", json.getString("name"));
							a.put("settingFlag", "");
							ringLists.add(a);
						}
					}
					returnJson.put("tones", ringLists);
				}
			}else if(ptype.equals("1")) {//订购沃音乐产品
				List<PageData> ringLists = new ArrayList<PageData>();
				String ringResult = WrbtCInterface.getOrderList(phone,"1");
				JSONObject resulJson = JSONObject.fromObject(ringResult);
				JSONObject returnJson = new JSONObject();
				returnJson.put("code","0");
				returnJson.put("msg", "成功");
				String resultCode = resulJson.getString("returnCode");
				if(resultCode.equals("0000")) {
					JSONArray ringArray = resulJson.getJSONArray("userDepotList");
					if(ringArray!=null&&ringArray.size()>0) {
						for(int i =0 ;i<ringArray.size();i++) {
							JSONObject ringjson = ringArray.getJSONObject(i);
							PageData a = new PageData();
							a.put("ringId", ringjson.getString("ringId"));
							a.put("ringName", ringjson.getString("ringName"));
							a.put("settingFlag", ringjson.getString("settingFlag"));
							ringLists.add(a);
						}
					}
					returnJson.put("tones", ringLists);
				}
			}else {
				result.put("code", "0001");
				result.put("msg", "产品不存在");
			}
		}else {
			result.put("code", "0001");
			result.put("msg", "产品不存在");
		}
		return result;
	}
	/**
	 * 免费权益订购视频（爱音乐和沃音乐）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public JSONObject orderVideo(PageData pd) throws Exception {
		JSONObject result = new JSONObject();
		String phone = pd.getString("phone");
		String videoId = pd.getString("toneCode");
		String productCode = pd.getString("productCode");
		PageData productInfo = this.findByProductCode(productCode);
		Long useTime = 0L;
		if(productInfo!=null) {
			String productType = productInfo.getString("PRODUCT_TYPE");
			if(productType.equals("4")||productType.equals("9")) {//订购电信产品
				String salesId = productInfo.getString("SALES_ID");
				PageData header = applicationSalesService.getSaleHeader(salesId);
				if(productInfo.getString("SALES_CODE").equals("135999999999999000271")) {
					header.put("channelId", Configuration.YHY_CHANNELID);
				}
				if(productInfo.containsKey("REQUEST_INFO")&&StringUtil.isNotEmpty(productInfo.getString("REQUEST_INFO"))) {
					String requestInfo = productInfo.getString("REQUEST_INFO");
					JSONObject requestJson = JSONObject.fromObject(requestInfo);
					if(requestJson.containsKey("channelId")) {
						header.put("channelId", requestJson.getString("channelId"));
					}
				}
				String orderResult = VolteInterface.addtonefreeonproduct(phone, videoId,header);
				JSONObject json = JSONObject.fromObject(orderResult);
				String resCode = json.getString("res_code");
				if(resCode.equals("0000")||resCode.equals("000000")) {
					resCode = "0";
				}
				result.put("code", resCode);
				result.put("msg", json.get("res_message"));
				result.put("packageId", json.get("packageId"));
			}else if(productType.equals("5")) {//订购沃音乐产品
				String orderResult = WrbtCInterface.orderRingOnePointMon(phone,videoId);
				JSONObject resulJson = JSONObject.fromObject(orderResult);
				String resultCode = resulJson.getString("returnCode");
				result.put("msg", resulJson.get("description"));
				if(resultCode.equals("0000")) {
					result.put("code", "0");
				}else {
					result.put("code", resulJson.get("returnCode"));
				}
			}else {
				result.put("code", "0001");
				result.put("msg", "产品不存在");
			}
		}else {
			result.put("code", "0001");
			result.put("msg", "产品不存在");
		}
		PageData log = new PageData();
		log.put("C_ID", pd.getString("cooperateId"));
		log.put("ORDER_METHOD", "com.sinontech.service.crbt.addToneFreeOnProduct");
		log.put("ORDER_INFO", JSONObject.fromObject(pd).toString());
		log.put("ORDER_TIME", DateUtil.getTime());
		log.put("BACK_INFO", result.toString());
		addLog(log);
		return result;
	}
	/**
	 * 删除订购视频（爱音乐和沃音乐）
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public JSONObject delVideo(PageData pd) throws Exception {
		JSONObject result = new JSONObject();
		String phone = pd.getString("phone");
		String videoId = pd.getString("toneCode");
		String productCode = pd.getString("productCode");
		PageData productInfo = this.findByProductCode(productCode);
		if(productInfo!=null) {
			String productType = productInfo.getString("PRODUCT_TYPE");
			if(productType.equals("4")||productType.equals("9")) {//订购电信产品
				String salesId = productInfo.getString("SALES_ID");
				PageData header = applicationSalesService.getSaleHeader(salesId);
				String orderResult = VolteInterface.delring(phone, videoId,header);
				JSONObject json = JSONObject.fromObject(orderResult);
				String resCode = json.getString("res_code");
				if(resCode.equals("0000")||resCode.equals("000000")) {
					resCode = "0";
				}
				result.put("code", resCode);
				result.put("msg", json.get("res_message"));
			}else if(productType.equals("5")) {//订购沃音乐产品
				String orderResult = WrbtCInterface.delOrder(phone,videoId);
				JSONObject resulJson = JSONObject.fromObject(orderResult);
				String resultCode = resulJson.getString("returnCode");
				result.put("msg", resulJson.get("description"));
				if(resultCode.equals("0000")) {
					result.put("code", "0");
				}else {
					result.put("code", resulJson.get("returnCode"));
				}
			}else {
				result.put("code", "0001");
				result.put("msg", "产品不存在");
			}
		}else {
			result.put("code", "0001");
			result.put("msg", "产品不存在");
		}
		return result;
	}
	/**
	 * 增加视频设置（爱音乐）
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public JSONObject addRingset(PageData data) throws Exception {
		JSONObject returnJson = new JSONObject();
		String userPhone = data.getString("phone");
		String toneCode = data.getString("toneCode");
		String setType = data.getString("setType");
		String callerGroupId = "";
		if("2".equals(setType)) {
			if(data.containsKey("callerGroupId")) {
				callerGroupId = data.getString("callerGroupId");
			}else {
				returnJson.put("code", "9999");
				returnJson.put("msg", "必填参数不能为空");
			}
		}
		String startTime = "";
		String endTime = "";
		String timeType = data.getString("timeType");
		if("2".equals(timeType)) {
			if(data.containsKey("startTime")&&data.containsKey("endTime")) {
				startTime = data.getString("startTime");
				endTime = data.getString("endTime");
			}else {
				returnJson.put("code", "9999");
				returnJson.put("msg", "必填参数不能为空");
			}
		}
		String productCode = data.getString("productCode");
		PageData productInfo = this.findByProductCode(productCode);
		if(productInfo!=null) {
			String productType = productInfo.getString("PRODUCT_TYPE");
			if(productType.equals("4")||productType.equals("9")) {//订购电信产品
				String salesId = productInfo.getString("SALES_ID");
				PageData header = applicationSalesService.getSaleHeader(salesId);
				String result = VolteInterface.addringset(userPhone, setType, callerGroupId, toneCode, timeType, startTime, endTime, header);
				JSONObject json = JSONObject.fromObject(result);
				String code = json.getString("res_code");
				if(code.equals("0000")||code.equals("000000")) {
					code = "0";
				}
				returnJson.put("code", code);
				returnJson.put("msg", json.get("res_message"));
				returnJson.put("settingId", json.get("settingId"));
			}
		}else {
			returnJson.put("code", "0001");
			returnJson.put("msg", "产品不存在");
		}
		return returnJson;
	}
	/*
	* 新增
	*/
	public void save(PageData pd)throws Exception{
		dao.save("ProductInfoMapper.save", pd);
	}
	
	/*
	* 删除
	*/
	public void delete(PageData pd)throws Exception{
		dao.delete("ProductInfoMapper.delete", pd);
	}
	
	/*
	* 修改
	*/
	public void edit(PageData pd)throws Exception{
		dao.update("ProductInfoMapper.edit", pd);
	}
	
	/*
	*列表
	*/
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("ProductInfoMapper.datalistPage", page);
	}
	
	/*
	*列表(全部)
	*/
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ProductInfoMapper.listAll", pd);
	}
	
	public PageData findByProductCode(String productCode)throws Exception{
		PageData pd = new PageData();
		pd.put("PRODUCT_CODE", productCode);
		return (PageData)dao.findForObject("ProductInfoMapper.findByProductCode", pd);
	}
	
	/*
	* 通过id获取数据
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ProductInfoMapper.findById", pd);
	}
	
	/*
	* 批量删除
	*/
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("ProductInfoMapper.deleteAll", ArrayDATA_IDS);
	}
	public void addLog(PageData log)throws Exception {
		dao.save("InterfaceLogMapper.save", log);
	}
}

