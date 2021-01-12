package com.mohamed.exceptions;

import javax.ws.rs.ext.Provider;

@Provider
public class ResourceNotFound extends Exception{
    public ResourceNotFound() {
        super();
    }

    public ResourceNotFound(String message) {
        super(message);
    }
}
