package com.ecocursos.ecocursos.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface IController<T> {

    @GetMapping
    public ResponseEntity<List<T>> getAll();
    @GetMapping("{id}")
    public ResponseEntity<T> getById(@PathVariable Integer id);
    @PostMapping
    public ResponseEntity<T> create(T object);
    @PutMapping("{id}")
    public ResponseEntity<T> update(@PathVariable Integer id, @RequestBody T object);
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id);

}
