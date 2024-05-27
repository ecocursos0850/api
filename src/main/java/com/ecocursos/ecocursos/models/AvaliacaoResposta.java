package com.ecocursos.ecocursos.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "avaliacao_resposta")
@Data
public class AvaliacaoResposta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String descricao;
    private Integer ordem;
    private boolean correta;

    @JsonIgnoreProperties({"respostas"})
    @ManyToOne
    @JoinColumn(name = "pergunta_id")
    private AvaliacaoPergunta pergunta;

}
