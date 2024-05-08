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

import com.ecocursos.ecocursos.models.Assinatura;
import com.ecocursos.ecocursos.services.AssinaturaService;

@RestController
@RequestMapping(value = "assinaturas")
@CrossOrigin("*")
public class AssinaturaController {

    @Autowired
    private AssinaturaService service;

    @GetMapping("{id}")
    public ResponseEntity<Object> listar(@PathVariable String id) {
        return ResponseEntity.ok().body(service.listarFaturasByCliente(id));
    } 

    @PostMapping
    public ResponseEntity<Void> salvar(@RequestBody Assinatura assinatura) {
        service.criarAssinatura(assinatura);
        return ResponseEntity.noContent().build();
    }

}
