package com.ecocursos.ecocursos.controllers;

import com.ecocursos.ecocursos.models.AlunoAvaliacao;
import com.ecocursos.ecocursos.services.AlunoAvaliacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(value = "aluno/simulado")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AlunoAvaliacaoController {
    
    private final AlunoAvaliacaoService service;

    @GetMapping
    public ResponseEntity<List<AlunoAvaliacao>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("{id}")
    public ResponseEntity<AlunoAvaliacao> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @GetMapping("matricula/{id}")
    public ResponseEntity<AlunoAvaliacao> listarByMatricula(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarByMatricula(id));
    }

    @PutMapping("matricula/{id}")
    public ResponseEntity<AlunoAvaliacao> alterarByMatricula(@PathVariable Integer id, @RequestBody AlunoAvaliacao  alunoAvaliacao, @RequestParam(name = "usuario") Integer idUsuario) {
        return ResponseEntity.ok().body(service.alterarByMatricula(id, alunoAvaliacao, idUsuario));
    }

    @PostMapping
    public ResponseEntity<AlunoAvaliacao> salvar(@RequestBody AlunoAvaliacao alunoSimulado) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(alunoSimulado));
    }

}
