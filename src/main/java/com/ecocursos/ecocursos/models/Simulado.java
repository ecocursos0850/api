package com.ecocursos.ecocursos.models;

import com.ecocursos.ecocursos.models.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "simulado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Simulado {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "categoria_simulado_id")
    private CategoriaSimulado categoriaSimulado;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @OneToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @JsonIgnoreProperties({"simulado"})
    @OneToMany(mappedBy = "simulado", cascade = CascadeType.ALL)
    private List<Pergunta> perguntas;

}
