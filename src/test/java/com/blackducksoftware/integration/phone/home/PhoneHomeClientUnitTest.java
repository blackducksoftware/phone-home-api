package com.blackducksoftware.integration.phone.home;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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

import com.blackducksoftware.integration.phone.home.exception.PhoneHomeException;
import com.blackducksoftware.integration.phone.home.exception.PropertiesLoaderException;

public class PhoneHomeClientUnitTest {
	private ClientAndServer server;
	private int port;

	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Before
	public void startProxy() throws PropertiesLoaderException, IOException, NumberFormatException {
		// Multiple potential test ports from mockServer.properties
		final Properties properties = new Properties();
		final String propFileName = PhoneHomeApiConstants.TEST_SERVER_FILE_NAME;
		final InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

		if (inputStream != null) {
			properties.load(inputStream);
			inputStream.close();
		} else {
			throw new PropertiesLoaderException("Unable to get find resource: " + propFileName);
		}

		String portsString = properties.getProperty(PhoneHomeApiConstants.TEST_SERVER_PROPERTY_PORTS);
		port = Integer.parseInt(portsString);

		try {
			server = ClientAndServer.startClientAndServer(port);
		} catch (Exception e) {
			e.printStackTrace();
		}

		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("Content-Type", "application/json; charset=UTF-8"));
		server.when(HttpRequest.request().withPath("/test")).respond(HttpResponse.response().withHeaders(headers));
	}

	@After
	public void stopProxy() {
		server.stop();
	}

	@Test
	public void callHomeNull() throws Exception {
		exception.expect(PhoneHomeException.class);
		final PhoneHomeClient phClient = new PhoneHomeClient();

		phClient.callHome(null, null);
	}

	@Test
	public void callHomeInvalidUrl() throws Exception {
		exception.expect(ResourceException.class);
		final PhoneHomeClient phClient = new PhoneHomeClient();

		String regId = "regId";
		Map<String, String> infoMap = new HashMap<String, String>();
		PhoneHomeInfo info = new PhoneHomeInfo(regId, infoMap);
		String targetUrl = "http://foo-bar/";

		phClient.callHome(info, targetUrl);
	}

	@Test
	public void callHomeValidUrl() throws Exception {
		final PhoneHomeClient phClient = new PhoneHomeClient();
		String regId = "regId";
		Map<String, String> infoMap = new HashMap<String, String>();
		PhoneHomeInfo info = new PhoneHomeInfo(regId, infoMap);
		String targetUrl = "http://localhost:" + this.port + "/test";

		phClient.callHome(info, targetUrl);
	}

	@Test
	public void callHomeIntegrationsTest() throws Exception {
		final PhoneHomeClient phClient = new PhoneHomeClient();

		String propertiesPath = PhoneHomeApiConstants.TEST_CONFIG_FILE_NAME;

		phClient.callHomeIntegrations("regKey", "blackDuckName", "blackDuckVersion", "thirdPartyName",
				"thirdPartyVersion", "pluginVersion", propertiesPath);
	}
}
