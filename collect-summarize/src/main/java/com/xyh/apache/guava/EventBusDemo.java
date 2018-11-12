package com.xyh.apache.guava;

import com.google.common.eventbus.EventBus;


/**
 * jdk自带的Observer观察模式代码
 * spring的event--listener机制
 * EventBbus发布订阅
 * 自己手动实现观察者模式
 * @author xyh
 *
 */
public class EventBusDemo {
	
	public static void main(String[] args) {
		
		//创建Event实例
		EventBus eventBus = new EventBus();
        //string构造参数，用于标识EventBus
        EventBus eventBus1 = new EventBus("My Event Bus");
		
        
	}
	

}
