package org.hotelme;
import java.text.ParseException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Console for user interaction.
 * 
 * 
 * @author David Nakonechnyy
 *
 */
public class HotelController {
	private HotelView view;
	private HotelModel model;
	private boolean running = true;
	private boolean runningOptions = true;
	private boolean displayBars = true;
	private boolean keepLooping = true;
	private String cred;
	
	
	transient Scanner in = new Scanner(System.in);
	
	/**
	 * Constructs the hotel controller with the model and view.
	 * @param model hotel model
	 * @param view hotel view
	 */
	public HotelController(HotelModel model, HotelView view) {
		this.model = model;
		this.view = view;
	}
	
	/**
	 * Run the program
	 */
	public void start(){
		running = true;
		while(running){			
			view.mainMenu();
			try{
				//Main Menu Selection
				int choice = in.nextInt();
				if(choice == 1){
					cred = view.signIn(in);
					String[] logInCred = cred.split(" ");
					if(model.userLogin(logInCred[0], logInCred[1])){
						view.signInSuccess(model.getUser().getFname());
						displayBars = false;
						options();
					}else{
						if(model.findUser())
							view.invalidUser();
						else if (model.checkPW())
							view.invalidPassword();
					}	
				} else if (choice == 2){
					cred = view.signUp(in);
					String[] signInCred = cred.split(" ");
					if(model.userSignUp(signInCred[0], signInCred[1], signInCred[2], signInCred[3])){
						view.signUpSuccess(model.getUser().getFname());
						displayBars = false;
						options();
					}else {
						view.signUpFailure();
					}
				} else if (choice == 3){
					
					view.exitMessage();
				} else {
					view.errorMessageMainMenu(choice);
				}		
			} catch(InputMismatchException e){
				view.inputMisMatch();
				in.next();
			}			
		}
	}
	
	/**
	 * Show options menu for the account.
	 */
	public void options(){
		runningOptions = true;
		while(runningOptions){
			view.optionsMenu(displayBars);
			displayBars = true;
			int choice = 0;
			try{
				choice = in.nextInt();
			}catch(InputMismatchException e){
				view.inputMisMatch();
				in.next();
			}
			if(choice == 1){
				checkReservations();
			}else if(choice == 2){
				createReservations();
			} else if(choice == 3){
				updateReservations();
			}else if(choice == 4){
				deleteReservations();
			} else if(choice == 5)
				runningOptions = false;
		} 
	}
	
	/**
	 * Check reservations option
	 */
	public void checkReservations(){
		view.reservedRooms();
		displayBars = false;
		if(model.checkReservedRooms()){
			float totalPrice = 0; 
			for(int i = 0; i < model.getRoom().size(); i++){
				view.printReservedRooms(model.getRoom().get(i).getRoomType(), model.getRoom().get(i).getRoomID(), model.getRoom().get(i).getMaxOccupancy(), model.getRoom().get(i).getAdults(), 
						model.getRoom().get(i).getChildren(), model.getRoom().get(i).getCheckIn(), model.getRoom().get(i).getCheckOut(), model.getRoom().get(i).getPrice());
				totalPrice += model.addTotalprice(model.getRoom().get(i).getPrice());
			}
			view.totalPrice(totalPrice);
			System.out.println("===================================================================================================");
		}
		else {
			view.noReservedRooms();
		}
	}
	
	/**
	 * Create reservations option
	 */
	public void createReservations(){
		Date checkIn = null;
		Date checkOut = null;
		String roomType = "";
		keepLooping = true;
		int count = 0;
		do{
			do{
				try{
					
					while(checkIn == null)
						checkIn = view.checkInDate(in);
					
					while(checkOut == null)
						checkOut = view.checkOutDate(in, checkIn);
					
					while(roomType.equals(""))
						roomType = view.roomTypes(in);
					
					keepLooping = false;
				}catch(ParseException e){
					view.invalidDateInput();
				}catch(InputMismatchException e){
					view.inputMisMatch();
					in.next();
				}
			}while(keepLooping);
			
			//Need to convert to the SQL date type
			java.sql.Date sqlCheckIn = new java.sql.Date(checkIn.getTime());
			java.sql.Date sqlCheckOut = new java.sql.Date(checkOut.getTime());
			count = model.checkRoomsAvailable(sqlCheckIn, sqlCheckOut, roomType);
		
			if(count > 0){
				int totalAdults = 0;
				int totalChildren = -1;
				int choice = 0;
				boolean confirmed = false;
				keepLooping = true;
				do{
					try{
						if(!confirmed)
							choice = view.roomConfirmation(in, count, roomType);
						if(choice == 1){
							confirmed = true;
							while(totalAdults == 0)
								totalAdults = view.totalAdults(in, model.getRoom().get(0).getMaxOccupancy());
							
							if(totalAdults < model.getRoom().get(0).getMaxOccupancy()){
								while(totalChildren == -1)
									totalChildren = view.totalChildren(in, model.getRoom().get(0).getMaxOccupancy() - totalAdults);
							}
							if(model.reserveRoom(model.getRoom().get(0).getRoomID(), totalAdults, model.getRoom().get(0).getRoomType(),
									totalChildren, model.getRoom().get(0).getOwner(), sqlCheckIn, sqlCheckOut))
								view.successfulRoomReg();
							else
								view.notSuccessfulRoomReg();
							keepLooping = false;
						}else
							keepLooping = false;
					}catch(InputMismatchException e){
						view.inputMisMatch();
						in.next();
					}
				}while(keepLooping);
			}else{
				view.noRoomsAvailable();
				checkIn = null;
				checkOut = null;
				roomType = "";
			}
				
		}while(count == 0);
		
	}
	
