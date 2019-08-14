package cst235.servicepals.model;

import java.util.ArrayList;
import java.util.List;

public class ServiceProvider {
	private int serviceId;
	private String serviceCategory;
	private String serviceDescription;
	private String phoneNumber;
	private double servicePrice;
	private List<String> availableTimeSlots = new ArrayList<>();
	
	//Constructor
	public ServiceProvider() {
		
	}
	
	public ServiceProvider(int serviceId, String serviceCategory, String serviceDescription, String phoneNumber, double servicePrice) {
		this.serviceId = serviceId;
		this.serviceCategory = serviceCategory;
		this.serviceDescription = serviceDescription;
		this.phoneNumber = phoneNumber;
		this.servicePrice = servicePrice;
	}
	
	@Override
	public String toString() {
		return "SP NAME: " + serviceDescription + ", PHONE: " + phoneNumber + ", PRICE: " + servicePrice;
	}

	/**
	 * @return the serviceId
	 */
	public int getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId the serviceId to set
	 */
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	/**
	 * @return the serviceCategory
	 */
	public String getServiceCategory() {
		return serviceCategory;
	}

	/**
	 * @param serviceCategory the serviceCategory to set
	 */
	public void setServiceCategory(String serviceCategory) {
		this.serviceCategory = serviceCategory;
	}

	public String getServiceDescription() {
		return serviceDescription;
	}
	
	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public double getServicePrice() {
		return servicePrice;
	}
	
	public void setServicePrice(double servicePrice) {
		this.servicePrice = servicePrice;
	}
	
	public List<String> getAvailableTimeSlots() {
		return availableTimeSlots;
	}
}
