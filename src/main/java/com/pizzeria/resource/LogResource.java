package com.pizzeria.resource;

import com.pizzeria.dao.LogDAO;
import com.pizzeria.model.entity.Log;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.WebApplicationException;
import org.jboss.logging.Logger;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Path("/logs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class LogResource {

    private static final Logger LOG = Logger.getLogger(LogResource.class);

    @Inject
    LogDAO logDAO;

    @GET
    public Response findAll() {
        try {
            List<Log> logs = logDAO.listarTodos();
            return Response.ok(logs).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Erro ao buscar os logs: " + e.getMessage())
                    .build();
        }
    }



    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            Optional<Log> logOpt = logDAO.buscarPorId(id);
            if (logOpt.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                             .entity("{\"erro\":\"Log não encontrado\"}")
                             .build();
            }
            return Response.ok(logOpt.get()).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro inesperado ao buscar log ID: %d", id);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                         .entity("{\"erro\":\"Erro interno do servidor\"}")
                         .build();
        }
    }


    @POST
    @Path("/frontend")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logFrontendError(String logMessage) {
        LOG.error("[FRONTEND] " + logMessage);
        return Response.ok().build();
    }
} 