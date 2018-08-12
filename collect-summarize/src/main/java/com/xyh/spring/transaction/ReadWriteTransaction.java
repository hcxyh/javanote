package com.xyh.spring.transaction;

import org.springframework.transaction.annotation.Transactional;

/**
* @ClassName: ReadWriteTransaction
* @Description: TODO 读写事务
* @author xueyh
* @date 2017年12月20日 下午1:46:58
*/
public class ReadWriteTransaction {
	
	/**
	 * 事务的声明配置
	 * 1.原生的transactionProxyFactoryBean
	 * 2.tx
	 * 3.aop-interceptor 这部分具体在 p400页左右
	 */
	
	@Transactional
	public void annonationTransaction(){
		/**
		 * <tx:annonation-driver transaction-manager="dataSourceManager"
		 * 	order 可以控制事务切面在 目标连接点的切入顺序, 与aop先后顺序定义
		 *  />
		 * proxy-targrt-classes 为true是创建子类来生成代理类
		 * false为基于接口的代理 --jdk
		 * ----------------------------------------------
		 * Transaction里面的属性
		 * 传播属性,隔离属性,超时时间,读写事务,回滚机制(对于哪些异常执行回滚)
		 * ----------------------------------------------
		 * 可以在一个应用里面使用 多个事务管理器.abstractTrasactional
		 * 不同的事务管理器使用不同的DataSource数据源,
		 * 并且给每一个事务管理器配置唯一Id和name
		 */
	}
	
}
