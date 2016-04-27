package cn.itcast.goods.book.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.beanutil.CriteriaBook;
import cn.itcast.goods.book.beanutil.Page;
import cn.itcast.goods.book.daomain.Book;
import cn.itcast.goods.category.daomain.Category;
import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {
	private QueryRunner qr = new TxQueryRunner();
	//删除图书
	public void delete(String bid) throws SQLException {
		String sql = "delete from t_book where bid=?";
		qr.update(sql, bid);
	}
	
	//修改图书
	public void edit(Book book) throws SQLException {
		String sql = "update t_book set bname=?,author=?,price=?,currPrice=?," +
				"discount=?,press=?,publishtime=?,edition=?,pageNum=?,wordNum=?," +
				"printtime=?,booksize=?,paper=?,cid=? where bid=?";
		Object[] params = {book.getBname(),book.getAuthor(),
				book.getPrice(),book.getCurrPrice(),book.getDiscount(),
				book.getPress(),book.getPublishtime(),book.getEdition(),
				book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(), 
				book.getCid(),book.getBid()};
		qr.update(sql, params);
	}
	
	//添加图书
	public void add(Book book) throws SQLException {
		String sql = "insert into t_book(bid,bname,author,price,currPrice," +
				"discount,press,publishtime,edition,pageNum,wordNum,printtime," +
				"booksize,paper,cid,image_w,image_b)" +
				" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Object[] params = {book.getBid(),book.getBname(),book.getAuthor(),
				book.getPrice(),book.getCurrPrice(),book.getDiscount(),
				book.getPress(),book.getPublishtime(),book.getEdition(),
				book.getPageNum(),book.getWordNum(),book.getPrinttime(),
				book.getBooksize(),book.getPaper(), book.getCid(),
				book.getImage_w(),book.getImage_b()};
		qr.update(sql, params);
	}
	//获取当前二级分类下是否含有图书
	public Long findBookCountByCategory(String cid) throws SQLException {
		String sql = "select count(*) from t_book where cid=?";
		Long cnt = (Long) qr.query(sql, new ScalarHandler(), cid);
		return  cnt;
	}
	
	public Page findBook(CriteriaBook criteriaBook) throws Exception{
		Page<Book> page=new Page<Book>(criteriaBook.getPageNo());
		page.setTotalItemNumber(findTotalItemNumber(criteriaBook));
		page.setList(findBookList(criteriaBook,12));
		System.out.println("findBook");
		return page;
	}

	private List<Book> findBookList(CriteriaBook c,int pageSize) throws Exception {
		List<Book> books=new ArrayList<Book>();
		String sql="select * from  t_book where cid like ? and bname like ? and author like ? and press like ?  LIMIT ?, ?";
		books=qr.query(sql, new BeanListHandler<Book>(Book.class),  c.getCategory(),c.getBname(),c.getAuthor(),c.getPress(),(c.getPageNo() - 1) * pageSize, pageSize);
		return books;
	}

	private long findTotalItemNumber(CriteriaBook c) throws Exception {
		String sql="select count(bid) from t_book where cid like ? and bname like ? and author like ? and press like ?";
		Long totalItemNumber=(Long) qr.query(sql,new ScalarHandler(), c.getCategory(),c.getBname(),c.getAuthor(),c.getPress());
		System.out.println(totalItemNumber);
		return totalItemNumber;
	}

	public Book findBookById(String bid) throws Exception {
			String sql = "select * from t_book where bid=?";
			Book book = qr.query(sql, new BeanHandler<Book>(Book.class), bid);

			return book;
		}
}
