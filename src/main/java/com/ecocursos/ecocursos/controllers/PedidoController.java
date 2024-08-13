package com.ecocursos.ecocursos.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecocursos.ecocursos.models.Pedido;
import com.ecocursos.ecocursos.models.dtos.FaturamentoTotal;
import com.ecocursos.ecocursos.models.dtos.PedidosPorEstado;
import com.ecocursos.ecocursos.models.enums.StatusPedido;
import com.ecocursos.ecocursos.models.enums.TipoCurso;
import com.ecocursos.ecocursos.services.PedidoService;

@RestController
@RequestMapping(value = "pedido")
@CrossOrigin("*")
public class PedidoController {
    
    @Autowired
    private PedidoService service;

    @GetMapping
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok().body(service.listar());
    }

    @GetMapping("relatorio")
    public ResponseEntity<List<Pedido>> listarByRelatorio(
            @RequestParam(required = false) Integer aluno,
            @RequestParam(required = false) Integer tipoPagamento,
            @RequestParam(required = false) Integer tipoCheckout,
            @RequestParam(required = false) LocalDateTime periodoInicial,
            @RequestParam(required = false) LocalDateTime periodoFinal,
            @RequestParam(required = false) Integer mes,
            @RequestParam(required = false) Integer ano,
            @RequestParam(required = false) Integer status
    ) {
        return ResponseEntity.ok().body(service.listarByRelatorio(aluno, tipoPagamento  , tipoCheckout, periodoInicial, periodoFinal, mes, ano, status));
    }

    @GetMapping("pagination")
    public ResponseEntity<List<Pedido>> listarBySearch(
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size") Integer size
    ) {
        return ResponseEntity.ok().body(service.listarByPagination(page, size));
    }

    @GetMapping("{id}")
    public ResponseEntity<Pedido> listarById(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarById(id));
    }

    @GetMapping("filtro")
    public ResponseEntity<List<Pedido>> filtro(
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "tipoCurso", required = false) Integer tipoCurso,
            @RequestParam(value = "aluno", required = false) String aluno,
            @RequestParam(value = "periodoInicial", required = false) LocalDateTime periodoInicial,
            @RequestParam(value = "periodoFinal", required = false) LocalDateTime periodoFinal,
            @RequestParam Integer page
            ) {
        return ResponseEntity.ok().body(service.filtro(status, tipoCurso, aluno, periodoInicial, periodoFinal, page));
    }

    @GetMapping("faturamento-total")
    public ResponseEntity<List<FaturamentoTotal>> listarFaturamentoTotal() {
        return ResponseEntity.ok().body(service.faturamentoTotal());
    }

    @GetMapping("pedidos-por-estado")
    public ResponseEntity<List<PedidosPorEstado>> listarPedidosPorEstado() {
        return ResponseEntity.ok().body(service.pedidosPorEstados());
    }

    @GetMapping("aluno/{id}")
    public ResponseEntity<List<Pedido>> listarByAluno(@PathVariable Integer id) {
        return ResponseEntity.ok().body(service.listarByAluno(id));
    }

    @GetMapping("search")
    public ResponseEntity<List<Pedido>> listarBySearch(@RequestParam StatusPedido status, TipoCurso tipoCurso) {
        return ResponseEntity.ok().body(service.listarBySearch(status, tipoCurso));
    }

    @GetMapping("atualizar/{referencia}")
    public ResponseEntity<Void> atualizarPagamento(@PathVariable String referencia) {
        service.atualizarPagamento(referencia);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Pedido> salvar(@RequestBody Pedido pedido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(pedido));
    }

    @PostMapping("cobranca/direta")
    public ResponseEntity<Map<String, String>> gerarCobrancaDireta(@RequestBody Map<String, Object> map) {
        return ResponseEntity.status(HttpStatus.OK).body(service.generateDirectBilling(map));
    }

    // @PostMapping("webhook")
    // public String webhook(@RequestBody Object entity) 
        
    //     return entity;
    // }
    

    @PostMapping("{id}/matricula")
    public ResponseEntity<String> gerarMatriculaByPedido(@PathVariable Integer id) {
        service.criarMatriculaByPedido(id);
        return ResponseEntity.ok().body("Matricula(s) gerada(s) com sucesso");
    }

    @PatchMapping("{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable Integer id) {
        service.cancelarPedido(id);
        return ResponseEntity.noContent().build();
    }

}
