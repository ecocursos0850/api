package com.ecocursos.ecocursos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ecocursos.ecocursos.models.Aluno;
import com.ecocursos.ecocursos.models.DeclaracaoMatricula;
import com.ecocursos.ecocursos.models.Matricula;

public interface DeclaracaoMatriculaRepository extends JpaRepository<DeclaracaoMatricula, Integer>{

    List<DeclaracaoMatricula> findAllByAluno(Aluno aluno);

    DeclaracaoMatricula findByMatricula(Matricula matricula);

    @Query("SELECT SUM(d.valor) FROM DeclaracaoMatricula d WHERE MONTH(d.dataCadastro) = :month AND YEAR(d.dataCadastro) = :year AND d.status = 0")
    Double valorTotalMesDeclaracao(@Param("month") Integer mes, @Param("year") Integer year);

}
