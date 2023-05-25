package com.sinontech.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sinontech.interceptor.InterfacelHandlerInterceptor;

@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {
	 @Resource
	 private InterfacelHandlerInterceptor interfacelHandlerInterceptor;
	 
	 @Override
     public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interfacelHandlerInterceptor)
                .addPathPatterns("/api/**")    // 要拦截的路径
                .excludePathPatterns("/api/crbt/message", "/api/back/synStatus", "/api/back/ismpSyn/**"
                		, "/api/back/wSyn", "/api/back/hnSyn", "/api/back/readSyn"
                		, "/api/back/fhOrderSyn", "/api/back/fhStepSyn", "/api/back/ydSyn"
                		, "/api/back/statusSyn", "/api/back/orderSyn", "/api/back/submitSyn", "/api/back/hnStatusSyn", "/api/back/orderComplexSyn");   // 要排除的路径
     }
}
