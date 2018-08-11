package com.xyh.java.thread.lock;

/**
 * 提供读锁、写锁。读锁是共享锁，写锁是排他锁。
 * 多个读锁不互斥，读锁写锁互斥，写锁写锁互斥。可以提高程序的运行效率
 * @author hcxyh  2018年8月11日
 *
 */
public class ReadWriteLockNote {

	/**
	 * 
		1.ReadWriteLock管理一组锁，一个是只读的锁，一个是写锁。读锁可以在没有写锁的时候被多个线程同时持有，写锁是独占的。
		2.读写锁比互斥锁允许对于共享数据更大程度的并发。每次只能有一个写线程，但是同时可以有多个线程并发地读数据。
		ReadWriteLock适用于读多写少的并发情况。 	  
		3.所有读写锁的实现必须确保写操作对读操作的内存影响。
		换句话说，一个获得了读锁的线程必须能看到前一个释放的写锁所更新的内容。	
		ReentrantReadWriteLock有如下特性： 
		- 获取顺序 
		- 非公平模式（默认） 
			当以非公平初始化时，读锁和写锁的获取的顺序是不确定的。非公平锁主张竞争获取，
			可能会延缓一个或多个读或写线程，但是会比公平锁有更高的吞吐量。 
				- 公平模式 
			当以公平模式初始化时，线程将会以队列的顺序获取锁。当当前线程释放锁后，
			等待时间最长的写锁线程就会被分配写锁；或者有一组读线程组等待时间比写线程长，
			那么这组读线程组将会被分配读锁。 
			当有写线程持有写锁或者有等待的写线程时，一个尝试获取公平的读锁（非重入）的线程就会阻塞。
			这个线程直到等待时间最长的写锁获得锁后并释放掉锁后才能获取到读锁。 
				- 可重入 
			允许读锁可写锁可重入。写锁可以获得读锁，读锁不能获得写锁。 
				- 锁降级 
			允许写锁降低为读锁 
				- 中断锁的获取 
			在读锁和写锁的获取过程中支持中断 
				- 支持Condition 
			写锁提供Condition实现 
				- 监控 
			提供确定锁是否被持有等辅助方法
	 */
	
