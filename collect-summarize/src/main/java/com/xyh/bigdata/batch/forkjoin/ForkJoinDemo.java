package com.xyh.bigdata.batch.forkjoin;

import java.util.concurrent.ForkJoinTask;

/**
 * @author xyh
 */
public class ForkJoinDemo {
	
	/**
	 * fork（）用来创建子进程，使得系统进程可以多一个执行分支。在 Java 中也沿用了类似的命名方式。
		而 Join（） 的含义和 Thread 类的 join 类似，表示等待。也就是使用 fork（） 后系统多了一个执行分支（线程），
		所以需要等待这个执行分支执行完毕，才有可能得到最终的结果，因此 join 就是表示等待。
		在实际使用中，如果毫无顾忌的使用 fork 开启线程进行处理，那么很有可能导致系统开启过多的线程而严重影响性能。
		所以，在JDK中，给出一个 ForkJoinPool 线程池，对于 fork（） 方法并不急着开启线程，
		而是提交给 ForkJoiinPool 线程池进行处理，以节省系统资源。
		由于线程池的优化，提交的任务和线程数量并不是一对一的关系。在绝大多数情况下，
		一个物理线程实际上是需要处理多个逻辑任务的。因此，每个线程必然需要拥有一个任务队列。
		因此，在实际执行过程中，可能遇到这么一种情况：线程A已经把自己的任务都处理完了，而线程B还有一堆任务等着处理，
		此时，线程A就会“帮助” 线程B，从线程 B的任务队列中拿一个任务来处理，尽可能的达到平衡。
		值得注意的是：当线程试图帮助别人时，总是从任务队列的底部开始拿数据，而线程试图执行自己的任务时，则从相反的顶部开始拿。
		因此这种行为也十分有利于避免数据竞争。
	 */
	
//	ForkJoinPool  线程池方法
//	public <T> ForkJoinTask<T> submit(ForkJoinTask<T> task) {
//        if (task == null)
//            throw new NullPointerException();
//        externalPush(task);
//        return task;
//    }
	/**
	 	你可以向 ForkJoinPool 线程池提交一个 ForkJoinTask 任务。
	 	所谓 ForkJoinTask 任务就是支持 fork （） 分解以及 join（）等待的任务。 
	 	ForkJoinTask 有两个重要的子类，RecursiveAction 和 RecursiveTask。
	 	他们分别表示没有返回值的任务和可以携带返回值的任务。有点像 Rannable 和 Callable。
	 */
	
	
}
