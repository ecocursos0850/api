package com.ecocursos.ecocursos.controllers;

import com.ecocursos.ecocursos.models.Total;
import com.ecocursos.ecocursos.services.TotalService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("total")
@CrossOrigin("*")
public class TotalController {

    @Autowired
    private TotalService totalService;

    @GetMapping
    public ResponseEntity<Total> listar() {
        return ResponseEntity.ok().body(totalService.listar());
    }

}
