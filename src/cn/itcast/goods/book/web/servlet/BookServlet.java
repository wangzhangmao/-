package cn.itcast.goods.book.web.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.goods.book.beanutil.CriteriaBook;
import cn.itcast.goods.book.beanutil.Page;
import cn.itcast.goods.book.daomain.Book;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.servlet.BaseServlet;

public class BookServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BookService bookService = new BookService();

	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String bid = req.getParameter("bid");//获取链接的参数bid
		System.out.println(bid);
		Book book = bookService.load(bid);//通过bid得到book对象
		req.setAttribute("book", book);//保存到req中
		return "f:/jsps/book/desc.jsp";//转发到desc.jsp
	}
	
	
	public String findBook(HttpServletRequest request,
			HttpServletResponse response) {
		CriteriaBook c = getCriteriaBook(request);
		try {
			Page<Book> books = bookService.findByCombination(c);
			request.setAttribute("books", books);
			System.out.println(books.getList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "f:/jsps/book/list.jsp";
	}

	private CriteriaBook getCriteriaBook(HttpServletRequest request) {
		String pageNostr = request.getParameter("pageNo");
		String category = request.getParameter("category");
		String bname = request.getParameter("bname");
		String author = request.getParameter("author");
		String press = request.getParameter("press");
		System.out.println(pageNostr + ":" + category + ":" + bname + ":"
				+ author + ":" + press );
		int pageNo = 1;
		try {
			pageNo = Integer.parseInt(pageNostr);
		} catch (Exception e) {
		}
		if (category == null || category.equals("")) {
			category = "%%";
		}
		if (bname == null || bname.equals("")) {
			bname = "%%";
		}
		if (author == null || author.equals("")) {
			author = "%%";
		}
		if (press == null || press.equals("")) {
			press = "%%";
		}
		System.out.println(pageNo + ":" + category + ":" + bname + ":" + author
				+ ":" + press);
		CriteriaBook c = new CriteriaBook(pageNo, category, bname, author,
				press);
		return c;
	}
}
