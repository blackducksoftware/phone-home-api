/**
 * Phone Home API
 *
 * Copyright (C) 2017 Black Duck Software, Inc.
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
 */
package com.blackducksoftware.integration.phone.home;

import java.net.MalformedURLException;
import java.net.URL;

import com.blackducksoftware.integration.exception.IntegrationException;
import com.blackducksoftware.integration.hub.request.HubRequest;
import com.blackducksoftware.integration.hub.rest.RestConnection;
import com.blackducksoftware.integration.hub.rest.UnauthenticatedRestConnection;
import com.blackducksoftware.integration.log.IntLogger;
import com.blackducksoftware.integration.phone.home.enums.BlackDuckName;
import com.blackducksoftware.integration.phone.home.enums.PhoneHomeSource;
import com.blackducksoftware.integration.phone.home.enums.ThirdPartyName;
import com.blackducksoftware.integration.phone.home.exception.PhoneHomeException;

public class PhoneHomeClient {
    private final IntLogger logger;

    private URL targetUrl = null;

    private int timeout = 300;

    private int proxyPort;

    private String proxyHost;

    private String proxyUsername;

    private String proxyPassword;

    private String proxyNoHosts;

    private boolean hasProxy = false;

    public PhoneHomeClient(final IntLogger logger) {
        this.logger = logger;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }

    public void callHome(final PhoneHomeRequest phoneHomeRequest) throws PhoneHomeException {
        if (targetUrl == null) {
            throw new PhoneHomeException("No server url found.");
        }
        final RestConnection restConnection = new UnauthenticatedRestConnection(logger, targetUrl, timeout);
        if (hasProxy){
            restConnection.proxyHost = proxyHost;
            restConnection.proxyPort = proxyPort;
            restConnection.proxyNoHosts = proxyNoHosts;
            restConnection.proxyUsername = proxyUsername;
            restConnection.proxyPassword = proxyPassword;
        }
        final HubRequest request = new HubRequest(restConnection);
        try {
            request.executePost(restConnection.gson.toJson(phoneHomeRequest));
        } catch (final IntegrationException e) {
            throw new PhoneHomeException(e.getMessage(), e);
        }
    }

    public void callHomeIntegrations(final String regId, final String hostName,
            final BlackDuckName blackDuckName, final String blackDuckVersion,
            final ThirdPartyName thirdPartyName, final String thirdPartyVersion,
            final String pluginVersion)
                    throws PhoneHomeException {
        callHome(regId, hostName, blackDuckName, blackDuckVersion, thirdPartyName,
                thirdPartyVersion, pluginVersion,
                PhoneHomeSource.INTEGRATIONS, PhoneHomeApiConstants.PROPERTIES_FILE_NAME);
    }

    public void callHome(final String regId, final String hostName,
            final BlackDuckName blackDuckName, final String blackDuckVersion,
            final ThirdPartyName thirdPartyName, final String thirdPartyVersion,
            final String pluginVersion, final PhoneHomeSource source, final String propertiesPath)
                    throws PhoneHomeException {
        //validateIntegrationPhoneHome(regId, hostName, blackDuckName, blackDuckVersion, thirdPartyName, thirdPartyVersion, pluginVersion);
        logger.debug("Integrations phone-home URL: " + targetUrl);

        final PhoneHomeRequestBuilder requestBuilder = new PhoneHomeRequestBuilder();
        requestBuilder.regId = regId;
        requestBuilder.hostName = hostName;
        requestBuilder.blackDuckName = blackDuckName;
        requestBuilder.blackDuckVersion = blackDuckVersion;
        requestBuilder.thirdPartyName = thirdPartyName;
        requestBuilder.thirdPartyVersion = thirdPartyVersion;
        requestBuilder.pluginVersion = pluginVersion;
        requestBuilder.source = source;
        final PhoneHomeRequest phoneHomeRequest = requestBuilder.build();

        callHome(phoneHomeRequest);
    }

    public void setServerInfo(final String protocol, final String host, final int port, final String extension) throws PhoneHomeException{
        try {
            targetUrl = new URL(protocol, host, port, extension);
        } catch (final MalformedURLException e) {
            throw new PhoneHomeException(e.getMessage(), e);
        }
    }

    public void setProxyInfo(final String host, final int port, final String username, final String decryptedPassword,
            final String ignoredProxyHosts) {
        hasProxy = true;
        proxyHost = host;
        proxyPort = port;
        proxyUsername = username;
        proxyPassword = decryptedPassword;
        proxyNoHosts = ignoredProxyHosts;
    }

}
