package cst235.servicepals.model;

import java.util.ArrayList;
import java.util.List;

public class ServiceProvider {
	private double price;
	private String serviceName;
	private String phoneNumber;
	private String firstName;
	private String lastName;
	private List<String> availableTimeSlots = new ArrayList<>();
	private List<Community> communities = new ArrayList<Community>();
	
	//Constructor
	public ServiceProvider(String firstName, String lastName, String serviceName,
			String phoneNumber , double price, Community community) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.price = price;
		this.serviceName = serviceName;
		this.phoneNumber = phoneNumber;
		this.communities.add(community);
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getServiceName() {
		return serviceName;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public void displayService(){
		
	}
	
	public List<String> getAvailable() {
		return availableTimeSlots;
	}

	/**
	 * @return the communities of which the service provider is a part
	 */
	public List<Community> getCommunities() {
		return communities;
	}
}
