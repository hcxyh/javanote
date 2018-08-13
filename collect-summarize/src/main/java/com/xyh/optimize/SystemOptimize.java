package com.xyh.optimize;

/**
 * TODO https://mp.weixin.qq.com/s/8hxOJNaqoELglOuECwHTzA
 * @author hcxyh  2018年8月13日
 *
 */
public class SystemOptimize {
	
	/**
	 * 系统性能定义:
			1.Throughout 吞吐量  （系统每秒钟可以处理的请求数）
			2.Latency 延迟 （系统处理一个请求的延迟）
			3.Usage 资源利用率
	       吞吐量和延迟的关系
			1.吞吐量越高，延迟会越大。因为请求量过大，系统太繁忙，所以响应时间会降低。
			2.延迟越小，能支持的吞吐量会越高。因为延迟短说明处理速度快，就可以处理更多的请求。
			3.异步化可以提高系统的吞吐量的灵活性，但是不会获得更快的响应时间。
	 */
	
	/**
	 * 压测常用工具:
	 		1.tcpdump
	 		2.tcpcopy
	 		3.wrk & ApacheBench  & Jmeter & webbench
	 */
	
	/**
	 * 定位性能瓶颈
	  	1.应用层面
	  		1.QPS
			2.响应时间，95、99线等。
			3.成功率
		2.系统层面
			系统层面指标有Cpu、内存、磁盘、网路等
			dstat -lcdngy
			1.cpu	
				1.利用率
				CPU利用率 = 1 - 程序占用cpu时间/程序总的运行时间
				用户时间/内核时间：大致判断应用是计算密集型还是IO密集型
				{
					CPU花在用户态代码的时间称为用户时间，而执行内核态代码的时间称为内核时间。
					内核时间主要包括系统调用，内核线程和中断的时间。
					当在整个系统范围内进行测量时，用户时间和内核时间之比揭示了运行的负载类型。
					计算密集型应用会把大量时间花在用户态代码上，用户时间/内核时间之比接近99/1。这样的例子有图像处理，数据分析等。
					I/O密集型应用的系统调用频率较高，通过执行内核代码进行I/O操作。
					一个进行网络I/O的Web服务器的用户/内核时间比大约为70/30。
				}
				2.load
				负载load：在特定时间间隔内运行队列中的平均进程数。每个CPU都有一个运行队列，队列里存放着已经就绪，等待被CPU执行的线程。
				理想状态下，希望负载平均值小于等于Cpu核数。
				3.Cpu使用率和load的区别：
				负载均值用来估量CPU利用率的发展趋势，而不是某一时刻的状况。
				负载均值包括所有CPU的需求，而不仅仅是在测量时活跃的。
			2.磁盘
				磁盘空间：没有空间会导致程序无法启动或者报错。
					du -sh //查看当前文件夹下所有文件大小
					df -hl //以磁盘分区为单位查看文件系统
				有时候linux服务器的系统日志文件过大导致磁盘使用率过高，推荐两种清理方式：
					sudo /dev/null > /var/log/**.log  //删除指定的较大日志文件，速度快
					sudo find /var/log/ -type f -mtime +30 -exec rm -f {} \  //删除30天之前的日志文件
				磁盘权限：没有权限会导致程序无法启动或者报错。
					ll /yourdir
				磁盘性能测试
					dd if=/dev/zero of=output.file bs=10M count=1
			3.网络
				1.netstat
					netstat -nt 查看tcp相关连接状态、连接数以及发送队列和接收队列
					{
						三次握手和四次挥手的过程:(各个阶段的状态)
						客户端：SYN_SENT、FIN_WAIT1、FIN_WAIT2、CLOSING、TIME_WAIT
						服务端：LISTEN、SYN_RCVD、CLOSE_WAIT、LAST_ACK
						Common：ESTABLISHED、CLOSED
					}
		3.JVM层面
			1.线程堆栈
				jstack信息是某个时刻的堆栈信息，有时间仅仅一个jstack并不能分析出问题所在，可以适当多几次jstack，然后进行对比分析
			2.
		4.Profiler
	 */

}
