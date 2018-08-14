package com.xyh.cache;


/**
 * https://mp.weixin.qq.com/s/pkGHXNCqGVbVofKwI2yP4A
 * @author hcxyh  2018年8月7日
 *
 */
public class RedisInterview {
	
	/**
	 1.redis的回收策略（淘汰算法）
	 	volatile-lru：从已设置过期时间的数据集（server.db[i].expires）中挑选最近最少使用的数据淘汰
		volatile-ttl：从已设置过期时间的数据集（server.db[i].expires）中挑选将要过期的数据淘汰
		volatile-random：从已设置过期时间的数据集（server.db[i].expires）中任意选择数据淘汰
		allkeys-lru：从数据集（server.db[i].dict）中挑选最近最少使用的数据淘汰
		allkeys-random：从数据集（server.db[i].dict）中任意选择数据淘汰
		no-enviction（驱逐）：禁止驱逐数据
		
	 2.缓存雪崩
	 	缓存雪崩我们可以简单的理解为：由于原有缓存失效，新缓存未到期间(例如：我们设置缓存时采用了相同的过期时间，在同一时刻出现大面积的缓存过期)，
	 	所有原本应该访问缓存的请求都去查询数据库了，而对数据库CPU和内存造成巨大压力，严重的会造成数据库宕机。
	 	从而形成一系列连锁反应，造成整个系统崩溃。
	 	解决方案：
	 	a)碰到这种情况，一般并发量不是特别多的时候，使用最多的解决方案是加锁排队.
	 	加锁排队只是为了减轻数据库的压力，并没有提高系统吞吐量。假设在高并发下，缓存重建期间key是锁着的，
	 	这是过来1000个请求999个都在阻塞的。同样会导致用户等待超时，这是个治标不治本的方法！
		注意：加锁排队的解决方式分布式环境的并发问题，有可能还要解决分布式锁的问题；线程还会被阻塞，用户体验很差！
		因此，在真正的高并发场景下很少使用！
		b)给每一个缓存数据增加相应的缓存标记，记录缓存的是否失效，如果缓存标记失效，则更新数据缓存
		{
			1、缓存标记：记录缓存数据是否过期，如果过期会触发通知另外的线程在后台去更新实际key的缓存；
			2、缓存数据：它的过期时间比缓存标记的时间延长1倍，例：标记缓存时间30分钟，数据缓存设置为60分钟。 
			这样，当缓存标记key过期后，实际缓存还能把旧数据返回给调用端，直到另外的线程在后台更新完成后，才会返回新缓存。
			关于缓存崩溃的解决方法，这里提出了三种方案：
			使用锁或队列、设置过期标志更新缓存、为key设置不同的缓存失效时间，还有一各被称为“二级缓存”的解决方法，
		}
		c)
		{
			通常的解决办法是对不同的数据使用不同的失效时间，甚至对相同的数据、不同的请求使用不同的失效时间，
			例如，我们要缓存user数据，会对每个用户的数据设置不同的缓存过期时间，可以定义一个基础时间，假设10秒，
			然后加上一个两秒以内的随机数，过期时间为10～12秒，就会避免缓存雪崩。
		}
		
		缓存穿透:
		缓存穿透指的是使用不存在的key进行大量的高并发查询，这导致缓存无法命中，每次请求都要穿透到后端数据库系统进行查询，使数据库压力过大，甚至使数据库服务被压死。
		缓存穿透是指用户查询数据，在数据库没有，自然在缓存中也不会有。这样就导致用户查询的时候，在缓存中找不到，
		每次都要去数据库再查询一遍，然后返回空（相当于进行了两次无用的查询）。这样请求就绕过缓存直接查数据库，
		这也是经常提的缓存命中率问题。
		缓存穿透解决方案：
		（1）采用布隆过滤器，将所有可能存在的数据哈希到一个足够大的bitmap中，一个一定不存在的数据会被这个bitmap拦截掉，
		从而避免了对底层存储系统的查询压力。
		（2）如果一个查询返回的数据为空（不管是数据不存在，还是系统故障），我们仍然把这个空结果进行缓存，但它的过期时间会很短，
		最长不超过五分钟。通过这个直接设置的默认值存放到缓存，这样第二次到缓存中获取就有值了，而不会继续访问数据库，
		这种办法最简单粗暴！
		eg:
		把空结果也给缓存起来，这样下次同样的请求就可以直接返回空了，即可以避免当查询的值为空时引起的缓存穿透。同时也可以单独设置个缓存区域存储空值，
		对要查询的key进行预先校验，然后再放行给后面的正常缓存处理逻辑。
		我们通常将空值缓存起来，再次接收到同样的查询请求时，若命中缓存并且值为空，就会直接返回，不会透传到数据库，
		避免缓存穿透。当然，有时恶意袭击者可以猜到我们使用了这种方案，每次都会使用不同的参数来查询，
		这就需要我们对输入的参数进行过滤，例如，如果我们使用ID进行查询，则可以对ID的格式进行分析，
		如果不符合产生ID的规则，就直接拒绝，或者在ID上放入时间信息，根据时间信息判断ID是否合法，
		或者是否是我们曾经生成的ID，这样可以拦截一定的无效请求。
		
		
		缓存预热
		 缓存预热就是系统上线后，提前将相关的缓存数据直接加载到缓存系统。避免在用户请求的时候，先查询数据库，
		 然后再将数据缓存的问题！用户直接查询事先被预热的缓存数据！
		 缓存预热解决方案：
		（1）直接写个缓存刷新页面，上线时手工操作下；
		（2）数据量不大，可以在项目启动的时候自动进行加载；
		（3）定时刷新缓存；
	 3.一致性hash
	 
	 
	 4.缓存与数据库数据不一致
	 	TODO https://mp.weixin.qq.com/s/XxgVsAxp5axplNDatfXzrA
	 	读请求: 先操作缓存还是先操作数据库?
	 		先读cache，再读db
				如果，cache hit，则直接返回数据
				如果，cache miss，则访问db，并将数据set回缓存
	 	写请求: 先操作缓存还是先操作数据库?
	 
	 */
	
}
