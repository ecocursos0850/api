package com.ecocursos.ecocursos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.AvaliacaoResposta;
import com.ecocursos.ecocursos.repositories.AvaliacaoRespostaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliacaoRespostaService {
    
    private final AvaliacaoRespostaRepository repository;
    private final AvaliacaoPerguntaService avaliacaoPerguntaService;

    public List<AvaliacaoResposta> listar() {
        return repository.findAll();
    }

    public List<AvaliacaoResposta> listarByPergunta(Integer id) {
        return repository.findAllByPergunta(avaliacaoPerguntaService.listarById(id));
    }

    public AvaliacaoResposta listarById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Resposta não encontrada"));
    }

    public AvaliacaoResposta salvar(AvaliacaoResposta avaliacaoResposta) {
        return repository.save(avaliacaoResposta);
    }

    public AvaliacaoResposta alterar(Integer id, AvaliacaoResposta avaliacaoResposta) {
        listarById(id);
        avaliacaoResposta.setId(id);
        return repository.save(avaliacaoResposta);
    }

    public void deletar(Integer id) {
        repository.delete(listarById(id));
    }

}
