package com.ecocursos.ecocursos.repositories;

import java.util.List;

import com.ecocursos.ecocursos.models.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MatriculaRepository extends JpaRepository<Matricula, Integer> {

    List<Matricula> findAllByAfiliado(Afiliado afiliado);

    List<Matricula> findAllByAluno(Aluno aluno);

    @Query("SELECT m FROM Matricula m WHERE m.status <> 2 AND m.aluno = :aluno")
    List<Matricula> findAllByAluno(@Param("aluno") Aluno aluno, Pageable pageable);
    List<Matricula> findAllByPedido(Pedido pedido);

    @Query("SELECT m FROM Matricula m ORDER BY m.dataMatricula DESC")
    List<Matricula> listarMatriculasOrdemDecrescente(Pageable pageable);

    @Query("SELECT COUNT(m) FROM Matricula m")
    Integer tamanhoTotalLista();

    @Query("SELECT COUNT(m) FROM Matricula m WHERE MONTH(m.dataMatricula) = :month AND YEAR(m.dataMatricula) = :year")
    Integer matriculaMes(@Param(("month")) Integer month, @Param("year") Integer year);

    @Query("SELECT COUNT(m) FROM Matricula m WHERE m.status = 4")
    Integer totalMatriculasLiberadoParceiro();

    @Query("SELECT COUNT(m) FROM Matricula m WHERE MONTH(m.dataMatricula) = :month AND YEAR(m.dataMatricula) = :year AND m.afiliado = :afiliado AND m.pedido.status = 1 AND m.valor_curso != '0.0'")
    Integer matriculaMesAfiliado(@Param(("month")) Integer month, @Param(("year")) Integer year, @Param("afiliado") Afiliado afiliado);

    @Query("SELECT COUNT(m) FROM Matricula m WHERE MONTH(m.dataMatricula) = :month AND YEAR(m.dataMatricula) =:year AND m.afiliado = :afiliado AND m.pedido.status = 1")
    Integer matriculaMesAfiliadoCursoLivre(@Param(("month")) Integer month, @Param(("year")) Integer year, @Param("afiliado") Afiliado afiliado);


    @Query("SELECT COUNT(m) FROM Matricula m WHERE MONTH(m.dataMatricula) = :month AND YEAR(m.dataMatricula) =:year  AND m.afiliado = :afiliado AND m.curso.categoria.titulo = 'PÓS-GRADUAÇÃO / MBA'")
    Integer matriculaMesAfiliadoPosGraduacao(@Param(("month")) Integer month, @Param(("year")) Integer year, @Param("afiliado") Afiliado afiliado);
//    @Query("SELECT m.curso, COUNT(m) as quantidadeMatriculas FROM Matricula m GROUP BY m.curso ORDER BY quantidadeMatriculas DESC")

    @Query("SELECT m.curso, COUNT(m) as quantidadeMatriculas " +
            "FROM Matricula m " +
            "GROUP BY m.curso " +
            "ORDER BY quantidadeMatriculas DESC LIMIT 10")
    List<Curso> findTop10CursosMaisVendidos();

    @Query("SELECT m FROM Matricula m WHERE m.afiliado = :afiliado AND MONTH(m.dataMatricula) = :mes")
    List<Matricula> findAllByAfiliado(@Param("afiliado") Afiliado afiliado, @Param("mes") Integer mes ,Pageable pageable);

    @Query("SELECT m FROM Matricula m WHERE m.afiliado = :afiliado AND MONTH(m.dataMatricula) = :mes")
    List<Matricula> listarMinhasVendas(@Param("afiliado") Afiliado afiliado, @Param("mes") Integer mes, Pageable pageable);

    @Query("SELECT m FROM Matricula m WHERE MONTH(m.dataMatricula) = :month AND YEAR(m.dataMatricula) = :year")
    List<Matricula> listarByMes(@Param("month") Integer month, @Param("year") Integer year, Pageable pageable);

    Boolean existsByPedido(Pedido pedido);

}
