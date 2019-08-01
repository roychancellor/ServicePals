package cst235.servicepals.model;

import java.util.ArrayList;
import java.util.List;

public class ServiceProvider {
	private int serviceNum;
	private double price;
	private String serviceName;
	private String phoneNumber;
	private String firstName;
	private String lastName;
	private List<String> available = new ArrayList<>();
	private List<Community> communities = new ArrayList<Community>();
	
	//Constructor
	public ServiceProvider(String firstName, String lastName, String serviceName,
		int serviceNum, String phoneNumber , double price, Community community) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.price = price;
		this.serviceName = serviceName;
		this.serviceNum = serviceNum;
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
	
	public int getServiceNum() {
		return serviceNum;
	}
	
	public void setServiceNum(int serviceNum) {
		this.serviceNum = serviceNum;
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
		return available;
	}

	/**
	 * @return the communities
	 */
	public List<Community> getCommunities() {
		return communities;
	}
}
