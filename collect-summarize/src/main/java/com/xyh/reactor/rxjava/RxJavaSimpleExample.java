package com.xyh.reactor.rxjava;


import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * https://www.infoq.com/articles/rxjava2-by-example
 */
public class RxJavaSimpleExample {


    public static void test(){
        Observable<String> hello = Observable.just("Howdy!");

        hello.subscribe( (str) -> System.out.println(str));

        Observable.just("Hello", "World")
                .subscribe(System.out::println);

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
        Observable.just(words).subscribe(System.out::println);
        //[the, quick, brown, fox, jumped, over, the, lazy, dog]
        /**
         * 我们期待每个单词作为单独的发射，但是我们得到了包含整个列表的单个发射。为了纠正这个问题，
         * 我们调用了更合适的fromIterable 方法：
         * 它将数组或iterable转换为一系列事件，每个元素一个。
         * 在rxjava1中有一个重载from方法。这已经被几种风格所取代from， 包括fromIterable 和  fromArray
         */
        Observable.fromIterable(words)
                .subscribe(System.out::println);
        /**
         zip使用成对的“zip”转换映射将源流的元素与提供的流的元素组合在一起，您可以以Lambda的形式提供。
         当其中任何一个流完成时，压缩流完成，因此来自其他流的任何剩余事件都将丢失。
         zip  最多可接受九个源流和zip操作。有一个相应的zipWith运算符，它将提供的流与现有流压缩。
         回到我们的例子。我们可以使用range和zipWith来添加我们的行号，使用String.format作为我们的zip转换：
         */
        Observable.fromIterable(words)
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        (string, count)->String.format("%2d. %s", count, string))
                .subscribe(System.out::println);
        /**
         现在让我们说我们想要列出的不是单词，而是列出包含这些单词的字母。这是flatMap的一项工作，
         它从Observable获取排放（对象，集合或数组），并将这些元素映射到单个Observable，
         然后将所有这些元素的排放平坦化为单个Observable。
         对于我们的示例，我们将使用split将每个单词转换为其组件字符的数组。
         然后我们将flatMap创建一个新的Observable，它包含所有单词的所有字符：
         */
        Observable.fromIterable(words)
                .flatMap(word -> Observable.fromArray(word.split("")))
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        (string, count) -> String.format("%2d. %s", count, string))
                .subscribe(System.out::println);

        /**
         * 所有单词都出现并占据。但是数据太多了，我们只想要不同的字母：
         */
        Observable.fromIterable(words)
                .flatMap(word -> Observable.fromArray(word.split("")))
                .distinct()
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        (string, count) -> String.format("%2d. %s", count, string))
                .subscribe(System.out::println);
        /**
         * 作为一个孩子，我被告知我们的“快速棕色狐狸”短语包含英文字母中的每个字母，但我们看到只有25个不是26个。
         * 让我们对它们进行排序以帮助找到丢失的字母：
         */
        Observable.fromIterable(words)
                .flatMap(word -> Observable.fromArray(word.split("")))
//                .flatMap(word -> Observable.fromIterable(word.split("")))
                .distinct()
                .sorted()
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        (string, count) -> String.format("%2d. %s", count, string))
                .subscribe(System.out::println);
    }


    public void test1() throws InterruptedException {

//        让我们创建两个Observable，快速和慢速，然后应用过滤来安排和合并它们。
//        我们将使用Observable.interval操作，该操作生成每个指定数量的时间单位的刻度（计数从0开始的连续Long s 。）

        Observable<Long> fast = Observable.interval(1, TimeUnit.SECONDS);
        Observable<Long> slow = Observable.interval(3, TimeUnit.SECONDS);
//        快速会每秒发出一个事件，慢速会每三秒发出一次。（我们将忽略事件的Long值，我们只对时间感兴趣。）
//        现在我们可以通过合并这两个observable来生成我们的切分钟，每个都应用一个过滤器，告诉快速流在工作日（或15秒）打勾，慢速打到周末（或替代15秒） 。
        Observable<Long> clock = Observable.merge(
                slow.filter(tick-> isSlowTickTime()),
                fast.filter(tick-> !isSlowTickTime())
        );
//        最后，让我们添加一个订阅来打印时间。启动它将根据我们要求的时间表打印系统日期和时间。

        clock.subscribe(tick-> System.out.println(new Date()));
//        你还需要一个活着来防止它退出，所以添加一个

        Thread.sleep(6000);
//        到方法的末尾（并处理InterruptedException）。
    }

    public static void main(String[] args){

        //只有一个事件.
            Single single = Single.create(new SingleOnSubscribe<String>(){

            @Override
            public void subscribe(SingleEmitter<String> emitter) throws Exception {

            }
        });

        test();
    }

    //判断是否是工作日
    private static boolean isSlowTickTime() {
        return LocalDate.now().getDayOfWeek() == DayOfWeek.SATURDAY ||
                LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY;
    }
}
