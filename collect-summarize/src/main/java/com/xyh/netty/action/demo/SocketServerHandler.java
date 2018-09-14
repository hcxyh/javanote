package com.xyh.netty.action.demo;

import java.util.UUID;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * socketHandle 处理器
 */
public class SocketServerHandler extends SimpleChannelInboundHandler<String>{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("请求来自:"+ctx.channel().remoteAddress() + ",内容:" + msg);
        ctx.channel().writeAndFlush("from server:"+UUID.randomUUID());
    }
}