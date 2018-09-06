/**
 * 
 */
package com.xyh.dubbo.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;

/**
 * @author hcxyh  2018年8月16日
 *
 */
@Activate(group = {Constants.PROVIDER, Constants.CONSUMER})
public class CallLogFilter implements Filter {
	
  private static final Logger LOGGER = LoggerFactory.getLogger(CallLogFilter.class);

  @Override
  public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
    String ifc = invoker.getInterface().getName();
    String methodName = invocation.getMethodName();
    String host = invoker.getUrl().getHost();
    int port = invoker.getUrl().getPort();
    boolean isConsumerSide = RpcContext.getContext().isConsumerSide();
    String serviceTag = ifc + "." + methodName;
    if (isConsumerSide) {
      LOGGER.info("Call remote service => " + host + ":" + port + " [" + serviceTag + "]");
    } else {
      LOGGER.info("Begin service [" + serviceTag + "]");
    }
    try {
      return invoker.invoke(invocation);
    } finally {
      if (!isConsumerSide) {
        LOGGER.info("End service [" + serviceTag + "]");
      }
    }
  }
}
