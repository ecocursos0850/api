package com.ecocursos.ecocursos.controllers;

import com.ecocursos.ecocursos.models.TaxaMatricula;
import com.ecocursos.ecocursos.services.TaxaMatriculaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "taxa/matricula")
@CrossOrigin("*")
public class TaxaMatriculaController {

    @Autowired
    private TaxaMatriculaService service;

    @GetMapping
    public ResponseEntity<List<TaxaMatricula>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("{id}")
    public ResponseEntity<TaxaMatricula> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @PostMapping
    public ResponseEntity<TaxaMatricula> salvar(@RequestBody TaxaMatricula taxaMatricula) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(taxaMatricula));
    }

    @PutMapping("{id}")
    public ResponseEntity<TaxaMatricula> alterar(@PathVariable Integer id, @RequestBody TaxaMatricula taxaMatricula) {
        return ResponseEntity.ok().body(service.alterar(id, taxaMatricula));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
