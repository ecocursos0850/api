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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecocursos.ecocursos.models.CpfParceiro;
import com.ecocursos.ecocursos.models.Parceiro;
import com.ecocursos.ecocursos.models.dtos.ParceiroDTO;
import com.ecocursos.ecocursos.services.ParceiroService;


@RestController
@RequestMapping(value = "parceiro")
@CrossOrigin("*")
public class ParceiroController {
    
    @Autowired
    private ParceiroService service;

    @GetMapping
    public ResponseEntity<List<Parceiro>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("search")
    public ResponseEntity<List<Parceiro>> listarBySearch(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size) {
        return ResponseEntity.ok().body(service.listarBySearch(page, size));
    }

    @GetMapping("/nome/{nome}")
    public ResponseEntity<List<Parceiro>> listarByNome(@PathVariable String nome) {
        return ResponseEntity.ok().body(service.listarByNome(nome));
    }

    @GetMapping("relatorio")
    public ResponseEntity<List<CpfParceiro>> listarByRelatorio(
            @RequestParam(required = true) Integer parceiro
    ) {
        return ResponseEntity.ok().body(service.listarByRelatorio(parceiro));
    }

    @GetMapping("{id}")
    public ResponseEntity<Parceiro> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @GetMapping("dto")
    public ResponseEntity<List<ParceiroDTO>> listarDTO() {
        return ResponseEntity.ok().body(service.listarDTO());
    }
    

    @PostMapping
    public ResponseEntity<Parceiro> salvar(@RequestBody Parceiro parceiro) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(parceiro));
    }

    @PutMapping("{id}/status")
    public ResponseEntity<Parceiro> alterarStatus(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.alterarStatus(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<Parceiro> alterar(@PathVariable Integer id, @RequestBody Parceiro parceiro) {
        return ResponseEntity.ok().body(service.alterar(id, parceiro));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
