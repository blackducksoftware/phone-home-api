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
package com.blackducksoftware.integration.phonehome.enums;

public enum ThirdPartyName {
    BLANK(""),
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
    FIREFOX("Firefox"),
    NEXUS("Nexus"),
    FORTIFY_ON_DEMAND("Fortify On-Demand"),
    FORTIFY_SSC("Fortify SSC"),
    PIVOTAL_SCANNER("Pivotal Scan Service Broker"),
    GOOGLE_CONTAINER_BUILDER("Google Container Builder"),
    GOOGLE_REGISTRY_SCANNER("Google Cloud Registry Scanning Integration");

    private final String name;

    private ThirdPartyName(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
