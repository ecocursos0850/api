package com.ecocursos.ecocursos.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "avaliacao_pergunta")
@Data
public class AvaliacaoPergunta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String titulo;
    private Double valor;
    private Integer ordem;

    @JsonIgnoreProperties({"perguntas"})
    @ManyToOne
    @JoinColumn(name = "avaliacao_id")
    private Avaliacao avaliacao;

    @JsonIgnoreProperties({"pergunta"})
    @OneToMany(mappedBy = "pergunta", cascade = CascadeType.ALL)
    private List<AvaliacaoResposta> avaliacaoRespostas; 


}
