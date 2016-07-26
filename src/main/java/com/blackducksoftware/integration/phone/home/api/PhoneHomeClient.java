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

package com.blackducksoftware.integration.phone.home.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author nrowles
 *
 */
public class PhoneHomeClient {
	private static final Logger logger = LoggerFactory.getLogger(PhoneHomeClient.class);
	
	public void callHome(PhoneHomeInfo info, String targetUrl) throws ResourceException, JSONException {
		final ClientResource clientResource = new ClientResource(targetUrl);
		
		final JSONObject jsonOb = new JSONObject();
		jsonOb.accumulate("regId", info.getRegId());
		for(final String infoKey : info.getInfoMap().keySet()){
			final String value = info.getInfoMap().get(infoKey);
			jsonOb.accumulate(infoKey, value);
		}
		
		JsonRepresentation jsonRep = new JsonRepresentation(jsonOb);
		clientResource.post(jsonRep);
	}
	
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
	
	public void callHomeIntegrations(String regId, String blackDuckName, String blackDuckVersion, String thirdPartyName, 
			String thirdPartyVersion, String pluginVersion) throws IOException, ResourceException, JSONException {
		callHomeIntegrations(regId, blackDuckName, blackDuckVersion, thirdPartyName, pluginVersion, "config.properties");
	} 
	
}
