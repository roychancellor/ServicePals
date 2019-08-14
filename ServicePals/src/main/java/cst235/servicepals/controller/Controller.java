package cst235.servicepals.controller;

import java.util.List;
import java.util.Scanner;
import cst235.servicepals.model.Community;
import cst235.servicepals.model.DataSource;
import cst235.servicepals.model.ServiceProvider;
import cst235.servicepals.model.User;
import cst235.servicepals.utils.Utils;

//FUTURE VERSION: NEED TO BE ABLE TO CREATE AN OBJECT OF SERVICEPALS FOR EACH LOGGED-IN USER
//SO MORE THAN ONE USER CAN OPERATE ON COMMUNITIES, ETC.

/**
 * the main controller for the application
 * all public methods are static so they can be accessed without making a Controller object
 */
public class Controller {
	//Scanner object for use throughout the application
	//TODO: Move ALL user input and menus into Menus class
	private static Scanner scan = new Scanner(System.in);
	
	//Class constants
	private static final int MENU_EXIT = 0;
	
	//Class data
	private static int currentUserId = 0;
	private static User currentUser;

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
			option = Utils.getValueFromUser(0, 2, "Oops, enter 1 or 2. Enter 0 to exit.");
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
		case MENU_EXIT:
			break;
		}
	}
	
	/**
	 * logs in a user using user name and password (allows up to three attempts)
	 * @return true if user name and password match; false if not
	 */
	public static boolean doUserLogin() {
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.println("⚪⚪ 	          SERVICE PALS: LOGIN	           ⚪⚪");
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.println("\nEnter username: ");
		String username = scan.nextLine();
		System.out.print("Enter password: ");
		String password = scan.nextLine();

		boolean credentialsMet = false;
		int numTries = 0;
		final int MAX_TRIES = 3;
		do {
			credentialsMet = false;
			if (checkLoginCredentials(username, password)) {
				return true;
			}
			else {
				numTries++;
				System.err.println("Incorrect user name or password. "
					+ (MAX_TRIES - numTries) + " attempts remaining.");
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
		
		//Get the new user's username and validate it does not already exist
		String username = getUsername();
		
		//Unique user validated, so get the rest of the information from the user
		String password = Utils.getStringMinLength(3, "Enter a password -->");		
		String firstName = Utils.getStringMinLength(1, "Enter your first name -->");
		String lastName = Utils.getStringMinLength(1, "Enter your last name -->");
		//Get the user's phone number in the format nnn-nnn-nnnn
		String phoneNumber = Utils.getPhoneNumber();
		//Get the user's e-mail address in the form *@*.cc(cc)
		String emailAddress = Utils.getEmailAddress();
		
		//Create the user in the database
		DataSource ds = new DataSource();
		int userId = ds.dbCreateUser(firstName, lastName, username, password, phoneNumber, emailAddress);
		ds.close();
		System.out.println("Success, created user " + firstName + " " + lastName + " with user id #" + userId + "\n");
		//Set the currentUser to a new User object
		currentUser = new User(firstName, lastName, username, password, emailAddress);
	}
	
	/**
	 * Gets a new user's username and verifies it does not already exist in the database
	 * @return the new user's username
	 */
	private static String getUsername() {
		DataSource ds = new DataSource();
		boolean userAlreadyExists = false;
		String username = "";
		do {
			userAlreadyExists = false;
			System.out.println("\nEnter a user name:");
			username = scan.nextLine();
			//Check to see if user name already exists
			if((userAlreadyExists = ds.dbCheckAlreadyExists(username, 'u')) == true) {
				System.out.println("\nUsername already exists, choose another one");
			}
		} while(userAlreadyExists);
		
		ds.close();
		return username;
	}
	
	/**
	 * the menu each user will see to access their communities
	 * @param validatedUser a User object whose user name and password are validated
	 */
	private static void showUserMenu() {
		boolean keepRunningMethod = true;
		do {
			System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
			System.out.println("⚪⚪ 	        SERVICE PALS USER MENU	           ⚪⚪");
			System.out.println("⚪⚪ 	     WELCOME " + currentUser.getFirstName() + " " + currentUser.getLastName() + "          ⚪⚪");
			System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
			System.out.println();
			//Show available options for the logged-in user
			System.out.println("1. Join an existing community");
			System.out.println("2. Create a new community");
			System.out.println("3. Delete an existing community (COMING SOON)");
			final int MENU_OFFSET = 3;
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
						System.out.println((c + MENU_OFFSET + 1) + ". Enter community "
							+ currentUser.getCommunities().get(c).getCommunityName());
						//TODO Add a feature that shows "A" for communities for which the user is also an admin
					}
				}
			}
			//Finish the menu
			System.out.println("----------------------------------------------------------------------");
			System.out.println(MENU_EXIT + ". Return to the main menu");
			System.out.println("\nMake a selection:");
			int selection = Utils.getValueFromUser(0, numUserCommunities + MENU_OFFSET, "Oops, enter a valid menu selection.");
			if(selection == MENU_EXIT) {
				keepRunningMethod = false;
			}
			else {
				processUserMenu(selection);
			}
		} while(keepRunningMethod);
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
			System.err.println("DELETE COMMUNITY: Coming soon...\n");
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
		boolean keepRunningMethod = true;
		do {
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
			int listSelection = Utils.getValueFromUser(MENU_EXIT, numAvailableComm,
				"Oops, enter a valid community number or 0 to return to the previous menu.");
			if(listSelection == MENU_EXIT) {
				keepRunningMethod = false;
			}
			else {
				ds.dbInsertIntoUserCommunity(currentUserId, availableCommunities.get(listSelection - 1).getCommunityId());
			}
			ds.close();
		} while(keepRunningMethod);
	}
	
	/**
	 * creates a new community and the current logged-in user becomes the administrator
	 */
	private static void doCreateNewCommunity() {
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.println("⚪⚪ 	         CREATE NEW COMMUNITY             ⚪⚪");
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		
		//Get the community name from the user and validate it is unique
		String commName = getCommunityName();
		
		//Now that community name is validated to be unique, create the community
		//with the current user as the admin, then add the user and community to the user-community table
		String accessCode = generateUniqueAccessNumber();
		
		//Make new community in the database
		DataSource ds = new DataSource();
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
	 * Gets a community name from the suer and verifies it does not already exist in the database
	 * @return the community name
	 */
	private static String getCommunityName() {
		final int MIN_COMMUNITY_NAME_LENGTH = 5;
		String commName;
		DataSource ds = new DataSource();
		boolean commExists = false;
		do {
			commExists = false;
			commName = Utils.getStringMinLength(3, "Enter a community name (at least " + MIN_COMMUNITY_NAME_LENGTH + " characters):");
			
			//VERIFY A COMMUNITY WITH THE SAME NAME DOES NOT YET EXIST;
			//IF ONE DOES, REJECT THE REQUEST; IF NOT, MAKE NEW COMMUNITY
			commExists = ds.dbCheckAlreadyExists(commName, 'c');
			if(commExists) {
				System.out.println("\nOops, a community with name " + commName + " already exists. Try again.");
			}
		} while(commExists);
		
		ds.close();
		return commName;
	}
	
	/**
	 * shows the options a user can perform in each community for which the user is a member
	 * @param commIndex
	 */
	private static void showCommunityActionsMenu(Community userComm) {
		boolean keepRunningMethod = true;
		//Run the method until the user enters the exit value
		do {
			System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
			System.out.println("⚪⚪ COMMUNITY " + userComm.getCommunityName() + " ⚪⚪");
			System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
	
			System.out.println("\n1. Become a provider for this community");
			System.out.println("2. Update your provider information for this community");
			//List available service providers for the community
			final int START_PROVIDERS = 3;
			DataSource ds = new DataSource();
			List<ServiceProvider> userCommProviders = ds.dbRetrieveProvidersByComm(userComm.getCommunityId());
			ds.close();
			int listItem = 2;
			if(userCommProviders != null) {
				listItem = START_PROVIDERS;
				userComm.setProviders(userCommProviders);
				for(int p = 0; p < userComm.getProviders().size(); p++) {
					System.out.println(listItem + ". Schedule provider: "
						+ userComm.getProviders().get(p).getServiceDescription());
					listItem++;
				}
				//Reverse the final increment in the for loop which goes one beyond the total providers
				listItem--;
			}
			System.out.println("----------------------------------------------------------------------");
			System.out.println("0. Return to previous menu");
			System.out.println("\nMake a selection:");
			int selection = Utils.getValueFromUser(0, listItem, "Oops, enter a value from the menu.");
			if(selection == MENU_EXIT) {
				keepRunningMethod = false;
			}
			else {
				//Act on the user's selection
				processProviderMenu(selection, userComm, selection - START_PROVIDERS);
			}
		} while(keepRunningMethod);
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
			break;
		case 2:
			System.err.println("\nUpdate provider information...COMING SOON!\n");
			break;
		default:
			System.out.println("Provider selected: "
				+ userComm.getProviders().get(provIndex).getServiceDescription());
			showScheduleProviderMenu(userComm.getProviders().get(provIndex));
		}
	}
	
	/**
	 * creates a new service provider from an existing user
	 * @param currentComm the current community for the current user
	 */
	private static void createServiceProvider(Community currentComm) {
		//GENERAL ALGORITHM:
		//Get a list of all service types from the services table
		//User picks a service type from the list
		//IF (userId, communityId, serviceId) matches an entry in the user_comm_service table, then alert
		//user that they already offer that service and terminate the method
		
		//Make a list of service categories from the services table
		DataSource ds = new DataSource();
		List<ServiceProvider> serviceCategories = ds.dbGetServiceCategories();
		if(serviceCategories != null) {
			//Get the serviceId from the user and return the index of that service in the list of service categories
			int listIndex = getServiceCategoryFromNewProvider(currentComm, serviceCategories);
			
			//If the user elected not to exit, get the rest of the information about the service
			//and write the service to the database
			if(listIndex >= 0) {
				//Get the service description from the provider
				String serviceDescription = getServiceDescription();
				
				//Get phone number for the service provider (must match regex pattern for phone number)
				String phoneNumber = Utils.getPhoneNumber();
				
				//Get the service cost (assumed to be $ per hour)
				System.out.println("\nEnter cost of service ($/hr): ");
				double servicePrice = Utils.getValueFromUser(0.0, Double.POSITIVE_INFINITY, "Oops, enter a value greater than zero");
				
				//Write the new service to the appropriate database tables
				ServiceProvider p = new ServiceProvider();
				p.setServiceId(serviceCategories.get(listIndex).getServiceId());
				p.setServiceCategory(serviceCategories.get(listIndex).getServiceCategory());
				p.setServiceDescription(serviceDescription);
				p.setPhoneNumber(phoneNumber);
				p.setServicePrice(servicePrice);
				int numProvidersAdded = ds.dbCreateServiceProvider(currentUserId, currentComm.getCommunityId(), p);
				if(numProvidersAdded > 0) {
					System.out.println("\nSUCCESS...you are added as a provider\n");
				}
				else {
					System.out.println("\nSYSTEM ERROR: UNABLE TO WRITE NEW PROVIDER TO DATABASE");
				}
			}
		}
		ds.close();
	}
	
	private static int getServiceCategoryFromNewProvider(Community currentComm, List<ServiceProvider> serviceCategories) {
		//Print all the service categories
		System.out.println("\nAvailable Service Categories");
		System.out.println("---------------------------------------");
		for(int i = 0; i < serviceCategories.size(); i++) {
			System.out.println(serviceCategories.get(i).getServiceId() + ": "
				+ serviceCategories.get(i).getServiceCategory());
		}
		System.out.println("---------------------------------------");
		System.out.println("0. Return to previous menu");
		//The serviceId the user will select from the list of categories and descriptions
		int serviceId = 0;
		//The serviceCategories list index that maps to the serviceId the user selects from the list
		int listIndex = 0;
		//flag whether the user already provides the service for the community (assume true until found otherwise)
		boolean serviceAlreadyExists = true;
		//Verify the user enters a valid id from the category list
		//and that the user is not already a provider of the chosen service category for the community
		boolean invalidInput = false;
		//Create a DataSource object
		DataSource ds = new DataSource();
		//Loop until the user enters:
		//(1) a correct category from the list and
		//(2) is not already a provider for that service in this community
		do {
			//Get user selection from the list
			System.out.println("\nChoose service category from list:");
			
			listIndex = 0;
			serviceId = Utils.getValueFromUser(0, 600, "Oops, enter a value in the range of the list...try again.");
			if(serviceId == MENU_EXIT) {
				//force an exit from the do-while loop
				invalidInput = false;
				serviceAlreadyExists = false;
				listIndex = -1;
			}
			else {
				//Verify the user entered a serviceId from the list
				invalidInput = true;
				for(int i = 0; i < serviceCategories.size(); i++) {
					if(serviceCategories.get(i).getServiceId() == serviceId) {
						invalidInput = false;
						//Set the listIndex to the current index when the serviceId is found
						listIndex = i;
						break;
					}
				}
				if(invalidInput) {
					System.err.println("\nEnter a service ID from the list...try again.");
				}
				else {
					//Check to see if the user-community-service is unique
					serviceAlreadyExists = ds.dbCheckUserCommServiceExists(currentUserId, currentComm.getCommunityId(), serviceId);
					if(serviceAlreadyExists) {
						System.err.println("\nOops, you already provide the service for " + currentComm.getCommunityName());
					}
				}
			}
		} while(invalidInput || serviceAlreadyExists);
		
		ds.close();
		return listIndex;
	}
	
	/**
	 * Gets the description of the provider's service, validated to be the correct length
	 * @return description of service
	 */
	private static String getServiceDescription() {
		final int MIN_LEN = 1;
		final int MAX_LEN = 300;
		boolean keepGoing = false;
		String serviceDescription = "";
		do {
			keepGoing = false;
			System.out.println("\nEnter a description of the service (" + MIN_LEN + " to " + MAX_LEN + " characters): ");
			serviceDescription = scan.nextLine();
			if(serviceDescription.length() < MIN_LEN || serviceDescription.length() > MAX_LEN) {
				System.err.println("\nOops, service description cannot be blank or more than 300 characters.");
				keepGoing = true;
			}
		} while(keepGoing);
		
		return serviceDescription;
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
		int selection = Utils.getValueFromUser(0, numSlots, "Oops, enter a value from the menu.");
		
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
        System.out.println("Scheduling for: " + p.getAvailableTimeSlots().get(index) + "...SUCCESS!\n");
        
        //Remove the scheduled item from the available date/time slots
        p.getAvailableTimeSlots().remove(index);
	}
	
	/**
	 * generates a unique access number for a new community that all users
	 * who are part of the community must use to access it
	 * @return random access number
	 */
	private static String generateUniqueAccessNumber() {
		String rand = "" + ((int) (Math.random() * 89999) + 10000);
		for (int i = 0; i < currentUser.getCommunities().size(); i++) {
			if (rand == currentUser.getCommunities().get(i).getAccess()) {
				rand = generateUniqueAccessNumber();
			}
		}
		return rand;
	}
	
	/**
	 * creates a list of day-times for scheduling a service provider
	 * TODO: make this interactive so service providers can set their own schedules
	 */
	public static void addServiceProviderSchedules(Community comm, int provIndex) {
        comm.getProviders().get(provIndex).getAvailableTimeSlots().add("Monday 8am-12pm");
        comm.getProviders().get(provIndex).getAvailableTimeSlots().add("Monday 1pm-4pm");
        comm.getProviders().get(provIndex).getAvailableTimeSlots().add("Tuesday 8am-12pm");
        comm.getProviders().get(provIndex).getAvailableTimeSlots().add("Tuesday 1pm-4pm");
        comm.getProviders().get(provIndex).getAvailableTimeSlots().add("Wednesday 8am-12pm");
        comm.getProviders().get(provIndex).getAvailableTimeSlots().add("Wednesday 1pm-4pm");
        comm.getProviders().get(provIndex).getAvailableTimeSlots().add("Thursday 8am-12pm");
        comm.getProviders().get(provIndex).getAvailableTimeSlots().add("Thursday 1pm-4pm");
        comm.getProviders().get(provIndex).getAvailableTimeSlots().add("Friday 8am-12pm");
        comm.getProviders().get(provIndex).getAvailableTimeSlots().add("Friday 1pm-4pm");
        comm.getProviders().get(provIndex).getAvailableTimeSlots().add("Saturday 8am-12pm");
        comm.getProviders().get(provIndex).getAvailableTimeSlots().add("Saturday 1pm-4pm");
        comm.getProviders().get(provIndex).getAvailableTimeSlots().add("Sunday 8am-12pm");
        comm.getProviders().get(provIndex).getAvailableTimeSlots().add("Sunday 1pm-4pm");
    }
	
}
