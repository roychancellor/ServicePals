package cst235.servicepals.model;

import java.util.ArrayList;
import java.util.List;

public class Community {
	//Class data
	private int communityId;
	private String communityName;
	private String accessCode;
	private User admin;
	private int adminUserId;
	List<User> users = new ArrayList<User>();
	List<ServiceProvider> providers = new ArrayList<ServiceProvider>();

	@Override
	public String toString() {
		return "COMM NAME: " + communityName + ", AC: " + accessCode + ", ADMIN: " + admin.toString();
	}

	//Constructors
	public Community() { }
	
	public Community(int communityIndex, String communityName, User admin, String accessCode) {
		this.communityId = communityIndex;
		this.communityName = communityName;
		this.admin = admin;
		this.accessCode = accessCode;
	}

	public Community(int communityId, String communityName, int adminUserId, String accessCode) {
		this.communityId = communityId;
		this.communityName = communityName;
		this.adminUserId = adminUserId;
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

	/**
	 * @return the adminUserId
	 */
	public int getAdminUserId() {
		return adminUserId;
	}

	/**
	 * @param adminUserId the adminUserId to set
	 */
	public void setAdminUserId(int adminUserId) {
		this.adminUserId = adminUserId;
	}

	public int getCommunityId() {
		return communityId;
	}

	public void setCommunityId(int communityId) {
		this.communityId = communityId;
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

	/**
	 * @param providers the providers to set
	 */
	public void setProviders(List<ServiceProvider> providers) {
		this.providers = providers;
	}
}
