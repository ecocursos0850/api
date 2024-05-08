package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.models.Afiliado;
import com.ecocursos.ecocursos.models.Categoria;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "comissao_afiliado")
@Data
public class ComissaoAfiliado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double desconto;
    private Double comissao;

    @JsonIncludeProperties({"id"})
    @ManyToOne
    @JoinColumn(name = "afiliado_id")
    private Afiliado afiliado;

    @JsonIncludeProperties({"id", "titulo"})
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

}
