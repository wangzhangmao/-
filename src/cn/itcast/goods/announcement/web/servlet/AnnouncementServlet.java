package cn.itcast.goods.announcement.web.servlet;


import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.goods.announcement.daomain.Announcement;
import cn.itcast.goods.announcement.service.AnnouncementService;
import cn.itcast.servlet.BaseServlet;

public class AnnouncementServlet extends BaseServlet {

	private AnnouncementService announcementService=new AnnouncementService();
	
	public String findAnnouncement(HttpServletRequest req,HttpServletResponse resp){
		Announcement a=announcementService.findAnnouncement();
		req.setAttribute("announcements", a);
		System.out.println(a);
		return "/jsps/body.jsp";
	}
}
