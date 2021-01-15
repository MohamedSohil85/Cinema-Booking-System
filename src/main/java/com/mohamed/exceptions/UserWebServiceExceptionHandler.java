package com.mohamed.exceptions;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class UserWebServiceExceptionHandler implements ExceptionMapper<ResourceNotFound> {
    @Override
    public Response toResponse(ResourceNotFound exception) {
        return Response
                .status(Response.Status.NO_CONTENT)
                .entity(new ErrorMessage(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON).build();
    }
}
