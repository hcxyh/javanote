package com.xyh.spring.rest;

import javax.xml.transform.Source;


/**
 * 使用:建议项目启动时,初始化一个单例的RestTemplate,保证线程安全.
 * RestTemplate这个类是用在同步调用上的，异步调用请移步 AsyncRestTemplate.
 * @author hcxyh  2018年8月13日
 *
 */
public class RestTemplateNote {
	
	/**
	 	在template内部使用是 HttpMessageConverter来负责http message与POJO之间的互相转换，
	 	默认主要类型的Mime type的Converter会被自动注册，
	 	此外你也可以通过 setMessageConverters来添加自定义converter. 
	 	来看默认的构造函数，只要对应的第三方类库存在classpath中，就会注册相应的 HttpMessageConverter
	 	
	 	public  RestTemplate() {
		this.messageConverters.add(new ByteArrayHttpMessageConverter());
		this.messageConverters.add(new StringHttpMessageConverter());
		this.messageConverters.add(new ResourceHttpMessageConverter());
		this.messageConverters.add(new SourceHttpMessageConverter<Source>());
		this.messageConverters.add(new AllEncompassingFormHttpMessageConverter());

		if (romePresent) {
			this.messageConverters.add(new AtomFeedHttpMessageConverter());
			this.messageConverters.add(new RssChannelHttpMessageConverter());
		}

		if (jackson2XmlPresent) {
			this.messageConverters.add(new MappingJackson2XmlHttpMessageConverter());
		}
		else if (jaxb2Present) {
			this.messageConverters.add(new Jaxb2RootElementHttpMessageConverter());
		}

		if (jackson2Present) {
			this.messageConverters.add(new MappingJackson2HttpMessageConverter());
		}
		else if (gsonPresent) {
			this.messageConverters.add(new GsonHttpMessageConverter());
		}
	}
	 	
	 */
	
}
