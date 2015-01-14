package cn.wzga.core.dao.base.support;

import java.io.Serializable;

/**
 * <p>
 * Description: 排序类
 * </p>
 * 
 * @author sutong
 * @version 1.0 2014-06-19
 */
@SuppressWarnings("serial")
public class Order implements Serializable {

	private StringBuffer orderStr;

	public Order() {
		orderStr = new StringBuffer();
	}

	public static Order getInstance() {
		Order order = new Order();
		return order;
	}

	public Order asc(String field) {
		if (orderStr == null)
			return this;
		if (orderStr.length() == 0)
			orderStr.append(" " + field + " asc ");
		else
			orderStr.append(", " + field + " asc ");
		return this;
	}

	public Order desc(String field) {
		if (orderStr == null)
			return this;
		if (orderStr.length() == 0)
			orderStr.append(" " + field + " desc ");
		else
			orderStr.append(", " + field + " desc ");
		return this;
	}

	public String getOrderStr() {
		return orderStr.toString();
	}

	public void setOrderStr(StringBuffer orderStr) {
		this.orderStr = orderStr;
	}

	@Override
	public String toString() {
		if (orderStr != null) {
			return orderStr.toString();
		} else {
			return null;
		}
	}

	public static void main(String[] args) {
		Order order = Order.getInstance();
		order.asc("id");
		order.desc("name");
		System.out.println(order.getOrderStr());
	}
}
