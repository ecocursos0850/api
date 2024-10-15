package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.models.SubCategoria;
import com.ecocursos.ecocursos.repositories.SubCategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubCategoriaService {
    
    @Autowired
    private SubCategoriaRepository repository;

    @Autowired
    private CategoriaService categoriaService;

    public List<SubCategoria> listar() {
        return repository.findAll();
    }

    public SubCategoria listarById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public SubCategoria salvar(SubCategoria subCategoria) {
        return repository.save(subCategoria);
    }

    public List<SubCategoria> listarByCategoria(Integer idCategoria) {
        return repository.findAllByCategoria(categoriaService.listarById(idCategoria));
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

