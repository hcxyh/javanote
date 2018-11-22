package com.xyh.reactor.reactortest;

import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

import static io.netty.handler.codec.mqtt.MqttMessageBuilders.subscribe;

/**
 * reactor文档
 * https://projectreactor.io/docs/core/release/reference/index.html#getting
 */
public class ReactorCoreTest {

    public void test(){
        Flux<String> seq1 = Flux.just("foo", "bar", "foobar");
        List<String> iterable = Arrays.asList("foo", "bar", "foobar");
        Flux<String> seq2 = Flux.fromIterable(iterable);
        Mono<String> noData = Mono.empty();  //	请注意，即使没有值，工厂方法也会遵循泛型类型。
        Mono<String> data = Mono.just("foo");
        Flux<Integer> numbersFromFiveToSeven = Flux.range(5, 3); //第一个参数是范围的开始，而第二个参数是要生成的项目数

    }

    // Flux并Mono使用Java 8 lambdas。.subscribe()对于不同的回调组合，
    // 您有多种变体选择lambdas，
    public void test1(){
        subscribe(); //	订阅并触发序列。

        /**

        subscribe(Consumer<? super T> consumer); //对每个产生的价值做一些事情

        subscribe(Consumer<? super T> consumer,
                Consumer<? super Throwable> errorConsumer); //	处理值但也会对错误做出反应。

        subscribe(Consumer<? super T> consumer,
                Consumer<? super Throwable> errorConsumer,
                Runnable completeConsumer);

        subscribe(Consumer<? super T> consumer,
                Consumer<? super Throwable> errorConsumer,
                Runnable completeConsumer,
                Consumer<? super Subscription> subscriptionConsumer); //	处理价值观和错误并成功完成，但也可以使用 Subscription此subscribe调用生成的内容。

         *
         */

        //演示错误
        Flux<Integer> ints = Flux.range(1, 4)
                .map(i -> {
                    if (i <= 3) return i;
                    throw new RuntimeException("Got to 4");
                });
        ints.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error: " + error));

        Flux<Integer> ints1 = Flux.range(1, 4);
        ints.subscribe(i -> System.out.println(i),
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done"));  //依次打印1,2,3,4,  然后done

        //错误信号和完成信号都是终端事件，彼此排斥（你永远不会同时获得）。要使完成消费者工作，我们必须注意不要触发错误。
    }

    // 取消subscribe()其Disposable
    // 所有这些基于lambda的变体subscribe()都具有Disposable返回类型。在这种情况下，Disposable接口表示可以通过调用其方法来取消订阅的事实dispose()。
    // 与rxJava相同
    public void test2(){
        /*
        对于a Flux或Mono，取消是源应停止生成元素的信号。但是，它不能立即保证：某些源可能会产生如此之快的元素，即使在收到取消指令之前它们也可以完成。
        Disposable本Disposables课程中提供了一些实用工具。其中，Disposables.swap()创建一个Disposable包装器，
        允许您原子地取消和替换混凝土Disposable。这可能很有用，例如，在您要取消请求并在用户单击按钮时将其替换为新请求的UI场景中。
        处理包装器本身将其关闭，处理当前的具体值以及将来尝试的所有替换。
        另一个有趣的用途是Disposables.composite(...)。该组合允许收集若干个Disposable，
        例如与服务呼叫相关联的多个飞行中请求，并在以后立即处置所有这些请求。一旦dispose()调用了复合方法，
        任何添加另一个的尝试都会Disposable立即处理它。
         */

    }


    /*
    BaseSubscriber:
       还有一种subscribe更通用的方法，需要一个完整的方法， Subscriber而不是用lambdas组成一个方法。
       为了帮助您编写这样的Subscriber，我们提供了一个名为的可扩展类BaseSubscriber
     */
    public void test3(){

        Flux.range(1, 10)
                .doOnRequest(r -> System.out.println("request of " + r))
                .subscribe(new BaseSubscriber<Integer>() {

                    @Override
                    public void hookOnSubscribe(Subscription subscription) {
                        request(1);
                    }

                    @Override
                    public void hookOnNext(Integer integer) {
                        System.out.println("Cancelling after having received " + integer);
                        cancel();
                    }
                });

    }






}
