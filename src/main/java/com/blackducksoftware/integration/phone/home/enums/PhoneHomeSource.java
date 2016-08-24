package com.blackducksoftware.integration.phone.home.enums;

public enum PhoneHomeSource {
	HUB ("Hub"), 
	INTEGRATIONS ("Integrations");
	
	private String name;
	
	private PhoneHomeSource(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}
