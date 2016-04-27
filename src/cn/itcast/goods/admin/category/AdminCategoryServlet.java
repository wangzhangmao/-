package cn.itcast.goods.admin.category;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.book.service.BookService;
import cn.itcast.goods.category.daomain.Category;
import cn.itcast.goods.category.service.CategoryService;
import cn.itcast.servlet.BaseServlet;

public class AdminCategoryServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CategoryService categoryService = new CategoryService();
	private BookService bookService = new BookService();

	//删除二级分类
	public String deleteChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		Long cnt = bookService.findBookCountByCategory(cid);
		if(cnt > 0) {
			req.setAttribute("msg", "该分类下还存在图书，不能删除！");
			return "f:/adminjsps/msg.jsp";
		} else {
			categoryService.delete(cid);
			return findAll(req, resp);
		}
	}
	
	// 修改二级分类：第一步
	public String editChildPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String cid = req.getParameter("cid");
		Category child = categoryService.load(cid);
		req.setAttribute("child", child);
		req.setAttribute("parents", categoryService.findParents());

		return "f:/adminjsps/admin/category/edit2.jsp";
	}

	// 修改二级分类：第二步
	public String editChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String cname = req.getParameter("cname");
		String desc = req.getParameter("desc");
		String cid = req.getParameter("cid");
		String pid = req.getParameter("pid");
		Category child = new Category(cname, desc);
		Category parent = new Category();
		child.setCid(cid);
		parent.setCid(pid);
		child.setParent(parent);

		categoryService.edit(child);
		return findAll(req, resp);
	}

	// 删除一级分类
	public String deleteParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String cid = req.getParameter("cid");
		int cnt = categoryService.findChildrenCountByParent(cid);
		if (cnt > 0) {
			req.setAttribute("msg", "该分类下还有子分类，不能删除！");
			return "f:/adminjsps/msg.jsp";
		} else {
			categoryService.delete(cid);
			return findAll(req, resp);
		}
	}

	// 获取指定要修改的一级分类
	public String editParentPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cid = req.getParameter("cid");
		Category parent = categoryService.load(cid);
		req.setAttribute("parent", parent);
		return "f:/adminjsps/admin/category/edit.jsp";
	}

	// 修改一级分类：第二步

	public String editParent(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cname = req.getParameter("cname");
		String desc = req.getParameter("desc");
		String cid = req.getParameter("cid");
		Category parent =new Category(cname, desc);
		parent.setCid(cid);
		categoryService.edit(parent);
		return findAll(req, resp);
	}

	// 添加一级目录
	public String addChild(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cname = req.getParameter("cname");
		String desc = req.getParameter("desc");

		Category child = new Category(cname, desc);
		child.setCid(CommonUtils.uuid());// 设置cid
		Category parent = new Category();
		parent.setCid(req.getParameter("pid"));
		child.setParent(parent);
		categoryService.add(child);
		return "r:/admin/AdminCategoryServlet?method=findAll";
	}

	// 获取所有的一级目录
	public String addChildPre(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String pid = req.getParameter("pid");// 当前点击的父分类id
		List<Category> parents = categoryService.findParents();
		req.setAttribute("pid", pid);
		req.setAttribute("parents", parents);

		return "f:/adminjsps/admin/category/add2.jsp";
	}

	// 添加一级目录
	public String addOneLevel(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String cname = req.getParameter("cname");
		String desc = req.getParameter("desc");
		Category parent = new Category(cname, desc);
		parent.setCid(CommonUtils.uuid());// 设置cid
		categoryService.add(parent);
		return "r:/admin/AdminCategoryServlet?method=findAll";
	}

	// 查询所有分类
	public String findAll(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		for (Category ca : categoryService.findAll()) {
			System.out.println(ca);
		}
		req.setAttribute("parents", categoryService.findAll());
		return "f:/adminjsps/admin/category/list.jsp";
	}
}
