<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Insert title here</title>

</head>

<body>
<!-- 현재년도 -->
<c:set var="now" value="<%=new java.util.Date()%>" />
<c:set var="sysYear"><fmt:formatDate value="${now}" pattern="yyyy-mm-dd" /></c:set> 


<!-- 화면에 뿌릴때 -->

<c:out value="${sysYear}" />
         <h1>Hello World</h1>
<a href="bbs">게시판</a><p>
<a href="json">JSON</a><p>
<a href="json2">JSON2</a><p>
<a href="jsoup3">JSOUP3 (크롤링 정보 수집)</a><p>
<a href="jsoup4">JSOUP4 (크롤링 정보 수집)</a><p>
<a href="formFile">formFile</a><p>
</body>

</html>
<!-- <html><meta http-equiv="refresh" content="0; url=/test/test.jsp"></meta></html> -->