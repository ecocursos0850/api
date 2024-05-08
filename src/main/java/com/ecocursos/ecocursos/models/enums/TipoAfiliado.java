package com.ecocursos.ecocursos.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

@Getter
public enum TipoAfiliado {
    FISICA(0, "Fisica"),
    JURIDICA(1, "Juridica");

    public Integer codigo;
    public String descricao;

    TipoAfiliado(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    @JsonValue
    public Integer getCodigo() {
        return this.codigo;
    }

}
