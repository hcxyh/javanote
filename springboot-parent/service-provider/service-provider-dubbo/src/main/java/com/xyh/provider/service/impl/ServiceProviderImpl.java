package com.xyh.provider.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.xyh.dubbo.annotation.AopAnnotation;
import com.xyh.dubbo.exception.MyException;
import com.xyh.provider.service.mapper.ServiceProviderMapper;
import com.xyh.provider.service.model.ServiceProviderModel;
import com.xyh.service.api.ServiceApi;
import com.xyh.service.model.req.ServiceReq;
import com.xyh.service.model.resp.ServiceRes;


/**
 * 服务接口提供
 * @author hcxyh  2018年8月15日
 *
 */
@Service(version = "1.0.0", interfaceClass = ServiceApi.class)
public class ServiceProviderImpl implements ServiceApi{
	
	@Autowired
	ServiceProviderMapper serviceProviderMapper;
	
	@Override
	public ServiceRes dubboServiceApi(ServiceReq serviceReq) {
		
		return null;
	}

	@Override
	public String sayHello() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return dateFormat.format(new Date()) + ": " + "bonjour nana";
	}

	@Override
	public ServiceRes<ServiceProviderModel> findUser() {
		ServiceProviderModel serviceProviderModel = new ServiceProviderModel();
		serviceProviderModel.setId(UUID.randomUUID().toString());
		serviceProviderModel.setAddress("xi'an");
		serviceProviderModel.setAge("26");
		serviceProviderModel.setName("xyh");
		ServiceRes<ServiceProviderModel> serviceRes = new ServiceRes<ServiceProviderModel>();
		serviceRes.setT(serviceProviderModel);
		return serviceRes;
	}
	
	
	@Override
	public ServiceRes getUser(int page, int pageSize) {
		PageHelper.startPage(page, pageSize);
		ServiceRes serviceRes = new ServiceRes<>();
		serviceRes.setT(serviceProviderMapper.getUsers());
		return serviceRes;
	}

	@Override
	public ServiceRes getUserForRedis(String key) {
		return null;
	}
	
	
	//事务测试
	@Override
	@AopAnnotation
	public ServiceRes selectUser(){
		ServiceProviderModel serviceProviderModel = new ServiceProviderModel();
		serviceProviderModel.setId(Integer.toString(2));
		serviceProviderModel.setAddress("xi'an");
		serviceProviderModel.setAge("26");
		serviceProviderModel.setName("xyh");
		int i = serviceProviderMapper.insertUser(serviceProviderModel);
		try {
			throw new Exception();
		} catch (Exception e) {
			throw new MyException();
		}
	}

	@Override
	public ServiceRes testLog() {
		return null;
	}

	@Override
	public ServiceRes testAop() {
		return null;
	}
	
	

}
