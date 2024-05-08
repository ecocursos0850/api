package com.ecocursos.ecocursos.controllers;

import com.azure.core.annotation.Get;
import com.ecocursos.ecocursos.models.EntradaCaixa;
import com.ecocursos.ecocursos.services.EntradaCaixaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "entrada/caixa")
@RequiredArgsConstructor
@CrossOrigin("*")
public class EntradaCaixaController {

    private final EntradaCaixaService service;

    @GetMapping
    public ResponseEntity<List<EntradaCaixa>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntradaCaixa> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @GetMapping("relatorio")
    public ResponseEntity<List<EntradaCaixa>> listarByRelatorio(
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer tipoEntrada,
            @RequestParam(required = false) Integer afiliado
    ) {
        return ResponseEntity.ok().body(service.listarByRelatorio(ano, mes, tipoEntrada, afiliado));
    }

    @GetMapping("filtro")
    public ResponseEntity<List<EntradaCaixa>> listarByFiltro(
            @RequestParam(name = "tipo", required = false) Integer tipo,
            @RequestParam(name = "ano", required = false) Integer ano,
            @RequestParam(name = "mes", required = false) Integer mes,
            @RequestParam(name = "page") Integer page
    ) {
        return ResponseEntity.ok().body(service.listarByFiltro(tipo, mes, ano, page));
    }

    @PostMapping
    public ResponseEntity<EntradaCaixa> salvar(@RequestBody EntradaCaixa entradaCaixa) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(entradaCaixa));
    }

    @PutMapping("{id}")
    public ResponseEntity<EntradaCaixa> alterar(@PathVariable Integer id, @RequestBody EntradaCaixa entradaCaixa) {
        return ResponseEntity.ok().body(service.alterar(id, entradaCaixa));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
