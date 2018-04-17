package org.hotelme;
import java.sql.*;
import java.util.ArrayList;

/**
 * Hotel Model which will have all SQL functions
 * @author David Nakonechnyy
 *
 */
public class HotelModel {
	// Do not serialize this
	private transient Connection conn;
	private transient PreparedStatement statement;
	private transient ResultSet resultSet;
	
	private User user;
	private Room room;
	private ArrayList<Room> rooms = new ArrayList<Room>();
	private boolean userNotFound = true;
	private boolean invalidPW = true;
	
	/**
	 * Constructs the hotel model with a connection to the database
	 * @param conn Connection to the database
	 */
	public HotelModel(Connection conn)
	{
		this.conn = conn;
		statement = null;
		resultSet = null;
	}
	
	/**
	 * Will try to login the user
	 * @param username username
	 * @param password password
	 * @return if the user successfully logged in
	 */
	public boolean userLogin(String username, String password){
		userNotFound = true;
		invalidPW = true;
		try{
			statement = conn.prepareStatement("SELECT * FROM users WHERE uname = ?");
			statement.setString(1, username);
			resultSet = statement.executeQuery();
			if(resultSet.next())
				userNotFound = false;
			else
				return false;
			if(resultSet.getString("pw").equals(password))
				invalidPW = false;
			else
				return false;
			user = new User(resultSet.getInt("userID"), resultSet.getString("fname"), resultSet.getString("lname"), resultSet.getString("uname"), resultSet.getString("pw"));
		} catch(Exception e){
			System.out.println("======================== Error: " + e + " =================================");
		}
		return true;
	}
	
	/**
	 * Will try to sign up a user.
	 * @param fname first name
	 * @param lname last name
	 * @param uname user name
	 * @param password password
	 * @return if the user successfully signed up
	 */
	public boolean userSignUp(String fname, String lname, String uname, String password){
		try{
			statement = conn.prepareStatement("SELECT * FROM users WHERE uname = ?");
			statement.setString(1, uname);
			resultSet = statement.executeQuery();
			if(resultSet.next())
				return false;
			else{
				statement = conn.prepareStatement("INSERT INTO users (userID, fname, lname, uname, pw) VALUES(0, ?, ?, ?, ?)");
				statement.setString(1, fname);
				statement.setString(2, lname);
				statement.setString(3, uname);
				statement.setString(4, password);
				statement.executeUpdate();
				
				statement = conn.prepareStatement("SELECT * FROM users WHERE uname = ?");
				statement.setString(1, uname);
				resultSet = statement.executeQuery();
				
				if(resultSet.next()){
					user = new User(resultSet.getInt("userID"), resultSet.getString("fname"), resultSet.getString("lname"), resultSet.getString("uname"), resultSet.getString("pw"));
				}
				else
					System.out.println("===================== Error: User didn't sign up properly ==============================");
				
			}
			
		} catch(Exception e){
			System.out.println("======================== Error: " + e + " =================================");
		}
		return true;
	}
	
	/**
	 * Will check if the user has any reserved rooms and return the list to him.
	 * @param user current user
	 * @return if the rooms exists
	 */
	public boolean checkReservedRooms(){
		//Clear rooms list
		rooms.clear();
		try{
			statement = conn.prepareStatement("SELECT * FROM rooms LEFT OUTER JOIN reservations ON rooms.roomID = reservations.roomID WHERE reserveID IS NOT NULL AND userID = ?");
			statement.setInt(1, user.getUserID());
			resultSet = statement.executeQuery();
			if(resultSet.next()){
				do {
					int maxOcc = resultSet.getInt("maxOccupancy");
					int roomNum = resultSet.getInt("roomID");
					String roomType = resultSet.getString("roomType");
					int adult = resultSet.getInt("totalAdults");
					int child = resultSet.getInt("totalChildren");
					Date checkIn = resultSet.getDate("checkIn");
					Date checkOut = resultSet.getDate("checkOut");
					float price = resultSet.getFloat("price");
					int reserveID = resultSet.getInt("reserveID");
					room = new Room(reserveID, roomNum, roomType, maxOcc, adult, child, user.getUserID(), checkIn, checkOut, price);
					rooms.add(room);
				} while(resultSet.next());
			} else
				return false;
		} catch (Exception e){
			System.out.println("======================== Error: " + e + " =================================");
		}
		return true;
	}
	
	/**
	 * Will return how many rooms are available of a specific room type
	 * @param checkIn date user will check in on
	 * @param checkOut date user will check out on
	 * @param roomType type of room the user wants
	 * @return the number of rooms available for the specific room type.
	 */
	public int checkRoomsAvailable(Date checkIn, Date checkOut, String roomType){
		rooms.clear();
		int count = 0;
		try{
			statement = conn.prepareStatement("SELECT * FROM rooms WHERE roomType = ? AND roomID NOT IN (SELECT roomID FROM reservations WHERE ? >= checkIn OR ? > checkIn)");
			statement.setString(1, roomType);
			statement.setDate(2, checkIn);
			statement.setDate(3, checkOut);
			resultSet = statement.executeQuery();
			if(resultSet.next()){
				int roomNum = resultSet.getInt("roomID");
				int adult = 0;
				int child = 0;
				float price = resultSet.getInt("price");
				int maxOcc = resultSet.getInt("maxOccupancy");
				//Template room to compare for inputing fields
				room = new Room(0, roomNum, roomType, maxOcc, adult, child, user.getUserID(), checkIn, checkOut, price);
				rooms.add(room);
				do {
					count += 1;
				} while(resultSet.next());
			}
		} catch (Exception e){
			System.out.println("======================== Error: " + e + " =================================");
		}
		return count;
	}
	
