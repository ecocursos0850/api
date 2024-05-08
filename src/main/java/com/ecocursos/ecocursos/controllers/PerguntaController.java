package com.ecocursos.ecocursos.controllers;

import com.ecocursos.ecocursos.models.Pergunta;
import com.ecocursos.ecocursos.services.PerguntaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "pergunta")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PerguntaController {

    private final PerguntaService service;

    @GetMapping
    public ResponseEntity<List<Pergunta>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("{id}")
    public ResponseEntity<Pergunta> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @GetMapping("simulado/{id}")
    public ResponseEntity<List> listarByPergunta(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarBySimulado(id));
    }

    @PostMapping
    public ResponseEntity<Pergunta> salvar(@RequestBody Pergunta pergunta) {
        return ResponseEntity.ok().body(service.salvar(pergunta));
    }

    @PutMapping("{id}")
    public ResponseEntity<Pergunta> alterar(@PathVariable Integer id, @RequestBody Pergunta pergunta) {
        return ResponseEntity.ok().body(service.alterar(id, pergunta));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
