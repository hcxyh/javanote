package com.xyh.action.shopcar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 购物车
 * @author hcxyh  2018年8月10日
 *
 */
public class ShopCar implements Serializable{
	
	/**
	 * 购物车实战
	 * 1）用户没登陆用户名和密码,添加商品, 关闭浏览器再打开后 不登录用户名和密码　
	 * 2）用户登陆了用户名密码,添加商品,关闭浏览器再打开后 不登录用户名和密码.
	 * 3）用户登陆了用户名密码,添加商品, 关闭浏览器,然后再打开,登陆用户名和密码 
	 * 4）用户登陆了用户名密码,添加商品, 关闭浏览器 外地老家打开浏览器  登陆用户名和密码 
	 * 
	 * 1)用户没有登录, 添加商品, 此时的商品是被添加到了浏览器的Cookie中,
	 *  所以当再次访问时(不登录),商品仍然在Cookie中, 所以购物车中的商品还是存在的.
	 * 2)用户登录了,添加商品, 此时会将Cookie中和用户选择的商品都添加到购物车中, 
	 *  然后删除Cookie中的商品. 所以当用户再次访问(不登录),
	 *  此时Cookie中的购物车商品已经被删除了, 所以此时购物车中的商品不在了.
	 * 3)用户登录, 添加商品,此时商品被添加到数据库做了持久化存储, 
	 *  再次打开登录用户名和密码, 该用户选择的商品肯定还是存在的, 
	 *  所以购物车中的商品还是存在的.
	 * 4)同上
	 * 
	 * 这里再说下 没登录 保存商品到Cookie的优点以及保存到Session和数据库的对比:
		1：Cookie： 优点： 保存用户浏览器（不用浪费我们公司的服务器） 缺点：Cookie禁用，不提供保存
		2：Session：（Redis ： 浪费大量服务器内存：实现、禁用Cookie）  速度很快
		3：数据库（Mysql、Redis、SOlr）  能持久化的就数据库  速度太慢
	 */
	  private static final long serialVersionUID = 1L;
	    
	  //商品结果集
	  private List<BuyerItem> items = new ArrayList<BuyerItem>();
	   
	   //添加购物项到购物车
	   public void addItem(BuyerItem item){
	       //判断是否包含同款
	       if (items.contains(item)) {
	           //追加数量
	           for (BuyerItem buyerItem : items) {
	               if (buyerItem.equals(item)) {
	                   buyerItem.setAmount(item.getAmount() + buyerItem.getAmount());
	               }
	           }
	       }else {
	           items.add(item);
	       }
	   }
	   
	   //读取购物车已存在购买项
	   public List<BuyerItem> getItems() {
	       return items;
	   }
	   
	   //设置购物项   1.清空购物车  2.登录后同步本地购物车
	   public void setItems(List<BuyerItem> items) {
	       this.items = items;
	   }
	   
	   
	   //小计
	   //商品数量
	   @JsonIgnore
	   public Integer getProductAmount(){
	       Integer result = 0;
	       //计算
	       for (BuyerItem buyerItem : items) {
	           result += buyerItem.getAmount();
	       }
	       return result;
	   }
	   
	   //商品金额
	   @JsonIgnore
	   public Float getProductPrice(){
	       Float result = 0f;
	       //计算
	       for (BuyerItem buyerItem : items) {
	           result += buyerItem.getAmount()*buyerItem.getSku().getPrice();
	       }
	       return result;
	   }
	   
	   //运费
	   @JsonIgnore
	   public Float getFee(){
	       Float result = 0f;
	       //计算
	       if (getProductPrice() < 79) {
	           result = 5f;
	       }
	       
	       return result;
	   }
	   
	   //总价
	   @JsonIgnore
	   public Float getTotalPrice(){
	       return getProductPrice() + getFee();
	   }
	
	
}
