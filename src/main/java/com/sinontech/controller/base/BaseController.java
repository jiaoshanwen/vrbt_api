package com.sinontech.controller.base;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.sinontech.tools.common.Page;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.common.Security;
import com.sinontech.tools.common.UuidUtil;

import net.sf.json.JSONObject;

public class BaseController {
	protected Logger logger = Logger.getLogger(this.getClass());

	private static final long serialVersionUID = 6357869213649815390L;
	
	public PageData jsonstrToPd(String data){
		PageData pd = new PageData();
		JSONObject fromObject=null;
		if(!StringUtils.isEmpty(data)){
			try {
	        	fromObject = JSONObject.fromObject(data);
	        	Set<Entry<String, String>> set = fromObject.entrySet();
	        	if(set != null && set.size()>0){
	        		for (Entry<String, String> en : set) {
	        			String key = en.getKey();
	        			Object value = en.getValue();
//	        			System.out.println(key+":"+value);
	        			if(key.equals("data")) {
	        				String json = Security.decrypt(value.toString(), Security.PASSWORD_CRYPT_KEY);
	        				value = JSONObject.fromObject(json);
	        			}
		        		pd.put(key, value);
					}
	        		return pd;
	        	}
	        } catch (Exception e) {
	        	logger.error("jsonstrToPd:"+e.getMessage());
	        }
		}
		return null;
	}
	public PageData getPageDataJson(){
		BufferedReader br = null;
		String str = "";
		StringBuffer bf = new StringBuffer();
		try {
			br = this.getRequest().getReader();
			while((str = br.readLine()) != null){
				bf =bf.append(str);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if(br!=null) {
					br.close();
				}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return jsonstrToPd(bf.toString());
	}
	/**
	 * 得到PageData
	 */
	public PageData getPageData(){
		return new PageData(this.getRequest());
	}
	
	/**
	 * 得到ModelAndView
	 */
	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}
	
	/**
	 * 得到request对象
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		
		return request;
	}

	/**
	 * 得到32位的uuid
	 * @return
	 */
	public String get32UUID(){
		
		return UuidUtil.get32UUID();
	}
	public void response(String text, HttpServletResponse response) {
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer;
		try {
			writer = response.getWriter();
			writer.write(text);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 得到分页列表的信息 
	 */
	public Page getPage(){
		
		return new Page();
	}
	
	public static void logBefore(Logger logger, String interfaceName){
		logger.info("");
		logger.info("start");
		logger.info(interfaceName);
	}
	
	public static void logAfter(Logger logger){
		logger.info("end");
		logger.info("");
	}
	
}
