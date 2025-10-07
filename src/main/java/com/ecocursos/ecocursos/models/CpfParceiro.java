package com.ecocursos.ecocursos.models;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cpf_parceiro")
@Data
public class CpfParceiro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String cpf;
    private String email;
    @Column(nullable = true)
    private String nome;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne
    @JoinColumn(name = "parceiro_id")
    private Parceiro parceiro;

    @CreationTimestamp
    @Column(name = "timestamp_insert")
    private LocalDateTime timestampInsert;
}
