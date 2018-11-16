package com.xyh.reactor;


import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 *
 */
public class ReactorNote <T>{

    /**
     编程范式:
     oop:面向对象编程 --> (抽象,封装，继承，多态) 是对数据的抽象
     fp:函数式编程是对   行为的抽象. {因为不存在多个对象操作同一个属性,不会存在线程安全问题,
     类比hadoop,或者js高阶函数  map ,reduce },可以满足更好的并发性.
     spring基于reactor3-core进行的扩展.
     Reactor是一个基础库，用在构建实时数据流应用、要求有容错和低延迟至毫秒、纳秒、皮秒的服务。
     Reactor是一个轻量级的JVM基础库，它可以帮助我们构建的服务和应用高效而异步的传递消息。

     1.高效的含义是什么呢？
     传递一个消息从A到B时GC产生的内存很小或者完全没有。
     当消费者处理消息的速度低于生产者产生消息的速度时产生了溢出时，必须尽快处理。
     尽可能的提供无锁的异步流。
     据以往的经验来看，我们知道异步编程是困难的，特别是当一个平台提供了很多选项如JVM。

     Reactor瞄准绝大部分场景中真正的无阻塞，并且提供了一组比原生Jdk的java.util.concurrent库更高效的API。Reactor也提供了一个可选性(不建议使用)：

     　　阻塞等待：如Future.get()。

     Unsafe数据获取：如ReentrantLock.lock()。

     　　异常抛出：如try ..catch ...finally

     　　同步阻塞：如 syschronized

     　　Wrapper配置(GC压力):例如 new Wrapper<T>(event)
     */

//    public <T> void test1(){
//        private ExecutorService threadPool = Executors.newFixedThreadPool(8);
//        final List<T> batches = new ArrayList<T>();
//        Callable t = new Callable() {  //1
//            public T run() {
//                synchronized (batches) {  //2
//                    T result = callDatabase(msg); //3
//                    batches.add(result);
//                    return result;
//                }
//            }
//        };
//    }


//    Future<T> f = threadPool.submit(t); //4
//    T result = f.get()  //5




}
