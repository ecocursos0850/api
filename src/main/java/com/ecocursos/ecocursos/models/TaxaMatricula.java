package com.ecocursos.ecocursos.models;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "taxa_matricula")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxaMatricula {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String titulo;
    private Double valor;

    @JsonIgnoreProperties({"cursos"})
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime dataCadastro;

    @OneToMany(mappedBy = "taxaMatricula")
    private List<Curso> cursos;

}
