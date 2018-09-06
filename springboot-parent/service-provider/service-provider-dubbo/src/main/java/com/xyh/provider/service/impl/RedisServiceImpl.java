package com.xyh.provider.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.xyh.provider.service.RedisService;

/**
 * 
 * @author hcxyh  2018年8月15日
 *
 */
@Component
public class RedisServiceImpl implements RedisService{
	
	@Autowired
	StringRedisTemplate  stringRedisTemplate;

	@Override
	public void set(String key, Object value) {
		stringRedisTemplate.opsForValue().set(key, value.toString());
	}

	@Override
	public Object get(String key) {
		return stringRedisTemplate.opsForValue().get(key);
	}

}
