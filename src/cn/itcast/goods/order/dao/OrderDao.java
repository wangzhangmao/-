package cn.itcast.goods.order.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.daomain.Book;
import cn.itcast.goods.order.domain.Page;
import cn.itcast.goods.order.domain.CriteriaOrder;
import cn.itcast.goods.order.domain.Order;
import cn.itcast.goods.order.domain.OrderItem;
import cn.itcast.goods.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {
	private QueryRunner qr = new TxQueryRunner();
	
	public void add(Order order) throws Exception {
		/*
		 * 1. 插入订单
		 */
		String sql = "insert into t_order values(?,?,?,?,?,?)";
		Object[] params = {order.getOid(), order.getOrdertime(),
				order.getTotal(),order.getStatus(),order.getAddress(),
				order.getOwner().getUid()};
		qr.update(sql, params);
		
		/*
		 * 2. 循环遍历订单的所有条目,让每个条目生成一个Object[]
		 * 多个条目就对应Object[][]
		 * 执行批处理，完成插入订单条目
		 */
		sql = "insert into t_orderitem values(?,?,?,?,?,?,?,?)";
		int len = order.getOrderItemList().size();
		Object[][] objs = new Object[len][];
		for(int i = 0; i < len; i++){
			OrderItem item = order.getOrderItemList().get(i);
			objs[i] = new Object[]{item.getOrderItemId(),item.getQuantity(),
					item.getSubtotal(),item.getBook().getBid(),
					item.getBook().getBname(),item.getBook().getCurrPrice(),
					item.getBook().getImage_b(),order.getOid()};
		}
		qr.batch(sql, objs);
		
	}

	public Page<Order> getOrderAll(CriteriaOrder c,User user) {
	    Page<Order> pageOrder=new Page<Order>(c.getPageNo());
	    try {
			pageOrder.setTotalItemNumber(getTotalItemNumber(user));
            c.setPageNo(pageOrder.getPageNo());
			pageOrder.setList(getOrderList(c,user));
			return pageOrder;
		} catch (SQLException e) {e.printStackTrace();}
		return null;
	}

	private List<Order> getOrderList(CriteriaOrder c, User user) throws SQLException  {
		String sql="select * from t_order where uid=? limit ?,?";
		List<Order> orders=qr.query(sql, new BeanListHandler<Order>(Order.class), user.getUid(),(c.getPageNo()-1)*5,5);
		for(Order order:orders){
			loadOrderItem(order);
		}
		return orders;
	}

	private long getTotalItemNumber(User user) throws SQLException {
		String sql="select count(oid) from t_order where uid=?";
		Long totalItemNumber=(Long) qr.query(sql, new ScalarHandler(), user.getUid());
		System.out.println(totalItemNumber);
		return totalItemNumber;
	}

	public Order load(String oid) throws SQLException {
		String sql = "select * from t_order where oid=?";
		Order order = qr.query(sql, new BeanHandler<Order>(Order.class), oid);
		loadOrderItem(order);//为当前订单加载它的所有订单条目
		return order;
	}

	private void loadOrderItem(Order order) throws SQLException {
		String sql = "select * from t_orderitem where oid=?";
		List<Map<String,Object>> mapList = qr.query(sql, new MapListHandler(), order.getOid());
		List<OrderItem> orderItemList = toOrderItemList(mapList);
		order.setOrderItemList(orderItemList);
	}

	/**
	 * 把多个Map转换成多个OrderItem
	 */
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for(Map<String,Object> map : mapList) {
			OrderItem orderItem = toOrderItem(map);
			orderItemList.add(orderItem);
		}
		return orderItemList;
	}
	
	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}
	
	/**
	 * 查询订单状态
	 */
	public int findStatus(String oid) throws SQLException {
		String sql = "select status from t_order where oid=?";
		Number number = (Number)qr.query(sql, new ScalarHandler(), oid);
		return number.intValue();
	}
	
	/**
	 * 修改订单状态
	 */
	public void updateStatus(String oid, int status) throws SQLException {
		String sql = "update t_order set status=? where oid=?";
		qr.update(sql, status, oid);
	}

	//获取全部订单
	public Page<Order> getOrderAllNoUser(CriteriaOrder c) {
		Page<Order> pageOrder=new Page<Order>(c.getPageNo());
	    try {
			pageOrder.setTotalItemNumber(getTotalItemNumberNoUser());
            c.setPageNo(pageOrder.getPageNo());
			pageOrder.setList(getOrderListNoUser(c));
			return pageOrder;
		} catch (SQLException e) {e.printStackTrace();}
		return null;
	}
	private long getTotalItemNumberNoUser() throws SQLException {
		String sql="select count(oid) from t_order";
		Long totalItemNumber=(Long) qr.query(sql, new ScalarHandler());
		System.out.println(totalItemNumber);
		return totalItemNumber;
	}
	private List<Order> getOrderListNoUser(CriteriaOrder c) throws SQLException  {
		String sql="select * from t_order limit ?,?";
		List<Order> orders=qr.query(sql, new BeanListHandler<Order>(Order.class),(c.getPageNo()-1)*5,5);
		for(Order order:orders){
			loadOrderItem(order);
		}
		return orders;
	}

	//获取对应状态的订单
	public Page<Order> findStatusByStatus(CriteriaOrder c, int status) {
		Page<Order> pageOrder=new Page<Order>(c.getPageNo());
	    try {
			pageOrder.setTotalItemNumber(getTotalItemNumberByStatus(status));
            c.setPageNo(pageOrder.getPageNo());
			pageOrder.setList(getOrderListByStatus(c,status));
			return pageOrder;
		} catch (SQLException e) {e.printStackTrace();}
		return null;
	}
	private long getTotalItemNumberByStatus(int status) throws SQLException {
		String sql="select count(oid) from t_order where status=?";
		Long totalItemNumber=(Long) qr.query(sql, new ScalarHandler(),status);
		System.out.println(totalItemNumber);
		return totalItemNumber;
	}
	private List<Order> getOrderListByStatus(CriteriaOrder c,int status) throws SQLException  {
		String sql="select * from t_order where status=? limit ?,?";
		List<Order> orders=qr.query(sql, new BeanListHandler<Order>(Order.class),status,(c.getPageNo()-1)*5,5);
		for(Order order:orders){
			loadOrderItem(order);
		}
		return orders;
	}

	//删除订单
	public void deleteOrder(String oid) throws SQLException {
		// TODO Auto-generated method stub
		String sql="delete from t_orderitem where oid=?";
		qr.update(sql,oid);
		sql="delete from t_order where oid=?";
		qr.update(sql,oid);
	}
}
