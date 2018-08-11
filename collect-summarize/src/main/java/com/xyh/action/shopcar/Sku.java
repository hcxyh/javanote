package com.xyh.action.shopcar;

/**
 * 电商系统中商品的最小原子单位.(一般在业务中不直接使用,仓库,订单多个系统都依赖,保持无状态)
 * @author hcxyh  2018年8月10日
 *
 */
public class Sku {
	
	private float price;
	private String id;

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	

}
