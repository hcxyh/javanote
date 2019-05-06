package com.xyh.netty.action.test.longlive;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ChannelHandler.Sharable
public class NettyServerHandler extends SimpleChannelInboundHandler<RequestInfoVO> {
    private static final Log log = LogFactory.getLog(NettyServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestInfoVO msg) throws Exception {

        System.out.print(msg);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }


    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {

    }

}

