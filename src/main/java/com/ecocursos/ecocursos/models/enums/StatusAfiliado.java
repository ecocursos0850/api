package com.ecocursos.ecocursos.models.enums;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusAfiliado {

    AGUARDANDO_LIBERACAO(0, "Aguardando liberação"),
    LIBERADO(1, "Liberado"),
    BLOQUEADO(2, "Bloqueado"),
    NAO_APROVADO(3, "Não Aprovado");

    private Integer codigo;
    private String descricao;

    @JsonValue
    public Integer getCodigo() {
        return this.codigo;
    }

    public static StatusAfiliado toEnum(Integer id) {
        for (StatusAfiliado value : StatusAfiliado.values()) {
            if(id == value.getCodigo()) {
                return value;
            }
        }
        return null;
    }

}
