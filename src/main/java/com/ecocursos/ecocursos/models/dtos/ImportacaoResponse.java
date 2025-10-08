package com.ecocursos.ecocursos.models.dtos;

import lombok.Data;
import java.util.List;

@Data
public class ImportacaoResponse {
    private boolean sucesso;
    private String mensagem;
    private int totalRegistrosProcessados;
    private int registrosInseridos;
    private int registrosIgnorados;
    private int registrosComErro;
    private List<String> erros;
    private List<RegistroImportado> registrosImportados;

    @Data
    public static class RegistroImportado {
        private String cpf;
        private String email;
        private String status;
        private String mensagem;
    }

    public ImportacaoResponse() {
        this.erros = new java.util.ArrayList<>();
        this.registrosImportados = new java.util.ArrayList<>();
    }
}