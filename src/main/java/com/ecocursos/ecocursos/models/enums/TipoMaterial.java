package com.ecocursos.ecocursos.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum TipoMaterial {
    VIDEO(0, "VIDEO"),
    PDF(1, "PDF");


    private Integer codigo;
    private String descricao;

    @JsonValue
    public Integer getCodigo() {
        return codigo;
    }
}
