package cn.itcast.goods.announcement.daomain;

import java.util.Date;
import java.util.List;

public class Announcement {
	

	private List<AnnouncementList> announcements;
	
	
	public List<AnnouncementList> getAnnouncements() {
		return announcements;
	}


	public void setAnnouncements(List<AnnouncementList> announcements) {
		this.announcements = announcements;
	}


	@Override
	public String toString() {
		return "Announcement [announcements=" + announcements + "]";
	}
	
	

}
