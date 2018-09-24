package com.xyh.netty.connectionpool;

public class FixedChannelPoolNote {

	/**
	 * 如今越来越多的应用采用Netty作为服务端高性能异步通讯框架，对于客户端而言，
	 * 大部分需求只需和服务端建立一条链接收发消息。但如果客户端需要和服务端建立多条链接的例子就比较少了。 
	 * 最简单的实现就是一个for循环，建立多个NioEventLoopGroup与服务端交互。
	 * 另外还有如果要和多个服务端进行交互又该如何解决。
	 * 其实Netty从4.0版本就提供了连接池ChannelPool，可以解决与多个服务端交互以及与单个服务端建立连接池的问题。
	 * 
	 * FixedChannelPool --> SimpleChannelPool --> ChannelPool
	 */
	
	
}
