package com.xyh.apache.guava.eventbus;

import com.google.common.eventbus.EventBus;

public class Test {

	public static void main(String[] args) {
		
		/**
		 * 在一个进程里面根据event设置多个
		 */
		EventBus eventBus = new EventBus("test");
		
		EventListener listener = new EventListener();

		eventBus.register(listener);

		eventBus.post(new TestEvent(200));
		eventBus.post(new TestEvent(300));
		eventBus.post(new TestEvent(400));

		System.out.println("LastMessage:" + listener.getLastMessage());
		;

	}

}
