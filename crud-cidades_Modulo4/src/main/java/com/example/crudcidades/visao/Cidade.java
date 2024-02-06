package com.example.crudcidades.visao;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class Cidade {

    @NotBlank(message = "Nome da cidade deve ser informado")
    @Size(min = 5, max = 60, message = "O nome da cidade deve ter entre 5 e 60 caracteres")
    private final String nome;

    @NotBlank(message = "Sigla do estado deve ser informado")
    @Size(min = 2, max = 2, message = "A sigla do estado está limitada a dois caracteres")
    private final String estado;

    public Cidade(final String nome, final String estado){
        
        this.nome = nome;
        this.estado = estado;
    }

    public String getNome() {
        return this.nome;
    }

    public String getEstado() {
        return this.estado;
    }
}
