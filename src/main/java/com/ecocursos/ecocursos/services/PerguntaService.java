package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.Pergunta;
import com.ecocursos.ecocursos.repositories.PerguntaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PerguntaService {

    private final PerguntaRepository repository;
    private final SimuladoService simuladoService;

    private void searchExternal(Pergunta pergunta) {
        pergunta.setSimulado(simuladoService.listarById(pergunta.getSimulado().getId()));
    }

    public List<Pergunta> listar() {
        return repository.findAll();
    }

    public Pergunta listarById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Pergunta não encontrada"));
    }

    public Pergunta salvar(Pergunta pergunta) {
        searchExternal(pergunta);
        return repository.save(pergunta);
    }

    public Pergunta alterar(Integer id, Pergunta pergunta) {
        listarById(id);
        pergunta.setId(id);
        searchExternal(pergunta);
        return repository.save(pergunta);
    }

    public void deletar(Integer id) {
        repository.delete(listarById(id));
    }

    public List<Pergunta> listarBySimulado  (Integer id) {
        return repository.findAllBySimulado(simuladoService.listarById(id));
    }
}
