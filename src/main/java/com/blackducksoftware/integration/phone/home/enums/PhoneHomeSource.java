package com.blackducksoftware.integration.phone.home.enums;

public enum PhoneHomeSource {
	HUB, INTEGRATIONS;
	
	@Override
	public String toString(){
		String s = null;
		
		switch(this) {
		case HUB: 
			s = "Hub";
			break;
		case INTEGRATIONS:
			s = "Integrations";
			break;
		}
		
		return s;
	}
}
