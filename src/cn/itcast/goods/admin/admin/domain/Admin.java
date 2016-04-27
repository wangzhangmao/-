package cn.itcast.goods.admin.admin.domain;

public class Admin {
	private String adminId;//主键
	private String adminName;//管理员的登录名
	private String adminPassword;//管理员的登录密码
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getAdminName() {
		return adminName;
	}
	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}
	public String getAdminPassword() {
		return adminPassword;
	}
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	@Override
	public String toString() {
		return "Admin [adminId=" + adminId + ", adminName=" + adminName
				+ ", adminPassword=" + adminPassword + "]";
	}
	public Admin(String adminName, String adminPassword) {
		super();
		this.adminName = adminName;
		this.adminPassword = adminPassword;
	}
	public Admin() {
		super();
	}
	
	
}
