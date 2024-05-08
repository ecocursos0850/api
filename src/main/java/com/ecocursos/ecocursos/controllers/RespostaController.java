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

import com.ecocursos.ecocursos.models.Resposta;
import com.ecocursos.ecocursos.services.RespostaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "resposta")
@CrossOrigin("*")
@RequiredArgsConstructor
public class RespostaController {

    private final RespostaService service;

    @GetMapping
    public ResponseEntity<List<Resposta>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("{id}")
    public ResponseEntity<Resposta> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @GetMapping("pergunta/{id}")
    public ResponseEntity<List<Resposta>> listarByPergunta(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarByPergunta(id));
    }

    @PostMapping
    public ResponseEntity<Resposta> salvar(@RequestBody Resposta resposta) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(resposta));
    }

    @PutMapping("{id}")
    public ResponseEntity<Resposta> alterar(@PathVariable Integer id, @RequestBody Resposta resposta) {
        return ResponseEntity.ok().body(service.alterar(id, resposta));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
