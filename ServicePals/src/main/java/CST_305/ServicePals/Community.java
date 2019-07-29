package CST_305.ServicePals;

import java.util.ArrayList;
import java.util.List;

public class Community {
	static List<Community> comm = new ArrayList<>();
	private String name;
	private String access;
	List<ServiceProvider> providers = new ArrayList<>();
	static Community current;

	
	Community(){
	
	}
	
	Community(String name, String access) {
		this.setName(name);
		this.access = access;
	}
	
	Community(ServiceProvider provider){
		providers.add(provider);
	}

	public void displayServices() {
		for(ServiceProvider serv : providers) {
			serv.displayService();
		}
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
