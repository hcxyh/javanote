package com.xyh.spring.mvc;

/**
 * SpringMvc与ajax数据交互
 * @author hcxyh  2018年8月6日
 *
 */

public class DataBind2Ajax {
	
	/**
	 context-type:标明客户端传输的媒体类型信息,即是Internet Media Type，互联网媒体类型；也叫做MIME类型.
	 ajax中默认的格式为：contentType一般为默认的application/x-www-form-urlencoded
	 
	ajax中的参数:
	1.data
	data 
	参数类型：Object,String 
	说明：发送到服务器的数据。若data数据类型为JavaScript对象或数组，
	Jquery在提交之前自动调用JQuery.param()方法把要发送的数据编码成为
	”application/x-www-form- urlencoded”格式的数据（即 name=value&name1=value1），
	此时参数为Object并且必须为 Key/Value 格式；如果为数组，jQuery 将自动为不同值对应同一个名称。
	如 {foo:[“bar1”, “bar2”]} 转换为 ‘&foo=bar1&foo=bar2’； 
　　	若data数据类型为String类型，则直接默认该数据已经按照
	”application/x-www-form-urlencoded”格式编码完成，不再转换。
	 
	 使用application/json,JSON.stringify(obj) 格式化为json字符串后.
	 服务端接受使用@RequestBody.
	data:JSON.stringify(dataObj) ,//传递参数必须是Json字符串
    contentType: "application/json; charset=utf-8",//必须声明contentType为application/json,否则后台使用@RequestBody标注的话无法解析参数
    dataType: "json",  //声明客户端期望接受的数据格式
	 */
	
	
}
