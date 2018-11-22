package com.xyh.java.jdk8;

import java.util.concurrent.CompletableFuture;

/**
 * @author xyh
 *
 */
public class CompletableFutureNote {
	
	/**
	 *  a异步, 
	 *  future , futureTask
	 *  @async  spring
	 */

	/*

	CompletableFuture
	public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)
	public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier,Executor executor)
	public static CompletableFuture<Void> runAsync(Runnable runnable)
	public static CompletableFuture<Void> runAsync(Runnable runnable,Executor executor);

	 其中supplyAsync()方法用于那些需要返回值的场景,比如计算某个数据,而runAsync()方法用于没有返回值的场景,比如,仅仅是简单地执行某一个异步动作.
     在这两对方法中,都有一个方法可以接手一个Executor参数,这使我们可以让Supplier<U>或者Runnable在指定的线程池工作,如果不指定,则在默认的系统公共的ForkJoinPool.common线程池中执行.
	*/


	/**
	 * future存在的缺陷：
	 	1.将两个异步计算合并为一个——这两个异步计算之间相互独立，同时第二个又依赖于第一个的结果。
	 	2.等待 Future 集合中的所有任务都完成。
	 	3.仅等待 Future 集合中最快结束的任务完成（有可能因为它们试图通过不同的方式计算同
	 一个值），并返回它的结果。
	 	4.通过编程方式完成一个 Future 任务的执行（即以手工设定异步操作结果的方式）。
	 	5.应对 Future 的完成事件（即当 Future 的完成事件发生时会收到通知，并能使用 Future
	 计算的结果进行下一步的操作，不只是简单地阻塞等待操作的结果）

	 */


		public static void test1() throws Exception{
			CompletableFuture<String> completableFuture=new CompletableFuture();
			new Thread(new Runnable() {
				@Override
				public void run() {
					//模拟执行耗时任务
					System.out.println("task doing...");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//告诉completableFuture任务已经完成
					completableFuture.complete("result");
				}
			}).start();
			//获取任务结果，如果没有完成会一直阻塞等待
			String result=completableFuture.get();
			System.out.println("计算结果:"+result);
		}

