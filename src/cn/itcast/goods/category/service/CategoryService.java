package cn.itcast.goods.category.service;

import java.sql.SQLException;
import java.util.List;

import cn.itcast.goods.category.dao.CategoryDao;
import cn.itcast.goods.category.domain.Category;

/**
 * 分层业务逻辑层
 * @author Administrator
 *
 */
public class CategoryService {
	CategoryDao categoryDao = new CategoryDao();
	/**
	 * 查询所有分类
	 * @return
	 */
	public List<Category> findAll(){
		try {
			return categoryDao.findAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public List<Category> findParents(){
		try {
			return categoryDao.findParents();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public List<Category> findChildren(String pid){
		try {
			return categoryDao.findByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//添加分类
	public void add(Category category){
		try {
			categoryDao.add(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//加载
	public Category load(String cid){
		try {
			return categoryDao.load(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//修改分类
	public void edit(Category category){
		try {
			categoryDao.edit(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * 查询指定副分类下子分类的个数
	 */
	public int  findChildrenCountByParent(String cid){
		try {
			return categoryDao.findChildrenCountByParent(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	 * 删除分类
	 */
	public void delete(String cid) throws SQLException{
		try {
			categoryDao.delete(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
