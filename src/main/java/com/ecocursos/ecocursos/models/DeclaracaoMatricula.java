package com.ecocursos.ecocursos.models;

import java.time.LocalDate;

import com.ecocursos.ecocursos.models.enums.StatusDeclaracaoMatricula;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "declaracao_matricula")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeclaracaoMatricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIncludeProperties({"id", "nome"})
    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate inicioPeriodo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate finalPeriodo;

    //@JsonFormat(pattern = "yyyy-MM-dd")
    //private LocalDate inicioPeriodo;
    
    //@JsonFormat(pattern = "yyyy-MM-dd")
    //private LocalDate finalPeriodo;

    @Enumerated(EnumType.ORDINAL)
    private StatusDeclaracaoMatricula status;

    @JsonFormat(shape= JsonFormat.Shape.STRING ,pattern = "yyyy-MM-dd")
    private LocalDate dataCadastro;

    private boolean aprovado;

    @JsonIncludeProperties({"id"})
    @OneToOne
    @JoinColumn(name = "matricula_id")
    private Matricula matricula;
    
    private Double valor;

    private String anexoComprovante;
    

}
