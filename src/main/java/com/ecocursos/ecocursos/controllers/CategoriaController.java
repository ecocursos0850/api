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

import com.ecocursos.ecocursos.models.Categoria;
import com.ecocursos.ecocursos.services.CategoriaService;


@RestController
@RequestMapping(value = "categoria")
@CrossOrigin("*")
public class CategoriaController{
    
    @Autowired
    private CategoriaService service;

    @GetMapping
    public ResponseEntity<List<Categoria>> getAll() {
        return ResponseEntity.ok().body(service.listar());
    }


    @GetMapping("{id}")
    public ResponseEntity<Categoria> getById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @PostMapping
    public ResponseEntity<Categoria> create(@RequestBody Categoria categoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(categoria));
    }

    @PutMapping("{id}")
    public ResponseEntity<Categoria> update(@PathVariable Integer id, @RequestBody Categoria categoria) {
        return ResponseEntity.ok().body(service.alterar(id, categoria));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
