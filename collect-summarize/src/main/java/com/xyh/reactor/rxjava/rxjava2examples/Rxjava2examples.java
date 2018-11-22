package com.xyh.reactor.rxjava.rxjava2examples;

import com.google.gson.Gson;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.SchedulerSupport;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.operators.observable.ObservableSubscribeOn;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.function.Consumer;

import static org.apache.tools.ant.taskdefs.Antlib.TAG;

public class Rxjava2examples {

    private static  final Logger LOGGER = LoggerFactory.getLogger(Rxjava2examples.class);


    /**
     jdk9:
     Publisher是能够发出元素的发布者，Subscriber是接收元素并做出响应的订阅者。
     当执行Publisher里的subscribe方法时，发布者会回调订阅者的onSubscribe方法，
     这个方法中，通常订阅者会借助传入的Subscription向发布者请求n个数据。
     然后发布者通过不断调用订阅者的onNext方法向订阅者发出最多n个数据。如果数据全部发完，
     则会调用onComplete告知订阅者流已经发完；如果有错误发生，则通过onError发出错误数据，同样也会终止流。

     其中，Subscription相当于是连接Publisher和Subscriber的“纽带”。因为当发布者调用subscribe方法注册订阅者时，
     会通过订阅者的回调方法onSubscribe传入Subscription对象，
     之后订阅者就可以使用这个Subscription对象的request方法向发布者“要”数据了。背压机制正是基于此来实现的。

     Processor则是集Publisher和Subscriber于一身，相当于是发布者与订阅者之间的一个”中间人“，可以通过Processor进行一些中间操作.
     public static interface Processor<T,R> extends Subscriber<T>, Publisher<R> {}



     */


