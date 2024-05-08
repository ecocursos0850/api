package com.ecocursos.ecocursos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecocursos.ecocursos.models.Curso;
import com.ecocursos.ecocursos.models.MaterialCurso;

public interface MaterialCursoRepository extends JpaRepository<MaterialCurso, Integer>{
    // List<MaterialCurso> findAllByCurso(Curso curso);

    @Query("SELECT m FROM MaterialCurso m WHERE m.curso = :curso ORDER BY m.ordenacao")
    List<MaterialCurso> findAllByCurso(Curso curso);
}
