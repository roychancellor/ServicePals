package cst235.servicepals.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cst235.servicepals.model.Community;
import cst235.servicepals.model.DataSource;
import cst235.servicepals.model.ServiceProvider;
import cst235.servicepals.model.User;

//FUTURE VERSION: NEED TO BE ABLE TO CREATE AN OBJECT OF SERVICEPALS FOR EACH LOGGED-IN USER
//SO MORE THAN ONE USER CAN OPERATE ON COMMUNITIES, ETC.

/**
 * the main controller for the application
 */
public class Controller {
	//Scanner object for use throughout the application
	public static Scanner scan = new Scanner(System.in);
	//Class constants
	private static final int MENU_EXIT = 0;
	public static final int MIN_COMMUNITY_NAME_LENGTH = 5;
	
	//Class data
	private static int currentUserId = 0;
	private static User currentUser;

	//The master list of communities for the application
	private static List<Community> communities = new ArrayList<Community>();
	
	//The master list of users for the application
	private static List<User> users = new ArrayList<User>();
	//NOTE:  DO NOT NEED A LIST OF PROVIDERS SINCE PROVIDERS ARE USERS FIRST
	//A PROVIDER OBJECT EXISTS IN EACH UER OBJECT AND CAN BE INSTANTIATED WHEN A USER
	//APPLIES TO BE A SERVICE PROVIDER FOR A COMMUNITY
	//HOW TO HANDLE SITUATION WHERE A USER IS A SERVICE PROVIDER IN ONE COMMUNITY
	//AND JOINS ANOTHER COMMUNITY BUT IS NOT TO BE A SERVICE PROVIDER IN THAT COMMUNITY???
	
	
	/**
	 * the main menu where the application starts
	 */
	public static void showMainMenu() {		
		int option = MENU_EXIT;
		do {
			System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
			System.out.println("⚪⚪ 	          SERVICE PALS	                  ⚪⚪");
			System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
			System.out.println();
			System.out.println("1. Login to existing ServicePals account");
			System.out.println("2. Create new ServicePals account");
			System.out.println("--------------------------------------------");
			System.out.println("0. Exit ServicePals");
			option = getValueFromUser(0, 2, "Oops, enter 1 or 2. Enter 0 to exit.");
			processMainMenu(option);
		} while(option != MENU_EXIT);
	}
	
	/**
	 * acts on the user's selection
	 * @param selection the user's selection from the main menu
	 */
	private static void processMainMenu(int selection) {
		switch(selection) {
		case 1:
			if(doUserLogin()) {
				DataSource ds = new DataSource();
				currentUser = ds.dbRetrieveUserById(currentUserId);
				ds.close();
				if(currentUser != null) {
					showUserMenu();
				}
				else {
					System.out.println("\nDATABASE ERROR: USER QUERY FAILED IN processMainMenu");
				}
			}
			break;
		case 2:
			doCreateUser();
			break;
		case 0:
			break;
		}
	}
	
	/**
	 * logs in a user using user name and password (allows up to three attempts)
	 * @return true if user name and password match; false if not
	 */
	public static boolean doUserLogin() {
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.println("⚪⚪ 	    SERVICE PALS: LOGIN	                  ⚪⚪");
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.println("\nEnter username: ");
		String user = scan.nextLine();
		System.out.print("Enter password: ");
		String password = scan.nextLine();

		boolean credentialsMet = false;
		int numTries = 0;
		int maxTries = 3;
		do {
			credentialsMet = false;
			if (checkLoginCredentials(user, password)) {
				return true;
			}
			else {
				numTries++;
				System.err.println("Incorrect user name or password. "
					+ (maxTries - numTries) + " attempts remaining.");
				if(numTries == 3) {
					System.out.println("\nMaximum number of attempts is 3. Returning to main menu.");
					return false;
				}
			}
		} while(!credentialsMet);
		return false;
	}

