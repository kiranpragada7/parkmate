<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.connection.*"%>
<%@ page import="java.sql.*"%>
<%
	String catename = request.getParameter("catename");
	try {
		Connection connection = DatabaseConnection.getConnection();
		Statement statement = connection.createStatement();
		int updateinfo = statement.executeUpdate("update tblgate set name='" + catename + "' where id='"+session.getAttribute("id")+"'");
		if (updateinfo > 0) {
			response.sendRedirect("manage-gate.jsp");
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
%>