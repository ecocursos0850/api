package com.ecocursos.ecocursos.controllers;

import com.ecocursos.ecocursos.models.Banner;
import com.ecocursos.ecocursos.models.BannerSite;
import com.ecocursos.ecocursos.services.BannerService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(value = "banner")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BannerController {

    private final BannerService service;

    @GetMapping
    public ResponseEntity<List<Banner>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("{id}")
    public ResponseEntity<Banner> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @SneakyThrows
    @PostMapping("{id}/upload")
    public void upload(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        byte[] bytes = file.getBytes();
        Path path = Paths.get("/var/www/html/arquivos/imgs/Banner/" + file.getOriginalFilename());
        Files.write(path, bytes);

        Banner banner = service.listarById(id);
        banner.setCaminhoFoto(file.getOriginalFilename());
        service.alterar(id, banner);
    }
    

    @PutMapping("{id}")
    public ResponseEntity<Banner> listarById(@PathVariable Integer id, @RequestBody Banner banner) {
        return ResponseEntity.ok().body(service.alterar(id, banner));
    }

    @PostMapping
    public ResponseEntity<Banner> salvar(@RequestBody Banner banner) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(banner));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
