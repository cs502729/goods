package cn.itcast.goods.admin.category.web.servlet;



import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.goods.category.service.CategoryService;
import cn.itcast.servlet.BaseServlet;

public class AdminCategoryServlet extends BaseServlet {
	private CategoryService  categoryService=new CategoryService();
	private BookService bookService=new BookService();
	
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setAttribute("parents", categoryService.findAll());
		return "f:/adminjsps/admin/category/list.jsp";
	}
	/*
	 * 添加一级分类
	 */
	public String addParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Category parent=CommonUtils.toBean(req.getParameterMap(),Category.class);
		parent.setCid(CommonUtils.uuid());
		categoryService.add(parent);
		return findAll(req,resp);
	}
	
	//添加二级分类
	public String addChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Category child=CommonUtils.toBean(req.getParameterMap(),Category.class);
		child.setCid(CommonUtils.uuid());
		String pid=req.getParameter("pid");
		Category parent=new Category();
		parent.setCid(pid);
		child.setParent(parent);
		categoryService.add(child);
		return findAll(req,resp);
	}
	/**
	 * 添加二级分类第一步
	 * @param req
	 * @param resp
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addChildPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pid=req.getParameter("pid");//当前点击的父分类id
		List<Category> parents=categoryService.findParents();
		req.setAttribute("pid", pid);
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/category/add2.jsp";
	}
	/*
	 * 修改一级分类第一步
	 */
	public String editParentPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid=req.getParameter("cid");
		Category parent=categoryService.load(cid);
		req.setAttribute("parent", parent);
		return "f:/adminjsps/admin/category/edit.jsp";
	}
	
	/*
	 * 修改一级分类第二步
	 */
	public String editParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Category category=CommonUtils.toBean(req.getParameterMap(),Category.class);
		categoryService.edit(category);
		return findAll(req, resp);
	}
	
	
	/*
	 * 修改二级分类第一步
	 */
	public String editChildPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid=req.getParameter("cid");
		Category child=categoryService.load(cid);
		req.setAttribute("child", child);
		req.setAttribute("parents", categoryService.findParents());
		return "f:/adminjsps/admin/category/edit2.jsp";
	}
	
	
	/*
	 * 修改二级分类第二步
	 */
	public String editChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Category child=CommonUtils.toBean(req.getParameterMap(),Category.class);
		String pid=req.getParameter("pid");
		Category parent=new Category();
		parent.setCid(pid);
		child.setParent(parent);
		categoryService.edit(child);
		return findAll(req, resp);
	}
	
	/*
	 * 删除一级分类
	 */
	public String deleteParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {
		String cid=req.getParameter("cid");
		int count=categoryService.findChildrenCountByParent(cid);
		if(count>0){
			req.setAttribute("msg", "该分类下还有子分类，不能删除！");
			return "f:/adminjsps/msg.jsp";
		}else{
			categoryService.delete(cid);
			return findAll(req, resp);
		}
	}
	
	/*
	 * 删除二级分类
	 */
	public String deleteChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException {
		String cid=req.getParameter("cid");
		int count=bookService.findBookCountByCategory(cid);
		if(count>0){
			req.setAttribute("msg", "该分类下还有图书 ，不能删除！");
			return "f:/adminjsps/msg.jsp";
		}else{
			categoryService.delete(cid);
			return findAll(req, resp);
		}
	}
	
}
