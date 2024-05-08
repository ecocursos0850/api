package com.ecocursos.ecocursos.controllers;

import com.ecocursos.ecocursos.models.AnexoMaterialCurso;
import com.ecocursos.ecocursos.services.AnexoMaterialCursoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("anexo/material/curso")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AnexoMaterialCursoController {

    private final AnexoMaterialCursoService service;

    @GetMapping
    public ResponseEntity<List<AnexoMaterialCurso>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("material/curso/{id}")
    public ResponseEntity<AnexoMaterialCurso> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @PostMapping("material/curso/{id}")
    public ResponseEntity<Map> salvar(@RequestParam("file") MultipartFile file, @PathVariable Integer id) {
        service.salvar(file, id);
        Map map = new HashMap();
        map.put("message", "Upload feito com sucesso");
        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }


}
