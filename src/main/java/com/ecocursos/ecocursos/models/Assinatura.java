package com.ecocursos.ecocursos.models;

import java.time.LocalDateTime;
import java.util.List;

import com.ecocursos.ecocursos.models.enums.TipoPagamento;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Assinatura {
    private String identificador;
    private String idCliente;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataExpiracao;
    private List<TipoPagamento> tipoPagamentos;

}
