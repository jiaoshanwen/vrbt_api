package com.sinontech.service;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.sinontech.dao.DaoSupport;
import com.sinontech.tools.common.Const;
import com.sinontech.tools.common.DateUtil;
import com.sinontech.tools.common.Page;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.common.RSAUtil;
import com.sinontech.tools.common.RateLimiterUtils;

import net.sf.json.JSONObject;


@Service("cooperatorInfoService")
public class CooperatorInfoService{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 处理接口访问参数
	 * 参数样例： {"loginname":"alipay","sign":"alipay|111","timestamp":"111","data":"{"user":"137,fh,111"}"}
	 * @param param
	 * @param loginip
	 * @return 
	 * @author zhuwq
	 * @time 2019年10月18日上午9:25:27
	 */
	public PageData getOpenParmApi(PageData pd){
		PageData json = new PageData();
		/**
		 * 参数
		 */
		if(pd == null){
			json.put("code",  "1001");
			json.put("msg", "参数不能为空");
			return json;
		}
		String data = pd.getString("data");
		String loginname = pd.getString("loginname");
		String sign = pd.getString("sign");
		String timestamp = pd.getString("timestamp");
		
		/**
		 * 开始流程
		 */
		if(org.springframework.util.StringUtils.isEmpty(loginname)){
			json.put("code", "1002");
			json.put("msg", "登录名不能为空");
			return json;
		}
		if(org.springframework.util.StringUtils.isEmpty(sign)){
			json.put("code",  "1003");
			json.put("msg", "签名不能为空");
			return json;
		}
		if(org.springframework.util.StringUtils.isEmpty(timestamp)){
			json.put("code", "1004");
			json.put("msg", "时间戳不能为空");
			return json;
		}
		PageData cooperatorPd = null;
		List<PageData> cooperatorList = Const.SYS_COOPERATOR_INFO;
		if(cooperatorList == null || cooperatorList.size()==0){
			json.put("code",  "1005");
			json.put("msg", "未查询到接口方信息");
			return json;
		}else{
			for(PageData cooperator:cooperatorList) {
				if(cooperator.getString("LOGIN_NAME").equals(loginname)) {
					cooperatorPd = cooperator;
					break;
				}
			}
		}
		if(cooperatorPd==null) {
			json.put("code",  "1006");
			json.put("msg", "用户信息有误,请确认用户名");
			return json;
		}
		String loginip = cooperatorPd.getString("LOGIN_IP");
		String requestip = pd.getString("requestip");
		String[] loginipS = loginip.split(",");
		boolean ff = false;
		for (String string : loginipS) {
			if(string.equals(requestip)){
				ff = true;
				break;
			}
		}
		//提供给破解方的接口不判断固定ip
		if(!loginname.startsWith("crack")&&!ff){
			json.put("code",  "1010");
			json.put("msg", "非法ip");
			return json;
		}
		List<PageData> interfaces = (List<PageData>)cooperatorPd.get("INTERFACE_LIST");
		boolean ifHave = false;
		if(interfaces!=null&&interfaces.size()>0) {
			for(PageData inter:interfaces) {
				if(inter.getString("INTERFACE_WORD").equals(pd.getString("method"))) {
					ifHave = true;
				}
			}
		}
		if(!ifHave){
			json.put("code",  "1009");
			json.put("msg", "权限不足");
			return json;
		}
		String privatekey = cooperatorPd.getString("PRIVATE_KEY");
		sign = RSAUtil.decrypt(sign,privatekey);
		if(org.springframework.util.StringUtils.isEmpty(sign)){
			json.put("code",  "1007");
			json.put("msg", "签名信息有误");
			return json;
		}
		String pwdlocal = cooperatorPd.getString("LOGIN_PWD")+"|"+timestamp;
		
		if(!sign.equals(pwdlocal)){
			json.put("code", "1007");
			json.put("msg", "签名信息有误");
			return json;
		}
		boolean limit = RateLimiterUtils.rateLimit(cooperatorPd.getString("C_ID"));
		if(!limit) {
			json.put("code", "1008");
			json.put("msg", "服务器繁忙");
			return json;
		}
		PageData dataPd = new PageData();
		if(!StringUtils.isEmpty(data)){
			dataPd = this.jsonstrToPd(data);
		}
		dataPd.put("cooperateId", cooperatorPd.get("C_ID"));
		dataPd.put("appKey", cooperatorPd.get("APP_KEY"));
		dataPd.put("appSecret", cooperatorPd.get("APP_SECRET"));
		dataPd.put("msg", "接口账号验证通过");
		dataPd.put("code", "0000");
		return dataPd;
	}
	/**
	 *  通过名称查询所有配置
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> listAllByName(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("CooperatorInfoMapper.listAllByName", pd);
	}
	private PageData jsonstrToPd(String data){
		PageData pd = new PageData();
		JSONObject fromObject=null;
		if(!StringUtils.isEmpty(data)){
			try {
	        	fromObject = JSONObject.fromObject(data);
	        	Set<Entry<String, String>> set = fromObject.entrySet();
	        	if(set != null && set.size()>0){
	        		for (Entry<String, String> en : set) {
		        		pd.put(en.getKey(), en.getValue());
					}
	        		return pd;
	        	}
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
		}
		return null;
	}
	/*
	* 新增
	*/
	public void save(PageData pd)throws Exception{
		dao.save("CooperatorInfoMapper.save", pd);
	}
	
	/*
	* 删除
	*/
	public void delete(PageData pd)throws Exception{
		dao.delete("CooperatorInfoMapper.delete", pd);
	}
	
	/*
	* 修改
	*/
	public void edit(PageData pd)throws Exception{
		dao.update("CooperatorInfoMapper.edit", pd);
	}
	
	/*
	*列表
	*/
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("CooperatorInfoMapper.datalistPage", page);
	}
	
	/*
	*列表(全部)
	*/
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("CooperatorInfoMapper.listAll", pd);
	}
	
	/*
	* 通过id获取数据
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("CooperatorInfoMapper.findById", pd);
	}
	
	/*
	* 批量删除
	*/
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("CooperatorInfoMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

