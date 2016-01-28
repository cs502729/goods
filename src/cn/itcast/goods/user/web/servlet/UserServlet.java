package cn.itcast.goods.user.web.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.omg.CORBA.UserException;

import sun.nio.cs.ext.MacHebrew;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.user.domain.User;
import cn.itcast.goods.user.service.UserService;
import cn.itcast.servlet.BaseServlet;

/**
 * 控制层
 * @author Administrator
 *
 */
public class UserServlet extends BaseServlet {
	private UserService userService= new UserService(); 
	
	
	public String quit(HttpServletRequest request,HttpServletResponse response) throws IOException{
		request.getSession().invalidate();
		return "r:/jsps/user/login.jsp";
	}
	/**
	 * 修改密码
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public String updatePassword(HttpServletRequest request,HttpServletResponse response) throws IOException{
		User formUser=CommonUtils.toBean(request.getParameterMap(),User.class);
		User user=(User) request.getSession().getAttribute("sessionUser");
		if(user==null){
			request.setAttribute("msg", "您还没有登录！");
			return "f:/jsp/user/login.jsp";
		}
		try {
			userService.update(user.getUid(), formUser.getNewpass(),user.getLoginpass());
			request.setAttribute("msg", "修改成功！");
			request.setAttribute("code", "success");
			return "f:/jsps/msg.jsp";
		} catch (UserException e) {
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("user", formUser);//为了回显
			return "f:/jsps/user/pwd.jsp";
		}
		
	}
	
	/**
	 * 校验用户名
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public String ajaxValidateLoginname(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String loginname=request.getParameter("loginname");
		boolean b=userService.ajaxValidateLoginname(loginname);
		response.getWriter().print(b);
		return null;
	}
	
	/**
	 * 校验邮箱
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public String ajaxValidateEmail(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String email=request.getParameter("email");
		boolean b=userService.ajaxValidateLoginname(email);
		response.getWriter().print(b);
		return null;
	}
	/**
	 * 输入的检验码是否正确
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public String ajaxValidateVerifyCode(HttpServletRequest request,HttpServletResponse response) 
			throws ServletException,IOException{
		String verifyCode=request.getParameter("verifyCode");
		String vcode=(String)request.getSession().getAttribute("vCode");
		boolean b=verifyCode.equalsIgnoreCase(vcode);
		response.getWriter().print(b);
		return null;
	}
	
	 /**
	  * 激活功能
	  * @param request
	  * @param response
	  * @return
	  * @throws IOException
	  */
		public String activation(HttpServletRequest request,HttpServletResponse response) throws IOException{
			System.out.println("激活");
			return null;
		}
	/**
	 * 激活功能
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
		public String regist(HttpServletRequest request,HttpServletResponse response) throws IOException{
			//将表单数据封装到user对象
			User formUser=CommonUtils.toBean(request.getParameterMap(), User.class);
			//校验,如果校验失败，返回错误信息，返回regist.jsp
			Map<String,String> errors=validateRegist(formUser,request.getSession());
			if(errors.size()>0){
				request.setAttribute("form", formUser);
				request.setAttribute("errors", errors);
				return "f:/jsps/user/regist.jsp";
				}
			//注册
			userService.regist(formUser);
			request.setAttribute("code", "success");
			request.setAttribute("mas","注册成功，请马上到邮箱激活！");
			return "f:/jsps/msg.jsp";
		}
	/**
	 * 注册校验
	 * @return
	 */
		private Map<String,String> validateRegist(User formUser,HttpSession session){
			Map<String,String> errors = new HashMap<String,String>();
			/**
			 * 用户名校验
			 */
			String loginname=formUser.getLoginname();
			if(loginname==null||loginname.trim().isEmpty()){
				errors.put("loginname","用户名不能为空！");
			}else if(loginname.length()<3||loginname.length()>20){
				errors.put("loginname","用户名长度必须在3-20之间！");
			}else if(!userService.ajaxValidateLoginname(loginname)){
				errors.put("loginname", "用户名已注册");
			}
			/**
			 * 密码校验
			 */
			String loginpass=formUser.getLoginpass();
			if(loginpass==null||loginpass.trim().isEmpty()){
				errors.put("loginpass","密码不能为空！");
			}else if(loginpass.length()<3||loginpass.length()>20){
				errors.put("loginpass","密码长度必须在3-20之间！");
			}
			
			/**
			 * 确认密码校验
			 */
			String reloginpass=formUser.getReloginpass();
			if(reloginpass==null||reloginpass.trim().isEmpty()){
				errors.put("reloginpass","确认密码不能为空！");
			}else if(!loginpass.equals(reloginpass)){
				errors.put("loginpass","两次密码输入不一致！");
			}
			
			/**
			 * email校验
			 */
			String email=formUser.getEmail();
			if(email==null||email.trim().isEmpty()){
				errors.put("email","邮箱不能为空！");
			}else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")){
				errors.put("email","邮箱格式错误！");
			}else if(!userService.ajaxValidateEmail(email)){
				errors.put("email", "邮箱名已注册");
			}
			// 对验证码进行校验
			String verifyCode = formUser.getVerifyCode();
			String vCode = (String) session.getAttribute("vCode");
			if(verifyCode == null || verifyCode.isEmpty()) {
				errors.put("verifyCode", "验证码不能为空！");
			} else if(verifyCode.length() != 4) {
				errors.put("verifyCode", "错误的验证码！");
			} else if(!verifyCode.equalsIgnoreCase(vCode)) {
				errors.put("verifyCode", "错误的验证码！");
			}
			return errors;
		}
		
		/**
		 * 登录功能
		 * @param request
		 * @param response
		 * @return
		 * @throws IOException
		 */
		public String login(HttpServletRequest request,HttpServletResponse response) throws IOException{
			User formUser=CommonUtils.toBean(request.getParameterMap(), User.class);
			Map<String,String> errors=validateLogin(formUser,request.getSession());
			if(errors.size()>0){
				request.setAttribute("form", formUser);
				request.setAttribute("errors", errors);
				return "f:/jsps/user/login.jsp";
				}
			User user=userService.login(formUser);
			if(user==null){
				request.setAttribute("msg", "密码错误！");
				request.setAttribute("user", formUser);
				return "f:/jsps/user/login.jsp";
			}else{
				if(!user.isStatus()){
					request.setAttribute("msg", "账号未激活！");
					request.setAttribute("user", formUser);
					return "f:/jsps/user/login.jsp";
				}else{
					request.getSession().setAttribute("sessionUser", user);
					String loginname=user.getLoginname();
					loginname=URLEncoder.encode(loginname, "utf-8");
					Cookie cookie=new Cookie("loginname",loginname);
					cookie.setMaxAge(60*60*24*10);//保存十天
					response.addCookie(cookie);
					return "r:/index.jsp";
				}
			}
		}
		
		
		/**
		 * 登录校验
		 * @return
		 */
			private Map<String,String> validateLogin(User formUser,HttpSession session){
				Map<String,String> errors = new HashMap<String,String>();
			
				return errors;
			}
		
}
