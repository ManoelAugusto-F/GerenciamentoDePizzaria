package com.pizzeria.rest;

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
    public Response listarComFiltros(@QueryParam("usuarioId") Long usuarioId,
                                   @QueryParam("acao") String acao,
                                   @QueryParam("dataInicio") String dataInicio,
                                   @QueryParam("dataFim") String dataFim) {
        try {
            LocalDateTime inicio = null;
            LocalDateTime fim = null;
            
            if (dataInicio != null && !dataInicio.isEmpty()) {
                try {
                    inicio = LocalDateTime.parse(dataInicio);
                } catch (DateTimeParseException e) {
                    throw new WebApplicationException("Formato de data de início inválido. Use: yyyy-MM-ddTHH:mm:ss", Response.Status.BAD_REQUEST);
                }
            }
            
            if (dataFim != null && !dataFim.isEmpty()) {
                try {
                    fim = LocalDateTime.parse(dataFim);
                } catch (DateTimeParseException e) {
                    throw new WebApplicationException("Formato de data de fim inválido. Use: yyyy-MM-ddTHH:mm:ss", Response.Status.BAD_REQUEST);
                }
            }
            
            List<Log> logs = logDAO.listarComFiltros(usuarioId, acao, inicio, fim);
            return Response.ok(logs).build();
            
        } catch (WebApplicationException e) {
            LOG.warnf("Erro ao listar logs: %s", e.getMessage());
            return Response.status(e.getResponse().getStatus())
                         .entity("{\"erro\":\"" + e.getMessage() + "\"}")
                         .build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro inesperado ao listar logs");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                         .entity("{\"erro\":\"Erro interno do servidor\"}")
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
    
    @GET
    @Path("/usuario/{usuarioId}")
    public Response listarPorUsuario(@PathParam("usuarioId") Long usuarioId) {
        try {
            List<Log> logs = logDAO.listarPorUsuario(usuarioId);
            return Response.ok(logs).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro inesperado ao listar logs do usuário ID: %d", usuarioId);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                         .entity("{\"erro\":\"Erro interno do servidor\"}")
                         .build();
        }
    }
    
    @GET
    @Path("/acao/{acao}")
    public Response listarPorAcao(@PathParam("acao") String acao) {
        try {
            List<Log> logs = logDAO.listarPorAcao(acao);
            return Response.ok(logs).build();
        } catch (Exception e) {
            LOG.errorf(e, "Erro inesperado ao listar logs por ação: %s", acao);
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