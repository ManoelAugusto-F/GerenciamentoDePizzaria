package com.pizzeria.dto;

import com.pizzeria.model.entity.Usuario.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {
    
    private Long id;
    
    @NotNull(message = "Nome completo é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nomeCompleto;
    
    @NotNull(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    private String email;
    
    @NotNull(message = "Perfil é obrigatório")
    private Perfil perfil;
    
    private boolean ativo = true;
    
    // Construtor para conversão de entidade para DTO
    public UsuarioDTO(Long id, String nomeCompleto, String email, Perfil perfil, boolean ativo) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.email = email;
        this.perfil = perfil;
        this.ativo = ativo;
    }
    
    // Construtor padrão
    public UsuarioDTO() {}
} 