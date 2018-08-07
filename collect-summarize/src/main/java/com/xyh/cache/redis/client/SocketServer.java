package com.xyh.cache.redis.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import redis.clients.jedis.Jedis;

/**
 * socket监听6379端口
 * @author hcxyh  2018年8月7日
 */
public class SocketServer {
	
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(6379);
		Socket socket = serverSocket.accept();
		byte[] bytes = new byte[64];
		socket.getInputStream().read(bytes);
		System.out.println(new String(bytes));
	}
	
	//使用jedis操作,查看socket监听的数据
	public void testJredis() {
		Jedis jedis = new Jedis("127.0.0.1",6379);
		jedis.set("xyh", "hcxyh");
	}
	
	/**
	 redis的客户端和服务端采取了一种RESP协议。相应文档地址如下:
	 https://redis.io/topics/protocol
	 RESP,设计巧妙
	（1）简单字符串Simple Strings, 以 "+"加号 开头
	（2）错误Errors, 以"-"减号 开头
	（3）整数型Integer， 以 ":" 冒号开头
	（4）大字符串类型Bulk Strings, 以 "$"美元符号开头，长度限制512M
	（5）组类型Arrays，以 "*"星号开头
	并且，协议的每部分都是以 "\r\n" (CRLF) 结尾的。
	TODO 
	https://mp.weixin.qq.com/s/GUKtFIxf28_wbDrs407owA
	 */
}
