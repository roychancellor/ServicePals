package cst235.servicepals.controller;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import cst235.servicepals.model.Community;
import cst235.servicepals.model.ServiceProvider;
import cst235.servicepals.model.User;

//NEED TO BE ABLE TO CREATE AN OBJECT OF SERVICEPALS FOR EACH LOGGED-IN USER
//SO MORE THAN ONE USER CAN OPERATE ON COMMUNITIES, ETC.

/**
 * the main controller for the application
 */
public class Controller {
	//Scanner object for use throughout the application
	public static Scanner scan = new Scanner(System.in);
	//Class constants
	private static final int MENU_EXIT = 0;
	
	//Class data
	//The master list of communities for the application
	private static List<Community> communities = new ArrayList<Community>();
	
	//The master list of users for the application
	private static List<User> users = new ArrayList<User>();
	
	//The master list of providers for the application
	//How to handle providers here??? Are they like users???
	//I DON'T THINK PROVIDERS IS NEEDED HERE SINCE PROVIDERS ARE USERS
	//AND COMMUNITIES HAVE PROVIDERS
	//private static List<ServiceProvider> providers = new ArrayList<ServiceProvider>();
	
	private static int currentUserIndex = 0;
	
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
			option = getIntFromUser(0, 2, "Oops, enter 1 or 2. Enter 0 to exit.");
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
				showUserMenu(users.get(currentUserIndex));
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
		System.out.print("\nEnter password: ");
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
	 * scans the list of users to looks for a user name and password match
	 * @param username the user name of the current attempt to log in
	 * @param password the password of the current attempt to log in
	 * @return true if un/pw match found and false if not
	 */
	public static boolean checkLoginCredentials(String username, String password) {
		for (int i = 0; i < users.size(); i++) {
			if(username.equals(users.get(i).getUsername()) &&
				password.equals(users.get(i).getPassword())) {
				currentUserIndex = i;
				System.out.println("Correct login credentials!");
				return true;
			}
		}
		return false;
	}
	
