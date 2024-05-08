package com.ecocursos.ecocursos.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.ecocursos.ecocursos.models.enums.Status;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "categoria")
@Data
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String titulo;

    @JsonIgnoreProperties({"matriculas", "taxaMatricula", "materiais", "categoria"})
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private List<Curso> cursos;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @JsonIncludeProperties({"id", "titulo"})
    @OneToMany(mappedBy = "categoria")
    private List<SubCategoria> subCategorias = new ArrayList<>();

//    public List<Curso> getCursos() {
//        return cursos.stream().filter(x -> x.getStatus().equals(Status.ATIVO)).collect(Collectors.toSet()).stream().toList();
//    }
}
