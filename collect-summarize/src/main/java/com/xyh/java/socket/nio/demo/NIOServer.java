package com.xyh.java.socket.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 发现服务端的事件有2个，一是接受连接事件，二是读取数据：
 * @author Administrator
 *
 */
public class NIOServer {

    private ByteBuffer readBuffer;    private Selector selector;    private ServerSocket serverSocket;    public static void main(String[] args) {
        NIOServer server = new NIOServer();
        server.init();
        System.out.println("server started:8383");
        server.listener();
    }    public void init() {        //1. 创建临时缓冲区
        readBuffer = ByteBuffer.allocate(1024);        //2. 创建服务端Socket非阻塞通道
        ServerSocketChannel serverSocketChannel;        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);            //3. 指定内部Socket绑定的服务端地址 并支持重用端口，因为有可能多个客户端同时访问同一端口
            serverSocket=serverSocketChannel.socket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(8383));            //4. 创建轮询器 并绑定到管道上，开始监听客户端请求
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    private void listener() {        while (true) {            try {                //5. 开始监听事件，不断取出事件的key，假如存在事件，则直接处理。
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    handleKey(key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }    private void handleKey(SelectionKey key) throws IOException {
        SocketChannel channel = null;        try {            //6. 如果有客户端要连接 这里则处理是否接受连接事件
            if (key.isAcceptable()) {
                ServerSocketChannel severChannel = (ServerSocketChannel) key.channel();
                channel = severChannel.accept();
                channel.configureBlocking(false);                // 告诉轮询器 接下来关心的是读取客户端数据这件事
                channel.register(selector, SelectionKey.OP_READ);
            } else if (key.isReadable()) { //7. 如果客户端发送数据，则这里读取数据。
                channel = (SocketChannel) key.channel();                // 清空缓冲区
                readBuffer.clear();                // 当客户端关闭channel后，会不断收到read事件，此刻read方法返回-1 所以对应的服务器端也需要关闭channel
                int readCount = channel.read(readBuffer);                if (readCount > 0) {
                    readBuffer.flip();
                    String question = CharsetHelper.decode(readBuffer).toString();
                    System.out.println("server get the question:" + question);
                    String answer = getAnswer(question);
                    channel.write(CharsetHelper.encode(CharBuffer.wrap(answer)));
                } else {
                    channel.close();
                }
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }finally {            //8. 断开连接通道
            if (channel!=null) {
                channel.close();
            }
        }
    }    public static String getAnswer(String question) {
        String answer = null;        switch (question) {        case "who":
            answer = "我是小娜\n";            break;        case "what":
            answer = "我是来帮你解闷的\n";            break;        case "where":
            answer = "我来自外太空\n";            break;        case "hi":
            answer = "hello\n";            break;        case "bye":
            answer = "88\n";            break;        default:
            answer = "请输入 who， 或者what， 或者where";
        }        return answer;
    }

}