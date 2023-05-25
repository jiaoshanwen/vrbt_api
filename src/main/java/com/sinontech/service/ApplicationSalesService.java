package com.sinontech.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sinontech.dao.DaoSupport;
import com.sinontech.tools.common.Page;
import com.sinontech.tools.common.PageData;


@Service("applicationSalesService")
public class ApplicationSalesService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 获取应用请求头部(针对电信)
	 * @param salesId
	 * @return
	 */
	public PageData getSaleHeader(String salesId){
		PageData result = new PageData();
		try {
			PageData querySales = new PageData();
			querySales.put("salesId", salesId);
			PageData apply = this.findApplicationBySalesId(querySales);
			if(apply!=null) {
				result.put("id", apply.get("id"));
				result.put("appKey", apply.get("app_key"));
				result.put("appSecret", apply.get("app_secret"));
				if(apply.get("channel_id")!=null) {
					result.put("channelId", apply.get("channel_id"));
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/*
	* 新增
	*/
	public void save(PageData pd)throws Exception{
		dao.save("ApplicationSalesMapper.save", pd);
	}
	
	/*
	* 删除
	*/
	public void delete(PageData pd)throws Exception{
		dao.delete("ApplicationSalesMapper.delete", pd);
	}
	
	/*
	* 修改
	*/
	public void edit(PageData pd)throws Exception{
		dao.update("ApplicationSalesMapper.edit", pd);
	}
	/**
	 * 通过产品名称产品
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public PageData findByCode(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ApplicationSalesMapper.findByCode", pd);
	}
	/*
	*列表
	*/
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("ApplicationSalesMapper.datalistPage", page);
	}
	
	/*
	*列表(全部)
	*/
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ApplicationSalesMapper.listAll", pd);
	}
	
	public PageData findApplicationBySalesId(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ApplicationSalesMapper.findApplicationBySalesId", pd);
	}
	
	/*
	* 通过id获取数据
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ApplicationSalesMapper.findById", pd);
	}
	
	/*
	* 批量删除
	*/
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("ApplicationSalesMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

