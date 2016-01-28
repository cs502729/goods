package cn.itcast.goods.book.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.pager.Expression;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.goods.pager.PageConstants;
import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/*
	 * 删除图书
	 */
	public void delete(String bid) throws SQLException{
		String sql="delete from t_book where bid=?";
		qr.update(sql, bid);
	}
	
	public Book findByBid(String bid) throws SQLException{
		String sql="select * from t_book b,t_category c where c.cid=b.cid and b.bid=?";
		Map<String,Object> map=qr.query(sql, new MapHandler(),bid);
		//把除cid以外的属性映射到book中
		Book book=CommonUtils.toBean(map, Book.class);
		//仅将cid映射到category中
		Category category=CommonUtils.toBean(map, Category.class);
		book.setCategory(category);
		
		if(map.get("pid")!=null){
			Category parent=new Category();
			parent.setCid((String)map.get("pid"));
			category.setParent(parent);
		}
		return book;
	}
	
	
	public PageBean<Book> findByCategory(String cid,int pc) throws SQLException{
		List<Expression> exprList=new ArrayList<Expression>();
		exprList.add(new Expression("cid","=",cid));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 按书名模糊查询
	 * @param bname
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByName(String bname,int pc) throws SQLException{
		List<Expression> exprList=new ArrayList<Expression>();
		exprList.add(new Expression("bname","like","%"+bname+"%"));
		return findByCriteria(exprList, pc);
	}
	/**
	 * 按作者模糊查询
	 * @param author
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByAuthor(String author,int pc) throws SQLException{
		List<Expression> exprList=new ArrayList<Expression>();
		exprList.add(new Expression("author","like","%"+author+"%"));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 按出版社查
	 * @param press
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByPress(String press,int pc) throws SQLException{
		List<Expression> exprList=new ArrayList<Expression>();
		exprList.add(new Expression("press","like","%"+press+"%"));
		return findByCriteria(exprList, pc);
	}
	
	/**
	 * 按书名查询
	 * @param bname
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByBname(String bname,int pc) throws SQLException{
		List<Expression> exprList=new ArrayList<Expression>();
		exprList.add(new Expression("bname","like","%"+bname+"%"));
		return findByCriteria(exprList, pc);
	}
	/**
	 * 组合条件查询
	 * @param criteria
	 * @param pc
	 * @return
	 * @throws SQLException
	 */
	public PageBean<Book> findByCombination(Book criteria,int pc) throws SQLException{
		List<Expression> exprList=new ArrayList<Expression>();
		exprList.add(new Expression("bname","like","%"+criteria.getBname()+"%"));
		exprList.add(new Expression("author","like","%"+criteria.getAuthor()+"%"));
		exprList.add(new Expression("press","like","%"+criteria.getPress()+"%"));
		return findByCriteria(exprList, pc);
	}
	
	
	public int findBookCountByCategory(String cid) throws SQLException{
		String sql="select count(*) from t_book where cid=?";
		Number num=(Number)qr.query(sql, new ScalarHandler(),cid);
		return num==null?0:num.intValue();
	}
	
	/**
	 * 通用的查询方法
	 * @param exprList
	 * @param pc 当前页码
	 * @return
	 * @throws SQLException
	 */
	private PageBean<Book> findByCriteria(List<Expression> exprList,int pc) throws SQLException{
		int ps=PageConstants.BOOK_PAGE_SIZE;
		/**
		 * 通过exprList来生成where语句
		 */
		StringBuilder whereSql=new StringBuilder(" where 1=1");
		List<Object> params=new ArrayList<Object>();
		for(Expression expr:exprList){
			whereSql.append(" and").append(" ").append(expr.getName())
				.append(" ").append(expr.getOperator()).append(" ");
			if(!expr.getValue().equals("is null")){
				whereSql.append("?");
				params.add(expr.getValue());
			}
		}
		//总记录数
		String sql="select count(*) from t_book "+whereSql;
		Number num=(Number)qr.query(sql, new ScalarHandler(),params.toArray());
		int tr=num.intValue();
		//得到BeanList 即当前页记录
	    sql="select * from t_book"+ whereSql+" order by orderBy limit ?,?";
	    System.out.println(sql);
	    params.add((pc-1)*ps);
	    params.add(ps);
	    List<Book> beanList =(List<Book>) qr.query(sql, new BeanListHandler<Book>(Book.class), params.toArray());
	    //创建pageBean,设置参数，其中PageBean没有url,由servlet完成
	    PageBean<Book> pb=new PageBean<Book>();
	    pb.setPc(pc);
	    pb.setBeanList(beanList);
	    pb.setPs(ps);
	    pb.setTr(tr);
		return pb;
	
	}


	/**
	 * 添加图书
	 * @param book
	 * @throws SQLException 
	 */
	public void add(Book book) throws SQLException {
		String sql = "insert into t_book(bid,bname,author,price,currPrice," +
				"discount,press,publishtime,edition,pageNum,wordNum,printtime," +
				"booksize,paper,cid,image_w,image_b)" +
				" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = {book.getBid(),book.getBname(),book.getAuthor(),
				book.getPrice(),book.getCurrPrice(),book.getDiscount(),
				book.getPress(),book.getPublishtime(),book.getEdition(),
				book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(), book.getCategory().getCid(),
				book.getImage_w(),book.getImage_b()};
		qr.update(sql, params);
	}
	
	
	
	/**
	 * 修改图书
	 * @param book
	 * 		String sql = "insert into t_book(bid,bname,author,price,currPrice," +
				"discount,press,publishtime,edition,pageNum,wordNum,printtime," +
				"booksize,paper,cid,image_w,image_b)" +
	 * @throws SQLException 
	 */
	public void edit(Book book) throws SQLException {
		String sql = "update t_book set bname=?,author=?,price=?,currPrice=?," +
				"discount=?,press=?,publishtime=?,edition=?,pageNum=?,wordNum=?," +
				"printtime=?,booksize=?,paper=?,cid=? where bid=?";
		Object[] params = {book.getBname(),book.getAuthor(),
				book.getPrice(),book.getCurrPrice(),book.getDiscount(),
				book.getPress(),book.getPublishtime(),book.getEdition(),
				book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(), 
				book.getCategory().getCid(),book.getBid()};
		qr.update(sql, params);
	}
}
