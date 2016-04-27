package cn.itcast.goods.car.web.servlet;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.daomain.Book;
import cn.itcast.goods.car.daomain.CartItem;
import cn.itcast.goods.car.service.CartItemService;
import cn.itcast.goods.user.domain.User;
import cn.itcast.servlet.BaseServlet;

public class CartItemServlet extends BaseServlet {
	private CartItemService cartItemService = new CartItemService();
	
	//结算
	public String loadCartItems(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cartItemIds = req.getParameter("cartItemIds");
		double total = Double.parseDouble(req.getParameter("total"));
		List<CartItem> cartItemList = cartItemService.loadCartItems(cartItemIds);
		req.setAttribute("cartItemList", cartItemList);
		req.setAttribute("total", total);
		req.setAttribute("cartItemIds", cartItemIds);
		return "f:/jsps/cart/showitem.jsp";
	}
	
	 //批量删除功能
	public String batchDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 获取cartItemIds参数
		 * 2. 调用service方法完成工作
		 * 3. 返回到list.jsp
		 */
		String cartItemIds = req.getParameter("cartItemIds");
		cartItemService.batchDelete(cartItemIds);
		return myCart(req, resp);
	}
	
	//修改购物车单项条目
	public void updateQuantity(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cartItemId=req.getParameter("cartItemId");
		String quantitystr=req.getParameter("quantity");
		int quantity=-1;
		try {
			quantity=Integer.parseInt(quantitystr);
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(quantity>0){
			CartItem cartItem = cartItemService.updateQuantity(cartItemId, quantity);
			
			// 给客户端返回一个json对象
			StringBuilder sb = new StringBuilder("{");
			sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
			sb.append(",");
			sb.append("\"subtotal\"").append(":").append(cartItem.getSubtotal());
			sb.append("}");

			resp.getWriter().print(sb);
		}
	}
	
	//删除购物车单项条目
	public String deleteCar(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid=req.getParameter("bid");
		cartItemService.deleteCar(bid);
		return "r:/CartItemServlet?method=add";
	}
	
	
	/**
	 * 添加购物车条目
	 */
	public String add(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 封装表单数据到CartItem(bid, quantity)
		 */
		Map map = req.getParameterMap();
		CartItem cartItem = CommonUtils.toBean(map, CartItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		User user = (User)req.getSession().getAttribute("sessionUser");
		cartItem.setBook(book);
		cartItem.setUser(user);
		
		/*
		 * 2. 调用service完成添加
		 */
		cartItemService.add(cartItem);
		/*
		 * 3. 查询出当前用户的所有条目，转发到list.jsp显示
		 */
		return myCart(req, resp);
	}
	
	/**
	 * 我的购物车
	 */
	public String myCart(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 1. 得到uid
		 */
		User user = (User)req.getSession().getAttribute("sessionUser");
		String uid = user.getUid();
		/*
		 * 2. 通过service得到当前用户的所有购物车条目
		 */
		List<CartItem> cartItemLIst = cartItemService.myCart(uid);
		/*
		 * 3. 保存起来，转发到/cart/list.jsp
		 */
		req.setAttribute("cartItemList", cartItemLIst);
		return "f:/jsps/cart/list.jsp";
	}
}
