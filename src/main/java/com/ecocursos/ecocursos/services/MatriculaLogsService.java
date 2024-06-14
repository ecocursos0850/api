package com.ecocursos.ecocursos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.exceptions.ErrorException;
import com.ecocursos.ecocursos.models.MatriculaLogs;
import com.ecocursos.ecocursos.repositories.MatriculaLogsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatriculaLogsService {
   
    private final MatriculaLogsRepository repository;
    private final MatriculaService matriculaService;

    public void salvar(MatriculaLogs matriculaLogs) {
        repository.save(matriculaLogs);
    }

    public List<MatriculaLogs> listarByMatricula(Integer idMatricula) {
        try {
            return repository.findAllByMatricula(matriculaService.listarById(idMatricula));
        } catch(Exception e) {
            throw new ErrorException("Erro ao listar logs de matrículas por matrícula");
        }
    }
    
}
