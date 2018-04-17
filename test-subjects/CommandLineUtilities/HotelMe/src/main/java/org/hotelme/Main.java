package org.hotelme;

//import java.util.ArrayList;
import java.sql.*;

/**
 * Run Reservation System.
 * 
 * @author David Nakonechnyy
 *
 */
public class Main {
	// JDBC driver name and database URL
	public static final String MYSQL_URL = "jdbc:mysql://localhost:3306/";
	//
	public static final String DB_URL = "jdbc:mysql://localhost:3306/hotelme";
	public static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";

	// Database credentials
	public static final String USER = "hotelmanager";
	public static final String PASS = "qweasd";

	public static void main(String[] args) {
		Connection conn = null;
		try {
			System.out.println("Connecting...");
			conn = DriverManager.getConnection(DB_URL, USER, PASS);

			HotelModel model = new HotelModel(conn);
			HotelView view = new HotelView();

			HotelController hotel = new HotelController(model, view);
			// TODO DISABLED
			hotel.start();
		} catch (SQLException e) {
			System.out.println("========== Error: " + e + " ==========");
			System.out.println("Couldn't establish connection to the database.");
		}
//		System.exit( 0 );
	}

}
