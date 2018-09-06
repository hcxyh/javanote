/**
 * 
 */
package com.xyh.provider.config;

import java.util.Properties;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * @author hcxyh  2018年8月16日
 *
 */
@Configuration
public class TransactionConfiguration {
	
	
//	  1.xml配置注解@transaction事务
//	  2.编程
//	  <!--======= 事务配置 Begin ==公式=============== -->
//    <!-- 事务管理器（由Spring管理MyBatis的事务） -->
//    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
//        <!-- 关联数据源 -->
//        <property name="dataSource" ref="dataSource"></property>
//    </bean>
//    <!-- 注解事务 -->
//    <tx:annotation-driven transaction-manager="transactionManager"/>
//    <!--======= 事务配置 End =================== -->
	
	
	@Bean(name = "transactionInterceptor")
	public TransactionInterceptor transactionInterceptor(PlatformTransactionManager platformTransactionManager) {
		TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
		// 事物管理器
		transactionInterceptor.setTransactionManager(platformTransactionManager);
		Properties transactionAttributes = new Properties();

		// 新增
		transactionAttributes.setProperty("insert*", "PROPAGATION_REQUIRED,-Throwable");
		// 修改
		transactionAttributes.setProperty("update*", "PROPAGATION_REQUIRED,-Throwable");
		// 删除
		transactionAttributes.setProperty("delete*", "PROPAGATION_REQUIRED,-Throwable");
		// 查询
		transactionAttributes.setProperty("select*", "PROPAGATION_REQUIRED,-Throwable, readOnly");

		transactionInterceptor.setTransactionAttributes(transactionAttributes);
		return transactionInterceptor;
	}

	// 代理到ServiceImpl的Bean
	@Bean
	public BeanNameAutoProxyCreator transactionAutoProxy() {
		BeanNameAutoProxyCreator transactionAutoProxy = new BeanNameAutoProxyCreator();
		transactionAutoProxy.setProxyTargetClass(true);
		transactionAutoProxy.setBeanNames("*Impl");
		transactionAutoProxy.setInterceptorNames("transactionInterceptor");
		return transactionAutoProxy;
	}

}
