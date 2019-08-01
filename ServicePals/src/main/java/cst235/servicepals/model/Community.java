package cst235.servicepals.model;

import java.util.ArrayList;
import java.util.List;

public class Community {
	//Class data
	private String adminName;
	private String access;
	List<ServiceProvider> providers = new ArrayList<ServiceProvider>();
	List<User> users = new ArrayList<User>();
	private Community currentCommunity;

	//Constructors
	public Community() {
		
	}
	
	public Community(String name, String access) {
		this.setAdminName(name);
		this.access = access;
	}
	
	public void addProvider(ServiceProvider provider){
		providers.add(provider);
	}

	//Class methods
	public void displayServices() {
		for(ServiceProvider serv : providers) {
			serv.displayService();
		}
	}

	//Getters and setters
	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public List<ServiceProvider> getProviders() {
		return providers;
	}

	public void setProviders(List<ServiceProvider> providers) {
		this.providers = providers;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String name) {
		this.adminName = name;
	}

	/**
	 * @return the currentCommunity
	 */
	public Community getCurrentCommunity() {
		return currentCommunity;
	}

	/**
	 * @param currentCommunity the currentCommunity to set
	 */
	public void setCurrentCommunity(Community currentCommunity) {
		this.currentCommunity = currentCommunity;
	}
	
}
