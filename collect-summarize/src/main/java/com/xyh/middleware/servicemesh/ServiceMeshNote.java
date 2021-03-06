package com.xyh.middleware.servicemesh;

/**
 * 
 * @author hcxyh  2018年8月13日
 *
 */
public class ServiceMeshNote {
	/**
	 * 技术产生的原因:
	   	1.微服务的盛行
	   		1.多语言
	   		微服务理念是提倡不同业务使用最适合它的语言开发，现实情况也确实如此，尤其是AI的兴起，
	   		一般大型互联网公司存在 C/C++、Java、Golang、PHP、Python、NodeJs 等语言的项目，
	   		这就意味着每种语言都需要实现了相同功能服务框架。然而，服务框架的 SDK 通常实现都比较重，
	   		需要实现服务注册与发现、服务路由、负载均衡、服务鉴权、服务降级、服务限流、网络传输等功能，
	   		所以这块的成本不言而喻。
	   		2.产品交付
	   		在伴随着服务组件的功能升级，bug 修复过程中，业务系统需要升级依赖的服务组件包，
	   		升级中还可能存在各种版本冲突，而且灰度验证过程也可能存在 bug，业务升级版本痛苦不堪，
	   		往往一个组件包想要全覆盖升级，需要耗费相当长的时间，交付效率极其低下。随着业务的不断发展，
	   		业务的规模和我们交付的效率已经成为主要的矛盾，所以组件团队期望以更高的效率去研发基础设施，
	   		而不希望基础设施的迭代受制于这个组件的使用规模。
	 */
	
}
