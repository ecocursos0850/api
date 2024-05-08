package com.ecocursos.ecocursos.controllers;

import com.azure.core.annotation.Get;
import com.ecocursos.ecocursos.models.Desconto;
import com.ecocursos.ecocursos.services.DescontoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "desconto")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DescontoController {

    private final DescontoService service;

    @GetMapping
    public ResponseEntity<List<Desconto>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("{id}")
    public ResponseEntity<Desconto> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @PostMapping
    public ResponseEntity<Desconto> salvar(@RequestBody Desconto desconto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(desconto));
    }

    @PutMapping("{id}")
    public ResponseEntity<Desconto> alterar(@PathVariable Integer id, @RequestBody Desconto desconto) {
        return ResponseEntity.ok().body(service.alterar(id, desconto));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
