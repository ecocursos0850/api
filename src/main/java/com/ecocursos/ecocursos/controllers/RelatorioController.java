package com.ecocursos.ecocursos.controllers;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecocursos.ecocursos.models.Aluno;
import com.ecocursos.ecocursos.models.dtos.RelatorioResponse;
import com.ecocursos.ecocursos.services.AlunoService;
import com.ecocursos.ecocursos.services.RelatorioService;

@RestController
@RequestMapping("relatorios")
@CrossOrigin("*")
public class RelatorioController {
    
    @Autowired
    private RelatorioService service;

    @Autowired
    private AlunoService alunoService;

    @PostMapping("aluno")
    public ResponseEntity<RelatorioResponse> aluno(@RequestBody Map<String, Object> params) {
        try {
            System.out.println("Parâmetros recebidos: " + params);
            
            // Extrair e converter parâmetros
            Integer status = params.get("status") != null ? Integer.valueOf(params.get("status").toString()) : null;
            Integer parceiro = params.get("parceiro") != null ? Integer.valueOf(params.get("parceiro").toString()) : null;
            String sexo = params.get("sexo") != null ? params.get("sexo").toString() : null;
            String estado = params.get("estado") != null ? params.get("estado").toString() : null;
            
            // Buscar alunos com os filtros
            List<Aluno> alunos = alunoService.listarByRelatorio(
                status, 
                parceiro != null ? parceiro.toString() : null, 
                sexo, 
                estado, 
                null, null, null
            );
            
            System.out.println("Alunos encontrados: " + alunos.size());
            
            // Preparar parâmetros para o JasperReports
            Map<String, Object> jasperParams = new HashMap<>();
            jasperParams.put("ALUNOS", alunos);
            jasperParams.put("TITULO", "Relatório de Alunos");
            
            // Adicionar informações dos filtros aplicados
            if (status != null) {
                jasperParams.put("FILTRO_STATUS", status == 1 ? "ATIVO" : "INATIVO");
            }
            if (parceiro != null) {
                jasperParams.put("FILTRO_PARCEIRO", "Parceiro ID: " + parceiro);
            }
            if (sexo != null) {
                jasperParams.put("FILTRO_SEXO", sexo);
            }
            
            byte[] pdf = service.gerarPdfRelatorio(jasperParams, "src/main/resources/relatorios/aluno.jrxml");
            
            RelatorioResponse relatorioResponse = new RelatorioResponse();
            relatorioResponse.setPdf(pdf);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(relatorioResponse);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Manter os outros métodos existentes...
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