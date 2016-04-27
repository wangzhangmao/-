<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>My JSP 'body.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

</head>

<body>
	<center>
		<h1 align="center">公告管理</h1>
		<p>
			<a href="<c:url value='/adminjsps/admin/Announcement/add.jsp'/>"
				style="margin: 20px; font-size: 20px;">添加公告</a>
		</p>
		<table>
			<c:if test="${!empty announcements }">
				<c:forEach items="${announcements.announcements }"
					var="announcement">
					<tr>
						<td align="center" style="color: blue" width="90%">${announcement.announcementTest }</td>
						<td align="left" width="50px"><a href="AdminAnnouncementServlet?method=delete&announcementId=${announcement.announcementId }">删除</a></td>
					</tr>
				</c:forEach>
			</c:if>
		</table>

	</center>
</body>
</html>
