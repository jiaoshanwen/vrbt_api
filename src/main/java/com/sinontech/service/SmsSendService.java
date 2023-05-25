package com.sinontech.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.sinontech.dao.DaoSupport;
import com.sinontech.tools.common.DateUtil;
import com.sinontech.tools.common.Page;
import com.sinontech.tools.common.PageData;

import net.sf.json.JSONObject;

@Service("smsSendService")
public class SmsSendService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	public void save(PageData pd) {
		try {
			dao.save("SmsSendMapper.save", pd);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void edit(PageData pd)throws Exception{
		dao.update("SmsSendMapper.edit", pd);
	}
	public PageData getSms(PageData pd) {
		try {
			List<PageData> list = (List<PageData>) dao.findForList("SmsSendMapper.getByPhone", pd);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 验证接口调用短信
	 */
	public JSONObject verifySms(String phone, String smsCode) throws Exception {
		JSONObject json = new JSONObject();
		String code = "";
		String msg = "";
		if (StringUtils.isNotBlank(smsCode)) {
			PageData querySms = new PageData();
			querySms.put("user_phone", phone);
			querySms.put("sms_code", smsCode);
			List<PageData> list = (List<PageData>) dao.findForList("SmsSendMapper.getByPhone", querySms);
			if (list != null && list.size() > 0) {
				PageData sms = list.get(0);
				sms.put("SMS_STATUS", "U");
				this.edit(sms);
				String sendTime = sms.getString("ADD_TIME");
				String nowTime = DateUtil.getTime();
				Long detailMinSub = DateUtil.getDetailMinSub(sendTime, nowTime);
				if (detailMinSub <= 30) {
					code = "0";
					msg = "短信验证通过";
				} else {
					code = "12";
					msg = "验证码已过期";
				}
			} else {
				code = "6";
				msg = "短信验证码错误";
			}
		} else {
			code = "5";
			msg = "请提供短信验证码";
		}
		json.put("code", code);
		json.put("msg", msg);
		return json;
	}

	/*
	 * 验证短信验证码发送是否合理
	 */
	public JSONObject verifySendSms(String phone) throws Exception {
		JSONObject json = new JSONObject();
		String code = "";
		String msg = "";
		PageData querySms = new PageData();
		querySms.put("user_phone", phone);
		querySms.put("NOW_DATE", DateUtil.getDay());

		List<PageData> list = (List<PageData>) dao.findForList("SmsSendMapper.getByPhoneDate", querySms);
		if (list == null || list.size() == 0) {
			code = "0";
			msg = "验证通过";
		} else {
			if (list.size() == 10) {
				code = "4";
				msg = "当天发送短信次数已达上限";
			} else {
				PageData pageData = list.get(0);
				String addTime = pageData.getString("ADD_TIME");
				Long detailMinSub = DateUtil.getDetailMinSub(addTime, DateUtil.getTime());
				if (detailMinSub >= 1) {
					code = "0";
					msg = "验证通过";
				} else {
					code = "5";
					msg = "短信发送频率过快，请稍后再试";
				}
			}
		}
		json.put("code", code);
		json.put("msg", msg);
		return json;
	}

	/**
	 * 查询列表
	 * 
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public List<PageData> list(Page page) throws Exception {
		List<PageData> list = (List<PageData>) dao.findForList("SmsSendMapper.datalistPage", page);
		return list;
	}

}
