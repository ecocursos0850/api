package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.CategoriaSimulado;
import com.ecocursos.ecocursos.repositories.CategoriaSimuladoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaSimuladoService {

    private final CategoriaSimuladoRepository repository;

    public List<CategoriaSimulado> listar() {
        return repository.findAll();
    }

    public CategoriaSimulado listarById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Categoria de simulado não encontrada"));
    }

    public CategoriaSimulado salvar(CategoriaSimulado categoriaSimulado) {
        return repository.save(categoriaSimulado);
    }

    public CategoriaSimulado alterar(Integer id, CategoriaSimulado categoriaSimulado) {
        listarById(id);
        categoriaSimulado.setId(id);
        return repository.save(categoriaSimulado);
    }

    public void deletar(Integer id) {
        repository.delete(listarById(id));
    }

}
