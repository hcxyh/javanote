package com.xyh.netty.compass.basic;


import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class TimeClientHandler extends ChannelHandlerAdapter {

    private static final Logger logger = Logger
	    .getLogger(TimeClientHandler.class.getName());

    private final ByteBuf firstMessage;

    /**
     * Creates a client-side handler.
     */
    public TimeClientHandler() {
	byte[] req = "QUERY TIME ORDER".getBytes();
	firstMessage = Unpooled.buffer(req.length);
	firstMessage.writeBytes(req);

    }
    
    
    /**
     * a 客户端和服务端TCP链路链路建立成功之后
     */
    public void channelActive(ChannelHandlerContext ctx) {
	ctx.writeAndFlush(firstMessage);
    }
    
    
    /**
     * a读取应答信息
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg)
	    throws Exception {
	ByteBuf buf = (ByteBuf) msg;
	byte[] req = new byte[buf.readableBytes()];
	buf.readBytes(req);
	String body = new String(req, "UTF-8");
	System.out.println("Now is : " + body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
	// 释放资源
	logger.warning("Unexpected exception from downstream : "
		+ cause.getMessage());
	ctx.close();
    }
}