	/**
	 * Create a reservation for the user.
	 * @param roomID room number
	 * @param totalAdults total adults staying in the room
	 * @param roomType type of room
	 * @param totalChildren total children staying in the room.
	 * @param roomOwner user ID
	 * @param checkIn check in date for the user
	 * @param checkOut check out date for the user
	 * @return if the user successfully was registered
	 */
	public boolean reserveRoom(int roomID, int totalAdults, String roomType, int totalChildren, int roomOwner, Date checkIn, Date checkOut){
		try{
			
			statement = conn.prepareStatement("INSERT INTO reservations(userID, roomID, totalAdults, totalChildren, checkIn, checkOut) VALUES (?, ?, ?, ?, ? ,?)");
			statement.setInt(1, roomOwner);
			statement.setInt(2, roomID);
			statement.setInt(3, totalAdults);
			statement.setInt(4, totalChildren);
			statement.setDate(5, checkIn);
			statement.setDate(6, checkOut);
			statement.executeUpdate();
			
			statement = conn.prepareStatement("SELECT * FROM reservations WHERE roomID = ? and userID = ?");
			statement.setInt(1, roomID);
			statement.setInt(2, roomOwner);
			resultSet = statement.executeQuery();
			if(resultSet.next())
				return true;
		} catch (Exception e){
			System.out.println("========== Error: " + e + " ==========");
		}
		return false;
	}
	
	/**
	 * Update the guests for a particular reservation.
	 * @param roomID Room number
	 * @param totalAdults number of adults in the room
	 * @param totalChildren number of children in the room
	 * @return if the room was updated properly
	 */
	public boolean updateGuests(int reserveID, int totalAdults, int totalChildren){
		try{
			statement = conn.prepareStatement("UPDATE reservations SET totalAdults = ? , totalChildren = ? WHERE reserveID = ?");
			statement.setInt(1, totalAdults);
			statement.setInt(2, totalChildren);
			statement.setInt(3, reserveID);
			statement.executeUpdate();
			
			statement = conn.prepareStatement("SELECT * FROM reservations WHERE reserveID = ?");
			statement.setInt(1, reserveID);
			resultSet = statement.executeQuery();
			if(resultSet.next())
				return true;
		} catch (Exception e){
			System.out.println("========== Error: " + e + " ==========");
		}
		return false;
	}
	
	/**
	 * Delete a reservation.
	 * @param reserveID reservation Id
	 * @return if reservation was deleted properly
	 */
	public boolean deleteReservation(int reserveID){
		try{
			statement = conn.prepareStatement("DELETE FROM reservations WHERE reserveID = ?");
			statement.setInt(1, reserveID);
			statement.executeUpdate();
			
			statement = conn.prepareStatement("SELECT * FROM reservations WHERE reserveID = ?");
			statement.setInt(1, reserveID);
			resultSet = statement.executeQuery();
			if(!resultSet.next())
				return true;
			
		} catch (Exception e){
			System.out.println("========== Error: " + e + " ==========");
		}
		return false;
	}
	
	/**
	 * Will upgrade a users room to the new room selected.
	 * @param reserveID reservation ID
	 * @param roomID room number
	 * @return if the room was upgraded
	 */
	public boolean upgradeRoom(int reserveID, int roomID){
		try{
			statement = conn.prepareStatement("UPDATE reservations SET roomID = ? WHERE reserveID = ?");
			statement.setInt(1, roomID);
			statement.setInt(2, reserveID);
			statement.executeUpdate();
			
			statement = conn.prepareStatement("SELECT * FROM reservations WHERE reserveID = ?");
			statement.setInt(1, reserveID);
			resultSet = statement.executeQuery();
			if(resultSet.next())
				return true;
			
		} catch(Exception e){
			System.out.println("========== Error: " + e + " ==========");
		}
		return false;
	}
	
	/**
	 * Return List of rooms
	 * @return list of rooms
	 */
	public ArrayList<Room> getRoom(){
		return rooms;
	}
	
	/**
	 * Add total price of the rooms.
	 * @param price price of the room
	 * @return total price so far
	 */
	public float addTotalprice(float price){
		room.setTotalPrice(price);
		return room.getTotalPrice();
	}
	
	/**
	 * Get the current user.
	 * @return user
	 */
	public User getUser()
	{
		return this.user;
	}
	
	/**
	 * Check if a user exists
	 * @return if user exists
	 */
	public boolean findUser()
	{
		return this.userNotFound;
	}
	
	/**
	 * Check if password exists
	 * @return if password exists
	 */
	public boolean checkPW()
	{
		return this.invalidPW;
	}
}
