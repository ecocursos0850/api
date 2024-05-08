package com.ecocursos.ecocursos.models;

import java.time.LocalDateTime;
import java.util.List;

import com.ecocursos.ecocursos.models.enums.Status;
import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parceiro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parceiro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String nome;
    private String porcentagemDesconto;
    @Column(columnDefinition = "TEXT")
    private byte[] logo;
    private Status status;
    private Boolean isParceiro;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCadastro;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "parceiro", fetch = FetchType.LAZY)
    private List<Aluno> alunos;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "parceiro")
    private List<CpfParceiro> cpfs;

    @Transient
    private Integer totalCpfs;
    @Transient
    private Integer totalAlunos;

    @JsonIncludeProperties({"id", "categoria", "desconto"})
    @OneToMany(mappedBy = "parceiro", cascade = CascadeType.ALL)
    private List<DescontoCategoriaParceiro> descontos;

}
