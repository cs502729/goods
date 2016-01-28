package cn.itcast.goods.category.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.category.domain.Category;
import cn.itcast.jdbc.TxQueryRunner;

/**
 * 分类持久层
 * @author Administrator
 *
 */
public class CategoryDao {
	private QueryRunner qr = new TxQueryRunner();
	/*
	 * 把一个Map中的数据映射到Category中
	 */
	private Category toCategory(Map<String,Object> map) {
		/*
		 * map {cid:xx, cname:xx, pid:xx, desc:xx, orderBy:xx}
		 * Category{cid:xx, cname:xx, parent:(cid=pid), desc:xx}
		 */
		Category category = CommonUtils.toBean(map, Category.class);
		String pid = (String)map.get("pid");// 如果是一级分类，那么pid是null
		if(pid != null) {//如果父分类ID不为空，
			/*
			 * 使用一个父分类对象来拦截pid
			 * 再把父分类设置给category
			 */
			Category parent = new Category();
			parent.setCid(pid);
			category.setParent(parent);
		}
		return category;
	}
	/**
	 * 将多个map映射到多个Category中
	 * @return
	 */
	private List<Category> toCategoryList(List<Map<String,Object>> mapList){
		List<Category> categoryList=new ArrayList<Category>();
		for(Map<String,Object> map:mapList){
			Category c=toCategory(map);
			categoryList.add(c);
		}
		return categoryList;
	}
	
	
	/**
	 * 返回所有分类
	 * @return
	 * @throws SQLException
	 */
	public List<Category> findAll() throws SQLException{
		/**
		 * 1.查询所有的一级分类
		 */
		String sql="select * from t_category where pid is null  order by orderBy";
		List<Map<String,Object>> mapList=qr.query(sql, new MapListHandler());
		List<Category> parents=toCategoryList(mapList);;
		/**
		 * 2.循环遍历所有的一级分类，加载它的二级分类
		 */
		for(Category parent:parents){
			List<Category> children =findByParent(parent.getCid());
			parent.setChildren(children);
		}
		return parents;
	}
	
	/**
	 * 查询所有的一级分类
	 */
	public List<Category> findParents() throws SQLException{
		String sql="select * from t_category where pid is null  order by orderBy";
		List<Map<String,Object>> mapList=qr.query(sql, new MapListHandler());
		return toCategoryList(mapList);
	}
	
	
	/**
	 * 根据父分类查询子分类
	 * @param pid
	 * @return
	 * @throws SQLException 
	 */
	public List<Category> findByParent(String pid) throws SQLException{
		String sql="select * from t_category where pid=?";
		List<Map<String,Object>> mapList=qr.query(sql, new MapListHandler(),pid);
		return toCategoryList(mapList);
	}
	//添加分类
	public void add(Category category) throws SQLException{
		String sql="insert into t_category(cid,cname,pid,`desc`) values(?,?,?,?)";
		/**
		 * 因为要添加一级分类个二级分类 所以要兼容，就要判断
		 */
		String pid=null;
		if(category.getParent()!=null){  //二级分类
			pid=category.getParent().getCid();  //二级分类的父节点为其一级分类的cid
		}
		qr.update(sql, category.getCid(),category.getCname(),pid,category.getDesc());
	}
	
	/**
	 * 加载分类，既可加载一级分类也可加载二级分类
	 * @param cid
	 * @return
	 * @throws SQLException 
	 */
	public Category load(String cid) throws SQLException{
		String sql="select * from t_category where cid=?";
		return toCategory(qr.query(sql, new MapHandler(),cid));
	}
	
	/**
	 * 修改分类，即可修改一级分类也可修改二级分类
	 * @param cid
	 * @return
	 * @throws SQLException
	 */
	public void edit(Category category) throws SQLException{
		String sql="update t_category set cname=?,pid=?,`desc`=? where cid=?";
		String pid=null;//一级分类的pid为空
		if(category.getParent()!=null){
			pid=category.getParent().getCid();
		}
		Object[] params={category.getCname(),pid,category.getDesc(),category.getCid()};
		qr.update(sql, params);
	}
	
	/*
	 * 查询指定副分类下子分类的个数
	 */
	public int findChildrenCountByParent(String pid) throws SQLException{
		String sql="select count(*) from t_category where pid=?";
		Number num=(Number) qr.query(sql, new ScalarHandler(),pid);
		return num==null?0:num.intValue();
	}
	/*
	 * 删除分类
	 */
	public void delete(String cid) throws SQLException{
		String sql="delete from t_category where cid=?";
		qr.update(sql, cid);
	}
}
