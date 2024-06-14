package com.ecocursos.ecocursos.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.ecocursos.ecocursos.models.enums.StatusAvaliacaoMatricula;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.*;
import org.aspectj.internal.lang.annotation.ajcDeclareAnnotation;

import com.ecocursos.ecocursos.models.enums.StatusPedido;

import lombok.Data;

@Entity
@Table(name = "matricula")
@Data
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataMatricula;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataLiberacao;

    @JsonIgnoreProperties({"matriculas", "materiais"})
    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @JsonIncludeProperties({"id", "nome"})
    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    private Double valorCurso;
    private Double valorMatricula;
    private String observacao;
    private String nomeVendedor;

    @Enumerated(EnumType.ORDINAL)
    private StatusAvaliacaoMatricula status;

    @JsonIncludeProperties({"id", "tipoCurso", "dataPedido", "total", "status"})
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @JsonIncludeProperties({"id", "nome", "categoria", "comissoes"})
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "afiliado_id", nullable = true)
    private Afiliado afiliado;

    private String formaPagamento;
    private String diaPagamento;
    private Integer qtdParcelas;
    private Double valorParcelas;
    private Integer acertos;
    private Double nota;

    @OneToOne(mappedBy = "matricula")
    private DeclaracaoMatricula declaracaoMatricula;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "aluno_avaliacao_id")
    private AlunoAvaliacao alunoAvaliacao;

    @Transient
    private Double valorComissao;

    @Transient Double porcentagemComissao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Transient
    private LocalDateTime dataLimite;

    @Transient
    private boolean provaExpirou;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Transient
    private LocalDateTime dataLimiteProva;

    @JsonIgnoreProperties({"matricula"})
    @OneToOne
    private Certificado certificado;

    @JsonIgnore
    @OneToMany(mappedBy = "matricula")
    private List<MatriculaLogs> logs;

    public Matricula() {
        this.dataMatricula = LocalDateTime.now();
    }

    public LocalDateTime getDataLimite() {
        if (this.getDataLiberacao() != null) {
            return this.getDataLiberacao().plusDays(Math.round(this.getCurso().getCargaHoraria() / 4));
        }
        return null;
    }

    public boolean isProvaExpirou() {
        if (this.getDataLimiteProva().isBefore(LocalDateTime.now())) {
            return true;
        }
        return false;
    }

    public LocalDateTime getDataLimiteProva() {
        if (this.getDataLiberacao() != null) {
            return this.getDataLiberacao().plusDays(181);
        }
        return null;
    }

    public Double getValorComissao() {
        if(getAfiliado() != null) {
            getAfiliado().getComissoes().stream().forEach(comissaoAfiliado -> {
                if (getPedido() != null) {
                    setValorComissao(getPedido().getTotal() * (comissaoAfiliado.getComissao() / 100));
                } else {
                    setValorComissao(valorCurso * (comissaoAfiliado.getComissao() / 100));
                }
            });
        }
        return valorComissao;
    }

    public Double getPorcentagemComissao() {
        if(getAfiliado() != null) {
            getAfiliado().getComissoes().forEach(comissao -> {
                if (comissao.getCategoria() == getCurso().getCategoria()) {
                    setPorcentagemComissao(comissao.getComissao());
                }
            });
        }
        return porcentagemComissao;
    }

}
