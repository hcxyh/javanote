package com.xyh.reactor.rxjava.helloRxJava;

import io.reactivex.*;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.apache.commons.logging.Log;
import org.apache.poi.ss.formula.functions.T;

import static org.apache.tools.ant.taskdefs.Antlib.TAG;

public class Test1 {

    public void test1(){

        /**
         *
        Observable.create(new ObservableOnSubscribe()) //创建一个事件流，参数是我们创建的一个事件源
                .map()//有时我们会需要使用操作符进行变换
                .subscribeOn(Schedulers.io())//指定事件源代码执行的线程
                .observeOn(AndroidSchedulers.mainThread())//指定订阅者代码执行的线程
                .subscribe(new Observer())//参数是我们创建的一个订阅者，在这里与事件流建立订阅关系
        */


        //被观察者
//        Observable中文意思就是被观察者，通过create方法生成对象，
//        里面放的参数ObservableOnSubscribe<T>，可以理解为一个计划表，
//        泛型T是要操作对象的类型，重写subscribe方法，里面写具体的计划，
//        本文的例子就是推送连载1、连载2和连载3，
//        在subscribe中的ObservableEmitter<String>对象的Emitter是发射器的意思。
//        ObservableEmitter有三种发射的方法，
//        分别是void onNext(T value)、void onError(Throwable error)、void onComplete()，
//        onNext方法可以无限调用，
//        Observer（观察者）所有的都能接收到，
//        onError和onComplete是互斥的，Observer（观察者）只能接收到一个，
//        OnComplete可以重复调用，但是Observer（观察者）只会接收一次，
//        而onError不可以重复调用，第二次调用就会报异常。
        Observable novel = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("连载1");
                emitter.onNext("连载2");
                emitter.onNext("连载3");
                emitter.onComplete();
            }
        });

        //观察者
        Observer<String> reader=new Observer<String>() {
            Disposable  mDisposable = null;
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable=d;
//                Log.info(TAG,"onSubscribe");
            }

            @Override
            public void onNext(String value) {
                if ("2".equals(value)){
                    mDisposable.dispose();
                    return;
                }
//                Log.e(TAG,"onNext:"+value);
            }

            @Override
            public void onError(Throwable e) {
//                Log.e(TAG,"onError="+e.getMessage());
            }

            @Override
            public void onComplete() {
//                Log.e(TAG,"onComplete()");
            }
        };
//        通过new创建接口，并实现其内部的方法，看方法其实就应该差不多知道干嘛的，
//        onNext、onError、onComplete都是跟被观察者发射的方法一一对应的，这里就相当于接收了。
//        onSubscribe（Disposable d）里面的Disposable对象要说一下，
//        Disposable英文意思是可随意使用的，这里就相当于读者和连载小说的订阅关系，
//        如果读者不想再订阅该小说了，可以调用 mDisposable.dispose()取消订阅，
//        此时连载小说更新的时候就不会再推送给读者了。

        novel.subscribe(reader);//一行代码搞定
    }



    public void rsyncInvoke(){

//        RxJava是支持异步的，但是RxJava是如何做到的呢？这里就需要Scheduler。
//        Scheduler，英文名调度器，它是RxJava用来控制线程。当我们没有设置的时候，
//        RxJava遵循哪个线程产生就在哪个线程消费的原则，也就是说线程不会产生变化，
//        始终在同一个。然后我们一般使用RxJava都是后台执行，前台调用，本着这个原则，
//        我们需要调用observeOn(AndroidSchedulers.mainThread())，observeOn是事件回调的线程，
//        AndroidSchedulers.mainThread()一看就知道是主线程，subscribeOn(Schedulers.io())，
//        subscribeOn是事件执行的线程，Schedulers.io()是子线程，
//        这里也可以用Schedulers.newThread()，只不过io线程可以重用空闲的线程，
//        因此多数情况下 io() 比 newThread() 更有效率。
//        前面的代码根据异步和链式编程的原则，我们可以写成

        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("连载1");
                emitter.onNext("连载2");
                emitter.onNext("连载3");
                emitter.onComplete();
            }
        })
//                .observeOn(AndroidSchedulers.mainThread())
                .observeOn( new Scheduler(){ //事件回调的线程
                    @Override
                    public Worker createWorker() {
                        return null;
                    }
                })//回调在主线程
                .subscribeOn(Schedulers.io())//执行在io线程
                .subscribe(new Observer<String>() {
                    //subscribe方法里面有多重实现,如果只订阅者只关心onNext,查看重载
                    @Override
                    public void onSubscribe(Disposable d) {
//                        Log.(TAG,"onSubscribe");
                    }

                    @Override
                    public void onNext(String value) {
//                        Log.e(TAG,"onNext:"+value);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Log.e(TAG,"onError="+e.getMessage());
                    }

                    @Override
                    public void onComplete() {
//                        Log.e(TAG,"onComplete()");
                    }
                });

    }

}
