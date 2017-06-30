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
package com.blackducksoftware.integration.phone.home;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import com.blackducksoftware.integration.phone.home.enums.PhoneHomeSource;

public class PhoneHomeRequest implements Serializable {
    private static final long serialVersionUID = 5604676370200060866L;

    private final String regId;

    private final String source;

    private final Map<String, String> infoMap;

    public PhoneHomeRequest(final String hubIdentifier, final PhoneHomeSource source,
            final Map<String, String> infoMap) {
        final String sourceString = source.name();
        this.regId = Objects.requireNonNull(hubIdentifier);
        this.source = Objects.requireNonNull(sourceString);
        this.infoMap = Objects.requireNonNull(infoMap);
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

}