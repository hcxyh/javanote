package com.xyh.netty.clientpool;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * IntegerFactory类用于生成消息的唯一序列号
 * @author xyh
 *
 */
public class IntegerFactory {
    private static class SingletonHolder {
        private static final AtomicInteger INSTANCE = new AtomicInteger();
    }
 
    private IntegerFactory(){}
 
    public static final AtomicInteger getInstance() {
        return SingletonHolder.INSTANCE;
    }
}