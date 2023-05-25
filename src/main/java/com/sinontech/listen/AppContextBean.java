package com.sinontech.listen;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class AppContextBean implements ApplicationContextAware{
	private static ApplicationContext applicationContext; 
	
	 public void setApplicationContext(ApplicationContext context) {
		 AppContextBean.applicationContext = context;
	 }
	   
    public static Object getBean(String name){
         return applicationContext.getBean(name);
    }
    
     public static ApplicationContext getApplicationContext() {  
          return applicationContext;  
      }  
     /**
      * 通过name,以及Clazz返回指定的Bean
      * @param name 容器名
      * @param clazz 对象Class
      * @return
      */
     public static <T> T getBean(String name, Class<T> clazz) {
         return getApplicationContext().getBean(name, clazz);
     }
}
