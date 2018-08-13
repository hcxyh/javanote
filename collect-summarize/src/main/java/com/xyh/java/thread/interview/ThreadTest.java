package com.xyh.java.thread.interview;

/**
 * 同步是指在多个线程并发访问共享数据时，保证共享数据在同一个时刻只被一条线程（或是一些，使用信号量的时候）线程使用。
 * @author hcxyh  2018年8月13日
 */
public class ThreadTest implements Runnable{
	
	int b = 100;
	synchronized void m1() throws InterruptedException {
		b = 1000;
		Thread.sleep(1000);
		System.out.println("m1 ： b" + b);
	}
	
	synchronized void m2() throws InterruptedException {
		Thread.sleep(500);
		b = 2000;
		System.out.println("m2： b" + b);
	}
	
	public static void main(String[] args) throws InterruptedException {
		ThreadTest tt = new ThreadTest();
		new Thread(tt).start();
		
		tt.m2();
		
		System.out.println("main thread b=" + tt.b);
		
	}
	

	@Override
	public void run() {
		try {
			m1();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 还是要理解线程的几个状态:
	 	new,runnable(thread.start()),running,blocking(Thread.Sleep())
	 	1. 新建一个线程t, 此时线程t为new状态。
		2. 调用t.start()，将线程至于runnable状态。
		3. 这里有个争议点到点是t线程先执行还是tt.m2先执行呢，我们知道此时线程t还是runnable状态，此时还没有被cpu调度，但是我们的tt.m2()是我们本地的方法代码，此时一定是tt.m2()先执行。
		4. 执行tt.m2()进入synchronized同步代码块，开始执行代码，这里的sleep()没啥用就是混淆大家视野的，此时b=2000。
		5. 在执行tt.m2()的时候。有两个情况：
		情况A：有可能t线程已经在执行了，但是由于m2先进入了同步代码块，这个时候t进入阻塞状态，然后主线程也将会执行输出，这个时候又有一个争议到底是谁先执行？是t先执行还是主线程，这里有小伙伴就会把第3点拿出来说，肯定是先输出啊,t线程不是阻塞的吗，调度到CPU肯定来不及啊？很多人忽略了一点，synchronized其实是在1.6之后做了很多优化的，其中就有一个自旋锁，就能保证不需要让出CPU，有可能刚好这部分时间和主线程输出重合，并且在他之前就有可能发生，b先等于1000，这个时候主线程输出其实就会有两种情况。2000 或者 1000。
		情况B：有可能t还没执行，tt.m2()一执行完，他刚好就执行，这个时候还是有两种情况。b=2000或者1000
		6. 在t线程中不论哪种情况，最后肯定会输出1000，因为此时没有修改1000的地方了。
		
	 */
	
	/**
	 * 1.何为线程安全
		当多个线程访问一个对象时，如果不用考虑这些线程在运行时环境下的调度和交替执行，也不需要进行额外的同步，
		或者在调用方进行任何其它的协调操作，调用这个对象的行为都可以获得正确的结果，那这个对象就是线程安全的。
		从上我们可以得知：
			1. 在什么样的环境:多个线程的环境下。
			2. 在什么样的操作:多个线程调度和交替执行。
			3. 发生什么样的情况: 可以获得正确结果。
			4. 谁：线程安全是用来描述对象是否是线程安全。
		2.线程安全性
		我们可以按照java共享对象的安全性，将线程安全分为五个等级：
		不可变、绝对线程安全、相对线程安全、线程兼容、线程对立：
			1. 不可变
			在java中Immutable（不可变）对象一定是线程安全的，这是因为线程的调度和交替执行不会对对象造成任何改变。
			同样不可变的还有自定义常量,final及常池中的对象同样都是不可变的。
			在java中一般枚举类，String都是常见的不可变类型，同样的枚举类用来实现单例模式是天生自带的线程安全,
			在String对象中你无论调用replace(),subString()都无法修改他原来的值
			2.绝对线程安全
			《java concurrency practice》：
			当多个线程访问某个类时，不管运行时环境采用何种调度方式或者这些线程将如何交替进行，并且在主调代码中不需要任何额外的同步或协同，这个类都能表现出正确的行为，那么称这个类是线程安全的。
			《》
	 */
	
}
