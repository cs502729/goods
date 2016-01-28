package cn.itcast.goods.cart.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.cart.dao.CartItemDao;
import cn.itcast.goods.cart.domain.CartItem;

public class CartItemService {
	private CartItemDao cartItemDao=new CartItemDao();
	
	
	public List<CartItem> loadCartItems(String cartItemIds){
		try {
			return cartItemDao.loadCartItems(cartItemIds);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/*
	 * 更新购物车中的数量
	 */
	public CartItem updateQuantity(String cartItemId,int quantity){
		try{
			cartItemDao.updateQuantity(cartItemId, quantity);
			return cartItemDao.findByCartItemId(cartItemId);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 添加条目
	 * @param cartItem
	 */
	public void add(CartItem cartItem){
		//查询条目是否存在，不存在添加，存在就更新
		
		try {
			CartItem _cartItem=cartItemDao.findByUidAndBid(cartItem.getUser().getUid(),cartItem.getBook().getBid());
			if(_cartItem==null){
				cartItem.setCartItemId(CommonUtils.uuid());
				cartItemDao.addCartItem(cartItem);
			}else{
				int quantity=cartItem.getQuantity()+_cartItem.getQuantity();
				cartItemDao.updateQuantity(_cartItem.getCartItemId(), quantity);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
	}
	/**
	 * 批量删除
	 * @param cartItemIds
	 */
	public void batchDelete(String cartItemIds){
		try {
			cartItemDao.batchDelete(cartItemIds);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	
	/**
	 * 查找订单
	 * @param uid
	 * @return
	 * @throws SQLException
	 */
	public List<CartItem> myCart(String uid) {
		try {
			return cartItemDao.findByUser(uid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
