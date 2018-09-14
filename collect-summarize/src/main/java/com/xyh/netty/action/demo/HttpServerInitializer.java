package com.xyh.netty.action.demo;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //处理http服务的关键handler
        pipeline.addLast("httpServerCodec",new HttpServerCodec());
        //自定义的handler
        pipeline.addLast("testHttpServerHandler",new HttpServerHandler());
    }
    
//    @Override
//    protected void initChannel(SocketChannel ch) throws Exception {
//        ChannelPipeline pipeline = ch.pipeline();
//        //1、添加解码器,用于解释二进制内容
//        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
//
//        //2、编码器,用于计算消息的长度,并把消息长度以二进制的形式追加到消息的前面
//        pipeline.addLast(new LengthFieldPrepender(4));
//
//        //3、socket编程中需要对字符串进行编码解码
//        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
//        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
//
//        //4、添加自定义处理器
//        pipeline.addLast(new SocketServerHandler());
//    }
}