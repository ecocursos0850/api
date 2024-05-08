package com.ecocursos.ecocursos.repositories;

import com.ecocursos.ecocursos.models.AnexoMaterialCurso;
import com.ecocursos.ecocursos.models.MaterialCurso;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnexoMaterialCursoRepository extends JpaRepository<AnexoMaterialCurso, Integer> {
    List<AnexoMaterialCurso> findAllByMaterialCurso(MaterialCurso materialCurso);
}
