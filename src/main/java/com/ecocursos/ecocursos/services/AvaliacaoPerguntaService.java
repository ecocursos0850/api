package com.ecocursos.ecocursos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.AvaliacaoPergunta;
import com.ecocursos.ecocursos.repositories.AvaliacaoPerguntaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AvaliacaoPerguntaService {
    
    private final AvaliacaoPerguntaRepository repository;
    private final AvaliacaoService avaliacaoService;

    public List<AvaliacaoPergunta> listar() {
        return repository.findAll();
    }

    public List<AvaliacaoPergunta> listarByAvaliacao(Integer id) {
        return repository.findAllByAvaliacao(avaliacaoService.listarById(id));
    }

    public AvaliacaoPergunta listarById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Avaliação não encontrado"));
    }

    public AvaliacaoPergunta salvar(AvaliacaoPergunta avaliacaoPergunta) {
        return repository.save(avaliacaoPergunta);
    }

    public AvaliacaoPergunta alterar(Integer id, AvaliacaoPergunta avaliacaoPergunta) {
        listarById(id);
        avaliacaoPergunta.setId(id);
        return repository.save(avaliacaoPergunta);
    }

    public void delete(Integer integer) {
        repository.delete(listarById(integer));
    }

}
