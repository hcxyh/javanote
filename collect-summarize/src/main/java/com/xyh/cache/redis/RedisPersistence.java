package com.xyh.cache.redis;

/**
 * Redis持久化
 * @author hcxyh  2018年8月7日
 */
public class RedisPersistence {
	
	/**
	 * 	TODO 持久化
	 	一、RDB持久化
	 	RDB持久化把当前进程数据生成快照（.rdb）文件保存到硬盘的过程，有手动触发和自动触发
		手动触发有save和bgsave两命令 
		save命令：阻塞当前Redis，直到RDB持久化过程完成为止，若内存实例比较大会造成长时间阻塞，线上环境不建议用它
		bgsave命令：redis进程执行fork操作创建子线程，由子线程完成持久化，阻塞时间很短（微秒级），是save的优化,
		在执行redis-cli shutdown关闭redis服务时，如果没有开启AOF持久化，自动执行bgsave;
		显然bgsave是对save的优化。
		
		RDB文件的操作
		   命令：config set dir /usr/local  //设置rdb文件保存路径
		   备份：bgsave  //将dump.rdb保存到usr/local下
		   恢复：将dump.rdb放到redis安装目录与redis.conf同级目录，重启redis即可
		   优点：1，压缩后的二进制文文件适用于备份、全量复制，用于灾难恢复
		     2，加载RDB恢复数据远快于AOF方式
		   缺点：1，无法做到实时持久化，每次都要创建子进程，频繁操作成本过高
		     2，保存后的二进制文件，存在老版本不兼容新版本rdb文件的问题  
		二、AOF持久化
			针对RDB不适合实时持久化，redis提供了AOF持久化方式来解决
			开启：redis.conf设置：appendonly yes  (默认不开启，为no)
			默认文件名：appendfilename "appendonly.aof"   
      		流程说明：
　　  			1，所有的写入命令(set hset)会append追加到aof_buf缓冲区中
         	2，AOF缓冲区向硬盘做sync同步
         	3，随着AOF文件越来越大，需定期对AOF文件rewrite重写，达到压缩
         	4，当redis服务重启，可load加载AOF文件进行恢复
			AOF持久化流程：命令写入(append),文件同步(sync),文件重写(rewrite),重启加载(load)
		AOF配置详解：
			appendonly yes     //启用aof持久化方式
			# appendfsync always //每收到写命令就立即强制写入磁盘，最慢的，但是保证完全的持久化，不推荐使用
			appendfsync everysec //每秒强制写入磁盘一次，性能和持久化方面做了折中，推荐
			# appendfsync no    //完全依赖os，性能最好,持久化没保证（操作系统自身的同步）
			no-appendfsync-on-rewrite  yes  //正在导出rdb快照的过程中,要不要停止同步aof
			auto-aof-rewrite-percentage 100  //aof文件大小比起上次重写时的大小,增长率100%时,重写
			auto-aof-rewrite-min-size 64mb   //aof文件,至少超过64M时,重写
		如何从AOF恢复？
			1. 设置appendonly yes；
			2. 将appendonly.aof放到dir参数指定的目录；
			3. 启动Redis，Redis会自动加载appendonly.aof文件。
		redis重启时恢复加载AOF与RDB顺序及流程：
			1，当AOF和RDB文件同时存在时，优先加载AOF
			2，若关闭了AOF，加载RDB文件
			3，加载AOF/RDB成功，redis重启成功
			4，AOF/RDB存在错误，redis启动失败并打印错误信息
	 */
	
	/**
	 * TODO 基准性能测试
	   redis-benchmark：基准性测试，测试redis的性能
	   100个客户端同时请求redis,共执行10000次,会对各类数据结构的命令进行测试：
	   ./redis-benchmark -h 127.0.0.1 -c 100 -n 10000  //100个并发连接，100000个请求，检测host为localhost 端口为6379的redis服务器性能
	 */
	
