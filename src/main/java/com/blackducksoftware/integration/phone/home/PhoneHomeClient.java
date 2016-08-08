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

import org.apache.commons.lang3.StringUtils;
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
import com.blackducksoftware.integration.phone.home.util.AuthenticatorUtil;

/**
 * @author nrowles
 *
 *         Client for the phone home server. This class contains three methods:
 *         callHome, and version of callHomeIntegrations. All three methods send
 *         information back to a specified REST endpoint.
 */
public class PhoneHomeClient {

	private final Logger logger = LoggerFactory.getLogger(PhoneHomeClient.class);

	/**
	 * Clears the previously set System properties I.E. https.proxyHost,
	 * https.proxyPort, http.proxyHost, http.proxyPort, http.nonProxyHosts
	 *
	 */
	private void cleanUpOldProxySettings() {

		System.clearProperty("http.proxyHost");
		System.clearProperty("http.proxyPort");
		System.clearProperty("http.nonProxyHosts");

		AuthenticatorUtil.resetAuthenticator();
	}

	/**
	 * The proxy settings get set as System properties. I.E. https.proxyHost,
	 * https.proxyPort, http.proxyHost, http.proxyPort, http.nonProxyHosts
	 *
	 */
	public void setProxyProperties(final String proxyHost, final int proxyPort, final String proxyUser,
			final String decryptedProxyPassword, final String ignoredProxyHost) {
		cleanUpOldProxySettings();

		if (!StringUtils.isBlank(proxyHost) && proxyPort > 0) {
			if (logger != null) {
				logger.debug("Using Proxy : " + proxyHost + ", at Port : " + proxyPort);
			}

			System.setProperty("http.proxyHost", proxyHost);
			System.setProperty("http.proxyPort", Integer.toString(proxyPort));

			try {
				if (!StringUtils.isBlank(proxyUser) && !StringUtils.isBlank(decryptedProxyPassword)) {
					AuthenticatorUtil.setAuthenticator(proxyUser, decryptedProxyPassword);
				}
			} catch (final Exception e) {
				if (logger != null) {
					logger.debug("Error setting up the Java Authenticator.", e);
				}
			}
		}
		if (!StringUtils.isBlank(ignoredProxyHost)) {
			System.setProperty("http.nonProxyHosts", ignoredProxyHost.replaceAll(",", "|"));
		}
	}

	/**
	 * @param info
	 *            information to be sent to REST endpoint
	 * @param targetUrl
	 *            the URL to make a POST request to
	 * @throws ResourceException
	 * @throws PhoneHomeException
	 *
	 *             This method posts to the specified 'targetUrl' the
	 *             information contained in 'info'
	 */
	public void callHome(PhoneHomeInfo info, final String targetUrl) throws ResourceException, PhoneHomeException {
		try {
			info = Objects.requireNonNull(info);
			info = Objects.requireNonNull(info);
		} catch (final NullPointerException e) {
			throw new PhoneHomeException("Expected parameters to not be null");
		}

		final PhoneHomeClientApi client = ClientResource.create(new Context(), new Reference(targetUrl),
				PhoneHomeClientApi.class);

		client.getClientResource().setEntityBuffering(true);
		logger.info("PhoneHomeInfo: " + info.toString());

		client.postPhoneHomeInfo(info);
		client.getClientResource().release();
		
		final int responseCode = client.getClientResource().getResponse().getStatus().getCode();
		if (responseCode >= 200 && responseCode < 300) {
			logger.info("Phone Home Call Successful, status returned: " + responseCode);
		} else {
			throw new PhoneHomeException("Error from server when phoning-home: " + responseCode);
		}
	}

