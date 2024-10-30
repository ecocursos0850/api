package com.ecocursos.ecocursos.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecocursos.ecocursos.models.DeclaracaoMatricula;
import com.ecocursos.ecocursos.services.DeclaracaoMatriculaService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(value = "declaracao/matricula")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DeclaracaoMatriculaController {
    
    private final DeclaracaoMatriculaService service;

    @GetMapping
    public ResponseEntity<List<DeclaracaoMatricula>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("pagination")
    public ResponseEntity<List<DeclaracaoMatricula>> listarByPagination(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ) {
        return ResponseEntity.ok().body(service.listarByPagination(page, size));
    }

    @GetMapping("relatorio")
    public ResponseEntity<List<DeclaracaoMatricula>> listarByRelatorio(
            @RequestParam(required = false) Integer curso,
            @RequestParam(required = false) LocalDate dataCadastroInicial,
            @RequestParam(required = false) LocalDate dataCadastroFinal,
            @RequestParam(required = false) Integer status
    ) {
        return ResponseEntity.ok().body(service.listarByRelatorio(curso,dataCadastroInicial, dataCadastroFinal, status));
    }

    @GetMapping("filtro")
    public ResponseEntity<List<DeclaracaoMatricula>> listarByFiltro(
        @RequestParam(required = false, name = "status") Integer status,
        @RequestParam(required = false, name = "nome") String nome,
        @RequestParam Integer page
        ) {
        return ResponseEntity.ok().body(service.listarByFiltro(status, nome, page));
    }

    @GetMapping("aluno/{id}")
    public ResponseEntity<List<DeclaracaoMatricula>> listarByAluno(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarByAluno(id));
    }

    @GetMapping("{id}")
    public ResponseEntity<DeclaracaoMatricula> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @GetMapping("matricula/{id}")
    public ResponseEntity<DeclaracaoMatricula> listarByMatricula(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarByMatricula(id));
    }

    @GetMapping("{id}/gerar-pdf")
    public ResponseEntity<byte[]> gerarDeclaracaoMatriculaPdf(@PathVariable Integer id) {
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=declaracao.pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(service.gerarCertificadoDeclaracao(id));
    }

    @PostMapping
    public ResponseEntity<DeclaracaoMatricula> salvar(@RequestBody DeclaracaoMatricula declaracaoMatricula, @RequestParam(name = "usuario") Integer idUsuario) {
            return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(declaracaoMatricula, idUsuario));
    }

    @SneakyThrows
    @PostMapping("{id}/upload")
    public ResponseEntity<Void> upload(@PathVariable Integer id, @RequestParam("file") MultipartFile file, @RequestParam(name = "usuario") Integer idUsuario) {
        byte[] bytes = file.getBytes();
        String uuidString = UUID.randomUUID().toString() + ".jpg";
        Path path = Paths.get("/var/www/html/Declaracao/" + uuidString);
        Files.write(path, bytes);

        DeclaracaoMatricula declaracaoMatricula = service.listarById(id);
        declaracaoMatricula.setAnexoComprovante(uuidString);
        service.salvar(declaracaoMatricula, idUsuario);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<DeclaracaoMatricula> alterar(@PathVariable Integer id, @RequestBody DeclaracaoMatricula declaracaoMatricula) {
        return ResponseEntity.ok().body(service.alterar(id, declaracaoMatricula));
    }

    @PatchMapping("{id}/aprovar")
    public ResponseEntity<DeclaracaoMatricula> aprovar(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.aprovar(id));
    }

    @PatchMapping("{id}/reprovar")
    public ResponseEntity<Void> reprovar(@PathVariable Integer id) {
        service.reprovar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }


}
