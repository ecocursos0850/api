package com.ecocursos.ecocursos.controllers;

import com.ecocursos.ecocursos.models.CupomDesconto;
import com.ecocursos.ecocursos.services.CupomDescontoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "cupom/desconto")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CupomDescontoController {

    private final CupomDescontoService service;

    @GetMapping
    public ResponseEntity<List<CupomDesconto>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("{id}")
    public ResponseEntity<CupomDesconto> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @GetMapping("cupom/{cupom}")
    public ResponseEntity<CupomDesconto> listarByCodigo(@PathVariable String cupom) {
        return ResponseEntity.ok().body(service.findByCodigo(cupom));
    }

    @PostMapping
    public ResponseEntity<CupomDesconto> salvar(@RequestBody CupomDesconto cupomDesconto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(cupomDesconto));
    }

    @PutMapping("{id}")
    public ResponseEntity<CupomDesconto> alterar(@PathVariable Integer id, @RequestBody CupomDesconto cupomDesconto) {
        return ResponseEntity.ok().body(service.alterar(id, cupomDesconto));
    }

    @PatchMapping("{id}")
    public ResponseEntity<CupomDesconto> alterarStatus(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.alterarStatus(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
