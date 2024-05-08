package com.ecocursos.ecocursos.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "variavel_global")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariavelGlobal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String chave;

    @Column(columnDefinition = "TEXT")
    private String valor;
}
