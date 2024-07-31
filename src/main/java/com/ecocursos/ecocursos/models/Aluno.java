package com.ecocursos.ecocursos.models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecocursos.ecocursos.models.enums.Status;
import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "aluno")
@Data
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String nome;
    @Enumerated(EnumType.ORDINAL)
    private Status status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataCadastro;
    private String email;
    private String sexo;
    private String cpf;
    private String rg;
    private String estadoCivil;
    private String orgaoEmissor;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataExpedicao;
    private String nomePai;
    private String nomeMae;
    private String naturalidade;
    private String faculdade;
    private String anoConclusao;
    private String telefoneFixo;
    private String idade;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dataNascimento;
    private String celular;
    private boolean receberEmail;
    private String cep;
    private String logradouro;
    private String cidade;
    private String estado;
    private String bairro;
    private String numero;
    private String referencia;
    private String complemento;
    private boolean emailAniversario;
    private Integer tentativas;

    @JsonIncludeProperties({ "id", "nome", "porcentagemDesconto", "logo", "isParceiro"})
    @ManyToOne
    @JoinColumn(nullable = true, name = "parceiro_id")
    private Parceiro parceiro;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "afiliado_id", nullable = true)
    private Afiliado afiliado;

    @JsonIgnore
    @OneToMany(mappedBy = "aluno")
    private List<Pedido> pedidos;

    @JsonIncludeProperties({"firstname", "lastname"})
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    @JsonIgnore
    @OneToMany(mappedBy = "aluno")
    private List<Matricula> matriculas;

    private Integer horasDisponiveis;

    @JsonProperty(namespace = "senha", access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @Column(columnDefinition = "TEXT")
    private String observacao;

    public static Map<String, Object> convertToAsaas(Aluno aluno) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", aluno.getNome());
            map.put("cpfCnpj", aluno.getCpf() );
            map.put("email", aluno.getEmail());
            map.put("phone", aluno.getCelular());
            map.put("address", aluno.getLogradouro());
            map.put("addressNumber", aluno.getNumero());
            map.put("complement", aluno.getComplemento());
            map.put("province", aluno.getCidade());
            map.put("postalCode", aluno.getCep());
            map.put("notificationDisabled", "true");
            map.put("additionalEmails", aluno.getEmail());
            return map;
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
