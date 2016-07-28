package com.blackducksoftware.integration.phone.home;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PropertiesLoaderUnitTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void createTargetUrlInvalidPropFile() throws Exception{
		//exception.expect(NullPointerException.class);
		final PropertiesLoader propLoader = new PropertiesLoader();
		String invalidPath = "not a valid path";
		
		propLoader.createTargetUrl(invalidPath);
	}
	
	@Test
	public void createTargetUrlValidPropFile() throws Exception{
		final PropertiesLoader propLoader = new PropertiesLoader();
		String validPath = "testConfig.properties";
		
		String url = propLoader.createTargetUrl(validPath);
		
		String expectedUrl = "http://localhost:8080/test";
		assertEquals(expectedUrl, url);
	}
}
