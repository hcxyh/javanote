package com.xyh.spring.transaction;

/**
* @ClassName: TransactionAnalyse
* @Description: TODO 事务答疑
* @author xueyh
* @date 2017年12月20日 下午3:03:52
*/
public class TransactionAnalyse {
	
	/**
	 * 1.关于spring中多线程的问题
	 * spring中通过单实例来解决多线程问题,
	 * 事务管理器通过ThreadLocal来保存与线程相关的connection,
	 * 在结合IOC与aop实现事务的高级功能.
	 * 2.web本身就是多线程的,会为每一个http请求创建一个独立的线程.
	 * 实际上,web服务器大多都是采用共享线程池的方式.所以由此请求所关联的spring中的bean也是
	 * 运行在多线程环境下的.
	 * ------------------------------------------
	 * spring中的单实例bean,必须设计为无状态的.
	 * 即就是一个bean里面不能有  状态化的成员变量.
	 * 但是dao持有状态化的connection,并且被service所持有.
	 * 因而spring竭尽所能的要将bean包装成无状态的.
	 * 就比如transactionSynchrozedManager中将connection与threadLical绑定,
	 * 从而完成bean对象的无状态.(与线程无关)
	 * -------------------------------------------
	 * 
	 */
	
	
	public void startNewThreadDoTask(){
		/**
		 * TODO 在中关村银行中,有那种在biz中调用ThreadPoolExecutor,
		 * 新增一个线程,通过反射区调用biz方法.-->结果都是新开一个事务.
		 * 对此情况进行分析:
		 * 被调用者的方法是否需要事务.
		 * 1.调用者有事务 ,并且依赖于被调用者的事务.
		 * ---------------------------------------------------
		 * 
		 * 在相同线程中工作的不同事务方法之间互相嵌套调用,工作在相同的事务中.
		 * 如果这些互相之间的嵌套调用工作在不同的线程中,则这些事务方法都在各自的独立事务中.
		 * 
		 * ---------------------------------------------------
		 */
	}
	
	public void notUseAop(){
		/**
		 * TODO
		 * 哪些方法不能使用aop.
		 * 1.procted,private私有方法.---->这是针对接口的
		 * 2.并且不能使用static修饰.---->仅能使用 public final 进行修饰
		 * -------------------------------------------------
		 * 讲真的,只用 public 修饰就ok.
		 */
	}
	
	
	
}
