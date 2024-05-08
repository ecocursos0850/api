package com.ecocursos.ecocursos.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum StatusPedido {

    PENDENTE(0, "Pendente"),
    PAGO(1, "Pago"),
    CANCELADO(2, "Cancelado"),
    EM_ANALISE(3, "Em analise"),
    RASCUNHO(4, "Rascunho"),
    PARCIALMENTE_PAGO(5, "Parcialmente pago"),
    REEMBOLSADO(6, "Reembolsado"),
    EXPIRADO(7, "Expirado"),
    EM_PROTESTO(8, "Em protesto"),
    CONTESTADO(9, "Contestado"),
    PAGO_EXTERNAMENTO(10, "Pago externamente");

    private Integer codigo;
    private String descricao;

    StatusPedido(Integer codigo, String descricao) {
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

    public static StatusPedido toEnum(Integer codigo) {
        for (StatusPedido status : StatusPedido.values()) {
            if (codigo.equals(status.getCodigo())) {
                return status;
            }
        }
        return null;
    }

}
