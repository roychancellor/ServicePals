package cst235.servicepals.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import cst235.servicepals.model.Community;
import cst235.servicepals.model.ServiceProvider;
import cst235.servicepals.model.User;
import cst235.servicepals.utils.DataSource;
import cst235.servicepals.utils.Utils;

//TODO: FUTURE VERSION: NEED TO BE ABLE TO CREATE AN OBJECT OF SERVICEPALS FOR EACH LOGGED-IN USER
//SO MORE THAN ONE USER CAN OPERATE ON COMMUNITIES, ETC.

/**
 * The main controller for the application. All public methods are static
 * so they can be accessed without making a Controller object
 */
//TODO: Move ALL user input and menus into Menus class
public class Controller {
	//Scanner object for use throughout the application
	private static Scanner scan = new Scanner(System.in);
	
	//Class constants
	private static final int MENU_EXIT = 0;
	
	//Class data
	private static int currentUserId = 0;
	private static User currentUser;

	/**
	 * The main menu where the application starts
	 */
	//TODO: Move this into a Menus class to complete the View layer of MVC
	public static void showMainMenu() {		
		int option = MENU_EXIT;
		do {
			String bannerText = "SERVICE PALS 2.0";
			Utils.makeBanner(bannerText);
			System.out.println();
			System.out.println("1. Login to your ServicePals account");
			System.out.println("2. Become a ServicePals member");
			Utils.printSeparator(Utils.NUM_BANNER_CHARS + (bannerText.length() > Utils.NUM_BANNER_CHARS - 4 ? 8 : 0));
			System.out.println("0. Exit ServicePals");
			option = Utils.getValueFromUser(0, 2, "Oops, enter 1 or 2. Enter 0 to exit.");
			processMainMenu(option);
		} while(option != MENU_EXIT);
	}
	