	/**
	 * queries the users database for a user name and password match
	 * @param username the user name of the current attempt to log in
	 * @param password the password of the current attempt to log in
	 * @return true if un/pw match found and false if not
	 */
	public static boolean checkLoginCredentials(String username, String password) {
		//Query the users table for username and password
		DataSource ds = new DataSource();
		int userId = ds.dbGetUserIdFromCredentials(username, password);
		if(userId > 0) {
			currentUserId = userId;
			ds.close();
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * creates a new user of ServicePals
	 */
	public static void doCreateUser() {
		System.out.println("Fill out your contact information below:");
		boolean userAlreadyExists = false;
		String username;
		DataSource ds = new DataSource();
		do {
			userAlreadyExists = false;
			System.out.println("Enter a user name:");
			username = scan.nextLine();
			//Check to see if user name already exists
			if((userAlreadyExists = ds.dbCheckAlreadyExists(username, 'u')) == true) {
				System.out.println("\nUsername already exists, choose another one");
			}
		} while(userAlreadyExists);
		
		//Unique user validated, so get the rest of the information from the user
		System.out.println("Enter a password -->");
		String password = scan.nextLine();
		
		System.out.println("Enter your first Name -->");
		String firstName = scan.nextLine();
		
		System.out.println("Enter your last name -->");
		String lastName = scan.nextLine();
		
		boolean phoneIncorrect;
		String phoneNumber;
		do {
			phoneIncorrect = false;
			System.out.println("Enter your phone number (xxx-xxx-xxxx) -->");
			phoneNumber = scan.nextLine();
			if(phoneNumber.length() != 12 || phoneNumber.charAt(3) != '-' || phoneNumber.charAt(7) != '-') {
				System.err.println("\nOops, phone number should be xxx-xxx-xxxx. Try again.");
				phoneIncorrect = true;
			}
		} while(phoneIncorrect);

		boolean emailInvalid = false;
		String emailAddress;
		do {
			emailInvalid = false;
			System.out.println("Enter your e-mail address --> ");
			emailAddress = scan.nextLine();
			if(emailAddress.length() < 5) {
				System.out.println("\nOops, email address must be > 5 characters (including the @ and .XXX)");
				emailInvalid = true;
			}
			else if(!emailAddress.contains("@") || emailAddress.charAt(emailAddress.length() - 4) != '.') {
				System.err.println("Oops, email format must be address@domain name.xxx");
				emailInvalid = true;
			}
		} while(emailInvalid);
		
		//Create the user
		int userId = ds.dbCreateUser(firstName, lastName, username, password, phoneNumber, emailAddress);
		ds.close();
		System.out.println("Success, created user id #" + userId);
		//users.add(new User(firstName, lastName, username, password, emailAddress));
		currentUser = new User(firstName, lastName, username, password, emailAddress);
	}
	
	/**
	 * the menu each user will see to access their communities
	 * @param validatedUser a User object whose user name and password are validated
	 */
	private static void showUserMenu() {
		boolean keepRunning = true;
		do {
			System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
			System.out.println("⚪⚪ 	        SERVICE PALS USER MENU	           ⚪⚪");
			System.out.println("⚪⚪ 	     WELCOME " + currentUser.getFirstName() + " " + currentUser.getLastName() + "          ⚪⚪");
			System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
			System.out.println();
			//Show available options for the logged-in user
			System.out.println("1. Join an existing community");
			System.out.println("2. Create a new community");
			System.out.println("3. Delete an existing community");
			//Get the available communities from the database for the current user
			DataSource ds = new DataSource();
			List<Community> userComms = ds.dbRetrieveCommunities(currentUserId, DataSource.INCLUDE_USER_ID);
			ds.close();
			//Show available communities to the user, if any
			int numUserCommunities = 0;
			if(userComms != null) {
				currentUser.setCommunities(userComms);
				numUserCommunities = currentUser.getCommunities().size();
				if(numUserCommunities > 0) {
					for(int c = 0; c < numUserCommunities; c++) {
						System.out.println((c + 4) + ". Enter community "
							+ currentUser.getCommunities().get(c).getCommunityName());
						//TODO Add a feature that shows "A" for communities for which the user is also an admin
					}
				}
			}
			//Finish the menu
			System.out.println("----------------------------------------------------------------------");
			System.out.println(MENU_EXIT + ". Return to the main menu");
			System.out.println("\nMake a selection:");
			int selection = getValueFromUser(0, numUserCommunities + 3, "Oops, enter a valid menu selection.");
			if(selection == MENU_EXIT) {
				keepRunning = false;
			}
			else {
				processUserMenu(selection);
			}
		} while(keepRunning);
	}
	
	/**
	 * acts on the user's community selection
	 * @param menuSelection the option the user selected
	 */
	private static void processUserMenu(int selection) {
		//Update this if the menu items changes
		final int MENU_ITEMS_OFFSET = 3;
		//community list index is the menu selection minus the offset minus 1
		int commListIndex = selection - MENU_ITEMS_OFFSET - 1;
		switch(selection) {
		case MENU_EXIT:
			break;
		case 1:
			doJoinExistingCommunity();
			break;
		case 2:
			doCreateNewCommunity();
			break;
		case 3:
			System.out.println("DELETE COMMUNITY: Coming soon...");
			break;
		default:
			//show the actions for the community
			showCommunityActionsMenu(currentUser.getCommunities().get(commListIndex)/*.getCommunityId()*/);
			break;
		}
	}
	
	/**
	 * joins an existing, logged-in user to an existing community
	 */
	private static void doJoinExistingCommunity() {
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.println("⚪⚪ 	       JOIN EXISTING COMMUNITY             ⚪⚪");
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");

		//List all communities for which the current user is NOT already a member
		//OR the administrator
		DataSource ds = new DataSource();
		List<Community> availableCommunities = ds.dbRetrieveCommunities(currentUserId, DataSource.EXCLUDE_USER_ID);
		System.out.println("\nAvailable communities to join:");
		int numAvailableComm = availableCommunities.size();
		if(numAvailableComm > 0) {
			for(int c = 0; c < numAvailableComm; c++) {
				System.out.println((c + 1) + ". " + availableCommunities.get(c).getCommunityName());
			}
		}
		else {
			System.out.println("\nNone: You are already the member or administrator of all existing communities.");
		}
		System.out.println("----------------------------------------------------------------------");
		System.out.println(MENU_EXIT + ". Return to previous menu");
		System.out.println("\nSelect a community number:");
		int listSelection = getValueFromUser(MENU_EXIT, numAvailableComm,
			"Oops, enter a valid community number or 0 to return.");
		if(listSelection != MENU_EXIT) {
			ds.dbInsertIntoUserCommunity(currentUserId, availableCommunities.get(listSelection - 1).getCommunityId());
		}
		ds.close();
	}
	
	/**
	 * creates a new community and the current logged-in user becomes the administrator
	 */
	private static void doCreateNewCommunity() {
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.println("⚪⚪ 	         CREATE NEW COMMUNITY             ⚪⚪");
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		
		String commName;
		boolean commExists = false;
		DataSource ds;
		do {
			commExists = false;
			System.out.print("\nEnter a community name (at least " + MIN_COMMUNITY_NAME_LENGTH + " characters): ");
			commName = scan.nextLine();
			//Validate the length of the community name
			while(commName.length() < MIN_COMMUNITY_NAME_LENGTH) {
				System.err.println("Community name must contain at least " + MIN_COMMUNITY_NAME_LENGTH + " characters");
				commName = scan.nextLine();
			}
			
			//VERIFY A COMMUNITY WITH THE SAME NAME DOES NOT YET EXIST;
			//IF ONE DOES, REJECT THE REQUEST; IF NOT, MAKE NEW COMMUNITY
			ds = new DataSource();
			commExists = ds.dbCheckAlreadyExists(commName, 'c');
			if(commExists) {
				System.out.println("\nOops, a community with name " + commName + " already exists. Try again.");
			}
		} while(commExists);
		
		//Now that community name is validated to be unique, create the community
		//with the current user as the admin, then add the user and community to the user-community table
		String accessCode = generateUniqueAccessNumber();
		//Make new community
		int newCommId = ds.dbCreateCommunity(commName, accessCode, currentUserId);
		//Write the user-community pair into the user_community table
		int userCommId = -1;
		if(newCommId > 0) {
			userCommId = ds.dbInsertIntoUserCommunity(currentUserId, newCommId);
			//Confirm with the user
			if(userCommId > 0) {
				System.out.println("\nCongratulations, new community created with ACCESS CODE: " + accessCode);
				System.out.println("Write it down!\n");
			}
			else {
				System.out.println("\nERROR: TRIED TO ADD COMMUNITY-USER PAIR THAT EXISTS");
			}
		}

		//Close the DataSource connection
		ds.close();
	}

	/**
	 * shows the options a user can perform in each community for which the user is a member
	 * @param commIndex
	 */
	private static void showCommunityActionsMenu(Community userComm) {
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.println("⚪⚪ COMMUNITY " + userComm.getCommunityName() + " ⚪⚪");
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");

		System.out.println("\n1. Become a provider for this community");
		System.out.println("2. Update your provider information for this community");
		System.out.println("----------------------------------------------------------------------");
		//List available service providers for the community
		final int START_PROVIDERS = 3;
		DataSource ds = new DataSource();
		List<ServiceProvider> userCommProviders = ds.dbRetrieveProvidersByComm(userComm.getCommunityId());
		ds.close();
		int listItem = START_PROVIDERS;
		if(userCommProviders != null) {
			userComm.setProviders(userCommProviders);
			System.out.println("\nSchedule a provider below:");
			for(int p = 0; p < userComm.getProviders().size(); p++) {
				System.out.println(listItem + ". Schedule provider: "
					+ userComm.getProviders().get(p).getServiceDescription());
				listItem++;
			}
		}
		System.out.println("----------------------------------------------------------------------");
		System.out.println("0. Return to previous menu");
		System.out.println("\nMake a selection:");
		int selection = getValueFromUser(0, listItem, "Oops, enter a value from the menu.");
		//Act on the user's selection
		processProviderMenu(selection, userComm, selection - START_PROVIDERS);
	}
	
	/**
	 * acts on the user's menu selection to either become a provider or schedule an existing provider
	 * @param selection
	 */
	private static void processProviderMenu(int selection, Community userComm, int provIndex) {
		switch(selection) {
		case MENU_EXIT:
			break;
		case 1:
			createServiceProvider(userComm);
			System.out.println("Becoming a provider........SUCCESS!");
			//TODO: Need to send to a separate method to get the user's service provider information
			//TODO: Need to verify NOT ALREADY A PROVIDER
			
			//Convert commIndex which is a USER community index back to a COMMUNITY-level index
			//GET THE COMMUNITY INDEX FROM THE USER LIST OF COMMUNITIES
			//Add the service provider to the community
//			communities.get(commIndex).getProviders().add(
//				new ServiceProvider(users.get(currentUserId).getUsername(), "SERVICE" + users.get(currentUserId).getUsername(), "PHONE", 111.11));
//			addServiceProviderSchedules(commIndex, communities.get(commIndex).getProviders().size() - 1);
			break;
		case 2:
			System.out.println("\nUpdate provider information...COMING SOON!");
			break;
		default:
			System.out.println("Provider selected: "
				+ userComm.getProviders().get(provIndex).getServiceDescription());
			showScheduleProviderMenu(userComm.getProviders().get(provIndex));
		}
	}
	
	/**
	 * creates a new service provider from an existing user
	 * TODO: Need to update this to be similar to creating a user with
	 * validation for all fields
	 */
	private static void createServiceProvider(Community currentComm) {
		//Get a list of all service types from the services table
		//User picks a service type from the list
		//IF (userId, communityId, serviceId) matches an entry in the user_comm_service table, then alert
		//user that they already offer that service and terminate the method
		boolean serviceAlreadyExists = true;
		//Make a list of service categories from the services table
		DataSource ds = new DataSource();
		List<ServiceProvider> serviceCategories = ds.dbGetServiceCategories();
		if(serviceCategories != null) {
			//Print all the service categories
			for(int i = 0; i < serviceCategories.size(); i++) {
				System.out.println(serviceCategories.get(i).getServiceId() + ": "
					+ serviceCategories.get(i).getServiceCategory());
			}
			System.out.println("----------------------------------------------------------------------");
			System.out.println("0. Return to previous menu");
			//Get user selection from the list
			System.out.println("Choose service category from list:");
			
			//The serviceId the user will select from the list of categories and descriptions
			int serviceId = 0;
			//The serviceCategories list index that maps to the serviceId the user selects from the list
			int listIndex = 0;
			//Verify the user enters a valid id from the category list
			//and that the user is not already a provider of the chosen service category for the community
			boolean keepGoing = false;
			do {
				listIndex = 0;
				keepGoing = true;
				serviceId = Controller.getValueFromUser(0, 600, "Oops, enter a value from the list");
				if(serviceId != MENU_EXIT) {
					for(int i = 0; i < serviceCategories.size(); i++) {
						if(serviceCategories.get(i).getServiceId() == serviceId) {
							keepGoing = false;
							listIndex = i;
							break;
						}
					}
					if(keepGoing) {
						System.err.println("\nEnter a value from the list...try again.");
					}
					else {
						//Check to see if the user-community-service is unique
						serviceAlreadyExists = ds.dbCheckUserCommServiceExists(currentUserId, currentComm.getCommunityId(), serviceId);
						if(serviceAlreadyExists) {
							System.out.println("\nOops, you already provide the service for " + currentComm.getCommunityName());
							keepGoing = true;
						}
						else {
							keepGoing = false;
						}
					}
				}
				else {
					keepGoing = false;
				}
			} while(keepGoing);
			
			//If service is available in this community for this user, get the rest of the information from the user
			if(!serviceAlreadyExists) {
				//Get the service description from the provider
				String serviceDescription;
				final int MIN_LEN = 1;
				final int MAX_LEN = 300;
				keepGoing = false;
				do {
					keepGoing = false;
					System.out.println("Enter a description of the service (1 to 300 characters): ");
					serviceDescription = scan.nextLine();
					if(serviceDescription.length() < MIN_LEN || serviceDescription.length() > MAX_LEN) {
						System.err.println("\nOops, service description cannot be blank or more than 300 characters.");
						keepGoing = true;
					}
				} while(keepGoing);
				
				//Get phone number for the service provider (must match regex pattern for phone number)
				String phoneNumber = "";
				String phoneRegex = "([0-9]{3})[-]([0-9]{3})[-]([0-9]{4})";
				Pattern phonePattern = Pattern.compile(phoneRegex);
				do {
					keepGoing = false;
					System.out.println("Enter provider phone number (xxx-xxx-xxxx): ");
					phoneNumber = scan.nextLine();
					Matcher phoneMatch = phonePattern.matcher(phoneNumber);
					if(!phoneMatch.matches()) {
						System.err.println("\n Oops, enter phone number as xxx-xxx-xxxx");
						keepGoing = true;
					}
				} while(keepGoing);
				
				//Get the service cost (assumed to be $ per hour)
				System.out.println("Enter cost of service ($/hr): ");
				double servicePrice = getValueFromUser(0.0, Double.POSITIVE_INFINITY, "Oops, enter a value greater than zero");
				
				//Write the new service to the appropriate database tables
				ServiceProvider p = new ServiceProvider();
				p.setServiceId(serviceCategories.get(listIndex).getServiceId());
				p.setServiceCategory(serviceCategories.get(listIndex).getServiceCategory());
				p.setServiceDescription(serviceDescription);
				p.setPhoneNumber(phoneNumber);
				p.setServicePrice(servicePrice);
				int numProvidersAdded = ds.dbCreateServiceProvider(currentUserId, currentComm.getCommunityId(), p);
			}
		}
		ds.close();
	}
	
	/**
	 * allows the user to schedule a time with the selected service provider
	 * @param p the SeriveProvider object for retrieving the schedule
	 */
	public static void showScheduleProviderMenu(ServiceProvider p) {
		//WHAT TO DO HERE???
		//List available times for the provider
		//Get the user's selection
		//Remove the scheduled time from the provider's schedule
		//Tell the user that the provider is scheduled
		//Alert the provider???
		//method stack will go back to showCommunityMenu where the user can
		//create a new community or go into another community
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.println("⚪⚪ 	PROVIDER SCHEDULE FOR " + p.getServiceDescription() + "    ⚪⚪");
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");

		int numSlots = p.getAvailableTimeSlots().size();
		for(int slot = 0; slot < numSlots; slot++) {
			System.out.println((slot + 1) + ". " + p.getAvailableTimeSlots().get(slot));
		}
		//Select a time slot
		System.out.println("----------------------------------------------------------------------");
		System.out.println("0. Return to previous menu");
		System.out.println("\nMake a selection:");
		int selection = getValueFromUser(0, numSlots, "Oops, enter a value from the menu.");
		
		//Process the user's selected time slot
		if(selection != MENU_EXIT) {
			processScheduleProvider(selection - 1, p);
		}
	}
	
	/**
	 * processes the schedule request for a service provider
	 * @param p
	 */
	public static void processScheduleProvider(int index, ServiceProvider p) {
		System.out.println("\nProcessing the schedule request for " + p.getServiceDescription() + "...");
        System.out.println("Scheduling for: " + p.getAvailableTimeSlots().get(index) + "...SUCCESS!");
        
        //Remove the scheduled item from the available date/time slots
        p.getAvailableTimeSlots().remove(index);
	}
	
	/**
	 * checks to see if the user entry is a double value or not
	 * @param str
	 * @return true if double, false if not
	 */
	boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
	
	/**
	 * generates a unique access number for a new community that all users
	 * who are part of the community must use to access it
	 * @return random access number
	 */
	private static String generateUniqueAccessNumber() {
		String rand = "" + ((int) (Math.random() * 89999) + 10000);
		for (int i = 0; i < communities.size(); i++) {
			if (rand == communities.get(i).getAccess()) {
				rand = generateUniqueAccessNumber();
			}
		}
		return rand;
	}
	
	/**
	 * creates a list of day-times for scheduling a service provider
	 * TODO: make this interactive so service providers can set their own schedules
	 */
	public static void addServiceProviderSchedules(int commIndex, int provIndex) {
        communities.get(commIndex).getProviders().get(provIndex).getAvailableTimeSlots().add("Monday 8am-12pm");
        communities.get(commIndex).getProviders().get(provIndex).getAvailableTimeSlots().add("Monday 1pm-4pm");
        communities.get(commIndex).getProviders().get(provIndex).getAvailableTimeSlots().add("Tuesday 8am-12pm");
        communities.get(commIndex).getProviders().get(provIndex).getAvailableTimeSlots().add("Tuesday 1pm-4pm");
        communities.get(commIndex).getProviders().get(provIndex).getAvailableTimeSlots().add("Wednesday 8am-12pm");
        communities.get(commIndex).getProviders().get(provIndex).getAvailableTimeSlots().add("Wednesday 1pm-4pm");
        communities.get(commIndex).getProviders().get(provIndex).getAvailableTimeSlots().add("Thursday 8am-12pm");
        communities.get(commIndex).getProviders().get(provIndex).getAvailableTimeSlots().add("Thursday 1pm-4pm");
        communities.get(commIndex).getProviders().get(provIndex).getAvailableTimeSlots().add("Friday 8am-12pm");
        communities.get(commIndex).getProviders().get(provIndex).getAvailableTimeSlots().add("Friday 1pm-4pm");
        communities.get(commIndex).getProviders().get(provIndex).getAvailableTimeSlots().add("Saturday 8am-12pm");
        communities.get(commIndex).getProviders().get(provIndex).getAvailableTimeSlots().add("Saturday 1pm-4pm");
        communities.get(commIndex).getProviders().get(provIndex).getAvailableTimeSlots().add("Sunday 8am-12pm");
        communities.get(commIndex).getProviders().get(provIndex).getAvailableTimeSlots().add("Sunday 1pm-4pm");
    }
	
	/**
	 * helper method that gets an integer between minValue and maxValue from the user
	 * If the user enters anything other than an integer, catches the exception
	 * and prints the error message received from the method call
	 * @param minValue the minimum value of the menu
	 * @param maxValue the maximum value of the menu
	 * @param errorMessage the error message to display for an invalid entry
	 * @return the integer value the user entered
	 */
	public static int getValueFromUser(int minValue, int maxValue, String errorMessage) {
		int userValue = 0;
		boolean invalidSelection;
		
		//Loop until the user enters an integer between the given limits
		do {
			invalidSelection = false;
			try {
				userValue = scan.nextInt();
				if(userValue < minValue || userValue > maxValue) {
					showErrorMessage(errorMessage);
					invalidSelection = true;
				}
			}
			catch(InputMismatchException e) {
				showErrorMessage(errorMessage);
				invalidSelection = true;
				scan.nextLine();
			}
		} while(invalidSelection);

		//scan the next line to clear out the newline character before returning
		scan.nextLine();
		
		return userValue;
	}	

	/**
	 * helper method that gets an integer between minValue and maxValue from the user
	 * If the user enters anything other than an integer, catches the exception
	 * and prints the error message received from the method call
	 * @param minValue the minimum value of the menu
	 * @param maxValue the maximum value of the menu
	 * @param errorMessage the error message to display for an invalid entry
	 * @return the double value the user entered
	 */
	public static double getValueFromUser(double minValue, double maxValue, String errorMessage) {
		double userValue = 0;
		boolean invalidSelection;
		
		//Loop until the user enters an integer between the given limits
		do {
			invalidSelection = false;
			try {
				userValue = scan.nextDouble();
				if(userValue < minValue || userValue > maxValue) {
					showErrorMessage(errorMessage);
					invalidSelection = true;
				}
			}
			catch(InputMismatchException e) {
				showErrorMessage(errorMessage);
				invalidSelection = true;
				scan.nextLine();
			}
		} while(invalidSelection);

		//scan the next line to clear out the newline character before returning
		scan.nextLine();
		
		return userValue;
	}	
	
	/**
	 * shows the cash error message when user enters the wrong type of cash
	 * @param message the error message to display
	 */
	public static void showErrorMessage(String message) {
		System.out.println("\n" + message);		
	}
	
//	//Make data for testing purposes
//	public static void makeTestData() {
//		//Create USER 0
//		users.add(new User("Roy", "Chancellor", "rc", "abc123", "rc@rc.com"));
//		//Create COMM 0 with USER 0 as the admin
//		communities.add(new Community(0, "Test1", users.get(0), "12345"));
//		//Add COMM 0 to USER 0
//		users.get(0).getCommunities().add(communities.get(0));
//		//Add USER 0 to COMM 0
//		communities.get(0).getUsers().add(users.get(0));
//		
//		//Create USER 1
//		users.add(new User("Adam", "Sandler", "as", "abc123", "as@as.com"));
//		//Add COMM 0 to USER 1
//		users.get(1).getCommunities().add(communities.get(0));
//		//Add USER 1 to COMM 0
//		communities.get(0).getUsers().add(users.get(1));
//		//Make USER 1 a SP in COMM 0
//		communities.get(0).getProviders().add(
//			new ServiceProvider(users.get(1).getUsername(), "Adam's Improv", "123-456-7890", 59));
//		addServiceProviderSchedules(0, 0);
//		
//		//Create USER 2
//		users.add(new User("Tina", "Fey", "tf", "abc123", "rtfg@tf.com"));
//		//Add COMM 0 to USER 2
//		users.get(2).getCommunities().add(communities.get(0));
//		//Add USER 2 to COMM 0
//		communities.get(0).getUsers().add(users.get(2));
//		
//		//Create USER 3
//		users.add(new User("Randy", "Johnson", "rj", "abc123", "rj@rj.com"));
//		//Create COMM 1 with USER 3 as the admin
//		communities.add(new Community(1, "Test2", users.get(3), "12345"));
//		//Add COMM 1 to USER 3
//		users.get(3).getCommunities().add(communities.get(1));
//		//Add USER 3 to COMM 1
//		communities.get(1).getUsers().add(users.get(3));
//		
//		//Create USER 4
//		users.add(new User("Curt", "Schilling", "cs", "abc123", "cs@cs.com"));
//		//Add COMM 1 to USER 4
//		users.get(4).getCommunities().add(communities.get(1));
//		//Add USER 4 to COMM 1
//		communities.get(1).getUsers().add(users.get(4));
//		//Make USER 4 a SP in COMM 1
//		communities.get(1).getProviders().add(
//			new ServiceProvider(users.get(4).getUsername(), "Curt's Pitching Lessons", "123-456-7890", 119));
//		addServiceProviderSchedules(1, 0);
//		
//		//Create USER 5
//		users.add(new User("Paul", "Goldschmidt", "pg", "abc123", "pg@pg.com"));
//		//Add USER 5 to COMM 1
//		users.get(5).getCommunities().add(communities.get(1));
//		//Add COMM 1 to USER 5
//		communities.get(1).getUsers().add(users.get(5));				
//	}
}
