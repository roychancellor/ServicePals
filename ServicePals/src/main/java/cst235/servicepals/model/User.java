package cst235.servicepals.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class template for a user (or more accurately, a member of ServicePals)
 */
public class User {
	//Class data
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String emailAddress;
	private List<Community> communities = new ArrayList<Community>();
	
	//Constructors
	
	/**
	 * No-argument constructor for User object
	 */
	public User() { }
	
	/**
	 * Constructor for User object with parameters
	 * @param firstName user's first name
	 * @param lastName user's last name
	 * @param username user's username (must be unique in the database)
	 * @param password user's password
	 * @param emailAddress user's e-mail address
	 */
	public User(String firstName, String lastName, String username, String password, String emailAddress) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.emailAddress = emailAddress;
	}

	@Override
	public String toString() {
		return "USER FN: " + firstName + ", LN: " + lastName + ", UN: " + username
			+ ", PW: " + password + ", EMAIL: " + emailAddress;
	}

	//Getters and setters
	
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the communities
	 */
	public List<Community> getCommunities() {
		return communities;
	}

	/**
	 * @param communities the communities to set
	 */
	public void setCommunities(List<Community> communities) {
		this.communities = communities;
	}
}
