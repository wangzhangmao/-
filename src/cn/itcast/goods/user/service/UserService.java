package cn.itcast.goods.user.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;

import cn.itcast.commons.CommonUtils;
import cn.itcast.goods.user.dao.UserDao;
import cn.itcast.goods.user.domain.User;
import cn.itcast.goods.user.service.exception.UserException;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;

/**
 * 用户模块业务层
 * @author qdmmy6
 *
 */
public class UserService {
	private UserDao userDao = new UserDao();
	
	public boolean validateName(String name){
		try {
			return userDao.validateName(name);
		} catch (Exception e) {e.printStackTrace();}
		return false;
	}

	public boolean validateEmail(String email){
		try {
			return userDao.validateEmail(email);
		} catch (Exception e) {}
		return false;
	}

	public void regist(User user) {

		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActivationCode(CommonUtils.uuid() + CommonUtils.uuid());
		try {
			userDao.add(user);
		} catch (Exception e) {}
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		} catch (Exception e) {}
		/*
		 * 登录邮件服务器，得到session
		 */
		String host = prop.getProperty("host");//服务器主机名
		String name = prop.getProperty("username");//登录名
		String pass = prop.getProperty("password");//登录密码
		Session session = MailUtils.createSession(host, name, pass);
		
		/*
		 * 创建Mail对象
		 */
		String from = prop.getProperty("from");
		String to = user.getEmail();
		String subject = prop.getProperty("subject");
		// MessageForm.format方法会把第一个参数中的{0},使用第二个参数来替换。
		// 例如MessageFormat.format("你好{0}, 你{1}!", "张三", "去死吧"); 返回“你好张三，你去死吧！”
		String content = MessageFormat.format(prop.getProperty("content"), user.getActivationCode());
		Mail mail = new Mail(from, to, subject, content);
		try {
			MailUtils.send(session, mail);
		} catch (Exception e) {}
	}

	public User login(User user) {
			try {
				return userDao.findByLoginnameAndLoginpass(user.getLoginname(), user.getLoginpass());
			} catch (SQLException e) {}
			return null;
	}

	public void updatePassword(String uid, String newPass, String oldPass) throws UserException {

			try {
				boolean bool = userDao.findByUidAndPassword(uid, oldPass);
				if(!bool) {//如果老密码错误
					throw new UserException("旧密码错误！");
				}
				userDao.updatePassword(uid, newPass);
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
}