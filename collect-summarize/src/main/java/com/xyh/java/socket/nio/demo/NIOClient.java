package com.xyh.java.socket.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * 客户端的实现有3个步骤：1.请求连接。2.当连接成功，写数据。3.读取服务端结果。
 * @author Administrator
 *
 */
public class NIOClient implements Runnable {

    private BlockingQueue<String> words;    private Random random;    public static void main(String[] args) {        // 多个线程发起Socket客户端连接请求
        for (int i = 0; i < 5; i++) {
            NIOClient c = new NIOClient();
            c.init();            new Thread(c).start();
        }
    }    //1. 初始化要发送的数据
    private void init() {
        words = new ArrayBlockingQueue<String>(5);
        random = new Random();        try {
            words.put("hi");
            words.put("who");
            words.put("what");
            words.put("where");
            words.put("bye");
        } catch (Exception e) {            // TODO: handle exception
        }
    }    //2. 启动子线程代码
    @Override
    public void run() {
        SocketChannel channel = null;
        Selector selector = null;        try {            //3. 创建连接服务端的通道 并设置为阻塞方法，这里需要指定服务端的ip和端口号
            channel = SocketChannel.open();
            channel.configureBlocking(false);
            channel.connect(new InetSocketAddress("localhost", 8383));
            selector = Selector.open();            //4. 请求关心连接事件
            channel.register(selector, SelectionKey.OP_CONNECT);            boolean isOver = false;            while (!isOver) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();                    if (key.isConnectable()) { //5. 当通道连接准备完毕，发送请求并指定接收允许获取服务端返回信息
                        if (channel.isConnectionPending()) {                            if (channel.finishConnect()) {
                                key.interestOps(SelectionKey.OP_READ);
                                channel.write(CharsetHelper.encode(CharBuffer.wrap(getWord())));
                                sleep();
                            } else {
                                key.cancel();
                            }
                        }
                    } else if (key.isReadable()) {//6. 开始读取服务端返回数据
                        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
                        channel.read(byteBuffer);
                        byteBuffer.flip();
                        String answer = CharsetHelper.decode(byteBuffer).toString();
                        System.out.println("client get the answer:" + answer);
                        String word = getWord();                        if (word != null) {
                            channel.write(CharsetHelper.encode(CharBuffer.wrap(getWord())));
                        } else {
                            isOver = true;
                        }
                        sleep();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {            //7. 关闭通道
            if (channel != null) {                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }    public String getWord() {        return words.poll();
    }    private void sleep() {        try {
            TimeUnit.SECONDS.sleep(random.nextInt(3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}