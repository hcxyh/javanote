
1.linux中shell的使用
	在bash中，$( )与` `（反引号）都是用来作命令替换的。
	命令替换与变量替换差不多，都是用来重组命令行的，先完成引号里的命令行，然后将其结果替换出来，再重组成新的命令行。
	
	exp 1
	
	[root@localhost ~]# echo today is $(date "+%Y-%m-%d")
	today is 2017-11-07
	[root@localhost ~]# echo today is `date "+%Y-%m-%d"`
	today is 2017-11-07
	$( )与｀｀
	在操作上，这两者都是达到相应的效果，但是建议使用$( )，理由如下：
	
	｀｀很容易与''搞混乱，尤其对初学者来说，而$( )比较直观。
	最后，$( )的弊端是，并不是所有的类unix系统都支持这种方式，但反引号是肯定支持的。

2.Shell 脚本有两种方法：
	1、作为可执行程序
	chmod +x ./test.sh  #使脚本具有执行权限
./test.sh  #执行脚本
注意，一定要写成 ./test.sh，而不是 test.sh，运行其它二进制的程序也一样，直接写 test.sh，linux 系统会去 PATH 里寻找有没有叫 test.sh 的，而只有 /bin, /sbin, /usr/bin，/usr/sbin 等在 PATH 里，你的当前目录通常不在 PATH 里，所以写成 test.sh 是会找不到命令的，要用 ./test.sh 告诉系统说，就在当前目录找。
	2.作为解释器参数
	这种运行方式是，直接运行解释器，其参数就是 shell 脚本的文件名，如：
/bin/sh test.sh
/bin/php test.php
这种方式运行的脚本，不需要在第一行指定解释器信息，写了也没用。



____________________________________
Linux Source命令及脚本的执行方式解析
 
当我修改了/etc/profile文件，我想让它立刻生效，而不用重新登录；这时就想到用source命令，如:source /etc/profile
对source进行了学习，并且用它与sh 执行脚本进行了对比，现在总结一下。

source命令：
source命令也称为“点命令”，也就是一个点符号（.）,是bash的内部命令。
功能：使Shell读入指定的Shell程序文件并依次执行文件中的所有语句
source命令通常用于重新执行刚修改的初始化文件，使之立即生效，而不必注销并重新登录。
用法：
source filename 或 . filename
source命令(从 C Shell 而来)是bash shell的内置命令;点命令(.)，就是个点符号(从Bourne Shell而来)是source的另一名称。

source filename 与 sh filename 及./filename执行脚本的区别在那里呢？
1.当shell脚本具有可执行权限时，用sh filename与./filename执行脚本是没有区别得。./filename是因为当前目录没有在PATH中，所有"."是用来表示当前目录的。
2.sh filename 重新建立一个子shell，在子shell中执行脚本里面的语句，该子shell继承父shell的环境变量，但子shell新建的、改变的变量不会被带回父shell，除非使用export。
3.source filename：这个命令其实只是简单地读取脚本里面的语句依次在当前shell里面执行，没有建立新的子shell。那么脚本里面所有新建、改变变量的语句都会保存在当前shell里面。


举例说明：
1.新建一个test.sh脚本，内容为:A=1
2.然后使其可执行chmod +x test.sh
3.运行sh test.sh后，echo $A，显示为空，因为A=1并未传回给当前shell
4.运行./test.sh后，也是一样的效果
5.运行source test.sh 或者 . test.sh，然后echo $A，则会显示1，说明A=1的变量在当前shell中


2./etc/profile，~/.bashrc，shell

【环境变量配置的三个方法】

如想将一个路径加入到$PATH中，可以像下面这样做： 
1. 控制台中,不赞成使用这种方法，因为换个shell，你的设置就无效了，因此这种方法仅仅是临时使用，以后要使用的时候又要重新设置，比较麻烦。
 这个只针对特定的shell; 
$ PATH="$PATH:/my_new_path"    （关闭shell，会还原PATH）
2. 修改/etc/profile文件,如果你的计算机仅仅作为开发使用时推荐使用这种方法，因为所有用户的shell都有权使用这些环境变量，可能会给系统带来安全性问题。 这里是针对所有的用户的,所有的shell; 
$ vi /etc/profile 
在里面加入: 
export PATH="$PATH:/my_new_path" 
使用source命令使修改立刻生效：  
source  /etc/profile

3. 修改.bashrc文件,这种方法更为安全，它可以把使用这些环境变量的权限控制到用户级别,这里是针对某一个特定的用户，如果你需要给某个用户权限使用这些环境变量，你只需要修改其个人用户主目录下的.bashrc文件就可以了。
$ vi /root/.bashrc 
在里面加入： 
export PATH="$PATH:/my_new_path" 
source  /root/.bashrc
后两种方法一般需要重新注销系统才能生效，也可以使用source 命令，使修改的配置立刻

4.linux的运算符号
文件比较运算符
-e filename 如果 filename存在，则为真 [ -e /var/log/syslog ]
-d filename 如果 filename为目录，则为真 [ -d /tmp/mydir ]
-f filename 如果 filename为常规文件，则为真 [ -f /usr/bin/grep ]
-L filename 如果 filename为符号链接，则为真 [ -L /usr/bin/grep ]
-r filename 如果 filename可读，则为真 [ -r /var/log/syslog ]
-w filename 如果 filename可写，则为真 [ -w /var/mytmp.txt ]
-x filename 如果 filename可执行，则为真 [ -L /usr/bin/grep ]
filename1-nt filename2 如果 filename1比 filename2新，则为真 [ /tmp/install/etc/services -nt /etc/services ]
filename1-ot filename2 如果 filename1比 filename2旧，则为真 [ /boot/bzImage -ot arch/i386/boot/bzImage ]
字符串比较运算符 （请注意引号的使用，这是防止空格扰乱代码的好方法）
-z string 如果 string长度为零，则为真 [ -z "$myvar" ]
-n string 如果 string长度非零，则为真 [ -n "$myvar" ]
string1= string2 如果 string1与 string2相同，则为真 [ "$myvar" = "one two three" ]
string1!= string2 如果 string1与 string2不同，则为真 [ "$myvar" != "one two three" ]
算术比较运算符
num1-eq num2 等于	[ 3 -eq $mynum ]
num1-ne num2 不等于	[ 3 -ne $mynum ]
num1-lt num2 小于	[ 3 -lt $mynum ]
num1-le num2 小于或等于	[ 3 -le $mynum ]
num1-gt num2 大于	[ 3 -gt $mynum ]
num1-ge num2 大于或等于	[ 3 -ge $mynum ]

5.linux 中的date 操作和计算 
	date -d  , date -s 
	
	date +"%Y-%m-%d" // 注意 ：+ 和格式之间没有空格
	date -d "1 day ago" +"%Y-%m-%d" 昨天时间
	date +%Y%m%d //显示前天年月日 
	date -d "+1 day" +%Y%m%d //显示前一天的日期 
	date -d "-1 day" +%Y%m%d //显示后一天的日期 
	date -d "-1 month" +%Y%m%d //显示上一月的日期 
	date -d "+1 month" +%Y%m%d //显示下一月的日期 
	date -d "-1 year" +%Y%m%d //显示前一年的日期 
	date -d "+1 year" +%Y%m%d //显示下一年的日期

	
6.linux中 cornTab + rsync 
	scp用于同一网段的主机间同步文件.
	
7.awk,sed,grep

8.
	

