package com.xyh.java.thread.disruptor.demo;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StartDisruptor {

    public static  void main(String[] args){

        EventFactory<MyEvent> myEventMyEventFactory = new MyEventFactory();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        int ringBufferSize = 1024 * 1024; // RingBuffer 大小，必须是 2 的 N 次方；

        Disruptor<MyEvent> disruptor = new Disruptor<MyEvent>(myEventMyEventFactory,
                ringBufferSize, executor, ProducerType.SINGLE,
                new YieldingWaitStrategy());

        EventHandler<MyEvent> eventHandler = new MyEventHandler();
        disruptor.handleEventsWith(eventHandler);

        disruptor.start();


        /**
         * Disruptor 的事件发布过程是一个两阶段提交的过程：
         * 　　第一步：先从 RingBuffer 获取下一个可以写入的事件的序号；
         * 　　第二步：获取对应的事件对象，将数据写入事件对象；
         * 　　第三部：将事件提交到 RingBuffer;
         * 事件只有在提交之后才会通知 EventProcessor 进行处理；
         */
        // 发布事件；
        RingBuffer<MyEvent> ringBuffer = disruptor.getRingBuffer();
        long sequence = ringBuffer.next();//请求下一个事件序号；

        try {
            MyEvent event = ringBuffer.get(sequence);//获取该序号对应的事件对象；
            long data = 100;//获取要通过事件传递的业务数据；
            event.setName( data + "");
        } finally{
            ringBuffer.publish(sequence);//发布事件；
        }
    }
}
