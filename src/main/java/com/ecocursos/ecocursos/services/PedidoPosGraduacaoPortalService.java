package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.exceptions.ErrorException;
import com.ecocursos.ecocursos.models.PedidoPosGraduacaoPortal;
import com.ecocursos.ecocursos.models.enums.Status;
import com.ecocursos.ecocursos.repositories.PedidoPosGraduacaoPortalRepository;
import com.ecocursos.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoPosGraduacaoPortalService {

    private final PedidoPosGraduacaoPortalRepository repository;

    public void aprovar(Integer id) {
        PedidoPosGraduacaoPortal pedidoPosGraduacaoPortal = repository.findById(id).get();
        pedidoPosGraduacaoPortal.setStatus(Status.ATIVO);
        repository.save(pedidoPosGraduacaoPortal);
//        enviarEmail(pedidoPosGraduacaoPortal);
    }

    public PedidoPosGraduacaoPortal alterar(PedidoPosGraduacaoPortal pedidoPosGraduacaoPortal, Integer id) {
        PedidoPosGraduacaoPortal pedidoPosGraduacaoPortalExistente = repository.findById(id)
                .orElseThrow(() -> new ErrorException("Pedido de pós graduação não encontrado"));
        pedidoPosGraduacaoPortal.setId(pedidoPosGraduacaoPortalExistente.getId());
        pedidoPosGraduacaoPortal.setPedido(pedidoPosGraduacaoPortalExistente.getPedido());
        pedidoPosGraduacaoPortal.setStatus(pedidoPosGraduacaoPortalExistente.getStatus());
        return repository.save(pedidoPosGraduacaoPortal);
    }

    private void enviarEmail(PedidoPosGraduacaoPortal pedidoPosGraduacaoPortal) {
        String assunto = "🎓🚀 Acesso à Pós-graduação! Vem por aqui!";
        String destinatario = pedidoPosGraduacaoPortal.getPedido().getAluno().getEmail();
        String login = pedidoPosGraduacaoPortal.getLogin();
        String senha = pedidoPosGraduacaoPortal.getSenha();
        EmailUtil sender = new EmailUtil();
        sender.requestPostgraduate(assunto, login, senha, destinatario);
    }

}
