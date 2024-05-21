package com.ecocursos.ecocursos.models;

import java.util.List;

import com.ecocursos.ecocursos.models.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "avaliacao")
@Data
public class Avaliacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titulo;

    @OneToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @JsonIgnoreProperties({"avaliacao"})
    @OneToMany(mappedBy = "avaliacao", cascade = CascadeType.ALL)
    private List<AvaliacaoPergunta> perguntas;

}
