package CST_305.ServicePals;

import java.util.ArrayList;
import java.util.List;

public class ServiceProvider {

	private List<String> available = new ArrayList<>();
	private double price;
	private String serviceName;
	private int serviceNum;
	private String phoneNumber;
	private String firstName;
	private String lastName;
	
	public ServiceProvider(String firstName, String lastName, String serviceName, int serviceNum, String phoneNumber , double price) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.price = price;
		this.serviceName = serviceName;
		this.serviceNum = serviceNum;
		this.phoneNumber = phoneNumber;
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
	public ServiceProvider() {
		
	}
	
	public ServiceProvider(String serviceName, double price) {
		this.price = price;
		this.serviceName = serviceName;
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

	public void setAvailable(List<String> available) {
		this.available = available;
	}
	
}
