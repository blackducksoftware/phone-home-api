package com.blackducksoftware.integration.phone.home.api;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

public interface PhAnalyticsServerApi {
	@Get("html")
	public Representation doGet();
	
	@Post("uri")
	public Representation doPost(Representation entity);
	
	@Put("html")
	public void doPut(Representation entity);
}
