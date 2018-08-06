package com.xyh.spring.mvc;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @author hcxyh  2018年8月6日
 *
 */
@Controller
public class RequestContext {
	
	/*
	1.在Spring MVC中，处理请求的Controller等对象都是单例的，因此获取request对象时最需要注意的问题，
	 便是request对象是否是线程安全的：当有大量并发请求时，能否保证不同请求/线程中使用不同的request对象。 
	2.处理请求时:
	a)spring的Bean中
	在Spring的Bean中使用request对象：既包括Controller、Service、Repository等MVC的Bean，也包括了Component等普通的Spring Bean。
	b)普通的java对象中
	在非Bean中使用request对象：如普通的Java对象的方法中使用，或在类的静态方法中使用。
	3.requeust的线程安全模拟测试
	模拟客户端大量并发请求，然后在服务器判断这些请求是否使用了相同的request对象。
	判断request对象是否相同，最直观的方式是打印出request对象的地址，如果相同则说明使用了相同的对象。
	然而，在几乎所有web服务器的实现中，都使用了线程池，这样就导致先后到达的两个请求，可能由同一个线程处理：
	在前一个请求处理完成后，线程池收回该线程，并将该线程重新分配给了后面的请求.
	而在同一线程中，使用的request对象很可能是同一个（地址相同，属性不同）。因此即便是对于线程安全的方法，不同的请求使用的request对象地址也可能相同。
	为了避免这个问题，一种方法是在请求处理过程中使线程休眠几秒，这样可以让每个线程工作的时间足够长，从而避免同一个线程分配给不同的请求；
	另一种方法，是使用request的其他属性（如参数、header、body等）作为request是否线程安全的依据，
	因为即便不同的请求先后使用了同一个线程（request对象地址也相同），只要使用不同的属性分别构造了两次request对象，
	那么request对象的使用就是线程安全的。本文使用第二种方法进行测试
	 */
	
	//客户端发起请求
	public static class Test{
		public static void main(String[] args) throws Exception {
	        String prefix = UUID.randomUUID().toString().replaceAll("-", "") + "::";
	        for (int i = 0; i < 1000; i++) {
	            final String value = prefix + i;
	            new Thread() {
	                @Override
	                public void run() {
	                    try {
	                        CloseableHttpClient httpClient = HttpClients.createDefault();
	                        HttpGet httpGet = new HttpGet("http://localhost:8080/test?key=" + value);
	                        httpClient.execute(httpGet);
	                        httpClient.close();
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                }
	            }.start();
	        }
	    }
	}
	
	
	// 存储已有参数，用于判断参数是否重复，从而判断线程是否安全
    public static Set<String> set = new ConcurrentSkipListSet<>();
    @RequestMapping("/test")  //后端接收判断request是否重复
    public  void test(HttpServletRequest request) throws InterruptedException {
        // …………………………通过某种方式获得了request对象………………………………

    	/**
    	1.直接使用参数HttpServletRequest
    	 在Controller方法开始处理请求时，
    	Spring会将request对象赋值到方法参数中。除了request对象，可以通过这种方法获取的参数还有很多。
    	线程安全
		分析：此时request对象是方法参数，相当于局部变量，毫无疑问是线程安全的。
		这种方法的主要缺点是request对象写起来冗余太多，主要体现在两点：
		1)      如果多个controller方法中都需要request对象，那么在每个方法中都需要添加一遍request参数
		2)      request对象的获取只能从controller开始，如果使用request对象的地方在函数调用层级比较深的地方，那么整个调用链上的所有方法都需要添加request参数
		实际上，在整个请求处理的过程中，request对象是贯穿始终的；也就是说，除了定时器等特殊情况，
		request对象相当于线程内部的一个全局变量。而该方法，相当于将这个全局变量，传来传去。 
    	*/
    	 // 模拟程序执行了一段时间
        Thread.sleep(1000);
        
 
        // 判断线程安全
        String value = request.getParameter("key");
        if (set.contains(value)) {  //重复出现
            System.out.println(value + "\t重复出现，request并发不安全！");
        } else {  //request变量线程安全
            System.out.println(value);
            set.add(value);
        }
         
        // 模拟程序执行了一段时间
        Thread.sleep(1000);
    }
    
    /**
    2.使用autowired自动注入
            线程安全性-->测试结果：线程安全
	分析：在Spring中，Controller的scope是singleton(单例)，也就是说在整个web系统中，
	只有一个TestController；但是其中注入的request却是线程安全的，原因在于：
	使用这种方式，当Bean（本例的TestController）初始化时，Spring并没有注入一个request对象，
	而是注入了一个代理（proxy）；当Bean中需要使用request对象时，通过该代理获取request对象。
	代理的实现参见AutowireUtils的内部类ObjectFactoryDelegatingInvocationHandler。
	也就是说，当我们调用request的方法method时，实际上是调用了由objectFactory.getObject()生成的对象的method方法；
	objectFactory.getObject()生成的对象才是真正的request对象。
	发现objectFactory的类型为WebApplicationContextUtils的内部类RequestObjectFactory；
	要获得request对象需要先调用currentRequestAttributes()方法获得RequestAttributes对象
	优缺点
	该方法的主要优点：
	1)      注入不局限于Controller中：在方法1中，只能在Controller中加入request参数。而对于方法2，
	不仅可以在Controller中注入，还可以在任何Bean中注入，包括Service、Repository及普通的Bean。
	2)      注入的对象不限于request：除了注入request对象，该方法还可以注入其他scope为request或session的对象，如response对象、session对象等；并保证线程安全。
	3)      减少代码冗余：只需要在需要request对象的Bean中注入request对象，便可以在该Bean的各个方法中使用，与方法1相比大大减少了代码冗余。
	但是，该方法也会存在代码冗余。考虑这样的场景：web系统中有很多controller，每个controller中都会使用request对象（这种场景实际上非常频繁），
	这时就需要写很多次注入request的代码；如果还需要注入response，代码就更繁琐了。下面说明自动注入方法的改进方法，并分析其线程安全性及优缺点。
		
    */
   @Autowired
   private HttpServletRequest request02; //自动注入request
   
   /**
    * 基类中自动注入
    1.BaseController 中使用  @Autowired private HttpServletRequest request03;
            完成自动注入
    */
   
   /**
    4.手动调用
    	该方法与方法2（自动注入）类似，只不过方法2中通过自动注入实现，本方法通过手动方法调用实现。因此本方法也是线程安全的。
    	优点：可以在非Bean中直接获取。缺点：如果使用的地方较多，代码非常繁琐；因此可以与其他方法配合使用。
    */
   HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
   
   
   /**
    	方法5：@ModelAttribute方法
    	测试结果：线程不安全
		分析：@ModelAttribute注解用在Controller中修饰方法时，其作用是Controller中的每个@RequestMapping方法执行前，
		该方法都会执行。因此在本例中，bindRequest()的作用是在test()执行前为request对象赋值。
		虽然bindRequest()中的参数request本身是线程安全的，但由于TestController是单例的，
		request作为TestController的一个域，无法保证线程安全。
    */
   private HttpServletRequest request05;
   @ModelAttribute
   public void bindRequest(HttpServletRequest request) {
       this.request = request;
   }
}
