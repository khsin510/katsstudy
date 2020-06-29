<%@page import="java.sql.Statement"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.DriverManager"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
    String driverName="com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://127.0.0.1:3306/sftk_office?useUnicode=true&characterEncoding=UTF8";
    String id = "root";
    String pwd ="admin";
 
    request.setCharacterEncoding("utf-8");
   
    Class.forName(driverName);
    Connection conn = DriverManager.getConnection(url,id,pwd);
    PreparedStatement pstmt;
	ResultSet rs;
    
			String SQL = "SELECT userName from member where 1=1 and userID='111-11-11111'";
			try {
				pstmt = conn.prepareStatement(SQL);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					System.out.println(rs.getString(1));
					out.println(rs.getString(1));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
%>
</body>
</html>