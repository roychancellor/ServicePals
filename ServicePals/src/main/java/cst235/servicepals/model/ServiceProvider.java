package cst235.servicepals.model;

/**
 * Class definition for a ServiceProvider object
 */
public class ServiceProvider {
	private int serviceId;
	private String serviceCategory;
	private String serviceDescription;
	private String phoneNumber;
	private double servicePrice;
	
	//Constructors
	/**
	 * No-argument constructor for ServiceProvider
	 */
	public ServiceProvider() {
		
	}
	
	/**
	 * Constructor for a ServiceProvider with parameters
	 * @param serviceId the service category code for pre-defined categories
	 * @param serviceCategory the description of the pre-defined category
	 * @param serviceDescription the provider's description for the service
	 * @param phoneNumber the provider's phone number
	 * @param servicePrice the hourly price for the service
	 */
	 //TODO: look into different ways of pricing
	public ServiceProvider(int serviceId, String serviceCategory, String serviceDescription,
			String phoneNumber, double servicePrice) {
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

	// Getters and setters
	
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

	/**
	 * @return the serviceDescription
	 */
	public String getServiceDescription() {
		return serviceDescription;
	}

	/**
	 * @param serviceDescription the serviceDescription to set
	 */
	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}

	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the servicePrice
	 */
	public double getServicePrice() {
		return servicePrice;
	}

	/**
	 * @param servicePrice the servicePrice to set
	 */
	public void setServicePrice(double servicePrice) {
		this.servicePrice = servicePrice;
	}

}
