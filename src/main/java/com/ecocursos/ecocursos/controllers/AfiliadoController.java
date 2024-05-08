package com.ecocursos.ecocursos.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecocursos.ecocursos.models.Afiliado;
import com.ecocursos.ecocursos.models.Curso;
import com.ecocursos.ecocursos.services.AfiliadoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "afiliado")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AfiliadoController {
    
    private final AfiliadoService service;

    @GetMapping
    public ResponseEntity<List<Afiliado>> listar(@RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size) {
        return ResponseEntity.ok().body(service.listar(page, size));
    }

    @GetMapping("{id}")
    public ResponseEntity<Afiliado> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @GetMapping("nome/{nome}")
    public ResponseEntity<Afiliado> listarByNome(@PathVariable String nome) {
        return ResponseEntity.ok().body(service.listarByNome(nome));
    }

    @PostMapping
    public ResponseEntity<Afiliado> salvar(@RequestBody Afiliado afiliado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(afiliado));
    }

    @PutMapping("{id}/curso") ResponseEntity<String> salvarCursosPermitidos(@PathVariable Integer id, @RequestBody List<Curso> cursos) {
        return ResponseEntity.ok().body(service.salvarCursosPermitidos(id ,cursos));
    }

    @PutMapping("{id}")
    public ResponseEntity<Afiliado> alterar(@PathVariable Integer id, @RequestBody Afiliado afiliado) {
        return ResponseEntity.ok().body(service.alterar(id, afiliado));
    }

    @PatchMapping("{id}/status")
    public ResponseEntity<Afiliado> alterarStatus(@PathVariable Integer id ,@RequestParam(name = "status") String statusAfiliado) {
        return ResponseEntity.ok().body(service.alterarStatus(id, statusAfiliado));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
