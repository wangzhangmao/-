package cn.itcast.goods.admin.book.web.servlet;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.beanutil.CriteriaBook;
import cn.itcast.goods.book.beanutil.Page;
import cn.itcast.goods.book.daomain.Book;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.category.daomain.Category;
import cn.itcast.goods.category.service.CategoryService;
import cn.itcast.servlet.BaseServlet;

public class AdminBookServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();

	//删除图书
	public String delete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String bid = req.getParameter("bid");
		Book book = bookService.load(bid);
		String savepath = this.getServletContext().getRealPath("/");//获取真实的路径
		new File(savepath, book.getImage_w()).delete();//删除文件
		new File(savepath, book.getImage_b()).delete();//删除文件
		
		bookService.delete(bid);//删除数据库的记录
		
		req.setAttribute("msg", "删除图书成功！");
		return "f:/adminjsps/msg.jsp";
	}
	
	//修改图书
	public String edit(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map map = req.getParameterMap();
		Book book = CommonUtils.toBean(map, Book.class);
		Category category = CommonUtils.toBean(map, Category.class);
		book.setCid(category.getCid());
		
		bookService.edit(book);
		req.setAttribute("msg", "修改图书成功！");
		return "f:/adminjsps/msg.jsp";
	}
	//查找图书并显示
	public String load(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		String bid = req.getParameter("bid");//获取链接的参数bid
		System.out.println(bid);
		Book book = bookService.load(bid);//通过bid得到book对象
		req.setAttribute("parents", categoryService.findParents());
		String cid=book.getCid();
		String pid=categoryService.getpid(cid);
		req.setAttribute("children", categoryService.findChildren(pid));
		req.setAttribute("book", book);//保存到req中
		System.out.println(categoryService.findParents());
		return "f:/adminjsps/admin/book/desc.jsp";//转发到desc.jsp
	}
	//添加图书获取分类
	public String addPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<Category> parents = categoryService.findParents();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/add.jsp";
	}
	
	//异步获取子分类
	public void ajaxFindChildren(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pid = req.getParameter("pid");
		List<Category> children = categoryService.findChildren(pid);
	    ObjectMapper mapper=new ObjectMapper();
	    String strJson=mapper.writeValueAsString(children);
	    System.out.println(strJson);
	    resp.getWriter().print(strJson);
	}
	
	//获取左边的分类
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		List<Category> parents = categoryService.findAll();
		req.setAttribute("parents", parents);
		return "f:/adminjsps/admin/book/left.jsp";
	}
	
	//获取左边分类的书信息
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
		return "f:/adminjsps/admin/book/list.jsp";
	}

	//查找图书
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
