package com.sinontech.interceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.sinontech.entity.JsonObj;
import com.sinontech.service.CooperatorInfoService;
import com.sinontech.tools.common.PageData;
import com.sinontech.tools.common.Security;

import net.sf.json.JSONObject;

/**
 *  接口账号拦截
 * @author guchao
 *
 */
@Component
public class InterfacelHandlerInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	public CooperatorInfoService cooperatorInfoService;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		PageData pd = this.getPageDataJson(request);
		if(pd==null) {
			pd = new PageData();
		}
		String requestPath = request.getRequestURI();
		String[] requestMethods = requestPath.split("/");
		//获取请求方法
		String requestMethod = requestMethods[requestMethods.length-1];
		pd.put("method", requestMethod);
		JsonObj jo = new JsonObj();
		pd.put("requestip", getIpAddress(request));
		PageData dataPd = cooperatorInfoService.getOpenParmApi(pd);
		if(!dataPd.getString("code").equals("0000")){
			jo.setCode(dataPd.getString("code"));
			jo.setMsg(dataPd.getString("msg"));
			request.setAttribute("message",jo.toString());
			request.getRequestDispatcher("/api/crbt/message").forward(request, response);
			return false;
		}else {
			request.setAttribute("data",dataPd);
			return true;
		}
	}
	protected String getIpAddress(HttpServletRequest request) {  
        String ip = request.getHeader("X-Forwarded-For");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_CLIENT_IP");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
        } 
        if(ip.contains(",")) {
        	ip = ip.split(",")[0];
        }
        return ip;  
	} 
	public PageData getPageDataJson(HttpServletRequest request){
		BufferedReader br = null;
		String str = "";
		StringBuffer bf = new StringBuffer();
		try {
			br = request.getReader();
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
	        			if(key.equals("data")) {
	        				String json = Security.decrypt(value.toString(), Security.PASSWORD_CRYPT_KEY);
	        				value = JSONObject.fromObject(json);
	        			}
		        		pd.put(key, value);
					}
	        		return pd;
	        	}
	        } catch (Exception e) {
//	        	System.out.println("jsonstrToPd:"+e.getMessage());
	        	e.printStackTrace();
	        }
		}
		return null;
	}
}
