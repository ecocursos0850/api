package com.ecocursos.ecocursos.controllers;

import com.ecocursos.ecocursos.models.PedidoPosGraduacaoPortal;
import com.ecocursos.ecocursos.services.PedidoPosGraduacaoPortalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pedido/pos/graduacao/portal")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PedidoPosGraduacaoPortalController {

    private final PedidoPosGraduacaoPortalService service;

    @PatchMapping("{id}/aprovar")
    public ResponseEntity<Void> aprovar(@PathVariable Integer id) {
        service.aprovar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<PedidoPosGraduacaoPortal> alterar(@PathVariable Integer id, @RequestBody PedidoPosGraduacaoPortal pedidoPosGraduacaoPortal) {
        return ResponseEntity.ok().body(service.alterar(pedidoPosGraduacaoPortal, id));
    }

}
