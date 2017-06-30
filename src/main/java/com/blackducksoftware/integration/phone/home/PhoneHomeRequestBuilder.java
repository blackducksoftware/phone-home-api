package com.blackducksoftware.integration.phone.home;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

import com.blackducksoftware.integration.builder.AbstractBuilder;
import com.blackducksoftware.integration.phone.home.enums.BlackDuckName;
import com.blackducksoftware.integration.phone.home.enums.PhoneHomeSource;
import com.blackducksoftware.integration.phone.home.enums.ThirdPartyName;

public class PhoneHomeRequestBuilder extends AbstractBuilder<PhoneHomeRequest>{
    private String registrationId;

    private String hostName;

    private BlackDuckName blackDuckName;

    private String blackDuckVersion;

    private ThirdPartyName thirdPartyName;

    private String thirdPartyVersion;

    private String pluginVersion;

    private PhoneHomeSource source;

    @Override
    public PhoneHomeRequest buildObject() {
        String hubIdentifier = null;
        if (registrationId != null) {
            hubIdentifier = registrationId;
        } else if (hostName != null) {
            hubIdentifier = md5Hash(hostName);
        }

        final Map<String, String> infoMap = new HashMap<>();
        infoMap.put(PhoneHomeApiConstants.BLACK_DUCK_NAME, blackDuckName.getName());
        infoMap.put(PhoneHomeApiConstants.BLACK_DUCK_VERSION, blackDuckVersion);
        infoMap.put(PhoneHomeApiConstants.THIRD_PARTY_NAME, thirdPartyName.getName());
        infoMap.put(PhoneHomeApiConstants.THIRD_PARTY_VERSION, thirdPartyVersion);
        infoMap.put(PhoneHomeApiConstants.PLUGIN_VERSION, pluginVersion);

        final PhoneHomeRequest info = new PhoneHomeRequest(hubIdentifier, source, infoMap);
        return info;
    }

    @Override
    public PhoneHomeRequestValidator createValidator() {
        final PhoneHomeRequestValidator phoneHomeRequestValidator = new PhoneHomeRequestValidator();
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
