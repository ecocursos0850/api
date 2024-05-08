package com.ecocursos.ecocursos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.ecocursos.ecocursos.models.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);

  User findByIdentificador(Integer identificador);

    @Modifying
    @Transactional
    @Query(value = "UPDATE usuarios SET password = :senha WHERE email = :email", nativeQuery = true )
    void atualizarSenha(@Param("email") String email, @Param("senha") String senha);
}