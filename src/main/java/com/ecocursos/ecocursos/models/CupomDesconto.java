package com.ecocursos.ecocursos.models;

import com.ecocursos.ecocursos.models.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "cupom_desconto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CupomDesconto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String titulo;  
    private String codigo;
    private Double valor;
    private String cupom;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataValidade;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataCadastro;

    @JsonIncludeProperties({"id", "nome"})
    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    private boolean aniversario;

    private Double valorMinimoCurso;

    @Enumerated(EnumType.ORDINAL)
    private Status status;
    @Enumerated(EnumType.ORDINAL)
    private Status limiteUso;

}
