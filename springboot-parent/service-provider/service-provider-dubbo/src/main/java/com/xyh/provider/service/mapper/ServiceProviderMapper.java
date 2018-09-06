package com.xyh.provider.service.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import com.xyh.provider.service.model.ServiceProviderModel;

/**
 * 
 * @author hcxyh  2018年8月15日
 *
 */
public interface ServiceProviderMapper {
	
	// 使用注解的方式
	@Select("select * from t_user where name like concat('%',#{name},'%')")
	public List<ServiceProviderModel> likeName(String name);

	@Select("select * from t_user where id = #{id}")
	public ServiceProviderModel getById(Long id);
	
	@Insert("insert into t_test value(#{id},#{name},#{address})")
	public int insertUser(ServiceProviderModel serviceProviderModel);

	// 使用xml的方式
	public List<ServiceProviderModel> getUsers();

}
