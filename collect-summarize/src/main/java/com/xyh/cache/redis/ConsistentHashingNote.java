package com.xyh.cache.redis;

/**
 * 当redis进行扩容是,缓存的命中率直线下降.
 * 一致性hash：可以保证当机器增加或者减少时，对缓存访问命中的概率影响减至很小。
 * @author hcxyh  2018年8月13日
 *
 */
public class ConsistentHashingNote {
	
	/**
	 * 这个环的起点是0，终点是2^32 - 1，并且起点与终点连接，环的中间的整数按逆时针分布，
	 * 故这个环的整数分布范围是[0, 2^32-1]：
	 */
	
}
