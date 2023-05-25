package com.sinontech.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sinontech.dao.DaoSupport;
import com.sinontech.tools.common.DateUtil;
import com.sinontech.tools.common.Page;
import com.sinontech.tools.common.PageData;


@Service("userInfoService")
public class UserInfoService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 更新记录
	 * @param order
	 */
	public void updateUserInfo(PageData order) {
		String orderId = order.getString("ID");
		String isOn = order.getString("IS_ON");
		try {
			PageData user = findByOrderId(orderId);
			if(user!=null) {
				if(isOn.equals("1")) {
					Integer month = Integer.valueOf(user.getString("ON_MONTH"));
					user.put("ON_MONTH", month+1);
				}else if(isOn.equals("0")) {
					user.put("IS_ON", "0");
					user.put("UN_TIME", user.get("UNSUBSCRIBE_TIME"));
				}
				user.put("UPDATE_TIME", DateUtil.getTime());
				edit(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	* 新增
	*/
	public void save(PageData pd)throws Exception{
		dao.save("UserInfoMapper.save", pd);
	}
	
	/*
	* 删除
	*/
	public void delete(PageData pd)throws Exception{
		dao.delete("UserInfoMapper.delete", pd);
	}
	
	/*
	* 修改
	*/
	public void edit(PageData pd)throws Exception{
		dao.update("UserInfoMapper.edit", pd);
	}
	
	/*
	*列表
	*/
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("UserInfoMapper.datalistPage", page);
	}
	
	/*
	*列表(全部)
	*/
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("UserInfoMapper.listAll", pd);
	}
	
	/*
	* 通过id获取数据
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("UserInfoMapper.findById", pd);
	}
	
	public PageData findByOrderId(String orderId)throws Exception{
		PageData query = new PageData();
		query.put("orderId", orderId);
		return (PageData)dao.findForObject("UserInfoMapper.findByOrderId", query);
	}
	
	/*
	* 批量删除
	*/
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("UserInfoMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

