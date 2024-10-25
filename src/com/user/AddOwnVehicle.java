package com.user;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.connection.DatabaseConnection;
import com.mysql.cj.Session;

/**
 * Servlet implementation class AddOwnVehicle
 */
@WebServlet("/AddOwnVehicle")
public class AddOwnVehicle extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Random rand = new Random();
		int ParkingNumber = rand.nextInt(9000000) + 1000000;
		HttpSession hs = request.getSession();
		System.out.println("ParkingNumber        " + ParkingNumber);
		String catename = request.getParameter("catename");
	      String gatename = request.getParameter("gatename");

		
		String vehcomp = request.getParameter("vehcomp");
		String vehreno = request.getParameter("vehreno");
		String ownername = request.getParameter("ownername");
		String ownercontno = request.getParameter("ownercontno");
		int parking_seat=0;
		int addVehicle = 0;
		String status = "";
		int count = 0;
		try {
			Connection connection = DatabaseConnection.getConnection();
			Statement statement = connection.createStatement();
			ResultSet total_parking_seat_result= statement.executeQuery("select * from tblParkingSeatCapacity");
			if(total_parking_seat_result.next()){
				parking_seat = total_parking_seat_result.getInt(1);
			}
			ResultSet resultSet = statement.executeQuery("select count(*) from tblvehicle where status=''");
			if (resultSet.next()) {
				count = resultSet.getInt(1);
			}
			
			String query="insert into tblvehicle(ParkingNumber,VehicleCategory,VehicleCompanyname,RegistrationNumber,OwnerName,OwnerContactNumber,InTime,OutTime,ParkingCharge,Remark,status,gatename)values"
			        + "(?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement pst=connection.prepareStatement(query);
			
			
			if (parking_seat > count) {
			    
                /*
                 * addVehicle = pst.executeUpdate(
                 * "insert into tblvehicle(ParkingNumber,VehicleCategory,VehicleCompanyname,RegistrationNumber,OwnerName,OwnerContactNumber,InTime,OutTime,status)values('"
                 * + ParkingNumber + "','" + catename + "','" + vehcomp + "','" + vehreno +
                 * "','"
                 * + ownername + "','" + ownercontno + "','" +"'None' ,'None',"+ status + "')");
                 * 
                 */
			    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			    Date now = new Date();
			    String strDate = format.format(now);
			    System.out.println("INTIME : "+strDate);  
			    pst.setInt(1, ParkingNumber);
				pst.setString(2, catename);
				pst.setString(3, vehcomp);
				pst.setString(4, vehreno);
				pst.setString(5, ownername);
				pst.setString(6, ownercontno);
				pst.setString(7, strDate);
				pst.setString(8, "");
				pst.setDouble(9, 0.0);
				pst.setString(10, "");
				pst.setString(11,status);
				pst.setString(12, gatename);
				
				addVehicle = pst.executeUpdate();
			} else {
				String message="Parking slot is full, Wait for sometimes";
				hs.setAttribute("message", message);
				response.sendRedirect("add-user-vehicle.jsp");
			}
			if (addVehicle > 0) {
				response.sendRedirect("add-user-vehicle.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
