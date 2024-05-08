package com.ecocursos.ecocursos.repositories;

import com.ecocursos.ecocursos.models.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecocursos.ecocursos.models.Aluno;
import com.ecocursos.ecocursos.models.AlunoAvaliacao;

public interface AlunoAvaliacaoRepository extends JpaRepository<AlunoAvaliacao, Integer>{
    
    boolean existsByAluno(Aluno aluno);

    AlunoAvaliacao findByMatricula(Matricula matricula);
}
