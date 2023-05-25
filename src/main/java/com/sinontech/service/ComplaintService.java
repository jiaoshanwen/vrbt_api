package com.sinontech.service;

import com.sinontech.dao.DaoSupport;
import com.sinontech.tools.common.*;
import com.sinontech.tools.imusic.VolteInterface;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("complaintService")
public class ComplaintService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Resource(name = "blackListService")
	private BlackListService blackListService;




	private String queryArea(String phone, String province) {
		try {
			String queryPhoneInfo = PhoneUtil.queryPhoneInfo(phone.trim());
			JSONObject pjson = JSONObject.fromObject(queryPhoneInfo);
			if ("0".equals(pjson.getString("code"))) {
				province = pjson.getString("parea");
			}
		} catch (Exception e) {
		}
		return province;
	}

	public Boolean queryExist(PageData pd) throws Exception {
		Boolean flag = false;
		PageData result = (PageData) dao.findForObject("ComplaintMapper.queryExist", pd);
		if (null != result) {
			flag = true;
		}
		return flag;
	}



	/**
	 * 批量添加,并保存黑名单纪录
	 *
	 * @param list
	 * @throws Exception
	 */
	public void saveBatch(List<PageData> list) throws Exception {
		if (!list.isEmpty()) {
			dao.save("ComplaintMapper.saveBatch", list);
			try {
				blackListService.complaintAddBlackList(list);
			} catch (Exception e) {

			}
		}
	}


	/**
	 * 投诉退费批量保存
	 *
	 * @param list
	 * @throws Exception
	 */
	public void saveBatchRefund(List<PageData> list) throws Exception {
		if (!list.isEmpty()) {
			List<PageData> arrayList = new ArrayList<>();
			for (int i = 0; i < list.size(); i++) {
				arrayList.add(list.get(i));
				if (arrayList.size() == 1000 && !arrayList.isEmpty()) {
					dao.save("ComplaintMapper.saveBatchRefund", arrayList);
					arrayList.clear();
				}
				if (i == list.size() - 1 && !arrayList.isEmpty()) {
					dao.save("ComplaintMapper.saveBatchRefund", arrayList);
					arrayList.clear();
				}
			}
		}

	}


	/**
	 * 查询订单
	 *
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData queryOrder(PageData pd){
		String phone = pd.getString("phone");
		PageData data = new PageData();
		if (StringUtils.isEmpty(phone)) {
			data.put("code", "-2");
			data.put("msg", "号码为空!");
			return data;
		}
		try{
		List<PageData> varList =(List<PageData>) dao.findForList("ComplaintMapper.queryOrder", pd);
			if (!varList.isEmpty()) {
				for (PageData pageData : varList) {
					pageData.put("OPEN_STATUS", "未开通");
					PageData header = new PageData();
					header.put("appKey", "2000000004724");
					header.put("appSecret", "p#K!HjW7deOoTyk%v2Dahw6DQcHUSyFx");
					String result = VolteInterface.queryaccountinfo(pd.getString("userPhone"), header);
					JSONObject json = JSONObject.fromObject(result);
					JSONObject returnJson = new JSONObject();
					returnJson.put("code", json.get("res_code"));
					returnJson.put("msg", json.get("res_message"));
					String code = json.getString("res_code");
					if (code.equals("0")) {
						String ringStatus = json.getString("ringStatus");
						String userStatus = json.getString("userStatus");
						if (userStatus.equals("1") && ringStatus.equals("1")) {
							pageData.put("OPEN_STATUS", "已开通");
						} else {
							pageData.put("OPEN_STATUS", "未开通");
							pageData.put("CLOSE_TIME", json.getString("lastUpdateTime"));
						}
					}
					String UNSUBSCRIBE_TIME = pageData.getString("UNSUBSCRIBE_TIME");
					if (!StringUtils.isEmpty(UNSUBSCRIBE_TIME)) {
						if (!UNSUBSCRIBE_TIME.contains("-")) {
							pageData.put("UNSUBSCRIBE_TIME", getTime(UNSUBSCRIBE_TIME));
						}
					} else {
						String packageResult = VolteInterface.querypackagelist(pd.getString("userPhone"),
								pageData.getString("ORDER_CODE"), header);
						JSONObject userPackageListResp = JSONObject.fromObject(packageResult);
						JSONObject packageJson = userPackageListResp.getJSONObject("UserPackageListResp");
						if (packageJson.containsKey("user_package_list")) {
							JSONArray list = new JSONArray();
							JSONObject packageList = packageJson.getJSONObject("user_package_list");
							String up = packageList.getString("user_package");
							if (up.startsWith("[")) {
								list = packageList.getJSONArray("user_package");
							} else {
								JSONObject op = JSONObject.fromObject(up);
								list.add(op);
							}
							if (list != null && list.size() > 0) {
								JSONObject pack = list.getJSONObject(0);
								String status = pack.getString("status");
								if (status.equals("2")) {
									pageData.put("UNSUBSCRIBE_TIME", pack.getString("unsubscribe_time"));
								}
							}
						}
					}
				}
			}
			data.put("code","0");
			data.put("msg","成功!");
			if(null!=varList && !varList.isEmpty()){
				data.put("data", Security.encrypt(JSONArray.fromObject(varList).toString()));
			}
		}catch (Exception e){
			data.put("code","-1");
			data.put("msg",e.getMessage());
		}
		return data;
	}

	public PageData saveComplaint(PageData pd) {
		PageData data = new PageData();
		String param = data.getString("param");//传入的参数
		List<PageData> list = new ArrayList<PageData>();
		String time = DateUtil.getTime();
		try {
			if (null!=pd) {
				PageData savepd = new PageData();
				savepd.put("complainttime", pd.get("CREATE_TIME"));
				savepd.put("phone", pd.get("PHONE"));
				savepd.put("type",
						pd.getString("ORDER_TYPE") .equals("1") ? "投诉单" : pd.getString("ORDER_TYPE") .equals("2")  ? "商机单" : "退订");
				savepd.put("product", pd.get("PRODUCT_NAME"));
				savepd.put("province", pd.get("CITY"));
				savepd.put("source", pd.get("CHANNEL"));
				savepd.put("content", pd.get("CONTENT"));
				savepd.put("category", pd.getString("ORDER_TYPE") .equals("1") ? 4 : 3);
				savepd.put("createtime", time);
				savepd.put("updatetime", time);
				savepd.put("uuid", UuidUtil.get32UUID());
				savepd.put("merchant",
						null == pd.get("PHONE_TYPE") ? 4
								: pd.getString("PHONE_TYPE").contains("电信") ? 1
								: pd.getString("PHONE_TYPE").contains("移动") ? 2 : 3);
				Boolean is_exist = this.queryExist(savepd);
				if (!is_exist) {
					list.add(savepd);
				}
				if (null != list && !list.isEmpty()) {
					this.saveBatch(list);
					list.clear();
				}
				data.put("code", "0");
				data.put("msg", "成功!");
			} else {
				data.put("code", "-2");
				data.put("msg", "数据为空!");
			}
		} catch (Exception e) {
			data.put("code", "-1");
			data.put("msg", e.getMessage());
		}
		return data;
	}

	public PageData saveComplaintRefund(PageData pd) {
		PageData data = new PageData();
		try {
			List<PageData> list = new ArrayList<PageData>();
			//String param = pd.getString("param");//param
			if (null!=pd) {
				//String deCode = Security.decrypt(param, Security.PASSWORD_CRYPT_KEY);
				//JSONObject res = JSONObject.fromObject(deCode);
				PageData savepd = new PageData();
				savepd.put("call_id", checkResult(pd.get("CALL_ID")));
				savepd.put("order_type", checkResult(pd.get("ORDER_TYPE")));
				savepd.put("phone", checkResult(pd.get("PHONE")));
				String type = "";
				if (checkResult(pd.get("PHONE_TYPE")).toString().contains("电信")) {
					type = "1";
				} else if (checkResult(pd.get("PHONE_TYPE")).toString().contains("移动")) {
					type = "2";
				} else if (checkResult(pd.get("PHONE_TYPE")).toString().contains("联通")) {
					type = "3";
				}
				String product_name = checkResult(pd.get("PRODUCT_NAME")).toString();
				if ("视频彩铃".equals(product_name)) {
					product_name = "妙趣彩铃";
				}
				savepd.put("phone_type", type);
				savepd.put("city", checkResult(pd.get("CITY")));
				savepd.put("business_type", checkResult(pd.get("BUSINESS_TYPE")));
				savepd.put("product_name", product_name);
				savepd.put("channel", checkResult(pd.get("CHANNEL")));
				savepd.put("channel_info", checkResult(pd.get("CHANNEL_INFO")));
				savepd.put("content", checkResult(pd.get("CONTENT")));
				savepd.put("accept_result", checkResult(pd.get("ACCEPT_RESULT")));
				savepd.put("cancel_order", checkResult(pd.get("CANCEL_ORDER")));
				savepd.put("cancel_fee", checkResult(pd.get("CANCEL_FEE")));
				savepd.put("create_time", checkResult(pd.get("CREATE_TIME")));
				savepd.put("complaint_channel", checkResult(pd.get("COMPLAINT_CHANNEL")));
				savepd.put("cancel_fee_phone_fee", checkResult(pd.get("CANCEL_FEE_PHONE_FEE")));
				savepd.put("cancel_fee_charge", checkResult(pd.get("CANCEL_FEE_CHARGE")));
				savepd.put("cancel_fee_type", checkResult(pd.get("CANCEL_FEE_TYPE")));
				savepd.put("cancel_fee_time", checkResult(pd.get("CANCEL_FEE_TIME")));
				list.add(savepd);
				if (null != list && !list.isEmpty()) {
					this.saveBatchRefund(list);
					list.clear();
				}
				data.put("code", "0");
				data.put("msg", "成功!");
			} else {
				data.put("code", "-2");
				data.put("msg", "数据为空!");
			}
		} catch (Exception e) {
			data.put("code", "-1");
			data.put("msg", e.getMessage());
		}
		return data;
	}

	public Object checkResult(Object obj) {
		if (null == obj) {
			return "";
		}
		return obj;
	}
	public String getTime(String time) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
			Date date = sdf2.parse(time);
			return sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
