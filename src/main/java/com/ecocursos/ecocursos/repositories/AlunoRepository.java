package com.ecocursos.ecocursos.repositories;

import com.ecocursos.ecocursos.models.Afiliado;
import com.ecocursos.ecocursos.models.Aluno;
import com.ecocursos.ecocursos.models.Parceiro;
import com.ecocursos.ecocursos.models.User;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AlunoRepository extends JpaRepository<Aluno, Integer>{
    List<Aluno> findAllByNomeContainingIgnoreCase(String nome);

    List<Aluno> findAllByEmailContainingIgnoreCase(String email);

    List<Aluno> findAllByAfiliado(Afiliado afiliado);

    List<Aluno> findAllByParceiro(Parceiro parceiro);

    Boolean existsByEmail(String email);

    Boolean existsByReferencia(String referencia);

    Aluno findByEmail(String emai);

    @Query(value = "SELECT * FROM aluno a WHERE a.sexo = :sexo", nativeQuery = true)
    List<Aluno> findAllBySexo(@Param("sexo") String sexo);

    @Query(value = "SELECT * FROM aluno a WHERE a.estado = :estado", nativeQuery = true)
    List<Aluno> findAllByEstado(@Param("estado") String estado);

    @Query(value = "SELECT * FROM aluno a WHERE a.cpf = :cpf", nativeQuery = true)
    List<Aluno> findAllByCpf(@Param("cpf") String cpf);

    boolean existsByUsuario(User user);

    @Query("SELECT COUNT(a) FROM Aluno a")
    Integer retornarTamanhoLista();

    Integer countByParceiro(Parceiro parceiro);

    Boolean existsByCpf(String cpf);

    @Query("SELECT a FROM Aluno a WHERE MONTH(a.dataNascimento) = month(now())  AND DAY(a.dataNascimento) = day(now())")
    List<Aluno> listarAniversariantesMes(Pageable pageable);
    
    Boolean existsByDataNascimento(LocalDateTime dataNascimento);

    @Modifying
    @Query(value = "UPDATE Aluno a SET a.senha = :senha WHERE a.cpf = :cpf")
    void recuperarSenha(@Param("cpf") String cpf, @Param("senha") String senha);

    Aluno findByCpf(String cpf);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Aluno a set a.referencia = :referencia where a.id = :id")
    void saveReferencia(@Param("referencia") String referencia, @Param("id") Integer id);
    
}
