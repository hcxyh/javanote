package com.xyh.reactor;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

/**
 * 响应式编程Example
 * https://www.infoq.com/articles/reactor-by-example
 * rxJava的Example
 * https://www.infoq.com/articles/rxjava2-by-example
 */
public class ReactorSimple {

    /**
     关于反应式编程的思想：
     反应式编程框架主要采用了观察者模式，而SpringReactor的核心则是对观察者模式的一种衍伸。
     关于观察者模式的架构中被观察者(Observable)和观察者(Subscriber)处在不同的线程环境中时，
     由于者各自的工作量不一样，导致它们产生事件和处理事件的速度不一样，这时就出现了两种情况：

     被观察者产生事件慢一些，观察者处理事件很快。那么观察者就会等着被观察者发送事件，
     （好比观察者在等米下锅，程序等待，这没有问题）。
     被观察者产生事件的速度很快，而观察者处理很慢。那就出问题了，如果不作处理的话，
     事件会堆积起来，最终挤爆你的内存，导致程序崩溃。（好比被观察者生产的大米没人吃，堆积最后就会烂掉）。
     为了方便下面理解Mono和Flux，也可以理解为Publisher（发布者也可以理解为被观察者）
     主动推送数据给Subscriber（订阅者也可以叫观察者），如果Publisher发布消息太快，
     超过了Subscriber的处理速度，如何处理。这时就出现了Backpressure
     （背压-----指在异步场景中，被观察者发送事件速度远快于观察者的处理速度的情况下，
     一种告诉上游的被观察者降低发送速度的策略）
     --------------------------------------------------------------------------------
     Reactor的主要类：
     在Reactor中，经常使用的类并不多，主要有以下两个：
         Mono 实现了 org.reactivestreams.Publisher 接口，代表0到1个元素的发布者（Publisher）。
         Flux 同样实现了 org.reactivestreams.Publisher 接口，代表0到N个元素的发布者（Subscriber）。
     可能会使用到的类：
     Scheduler 表示背后驱动反应式流的调度器，通常由各种线程池实现。
     ------------------------------------------------------------------------------------
     关于Reactive Streams、Srping Reactor 和 Spring Flux（Web Flux）之间的关系
     Reactive Streams 是规范，Reactor 实现了 Reactive Streams。Web Flux 以 Reactor 为基础，
     实现 Web 领域的反应式编程框架
     --------------------- --------------------------------------------------------
     关于Mono和Flux
     Mono和Flux都是Publisher（发布者）。
     其实，对于大部分业务开发人员来说，当编写反应式代码时，我们通常只会接触到 Publisher 这个接口，
     对应到 Reactor 便是 Mono 和 Flux。对于 Subscriber 和 Subcription 这两个接口，
     Reactor 必然也有相应的实现。但是，这些都是 Web Flux 和 Spring Data Reactive 这样的框架用到的。
     如果不开发中间件，通常开发人员是不会接触到的。
     比如，在 Web Flux，你的方法只需返回 Mono 或 Flux 即可。你的代码基本也只和 Mono 或 Flux 打交道。
     而 Web Flux 则会实现 Subscriber ，
     onNext 时将业务开发人员编写的 Mono 或 Flux 转换为 HTTP Response 返回给客户端。
     -------------------------------------------------------------------------------------
     Flux和Mono的一些操作利用了这个特点在这两种类型间互相转换。例如，调用Flux<T>的single()方法将返回一个Mono<T>，
     而使用concatWith()方法把两个Mono串在一起就可以得到一个Flux。类似地，
     有些操作对Mono来说毫无意义（例如take(n)会得到n>1的结果），而有些操作只有作用在Mono上才有意义（例如or(otherMono)）。
     --------------------- --------------------------------------------------------------------------
     java 8 Stream API和CompletableFuture跟Flux/Mono之间可以很容易地进行互相转换。那么一般情况下我们是否要把Stream转成Flux？
     不一定。虽然说Flux或Mono对IO和内存相关操作的封装所产生的开销微不足道，不过Stream本身也并不会带来很大延迟，
     所以直接使用Stream API是没有问题的。对于上述情况，在RxJava 2里需要使用Observable，因为Observable不支持回压，
     所以一旦对其进行订阅，它就成为事件推送的来源。Reactor是基于Java 8的，所以在大部分情况下，
     Stream API已经能够满足需求了。要注意的是，尽管Flux和Mono的工厂模式也支持简单类型，
     但它们的主要用途还是在于把对象合并到更高层次的流里面。所以一般来说，在现有代码上应用响应式模式时，
     你不会希望把“long getCount()”这样的方法转成“Mono<Long> getCount()”。
     -------------------------------------------------------------------------------------------------------




     */


    /*
     * 1.如何将数据概念化为流，Observable类及其各种运算符，
     * 以及从中创建Observable的工厂方法静态和动态来源。
     * 2.Observable是推送源，Observer是通过订阅行为消费此源的简单接口。
     * 请记住，Observable的合同是通过onNext通知其Observer 0个或更多数据项，
     * 可选地后跟onError或onComplete终止事件。
     * 3.Reactor的两个主要类型是Flux<T>和Mono<T>。
     * Flux相当于RxJava Observable，能够发出0个或更多项，然后可选择完成或错误。
     * 4.Mono 最多可以发射一次。它对应于RxJava端的两者Single和Maybe类型。
     * 因此，只想表示完成的异步任务可以使用a Mono<Void>。
     */




    public void test(){
         List<String> words = Arrays.asList(
                "the",
                "quick",
                "brown",
                "fox",
                "jumped",
                "over",
                "the",
                "lazy",
                "dog"
        );

        Flux<String> fewWords = Flux.just("Hello", "World");
        Flux<String> manyWords = Flux.fromIterable(words);

        fewWords.subscribe(System.out::println);
        System.out.println();
        manyWords.subscribe(System.out::println);

        /*
        为了输出狐狸句中的单个字母，我们也需要flatMap（正如我们在RxJava中通过示例所做的那样），
        但在Reactor中我们使用fromArray而不是from。然后我们想要过滤掉重复的字母并使用distinct和对它们进行排序sort。
        最后，我们想要为每个不同的字母输出一个索引，可以使用zipWith和完成range：
         */


        Flux<String> manyLetters = Flux
                .fromIterable(words)
                .flatMap(word -> Flux.fromArray(word.split("")))
                .distinct()
                .sort()
                .zipWith(Flux.range(1, Integer.MAX_VALUE),
                        (string, count) -> String.format("%2d. %s", count, string));

        manyLetters.subscribe(System.out::println);

        /*
        解决原始单词数组的一种方法是修正原始单词数组，但我们也可以Flux使用concat/concatWith和a 手动将“s”值添加到字母中Mono：
         */
        Mono<String> missing = Mono.just("s");
        Flux<String> allLetters = Flux
                .fromIterable(words)
                .flatMap(word -> Flux.fromArray(word.split("")))
                .concatWith(missing)
                .distinct()
                .sort()
                .zipWith(Flux.range(1, Integer.MAX_VALUE),
                        (string, count) -> String.format("%2d. %s", count, string));

        allLetters.subscribe(System.out::println);


    }




}
