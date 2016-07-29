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

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.blackducksoftware.integration.phone.home.exception.PropertiesLoaderException;

/**
 * @author nrowles
 *
 */
public class PropertiesLoaderUnitTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void createTargetUrlInvalidPropFile() throws Exception {
		exception.expect(PropertiesLoaderException.class);
		final PropertiesLoader propLoader = new PropertiesLoader();
		String invalidPath = "not a valid path";

		propLoader.createTargetUrl(invalidPath);
	}

	@Test
	public void createTargetUrlValidPropFile() throws Exception {
		final PropertiesLoader propLoader = new PropertiesLoader();
		String validPath = PhoneHomeApiConstants.TEST_CONFIG_FILE_NAME;

		String url = propLoader.createTargetUrl(validPath);

		String expectedUrl = "http://localhost:8121/test";
		assertEquals(expectedUrl, url);
	}
}
