package org.hotelme;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.Days;


/**
 * Console view that will be displayed to the user for interaction.
 * @author David Nakonechnyy
 *
 */
public class HotelView {
	
	/**
	 * Print main menu options
	 */
	public void mainMenu(){
		System.out.println("=======================================================================================================");
		System.out.println("Welcome to Happy Feet Hotels automated booking system!");
		System.out.println("Please select an option: \n");
		System.out.println("1. Sign-In");
		System.out.println("2. Sign-Up");
		System.out.println("3. Exit");
		System.out.println("=======================================================================================================");
	}
	
	/**
	 * Print options menu options
	 * @param displayBars trigger to display bars
	 */
	public void optionsMenu(boolean displayBars){
		if(displayBars)
			System.out.println("=======================================================================================================");
		System.out.println("Please select an option: \n");
		System.out.println("1. Check Reserved Rooms");
		System.out.println("2. Reserve a Room");
		System.out.println("3. Update Reservation");
		System.out.println("4. Cancel a Room");
		System.out.println("5. Sign Out");
		System.out.println("=======================================================================================================");
	}
	
	/**
	 * Print sign in questions
	 * @param in scanner for user input
	 * @return user name and password
	 */
	public String signIn(Scanner in){
		System.out.println("=======================================================================================================");
		System.out.println("Sign-In:");
		System.out.println("Please enter your user name: ");
		String userName = in.next();
		System.out.println("Please enter your password: ");
		String password = in.next();
		String credentials = userName + " " + password;
		return credentials;
	}
	
	/**
	 * Sign in succeeds
	 * @param fname users first name
	 */
	public void signInSuccess(String fname){
		System.out.println("=======================================================================================================");
		System.out.println("Welcome " + fname + "! How can I assist you today?");
	}
	
	/**
	 * Print sign up questions
	 * @param in scanner for user input
	 * @return user credentials to sign up
	 */
	public String signUp(Scanner in){
		System.out.println("=======================================================================================================");
		System.out.println("Sign-Up:");
		System.out.println("Please enter your first name: ");
		String fname = in.next();
		System.out.println("Please enter your last name: ");
		String lname = in.next();
		System.out.println("Please enter a user name: ");
		String userName = in.next();
		System.out.println("Please enter a password: ");
		String password = in.next();
		String credentials = fname + " " + lname + " " + userName + " " + password;
		return credentials;
	}
	
	/**
	 * Print sign up succeeds
	 * @param fname users first name
	 */
	public void signUpSuccess(String fname){
		System.out.println("=======================================================================================================");
		System.out.println("Account successfully registered!");
		System.out.println("Welcome " + fname + "! How can I assist you today?");
	}
	
	/**
	 * Print header for reserved rooms
	 */
	public void reservedRooms(){
		System.out.println("===================================================================================================");
		System.out.println("Reserved Rooms:");
		System.out.printf("%-13s %-9s %-16s %-9s %-11s %-13s %-13s %-7s ", "RoomType ", "| Room # ", "| Max Occupancy ", "| Adults ", "| Children ", "| Check-In ", "| Check-Out ", "| Price");
		System.out.println();
		System.out.println("---------------------------------------------------------------------------------------------------");
	}
	
	/**
	 * Print each row for reserved rooms
	 * @param roomType room type
	 * @param roomNum room ID
	 * @param maxOcc max occupancy
	 * @param adult total adults
	 * @param child total children
	 * @param checkIn check in date
	 * @param checkOut check out date
	 * @param price price of room
	 */
	public void printReservedRooms(String roomType, int roomNum, int maxOcc, int adult, int child, Date checkIn, Date checkOut, float price){
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		System.out.printf("%-13s %-9s %-16s %-9s %-11s %-13s %-13s %-7s ", roomType + " ", "| " + roomNum, "| " + maxOcc, "| " + adult, "| " + child, "| " + sdf.format(checkIn), "| " +sdf.format(checkOut), "| $" + price);
		System.out.println();
	}
	
	/**
	 * Print total price of all rooms owned by the user.
	 * @param totalPrice total price
	 */
	public void totalPrice(float totalPrice){
		System.out.println("---------------------------------------------------------------------------------------------------");
		System.out.println("Total Price: $" + totalPrice);
	}
	
	/**
	 * Print no reservations found message.
	 */
	public void noReservedRooms(){
		System.out.println("You don't have any reservations yet.");
		System.out.println("===================================================================================================");
	}
	
