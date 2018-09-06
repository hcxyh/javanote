package com.xyh.service.model;

import java.io.Serializable;

/**
 * 服务实体类
 * @author hcxyh  2018年8月15日
 * 此处对应的是rpc交互所定义的实体类的公共参数.
 * 如果服务拆分的原子性比较好,此处是直接可以使用dao层的model.
 */
public class ServiceModel implements Serializable{
	
	private static final long serialVersionUID = -3555630467148015635L;

	private String reqId;
	
	private String resCode;
	
	private String resMsg;

	public String getReqId() {
		return reqId;
	}

	public void setReqId(String reqId) {
		this.reqId = reqId;
	}

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}
	
}
