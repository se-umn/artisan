package org.hotelme;
import java.util.Date;

/**
 * Room model for the Hotel.
 * @author David Nakonechnyy
 *
 */
public class Room {
	private int roomID;
	private String roomType;
	private int maxOccupancy;
	private int totalAdults;
	private int totalChildren;
	private Date checkIn;
	private Date checkOut;
	private int owner;
	private int reserveID;
	
	private float price;
	private float totalPrice = 0;
	
	/**
	 * Create an empty room.
	 * @param maxOccupancy max number of people in the room
	 * @param totalAdults how many adults in the room
	 * @param totalChildren how many children in the room
	 * @param owner owner of the room.
	 * @param checkIn when is the check in for the room
	 * @param checkOut when is the check out for the room
	 * @param price price of the room
	 * @param available is the room available
	 */
	public Room(int reserveID, int roomID, String roomType, int maxOccupancy, int totalAdults, int totalChildren, int owner, Date checkIn, Date checkOut, float price){
		
		this.reserveID = reserveID;
		this.roomType = roomType;
		this.roomID = roomID;
		this.maxOccupancy = maxOccupancy;
		this.totalAdults = totalAdults;
		this.totalChildren = totalChildren;
		this.owner = owner;
		this.checkIn = checkIn;
		this.checkOut =  checkOut;
		this.price = price;
	}
	
	/**
	 * Get room number(ID)
	 * @return room number
	 */
	public int getRoomID(){
		return roomID;
	}
	
	/**
	 * Get the room type.
	 * @return room type.
	 */
	public String getRoomType(){
		return roomType;
	}
	
	/**
	 * Get max occupancy of the room.
	 * @return max occupancy of the room
	 */
	public int getMaxOccupancy(){
		return maxOccupancy;
	}
	
	/**
	 * Get totals adults in the room.
	 * @return total adults
	 */
	public int getAdults(){
		return totalAdults;
	}
	
	/**
	 * Add more adults to total adults in the room.
	 * @param adults adults to be adde
	 */
	public void setAdult(int adults){
		totalAdults += adults;
	}
	
	/**
	 * Get total children in the room.
	 * @return total children
	 */
	public int getChildren(){
		return totalChildren;
	}
	
	/**
	 * Add more children to total children in the room.
	 * @param children children to be added
	 */
	public void setChildren(int children){
		totalChildren += children;
	}
	
	/**
	 * Return owner of the room.
	 * @return owner of the room
	 */
	public int getOwner(){
		return owner;
	}
	
	/**
	 * Get check in date.
	 * @return check in date
	 */
	public Date getCheckIn(){
		return checkIn;
	}
	
	/**
	 * Set the check in date for the room.
	 * @param start check in date
	 */
	public void setCheckIn(Date start){
		checkIn = start;
	}
	
	/**
	 * Get check out date.
	 * @return check out date
	 */
	public Date getCheckOut(){
		return checkOut;
	}
	
	/**
	 * Set total price of the room
	 * @param price price of the room
	 */
	public void setTotalPrice(float price){
		totalPrice = price;
	}
	
	/**
	 * Get total price of the room
	 * @return total price
	 */
	public float getTotalPrice(){
		return totalPrice;
	}
	
	/**
	 * Set the check out date for the room.
	 * @param end check out date
	 */
	public void setCheckOut(Date end){
		checkOut = end;
	}
	
	/**
	 * Get price of the room
	 * @return price
	 */
	public float getPrice(){
		return price;
	}
	
	/**
	 * Get reservation ID.
	 * @return reservation ID
	 */
	public int getReserveID(){
		return reserveID;
	}
}
