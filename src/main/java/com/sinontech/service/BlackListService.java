package com.sinontech.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.sinontech.dao.DaoSupport;
import com.sinontech.tools.common.DateUtil;
import com.sinontech.tools.common.Page;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.common.XxlUtils;
import com.sinontech.tools.common.result.ReResult;

import net.sf.json.JSONObject;

@Service("blackListService")
public class BlackListService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	/*
	 * 新增
	 */
	public void save(PageData pd) throws Exception {
		// 查询号码是否存在
		Boolean flag = this.queryExist(pd);
		if (!flag) {
			pd.put("createtime", DateUtil.getTime());
			pd.put("updatetime", DateUtil.getTime());
			pd.put("area", getArea(pd.getString("phone")));
			dao.save("BlackListMapper.save", pd);
		}
	}

	/**
	 * 查询是否存在,如果为true则是黑名单,如果是false则非黑名单
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public Boolean queryExist(PageData pd) throws Exception {
		return null != this.findByCondition(pd);
	}

	/*
	 * 删除
	 */
	public void delete(PageData pd) throws Exception {
		dao.delete("BlackListMapper.delete", pd);
	}

	/*
	 * 修改
	 */
	public void edit(PageData pd) throws Exception {
		dao.update("BlackListMapper.edit", pd);
	}

	/*
	 * 列表
	 */
	public List<PageData> list(Page page) throws Exception {
		return (List<PageData>) dao.findForList("BlackListMapper.datalistPage", page);
	}

	/*
	 * 列表(全部)
	 */
	public List<PageData> listAll(PageData pd) throws Exception {
		return (List<PageData>) dao.findForList("BlackListMapper.listAll", pd);
	}

	/*
	 * 通过id获取数据
	 */
	public PageData findById(PageData pd) throws Exception {
		return (PageData) dao.findForObject("BlackListMapper.findById", pd);
	}

	public PageData findByCondition(PageData pd) throws Exception {
		return (PageData) dao.findForObject("BlackListMapper.findByCondition", pd);
	}

	/*
	 * 批量删除
	 */
	public void deleteAll(String[] ArrayDATA_IDS) throws Exception {
		dao.delete("BlackListMapper.deleteAll", ArrayDATA_IDS);
	}

	/**
	 * 读取txt文件
	 * 
	 * @param myfile
	 * @return
	 * @throws Exception
	 */
	public PageData importTxt(MultipartFile myfile) throws Exception {
		PageData json = new PageData();
		String name = myfile.getOriginalFilename();
		if (!name.contains(".txt") && !name.contains(".TXT")) {
			return ReResult.fail(json, "不是txt文件!");
		}
		boolean empty = myfile.isEmpty();
		if (empty) {
			return ReResult.fail(json, "文件为空!");
		}
		String encoding = "GBK";
		InputStream inputStream = myfile.getInputStream();
		InputStreamReader read = new InputStreamReader(inputStream, encoding);
		String lineTxt = null;
		BufferedReader bufferedReader = new BufferedReader(read);
		List<PageData> list = new ArrayList<>();
		String time = DateUtil.getTime();
		while ((lineTxt = bufferedReader.readLine()) != null) {
			String p = lineTxt.trim();
			PageData data = new PageData();
			data.put("phone", p);
			if (!queryExist(data)) {
				data.put("createtime", time);
				data.put("updatetime", time);
				data.put("area", getArea(p));
				list.add(data);
			}
		}
		try {
			if (!list.isEmpty()) {
				this.saveBatch(list);
			}
		} catch (Exception e) {
			return ReResult.fail(json, e.getMessage());
		}
		return ReResult.success(json);
	}

	private void saveBatch(List<PageData> list) throws Exception {
		dao.save("BlackListMapper.saveBatch", list);
	}

	/**
	 * 查询归属地
	 * 
	 * @param phone
	 * @return
	 */
	public static String getArea(String phone) {
		String area = "";
		try {
			String areaByPhone = XxlUtils.getAreaByPhone(phone);
			JSONObject json = JSONObject.fromObject(areaByPhone);
			if (json.getInt("status") == 200) {
				JSONObject jsonObject = json.getJSONObject("payload");
				if (null != jsonObject) {
					String province = jsonObject.getString("provinceName");
					String cityName = jsonObject.getString("cityName");
					if (!StringUtils.isEmpty(province)) {
						area += province;
					}
					if (!StringUtils.isEmpty(cityName)) {
						area += cityName;
					}
				}

			}
		} catch (Exception e) {
			return area;
		}
		return area;
	}

	/**
	 * 投诉定期器和投诉导入增加黑名单
	 * 
	 * @param phone
	 * @return
	 * @throws Exception
	 */
	public void complaintAddBlackList(List<PageData> list) throws Exception {
		List<PageData> black_list = new ArrayList<>();
		String time = DateUtil.getTime();
		for (PageData pageData : list) {
			String phone = pageData.getString("phone");
			if (!StringUtils.isEmpty(phone) && !this.queryExist(pageData)) {
				PageData data = new PageData();
				data.put("phone", phone);
				data.put("area", pageData.get("province"));
				data.put("createtime", time);
				data.put("updatetime", time);
				black_list.add(data);
			}
		}
		if (!black_list.isEmpty()) {
			this.saveBatch(black_list);
		}
	}

}
