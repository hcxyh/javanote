package com.xyh.java.thread.disruptor.lesson2.demo01;

import java.io.Serializable;

/*
 *  事件对象 (秒杀事件)
 *  Disruptor中传递的数据都是以事件这种方式抽象出来的.
 */
public class SeckillEvent implements Serializable {

    private static final long serializableId = 1L;
    private long seckillId;
    private long userId;

    public SeckillEvent() {
    }

    public SeckillEvent(long seckillId, long userId) {
        this.seckillId = seckillId;
        this.userId = userId;
    }

    public static long getSerializableId() {
        return serializableId;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SeckillEvent{" +
                "seckillId=" + seckillId +
                ", userId=" + userId +
                '}';
    }
}
