package com.ecocursos.ecocursos.models;

import java.time.LocalDateTime;
import java.util.List;

import com.ecocursos.ecocursos.models.enums.Status;
import com.ecocursos.ecocursos.models.enums.TipoCurso;
import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "curso")
@Data
@NoArgsConstructor
public class Curso {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titulo;
    @Column(columnDefinition = "TEXT")
    private String descricao; 
    @Column(columnDefinition = "TEXT")
    private String capa;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCadastro;

    @JsonIncludeProperties({"id", "titulo"})
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;


    @JsonIgnoreProperties({"cursos", "categoria", "status"})
    @ManyToOne
    @JoinColumn(name = "subcategoria_id")
    private SubCategoria subCategoria;

    // @JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
    @JsonIgnoreProperties({"curso"})
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<MaterialCurso> materiais;

    private Double preco;
    private Double precoMinimo;
    private String qtdParcelas;
    private Integer cargaHoraria;

    @Enumerated(EnumType.ORDINAL)
    private TipoCurso tipoCurso;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @JsonIgnoreProperties({"cursos", "categoria"})
    @ManyToOne
    @JoinColumn(name = "taxa_matricula_id")
    private TaxaMatricula taxaMatricula;

    @JsonIgnore
    @JsonIncludeProperties({"id"})
    @OneToMany(mappedBy = "curso")
    private List<Matricula> matriculas;

    private Boolean promocao;
    private Boolean descontoParceiro;
    @Column(columnDefinition = "TEXT")
    private String conteudo;
    @Column(columnDefinition = "TEXT")
    private String conteudoImpressao;
    @Column(columnDefinition = "TEXT")
    private String rodape;
    @Column(columnDefinition = "TEXT")
    private String rodapeAfiliado;

}
