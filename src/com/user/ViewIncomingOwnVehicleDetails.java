package com.user;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.connection.DatabaseConnection;

/**
 * Servlet implementation class ViewIncomingOwnVehicleDetails
 */
@WebServlet("/ViewIncomingOwnVehicleDetails")
public class ViewIncomingOwnVehicleDetails extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String category = request.getParameter("category");
		String intime = request.getParameter("intime");
		String remark = request.getParameter("remark");
		String status = request.getParameter("status");
		int parkingcharge = 0;
		Date d1 = null;
		Date d2 = null;
		long differenceHours = 0;
		HttpSession session = request.getSession();
		System.out.println("category  " + category);
		// HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			d1 = format.parse(intime);
			d2 = format.parse(format.format(new Date()));
			long timeDifference = d2.getTime() - d1.getTime();
			differenceHours = timeDifference / (60 * 60 * 1000) % 24;
			long diffDays = timeDifference / (24 * 60 * 60 * 1000);
			differenceHours = differenceHours + (diffDays * 24);
			
			System.out.println("Hours " + differenceHours);

			if ("Six Wheeler Vehicles".equals(category) && differenceHours < 8) {
				parkingcharge = 40;
			} else if ("Six Wheeler Vehicles".equals(category) && differenceHours > 8 && differenceHours < 16) {
				parkingcharge = 60;
			} else if ("Six Wheeler Vehicles".equals(category) && differenceHours > 16 && differenceHours < 24) {
				parkingcharge = 100;
			} else if ("Six Wheeler Vehicles".equals(category) && differenceHours > 24) {
				parkingcharge = 150;
			} else if ("Four Wheeler Vehicle".equals(category) && differenceHours < 8) {
				parkingcharge = 30;
			} else if ("Four Wheeler Vehicle".equals(category) && differenceHours > 8 && differenceHours < 16) {
				parkingcharge = 50;
			} else if ("Four Wheeler Vehicle".equals(category) && differenceHours > 16 && differenceHours < 24) {
				parkingcharge = 80;
			} else if ("Four Wheeler Vehicle".equals(category) && differenceHours > 24) {
				parkingcharge = 120;
			} else if ("Two Wheeler Vehicle".equals(category) && differenceHours < 8) {
				parkingcharge = 15;
			} else if ("Two Wheeler Vehicle".equals(category) && differenceHours > 8 && differenceHours < 16) {
				parkingcharge = 20;
			} else if ("Two Wheeler Vehicle".equals(category) && differenceHours > 16 && differenceHours < 24) {
				parkingcharge = 25;
			} else if ("Two Wheeler Vehicle".equals(category) && differenceHours > 24) {
				parkingcharge = 45;
			} else if ("Bicycles".equals(category) && differenceHours < 8) {
				parkingcharge = 2;
			} else if ("Bicycles".equals(category) && differenceHours > 8 && differenceHours < 16) {
				parkingcharge = 5;
			} else if ("Bicycles".equals(category) && differenceHours > 16 && differenceHours < 24) {
				parkingcharge = 10;
			} else if ("Bicycles".equals(category) && differenceHours > 24) {
				parkingcharge = 12;
			} else {
				System.out.println("Bye");
			}

			Connection connection = DatabaseConnection.getConnection();
			Statement statement = connection.createStatement();
			SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
            Date now = new Date();
            String strDate = ff.format(now);
            System.out.println("INTIME : "+strDate); 
			int updateinfo = statement.executeUpdate(
					"update  tblvehicle set OutTime='" + strDate +"',Remark='" + remark + "',Status='" + status + "',ParkingCharge='"
							+ parkingcharge + "' where ownername='" + session.getAttribute("fullName") + "' and vehiclecategory='"+category+"'");
			if (updateinfo > 0) {
				response.sendRedirect("user-managingvehicle.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
