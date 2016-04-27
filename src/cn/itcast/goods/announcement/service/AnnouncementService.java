package cn.itcast.goods.announcement.service;

import java.sql.SQLException;

import cn.itcast.goods.announcement.dao.AnnouncementDao;
import cn.itcast.goods.announcement.daomain.Announcement;
import cn.itcast.goods.announcement.daomain.AnnouncementList;

public class AnnouncementService {

	private AnnouncementDao announcementDao=new AnnouncementDao();
	
	public Announcement findAnnouncement(){
		
		try {
			return announcementDao.findAnnouncementAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void addAnnouncement(AnnouncementList announcement) {
		// TODO Auto-generated method stub
		try {
			announcementDao.addAnnouncement(announcement);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void deleteAnnouncement(String announcementId) {
		// TODO Auto-generated method stub
		try {
			announcementDao.deleteAnnouncement(announcementId);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
