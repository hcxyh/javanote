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
                    4. list --> 列出所有的表
                       describe 'tabName' 查看该表的默认参数

              }

            eg:以twitter为例,分析hbase的使用.
                用户（users）， 推贴（），关系（relation）
                list --> 列出所有的表

            1.hbase的表中至少需要有一个 columnFamily 列族.列族直接影响数据存储的物理性质.
            创建表时必须至少指定一个列族，之后还可以对列族进行修改(很麻烦).



            java api 交互：
                    (显示设定配置信息来获取连接，或者通过hbase-site.xml文件获取配置信息)
                    Configuration myConf = HBaseConfiguration.create();
                    myConf.set("parameter_name","parameter_value");
                    HTableInterface myHbaseTab = new HTable(myconf,"tabName");
                * hbase客户端需要配置 zookeeper quorum地址来获取连接.

                habse的连接管理:
                    创建表是一个开销很大的操作,需要占用比较多的网络资源.与建表句柄相比,使用连接池比较好.
                    连接从连接池中获取,然后再返回连接池.
                  HTablePool hp = new HTablePool();
                  HTableInterface userTable = pool.getTable("tableName");
                  ````
                  userTable.close(); 返回连接资源到连接池.
                1.数据操作
                    GET（读），put（写），delete（删除）,scan(扫描),Increment(递增)

                    Put p = new Put(Bytes.toBytes("rowkey"));
                    hbase中所有的数据都是作为原始数据(raw data)使用字节数组进行存储的.
                    p.add(Bytes.toBytes("columnFamily"),Bytes.toBytes("columnName1"),Bytes.toBytes("columnName1"))
                    p.add(Bytes.toBytes("columnFamily"),Bytes.toBytes("columnName2"),Bytes.toBytes("columnName2"))
                    p.add(Bytes.toBytes("columnFamily"),Bytes.toBytes("columnName3"),Bytes.toBytes("columnName3"))
                    hbase使用坐标来定位数据,rowkey是第一层,columnfamily是第二层,(列族代表一组列).
                    再下一坐标是列限定符(column qualifier),简称列(column)或标志(qual)
                    三个坐标可以唯一确定数据单元(cell）的位置.[rowkey,columnfamily,column qual]
                    userTable.put(p);
                    userTable.close();
                    修改数据的方式和新增是一样的,创建put对象,在正确的坐标上给出数据。

           2.读数据
                工作机制
                1.无论新增还是修改已有的行,hbase接到命令后存下变化信息或者写入失败抛出异常.
                默认情况下,执行写入操作时会写入到两个地方.
                    a)预写时日志（write-ahead-log）WAL
                    b)MemStore(内存内的写入缓冲区)，hbase数据在永久写入磁盘之前在这里进行累积.当MemStore填满后，数据会刷写
                    到硬盘,生成一个HFile（hbase的底层存储格式）.
                    HFile对应于列族,一个列族可以有多个HFile，但一个HFile不能存储多个列族的数据.
                    (在集群的每个节点上,每个列族有一个MemStore)memStore的大小hbase-site.xml文件里的系统级属性
                    hbase.hregion.mwmstore.flush.size来确定.
                    大型分布式系统的故障很常见,试想如果在MemStore没有存储到HFile之前发生了故障.内存中没有写入硬盘的数据
                    就会丢失.hbase的应对办法是在写动作完成之前,先写入WAL,hbase集群中的每台服务器维护一个WAL来记录发生的变化.
                    WAL是底层文件系统的一个文件,知道wal新纪录被写入完成后,写动作才被认为真正完成.
                    (?如果在普通的文件系统类如linux-ext，windows的fat32，ndfs上如何提供wal的功能)
                    一般情况下,hbase使用hadoop分布式文件系统hdfs来作为底层文件系统
                    如果hbase服务器宕机,没有从memstore写入到hfile的数据可以通过wal进行恢复.
                    每台服务器有一个WAL,给这台服务器所有的表和他们的列族共享.
                    p.setWriteToWal（false）; //会有数据丢失且不可恢复的风险,但是可以提升写性能.

                只有当这两个地方都写入的变化信息都得到确认之后,才认为写动作完成.
           3.取数据
                Get g = new Get(Bytes.toByte("rowKey"));
                Result r = userTable.get(g);
                返回一个包含数据的result实例.包含所有列族的所有列。
                g.addFamily(Bytes.toBytes("colimnFamily"));
                g.addColumn(Bytes.toBytes("columnfamily"),Bytes.toBytes("columnName1"));
                g.addColumn(Bytes.toBytes("columnfamily"),Bytes.toBytes("columnName2")); //缩小查找范围
                byte[] b = r.getValue(Bytes.toBytes("columnName"));  //检索特定值，将返回值转换为字符串
                

   */


}
