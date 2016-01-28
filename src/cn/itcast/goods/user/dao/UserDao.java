package cn.itcast.goods.user.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import sun.security.action.GetLongAction;

import cn.itcast.goods.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

/**
 * 用户持久层
 * @author Administrator
 *
 */
public class UserDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 根据uid和password查询用户
	 * @param uid
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public boolean findByUidAndPassword(String uid,String password) throws SQLException{
		String sql="select count(*) from t_user where uid=? and loginpass=?";
		Number num=(Number) qr.query(sql, new ScalarHandler(), uid,password);
		return num.intValue()>0;
	}
	/**
	 * 根据uid修改密码
	 * @param uid
	 * @param password
	 * @throws SQLException
	 */
	public void updatePassword(String uid,String password) throws SQLException{
		String sql="update t_user set loginpass=? where uid=?";
		qr.update(sql, password,uid);
	}
	/**
	 * 用户登录
	 * @param loginname
	 * @param loginpass
	 * @return
	 * @throws SQLException
	 */
	public User findByLoginnameandloginpass(String loginname,String loginpass) throws SQLException{
		String sql="select * from t_user where loginname=? and loginpass=?";
		return qr.query(sql, new BeanHandler<User>(User.class), loginname,loginpass);
	}
	
		
	/**
	 * 校验用户名是否注册
	 * @param loginname
	 * @return
	 * @throws SQLException 
	 */
	
	public boolean ajaxValidateLoginname(String loginname) throws SQLException{
		String sql = "select count(1) from t_user where loginname=?";
		Number number = (Number)qr.query(sql, new ScalarHandler(), loginname);
		return number.intValue() == 0;
	} 
	/**
	 * 检验邮箱是否注册
	 * @param exmail
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateEmail(String exmail) throws SQLException{
		String sql="select count(1) from t_user where email=?";
		Number num=(Number)qr.query(sql, new ScalarHandler(), exmail);
		return num.intValue()==0;
	} 
	
	public void add(User user) throws SQLException{
		String sql="insert into t_user values(?,?,?,?,?,?)";
		Object[] params={user.getUid(),user.getLoginname(),user.getLoginpass(),user.getEmail(),user.isStatus(),user.getActiviationCode()};
		qr.update(sql, params);
	}
}
