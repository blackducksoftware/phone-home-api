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

import java.net.URL;

import com.blackducksoftware.integration.exception.IntegrationException;
import com.blackducksoftware.integration.hub.request.HubRequest;
import com.blackducksoftware.integration.hub.rest.RestConnection;
import com.blackducksoftware.integration.hub.rest.UnauthenticatedRestConnection;
import com.blackducksoftware.integration.log.IntLogger;
import com.blackducksoftware.integration.phone.home.exception.PhoneHomeException;

public class PhoneHomeClient {
    public static final String PHONE_HOME_BACKEND_URL = "";

    private final IntLogger logger;

    private URL phoneHomeBackendUrl;

    private int timeout = 300;

    private int proxyPort;

    private String proxyHost;

    private String proxyUsername;

    private String proxyPassword;

    private String proxyNoHosts;

    public PhoneHomeClient(final IntLogger logger) {
        this.logger = logger;
    }

    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setProxyInfo(final String host, final int port, final String username, final String decryptedPassword,
            final String ignoredProxyHosts) {
        proxyHost = host;
        proxyPort = port;
        proxyUsername = username;
        proxyPassword = decryptedPassword;
        proxyNoHosts = ignoredProxyHosts;
    }

    public void phoneHome(final PhoneHomeRequest phoneHomeRequest) throws PhoneHomeException {
        if (phoneHomeBackendUrl == null) {
            throw new PhoneHomeException("No server url found.");
        }
        logger.debug("Phoning home to " + phoneHomeBackendUrl);
        final RestConnection restConnection = new UnauthenticatedRestConnection(logger, phoneHomeBackendUrl, timeout);
        restConnection.proxyHost = proxyHost;
        restConnection.proxyPort = proxyPort;
        restConnection.proxyNoHosts = proxyNoHosts;
        restConnection.proxyUsername = proxyUsername;
        restConnection.proxyPassword = proxyPassword;
        final HubRequest request = new HubRequest(restConnection);
        try {
            request.executePost(restConnection.gson.toJson(phoneHomeRequest));
        } catch (final IntegrationException e) {
            throw new PhoneHomeException(e.getMessage(), e);
        }
    }

}