	/**
	 * TODO  pipeline
	   没有pipeline之前，一般的redis命令的执行过程都是：发送命令－〉命令排队－〉命令执行－〉返回结果。
	   这个时候需要pipeline来解决这个问题：使用pipeline来打包执行N条命令，
	   这样的话就只需简历一次网络连接，网络开销就少了
	 
	 pipeline是多条命令的组合，为了保证它的原子性，redis提供了简单的事务；
	 redis的事物与mysql事物的最大区别是redis事物不支持事物回滚
	  事务：事务是指一组动作的执行，这一组动作要么都成功，要么都失败。
	 1. redis的简单事务，将一组需要一起执行的命令放到multi和exec两个命令之间，
	   其中multi代表事务开始，exec代表事务结束
	   
	 */
	
	/**
	 	发布和订阅:
	 		redis提供了“发布、订阅”模式的消息机制，其中消息订阅者与发布者不直接通信，发布者向指定的频道（channel）发布消息，订阅该频道的每个客户端都可以接收到消息
	 		redis主要提供发布消息、订阅频道、取消订阅以及按照模式订阅和取消订阅
			1.发布消息
			publish channel:test "hello world"
			2.订阅消息
			subscrible channel:test
			此时另一个客户端发布一个消息:publish channel:test "james test"
		和很多专业的消息队列（kafka rabbitmq）,redis的发布订阅显得很lower, 比如无法实现消息规程和回溯， 但就是简单，如果能满足应用场景，用这个也可以
			3.查看订阅数：
			pubsub numsub channel:test // 频道channel:test的订阅数
			4.取消订阅
			unsubscribe channel:test
			客户端可以通过unsubscribe命令取消对指定频道的订阅，取消后，不会再收到该频道的消息
			5.按模式订阅和取消订阅
			psubscribe ch* //订阅以ch开头的所有频道
			punsubscribe ch*  //取消以ch开头的所有频道
			6.应用场景：
			1、今日头条订阅号、微信订阅公众号、新浪微博关注、邮件订阅系统
			2、即时通信系统
			3、群聊部落系统（微信群）
	 */
	
