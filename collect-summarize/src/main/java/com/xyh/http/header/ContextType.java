package com.xyh.http.header;

/**
 * 
 * @author hcxyh  2018年8月6日
 *
	HTTP响应头和请求头信息对照表
	HTTP请求头提供了关于请求，响应或者其他的发送实体的信息。HTTP的头信息包括通用头、请求头、响应头和实体头四个部分。每个头域由一个域名，冒号（:）和域值三部分组成。
	
	通用头标：即可用于请求，也可用于响应，是作为一个整体而不是特定资源与事务相关联。
	请求头标：允许客户端传递关于自身的信息和希望的响应形式。
	响应头标：服务器和于传递自身信息的响应。
	实体头标：定义被传送资源的信息。即可用于请求，也可用于响应。
 *
 */
public class ContextType {
	
	/**
	 HTTP Request Header 请求头
	
	
	Accept : 浏览器（或者其他基于HTTP的客户端程序）可以接收的内容类型（Content-types）,例如 Accept: text/plain

		Accept-Charset：浏览器能识别的字符集，例如 Accept-Charset: utf-8
		
		Accept-Encoding：浏览器可以处理的编码方式，注意这里的编码方式有别于字符集，这里的编码方式通常指gzip,deflate等。例如 Accept-Encoding: gzip, deflate
		Accept-Language：浏览器接收的语言，其实也就是用户在什么语言地区，例如简体中文的就是 Accept-Language: zh-CN
		
		Accept-Datetime：设置接受的版本时间，例如Accept-Datetime: Thu, 31 May 2007 20:35:00 GMT
		Authorization：在HTTP中，服务器可以对一些资源进行认证保护，如果你要访问这些资源，就要提供用户名和密码，这个用户名和密码就是在Authorization头中附带的，格式是“username:password”字符串的base64编码，例如：Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ中，basic指使用basic认证方式，　QWxhZGRpbjpvcGVuIHNlc2FtZQ使用base64解码就是Aladdin:open sesame
		
		Cache-Control：这个指令在request和response中都有，用来指示缓存系统（服务器上的，或者浏览器上的）应该怎样处理缓存，因为这个头域比较重要，特别是希望使用缓存改善性能的时候，内容也较多，设置请求响应链上所有的缓存机制必须遵守的指令，eg：Cache-Control: no-cache
		
		Connection：告诉服务器这个user agent（通常就是浏览器）想要使用怎样的连接方式。值有keep-alive和close。http1.1默认是keep-alive。keep-alive就是浏览器和服务器　的通信连接会被持续保存，不会马上关闭，而close就会在response后马上关闭。但这里要注意一点，我们说HTTP是无状态的，跟这个是否keep-alive没有关系，不要认为keep-alive是对HTTP无状态的特性的改进。
		设置当前连接和hop-by-hop协议请求字段列表的控制选项
		
		Connection: keep-alive
		Connection: Upgrade
		
		Cookie：浏览器向服务器发送请求时发送cookie，或者服务器向浏览器附加cookie，就是将cookie附近在这里的。例如：Cookie:user=admin
		设置服务器使用Set-Cookie发送的http cookie
		Cookie: $Version=1; Skin=new;
		
		**Content-Length：**一个请求的请求体的内存长度，单位为字节(byte)。请求体是指在HTTP头结束后，两个CR-LF字符组之后的内容，常见的有POST提交的表单数据，这个Content-Length并不包含请求行和HTTP头的数据长度。
		设置请求体的字节长度
		Content-Length: 348
		
		Content-MD5：使用base64进行了编码的请求体的MD5校验和。例如：Content-MD5: Q2hlY2sgSW50ZWdyaXR5IQ==
		基于MD5算法对请求体内容进行Base64二进制编码
		
		Content-Type：请求体中的内容的mime类型。通常只会用在POST和PUT方法的请求中。例如：Content-Type: application/x-www-form-urlencoded
		设置请求体的MIME类型（适用POST和PUT请求）
		
		**Date：**发送请求时的GMT时间。例如：Date: Tue, 15 Nov 1994 08:12:31 GMT
		设置消息发送的日期和时间
		
		Expect：标识客户端需要的特殊浏览器行为
		Expect: 100-continue
		
		From：发送这个请求的用户的email地址。例如：From: user@example.com
		设置发送请求的用户的email地址
		
		Host：被服务器的域名或IP地址，如果不是通用端口，还包含该端口号，例如：Host: www.some.com:182
		设置服务器域名和TCP端口号，如果使用的是服务请求标准端口号，端口号可以省略
		Host: en.wikipedia.org:8080
		Host: en.wikipedia.org
		
		If-Match :通常用在使用PUT方法对服务器资源进行更新的请求中，意思就是，询问服务器，现在正在请求的资源的tag和这个If-Match的tag相不相同，如果相同，则证明服务器上的这个资源还是旧的，现在可以被更新，如果不相同，则证明该资源被更新过，现在就不用再更新了（否则有可能覆盖掉其他人所做的更改）。
		设置客户端的ETag,当时客户端ETag和服务器生成的ETag一致才执行，适用于更新自从上次更新之后没有改变的资源
		
		If-Modified-Since：询问服务器现在正在请求的资源在某个时间以来有没有被修改过，如果没有，服务器则返回304状态来告诉浏览器使用浏览器自己本地的缓存，如果有修改过，则返回200，并发送新的资源（当然如果资源不存在，则返回404。）
		设置更新时间，从更新时间到服务端接受请求这段时间内如果资源没有改变，允许服务端返回304 Not Modified
		
		If-None-Match：和If-Modified-Since用意差不多，不过不是根据时间来确定，而是根据一个叫ETag的东西来确定。关于etag我想在下一篇博客介绍一下。
		设置客户端ETag，如果和服务端接受请求生成的ETage相同，允许服务端返回304 Not Modified
		
		If-Range：告诉服务器如果这个资源没有更改过(根据If-Range后面给出的Etag判断)，就发送这个资源中在浏览器缺少了的某些部分给浏览器，如果该资源以及被修改过，则将整个资源重新发送一份给浏览器。
		
		If-Unmodified-Since：询问服务器现在正在请求的资源在某个时刻以来是否没有被修改过。
		
		Max-Forwards：限制请求信息在代理服务器或网关中向前传递的次数。
		Pragma：好像只有一个值，就是:no-cache。Pragma:no-cache 与cache-control:no-cache相同，只不过cache-control:no-cache是http1.1专门指定的，而Pragma:no-cache可以在http1.0和1.1中使用
		
		Proxy-Authorization：连接到某个代理时使用的身份认证信息，跟Authorization头差不多。例如：Proxy-Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
		为连接代理授权认证信息
		
		Range：在HTTP头中，”Range”字眼都表示“资源的byte形式数据的顺序排列，并且取其某一段数据”的意思。Range头就是表示请求资源的从某个数值到某个数值间的数据，例如：Range: bytes=500-999 就是表示请求资源从500到999byte的数据。数据的分段下载和多线程下载就是利用这个实现的。
		
		Referer：指当前请求的URL是在什么地址引用的。例如在www.a.com/index.html页面中点击一个指向www.b.com的超链接，那么，这个www.b.com的请求中的Referer就是www.a.com/index.html。通常我们见到的图片防盗链就是用这个实现的。
		
		Upgrade：请求服务器更新至另外一个协议，例如：Upgrade: HTTP/2.0, SHTTP/1.3, IRC/6.9, RTA/x11
		
		User-Agent：通常就是用户的浏览器相关信息。例如：User-Agent: Mozilla/5.0 (X11; Linux x86_64; rv:12.0) Gecko/20100101 Firefox/12.0
		
		Via：用来记录一个请求经过了哪些代理或网关才被送到目标服务器上。例如一个请求从浏览器出发(假设使用http/1.0)，发送给名为 SomeProxy的内部代理，然后被转发至www.somenet.com的公共代理（使用http/1.1），最后被转发至目标服务器www.someweb.com，那么在someweb.com中收到的via 头应该是：via:1.0 someProxy 1.1 www.someweb.com(apache 1.1)
		
		Warning：记录一些警告信息。
		通用但非标准的HTTP头（通常，非标准的头域都是用“X-”开头，例如”x-powered-by”）：
		
		X-Requested-With：主要是用来识别ajax请求，很多javascript框架会发送这个头域（值为XMLHttpRequest）
		
		DNT : DO NOT TRACK的缩写，要求服务器程序不要跟踪记录用户信息。DNT: 1 (开启DNT) DNT: 0 (关闭DNT)火狐，safari,IE9都支持这个头域，并且于2011年3月7日被提交至IETF组织实现标准化
		
		X-Forwarded-For : 记录一个请求从客户端出发到目标服务器过程中经历的代理，或者负载平衡设备的IP。
		
		X-Forwarded-Proto：记录一个请求一个请求最初从浏览器发出时候，是使用什么协议。因为有可能当一个请求最初和反向代理通信时，是使用https，但反向代理和服务器通信时改变成http协议，这个时候，X-Forwarded-Proto的值应该是https
		
		Front-End-Https：微软使用与其负载平衡的一个头域。
			
	 *
	 *
	 */
	
	
	
}
