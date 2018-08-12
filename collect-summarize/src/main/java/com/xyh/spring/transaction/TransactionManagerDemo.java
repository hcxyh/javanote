package com.xyh.spring.transaction;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
* @ClassName: TransactionManagerDemo
* @author xueyh
* @date 2017年12月19日 下午8:12:18
*/
public class TransactionManagerDemo {
	
	/**
	 * spring事务管理三大组件
	 * 1.TransactionDefined (隔离级别--对应数据库的隔离级别,超时时间,是否读写事务,传播属性)
	 * 可以通过xml或者编程实现.
	 * 2.PlatformTransactionManager(根据事务status来创建事务)
	 * 3.TransactionStatus
	 * 我们经常使用的事务管理器一般都是 DatasourceTransaction,基于数据源的事务管理器.
	 * 
	 * TransactionSynchrizedManager为不同的事务线程提供了独立的资源副本.
	 * 
	 */
	@Autowired
	private DataSource dataSource;
	
	public void getConnectionByTransaction(){
		/**
		 * p374页,线程绑定,当需要脱离模板类时,操作底层api的时候.
		 * SqlsessionTemplate等等的模板类里面,间接访问TransactionSynchrizedManager
		 * 这个就是被称之为  线程上下文(
		 * 里面持有了  connection,事务名字,事务隔离级别,事务的传播属性.
		 * )的东西.
		 * 中的线程绑定资源.如果dao使用template进行持久化的话,
		 * 就可以将Dao配置为singleton.
		 */
		Connection conn = DataSourceUtils.getConnection(dataSource);
		/**
		 * 在事务环境下,通过DatasourceUtils获取数据连接,
		 * 获取的就是  事务上下文所绑定的那个链接Connection数据库.
		 * ---------------------------------------------------
		 * 但在无事务的环境下,使用DataSourceUtils.getConnection()
		 * 极有可能造成数据库连接泄露.坚决不推荐
		 * ---------------------------------------------------
		 * 
		 */
		
		
		
		
		//事务的对象定义
		new TransactionDefinition() {
			@Override
			public boolean isReadOnly() {//是否只读事务
				return false;
			}
			@Override
			public int getTimeout() {//超时时间
				return 0;
			}
			@Override
			public int getPropagationBehavior() {//传播属性
				return 0;
			}
			@Override
			public String getName() {//事务名称
				return null;
			}
			@Override
			public int getIsolationLevel() { //隔离属性
				return 0;
			}
		};
	}
	
	/**
	 * TODO 
	 * 使用transactionTemplate是线程安全的,编程式事务
	 */
	private TransactionTemplate transactionTemplate;
	public void transactionTemplateTest(){
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			//调用不同的匿名内部类来实现是否存在ResultSet的调用.
			//重载的还有带有返回值的方法
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				//需要执行的业务代码
			}
		});
	}
	
	
	public void transactionSynchronizationManager(){
		new TransactionSynchronizationManager() {
			/**
			 * p394页
			 * TODO 仔细TransactionSynchronizationManager的方法
			 * 里面的static 资源变量保存了包括
			 * 1.connection或者resource 等资源
			 * 2.currentTransactionName每个事务对应的名称
			 * 3.currentTransactionReadOnly,每个事务对应的只读状态
			 * 4.currentTransactionIsolationLevel,每个事务的隔离级别
			 * 5.actualTransactionActive 每个事务的激活状态
			 * 千万注意,传播属性不属于该Manager管理
			 */
		};
	}
	
	
	public void transactionStatus(){
		new TransactionStatus() {
			//扩展子savingPointManager接口
			@Override
			public void rollbackToSavepoint(Object savepoint) throws TransactionException {
				//回滚到特定的保存点上
			}
			
			@Override
			public void releaseSavepoint(Object savepoint) throws TransactionException {
				//释放一个保存点,若事务提交,则释放所有的保存点
			}
			
			@Override
			public Object createSavepoint() throws TransactionException {
				//创建一个保存点对象,以便用于后续回滚
				return null;
			}
			
			@Override
			public void setRollbackOnly() {
				//将当前事务修改为RollbackOnly
			}
			
			@Override
			public boolean isRollbackOnly() {
				//判断是否是 RollbackOnly事务
				return false;
			}
			
			@Override
			public boolean isNewTransaction() {
				//判断当前事务是否是一个新的事务,不存在事务,返回false
				return false;
			}
			
			@Override
			public boolean isCompleted() {
				//判断当前事务是否  结束或者提交.回滚
				return false;
			}
			
			@Override
			public boolean hasSavepoint() {
				//判断当前事务是否有保存点
				return false;
			}
			
			@Override
			public void flush() {
				//
			}
		};
	}

}
