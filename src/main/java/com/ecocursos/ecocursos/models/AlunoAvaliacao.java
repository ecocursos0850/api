package com.ecocursos.ecocursos.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aluno_avaliacao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoAvaliacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTIDENTITYO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne
    @JoinColumn(name = "avaliacao_id")
    private Avaliacao avaliacao;
    private Integer acertos;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataLiberacao;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataLimite;
    private boolean aprovado;
    private boolean finalizada;
    private Double nota;

    @OneToOne
    @JoinColumn(name = "matricula_id")
    private Matricula matricula;

}
