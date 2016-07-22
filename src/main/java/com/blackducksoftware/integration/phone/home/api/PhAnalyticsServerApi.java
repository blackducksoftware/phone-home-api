package com.blackducksoftware.integration.phone.home.api;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

public interface PhAnalyticsServerApi {
	@Get("html")
	public Representation doGet();
	
	@Post("json")
	public Representation doPost(JsonRepresentation entity);
	
}
