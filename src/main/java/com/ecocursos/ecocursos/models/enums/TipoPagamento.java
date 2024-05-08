package com.ecocursos.ecocursos.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoPagamento {

    GRATUITO(0, "Gratuito"),
    BOLETO(1, "Boleto"),
    CARTAO_CREDITO(2, "Cartão de crédito"),
    PIX(3, "Pix"),
    TODOS(4, "Undefined");

    private Integer codigo;
    private String descricao;

    TipoPagamento(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    @JsonValue
    public Integer getCodigo() {
        return this.codigo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public static TipoPagamento toEnum(Integer codigo) {
        for (TipoPagamento tipoPagamento : TipoPagamento.values()) {
            if (tipoPagamento.getCodigo().equals(codigo)) {
                return tipoPagamento;
            }
        }
        return null;
    }

}
