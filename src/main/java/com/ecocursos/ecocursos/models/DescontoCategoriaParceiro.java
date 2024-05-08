package com.ecocursos.ecocursos.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "desconto_parceiro_categoria")
@Data
public class DescontoCategoriaParceiro {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Double desconto;

    @JsonIgnoreProperties({"cursos", "subCategorias"})
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @JsonIgnoreProperties({"descontos"})
    @ManyToOne
    @JoinColumn(name = "parceiro_id")
    private Parceiro parceiro;
}