	/**
	 * @param regId
	 *            Registration ID of the 'BlackDuck Hub' instance in use
	 * @param blackDuckName
	 *            Name of the 'BlackDuck' product
	 * @param blackDuckVersion
	 *            Version of the 'BlackDuck' product
	 * @param thirdPartyName
	 *            Name of the third party product integrated with the given
	 *            'BlackDuck' product
	 * @param thirdPartyVersion
	 *            Version of the third party product integrated with the given
	 *            'BlackDuck' product
	 * @param pluginVersion
	 *            Version of the 'BlackDuck' integration with the third party
	 *            product
	 * @param propertiesPath
	 *            Path to a properties file which contains the URL of the REST
	 *            endpoint
	 * @throws IOException
	 * @throws ResourceException
	 * @throws JSONException
	 * @throws PropertiesLoaderException
	 * @throws PhoneHomeException
	 *
	 *             This method is used to phone-home to the internal 'BlackDuck'
	 *             Integrations server with integrations usage information.
	 *             *NOTE:* This method, in most instances, SHOULD NOT be called.
	 *             Instead, use
	 *             'callHomeIntegrations(String,String,String,String,String,String)';
	 *             as it points to a valid properties file containing the URL to
	 *             the internal 'BlackDuck' server.
	 */
	public void callHomeIntegrations(final String regId, final String blackDuckName, final String blackDuckVersion, final String thirdPartyName,
			final String thirdPartyVersion, final String pluginVersion, final String source, final String propertiesPath)
					throws IOException, ResourceException, JSONException, PropertiesLoaderException, PhoneHomeException {

		final PropertiesLoader propertiesLoader = new PropertiesLoader();
		final String targetUrl = propertiesLoader.createTargetUrl(propertiesPath);
		logger.debug("Integrations phone-home URL: " + targetUrl);

		final Map<String, String> infoMap = new HashMap<String, String>();
		infoMap.put(PhoneHomeApiConstants.BLACK_DUCK_NAME, blackDuckName);
		infoMap.put(PhoneHomeApiConstants.BLACK_DUCK_VERSION, blackDuckVersion);
		infoMap.put(PhoneHomeApiConstants.THIRD_PARTY_NAME, thirdPartyName);
		infoMap.put(PhoneHomeApiConstants.THIRD_PARTY_VERSION, thirdPartyVersion);
		infoMap.put(PhoneHomeApiConstants.PLUGIN_VERSION, pluginVersion);

		final PhoneHomeInfo info = new PhoneHomeInfo(regId, source, infoMap);

		logger.info("PhoneHomeInfo info: " + info.toString());
		callHome(info, targetUrl);
	}

	/**
	 *
	 * @param regId
	 *            Registration ID of the 'BlackDuck Hub' instance in use
	 * @param blackDuckName
	 *            Name of the 'BlackDuck' product
	 * @param blackDuckVersion
	 *            Version of the 'BlackDuck' product
	 * @param thirdPartyName
	 *            Name of the third party product integrated with the given
	 *            'BlackDuck' product
	 * @param thirdPartyVersion
	 *            Version of the third party product integrated with the given
	 *            'BlackDuck' product
	 * @param pluginVersion
	 *            Version of the 'BlackDuck' integration with the third party
	 *            product
	 * @throws IOException
	 * @throws ResourceException
	 * @throws JSONException
	 * @throws PropertiesLoaderException
	 * @throws PhoneHomeException
	 *
	 *             This method is used to phone-home to the internal 'BlackDuck'
	 *             Integrations server with integrations usage information.
	 */
	public void callHomeIntegrations(final String regId, final String blackDuckName, final String blackDuckVersion, final String thirdPartyName,
			final String thirdPartyVersion, final String pluginVersion)
					throws IOException, ResourceException, JSONException, PropertiesLoaderException, PhoneHomeException {

		callHomeIntegrations(regId, blackDuckName, blackDuckVersion, thirdPartyName, thirdPartyVersion, pluginVersion,
				PhoneHomeApiConstants.INTEGRATIONS, PhoneHomeApiConstants.PROPERTIES_FILE_NAME);
	}
}