	/**
	 * Print update reservations header
	 */
	public void updateReservations(){
		System.out.println("=======================================================================================================");
		System.out.println("Update Reservations:");
		System.out.println("Which reservation do you want to update? Type 0 to quit.");
		System.out.printf("%-17s %-9s %-16s %-9s %-11s %-13s %-13s %-7s ", "    RoomType ", "| Room # ", "| Max Occupancy ", "| Adults ", "| Children ", "| Check-In ", "| Check-Out ", "| Price");
		System.out.println();
		System.out.println("    ---------------------------------------------------------------------------------------------------");
	}
	
	/**
	 * Print delete reservations header
	 */
	public void deleteReservations(){
		System.out.println("=======================================================================================================");
		System.out.println("Cancel a Reservations:");
		System.out.println("Which reservation do you want to cancel? Type 0 to quit.");
		System.out.printf("%-17s %-9s %-16s %-9s %-11s %-13s %-13s %-7s ", "    RoomType ", "| Room # ", "| Max Occupancy ", "| Adults ", "| Children ", "| Check-In ", "| Check-Out ", "| Price");
		System.out.println();
		System.out.println("    ---------------------------------------------------------------------------------------------------");
	}
	
	/**
	 * Print confirm delete reservation
	 * @param in scanner for user input
	 * @return if confirmed or no
	 */
	public int deleteConfirmation(Scanner in){
		boolean select = false;
		int option = 0;
		while(!select){
			System.out.println("=======================================================================================================");
			System.out.println("Are you sure you want to cancel this room?");
			System.out.println("1. Yes");
			System.out.println("2. No");
			System.out.println("=======================================================================================================");
			option = in.nextInt();
			if(option < 1 || option > 2){
				System.out.println("=======================================================================================================");
				System.out.println("Error: That wasn't a valid choice. Please type 1 or 2.");
			}else
				select = true;
		}
		return option;
		
	}
	
	/**
	 * Print cancellation was successful
	 */
	public void reservationCancelationSuccessful(){
		System.out.println("=======================================================================================================");
		System.out.println("Reservation was successfully canceled!");
	}
	
	/**
	 * Print update options header
	 * @param in scanner for user input
	 * @return which option was selected
	 */
	public int updateOptions(Scanner in){
		boolean select = false;
		int option = 0;
		while(!select){
			System.out.println("=======================================================================================================");
			System.out.println("What would you like to update?");
			System.out.println("1. Guests");
			System.out.println("2. Upgrade the room");
			System.out.println("=======================================================================================================");
			option = in.nextInt();
			if(option < 1 || option > 2){
				System.out.println("=======================================================================================================");
				System.out.println("Error: That wasn't a valid choice. Please try again.");
			}else
				select = true;
		}
		return option;
		
	}
	
	/**
	 * Print message guest update was successful
	 */
	public void guestUpdateSuccessful(){
		System.out.println("=======================================================================================================");
		System.out.println("Room update was successful!");
	}
	
	/**
	 * Print upgrade options menu to select a room to upgrade
	 * @param in scanner for user input
	 * @param roomType the current room type
	 * @return the new room type selected for upgrade
	 */
	public String upgradeRoom(Scanner in, String roomType){
		String roomSelected = "";
		do{
			System.out.println("=======================================================================================================");
			System.out.println("Which room would you like to upgrade too?");
			int options = 0;
			if(roomType.equals("Happy Double")){
				System.out.println("1. Happy Queen");
				System.out.println("2. Happy King");
				System.out.println("3. Happy Suite");
				options = 1;
			}else if(roomType.equals("Happy Queen")){
				System.out.println("1. Happy King");
				System.out.println("2. Happy Suite");
				options = 2;
			}else if(roomType.equals("Happy King")){
				System.out.println("1. Happy Suite");
				options = 3;
			}else
				System.out.println("You have our best room!");
			
			//If it's the best room just return
			if(options == 0){
				roomSelected = roomType;
				return roomSelected;
			}
				
			
			int roomChoice = in.nextInt();
			if(options == 1){
				switch (roomChoice){
					case 1: roomSelected = "Happy Queen";
							break;
					case 2: roomSelected = "Happy King";
							break;
					case 3: roomSelected = "Happy Suite";
							break;
					default: roomSelected = "invalid";
							break;
				}	
			} else if(options == 2){
				switch (roomChoice){
					case 1: roomSelected = "Happy King";
							break;
					case 2: roomSelected = "Happy Suite";
							break;
					default: roomSelected = "invalid";
							break;
				}	
			} else if(options == 3){
				switch (roomChoice){
					case 1: roomSelected = "Happy Suite";
							break;
					default: roomSelected = "invalid";
							break;
				}	
			}
		
			if(roomSelected.equals("invalid") || roomSelected.equals("")){
				System.out.println("=======================================================================================================");
				System.out.println("Error: That wasn't a valid choice. Please try again.");
			}
			
		}while(roomSelected.equals("invalid") || roomSelected.equals(""));
		
		return roomSelected;
	}
	
