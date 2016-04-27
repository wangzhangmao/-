package cn.itcast.goods.user.web.servlet;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.user.domain.User;
import cn.itcast.goods.user.service.UserService;
import cn.itcast.servlet.BaseServlet;

/**
 * 用户模块WEB层
 * 
 * @author qdmmy6
 * 
 */
public class UserServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserService userService = new UserService();

	// 修改密码
	public String updatePassword(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {

		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		Map<String, String> errors = validateUpdatePwd(formUser,
				req.getSession());
		if (errors.size() > 0) {
			req.setAttribute("form", formUser);
			req.setAttribute("errors", errors);
			return "f:/jsps/user/pwd.jsp";
		}
		User user = (User) req.getSession().getAttribute("sessionUser");
		// 如果用户没有登录，返回到登录页面，显示错误信息
		if (user == null) {
			req.setAttribute("msg", "您还没有登录！");
			return "f:/jsps/user/login.jsp";
		}

		try {
			userService.updatePassword(user.getUid(), formUser.getNewpass(),
					formUser.getLoginpass());
			req.setAttribute("msg", "修改密码成功");
			req.setAttribute("code", "success");
			return "f:/jsps/msg.jsp";
		} catch (Exception e) {
			req.setAttribute("msg", e.getMessage());// 保存异常信息到request
			req.setAttribute("user", formUser);// 为了回显
			return "f:/jsps/user/pwd.jsp";
		}
	}

	private Map<String, String> validateUpdatePwd(User formUser,
			HttpSession session) {
		Map<String, String> errors = new HashMap<String, String>();
		String oldpwd = formUser.getLoginpass();
		String newpwd = formUser.getNewpass();
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if (oldpwd == null || oldpwd.trim().isEmpty()) {
			errors.put("oldpwd", "密码不能为空！");
		}else if (oldpwd.length() < 3 || oldpwd.length() > 20) {
			errors.put("oldpwd", "密码长度必须在3~20之间！");
		}
		if (newpwd == null || newpwd.trim().isEmpty()) {
			errors.put("newpwd", "新密码不能为空！");
		} else if (newpwd.length() < 3 || newpwd.length() > 20) {
			errors.put("newpwd", "新密码长度必须在3~20之间！");
		}
		if (verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if (!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "验证码错误！");
		}
		return errors;
	}

	// 退出登录
	public String quit(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {
		req.getSession().invalidate();
		return "r:/jsps/main.jsp";
	}

	public String login(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);

		Map<String, String> errors = validateLogin(formUser, req.getSession());
		if (errors.size() > 0) {
			req.setAttribute("form", formUser);
			req.setAttribute("errors", errors);
			return "f:/jsps/user/login.jsp";
		}

		User user = userService.login(formUser);
		if (user == null) {
			req.setAttribute("msg", "用户名或密码错误！");
			req.setAttribute("user", formUser);
			return "f:/jsps/user/login.jsp";
		} else {
			if (!user.isStatus()) {
				req.setAttribute("msg", "您还没有激活！");
				req.setAttribute("user", formUser);
				return "f:/jsps/user/login.jsp";
			} else {
				// 保存用户到session
				req.getSession().setAttribute("sessionUser", user);
				// 获取用户名保存到cookie中
				String loginname = user.getLoginname();
				loginname = URLEncoder.encode(loginname, "utf-8");
				Cookie cookie = new Cookie("loginname", loginname);
				cookie.setMaxAge(60 * 60 * 24 * 10);// 保存10天
				resp.addCookie(cookie);
				return "r:/index.jsp";// 重定向到主页
			}
		}
	}

	private Map<String, String> validateLogin(User formUser, HttpSession session) {
		Map<String, String> errors = new HashMap<String, String>();
		String loginname = formUser.getLoginname();
		String loginpass = formUser.getLoginpass();
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if (loginname == null || loginname.trim().isEmpty()) {
			errors.put("loginname", "用户名不能为空！");
		}
		if (loginpass == null || loginpass.trim().isEmpty()) {
			errors.put("loginpass", "密码不能为空！");
		} else if (loginpass.length() < 3 || loginpass.length() > 20) {
			errors.put("loginpass", "密码长度必须在3~20之间！");
		}
		if (verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if (!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "验证码错误！");
		}
		return errors;
	}

	/**
	 * 注册功能
	 */
	public String regist(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		User formUser = CommonUtils.toBean(req.getParameterMap(), User.class);
		Map<String, String> errors = validateRegist(formUser, req.getSession());
		if (errors.size() > 0) {
			req.setAttribute("form", formUser);
			req.setAttribute("errors", errors);
			return "f:/jsps/user/regist.jsp";
		}
		userService.regist(formUser);
		req.setAttribute("code", "success");
		req.setAttribute("msg", "注册功能，请马上到邮箱激活！");
		return "f:/jsps/msg.jsp";
	}

	private Map<String, String> validateRegist(User formUser,
			HttpSession session) {
		Map<String, String> errors = new HashMap<String, String>();

		String loginname = formUser.getLoginname();
		if (loginname == null || loginname.trim().isEmpty()) {
			errors.put("loginname", "用户名不能为空！");
		} else if (loginname.length() < 3 || loginname.length() > 20) {
			errors.put("loginname", "用户名长度必须在3~20之间！");
		} else if (!userService.validateName(loginname)) {
			errors.put("loginname", "用户名已被注册！");
		}
		String loginpass = formUser.getLoginpass();
		if (loginpass == null || loginpass.trim().isEmpty()) {
			errors.put("loginpass", "密码不能为空！");
		} else if (loginpass.length() < 3 || loginpass.length() > 20) {
			errors.put("loginpass", "密码长度必须在3~20之间！");
		}
		String reloginpass = formUser.getReloginpass();
		if (reloginpass == null || reloginpass.trim().isEmpty()) {
			errors.put("reloginpass", "确认密码不能为空！");
		} else if (!reloginpass.equals(loginpass)) {
			errors.put("reloginpass", "两次输入不一致！");
		}
		String email = formUser.getEmail();
		if (email == null || email.trim().isEmpty()) {
			errors.put("email", "Email不能为空！");
		} else if (!email
				.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
			errors.put("email", "Email格式错误！");
		} else if (!userService.validateEmail(email)) {
			errors.put("email", "Email已被注册！");
		}
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if (verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if (!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "验证码错误！");
		}

		return errors;
	}

	/**
	 * ajax用户名是否注册校验
	 */
	public String ajaxValidateLoginname(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		/*
		 * 1. 获取用户名
		 */
		System.out.println("1111");
		String loginname = req.getParameter("loginname");
		/*
		 * 2. 通过service得到校验结果
		 */
		System.out.println(loginname);
		boolean b = userService.validateName(loginname);
		/*
		 * 3. 发给客户端
		 */
		System.out.println(b);
		resp.getWriter().print(b);
		return null;
	}

	/**
	 * ajax Email是否注册校验
	 */
	public String ajaxValidateEmail(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		/*
		 * 1. 获取Email
		 */
		String email = req.getParameter("email");
		/*
		 * 2. 通过service得到校验结果
		 */
		boolean b = userService.validateEmail(email);
		/*
		 * 3. 发给客户端
		 */
		resp.getWriter().print(b);
		return null;
	}

	/**
	 * ajax验证码是否正确校验
	 */
	public String ajaxValidateVerifyCode(HttpServletRequest req,
			HttpServletResponse resp) throws Exception {
		/*
		 * 1. 获取输入框中的验证码
		 */
		String verifyCode = req.getParameter("verifyCode");
		/*
		 * 2. 获取图片上真实的校验码
		 */
		String vcode = (String) req.getSession().getAttribute("vCode");
		/*
		 * 3. 进行忽略大小写比较，得到结果
		 */
		boolean b = verifyCode.equalsIgnoreCase(vcode);
		/*
		 * 4. 发送给客户端
		 */
		resp.getWriter().print(b);
		return null;
	}
}
