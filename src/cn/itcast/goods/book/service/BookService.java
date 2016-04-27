package cn.itcast.goods.book.service;

import java.sql.SQLException;

import cn.itcast.goods.book.beanutil.CriteriaBook;
import cn.itcast.goods.book.beanutil.Page;
import cn.itcast.goods.book.dao.BookDao;
import cn.itcast.goods.book.daomain.Book;



public class BookService {
	private BookDao bookDao = new BookDao();
	
	//获取当前二级分类下是否含有图书
	public Long findBookCountByCategory(String cid) {
		try {
			return bookDao.findBookCountByCategory(cid);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 多条件组合查询
	 * @throws Exception 
	 */
	public Page<Book> findByCombination(CriteriaBook c) throws Exception {
		try {
			return bookDao.findBook(c);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public Book load(String bid) {
		try {
			return bookDao.findBookById(bid);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	//添加图书
	public void add(Book book) {
		try {
			bookDao.add(book);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	//修改图书
	public void edit(Book book) {
		try {
			bookDao.edit(book);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	//删除图书
	public void delete(String bid) {
		try {
			bookDao.delete(bid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
