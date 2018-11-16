package com.xyh.reactor.springboot;

/**
 *
 */
public class WebFlux {

    /**
     什么是函数式
     函数式编程是种编程方式，它将电脑运算视为函数的计算。函数编程语言最重要的基础是λ演算（lambda calculus），而且λ演算的函数可以接受函数当作输入（参数）和输出（返回值）。

     特性
     - 惰性计算
     - 函数是“第一等公民”
     - 只使用表达式而不使用语句
     - 没有副作用
     什么是响应式
     简单来说响应式编程是关于异步的事件驱动的需要少量线程的垂直扩展而非水平扩展的无阻塞应用A key aspect of reactive applications is the concept of backpressure which is a mechanism to ensure producers don’t overwhelm consumers. For example in a pipeline of reactive components extending from the database to the HTTP response when the HTTP connection is too slow the data repository can also slow down or stop completely until network capacity frees up.

     Reactive programming also leads to a major shift from imperative to declarative async composition of logic. It is comparable to writing blocking code vs using the CompletableFuture from Java 8 to compose follow-up actions via lambda expressions.
     For a longer introduction check the blog series “Notes on Reactive Programming” by Dave Syer.
     百度百科： 响应式编程是一种面向数据流和变化传播的编程范式。这意味着可以在编程语言中很方便地表达静态或动态的数据流，而相关的计算模型会自动将变化的值通过数据流进行传播。
     */


}
