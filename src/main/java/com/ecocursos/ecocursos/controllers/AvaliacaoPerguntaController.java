package com.ecocursos.ecocursos.controllers;

import java.util.List;

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
import org.springframework.web.bind.annotation.RestController;

import com.ecocursos.ecocursos.models.AvaliacaoPergunta;
import com.ecocursos.ecocursos.services.AvaliacaoPerguntaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "avaliacao/pergunta")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AvaliacaoPerguntaController {
    
    private final AvaliacaoPerguntaService service;

    @GetMapping
    public ResponseEntity<List<AvaliacaoPergunta>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("{id}")
    public ResponseEntity<AvaliacaoPergunta> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @GetMapping("avaliacao/{id}")
    public ResponseEntity<List<AvaliacaoPergunta>> listarByAvaliacao(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarByAvaliacao(id));
    }

    @PostMapping
    public ResponseEntity<AvaliacaoPergunta> salvar(@RequestBody AvaliacaoPergunta avaliacaoPergunta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(avaliacaoPergunta));
    }

    @PutMapping("{id}")
    public ResponseEntity<AvaliacaoPergunta> alterar(@PathVariable Integer id, @RequestBody AvaliacaoPergunta avaliacaoPergunta) {
        return ResponseEntity.ok().body(service.alterar(id, avaliacaoPergunta));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}   
