package com.blackducksoftware.integration.phone.home.enums;

public enum ThirdPartyName {
    JENKINS("Jenkins"),
    BAMBOO("Bamboo"),
    TEAM_CITY("Team-City"),
    JIRA("Jira"),
    MAVEN("Maven"),
    GRADLE("Gradle"),
    SBT("Scala Build Tool"),
    ARTIFACTORY("Artifactory"),
    FORTIFY("Fortify"),
    SONARQUBE("Sonarqube"),
    ECLIPSE("Eclipse"),
    VISUAL_STUDIO("Visual Studio"),
    EMAIL_EXTENSION("Email-Extension"),
    CHROME("Chrome"),
    FIREFOX("Firefox");

    private final String name;

    private ThirdPartyName(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
