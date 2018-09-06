package com.xyh.dubbo.exception;


/**
 * 
 * @author hcxyh  2018年8月16日
 *
 */
public class MyException extends RuntimeException{ 
	
	/**
	 * 此处暂时这么处理,  20180816
	 * 后期根据业务类型,具体进行分类.
	 */

	private static final long serialVersionUID = 1756368854408608348L;
	  private String errCode;
	  private String errMsg;

	  public MyException() {
	  }


	  public MyException(String errCode, String errMsg) {
	    super(errMsg);
	    this.errCode = errCode;
	    this.errMsg = errMsg;
	  }

	  public MyException(String errCode, String errMsg, Throwable cause) {
	    super(errMsg + " : " + cause.getMessage(), cause);
	    this.errCode = errCode;
	    this.errMsg = errMsg;
	  }

	  /**
	   * 该类主要是为了在类序列化的过程中存储转换后的栈消息
	   *
	   * @param errCode 错误码
	   * @param errMsg  错误描述
	   * @param message 错误消息
	   */
	  public MyException(String errCode, String errMsg, String message) {
	    super(message);
	    this.errCode = errCode;
	    this.errMsg = errMsg;
	  }

	  public String getErrCode() {
	    return errCode;
	  }

	  public void setErrCode(String errCode) {
	    this.errCode = errCode;
	  }

	  public String getErrMsg() {
	    return errMsg;
	  }

	  public void setErrMsg(String errMsg) {
	    this.errMsg = errMsg;
	  }

	  public MyException(String errCode, String errMsg, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	    super(errMsg, cause, enableSuppression, writableStackTrace);
	    this.errCode = errCode;
	    this.errMsg = errMsg;
	  }
	
}
