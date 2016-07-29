/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
 * http://www.blackducksoftware.com/
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/

package com.blackducksoftware.integration.phone.home;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.json.JSONException;
import org.restlet.Context;
import org.restlet.data.Reference;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.integration.phone.home.client.PhoneHomeClientApi;
import com.blackducksoftware.integration.phone.home.exception.PhoneHomeException;
import com.blackducksoftware.integration.phone.home.exception.PropertiesLoaderException;

/**
 * @author nrowles
 *
 * Client for the phone home server. This class contains three methods: callHome, and version of callHomeIntegrations.
 * All three methods send information back to a specified REST endpoint.
 */
public class PhoneHomeClient {

	private final Logger logger = LoggerFactory.getLogger(PhoneHomeClient.class);
	
	/**
	 * @param info					information to be sent to REST endpoint
	 * @param targetUrl				the URL to make a POST request to
	 * @throws ResourceException
	 * @throws PhoneHomeException 
	 * 
	 * This method posts to the specified 'targetUrl' the information contained in 'info'
	 */
	public void callHome(PhoneHomeInfo info, String targetUrl) throws ResourceException, PhoneHomeException {
		try{
			info = Objects.requireNonNull(info);
			info = Objects.requireNonNull(info);
		} catch (NullPointerException e){
			throw new PhoneHomeException("Expected parameters to not be null");
		}
		
		final PhoneHomeClientApi client = ClientResource.create(new Context(), new Reference(targetUrl), PhoneHomeClientApi.class);
		
		client.postPhoneHomeInfo(info);
		
		int responseCode = client.getClientResource().getResponse().getStatus().getCode();
		if(responseCode >= 200 && responseCode < 300){
			logger.info("Phone Home Call Successful, status returned: " + responseCode);
		} else{
			throw new PhoneHomeException("Error from server when phoning-home: " + responseCode);
		}
	}
	
	/**
	 * @param regId					Registration ID of the 'BlackDuck Hub' instance in use
	 * @param blackDuckName			Name of the 'BlackDuck' product
	 * @param blackDuckVersion		Version of the 'BlackDuck' product
	 * @param thirdPartyName		Name of the third party product integrated with the given 'BlackDuck' product
	 * @param thirdPartyVersion		Version of the third party product integrated with the given 'BlackDuck' product
	 * @param pluginVersion			Version of the 'BlackDuck' integration with the third party product
	 * @param propertiesPath		Path to a properties file which contains the URL of the REST endpoint
	 * @throws IOException
	 * @throws ResourceException
	 * @throws JSONException
	 * @throws PropertiesLoaderException 
	 * @throws PhoneHomeException 
	 * 
	 * This method is used to phone-home to the internal 'BlackDuck' Integrations server with integrations usage
	 * information. *NOTE:* This method, in most instances, SHOULD NOT be called. Instead, use
	 * 'callHomeIntegrations(String,String,String,String,String,String)'; as it points to a valid properties file
	 * containing the URL to the internal 'BlackDuck' server.
	 */
	public void callHomeIntegrations(String regId, String blackDuckName, String blackDuckVersion, String thirdPartyName, 
			String thirdPartyVersion, String pluginVersion, String propertiesPath) throws IOException, ResourceException, 
			JSONException, PropertiesLoaderException, PhoneHomeException {
		
		final PropertiesLoader propertiesLoader = new PropertiesLoader();
		final String targetUrl = propertiesLoader.createTargetUrl(propertiesPath);
		
		final Map<String, String> infoMap = new HashMap<String, String>();
		infoMap.put(PhoneHomeApiConstants.BLACK_DUCK_NAME, blackDuckName);
		infoMap.put(PhoneHomeApiConstants.BLACK_DUCK_VERSION, blackDuckVersion);
		infoMap.put(PhoneHomeApiConstants.THIRD_PARTY_NAME, thirdPartyName);
		infoMap.put(PhoneHomeApiConstants.THIRD_PARTY_VERSION, thirdPartyVersion);
		infoMap.put(PhoneHomeApiConstants.PLUGIN_VERSION, pluginVersion);
		
		final PhoneHomeInfo info = new PhoneHomeInfo(regId, infoMap);
		
		callHome(info, targetUrl);
	}
	
	/**
	 * 
	 * @param regId					Registration ID of the 'BlackDuck Hub' instance in use
	 * @param blackDuckName			Name of the 'BlackDuck' product
	 * @param blackDuckVersion		Version of the 'BlackDuck' product
	 * @param thirdPartyName		Name of the third party product integrated with the given 'BlackDuck' product
	 * @param thirdPartyVersion		Version of the third party product integrated with the given 'BlackDuck' product
	 * @param pluginVersion			Version of the 'BlackDuck' integration with the third party product
	 * @throws IOException
	 * @throws ResourceException
	 * @throws JSONException
	 * @throws PropertiesLoaderException 
	 * @throws PhoneHomeException 
	 * 
	 * This method is used to phone-home to the internal 'BlackDuck' Integrations server with integrations usage
	 * information.
	 */
	public void callHomeIntegrations(String regId, String blackDuckName, String blackDuckVersion, String thirdPartyName, 
			String thirdPartyVersion, String pluginVersion) throws IOException, ResourceException, JSONException, PropertiesLoaderException, PhoneHomeException {
		
		callHomeIntegrations(regId, blackDuckName, blackDuckVersion, thirdPartyName, thirdPartyVersion, pluginVersion, PhoneHomeApiConstants.PROPERTIES_FILE_NAME);
	} 
	
}
