package com.ecocursos.ecocursos.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecocursos.ecocursos.models.AlunoAvaliacao;
import com.ecocursos.ecocursos.models.Curso;
import com.ecocursos.ecocursos.models.Matricula;
import com.ecocursos.ecocursos.models.dtos.MatriculaMes;
import com.ecocursos.ecocursos.models.enums.StatusAvaliacaoMatricula;
import com.ecocursos.ecocursos.services.MatriculaService;

@RestController
@RequestMapping(value = "matricula")
@CrossOrigin("*")
public class MatriculaController {
    
    @Autowired
    private MatriculaService service;

    @GetMapping
    public ResponseEntity<List<Matricula>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("search")
    public ResponseEntity<List> listarBySearch(
                @RequestParam(name = "page") Integer page,
                @RequestParam(name = "size") Integer size,
                @RequestParam(name = "afiliado", required = false) Integer afiliado
    ) {
            return ResponseEntity.ok().body(service.listarBySearch(page, size, afiliado));
    }

    @GetMapping("mes")
    public ResponseEntity<List> listarByMes(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size
            ) {
        return ResponseEntity.ok().body(service.listarByMes(page, size));
    }

    @GetMapping("matricula-mes")
    public ResponseEntity<List<MatriculaMes>> listarMatriculaMes() {
        return ResponseEntity.ok().body(service.listarMatriculaMes());
    }

