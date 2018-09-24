/**
 * 
 */
/**
 * @author xyh
 *
 */
package com.xyh.netty.heartcheckandlongconn;

/*
 * 
 * 
 * netty5中 channelRead0() → messageReceived()
 * 如果使用了SimpleChannelInboundHandler，你需要把channelRead0()重命名为messageReceived()。
 * 
 * 1.netty建立长连接 ，在没有数据的情况下，使用心跳检测.
 * 
 * ChannelInboundHandlerAdapter 
 * 自定义处理类Handler继承ChannlInboundHandlerAdapter，实现其userEventTriggered()方法，在出现超时事件时会被触发，包括读空闲超时或者写空闲超时；
 */
