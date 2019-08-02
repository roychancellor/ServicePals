package cst235.servicepals.model;

import java.util.ArrayList;
import java.util.List;

public class Community {
	//Class data
	private String communityName;
	private String accessCode;
	private User admin;
	List<User> users = new ArrayList<User>();

	@Override
	public String toString() {
		return String.format("Community [communityName=%s, accessCode=%s, admin=%s, users=%s]", communityName,
				accessCode, admin, users);
	}

	//Constructors
	public Community(String communityName, User admin, String accessCode) {
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

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
}
