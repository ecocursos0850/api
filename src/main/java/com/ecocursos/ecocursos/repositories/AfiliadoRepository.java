package com.ecocursos.ecocursos.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecocursos.ecocursos.models.Afiliado;
import com.ecocursos.ecocursos.models.User;


public interface AfiliadoRepository extends JpaRepository<Afiliado, Integer>{
    
    @Query("SELECT a FROM Afiliado a WHERE a.nome = :nome OR a.nomeFantasia = :nome")
    Afiliado findByNomeOrNomeFantasiaContainingAllIgnoreCase(String nome);

    boolean existsByUsuario(User user);

    Optional<Afiliado> findByEmail(String email);

}
