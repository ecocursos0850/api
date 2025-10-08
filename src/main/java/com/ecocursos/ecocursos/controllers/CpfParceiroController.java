package com.ecocursos.ecocursos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ecocursos.ecocursos.models.CpfParceiro;
import com.ecocursos.ecocursos.models.dtos.ImportacaoResponse; 
import com.ecocursos.ecocursos.services.CpfParceiroService;

@RestController
@RequestMapping(value = "cpf/parceiro")
@CrossOrigin("*")
public class CpfParceiroController {

    @Autowired
    private CpfParceiroService service;

    @GetMapping("parceiro/{id}")
    public ResponseEntity<List<CpfParceiro>> listarByParceiro(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarByParceiro(id));
    }

    @GetMapping("search")
    public ResponseEntity<List<CpfParceiro>> listarByParceiro(
            @RequestParam("page") Integer page,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam("parceiro") Integer parceiro
            ) {
        return ResponseEntity.ok().body(service.listarBySearch(page, size, parceiro));
    }

    @PostMapping("parceiro/{id}")
    public ResponseEntity<ImportacaoResponse> salvar(@RequestParam("file") MultipartFile file, @PathVariable Integer id) {
        ImportacaoResponse response = service.salvar(file, id);
        
        if (response.isSucesso()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }


}
