package com.ecocursos.ecocursos.controllers;

import com.ecocursos.ecocursos.models.Documento;
import com.ecocursos.ecocursos.services.DocumentoService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/documento")
@CrossOrigin("*")
public class DocumentoController {

    private final DocumentoService documentService;
    public DocumentoController(DocumentoService documentService) {
        this.documentService = documentService;
    }
    @PostMapping
    public Documento upload(@RequestParam MultipartFile file) {
        return documentService.upload(file);
    }
}
