package com.blackducksoftware.integration.phone.home.api;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;

public interface PhClientServerApi {
	@Get("html")
	public Representation doGet();
}
