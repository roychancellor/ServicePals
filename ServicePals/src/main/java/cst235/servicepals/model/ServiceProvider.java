package cst235.servicepals.model;

import java.util.ArrayList;
import java.util.List;

public class ServiceProvider {
	private String username;
	private String serviceName;
	private String phoneNumber;
	private double price;
	private List<String> availableTimeSlots = new ArrayList<>();
	
	//Constructor
	public ServiceProvider(String username, String serviceName, String phoneNumber, double price) {
		this.username = username;
		this.serviceName = serviceName;
		this.phoneNumber = phoneNumber;
		this.price = price;
	}
	
	@Override
	public String toString() {
		return "SP NAME: " + serviceName + ", PHONE: " + phoneNumber + ", PRICE: " + price;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getServiceName() {
		return serviceName;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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
	
	public List<String> getAvailableTimeSlots() {
		return availableTimeSlots;
	}
}
