package com.blackducksoftware.integration.phone.home.enums;

public enum BlackDuckName {
	HUB ("Hub"), 
	PROTEX ("Protex"), 
	CODE_CENTER ("Code-Center");
	
	private final String name;
	
	private BlackDuckName(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
}