	/**
	 	 public ReentrantReadWriteLock() {
	        this(false);
	  }
	  public ReentrantReadWriteLock(boolean fair) {
	        sync = fair ? new FairSync() : new NonfairSync();
	        readerLock = new ReadLock(this);
	        writerLock = new WriteLock(this);
	  }
	  1.默认使用非公平模式,初始化读锁和写锁.此时,
	  public ReentrantReadWriteLock.WriteLock writeLock() { return writerLock; }
      public ReentrantReadWriteLock.ReadLock  readLock()  { return readerLock; }
	  
	  2.构造方法决定了Sync是FairSync还是NonfairSync。
	  Sync继承了AbstractQueuedSynchronizer，
	      而Sync是一个抽象类，NonfairSync和FairSync继承了Sync
	  
	  3.sync分析
		  	static final class FairSync extends Sync {
		        private static final long serialVersionUID = -2274990926593161451L;
		        final boolean writerShouldBlock() {
		            return hasQueuedPredecessors();
		        }
		        final boolean readerShouldBlock() {
		            return hasQueuedPredecessors();
		        }
		    }
		   writerShouldBlock和readerShouldBlock方法都表示当有别的线程也在尝试获取锁时，是否应该阻塞。 
		   a)对于公平模式，hasQueuedPredecessors()方法表示前面是否有等待线程。一旦前面有等待线程，那么为了遵循公平，当前线程也就应该被挂起。
		   
		    static final class NonfairSync extends Sync {
		        private static final long serialVersionUID = -8159625535654395037L;
		        final boolean writerShouldBlock() {
		            return false; // writers can always barge
		        }
		        final boolean readerShouldBlock() {
		            /* As a heuristic to avoid indefinite writer starvation,
		             * block if the thread that momentarily appears to be head
		             * of queue, if one exists, is a waiting writer.  This is
		             * only a probabilistic effect since a new reader will not
		             * block if there is a waiting writer behind other enabled
		             * readers that have not yet drained from the queue.
		             *  
		            return apparentlyFirstQueuedIsExclusive();
		        }
		    }
		    非公平模式下，writerShouldBlock直接返回false，说明不需要阻塞；而readShouldBlock调用了apparentFirstQueuedIsExcluisve()方法。
		    该方法在当前线程是写锁占用的线程时，返回true；否则返回false。也就说明，如果当前有一个写线程正在写，那么该读线程应该阻塞。 
	  */
	
	
	/**
	 * TODO 源码分析,未完待续
	 	ReadLock
	 		1.public void lock() {
	            sync.acquireShared(1);
	        }
	       读锁使用的是AQS的共享模式，AQS的acquireShared方法如下：
 			if (tryAcquireShared(arg) < 0)
            	doAcquireShared(arg);
                   当tryAcquireShared()方法小于0时，那么会执行doAcquireShared方法将该线程加入到等待队列中。 Sync实现了tryAcquireShared方法，如下：
	 		protected final int tryAcquireShared(int unused) {
		            Thread current = Thread.currentThread();
		            int c = getState();
		            //如果当前有写线程并且本线程不是写线程，不符合重入，失败
		            if (exclusiveCount(c) != 0 &&
		                getExclusiveOwnerThread() != current)
		                return -1;
		            //得到读锁的个数
		            int r = sharedCount(c);
		            //如果读不应该阻塞并且读锁的个数小于最大值65535，并且可以成功更新状态值,成功
		            if (!readerShouldBlock() &&
		                r < MAX_COUNT &&
		                compareAndSetState(c, c + SHARED_UNIT)) {
		                //如果当前读锁为0
		                if (r == 0) {
		                    //第一个读线程就是当前线程
		                    firstReader = current;
		                    firstReaderHoldCount = 1;
		                }
		                //如果当前线程重入了，记录firstReaderHoldCount
		                else if (firstReader == current) {
		                    firstReaderHoldCount++;
		                }
		                //当前读线程和第一个读线程不同,记录每一个线程读的次数
		                else {
		                    HoldCounter rh = cachedHoldCounter;
		                    if (rh == null || rh.tid != getThreadId(current))
		                        cachedHoldCounter = rh = readHolds.get();
		                    else if (rh.count == 0)
		                        readHolds.set(rh);
		                    rh.count++;
		                }
		                return 1;
		            }
		            //否则，循环尝试
		            return fullTryAcquireShared(current);
        }
        1. 如果当前有写线程并且本线程不是写线程，那么失败，返回-1 
		2. 否则，说明当前没有写线程或者本线程就是写线程（可重入）,接下来判断是否应该读线程阻塞并且读锁的个数是否小于最小值，并且CAS成功使读锁+1，成功，返回1。其余的操作主要是用于计数的 
		3. 如果2中失败了，失败的原因有三，第一是应该读线程应该阻塞；第二是因为读锁达到了上线；第三是因为CAS失败，有其他线程在并发更新state，那么会调动fullTryAcquireShared方法。
        
        final int fullTryAcquireShared(Thread current) {

            HoldCounter rh = null;
            for (;;) {
                int c = getState();
                //一旦有别的线程获得了写锁，返回-1，失败
                if (exclusiveCount(c) != 0) {
                    if (getExclusiveOwnerThread() != current)
                        return -1;
                } 
                //如果读线程需要阻塞
                else if (readerShouldBlock()) {
                    // Make sure we're not acquiring read lock reentrantly
                    if (firstReader == current) {
                        // assert firstReaderHoldCount > 0;
                    }
                    //说明有别的读线程占有了锁
                    else {
                        if (rh == null) {
                            rh = cachedHoldCounter;
                            if (rh == null || rh.tid != getThreadId(current)) {
                                rh = readHolds.get();
                                if (rh.count == 0)
                                    readHolds.remove();
                            }
                        }
                        if (rh.count == 0)
                            return -1;
                    }
                }
                //如果读锁达到了最大值，抛出异常
                if (sharedCount(c) == MAX_COUNT)
                    throw new Error("Maximum lock count exceeded");
                //如果成功更改状态，成功返回
                if (compareAndSetState(c, c + SHARED_UNIT)) {
                    if (sharedCount(c) == 0) {
                        firstReader = current;
                        firstReaderHoldCount = 1;
                    } else if (firstReader == current) {
                        firstReaderHoldCount++;
                    } else {
                        if (rh == null)
                            rh = cachedHoldCounter;
                        if (rh == null || rh.tid != getThreadId(current))
                            rh = readHolds.get();
                        else if (rh.count == 0)
                            readHolds.set(rh);
                        rh.count++;
                        cachedHoldCounter = rh; // cache for release
                    }
                    return 1;
                }
            }
        }
	 
	 
	 */
	
}
