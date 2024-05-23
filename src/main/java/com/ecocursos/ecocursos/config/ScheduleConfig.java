package com.ecocursos.ecocursos.config;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ecocursos.ecocursos.services.AlunoService;
import com.ecocursos.ecocursos.services.PedidoService;

import lombok.RequiredArgsConstructor;

@Component
// @EnableScheduling
@RequiredArgsConstructor
public class ScheduleConfig {
    
    private final PedidoService pedidoService;
    private final AlunoService alunoService;

    @Scheduled(fixedRate = 300000)
    private void atualizarStatusPagamentoPedido() {
        System.out.println("Rodando atualização de status de pagamento!");
        pedidoService.atualizarStatusPagamentos();
    }

    @Scheduled(fixedRate = 300000)
    private void atualizarFaturaEmAnalise() {
        System.out.println("Rodando atualização de status de pagamento de cartão de crédito");
        pedidoService.atualizarFaturaEmAnalise();
    }

    @Scheduled(cron = "10 16 * * *")
    private void verificarAniversarioAluno() {
        alunoService.verificarAniversarioAluno();
    }

}
