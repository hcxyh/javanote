package com.xyh.spring.mvc.message;

/**
 * HttpMessageConveter
 * @author hcxyh  2018年8月13日
 *
 */
public class MessageConvertNote {
	
	
	/**
	 HttpMessageConverter主要功能在于Java对象和Json Xml等Http消息格式之前的来回转换. 
	 spring会通过某种规则（ ContentNegotation),选取对应的messageConvert.
	 1.MIMEType（不同的媒体类型对应使用不同的格式转换,spring控制）
	 	ByteArrayHttpMessageConverter
	 	StringHttpMessageConverter  -->  text/html
	 	FormHttpMessageConverter  	-->  application/x-www-form-urlencoded
	 	MappingJackson2HttpMessageConverter  --> applicatioin/json  默认classpath下的Jackson
	 	MappingJacksonHttpMessageConverter   -->  jackson(2) , fastxml(1)
	 	AtomFeedHttpMessageConverter   --> atom feeds(classpath下有 rome)
	 2.处理逻辑
	  	一个请求到SpringMvc的Controller时，
	  	Spring会利用Content-Type来判断使用哪个HttpMessageConverter来读取并转成Java Object 
	  	当请求处理完成时，如果是用 @ResponseBody标注的Controller方法，
	  	(根据http请求的中的accept来判断需要返回的数据)
	  	Spring会通过当前http request头中的Accept来判断使用哪个HttpMessageConverter来转换并回写数据到
	  	Http reponse的body中.
	  	1.从请求中读取信息
	  		@RequestBody用于 Controller方法中的参数前时，表示需要将Http请求中的body转成当前的Java实体对象，
	  		如何转，是利用上面提到的http协议头中的 Content-Type ，找到恰当的HttpMessageConverter，完成转换
	  		
	 */
}
