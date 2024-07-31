package com.ecocursos.ecocursos.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecocursos.ecocursos.exceptions.ErrorException;

import lombok.SneakyThrows;

@RestController
@RequestMapping(value = "certificado/ssl")
@CrossOrigin("*")
public class CertificadoSSLController {

    @SneakyThrows
    @PostMapping("{id}/upload")
    public ResponseEntity<Void> upload(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get("/var/www/html/certificadosssl/" + file.getOriginalFilename());
            Files.write(path, bytes);

            return ResponseEntity.noContent().build();
        } catch(Exception e) {
            throw new ErrorException("Erro ao subir imagem");
        }
        
    }
    
}
