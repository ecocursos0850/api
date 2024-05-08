package com.ecocursos.ecocursos.repositories;

import com.ecocursos.ecocursos.models.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecocursos.ecocursos.models.Avaliacao;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Integer>{

    Avaliacao findByCurso(Curso curso);
}
