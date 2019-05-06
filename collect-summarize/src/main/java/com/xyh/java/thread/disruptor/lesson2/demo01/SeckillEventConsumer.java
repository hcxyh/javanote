package com.xyh.java.thread.disruptor.lesson2.demo01;


import com.lmax.disruptor.EventHandler;

/**
 * 事件的消费者
 */
public class SeckillEventConsumer implements EventHandler<SeckillEvent> {

    //业务处理、这里是无法注入的，需要手动获取，见源码
//    private ISeckillService seckillService = (ISeckillService) SpringUtil.getBean("seckillService");

    public void onEvent(SeckillEvent seckillEvent, long seq, boolean bool) throws Exception {
//        seckillService.startSeckil(seckillEvent.getSeckillId(), seckillEvent.getUserId());
        System.out.println(seckillEvent.toString());

    }
}
