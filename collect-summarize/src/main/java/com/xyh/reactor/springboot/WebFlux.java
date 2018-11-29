package com.xyh.reactor.springboot;

/**
 *
 */
public class WebFlux {

    /*
     什么是函数式
     函数式编程是种编程方式，它将电脑运算视为函数的计算。函数编程语言最重要的基础是λ演算（lambda calculus），
     而且λ演算的函数可以接受函数当作输入（参数）和输出（返回值）。

     特性
     - 惰性计算
     - 函数是“第一等公民”
     - 只使用表达式而不使用语句
     - 没有副作用
     什么是响应式
     简单来说响应式编程是关于异步的事件驱动的需要少量线程的垂直扩展而非水平扩展的无阻塞应用A key aspect of reactive applications is the concept of backpressure which is a mechanism to ensure producers don’t overwhelm consumers. For example in a pipeline of reactive components extending from the database to the HTTP response when the HTTP connection is too slow the data repository can also slow down or stop completely until network capacity frees up.

     Reactive programming also leads to a major shift from imperative to declarative async composition of logic. It is comparable to writing blocking code vs using the CompletableFuture from Java 8 to compose follow-up actions via lambda expressions.
     For a longer introduction check the blog series “Notes on Reactive Programming” by Dave Syer.
     百度百科： 响应式编程是一种面向数据流和变化传播的编程范式。这意味着可以在编程语言中很方便地表达静态或动态的数据流，
     而相关的计算模型会自动将变化的值通过数据流进行传播。
     */

    /*
    2. 反应式的编程模型的好处是什么？
        为了给各位看官充分证明其好处，我们先来分析下传统的模式有哪些不足！
        作为Java web开发人员，我们写的最多的代码都是放在web容器中tomcat中运行的，Tomcat就是基于Servelt运行的,我们回顾下Servlet的知识,
        可能我们最熟悉的就是HttpServletRequest，和HttpServletResponse.
        1.当你脑子中有Servlet的概念就好理解了。
            Servlet3.0之前 线程会一直阻塞，只有当业务处理完成并返回后时结束 Servlet线程。
            3.0规范其中一个新特性是异步处理支持,即是在接收到请求之后，Servlet 线程可以将耗时的操作委派给另一个线程来完成，
            在不生成响应的情况下返回至容器.这样说可能大家还不太容易理解，我们来举一个例子(这点引用小编之前写的文章)
            SpringBoot2.0之WebFlux解析及实战
        eg：我们假设,设置tomcat最大线程为200,遇到200个非常耗时的请求
            那么当有200个线程同时并发在处理,那么当来201个请求的时候,就已经处理不了，因为所有的线程都阻塞了。这是3.0之前的处理情况
            而3.0之后异步处理是怎样处理呢？学过Netty通信框架的同学会比较容易理解一点，
            Servlet3.0类似于Netty一样就一个boss线程池和work线程池，boss线程只负责接收请求,work线程只负责处理逻辑。
            那么servlet3.0规范中，这200个线程只负责接收请求，然后每个线程将收到的请求，转发到work线程去处理。
            因为这200个线程只负责接收请求，并不负责处理逻辑，故不会被阻塞，而影响通信，就算处理非常耗时，也只是对work线程形成阻塞，
            所以当再来请求，同样可以处理,其主要应用场景是针对业务处理较耗时的情况可以减少服务器资源的占用，并且提高并发处理速度。
     3.代码如何去实现反应式编程?
            前面说了反应式编程可以理解为事件模式或者是订阅者模式,我们如何去实现事件模式？
            小编在这里引入两个框架或许各位看官就理解了
            1.RxJava
            下图是RxJava小编写的例子,大概描述下,事件源即使onNext方法的入参，将信息发送,消费者就是
            subscribe方法，里面会对事件源做处理，onCompleted: 完成 onError:错误
            2.Guava的EventBus实现
            3.reactor.core
      4.Mono和Flux常用API
            Mone和Flux都是数据反应式编程的核心组件，开发人员就是多利用其编写出高效率的代码.
            Reactor是JVM的完全非阻塞反应式编程基础，具有高效的需求管理（以管理“背压”的形式）。
            它直接与Java 8功能的API，特别是整合CompletableFuture，Stream和 Duration。
            它提供了可组合的异步序列API Flux（用于[N]元素）和Mono（用于[0 | 1]元素），广泛地实现了Reactive Extensions规范。
            这段的重点是和Java8结合利用lambda表达式简洁的优点。
                Flux 相当于一个 RxJava Observable 观察者,观察者可以把生产者的消息Publisher,推送给消费者subscribe.
     */

    /*
    WebFlux请求的生命周期:
        https://www.jianshu.com/p/2df61dbf6b2b.
        http://coyee.com/article/12086-spring-5-reactive-web


     */

}
