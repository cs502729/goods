package cn.itcast.goods.cart.web.servlet;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.domain.Book;
import cn.itcast.goods.cart.domain.CartItem;
import cn.itcast.goods.cart.service.CartItemService;
import cn.itcast.goods.user.domain.User;
import cn.itcast.servlet.BaseServlet;

	public class CartItemServlet extends BaseServlet {
		private CartItemService cartItemService=new CartItemService();
		
		
		public String loadCartItems(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			String cartItemIds=req.getParameter("cartItemIds");
			Double total=Double.parseDouble(req.getParameter("total"));
			List<CartItem> cartItemList=cartItemService.loadCartItems(cartItemIds);
			req.setAttribute("cartItemList", cartItemList);
			req.setAttribute("total", total);
			req.setAttribute("cartItemIds", cartItemIds);
			return "f:/jsps/cart/showitem.jsp";
		}
		
		
		public String updateQuantity(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			String cartItemId=req.getParameter("cartItemId");
			int quantity=Integer.valueOf(req.getParameter("quantity"));
			CartItem cartItem=cartItemService.updateQuantity(cartItemId, quantity);
			//给客户端返回一个json对象
			StringBuilder sb=new StringBuilder("{");
			sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
			sb.append(",");
			sb.append("\"subtotal\"").append(":").append(cartItem.getSubtotal());
			sb.append("}");
			System.out.println(sb);
			resp.getWriter().print(sb);
			return null;
		}
		
		
		
		/*
		 * 批量删除	
		 */
		public String batchDelete(HttpServletRequest req, HttpServletResponse resp)
					throws ServletException, IOException {
			String cartItemIds=req.getParameter("cartItemIds");
			cartItemService.batchDelete(cartItemIds);
			return myCart(req, resp);
		}
		
			
			
		/**
		 * 我的购物车	
		 * @param req
		 * @param resp
		 * @return
		 * @throws ServletException
		 * @throws IOException
		 */
		public String myCart(HttpServletRequest req, HttpServletResponse resp)
					throws ServletException, IOException {
				User user=(User)req.getSession().getAttribute("sessionUser");
				String uid=user.getUid();
				List<CartItem>  cartItemList=cartItemService.myCart(uid);
				req.setAttribute("cartItemList",cartItemList);
				return "f:/jsps/cart/list.jsp";
				
			}
		
		/**
		 * 增加条目
		 * @param req
		 * @param resp
		 * @return
		 * @throws ServletException
		 * @throws IOException
		 */
		public String add(HttpServletRequest req, HttpServletResponse resp)
				throws ServletException, IOException {
			CartItem cartItem=CommonUtils.toBean(req.getParameterMap(), CartItem.class);
			Book book=CommonUtils.toBean(req.getParameterMap(), Book.class);
			User user=(User)req.getSession().getAttribute("sessionUser");
			cartItem.setBook(book);
			cartItem.setUser(user);
			cartItemService.add(cartItem);
			return myCart(req, resp);
		}
}
