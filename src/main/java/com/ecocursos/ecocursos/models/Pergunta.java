package com.ecocursos.ecocursos.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "pergunta")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pergunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(columnDefinition = "TEXT")
    private String descricao;

    @JsonIgnoreProperties({"perguntas"})
    @ManyToOne
    @JoinColumn(name = "simulado_id")
    private Simulado simulado;

    @JsonIgnoreProperties({"pergunta"})
    @OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL)
    private List<Resposta> respostas;

}
