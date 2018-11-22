package com.xyh.reactor;


import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 同步调用 , 异步调用(future[阻塞],listerableFuture,completeFuture) ,
 * 回调(同步异步 netty的扩展实现[future/promise]),
 * 设计模式-->发布订阅(同步,异步) [guava-eventBus, rxJava, ]
 * 反应式编程是一种涉及数据流和变化传播的异步编程范例 reactor-core,akka
 *
 * 联想到消息队列的发布订阅的推拉模式,同样在reactor中,
 * 过订阅行为，您将Publishera 绑定到a Subscriber，从而触发整个链中的数据流。这是通过上游传播的单个request 信号在内部实现的Subscriber，一直传回源 Publisher。
 * 背压:
 * 这将推模型转换为推拉式混合动力，如果它们随时可用，下游可以从上游拉出n个元素。但是如果元素没有准备好，它们会在生成时被上游推动。
 *
 *
 */
public class ReactorNote <T>{

    /*
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

    /*
     1.反应式编程范例通常以面向对象的语言呈现，作为Observer设计模式的扩展。
     还可以将主要的反应流模式与熟悉的迭代器设计模式进行比较，
     因为在所有这些库中对Iterable- Iterator对存在双重性 。
     一个主要的区别是，虽然迭代器是基于拉的，但是反应流是基于推的。
     2.为什么需要响应式
         当前人们可以通过两种方式来提高计划的绩效：
            1.并行化：使用更多线程和更多硬件资源。
            2.在现有资源的使用方式上寻求更高的效率。(通过编写异步，非阻塞代码，
     您可以使用相同的底层资源将执行切换到另一个活动任务，然后在异步处理完成后返回到当前进程。)
     3.但是如何在JVM上生成异步代码？Java提供了两种异步编程模型：
         回调：异步方法没有返回值，但需要额外的 callback参数（lambda或匿名类），
         在结果可用时调用它们。一个众所周知的例子是Swing的EventListener层次结构。
         期货：异步方法Future<T> 立即返回。异步进程计算一个T值，但该Future对象包含对它的访问。
         该值不会立即可用，并且可以轮询对象，直到该值可用。例如，ExecutorService运行Callable<T>任务使用Future对象。
     4.回调解析
        回调很难组合在一起，很快导致难以阅读和维护的代码（称为“Callback Hell”）。
        考虑一个例子：在用户界面上显示用户的前五个收藏夹，或者建议她是否没有收藏夹。
        这通过三个服务（一个提供喜欢的ID，第二个提取喜欢的详细信息，第三个提供详细建议）：
        之前使用node.js写过express的都知道稍微一不小心就容易写出回调地域的代码.
     5.future
        尽管Java 8中带来了改进，但期货比回调要好一些，但它们在构图方面仍然表现不佳CompletableFuture。
     一起编排多个未来是可行但不容易的。此外，Future还有其他问题：Future通过调用get() 方法很容易结束对象的另一个阻塞情况，
     它们不支持延迟计算，并且它们不支持多个值和高级错误处理。
        eg：
     CompletableFuture<List<String>> ids = ifhIds();
     CompletableFuture<List<String>> result = ids.thenComposeAsync(l -> {
        Stream<CompletableFuture<String>> zip =
            l.stream().map(i -> {
                 CompletableFuture<String> nameTask = ifhName(i);
                 CompletableFuture<Integer> statTask = ifhStat(i);
                 return nameTask.thenCombineAsync(statTask, (name, stat) -> "Name " + name + " has stats " + stat);
            });
         List<CompletableFuture<String>> combinationList = zip.collect(Collectors.toList());
         CompletableFuture<String>[] combinationArray = combinationList.toArray(new CompletableFuture[combinationList.size()]);
     CompletableFuture<Void> allDone = CompletableFuture.allOf(combinationArray);
     return allDone.thenApply(v -> combinationList.stream()
     .map(CompletableFuture::join)
     .collect(Collectors.toList()));
     });


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
