package com.ecocursos.ecocursos.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecocursos.ecocursos.models.enums.StatusMatricula;
import com.ecocursos.ecocursos.models.enums.StatusPedido;
import com.ecocursos.ecocursos.models.enums.TipoCurso;
import com.ecocursos.ecocursos.models.enums.TipoPagamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "pedido")
@Data
public class Pedido {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIncludeProperties({"nome", "id", "estado", "horasDisponiveis", "referencia", "email"})
    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @JsonIncludeProperties({"id", "titulo", "categoria", "cargaHoraria", "preco", "tipoCurso", "qtdParcelas"})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "pedido_curso",
        joinColumns = @JoinColumn(name = "pedido_id"),
        inverseJoinColumns = @JoinColumn(name = "curso_id")
    )
    private List<Curso> cursos;

    @Enumerated(EnumType.ORDINAL)
    private StatusPedido status;

    @ElementCollection
    private List<TipoPagamento> tipoPagamentos = new ArrayList<TipoPagamento>();

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT-3")
    private LocalDateTime dataPedido;
    
    private Integer qtdItens;
    
    private String referencia;
    private String codigoTransacao;
    private Double subtotal;
    private Double descontos;

    private Integer cargaHorariaTotal;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = JsonFormat.DEFAULT_TIMEZONE)
    private LocalDateTime dataAtualizacaoTransacao;

    private Double total;

    @Column(columnDefinition = "TEXT")
    private String observacao;
    private String linkPagamento;

    @Enumerated(EnumType.ORDINAL)
    private TipoCurso tipoCurso;

    @Enumerated(EnumType.ORDINAL)
    private StatusMatricula isento;

    private Double taxaMatricula;

    @JsonProperty(access = Access.WRITE_ONLY)
    private Integer totalParcelas;

    @Transient
    private CartaoCredito cartaoCredito;

    @Transient
    private DadosTitularCartao dadosTitularCartao;

    @JsonProperty(access = Access.WRITE_ONLY)
    @JsonIgnoreProperties({"pedido"})
    @OneToMany(mappedBy = "pedido")
    private List<Matricula> matriculas;

    @JsonIgnoreProperties({"pedido"})
    @OneToOne
    @JoinColumn(name = "pedido_pos_graduacao_portal_id")
    private PedidoPosGraduacaoPortal pedidoPosGraduacaoPortal;

    @Transient
    private Boolean isPortal = false;

    public static Map<String, Object> criarPedidoAsaas(Pedido pedido) {
        Map<String, Object> map = new HashMap<>();
        double total = pedido.getTotal() == null ? pedido.getCursos().stream()
                .mapToDouble(Curso::getPreco).sum() : pedido.getTotal();
        if (pedido.getTipoPagamentos().contains(TipoPagamento.CARTAO_CREDITO) && pedido.getCartaoCredito() != null) {
            int totalParcelas = pedido.getTotalParcelas() == null ? pedido.getCursos().stream()
                .mapToInt(curso -> Integer.parseInt(curso.getQtdParcelas())).sum() : pedido.getTotalParcelas();
            map.put("installmentCount", totalParcelas);
            map.put("installmentValue", total / totalParcelas);
            map.put("creditCard", criarCartaoCredito(pedido.getCartaoCredito()));
            map.put("creditCardHolderInfo", criarDadosTitularCartao(pedido.getDadosTitularCartao()));
            map.put("billingType", "UNDEFINED");
        } else {
            map.put("billingType", "UNDEFINED");
        }
        map.put("customer", pedido.getAluno().getReferencia());
        if (pedido.getTipoPagamentos().contains(TipoPagamento.CARTAO_CREDITO)
            && pedido.getCartaoCredito() == null && (pedido.getIsPortal() != null && pedido.getIsPortal())
        ) {
            int totalParcelas = pedido.getTotalParcelas() == null ? pedido.getCursos().stream()
                .mapToInt(curso -> Integer.parseInt(curso.getQtdParcelas())).sum() : pedido.getTotalParcelas();
            map.put("installmentCount", totalParcelas);
            map.put("installmentValue", total / totalParcelas);
            map.put("billingType", "CREDIT_CARD");
        }
        map.put("value", total);
        map.put("discount", criarDescontoPedido(pedido));
        map.put("dueDate", DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().plusDays(3)));
        map.put("description", "Pedido " + pedido.getId());
        return map;
    }

    private static Map<String, Object> criarDescontoPedido(Pedido pedido) {
        Map<String, Object> map = new HashMap<>();
        map.put("value", pedido.getDescontos());
        map.put("type", "FIXED");
        return map;
    }

    private static Map<String, Object> criarCartaoCredito(CartaoCredito cartaoCredito) {
        Map<String, Object> map = new HashMap<>();
        map.put("number", cartaoCredito.getNumber());
        map.put("expiryMonth", cartaoCredito.getMes());
        map.put("expiryYear", cartaoCredito.getAno());
        map.put("ccv", cartaoCredito.getCcv());
        map.put("holderName", cartaoCredito.getNomeTitular());
        return map;
    }
    
    private static Map<String, Object> criarDadosTitularCartao(DadosTitularCartao dadosTitularCartao) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", dadosTitularCartao.getNome());
        map.put("email", dadosTitularCartao.getEmail());
        map.put("cpfCnpj", dadosTitularCartao.getCpfCnpj());
        map.put("postalCode", dadosTitularCartao.getCep());
        map.put("addressNumber", dadosTitularCartao.getNumeroEndereco());
        map.put("phone", dadosTitularCartao.getCelular());
        return map;
    }
}
 
