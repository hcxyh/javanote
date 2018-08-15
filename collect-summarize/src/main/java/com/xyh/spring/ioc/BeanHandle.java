/**   
* @Title: BeanHandle.java
* @Package com.xyh.spring.ioc
* @Description: TODO(用一句话描述该文件做什么)
* @author A18ccms A18ccms_gmail_com   
* @date 2018年8月15日 下午9:32:25
* @version V1.0   
*/
package com.xyh.spring.ioc;

import java.beans.PropertyDescriptor;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;


/**
* @ClassName: BeanHandle
* @Description: TODO(这里用一句话描述这个类的作用)
* @author xueyh
* @date 2018年8月15日 下午9:32:25
* 
*/

/**
* @ClassName: BeanHandle
* @author xueyh
* @date 2017年12月18日 下午8:26:41
* 
*/
public class BeanHandle implements BeanFactoryPostProcessor,BeanPostProcessor,Ordered,InstantiationAwareBeanPostProcessor,ApplicationContextAware{
	
	ApplicationContext applicationContext;
	
	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		/**
		 * BeanPostProcessor  -->  postProcessBeforeInitialization
		 */
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		/**
		 * BeanPostProcessor  -->  postProcessAfterInitialization
		 */
		return bean;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		/**
		 * 1.在bean实例化之前,预先封装一些参数。
		 * 2.为了实现在spring容器启动阶段,能动态注入自定义的bean
		 * 并且能被aop所增强.
		 * DefaultListableBeanFactory实现了ConfigurableListableBeanFactory,
		 * 提供了可扩展配置,循环枚举的功能.通过此类实现动态注入.
		 */
		DefaultListableBeanFactory df = (DefaultListableBeanFactory)beanFactory;
		//BeanDefinitionBuilder创建bean的定义元数据
		BeanDefinitionBuilder beanDefinitionBuilder =BeanDefinitionBuilder.genericBeanDefinition(Object.class);
		//设置Nana的属性name,依赖beanName实例
		beanDefinitionBuilder.addPropertyReference("name", "beanName");
		//注册bean的定义
		df.registerBeanDefinition("nanaExt", beanDefinitionBuilder.getRawBeanDefinition());
		//注册一个实例
		df.registerSingleton("beanName", new Object());
		
		BeanDefinition beanDefinition = beanFactory.getBeanDefinition("beanName");
		System.setProperty("key", "value");
	}

	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		//bean初始化之前
		if(beanClass == Object.class){
			//对特殊的类进行处理
		}
		return null;
	}

	@Override
	public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
		//bean初始化之后
		return false;
	}

	@Override
	public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean,
			String beanName) throws BeansException {
		//bean初始化之后,对属性进行赋值
		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		//applicationContext 装载
		this.applicationContext = applicationContext;
	}

}
