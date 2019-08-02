package cst235.servicepals.model;

import java.util.ArrayList;
import java.util.List;

public class ServiceProvider {
	private String serviceName;
	private String phoneNumber;
	private double price;
	private List<String> availableTimeSlots = new ArrayList<>();
	
	//Constructor
	public ServiceProvider(String serviceName, String phoneNumber, double price) {
		this.serviceName = serviceName;
		this.phoneNumber = phoneNumber;
		this.price = price;
	}
	
	@Override
	public String toString() {
		return String.format("ServiceProvider [serviceName=%s, phoneNumber=%s, price=%s, availableTimeSlots=%s]",
				serviceName, phoneNumber, price, availableTimeSlots);
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
	
	public List<String> getAvailable() {
		return availableTimeSlots;
	}
}
