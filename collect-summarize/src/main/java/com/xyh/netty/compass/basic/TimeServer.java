package com.xyh.netty.compass.basic;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {
	
	private SocketChannel socketChannel;

    public void bind(int port) throws Exception {
	
    // 配置服务端的NIO线程组
	EventLoopGroup bossGroup = new NioEventLoopGroup();
	EventLoopGroup workerGroup = new NioEventLoopGroup();
	/**
	 * 实际上,上面那两个就是reactor线程组.
	 * 一个用于服务端接受客户端的连接,一个用于进行socketChannel的读写.
	 */
	
	try {
	    ServerBootstrap b = new ServerBootstrap();
	    b.group(bossGroup, workerGroup)
		    .channel(NioServerSocketChannel.class)  //设置通道类型
		    .option(ChannelOption.SO_BACKLOG, 1024) //配置channel的tcp参数
		    .childHandler(new ChildChannelHandler()); //绑定IO事件的处理类
	    
	    // 绑定端口，同步等待端口绑定成功
	    ChannelFuture f = b.bind(port).sync();
	    
	    try {
			// 判断是否连接成功
			if (f.isSuccess()) {
				// 得到管道，便于通信
				socketChannel = (SocketChannel) f.channel();
				System.out.println("客户端开启成功...");
			}
			else{
				System.out.println("客户端开启失败...");
			}
			// 等待客户端链路关闭，就是由于这里会将线程阻塞，导致无法发送信息，所以我这里开了线程
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 

	    // 等待服务端监听端口关闭
	    f.channel().closeFuture().sync();
	} finally {
	    // 优雅退出，释放线程池资源
	    bossGroup.shutdownGracefully();
	    workerGroup.shutdownGracefully();
	}
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
	@Override
	protected void initChannel(SocketChannel arg0) throws Exception {
	    arg0.pipeline().addLast(new TimeServerHandler());
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
	new TimeServer().bind(port);
    }
    
    public void sendMessage(Object msg) {
		if (socketChannel != null) {
			socketChannel.writeAndFlush(msg);
		}
	}

}

