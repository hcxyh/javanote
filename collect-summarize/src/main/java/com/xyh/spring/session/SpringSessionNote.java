package com.xyh.spring.session;

/**
 * 	springsession+springsecroty
 * 	总结：
 * 	1.使用第三方仓储来实现集群session管理，也就是常说的分布式session容器，
 	      替换应用容器（如tomcat的session容器）。
	仓储的实现，Spring Session提供了三个实现（redis，mongodb，jdbc），其中redis使我们最常用的。
	程序的实现，使用AOP技术，几乎可以做到透明化地替换。（核心）
	2.可以非常方便的扩展Cookie和自定义Session相关的Listener，Filter。
	3.可以很方便的与Spring Security集成，增加诸如findSessionsByUserName，rememberMe，
	限制同一个账号可以同时在线的Session数（如设置成1，即可达到把前一次登录顶掉的效果）等等
 * @author hcxyh  2018年8月11日
 *
 */
public class SpringSessionNote {
	
	/**
	因为我们在项目中使用的spring-session + redis,特地分析下redis中可以的存储.  --xyh
		1.spring:session是默认的Redis HttpSession前缀（redis中，我们常用':'作为分割符）。
		2.每一个session都会有三个相关的key，第三个key最为重要，它是一个HASH数据结构,
		将内存中的session信息序列化到了redis中。
		3.另外两个key,
			expirations:1504446540000  SET类型
			sessions:expires:7079...   String类型
		Q:疑问,redis自身就有过期时间的设置方式TTL，为什么要额外添加两个key来维持session过期的特性呢？
		A:这需要对redis有一定深入的了解才能想到这层设计。
			redis清除过期key的行为是一个异步行为且是一个低优先级的行为，
			用redis文档中的原话来说便是，可能会导致session不被清除。
			于是引入了专门的expiresKey，来专门负责session的清除，
		包括我们自己在使用redis时也需要关注这一点。在开发层面，我们仅仅需要关注第三个key就行了。
	 */
	
	
	/**
	 TODO
	 	springSession解析：
	 	1.redis中session的存储结构
			A:"spring:session:sessions:39feb101-87d4-42c7-ab53-ac6fe0d91925"
			B:"spring:session:expirations:1523934840000"
			B:"spring:session:sessions:expires:39feb101-87d4-42c7-ab53-ac6fe0d91925"
			他们公用的前缀是 spring:session
			1.A 类型键的组成是前缀 +”sessions”+sessionId，对应的值是一个 hash 数据结构。
			在我的 demo 中，其值如下
			其中 creationTime（创建时间），lastAccessedTime（最后访问时间），
			maxInactiveInterval（session 失效的间隔时长） 等字段是系统字段，
			sessionAttr:xx 可能会存在多个键值对，用户存放在 session 中的数据如数存放于此。
			A 类型键对应的默认 TTL 是 35 分钟。
			2.B 类型键的组成是前缀+”expirations”+时间戳，无需纠结这个时间戳的含义，先卖个关子。
			其对应的值是一个 set 数据结构，这个 set 数据结构中存储着一系列的 C 类型键。在我的 demo 中，其值如下:
			B 类型键对应的默认 TTL 是 30 分钟
			3.C 类型键的组成是前缀+”sessions:expires”+sessionId，对应一个空值，
			它仅仅是 sessionId 在 redis 中的一个引用，具体作用继续卖关子。
			C 类型键对应的默认 TTL 是 30 分钟。
		
		FIXME 自己设定session管理机制
		2.kirito-session 的天使轮方案
			1.A:
				然后对 A 类型的键设置 ttl A 30 分钟，这样 30分钟之后 session 过期，
				0-30 分钟期间如果用户持续操作，那就根据 sessionId 找到 A 类型的 key，
				刷新 lastAccessedTime 的值，并重新设置 ttl，这样就完成了「续签」的特性。
					{
						redis 在键实际过期之后不一定会被删除，可能会继续存留，
						但具体存留的时间我没有做过研究，可能是 1~2 分钟，可能会更久。
						具有过期时间的 key 有两种方式来保证过期，一是这个键在过期的时候被访问了，
						二是后台运行一个定时任务自己删除过期的 key。划重点：
						这启发我们在 key 到期后只需要访问一下 key 就可以确保 redis 删除该过期键
						如果没有指令持续关注 key，并且 redis 中存在许多与 TTL 关联的 key，
						则 key 真正被删除的时间将会有显著的延迟！显著的延迟！显著的延迟！
					}
			2.A 轮改造—引入 B 类型键确保 session 的过期机制
				{
					redis 的官方文档启发我们，可以启用一个后台定时任务，定时去删除那些过期的键，
					配合上 redis 的自动过期，这样可以双重保险。第一个问题来了，
					我们将这些过期键存在哪儿呢？不找个合适的地方存起来，
					定时任务到哪儿去删除这些应该过期的键呢？总不能扫描全库吧！来解释我前面卖的第一个关子，
					看看 B 类型键的特点：
					spring:session:expirations:1523934840000
					时间戳的含义
						1523934840000 这明显是个 Unix 时间戳，它的含义是存放着这一分钟内应该过期的键，
						所以它是一个 set 数据结构。解释下这个时间戳是怎么计算出来的
						org.springframework.session.data.redis.RedisSessionExpirationPolicy#roundUpToNextMinute
						{
							还记得 lastAccessedTime=1523933008926，maxInactiveInterval=1800 吧，lastAccessedTime 转换成北京时间是:
							2018/4/1710:43:28，向上取整是 2018/4/1710:44:00，
							再次转换为 Unix 时间戳得到 1523932980000，单位是 ms，1800 是过期时间的间隔，单位是 s，
							二者相加 1523932980000+1800*1000=1523934840000。这样 B 类型键便作为了一个「桶」，
							存放着这一分钟应当过期的 session 的 key。
						}
				1.后台定时任务
					org.springframework.session.data.redis.RedisSessionExpirationPolicy#cleanupExpiredSessions
					后台提供了定时任务去“删除”过期的 key，来补偿 redis 到期未删除的 key。
					方案再描述下，方便大家理解：取得当前时间的时间戳作为 key，去 redis 中定位到
					 spring:session:expirations:{当前时间戳} ，这个 set 里面存放的便是所有过期的 key 了。
				2.续签的影响
					每次 session 的续签，需要将旧桶中的数据移除，放到新桶中。验证这一点很容易。
					在第一分钟访问一次 http://localhost:8080/helloworld 端点，
					得到的 B 类型键为：spring:session:expirations:1523934840000；
					第二分钟再访问一次 http://localhost:8080/helloworld 端点，
					A 类型键的 lastAccessedTime 得到更新，
					并且 spring:session:expirations:1523934840000 这个桶被删除了，
					新增了 spring:session:expirations:1523934900000 这个桶。
					当众多用户活跃时，桶的增删和以及 set 中数据的增删都是很频繁的。
					对了，没提到的一点，对应 key 的 ttl 时间也会被更新。
					kirito-session 方案貌似比之前严谨了，目前为止使用了 A 类型键和 B 类型键解决了 session 存储和 redis 键到期不删除的两个问题，但还是存在问题的。
				}
			3.B 轮改造—优雅地解决 B 类型键的并发问题
				引入 B 类型键看似解决了问题，却也引入了一个新的问题：并发问题。
				来看看一个场景：
				假设存在一个 sessionId=1 的会话，初始时间戳为 1420656360000
				spring:session:expirations:1420656360000 -> [1]
				spring:session:session:1 -> <session>
			接下来迎来了并发访问，（用户可能在浏览器中多次点击）：
				{
					a)线程 1 在第 2 分钟请求，产生了续签，session:1 应当从 1420656360000 这个桶移动到 142065642000 这个桶
					b)线程 2 在第 3 分钟请求，也产生了续签，session:1 本应当从 1420656360000 这个桶移动到 142065648000 这个桶
					如果上两步按照次序执行，自然不会有问题。但第 3 分钟的请求可能已经执行完毕了，第 2 分钟才刚开始执行。
				}
			一种简单的方法是用户的每次 session 续期加上分布式锁，这显然不能被接受。来看看 Spring Session 是怎么巧妙地应对这个并发问题的。
			org.springframework.session.data.redis.RedisSessionExpirationPolicy#cleanExpiredSessions
			{
				这里面逻辑主要是拿到过期键的集合（实际上是 C 类型的 key，但这里可以理解为 sessionId，C 类型我下面会介绍），此时这个集合里面存在三种类型的 sessionId。
				已经被 redis 删除的过期键。万事大吉，redis 很靠谱的及时清理了过期的键。
				已经过期，但是还没来得及被 redis 清除的 key。还记得前面 redis 文档里面提到的一个技巧吗？我们在 key 到期后只需要访问一下 key 就可以确保 redis 删除该过期键，所以 redis.hasKey(key); 该操作就是为了触发 redis 的自己删除。
				并发问题导致的多余数据，实际上并未过期。如上所述，第 32 分钟的桶里面存在的 session:1 实际上并不应该被删除，使用 touch 的好处便是我只负责检测，删不删交给 redis 判断。session:1 在第 32 分钟被 touch 了一次，并未被删除，在第 33 分钟时应当被 redis 删除，但可能存在延时，这个时候 touch 一次，确保删除。
				所以，源码里面特别强调了一下：要用 touch 去触发 key 的删除，而不能直接 del key。
				https://github.com/spring-projects/spring-session/issues/93
			}	
		3.C 轮改造—增加 C 类型键完善过期通知事件
			虽然引入了 B 类型键，并且在后台加了定时器去确保 session 的过期，
			但似乎…emmmmm…还是不够完善。在此之前，kirito-session 的设计方案中，
			存储 session 实际内容的 A 类型键和用于定时器确保删除的桶 B 类型键过期时间都是 30 分钟(key 的 TTL 是 30 分钟)，
			注意一个细节，spring-session 中 A 类型键的过期时间是 35 分钟，
			比实际的 30 分钟多了 5 分钟，这意味着即便 session 已经过期，
			我们还是可以在 redis 中有 5 分钟间隔来操作过期的 session。
			于此同时，spring-session 引入了 C 类型键来作为 session 的引用。
			解释下之前卖的第二个关子，C 类型键的组成为前缀+”sessions:expires”+sessionId，
			对应一个空值，同时也是 B 类型键桶中存放的 session 引用，ttl 为 30 分钟，
			具体作用便是在自身过期后触发 redis 的 keyspace notifications 
			具体如何监听redis 的过期事件简单介绍下：
			 org.springframework.session.data.redis.config.ConfigureNotifyKeyspaceEventsAction 该类配置了相关的过期监听，并使用 SessionExpiredEvent 事件发放 session 的过期事件。为什么引入 C 类型键？keyspace notifications 只会告诉我们哪个键过期了，不会告诉我们内容是什么。关键就在于如果 session 过期后监听器可能想要访问 session 的具体内容，然而自身都过期了，还怎么获取内容。所以，C 类型键存在的意义便是解耦 session 的存储和 session 的过期，并且使得 server 获取到过期通知后可以访问到 session 真实的值。对于用户来说，C 类型键过期后，意味着登录失效，而对于服务端而言，真正的过期其实是 A 类型键过期，这中间会有 5 分钟的误差。

		4.一点点想法，担忧，疑惑
			以上介绍了 Spring Session 的三种 key 的原因，理清楚其中的逻辑花了不少时间，
		项目改造正好涉及到相关的缓存值过期这一需求，完全可以参考 Spring Session 的方案。
		但担忧也是有的，如果真的只是 1~2 两分钟的延迟过期（对应 A 轮改造中遇到的问题），
		以及 1 分钟的提前删除（对应 B 轮改造中的并发问题）其实个人感觉没必要计较。
		从产品体验上来说，用户应该不会在意 32 分钟自动退出和 30 分钟退出，
		可以说 Spring Session 是为了严谨而设计了这一套方案，但引入了定时器和很多辅助的键值对，
		无疑对内存消耗和 cpu 消耗都是一种浪费。
		如果在生产环境大量使用 Spring Session，最好权衡下本文提及的相关问题。
	 
	 
	 */
	
}
