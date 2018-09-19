package com.xyh.netty.compass.basic;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * ChannelHandlerAdapter
 * ChannelInboundHandlerAdapter
 * @author xyh
 *
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter{
	
	/**
	 * channelHandlerAdapter对网络读写事件进行操作.
	 */

    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    throws Exception {
    	
    //ByteBuf 类似于 jdk中的 buffer对象.
    	/**
    	 * readableBytes()可以获取缓冲区中可读的字节数
    	 * 
    	 */
	ByteBuf buf = (ByteBuf) msg;
	byte[] req = new byte[buf.readableBytes()];
	
	buf.readBytes(req);
	
	String body = new String(req, "UTF-8");
	
	System.out.println("The time server receive order : " + body);
	
	String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new java.util.Date(
		System.currentTimeMillis()).toString() : "BAD ORDER";
	ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
	
	
	ctx.write(resp); //异步发送应答消息给客户端
	
	/**
	 * 性能的角度考虑,防止频繁的唤醒Selector进行消息发送,Netty的write方法并不直接将消息写入到SocketChannel中,
	 * 调用write方法,只不过是将待发送消息放到发送缓冲数组中,
	 * 再通过调用flush方法,将发送缓冲区中的消息全部写入到socketChannel中
	 */
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    	
	ctx.flush(); //将消息发送队列中的消息发送到SocketChannel中,发送给对方.
    
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	/**
    	 *  发生异常时,关闭ChannelHandlerContext,并释放相关的句柄.
    	 */
	ctx.close();
    }
}
