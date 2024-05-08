package com.ecocursos.ecocursos.models.dtos;

import com.ecocursos.ecocursos.models.Matricula;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Extrato {

    private int totalVendas;
    private String primeiraVenda;
    private String dataUltimaVenda;
    private String totalValorVenda;
    private String totalValorPagar;
    private List<VendaDetalhe> data;

}
