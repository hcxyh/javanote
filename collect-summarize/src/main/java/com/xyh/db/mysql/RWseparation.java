package com.xyh.db.mysql;

/**
 * Mysql读写分离,基于mysql-proxy
 * 含有一个读写分离的lua文件，这也是我们使用mysql-proxy实现读写分离必用的文件，
 * 它需要lua解析器进行解析。因此我们还需要安装一个lua解析器。
 * @author hcxyh  2018年8月13日
 *
 */
public class RWseparation {
	
	/**
	 1.基础环境
	 	三台linux虚拟主机
			Linux版本CentOS6.6、MySQL 5.5
			mysql-proxy-0.8.5
			lua-5.1.4
		ip：192.168.95.11（写）、192.168.95.12（读）、192.168.95.13（mysql-proxy）
	2.配置主从复制
		详细可以参考：mysql主从复制与主主复制
		http://www.cnblogs.com/phpstudy2015-6/p/6485819.html#_label2
		粗略介绍一下数据库的主从复制的配置：
			1.创建允许主服务登录的从服务mysql用户
				在192.168.95.11中创建一个192.168.95.12主机中可以登录的MySQL用户
				用户：mysql12
				密码：mysql12
				mysql>GRANT REPLICATION SLAVE ON *.* TO ‘mysql12’@’192.168.95.12’ IDENTIFIED BY ‘mysql12’;
				mysql>FLUSH PRIVILEGES;
			2.查看服务二进制文件位置
				查看192.168.95.11MySQL服务器二进制文件名与位置
				mysql>SHOW MASTER STATUS;
			3.后面不详细叙述了
	3.mysql读写分离配置
		1.安装lua
		2.安装mysql-proxy
		
	 */
	
	
}
