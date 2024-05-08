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

import com.azure.core.annotation.Patch;
import com.ecocursos.ecocursos.models.AvaliacaoResposta;
import com.ecocursos.ecocursos.services.AvaliacaoRespostaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "avaliacao/resposta")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AvaliacaoRespostaController {
    
    private final AvaliacaoRespostaService service;

    @GetMapping
    public ResponseEntity<List<AvaliacaoResposta>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("{id}")
    public ResponseEntity<AvaliacaoResposta> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @GetMapping("pergunta/{id}")
    public ResponseEntity<List<AvaliacaoResposta>> listarByPergunta(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarByPergunta(id));
    }

    @PostMapping
    public ResponseEntity<AvaliacaoResposta> salvar(@RequestBody AvaliacaoResposta avaliacaoResposta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(avaliacaoResposta));
    }

    @PutMapping("{id}")
    public ResponseEntity<AvaliacaoResposta> alterar(@PathVariable Integer id, @RequestBody AvaliacaoResposta avaliacaoResposta) {
        return ResponseEntity.ok().body(service.alterar(id, avaliacaoResposta));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
