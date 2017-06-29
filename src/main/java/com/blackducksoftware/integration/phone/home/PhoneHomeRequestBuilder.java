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
    public String regId;

    public String hostName;

    public BlackDuckName blackDuckName;

    public String blackDuckVersion;

    public ThirdPartyName thirdPartyName;

    public String thirdPartyVersion;

    public String pluginVersion;

    public PhoneHomeSource source = PhoneHomeSource.INTEGRATIONS;

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

    @Override
    public PhoneHomeRequest buildObject() {
        String hubIdentifier = null;
        if (regId != null) {
            hubIdentifier = regId;
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
}
