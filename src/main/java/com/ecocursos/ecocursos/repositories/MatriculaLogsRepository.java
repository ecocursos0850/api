package com.ecocursos.ecocursos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecocursos.ecocursos.models.Matricula;
import com.ecocursos.ecocursos.models.MatriculaLogs;

public interface MatriculaLogsRepository extends JpaRepository<MatriculaLogs, Integer> {
 
    List<MatriculaLogs> listarByMatricula(Matricula matricula);

}
