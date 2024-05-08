package com.ecocursos.ecocursos.models;

import java.time.LocalDate;
import java.util.List;

import com.ecocursos.ecocursos.models.enums.StatusAfiliado;
import com.ecocursos.ecocursos.models.enums.TipoAfiliado;
import com.ecocursos.ecocursos.services.ComissaoAfiliado;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "afiliado")
@Data
public class Afiliado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.ORDINAL)
    private TipoAfiliado tipoAfiliado;

    private String cpfCnpj;
    private String rg;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;
    private String nome;
    private String email;
    
    /* Atributos para pessoa juridica */
    private String incricaoEstadual;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataAbertura;
    private String cpfResponsavel;
    private String razaoSocial;
    private String nomeFantasia;

    private String site;
    private String telefoneFixo;
    private String celular;
    private String cep;
    private String cidade;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataLiberacao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataCadastro;
    private String estado;
    private String numero;
    private String bairro;
    private String complemento;

    @JsonIgnoreProperties({"afiliado"})
    @OneToMany(mappedBy = "afiliado", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ComissaoAfiliado> comissoes;

    @Enumerated(EnumType.ORDINAL)
    private StatusAfiliado status;

    @JsonProperty(access = Access.WRITE_ONLY)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "afiliado_curso",
        joinColumns = @JoinColumn(name= "afiliado_id"),
        inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private List<Curso> cursos;

    @JsonIgnore
    @JsonIgnoreProperties({"afiliado"})
    @OneToMany(mappedBy = "afiliado", cascade = CascadeType.ALL)
    private List<Aluno> alunos;

    @JsonIncludeProperties({"firstname", "lastname"})
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    @JsonIgnore
    @JsonIgnoreProperties({"afiliado"})
    @OneToMany(mappedBy = "afiliado", cascade = CascadeType.ALL)
    private List<Matricula> matriculas;

}
