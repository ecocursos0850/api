package com.ecocursos.ecocursos.controllers;

import com.ecocursos.ecocursos.models.VariavelGlobal;
import com.ecocursos.ecocursos.repositories.VariavelGlobalRepository;
import com.ecocursos.ecocursos.services.VariavelGlobalService;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Var;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "variavel/global")
@RequiredArgsConstructor
@CrossOrigin("*")
public class VariavelGlobalController {

    private final VariavelGlobalService service;

    @GetMapping
    public ResponseEntity<List<VariavelGlobal>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("{id}")
    public ResponseEntity<VariavelGlobal> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @PostMapping
    public ResponseEntity<VariavelGlobal> salvar(@RequestBody VariavelGlobal variavelGlobal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(variavelGlobal));
    }

    @PutMapping("{id}")
    public ResponseEntity<VariavelGlobal> alterar(@PathVariable Integer id, @RequestBody VariavelGlobal variavelGlobal) {
        return ResponseEntity.ok().body(service.alterar(id, variavelGlobal));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
