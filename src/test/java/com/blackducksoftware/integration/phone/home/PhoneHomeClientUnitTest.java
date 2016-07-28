package com.blackducksoftware.integration.phone.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.restlet.resource.ResourceException;

public class PhoneHomeClientUnitTest {
	private ClientAndServer server;
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	
	@Before
	public void startProxy(){
		int port = 8080;
		try{
			server = ClientAndServer.startClientAndServer(port);
		} catch(Exception e){
			//TODO update
			e.printStackTrace();
		}
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("Content-Type","application/json; charset=UTF-8"));
		server.when(HttpRequest.request().withPath("/test")).respond(HttpResponse.response().withHeaders(headers));
	}
	
	@After
	public void stopProxy(){
		server.stop();
	}
	
	@Test
	public void callHomeNull() throws Exception{
		exception.expect(NullPointerException.class);
		final PhoneHomeClient phClient = new PhoneHomeClient();
		
		phClient.callHome(null, null);
	}
	
	@Test
	public void callHomeInvalidUrl() throws Exception{
		exception.expect(ResourceException.class);
		final PhoneHomeClient phClient = new PhoneHomeClient();
		
		String regId = "regId";
		Map<String, String> infoMap = new HashMap<String, String>();
		PhoneHomeInfo info = new PhoneHomeInfo(regId, infoMap);
		String targetUrl = "http://foo-bar/";
		
		phClient.callHome(info, targetUrl);
	}
	
	@Test
	public void callHomeValidUrl() throws Exception{
		final PhoneHomeClient phClient = new PhoneHomeClient();
		String regId = "regId";
		Map<String, String> infoMap = new HashMap<String, String>();
		PhoneHomeInfo info = new PhoneHomeInfo(regId, infoMap);
		String targetUrl = "http://localhost:8080/test";
		
		phClient.callHome(info, targetUrl);
	}
	
	@Test
	public void callHomeIntegrationsTest() throws Exception{
		final PhoneHomeClient phClient = new PhoneHomeClient();
		
		String propertiesPath = "testConfig.properties";
		
		phClient.callHomeIntegrations("regKey", "blackDuckName", "blackDuckVersion", "thirdPartyName", "thirdPartyVersion", "pluginVersion", propertiesPath);
	}
}
