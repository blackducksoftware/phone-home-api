package com.blackducksoftware.integration.phone.home.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.restlet.data.MediaType;
import org.restlet.resource.ClientResource;

public class PhoneHomeInfo {
	//member info
	private String blackDuckName;
	private String blackDuckVersion;
	private String thirdPartyName;
	private String thirdPartyVersion;
	private String targetUrl;
	
	//ctors
	public PhoneHomeInfo(){
		this.targetUrl = buildUrlFromProperties();
	};
	public PhoneHomeInfo(String blackDuckName, 
			String blackDuckVersion, 
			String thirdPartyName, 
			String thirdPartyVersion){
		this.blackDuckName = blackDuckName;
		this.blackDuckVersion = blackDuckVersion;
		this.thirdPartyName = thirdPartyName;
		this.thirdPartyVersion = thirdPartyVersion;
		this.targetUrl = buildUrlFromProperties();
	}

	
	//getters and setters
	public String getBlackDuckName() {
		return blackDuckName;
	}
	public void setBlackDuckName(String blackDuckName) {
		this.blackDuckName = blackDuckName;
	}
	public String getBlackDuckVersion() {
		return blackDuckVersion;
	}
	public void setBlackDuckVersion(String blackDuckVersion) {
		this.blackDuckVersion = blackDuckVersion;
	}
	public String getThirdPartyName() {
		return thirdPartyName;
	}
	public void setThirdPartyName(String thirdPartyName) {
		this.thirdPartyName = thirdPartyName;
	}
	public String getThirdPartyVersion() {
		return thirdPartyVersion;
	}
	public void setThirdPartyVersion(String thirdPartyVersion) {
		this.thirdPartyVersion = thirdPartyVersion;
	}
	public String getTargetUrl() {
		return targetUrl;
	}
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
	
	
	//TODO fix so this doesn't load properties file every time this method is called, possible
	//     change so that it loads it when ctor is called?
	public void phoneHome(){
		ClientResource clientResource = new ClientResource(targetUrl);
		PhoneHomeServerApi ar = clientResource.wrap(PhoneHomeServerApi.class);
		clientResource.post(this, MediaType.APPLICATION_JSON);
		
		//TODO get response from server
	}
	
	private String buildUrlFromProperties(){
		Properties properties = new Properties();
		String propertiesFileName = "config.properties";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);
		
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		StringBuilder target = new StringBuilder();
		target.append(properties.getProperty("targetUrl"));
		target.append(":");
		target.append(properties.getProperty("targetPort"));
		target.append("/");
		target.append(properties.getProperty("targetExt"));
		return target.toString();
	}
	
}