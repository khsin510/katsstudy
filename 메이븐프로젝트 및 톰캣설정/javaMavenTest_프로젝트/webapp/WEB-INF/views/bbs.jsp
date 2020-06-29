<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Insert title here</title>

</head>

<body>

<!-- 화면에 뿌릴때 -->
         <h1>BBS</h1>
         <img src="${papgeContext.servletContext.contextPath }/images/123.jpg">
<table>
<tr>
<th>제목</th>
<th>사용자</th>
<th>등록일</th>
</tr>
<c:forEach items="${list}" var="result" varStatus="status">
<tr>
<td>${result.bbsTitle }</td>
<td>${result.userID }</td>
<td>${result.bbsDate }</td>
</tr>
</c:forEach>

</table>
</body>

</html>