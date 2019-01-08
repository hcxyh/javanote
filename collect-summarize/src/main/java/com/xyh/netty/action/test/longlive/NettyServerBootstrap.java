package com.xyh.netty.action.test.longlive;

import com.xyh.netty.heartcheckandlongconn.server.NettyServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/*
定义客户端服务端通信协议，一下是一个简单的通信协议
协议分为header和body两部分，都是用网络字节序（BIG ENDIAN）
header{
magic 32bit;  //校验用固定值0x0CAFFEE0
version 8bit;  //版本号
type 8bit;     //类型，请求或者响应
seq 32bit;     //序号标记一对请求响应
length 32bit;  //body长度
}
body{
}
 */
public class NettyServerBootstrap {

//    private static final Log log = LogFactory.getLog(NettyServerBootstrap.class);


    private Integer port;
    private SocketChannel socketChannel;
    public NettyServerBootstrap(Integer port) throws Exception {
        this.port = port;
        bind(port);
    }
    public Integer getPort() {
        return port;
    }
    public void setPort(Integer port) {
        this.port = port;
    }
    public SocketChannel getSocketChannel() {
        return socketChannel;
    }
    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }
    private void bind(int serverPort) throws Exception {
        // 连接处理group
        EventLoopGroup boss = new NioEventLoopGroup();
        // 事件处理group
        EventLoopGroup worker = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 绑定处理group
        bootstrap.group(boss, worker);
        bootstrap.channel(NioServerSocketChannel.class);
        // 保持连接数
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024 * 1024);
        // 有数据立即发送
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        // 保持连接
        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        // 处理新连接
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel sc) throws Exception {
                // 增加任务处理
                ChannelPipeline p = sc.pipeline();
                p.addLast(new MessageDecoder(), new MessageEncoder(), new NettyServerHandler());
            }
        });

        ChannelFuture f = bootstrap.bind(serverPort).sync();
        if (f.isSuccess()) {
            System.out.println("long connection started success");
//            log.info("long connection started success");
        } else {
            System.out.println("long connection started success");
//            log.error("long connection started fail");
        }
    }


    public static void main(String[] args) {
        try {
            new NettyServerBootstrap(9999);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

}
