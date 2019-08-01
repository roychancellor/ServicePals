package cst235.servicepals.model;

import java.util.ArrayList;
import java.util.List;

public class User {
	//Class data
	private String firstName;
	private String lastName;
	private String userName;
	private String password;
	private boolean isCommAdmin;
	private List<Community> communities = new ArrayList<Community>();
	
	//Constructor
	public User(String firstName, String lastName, String userName, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
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
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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
	 * @return the isCommAdmin
	 */
	public boolean isCommAdmin() {
		return isCommAdmin;
	}

	/**
	 * @param isCommAdmin the isCommAdmin to set
	 */
	public void setCommAdmin(boolean isCommAdmin) {
		this.isCommAdmin = isCommAdmin;
	}
	
	
}
