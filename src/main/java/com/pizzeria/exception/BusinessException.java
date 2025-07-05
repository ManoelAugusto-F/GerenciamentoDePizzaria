package com.pizzeria.exception;

import jakarta.ws.rs.core.Response;

public class BusinessException extends RuntimeException {
    
    private final Response.Status status;
    
    public BusinessException(String message) {
        super(message);
        this.status = Response.Status.BAD_REQUEST;
    }
    
    public BusinessException(String message, Response.Status status) {
        super(message);
        this.status = status;
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.status = Response.Status.BAD_REQUEST;
    }
    
    public BusinessException(String message, Throwable cause, Response.Status status) {
        super(message, cause);
        this.status = status;
    }
    
    public Response.Status getStatus() {
        return status;
    }
} 