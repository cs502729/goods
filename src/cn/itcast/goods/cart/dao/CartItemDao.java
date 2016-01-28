package cn.itcast.goods.cart.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.cart.domain.CartItem;
import cn.itcast.goods.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

public class CartItemDao {
	private QueryRunner qr=new TxQueryRunner();
	/**
	 * 用来生成where子句
	 * @param length
	 * @return
	 */
	public String toWhereSql(int length){
		StringBuilder sb=new StringBuilder(" cartItemId in (");
		for(int i=0;i<length;i++){
			sb.append("?");
			if(i<length-1){
				sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
	/*
	 * 根据cartItemIds加载CartItem
	 */
	public List<CartItem> loadCartItems(String cartItemIds) throws SQLException{
		Object[] cartItemIdArray=cartItemIds.split(",");
		String whereSql=toWhereSql(cartItemIdArray.length);
		String sql="select * from t_cartItem c,t_book b where c.bid=b.bid and"+toWhereSql(cartItemIdArray.length);
		return toCartItemsList(qr.query(sql, new MapListHandler(), cartItemIdArray));
	}
	
	public void batchDelete(String cartItemIds) throws SQLException{
		Object[] cartItemIdArray=cartItemIds.split(",");
		String whereSql=toWhereSql(cartItemIdArray.length);
		String sql="delete from t_cartItem where"+whereSql;
		qr.update(sql,cartItemIdArray);
	}
	/*
	 * 根据cartItemId查询CartItem
	 */
	public CartItem findByCartItemId(String cartItemId) throws SQLException{
		String sql="select * from t_cartItem c,t_book b where c.bid=b.bid and c.cartItemId=?";
		Map<String,Object> map=qr.query(sql, new MapHandler(),cartItemId);
		return toCartItem(map);
		
	}
	
	/**
	 * 查询某个人某本书是否存在
	 * @param uid
	 * @param bid
	 * @return
	 * @throws SQLException
	 */
	public CartItem findByUidAndBid(String uid,String bid) throws SQLException{
		String sql="select * from t_cartitem where uid=? and bid=?";
		Map<String,Object> map=qr.query(sql, new MapHandler(),uid,bid);
		CartItem cartItem=toCartItem(map);
		return cartItem;
		
	}
	/**
	 * 修改制定条目的数量
	 * @param cartItemId
	 * @param quantity
	 * @throws SQLException
	 */
	public void updateQuantity(String cartItemId,int quantity) throws SQLException{
		String sql="update t_cartitem set quantity=? where cartItemId=?";
		qr.update(sql,quantity,cartItemId);	
	}
	
	
	public void addCartItem(CartItem cartItem) throws SQLException{
		String sql="insert into t_cartitem(cartItemId,quantity,bid,uid) values(?,?,?,?)";
		Object[] params={cartItem.getCartItemId(),cartItem.getQuantity(),cartItem.getBook().getBid(),cartItem.getUser().getUid()};
		qr.update(sql,params);
	}
	
	
	/*
	 * 将map转换为CardItem
	 */
	private CartItem toCartItem(Map<String,Object> map){
		if(map==null||map.size()==0)
			return null;
		CartItem cartItem=CommonUtils.toBean(map, CartItem.class);
		String cartItemId=(String)map.get("cartItemId");
		Book book=CommonUtils.toBean(map, Book.class);
		User user= CommonUtils.toBean(map, User.class);
		cartItem.setBook(book);
		cartItem.setUser(user);
		return cartItem;
	}
	
	private List<CartItem> toCartItemsList(List<Map<String,Object>> mapList){
		List<CartItem> cartItemList = new ArrayList<CartItem>();
		if(mapList==null)
			return null;
		for(Map<String,Object> map:mapList){
			cartItemList.add(toCartItem(map));
		}
		return cartItemList;
	}
	
	/**
	 * 根据uid查找购物车条目
	 * @param uid
	 * @return
	 * @throws SQLException 
	 */
	public List<CartItem> findByUser(String uid) throws SQLException{
		String sql="select * from t_cartitem c,t_book b where c.bid=b.bid and uid=? order by c.orderBy";
		List<Map<String,Object>> mapList=qr.query(sql, new MapListHandler(), uid);
		return toCartItemsList(mapList);
		
	}
}
