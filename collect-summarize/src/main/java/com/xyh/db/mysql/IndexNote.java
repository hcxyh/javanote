package com.xyh.db.mysql;

/**
 * Mysql索引总结:
 * @author hcxyh  2018年8月11日
 *
 */
public class IndexNote {
	

	/**
	 * TODO
	 * 1.索引的存储机制
	 		聚集索引和非聚集索引的根本区别是表记录的排列顺序和与索引的排列顺序是否一致，其实理解起来非常简单，
	 		还是举字典的例子：如果按照拼音查询，那么都是从a-z的，是具有连续性的，a后面就是b，b后面就是c，
	 		 聚集索引就是这样的，他是和表的物理排列顺序是一样的，例如有id为聚集索引，那么1后面肯定是2,2后面肯定是3，
	 		 所以说这样的搜索顺序的就是聚集索引。
	 		 非聚集索引就和按照部首查询是一样是，可能按照偏房查询的时候，根据偏旁‘弓’字旁，索引出两个汉字，
	 		 张和弘，但是这两个其实一个在100页，一个在1000页，（这里只是举个例子），
	 		 他们的索引顺序和数据库表的排列顺序是不一样的，这个样的就是非聚集索引。
      	原理明白了，那他们是怎么存储的呢？在这里简单的说一下，
      	聚集索引就是在数据库被开辟一个物理空间存放他的排列的值，例如1-100，所以当插入数据时，
      	他会重新排列整个整个物理空间，
      	而非聚集索引其实可以看作是一个含有聚集索引的表，他只仅包含原表中非聚集索引的列和指向实际物理表的指针。
      	他只记录一个指针，其实就有点和堆栈差不多的感觉了.
      	
      	
      	建立索引的原则：
      		{
      			1) 定义主键的数据列一定要建立索引。
				2) 定义有外键的数据列一定要建立索引。
				3) 对于经常查询的数据列最好建立索引。
				4) 对于需要在指定范围内的快速或频繁查询的数据列;
				5) 经常用在WHERE子句中的数据列。
				6) 经常出现在关键字order by、group by、distinct后面的字段，建立索引。如果建立的是复合索引，索引的字段顺序要和这些关键字后面的字段顺序一致，否则索引不会被使用。
				7) 对于那些查询中很少涉及的列，重复值比较多的列不要建立索引。
				8) 对于定义为text、image和bit的数据类型的列不要建立索引。
				9) 对于经常存取的列避免建立索引 
				9) 限制表上的索引数目。对一个存在大量更新操作的表，所建索引的数目一般不要超过3个，最多不要超过5个。索引虽说提高了访问速度，但太多索引会影响数据的更新操作。
				10) 对复合索引，按照字段在查询条件中出现的频度建立索引。在复合索引中，记录首先按照第一个字段排序。对于在第一个字段上取值相同的记录，系统再按照第二个字段的取值排序，以此类推。因此只有复合索引的第一个字段出现在查询条件中，该索引才可能被使用,因此将应用频度高的字段，放置在复合索引的前面，会使系统最大可能地使用此索引，发挥索引的作用。
      		}
      	
	 */
	
	
}
