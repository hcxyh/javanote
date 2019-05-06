package com.xyh.netty.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author hcxyh  2018年8月8日
 *
 */
public class IOServer {
    public static void main(String[] args) throws Exception {
    	
        ServerSocket serverSocket = new ServerSocket(8000);

        // (1) 接收新连接线程   ,此处可以使用线程池来进行优化,  n（client） --> m(server)
        new Thread(() -> {
//            while (true) {
                try {
                    // (1) 阻塞方法获取新的连接
                    Socket socket = serverSocket.accept();
                    // (2) 每一个新的连接都创建一个线程，负责读取数据
                    new Thread(() -> {
                        try {
                            byte[] data = new byte[1024];
                            InputStream inputStream = socket.getInputStream();
                            OutputStream outputStream = socket.getOutputStream();
                                int len;
//                                System.out.println(inputStream.read(data) +"111");
                                // (3) 按字节流方式读取数据
                                while ((len = inputStream.read(data)) != -1) {
                                    System.out.println(new String(data, 0, len));
                                }
                            System.out.println("--------------");
                            outputStream.write(data);
                            outputStream.flush();
                        } catch (IOException e) {
                        }
                    }).start();

                } catch (IOException e) {
                }

//            }
        }).start();
    }
    
    /**
     	从服务端代码中我们可以看到，在传统的IO模型中，每个连接创建成功之后都需要一个线程来维护，每个线程包含一个while死循环，
     	那么1w个连接对应1w个线程，继而1w个while死循环，这就带来如下几个问题：
			1.线程资源受限：线程是操作系统中非常宝贵的资源，同一时刻有大量的线程处于阻塞状态是非常严重的资源浪费，操作系统耗不起。（并且java底层直接调用的是操作系统线程）
			2.线程切换效率低下：单机cpu核数固定，线程爆炸之后操作系统频繁进行线程切换，应用性能急剧下降。
			3.除了以上两个问题，IO编程中，我们看到数据读写是以字节流为单位，效率不高。
     */
    
}
