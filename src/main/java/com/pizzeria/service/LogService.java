package com.pizzeria.service;

import com.pizzeria.model.entity.Log;
import com.pizzeria.model.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class LogService {
    @Transactional
    public void registrarLog(User usuario, String acao, String descricao) {
        Log log = new Log();
        log.setUsuario(usuario);
        log.setAcao(acao);
        log.setDescricao(descricao);
        log.persist();
    }
}
