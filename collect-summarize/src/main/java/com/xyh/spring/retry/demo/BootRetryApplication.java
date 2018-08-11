package com.xyh.spring.retry.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;

@SpringBootApplication
@EnableRetry
@ComponentScan
public class BootRetryApplication {
	
	/**
	 *  有注解和 代码两种方式实现重试. 
	 */
	
	public static void main(String[] args) {
		SpringApplication.run(BootRetryApplication.class, args);
	}
	
}