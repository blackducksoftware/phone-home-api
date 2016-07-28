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

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.Context;
import org.restlet.data.Reference;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.integration.phone.home.client.PhoneHomeClientApi;

/**
 * 
 * @author nrowles
 *
 * Client for the phone home server. This class contains three methods: callHome, and version of callHomeIntegrations.
 * All three methods send information back to a specified REST endpoint.
 */
public class PhoneHomeClient {

	private static final Logger logger = LoggerFactory.getLogger(PhoneHomeClient.class);
	
	/**
	 * 
	 * @param info					information to be sent to REST endpoint
	 * @param targetUrl				the URL to make a POST request to
	 * @throws ResourceException
	 * @throws JSONException
	 * 
	 * This method posts to the specified 'targetUrl' the information contained in 'info'
	 */
	public void callHome(PhoneHomeInfo info, String targetUrl) throws ResourceException, JSONException {
		final PhoneHomeClientApi client = ClientResource.create(new Context(), new Reference(targetUrl), PhoneHomeClientApi.class);
		
		client.postPhoneHomeInfo(info);
		
		int responseCode = client.getClientResource().getResponse().getStatus().getCode();
		if(responseCode >= 200 && responseCode < 300){
			logger.info("Phone Home Call Successful, status returned: " + responseCode);
		} else{
			throw new ResourceException(responseCode);
		}
	}
	
	/**
	 * 
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
	 * 
	 * This method is used to phone-home to the internal 'BlackDuck' Integrations server with integrations usage
	 * information. *NOTE:* This method, in most instances, SHOULD NOT be called. Instead, use
	 * 'callHomeIntegrations(String,String,String,String,String,String)'; as it points to a valid properties file
	 * containing the URL to the internal 'BlackDuck' server.
	 */
	public void callHomeIntegrations(String regId, String blackDuckName, String blackDuckVersion, String thirdPartyName, 
			String thirdPartyVersion, String pluginVersion, String propertiesPath) throws IOException, ResourceException, JSONException {
		final PropertiesLoader propertiesLoader = new PropertiesLoader();
		final String targetUrl = propertiesLoader.createTargetUrl(propertiesPath);
		
		final Map<String, String> infoMap = new HashMap<String, String>();
		infoMap.put("blackDuckName", blackDuckName);
		infoMap.put("blackDuckVersion", blackDuckVersion);
		infoMap.put("thirdPartyName", thirdPartyName);
		infoMap.put("thirdPartyVersion", thirdPartyVersion);
		infoMap.put("pluginVersion", pluginVersion);
		
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
	 * 
	 * This method is used to phone-home to the internal 'BlackDuck' Integrations server with integrations usage
	 * information.
	 */
	public void callHomeIntegrations(String regId, String blackDuckName, String blackDuckVersion, String thirdPartyName, 
			String thirdPartyVersion, String pluginVersion) throws IOException, ResourceException, JSONException {
		callHomeIntegrations(regId, blackDuckName, blackDuckVersion, thirdPartyName, thirdPartyVersion, pluginVersion, "config.properties");
	} 
	
}
