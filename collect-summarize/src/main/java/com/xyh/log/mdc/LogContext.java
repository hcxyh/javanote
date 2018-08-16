package com.xyh.log.mdc;

import org.slf4j.MDC;

/**
* @ClassName: LogContext
* @author xueyh
* 
* 1.在web端的springMvc中使用handlerInterceptorAdapter对LogContext进行赋值.
* 2.在dubbo或者分布式多节点调用时,将请求通过RPCcontext发到下一个节点.
* 3.当服务端最终的服务提供者调用结束后,清理服务端.clear.
* 4.web端清理LogContext.
* 	
* 	TODO 没有完成
* 	1.日志按天滚动分隔
*	2.info和error输出到不同的文件
*	日志文件改为json格式,通过每天的统计，
*	经由Echarts来做视图化解析.
* 
*   MDC ,NDC
* 
*/
public class LogContext {
	
	private LogContext(){
	}
	
	public static String get(String key){
		return MDC.get(key);
	}
	
	public static void put(String key,String value){
		MDC.put(key, value);
	}
	
	public static void remove(String key){
		MDC.remove(key);
	}
	
	public static void clear(){
		MDC.clear();
	}
	
}