	/**
	 * 
	 * TODO 可以看看他其他的博客
	 * Redis持久化存储
	 * Redis持久化存储(AOF与RDB两种模式)
	 * https://blog.csdn.net/canot/article/details/52886923
	 */


/**
 * 使用redis作为mybatis的二级缓存数据源
 * https://blog.csdn.net/canot/article/details/73695297
 * https://blog.csdn.net/canot/article/category/6675952
 */

/**
 * TODO(https://www.cnblogs.com/kismetv/p/9137897.html)
  	在 Redis 中，实现高可用的技术主要包括持久化、复制、哨兵和集群，下面分别说明它们的作用，以及解决了什么样的问题：
	持久化：持久化是最简单的高可用方法(有时甚至不被归为高可用的手段)，主要作用是数据备份，即将数据存储在硬盘，保证数据不会因进程退出而丢失。
	复制：复制是高可用 Redis 的基础，哨兵和集群都是在复制基础上实现高可用的。复制主要实现了数据的多机备份，以及对于读操作的负载均衡和简单的故障恢复。
	缺陷：故障恢复无法自动化，写操作无法负载均衡，存储能力受到单机的限制。
	哨兵：在复制的基础上，哨兵实现了自动化的故障恢复。
	缺陷：写操作无法负载均衡；存储能力受到单机的限制。
	集群：通过集群，Redis 解决了写操作无法负载均衡，以及存储能力受到单机限制的问题，实现了较为完善的高可用方案。
	
	持久化的功能：Redis 是内存数据库，数据都是存储在内存中。
	为了避免进程退出导致数据的永久丢失，需要定期将 Redis 中的数据以某种形式(数据或命令)从内存保存到硬盘；当下次 Redis 重启时，利用持久化文件实现数据恢复。
	除此之外，为了进行灾难备份，可以将持久化文件拷贝到一个远程位置。
	Redis 持久化分为 RDB 持久化和 AOF 持久化：
	前者将当前数据保存到硬盘
	后者则是将每次执行的写命令保存到硬盘（类似于 MySQL 的 binlog）
	由于 AOF 持久化的实时性更好，即当进程意外退出时丢失的数据更少，因此 AOF 是目前主流的持久化方式，不过 RDB 持久化仍然有其用武之地。
	
	
	RDB
		RDB 持久化是将当前进程中的数据生成快照保存到硬盘(因此也称作快照持久化)，保存的文件后缀是 RDB；当 Redis 重新启动时，可以读取快照文件恢复数据。
		RDB 持久化的触发分为手动触发和自动触发两种：
			手动触发
			自动触发
		1.手动触发：save 命令和 bgsave 命令都可以生成 RDB 文件。
		save 命令会阻塞 Redis 服务器进程，直到 RDB 文件创建完毕为止，在 Redis 服务器阻塞期间，服务器不能处理任何命令请求。
 		而 bgsave 命令会创建一个子进程，由子进程来负责创建 RDB 文件，父进程(即 Redis 主进程)则继续处理请求。
 		bgsave 命令执行过程中，只有 fork 子进程时会阻塞服务器，而对于 save 命令，整个过程都会阻塞服务器。S
 		2.自动触发：最常见的情况是在配置文件中通过 save m n，指定当 m 秒内发生 n 次变化时，会触发 bgsave。
 			其中 save 900 1 的含义是：当时间到 900 秒时，如果 Redis 数据发生了至少 1 次变化，则执行 bgsave。
 			save m n 的实现原理：Redis 的 save m n，是通过 serverCron 函数、dirty 计数器和 lastsave 时间戳来实现的。
 			serverCron 是 Redis 服务器的周期性操作函数，默认每隔 100ms 执行一次；该函数对服务器的状态进行维护，其中一项工作就是检查 save m n 配置的条件是否满足，如果满足就执行 bgsave。
 			dirty 计数器是 Redis 服务器维持的一个状态，记录了上一次执行 bgsave/save 命令后，服务器状态进行了多少次修改(包括增删改)；而当 save/bgsave 执行完成后，会将 dirty 重新置为 0。
 		save m n 的原理如下：每隔 100ms，执行 serverCron 函数；在 serverCron 函数中，遍历 save m n 配置的保存条件，只要有一个条件满足，就进行 bgsave。
 		对于每一个 save m n 条件，只有下面两条同时满足时才算满足：
				当前时间-lastsave > m
				dirty >= n	
		除了 save m n 以外，还有一些其他情况会触发 bgsave：
			在主从复制场景下，如果从节点执行全量复制操作，则主节点会执行 bgsave 命令，并将 RDB 文件发送给从节点。
			执行 shutdown 命令时，自动执行 RDB 持久化，如下图所示：
		3.bgsave執行流程
			1.Redis 父进程首先判断：当前是否在执行 save 或 bgsave/bgrewriteaof（后面会详细介绍该命令）的子进程，如果在执行则 bgsave 命令直接返回。
			bgsave/bgrewriteaof 的子进程不能同时执行，主要是基于性能方面的考虑：两个并发的子进程同时执行大量的磁盘写操作，可能引起严重的性能问题。
			2.父进程执行 fork 操作创建子进程，这个过程中父进程是阻塞的，Redis 不能执行来自客户端的任何命令。
			3.父进程 fork 后，bgsave 命令返回”Background saving started”信息并不再阻塞父进程，并可以响应其他命令。
			4.子进程创建 RDB 文件，根据父进程内存快照生成临时快照文件，完成后对原有文件进行原子替换。
			5.子进程发送信号给父进程表示完成，父进程更新统计信息。
		4.RDB文件
			存储路径
			RDB 文件的存储路径既可以在启动前配置，也可以通过命令动态设定。
			配置：dir 配置指定目录，dbfilename 指定文件名。默认是 Redis 根目录下的 dump.rdb 文件。
			动态设定：Redis 启动后也可以动态修改 RDB 存储路径，在磁盘损害或空间不足时非常有用；执行命令为 config set dir {newdir}和 config set dbfilename {newFileName}。
			a)RDB 文件格式
				REDIS：常量，保存着“REDIS”5 个字符。
				db_version：RDB 文件的版本号，注意不是 Redis 的版本号。
				SELECTDB 0 pairs：表示一个完整的数据库(0 号数据库)，同理 SELECTDB 3 pairs 表示完整的 3 号数据库。
				只有当数据库中有键值对时，RDB 文件中才会有该数据库的信息(上图所示的 Redis 中只有 0 号和 3 号数据库有键值对)；如果 Redis 中所有的数据库都没有键值对，则这一部分直接省略。
				其中：SELECTDB 是一个常量，代表后面跟着的是数据库号码；0 和 3 是数据库号码；pairs 则存储了具体的键值对信息，包括 key、value 值，及其数据类型、内部编码、过期时间、压缩信息等等。
				EOF：常量，标志 RDB 文件正文内容结束。
				check_sum：前面所有内容的校验和；Redis 在载入 RBD 文件时，会计算前面的校验和并与 check_sum 值比较，判断文件是否损坏。
			b)压缩
			c)启动时加载
				RDB 文件的载入工作是在服务器启动时自动执行的，并没有专门的命令。但是由于 AOF 的优先级更高，因此当 AOF 开启时，Redis 会优先载入 AOF 文件来恢复数据。
				只有当 AOF 关闭时，才会在 Redis 服务器启动时检测 RDB 文件，并自动载入。服务器载入 RDB 文件期间处于阻塞状态，直到载入完成为止。
				Redis 载入 RDB 文件时，会对 RDB 文件进行校验，如果文件损坏，则日志中会打印错误，Redis 启动失败。
			d)RDB常用配置总结
				save m n：bgsave 自动触发的条件；如果没有 save m n 配置，相当于自动的 RDB 持久化关闭，不过此时仍可以通过其他方式触发。
				stop-writes-on-bgsave-error yes：当 bgsave 出现错误时，Redis 是否停止执行写命令；设置为 yes，则当硬盘出现问题时，可以及时发现，避免数据的大量丢失。
				设置为 no，则 Redis 无视 bgsave 的错误继续执行写命令，当对 Redis 服务器的系统(尤其是硬盘)使用了监控时，该选项考虑设置为 no。
				rdbcompression yes：是否开启 RDB 文件压缩。
				rdbchecksum yes：是否开启 RDB 文件的校验，在写入文件和读取文件时都起作用；关闭 checksum 在写入文件和启动文件时大约能带来 10% 的性能提升，但是数据损坏时无法发现。
				dbfilename dump.rdb：RDB 文件名。
				dir ./：RDB 文件和 AOF 文件所在目录。
	AOF持久化：
		AOF 持久化(即 Append Only File 持久化)，则是将 Redis 执行的每次写命令记录到单独的日志文件中（有点像 MySQL 的 binlog），当 Redis 重启时再次执行 AOF 文件中的命令来恢复数据。
		与 RDB 相比，AOF 的实时性更好，因此已成为主流的持久化方案。
		1.开启AOF
			Redis 服务器默认开启 RDB，关闭 AOF；要开启 AOF，需要在配置文件中配置：appendonly yes。
		2.执行流程
			由于需要记录 Redis 的每条写命令，因此 AOF 不需要触发，下面介绍 AOF 的执行流程。
			AOF 的执行流程包括：
				1.命令追加(append)：将 Redis 的写命令追加到缓冲区 aof_buf。
				2.文件写入(write)和文件同步(sync)：根据不同的同步策略将 aof_buf 中的内容同步到硬盘。
				3.文件重写(rewrite)：定期重写 AOF 文件，达到压缩的目的。
			a)命令追加(append)
				Redis 先将写命令追加到缓冲区，而不是直接写入文件，主要是为了避免每次有写命令都直接写入硬盘，导致硬盘 IO 成为 Redis 负载的瓶颈。
				命令追加的格式是 Redis 命令请求的协议格式，它是一种纯文本格式，具有兼容性好、可读性强、容易处理、操作简单避免二次开销等优点，具体格式略。
				在 AOF 文件中，除了用于指定数据库的 select 命令（如 select 0 为选中 0 号数据库）是由 Redis 添加的，其他都是客户端发送来的写命令。
			b)文件写入(write)和文件同步(sync)
				Redis 提供了多种 AOF 缓存区的同步文件策略，策略涉及到操作系统的 write 函数和 fsync 函数，说明如下：
				为了提高文件写入效率，在现代操作系统中，当用户调用 write 函数将数据写入文件时，操作系统通常会将数据暂存到一个内存缓冲区里，当缓冲区被填满或超过了指定时限后，才真正将缓冲区的数据写入到硬盘里。
				这样的操作虽然提高了效率，但也带来了安全问题：如果计算机停机，内存缓冲区中的数据会丢失。
				因此系统同时提供了 fsync、fdatasync 等同步函数，可以强制操作系统立刻将缓冲区中的数据写入到硬盘里，从而确保数据的安全性。
				 
				AOF 缓存区的同步文件策略由参数 appendfsync 控制，各个值的含义如下：
				always：命令写入 aof_buf 后立即调用系统 fsync 操作同步到 AOF 文件，fsync 完成后线程返回。
				这种情况下，每次有写命令都要同步到 AOF 文件，硬盘 IO 成为性能瓶颈，Redis 只能支持大约几百 TPS 写入，严重降低了 Redis 的性能。
				即便是使用固态硬盘（SSD），每秒大约也只能处理几万个命令，而且会大大降低 SSD 的寿命。
				no：命令写入 aof_buf 后调用系统 write 操作，不对 AOF 文件做 fsync 同步；同步由操作系统负责，通常同步周期为 30 秒。
				这种情况下，文件同步的时间不可控，且缓冲区中堆积的数据会很多，数据安全性无法保证。
				everysec：命令写入 aof_buf 后调用系统 write 操作，write 完成后线程返回；fsync 同步文件操作由专门的线程每秒调用一次。
				everysec 是前述两种策略的折中，是性能和数据安全性的平衡，因此是 Redis 的默认配置，也是我们推荐的配置。
			c)文件重写(rewrite)
				随着时间流逝，Redis 服务器执行的写命令越来越多，AOF 文件也会越来越大；过大的 AOF 文件不仅会影响服务器的正常运行，也会导致数据恢复需要的时间过长。
				文件重写是指定期重写 AOF 文件，减小 AOF 文件的体积。需要注意的是，AOF 重写是把 Redis 进程内的数据转化为写命令，同步到新的 AOF 文件；不会对旧的 AOF 文件进行任何读取、写入操作！
				关于文件重写需要注意的另一点是：对于 AOF 持久化来说，文件重写虽然是强烈推荐的，但并不是必须的。即使没有文件重写，数据也可以被持久化并在 Redis 启动的时候导入。
				因此在一些实现中，会关闭自动的文件重写，然后通过定时任务在每天的某一时刻定时执行。
				文件重写之所以能够压缩 AOF 文件，原因在于：
				过期的数据不再写入文件。
				无效的命令不再写入文件：如有些数据被重复设值(set mykey v1，set mykey v2)、有些数据被删除了(sadd myset v1，del myset)等等。
				多条命令可以合并为一个：如 sadd myset v1，sadd myset v2，sadd myset v3 可以合并为 sadd myset v1 v2 v3。
				不过为了防止单条命令过大造成客户端缓冲区溢出，对于 list、set、hash、zset 类型的 key，并不一定只使用一条命令。
				而是以某个常量为界将命令拆分为多条。这个常量在 redis.h/REDIS_AOF_REWRITE_ITEMS_PER_CMD 中定义，不可更改，3.0 版本中值是 64。
				通过上述内容可以看出，由于重写后 AOF 执行的命令减少了，文件重写既可以减少文件占用的空间，也可以加快恢复速度。
					1.文件重写的触发
						1.手动触发
							手动触发，直接调用 bgrewriteaof 命令，该命令的执行与 bgsave 有些类似：都是 fork 子进程进行具体的工作，且都只有在 fork 时阻塞。
						2.自动触发
							根据 auto-aof-rewrite-min-size 和 auto-aof-rewrite-percentage 参数，以及 aof_current_size 和 aof_base_size 状态确定触发时机：
							auto-aof-rewrite-min-size：执行 AOF 重写时，文件的最小体积，默认值为 64MB。
							auto-aof-rewrite-percentage：执行 AOF 重写时，当前 AOF 大小(即 aof_current_size)和上一次重写时 AOF 大小(aof_base_size)的比值。
				2.文件重写的流程
					关于文件重写的流程，有两点需要特别注意：
					重写由父进程 fork 子进程进行。
					重写期间 Redis 执行的写命令，需要追加到新的 AOF 文件中，为此 Redis 引入了 aof_rewrite_buf 缓存。
					
					对照上图，文件重写的流程如下：
					1)：Redis 父进程首先判断当前是否存在正在执行 bgsave/bgrewriteaof 的子进程，如果存在则 bgrewriteaof 命令直接返回；如果存在 bgsave 命令则等 bgsave 执行完成后再执行，这个主要是基于性能方面的考虑。
					2)：父进程执行 fork 操作创建子进程，这个过程中父进程是阻塞的。
					3.1)：父进程 fork 后，bgrewriteaof 命令返回“Background append only file rewrite started”信息并不再阻塞父进程，并可以响应其他命令。
					Redis 的所有写命令依然写入 AOF 缓冲区，并根据 appendfsync 策略同步到硬盘，保证原有 AOF 机制的正确。
					3.2)：由于 fork 操作使用写时复制技术，子进程只能共享 fork 操作时的内存数据。
					由于父进程依然在响应命令，因此 Redis 使用 AOF 重写缓冲区(图中的 aof_rewrite_buf)保存这部分数据，防止新 AOF 文件生成期间丢失这部分数据。
					也就是说，bgrewriteaof 执行期间，Redis 的写命令同时追加到 aof_buf 和 aof_rewirte_buf 两个缓冲区。
					4)：子进程根据内存快照，按照命令合并规则写入到新的 AOF 文件。
					5.1)：子进程写完新的 AOF 文件后，向父进程发信号，父进程更新统计信息，具体可以通过 info persistence 查看。
					5.2)：父进程把 AOF 重写缓冲区的数据写入到新的 AOF 文件，这样就保证了新 AOF 文件所保存的数据库状态和服务器当前状态一致。
					5.3)：使用新的 AOF 文件替换老文件，完成 AOF 重写。
				3.AOF启动时加载
					前面提到过，当 AOF 开启时，Redis 启动时会优先载入 AOF 文件来恢复数据；只有当 AOF 关闭时，才会载入 RDB 文件恢复数据。
					当 AOF 开启，且 AOF 文件存在时，Redis 启动日志：
					当 AOF 开启，但 AOF 文件不存在时，即使 RDB 文件存在也不会加载(更早的一些版本可能会加载，但 3.0 不会)，Redis 启动日志如下：
				4.AOF配置文件总结
					下面是 AOF 常用的配置项，以及默认值：
					appendonly no：是否开启 AOF。
					appendfilename "appendonly.aof"：AOF 文件名。
					dir ./：RDB 文件和 AOF 文件所在目录。
					appendfsync everysec：fsync 持久化策略。
					no-appendfsync-on-rewrite no：AOF 重写期间是否禁止 fsync；如果开启该选项，可以减轻文件重写时 CPU 和硬盘的负载（尤其是硬盘），但是可能会丢失 AOF 重写期间的数据；需要在负载和安全性之间进行平衡。
					auto-aof-rewrite-percentage 100：文件重写触发条件之一。
					auto-aof-rewrite-min-size 64mb：文件重写触发提交之一。
					aof-load-truncated yes：如果 AOF 文件结尾损坏，Redis 启动时是否仍载入 AOF 文件。
TODO 比较：
RDB 持久化
	优点：RDB 文件紧凑，体积小，网络传输快，适合全量复制；恢复速度比 AOF 快很多。当然，与 AOF 相比，RDB 最重要的优点之一是对性能的影响相对较小。
	缺点：RDB 文件的致命缺点在于其数据快照的持久化方式决定了必然做不到实时持久化，而在数据越来越重要的今天，数据的大量丢失很多时候是无法接受的，因此 AOF 持久化成为主流。
	此外，RDB 文件需要满足特定格式，兼容性差（如老版本的 Redis 不兼容新版本的 RDB 文件）。
AOF 持久化
	与 RDB 持久化相对应，AOF 的优点在于支持秒级持久化、兼容性好，缺点是文件大、恢复速度慢、对性能影响大。		

1.策略选择
	在介绍持久化策略之前，首先要明白无论是 RDB 还是 AOF，持久化的开启都是要付出性能方面代价的：
	对于 RDB 持久化，一方面是 bgsave 在进行 fork 操作时 Redis 主进程会阻塞，另一方面，子进程向硬盘写数据也会带来 IO 压力。
	对于 AOF 持久化，向硬盘写数据的频率大大提高(everysec 策略下为秒级)，IO 压力更大，甚至可能造成 AOF 追加阻塞问题（后面会详细介绍这种阻塞）。
	此外，AOF 文件的重写与 RDB 的 bgsave 类似，会有 fork 时的阻塞和子进程的 IO 压力问题。
	相对来说，由于 AOF 向硬盘中写数据的频率更高，因此对 Redis 主进程性能的影响会更大。
	在实际生产环境中，根据数据量、应用对数据的安全要求、预算限制等不同情况，会有各种各样的持久化策略。
	如完全不使用任何持久化、使用 RDB 或 AOF 的一种，或同时开启 RDB 和 AOF 持久化等。
	此外，持久化的选择必须与 Redis 的主从策略一起考虑，因为主从复制与持久化同样具有数据备份的功能，而且主机 master 和从机 slave 可以独立的选择持久化方案。
2.场景套路
	下面分场景来讨论持久化策略的选择，讨论也只是作为参考，实际方案可能更复杂更具多样性：
	1.如果 Redis 中的数据完全丢弃也没有关系（如 Redis 完全用作 DB 层数据的 Cache），那么无论是单机，还是主从架构，都可以不进行任何持久化。
	2.在单机环境下（对于个人开发者，这种情况可能比较常见），如果可以接受十几分钟或更多的数据丢失，选择 RDB 对 Redis 的性能更加有利；如果只能接受秒级别的数据丢失，应该选择 AOF。
	3.但在多数情况下，我们都会配置主从环境，slave 的存在既可以实现数据的热备，也可以进行读写分离分担 Redis 读请求，以及在 master 宕掉后继续提供服务。
	4.master：完全关闭持久化（包括 RDB 和 AOF），这样可以让 master 的性能达到最好。
	5.slave：关闭 RDB，开启 AOF（如果对数据安全要求不高，开启 RDB 关闭 AOF 也可以），并定时对持久化文件进行备份（如备份到其他文件夹，并标记好备份的时间）。
	然后关闭 AOF 的自动重写，然后添加定时任务，在每天 Redis 闲时（如凌晨 12 点）调用 bgrewriteaof。
	这里需要解释一下，为什么开启了主从复制，可以实现数据的热备份，还需要设置持久化呢？
	因为在一些特殊情况下，主从复制仍然不足以保证数据的安全，例如：
		1.master 和 slave 进程同时停止：考虑这样一种场景，如果 master 和 slave 在同一栋大楼或同一个机房，则一次停电事故就可能导致 master 和 slave 机器同时关机，Redis 进程停止；如果没有持久化，则面临的是数据的完全丢失。
		2.master误重启：考虑这样一种场景，master 服务因为故障宕掉了，如果系统中有自动拉起机制（即检测到服务停止后重启该服务）将 master 自动重启，由于没有持久化文件，那么 master 重启后数据是空的，slave 同步数据也变成了空的；
		如果 master 和 slave 都没有持久化，同样会面临数据的完全丢失。
		需要注意的是，即便是使用了哨兵(关于哨兵后面会有文章介绍)进行自动的主从切换，也有可能在哨兵轮询到 master 之前，便被自动拉起机制重启了。
		因此，应尽量避免“自动拉起机制”和“不做持久化”同时出现。
3.异地灾备
	上述讨论的几种持久化策略，针对的都是一般的系统故障，如进程异常退出、宕机、断电等，这些故障不会损坏硬盘。
	但是对于一些可能导致硬盘损坏的灾难情况，如火灾地震，就需要进行异地灾备。
	例如对于单机的情形，可以定时将 RDB 文件或重写后的 AOF 文件，通过 scp 拷贝到远程机器，如阿里云、AWS 等。
	对于主从的情形，可以定时在 master 上执行 bgsave，然后将 RDB 文件拷贝到远程机器，或者在 slave 上执行 bgrewriteaof 重写 AOF 文件后，将 AOF 文件拷贝到远程机器上。
	一般来说，由于 RDB 文件文件小、恢复快，因此灾难恢复常用 RDB 文件；异地备份的频率根据数据安全性的需要及其他条件来确定，但最好不要低于一天一次。
TODO:	
fork 阻塞：CPU 的阻塞
*/
}
