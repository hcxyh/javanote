package com.xyh.java.proxy.staticproxy;

public class Client {
	
	public static void main(String[] args) {
		
		//客户端对象只需知道代理类，无需知道具体主题类
        Subject proxy = new Proxy();
        proxy.request();
        long costTime = ((Proxy) proxy).getCost();
        System.out.println("Cost time : " + costTime + " ns");
	}
}