	/**
	 * Acts on the user's main menu selection
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
	 * Logs in a user using username and password (allows up to three attempts)
	 * @return true if user name and password match the database; false if not
	 */
	public static boolean doUserLogin() {
		String bannerText = "SERVICE PALS: USER LOGIN";
		Utils.makeBanner(bannerText);
		boolean credentialsMet = false;
		int numTries = 0;
		final int MAX_TRIES = 3;
		do {
			//TODO: Move this into a Menus class to complete the View layer of MVC
			System.out.println();
			Utils.printSeparator(19);
			System.out.print("| Enter username: |\n");
			Utils.printSeparator(19);
			String username = scan.nextLine();
			System.out.println();
			Utils.printSeparator(19);
			System.out.print("| Enter password: |\n");
			Utils.printSeparator(19);
			String password = scan.nextLine();

			//Call method to check the user's credentials
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
	 * Queries the users database for a user name and password match
	 * @param username the user name of the current attempt to log in
	 * @param password the password of the current attempt to log in
	 * @return true if un/pw match found and false if not
	 */
	public static boolean checkLoginCredentials(String username, String password) {
		//Query the users table for username and password
		DataSource ds = new DataSource();
		int userId = ds.dbGetUserIdFromCredentials(username, password);
		ds.close();
		if(userId > 0) {
			currentUserId = userId;
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Creates a new user of ServicePals
	 */
	//TODO: Move this into a Menus class to complete the View layer of MVC	
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
	//TODO: Move this into a Menus class to complete the View layer of MVC
	private static void showUserMenu() {
		boolean keepRunningMethod = true;
		do {
			String bannerTitle = "SERVICE PALS: USER MENU";
			String bannerText = "WELCOME " + currentUser.getFirstName() + " " + currentUser.getLastName();
			Utils.makeBanner(bannerTitle, bannerText);
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
			
			//Show available communities (if any) to the user
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
			Utils.printSeparator(Utils.NUM_BANNER_CHARS
				+ ((bannerText.length() > Utils.NUM_BANNER_CHARS - 4 || bannerTitle.length() > Utils.NUM_BANNER_CHARS - 4) ? 8 : 0));
			System.out.println(MENU_EXIT + ". Return to the main menu");
			
			System.out.println("\nMake a selection:");
			int selection = Utils.getValueFromUser(0, numUserCommunities + MENU_OFFSET, "Oops, enter a valid menu selection.");
			
			if(selection == MENU_EXIT) {
				keepRunningMethod = false;
			}
			else {
				processUserMenu(selection, MENU_OFFSET);
			}
		} while(keepRunningMethod);
	}
	
	/**
	 * acts on the user's community selection
	 * @param menuSelection the option the user selected
	 * @param MENU_ITEMS_OFFSET the number of menu items before the first community appears in the user menu
	 */
	private static void processUserMenu(int menuSelection, final int MENU_ITEMS_OFFSET) {
		//community list index is the menu selection minus the offset minus 1
		int commListIndex = menuSelection - MENU_ITEMS_OFFSET - 1;
		switch(menuSelection) {
		case MENU_EXIT:
			break;
		case 1:
			doJoinExistingCommunity();
			break;
		case 2:
			doCreateNewCommunity();
			break;
		case 3:
			//TODO: Add ability to delete a community if the current user is the community administrator
			System.err.println("DELETE COMMUNITY: Coming soon...\n");
			break;
		default:
			//show the actions for the community
			showCommunityActionsMenu(currentUser.getCommunities().get(commListIndex));
			break;
		}
	}
	
	/**
	 * joins an existing, logged-in user to an existing community
	 */
	//TODO: Add a login feature to this so user has to enter the community access code
	private static void doJoinExistingCommunity() {
		boolean keepRunningMethod = true;
		do {
			//TODO: Move this into a Menus class to complete the View layer of MVC
			String bannerText = "JOIN EXISTING COMMUNITY";
			Utils.makeBanner(bannerText);
	
			//List all communities for which the current user is NOT already a member
			//OR already the administrator
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
			
			//Finish the menu
			Utils.printSeparator(Utils.NUM_BANNER_CHARS
					+ ((bannerText.length() > Utils.NUM_BANNER_CHARS - 4) ? 8 : 0));
			System.out.println(MENU_EXIT + ". Return to previous menu");
			
			//Get the user's selection
			System.out.println("\nSelect a community number:");
			int listSelection = Utils.getValueFromUser(MENU_EXIT, numAvailableComm,
				"Oops, enter a valid community number or 0 to return to the previous menu.");
			if(listSelection == MENU_EXIT) {
				keepRunningMethod = false;
			}
			else {
				//Add the user to the community
				//TODO: Add a moderator function that approves the user first
				ds.dbInsertIntoUserCommunity(currentUserId, availableCommunities.get(listSelection - 1).getCommunityId());
			}
			ds.close();
		} while(keepRunningMethod);
	}
	
	/**
	 * Creates a new community and the current logged-in user becomes the administrator
	 */
	//TODO: Move this into a Menus class to complete the View layer of MVC
	private static void doCreateNewCommunity() {
		String banner = "CREATE NEW COMMUNITY";
		Utils.makeBanner(banner);
		
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
				System.err.println("\nERROR: TRIED TO ADD COMMUNITY-USER PAIR THAT EXISTS");
			}
		}
		//Close the DataSource connection
		ds.close();
	}

	/**
	 * Gets a community name from the user and verifies it does not already exist in the database
	 * @return the community name
	 */
	private static String getCommunityName() {
		final int MIN_COMMUNITY_NAME_LENGTH = 5;
		String commName;
		DataSource ds = new DataSource();
		boolean commExists = false;
		do {
			commExists = false;
			commName = Utils.getStringMinLength(3,
				"Enter a community name (at least " + MIN_COMMUNITY_NAME_LENGTH + " characters):");
			
			//VERIFY A COMMUNITY WITH THE SAME NAME DOES NOT YET EXIST;
			//IF ONE DOES, REJECT THE REQUEST; IF NOT, MAKE NEW COMMUNITY
			commExists = ds.dbCheckAlreadyExists(commName, 'c');
			if(commExists) {
				System.err.println("\nOops, a community with name " + commName + " already exists. Try again.");
			}
		} while(commExists);
		
		ds.close();
		return commName;
	}
	
	/**
	 * Shows the options a user can perform in each community for which the user is a member
	 * @param commIndex
	 */
	//TODO: Move this into a Menus class to complete the View layer of MVC
	private static void showCommunityActionsMenu(Community userComm) {
		boolean keepRunningMethod = true;
		//Run the method until the user enters the exit value
		do {
			String bannerTitle = "COMMUNITY";
			String bannerText = userComm.getCommunityName();
			Utils.makeBanner(bannerTitle, bannerText);
	
			System.out.println("\n1. Become a provider for this community");
			System.out.println("2. Update your provider information - FUTURE");
			
			//List available service providers for the community
			DataSource ds = new DataSource();
			List<ServiceProvider> userCommProviders = ds.dbRetrieveProvidersByComm(userComm.getCommunityId());
			ds.close();
			
			int listItem = 2;
			final int START_PROVIDERS = 3;
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
			//Finish the menu
			Utils.printSeparator(Utils.NUM_BANNER_CHARS
					+ ((bannerText.length() > Utils.NUM_BANNER_CHARS - 4 || bannerTitle.length() > Utils.NUM_BANNER_CHARS - 4) ? 8 : 0));
			System.out.println("0. Return to previous menu");
			
			//Get user's selection
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
	 * Acts on the user's menu selection to either become a provider or schedule an existing provider
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
	 * Creates a new service provider from an existing user
	 * @param currentComm the current community for the current user
	 */
	private static void createServiceProvider(Community currentComm) {
		//GENERAL ALGORITHM:
		//Get a list of all service categories from the services table
		//User picks a service type from the list
		//IF (userId, communityId, serviceId) matches an entry in the user_comm_service table, then alert
		//user that they already offer that service and terminate the method
		
		//Make a list of service categories from the services table
		DataSource ds = new DataSource();
		List<ServiceProvider> serviceCategories = ds.dbGetServiceCategories();
		
		if(serviceCategories != null) {
			//Get the serviceId from the user
			//and return the index of that service in the list of service categories
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
				double servicePrice = Utils.getValueFromUser(0.0,
					Double.POSITIVE_INFINITY, "Oops, enter a value greater than zero");
				
				//Write the new service information to a new ServiceProvider object
				ServiceProvider provider = new ServiceProvider();
				provider.setServiceId(serviceCategories.get(listIndex).getServiceId());
				provider.setServiceCategory(serviceCategories.get(listIndex).getServiceCategory());
				provider.setServiceDescription(serviceDescription);
				provider.setPhoneNumber(phoneNumber);
				provider.setServicePrice(servicePrice);
				
				//Write the new provider object information to the database
				int numProvidersAdded = ds.dbCreateServiceProvider(
					currentUserId, currentComm.getCommunityId(), provider);
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
	
	/**
	 * Gets the service category from the user for a new service the user wishes to provide.
	 * The list of service categories comes from a database of pre-defined categories
	 * @param currentComm the current community object into which the method adds the service
	 * @param serviceCategories the pre-defined list of service categories
	 * @return the index of the service category list that maps to the category id the user
	 * selects from the list of categories
	 */
	//TODO: Move this into a Menus class to complete the View layer of MVC
	private static int getServiceCategoryFromNewProvider(Community currentComm, List<ServiceProvider> serviceCategories) {
		//Print all the service categories
		String bannerTitle = "SERVICE CATEGORIES";
		Utils.makeBanner(bannerTitle);
		
		System.out.println();
		for(int i = 0; i < serviceCategories.size(); i++) {
			System.out.println(serviceCategories.get(i).getServiceId() + ": "
				+ serviceCategories.get(i).getServiceCategory());
		}
		
		//Finish the menu list
		Utils.printSeparator(Utils.NUM_BANNER_CHARS
				+ ((bannerTitle.length() > Utils.NUM_BANNER_CHARS - 4) ? 8 : 0));
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
			//Get the user's selection
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
						//and exit the loop
						listIndex = i;
						break;
					}
				}
				if(invalidInput) {
					System.err.println("\nEnter a service ID from the list...try again.");
				}
				else {
					//Check to see if the user-community-service is unique
					serviceAlreadyExists = ds.dbCheckUserCommServiceExists(
						currentUserId, currentComm.getCommunityId(), serviceId);
					if(serviceAlreadyExists) {
						System.err.println("\nOops, you already provide the service for "
							+ currentComm.getCommunityName());
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
	 * Allows the user to schedule a time with the selected service provider
	 * @param p the SeriveProvider object for retrieving the schedule
	 */
	public static void showScheduleProviderMenu(ServiceProvider p) {
		//GENERAL ALGORITHM:
		//User enters a date in mm-dd-yyyy format for the appointment with the selected provider

		//Method queries the database and determines which time slots are available and creates
		//a list of available time slots and displays for the user

		//User selects an available time slot from the list

		//Method writes the service provider user_id, service date, and schedule block_id
		//into the user_date_block table which schedules the provider and prevents another
		//schedule request at the same date and time for the provider

		//Alert the user that the appointment is scheduled

		//TODO: Alert the provider??? HOW???

		//method stack will go back to the current community menu where the user can
		//schedule another provider, become a provider, or return to the previous menu
		String bannerText = "SCHEDULE PROVIDER:";
		Utils.makeBanner(bannerText, p.getServiceDescription());
		
		//Get the requested service date from the user in the SQL-friendly form YYYY-MM-DD...
		String requestedDate = Utils.getServiceDate();
		//...but print the data in user-friendly MM-DD-YYYY format
		System.out.println("\nYou requested service on the date "
			+ Utils.convertYYYYMMDDtoMMDDYYYY(requestedDate));
		
		//Get the user id of the provider from user_comm_service table
		//by matching service_id and service_description
		//TODO: Figure out a more efficient way of getting the user id of the service provider???
		DataSource ds = new DataSource();
		int providerUserId = ds.dbGetUserIdFromServiceDescriptionAndId(
			p.getServiceId(), p.getServiceDescription());
		
		//If the database contained the service description / service_id combination,
		//which is should, then the user id will come back > 0
		if(providerUserId > 0) {
			//Make a list of available slots for the user to choose
			//The map hold (time block id, time block description) key-value pairs
			Map<Integer, String> timeBlocks = new HashMap<Integer, String>();
			
			//Query the database to get available service times for that date, if any
			timeBlocks = ds.dbGetAvailableTimeSlotsByDate(providerUserId, requestedDate);
			
			//Print the time block list for the user
			System.out.println("\nAvailable Time Slots");
			Utils.printSeparator(Utils.NUM_BANNER_CHARS
					+ ((bannerText.length() > Utils.NUM_BANNER_CHARS - 4) ? 8 : 0));
			
			int numSlots = timeBlocks.size();
			if(numSlots == 0) {
				System.out.println("\n\t!!! ALL TIME SLOTS ARE FULL !!!");
			}
			else {
				//Iterate over the map of available time slots by key value
				for(Integer key : timeBlocks.keySet()) {
					System.out.println(key + ". " + timeBlocks.get(key));
				}
			}
			
			//MENU END
			Utils.printSeparator(Utils.NUM_BANNER_CHARS
				+ ((bannerText.length() > Utils.NUM_BANNER_CHARS - 4) ? 8 : 0));
			System.out.println("0. Return to previous menu");
			
			//Select a time slot
			System.out.println("\nMake a selection:");
			int selection = Utils.getValueFromUser(0, numSlots, "Oops, enter a value from the menu.");
			
			//Process the user's selected time slot
			if(selection != MENU_EXIT) {
				processScheduleProvider(p.getServiceDescription(), providerUserId, requestedDate, selection);
			}
		}
		else {
			System.err.println("\nERROR: Unable to find this service in the database.");
		}
		ds.close();
	}
	
	/**
	 * Processes the schedule request in the database (Creates a record)
	 * @param serviceDescription the description of the service provider's service
	 * @param providerUserId the user id of the service provider
	 * @param serviceDate the date of service
	 * @param blockId the id of the service time block
	 */
	public static void processScheduleProvider(String serviceDescription, int providerUserId,
			String serviceDate, int blockId) {
		System.out.print("\nProcessing the schedule request for\n" + serviceDescription);
        System.out.print(" on " + Utils.convertYYYYMMDDtoMMDDYYYY(serviceDate));
        
        //Write the schedule record to the database
        DataSource ds = new DataSource();
        int returnedKey = ds.dbCreateUserDateSchedule(providerUserId, serviceDate, blockId);
        ds.close();
        if(returnedKey > 0) {
        	System.out.println("...SUCCESS!\n");
        }
        else {
        	System.err.println("\nERROR: Unable to write to user_date_block\n");
        }
	}
	
	/**
	 * Generates a unique access number for a new community that all users
	 * who are part of the community must use to access it
	 * @return random access number
	 */
	//TODO: Implement this access number in the community login menu
	private static String generateUniqueAccessNumber() {
		String rand = "" + ((int) (Math.random() * 89999) + 10000);
		for (int i = 0; i < currentUser.getCommunities().size(); i++) {
			if (rand == currentUser.getCommunities().get(i).getAccessCode()) {
				rand = generateUniqueAccessNumber();
			}
		}
		return rand;
	}	
}