    /*

     接口变化
     RxJava 2.x 拥有了新的特性，其依赖于4个基础接口，它们分别是

     Publisher          发布者(生产者)
     Subscriber         订阅者(消费者)
     Subscription       发布者与订阅者之间的关系纽带，订阅令牌
     Processor          数据处理器
     其中最核心的莫过于 Publisher 和 Subscriber。Publisher 可以发出一系列的事件，而 Subscriber 负责和处理这些事件。
     其中用的比较多的自然是 Publisher 的 Flowable，它支持背压。关于背压给个简洁的定义就是：
     背压是指在异步场景中，被观察者发送事件速度远快于观察者的处理速度的情况下，一种告诉上游的被观察者降低发送速度的策略。
     简而言之，背压是流速控制的一种策略。
     可以明显地发现，RxJava 2.x 最大的改动就是对于 backpressure 的处理，为此将原来的  Observable
     拆分成了新的 Observable 和 Flowable，同时其他相关部分也同时进行了拆分，但令人庆幸的是，是它，是它，还是它，
     还是我们最熟悉和最喜欢的 RxJava。

    观察者模式
        大家可能都知道， RxJava 以观察者模式为骨架，在 2.0 中依旧如此。

        不过此次更新中，出现了两种观察者模式：

        Observable ( 被观察者 ) / Observer ( 观察者 )
        Flowable （被观察者）/ Subscriber （观察者）

        在 RxJava 2.x 中，Observable 用于订阅 Observer，不再支持背压（1.x 中可以使用背压策略），
        而 Flowable 用于订阅 Subscriber ， 是支持背压（Backpressure）的。
     Observable
        在 RxJava 1.x 中，我们最熟悉的莫过于 Observable 这个类了，笔者在刚刚使用 RxJava 2.x 的时候，
        创建了 一个 Observable，瞬间一脸懵逼有木有，居然连我们最最熟悉的 Subscriber 都没了，
        取而代之的是 ObservableEmmiter，俗称发射器。此外，由于没有了Subscriber的踪影，
        我们创建观察者时需使用 Observer。而 Observer 也不是我们熟悉的那个 Observer，又出现了一个 Disposable 参数带你装逼带你飞。
     废话不多说，从会用开始，还记得 RxJava 的三部曲吗？
        {
            ** 第一步：初始化 Observable **
            ** 第二步：初始化 Observer **
            ** 第三步：建立订阅关系 **
        }

     */
    public void test() {

        /*
            在发射事件中，我们在发射了数值 3 之后，直接调用了 e.onComlete()，虽然无法接收事件，但发送事件还是继续的。
            另外一个值得注意的点是，在 RxJava 2.x 中，可以看到发射事件方法相比 1.x 多了一个 throws Excetion，
            意味着我们做一些特定操作再也不用 try-catch 了。
            并且 2.x 中有一个 Disposable 概念，这个东西可以直接调用切断，可以看到，当它的  isDisposed() 返回为 false 的时候，
            接收器能正常接收事件，但当其为 true 的时候，接收器停止了接收。所以可以通过此参数动态控制接收事件了。

         */

        Observable.create(new ObservableOnSubscribe<Integer>() { // 第一步：初始化Observable
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
//                Log.e(TAG, "Observable emit 1" + "\n");
                LOGGER.info("Observable emit 1" + "\n");
                e.onNext(1);
                LOGGER.info(TAG, "Observable emit 2" + "\n");
                e.onNext(2);
                LOGGER.info(TAG, "Observable emit 3" + "\n");
                e.onNext(3);
                e.onComplete();
                LOGGER.info(TAG, "Observable emit 4" + "\n");
                e.onNext(4);
            }
        }).subscribe(new Observer<Integer>() { // 第三步：订阅

            // 第二步：初始化Observer
            private int i;
            private Disposable mDisposable;

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                LOGGER.info("建立订阅关系,build subscribe");
                mDisposable = d;
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                i++;
                if (i == 2) {
                    // 在RxJava 2.x 中，新增的Disposable可以做到切断的操作，让Observer观察者不再接收上游事件
                    mDisposable.dispose();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                LOGGER.info(TAG, "onError : value : " + e.getMessage() + "\n");
            }

            @Override
            public void onComplete() {
                LOGGER.info(TAG, "onComplete" + "\n");
            }
        });

    }

    /*
    不难看出，RxJava 2.x 与 1.x 还是存在着一些区别的。首先，创建 Observable 时，回调的是 ObservableEmitter ，
    字面意思即发射器，并且直接 throws Exception。其次，在创建的 Observer 中，也多了一个回调方法：onSubscribe，
    传递参数为Disposable，Disposable 相当于 RxJava 1.x 中的 Subscription， 用于解除订阅。
    可以看到示例代码中，在 i 自增到 2 的时候，订阅关系被切断。

    当然，我们的 RxJava 2.x 也为我们保留了简化订阅方法，我们可以根据需求，进行相应的简化订阅，只不过传入对象改为了 Consumer。
    Consumer 即消费者，用于接收单个值，BiConsumer 则是接收两个值，Function 用于变换对象，Predicate 用于判断。
    这些接口命名大多参照了 Java 8 ，熟悉 Java 8 新特性的应该都知道意思，这里也不再赘述。

    线程调度
    关于线程切换这点，RxJava 1.x 和 RxJava 2.x 的实现思路是一样的。这里简单的说一下，以便于我们的新司机入手。
    subScribeOn
        同 RxJava 1.x 一样，subscribeOn 用于指定 subscribe() 时所发生的线程，从源码角度可以看出，
        内部线程调度是通过 ObservableSubscribeOn来实现的。
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> subscribeOn(Scheduler scheduler) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        return RxJavaPlugins.onAssembly(new ObservableSubscribeOn<T>(this, scheduler));
    }
    ObservableSubscribeOn 的核心源码在 subscribeActual 方法中，通过代理的方式使用 SubscribeOnObserver 包装 Observer 后，
    设置 Disposable 来将 subscribe 切换到 Scheduler 线程中。

    observeOn
        observeOn 方法用于指定下游 Observer 回调发生的线程。
    @SchedulerSupport(SchedulerSupport.CUSTOM)
    public final Observable<T> observeOn(Scheduler scheduler, boolean delayError, int bufferSize) {
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObjectHelper.verifyPositive(bufferSize, "bufferSize");
        return RxJavaPlugins.onAssembly(new ObservableObserveOn<T>(this, scheduler, delayError, bufferSize));
    }

    线程切换需要注意的：
        RxJava 内置的线程调度器的确可以让我们的线程切换得心应手，但其中也有些需要注意的地方。
            1.简单地说，subscribeOn() 指定的就是发射事件的线程，observerOn 指定的就是订阅者接收事件的线程。
            2.多次指定发射事件的线程只有第一次指定的有效，也就是说多次调用 subscribeOn() 只有第一次的有效，其余的会被忽略。
            3.但多次指定订阅者接收线程是可以的，也就是说每调用一次 observerOn()，下游的线程就会切换一次。

     */
    public void  test1() throws Exception {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                LOGGER.info(TAG, "Observable thread is : " + Thread.currentThread().getName());
                e.onNext(1);
                e.onComplete();
            }
        }).subscribeOn(Schedulers.newThread())   //发布者的线程
                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(new Consumer<Integer>() {
//                    @Override
//                    public void accept(@NonNull Integer integer) throws Exception {
//                        Log.e(TAG, "After observeOn(mainThread)，Current thread is " + Thread.currentThread().getName());
//                    }
//                })
                .observeOn(Schedulers.io())
                .subscribe(
//                        new Consumer<Integer>() {
//                    @Override
//                    public void accept(@NonNull Integer integer)  {
//                        LOGGER.info(TAG, "After observeOn(io)，Current thread is " + Thread.currentThread().getName());
//                    }
//                }
                        (i) -> {}
                );
    }
    /*
    07-03 14:54:01.177 15121-15438/com.nanchen.rxjava2examples E/RxThreadActivity: Observable thread is : RxNewThreadScheduler-1
    07-03 14:54:01.178 15121-15121/com.nanchen.rxjava2examples E/RxThreadActivity: After observeOn(mainThread)，Current thread is main
    07-03 14:54:01.179 15121-15439/com.nanchen.rxjava2examples E/RxThreadActivity: After observeOn(io)，Current thread is RxCachedThreadScheduler-2

        实例代码中，分别用 Schedulers.newThread() 和 Schedulers.io() 对发射线程进行切换，
        并采用 observeOn(AndroidSchedulers.mainThread() 和 Schedulers.io() 进行了接收线程的切换。
        可以看到输出中发射线程仅仅响应了第一个 newThread，但每调用一次 observeOn() ，线程便会切换一次，
        因此如果我们有类似的需求时，便知道如何处理了。

        RxJava 中，已经内置了很多线程选项供我们选择，例如有：

        Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作；
        Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作；
        Schedulers.newThread() 代表一个常规的新线程；
        AndroidSchedulers.mainThread() 代表Android的主线程
        这些内置的 Scheduler 已经足够满足我们开发的需求，因此我们应该使用内置的这些选项，
        而 RxJava 内部使用的是线程池来维护这些线程，所以效率也比较高。

      操作符
        关于操作符，在官方文档中已经做了非常完善的讲解，并且笔者前面的系列教程中也着重讲解了绝大多数的操作符作用，这里受于篇幅限制，
        就不多做赘述，只挑选几个进行实际情景的讲解:
            1.map
            map 操作符可以将一个 Observable 对象通过某种关系转换为另一个Observable 对象。
            在 2.x 中和 1.x 中作用几乎一致，不同点在于：2.x 将 1.x 中的 Func1 和 Func2 改为了 Function 和 BiFunction。
        采用 map 操作符进行网络数据解析
            想必大家都知道，很多时候我们在使用 RxJava 的时候总是和 Retrofit 进行结合使用，而为了方便演示，这里我们就暂且采用 OkHttp3 进行演示，
            配合 map，doOnNext ，线程切换进行简单的网络请求：
            1）通过 Observable.create() 方法，调用 OkHttp 网络请求；
            2）通过 map 操作符集合 gson，将 Response 转换为 bean 类；
            3）通过 doOnNext() 方法，解析 bean 中的数据，并进行数据库存储等操作；
            4）调度线程，在子线程中进行耗时操作任务，在主线程中更新 UI ；
            5）通过 subscribe()，根据请求成功或者失败来更新 UI 。
       [
            Observable.create(new ObservableOnSubscribe<Response>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Response> e) throws Exception {
                Builder builder = new Builder()
                        .url("http://api.avatardata.cn/MobilePlace/LookUp?key=ec47b85086be4dc8b5d941f5abd37a4e&mobileNumber=13021671512")
                        .get();
                Request request = builder.build();
                Call call = new OkHttpClient().newCall(request);
                Response response = call.execute();
                e.onNext(response);
            }
            }).map(new Function<Response, MobileAddress>() {
                @Override
                public MobileAddress apply(@NonNull Response response) throws Exception {
                    if (response.isSuccessful()) {
                        ResponseBody body = response.body();
                        if (body != null) {
                            LOGGER.info(TAG, "map:转换前:" + response.body());
                            return new Gson().fromJson(body.string(), MobileAddress.class);
                        }
                    }
                    return null;
                }
            }).observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(new Consumer<MobileAddress>() {
                        @Override
                        public void accept(@NonNull MobileAddress s) throws Exception {
                            LOGGER.e(TAG, "doOnNext: 保存成功：" + s.toString() + "\n");
                        }
                    }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<MobileAddress>() {
                        @Override
                        public void accept(@NonNull MobileAddress data) throws Exception {
                            LOGGER.e(TAG, "成功:" + data.toString() + "\n");
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable throwable) throws Exception {
                                LOGGER.e(TAG, "失败：" + throwable.getMessage() + "\n");
                            }
                        });
       ]


       采用 concat 操作符先读取缓存再通过网络请求获取数据
            想必在实际应用中，很多时候（对数据操作不敏感时）都需要我们先读取缓存的数据，如果缓存没有数据，
            再通过网络请求获取，随后在主线程更新我们的UI。
       concat 操作符简直就是为我们这种需求量身定做。
            利用 concat 的必须调用 onComplete 后才能订阅下一个 Observable 的特性，我们就可以先读取缓存数据，
            倘若获取到的缓存数据不是我们想要的，再调用 onComplete() 以执行获取网络数据的 Observable，
            如果缓存数据能应我们所需，则直接调用 onNext()，防止过度的网络请求，浪费用户的流量。

       Observable<FoodList> cache = Observable.create(new ObservableOnSubscribe<FoodList>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<FoodList> e) throws Exception {
                Log.e(TAG, "create当前线程:"+Thread.currentThread().getName() );
                FoodList data = CacheManager.getInstance().getFoodListData();

                // 在操作符 concat 中，只有调用 onComplete 之后才会执行下一个 Observable
                if (data != null){ // 如果缓存数据不为空，则直接读取缓存数据，而不读取网络数据
                    isFromNet = false;
                    Log.e(TAG, "\nsubscribe: 读取缓存数据:" );
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRxOperatorsText.append("\nsubscribe: 读取缓存数据:\n");
                        }
                    });

                    e.onNext(data);
                }else {
                    isFromNet = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRxOperatorsText.append("\nsubscribe: 读取网络数据:\n");
                        }
                    });
                    Log.e(TAG, "\nsubscribe: 读取网络数据:" );
                    e.onComplete();
                }


            }
        });

        Observable<FoodList> network = Rx2AndroidNetworking.get("http://www.tngou.net/api/food/list")
                .addQueryParameter("rows",10+"")
                .build()
                .getObjectObservable(FoodList.class);


        // 两个 Observable 的泛型应当保持一致

        Observable.concat(cache,network)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FoodList>() {
                    @Override
                    public void accept(@NonNull FoodList tngouBeen) throws Exception {
                        Log.e(TAG, "subscribe 成功:"+Thread.currentThread().getName() );
                        if (isFromNet){
                            mRxOperatorsText.append("accept : 网络获取数据设置缓存: \n");
                            Log.e(TAG, "accept : 网络获取数据设置缓存: \n"+tngouBeen.toString() );
                            CacheManager.getInstance().setFoodListData(tngouBeen);
                        }

                        mRxOperatorsText.append("accept: 读取数据成功:" + tngouBeen.toString()+"\n");
                        Log.e(TAG, "accept: 读取数据成功:" + tngouBeen.toString());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(TAG, "subscribe 失败:"+Thread.currentThread().getName() );
                        Log.e(TAG, "accept: 读取数据失败："+throwable.getMessage() );
                        mRxOperatorsText.append("accept: 读取数据失败："+throwable.getMessage()+"\n");
                    }
                });

     */

}
