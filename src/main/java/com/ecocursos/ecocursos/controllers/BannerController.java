package com.ecocursos.ecocursos.controllers;

import com.ecocursos.ecocursos.models.BannerSite;
import com.ecocursos.ecocursos.services.BannerService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "banner")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BannerController {

    private final BannerService service;

    @GetMapping
    public ResponseEntity<List<BannerSite>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("{id}")
    public ResponseEntity<BannerSite> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @PutMapping("{id}")
    public ResponseEntity<BannerSite> listarById(@PathVariable Integer id, @RequestBody BannerSite banner) {
        return ResponseEntity.ok().body(service.alterar(id, banner));
    }

    @PostMapping
    public ResponseEntity<BannerSite> salvar(@RequestBody BannerSite banner) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(banner));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
