package com.xyh.netty.bio;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

/**
 * 
 * @author hcxyh  2018年8月13日
 *
 */
public class IOClient {
	
	public static void main(String[] args) {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8000);
//                while (true) {
                    try {
                    	//发送
                        OutputStream ops = socket.getOutputStream();
//                        socket.getOutputStream().write((new Date() + ": hello world").getBytes());
                        
                        PrintWriter pw = new PrintWriter(ops);
                        pw.write((new Date() + ": cccccccc"));
                        pw.flush();
                        Thread.sleep(2000);
                        
                        
//                        System.out.println("@@@@@@@@@@@@@@@@@");
                        //接收返回
                        byte[] bytes = new byte[2048];
                        socket.getInputStream().read(bytes);
                        System.out.println(new String(bytes,"UTF-8"));
                        
                        pw.close();
                        socket.close();
                    } catch (Exception e) {
                    }
//                }
            } catch (IOException e) {
            }
        }).start();
    }
}
