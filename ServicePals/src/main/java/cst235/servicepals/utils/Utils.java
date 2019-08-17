package cst235.servicepals.utils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class containing static utility methods that support the Controller
 * in retrieving and processing user information
 */
public class Utils {
	private static Scanner scan = new Scanner(System.in);
	public static final int NUM_BANNER_CHARS = 50;
	
	/**
	 * Helper method that gets an integer between minValue and maxValue from the user
	 * If the user enters anything other than an integer, catches the exception
	 * and prints the error message received from the method call
	 * @param minValue the minimum allowed value of the number
	 * @param maxValue the maximum allowed value of the number
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
	 * Overloaded helper method that gets a double between minValue and maxValue from the user
	 * If the user enters anything other than a double, catches the exception
	 * and prints the error message received from the method call
	 * @param minValue the minimum allowed value of the number
	 * @param maxValue the maximum allowed value of the number
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
	 * Simple helper method to shows an error message
	 * @param message the error message to display
	 */
	public static void showErrorMessage(String message) {
		System.out.println("\n" + message);		
	}	

	/**
	 * Gets a phone number from the user and validates it is in the form nnn-nnn-nnnn
	 * @return a phone number as a String object
	 */
	public static String getPhoneNumber() {
		boolean keepGoing = false;
		String phoneRegex = "([0-9]{3})[-]([0-9]{3})[-]([0-9]{4})";
		String phoneNumber = "";
		do {
			keepGoing = false;
			System.out.println("\nEnter phone number (nnn-nnn-nnnn): ");
			phoneNumber = scan.nextLine();
			if(!verifyRegex(phoneRegex, phoneNumber)) {
				System.err.println("\nOops, enter phone number as nnn-nnn-nnnn");
				keepGoing = true;
			}
		} while(keepGoing);
		
		return phoneNumber;
	}
	
	/**
	 * Gets a user's email address and validates it against a regular expression
	 * @return email address as a String
	 */
	//TODO: Make this more robust and inclusive of other types of e-mail addresses
	public static String getEmailAddress() {
		boolean emailInvalid = false;
		String emailRegex = "[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}";
		String emailAddress = "";
		do {
			emailInvalid = false;
			System.out.println("\nEnter your e-mail address --> ");
			emailAddress = scan.nextLine();
			if(!verifyRegex(emailRegex, emailAddress.toUpperCase())) {
				System.err.println("Oops, email format must be address@domainName.extension");
				emailInvalid = true;				
			}
		} while(emailInvalid);
		
		return emailAddress;
	}
	
	/**
	 * Gets a date from the user that meets a regex format for date
	 * and validates that it is a valid date
	 * @return the user's requested service date as a String in the format yyyy-mm-dd
	 * so that it is SQL-friendly for use in writing to table using SQL date format
	 */
	public static String getServiceDate() {
		boolean dateInvalid = false;
		String dateRegex = "[0-1]{1}[1-9]{1}[-]{1}[0-3]{1}[1-9]{1}[-]{1}20[1-2]{1}[0-9]{1}";
		//Get the date string from the user and validate it meets the regex
		//then validate that it is a valid date
		LocalDate date = null;
		do {
			dateInvalid = false;
			System.out.println("\nEnter requested date of service (MM-DD-YYYY) --> ");
			String serviceDate = scan.nextLine();
			
			//Check that the date meets the regex format
			if(!verifyRegex(dateRegex, serviceDate)) {
				System.err.println("Oops, date format must be MM-DD-YYYY");
				dateInvalid = true;				
			}
			else {
				//Determine if the user-entered a valid date
				try {
					//put the user-entered date in the format yyyy-mm-dd for the parse method
					StringBuilder parseDate = new StringBuilder();
					
					//If the date is valid (i.e. parse does not throw an exception),
					//convert mm-dd-yyyy into yyyy-mm-dd for use in SQL queries
					parseDate.append(serviceDate.substring(6));
					parseDate.append("-");
					parseDate.append(serviceDate.substring(0,5));
					date = LocalDate.parse(parseDate.toString());
					
					//Check to see if the date is not in the past
					if(date.compareTo(LocalDate.now()) < 0) {
						System.err.println("\nOops, that date is in the past. Enter a date today or later.");
						dateInvalid = true;
					}
				}
				catch(DateTimeParseException e) {
					System.err.println("\nOops, the date you entered meets the format, but is invalid.");
					dateInvalid = true;
				}
			}
		} while(dateInvalid);
		
		return date.toString();
	}
	
