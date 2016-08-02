/*******************************************************************************
 * Copyright (C) 2016 Black Duck Software, Inc.
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
 *******************************************************************************/

package com.blackducksoftware.integration.phone.home;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blackducksoftware.integration.phone.home.exception.PropertiesLoaderException;

/**
 * @author nrowles
 *
 *         Utility class to load properties from an external file.
 */
public class PropertiesLoader {

	private final Logger logger = LoggerFactory.getLogger(PropertiesLoader.class);

	/**
	 * 
	 * @param propertiesFileName
	 *            Path to a properties file
	 * @return URL built from properties file
	 * @throws IOException
	 * 
	 *             This method builds a URL from the given properties file name,
	 *             and returns it as a String.
	 * @throws PropertiesLoaderException
	 */
	public String createTargetUrl(String propertiesFileName) throws IOException, PropertiesLoaderException {
		final Properties properties = new Properties();
		final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);

		if (inputStream != null) {
			properties.load(inputStream);
			inputStream.close();
		} else {
			throw new PropertiesLoaderException("Unable to get find resource: " + propertiesFileName);
		}

		final StringBuilder target = new StringBuilder();
		
		target.append(properties.getProperty(PhoneHomeApiConstants.PROPERTY_TARGETURL));
		
		final String targetPort = properties.getProperty(PhoneHomeApiConstants.PROPERTY_TARGETPORT);
		if(targetPort != null && targetPort.trim().length() > 0){
			target.append(":" + targetPort.trim());
		}
		
		target.append("/");
		final String targetExt = properties.getProperty(PhoneHomeApiConstants.PROPERTY_TARGETEXT);
		if(targetExt != null && targetExt.trim().length() > 0){
			target.append(targetExt.trim());
		}

		logger.debug("Target URL: " + target.toString());

		return target.toString();
	}
}
