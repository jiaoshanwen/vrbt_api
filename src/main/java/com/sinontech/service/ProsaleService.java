package com.sinontech.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.sinontech.dao.DaoSupport;
import com.sinontech.tools.common.PageData;

import net.sf.json.JSONObject;

@Service("prosaleService")
public class ProsaleService {

	@Resource(name = "daoSupport")
	private DaoSupport dao;


	/**
	 * 根据省份和产品code查询状态
	 * 
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public Boolean queryStatusByCondition(String name, String code) {
		Boolean flag = false;
		PageData data = new PageData();
		data.put("sname", name);
		data.put("sales_code", code);
		try {
			PageData product = (PageData) dao.findForObject("ProsaleMapper.queryStatusByCondition", data);
			if (null != product) {
				String onoff = product.getString("onoff");
				if ("1".equals(onoff)) {
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	
}
