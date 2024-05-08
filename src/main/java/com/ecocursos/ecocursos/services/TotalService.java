package com.ecocursos.ecocursos.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.models.Total;
import com.ecocursos.ecocursos.repositories.AlunoRepository;
import com.ecocursos.ecocursos.repositories.MatriculaRepository;
import com.ecocursos.ecocursos.repositories.PedidoRepository;

@Service
public class TotalService {

    @Autowired
    private AlunoRepository alunoRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private MatriculaRepository matriculaRepository;


    public Total listar() {
        Total total = new Total();
        total.setTotalAlunos(alunoRepository.retornarTamanhoLista());
        total.setTotalPedidos(pedidoRepository.tamanhoTotalLista(LocalDateTime.now().getMonthValue()));
        total.setTotalAguardandoPagamento(pedidoRepository.tamanhoTotalListaPendente());
        total.setTotalPedidosPagos(pedidoRepository.tamanhoTotalListaPago());
        total.setTotalMatriculas(matriculaRepository.tamanhoTotalLista());
        total.setTotalLiberadoParceiro(matriculaRepository.totalMatriculasLiberadoParceiro());
        return total;
    }

}