	/**
	 * 执行过程中:异常会被限制在执行任务的线程的范围内，最终会杀死该线程，而这会导致等待 get 方法返回结果的线程永久地被阻塞。
	 * 客户端可以使用重载版本的 get 方法，它使用一个超时参数来避免发生这样的情况。
	 * 使用这种方法至少能防止程序永久地等待下去，超时发生时，程序会得到通知发生了 Timeout-Exception 。不过，也因为如此，你不能指定执行任务的线程内到底发生了什么问题。
	 *
	 * 为了能获取任务线程内发生的异常，你需要使用
	 * CompletableFuture 的completeExceptionally方法将导致CompletableFuture 内发生问题的异常抛出。
	 * 这样，当执行任务发生异常时，调用get()方法的线程将会收到一个 ExecutionException 异常，
	 * 该异常接收了一个包含失败原因的Exception 参数。
	 *
	 */
	public static void test2() throws Exception{
		CompletableFuture<String> completableFuture=new CompletableFuture();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//模拟执行耗时任务
					System.out.println("task doing...");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					throw new RuntimeException("抛异常了");
				}catch (Exception e) {
					//告诉completableFuture任务发生异常了
					completableFuture.completeExceptionally(e);
				}
			}
		}).start();
		//获取任务结果，如果没有完成会一直阻塞等待
		String result=completableFuture.get();
		System.out.println("计算结果:"+result);
	}


	/**
	 工厂方法：
	 supplyAsync 方法接受一个生产者（Supplier）作为参数，返回一个 CompletableFuture
	 对象。生产者方法会交由 ForkJoinPool池中的某个执行线程（ Executor ）运行，
	 但是你也可以使用 supplyAsync 方法的重载版本，传递第二个参数指定线程池执行器执行生产者方法。

	 */

	public static void test3() throws Exception {
		//supplyAsync内部使用ForkJoinPool线程池执行任务
		CompletableFuture<String> completableFuture=CompletableFuture.supplyAsync(()->{
			//模拟执行耗时任务
			System.out.println("task doing...");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//返回结果
			return "result";
		});
		System.out.println("计算结果:"+completableFuture.get());
	}

	/**
	 回答上面的问题:
	    allOf是等待所有任务完成，构造后CompletableFuture完成
	    anyOf是只要有一个任务完成，构造后CompletableFuture就完成
	 	1.allOf 工厂方法接收一个由CompletableFuture 构成的数组，
	 	数组中的所有 Completable-Future 对象执行完成之后，
	 	它返回一个 CompletableFuture<Void> 对象。这意味着，
	 	如果你需要等待多个 CompletableFuture 对象执行完毕，对 allOf 方法返回的
	 	CompletableFuture 执行 join 操作可以等待CompletableFuture执行完成。
		2.anyOf接收一个 CompletableFuture 对象构成的数组，
	 	返回由第一个执行完毕的 CompletableFuture 对象的返回值构成的 CompletableFuture<Object> 。
	 */
	public static void test4() throws Exception {

		CompletableFuture<String> completableFuture1=CompletableFuture.supplyAsync(()->{
			//模拟执行耗时任务
			System.out.println("task1 doing...");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//返回结果
			return "result1";
		});

		CompletableFuture<String> completableFuture2=CompletableFuture.supplyAsync(()->{
			//模拟执行耗时任务
			System.out.println("task2 doing...");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//返回结果
			return "result2";
		});

		CompletableFuture<Object> anyResult=CompletableFuture.anyOf(completableFuture1,completableFuture2);

		System.out.println("第一个完成的任务结果:"+anyResult.get());

		CompletableFuture<Void> allResult=CompletableFuture.allOf(completableFuture1,completableFuture2);

		//阻塞等待所有任务执行完成
		allResult.join();
		System.out.println("所有任务执行完成");

	}


	/**
	 将两个CompletableFuture建立联系
	 通常，我们会有多个需要独立运行但又有所依赖的的任务。比如先等用于的订单处理完毕然后才发送邮件通知客户。
	 thenCompose 方法允许你对两个异步操作进行流水线，第一个操作完成时，将其结果作为参数传递给第二个操作。
	 你可以创建两个CompletableFutures 对象，对第一个 CompletableFuture 对象调用thenCompose ，并向其传递一个函数。
	 当第一个CompletableFuture 执行完毕后，它的结果将作为该函数的参数，
	 这个函数的返回值是以第一个 CompletableFuture 的返回做输入计算出的第二个 CompletableFuture 对象。
	 */
	public static void test5() throws Exception {

		CompletableFuture<String> completableFuture1=CompletableFuture.supplyAsync(()->{
			//模拟执行耗时任务
			System.out.println("task1 doing...");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//返回结果
			return "result1";
		});

		//等第一个任务完成后，将任务结果传给参数result，执行后面的任务并返回一个代表任务的completableFuture
		CompletableFuture<String> completableFuture2= completableFuture1.thenCompose(result->CompletableFuture.supplyAsync(()->{
			//模拟执行耗时任务
			System.out.println("task2 doing...");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//返回结果
			return "result2";
		}));

		System.out.println(completableFuture2.get());

	}

	/**
	 另一种比较常见的情况是，你需要将两个完
	 全不相干的 CompletableFuture 对象的结果整合起来，而且你也不希望等到第一个任务完全结
	 束才开始第二项任务。

	 这种情况，你应该使用 thenCombine 方法，它接收名为 BiFunction 的第二参数，这个参数
	 定义了当两个 CompletableFuture 对象完成计算后，结果如何合并。

	 */
	public static void test6() throws Exception {

		CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
			//模拟执行耗时任务
			System.out.println("task1 doing...");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//返回结果
			return 100;
		});

		//将第一个任务与第二个任务组合一起执行，都执行完成后，将两个任务的结果合并
		CompletableFuture<Integer> completableFuture2 = completableFuture1.thenCombine(
				//第二个任务
				CompletableFuture.supplyAsync(() -> {
					//模拟执行耗时任务
					System.out.println("task2 doing...");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//返回结果
					return 2000;
				}),
				//合并函数
				(result1, result2) -> result1 + result2);

		System.out.println(completableFuture2.get());
	}

	/**
	 响应 CompletableFuture 的 completion 事件
	 我们可以在每个CompletableFuture 上注册一个操作，该操作会在 CompletableFuture 完成执行后调用它。
	 CompletableFuture 通过 thenAccept 方法提供了这一功能，它接收
	 CompletableFuture 执行完毕后的返回值做参数。
	 */

	public static void test7() throws Exception {

		CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
			//模拟执行耗时任务
			System.out.println("task1 doing...");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//返回结果
			return 100;
		});

		//注册完成事件
		completableFuture1.thenAccept(result->System.out.println("task1 done,result:"+result));

		CompletableFuture<Integer> completableFuture2=
				//第二个任务
				CompletableFuture.supplyAsync(() -> {
					//模拟执行耗时任务
					System.out.println("task2 doing...");
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//返回结果
					return 2000;
				});

		//注册完成事件
		completableFuture2.thenAccept(result->System.out.println("task2 done,result:"+result));

		//将第一个任务与第二个任务组合一起执行，都执行完成后，将两个任务的结果合并
		CompletableFuture<Integer> completableFuture3 = completableFuture1.thenCombine(completableFuture2,
				//合并函数
				(result1, result2) -> {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return result1 + result2;
				});

		System.out.println(completableFuture3.get());
	}


	/*
	thenAccept()
	public CompletionStage<Void> thenAccept(Consumer<? super T> action);
	public CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action);
	public CompletionStage<Void> thenAcceptAsync(Consumer<? super T> action,Executor executor);
　　功能:当前任务正常完成以后执行,当前任务的执行结果可以作为下一任务的输入参数,无返回值.

　　场景:执行任务A,同时异步执行任务B,待任务B正常返回之后,用B的返回值执行任务C,任务C无返回值
	 */
	public void test10(){
		CompletableFuture<String> futureA = CompletableFuture.supplyAsync(() -> "任务A");
		CompletableFuture<String> futureB = CompletableFuture.supplyAsync(() -> "任务B");
		CompletableFuture<String> futureC = futureB.thenApply(b -> {
			System.out.println("执行任务C.");
			System.out.println("参数:" + b);//参数:任务B
			return "a";
		});
	}
	/*
	thenRun(..)
		public CompletionStage<Void> thenRun(Runnable action);
		public CompletionStage<Void> thenRunAsync(Runnable action);
		public CompletionStage<Void> thenRunAsync(Runnable action,Executor executor);
　　功能:对不关心上一步的计算结果，执行下一个操作
　　场景:执行任务A,任务A执行完以后,执行任务B,任务B不接受任务A的返回值(不管A有没有返回值),也无返回值

	 */
	public void test11(){
		CompletableFuture<String> futureA = CompletableFuture.supplyAsync(() -> "任务A");
		futureA.thenRun(() -> System.out.println("执行任务B"));
	}

	/*
	thenApply(..)
		public <U> CompletableFuture<U>     thenApply(Function<? super T,? extends U> fn)
		public <U> CompletableFuture<U>     thenApplyAsync(Function<? super T,? extends U> fn)
		public <U> CompletableFuture<U>     thenApplyAsync(Function<? super T,? extends U> fn, Executor executor)
　　功能:当前任务正常完成以后执行，当前任务的执行的结果会作为下一任务的输入参数,有返回值
　　场景:多个任务串联执行,下一个任务的执行依赖上一个任务的结果,每个任务都有输入和输出
　　实例1:异步执行任务A,当任务A完成时使用A的返回结果resultA作为入参进行任务B的处理,可实现任意多个任务的串联执行
	 */
	public void test12(){
		CompletableFuture<String> futureA = CompletableFuture.supplyAsync(() -> "hello");
		CompletableFuture<String> futureB = futureA.thenApply(s->s + " world");
		CompletableFuture<String> future3 = futureB.thenApply(String::toUpperCase);
		System.out.println(future3.join());
	}
}
