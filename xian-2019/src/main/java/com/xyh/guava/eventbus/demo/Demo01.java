package com.xyh.guava.eventbus.demo;

import com.google.common.eventbus.EventBus;

public class Demo01 {

	//1.创建EventBus实例
	EventBus eventBus = new EventBus();
	EventBus myEventBus = new EventBus("myEventBus");
		
	//2.Subscribe事件
	/**
	
	
	Subsciber对象需要定义handler method，用于接受并处理一个通知事件对象
	使用Subscribe标签标识事件handler method
	Subscriber向EvenetBus注册，通过EventBus.register方法进行注册.
	
	Post事件:
	post一个事件很简单，只需要调用EventBus.post方法即可以实现。
	EventBus会调用Subscriber的handler method处理事件对象。

	定义handler Method:
	方法接受一个事件类型对象，当publisher发布一个事件，eventbus会串行的处理event-handling method, 
	所以我们需要让event-handing method处理的速度快一些，通常我们可以通过多线程手段来解决延迟的问题。

	Concurrency:
	EventBus可以通过使用AllowConcurrentEvent注解来实现并发调用handle method。
	当handler method被标记为AllowConcurrentEvent（replace Subscribe标签），
	我们认为handler Method是线程安全的。
	
	*/
	
}
