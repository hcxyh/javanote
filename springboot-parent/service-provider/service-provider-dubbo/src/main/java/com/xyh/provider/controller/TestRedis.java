package com.xyh.provider.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRedis {
	private static final Logger logger = LoggerFactory.getLogger(TestRedis.class);

	@Autowired
	RedisTemplate redisTemplate;

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@RequestMapping("/set")
	public void set() {
		redisTemplate.opsForValue().set("test", "4321");
	}

	@RequestMapping("/show")
	public String show() {

		logger.info(redisTemplate.opsForValue().get("test").toString());
		return "Hello World";
	}

}
