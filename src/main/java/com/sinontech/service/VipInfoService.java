package com.sinontech.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sinontech.dao.DaoSupport;
import com.sinontech.tools.common.Page;
import com.sinontech.tools.common.PageData;


@Service("vipInfoService")
public class VipInfoService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**
	 * 检查是否是白名单，城市或者号码
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public boolean chekIfVip(PageData pd){
		boolean result = false;
		try {
			List<PageData> list = (List<PageData>)dao.findForList("VipInfoMapper.queryVip", pd);
			if(list!=null&&list.size()>0) {
				result =true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/*
	* 新增
	*/
	public void save(PageData pd)throws Exception{
		dao.save("VipInfoMapper.save", pd);
	}
	
	/*
	* 删除
	*/
	public void delete(PageData pd)throws Exception{
		dao.delete("VipInfoMapper.delete", pd);
	}
	
	/*
	* 修改
	*/
	public void edit(PageData pd)throws Exception{
		dao.update("VipInfoMapper.edit", pd);
	}
	
	/*
	*列表
	*/
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("VipInfoMapper.datalistPage", page);
	}
	
	/*
	*列表(全部)
	*/
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("VipInfoMapper.listAll", pd);
	}
	
	/*
	* 通过id获取数据
	*/
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("VipInfoMapper.findById", pd);
	}
	
	/*
	* 批量删除
	*/
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("VipInfoMapper.deleteAll", ArrayDATA_IDS);
	}
	
}