    @GetMapping("afiliado/{id}/matricula-mes")
    public ResponseEntity<List<MatriculaMes>> listarByMesCurso(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarMatriculaMesAfiliado(id));
    }

    @GetMapping("afiliado/{id}/matricula-mes-curso-livre")
    public ResponseEntity<List<MatriculaMes>> listar(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarByMesCurso(id));
    }


    @GetMapping("minhas-vendas")
    public ResponseEntity<List<Matricula>> listarMatriculaMes(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size,
            @RequestParam(name = "ano", required = false) Integer ano,
            @RequestParam(name = "mes", required = false) Integer mes,
            @RequestParam(name = "afiliado") Integer afiliado
    ) {
        return ResponseEntity.ok().body(service.vendasByAfiliado(page, size, ano, mes, afiliado));
    }

    @GetMapping("relatorio")
    public ResponseEntity<List<Matricula>> listarByRelatorio(
            @RequestParam(required = false) Integer tipoCurso,
            @RequestParam(required = false) Integer categoria,
            @RequestParam(required = false) Integer curso,
            @RequestParam(required = false) Integer parceiro,
            @RequestParam(required = false) LocalDateTime dataCadastroInicial,
            @RequestParam(required = false) LocalDateTime dataCadastroFinal,
            @RequestParam(required = false) LocalDateTime dataLiberacaoInicial,
            @RequestParam(required = false) LocalDateTime dataLiberacaoFinal,
            @RequestParam(required = false) LocalDateTime dataCertificadoInicial,
            @RequestParam(required = false) LocalDateTime dataCertificadoFinal,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer status
    ) {
        return ResponseEntity.ok().body(service.listarByRelatorio(tipoCurso, categoria, curso, parceiro, dataCadastroInicial, dataCadastroFinal, dataLiberacaoInicial, dataLiberacaoFinal, dataCertificadoInicial, dataCertificadoFinal, estado, status));
    }

    @GetMapping("relatorio-pdf")
    public ResponseEntity<Map<String, Object>> gerarRelatorioPdf(
        @RequestParam(required = false) String curso,
        @RequestParam(required = false) String parceiro,    
        @RequestParam(required = false) String aluno,
        @RequestParam(required = false) String dataInicio,
        @RequestParam(required = false) String dataFinal
    ) {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> requests = new HashMap<>();
        if (curso != null) requests.put("curso", curso);
        if (parceiro != null) requests.put("parceiro", parceiro);
        if (aluno != null) requests.put("aluno", aluno);
        if (dataInicio != null) requests.put("dataInicio", dataInicio);
        if (dataFinal != null) requests.put("dataFinal", dataFinal);

        response.put("pdf", service.gerarPdfRelatorio(requests));
        return ResponseEntity.ok().body(response);
    }
    

    @GetMapping("cursos-mais-vendidos")
    public ResponseEntity<List<Curso>> cursosMaisVendidos() {
        return ResponseEntity.ok().body(service.cursosMaisVendidos());
    }

    @GetMapping("filtro")
    public ResponseEntity<List<Matricula>> listarByFiltro(
        @RequestParam(name = "status", required = false) Integer status,
        @RequestParam(name = "nome", required = false) String nome,
        @RequestParam(name = "curso", required = false) String curso,
        @RequestParam(name = "periodoInicial", required = false) LocalDateTime periodoInicial,
        @RequestParam(name = "periodoFinal", required = false) LocalDateTime periodoFinal,
        @RequestParam(name = "afiliado", required = false) String afiliado,
        @RequestParam(name = "page", required = false) Integer page
        ) {
        return ResponseEntity.ok().body(service.listarByFiltro(status, nome, curso, periodoInicial, periodoFinal, afiliado, page));
    }
    

    @GetMapping("{id}")
    public ResponseEntity<Matricula> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @GetMapping("{id}/certificado")
    public ResponseEntity<byte[]>  gerarCertificadoByMatricula(@PathVariable Integer id) {
//        service.gerarCertificadoByMatricula(id);
//        return ResponseEntity.ok().body("Certificado gerado com successo");
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=certificado.pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(service.gerarCertificadoByMatricula(id));
    }

    @GetMapping("aluno/{id}")
    public ResponseEntity<List<Matricula>> listarByAluno(
        @PathVariable Integer id,
        @RequestParam(name = "page") Integer page
        ) {
        return ResponseEntity.ok().body(service.listarByAluno(id, page));
    }

    @GetMapping("afiliado/{id}")
    public ResponseEntity<List<Matricula>> listarByAfiliado(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarByAfiliado(id));
    }

    
    @PostMapping
    public ResponseEntity<Matricula> salvar(@RequestBody Matricula matricula) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(matricula));
    }

    @PostMapping("{id}/finalizar/avaliacao")
    public ResponseEntity<Void> finalizarAvaliacao(@RequestBody AlunoAvaliacao alunoAvaliacao, @PathVariable Integer id, @RequestParam(name = "usuario") Integer idUsuario) {
        service.finalizarAvaliacao(alunoAvaliacao, id, idUsuario);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Matricula> alterar(@PathVariable Integer id, @RequestBody Matricula matricula) {
        return ResponseEntity.ok().body(service.alterar(id, matricula));
    }

    @PutMapping("{id}/status/{status}")
    public ResponseEntity<Matricula> atualizarStatus(@PathVariable Integer id, @PathVariable StatusAvaliacaoMatricula status) {
        return ResponseEntity.ok().body(service.atualizarStatus(id, status));
    }

    @PutMapping("{id}/dataLiberacao/{dataLiberacao}")
    public ResponseEntity<Matricula> atualizarDataLiberacao(@PathVariable Integer id, @PathVariable LocalDateTime dataLiberacao) {
        return ResponseEntity.ok().body(service.atualizarDataLiberacao(id, dataLiberacao));
    }

    @PutMapping("{id}/dataMatricula/{dataMatricula}")
    public ResponseEntity<Matricula> atualizarDataMatricula(@PathVariable Integer id, @PathVariable LocalDateTime dataMatricula) {
        return ResponseEntity.ok().body(service.atualizarDataMatricula(id, dataMatricula));
    }

    @PutMapping("{id}/alterar/curso/{idCurso}/usuario/{idUsuario}")
    public ResponseEntity<Void> atualizarMatriculaCurso(@PathVariable Integer id, @PathVariable Integer idCurso, @PathVariable Integer idUsuario) {
        service.alterarCurso(id, idCurso, idUsuario);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
