package cn.itcast.goods.announcement.daomain;

import java.util.Date;

public class AnnouncementList {

	private Integer announcementId;
	private String announcementTest;
	private Date time;

	public String getAnnouncementTest() {
		return announcementTest;
	}

	public void setAnnouncementTest(String announcementTest) {
		this.announcementTest = announcementTest;
	}

	public Integer getAnnouncementId() {
		return announcementId;
	}

	public void setAnnouncementId(Integer announcementId) {
		this.announcementId = announcementId;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "AnnouncementList [announcementId=" + announcementId
				+ ", announcementTest=" + announcementTest + ", time=" + time
				+ "]";
	}
	
	
	
}
