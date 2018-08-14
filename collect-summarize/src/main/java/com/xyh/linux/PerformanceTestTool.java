package com.xyh.linux;

/**
 * 时间久了,好多命令和参数都记得不牢靠.在此整理下.
 * @author hcxyh  2018年8月13日
 *
 */
public class PerformanceTestTool {
	
	/**
	 1.性能测试工具
	 	https://www.cnblogs.com/jmcui/p/7452547.html
	 	hundsun的总结
	 	http://rdcqii.hundsun.com/portal/article/731.html
	 2.free -m -g -s 2 
	 	1.mem 	物理内存
	 	2.+/-	buffers/cache
	 	3.swap	当物理内存不足时，可能会借用硬盘空间来充当内存使用
	 	buffers 就是存放要输出到disk（块设备）的数据，缓冲满了一次写，提高IO性能（内存 -> 磁盘）
		cached 就是存放从disk上读出的数据，常用的缓存起来，减少IO（磁盘 -> 内存）
		buffer 和 cache，两者都是RAM中的数据。简单来说，buffer是即将要被写入磁盘的，cache是被从磁盘中读出来的。
	 */
	
}
