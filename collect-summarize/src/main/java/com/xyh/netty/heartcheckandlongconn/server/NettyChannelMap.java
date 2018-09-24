package com.xyh.netty.heartcheckandlongconn.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;


/**
 * 维护客户端的连接
 * @author xyh
 *
 */
public class NettyChannelMap {
    
	/**
	 * TODO 
	 * netty提供了原生的ChannelGroups来做map的事情.
	 * public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	 */
	
	private static Map<String,SocketChannel> map = new ConcurrentHashMap<String, SocketChannel>();
    
    
    public static void add(String clientId,SocketChannel socketChannel){
        map.put(clientId,socketChannel);
    }
    
    public static Channel get(String clientId){
       return map.get(clientId);
    }
    
    public static void remove(SocketChannel socketChannel){
        for (Map.Entry entry:map.entrySet()){
            if (entry.getValue()==socketChannel){
                map.remove(entry.getKey());
            }
        }
    }

}