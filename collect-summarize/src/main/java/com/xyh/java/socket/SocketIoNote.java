package com.xyh.java.socket;


/**
 * 网络IO笔记
 * @author hcxyh  2018年8月14日
 *
 */
public class SocketIoNote {

	/**
	  1、同步和异步，阻塞和非阻塞
			同步和异步，阻塞和非阻塞, 这个几个词已经是老生常谈，当时常常还是有很多同学分不清楚，以为同步肯定就是阻塞，异步肯定就是非阻塞，其他他们不是一回事。

		同步和异步关注的是结果消息的通信机制
			同步：同步的意思就是调用方需要主动等待结果的返回
			异步：异步的意思就是不需要主动等待结果的返回，而是通过其他手段比如，状态通知，回调函数等。

		阻塞和非阻塞主要关注的是等待结果返回调用方的状态
			阻塞：是指结果返回之前，当前线程被挂起，不做任何事
			非阻塞：是指结果在返回之前，线程可以做一些其他事，不会被挂起。

		可以看见同步和异步，阻塞和非阻塞主要关注的点不同，有人会问同步还能非阻塞，异步还能阻塞？当然是可以的，下面为了更好的说明他们的组合之间的意思，用几个简单的例子说明：
		1. 同步阻塞：同步阻塞基本也是编程中最常见的模型，打个比方你去商店买衣服，你去了之后发现衣服卖完了，那你就在店里面一直等，期间不做任何事(包括看手机)，等着商家进货，直到有货为止，这个效率很低。
		2. 同步非阻塞：同步非阻塞在编程中可以抽象为一个轮询模式，你去了商店之后，发现衣服卖完了，这个时候不需要傻傻的等着，你可以去其他地方比如奶茶店，买杯水，但是你还是需要时不时的去商店问老板新衣服到了吗。
		3. 异步阻塞：异步阻塞这个编程里面用的较少，有点类似你写了个线程池,submit然后马上future.get()，这样线程其实还是挂起的。有点像你去商店买衣服，这个时候发现衣服没有了，这个时候你就给老板留给电话，说衣服到了就给我打电话，然后你就守着这个电话，一直等着他响什么事也不做。这样感觉的确有点傻，所以这个模式用得比较少。
		4. 异步非阻塞：异步非阻塞这也是现在高并发编程的一个核心，也是今天主要讲的一个核心。好比你去商店买衣服，衣服没了，你只需要给老板说这是我的电话，衣服到了就打。然后你就随心所欲的去玩，也不用操心衣服什么时候到，衣服一到，电话一响就可以去买衣服了。
	2、同步阻塞 PK 异步非阻塞
		上面已经看到了同步阻塞的效率是多么的低，如果使用同步阻塞的方式去买衣服，你有可能一天只能买一件衣服，
		其他什么事都不能干，如果用异步非阻塞的方式去买，买衣服只是你一天中进行的一个小事。
		
		我们把这个映射到我们代码中，当我们的线程发生一次rpc调用或者http调用，又或者其他的一些耗时的IO调用，发起之后，如果是同步阻塞，我们的这个线程就会被阻塞挂起，直到结果返回，试想一下如果IO调用很频繁那我们的CPU使用率其实是很低很低。正所谓是物尽其用，既然CPU的使用率被IO调用搞得很低，那我们就可以使用异步非阻塞，当发生IO调用时我并不马上关心结果，我只需要把回调函数写入这次IO调用，我这个时候线程可以继续处理新的请求，当IO调用结束结束时，会调用回调函数。而我们的线程始终处于忙碌之中，这样就能做更多的有意义的事了。
		这里首先要说明的是，异步化不是万能，异步化并不能缩短你整个链路调用时间长的问题，但是他能极大的提升你的最大qps。一般我们的业务中有两处比较耗时：

		cpu：cpu耗时指的是我们的一般的业务处理逻辑，比如一些数据的运算，对象的序列化。这些异步化是不能解决的，得需要靠一些算法的优化，或者一些高性能框架。
		iowait：io耗时就像我们上面说的,一般发生在网络调用，文件传输中等等，这个时候线程一般会挂起阻塞。而我们的异步化通常用于解决这部分的问题。
	3、哪些可以异步化？
		上面说了异步化是用于解决IO阻塞的问题，而我们一般项目中可以使用异步化如下：
			1.servlet异步化,springmvc异步化
			2.rpc调用如(dubbo,thrift),http调用异步化
			3.数据库调用，缓存调用异步化

	  
	 */
	
	
}