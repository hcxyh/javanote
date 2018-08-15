/**   
* @Title: BeanRegister.java
* @Package com.xyh.spring.ioc
* @Description: TODO(用一句话描述该文件做什么)
* @author A18ccms A18ccms_gmail_com   
* @date 2018年8月15日 下午9:34:50
* @version V1.0   
*/
package com.xyh.spring.ioc;

import java.lang.reflect.Method;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.MethodReplacer;

/**
* @ClassName: 	BeanRegister
* @Description: BeanRegister
* @author xueyh
* @date 2017年12月18日 下午9:08:14
*/
public class BeanRegister<T> implements MethodReplacer,FactoryBean<T>{
	
	//方法替换
	@Override
	public Object reimplement(Object obj, Method method, Object[] args) throws Throwable {
		
		
		
		return null;
	}
	/**
	 * spring启动时读取bean的配置信息,在容器中生成一份bean的配置注册表.
	 * 然后根据这张表实例化bean,装配好bean之间的依赖关系.
	 */

	@Override
	public T getObject() throws Exception {
		//相当于代理了beanFactory的getBean()方法
		return null;
	}

	@Override
	public Class<?> getObjectType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}
	
	
}
