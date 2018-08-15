


记录下linux下的软件安装.
(因为一直使用contos比较多,默认以此为例)

1.源码安装
	通常都要有对应的编译环境(gcc，gcc++等)
	./configure --prefix =  配置  
	{不指定prefix，则可执行文件默认放在/usr /local/bin，库文件默认放在/usr/local/lib，配置文件默认放在/usr/local/etc。其它的资源文件放在/usr 	/local/share。}
	make
	make install 		

2.rpm安装
	(rpm的缺点就是关联性比较大,经常会出现比较多的依赖)
	1.查看yum安装的路径
		1.查找软件的安装包	  rpm -qa|grep redis 
		2.查找安装包的路径	  rpm -ql redis-3.2.10-2.el7.x86_64
	2.yum安装是不建议自定义目录(有些依赖以及存在的情况下,会再次下载占用空间)
	3.yum添加自定义源仓库
	
yum clean packages  清除缓存中的软件包文件
yum clean headers   清除缓存中的软件包文件头信息
yum clean metadata  清除缓存中的描述信息
yum clean dbcache  清除sqlite格式的描述信息
yum clean all 清除缓存中的所有数据信息
yum list all  列出所有软件包
yum list installed 列出所有已经安装的软件包
yum list available 列出可安装的软件包
yum list updates  列出所有可以更新的软件包
yum list extras 显示额外的软件包
yum list obsoletes  显示已经被淘汰的软件包
yum list recent  显示近期的软件包
	
3.service,chkconfig
	1.etc/init.d 自定义添加service
	
