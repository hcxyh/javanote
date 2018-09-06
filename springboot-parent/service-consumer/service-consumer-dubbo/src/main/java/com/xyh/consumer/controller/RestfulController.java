package com.xyh.consumer.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xyh.service.api.ServiceApi;
import com.xyh.service.model.resp.ServiceRes;

/**
 * 
 * @author hcxyh  2018年8月15日
 *
 */

@RestController
@RequestMapping("/")
public class RestfulController {
	
	@Reference(version = "1.0.0")
	private ServiceApi testService;
	

	@GetMapping("hello")
	public String hello() {
		return testService.sayHello();
	}

	@GetMapping("user")
	public ServiceRes user() {
		return testService.findUser();
	}

	@GetMapping("list")
	public ServiceRes list(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int pageSize) {
		return testService.getUser(page, pageSize);
	}


	// 从redis获取某个用户
	@RequestMapping(value = "/getuserfromredis", method = RequestMethod.GET)
	public ServiceRes getRedis(@RequestParam String key) {
		return testService.getUserForRedis(key);
	}
	
	@RequestMapping(value = "/testTransaction", method = RequestMethod.GET)
	public ServiceRes getTransaction(@RequestParam String key) {
		return testService.selectUser();
	}
	
	
}