	/**
	 * Update reservations options
	 */
	public void updateReservations(){
		boolean validRoomSelected = false;
		int option = 0;	
		int roomSelected = -1;
		int totalAdults = -1;
		int totalChildren = 0;
		boolean roomPicked  = false;
		String roomUpgrade = "";

		while(!validRoomSelected){
			keepLooping = true;
			if(roomSelected <= -1 || roomSelected > model.getRoom().size()){
				view.updateReservations();
				displayBars = true;
				if(model.checkReservedRooms()){
					for(int i = 0; i < model.getRoom().size(); i++){
						System.out.print(i + 1 + ".  ");
						view.printReservedRooms(model.getRoom().get(i).getRoomType(), model.getRoom().get(i).getRoomID(), model.getRoom().get(i).getMaxOccupancy(), model.getRoom().get(i).getAdults(), 
								model.getRoom().get(i).getChildren(), model.getRoom().get(i).getCheckIn(), model.getRoom().get(i).getCheckOut(), model.getRoom().get(i).getPrice());
					}
					System.out.println("========================================================================================================");
				}else{
					displayBars = false;
					view.noReservedRooms();
					break;
				}
			}
			do{
				try{
					while(!roomPicked){
						roomSelected = in.nextInt();
						roomPicked = true;
					}
					if(roomSelected >= 1 && roomSelected <= model.getRoom().size()){
						while(option == 0)
							option = view.updateOptions(in);
						if(option == 1){
							while(totalAdults == -1)
								totalAdults = view.totalAdults(in, model.getRoom().get(roomSelected - 1).getMaxOccupancy());
							while(totalChildren == 0)
								totalChildren = view.totalChildren(in, model.getRoom().get(roomSelected - 1).getMaxOccupancy() - totalAdults);
							if(model.updateGuests(model.getRoom().get(roomSelected - 1).getReserveID(), totalAdults, totalChildren)){
									displayBars = true;
									validRoomSelected = true;
									view.guestUpdateSuccessful();
								}
						//Upgrade the room if possible	
						} else if (option == 2){
							while(roomUpgrade.equals(""))
								roomUpgrade = view.upgradeRoom(in, model.getRoom().get(roomSelected - 1).getRoomType());
							int reserveID = model.getRoom().get(roomSelected - 1).getReserveID();
							//user cannot upgrade anymore.
							if(model.getRoom().get(roomSelected - 1).getRoomType().equals("Happy Suite")){
								displayBars = true;
								validRoomSelected = true;
							}else{
								//Need to convert to the SQL date type
								java.sql.Date sqlCheckIn = new java.sql.Date(model.getRoom().get(roomSelected - 1).getCheckIn().getTime());
								java.sql.Date sqlCheckOut = new java.sql.Date(model.getRoom().get(roomSelected - 1).getCheckOut().getTime());
								int count = model.checkRoomsAvailable(sqlCheckIn, sqlCheckOut, roomUpgrade);
								displayBars = true;
								if(count > 0){
									if(model.upgradeRoom(reserveID, model.getRoom().get(0).getRoomID())){
										view.upgradeSuccessful();
										validRoomSelected = true;
									} else
										view.upgradeFailed();
										
								}else{
									view.noRoomUpgradeAvailable();
								}
							}
						}else{
							option = 0;
							keepLooping = false;
						}
					}else if(roomSelected == 0){
						displayBars = true;
						option = 0;
						keepLooping = false;
						validRoomSelected = true;
						roomPicked = false;
					}else
						view.inputMisMatch();
					
					keepLooping = false;	
				}catch (InputMismatchException e){
					view.inputMisMatch();
					in.next();
					break;
				}
			}while(keepLooping);
	}
}
	
	/**
	 * Delete Reservations options
	 */
	public void deleteReservations(){
		boolean validRoomSelected = false;
		int option = 0;	
		int roomSelected = -1;
		boolean roomPicked  = false;
		
		while(!validRoomSelected){
			 keepLooping = true;
			if(roomSelected <= -1 || roomSelected > model.getRoom().size()){
				view.deleteReservations();
				displayBars = true;
				if(model.checkReservedRooms()){
					for(int i = 0; i < model.getRoom().size(); i++){
						System.out.print(i + 1 + ".  ");
						view.printReservedRooms(model.getRoom().get(i).getRoomType(), model.getRoom().get(i).getRoomID(), model.getRoom().get(i).getMaxOccupancy(), model.getRoom().get(i).getAdults(), 
								model.getRoom().get(i).getChildren(), model.getRoom().get(i).getCheckIn(), model.getRoom().get(i).getCheckOut(), model.getRoom().get(i).getPrice());
					}
					System.out.println("========================================================================================================");
				}else{
					displayBars = false;
					view.noReservedRooms();
					break;
				}
			}
			do{
				try{
					while(!roomPicked){
						roomSelected = in.nextInt();
						roomPicked = true;
					}
					if(roomSelected >= 1 && roomSelected <= model.getRoom().size()){
						while(option == 0)
							option = view.deleteConfirmation(in);
						if(option == 1){
							validRoomSelected = true;
							model.deleteReservation(model.getRoom().get(roomSelected - 1).getReserveID());
							view.reservationCancelationSuccessful();
							keepLooping = false;
						}else{
							option = 0;
							keepLooping = false;
							roomSelected = -1;
							roomPicked  = false;
						}
					} else if(roomSelected == 0){
						displayBars = true;
						option = 0;
						keepLooping = false;
						validRoomSelected = true;
						
					}else
						view.inputMisMatch();
					
					keepLooping = false;
				}catch(InputMismatchException e){
					view.inputMisMatch();
					in.next();
					break;
				}
			}while(keepLooping);
		} 
	}
}


