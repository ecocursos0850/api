package com.ecocursos.ecocursos.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Status {
    INATIVO(0, "INATIVO"),
    ATIVO(1 , "ATIVO");

    private Integer codigo;
    private String descricao;

    Status(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    @JsonValue
    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Status toEnum(Integer codigo) {
        for(Status status: Status.values()) {
            if(codigo == status.getCodigo()) {
                return status;
            }
        }
        return null;
    }

}
