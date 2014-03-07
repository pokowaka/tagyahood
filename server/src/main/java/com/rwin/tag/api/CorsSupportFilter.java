package com.rwin.tag.api;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CorsSupportFilter implements ContainerResponseFilter {

    private static final String ORIGINHEADER = "Origin";
    private static final String ACAOHEADER = "Access-Control-Allow-Origin";
    private static final String ACRHHEADER = "Access-Control-Request-Headers";
    private static final String ACAHHEADER = "Access-Control-Allow-Headers";

    public static Logger log = LoggerFactory.getLogger(CorsSupportFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) throws IOException {

        final String requestOrigin = requestContext
                .getHeaderString(ORIGINHEADER);
        if (requestOrigin != null)
            responseContext.getHeaders().add(ACAOHEADER, requestOrigin);

        final String requestHeaders = requestContext
                .getHeaderString(ACRHHEADER);
        if (requestHeaders != null)
            responseContext.getHeaders().add(ACAHHEADER, requestHeaders);
    }

}