	/**
	 * Converts a date in the form yyyy-mm-dd into mm-dd-yyyy
	 * for easier interpretation by the user
	 * @param YYYYMMDD the string in yyyy-mm-dd format
	 * @return String in the format mm-dd-yyyy
	 */
	public static String convertYYYYMMDDtoMMDDYYYY(String YYYYMMDD) {
		if(YYYYMMDD != null && YYYYMMDD.length() >= 10 && verifyRegex("[0-9]{4}-[0-9]{2}-[0-9]{2}", YYYYMMDD)) {
			StringBuilder MMDDYYYY = new StringBuilder();
			MMDDYYYY.append(YYYYMMDD.substring(5,7));
			MMDDYYYY.append("-");
			MMDDYYYY.append(YYYYMMDD.substring(YYYYMMDD.length()-2));
			MMDDYYYY.append("-");
			MMDDYYYY.append(YYYYMMDD.substring(0,4));
			return MMDDYYYY.toString();
		}
		else {
			return null;
		}
	}
	
	/**
	 * General method that checks whether a string matches a regular expression pattern
	 * @param regex the regular expression string to check against
	 * @param stringToTest the string to test for a match to the regex
	 * @return true if string matches pattern; false otherwise
	 */
	public static boolean verifyRegex(String regex, String stringToTest) {
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(stringToTest);
		if(match.matches()) {
			return true;
		}
		return false;
	}
	
	/**
	 * Gets a string of minimum length from the user
	 * @param minLength the minimum allowable length of the user-entered string
	 * @param prompt the prompt message to the user for what to enter
	 * @return the string the user entered
	 */
	public static String getStringMinLength(int minLength, String prompt) {
		boolean stringInvalid = false;
		String str = "";
		
		if(minLength > 0 && prompt != null) {
			do {
				stringInvalid = false;
				System.out.println("\n" + prompt);
				str = scan.nextLine();
				
				if(str == null || str.length() < minLength) {
					System.err.println("\nOops, you must enter at least " + minLength + " characters. Try again.");
					stringInvalid = true;
				}
			} while(stringInvalid);
		}
		else {
			str = null;
		}
		
		return str;
	}
	
	/**
	 * Prints out a header with a single message,
	 * adjusted in size for the message length
	 * @param headerMessage the message to put in the header
	 */
	public static void makeBanner(String headerMessage) {
		int adder = 0;
		if(headerMessage.length() > NUM_BANNER_CHARS - 4) {
			adder = 8;
		}
		System.out.println();
		for(int i = 0; i < NUM_BANNER_CHARS + adder; i++) {
			System.out.print("#");
		}
		System.out.print("\n##");
		int numSpaces = (NUM_BANNER_CHARS + adder - 4 - headerMessage.length()) / 2;
		for(int i = 0; i < numSpaces; i++) {
			System.out.print(" ");
		}
		
		System.out.print(headerMessage);
		
		//Print an extra space on the right if the message length is odd
		numSpaces += headerMessage.length() % 2;
		for(int i = 0; i < numSpaces; i++) {
			System.out.print(" ");
		}
		System.out.print("##\n");
		for(int i = 0; i < NUM_BANNER_CHARS + adder; i++) {
			System.out.print("#");
		}
	}
	
	/**
	 * Overloaded method that prints out a header with a single message,
	 * adjusted in size for the message length
	 * @param titleMessage the message to put in the header
	 * @param secondMessage a message that appears below the title
	 */
	public static void makeBanner(String titleMessage, String secondMessage) {
		int adder = 0;
		if(titleMessage.length() > NUM_BANNER_CHARS - 4 || secondMessage.length() > NUM_BANNER_CHARS) {
			adder = 8;
		}
		
		//TOP OF BANNER
		System.out.println();
		for(int i = 0; i < NUM_BANNER_CHARS + adder; i++) {
			System.out.print("#");
		}
		
		//TITLE
		System.out.print("\n##");
		int numSpaces = (NUM_BANNER_CHARS + adder - 4 - titleMessage.length()) / 2;
		for(int i = 0; i < numSpaces; i++) {
			System.out.print(" ");
		}
		
		System.out.print(titleMessage);
		
		//Print an extra space on the right if the message length is odd
		numSpaces += titleMessage.length() % 2;
		for(int i = 0; i < numSpaces; i++) {
			System.out.print(" ");
		}
		System.out.print("##\n");
		
		//SECOND MESSAGE
		System.out.print("##");
		numSpaces = (NUM_BANNER_CHARS + adder - 4 - secondMessage.length()) / 2;
		for(int i = 0; i < numSpaces; i++) {
			System.out.print(" ");
		}
		System.out.print(secondMessage);
		//Print an extra space on the right if the message length is odd
		numSpaces += secondMessage.length() % 2;
		for(int i = 0; i < numSpaces; i++) {
			System.out.print(" ");
		}
		
		//BOTTOM OF BANNER
		System.out.print("##\n");
		for(int i = 0; i < NUM_BANNER_CHARS + adder; i++) {
			System.out.print("#");
		}
	}

	/**
	 * Prints a footer which is just a line of dashes
	 * @param numChars the number of characters to print
	 */
	public static void printSeparator(int numChars) {
		for(int i = 0; i < numChars; i++) {
			System.out.print("-");
		}
		System.out.print("\n");
	}	
}
