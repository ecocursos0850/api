package com.ecocursos.ecocursos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.models.SubCategoria;
import com.ecocursos.ecocursos.repositories.SubCategoriaRepository;

@Service
public class SubCategoriaService {
    
    @Autowired
    private SubCategoriaRepository repository;

    public List<SubCategoria> listar() {
        return repository.findAll();
    }

    public SubCategoria listarById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public SubCategoria salvar(SubCategoria subCategoria) {
        return repository.save(subCategoria);
    }

    public SubCategoria alterar(Integer id, SubCategoria subCategoria) {
        SubCategoria subCategoriaExistente = listarById(id);
        if (subCategoriaExistente != null) {
            subCategoria.setId(subCategoriaExistente.getId());
            return repository.save(subCategoria);
        }
        return null;
    }

    public void deletar(Integer id) {
        SubCategoria subCategoria = listarById(id);
        if (subCategoria != null) {
            repository.delete(subCategoria);
        }
    }

}

