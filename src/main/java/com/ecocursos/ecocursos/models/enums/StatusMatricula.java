package com.ecocursos.ecocursos.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatusMatricula {

    ISENTO(0, "Isento"),
    NAO_ISENTO(1, "Nao isento");

    private Integer codigo;
    private String descricao;

    @JsonValue
    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }
}
