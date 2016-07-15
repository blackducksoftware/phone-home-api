package com.blackducksoftware.integration.phone.home.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Properties;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import org.json.JSONObject;

/**
 * 
 * @author nrowles
 *
 */
public class PhoneHomeInfo{
	
	private String blackDuckName;
	private String blackDuckVersion;
	private String thirdPartyName;
	private String thirdPartyVersion;
	private String pluginVersion;
	private String targetUrl;
	
	//constructors
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
	public String getPluginVersion() {
		return pluginVersion;
	}
	public void setPluginVersion(String pluginVersion) {
		this.pluginVersion = pluginVersion;
	}
	public String getTargetUrl() {
		return targetUrl;
	}
	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}
	
	/**
	 * This method sends the information stored in this object to the 
	 * server specified in config.properties
	 */
	public void phoneHome(){
		ClientResource clientResource = new ClientResource(targetUrl);
		
		JSONObject jsonOb = new JSONObject();
		jsonOb.accumulate("blackDuckName", this.blackDuckName);
		jsonOb.accumulate("blackDuckVersion", this.blackDuckVersion);
		jsonOb.accumulate("thirdPartyName", this.thirdPartyName);
		jsonOb.accumulate("thirdPartyVersion", this.thirdPartyVersion);
		jsonOb.accumulate("pluginVersion", this.pluginVersion);
		
		JsonRepresentation jsonRep = new JsonRepresentation(jsonOb);
		clientResource.post(jsonRep);
	}
	
	/**
	 * 
	 * @return Url of the target server
	 * 
	 * This method returns builds the server url from the included config.properties file
	 */
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