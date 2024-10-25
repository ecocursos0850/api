package com.ecocursos.ecocursos.models.dtos;

public class RelatorioResponse {
    public byte[] pdf;

    public RelatorioResponse() {}

    public byte[] getPdf() {
        return pdf;
    }

    public void setPdf(byte[] pdf) {
        this.pdf = pdf;
    }
}
