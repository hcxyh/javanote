package com.xyh.spring.eventlistener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * spring中event和Listener中的使用
 * @author xyh
 *
 */
public class EventListenerNote implements ApplicationListener<ContextRefreshedEvent>{

	@Override
	public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
		   System.out.println("我的父容器为：" + contextRefreshedEvent.getApplicationContext().getParent());
	        System.out.println("初始化时我被调用了。");
	}
	
	/**
	 * 1.传统的代码  extends ApplicationEvent,implements ApplicationListener,然后通过context.publishEvent(event);
	 * 		或者 implements ApplicationEventPublisherAware.
	 * 2.使用注解
	 * 	@EventListener(condition = "#sendSmsEvent.sender == 'andy1'")可以使用condition进行event属性过滤
	 * 	@Async进行组合使用，以提供异步事件处理的机制。
	 * 3.原生提供的默认事件
	 * 	ContextRefreshedEvent	当ApplicationContext或者叫spring被初始化或者刷新initialized会触发该事件
		ContextStartedEvent	spring初始化完，时触发
		ContextStoppedEvent	spring停止后触发，一个停止了的动作，可以通过start()方法从新启动
		ContextClosedEvent	spring关闭，所有bean都被destroyed掉了,这个时候不能被刷新，或者从新启动了
		RequestHandledEvent	请求经过DispatcherServlet时被触发，在request完成之后
	 * 
	 */
	
	
}
