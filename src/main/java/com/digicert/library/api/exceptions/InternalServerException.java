package com.digicert.library.api.exceptions;


public class InternalServerException extends BaseExceptions  {
    private static final long serialVersionUID = 1L;

    public InternalServerException(String msg,String metadata) {
        super(msg,metadata);
    }

}
