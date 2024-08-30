package com.ecocursos.ecocursos.repositories;

import com.ecocursos.ecocursos.models.Aluno;
import com.ecocursos.ecocursos.models.Pedido;
import com.ecocursos.ecocursos.models.enums.StatusPedido;
import com.ecocursos.ecocursos.models.enums.TipoCurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer>{

    Pedido findByReferencia(String referencia);

    List<Pedido> findAllByStatus(StatusPedido statusPedido);

    List<Pedido> findAllByTipoCurso(TipoCurso tipoCurso);

    @Query("UPDATE Pedido p SET p.status = :status WHERE p.id = :id")
    void atualizarStatusPedido(@Param("status") StatusPedido statusPedido, @Param("id") Integer id);

    List<Pedido> findAllByAluno(Aluno aluno);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE MONTH(p.dataPedido) = :mes")
    Integer tamanhoTotalLista(@Param("mes") Integer mes);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.status = 0")
    Integer tamanhoTotalListaPendente();

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.status = 1")
    Integer tamanhoTotalListaPago();

    @Query(value = "select sum(p.total) from pedido p \n" + //
            "inner join matricula m on m.pedido_id = p.id \n" + //
            "where p.status = 1 and MONTH(p.data_pedido) = :mes and YEAR(p.data_pedido) = :year and m.status != 4;", nativeQuery = true)
    Double pegarValorTotalMes(@Param("mes") Integer mes, @Param("year") Integer year);

    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.status = 1 AND p.referencia <> null AND MONTH(p.dataPedido) = :mes AND YEAR(p.dataPedido) = :year AND p.status = 1")
    Double pegarValorTotalMesSite(@Param("mes") Integer mes, @Param("year") Integer year);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE MONTH(p.dataPedido) = :mes AND YEAR(p.dataPedido) = :year AND p.status = 1")
    Integer pegarQuantidadeMes(@Param("mes") Integer mes, @Param("year") Integer year);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.aluno.estado = :estado")
    Integer pegarQuantidadePorEstado(@Param("estado") String estado);

    @Query(value = "SELECT * FROM pedido p WHERE p.status = :status", nativeQuery = true)
    List<Pedido> pegarPorStatus(@Param("status") Integer statusPedido);

    @Query(value = "SELECT * FROM pedido p WHERE p.tipoCurso = :tipoCurso", nativeQuery = true)
    List<Pedido> pegarPorTipoCurso(@Param("tipoCurso") TipoCurso tipoCurso);

    @Query(value = "SELECT * FROM pedido p WHERE p.aluno.nome like %:nome%", nativeQuery = true)
    List<Pedido> pegarPorAluno(@Param("nome") String nome);

    @Query(value = "SELECT * FROM pedido p WHERE p.dataPedido BETWEEN :dataInicial AND :dataFinal", nativeQuery = true)
    List<Pedido> pegarEntreDatas(@Param("dataInicial") LocalDateTime dataInicial, @Param("dataFinal") LocalDateTime dataFinal);

    @Query(value = "SELECT * FROM pedido p WHERE p.dataPedido > :dataInicial", nativeQuery = true)
    List<Pedido> pegarAPartirdeDataInicial(@Param("dataInicial") LocalDateTime dataInicial);

    @Query(value = "SELECT * FROM pedido p WHERE p.dataPedido < :dataFinal", nativeQuery = true)
    List<Pedido> pegarAAntesDataFinal(@Param("dataFinal") LocalDateTime dataFinal);

    @Query(value = "SELECT p FROM Pedido p WHERE p.status <> 1 AND p.status <> 3 AND p.referencia <> null AND p.status <> 2 ORDER BY p.dataPedido DESC LIMIT 25")
    List<Pedido> listar25Ultimos();

    @Query("SELECT p FROM Pedido p WHERE p.referencia <> null AND p.status <> 1 AND p.status <> 2 ORDER BY p.dataPedido DESC LIMIT 25")
    List<Pedido> listar25UltimosSemFiltro();

}
