package com.pizzeria.model.dto;

import com.pizzeria.Enum.Tipo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProdutoDTO {
    private Tipo tipo;
    private Long id;
    
    @NotNull(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "Nome deve ter entre 2 e 100 caracteres")
    private String nome;
    
    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    private String descricao;
    
    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "Preço deve ser maior que zero")
    private BigDecimal preco;
    
    @NotNull(message = "Tipo é obrigatório")

    
    private boolean disponivel = true;
    
    @Size(max = 500, message = "URL da imagem deve ter no máximo 500 caracteres")
    private String imagemUrl;

    private String imagemBase64;

} 