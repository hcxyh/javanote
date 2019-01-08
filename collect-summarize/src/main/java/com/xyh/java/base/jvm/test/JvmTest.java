package com.xyh.java.base.jvm.test;

import com.xyh.java.thread.disruptor.demo.MyEvent;

import java.util.ArrayList;
import java.util.List;

public class JvmTest {

    static int stackLength = 1;

    public static  void testOutOfMemory(){

        int i = 1;
        List<MyEvent> list = new ArrayList<>();

        while (true){
//            String s = new String("str" + i);
            MyEvent myEvent = new MyEvent();
            i++;
            list.add(myEvent);
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Thread.sleep(50000);

        testOutOfMemory();
        testStackOverflowError();
    }

    /*
     * 线程请求的栈深度超过虚拟机所允许的最大深度，将抛出StackOverflowError异常
     * 最常见引起此类异常的情形时使用不合理的递归调用
     * VM Args:-Xss256k
     */
    public static void testStackOverflowError(){
        stackLength++;
        testStackOverflowError();
    }

}
