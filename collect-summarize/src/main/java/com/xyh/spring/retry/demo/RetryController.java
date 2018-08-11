package com.xyh.spring.retry.demo;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RetryController {
	
		//
		 @Retryable(maxAttempts=9,exclude = ArrayIndexOutOfBoundsException.class,value=Exception.class,backoff=@Backoff(delay = 1000))
		 @RequestMapping("/retry")   
		 public String getResult(){
			 	String name = "kelly";
		    	int i = 0 ;
		        System.out.println("尝试调用第"+i+++"次");
		    	throw new NullPointerException();
//		        name= name.split(",")[1111];//异常测试
//		        if(i!=8){
//		            name= name.split(",")[1111];//异常测试
//		        }
//		        return name+":你好！";
		    }
	
}
