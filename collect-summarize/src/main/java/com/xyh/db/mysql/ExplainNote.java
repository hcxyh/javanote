package com.xyh.db.mysql;

/**
 * explain对sql进行优化
 * @author hcxyh  2018年8月11日
 *
 */
public class ExplainNote {
	
	/**
	 * 执行顺序
	  	1.FROM 对FROM的左边的表和右边的表计算笛卡尔积。产生虚表VT1
		2.ON 对虚表VT1进行ON筛选，只有那些符合<join-condition>的行才会被记录在虚表VT2中。
		3.JOIN 如果指定了OUTER JOIN（比如left join、 right join），那么保留表中未匹配的行就会作为外部行添加到虚拟表VT2中，产生虚拟表VT3, rug from子句中包含两个以上的表的话，那么就会对上一个join连接产生的结果VT3和下一个表重复执行步骤1~3这三个步骤，一直到处理完所有的表为止。
		4.WHERE 对虚拟表VT3进行WHERE条件过滤。只有符合<where-condition>的记录才会被插入到虚拟表VT4中。
		5.GROUP BY 根据group by子句中的列，对VT4中的记录进行分组操作，产生VT5.
		6.WITH CUBE or WITH ROLLUP 对表VT5进行cube或者rollup操作，产生表VT6.
		7.HAVING 对虚拟表VT6应用having过滤，只有符合<having-condition>的记录才会被 插入到虚拟表VT7中。
		8.SELECT 执行select操作，选择指定的列，插入到虚拟表VT8中。
		9.DISTINCT 对VT8中的记录进行去重。产生虚拟表VT9.
		10.ORDER BY 将虚拟表VT9中的记录按照<order_by_list>进行排序操作，产生虚拟表VT10.
		11.TOP 取出指定行的记录，产生虚拟表VT11, 并将结果返回。
 * 		From
 * 		ON
 * 		(left,right,)Join
 * 		where
 * 		group by
 * 		having
 * 		select
 * 		Distinct
 * 		order by 
 * 		limit 
		from >  on > (left/right)join >where > Group by > having > select > DISTINCT > order by > TOP
	 */
	
}
