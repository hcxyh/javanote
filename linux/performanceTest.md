
# 性能检测

1.uptim
	该命令可以大致的看出计算机的整体负载情况，load average后的数字分别表示计算机在1min、5min、15min内的平均负载。
	
2.dmesg | tail
	打印内核环形缓存区中的内容，可以用来查看一些错误；
	通过dmesg可以快速判断是否有导致系统性能异常的问题。
3.vmstat 1
	打印进程、内存、交换分区、IO和CPU等的统计信息；
	{
r：表示正在运行或者等待CPU调度的进程数。因为该列数据不包含I/O的统计信息，因此可以用来检测CPU是否饱和。若r列中的数字大于CPU的核数，表示CPU已经处于饱和状态。
free：当前剩余的内存；
si, so：交换分区换入和换出的个数，若换入换出个数大于0，表示内存不足；
us, sy, id, wa：CPU的统计信息，分别表示user time、system time(kernel)、idle、wait I/O。I/O处理所用的时间包含在system time中，因此若system time超过20%，则I/O可能存在瓶颈或异常；
	}
4.mpstat -P ALL 1
	该命令用于每秒打印一次每个CPU的统计信息，可用于查看CPU的调度是否均匀。
5.pidstat 1
	该命令用于打印各个进程对CPU的占用情况，类似top命令中显示的内容。pidstat的优势在于，可以滚动的打印进程运行情况，而不像top那样会清屏。
6.iostat -xz 1
7.free -m
8.sar -n DEV 1
9.sar -n TCP,ETCP 1
10.top


!(linux性能监控)[http://rdcqii.hundsun.com/portal/article/731.html]
