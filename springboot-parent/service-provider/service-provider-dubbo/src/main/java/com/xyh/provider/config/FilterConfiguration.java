/**
 * 
 */
package com.xyh.provider.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.WebStatFilter;


/**
 * @author hcxyh 2018年8月16日
 *
 */

@Configuration
public class FilterConfiguration {

	@Bean
	public FilterRegistrationBean druidStatFilterBean() {
		FilterRegistrationBean druidStatFilterBean = new FilterRegistrationBean(new WebStatFilter());
		List<String> urlPattern = new ArrayList<>();
		urlPattern.add("/*");
		druidStatFilterBean.setUrlPatterns(urlPattern);
		Map<String, String> initParams = new HashMap<>();
		initParams.put("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
		druidStatFilterBean.setInitParameters(initParams);
		return druidStatFilterBean;
	}

}
