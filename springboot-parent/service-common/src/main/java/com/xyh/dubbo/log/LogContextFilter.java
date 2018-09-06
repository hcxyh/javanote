/**
 * 
 */
package com.xyh.dubbo.log;

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

@Activate(group = {Constants.CONSUMER, Constants.PROVIDER})
public class LogContextFilter implements Filter {
  @Override
  public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
    boolean isProvider = RpcContext.getContext().isProviderSide();
    if (!isProvider) {
      // 将请求id转发到下一个节点
      String reqId = LogContext.get(LogContextConstants.REQ_ID);
      if (reqId != null) {
        invocation.getAttachments().put(LogContextConstants.REQ_ID, reqId);
      }
    } else {
      // 将请求id设置到当前的日志上下文
      String reqId = invocation.getAttachment(LogContextConstants.REQ_ID);
      if (reqId != null) {
        LogContext.put(LogContextConstants.REQ_ID, reqId);
      }
    }
    try {
      return invoker.invoke(invocation);
    } finally {
      if (isProvider) {
        // 服务端要清空日志上下文
        LogContext.clear();
      }
    }
  }
}