package com.xyh.java.thread.disruptor.lesson2.demo01;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.ThreadFactory;

/**
 * 测试主方法:
 */
public class SeckillEventMain {

    public static void main(String[] args) {
        producerWithTranslator();
    }


    public static void producerWithTranslator(){
        SeckillEventFactory factory = new SeckillEventFactory();
        int ringBufferSize = 1024;

        ThreadFactory threadFactory = new ThreadFactory() {
            public Thread newThread(Runnable runnable) {
                return new Thread(runnable);
            }
        };
        //创建disruptor
        Disruptor<SeckillEvent> disruptor = new Disruptor<SeckillEvent>(factory, ringBufferSize, threadFactory);
        //连接消费事件方法
        disruptor.handleEventsWith(new SeckillEventConsumer());
        //启动
        disruptor.start();

        RingBuffer<SeckillEvent> ringBuffer = disruptor.getRingBuffer();

        SeckillEventProduce producer = new SeckillEventProduce(ringBuffer);
        for(long i = 0; i<1000000; i++){
            producer.seckill(i, i);
        }
        disruptor.shutdown();//关闭 disruptor，方法会堵塞，直至所有的事件都得到处理；


    }
}
