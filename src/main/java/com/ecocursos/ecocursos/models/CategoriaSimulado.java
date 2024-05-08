package com.ecocursos.ecocursos.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "categoria_simulado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaSimulado {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String descricao;

}
