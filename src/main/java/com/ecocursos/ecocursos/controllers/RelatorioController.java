package com.ecocursos.ecocursos.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecocursos.ecocursos.models.dtos.RelatorioResponse;
import com.ecocursos.ecocursos.services.RelatorioService;



@RestController
@RequestMapping("relatorios")
@CrossOrigin("*")
public class RelatorioController {
    
    @Autowired
    private RelatorioService service;

    @PostMapping("matriculas")
    public ResponseEntity<RelatorioResponse> matricula(@RequestBody Map<String, Object> param) {
        RelatorioResponse relatorioResponse = new RelatorioResponse();
        relatorioResponse.setPdf(service.gerarPdfRelatorio(param, "src/main/resources/relatorios/matricula.jrxml"));
        return ResponseEntity.status(HttpStatus.CREATED).body(relatorioResponse);
    }

    @PostMapping("parceiros")
    public ResponseEntity<RelatorioResponse> parceiro(@RequestBody Map<String, Object> param) {
        RelatorioResponse relatorioResponse = new RelatorioResponse();
        relatorioResponse.setPdf(service.gerarPdfRelatorio(param, "src/main/resources/relatorios/parceiro.jrxml"));
        return ResponseEntity.status(HttpStatus.CREATED).body(relatorioResponse);
    }

    @PostMapping("declaracoes")
    public ResponseEntity<RelatorioResponse> declaracoes(@RequestBody Map<String, Object> param) {
        RelatorioResponse relatorioResponse = new RelatorioResponse();
        relatorioResponse.setPdf(service.gerarPdfRelatorio(param, "src/main/resources/relatorios/declaracao.jrxml"));
        return ResponseEntity.status(HttpStatus.CREATED).body(relatorioResponse);
    }

    @PostMapping("aluno")
    public ResponseEntity<RelatorioResponse> aluno(@RequestBody Map<String, Object> param) {
        RelatorioResponse relatorioResponse = new RelatorioResponse();
        relatorioResponse.setPdf(service.gerarPdfRelatorio(param, "src/main/resources/relatorios/aluno.jrxml"));
        return ResponseEntity.status(HttpStatus.CREATED).body(relatorioResponse);
    }

    @PostMapping("financeiro")
    public ResponseEntity<RelatorioResponse> financeiro(@RequestBody Map<String, Object> param) {
        RelatorioResponse relatorioResponse = new RelatorioResponse();
        relatorioResponse.setPdf(service.gerarPdfRelatorio(param, "src/main/resources/relatorios/financeiro.jrxml"));
        return ResponseEntity.status(HttpStatus.CREATED).body(relatorioResponse);
    }

    @PostMapping("vendas")
    public ResponseEntity<RelatorioResponse> vendas(@RequestBody Map<String, Object> param) {
        RelatorioResponse relatorioResponse = new RelatorioResponse();
        relatorioResponse.setPdf(service.gerarPdfRelatorio(param, "src/main/resources/relatorios/vendas.jrxml"));
        return ResponseEntity.status(HttpStatus.CREATED).body(relatorioResponse);
    }

    @PostMapping("aluno_parceiro_matriculas")
    public ResponseEntity<RelatorioResponse> alunoParceiroMatriculas(@RequestBody Map<String, Object> param) {
        RelatorioResponse relatorioResponse = new RelatorioResponse();
        relatorioResponse.setPdf(service.gerarPdfRelatorio(param, "src/main/resources/relatorios/aluno_parceiro_matriculas.jrxml"));
        return ResponseEntity.status(HttpStatus.CREATED).body(relatorioResponse);
    }
    

}
