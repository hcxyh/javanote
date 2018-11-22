package com.xyh.reactor.rxjava.rxjava2examples;

import io.reactivex.*;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.apache.tools.ant.taskdefs.Antlib.TAG;

public class Demo1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(Demo1.class);

    /*
    1、Nulls
    这是一个很大的变化，熟悉 RxJava 1.x 的童鞋一定都知道，
    1.x 是允许我们在发射事件的时候传入 null 值的，但现在我们的 2.x 不支持了，不信你试试？
    大大的 NullPointerException 教你做人。
    这意味着 Observable<Void> 不再发射任何值，而是正常结束或者抛出空指针。
    2、Flowable
        在 RxJava 1.x 中关于介绍 backpressure 部分有一个小小的遗憾，那就是没有用一个单独的类，
        而是使用 Observable 。而在 2.x 中 Observable 不支持背压了，
        将用一个全新的 Flowable 来支持背压。或许对于背压，有些小伙伴们还不是特别理解，
        这里简单说一下。
        大概就是指在异步场景中，被观察者发送事件的速度远快于观察者的处理速度的情况下，
        一种告诉上游的被观察者降低发送速度的策略。感兴趣的小伙伴可以模拟这种情况，
        在差距太大的时候，我们的内存会猛增，直到OOM。
        而我们的 Flowable 一定意义上可以解决这样的问题，但其实并不能完全解决，这个后面可能会提到。
    3.Single/Completable/Maybe
        其实这三者都差不多，Single 顾名思义，只能发送一个事件，
        和 Observable接受可变参数完全不同。而 Completable 侧重于观察结果，
        而 Maybe 是上面两种的结合体。也就是说，
        当你只想要某个事件的结果（true or false）的时候，你可以使用这种观察者模式。
    4.线程调度相关
        这一块基本没什么改动，但细心的小伙伴一定会发现，
        RxJava 2.x 中已经没有了 Schedulers.immediate() 这个线程环境，
        还有 Schedulers.test()。
    5.Function相关
        熟悉 1.x 的小伙伴一定都知道，我们在1.x 中是有 Func1，Func2.....FuncN的，
        但 2.x 中将它们移除，而采用 Function 替换了 Func1，
        采用 BiFunction 替换了 Func 2..N。并且，它们都增加了 throws Exception，
        也就是说，妈妈再也不用担心我们做某些操作还需要 try-catch 了。
        其他操作符相关
        如 Func1...N 的变化，现在同样用 Consumer 和 BiConsumer 对 Action1 和 Action2 进行了替换。
        后面的 Action 都被替换了，只保留了 ActionN。
    6.Create
        create 操作符应该是最常见的操作符了，主要用于产生一个 Obserable 被观察者对象，
        为了方便大家的认知，以后的教程中统一把被观察者 Observable 称为发射器（上游事件），
        观察者 Observer 称为接收器（下游事件）。


     */

    public void create(){

        StringBuffer mRxOperatorsText = new StringBuffer();

        Observable.create(new ObservableOnSubscribe<Integer>() {  //创建发布者和被观察者
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                mRxOperatorsText.append("Observable emit 1" + "\n");
                LOGGER.info(TAG, "Observable emit 1" + "\n");
                e.onNext(1);
                mRxOperatorsText.append("Observable emit 2" + "\n");
                LOGGER.info(TAG, "Observable emit 2" + "\n");
                e.onNext(2);
                mRxOperatorsText.append("Observable emit 3" + "\n");
                LOGGER.info(TAG, "Observable emit 3" + "\n");
                e.onNext(3);
                e.onComplete();
                mRxOperatorsText.append("Observable emit 4" + "\n");
                LOGGER.info(TAG, "Observable emit 4" + "\n" );
                e.onNext(4);
            }
        }).subscribe(new Observer<Integer>() {  //创建订阅者和观察者
            private int i;
            private Disposable mDisposable;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mRxOperatorsText.append("onSubscribe : " + d.isDisposed() + "\n");
                LOGGER.info(TAG, "onSubscribe : " + d.isDisposed() + "\n" );
                mDisposable = d;
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                mRxOperatorsText.append("onNext : value : " + integer + "\n");
                LOGGER.info(TAG, "onNext : value : " + integer + "\n" );
                i++;
                if (i == 2) {
                    // 在RxJava 2.x 中，新增的Disposable可以做到切断的操作，让Observer观察者不再接收上游事件
                    mDisposable.dispose();
                    mRxOperatorsText.append("onNext : isDisposable : " + mDisposable.isDisposed() + "\n");
                    LOGGER.info(TAG, "onNext : isDisposable : " + mDisposable.isDisposed() + "\n");
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                mRxOperatorsText.append("onError : value : " + e.getMessage() + "\n");
                LOGGER.info(TAG, "onError : value : " + e.getMessage() + "\n" );
            }

            @Override
            public void onComplete() {
                mRxOperatorsText.append("onComplete" + "\n");
                LOGGER.info(TAG, "onComplete" + "\n" );
            }
        });
    /*
        在发射事件中，我们在发射了数值 3 之后，直接调用了 e.onComlete()，
        虽然无法接收事件，但发送事件还是继续的。
        另外一个值得注意的点是，在 RxJava 2.x 中，可以看到发射事件方法相比 1.x 多了一个 throws Excetion，
        意味着我们做一些特定操作再也不用 try-catch 了。
        并且 2.x 中有一个 Disposable 概念，这个东西可以直接调用切断，可以看到，当它的  isDisposed() 返回为 false 的时候，
        接收器能正常接收事件，但当其为 true 的时候，接收器停止了接收。所以可以通过此参数动态控制接收事件了。
     */
    }


    /*
    Map
        Map 基本算是 RxJava 中一个最简单的操作符了，熟悉 RxJava 1.x 的知道，它的作用是对发射时间发送的每一个事件应用一个函数，
        是的每一个事件都按照指定的函数去变化，而在 2.x 中它的作用几乎一致。
     */
    public void testMap(){

        StringBuffer mRxOperatorsText = new StringBuffer();

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).map(new Function<Integer, String>() {  //修改发布事件的类型
            @Override
            public String apply(@NonNull Integer integer) throws Exception {
                return "This is result " + integer;
            }
        }).subscribe(new Consumer<String>() {  //修改订阅事件的类型
            @Override
            public void accept(@NonNull String s) throws Exception {
                mRxOperatorsText.append("accept : " + s +"\n");
                LOGGER.info(TAG, "accept : " + s +"\n" );
            }
        });
    }
    /*
    是的，map 基本作用就是将一个 Observable 通过某种函数关系，转换为另一种 Observable，
    上面例子中就是把我们的 Integer 数据变成了 String 类型。从Log日志显而易见。
    Zip
        zip 专用于合并事件，该合并不是连接（连接操作符后面会说），而是两两配对，也就意味着，
        最终配对出的 Observable 发射事件数目只和少的那个相同。
     */
    public void testZip(){
        StringBuffer mRxOperatorsText = new StringBuffer();

        Observable.zip(getStringObservable(), getIntegerObservable(), new BiFunction<String, Integer, String>() {
            @Override
            public String apply(@NonNull String s, @NonNull Integer integer) throws Exception {
                return s + integer;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                mRxOperatorsText.append("zip : accept : " + s + "\n");
                LOGGER.info(TAG, "zip : accept : " + s + "\n");
            }
        });
    }

    private Observable<String> getStringObservable() {
        StringBuffer mRxOperatorsText = new StringBuffer();
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                if (!e.isDisposed()) {
                    e.onNext("A");
                    mRxOperatorsText.append("String emit : A \n");
                    LOGGER.info(TAG, "String emit : A \n");
                    e.onNext("B");
                    mRxOperatorsText.append("String emit : B \n");
                    LOGGER.info(TAG, "String emit : B \n");
                    e.onNext("C");
                    mRxOperatorsText.append("String emit : C \n");
                    LOGGER.info(TAG, "String emit : C \n");
                }
            }
        });
    }

    private Observable<Integer> getIntegerObservable() {
        StringBuffer mRxOperatorsText = new StringBuffer();
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (!e.isDisposed()) {
                    e.onNext(1);
                    mRxOperatorsText.append("Integer emit : 1 \n");
                    LOGGER.info(TAG, "Integer emit : 1 \n");
                    e.onNext(2);
                    mRxOperatorsText.append("Integer emit : 2 \n");
                    LOGGER.info(TAG, "Integer emit : 2 \n");
                    e.onNext(3);
                    mRxOperatorsText.append("Integer emit : 3 \n");
                    LOGGER.info(TAG, "Integer emit : 3 \n");
                    e.onNext(4);
                    mRxOperatorsText.append("Integer emit : 4 \n");
                    LOGGER.info(TAG, "Integer emit : 4 \n");
                    e.onNext(5);
                    mRxOperatorsText.append("Integer emit : 5 \n");
                    LOGGER.info(TAG, "Integer emit : 5 \n");
                }
            }
        });
    }
    /*
    zip 组合事件的过程就是分别从发射器 A 和发射器 B 各取出一个事件来组合，
    并且一个事件只能被使用一次，组合的顺序是严格按照事件发送的顺序来进行的，
    所以上面截图中，可以看到，1 永远是和 A 结合的，2 永远是和 B 结合的。
    最终接收器收到的事件数量是和发送器发送事件最少的那个发送器的发送事件数目相同，
    所以如截图中，5 很孤单，没有人愿意和它交往，孤独终老的单身狗
    Concat
        对于单一的把两个发射器连接成一个发射器，虽然 zip 不能完成，但我们还是可以自力更生，
        官方提供的 concat 让我们的问题得到了完美解决。
     */
    public void testConcat(){
        StringBuffer mRxOperatorsText = new StringBuffer();
        Observable.concat(Observable.just(1,2,3), Observable.just(4,5,6))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        mRxOperatorsText.append("concat : "+ integer + "\n");
                        LOGGER.info(TAG, "concat : "+ integer + "\n" );
                    }
                });
    }

    /*
    FlatMap
        FlatMap 是一个很有趣的东西，我坚信你在实际开发中会经常用到。
        它可以把一个发射器 Observable 通过某种方法转换为多个 Observables，
        然后再把这些分散的 Observables装进一个单一的发射器 Observable。但有个需要注意的是，
        flatMap 并不能保证事件的顺序，如果需要保证，需要用到我们下面要讲的 ConcatMap。
     */
    public void testFlatMap(){
        StringBuffer mRxOperatorsText = new StringBuffer();

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                int delayTime = (int) (1 + Math.random() * 10);
                return Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
            }
        }).subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        LOGGER.info(TAG, "flatMap : accept : " + s + "\n");
                        mRxOperatorsText.append("flatMap : accept : " + s + "\n");
                    }
                });
        /*
        一切都如我们预期中的有意思，为了区分 concatMap（下一个会讲），我在代码中特意动了一点小手脚，
        我采用一个随机数，生成一个时间，然后通过 delay（后面会讲）操作符，做一个小延时操作，
        而查看 Log 日志也确认验证了我们上面的说法，它是无序的。

        concatMap
        上面其实就说了，concatMap 与 FlatMap 的唯一区别就是 concatMap 保证了顺序，
        所以，我们就直接把 flatMap 替换为 concatMap 验证吧。
         */
    }

    public  void testConcatMap(){
        StringBuffer mRxOperatorsText = new StringBuffer();
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("I am value " + integer);
                }
                int delayTime = (int) (1 + Math.random() * 10);
                return Observable.fromIterable(list).delay(delayTime, TimeUnit.MILLISECONDS);
            }
        }).subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        LOGGER.info(TAG, "flatMap : accept : " + s + "\n");
                        mRxOperatorsText.append("flatMap : accept : " + s + "\n");
                    }
                });
    }


    public void testDistinct(){
        StringBuffer mRxOperatorsText = new StringBuffer();
        Observable.just(1, 1, 1, 2, 2, 3, 4, 5)
                .distinct()
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                        mRxOperatorsText.append("distinct : " + integer + "\n");
                        LOGGER.info(TAG, "distinct : " + integer + "\n");
                    }
                });
    }

    /*
    Filter
    信我，Filter 你会很常用的，它的作用也很简单，过滤器嘛。可以接受一个参数，让其过滤掉不符合我们条件的值
     */
    public void testFilter(){
        StringBuffer mRxOperatorsText = new StringBuffer();
        Observable.just(1, 20, 65, -5, 7, 19)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer >= 10;
                    }
                }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer integer) throws Exception {
                mRxOperatorsText.append("filter : " + integer + "\n");
                LOGGER.info(TAG, "filter : " + integer + "\n");
            }
        });
    }


    /*
        buffer
        buffer 操作符接受两个参数，buffer(count,skip)，作用是将 Observable 中的数据
        按 skip (步长) 分成最大不超过 count 的 buffer ，然后生成一个  Observable 。也许你还不太理解，
        我们可以通过我们的示例图和示例代码来进一步深化它。
     */
    public void testBuffer(){
        StringBuffer mRxOperatorsText = new StringBuffer();
        Observable.just(1, 2, 3, 4, 5)
                .buffer(3, 2)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(@NonNull List<Integer> integers) throws Exception {
                        mRxOperatorsText.append("buffer size : " + integers.size() + "\n");
                        LOGGER.info(TAG, "buffer size : " + integers.size() + "\n");
                        mRxOperatorsText.append("buffer value : ");
                        LOGGER.info(TAG, "buffer value : " );
                        for (Integer i : integers) {
                            mRxOperatorsText.append(i + "");
                            LOGGER.info(TAG, i + "");
                        }
                        mRxOperatorsText.append("\n");
                        LOGGER.info(TAG, "\n");
                    }
                });

    }
    /*
        如图，我们把 1, 2, 3, 4, 5 依次发射出来，经过 buffer 操作符，
        其中参数 skip 为 2， count 为 3，而我们的输出 依次是 123，345，5。显而易见，
        我们 buffer 的第一个参数是 count，代表最大取值，在事件足够的时候，
        一般都是取 count 个值，然后每次跳过 skip 个事件。其实看 Log 日志，我相信大家都明白了。

        timer
        timer 很有意思，相当于一个定时任务。在 1.x 中它还可以执行间隔逻辑，但在 2.x 中此功能被交给了 interval，
        下一个会介绍。但需要注意的是，timer 和 interval 均默认在新线程。

    public void testTimer(){
        mRxOperatorsText.append("timer start : " + TimeUtil.getNowStrTime() + "\n");
        Log.e(TAG, "timer start : " + TimeUtil.getNowStrTime() + "\n");
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()) // timer 默认在新线程，所以需要切换回主线程
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {
                        mRxOperatorsText.append("timer :" + aLong + " at " + TimeUtil.getNowStrTime() + "\n");
                        Log.e(TAG, "timer :" + aLong + " at " + TimeUtil.getNowStrTime() + "\n");
                    }
                });
    }


    interval
        如同我们上面可说，interval 操作符用于间隔时间执行某个操作，其接受三个参数，分别是第一次发送延迟，间隔时间，时间单位。
    */

}
