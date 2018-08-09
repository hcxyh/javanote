package com.xyh.apache.guava;

/**
 * cache Object
 * @author hcxyh  2018年8月9日
 *
 */
public class CacheObj {
	
	private String cacheName;
	private String cacheId;
	private String cacheGroup;
	public String getCacheName() {
		return cacheName;
	}
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
	public String getCacheId() {
		return cacheId;
	}
	public void setCacheId(String cacheId) {
		this.cacheId = cacheId;
	}
	public String getCacheGroup() {
		return cacheGroup;
	}
	public void setCacheGroup(String cacheGroup) {
		this.cacheGroup = cacheGroup;
	}
	public CacheObj(String cacheName, String cacheId, String cacheGroup) {
		super();
		this.cacheName = cacheName;
		this.cacheId = cacheId;
		this.cacheGroup = cacheGroup;
	}
	
}
