package cn.itcast.goods.admin.announcement;


import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.itcast.goods.announcement.daomain.Announcement;
import cn.itcast.goods.announcement.daomain.AnnouncementList;
import cn.itcast.goods.announcement.service.AnnouncementService;
import cn.itcast.servlet.BaseServlet;

public class AdminAnnouncementServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AnnouncementService announcementService=new AnnouncementService();
	
	public String findAnnouncement(HttpServletRequest req,HttpServletResponse resp){
		Announcement a=announcementService.findAnnouncement();
		req.setAttribute("announcements", a);
		System.out.println(a);
		return "/adminjsps/admin/Announcement/list.jsp";
	}
	
	public String addAnnouncement(HttpServletRequest req,HttpServletResponse resp){
		String announcementTest=req.getParameter("announcementTest");
		AnnouncementList announcement=new AnnouncementList();
		announcement.setAnnouncementTest(announcementTest);
		announcement.setTime(new Date());
		announcementService.addAnnouncement(announcement);
		return findAnnouncement(req,resp);
	}
	
	public String delete(HttpServletRequest req,HttpServletResponse resp){
		String announcementId=req.getParameter("announcementId");
		announcementService.deleteAnnouncement(announcementId);
		return findAnnouncement(req,resp);
	}
}
