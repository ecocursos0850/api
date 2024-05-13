package com.ecocursos.ecocursos.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.List;

import java.io.IOException;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecocursos.ecocursos.models.MaterialCurso;
import com.ecocursos.ecocursos.services.MaterialCursoService;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "material/curso")
@CrossOrigin("*")
public class MaterialCursoController {
    
    @Autowired
    private MaterialCursoService service;

    @GetMapping
    private ResponseEntity<List<MaterialCurso>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("curso/{id}")
    private ResponseEntity<List<MaterialCurso>> listarByCurso(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarByCurso(id));
    }

    @GetMapping("{id}")
    private ResponseEntity<MaterialCurso> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @PostMapping
    private ResponseEntity<MaterialCurso> salvar(@RequestBody MaterialCurso materialCurso) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(materialCurso));
    }

    @PutMapping("{id}")
    public ResponseEntity<MaterialCurso> alterar(@PathVariable Integer id, @RequestBody MaterialCurso materialCurso) {
        return ResponseEntity.ok().body(service.alterar(id, materialCurso));
    }

    @SneakyThrows
    @PostMapping("{id}/upload")
    public ResponseEntity<Void> upload(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            
            // Gerando um nome de arquivo único usando UUID
            String randomFileName = UUID.randomUUID().toString().replace("-", "") + fileExtension;
            
            Path path = Paths.get("/var/www/html/Materiais/" + randomFileName);
            Files.write(path, bytes);
            
            MaterialCurso materialCurso = service.listarById(id);
            materialCurso.setLink(randomFileName);
            service.salvar(materialCurso);
            
            return ResponseEntity.noContent().build();
        } catch (IOException e) {
            // Tratar exceção de IO, se necessário
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
