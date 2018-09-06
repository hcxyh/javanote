/**
 * 
 */
package com.xyh.dubbo.log;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.common.utils.ReflectUtils;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.dubbo.rpc.service.GenericService;

/**
 * @author hcxyh  2018年8月16日
 *
 */
@Activate(group = Constants.PROVIDER)
public class ICoreExceptionFilter  { //implements Filter

//  private static final Logger LOGGER = LoggerFactory.getLogger(ICoreExceptionFilter.class);
//
//  private static final boolean fillStackTrace = Boolean.valueOf(System.getProperty("icore.exception.fill", "true"));
//
//  public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
//    try {
//      Result result = invoker.invoke(invocation);
//      if (result.hasException() && GenericService.class != invoker.getInterface()) {
//        try {
//          Throwable exception = result.getException();
//
//          // 如果是checked异常，直接抛出
//          if (!(exception instanceof RuntimeException) && (exception instanceof Exception)) {
//            return result;
//          }
//          // 在方法签名上有声明，直接抛出
//          try {
//            Method method = invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
//            Class<?>[] exceptionClasses = method.getExceptionTypes();
//            for (Class<?> exceptionClass : exceptionClasses) {
//              if (exception.getClass().equals(exceptionClass)) {
//                if ((exception instanceof BizException) && !fillStackTrace) {
//                  BizException bizException = (BizException) exception;
//                  return new RpcResult(new RpcBizException(bizException.getErrCode(), bizException.getErrMsg(),
//                      null, false, false));
//                }
//                return result;
//              }
//            }
//          } catch (NoSuchMethodException e) {
//            return result;
//          }
//
//          // 未在方法签名上定义的异常，在服务器端打印ERROR日志
//          LOGGER.error("Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost()
//              + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
//              + ", exception: " + exception.getClass().getName() + ": " + exception.getMessage(), exception);
//
//          // 异常类和接口类在同一jar包里，直接抛出
//          String serviceFile = ReflectUtils.getCodeBase(invoker.getInterface());
//          String exceptionFile = ReflectUtils.getCodeBase(exception.getClass());
//          if (serviceFile == null || exceptionFile == null || serviceFile.equals(exceptionFile)) {
//            if ((exception instanceof BizException) && !fillStackTrace) {
//              BizException bizException = (BizException) exception;
//              return new RpcResult(new RpcBizException(bizException.getErrCode(), bizException.getErrMsg(),
//                  null, false, false));
//            }
//            return result;
//          }
//          // 是JDK自带的异常，直接抛出
//          String className = exception.getClass().getName();
//          if (className.startsWith("java.") || className.startsWith("javax.")) {
//            return result;
//          }
//          // 是Dubbo本身的异常，直接抛出
//          if (exception instanceof RpcException) {
//            return result;
//          }
//
//          if (exception instanceof BizException) {
//            // 如果是biz异常，那么直接转换成消除栈的本地异常
//            BizException bizException = (BizException) exception;
//            RpcBizException rpcBizException;
//            if (fillStackTrace) {
//              rpcBizException = new RpcBizException(bizException.getErrCode(), bizException.getErrMsg(), StringUtils.toString(bizException));
//            } else {
//              rpcBizException = new RpcBizException(bizException.getErrCode(), bizException.getErrMsg(), null, false, false);
//            }
//            return new RpcResult(rpcBizException);
//          }
//
//          // 否则，包装成RuntimeException抛给客户端
//          return new RpcResult(new RuntimeException(StringUtils.toString(exception)));
//        } catch (Throwable e) {
//          LOGGER.warn("Fail to ExceptionFilter when called by " + RpcContext.getContext().getRemoteHost()
//              + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
//              + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
//          return result;
//        }
//      }
//      return result;
//    } catch (RuntimeException e) {
//      LOGGER.error("Got unchecked and undeclared exception which called by " + RpcContext.getContext().getRemoteHost()
//          + ". service: " + invoker.getInterface().getName() + ", method: " + invocation.getMethodName()
//          + ", exception: " + e.getClass().getName() + ": " + e.getMessage(), e);
//      throw e;
//    }
//  }

}
