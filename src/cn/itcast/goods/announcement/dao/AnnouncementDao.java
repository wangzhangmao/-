package cn.itcast.goods.announcement.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import cn.itcast.goods.announcement.daomain.Announcement;
import cn.itcast.goods.announcement.daomain.AnnouncementList;
import cn.itcast.jdbc.TxQueryRunner;

public class AnnouncementDao {

	private QueryRunner qr=new TxQueryRunner();
	
	public Announcement findAnnouncementAll() throws SQLException{
		Announcement a=new Announcement();
		a.setAnnouncements(findAnnouncementList());
		return a;
	}
	
	private List<AnnouncementList> findAnnouncementList() throws SQLException {
		String sql="select * from t_announcement";
		return qr.query(sql, new BeanListHandler<AnnouncementList>(AnnouncementList.class));
	}

	public void addAnnouncement(AnnouncementList announcement) throws SQLException {
		// TODO Auto-generated method stub
		String sql="insert into t_announcement(announcementTest,time) values(?,?)";
		qr.update(sql, announcement.getAnnouncementTest(),announcement.getTime());
	}

	public void deleteAnnouncement(String announcementId) throws SQLException {
		// TODO Auto-generated method stub
		String sql="delete from t_announcement where announcementId=?";
		qr.update(sql, announcementId);
	}
}
