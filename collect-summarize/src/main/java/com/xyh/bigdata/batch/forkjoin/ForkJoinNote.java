package com.xyh.bigdata.batch.forkjoin;

public class ForkJoinNote {
	
	/**
	 * 需求:
	 * 假如目前有个需求，计算1000个数字之和，此需求是不是很简单，一次循环，即可完成计算；
	 * 但如果是计算100W甚至更多的呢？当然，此时的循环依然可以达到目的，但效率就不敢恭维；
	 * 同时，如果此时有个需求，需要统计100个文件中某个单词出现的次数呢？最直接的办法也是依次循环这100个文件，最终统计到结果，
	 * 更好一步，你应该想到了线程池处理，起10个线程，每个线程读10个文件统计，这样效率就提升10倍左右。
	 * 使用线程池可以很好的解决问题，但是对最终结果合并可能比较麻烦。幸好，JDK为我们提供了很好的并行解决方案fork/join框架。
	 * 介绍:
	 * Fork/Join主要功能即将一个任务拆分为多个子任务执行，最终再将子任务的执行结果合并（联想Hadoop的MapReduce）。
	 *  如果需要学习Fork/Join，首先需要了解如下四个类： 
	 *  - ForkJoinPoll：程序中只需要创建一个这样的实例来运行所有fork-join任务。 
	 *  - RecursiveTask：在线程池中运行一个本类的子类，它可以返回结果。  处理有返回值的任务
	 *  - RecursiveAction：和RecursiveTask类似但是不返回结果。 
	 *  - ForkJoinTask：是RecursiveTask和RecursiveAction的父类，join方法就是定义在本类中。
	 */
	
	/**
	 * public ForkJoinPool(int parallelism,
                        ForkJoinWorkerThreadFactory factory,
                        UncaughtExceptionHandler handler,
                        boolean asyncMode)
      
       1.parallelism：可并行级别，Fork/Join框架将依据这个并行级别的设定，决定框架内并行执行的线程数量。
       	并行的每一个任务都会有一个线程进行处理，但是千万不要将这个属性理解成Fork/Join框架中最多存在的线程数量，
       	也不要将这个属性和ThreadPoolExecutor线程池中的corePoolSize、maximumPoolSize属性进行比较，
       	因为ForkJoinPool的组织结构和工作方式与后者完全不一样。而后续的讨论中，
       	读者还可以发现Fork/Join框架中可存在的线程数量和这个参数值的关系并不是绝对的关联（有依据但并不全由它决定）。
	   2.factory：当Fork/Join框架创建一个新的线程时，同样会用到线程创建工厂。只不过这个线程工厂不再需要实现ThreadFactory接口，
	   	而是需要实现ForkJoinWorkerThreadFactory接口。后者是一个函数式接口，只需要实现一个名叫newThread的方法。
	   	在Fork/Join框架中有一个默认的ForkJoinWorkerThreadFactory接口实现：DefaultForkJoinWorkerThreadFactory。
	   3.handler：异常捕获处理器。当执行的任务中出现异常，并从任务中被抛出时，就会被handler捕获。
	   4.asyncMode：这个参数也非常重要，从字面意思来看是指的异步模式，它并不是说Fork/Join框架是采用同步模式还是采用异步模式工作。
	    Fork/Join框架中为每一个独立工作的线程准备了对应的待执行任务队列，这个任务队列是使用数组进行组合的双向队列。
	   	即是说存在于队列中的待执行任务，即可以使用先进先出的工作模式，也可以使用后进先出的工作模式。
		当asyncMode设置为ture的时候，队列采用先进先出方式工作；反之则是采用后进先出的方式工作，该值默认为false
	 */
	
	private void ForkJoi() {
		/**
		 * 不带有任何参数，对于最大并行任务数量也只是一个默认值——当前操作系统可以使用的CPU内核数量（Runtime.getRuntime().availableProcessors()）
		 */
	}
	
}
