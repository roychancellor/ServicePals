package cst235.servicepals.utils;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	private static Scanner scan = new Scanner(System.in);
	public static final int NUM_BANNER_CHARS = 50;
	
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

	/**
	 * checks to see if the user entry is a double value or not
	 * @param str
	 * @return true if double, false if not
	 */
	public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
	
	/**
	 * Returns a phone number validated to be of the form nnn-nnn-nnnn
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
	 * Checks whether a string matches a regular expression pattern
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
	 * prints out a header with a single message, adjusted in size for the message length
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
	 * prints out a header with a single message, adjusted in size for the message length
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
	 * prints a footer which is just a line of dashes
	 * @param numChars the number of characters to print
	 */
	public static void printSeparator(int numChars) {
		for(int i = 0; i < numChars; i++) {
			System.out.print("-");
		}
		System.out.print("\n");
	}	
}
