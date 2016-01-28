package cn.itcast.goods.cart.domain;

import java.math.BigDecimal;

import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.user.domain.User;

public class CartItem {
	private String cartItemId;//商品的id
	private int quantity;//商品的数量
	private Book book;//书对象  对应与bid
	private User user;//用户对象，对应于用户的uid
	
	//添加小计方法,使用BigDecimal不会有误差
	public double getSubtotal(){
		BigDecimal b1=new BigDecimal(book.getCurrPrice()+"");
		BigDecimal b2=new BigDecimal(quantity+"");
		BigDecimal b3=b1.multiply(b2);
		return b3.doubleValue();
	}
	public String getCartItemId() {
		return cartItemId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public void setCartItemId(String cartItemId) {
		this.cartItemId = cartItemId;
	}

	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
