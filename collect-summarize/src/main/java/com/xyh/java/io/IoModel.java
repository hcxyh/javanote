package com.xyh.java.io;

/**
 * 线程模型  --> 要区分讨论的上下文
 * @author hcxyh  2018年8月12日
 *
 */
public class IoModel {
	
	/**
	 * IO模型
	 * IO 多路复用是5种I/O模型中的第3种，对各种模型讲个故事，描述下区别：
		
		故事情节为：老李去买火车票，三天后买到一张退票。参演人员（老李，黄牛，售票员，快递员），往返车站耗费1小时。
		
		1.阻塞I/O模型
		老李去火车站买票，排队三天买到一张退票。
		耗费：在车站吃喝拉撒睡 3天，其他事一件没干。
		
		2.非阻塞I/O模型
		老李去火车站买票，隔12小时去火车站问有没有退票，三天后买到一张票。
		耗费：往返车站6次，路上6小时，其他时间做了好多事。
		
		3.I/O复用模型
		
		1.select/poll
		老李去火车站买票，委托黄牛，然后每隔6小时电话黄牛询问，黄牛三天内买到票，然后老李去火车站交钱领票。 
		耗费：往返车站2次，路上2小时，黄牛手续费100元，打电话17次
		
		2.epoll
		老李去火车站买票，委托黄牛，黄牛买到后即通知老李去领，然后老李去火车站交钱领票。 
		耗费：往返车站2次，路上2小时，黄牛手续费100元，无需打电话
		
		4.信号驱动I/O模型
		老李去火车站买票，给售票员留下电话，有票后，售票员电话通知老李，然后老李去火车站交钱领票。 
		耗费：往返车站2次，路上2小时，免黄牛费100元，无需打电话
		
		5.异步I/O模型
		老李去火车站买票，给售票员留下电话，有票后，售票员电话通知老李并快递送票上门。 
		耗费：往返车站1次，路上1小时，免黄牛费100元，无需打电话
		
		1同2的区别是：自己轮询
		2同3的区别是：委托黄牛
		3同4的区别是：电话代替黄牛
		4同5的区别是：电话通知是自取还是送票上门
		
	2.io模型
	{
		A君喜欢下馆子吃饭，服务员点完餐后，A君一直坐在座位上等待厨师炒菜，什么事情也没有干，过了一会服务员端上饭菜后，A君就开吃了 -- 【阻塞I/O】

		B君也喜欢下馆子，服务员点完餐后，B君看这个服务员姿色不错，便一直和服务员聊人生理想，并时不时的打听自己的饭做好了没有，过了一会饭也做好了，B君也撩到了美女服务员的微信号 -- 【非阻塞I/O 】顺便撩了个妹子☺
		
		C君同样喜欢下馆子吃饭，但是C君不喜欢一个人下馆子吃，要呼朋唤友一起下馆子，但是这帮人到了饭店之后，每个人只点自己的，服务员一起给他们下单后，就交给后厨去做了，每做好一个人的，服务员就负责给他们端上来。做他们的服务员真滴好累😫 -- 【IO多路复用】
		
		D君比较宅，不喜欢下馆子，那怎么办呢？美团外卖啊（此处应有广告费:-D）手机下单后，自己啥也不用操心，只要等快递小哥上门就行了，这段时间可以撸好几把王者农药的了，嘿嘿 -- 【异步IO】
	}
		
	 */
	

}
