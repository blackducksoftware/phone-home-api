package com.blackducksoftware.integration.phone.home.enums;

public enum ThirdPartyName {
    JENKINS("Jenkins"),
    BAMBOO("Bamboo"),
    TEAM_CITY("Team-City"),
    JIRA("Jira"),
    MAVEN("Maven"),
    GRADLE("Gradle"),
    ARTIFACTORY("Artifactory"),
    FORTIFY("Fortify"),
    SONARQUBE("Sonarqube"),
    ECLIPSE("Eclipse");

    private final String name;

    private ThirdPartyName(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
