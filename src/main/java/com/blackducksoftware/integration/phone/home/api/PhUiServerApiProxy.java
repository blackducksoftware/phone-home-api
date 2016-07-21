package com.blackducksoftware.integration.phone.home.api;

import org.json.JSONObject;
import org.restlet.resource.ClientProxy;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Result;

public interface PhUiServerApiProxy extends ClientProxy {
	@Get
	public JSONObject doGet(Result callback);
	
	@Post
	public JSONObject doPost(JSONObject jo, Result callback);
}
