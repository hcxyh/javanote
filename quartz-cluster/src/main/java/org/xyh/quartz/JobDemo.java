/**   
* @Title: JobDemo.java
* @Package net.aimeizi.quartz.event
* @Description: TODO(用一句话描述该文件做什么)
* @author A18ccms A18ccms_gmail_com   
* @date 2018年4月16日 上午11:31:23
* @version V1.0   
*/
package org.xyh.quartz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dexcoder.dal.JdbcDao;


/**
* @ClassName: JobDemo
* @author xueyh
* @date 2018年4月16日 上午11:31:23
*/

@Service("jobdemo")
public class JobDemo {
	
	 /** 通用dao */
    @Autowired
    private JdbcDao jdbcDao;	
	
	 public String jobTest(){
	    	System.out.println("job-start");
//	    	String name = "xyh";
//	    	JobTest jobTest = new JobTest();
//	    	jobTest.setName(name);
//	    	Long out = jdbcDao.insert(jobTest);
	    	System.out.println("hello world!");
	    	System.out.println("-------------");
	    	System.out.println("job-end");
	    	return "";
	    }
	
}
