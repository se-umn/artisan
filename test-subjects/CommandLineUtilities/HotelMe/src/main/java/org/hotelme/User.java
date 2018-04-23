package org.hotelme;

/**
 * User model for the hotel.
 * 
 * @author David Nakonechnyy
 *
 */
public class User {
	private int userID;
	private String fname;
	private String lname;
	private String userName;
	private String password;

	/**
	 * Create a new User.
	 * 
	 * @param fname
	 *            first name
	 * @param lname
	 *            last name
	 * @param userName
	 *            user name
	 * @param password
	 *            password
	 */
	public User(int userID, String fname, String lname, String userName, String password) {
		this.userID = userID;
		this.fname = fname;
		this.lname = lname;
		this.userName = userName;
		this.password = password;
	}

	/**
	 * Get the first name of a user.
	 * 
	 * @return first name
	 */
	public String getFname() {
		return fname;
	}

	/**
	 * Get usersID
	 * 
	 * @return user ID
	 */
	public int getUserID() {
		return userID;
	}
}