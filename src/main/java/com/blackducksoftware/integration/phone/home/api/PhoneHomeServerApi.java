package com.blackducksoftware.integration.phone.home.api;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

import com.blackducksoftware.integration.phone.home.api.PhoneHomeInfo;

public interface PhoneHomeServerApi {
	
	@Post("json")
	public void updatePhoneHomeInfo(PhoneHomeInfo info);
}