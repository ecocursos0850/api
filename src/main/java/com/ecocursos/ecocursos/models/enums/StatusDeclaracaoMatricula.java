package com.ecocursos.ecocursos.models.enums;

import com.ecocursos.ecocursos.models.DeclaracaoMatricula;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusDeclaracaoMatricula {
    
    APROVADA(0, "Aprovada"),
    AGUARDANDO(1, "Aguardando"),
    REPROVADO(2, "Reprovado");

    private Integer codigo;
    private String descricao;

    @JsonValue
    public Integer getCodigo() {
        return this.codigo;
    }

    public static StatusDeclaracaoMatricula toEnum(Integer codigo) {
        for(StatusDeclaracaoMatricula d : StatusDeclaracaoMatricula.values()) {
            if (d.equals(codigo)) {
                return d;
            }
        }
        return null;
    }

}
