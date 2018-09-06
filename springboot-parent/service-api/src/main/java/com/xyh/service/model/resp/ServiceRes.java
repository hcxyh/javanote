package com.xyh.service.model.resp;

import com.xyh.service.model.ServiceModel;

/**
 * 服务接口出参
 * @author hcxyh  2018年8月15日
 *
 */
public class ServiceRes<T> extends ServiceModel{
	
	private T t;

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}
}
