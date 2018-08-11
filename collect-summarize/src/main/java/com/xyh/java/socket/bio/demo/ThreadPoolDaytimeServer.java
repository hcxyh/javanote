package com.xyh.java.socket.bio.demo;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

//基于线程池的方式实现网络服务端提供时间服务
public class ThreadPoolDaytimeServer {
	
	 // 服务的端口
    public final static int PORT = 8899;
    
    public static void main(String[] args) {
    
        // 固定线程数目的线程池，最多允许同时50个网络连接
        // 超过服务的极限就会拒绝连接
        ExecutorService pool = Executors.newFixedThreadPool(50);
        
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Started server ... ");
            
            // 永不消逝的监听          
            while (true) {
                try {
                    // 接受客户端建立连接的请求，并返回Socket对象，以便和客户端进行交互
                    Socket connection = server.accept();
                    // 开启一个线程对新接入的连接进行处理，这是“一线程一连接”实现的关键
                    // 注意：这里是通过线程池的方式进行提交
                    Callable<Void> task = new DaytimeTask(connection);
                    pool.submit(task);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            
        } catch (IOException ex) {
            System.err.println("Start Server Failed!");
        }
    }
    
    // 利用线程来处理连接
    private static class DaytimeTask implements Callable<Void> {
    
        private Socket connection;
        
        DaytimeTask(Socket connection) {
            this.connection = connection;
        }
        
        @Override
        public Void call() {
        
            try {
                Writer out = new OutputStreamWriter(
                        connection.getOutputStream());
                // 获取当前时间
                Date now = new Date();
                // 发送给客户端
                out.write(now.toString() + "\r\n");
                out.flush();
            } catch (IOException ex) {
                System.err.println(ex);
            } finally {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            return null;
        }
    }
	/**
	 * 一线程一连接”的工作原理，可以如图Fig.2所示，通过对该图的分析，我们可以得到该模型具有以下特点（局限）：
		接收数据One by One。在服务器端，接收到数据后的处理可交给一个独立的线程进行处理，
		但是操系统通知accept的方式还是单线程方式。
		即：实际上服务器接收到数据报文后的处理过程可以采用多线程的方式，但是数据的接收方式还是一个接一个地进行。
		线程资源问题。虽然我们采用了Java线程池技术缓解了线程创建和切换的资源消耗问题，
		但是又会造成线程池中待处理任务的持续增加，同样消耗了大量的内存资源。
		此外，如果应用程序是采用长连接方式进行通信，那么线程池中的线程就被对应的任务持续占有，
		这样一来服务端处理的不同连接请求的数量上不去。
	 */

}
