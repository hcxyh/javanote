package com.xyh.netty.compass.basic;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {

	private SocketChannel socketChannel;
	
    public void connect(int port, String host) throws Exception {
	// 配置客户端NIO线程组  (客户端处理IO读写)
	EventLoopGroup group = new NioEventLoopGroup(); //
	
	try {
	    Bootstrap b = new Bootstrap();
	    b.group(group).channel(NioSocketChannel.class)  //设置channel的类型
		    .option(ChannelOption.TCP_NODELAY, true)
		    .handler(new ChannelInitializer<SocketChannel>() {  //IO响应处理类
			@Override
			public void initChannel(SocketChannel ch)
				throws Exception {
			    ch.pipeline().addLast(new TimeClientHandler());
			}
		    });

	    // 发起异步连接操作
	    ChannelFuture f = b.connect(host, port).sync();
	    
	    
	 // 进行连接
		ChannelFuture future;
		try {
			future = b.connect(host, port).sync();
			// 判断是否连接成功
			if (future.isSuccess()) {
				// 得到管道，便于通信
				socketChannel = (SocketChannel) future.channel();
				System.out.println("客户端开启成功...");
			}
			else{
				System.out.println("客户端开启失败...");
			}
			// 等待客户端链路关闭，就是由于这里会将线程阻塞，导致无法发送信息，所以我这里开了线程
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 

	    // 当代客户端链路关闭
	    f.channel().closeFuture().sync();  
	    
	    
	} finally {
		
		
	    // 优雅退出，释放NIO线程组
	    group.shutdownGracefully();
	}
    }

    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
	int port = 8080;
	if (args != null && args.length > 0) {
	    try {
		port = Integer.valueOf(args[0]);
	    } catch (NumberFormatException e) {
		// 采用默认值
	    }
	}
	new TimeClient().connect(port, "127.0.0.1");  //建立连接,触发handle中的active
    }
    
    public void sendMessage(Object msg) {
		if (socketChannel != null) {
			socketChannel.writeAndFlush(msg);
		}
	}

}
