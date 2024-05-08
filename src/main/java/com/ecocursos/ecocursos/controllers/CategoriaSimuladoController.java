    package com.ecocursos.ecocursos.controllers;

import com.ecocursos.ecocursos.models.CategoriaSimulado;
import com.ecocursos.ecocursos.services.CategoriaSimuladoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "categoria/simulado")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CategoriaSimuladoController {

    private final CategoriaSimuladoService service;

    @GetMapping
    public ResponseEntity<List<CategoriaSimulado>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoriaSimulado> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @PostMapping
    public ResponseEntity<CategoriaSimulado> salvar(@RequestBody CategoriaSimulado categoriaSimulado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(categoriaSimulado));
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoriaSimulado> alterar(@PathVariable Integer id, @RequestBody CategoriaSimulado categoriaSimulado) {
        return ResponseEntity.ok().body(service.alterar(id, categoriaSimulado));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }


}
