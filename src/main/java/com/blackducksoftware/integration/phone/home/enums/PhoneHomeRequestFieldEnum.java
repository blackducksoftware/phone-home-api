package com.blackducksoftware.integration.phone.home.enums;

import com.blackducksoftware.integration.validator.FieldEnum;

public enum PhoneHomeRequestFieldEnum implements FieldEnum{
    REGID("regId"),
    BLACKDUCKNAME("blackDuckName"),
    BLACKDUCKVERSION("blackDuckVersion"),
    THIRDPARTYNAME("thirdPartyName"),
    THIRDPARTYVERSION("thirdPartyVersion"),
    PLUGINVERSION("pluginVersion");

    private String key;

    private PhoneHomeRequestFieldEnum(final String key) {
        this.key = key;
    }

    @Override
    public String getKey() {
        return key;
    }
}
