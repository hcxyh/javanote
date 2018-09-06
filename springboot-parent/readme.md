

過程总结:
1.mysql中ssl的问题
2.mybatis使用中,表字段和java对象的格式不一致的转换.
	1.mybatis的使用
		{1.注解    2.xml(重写mybatis-generator)}
3.yum 安装的路径 默认的
4.firewell，systenctl 
		
5.mysql的密码   12345678
	配置文件
	vim /etc/my.cnf
	存放数据库文件的目录
	cd /var/lib/mysql
	日志记录文件
	vim /var/log/ mysqld.log
	服务启动脚本
	/usr/lib/systemd/system/mysqld.service
	socket文件
	/var/run/mysqld/mysqld.pid

6.nginx 中 虚拟主机中 server_name 中的名字
http请求中会自带host (域名)，nginx中会匹配
proxy_pass 中配置过之后,在 upstream中就不能使用 localhost 和127.0.0.1了.
在nginx的upstream做负载均衡时要用ip或者远程主机名，不能使用localhost
8.127.0.0.1和localhost的区别
localhost也叫local ，正确解释为：本地服务器
127.0.0.1在系统的正确解释是：本机地址（本机服务器）
localhot（local）是不经网卡传输！这点很重要，它不受网络防火墙和网卡相关的的限制。访问localhost也不会解析成ip，不会占用网卡、网络资源。
而127.0.0.1是需要通过网卡传输，依赖网卡，并受到网络防火墙和网卡相关的限制。
这就是为什么有时候用localhost可以访问，但用127.0.0.1就不可以的情况。


9.systemctl  ,  firewalls 
https://www.cnblogs.com/moxiaoan/p/5683743.html

10top   M  ,P  ,k  ,T
内存状态 ，cpu状态

第三行：cpu状态
    0.3% us — 用户空间占用CPU的百分比。
    0.0% sy — 内核空间占用CPU的百分比。
    0.0% ni — 改变过优先级的进程占用CPU的百分比
    99.7% id — 空闲CPU百分比
    0.0% wa — IO等待占用CPU的百分比
    0.0% hi — 硬中断（Hardware IRQ）占用CPU的百分比
    0.0% si — 软中断（Software Interrupts）占用CPU的百分比
    
    
    第四行：内存状态
    3808060k total — 物理内存总量（4GB）
    3660048k used — 使用中的内存总量（3.6GB）
    148012k free — 空闲内存总量（148M）
    359760k buffers — 缓存的内存量 （359M）

    第五行：swap交换分区
    4184924k total — 交换区总量（4G）
    0k used — 使用的交换区总量（0M）
    4184924k free — 空闲交换区总量（4G）
    2483956k cached — 缓冲的交换区总量（2483M）

第四行中使用中的内存总量（used）指的是现在系统内核控制的内存数，空闲内存总量（free）是内核还未纳入其管控范围的数量。纳入内核管理的内存不见得都在使用中，还包括过去使用过的现在可以被重复利用的内存，内核并不把这些可被重新使用的内存交还到free中去，因此在linux上free内存会越来越少，但不用为此担心。

如果出于习惯去计算可用内存数，这里有个近似的计算公式：第四行的free + 第四行的buffers + 第五行的cached，按这个公式此台服务器的可用内存：148M+259M+2483M = 2990M。

对于内存监控，在top里我们要时刻监控第五行swap交换分区的used，如果这个数值在不断的变化，说明内核在不断进行内存和swap的数据交换，这是真正的内存不够用了。

 第六行是空行

   第七行以下：各进程（任务）的状态监控
    PID — 进程id
    USER — 进程所有者
    PR — 进程优先级
    NI — nice值。负值表示高优先级，正值表示低优先级
    VIRT — 进程使用的虚拟内存总量，单位kb。VIRT=SWAP+RES
    RES — 进程使用的、未被换出的物理内存大小，单位kb。RES=CODE+DATA
    SHR — 共享内存大小，单位kb
    S — 进程状态。D=不可中断的睡眠状态 R=运行 S=睡眠 T=跟踪/停止 Z=僵尸进程
    %CPU — 上次更新到现在的CPU时间占用百分比
    %MEM — 进程使用的物理内存百分比
    TIME+ — 进程使用的CPU时间总计，单位1/100秒
    COMMAND — 进程名称（命令名/命令行）
    



