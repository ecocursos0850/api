package com.ecocursos.ecocursos.repositories;

import com.ecocursos.ecocursos.models.Aluno;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ecocursos.ecocursos.models.Parceiro;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParceiroRepository extends JpaRepository<Parceiro, Integer>{

    @Query("SELECT p FROM Parceiro p ORDER BY p.status DESC")
    List<Parceiro> listarByFirstAtivos(Pageable pageable);

}
