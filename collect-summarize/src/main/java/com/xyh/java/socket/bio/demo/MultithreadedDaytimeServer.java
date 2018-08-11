package com.xyh.java.socket.bio.demo;

import java.net.*;
import java.io.*;
import java.util.Date;

//网络服务端提供时间服务
public class MultithreadedDaytimeServer {
	
	/**
	 * TODO
	 * 上述服务器上有可能会受到一种拒绝服务攻击。
	 * 由于服务器端为每个连接都生成了一个线程，
	 * 大量几乎同时的客户端连接请求可能会导致它生成极大数量的线程。
	 * 最终，JVM会因耗尽内存而崩溃。一种更好的办法，是使用一种固定的线程池来限制可能的资源使用。
	 * 这样无论负载有多大，可能会出现拒绝服务（因为服务的数量有限），但是服务器端永远都不会崩溃。
	 */
	
	 // 服务的端口
    public final static int PORT = 8899;
    
    public static void main(String[] args) {
        
        //创立连接
        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Starting server ... ");
            while (true) {
                try {
                    // 接受客户端建立连接的请求，并返回Socket对象，以便和客户端进行交互
                    Socket connection = server.accept();
                    // 开启一个线程对新接入的连接进行处理，这是“一线程一连接”实现的关键
                    Thread task = new DaytimeThread(connection);
                    task.start();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            System.err.println("Start server failed !");
        }
    }
    
    // 开启一个线程来处理连接的请求
    private static class DaytimeThread extends Thread {
        
        private Socket connection;
        
        DaytimeThread(Socket connection) {
            this.connection = connection;
        }
        
        @Override
        public void run() {
            try {
                Writer out = new OutputStreamWriter(
                        connection.getOutputStream());
                //获取当前时间
                Date now = new Date();
                //发送给客户端
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
        }
    }
	
}
