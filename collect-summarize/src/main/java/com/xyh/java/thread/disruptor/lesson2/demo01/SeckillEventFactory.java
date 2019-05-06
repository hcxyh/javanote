package com.xyh.java.thread.disruptor.lesson2.demo01;

import com.lmax.disruptor.EventFactory;

/**
 * 事件生成工厂
 */
public class SeckillEventFactory implements EventFactory<SeckillEvent> {

    public SeckillEvent newInstance(){
        return new SeckillEvent();
    }
}
