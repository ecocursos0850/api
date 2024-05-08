package com.ecocursos.ecocursos.models;

import com.ecocursos.ecocursos.models.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pedido_pos_graduacao_portal")
@Data
public class PedidoPosGraduacaoPortal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    private String login;
    private String senha;
    private String urlPortal;

    @Enumerated(EnumType.ORDINAL)
    private Status status;
}
