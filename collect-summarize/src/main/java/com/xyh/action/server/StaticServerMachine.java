package com.xyh.action.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class StaticServerMachine {

	// http://localhost:9999/docs/index.html

	public static void main(String[] args) {
		ServerSocket serverSocket = null;
		Socket client = null;
		try {
			serverSocket = new ServerSocket(9999);
			// 不断接收客户连接
			while (true) {
				// 服务器阻塞等待客户端socket连接过来
				client = serverSocket.accept();
				// 对客户端里面端请求信息进行处理
				InputStream in = client.getInputStream();
				// 定义一个读取缓冲池 主要是在inputstram流中读取字节
				byte[] buff = new byte[1024];
				int len = in.read(buff);
				if (len > 0) {
					String msg = new String(buff, 0, len);
					System.out.println("====" + msg + "======");
					OutputStream out = client.getOutputStream();
					// 构建响应信息
					StringBuffer sb = new StringBuffer();
					sb.append("HTTP/1.1 200 OK\n");
					sb.append("Content-Type: text/html; charset=UTF-8\n");
					sb.append("\n");
					String html = "<html><head><title>静态服务器</title></head></html><body>花好月正圆"
							+ "<font size='14' color='red'>" + new Date() + "</font>" + "<br/>永远的微笑</body></html>";
					sb.append(html);
					out.write(sb.toString().getBytes());
					out.flush();
					out.close();
				}
			}
		} catch (Exception e) {
		}
	}

}
