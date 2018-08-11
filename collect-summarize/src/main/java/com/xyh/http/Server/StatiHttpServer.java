package com.xyh.http.Server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 
 * @author hcxyh  2018年8月10日
 *
 */
public class StatiHttpServer {
	public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket client = null;
        try {
            serverSocket = new ServerSocket(9999);
            // 不断接收客户连接
            while (true) {
                // 服务器阻塞等待客户端socket连接过来
                client = serverSocket.accept();
                // =========请求类处理=======
                InputStream in = client.getInputStream();
                MyHttpRequest request = new MyHttpRequest(in);
                String requestUri = request.getUri();
                
                
                // =========响应类处理=======
                OutputStream os = client.getOutputStream();
                MyHttpResponse response = new MyHttpResponse(os);
                //请求格式是/html/login.html这种，需要把开头的/删除
                response.writerFile(requestUri.substring(1));
                client.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
 }
	
}
