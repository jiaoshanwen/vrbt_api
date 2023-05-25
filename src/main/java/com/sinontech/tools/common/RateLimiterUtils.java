package com.sinontech.tools.common;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.util.concurrent.RateLimiter;
import com.sinontech.entity.SysCooperator;

public class RateLimiterUtils {
protected Logger logger = Logger.getLogger(this.getClass());
	private static final HashMap<String, RateLimiter> resourceLimitMap = 
			new HashMap<String, RateLimiter>();
	
	
	/**
	 * 限流
	 * @param resource 需要限流的对象的标识
	 * @return true表示得到了许可，没有达到限流阀值，false表示得不到许可，达到了限流阀值。
	 */
	public static boolean rateLimit(String resource) {
		RateLimiter limit = resourceLimitMap.get(resource);
		if(limit != null){
			return limit.tryAcquire();
		}else{
			return false;
		}
		
	}
	  /**
		 * 初始化
		 * @param resource 需要限流的对象的标识
		 */
	public static void loadRateLimiter(List<PageData> listResource) {
		for(PageData temp:listResource){
			RateLimiter  limit = RateLimiter.create(Double.parseDouble(temp.getString("QPS")));
			resourceLimitMap.put(temp.getString("C_ID"), limit);
		}
		
		
	}
}
