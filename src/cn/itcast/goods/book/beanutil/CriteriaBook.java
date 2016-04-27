package cn.itcast.goods.book.beanutil;

//查询条件类
public class CriteriaBook {

	private int pageNo;
	private String category;
	private String bname;
	private String author;
	private String press;
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public String getCategory() {
		if (category == null) {
			category = "";
		} else {
			category = "%" + category + "%";
		}
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getBname() {
		if (bname == null) {
			bname = "";
		} else {
			bname = "%" + bname + "%";
		}
		return bname;
	}
	public void setBname(String bname) {
		this.bname = bname;
	}
	public String getAuthor() {
		if (author == null) {
			author = "";
		} else {
			author = "%" + author + "%";
		}
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPress() {
		if (press == null) {
			press = "";
		} else {
			press = "%" + press + "%";
		}
		return press;
	}
	public void setPress(String press) {
		this.press = press;
	}
	@Override
	public String toString() {
		return "CriteriaBook [pageNo=" + pageNo + ", category=" + category
				+ ", bname=" + bname + ", author=" + author + ", press="
				+ press + "]";
	}
	public CriteriaBook(int pageNo, String category, String bname,
			String author, String press) {
		super();
		this.pageNo = pageNo;
		this.category = category;
		this.bname = bname;
		this.author = author;
		this.press = press;
	}

}
