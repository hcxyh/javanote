package com.xyh.java.jdk8.stream;


/**
 * Java8 Stream Note
 */
public class StreamNote {

    /*

        Stream跟Iterator的差别是
            1.无存储：Stream是基于数据源的对象，它本身不存储数据元素，
            而是通过管道将数据源的元素传递给操作。
            2.函数式编程：对Stream的任何修改都不会修改背后的数据源，
            比如对Stream执行filter操作并不会删除被过滤的元素，
            而是会产生一个不包含被过滤元素的新的Stream。
            3.延迟执行：Stream的操作由零个或多个中间操作（intermediate operation）
            和一个结束操作（terminal operation）两部分组成。
            只有执行了结束操作，Stream定义的中间操作才会依次执行，这就是Stream的延迟特性。
            4.可消费性：Stream只能被“消费”一次，一旦遍历过就会失效。就像容器的迭代器那样，
            想要再次遍历必须重新生成一个新的Stream。


          Stream的操作是建立在函数式接口的组合之上的。Java8中新增的函数式接口都在java.util.function包下。
          这些函数式接口可以有多种分类方式

          Java 8有多种方式来创建Stream：
            1.通过集合的stream()方法或者parallelStream()
            2.使用流的静态方法，比如Stream.of(Object[]), IntStream.range(int, int) 或者
            Stream.iterate(Object, UnaryOperator)。
            3.通过Arrays.stream(Object[])方法。
            4.BufferedReader.lines()从文件中获得行的流。
            5.Files类的操作路径的方法，如list、find、walk等。
            6.随机数流Random.ints()。
            7.其它一些类提供了创建流的方法，如BitSet.stream(), Pattern.splitAsStream(java.lang.CharSequence), 和 JarFile.stream()。
            其实最终都是依赖底层的StreamSupport类来完成Stream创建。


     */


}
