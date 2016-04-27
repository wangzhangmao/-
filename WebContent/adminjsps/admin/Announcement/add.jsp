<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'add.jsp' starting page</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript"
	src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
<script type="text/javascript">
	function checkForm() {
		if (!$("#announcementTest").val()) {
			alert("公告内容不能为空！");
			return false;
		}
		return true;
	}
</script>

</head>

<body>
	<center>
		<form action="<c:url value='admin/AdminAnnouncementServlet'/>" method="post">
			<input type="hidden" name="method" value="addAnnouncement">
			<table>
				<tr>
					<td>公告内容：</td>
					<td><textarea rows="5" cols="50" name="announcementTest"
							id="announcementTest"></textarea></td>
				</tr>
				<tr>
					<td colspan="2" align="center"><input type="submit" value="提交"></td>
				</tr>
				
			</table>
		</form>

	</center>
</body>
</html>
