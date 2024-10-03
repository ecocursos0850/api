package com.ecocursos.ecocursos.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecocursos.ecocursos.models.dtos.RelatorioResponse;
import com.ecocursos.ecocursos.services.RelatorioService;



@RestController
@RequestMapping("relatorios")
@CrossOrigin("*")
public class RelatorioController {
    
    @Autowired
    private RelatorioService service;

    @PostMapping("matricula")
    public ResponseEntity<RelatorioResponse> matricula(@RequestBody Map<String, Object> param) {
        RelatorioResponse relatorioResponse = RelatorioResponse.builder()
            .pdf(service.gerarPdfRelatorio(param, "src/main/resources/relatorios/matricula.jrxml"))
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(relatorioResponse);
    }

    @PostMapping("parceiro")
    public ResponseEntity<RelatorioResponse> parceiro(@RequestBody Map<String, Object> param) {
        RelatorioResponse relatorioResponse = RelatorioResponse.builder()
            .pdf(service.gerarPdfRelatorio(param, "src/main/resources/relatorios/matricula.jrxml"))
            .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(relatorioResponse);
    }
    

}
