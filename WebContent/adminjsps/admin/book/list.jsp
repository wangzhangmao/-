<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>图书列表</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<meta http-equiv="content-type" content="text/html;charset=utf-8">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<link rel="stylesheet" type="text/css"
	href="<c:url value='/jsps/css/book/list.css'/>">
<link rel="stylesheet" type="text/css"
	href="<c:url value='/jsps/pager/pager.css'/>" />
<script type="text/javascript"
	src="<c:url value='/jsps/pager/pager.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<script type="text/javascript"
	src="<c:url value='/jsps/js/book/list.js'/>"></script>
<script type="text/javascript">
	$(function() {
		$("#pageNo")
				.change(
						function() {
							var val = $(this).val();
							val = $.trim(val);

							//1. 校验 val 是否为数字 1, 2, 而不是 a12, b
							var flag = false;
							var reg = /^\d+$/g;
							var pageNo = 0;

							if (reg.test(val)) {
								//2. 校验 val 在一个合法的范围内： 1-totalPageNumber
								pageNo = parseInt(val);
								if (pageNo >= 1
										&& pageNo <= parseInt("${books.totalPageNumber }")) {
									flag = true;
								}
							}

							if (!flag) {
								alert("输入的不是合法的页码.");
								$(this).val("");
								return;
							}

							//3. 页面跳转
							var href = "/goods/admin/AdminBookServlet?method=findBook&pageNo="
									+ pageNo
									+ "&"
									+ "category=${param.category}&bname=${param.bname}&press=${param.press}&author=${param.author}";
							window.location.href = href;
						});
	})
</script>
</head>
<body>

	<ul>
		<c:forEach items="${books.list }" var="book">
			<li>
				<div class="inner">
					<a class="pic"
						href="<c:url value='/admin/AdminBookServlet?method=load&bid=${book.bid }'/>"><img
						src="<c:url value='/${book.image_b }'/>" border="0" /> </a>
					<p class="price">
						<span class="price_n">&yen;${book.currPrice }</span> <span
							class="price_r">&yen;${book.price }</span> (<span class="price_s">${book.discount
							}折</span>)
					</p>
					<p>
						<a id="bookname" title="${book.bname }"
							href="<c:url value='/admin/AdminBookServlet?method=load&bid=${book.bid }'/>">${book.bname
							}</a>
					</p>
					<%-- url标签会自动对参数进行url编码 --%>
					<c:url value="/BookServlet" var="authorUrl">
						<c:param name="method" value="findByAuthor" />
						<c:param name="author" value="${book.author }" />
					</c:url>
					<c:url value="/BookServlet" var="pressUrl">
						<c:param name="method" value="findByPress" />
						<c:param name="press" value="${book.press }" />
					</c:url>
					<p>
						<a href="${authorUrl }" name='P_zz' title='${book.author }'>${book.author
							}</a>
					</p>
					<p class="publishing">
						<span>出 版 社：</span><a href="${pressUrl }">${book.press }</a>
					</p>
					<p class="publishing_time">
						<span>出版时间：</span>${book.publishtime }
					</p>
				</div>
			</li>
		</c:forEach>

	</ul>
	<div style="float:left; width: 100%; text-align: center;">
		<hr />
		<br /> <br> 共 ${books.totalPageNumber } 页 &nbsp;&nbsp; 当前第
		${books.pageNo } 页 &nbsp;&nbsp;

		<c:if test="${books.hasPrev }">
			<a
				href="/goods/admin/AdminBookServlet?method=findBook&pageNo=1&category=${param.category}&bname=${param.bname}&press=${param.press}&author=${param.author}">首页</a>
			&nbsp;&nbsp;
			<a
				href="/goods/admin/AdminBookServlet?method=findBook&pageNo=${books.prevPage }&category=${param.category}&bname=${param.bname}&press=${param.press}&author=${param.author}">上一页</a>
		</c:if>

		&nbsp;&nbsp;

		<c:if test="${books.hasNext }">
			<a
				href="/goods/admin/AdminBookServlet?method=findBook&pageNo=${books.nextPage }&category=${param.category}&bname=${param.bname}&press=${param.press}&author=${param.author}">下一页</a>
			&nbsp;&nbsp;
			<a
				href="/goods/admin/AdminBookServlet?method=findBook&pageNo=${books.totalPageNumber }&category=${param.category}&bname=${param.bname}&press=${param.press}&author=${param.author}">末页</a>
		</c:if>

		&nbsp;&nbsp; 转到 <input type="text" size="1" id="pageNo" /> 页
	</div>

</body>

</html>

