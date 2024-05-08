package com.ecocursos.ecocursos.repositories;

import com.ecocursos.ecocursos.models.Aluno;
import com.ecocursos.ecocursos.models.Certificado;
import com.ecocursos.ecocursos.models.Matricula;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CertificadoRepository extends JpaRepository<Certificado, Integer> {
    @Query("SELECT c FROM Certificado c WHERE c.matricula.aluno = :aluno ")
    List<Certificado> findAllByAluno(@Param("aluno")Aluno aluno);

    Optional<Certificado> findById(UUID id);

    Certificado findByMatricula(Matricula matricula);
}
