package com.ecocursos.ecocursos.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TipoUsuario {
    ADMINISTRADOR(0, "Administrador"),
    ALUNO(1, "Aluno"),
    AFILIADO(2, "Afiliado");

    private Integer codigo;
    private String descricao;

    @JsonValue
    public Integer getCodigo() {
        return this.codigo;
    }

}
