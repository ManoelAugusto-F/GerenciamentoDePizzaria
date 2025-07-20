//package com.pizzeria.Provider;
//
//import jakarta.annotation.Priority;
//import jakarta.inject.Inject;
//import jakarta.ws.rs.container.ContainerRequestContext;
//import jakarta.ws.rs.container.ContainerRequestFilter;
//import jakarta.ws.rs.core.Cookie;
//import jakarta.ws.rs.ext.Provider;
//import org.eclipse.microprofile.jwt.JsonWebToken;
//import org.jboss.logging.Logger;
//
//import java.io.IOException;
//
//@Provider // 1. Anotação para que o JAX-RS descubra e use este filtro
//@Priority(100) // 2. Define uma alta prioridade para executar antes dos filtros de segurança
//public class TokenCookieFilter implements ContainerRequestFilter {
//    @Inject
//    JsonWebToken jwt;
//    private static final Logger LOG = Logger.getLogger(TokenCookieFilter.class);
//
//    private static final String COOKIE_NAME = "token";
//    private static final String HEADER_NAME = "Authorization";
//    private static final String HEADER_SCHEME = "Bearer ";
//
//    @Override
//    public void filter(ContainerRequestContext requestContext) throws IOException {
//        LOG.info(">>> TokenCookieFilter EXECUTADO. Verificando cookie 'token'...");
//        // 3. Verifica se o cabeçalho Authorization JÁ NÃO EXISTE
//        //    Isso dá prioridade a um token enviado manualmente (ex: por Postman)
//        if (requestContext.getHeaderString(HEADER_NAME) != null) {
//            return; // Já existe um cabeçalho, não faz nada.
//        }
//
//        // 4. Pega o cookie da requisição
//        Cookie tokenCookie = requestContext.getCookies().get(COOKIE_NAME);
//
//        if (tokenCookie != null) {
//            // 5. Se o cookie existir, extrai o valor do token
//            String token = tokenCookie.getValue();
//
//            // 6. Adiciona o cabeçalho "Authorization" à requisição
//            //    A requisição é modificada em tempo real
//            requestContext.getHeaders().add(HEADER_NAME, HEADER_SCHEME + token);
//        }
//    }
//}
