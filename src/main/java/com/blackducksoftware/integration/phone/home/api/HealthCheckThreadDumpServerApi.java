package com.blackducksoftware.integration.phone.home.api;

import org.restlet.resource.Get;

/**
 * @author jrichard
 *
 */
public interface HealthCheckThreadDumpServerApi {

    @Get("txt")
    public String getBomServiceHealthThreadDump();
}