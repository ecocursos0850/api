package com.ecocursos.ecocursos.controllers;

import com.ecocursos.ecocursos.models.Aluno;
import com.ecocursos.ecocursos.services.AlunoService;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(path = "/aluno")
@CrossOrigin("*")
public class AlunoController {
    
    @Autowired
    private AlunoService service;

    @GetMapping
    public ResponseEntity<List<Aluno>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("search")
    public ResponseEntity<List<Aluno>> listarBySearch(@RequestParam(required = false) String nome, @RequestParam(required = false) String email) {
        return ResponseEntity.ok().body(service.listarBySearch(nome, email));
    }

    @GetMapping("pagination")
    public ResponseEntity<List<Aluno>> listarBySearch(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size) {
        return ResponseEntity.ok().body(service.listarByPagination(page, size));
    }

    @GetMapping("aniversariantes")
    public ResponseEntity<List<Aluno>> listarByAniversariantes(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ) {
        return ResponseEntity.ok().body(service.listarAniversariantes(page, size));
    }

    @PostMapping
    public ResponseEntity<Aluno> salvar(@RequestBody Aluno aluno) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(aluno));
    }

    @GetMapping("{id}")
    public Aluno listarById(@PathVariable Integer id) {
        return service.listarById(id);
    }

    @GetMapping("afiliado/{id}")
    public ResponseEntity<List<Aluno>> listarByAfiliado(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarByAfiliado(id));
    }

    @GetMapping("filtro")
    public ResponseEntity<List<Aluno>> listarByFiltro(
            @RequestParam(value = "parceiro", required = false) String parceiro,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "estado", required = false) String estado,
            @RequestParam(value = "cpf", required = false) String cpf,
            @RequestParam(value = "celular", required = false) String celular,
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam Integer page
     ) {
        return ResponseEntity.ok().body(service.listarByFilter(parceiro, sexo, estado, cpf, nome, email, celular, page));
    }

    @GetMapping("relatorio")
    public ResponseEntity<List<Aluno>> relatorio(
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "parceiro", required = false) String parceiro,
            @RequestParam(value = "sexo", required = false) String sexo,
            @RequestParam(value = "estado", required = false) String estado,
            @RequestParam(value = "dataNascimento", required = false) LocalDateTime dataNascimento,
            @RequestParam(value = "periodoInicial", required = false) LocalDateTime periodoInicial,
            @RequestParam(value = "periodoFinal", required = false) LocalDateTime periodoFinal
    ) {
        return ResponseEntity.ok().body(service.listarByRelatorio(status, parceiro, sexo, estado, dataNascimento, periodoInicial, periodoFinal));
    }

    @PutMapping("{id}")
    public ResponseEntity<Aluno> alterar(@PathVariable Integer id, @RequestBody Aluno aluno) {
        return ResponseEntity.ok().body(service.alterar(id, aluno));
    }

    @PutMapping("{id}/observacao/{observacao}")
    public ResponseEntity<Void> alterarObservacao(@PathVariable Integer id, @PathVariable  String observacao) {
        service.alterarObservacao(id, observacao);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}/status")
    public ResponseEntity<Aluno> alterarStatus(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.alterarStatus(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.ok().body("Deletado com sucesso");
    }

    @DeleteMapping("{referencia}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable String referencia) {
        service.desativar(referencia);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("{referencia}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable String referencia) {
        service.reativar(referencia);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("recuperar")
    public ResponseEntity<Void> recuperarSenha(@RequestBody Map<String, String> map) {        
        service.recuperarSenha(map);
        return ResponseEntity.noContent().build();
    }
    

}
