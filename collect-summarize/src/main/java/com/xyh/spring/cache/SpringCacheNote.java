package com.xyh.spring.cache;

/**
 * springCche使用笔记
 * @author hcxyh  2018年8月11日
 *
 */
public class SpringCacheNote {
	
	/**
	 * key的使用
	 * @Cacheable(value="cacheName", key"#id")  
		public ResultDTO method(int id);  
	 * 
	 * @Cacheable(value="cacheName", key"#user.id)  
		public ResultDTO method(User user);  
	 * 
	 * @Cacheable(value="cacheName", key"T(String).valueOf(#name).concat('-').concat(#password))  
		public ResultDTO method(int name, String password);
	 * 
	 * @Cacheable(value="gomeo2oCache", keyGenerator = "keyGenerator")  
		public ResultDTO method(User user);  
	 * 
	 */
	
	/**
	 * 	@Cacheable 触发缓存入口
	 *	@CacheEvict 触发移除缓存
	 *	@CacahePut 更新缓存
	 *	@Caching 将多种缓存操作分组
	 *
	 *	value (也可使用 cacheNames) : 可看做命名空间，表示存到哪个缓存里了。
		key : 表示命名空间下缓存唯一key,使用Spring Expression Language(简称SpEL,详见参考文献[5])生成。
		condition : 表示在哪种情况下才缓存结果(对应的还有unless,哪种情况不缓存),同样使用SpEL
		
		---------------------------------------
		记住在spring.xml文件中使用spel直接调用方法的巨大神坑.
		---------------------------------------
	 *
	 *
	 *	@CacheConfig 类级别的缓存注解，允许共享缓存名称
	 *	@CacheConfig(cacheNames = "user") 对应的是 cacheManager中对应的cacheName
	 *
	 *	@Cacheable
		一般用于查询操作，根据key查询缓存.
		如果key不存在，查询db，并将结果更新到缓存中。
		如果key存在，直接查询缓存中的数据。
		
		@CachePut
		一般用于更新和插入操作，每次都会请求db 
		通过key去redis中进行操作。 
		1. 如果key存在，更新内容 
		2. 如果key不存在，插入内容。
		@CachePut(key = "\"user_\" + #user.id")  自己声明key
	 *  
	 *  @CacheEvict
		根据key删除缓存中的数据。allEntries=true表示删除缓存中的所有数据。
	 *  
	 *  
	 *  @Caching(
            put = {
                    @CachePut(value = "user", key = "\"user_\" + #user.id"),
                    @CachePut(value = "user", key = "#user.name"),
                    @CachePut(value = "user", key = "#user.account")
            },
            cacheable = {
            
            }
    	)
	 *  
	 *  @Caching(
	        put = {
	                @CachePut(value = "user", key = "\"user_\" + #user.id"),
	                @CachePut(value = "user", key = "#user.name"),
	                @CachePut(value = "user", key = "#user.account")
	        }
		)
		@Target({ElementType.METHOD, ElementType.TYPE})
		@Retention(RetentionPolicy.RUNTIME)
		@Inherited
		@Documented
		public @interface SaveUserInfo {
		
		}
	 *  
	 *  
	 *  
	 *  
	 */
	
}
