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
import cn.itcast.goods.category.daomain.Category;
import cn.itcast.jdbc.TxQueryRunner;

/**
 * 分类持久层
 */
public class CategoryDao {
	private QueryRunner qr = new TxQueryRunner();
	
	//删除分类
	public void delete(String cid) throws SQLException {
		String sql = "delete from t_category where cid=?";
		qr.update(sql, cid);
	}
	
	//获取指定父分类的子分类
	public int findChildrenCountByParent(String pid) throws SQLException {
		String sql = "select count(*) from t_category where pid=?";
		Number cnt = (Number)qr.query(sql, new ScalarHandler(), pid);
		return cnt == null ? 0 : cnt.intValue();
	}
	
	//加载指定的一级分类
	public Category load(String cid) throws SQLException {
		String sql = "select * from t_category where cid=?";
		return toCategory(qr.query(sql, new MapHandler(), cid));
	}
	
	//修改分类 可以修改一级和二级分类
	public void edit(Category category) throws SQLException {
		String sql = "update t_category set cname=?, pid=?, `desc`=? where cid=?";
		String pid = null;
		if(category.getParent() != null) {
			pid = category.getParent().getCid();
		}
		Object[] params = {category.getCname(), pid, category.getDesc(), category.getCid()};
		qr.update(sql, params);
	}

	/**
	 * 返回所有分类
	 */
	public List<Category> findAll() throws SQLException {
		/*
		 * 1. 查询出所有一级分类
		 */
		String sql = "select * from t_category where pid is null";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler());
		List<Category> parents = toCategoryList(mapList);
		for (Category parent : parents) {
			// 查询出当前父分类的所有子分类
			List<Category> children = findByParent(parent.getCid());
			// 设置给父分类
			parent.setChildren(children);
		}
		return parents;
	}

	/**
	 * 通过父分类查询子分类
	 */
	public List<Category> findByParent(String pid) throws SQLException {
		String sql = "select * from t_category where pid=?";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(),
				pid);
		return toCategoryList(mapList);
	}

	/**
	 * 添加分类
	 */
	public void add(Category category) throws SQLException {
		String sql = "insert into t_category(cid,cname,pid,`desc`) values(?,?,?,?)";
		/*
		 * 因为一级分类，没有parent，而二级分类有！ 我们这个方法，要兼容两次分类，所以需要判断 desc 为关键字 必须加esc上的小点
		 */
		String pid = null;// 一级分类
		if (category.getParent() != null) {
			pid = category.getParent().getCid();
		}
		Object[] params = { category.getCid(), category.getCname(), pid,
				category.getDesc() };
		qr.update(sql, params);
	}

	// 获取所有的一级分类
	public List<Category> findParents() throws SQLException {
		String sql = "select * from t_category where pid is null order by orderBy";
		List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler());
		return toCategoryList(mapList);
	}

	/*
	 * 把一个Map中的数据映射到Category中
	 */
	private Category toCategory(Map<String, Object> map) {
		Category category = CommonUtils.toBean(map, Category.class);
		String pid = (String) map.get("pid");
		if (pid != null) {
			/*
			 * 使用一个父分类对象来拦截pid 再把父分类设置给category
			 */
			Category parent = new Category();
			parent.setCid(pid);
			category.setParent(parent);
		}
		return category;
	}

	/*
	 * 可以把多个Map(List<Map>)映射成多个Category(List<Category>)
	 */
	private List<Category> toCategoryList(List<Map<String, Object>> mapList) {
		List<Category> categoryList = new ArrayList<Category>();// 创建一个空集合
		for (Map<String, Object> map : mapList) {// 循环遍历每个Map
			Category c = toCategory(map);// 把一个Map转换成一个Category
			categoryList.add(c);// 添加到集合中
		}
		return categoryList;// 返回集合
	}

	public String getpid(String cid) throws SQLException {
		String sql="select pid from t_category where cid=?";
		return (String) qr.query(sql, new ScalarHandler(), cid);
	}

}
