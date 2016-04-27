package cn.itcast.goods.category.service;

import java.sql.SQLException;
import java.util.List;

import javax.management.RuntimeErrorException;

import cn.itcast.goods.category.dao.CategoryDao;
import cn.itcast.goods.category.daomain.Category;

/**
 * 分类模块业务层
 * @author qdmmy6
 *
 */
public class CategoryService {
	private CategoryDao categoryDao = new CategoryDao();
	
	/**
	 * 查询所有分类
	 * @return
	 */
	public List<Category> findAll() {
		try {
			return categoryDao.findAll();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void add(Category parent) {
		// TODO Auto-generated method stub
		try {
			categoryDao.add(parent);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<Category> findParents() {
		// TODO Auto-generated method stub
		try {
			return categoryDao.findParents();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		return null;
	}
	
	/**
	 * 修改分类
	 */
	public void edit(Category category) {
		try {
			categoryDao.edit(category);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 加载分类
	 */
	public Category load(String cid) {
		try {
			return categoryDao.load(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	//获取子类
	public List<Category> findChildren(String pid) {
		try {
			return categoryDao.findByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	//获取指定父分类的子分类的个数  即判断是否存在子类
	public int findChildrenCountByParent(String pid) {
		try {
			return categoryDao.findChildrenCountByParent(pid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//删除分类
	public void delete(String cid) {
		try {
			categoryDao.delete(cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public String getpid(String cid) {
		// TODO Auto-generated method stub
		try {
			return categoryDao.getpid(cid);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
