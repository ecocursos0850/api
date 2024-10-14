package com.ecocursos.ecocursos.models.dtos;

import lombok.Builder;

@Builder
public record RelatorioResponse(byte[] pdf) {
    
}
