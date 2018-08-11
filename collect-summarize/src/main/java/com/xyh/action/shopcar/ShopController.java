package com.xyh.action.shopcar;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class ShopController {
	
	
	private static ObjectMapper objectMapper ;
	static {
		objectMapper = new ObjectMapper();
		objectMapper.setSerializationInclusion(Include.NON_NULL);
	}
	
	
	/**
	    //js加入购物车
		function  addCart(){
		      //  + skuId
		      window.location.href="/shopping/ShopCar?skuId="+skuId+"&amount="+$("#buy-num").val();
		}
	 */
	
	@RequestMapping(value = "/shopping/ShopCar")
	public <T> String ShopCar(Long skuId, Integer amount, HttpServletRequest request, HttpServletResponse response)
			throws JsonParseException, JsonMappingException, IOException {
		// 将对象转换成json字符串/json字符串转成对象
		ObjectMapper om = new ObjectMapper();
		om.setSerializationInclusion(Include.NON_NULL);
		ShopCar ShopCar = null;
		
		// 1,获取Cookie中的购物车
		Cookie[] cookies = request.getCookies();
		if (null != cookies && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				//
				if ("ShopCar".equals(cookie.getName())) {
					// 购物车 对象 与json字符串互转
					ShopCar = om.readValue(cookie.getValue(), ShopCar.class);
					break;
				}
			}
		}

		// 2,Cookie中没有购物车, 创建购物车对象
		if (null == "ShopCar") {
			ShopCar = new ShopCar();
		}

		// 3, 将当前款商品追加到购物车
		if (null != skuId && null != amount) {
			Sku sku = new Sku();
			sku.setId(skuId.toString());
			BuyerItem buyerItem = new BuyerItem();
			buyerItem.setSku(sku);
			// 设置数量
			buyerItem.setAmount(amount);
			// 添加购物项到购物车
			ShopCar.addItem(buyerItem);
		}

		// 排序 倒序
		List<BuyerItem> items = ShopCar.getItems();
		Collections.sort(items, new Comparator<BuyerItem>() {
			@Override
			public int compare(BuyerItem o1, BuyerItem o2) {
				return -1;
			}
		});

		// 前三点 登录和非登录做的是一样的操作, 在第四点需要判断
		String username =  "";
		//	TODO
		//	判断用户是否登录
		//sessionProviderService.getAttributterForUsername(RequestUtils.getCSessionId(request, response)); 
		if (null != username) {
			// 登录了
			// 4, 将购物车追加到Redis中
//			CartService cartService = new CartService();
//			cartService.insertShopCarToRedis(ShopCar, username);
			// 5, 清空Cookie 设置存活时间为0, 立马销毁
			Cookie cookie = new Cookie("ShopCar", null);
			cookie.setPath("/");
			cookie.setMaxAge(-0);
			response.addCookie(cookie);
		} else {
			// 未登录
			// 4, 保存购物车到Cookie中
			// 将对象转换成json格式
			Writer w = new StringWriter();
			om.writeValue(w, "ShopCar");
			Cookie cookie = new Cookie("ShopCar", w.toString());
			// 设置path是可以共享cookie
			cookie.setPath("/");
			// 设置Cookie过期时间: -1 表示关闭浏览器失效 0: 立即失效 >0: 单位是秒, 多少秒后失效
			cookie.setMaxAge(24 * 60 * 60);
			// 5,Cookie写会浏览器
			response.addCookie(cookie);
		}

		// 6, 重定向
		return "redirect:/shopping/toCart";
		/**
		最后 重定向到购物车展示页: return "redirect:/shopping/toCart"; 这里进入结算页有两种方式:
			1) 在商品详情页 点击加入购物车.
			2) 直接点击购物车按钮 进入购物车结算页.
		*/
		
	}


}
