package com.xyh.action.repeatsubmit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Token校验
 */
public class TokenValidInterceptor implements HandlerInterceptor{
	
	@Override
	 public void afterCompletion(HttpServletRequest request,
	            HttpServletResponse response, Object arg2, Exception arg3)
	            throws Exception {
	    }
	@Override
	    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
	            Object arg2, ModelAndView arg3) throws Exception {
	         
	    }
	@Override
	    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
	            Object arg2) throws Exception {
	        if(!TokenHandler.validToken(request)){
	        	//token校验不通过
	            //response.sendRedirect(Constants.DEFAULT_TOKEN_MSG_JSP);
	            return false;
	        }
	    return true;
	    }
	
}