	/**
	 * Print upgrade was successful message.
	 */
	public void upgradeSuccessful(){
		System.out.println("=======================================================================================================");
		System.out.println("Room upgrade was successful!");
	}
	
	/**
	 * Print check in date selection
	 * @param in scanner for user input
	 * @return check in date
	 * @throws ParseException
	 */
	public Date checkInDate(Scanner in) throws ParseException{
		System.out.println("=======================================================================================================");
		System.out.println("When will you be staying with us?");
		System.out.println("Check-In Date(mm/dd/yy): ");
		String dateString = in.next();
		DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
		Date checkIn = formatter.parse(dateString);
		// This is an interesting case, but problematic 
		// Alessio: disable this for the moment
		Date currDate = new Date(0);
		currDate = formatter.parse(formatter.format(new Date(0)));
		//
		while(!checkIn.after(currDate) && checkIn.before(currDate)){
			System.out.println("=======================================================================================================");
			System.out.println("Error: You need to use a date after todays date or todays date. Todays date is : " + formatter.format(currDate));
			System.out.println("Check-In Date(mm/dd/yy): ");
			dateString = in.next();
			checkIn = formatter.parse(dateString);
		}
		return checkIn;
	}
	
	/**
	 * Print check out date selection
	 * @param in scanner for user input
	 * @param checkIn date checking in
	 * @return check out date
	 * @throws ParseException
	 */
	public Date checkOutDate(Scanner in, Date checkIn) throws ParseException{
		System.out.println("=======================================================================================================");
		System.out.println("Check-Out Date(mm/dd/yy): ");
		String dateString = in.next();
		DateFormat formatter = new SimpleDateFormat("MM/dd/yy");
		Date checkOut = formatter.parse(dateString);
		DateTime checkInDateTime = new DateTime(checkIn.getTime());
		DateTime checkOutDateTime = new DateTime(checkOut.getTime());
		int days = Days.daysBetween(checkInDateTime, checkOutDateTime).getDays();
		while(days <= 0 || days > 14){
			if(days <= 0){
				System.out.println("=======================================================================================================");
				System.out.println("Error: You have to stay for at least for one night.");
				System.out.println("Check-In Date(mm/dd/yy): ");
			} else {
				System.out.println("=======================================================================================================");
				System.out.println("Error: You can't stay longer than 14 days please shorten your trip.");
				System.out.println("Check-In Date(mm/dd/yy): ");
			}
			dateString = in.next();
			checkOut = formatter.parse(dateString);
			checkOutDateTime = new DateTime(checkOut.getTime());
			days = Days.daysBetween(checkInDateTime, checkOutDateTime).getDays();
		}
		return checkOut;
	}
	
	/**
	 * Print message to select a room type.
	 * @param in scanner for user input
	 * @return which room type was selected
	 */
	public String roomTypes(Scanner in){
		String roomType = "";
		do{
			System.out.println("=======================================================================================================");
			System.out.println("What type of room would you like to reserve?");
			System.out.println("1. Happy Double");
			System.out.println("2. Happy Queen");
			System.out.println("3. Happy King");
			System.out.println("4. Happy Suite");
			System.out.println("=======================================================================================================");
			int roomChoice = in.nextInt();
			
			switch (roomChoice){
			case 1: roomType = "Happy Double";
					break;
			case 2: roomType = "Happy Queen";
					break;
			case 3: roomType = "Happy King";
					break;
			case 4: roomType = "Happy Suite";
					break;
			default: roomType = "invalid";
					break;
			}	
			
			if(roomType.equals("invalid") || roomType.equals("")){
				System.out.println("=======================================================================================================");
				System.out.println("Error: That wasn't a valid choice. Please try again.");
			}	
			
		}while(roomType.equals("invalid") || roomType.equals(""));
		
		return roomType;
	}
	
	/**
	 * Print message to confirm a room
	 * @param in scanner for user input
	 * @param count how many rooms are available of a specific type
	 * @param roomType room type
	 * @return if user confirms or not
	 */
	public int roomConfirmation(Scanner in, int count, String roomType){
		int confirm = 0;
		boolean wrongCount = false;
		while(!wrongCount){
			System.out.println("=======================================================================================================");
			if(count > 1)
				System.out.println("We have " + count + " " + roomType + "s available. Confirm a room?");
			else
				System.out.println("We have " + count + " " + roomType + " available. Confirm the room?");
			System.out.println("1. Yes");
			System.out.println("2. No");
			confirm = in.nextInt();
			if(confirm <1 || confirm > 2){
				System.out.println("=======================================================================================================");
				System.out.println("Error: That wasn't a valid choice. Please try again.");
			}else{
				wrongCount = true;
			}
		}
		return confirm;
	}
	
