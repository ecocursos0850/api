package com.ecocursos.ecocursos.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

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
import net.lingala.zip4j.ZipFile;

@RestController
@RequestMapping(value = "certificado/ssl")
@CrossOrigin("*")
public class CertificadoSSLController {

    @SneakyThrows
    @PostMapping("{id}/upload")
    public ResponseEntity<Void> upload(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        try {
            File tempFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            
            
            InputStream  inputStream = file.getInputStream();
            FileOutputStream outputStream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int bytesRead;

            while((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            
            ZipFile zipFile = new ZipFile(tempFile);
            
            zipFile.extractAll("/var/www/html/certificadosssl");
           
            return ResponseEntity.noContent().build();
        } catch(Exception e) {
            throw new ErrorException("Erro ao subir imagem");
        }
        
    }
    
}
