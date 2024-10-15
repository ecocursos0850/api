package com.ecocursos.ecocursos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.ecocursos.ecocursos.models.SubCategoria;
import com.ecocursos.ecocursos.services.SubCategoriaService;

@RestController
@RequestMapping(value = "subcategoria")
@CrossOrigin("*")
public class SubCategoriaController {
    
    @Autowired
    private SubCategoriaService service;

    @GetMapping
    public ResponseEntity<List<SubCategoria>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("{id}")
    public ResponseEntity<SubCategoria> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @GetMapping("categoria/{id}")
    public ResponseEntity<List<SubCategoria>> listarByCategoria(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarByCategoria(id));
    }

    @PostMapping
    public ResponseEntity<SubCategoria> salvar(@RequestBody SubCategoria subCategoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(subCategoria));
    }

    @PutMapping("{id}")
    public ResponseEntity<SubCategoria> alterar(@PathVariable Integer id, @RequestBody SubCategoria subCategoria) {
        return ResponseEntity.ok().body(service.alterar(id, subCategoria));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