	/**
	 * the menu each user will see to access their communities
	 * @param validatedUser a User object whose user name and password are validated
	 */
	private static void showUserMenu(User validatedUser) {
		boolean keepRunning = true;
		do {
			System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
			System.out.println("⚪⚪ 	          SERVICE PALS	                  ⚪⚪");
			System.out.println("⚪⚪ 	   WELCOME " + validatedUser.toString() + "	                  ⚪⚪");
			System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
			System.out.println();
			//Show available communities to the user
			System.out.println("1. Join an existing community");
			System.out.println("2. Create a new community");
			int numUserCommunities = validatedUser.getCommunities().size();
			if(numUserCommunities > 0) {
				System.out.println("\nSelect an existing community:");
				for(int c = 0; c < numUserCommunities; c++) {
					System.out.println((c + 3) + ". " + validatedUser.getCommunities().get(c).toString());
					//Add a feature that shows "A" for communities for which the user is also an admin
				}
			}
			System.out.println(MENU_EXIT + ". Return to the main menu");
			System.out.println("\nMake a selection:");
			int selection = getIntFromUser(0, numUserCommunities + 2, "Oops, enter a valid menu selection.");
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
		switch(selection) {
		case 1:
			doJoinExistingCommunity();
			break;
		case 2:
			doCreateNewCommunity();
			break;
		case MENU_EXIT:
			break;
		default:
			System.out.println("Accessing community index" + (selection - 1));
			break;
		}
		if(selection != MENU_EXIT) {
			showCommunityActionsMenu(selection);
		}
	}
	
	/**
	 * creates a new user of ServicePals
	 */
	private static void doCreateUser() {
		System.out.println("\nCreating user.....");
	}

	/**
	 * creates a new community and the current logged-in user becomes the administrator
	 */
	private static void doCreateNewCommunity() {
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.println("⚪⚪ 	         CREATE NEW COMMUNITY             ⚪⚪");
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.print("\nENTER A COMMUNITY NAME (must be at least 5 characters)\nNAME: ");
		String name = scan.nextLine();
		while(name.length() < 5) {
			System.err.println("COMMUNITY NAME MUST BE AT LEAST 5 CHARACTERS LONG");
			name = scan.nextLine();
		}
		String accessCode = generateUniqueAccessNumber();
		users.get(currentUserIndex).getCommunities().add(new Community(users.get(currentUserIndex), accessCode));
//		Community.current = communities.get(communities.size() - 1);
//
//		System.out.println(Community.current.getAdminName() + ", " + Community.current.getAccess());
//		if (checkCode(access))
//			serviceMenu();
	}

	/**
	 * joins an existing, logged-in user to an existing community
	 */
	private static void doJoinExistingCommunity() {
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.println("⚪⚪ 	       JOIN EXISTING COMMUNITY             ⚪⚪");
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");

		//List all available communities
		System.out.println("\nAvailable communities:");
		int numAvailableComm = communities.size();
		if(numAvailableComm > 0) {
			for(int c = 0; c < numAvailableComm; c++) {
				System.out.println(c + ". " + communities.get(c));
			}
		}
		else {
			System.out.println("\nNo communities currently available.");
		}
		System.out.println(MENU_EXIT + ". Return to previous menu");
		System.out.println("\nSelect a community:");
		int selection = getIntFromUser(MENU_EXIT, numAvailableComm, "Oops, enter a valid community number or 0 to return.");
		processJoinCommunityRequest(selection);
	}
	
	/**
	 * Adds a user to the selected community
	 * @param selection
	 */
	private static void processJoinCommunityRequest(int selection) {
		//Admin will need to add the user to the community
		System.out.println("\n<User>, you have been added to " + communities.get(selection - 1).toString());
	}
	
	/**
	 * shows the options a user can perform in each community for which the user is a member
	 * @param menuSelection
	 */
	private static void showCommunityActionsMenu(int menuSelection) {
		System.out.println("Showing community " + communities.get(menuSelection - 1).toString() + " menu...");
		//List available providers
		//Add provider??? How will providers get added to communities?
		//Select a provider
		
		//0. Return to community menu
		//Get option from user
		int providerSelection = 0;
		processProviderMenu(communities.get(menuSelection - 1), providerSelection);
	}
	
	/**
	 * shows the provider the user selected and calls the provider's schedule
	 * @param c
	 * @param providerSelection
	 */
	private static void processProviderMenu(Community c, int providerSelection) {
		if(providerSelection != MENU_EXIT) {
			System.out.println("Showing provider " + c.getProviders().get(providerSelection - 1));
			showScheduleProviderMenu(c.getProviders().get(providerSelection - 1));
		}
	}
	
	/**
	 * allows the user to schedule a time with the selected service provider
	 * @param p the SeriveProvider object for retrieving the schedule
	 */
	public static void showScheduleProviderMenu(ServiceProvider p) {
		//List available times for the provider
		//Get the user's selection
		//WHAT TO DO HERE???
		//Remove the scheduled time from the provider's schedule
		//Tell the user that the provider is scheduled
		//Alert the provider???
		//method stack will go back to showCommunityMenu where the user can
		//create a new community or go into another community
		System.out.println("Scheduling for provider " + p.toString());
        System.out.println("Questions? call " + p.getFirstName() + " " + p.getLastName() + " " + p.getPhoneNumber() );
        int selection = 0;
        processScheduleProvider(selection, p);
	}
	
	/**
	 * processes the schedule request for a service provider
	 * @param p
	 */
	public static void processScheduleProvider(int selection, ServiceProvider p) {
		System.out.println("Processing the schedule request for " + p.toString());
        System.out.println("Scheduling for: " + p.getAvailable().get(selection - 1));
        //Remove the scheduled item from the available date/time slots
        p.getAvailable().remove(selection - 1);
	}
	
	public void displayProvider() {
		// scan.nextLine();
		System.out.println("Select a service provider: ");
		int x = 1;
		List<ServiceProvider> p = new ArrayList<>();
		System.out.println("0.) GO BACK");

//		for (int i = 0; i < Community.current.providers.size(); i++) {
//			System.out.println((x) + ".) " + Community.current.providers.get(i).getServiceName() + " >  "
//					+ Community.current.providers.get(i).getFirstName() + " "
//					+ Community.current.providers.get(i).getLastName() + "\tRATE: " + " "
//					+ NumberFormat.getCurrencyInstance().format(Community.current.providers.get(i).getPrice()) + "/hr" 
//					);
//			p.add(Community.current.providers.get(i));
			x++;
//		}
		
		
		try {// Selects Service Provider
			String input = scan.nextLine();
			if (input.equals("0")) {
//				serviceMenu();
			}
			int providerSelection = Integer.parseInt(input) - 1;
			
			
			try {
	            //Displays Availability
	            for(int i = 0; i < p.get(providerSelection).getAvailable().size(); i++) {
	            	System.out.println(p.get(providerSelection).getAvailable().get(i));
	            }
	            System.out.println("Choose a day and time");
	            int date = scan.nextInt() -1;
	            System.out.println("Schedule for: " + p.get(providerSelection).getAvailable().get(date));
	            p.get(providerSelection).getAvailable().remove(date);

	        } catch(Exception e) {
	            scan.nextLine();
	            System.out.println("Invalid entry");
	            displayProvider();
	        }
	        
	        System.out.println("Questions? call " + p.get(providerSelection).getFirstName()
	                + " " + p.get(providerSelection).getLastName() + " " + p.get(providerSelection).getPhoneNumber() );
	        scan.nextLine();
			
			

			if (providerSelection == 0) {
				//scan.nextLine();
//				serviceMenu();

			} else {
				getNewUserInformation();
				System.out.println("Thank you! " + p.get(providerSelection - 1).getFirstName() + " "
						+ p.get(providerSelection - 1).getLastName() + " "
						+ p.get(providerSelection - 1).getPhoneNumber());
				//scan.nextLine();
//				selectAnotherProvider();
			}

		} catch (Exception e) {

			System.err.println("NOT A VALID ENTRY");
			displayProvider();
		}
	}
	// Gathers new user's information
	// Store in a database for persistence to
	// avoid users from having to re enter their info after each use
	//THIS NEEDS HUGE IMPROVEMENT
	public void getNewUserInformation() {
		System.out.println("Fill out your contact information below");
		System.out.println("Name >");
		
		String name = scan.nextLine();
		while(name.length() < 5) {
			System.err.println("NAME MUST BE AT LEAST 5 CHARACTERS LONG");
			name = scan.nextLine();
		}

		System.out.println("Phone Number >");
		String phone = scan.nextLine();
		while(phone.length() != 10 && phone.length() != 12) {
			System.err.println("INCORRECT PHONE LENGTH");
			phone = scan.nextLine();
		}

		System.out.println("Email > ");
		String email = scan.nextLine();
		boolean flag = email.contains("@");
		
		while(!flag) {
			System.err.println("INCORRECT EMAIL ADDRESS");
			email = scan.nextLine();
			flag = email.contains("@"); 
		}

		
	}
	
	/*public void selectAnotherProvider() {
		System.out.print("\nWould you like to find another provider? > y or n >");
		String input = scan.nextLine().toLowerCase();

		if (input.equals("y")) {
			displayServices(Community.current);
		} else if (input.equals("n")) {
			System.out.println("Have a nice day!");
			serviceMenu();
		} else {
			//System.out.println("Test");
			selectAnotherProvider();
		}

	}

	public int getComm(String access) {
		for (int i = 0; i < Community.comm.size(); i++) {
			if (access.equals(Community.comm.get(i).getAccess())) {

				return i;
			}
		}
		return -1;
	}*/

	/*public void serviceMenu() {
		System.out.println();
		System.out.println("WELCOME TO THE " + Community.current.getAdminName().toUpperCase() + " COMMUNITY ");
		System.out.println();
		System.out.println("Select an option: ");
		System.out.println("0.) Go Back");
		System.out.println("1.) Create a new service provider");
		System.out.println("2.) Display list of service providers\n");
		//System.out.println("TEST");
		//scan.nextLine();
		String selection = scan.nextLine();

		if (Community.current.providers.size() == 0 && selection.equals("2")) {
			
			System.err.println("No providers available at this time.");
			selection = "7";
		}

		switch (selection) {
		case "1":
			createServiceProvider();
			displayServices(Community.current);
			break;
		case "2":
			displayServices(Community.current);
			break;
		case "0":
			//scan.nextLine();
			mainMenu();
			break;
		default:
			serviceMenu();
		}
	}*/

	public void createServiceProvider() {
		
			//scan.nextLine();
		
		
			System.out.println("Enter first name : ");
			String first = scan.nextLine();
			while(first.length() < 2) {
				System.err.println("FIRST NAME MUST BE AT LEAST 2 LETTERS LONG");
				first = scan.nextLine();
			}
			
			System.out.println("Enter last name : ");
			String last = scan.nextLine();
			while(last.length() < 2) {
				System.err.println("LAST NAME MUST BE AT LEAST 2 LETTERS LONG");
				last = scan.nextLine();
			}
			System.out.println("Enter service name : ");
			String service = scan.nextLine();
			while(service.length() < 3) {
				System.err.println("SERVICE NAME MUST BE AT LEAST 3 LETTERS LONG");
				service = scan.nextLine();
			}
			
			System.out.println("Enter phone number (xxx-xxx-xxxx): ");
			String phone = scan.nextLine();
			while(phone.length() != 10 && phone.length() != 12) {
				System.err.println("INCORRECT PHONE LENGTH");
				phone = scan.nextLine();
			}
			
			System.out.println("Enter cost of service ($/hr): ");
			String c = scan.nextLine();
			
			while(!isDouble(c)) {
				System.err.println("Invalid Entry! ");
				System.out.println("Enter cost of service ($/hr): ");
				 c = scan.nextLine();
			}
			
			double cost = Double.parseDouble(c);
			//Community.current.providers.add(new ServiceProvider(first, last, service, 0, phone, cost));
	}
	
	boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
	
	private static String generateUniqueAccessNumber() {
		String rand = "" + ((int) (Math.random() * 89999) + 10000);
		for (int i = 0; i < communities.size(); i++) {
			if (rand == communities.get(i).getAccess()) {
				rand = generateUniqueAccessNumber();
			}
		}
		return rand;
	}
	//Automatically adds the schedule to all pre made providers
	public static void addSchedules() {
        for(int i = 0; i < communities.size(); i++) {
            for(int j = 0; j < communities.get(i).getProviders().size(); j++) {
                communities.get(i).getProviders().get(j).getAvailable().add("Monday 8am-12pm");
                communities.get(i).getProviders().get(j).getAvailable().add("Monday 1pm-4pm");
                communities.get(i).getProviders().get(j).getAvailable().add("Tuesday 8am-12pm");
                communities.get(i).getProviders().get(j).getAvailable().add("Tuesday 1pm-4pm");
                communities.get(i).getProviders().get(j).getAvailable().add("Wednesday 8am-12pm");
                communities.get(i).getProviders().get(j).getAvailable().add("Wednesday 1pm-4pm");
                communities.get(i).getProviders().get(j).getAvailable().add("Thursday 8am-12pm");
                communities.get(i).getProviders().get(j).getAvailable().add("Thursday 1pm-4pm");
                communities.get(i).getProviders().get(j).getAvailable().add("Friday 8am-12pm");
                communities.get(i).getProviders().get(j).getAvailable().add("Friday 1pm-4pm");
                communities.get(i).getProviders().get(j).getAvailable().add("Saturday 8am-12pm");
                communities.get(i).getProviders().get(j).getAvailable().add("Saturday 1pm-4pm");
                communities.get(i).getProviders().get(j).getAvailable().add("Sunday 8am-12pm");
                communities.get(i).getProviders().get(j).getAvailable().add("Sunday 1pm-4pm");
            }
        }
    }
	/**
	 * helper method that gets an integer between minValue and maxValue from the user
	 * If the user enters anything other than an integer, catches the exception
	 * and prints the error message received from the method call
	 * @param minValue the minimum value of the menu
	 * @param maxValue the maximum value of the menu
	 * @param errorMessage the error message to display for an invalid entry
	 * @return the integer the user entered
	 */
	public static int getIntFromUser(int minValue, int maxValue, String errorMessage) {
		int selection = 0;
		boolean invalidSelection;
		
		//Loop until the user enters an integer between the given limits
		do {
			invalidSelection = false;
			try {
				selection = scan.nextInt();
				if(selection < minValue || selection > maxValue) {
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
		
		return selection;
	}	
	/**
	 * shows the cash error message when user enters the wrong type of cash
	 * @param message the error message to display
	 */
	public static void showErrorMessage(String message) {
		System.out.println("\n" + message);		
	}
	
	//Make data for testing purposes
	public static void makeTestData() {
		//Create a new USER
		users.add(new User("Roy", "Chancellor", "rc", "abc123"));
		//Create a new COMMUNITY with USER(0) as the ADMIN
		communities.add(new Community(users.get(0), "12345"));
		//Add new PROVIDERS to COMUNITY(0)
		communities.get(0).getProviders().add(
			new ServiceProvider("Heather", "Stone", "Heather's Tennis Lessons", "480-742-2311", 50, communities.get(0)));
		communities.get(0).getProviders().add(
				new ServiceProvider("Max", "Lopez", "Max's Solar Provider", "602-212-9851", 5000, communities.get(0)));
		//Create a new USER
		users.add(new User("Paul", "Weinberg", "pw", "abc123"));		
		//Add a new USER to COMMUNITY(0) as a USER
		users.get(0).getCommunities().add(communities.get(0));
		
		//Add a new COMMUNITY with USER(1) as the ADMIN
		communities.add(new Community(users.get(1), "12345"));
		//Create a new USER
		users.add(new User("Randy", "Johnson", "rj", "abc123"));		
		//Add a new USER to COMMUNITY(1) as USER
		users.get(2).getCommunities().add(communities.get(1));
		//Add new PROVIDERS to COMUNITY(1)
		communities.get(1).getProviders().add(
			new ServiceProvider("John", "Doe", "Cleaning Co", "623-742-2311", 9.99, communities.get(1)));
		communities.get(1).getProviders().add(
			new ServiceProvider("Rob", "Loy", "Tutor", "623-202-1551", 150, communities.get(1)));
		
		//Add schedules to all providers in all communities
		addSchedules();
	}
}
