package com.ecocursos.ecocursos.models.dtos;

import lombok.Data;

@Data
public class FaturamentoTotal {

    private Integer mes;
    private Double valorTotal;
    private Double totalSite;
    private Double totalDeclaracao;
    private Integer quantidade;

}
