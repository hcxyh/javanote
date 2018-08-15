/**   
* @Title: BeanLifeCycle.java
* @Package com.xyh.spring.ioc
* @Description: TODO(用一句话描述该文件做什么)
* @author A18ccms A18ccms_gmail_com   
* @date 2018年8月15日 下午9:31:49
* @version V1.0   
*/
package com.xyh.spring.ioc;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
* @ClassName: BeanLifeCycle
* @Description: TODO Bean级生命周期接口
* @author xueyh
* @date 2017年12月18日 下午8:19:05
* 
*/
@Component("beanLifeCycle")
public class BeanLifeCycle implements BeanFactoryAware,BeanNameAware,InitializingBean,DisposableBean{

	@PostConstruct
	@PreDestroy
	public void initMethod(){
		System.out.println("init-method");
	}
	
	@Override
	public void destroy() throws Exception {
		System.out.println("DisposableBean");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		System.out.println("InitializingBean");
	}

	@Override
	public void setBeanName(String name) {
		System.out.println("BeanName");
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		System.out.println("BeanFactoryAware");
	}

}
