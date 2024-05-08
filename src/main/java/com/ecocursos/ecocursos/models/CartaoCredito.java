package com.ecocursos.ecocursos.models;

import lombok.Data;

@Data
public class CartaoCredito {
    
    private String number;
    private String mes;
    private String ano;
    private String ccv;
    private String nomeTitular;

}
