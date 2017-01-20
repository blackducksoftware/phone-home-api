/**
 * Integration Common
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
package com.blackducksoftware.integration.phone.home.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ProxyUtil {

    /**
     * Checks the list of user defined host names that should be connected to
     * directly and not go through the proxy. If the hostToMatch matches any of
     * these hose names then this method returns true.
     *
     */
    public static boolean shouldIgnoreHost(final String hostToMatch, final List<Pattern> ignoredProxyHostPatterns) {
        if (StringUtils.isBlank(hostToMatch) || ignoredProxyHostPatterns == null
                || ignoredProxyHostPatterns.isEmpty()) {
            return false;
        }

        for (final Pattern ignoredProxyHostPattern : ignoredProxyHostPatterns) {
            final Matcher m = ignoredProxyHostPattern.matcher(hostToMatch);
            if (m.matches()) {
                return true;
            }
        }
        return false;
    }

    public static List<Pattern> getIgnoredProxyHostPatterns(final String ignoredProxyHosts) {
        final List<Pattern> ignoredProxyHostPatterns = new ArrayList<>();
        if (StringUtils.isNotBlank(ignoredProxyHosts)) {
            String[] ignoreHosts = null;
            if (ignoredProxyHosts.contains(",")) {
                ignoreHosts = ignoredProxyHosts.split(",");
                for (final String ignoreHost : ignoreHosts) {
                    final Pattern pattern = Pattern.compile(ignoreHost.trim());
                    ignoredProxyHostPatterns.add(pattern);
                }
            } else {
                final Pattern pattern = Pattern.compile(ignoredProxyHosts);
                ignoredProxyHostPatterns.add(pattern);
            }
        }
        return ignoredProxyHostPatterns;
    }

}
