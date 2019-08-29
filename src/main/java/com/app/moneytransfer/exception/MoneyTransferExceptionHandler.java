package com.app.moneytransfer.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Application ExceptionHandler , which converts UserValidationException it proper Response
 */
@Provider
public class MoneyTransferExceptionHandler implements ExceptionMapper<UserValidationException> {
    
    @Override
    public Response toResponse(UserValidationException e) {        
        return Response.status(Response.Status.fromStatusCode(e.getErrorNo())).entity(e.getMessage()).build();
    }
}
