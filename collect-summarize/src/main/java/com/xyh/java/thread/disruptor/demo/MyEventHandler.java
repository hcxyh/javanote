package com.xyh.java.thread.disruptor.demo;

import com.lmax.disruptor.EventHandler;

/*
 * 定义事件处理的具体实现
 * 通过实现接口 com.lmax.disruptor.EventHandler<T> 定义事件处理的具体实现。
 */
public class MyEventHandler implements EventHandler<MyEvent> {

    @Override
    public void onEvent(MyEvent event, long sequence, boolean endOfBatch) throws Exception {

        System.out.print(event.toString());

    }

}
