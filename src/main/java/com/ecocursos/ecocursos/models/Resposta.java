package com.ecocursos.ecocursos.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resposta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String resposta;
    private boolean correta;

    @JsonIgnoreProperties({"respostas"})
    @ManyToOne
    @JoinColumn(name = "pergunta_id")
    private Pergunta pergunta;

}
