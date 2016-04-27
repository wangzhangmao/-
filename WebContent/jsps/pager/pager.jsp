<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
	function _go() {
		var pc = $("#pageCode").val();//获取文本框中的当前页码
		if(!/^[1-9]\d*$/.test(pc)) {//对当前页码进行整数校验
			alert('请输入正确的页码！');
			return;
		}
		if(pc > 10) {//判断当前页码是否大于最大页
			alert('请输入正确的页码！');
			return;
		}
		location = "";
	}
</script>


<div class="divBody">
  <div class="divContent">
    <br> 共 ${bookpage.totalPageNumber } 页 &nbsp;&nbsp; 当前第
		${bookpage.pageNo } 页 &nbsp;&nbsp;

		<c:if test="${bookpage.hasPrev }">
			<a
				href="BookServlet?action=listBook&pageNo=1&minPrice=${criteriaBook.minPrice }&maxPrice=${criteriaBook.maxPrice }">首页</a>
			&nbsp;&nbsp;
			<a
				href="BookServlet?action=listBook&pageNo=${bookpage.prevPage }&minPrice=${criteriaBook.minPrice }&maxPrice=${criteriaBook.maxPrice }">上一页</a>
		</c:if>

		&nbsp;&nbsp;

		<c:if test="${bookpage.hasNext }">
			<a
				href="BookServlet?action=listBook&pageNo=${bookpage.nextPage }&minPrice=${criteriaBook.minPrice }&maxPrice=${criteriaBook.maxPrice }">下一页</a>
			&nbsp;&nbsp;
			<a
				href="BookServlet?action=listBook&pageNo=${bookpage.totalPageNumber }&minPrice=${criteriaBook.minPrice }&maxPrice=${criteriaBook.maxPrice }">末页</a>
		</c:if>

		&nbsp;&nbsp; 转到 <input type="text" size="1" id="pageNo" /> 页
  </div>
</div>