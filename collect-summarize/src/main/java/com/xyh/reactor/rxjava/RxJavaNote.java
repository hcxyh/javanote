package com.xyh.reactor.rxjava;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.AsyncSubject;
import org.reactivestreams.Subscriber;

public class RxJavaNote {

    /**
     * 基于发布订阅模式,运行在jvm上的异步开发框架.
     * RxJava是一种基于观察者模式的响应式编程框架，其中的主要角色有
     */

    /**
     * 观察者模式
     * 简单介绍一下，A和B两个，A是被观察者，B是观察者，B对A进行观察，
     * B并不是需要时刻盯着A，而是A如果发生了变化，会主动通知B，
     * B会对应做一些变化。举个例子，假设A是连载小说，B是读者，
     * 读者订阅了连载小说，当小说出现了新的连载的时候，会推送给读者。
     * 读者不用时刻盯着小说连载，而小说有了新的连载会主动推送给读者。
     * 这就是观察者模式。而RxJava正是基于观察者模式开发的。
     */

    /**
     1.在ReactiveX中，一个观察者(Observer)订阅一个可观察对象(Observable)。
     观察者对Observable发射的数据或数据序列作出响应。这种模式可以极大地简化并发操作，
     因为它创建了一个处于待命状态的观察者哨兵，在未来某个时刻响应Observable的通知，
     不需要阻塞等待Observable发射数据。
     -------------------------------------------------------------------
     回调方法 (onNext, onCompleted, onError)
     Subscribe方法用于将观察者连接到Observable，你的观察者需要实现以下方法的一个子集：
     onNext(T item)
     Observable调用这个方法发射数据，方法的参数就是Observable发射的数据，
     这个方法可能会被调用多次，取决于你的实现。
     onError(Exception ex)
     当Observable遇到错误或者无法返回期望的数据时会调用这个方法，这个调用会终止Observable，后续不会再调用onNext和onCompleted，onError方法的参数是抛出的异常。
     onComplete
     正常终止，如果没有遇到错误，Observable在最后一次调用onNext之后调用此方法。根据Observable协议的定义，onNext可能会被调用零次或者很多次，最后会有一次onCompleted或onError调用（不会同时），传递数据给onNext通常被称作发射，onCompleted和onError被称作通知。

     取消订阅 (Unsubscribing)
     在一些ReactiveX实现中，有一个特殊的观察者接口Subscriber，它有一个unsubscribe方法。
     调用这个方法表示你不关心当前订阅的Observable了，
     因此Observable可以选择停止发射新的数据项（如果没有其它观察者订阅）。
     取消订阅的结果会传递给这个Observable的操作符链，而且会导致这个链条上的每个环节都停止发射数据项。
     这些并不保证会立即发生，然而，对一个Observable来说，即使没有观察者了，
     它也可以在一个while循环中继续生成并尝试发射数据项。

     Observables的"热"和"冷"
     Observable什么时候开始发射数据序列？这取决于Observable的实现，
     一个"热"的Observable可能一创建完就开始发射数据，
     因此所有后续订阅它的观察者可能从序列中间的某个位置开始接受数据（有一些数据错过了）。
     一个"冷"的Observable会一直等待，直到有观察者订阅它才开始发射数据，
     因此这个观察者可以确保会收到整个数据序列。
     在一些ReactiveX实现里，还存在一种被称作Connectable的Observable，不管有没有观察者订阅它，
     这种Observable都不会开始发射数据，除非Connect方法被调用。

     RxJava
     在RxJava中，一个实现了Observer接口的对象可以订阅(subscribe)一个Observable 类的实例。
     订阅者(subscriber)对Observable发射(emit)的任何数据或数据序列作出响应。
     这种模式简化了并发操作，因为它不需要阻塞等待bservable发射数据，
     而是创建了一个处于待命状态的观察者哨兵，哨兵在未来某个时刻响应Observable的通知。
     -----------------------------------------------------------------------------
     */


    /**
        Single:
     Single类似于Observable，不同的是，它总是只发射一个值，或者一个错误通知，而不是发射一系列的值。
     因此，不同于Observable需要三个方法onNext, onError, onCompleted，订阅Single只需要两个方法：
     onSuccess - Single发射单个的值到这个方法
     onError - 如果无法发射需要的值，Single发射一个Throwable对象到这个方法
     Single只会调用这两个方法中的一个，而且只会调用一次，调用了任何一个方法之后，订阅关系终止。
     ---------------------

     */

    /*
    rxJava1:
    private void testSigngleOperator() {
        Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> singleSubscriber) {
                singleSubscriber.onSuccess("success");
            }
        }).subscribe(new SingleSubscriber<String>() {
            @Override
            public void onSuccess(String value) {
                System.out.print("onSuccess:" + value);
            }

            @Override
            public void onError(Throwable error) {
                System.out.print("onError:" + error);
            }
        });
    }
    */

