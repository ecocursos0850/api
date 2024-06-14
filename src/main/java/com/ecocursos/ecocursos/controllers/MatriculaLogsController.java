package com.ecocursos.ecocursos.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecocursos.ecocursos.models.MatriculaLogs;
import com.ecocursos.ecocursos.services.MatriculaLogsService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping(value = "matricula/logs")
@RequiredArgsConstructor
public class MatriculaLogsController {
    
    private final MatriculaLogsService service;

    @GetMapping("matricula/{id}")
    public ResponseEntity<List<MatriculaLogs>> listarByMatricula(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarByMatricula(id));
    }
    

}
