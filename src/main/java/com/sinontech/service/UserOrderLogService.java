package com.sinontech.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.sinontech.dao.DaoSupport;
import com.sinontech.tools.common.Page;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.common.XxlUtils;

import net.sf.json.JSONObject;


@Service("userOrderLogService")
public class UserOrderLogService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/*
	* 新增
	*/
	public void save(PageData pd)throws Exception{
		String id = pd.getString("ID");
		if(StringUtils.isBlank(id)) {
			String userPhone = pd.getString("USER_PHONE");
			String result = XxlUtils.getAreaByPhone(userPhone);
			JSONObject phoneJson = JSONObject.fromObject(result);
			String code = phoneJson.getString("status");
			if(code.equals("200")) {
				JSONObject area = phoneJson.getJSONObject("payload");
				String system_code = area.getString("system_code");
				if(system_code.equals("0")) {
					String provinceName = area.getString("provinceName");
					String cityName = area.getString("cityName");
					pd.put("P_PROVINCE", provinceName);
					pd.put("P_CITY", cityName);
				}
			}
			dao.save("UserOrderLogMapper.save", pd);
		}else {
			dao.update("UserOrderLogMapper.edit", pd);
		}
	}
	
	/*
	* 删除
	*/
	public void delete(PageData pd)throws Exception{
		dao.delete("UserOrderLogMapper.delete", pd);
	}
	
	/*
	* 修改
	*/
	public void edit(PageData pd)throws Exception{
		dao.update("UserOrderLogMapper.edit", pd);
	}
	
	/*
	*列表
	*/
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("UserOrderLogMapper.datalistPage", page);
	}
	
	/*
	*列表(全部)
	*/
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("UserOrderLogMapper.listAll", pd);
	}
	/**
	 * 获取待检查状态的订单
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> queryCheckOrder(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("UserOrderLogMapper.queryCheckOrder", pd);
	}
	/**
	 * 获取受理成功的订单，校验号码产品包状态
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public List<PageData> querySuccessOrder(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("UserOrderLogMapper.querySuccessOrder", pd);
	}
	/*
	* 通过id获取数据
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("UserOrderLogMapper.findById", pd);
	}
	public PageData findByOrderNo(PageData pd)throws Exception{
		return (PageData)dao.findForObject("UserOrderLogMapper.findByOrderNo", pd);
	}
	/*
	* 批量删除
	*/
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("UserOrderLogMapper.deleteAll", ArrayDATA_IDS);
	}
	
	
	public void saveOneHour(PageData pd)throws Exception{
		dao.save("UserOrderLogMapper.saveOneHour", pd);
	}
	
	public void addLog(PageData log)throws Exception {
		dao.save("InterfaceLogMapper.save", log);
	}
	
	public void saveZjOrder(PageData pd)throws Exception{
		dao.save("UserOrderLogMapper.saveZJOrder", pd);
	}
	
	public PageData findZJOrderByOrderNo(PageData pd)throws Exception{
		return (PageData)dao.findForObject("UserOrderLogMapper.findZJOrderByOrderNo", pd);
	}
}

