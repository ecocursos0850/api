package com.ecocursos.ecocursos.models;

import com.ecocursos.ecocursos.models.enums.TipoEntradaCaixa;
import com.ecocursos.ecocursos.models.enums.TipoPagamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "entrada_caixa")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntradaCaixa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Double valor;
    @OneToOne
    @JoinColumn(name = "afiliado_id")
    private Afiliado afiliado;
    @OneToOne
    @JoinColumn(name = "parceiro_id")
    private Parceiro parceiro;
    @Enumerated(EnumType.ORDINAL)
    private TipoEntradaCaixa tipoEntradaCaixa;
    @Enumerated(EnumType.ORDINAL)
    private TipoPagamento tipoPagamento;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataPagamento;
    @JsonIncludeProperties({"id", "titulo"})
    @OneToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

}