	/**
	 * Print how many adults are staying
	 * @param in scanner for user input
	 * @param maxOcc max occupancy of the room
	 * @return how many adults are staying
	 */
	public int totalAdults(Scanner in, int maxOcc){
		int adults = 0;
		do{
			System.out.println("=======================================================================================================");
			System.out.println("Room occupancy is " + maxOcc);
			System.out.println("How many adults will be staying?");
			System.out.println("=======================================================================================================");
			adults = in.nextInt();
		
			if(adults > maxOcc){
				System.out.println("=======================================================================================================");
				System.out.println("Error: Sorry that many people exceed the room occupancy. Please try again.");
			}else if(adults < 1){
				System.out.println("=======================================================================================================");
				System.out.println("Error: There must be at least one adult in the room. Please try again.");
			}			
			
		} while(adults > maxOcc || adults < 1);
		
		return adults;
	}
	
	/**
	 * Print how many children are staying
	 * @param in scanner for user input
	 * @param maxOcc max occupancy of the room
	 * @return how many children are staying
	 */
	public int totalChildren(Scanner in, int maxOcc){
		int children = 0;
		do {
			System.out.println("=======================================================================================================");
			System.out.println("Room occupancy left is " + maxOcc);
			System.out.println("How many Children will be staying?");
			System.out.println("=======================================================================================================");
			children = in.nextInt();
			
			if(children > maxOcc){
				System.out.println("=======================================================================================================");
				System.out.println("Error: Sorry that many people exceed the room occupancy. Please try again.");;
			}	
		}while(children > maxOcc);
		
		return children;
	}
	
	/**
	 * Print room was sucessfully booked
	 */
	public void successfulRoomReg(){
		System.out.println("=======================================================================================================");
		System.out.println("Room was successfully booked!");
	}
	
	
	//Error Messages
	
	/**
	 * Exiting message.
	 */
	public void exitMessage(){
		System.out.println("=======================================================================================================");
		System.out.println("Thank you for visitng Happy Feet Hotels!");
		System.out.println("=======================================================================================================");
		System.exit(0);
	}
	
	/**
	 * Error message for user name not being found.
	 */
	public void invalidUser(){
		System.out.println("=======================================================================================================");
		System.out.println("Error: Sorry that user name doesn't exist. Please try again.");
	}
	
	/**
	 * Error message for password not matching.
	 */
	public void invalidPassword(){
		System.out.println("=======================================================================================================");
		System.out.println("Error: Sorry that password is incorrect. Please try again.");
	}
	
	/**
	 * Error message for sign ups failed. User name is taken.
	 */
	public void signUpFailure(){
		System.out.println("=======================================================================================================");
		System.out.println("Error: Sorry that user name is taken. Please try again.");
	}
	
	/**
	 * Error message upgrade failed.
	 */
	public void upgradeFailed(){
		System.out.println("=======================================================================================================");
		System.out.println("Error: Room upgrade was unsuccessful. Please try again.");
	}
	
	/**
	 * Error message if the user entered an invalid date.
	 */
	public void invalidDateInput(){
		System.out.println("=======================================================================================================");
		System.out.println("Error: Invalid date input. Must be in the form mm/dd/yy.");
	}
	
	/**
	 * Error message room was not successfully registered.
	 */
	public void notSuccessfulRoomReg(){
		System.out.println("=======================================================================================================");
		System.out.println("Error: We were not able to register your room. Please try again.");
		System.out.println();
	}
	
	/**
	 * Error message no rooms are available for a upgrade in a given time range
	 */
	public void noRoomUpgradeAvailable(){
		System.out.println("=======================================================================================================");
		System.out.println("Error: Unfortunately we are out of those rooms for your selected date.");
	}
	

	/**
	 * Error message: No rooms are available. Need to select another date.
	 */
	public void noRoomsAvailable(){
		System.out.println("=======================================================================================================");
		System.out.println("Error: Unfortunately we are out of those rooms for your selected date.");
		System.out.println("Please choose another room or pick a different date.");
	}
	
	
	/**
	 * Error message main menu option not selected correctly
	 * @param choice users input
	 */
	public void errorMessageMainMenu(int choice){
		System.out.println("=======================================================================================================");
		System.out.println("Error: " + choice + " is not a valid option. Please type 1, 2, or 3.");
	}
	
	/**
	 * Error message users had an input mismatch error.
	 */
	public void inputMisMatch(){
		System.out.println("=======================================================================================================");
		System.out.println("Error: That was an invalid option. Please try again.");
	}
	
}
