package com.xyh.apache.guava.eventbus;

import com.google.common.eventbus.Subscribe;

/**
 * 监听者,订阅者,观察者
 * @author xyh
 *
 */
public class EventListener {
	
	public int lastMessage = 0;

    @Subscribe
    public void listen(TestEvent event) {
        lastMessage = event.getMessage();
        System.out.println("Message:"+lastMessage);
    }
    
    /**
     * 多个订阅
     */
    @Subscribe
    public void listen(Integer event) {
        lastMessage = event.intValue();
        System.out.println("Message:"+lastMessage);
    }
    
    
    
    public int getLastMessage() {      
        return lastMessage;
    }
	

}
