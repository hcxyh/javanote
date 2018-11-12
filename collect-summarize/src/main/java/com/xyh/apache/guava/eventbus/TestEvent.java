package com.xyh.apache.guava.eventbus;

/**
 * 消息,事件抽象类
 * @author xyh
 *
 */
public class TestEvent {
	
	 private final int message;
	    public TestEvent(int message) {        
	        this.message = message;
	        System.out.println("event message:"+message);
	    }
	    public int getMessage() {
	        return message;
	    }
	

}
