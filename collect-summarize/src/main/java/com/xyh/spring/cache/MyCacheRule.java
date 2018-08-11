package com.xyh.spring.cache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import net.sf.ehcache.Element;

/**
 * 实现SpringCache的接口,自定义缓存规则.
 * 此处以Ehcache+redis,实现二级缓存为例.
 * @author hcxyh  2018年8月11日
 *
 */
public class MyCacheRule implements Cache{
	
	private static final Logger LOG = LoggerFactory.getLogger(MyCacheRule.class);  
	  
    private String name;  
  
    //Ehcache,一定容量的LRU队列   
    private net.sf.ehcache.Cache ehCache;  
      
    //redis,无容量限制key带时效性
    private RedisTemplate<String, Object> redisTemplate;  
  
    private long liveTime = 100000; // 默认1h=1*60*60  
  
    private int activeCount = 10;  
  
    @Override  
    public String getName() {  
        return this.name;  
    }  
  
    @Override  
    public Object getNativeCache() {  
        return this;  
    }  
  
    /** 
     * 获取自定义缓存 
     */  
    @Override  
    public ValueWrapper get(Object key) {  
        Element value = ehCache.get(key);  
        LOG.info("Cache L1 (ehcache) :{"+name+"}{"+key+"}={"+value+"}");  
        if (value != null) {  
            // TODO 访问10次EhCache 强制访问一次redis 使得数据不失效  
          if (value.getHitCount() < activeCount) {  
                return (value != null ? new SimpleValueWrapper(value.getObjectValue()) : null);  
          } else {  
              value.resetAccessStatistics();  
          }  
        }  
        final String keyStr = name+"_"+key.toString(); 
        
        Object objectValue = redisTemplate.execute(new RedisCallback<Object>() {  
            public Object doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                byte[] key = keyStr.getBytes();  
                byte[] value = connection.get(key);  
                if (value == null) {  
                    return null;  
                }  
                // 每次获得延迟时间  
                if (liveTime > 0) {  
                    connection.expire(key, liveTime);  
                }  
                return toObject(value);  
            }  
        }, true);  
        
        ehCache.put(new Element(key, objectValue));// 取出来之后缓存到本地  
        
        LOG.info("Cache L2 (redis) :{"+name+"}{"+key+"}={"+objectValue+"}");  
        return (objectValue != null ? new SimpleValueWrapper(objectValue) : null);  
  
    }  
  
    /** 
     * 更新自定义缓存 
     */  
    @Override  
    public void put(Object key, Object value) {  
        ehCache.put(new Element(key, value));  
        
        final String keyStr = key.toString();  
        final Object valueStr = value;  
        
        redisTemplate.execute(new RedisCallback<Long>() {  
            public Long doInRedis(RedisConnection connection) throws DataAccessException {  
                byte[] keyb = keyStr.getBytes();  
                byte[] valueb = toByteArray(valueStr);  
                connection.set(keyb, valueb);  
                if (liveTime > 0) {  
                    connection.expire(keyb, liveTime);  
                }  
                return 1L;  
            }  
        }, true);  
  
    }  
  
    /** 
     * 删除指定key缓存 
     */  
    @Override  
    public void evict(Object key) {  
        ehCache.remove(key);
        
        final String keyStr = key.toString();  
        redisTemplate.execute(new RedisCallback<Long>() {  
            public Long doInRedis(RedisConnection connection) throws DataAccessException {  
                return connection.del(keyStr.getBytes());  
            }  
        }, true);  
    }  
  
    /** 
     * 清除缓存 
     */  
    @Override  
    public void clear() {  
        ehCache.removeAll();  
        redisTemplate.execute(new RedisCallback<String>() {  
            public String doInRedis(RedisConnection connection)  
                    throws DataAccessException {  
                connection.flushDb();  
                return "clear done.";  
            }  
        }, true);  
    }  
  
      
    public net.sf.ehcache.Cache getEhCache() {  
        return ehCache;  
    }  
  
    public void setEhCache(net.sf.ehcache.Cache ehCache) {  
        this.ehCache = ehCache;  
    }  
  
    public RedisTemplate<String, Object> getRedisTemplate() {  
        return redisTemplate;  
    }  
  
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {  
        this.redisTemplate = redisTemplate;  
    }  
  
    public long getLiveTime() {  
        return liveTime;  
    }  
  
    public void setLiveTime(long liveTime) {  
        this.liveTime = liveTime;  
    }  
  
    public void setName(String name) {  
        this.name = name;  
    }  
  
    public int getActiveCount() {  
        return activeCount;  
    }  
  
    public void setActiveCount(int activeCount) {  
        this.activeCount = activeCount;  
    }  
  
    private byte[] toByteArray(Object obj) {  
        byte[] bytes = null;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        try {  
            ObjectOutputStream oos = new ObjectOutputStream(bos);  
            oos.writeObject(obj);  
            oos.flush();  
            bytes = bos.toByteArray();  
            oos.close();  
            bos.close();  
        } catch (IOException ex) {  
            ex.printStackTrace();  
        }  
        return bytes;  
    }  
  
    private Object toObject(byte[] bytes) {  
        Object obj = null;  
        try {  
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);  
            ObjectInputStream ois = new ObjectInputStream(bis);  
            obj = ois.readObject();  
            ois.close();  
            bis.close();  
        } catch (IOException ex) {  
            ex.printStackTrace();  
        } catch (ClassNotFoundException ex) {  
            ex.printStackTrace();  
        }  
        return obj;  
    }  
  

    @Override  
    public <T> T get(Object key, Class<T> type) {  
        if (StringUtils.isEmpty(key) || null == type) {  
            return null;  
        } else {  
//          final String finalKey;  
            final Class<T> finalType = type;  
//          if (key instanceof String) {  
//              finalKey = (String) key;  
//          } else {  
//              finalKey = key.toString();  
//          }  
//          final Object object = this.get(finalKey);  
            final Object object = this.get(key);  
            if (finalType != null && finalType.isInstance(object)  
                    && null != object) {  
                return (T) object;  
            } else {  
                return null;  
            }  
        }  
    }  
  

    @Override  
    public ValueWrapper putIfAbsent(Object key, Object value) {  
//      final String finalKey;  StringUtils.isEmpty(key) || StringUtils.isEmpty(value)
        if (key == null) {  
            return null;  
        } else {  
//          if (key instanceof String) {  
//              finalKey = (String) key;  
//          } else {  
//              finalKey = key.toString();  
//          }  
//          if (!StringUtils.isEmpty(finalKey)) {  
//              final Object finalValue = value;  
                this.put(key, value);  
//          }  
        }  
        return new SimpleValueWrapper(value);  
    }

	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {
		// TODO Auto-generated method stub
		return null;
	}  
	
	
	/**
     * 自定义生成redis-key
     *
     * @return
     */
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... objects) {
				StringBuilder sb = new StringBuilder();
                sb.append(target.getClass().getName()).append(".");
                sb.append(method.getName()).append(".");
                for (Object obj : objects) {
                    sb.append(obj.toString());
                }
                System.out.println("keyGenerator=" + sb.toString());
                return sb.toString();
			}
        };
    }
	
}
