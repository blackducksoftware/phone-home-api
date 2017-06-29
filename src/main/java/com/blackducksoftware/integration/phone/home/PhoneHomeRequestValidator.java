package com.blackducksoftware.integration.phone.home;

import org.apache.commons.lang3.StringUtils;

import com.blackducksoftware.integration.phone.home.enums.PhoneHomeRequestFieldEnum;
import com.blackducksoftware.integration.validator.AbstractValidator;
import com.blackducksoftware.integration.validator.ValidationResult;
import com.blackducksoftware.integration.validator.ValidationResultEnum;
import com.blackducksoftware.integration.validator.ValidationResults;

public class PhoneHomeRequestValidator extends AbstractValidator{
    private String regId;

    private String hostName;

    private String blackDuckName;

    private String blackDuckVersion;

    private String thirdPartyName;

    private String thirdPartyVersion;

    private String pluginVersion;

    @Override
    public ValidationResults assertValid() {
        final ValidationResults result = new ValidationResults();

        validateHubServerIdentifier(result);
        validateBlackDuckProductInfo(result);
        validateThirdPartyProductInfo(result);
        validateIntegrationInfo(result);

        return result;
    }

    public void validateHubServerIdentifier(final ValidationResults result){
        if (StringUtils.isBlank(regId) && StringUtils.isBlank(hostName)) {
            result.addResult(PhoneHomeRequestFieldEnum.REGID,
                    new ValidationResult(ValidationResultEnum.ERROR, "No Hub server identifier was found."));
        }
    }

    public void validateBlackDuckProductInfo(final ValidationResults result){
        if (StringUtils.isBlank(blackDuckName)) {
            result.addResult(PhoneHomeRequestFieldEnum.BLACKDUCKNAME,
                    new ValidationResult(ValidationResultEnum.ERROR, "No Black Duck product name was found."));
        }else if (StringUtils.isBlank(blackDuckVersion)) {
            result.addResult(PhoneHomeRequestFieldEnum.BLACKDUCKVERSION,
                    new ValidationResult(ValidationResultEnum.ERROR, String.format("No version of %s was found.", blackDuckName)));
        }
    }

    public void validateThirdPartyProductInfo(final ValidationResults result){
        if (StringUtils.isBlank(thirdPartyName)) {
            result.addResult(PhoneHomeRequestFieldEnum.THIRDPARTYNAME,
                    new ValidationResult(ValidationResultEnum.ERROR, "No third party name was found."));
        } else if (StringUtils.isBlank(thirdPartyVersion)) {
            result.addResult(PhoneHomeRequestFieldEnum.THIRDPARTYVERSION,
                    new ValidationResult(ValidationResultEnum.ERROR, String.format("No version of %s was found.", thirdPartyName)));
        }
    }

    public void validateIntegrationInfo(final ValidationResults result){
        if (StringUtils.isBlank(pluginVersion)) {
            result.addResult(PhoneHomeRequestFieldEnum.PLUGINVERSION,
                    new ValidationResult(ValidationResultEnum.ERROR, "No plugin version was found."));
        }
    }
}
