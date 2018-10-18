package com.xyh.optimize;


/**
 * Akka in action
 *	并发和并行的区别
 * 在学习使用springBatch 的时候，了解到step可以使用并行parall执行.
 *  TODO 
 *  记录对于并发和并行的理解.
 *
 */
public class ConcurrencyANDParallellism {
	
/**
  
  并发和并行的区别：
     所有的并发处理都有排队等候，唤醒和执行这三个步骤，所以并发是宏观的观念，在微观上他们都是序列被处理的，只不过资源不会在某一个上被阻塞（一般是通过时间片轮转），所以在宏观上多个几乎同时到达的请求同时在被处理。如果是同一时刻到达的请求也会根据优先级的不同，先后进入队列排队等候执行。

     并发与并行是两个既相似但是却不相同的概念：

         并发性：又称共行性，是指处理多个同时性活动的能力，。

         并行：指同时发生两个并发事件，具有并发的含义。并发不一定并行，也可以说并发事件之间不一定要同一时刻发生。 

     并发的实质是一个物理CPU（也可以是多个物理CPU）在若干个程序之间多路复用，并发性是对有限物理资源强制行使 多用户共享以提高效率。

     并行指两个或两个以上事件或活动在同一时刻发生，在多道程序环境下，并行使多个程序同一时刻可在不同CPU上同时执行。    

     并发是在同一个cpu上同时（不是真正的同时，而是看来是同时，因为CPU要在多个程序之间切换）运行多个程序。

     并行是每一个CPU运行一个程序。

     打个比方：并发就像一个人（CPU）喂两个小孩（程序）吃饭，表面上是两个小孩在吃饭，实际是一个人在喂。

                   并行就是两个人喂两个小孩子吃饭。 
                   
                   
    并发、并行和多线程的关系：
     并行需要两个或两个以上的线程跑在不同的处理器上，并发可以跑在一个处理器上通过时间片进行切换。
     
     1.并发
     Concurrency，是并发的意思。并发的实质是一个物理CPU(也可以多个物理CPU) 在若干道程序（或线程）之间多路复用，并发性是对有限物理资源强制行使多用户共享以提高效率。
微观角度：所有的并发处理都有排队等候，唤醒，执行等这样的步骤，在微观上他们都是序列被处理的，如果是同一时刻到达的请求（或线程）也会根据优先级的不同，而先后进入队列排队等候执行。
宏观角度：多个几乎同时到达的请求（或线程）在宏观上看就像是同时在被处理。
通俗点讲，并发就是只有一个CPU资源，程序（或线程）之间要竞争得到执行机会。图中的第一个阶段，在A执行的过程中B，C不会执行，因为这段时间内这个CPU资源被A竞争到了，同理，第二个阶段只有B在执行，第三个阶段只有C在执行。其实，并发过程中，A，B，C并不是同时在进行的（微观角度）。但又是同时进行的（宏观角度）。

	2.并行
	Parallelism，即并行，指两个或两个以上事件（或线程）在同一时刻发生，是真正意义上的不同事件或线程在同一时刻，在不同CPU资源呢上（多核），同时执行。
并行，不存在像并发那样竞争，等待的概念。
图中，A，B，C都在同时运行（微观，宏观）。
	
	3.通过多线程实现并发和并行
	
	java中的Thread类定义了多线程，通过多线程可以实现并发或并行。
在CPU比较繁忙，资源不足的时候（开启了很多进程），操作系统只为一个含有多线程的进程分配仅有的CPU资源，这些线程就会为自己尽量多抢时间片，这就是通过多线程实现并发，线程之间会竞争CPU资源争取执行机会。
在CPU资源比较充足的时候，一个进程内的多线程，可以被分配到不同的CPU资源，这就是通过多线程实现并行。
至于多线程实现的是并发还是并行？上面所说，所写多线程可能被分配到一个CPU内核中执行，也可能被分配到不同CPU执行，分配过程是操作系统所为，不可人为控制。所有，如果有人问我我所写的多线程是并发还是并行的？我会说，都有可能。
不管并发还是并行，都提高了程序对CPU资源的利用率，最大限度地利用CPU资源。
  
  并发(Concurrency):看起来是同时运行(不一定多核) 并行(Parallelism):
  真的同时运行(必须多核) 可以理解为:并行是并发的子集(真的同时运行,那么看起来也是同时运行) 我们的编程叫做并发编程,
  运行时可能是并发运行,也可能是并行运行
  
 
 */

}