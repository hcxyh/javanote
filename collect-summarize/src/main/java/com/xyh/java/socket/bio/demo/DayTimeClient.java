package com.xyh.java.socket.bio.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

//客户端获取服务器端提供的时间服务
public class DayTimeClient {
	
	/**
	 * 通过引入线程池，大大优化了线程的创建与使用方式，
	 * 每次服务器端接收到新连接后从线程池中取得一空闲线程进行处理，
	 * 处理完成后再放回池中，实现了线程的复用，
	 * 解决了上文中多线程模型中出现的线程重复创建、
	 * 销毁带来的开销与潜在的“拒绝服务攻击”等问题。
	 */
	
	public static void main(String[] args) throws IOException {
	       
        Socket client = null;
        BufferedReader reader = null;
       
        // 创建连接
        client = new Socket();
        try {
            // 连接服务器
            client.connect(new InetSocketAddress("localhost",8899));
            
            // 创建读取服务器端返回流的BufferedReader
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            
            // 阻塞式获取服务器端返回来的信息
            System.out.println("Form Server : " + reader.readLine());
        
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            //非常重要，避免内存泄漏
            if(reader != null) reader.close();
            if(client != null) client.close();
        }   
    }
	
}
