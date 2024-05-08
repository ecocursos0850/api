package com.ecocursos.ecocursos.models;

import lombok.Data;

@Data
public class DadosTitularCartao {
    
    private String nome;
    private String email;
    private String cpfCnpj;
    private String cep;
    private String numeroEndereco;
    private String celular;

}
