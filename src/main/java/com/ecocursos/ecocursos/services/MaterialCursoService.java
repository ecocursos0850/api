package com.ecocursos.ecocursos.services;

import java.util.List;

import com.ecocursos.ecocursos.models.enums.TipoMaterial;

import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.exceptions.ErrorException;
import com.ecocursos.ecocursos.models.MaterialCurso;
import com.ecocursos.ecocursos.repositories.MaterialCursoRepository;

@Service
public class MaterialCursoService {
    
    @Autowired
    private MaterialCursoRepository repository;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private FtpService ftpService;

    public List<MaterialCurso> listar() {
        return repository.findAll();
    }

    public MaterialCurso listarById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public MaterialCurso salvar(MaterialCurso materialCurso) {
        return repository.save(materialCurso);
    }

    public MaterialCurso alterar(Integer id, MaterialCurso materialCurso) {
        MaterialCurso materialCursoExistente = listarById(id);
        if (materialCursoExistente != null) {
            materialCurso.setId(materialCursoExistente.getId());
            return repository.save(materialCurso);
        }
        return null;
    }

    public void deletar(Integer id) {
        try {
            MaterialCurso materialCurso = listarById(id);
            if (materialCurso != null) {
                repository.delete(id);
            }
        } catch(Exception e) {
            throw new ErrorException("Erro ao deletar material");
        }
    }

    public List<MaterialCurso> listarByCurso(Integer id) {
        return repository.findAllByCurso(cursoService.listarById(id));
    }

}
