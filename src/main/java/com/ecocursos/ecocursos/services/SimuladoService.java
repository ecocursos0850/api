package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.Pergunta;
import com.ecocursos.ecocursos.models.Simulado;
import com.ecocursos.ecocursos.repositories.SimuladoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SimuladoService {

    private final SimuladoRepository repository;
    private final CategoriaSimuladoService categoriaSimuladoService;

    public List<Simulado> listar() {
        return repository.findAll();
    }

    public Simulado listarById(Integer id) {
        Simulado simulado = repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Simulado não encontrado"));
        embaralharPerguntas(simulado);
        return simulado;
    }

    public Simulado salvar(Simulado simulado) {
        return repository.save(simulado);
    }

    public Simulado alterar(Integer id, Simulado simulado) {
        listarById(id);
        simulado.setId(id);
        return repository.save(simulado);
    }

    public void deletar(Integer id) {
        repository.delete(listarById(id));
    }

    public List<Simulado> listarByCategoriaSimulado(Integer id) {
        List<Simulado> simulados = repository.findAllByCategoriaSimulado(categoriaSimuladoService.listarById(id));
        simulados.forEach(SimuladoService::embaralharPerguntas);
        return repository.findAllByCategoriaSimulado(categoriaSimuladoService.listarById(id));
    }

    private static void embaralharPerguntas(Simulado simulado) {
        List<Pergunta> perguntasOriginais = simulado.getPerguntas();
        Collections.shuffle(perguntasOriginais);
        List<Pergunta> perguntasLimitadas = perguntasOriginais.stream().limit(10).collect(Collectors.toList());
        simulado.setPerguntas(perguntasLimitadas);
    }

}
