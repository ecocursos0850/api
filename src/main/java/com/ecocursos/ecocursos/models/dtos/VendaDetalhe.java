package com.ecocursos.ecocursos.models.dtos;

import com.ecocursos.ecocursos.models.Aluno;
import com.ecocursos.ecocursos.models.Curso;
import com.ecocursos.ecocursos.models.enums.StatusPedido;
import com.ecocursos.ecocursos.models.enums.TipoPagamento;

public class VendaDetalhe {
    private Curso curso;
    private Aluno aluno;
    private StatusPedido statusPedido;
    private TipoPagamento tipoPagamento;
    private String tipo;
    private String numeroPedido;
    private String dataPedido;
    private String valorVenda;
    private String valorPagar;
    private String comissao;

}
