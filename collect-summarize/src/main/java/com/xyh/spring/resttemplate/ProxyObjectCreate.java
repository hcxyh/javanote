package com.xyh.spring.resttemplate;

import org.aopalliance.aop.Advice;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.AopProxyFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;
import org.springframework.aop.support.RegexpMethodPointcutAdvisor;


/**
* @ClassName: ProxyObjectCreate
* @author xueyh
* @date 2018年8月15日 下午9:26:16
* 
*/
public class ProxyObjectCreate {
	
	/**
	 * Spring提供了两种方式来生成代理对象: 
	 * JDKProxy和Cglib，具体使用哪种方式生成由AopProxyFactory
	 * 根据AdvisedSupport对象的配置来决定。默认的策略是如果目标类是接口，
	 * 则使用JDK动态代理技术，否则使用Cglib来生成代理。
	 * 
	 * http://blog.csdn.net/moreevan/article/details/11977115/
	 */
	private AopProxyFactory aopProxyFactory;
	
	//private static ProxyFactory proxyFactory;  //通过手工调用创建代理,熟悉spring代理创建
	
	private Nana nana;
	
	public static void main(String[] args) {
		/**
		 * Advisor(aspect切面) = advice(通知,增强) + pointCut(切点)
		 * FIXME 为啥不能引入这个类,右下角小蓝标
		 * 具体代码在JdkDynamicAopProxy.create()
		 * 获取代理类要实现的接口,除了Advised对象中配置的,还会加上SpringProxy, Advised(opaque=false) 
		 * <li>检查上面得到的接口中有没有定义 equals或者hashcode的接口 
		 * <li>调用Proxy.newProxyInstance创建代理对象 
		 */
		ProxyFactory proxyFactory = new ProxyFactory(); 
		proxyFactory.setOptimize(true);//设置动态代理的方式
		proxyFactory.setTarget(new Nana()); //传入target
		proxyFactory.setInterfaces(Nana.class.getInterfaces());//如果使用jdk，需要声明实现的接口
		proxyFactory.addAdvice(new Advice() {
			/*
			 * 可以设置多个增强,是add. 多个需要声明顺序
			 * 并且这个bean必须要实现methodInterceptor或者aop.Advisor
			 */
		});   
		// AopProxy 有两个实现类 jdkDynamicAopProxy , cglib2AopProxy 
		proxyFactory.getProxy();
		ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
		proxyFactoryBean.setInterceptorNames("");
		/**
		 * 此处注意传入的是adcive的名字,而并非实例.因为织入时是需要取得class中的代码而非实例化对象的代码
		 * TODO 以上还都是对所有的方法进行了拦截代理,并且每一个target都需要一个factory.
		 * 手写 pointCut,继承或实现spring中的切点父类.
		 * 以下几个自动生成代理对象都是基于 beanPostprocrss.
		 */
		RegexpMethodPointcutAdvisor regexpMethodPointcutAdvisor;
		//我们常用的是通过PointCutAdvisor来定义切面.
		NameMatchMethodPointcutAdvisor nameMatchMethodPointcutAdvisor;
		//通过任意的point+advice来声明
		DefaultPointcutAdvisor defaultPointcutAdvisor;
		//基于beanName创建自动代理
		BeanNameAutoProxyCreator  beanNameAutoProxyCreator = null ;
		beanNameAutoProxyCreator.setInterceptorNames("");//添加增强
		beanNameAutoProxyCreator.setBeanNames("");//添加需要生成代理的beanName
		//对容器中的advisor进行扫描,并自动生成代理类
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator;
		//基于注解去生成代理对象
		AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator;
		/**
		 * spring中的 jdk使用invoke,cglib的create使用methodInterceptor实施了拦截链.
		 * 以上几乎完成了spring-aop的流程.
		 * FIXME
		 * 定义Advice -- 配置切点 -- 组合切面 --生成Porxy对象.
		 */
	}

}

class Nana {
	
}
