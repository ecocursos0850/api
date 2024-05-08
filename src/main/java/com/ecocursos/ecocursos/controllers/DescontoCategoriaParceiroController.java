package com.ecocursos.ecocursos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecocursos.ecocursos.models.DescontoCategoriaParceiro;
import com.ecocursos.ecocursos.services.DescontoCategoriaParceiroService;

@RestController
@RequestMapping(value = "desconto/categoria/parceiro")
@CrossOrigin("*")
public class DescontoCategoriaParceiroController {

    @Autowired
    private DescontoCategoriaParceiroService service;

    @GetMapping("{id}")
    private ResponseEntity<DescontoCategoriaParceiro> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @GetMapping("parceiro/{id}")
    public ResponseEntity<List<DescontoCategoriaParceiro>> listarByParceiro(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarByParceiro(id));
    }

    @PostMapping
    public ResponseEntity<Void> salvarTodosDescontos(@RequestBody List<DescontoCategoriaParceiro> descontoCategoriaParceiros) {
        service.salvarTodosDescontos(descontoCategoriaParceiros);
        return ResponseEntity.noContent().build();
    }

}
