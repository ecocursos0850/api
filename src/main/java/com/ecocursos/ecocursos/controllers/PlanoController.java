package com.ecocursos.ecocursos.controllers;

import com.ecocursos.ecocursos.models.Plano;
import com.ecocursos.ecocursos.services.PlanoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("planos")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PlanoController {

    private final PlanoService service;

    @GetMapping
    public ResponseEntity<List<Plano>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

}
