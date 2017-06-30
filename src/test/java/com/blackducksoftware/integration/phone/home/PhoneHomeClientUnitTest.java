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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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

import com.blackducksoftware.integration.log.IntBufferedLogger;
import com.blackducksoftware.integration.phone.home.enums.BlackDuckName;
import com.blackducksoftware.integration.phone.home.enums.PhoneHomeSource;
import com.blackducksoftware.integration.phone.home.enums.ThirdPartyName;
import com.blackducksoftware.integration.phone.home.exception.PhoneHomeException;

public class PhoneHomeClientUnitTest {
    public static String LOCALHOST = "127.0.0.1";

    public static String PROPERTY_TARGETPORT = "port";

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Rule
    public final MockServerRule msRule = new MockServerRule(this);

    private final MockServerClient msClient = new MockServerClient(LOCALHOST, msRule.getPort());

    private final int port = msRule.getPort();

    @Before
    public void startProxy()
            throws IOException, NumberFormatException, FileNotFoundException, SecurityException, UnknownHostException {

        msClient
        .when(
                new HttpRequest()
                .withPath("/test"))
        .respond(
                new HttpResponse()
                .withHeader(
                        new Header("Content-Type", "json")));

        String propPath = this.getClass().getClassLoader()
                .getResource(PhoneHomeApiConstants.MOCKSERVER_CONFIG_FILE_NAME).getPath();
        propPath = URLDecoder.decode(propPath, "UTF-8");
        final FileInputStream in = new FileInputStream(propPath);
        final Properties prop = new Properties();
        prop.load(in);
        in.close();

        final FileOutputStream out = new FileOutputStream(propPath);
        prop.setProperty(PROPERTY_TARGETPORT, Integer.toString(port));
        prop.store(out, null);
        out.close();

    }

    @After
    public void stopProxy() {
        // Intentionally left blank
    }

    @Test
    public void callHomeNull() throws Exception {
        exception.expect(PhoneHomeException.class);
        final PhoneHomeClient phClient = new PhoneHomeClient(new IntBufferedLogger());

        phClient.phoneHome(null);
    }

    @Test
    public void callHomeInvalidUrl() throws Exception {
        exception.expect(PhoneHomeException.class);
        final PhoneHomeClient phClient = new PhoneHomeClient(new IntBufferedLogger());

        final String regId = "regId";
        final PhoneHomeSource source = PhoneHomeSource.INTEGRATIONS;
        final Map<String, String> infoMap = new HashMap<>();
        final PhoneHomeRequest phoneHomeRequest = new PhoneHomeRequest(regId, source, infoMap);
        //final String targetUrl = "http://foo-bar/";

        phClient.phoneHome(phoneHomeRequest);
    }

    @Test
    public void callHomeValidUrl() throws Exception {
        final PhoneHomeClient phClient = new PhoneHomeClient(new IntBufferedLogger());

        final String regId = "regId";
        final PhoneHomeSource source = PhoneHomeSource.INTEGRATIONS;
        final Map<String, String> infoMap = new HashMap<>();
        final PhoneHomeRequest phoneHomeRequest = new PhoneHomeRequest(regId, source, infoMap);
        //final String targetUrl = LOCALHOST + ":" + this.port + "/test";

        phClient.phoneHome(phoneHomeRequest);
    }

    @Test
    public void callHomeIntegrationsTest() throws Exception {
        final PhoneHomeClient phClient = new PhoneHomeClient(new IntBufferedLogger());

        final String propertiesPath = PhoneHomeApiConstants.MOCKSERVER_CONFIG_FILE_NAME;

        final PhoneHomeRequestBuilder phoneHomeRequestBuilder = new PhoneHomeRequestBuilder();
        phoneHomeRequestBuilder.setRegistrationId("regKey");
        phoneHomeRequestBuilder.setHostName(null);
        phoneHomeRequestBuilder.setBlackDuckName(BlackDuckName.HUB);
        phoneHomeRequestBuilder.setBlackDuckVersion("blackDuckVersion");
        phoneHomeRequestBuilder.setPluginVersion("pluginVersion");
        phoneHomeRequestBuilder.setThirdPartyName(ThirdPartyName.JENKINS);
        phoneHomeRequestBuilder.setThirdPartyVersion("thirdPartyVersion");
        phoneHomeRequestBuilder.setSource(PhoneHomeSource.INTEGRATIONS);
        final PhoneHomeRequest phoneHomeRequest = phoneHomeRequestBuilder.build();

        phClient.phoneHome(phoneHomeRequest);
    }

    @Test
    public void callHomeIntegrationsTestWithHostName() throws Exception {
        final PhoneHomeClient phClient = new PhoneHomeClient(new IntBufferedLogger());

        final String propertiesPath = PhoneHomeApiConstants.MOCKSERVER_CONFIG_FILE_NAME;

        final PhoneHomeRequestBuilder phoneHomeRequestBuilder = new PhoneHomeRequestBuilder();
        phoneHomeRequestBuilder.setRegistrationId(null);
        phoneHomeRequestBuilder.setHostName("hostName");
        phoneHomeRequestBuilder.setBlackDuckName(BlackDuckName.HUB);
        phoneHomeRequestBuilder.setBlackDuckVersion("blackDuckVersion");
        phoneHomeRequestBuilder.setPluginVersion("pluginVersion");
        phoneHomeRequestBuilder.setThirdPartyName(ThirdPartyName.JENKINS);
        phoneHomeRequestBuilder.setThirdPartyVersion("thirdPartyVersion");
        phoneHomeRequestBuilder.setSource(PhoneHomeSource.INTEGRATIONS);
        final PhoneHomeRequest phoneHomeRequest = phoneHomeRequestBuilder.build();

        phClient.phoneHome(phoneHomeRequest);
    }

}
