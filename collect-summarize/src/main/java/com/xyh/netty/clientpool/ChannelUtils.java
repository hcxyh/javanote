package com.xyh.netty.clientpool;

import java.util.Map;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * ChannelUtils类则用于建立channel、消息序列号和callback程序的对应关系
 * @author xyh
 *
 */
public class ChannelUtils {
    public static final int MESSAGE_LENGTH = 16;
    public static final AttributeKey<Map<Integer, Object>> DATA_MAP_ATTRIBUTEKEY = AttributeKey.valueOf("dataMap");
    public static <T> void putCallback2DataMap(Channel channel, int seq, T callback) {
        channel.attr(DATA_MAP_ATTRIBUTEKEY).get().put(seq, callback);
    }
 
    public static <T> T removeCallback(Channel channel, int seq) {
        return (T) channel.attr(DATA_MAP_ATTRIBUTEKEY).get().remove(seq);
    }
}