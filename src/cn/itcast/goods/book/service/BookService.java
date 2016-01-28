package cn.itcast.goods.book.service;

import java.sql.SQLException;

import cn.itcast.goods.book.dao.BookDao;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.pager.PageBean;

public class BookService {
	private BookDao bookDao=new BookDao();
	
	/**
	 * 根据bid查到book
	 * @param bid
	 * @return
	 */
	public Book load(String bid){
		try {
			return bookDao.findByBid(bid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public int findBookCountByCategory(String cid) {
		try{
			return bookDao.findBookCountByCategory(cid);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	
	//按目录查
	public PageBean<Book> findByCategory(String cid,int pc) throws SQLException{
		System.out.println( bookDao.findByCategory(cid, pc));
		return bookDao.findByCategory(cid, pc);
	}
	//按出版社查
	public PageBean<Book> findByPress(String press,int pc) throws SQLException{
		return bookDao.findByPress(press, pc);
	}
	//按作者查
	public PageBean<Book> findByAuthor(String author,int pc) throws SQLException{
		return bookDao.findByAuthor(author, pc);
	}
	//按书名查
	public PageBean<Book> findByBname(String bname,int pc) throws SQLException{
		return bookDao.findByBname(bname, pc);
	}
	//按多条件查
	public PageBean<Book> findByCombination(Book criteria,int pc) throws SQLException{
		return bookDao.findByCombination(criteria, pc);
	}
	
	
	//添加图书
	public void add(Book book){
		try{
			bookDao.add(book);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	//编辑图书
		public void edit(Book book){
			try{
				bookDao.edit(book);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
		
		
	/*
	 * 删除图书	
	 */
		public void delete(String bid){
			try{
				bookDao.delete(bid);
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}	
}
