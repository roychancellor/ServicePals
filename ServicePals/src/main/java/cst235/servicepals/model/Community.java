package cst235.servicepals.model;

import java.util.ArrayList;
import java.util.List;

public class Community {
	//Class data
	private int communityIndex;
	private String communityName;
	private String accessCode;
	private User admin;
	List<User> users = new ArrayList<User>();
	List<ServiceProvider> providers = new ArrayList<ServiceProvider>();

	@Override
	public String toString() {
		return "COMM NAME: " + communityName + ", AC: " + accessCode + ", ADMIN: " + admin.toString();
	}

	//Constructors
	public Community(int communityIndex, String communityName, User admin, String accessCode) {
		this.communityIndex = communityIndex;
		this.communityName = communityName;
		this.admin = admin;
		this.accessCode = accessCode;
	}
	
	//Getters and setters
	public String getAccess() {
		return accessCode;
	}

	public void setAccess(String accessCode) {
		this.accessCode = accessCode;
	}

	public User getAdmin() {
		return admin;
	}

	public void setAdmin(User admin) {
		this.admin = admin;
	}

	public int getCommunityIndex() {
		return communityIndex;
	}

	public void setCommunityIndex(int communityIndex) {
		this.communityIndex = communityIndex;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public List<User> getUsers() {
		return users;
	}

	public List<ServiceProvider> getProviders() {
		return providers;
	}
}
