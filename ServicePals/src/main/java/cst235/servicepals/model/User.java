package cst235.servicepals.model;

import java.util.ArrayList;
import java.util.List;

public class User {
	//Class data
	private String firstName;
	private String lastName;
	private String username;
	private String password;
	private String emailAddress;
	private List<Community> communities = new ArrayList<Community>();
	
	//Constructor
	public User(String firstName, String lastName, String username, String password, String emailAddress) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.emailAddress = emailAddress;
	}

	@Override
	public String toString() {
		return "USER FN: " + firstName + ", LN: " + lastName + ", UN: " + username + ", PW: " + password + ", EMAIL: " + emailAddress;
	}

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
	 * @return the communities
	 */
	public List<Community> getCommunities() {
		return communities;
	}

	/**
	 * @return the userName
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUsername(String userName) {
		this.username = userName;
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

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
}
