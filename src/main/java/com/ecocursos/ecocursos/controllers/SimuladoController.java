package com.ecocursos.ecocursos.controllers;

import com.ecocursos.ecocursos.models.Simulado;
import com.ecocursos.ecocursos.services.SimuladoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "simulado")
@CrossOrigin("*")
@RequiredArgsConstructor
public class SimuladoController {

    private final SimuladoService service;

    @GetMapping
    public ResponseEntity<List<Simulado>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("{id}")
    public ResponseEntity<Simulado> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @GetMapping("categoria/simulado/{id}")
    public ResponseEntity<List<Simulado>> listarByCategoriaSimulado(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarByCategoriaSimulado(id));
    } 

    @PostMapping
    public ResponseEntity<Simulado> salvar(@RequestBody Simulado simulado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(simulado));
    }

    @PutMapping("{id}")
    public ResponseEntity<Simulado> alterar(@PathVariable Integer id, @RequestBody Simulado simulado) {
        return ResponseEntity.ok().body(service.alterar(id, simulado));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
