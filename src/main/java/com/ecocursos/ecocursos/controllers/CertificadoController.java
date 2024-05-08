package com.ecocursos.ecocursos.controllers;

import com.azure.core.annotation.Get;
import com.ecocursos.ecocursos.models.Certificado;
import com.ecocursos.ecocursos.services.CertificadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("certificado")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CertificadoController {

    private final CertificadoService service;

    @GetMapping("{id}")
    public ResponseEntity<Certificado> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @GetMapping("aluno/{id}")
    public ResponseEntity<List<Certificado>> listarByAluno(@PathVariable Integer id) {
        return ResponseEntity.ok(service.listarByAluno(id));
    }

}
