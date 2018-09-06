package com.xyh.service.api;

import com.xyh.service.model.req.ServiceReq;
import com.xyh.service.model.resp.ServiceRes;

/**
 * 服务接口定义
 * @author hcxyh  2018年8月15日
 *
 */
public interface ServiceApi {
	
	public ServiceRes dubboServiceApi (ServiceReq  serviceReq);
	
	public String sayHello();
	
	ServiceRes findUser();
	
	ServiceRes getUser(int page, int pageSize);

	ServiceRes getUserForRedis(String key);
	
	ServiceRes selectUser();  //test transaction
	
	ServiceRes testLog();
	
	ServiceRes testAop();

}
