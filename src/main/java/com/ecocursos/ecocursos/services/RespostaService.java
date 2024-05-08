package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.Pergunta;
import com.ecocursos.ecocursos.models.Resposta;
import com.ecocursos.ecocursos.repositories.RespostaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RespostaService {

    private final RespostaRepository repository;
    private final PerguntaService perguntaService;

    private void searchExternal(Resposta resposta) {
        resposta.setPergunta(perguntaService.listarById(resposta.getPergunta().getId()));
    }

    public List<Resposta> listar() {
        return repository.findAll();
    }

    public Resposta listarById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Resposta não encontrada"));
    }

    public Resposta salvar(Resposta resposta) {
        searchExternal(resposta);
        return repository.save(resposta);
    }

    public Resposta alterar(Integer id, Resposta resposta) {
        listarById(id);
        resposta.setId(id);
        searchExternal(resposta);
        return repository.save(resposta);
    }

    public void deletar(Integer id) {
        repository.delete(listarById(id));
    }

    public List<Resposta> listarByPergunta(Integer id) {
        return repository.findAllByPergunta(perguntaService.listarById(id));
    }
}
