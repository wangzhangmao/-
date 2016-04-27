package cn.itcast.goods.order.service;

import java.sql.SQLException;

import cn.itcast.goods.order.domain.Page;
import cn.itcast.goods.order.dao.OrderDao;
import cn.itcast.goods.order.domain.CriteriaOrder;
import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.user.domain.User;
import cn.itcast.jdbc.JdbcUtils;

public class OrderService {
	private OrderDao orderDao = new OrderDao();

	/**
	 * 修改订单状态
	 */
	public void updateStatus(String oid, int status) {
		try {
			orderDao.updateStatus(oid, status);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// 通过状态查询订单
	public Page<Order> findStatusByStatus(CriteriaOrder c, int status) {
		try {
			return orderDao.findStatusByStatus(c,status);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	// 通过订单号查询订单状态
	public int findStatus(String oid) {
		try {
			return orderDao.findStatus(oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void createOrder(Order order) {
		try {
			JdbcUtils.beginTransaction();
			orderDao.add(order);
			JdbcUtils.commitTransaction();
		} catch (Exception e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (Exception e1) {
			}
			throw new RuntimeException(e);
		}
	}

	public Page<Order> getOrderAll(CriteriaOrder c, User user) {
		Page<Order> pageOrder = orderDao.getOrderAll(c, user);
		return pageOrder;
	}

	/**
	 * 加载订单
	 */
	public Order load(String oid) {
		try {
			JdbcUtils.beginTransaction();
			Order order = orderDao.load(oid);
			JdbcUtils.commitTransaction();
			return order;
		} catch (SQLException e) {
			try {
				JdbcUtils.rollbackTransaction();
			} catch (SQLException e1) {
			}
			throw new RuntimeException(e);
		}
	}

	public Page<Order> getOrderAllNoUser(CriteriaOrder c) {
		// TODO Auto-generated method stub
		return orderDao.getOrderAllNoUser(c);
	}

	public void deleteOrder(String oid) {
		// TODO Auto-generated method stub
		try {
			orderDao.deleteOrder(oid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
