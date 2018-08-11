
1.共享锁就是允许多个线程同时获取一个锁，一个锁可以同时被多个线程拥有。
排它锁，也称作独占锁，一个锁在某一时刻只能被一个线程占有，其它线程必须等待锁被释放之后才可能获取到锁。

2.ReentrantLock就是一种排它锁。CountDownLatch是一种共享锁。这两类都是单纯的一类，即，要么是排它锁，要么是共享锁。
ReentrantReadWriteLock是同时包含排它锁和共享锁特性的一种锁.
   	   我们使用ReentrantReadWriteLock的写锁时，使用的便是排它锁的特性；
   	   使用ReentrantReadWriteLock的读锁时，使用的便是共享锁的特性。
   	   
3.countdown和cyclicbarrier的比较
	1.CountDownLatch一般用于某个线程A等待若干个其他线程执行完任务之后，它才执行；
	而CyclicBarrier一般用于一组线程互相等待至某个状态，然后这一组线程再同时执行；
	CountDownLatch强调一个线程等多个线程完成某件事情。
	CyclicBarrier是多个线程互等，等大家都完成，再携手共进。
	2.调用CountDownLatch的countDown方法后，当前线程并不会阻塞，会继续往下执行；
	调用CyclicBarrier的await方法，会阻塞当前线程，直到CyclicBarrier指定的线程全部都到达了指定点的时候，才能继续往下执行；
	3.CountDownLatch方法比较少，操作比较简单，而CyclicBarrier提供的方法更多，
	比如能够通过getNumberWaiting()，isBroken()这些方法获取当前多个线程的状态，
	并且CyclicBarrier的构造方法可以传入barrierAction，指定当所有线程都到达时执行的业务功能；
	4.CountDownLatch是不能复用的，而CyclicLatch是可以复用的。

