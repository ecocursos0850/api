package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.models.TaxaMatricula;
import com.ecocursos.ecocursos.repositories.TaxaMatriculaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaxaMatriculaService {

    @Autowired
    private TaxaMatriculaRepository repository;

    public List<TaxaMatricula> listar() {
        return repository.findAll();
    }

    public TaxaMatricula listarById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public TaxaMatricula salvar(TaxaMatricula taxaMatricula) {
        taxaMatricula.setDataCadastro(LocalDateTime.now());
        return repository.save(taxaMatricula);
    }

    public TaxaMatricula alterar(Integer id, TaxaMatricula taxaMatricula) {
        TaxaMatricula taxaMatriculaExistente = listarById(id);
        if (taxaMatriculaExistente != null) {
            taxaMatricula.setId(taxaMatriculaExistente.getId());
            taxaMatricula.setDataCadastro(taxaMatriculaExistente.getDataCadastro());
            return repository.save(taxaMatricula);
        }
        return null;
    }

    public void deletar(Integer id) {
        TaxaMatricula taxaMatricula = listarById(id);
        if (taxaMatricula != null) {
            repository.delete(taxaMatricula);
        }
    }

}
