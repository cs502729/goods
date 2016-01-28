package cn.itcast.goods.admin.category.web.servlet;



import javax.servlet.http.HttpServletRequest;

import cn.itcast.goods.order.service.OrderService;
import cn.itcast.servlet.BaseServlet;

public class AdminOrderServlet extends BaseServlet {
	private OrderService orderService=new OrderService();
	
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
	
}
