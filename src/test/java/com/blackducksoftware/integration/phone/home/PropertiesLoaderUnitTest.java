package com.blackducksoftware.integration.phone.home;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.blackducksoftware.integration.phone.home.exception.PropertiesLoaderException;

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
