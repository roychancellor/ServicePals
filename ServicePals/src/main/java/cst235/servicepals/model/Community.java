package cst235.servicepals.model;

import java.util.ArrayList;
import java.util.List;

public class Community {
	//Class data
	private String accessCode;
	private User admin;
	List<User> users = new ArrayList<User>();
	List<ServiceProvider> providers = new ArrayList<ServiceProvider>();

	//Constructors
	public Community(User admin, String accessCode) {
		this.admin = admin;
		this.accessCode = accessCode;
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
		return accessCode;
	}

	public void setAccess(String accessCode) {
		this.accessCode = accessCode;
	}

	public List<ServiceProvider> getProviders() {
		return providers;
	}

	public void setProviders(List<ServiceProvider> providers) {
		this.providers = providers;
	}

	public User getAdmin() {
		return admin;
	}

	public void setAdmin(User admin) {
		this.admin = admin;
	}
}
