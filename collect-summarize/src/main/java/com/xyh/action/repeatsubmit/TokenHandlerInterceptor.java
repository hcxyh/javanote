package com.xyh.action.repeatsubmit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
* @ClassName: TokenHandlerInterceptor
* @author xueyh
* @date 2018年8月8日 下午8:18:41
* 
*/
public class TokenHandlerInterceptor implements HandlerInterceptor{
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
            HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {
    }
	
	@Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
            Object arg2, ModelAndView arg3) throws Exception {
        TokenHandler.generateGUID(request.getSession());
    }
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object arg2) throws Exception {
        return true;
    }
	
}