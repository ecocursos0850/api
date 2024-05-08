package com.ecocursos.ecocursos.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
public class Total {

    private Integer totalParceiros;
    private Integer totalAlunos;
    private Integer totalPedidos;
    private Integer totalAguardandoPagamento;
    private Integer totalPedidosPagos;
    private Integer totalMatriculas;
    private Integer totalLiberadoParceiro;


}
