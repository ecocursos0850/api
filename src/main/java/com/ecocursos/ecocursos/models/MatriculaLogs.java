package com.ecocursos.ecocursos.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "matricula_logs")
@Data
public class MatriculaLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @JsonIncludeProperties({"id", "curso"})
    @ManyToOne
    @JoinColumn(name = "matricula_id")
    private Matricula matricula;

    @JsonIncludeProperties({"id", "firstname"})
    @OneToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    private String descricao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern ="yyyy-MM-dd")
    private LocalDate data;

    public MatriculaLogs() {}
    
}
