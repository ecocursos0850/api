package com.ecocursos.ecocursos.models.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatusAvaliacaoMatricula {

    AGUARDANDO_PAGAMENTO(0, "Aguardando Pagamento"),
    APROVADO(1, "Aprovado"),
    CANCELADO(2, "Cancelado"),
    LIBERADO_PAGO(3, "Liberado(Pago)"),
    LIBERADO_PARCEIRO(4, "Liberador(Parceiro)"),
    REPROVADO(5, "Reprovado");

    private Integer codigo;
    private String descricao;

    @JsonValue
    public Integer getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusAvaliacaoMatricula toEnum(Integer id) {
        for (StatusAvaliacaoMatricula value : StatusAvaliacaoMatricula.values()) {
            if(id == value.getCodigo()) {
                return value;
            }
        }
        return null;
    }
}
