package com.ecocursos.ecocursos.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TipoEntradaCaixa {
    SINDICATO(0, "Sindicato"),
    SITE(1, "Venda pelo site"),
    VENDEDOR(2, "Vendedor");

    private Integer codigo;
    private String descricao;

    @JsonValue
    public Integer getCodigo() {
        return codigo;
    }

    public static TipoEntradaCaixa toEnum(Integer codigo) {
        for (TipoEntradaCaixa tipoEntradaCaixa : TipoEntradaCaixa.values()) {
            if (tipoEntradaCaixa.getCodigo().equals(codigo)) {
                return tipoEntradaCaixa;
            }
        }
        return null;
    }

}
