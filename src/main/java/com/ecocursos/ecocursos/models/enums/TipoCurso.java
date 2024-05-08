package com.ecocursos.ecocursos.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoCurso {
    CURSO_ONLINE(0, "CURSO ONLINE"),
    TAXA_MATRICULA(1, "TAXA MATRICULA"),
    LINK_EXTERNO(2, "LINK EXTERNO"),
    MESTRADO_DOUTORADO(3, "DOUTORADO/MESTRADO");

    private Integer codigo;
    private String descricao;

    TipoCurso(Integer codigo, String descricao) {
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

    public static TipoCurso toEnum(Integer codigo) {
        if (codigo != null) {
            for (TipoCurso tipoCurso: TipoCurso.values()) {
                if (codigo.equals(tipoCurso.getCodigo())) {
                    return tipoCurso;
                }
            }
        }
        return null;
    }
}
