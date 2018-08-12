package com.xyh.spring.transaction;

import java.sql.SQLException;

import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

/**
* @ClassName: DataSourceProxy
* @author xueyh
* @date 2017年12月20日 下午4:16:55
*/
public class DataSourceProxy {
	
	/**
	 * TransactionAwareDataSourceProxy 对数据源进行代理
	 * <bean id="dataSource"
	 * 		class = "TransactionAwareDataSourceProxy"
	 *  里面依赖具体的Datasource; />
	 */
	public void transactionAwareDataSourceProxyTest() throws SQLException{
		/**
		 * 这个类对DataSource进行了代理,便具有了对事务上下文的感知能力.
		 * 与DataSourceUtils获取connection是相同的效果.
		 */
		new TransactionAwareDataSourceProxy().getConnection();
	}
	
	
}
