package com.xyh.spring.resttemplate;

import java.lang.reflect.Field;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

/**
* @ClassName: AopUtils
* @author xueyh
* @date 2018年8月15日 下午9:20:24
* git不对空文件夹进行管理
* 
* 这个问题是开涛学spring中遇到的,我们的通过aop的代理类,究竟是何时生成的代理类》
* jdk ， 和cglib 加载的区别
*/
public class AopTargetUtils {
	
	/**
	 * 问题描述：：
	 *我现在遇到个棘手的问题,要通过spring托管的service类保存对象,这个类是通过反射拿到的,
	 *经过实验发现这个类只能反射取得sservice实现了接口的方法,而extends类的方法一律不出现,
	 *debug后发现这个servie实例被spring替换成jdkdynmicproxy类,而不是原始对象了,,
	 *它里面只有service继承的接口方法,而没有extends 过的super class方法,怎么调用原生对象的方法!!!!!
	 *用托管的spring service类调用getClass().getName()方法,发现输出都是$proxy43这类东西!!
	 *通过此种方式获取目标对象是不可靠的，或者说任何获取目标对象的方式都是不可靠的，
	 *因为TargetSource，TargetSource中存放了目标对象，
	 *但TargetSource有很多种实现，默认我们使用的是SingletonTargetSource ，
	 *但还有其他的比如ThreadLocalTargetSource、CommonsPoolTargetSource 等等。
	 *这也是为什么spring没有提供获取目标对象的API。
	 *
	 *	FIXME
	 *	 在 Spring 中已经提供了该特性，可调用 org.springframework.aop.support.AopUtils 类的
	 * isCglibProxy 方法来判断实例是否被 CGLib 动态代理了。
	 * 同样地，可以调用该类的 isJdkDynamicProxy 方法来判断实例是否被 JDK 动态代理。
	 * 若不知道是动态代理方式（CGLib 或 JDK），则可以使用 isAopProxy 方法来判断。
	 * 最后，也可使用 AopUtils 的 getTargetClass 方法获取被动态代理的目标类。
	 */
	
	
	/**
	 * 获取 目标对象
	 * @param proxy 代理对象
	 * @throws Exception
	 */
	public static Object getTarget(Object proxy) throws Exception {
        
		if(!AopUtils.isAopProxy(proxy)) {
			return proxy;//不是代理对象
		}
		if(AopUtils.isJdkDynamicProxy(proxy)) {
			return getJdkDynamicProxyTargetObject(proxy);
		} else { //cglib
			return getCglibProxyTargetObject(proxy);
		}
	}


	private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
		Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        
        Object target = ((AdvisedSupport)advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        
        return target;
	}


	private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
		Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        
        Object target = ((AdvisedSupport)advised.get(aopProxy)).getTargetSource().getTarget();
        
        return target;
	}
	
	
	
}
