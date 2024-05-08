package com.ecocursos.ecocursos.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecocursos.ecocursos.services.ComissaoAfiliado;
import com.ecocursos.ecocursos.services.ComissaoAfiliadoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "comissao/afiliado")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ComissaoAfiliadoController {
    
    private final ComissaoAfiliadoService service;

    @GetMapping("afiliado/{id}")
    public ResponseEntity<List<ComissaoAfiliado>> listarByAfiliado(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarByAfiliado(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<ComissaoAfiliado> alterar(@PathVariable Integer id, @RequestBody ComissaoAfiliado comissaoAfiliado) {
        return ResponseEntity.ok().body(service.alterar(id, comissaoAfiliado));
    }

}
