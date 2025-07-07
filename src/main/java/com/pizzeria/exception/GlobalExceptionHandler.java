package com.pizzeria.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import java.util.stream.Collectors;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionHandler.class);

    @Override
    public Response toResponse(Exception exception) {
        
        // Log da exceção
        LOG.errorf(exception, "Exceção não tratada capturada: %s", exception.getMessage());
        
        // Tratamento específico para diferentes tipos de exceção
        if (exception instanceof BusinessException) {
            BusinessException be = (BusinessException) exception;
            return Response.status(be.getStatus())
                         .type(MediaType.APPLICATION_JSON)
                         .entity("{\"erro\":\"" + be.getMessage() + "\"}")
                         .build();
        }
        
        if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException cve = (ConstraintViolationException) exception;
            String mensagens = cve.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
            
            return Response.status(Response.Status.BAD_REQUEST)
                         .type(MediaType.APPLICATION_JSON)
                         .entity("{\"erro\":\"Dados inválidos: " + mensagens + "\"}")
                         .build();
        }
        
        if (exception instanceof jakarta.ws.rs.WebApplicationException) {
            jakarta.ws.rs.WebApplicationException wae = (jakarta.ws.rs.WebApplicationException) exception;
            return Response.status(wae.getResponse().getStatus())
                         .type(MediaType.APPLICATION_JSON)
                         .entity("{\"erro\":\"" + wae.getMessage() + "\"}")
                         .build();
        }
        
        // Exceção genérica - não expor detalhes internos
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                     .type(MediaType.APPLICATION_JSON)
                     .entity("{\"erro\":\"Erro interno do servidor\"}")
                     .build();
    }
} 