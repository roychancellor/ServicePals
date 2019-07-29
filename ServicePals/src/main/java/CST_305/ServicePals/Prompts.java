package CST_305.ServicePals;

import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Prompts {

	List<Community> comm = new ArrayList<>();
	Scanner scan = new Scanner(System.in);
	
	
	public void mainMenu() throws FileNotFoundException {
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.println("⚪⚪ 	          SERVICE PALS	                  ⚪⚪");
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.println();
		System.out.println("1.) Enter your community access code.");
		System.out.println("2.) Create new community");
		String input = scan.nextLine();

		switch (input) {
		case "1":
			firstScreen();
			break;
		case "2":
			newCommunity();
			break;
		default:
			mainMenu();
		}
	}

	public void firstScreen() throws FileNotFoundException {
		System.out.print("Enter 5 digit Access Code : ");
		String access = scan.nextLine();

		if (!checkCode(access)) {
			System.err.println("WRONG ACCESS CODE");
			firstScreen();
		} else {
			serviceMenu();
		}
	}

	private void newCommunity() throws FileNotFoundException {
		
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.println("⚪⚪ 	         CREATE NEW COMMUNITY             ⚪⚪");
		System.out.println("⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪⚪");
		System.out.print("\nENTER IN A COMMUNITY NAME\nNAME: ");
		String name = scan.nextLine();
		while(name.length() < 5) {
			System.err.println("COMMUNITY NAME MUST BE AT LEAST 5 CHARACTERS LONG");
			name = scan.nextLine();
		}
		String access = generateUniqueAccessNumber();
		comm.add(new Community(name, access));
		Community.current = comm.get(comm.size() - 1);

		System.out.println(Community.current.getName() + ", " + Community.current.getAccess());
		if (checkCode(access))
			serviceMenu();
	}

	public void displayServices(Community comm) throws FileNotFoundException {
		System.out.println("\nCommunity: " + comm.getName());
		System.out.println("\nSERVICES YOUR PALS OFFER");
		System.out.println();
		//scan.nextLine();
		displayProvider();
	}

	public void displayProvider() throws FileNotFoundException {
		// scan.nextLine();
		System.out.println("Select a service provider: ");
		int x = 1;
		List<ServiceProvider> p = new ArrayList<>();
		System.out.println("0.) GO BACK");

		for (int i = 0; i < Community.current.providers.size(); i++) {
			System.out.println((x) + ".) " + Community.current.providers.get(i).getServiceName() + " >  "
					+ Community.current.providers.get(i).getFirstName() + " "
					+ Community.current.providers.get(i).getLastName() + "\tRATE: " + " "
					+ NumberFormat.getCurrencyInstance().format(Community.current.providers.get(i).getPrice()) + "/hr" 
					);
			p.add(Community.current.providers.get(i));
			x++;
		}
		
		
		try {// Selects Service Provider
			String input = scan.nextLine();
			if (input.equals("0")) {
				serviceMenu();
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
				serviceMenu();

			} else {
				requestContactDetails();
				System.out.println("Thank you! " + p.get(providerSelection - 1).getFirstName() + " "
						+ p.get(providerSelection - 1).getLastName() + " "
						+ p.get(providerSelection - 1).getPhoneNumber());
				//scan.nextLine();
				selectAnotherProvider();
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
	
	public void selectAnotherProvider() throws FileNotFoundException {
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

	public boolean checkCode(String access) {
		for (int i = 0; i < comm.size(); i++) {
			if (access.equals(comm.get(i).getAccess())) {
				Community.current = comm.get(i);
				return true;
			}
		}
		return false;
	}

	public int getComm(String access) {
		for (int i = 0; i < Community.comm.size(); i++) {
			if (access.equals(Community.comm.get(i).getAccess())) {

				return i;
			}
		}
		return -1;
	}

	public void serviceMenu() throws FileNotFoundException {
		System.out.println();
		System.out.println("WELCOME TO THE " + Community.current.getName().toUpperCase() + " COMMUNITY ");
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
	}

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
			Community.current.providers.add(new ServiceProvider(first, last, service, 0, phone, cost));
	}
	
	boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
	
	private String generateUniqueAccessNumber() {
		String rand = "" + ((int) (Math.random() * 89999) + 10000);
		for (int i = 0; i < comm.size(); i++) {
			if (rand == comm.get(i).getAccess()) {
				rand = generateUniqueAccessNumber();
			}
		}
		return rand;
	}
	// Pre made communities
	public void createCommunities() throws FileNotFoundException {
		comm.add(new Community("Church", "12345"));
		comm.add(new Community("Developer's", "54321"));
		createServiceProviders();
	}
	// Pre made Providers
	public void createServiceProviders() throws FileNotFoundException {
		comm.get(0).providers.add(new ServiceProvider("Heather", "Stone", "Heather's Tennis Lessons", 0, "480-742-2311", 75));
		comm.get(0).providers.add(new ServiceProvider("Max", "Lopez", "Max's Solar Provider", 1, "602-212-9851", 0));
		comm.get(1).providers.add(new ServiceProvider("John", "Doe", "Cleaning Co", 0, "623-742-2311", 9.99));
		comm.get(1).providers.add(new ServiceProvider("Rob", "Loy", "Tutor", 1, "623-202-1551", 15));
		addSchedules();
	}

	//Automatically adds the schedule to all pre made providers
	public void addSchedules() {
        for(int i = 0; i < comm.size(); i++) {
            for(int j = 0; j < comm.get(i).getProviders().size(); j++) {
                comm.get(i).getProviders().get(j).getAvailable().add("Monday 8am-12pm");
                comm.get(i).getProviders().get(j).getAvailable().add("Monday 1pm-4pm");
                comm.get(i).getProviders().get(j).getAvailable().add("Tuesday 8am-12pm");
                comm.get(i).getProviders().get(j).getAvailable().add("Tuesday 1pm-4pm");
                comm.get(i).getProviders().get(j).getAvailable().add("Wednesday 8am-12pm");
                comm.get(i).getProviders().get(j).getAvailable().add("Wednesday 1pm-4pm");
                comm.get(i).getProviders().get(j).getAvailable().add("Thursday 8am-12pm");
                comm.get(i).getProviders().get(j).getAvailable().add("Thursday 1pm-4pm");
                comm.get(i).getProviders().get(j).getAvailable().add("Friday 8am-12pm");
                comm.get(i).getProviders().get(j).getAvailable().add("Friday 1pm-4pm");
                comm.get(i).getProviders().get(j).getAvailable().add("Saturday 8am-12pm");
                comm.get(i).getProviders().get(j).getAvailable().add("Saturday 1pm-4pm");
                comm.get(i).getProviders().get(j).getAvailable().add("Sunday 8am-12pm");
                comm.get(i).getProviders().get(j).getAvailable().add("Sunday 1pm-4pm");
            }
        }
    }
}
