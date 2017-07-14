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
package com.blackducksoftware.integration.phonehome;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import com.blackducksoftware.integration.builder.AbstractBuilder;
import com.blackducksoftware.integration.phonehome.enums.BlackDuckName;
import com.blackducksoftware.integration.phonehome.enums.PhoneHomeSource;
import com.blackducksoftware.integration.phonehome.enums.ThirdPartyName;

public class PhoneHomeRequestBodyBuilder extends AbstractBuilder<PhoneHomeRequestBody>{
    private String registrationId;

    private String hostName;

    private BlackDuckName blackDuckName;

    private String blackDuckVersion;

    private ThirdPartyName thirdPartyName;

    private String thirdPartyVersion;

    private String pluginVersion;

    private PhoneHomeSource source;

    private final Map<String, String> metaDataMap = new HashMap<>();

    @Override
    public PhoneHomeRequestBody buildObject() {
        String hubIdentifier = null;
        if (registrationId != null) {
            hubIdentifier = registrationId;
        } else if (hostName != null) {
            hubIdentifier = md5Hash(hostName);
        }

        final Map<String, String> infoMap = metaDataMap;
        infoMap.put(PhoneHomeApiConstants.BLACK_DUCK_NAME, blackDuckName.getName());
        infoMap.put(PhoneHomeApiConstants.BLACK_DUCK_VERSION, blackDuckVersion);
        infoMap.put(PhoneHomeApiConstants.THIRD_PARTY_NAME, thirdPartyName.getName());
        infoMap.put(PhoneHomeApiConstants.THIRD_PARTY_VERSION, thirdPartyVersion);
        infoMap.put(PhoneHomeApiConstants.PLUGIN_VERSION, pluginVersion);

        final PhoneHomeRequestBody info = new PhoneHomeRequestBody(hubIdentifier, source, infoMap);
        return info;
    }

    @Override
    public PhoneHomeRequestBodyValidator createValidator() {
        final PhoneHomeRequestBodyValidator phoneHomeRequestValidator = new PhoneHomeRequestBodyValidator();
        phoneHomeRequestValidator.setBlackDuckName(blackDuckName);
        phoneHomeRequestValidator.setBlackDuckVersion(blackDuckVersion);
        phoneHomeRequestValidator.setHostName(hostName);
        phoneHomeRequestValidator.setPluginVersion(pluginVersion);
        phoneHomeRequestValidator.setRegistrationId(registrationId);
        phoneHomeRequestValidator.setThirdPartyName(thirdPartyName);
        phoneHomeRequestValidator.setThirdPartyVersion(thirdPartyVersion);
        return phoneHomeRequestValidator;
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

    public Map<String, String> getMetaDataMap() {
        return metaDataMap;
    }

    public void addToMetaDataMap(final String key, final String value){
        metaDataMap.put(key, value);
    }


    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(final String registrationId) {
        this.registrationId = registrationId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(final String hostName) {
        this.hostName = hostName;
    }

    public BlackDuckName getBlackDuckName() {
        return blackDuckName;
    }

    public void setBlackDuckName(final BlackDuckName blackDuckName) {
        this.blackDuckName = blackDuckName;
    }

    public String getBlackDuckVersion() {
        return blackDuckVersion;
    }

    public void setBlackDuckVersion(final String blackDuckVersion) {
        this.blackDuckVersion = blackDuckVersion;
    }

    public ThirdPartyName getThirdPartyName() {
        return thirdPartyName;
    }

    public void setThirdPartyName(final ThirdPartyName thirdPartyName) {
        this.thirdPartyName = thirdPartyName;
    }

    public String getThirdPartyVersion() {
        return thirdPartyVersion;
    }

    public void setThirdPartyVersion(final String thirdPartyVersion) {
        this.thirdPartyVersion = thirdPartyVersion;
    }

    public String getPluginVersion() {
        return pluginVersion;
    }

    public void setPluginVersion(final String pluginVersion) {
        this.pluginVersion = pluginVersion;
    }

    public PhoneHomeSource getSource() {
        return source;
    }

    public void setSource(final PhoneHomeSource source) {
        this.source = source;
    }

}
