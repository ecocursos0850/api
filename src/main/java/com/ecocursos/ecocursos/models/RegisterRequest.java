package com.ecocursos.ecocursos.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String nome;
    private String sobrenome;
    private String email;
    private String password;
    private Role role;
    private Integer id;
}
