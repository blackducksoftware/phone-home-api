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
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.apache.commons.lang3.StringUtils;

import com.blackducksoftware.integration.log.IntLogger;
import com.blackducksoftware.integration.phone.home.enums.BlackDuckName;
import com.blackducksoftware.integration.phone.home.enums.PhoneHomeSource;
import com.blackducksoftware.integration.phone.home.enums.ThirdPartyName;
import com.blackducksoftware.integration.phone.home.exception.PhoneHomeArgumentException;
import com.blackducksoftware.integration.phone.home.exception.PhoneHomeConnectionException;
import com.blackducksoftware.integration.phone.home.exception.PhoneHomeException;
import com.blackducksoftware.integration.phone.home.util.OkAuthenticator;
import com.blackducksoftware.integration.phone.home.util.ProxyUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author nrowles
 *
 *         Client for the phone home server. This class contains three methods:
 *         callHome, and version of callHomeIntegrations. All three methods send
 *         information back to a specified REST endpoint.
 */
public class PhoneHomeClient {
    private final IntLogger logger;

    private final OkHttpClient.Builder builder = new OkHttpClient.Builder();

    private OkHttpClient client;

    private int timeout = 300;

    public PhoneHomeClient(final IntLogger logger) {
        this.logger = logger;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(final int timeout) {
        this.timeout = timeout;
    }

    /**
     * Clears the previously set System properties I.E. https.proxyHost,
     * https.proxyPort, http.proxyHost, http.proxyPort, http.nonProxyHosts
     *
     */
    public void setProxyProperties(final String proxyHost, final int proxyPort, final String proxyUser,
            final String decryptedProxyPassword, final String ignoredProxyHost) throws PhoneHomeArgumentException {
        if (StringUtils.isNotBlank(proxyHost) && proxyPort > 0) {
            boolean shouldUseProxy = true;
            if (StringUtils.isNotBlank(ignoredProxyHost)) {
                final PropertiesLoader propertiesLoader = new PropertiesLoader(logger);
                URL targetUrl;
                try {
                    targetUrl = new URL(propertiesLoader.createTargetUrl(PhoneHomeApiConstants.PROPERTIES_FILE_NAME));
                } catch (final Exception e) {
                    throw new PhoneHomeArgumentException(e.getMessage(), e);
                }

                final List<Pattern> ignoredProxyHostPatterns = ProxyUtil.getIgnoredProxyHostPatterns(ignoredProxyHost);
                shouldUseProxy = !ProxyUtil.shouldIgnoreHost(targetUrl.getHost(), ignoredProxyHostPatterns);
            }
            if (shouldUseProxy) {
                final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                builder.proxy(proxy);
                if (StringUtils.isNotBlank(proxyUser) && StringUtils.isNotBlank(decryptedProxyPassword)) {
                    builder.proxyAuthenticator(new OkAuthenticator(proxyUser, decryptedProxyPassword));
                }
            }
        }
    }

    private void createClient() {
        builder.connectTimeout(timeout, TimeUnit.SECONDS);
        builder.writeTimeout(timeout, TimeUnit.SECONDS);
        builder.readTimeout(timeout, TimeUnit.SECONDS);
        client = builder.build();
    }

    /**
     * @param info
     *            information to be sent to REST endpoint
     * @param targetUrl
     *            the URL to make a POST request to
     * @throws PhoneHomeException
     *
     *             This method posts to the specified 'targetUrl' the
     *             information contained in 'info'
     */
    public void callHome(final PhoneHomeInfo info, final String targetUrl) throws PhoneHomeException {
        if (info == null) {
            throw new PhoneHomeArgumentException("Could not find the information needed for the phone home.");
        }
        if (StringUtils.isBlank(targetUrl)) {
            throw new PhoneHomeArgumentException("The targetURL for the phone home was not provided.");
        }
        createClient();
        final HttpUrl httpUrl = HttpUrl.parse(targetUrl).newBuilder().build();

        final Gson gson = new GsonBuilder().create();
        final String json = gson.toJson(info);

        final RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);

        final Request request = new Request.Builder().url(httpUrl).post(body).build();

        Response response = null;
        try {
            try {
                response = client.newCall(request).execute();
            } catch (final IOException e) {
                throw new PhoneHomeConnectionException(e.getMessage(), e);
            }
            if (!response.isSuccessful()) {
                throw new PhoneHomeConnectionException(response.message());
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    /**
     * @param regId
     *            Registration Id of the 'BlackDuck Hub' instance in use
     * @param hostName
     *            Host name of the 'BlackDuck Hub' instance in use
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
     * @throws PhoneHomeException
     *
     *             This method is used to phone-home to the internal 'BlackDuck'
     *             Integrations server with integrations usage information.
     */
    public void callHomeIntegrations(final String regId, final String hostName, final String blackDuckName,
            final String blackDuckVersion,
            final String thirdPartyName,
            final String thirdPartyVersion, final String pluginVersion)
            throws PhoneHomeException {
        callHomeIntegrations(regId, hostName, blackDuckName, blackDuckVersion, thirdPartyName, thirdPartyVersion,
                pluginVersion,
                PhoneHomeSource.INTEGRATIONS, PhoneHomeApiConstants.PROPERTIES_FILE_NAME);
    }

    /**
     * @param regId
     *            Registration Id of the 'BlackDuck Hub' instance in use
     * @param hostName
     *            Host name of the 'BlackDuck Hub' instance in use
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
     * @throws PhoneHomeException
     *
     *             This method is used to phone-home to the internal 'BlackDuck'
     *             Integrations server with integrations usage information.
     */
    public void callHomeIntegrations(final String regId, final String hostName, final BlackDuckName blackDuckName,
            final String blackDuckVersion, final ThirdPartyName thirdPartyName,
            final String thirdPartyVersion, final String pluginVersion)
            throws PhoneHomeException {
        callHomeIntegrations(regId, hostName, blackDuckName.getName(), blackDuckVersion, thirdPartyName.getName(),
                thirdPartyVersion, pluginVersion,
                PhoneHomeSource.INTEGRATIONS, PhoneHomeApiConstants.PROPERTIES_FILE_NAME);
    }

    /**
     * @param regId
     *            Registration Id of the 'BlackDuck Hub' instance in use
     * @param hostName
     *            Host name of the 'BlackDuck Hub' instance in use
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
    public void callHomeIntegrations(final String regId, final String hostName, final String blackDuckName,
            final String blackDuckVersion,
            final String thirdPartyName,
            final String thirdPartyVersion, final String pluginVersion, final PhoneHomeSource source, final String propertiesPath)
            throws PhoneHomeException {
        validateIntegrationPhoneHome(regId, hostName, blackDuckName, blackDuckVersion, thirdPartyName, thirdPartyVersion, pluginVersion);
        final PropertiesLoader propertiesLoader = new PropertiesLoader(logger);
        String targetUrl;
        try {
            targetUrl = propertiesLoader.createTargetUrl(propertiesPath);
        } catch (final Exception e) {
            throw new PhoneHomeArgumentException(e.getMessage(), e);
        }
        logger.debug("Integrations phone-home URL: " + targetUrl);

        final Map<String, String> infoMap = new HashMap<>();
        infoMap.put(PhoneHomeApiConstants.BLACK_DUCK_NAME, blackDuckName);
        infoMap.put(PhoneHomeApiConstants.BLACK_DUCK_VERSION, blackDuckVersion);
        infoMap.put(PhoneHomeApiConstants.THIRD_PARTY_NAME, thirdPartyName);
        infoMap.put(PhoneHomeApiConstants.THIRD_PARTY_VERSION, thirdPartyVersion);
        infoMap.put(PhoneHomeApiConstants.PLUGIN_VERSION, pluginVersion);

        String hubIdentifier = null;
        if (regId != null) {
            hubIdentifier = regId;
        } else if (hostName != null) {
            hubIdentifier = md5Hash(hostName);
        }

        final PhoneHomeInfo info = new PhoneHomeInfo(hubIdentifier, source, infoMap);

        callHome(info, targetUrl);
    }

    private void validateIntegrationPhoneHome(final String regId, final String hostName, final String blackDuckName,
            final String blackDuckVersion,
            final String thirdPartyName,
            final String thirdPartyVersion, final String pluginVersion)
            throws PhoneHomeArgumentException {

        if (StringUtils.isBlank(regId) && StringUtils.isBlank(hostName)) {
            throw new PhoneHomeArgumentException("No identifier of the Hub server was provided for the phone home.");
        }
        if (StringUtils.isBlank(blackDuckName)) {
            throw new PhoneHomeArgumentException("The Black Duck product name was not provided for the phone home.");
        }
        if (StringUtils.isBlank(blackDuckVersion)) {
            throw new PhoneHomeArgumentException(String.format("The version of %s was not provided for the phone home.", blackDuckName));
        }
        if (StringUtils.isBlank(thirdPartyName)) {
            throw new PhoneHomeArgumentException("The third party name was not provided for the phone home.");
        }
        if (StringUtils.isBlank(thirdPartyVersion)) {
            throw new PhoneHomeArgumentException(String.format("The version of %s was not provided for the phone home.", thirdPartyName));
        }
        if (StringUtils.isBlank(pluginVersion)) {
            throw new PhoneHomeArgumentException("The plugin version was not provided for the phone home.");
        }
    }

    private String md5Hash(final String string) {
        try {
            final MessageDigest md = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
            final byte[] hashedBytes = md.digest(string.getBytes("UTF-8"));
            return DigestUtils.md5Hex(hashedBytes);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
