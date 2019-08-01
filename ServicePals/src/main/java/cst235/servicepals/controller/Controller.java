package cst235.servicepals.controller;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import cst235.servicepals.model.Community;
import cst235.servicepals.model.ServiceProvider;
import cst235.servicepals.model.User;

public class Controller {
	//Scanner object for use throughout the application
	public static Scanner scan = new Scanner(System.in);
	
	//Class data
	private static List<Community> communities = new ArrayList<Community>();
	private static List<User> users = new ArrayList<User>();
	private static List<ServiceProvider> providers = new ArrayList<ServiceProvider>();
	private static int currentCommunityIndex = 0;
	private static int currentUserIndex = 0;
	
	public static void showMainMenu() {
		//Make fake data for testing purposes
		communities.add(new Community("Church", "12345"));
		communities.add(new Community("Developers", "12345"));
		communities.get(0).getProviders().add(
			new ServiceProvider("Heather", "Stone", "Heather's Tennis Lessons", 0, "480-742-2311", 50, communities.get(0)));
		communities.get(0).getProviders().add(
			new ServiceProvider("Max", "Lopez", "Max's Solar Provider", 1, "602-212-9851", 0, communities.get(0)));
		communities.get(1).getProviders().add(
			new ServiceProvider("John", "Doe", "Cleaning Co", 0, "623-742-2311", 9.99, communities.get(1)));
		communities.get(1).getProviders().add(
			new ServiceProvider("Rob", "Loy", "Tutor", 1, "623-202-1551", 15, communities.get(1)));
		users.add(new User("Roy", "Chancellor", "rc", "abc123"));
		users.get(0).setCommAdmin(true);
		users.get(0).getCommunities().add(communities.get(0));
		users.add(new User("Paul", "Weinberg", "pw", "abc123"));
		users.get(1).setCommAdmin(true);
		users.get(1).getCommunities().add(communities.get(1));
		
		int option = 0;
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
		} while(option != 0);
	}
	
	private static void processMainMenu(int selection) {
		switch(selection) {
		case 1:
			if(doUserLogin()) {
				showUserMenu();
			}
			break;
		case 2:
			doCreateUser();
			break;
		case 0:
			break;
		}
	}
	
	private static void showUserMenu() {
		//Show header
		//Show available communities (if any)
		//Show community action menu
		//1. Comm 1
		//2. Comm 2
		//N. Comm N
		//N + 1. Create new community
		//Show footer
		//0. Return to main menu
		//Get option from user
		int selection = 1;
		processUserMenu(selection - 1);
	}
	
	private static void processUserMenu(int option) {
		//Get access code from the user and do not proceed until it is correct
		if(option != 0) {
			System.out.println("Accessing community index" + option);
			showCommunityMenu(option - 1);
		}
	}
	
	private static void showCommunityMenu(int commIndex) {
		System.out.println("Showing community " + communities.get(commIndex).toString() + " menu...");
		//List available providers
		//Add provider???
		//Select a provider
		//0. Return to community menu
		//Get option from user
		int providerIndex = 0;
		processProviderMenu(communities.get(commIndex), providerIndex);
	}
	
	private static void processProviderMenu(Community c, int providerIndex) {
		if(providerIndex != 0) {
			System.out.println("Showing provider " + c.getProviders().get(providerIndex));
			showScheduleProviderMenu(c.getProviders().get(providerIndex));
		}
	}
	
	public static void showScheduleProviderMenu(ServiceProvider p) {
		//List available times for the provider
		//Get the user's selection
		//WHAT TO DO HERE???
		//Remove the scheduled time from the provider's schedule
		//Tell the user that the provider is scheduled
		//Alert the provider???
		//method stack will go back to showCommunityMenu where the user can
		//create a new community or go into another community
	}
	
	public static boolean doUserLogin() {
		System.out.println("\n-----------------------------");
		System.out.println("        USER LOGIN");
		System.out.println("-----------------------------");
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
	 * scans the list of users to looks for a username and password match
	 * @param user the username of the current attempt to log in
	 * @param password the password of the current attempt to log in
	 * @return true if un/pw match found and false if not
	 */
	public static boolean checkLoginCredentials(String user, String password) {
		for (int i = 0; i < users.size(); i++) {
			if(user.equals(users.get(i).getUserName()) &&
				password.equals(users.get(i).getPassword())) {
				currentUserIndex = i;
				System.out.println("Correct login credentials!");
				return true;
			}
		}
		return false;
	}
	
	private static void doCreateUser() {
		System.out.println("\nCreating user.....");
	}

	public static void mainMenu() {
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.println("⚪⚪ 	          SERVICE PALS	                  ⚪⚪");
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.println();
		System.out.println("1.) Enter your community access code.");
		System.out.println("2.) Create new community");
		String input = scan.nextLine();

		switch (input) {
		case "1":
			doUserLogin();
			break;
		case "2":
			makeNewCommunity();
			break;
		default:
			mainMenu();
		}
	}

	private static void makeNewCommunity() {
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.println("⚪⚪ 	         CREATE NEW COMMUNITY             ⚪⚪");
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.print("\nENTER A COMMUNITY NAME (must be at least 5 characters)\nNAME: ");
		String name = scan.nextLine();
		while(name.length() < 5) {
			System.err.println("COMMUNITY NAME MUST BE AT LEAST 5 CHARACTERS LONG");
			name = scan.nextLine();
		}
		String access = generateUniqueAccessNumber();
		communities.add(new Community(name, access));
//		Community.current = communities.get(communities.size() - 1);
//
//		System.out.println(Community.current.getAdminName() + ", " + Community.current.getAccess());
//		if (checkCode(access))
//			serviceMenu();
	}

	public void displayServices(Community comm) {
		System.out.println("\nCommunity: " + comm.getAdminName());
		System.out.println("\nSERVICES YOUR PALS OFFER");
		System.out.println();
		//scan.nextLine();
		displayProvider();
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
				requestContactDetails();
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
	//Gathers User's information> This information should be a user profile to
	// avoid users from having to re enter their info after each use
	public void requestContactDetails() {
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
	// Pre made communities
	public void createCommunities() {
		communities.add(new Community("Church", "12345"));
		communities.add(new Community("Developer's", "54321"));
//		createServiceProviders();
	}
	// Pre made Providers
//	public void createServiceProviders() {
//		communities.get(0).providers.add(new ServiceProvider("Heather", "Stone", "Heather's Tennis Lessons", 0, "480-742-2311", 75));
//		communities.get(0).providers.add(new ServiceProvider("Max", "Lopez", "Max's Solar Provider", 1, "602-212-9851", 0));
//		communities.get(1).providers.add(new ServiceProvider("John", "Doe", "Cleaning Co", 0, "623-742-2311", 9.99));
//		communities.get(1).providers.add(new ServiceProvider("Rob", "Loy", "Tutor", 1, "623-202-1551", 15));
//		addSchedules();
//	}

	//Automatically adds the schedule to all pre made providers
	public void addSchedules() {
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
}
