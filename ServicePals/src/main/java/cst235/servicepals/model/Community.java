package cst235.servicepals.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Class definition for a ServicePals community object
 */
public class Community {
	//Class data
	private int communityId;
	private String communityName;
	private String accessCode;
	private User admin;
	private int adminUserId;
	List<ServiceProvider> providers = new ArrayList<ServiceProvider>();

	@Override
	public String toString() {
		return "COMM NAME: " + communityName + ", AC: " + accessCode + ", ADMIN: " + admin.toString();
	}

	//Constructors
	/**
	 * No-argument constructor
	 */
	public Community() { }
	
	/**
	 * Constructor for a new Community object with parameters
	 * @param communityId the community identification number for the database
	 * @param communityName the user-defined name of the community
	 * @param adminUserId the user id of the administrator
	 * @param accessCode the access code for entering the community
	 */
	public Community(int communityId, String communityName, int adminUserId, String accessCode) {
		this.communityId = communityId;
		this.communityName = communityName;
		this.adminUserId = adminUserId;
		this.accessCode = accessCode;
	}

	//Getters and setters

	/**
	 * @return the communityId
	 */
	public int getCommunityId() {
		return communityId;
	}

	/**
	 * @param communityId the communityId to set
	 */
	public void setCommunityId(int communityId) {
		this.communityId = communityId;
	}

	/**
	 * @return the communityName
	 */
	public String getCommunityName() {
		return communityName;
	}

	/**
	 * @param communityName the communityName to set
	 */
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	/**
	 * @return the accessCode
	 */
	public String getAccessCode() {
		return accessCode;
	}

	/**
	 * @param accessCode the accessCode to set
	 */
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
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

	/**
	 * @return the providers
	 */
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
