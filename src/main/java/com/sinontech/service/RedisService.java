package com.sinontech.service;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service("redisService")
public class RedisService {
	
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	/**
	 * 
	 * @param key
	 * @param value
	 * @param time 单位秒
	 */
	public void put(String key, Object value,Long time) {
		if(time==null||time<0) {
			 redisTemplate.opsForValue().set(key, value); 
		}else {
			 redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS); 
		}
	} 
	
	public String get(Object key) {
		Object phone = redisTemplate.opsForValue().get(key);
		if(phone==null) {
			return null;
		}
		/**
		 * 更新key值有效期
		 */
		//redisTemplate.opsForValue().set(key+"", phone, 30L, TimeUnit.MINUTES); 
		return phone.toString();
	}
}
