package com.ecocursos.ecocursos.models;

import com.ecocursos.ecocursos.models.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "desconto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Desconto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String nome;
    private Double valor;

    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @ManyToMany
    @JoinTable(
            name = "desconto_curso",
            joinColumns = @JoinColumn(name = "desconto_id"),
            inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private List<Curso> cursos;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate periodoInicial;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate periodoFinal;

    private boolean global;

}
