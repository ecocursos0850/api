package com.ecocursos.ecocursos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecocursos.ecocursos.models.Avaliacao;
import com.ecocursos.ecocursos.services.AvaliacaoService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping(value = "avaliacao")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AvaliacaoController {
    
    private final AvaliacaoService service;

    @GetMapping
    public ResponseEntity<List<Avaliacao>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("{id}")
    public ResponseEntity<Avaliacao> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @GetMapping("curso/{id}")
    public ResponseEntity<Avaliacao> listarByCurso(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.getByCurso(id));
    }
    

    @GetMapping("pagination")
    public ResponseEntity<List<Avaliacao>> listarByPagination(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ) {
        return ResponseEntity.ok().body(service.listaByPagination(page, size));
    }

    @GetMapping("filter")
    public ResponseEntity<List<Avaliacao>> listarByFilter(
            @RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "page") Integer page
    ) {
        return ResponseEntity.ok().body(service.listarByFilter(nome, page));
    }

    @PostMapping
    public ResponseEntity<Avaliacao> salvar(@RequestBody Avaliacao avaliacao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(avaliacao));
    }

    @PutMapping("{id}")
    public ResponseEntity<Avaliacao> alterar(@PathVariable Integer id, @RequestBody Avaliacao avaliacao) {
        return ResponseEntity.ok().body(service.alterar(id, avaliacao));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
