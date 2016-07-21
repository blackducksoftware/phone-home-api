package com.blackducksoftware.integration.phone.home.api;

import org.json.JSONObject;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

public interface PhUiServerApi {
	@Get
	public JSONObject doGet();
	
	@Post
	public JSONObject doPost(JSONObject jo);
	
	
}
