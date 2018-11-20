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
	
	private void forkJoinNote() {
		/**
		 * 不带有任何参数，对于最大并行任务数量也只是一个默认值——当前操作系统可以使用的CPU内核数量（Runtime.getRuntime().availableProcessors()）
		 */
		
		/**
		 1.当硬件处理能力不能按照摩尔定律垂直发展的时候，选择了水平发展，多核处理器已经广泛应用。未来随着技术的进一步发展，可能出现成百上千个处理核心，
		 但现有的程序运行在多核心处理器上并不能得到较大性能的提升，主要的瓶颈在于程序本身的并发处理能力不强，不能够合理的利用多核心资源。
		 现有的处理方案是从软件入手，试图采用多线程，是程序在同一时间支持多个任务的计算，这种多线程的处理方案在处理器数目较少的情况下可以较为明显的提高应用性能，
		 但我们更加青睐于由硬件实现的多线程处理模式，但这一领域至今没有很好的结果。
 
		ForkJoin是Java7提供的原生多线程并行处理框架，其基本思想是将大人物分割成小任务，最后将小任务聚合起来得到结果。它非常类似于HADOOP提供的MapReduce框架，
		只是MapReduce的任务可以针对集群内的所有计算节点，可以充分利用集群的能力完成计算任务。ForkJoin更加类似于单机版的MapReduce。
		即使不通过mapreduce，仅有应用程序本身进行任务的分解与合成也是可以的，但从实现难度上考虑，自己实现可能会带来较大规模的复杂度，
		因此程序员急需一种范式来处理这一类的任务。在处理多线程中已经有了如AKKA这样的基于ACTOR模型的框架，而FORKJOIN则是针对具有明显可以进行任务分割特性需求的实现。
		
		其场景为：如果一个应用程序能够被分解成多个子任务，而且结合多个子任务的结果就能够得到最终的答案，那么它就适合使用FORK/JOIN模式来实现。
		ForkJoinTask: 我们要使用ForkJoin框架，必须首先创建一个ForkJoin任务。它提供在任务中执行fork()和join的操作机制，通常我们不直接继承ForkjoinTask类，只需要直接继承其子类。
    	1. RecursiveAction，用于没有返回结果的任务
    	2. RecursiveTask，用于有返回值的任务
		ForkJoinPool：task要通过ForkJoinPool来执行，分割的子任务也会添加到当前工作线程的双端队列中，进入队列的头部。当一个工作线程中没有任务时，会从其他工作线程的队列尾部获取一个任务。 
		  
		ForkJoin框架使用了工作窃取的思想（work-stealing），算法从其他队列中窃取任务来执行，其工作流图为：
		工作窃取（work-stealing）算法是指某个线程从其他队列里窃取任务来执行。
		{
			那么为什么需要使用工作窃取算法呢？假如我们需要做一个比较大的任务，我们可以把这个任务分割为若干互不依赖的子任务，为了减少线程间的竞争，
			于是把这些子任务分别放到不同的队列里，并为每个队列创建一个单独的线程来执行队列里的任务，线程和队列一一对应，
			比如A线程负责处理A队列里的任务。但是有的线程会先把自己队列里的任务干完，而其他线程对应的队列里还有任务等待处理。
			干完活的线程与其等着，不如去帮其他线程干活，于是它就去其他线程的队列里窃取一个任务来执行。
			而在这时它们会访问同一个队列，所以为了减少窃取任务线程和被窃取任务线程之间的竞争，通常会使用双端队列，
			被窃取任务线程永远从双端队列的头部拿任务执行，而窃取任务的线程永远从双端队列的尾部拿任务执行。
			工作窃取算法的优点是充分利用线程进行并行计算，并减少了线程间的竞争，其缺点是在某些情况下还是存在竞争，比如双端队列里只有一个任务时。
			并且消耗了更多的系统资源，比如创建多个线程和多个双端队列。
		}
		
		Fork/Join与传统线程池的区别！
		Fork/Join采用“工作窃取模式”，当执行新的任务时他可以将其拆分成更小的任务执行，并将小任务加到线程队列中，
		然后再从一个随即线程中偷一个并把它加入自己的队列中。
		就比如两个CPU上有不同的任务，这时候A已经执行完，B还有任务等待执行，这时候A就会将B队尾的任务偷过来，加入自己的队列中，
		对于传统的线程，ForkJoin更有效的利用的CPU资源！
		
		3. Fork/Join框架的介绍
		我们已经很清楚Fork/Join框架的需求了，那么我们可以思考一下，如果让我们来设计一个Fork/Join框架，该如何设计？这个思考有助于你理解Fork/Join框架的设计。
		第一步分割任务。首先我们需要有一个fork类来把大任务分割成子任务，有可能子任务还是很大，所以还需要不停的分割，直到分割出的子任务足够小。
		第二步执行任务并合并结果。分割的子任务分别放在双端队列里，然后几个启动线程分别从双端队列里获取任务执行。子任务执行完的结果都统一放在一个队列里，启动一个线程从队列里拿数据，然后合并这些数据。
		Fork/Join使用两个类来完成以上两件事情：
			ForkJoinTask：我们要使用ForkJoin框架，必须首先创建一个ForkJoin任务。它提供在任务中执行fork()和join()操作的机制，通常情况下我们不需要直接继承ForkJoinTask类，
			而只需要继承它的子类，Fork/Join框架提供了以下两个子类：
				RecursiveAction：用于没有返回结果的任务。
				RecursiveTask ：用于有返回结果的任务。
				ForkJoinPool ：ForkJoinTask需要通过ForkJoinPool来执行，任务分割出的子任务会添加到当前工作线程所维护的双端队列中，进入队列的头部。
				当一个工作线程的队列里暂时没有任务时，它会随机从其他工作线程的队列的尾部获取一个任务。
		
		5. Fork/Join框架的异常处理
		ForkJoinTask在执行的时候可能会抛出异常，但是我们没办法在主线程里直接捕获异常，所以ForkJoinTask提供了isCompletedAbnormally()方法来检查任务是否已经抛出异常或已经被取消了，
		并且可以通过ForkJoinTask的getException方法获取异常。使用如下代码
			if(task.isCompletedAbnormally())
				{
				    System.out.println(task.getException());
				}
			getException方法返回Throwable对象，如果任务被取消了则返回CancellationException。如果任务没有完成或者没有抛出异常则返回null。
			
		6. Fork/Join框架的实现原理
			ForkJoinPool由ForkJoinTask数组和ForkJoinWorkerThread数组组成，
			ForkJoinTask数组负责存放程序提交给ForkJoinPool的任务，而ForkJoinWorkerThread数组负责执行这些任务。
			
			ForkJoinTask的fork方法实现原理。当我们调用ForkJoinTask的fork方法时，程序会调用ForkJoinWorkerThread的pushTask方法异步的执行这个任务，然后立即返回结果。代码如下：
				public final ForkJoinTask fork() {
				        ((ForkJoinWorkerThread) Thread.currentThread())
				            .pushTask(this);
				        return this;
				}
			pushTask方法把当前任务存放在ForkJoinTask 数组queue里。然后再调用ForkJoinPool的signalWork()方法唤醒或创建一个工作线程来执行任务。代码如下：
				final void pushTask(ForkJoinTask t) {
				        ForkJoinTask[] q; 
				        int s, m;
				        if ((q = queue) != null) {    // ignore if queue removed
				            long u = (((s = queueTop) & (m = q.length - 1)) << ASHIFT) + ABASE;
				            UNSAFE.putOrderedObject(q, u, t);
				            queueTop = s + 1;         // or use putOrderedInt
				            if ((s -= queueBase) <= 2)
				                pool.signalWork();
				    else if (s == m)
				                growQueue();
				        }
				    }
			ForkJoinTask的join方法实现原理。Join方法的主要作用是阻塞当前线程并等待获取结果。让我们一起看看ForkJoinTask的join方法的实现，代码如下：
					public final V join() {
					        if (doJoin() != NORMAL)
					            return reportResult();
					        else
					            return getRawResult();
					}
					private V reportResult() {
					        int s; Throwable ex;
					        if ((s = status) == CANCELLED)
					            throw new CancellationException();
					if (s == EXCEPTIONAL && (ex = getThrowableException()) != null)
					            UNSAFE.throwException(ex);
					        return getRawResult();
					}
			首先，它调用了doJoin()方法，通过doJoin()方法得到当前任务的状态来判断返回什么结果，任务状态有四种：
			已完成（NORMAL），被取消（CANCELLED），信号（SIGNAL）和出现异常（EXCEPTIONAL）。
				
				如果任务状态是已完成，则直接返回任务结果。
				如果任务状态是被取消，则直接抛出CancellationException。
				如果任务状态是抛出异常，则直接抛出对应的异常。
				让我们再来分析下doJoin()方法的实现代码：
					private int doJoin() {
					        Thread t; ForkJoinWorkerThread w; int s; boolean completed;
					        if ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) {
					            if ((s = status) < 0)
					 return s;
					            if ((w = (ForkJoinWorkerThread)t).unpushTask(this)) {
					                try {
					                    completed = exec();
					                } catch (Throwable rex) {
					                    return setExceptionalCompletion(rex);
					                }
					                if (completed)
					                    return setCompletion(NORMAL);
					            }
					            return w.joinTask(this);
					        }
					        else
					            return externalAwaitDone();
					    }
		在doJoin()方法里，首先通过查看任务的状态，看任务是否已经执行完了，如果执行完了，则直接返回任务状态，如果没有执行完，则从任务数组里取出任务并执行。
		如果任务顺利执行完成了，则设置任务状态为NORMAL，如果出现异常，则纪录异常，并将任务状态设置为EXCEPTIONAL。
		
		 */
	}
	
}
