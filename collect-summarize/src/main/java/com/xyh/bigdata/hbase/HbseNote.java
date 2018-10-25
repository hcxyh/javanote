package com.xyh.bigdata.hbase;

/**
 * Hbasesu'读书笔记
 */
public class HbseNote {


  /*
    行数据库的数据模型:
    1.行数据库对每一行记录的所有列都是顺序存放，当读取其中一列时，必须顺序读下去找到，行记录在物理上是在一起的。
    2.列数据库hbase的逻辑模型是一种有序的嵌套map映射，用java语言描述的话是这样的:Map<行键,Map<列族，Map<列名,Map<时间戳，值>>>>
    其中对于同一列的数据可以保存历史版本，为了使得最新版的数据放在前面，时间戳逆序排序，其他映射键升序排列。
    {
        面向行存储的数据库主要适合于事务性要求严格场合，或者说面向行存储的存储系统适合OLTP，但是根据CAP理论，传统的RDBMS，
        为了实现强一致性，通过严格的ACID事务来进行同步，这就造成了系统的可用性和伸缩性方面大大折扣，而目前的很多NoSQL产品，
        包括Hbase，它们都是一种最终一致性的系统，它们为了高的可用性牺牲了一部分的一致性。

        Column family: 列族
            如果想在该 cloumnFamily增加另外的属性，这样很方便只需要info:newProperty就可以.
        row key:行键
            Hbase不支持条件查询以及Order by等查询，因此Row key的设计就要根据你系统的查询需求来设计了
            hbase中一条数据的唯一标识.
    }



    1.HBase
    Hbase可以自如的存储结构化和非结构化数据。
    Hbase不允许跨行的事务,可以在一行的某一列存储整数,而在另一行的同一列来存储字符串，
    Hbase被设计成在服务器集群上运行.可以横向扩展,集群中的某个节点提供一部分的存储空间,一部分缓存和一部分计算能力,
    Hbase可以自如的存储结构化和非结构化数据。
    Hbase不允许跨行的事务,可以在一行的某一列存储整数,而在另一行的同一列来存储字符串，
    Hbase被设计成在服务器集群上运行.可以横向扩展,集群中的某个节点提供一部分的存储空间,一部分缓存和一部分计算能力,
        a）版本选择
            基于不同的版本jdk,选择对于的hbase。
            HBase 和 Hadoop 兼容性。
            Hadoop 和 JDK 兼容性
        b)gui
            类比redis,选择一款通常的客户端图形化界面.
        c)环境搭建
            单机版，伪集群(一台物理机启多个进程),真实集群.
            hbase/bin.start-hbase.sh
            最重要的的两个文件(hbase-env.sh,hbase-site.xml)写在 /etc/hbase/conf路径下.
            单机模式的默认配置里,写数据到/tmp目录下。
            编辑hbase-site.xml来修改数据的存储路径.
            <propetry>
                <name>hbase.rootdir</name>
                <value>file:///home/user/myhbasedirectory</value>
            </property>

            hbase的控制台界面  默认60010端口
            1.0.1版本的hbase的master web 默认是不运行的，所以需要自己配置默认端口。
            在hbase-site.xml中加入一下内容即可
            <property>
                <name>hbase.master.info.port</name>
                <value>60010</value>
            </property>
           d)habse shell 命令行交互
                hbase shell是一个封装了java客户端api的jruby应用软件.
                有两种运行方式:交互模式和批处理模式
                交互模式用于对HBase进行随时的访问。
                批处理模式主要通过shell脚本进行程序化交互或者记载小文件.
              /bin目录下   ./hbase shell 进入交互界面
              {
                命令行存储数据：
                    1.创建hbase表
                        create 'tabName', 'cloumnName(列族名)'
                    2.写数据
                        put 'tabName','rowNum(行号)','cloumnNmae:列名','value数据值'
                        写数据的时候,不需要提前定义列以及列中存储数据的类型.
                    3.读数据
                        get 'tabName','rowNum'
                            输出值还附带时间戳,(hbase可以存储每个数据单元的多个时间版本,存储的版本
                            数量默认是3个,可以进行设置。默认返回最新的时间版本)
                        scan  'tabName'
                            返回所有的行数据
                            返回的顺序是按行的名字进行排序,hbase称之为 rowKsy(行键).




              }

            java api 交互


           2.读数据


           3.取数据



   */


}
