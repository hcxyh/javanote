package com.xyh.microserver.restful;

/**
 * 
 * @author hcxyh  2018年8月13日
 *
 */
public class RestFulApiNote {

	/**
	 1.接口设计 RESTful
	 	1.Protocol
	 		http,https
	 	2.API Root URL
	 		API 的根入口点应尽可能保持足够简单
	 	3.Versioning
	 		1.在 URL 中嵌入版本编号
	 			api.example.com/v1/*
	 		2.通过媒体类型来指定版本信息
	 			Accept: application/vnd.example.com.v1+json
	 	4.Endpoints
	 		端点就是指向特定资源或资源集合的 URL。在端点的设计中
	 		URL 的命名 必须 全部小写
				1.URL 中资源（resource）的命名 必须 是名词，并且 必须 是复数形式
				2.必须 优先使用 Restful 类型的 URL
				3.URL 必须 是易读的
				4.URL 一定不可 暴露服务器架构
				5.至于 URL 是否必须使用连字符（-） 或下划线（_），不做硬性规定，但 必须 根据团队情况统一一种风格。
	 		{
	 			来看一个反例
				https://api.example.com/getUserInfo?userid=1
				https://api.example.com/getusers
				https://api.example.com/sv/u
				https://api.example.com/cgi-bin/users/get_user.php?userid=1
				再来看一个正列
				https://api.example.com/zoos
				https://api.example.com/animals
				https://api.example.com/zoos/{zoo}/animals
				https://api.example.com/animal_types
				https://api.example.com/employees
	 		}
	 		
	 	5.HTTP 动词
	 		GET（SELECT）：从服务器取出资源（一项或多项）。
			POST（CREATE）：在服务器新建一个资源。
			PUT（UPDATE）：在服务器更新资源（客户端提供改变后的完整资源）。
			PATCH（UPDATE）：在服务器更新资源（客户端提供改变的属性）。
			DELETE（DELETE）：从服务器删除资源。
	 	6.Filtering
	 		如果记录数量很多，服务器不可能都将它们返回给用户。API 应该 提供参数，过滤返回结果。下面是一些常见的参数。
			
			?limit=10：指定返回记录的数量
			?offset=10：指定返回记录的开始位置。
			?page=2&per_page=100：指定第几页，以及每页的记录数。
			?sortby=name&order=asc：指定返回结果按照哪个属性排序，以及排序顺序。
			?animal_type_id=1：指定筛选条件
			所有 URL 参数 必须 是全小写，必须 使用下划线类型的参数形式。
			分页参数 必须 固定为 page、per_page
		7.Authentication
			
		8.Response
	 */
	
}
