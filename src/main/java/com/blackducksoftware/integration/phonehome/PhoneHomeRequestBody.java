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

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import com.blackducksoftware.integration.phonehome.enums.PhoneHomeSource;

public class PhoneHomeRequestBody implements Serializable {
    private static final long serialVersionUID = 5604676370200060866L;
    public static final PhoneHomeRequestBody DO_NOT_PHONE_HOME = null;
    private final String regId;
    private final String source;
    private final Map<String, String> infoMap;
    private final boolean bypassIpCaching;

    public PhoneHomeRequestBody(final String hubIdentifier, final PhoneHomeSource source, final Map<String, String> infoMap, final boolean bypassIpCaching) {
        final String sourceString = source.getName();
        this.regId = Objects.requireNonNull(hubIdentifier);
        this.source = Objects.requireNonNull(sourceString);
        this.infoMap = Objects.requireNonNull(infoMap);
        this.bypassIpCaching = bypassIpCaching;
    }

    public String getRegId() {
        return regId;
    }

    public String getSource() {
        return source;
    }

    public Map<String, String> getInfoMap() {
        return infoMap;
    }

    public boolean getBypassIpCaching(){
        return bypassIpCaching;
    }

}
