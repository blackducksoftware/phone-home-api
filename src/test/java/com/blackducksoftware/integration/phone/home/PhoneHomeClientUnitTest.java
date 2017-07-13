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
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import com.blackducksoftware.integration.hub.rest.RestConnection;
import com.blackducksoftware.integration.hub.rest.UnauthenticatedRestConnection;
import com.blackducksoftware.integration.log.IntBufferedLogger;
import com.blackducksoftware.integration.phone.home.enums.BlackDuckName;
import com.blackducksoftware.integration.phone.home.enums.PhoneHomeSource;
import com.blackducksoftware.integration.phone.home.enums.ThirdPartyName;
import com.blackducksoftware.integration.phone.home.exception.PhoneHomeException;

public class PhoneHomeClientUnitTest {
    public static final String LOCALHOST = "127.0.0.1";

    public static final int TIMEOUT = 5;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Rule
    public final MockServerRule msRule = new MockServerRule(this);

    private final MockServerClient msClient = new MockServerClient(LOCALHOST, msRule.getPort());

    private final int port = msRule.getPort();

    @Before
    public void startProxy(){
        msClient
        .when(
                new HttpRequest()
                .withPath("/test"))
        .respond(
                new HttpResponse()
                .withHeader(
                        new Header("Content-Type", "json")));
    }

    @After
    public void stopProxy() {
        // Intentionally left blank
    }

    @Test
    public void callHomeInvalidUrl() throws Exception {
        exception.expect(PhoneHomeException.class);
        final String targetUrl = "http://example.com:"+this.port+"/test";
        final URL url = new URL(targetUrl);
        final RestConnection restConnection = new UnauthenticatedRestConnection(new IntBufferedLogger(), url, TIMEOUT);
        final PhoneHomeClient phClient = new PhoneHomeClient(new IntBufferedLogger(), url, restConnection);

        final String regId = "regId";
        final PhoneHomeSource source = PhoneHomeSource.INTEGRATIONS;
        final Map<String, String> infoMap = new HashMap<>();
        final PhoneHomeRequestBody phoneHomeRequest = new PhoneHomeRequestBody(regId, source, infoMap);

        phClient.postPhoneHomeRequest(phoneHomeRequest);
    }

    @Test
    public void callHomeValidUrl() throws Exception {
        final String targetUrl = "http://"+LOCALHOST + ":" + this.port + "/test";
        final URL url = new URL(targetUrl);
        final RestConnection restConnection = new UnauthenticatedRestConnection(new IntBufferedLogger(), url, TIMEOUT);
        final PhoneHomeClient phClient = new PhoneHomeClient(new IntBufferedLogger(), url, restConnection);
        final String regId = "regId";
        final PhoneHomeSource source = PhoneHomeSource.INTEGRATIONS;
        final Map<String, String> infoMap = new HashMap<>();
        final PhoneHomeRequestBody phoneHomeRequest = new PhoneHomeRequestBody(regId, source, infoMap);

        phClient.postPhoneHomeRequest(phoneHomeRequest);
    }

    @Test
    public void callHomeIntegrationsTest() throws Exception {
        final String targetUrl = "http://"+LOCALHOST + ":" + this.port + "/test";
        final URL url = new URL(targetUrl);
        final RestConnection restConnection = new UnauthenticatedRestConnection(new IntBufferedLogger(), url, TIMEOUT);
        final PhoneHomeClient phClient = new PhoneHomeClient(new IntBufferedLogger(), url, restConnection);

        final PhoneHomeRequestBodyBuilder phoneHomeRequestBuilder = new PhoneHomeRequestBodyBuilder();
        phoneHomeRequestBuilder.setRegistrationId("regKey");
        phoneHomeRequestBuilder.setHostName(null);
        phoneHomeRequestBuilder.setBlackDuckName(BlackDuckName.HUB);
        phoneHomeRequestBuilder.setBlackDuckVersion("blackDuckVersion");
        phoneHomeRequestBuilder.setPluginVersion("pluginVersion");
        phoneHomeRequestBuilder.setThirdPartyName(ThirdPartyName.JENKINS);
        phoneHomeRequestBuilder.setThirdPartyVersion("thirdPartyVersion");
        phoneHomeRequestBuilder.setSource(PhoneHomeSource.INTEGRATIONS);
        final PhoneHomeRequestBody phoneHomeRequest = phoneHomeRequestBuilder.build();

        phClient.postPhoneHomeRequest(phoneHomeRequest);
    }

    @Test
    public void callHomeIntegrationsTestWithHostName() throws Exception {
        final String targetUrl = "http://"+LOCALHOST + ":" + this.port + "/test";
        final URL url = new URL(targetUrl);
        final RestConnection restConnection = new UnauthenticatedRestConnection(new IntBufferedLogger(), url, TIMEOUT);
        final PhoneHomeClient phClient = new PhoneHomeClient(new IntBufferedLogger(), url, restConnection);

        final PhoneHomeRequestBodyBuilder phoneHomeRequestBuilder = new PhoneHomeRequestBodyBuilder();
        phoneHomeRequestBuilder.setRegistrationId(null);
        phoneHomeRequestBuilder.setHostName("hostName");
        phoneHomeRequestBuilder.setBlackDuckName(BlackDuckName.HUB);
        phoneHomeRequestBuilder.setBlackDuckVersion("blackDuckVersion");
        phoneHomeRequestBuilder.setPluginVersion("pluginVersion");
        phoneHomeRequestBuilder.setThirdPartyName(ThirdPartyName.JENKINS);
        phoneHomeRequestBuilder.setThirdPartyVersion("thirdPartyVersion");
        phoneHomeRequestBuilder.setSource(PhoneHomeSource.INTEGRATIONS);
        final PhoneHomeRequestBody phoneHomeRequest = phoneHomeRequestBuilder.build();

        phClient.postPhoneHomeRequest(phoneHomeRequest);
    }

}
