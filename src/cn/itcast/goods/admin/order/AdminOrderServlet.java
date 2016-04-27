package cn.itcast.goods.admin.order;

import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.goods.order.domain.CriteriaOrder;
import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.order.domain.Page;
import cn.itcast.goods.order.service.OrderService;
import cn.itcast.goods.user.domain.User;
import cn.itcast.servlet.BaseServlet;

public class AdminOrderServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OrderService orderService = new OrderService();
	//删除订单
	public String delete(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String oid = req.getParameter("oid");
		/*
		 * 校验订单状态
		 */
		int status = orderService.findStatus(oid);
		if(status != 5) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，不能删除！");
			return "f:/adminjsps/msg.jsp";
		}
		orderService.deleteOrder(oid);//设置状态为取消！
		req.setAttribute("code", "success");
		req.setAttribute("msg", "该订单已删除！");
		return "f:/adminjsps/admin/order/msg.jsp";		
	}
	
	//发货
	public String deliver(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String oid = req.getParameter("oid");
		/*
		 * 校验订单状态
		 */
		int status = orderService.findStatus(oid);
		if(status != 2) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，不能发货！");
			return "f:/adminjsps/msg.jsp";
		}
		orderService.updateStatus(oid, 3);//设置状态为取消！
		req.setAttribute("code", "success");
		req.setAttribute("msg", "该订单已发货，状态已更新！");
		return "f:/adminjsps/admin/order/msg.jsp";		
	}
	
	//取消订单
	public String cancel(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String oid = req.getParameter("oid");
		/*
		 * 校验订单状态
		 */
		int status = orderService.findStatus(oid);
		if(status == 2||status == 3) {
			req.setAttribute("code", "error");
			req.setAttribute("msg", "状态不对，不能取消！");
			return "f:/adminjsps/msg.jsp";
		}
		orderService.updateStatus(oid, 5);//设置状态为取消！
		req.setAttribute("code", "success");
		req.setAttribute("msg", "该的订单已取消，状态已更新");
		return "f:/adminjsps/admin/order/msg.jsp";		
	}
	
	//查询详细信息
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String oid = req.getParameter("oid");
		Order order = orderService.load(oid);
		System.out.println(order);
		req.setAttribute("order", order);
		String btn = req.getParameter("btn");//btn说明了用户点击哪个超链接来访问本方法的
		System.out.println(btn);
		req.setAttribute("btn", btn);
		return "/adminjsps/admin/order/desc.jsp";
	}
	
	//按订单状态查询
	public String findByStatus(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String pageNoStr=req.getParameter("pageNo");
		String statusStr=req.getParameter("status");
		int pageNo=1;
		int status=-1;
		try {
			status=Integer.parseInt(statusStr);
			pageNo=Integer.parseInt(pageNoStr);
		} catch (Exception e) {}
		if(status>0){
			CriteriaOrder c=new CriteriaOrder();
			c.setPageNo(pageNo);
			Page<Order> pageOrder=orderService.findStatusByStatus(c,status);
			req.setAttribute("pb", pageOrder);
		return "f:/adminjsps/admin/order/list.jsp";
		}
		return "r:/jsps/error.jsp";
	}
	
	//查询所有订单
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String pageNoStr=req.getParameter("pageNo");
		int pageNo=1;
		try {
			pageNo=Integer.parseInt(pageNoStr);
		} catch (Exception e) {}
			CriteriaOrder c=new CriteriaOrder();
			c.setPageNo(pageNo);
			Page<Order> pageOrder=orderService.getOrderAllNoUser(c);
			req.setAttribute("pb", pageOrder);
			return "f:/adminjsps/admin/order/list.jsp";	
	}

}
