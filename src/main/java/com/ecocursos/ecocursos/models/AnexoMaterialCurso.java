package com.ecocursos.ecocursos.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "anexo_material_curso")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnexoMaterialCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String caminho;

    @ManyToOne
    @JoinColumn(name = "material_curso_id")
    private MaterialCurso materialCurso;

}
