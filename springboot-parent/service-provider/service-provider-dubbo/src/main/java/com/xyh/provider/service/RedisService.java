package com.xyh.provider.service;

/**
 * 
 * @author hcxyh  2018年8月15日
 *
 */
public interface RedisService {

	  public void set(String key, Object value);  

	  public Object get(String key);  
	
}
