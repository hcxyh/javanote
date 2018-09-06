package com.xyh.provider;

import org.apache.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 
 * @author hcxyh  2018年8月15日
 *
 */

@SpringBootApplication()
//@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
//exposePorxy默认false, 设为true,才可使用AopContext.currentProxy()获取TreadLocal中当前类的代理实例
@ImportResource({"classpath:config/spring-dubbo.xml"})
public class App {
	
	private static Logger logger = Logger.getLogger(App.class);

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		logger.info("SpringBoot Start Success");
	}
	
}
