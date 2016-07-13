package com.blackducksoftware.integration.phone.home.api;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;

import com.blackducksoftware.integration.phone.home.api.PhoneHomeInfo;

/**
 * 
 * @author nrowles
 *
 * This interface defines the 'Post' method for the phone-home endpoint
 */
public interface PhoneHomeServerApi {
	
	@Post("json")
	public void updatePhoneHomeInfo(JsonRepresentation info);
}