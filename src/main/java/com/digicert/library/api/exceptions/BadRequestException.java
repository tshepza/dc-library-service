package com.digicert.library.api.exceptions;


public class BadRequestException extends BaseExceptions {
    private static final long serialVersionUID = 1L;

    public BadRequestException(String msg,String metadata) {
        super(msg,metadata);
    }

}
