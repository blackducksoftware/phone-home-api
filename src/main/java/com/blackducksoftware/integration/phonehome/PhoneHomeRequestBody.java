/**
 * phone-home-api
 *
 * Copyright (C) 2018 Black Duck Software, Inc.
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

import java.util.Map;
import java.util.Objects;

public class PhoneHomeRequestBody {
    public static final PhoneHomeRequestBody DO_NOT_PHONE_HOME = null;
    private final String regId;
    private final String hostName;
    private final String source;
    private final Map<String, String> infoMap;

    public PhoneHomeRequestBody(final String hubIdentifier, final String hostName, final String source, final Map<String, String> infoMap) {
        this.regId = Objects.requireNonNull(hubIdentifier);
        this.hostName = Objects.requireNonNull(hostName);
        this.source = Objects.requireNonNull(source);
        this.infoMap = Objects.requireNonNull(infoMap);
    }

    public String getRegId() {
        return regId;
    }

    public String getHostName() {
        return hostName;
    }

    public String getSource() {
        return source;
    }

    public Map<String, String> getInfoMap() {
        return infoMap;
    }

}
