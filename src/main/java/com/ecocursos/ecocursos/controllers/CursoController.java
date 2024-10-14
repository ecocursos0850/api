package com.ecocursos.ecocursos.controllers;

import com.ecocursos.ecocursos.models.*;
import com.ecocursos.ecocursos.models.dtos.CursoDTO;
import com.ecocursos.ecocursos.models.enums.TipoCurso;
import com.ecocursos.ecocursos.services.CursoService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(value = "curso")
@CrossOrigin("*")
public class CursoController {
    
    @Autowired
    private CursoService service;

    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("dto")
    public ResponseEntity<List<CursoDTO>> listarDTO() {
        return ResponseEntity.ok().body(service.listarDTO());
    }

    @GetMapping("{id}")
    public ResponseEntity<Curso> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @GetMapping("filtro")
    public ResponseEntity<List<Curso>> listarCursosPorFiltro(
            @RequestParam(required = false) Integer tipoCurso,
            @RequestParam(required = false) Categoria categoria,
            @RequestParam(required = false) SubCategoria subCategoria,
            @RequestParam(required = false) String nomeCurso,
            @RequestParam Integer page
    ) { // Exemplo: página 0, ajuste conforme necessário

        return ResponseEntity.ok().body(service.listarCursosPorFiltro(tipoCurso, categoria, subCategoria, nomeCurso, page));
    }

    @GetMapping("relatorio")
    public ResponseEntity<List<Curso>> relatorio(
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer categoria) {
        return ResponseEntity.ok().body(service.listarByFiltro(status, categoria));
    }

    @GetMapping("status/{status}")
    public ResponseEntity<List<Curso>> listarByMatricula(@PathVariable Integer status) {
        return ResponseEntity.ok().body(service.listarByStatus(status));
    }

    @GetMapping("search")
    public ResponseEntity<List<Curso>> listarBySearch(@RequestParam(name = "nome") String nome) {
        return ResponseEntity.ok().body(service.listarBySearch(nome));
    }

    @GetMapping("pagination")
    public ResponseEntity<List<Curso>> listarByPagination(
            @RequestParam("page") Integer page,
            @RequestParam("size") Integer size
    ) {
        return ResponseEntity.ok().body(service.listarByPagination(page, size));
    }

    @GetMapping("categoria/{id}")
    public ResponseEntity<List<Curso>> listarByCategoria(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarByCategoria(id));
    }

    @GetMapping("subcategoria/{id}")
    public ResponseEntity<List<Curso>>  listarBySubcategoria(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarBySubcategoria(id));
    }

    @PostMapping
    public ResponseEntity<Curso> salvar(@RequestBody Curso curso) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(curso));
    }

    @PutMapping("{id}")
    public ResponseEntity<Curso> alterar(@PathVariable Integer id, @RequestBody Curso curso) {
        return ResponseEntity.ok().body(service.alterar(id, curso));
    }

    @SneakyThrows
    @PostMapping("{id}/upload")
    public ResponseEntity<Void> upload(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        byte[] bytes = file.getBytes();
        Path path = Paths.get("/var/www/html/Cursos/" + file.getOriginalFilename());
        Files.write(path, bytes);

        Curso curso = service.listarById(id);
        curso.setCapa(file.getOriginalFilename());
        service.salvar(curso);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{id}/status")
    public ResponseEntity<Curso> alterarStatus(@PathVariable Integer id){
        return ResponseEntity.ok().body(service.alterarStatus(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
