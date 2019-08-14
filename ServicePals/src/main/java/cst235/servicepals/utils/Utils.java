package cst235.servicepals.utils;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	private static Scanner scan = new Scanner(System.in);
	
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
			System.out.println("\nEnter provider phone number (nnn-nnn-nnnn): ");
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
			System.out.println("Enter your e-mail address --> ");
			emailAddress = scan.nextLine();
			if(!verifyRegex(emailRegex, emailAddress)) {
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
	 * @return true if string mateches pattern; false otherwise
	 */
	public static boolean verifyRegex(String regex, String stringToTest) {
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(stringToTest);
		if(match.matches()) {
			return true;
		}
		return false;
	}
	
}
