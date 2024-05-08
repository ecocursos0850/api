package com.ecocursos.ecocursos.models.dtos;

import com.ecocursos.ecocursos.models.Parceiro;

import lombok.Data;

@Data
public class ParceiroDTO {
    private Integer id;
    private String nome;    

    public static ParceiroDTO toDTO(Parceiro parceiro) {
        ParceiroDTO parceiroDTO = new ParceiroDTO();
        parceiroDTO.setId(parceiro.getId());
        parceiroDTO.setNome(parceiro.getNome());
        return parceiroDTO; 
    }
}
