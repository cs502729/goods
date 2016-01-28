package cn.itcast.goods.book.web.servlet;



import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.servlet.BaseServlet;

public class BookServlet extends BaseServlet {
	private BookService bookService=new BookService();
	
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {
		String bid=req.getParameter("bid");
		Book book= bookService.load(bid);
		req.setAttribute("book", book);
		return "f:/jsps/book/desc.jsp";
	}
	
	/**
	 * 获取当前页码
	 * @param request
	 * @return
	 */
	private int getPc(HttpServletRequest request){
		int pc=1;
		String param=request.getParameter("pc");
		if(param!=null&&!param.trim().isEmpty()){
			try{
				pc=Integer.parseInt(param);
			}catch(Exception e){}
		}
		return pc;
	}
	/*
	 * 截取url，页面中的分页导航需要使用它来做超链接的目标
	 */
	public String getUrl(HttpServletRequest request){
		String url=request.getRequestURI()+"?"+request.getQueryString();
		int index=url.lastIndexOf("&pc=");
		if(index!=-1){
			url=url.substring(0,index);
		}
		return url;
	}

	public String findByCategory(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {
		int pc=getPc(req);
		String url=getUrl(req);
		String cid=req.getParameter("cid");
		PageBean<Book> pb=bookService.findByCategory(cid, pc);
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	/*
	 * 按作者查
	 */
	public String findByAuthor(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {
		int pc=getPc(req);
		String url=getUrl(req);
		String author=req.getParameter("author");
		PageBean<Book> pb=bookService.findByAuthor(author, pc);
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	/*
	 * 按出版社查
	 */
	public String findByPress(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {
		int pc=getPc(req);
		String url=getUrl(req);
		String press=req.getParameter("press");
		PageBean<Book> pb=bookService.findByPress(press, pc);
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	/*
	 * 按出书名查
	 */
	public String findByBname(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {
		int pc=getPc(req);
		String url=getUrl(req);
		String bname=req.getParameter("bname");
		PageBean<Book> pb=bookService.findByBname(bname, pc);
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
	/*
	 * 多条件组合查询
	 */
	public String findByCombination(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {
		int pc=getPc(req);
		Book criteria=CommonUtils.toBean(req.getParameterMap(), Book.class);
		PageBean<Book> pb=bookService.findByCombination(criteria, pc);
		String url=getUrl(req);
		pb.setUrl(url);
		req.setAttribute("pb", pb);
		return "f:/jsps/book/list.jsp";
	}
	
}
