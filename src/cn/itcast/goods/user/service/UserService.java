package cn.itcast.goods.user.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.omg.CORBA.UserException;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.user.dao.UserDao;
import cn.itcast.goods.user.domain.User;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;

/**
 * 业务层
 * @author Administrator
 *
 */
public class UserService {
	private UserDao userDao = new UserDao(); 
	
	public void update(String uid,String newpass,String oldpass) throws UserException{
		try {
			boolean bool=userDao.findByUidAndPassword(uid, oldpass);
			if(!bool){
				throw new UserException("老密码错误！") {
				};
			}
			userDao.updatePassword(uid, newpass);
		} catch (SQLException e) {
			throw new RuntimeException();
		}
	}
	
	
	
	/**
	 * 登录
	 * @param loginname
	 * @param loginpass
	 * @return
	 * @throws SQLException 
	 */
	public User login(User user){
		try {
			return userDao.findByLoginnameandloginpass(user.getLoginname(), user.getLoginpass());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 校验用户名是否注册
	 * @param loginname
	 * @return
	 * @throws SQLException 
	 */
	public boolean ajaxValidateLoginname(String loginname){
		try {
			return userDao.ajaxValidateLoginname(loginname);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	} 
	/**
	 * 检验邮箱是否注册
	 * @param exmail
	 * @return
	 * @throws SQLException
	 */
	public boolean ajaxValidateEmail(String exmail){
		try {
			return userDao.ajaxValidateEmail(exmail);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	} 
	/**
	 * 注册
	 * @param user
	 */
	public void regist(User user){
		//数据的补齐
		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActiviationCode(CommonUtils.uuid()+CommonUtils.uuid());
		try {
			userDao.add(user);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		
		//加载需要的配置文件
		Properties prop=new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		} catch (IOException e1) {
			throw new RuntimeException(e1);
		}
		//发激活邮件
		/**
		 * 登录邮件服务器得到session
		 */
		String host=prop.getProperty("host");//主机名
		String name=prop.getProperty("username");
		String pass=prop.getProperty("password");
		Session session=MailUtils.createSession(host, name, pass);
		/**
		 * 创建mail对象
		 */
		String from=prop.getProperty("from");
		String to=user.getEmail();
		String subject=prop.getProperty("subject");//主题
		//将末班中的占位符替换
		String content=MessageFormat.format(prop.getProperty("content"), user.getActiviationCode());
		//Mail mail=new Mail(from,to,subject,content);
		
		/**
		 * 发送邮件
		 */
		System.out.println("发送邮件");
		/*try {
			MailUtils.send(session, mail);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}*/
	}
}