    /*
        Single也是可以使用onNext, onError, onCompleted这个回调的：
        onNext:success
        onCompleted

     private void testSigngleOperator(){
        Single.create(new Single.OnSubscribe<String>() {
            @Override
            public void call(SingleSubscriber<? super String> singleSubscriber) {
                singleSubscriber.onSuccess("success");
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError:"+e.getMessage());
            }

            @Override
            public void onNext(String s) {
                System.out.println("onNext:"+s);
            }
        });
    }


     */


    /**

     Subject可以看成是一个桥梁或者代理，它同时充当了Observer和Observable的角色。
     因为它是一个Observer，它可以订阅一个或多个Observable；
     又因为它是一个Observable，它可以转发它收到(Observe)的数据，也可以发射新的数据。
     由于一个Subject订阅一个Observable，
     它可以触发这个Observable开始发射数据（如果那个Observable是"冷"的--就是说，它等待有订阅才开始发射数据）。
     因此有这样的效果，Subject可以把原来那个"冷"的Observable变成"热"的。

     针对不同的场景Subject一共有四种类型：AsyncSubject、BehaviorSubject、PublishSubject
     和ReplaySubject。

     */


    /*
    1、AsyncSubject
    一个AsyncSubject只在原始Observable完成后，发射来自原始Observable的最后一个值。
    （如果原始Observable没有发射任何值，AsyncSubject也不发射任何值）它会把这最后一个值发射给任何后续的观察者。
    然而，如果原始的Observable因为发生了错误而终止，AsyncSubject将不会发射任何数据，
    只是简单的向前传递这个错误通知。
    2，BehaviorSubject
    当观察者订阅BehaviorSubject时，它开始发射原始Observable最近发射的数据（如果此时还没有收到任何数据，
    它会发射一个默认值），然后继续发射其它任何来自原始Observable的数据。然而，
    如果原始的Observable因为发生了一个错误而终止，BehaviorSubject将不会发射任何数据，
    只是简单的向前传递这个错误通知。
    3.PublishSubject
        PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者。
        需要注意的是，PublishSubject可能会一创建完成就立刻开始发射数据（除非你可以阻止它发生），
        因此这里有一个风险：在Subject被创建后到有观察者订阅它之前这个时间段内，一个或多个数据可能会丢失。
        如果要确保来自原始Observable的所有数据都被分发，你需要这样做：
        或者使用Create创建那个Observable以便手动给它引入"冷"Observable的行为
        （当所有观察者都已经订阅时才开始发射数据），或者改用ReplaySubject。
    4，ReplaySubject
        ReplaySubject会发射所有来自原始Observable的数据给观察者，无论它们是何时订阅的。
        也有其它版本的ReplaySubject，在重放缓存增长到一定大小的时候或过了一段时间后会丢弃旧的数据
        （原始Observable发射的）。
        如果你把ReplaySubject当作一个观察者使用，注意不要从多个线程中调用它的onNext方法（包括其它的on系列方法），
        这可能导致同时（非顺序）调用，这会违反Observable协议，给Subject的结果增加了不确定性。
     */

    /**
     四、Scheduler
     如果你想给Observable操作符链添加多线程功能，你可以指定操作符（或者特定的Observable）
     在特定的调度器(Scheduler)上执行。
     某些ReactiveX的Observable操作符有一些变体，它们可以接受一个Scheduler参数。
     这个参数指定操作符将它们的部分或全部任务放在一个特定的调度器上执行。
     使用ObserveOn和SubscribeOn操作符，你可以让Observable在一个特定的调度器上执行，
     ObserveOn指示一个Observable在一个特定的调度器上调用观察者的onNext, onError和onCompleted方法，
     SubscribeOn更进一步，它指示Observable将全部的处理过程（包括发射数据和通知）放在特定的调度器上执行。

     下表展示了RxJava中可用的调度器种类：

     调度器类型	效果
     Schedulers.computation( )	用于计算任务，如事件循环或和回调处理，不要用于IO操作(IO操作请使用Schedulers.io())；默认线程数等于处理器的数量
     Schedulers.from(executor)	使用指定的Executor作为调度器
     Schedulers.immediate( )	在当前线程立即开始执行任务
     Schedulers.io( )	用于IO密集型任务，如异步阻塞IO操作，这个调度器的线程池会根据需要增长；对于普通的计算任务，请使用Schedulers.computation()；Schedulers.io( )默认是一个CachedThreadScheduler，很像一个有线程缓存的新线程调度器
     Schedulers.newThread( )	为每个任务创建一个新线程
     Schedulers.trampoline( )	当其它排队的任务完成后，在当前线程排队开始执行

     */


}
