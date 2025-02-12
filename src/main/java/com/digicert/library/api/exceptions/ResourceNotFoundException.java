package com.digicert.library.api.exceptions;

public class ResourceNotFoundException extends BaseExceptions {
    private static final long serialVersionUID = 1L;


    public ResourceNotFoundException(String msg,String metadata) {
        super(msg,metadata);
    }

}
