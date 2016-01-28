package cn.itcast.goods.admin.book.web.servlet;



import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.category.service.CategoryService;
import cn.itcast.goods.pager.PageBean;
import cn.itcast.servlet.BaseServlet;

public class AdminBookServlet extends BaseServlet {
	private BookService bookService=new BookService();
	private CategoryService categoryService=new CategoryService();
	
	
	/**
	 * 添加图书第一步
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<Category> parents=categoryService.findParents();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/add.jsp";
	}
	
	public String ajaxFindChildren(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pid=req.getParameter("pid");
		List<Category> children=categoryService.findChildren(pid);
		String json=toJson(children);
		resp.getWriter().print(json);
		return null;
	}
	
	private String toJson(Category category){
		StringBuilder sb=new StringBuilder("{");
		sb.append("\"cid\"").append(":").append("\"").append(category.getCid()).append("\"");
		sb.append(",");
		sb.append("\"cname\"").append(":").append("\"").append(category.getCname()).append("\"");
		sb.append("}");
		return sb.toString();
	}
	
	private String toJson(List<Category> categoryList){
		StringBuilder sb=new StringBuilder("[");
		for(int i=0;i<categoryList.size();i++){
				sb.append(toJson(categoryList.get(i)));
				if(i<categoryList.size()-1)
					sb.append(",");
			}
		sb.append("]");
		return sb.toString();
	}
	
	/**
	 * 所有分类
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findCategoryAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//得到所有分类
		List<Category> parents=categoryService.findAll();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/left.jsp";
	}
	
	
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {
		String bid=req.getParameter("bid");
		Book book= bookService.load(bid);
		req.setAttribute("book", book);
		/*
		 * 获取所有一级分类并保存
		 */
		req.setAttribute("parents", categoryService.findParents());
		/*
		 * 获取当前一级分类下得所有二级分类并保存
		 */
		String pid=book.getCategory().getParent().getCid();
		
		req.setAttribute("children", categoryService.findChildren(pid));
		return "f:/adminjsps/admin/book/desc.jsp";
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
		return "f:/adminjsps/admin/book/list.jsp";
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
		return "f:/adminjsps/admin/book/list.jsp";
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
		return "f:/adminjsps/admin/book/list.jsp";
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
		return "f:/adminjsps/admin/book/list.jsp";
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
		return "f:/adminjsps/admin/book/list.jsp";
	}
	
	/*
	 * 修改图书
	 */
	public String edit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map map=req.getParameterMap();
		Book book=CommonUtils.toBean(map, Book.class);
		Category category=CommonUtils.toBean(map, Category.class);
		book.setCategory(category);
		bookService.edit(book);
		req.setAttribute("msg", "修改图书成功");
		return "f:/adminjsps/admin/msg.jsp";
	}
	
	public String delete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid=req.getParameter("bid");
		Book book=bookService.load(bid);
		/*
		 * 删除图片
		 */
		String savepath=this.getServletContext().getRealPath("/");//获得真实的路径
		new File(savepath,book.getImage_w()).delete();
		new File(savepath,book.getImage_b()).delete();
		
		bookService.delete(bid);
		req.setAttribute("msg", "删除图书成功！");
		return "f:/adminjsps/msg.jsp";
	}
	
}
