package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.EntradaCaixa;
import com.ecocursos.ecocursos.models.enums.TipoEntradaCaixa;
import com.ecocursos.ecocursos.repositories.EntradaCaixaRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EntradaCaixaService {

    private final EntradaCaixaRepository repository;
    private final ParceiroService parceiroService;
    private final AfiliadoService afiliadoService;
    private final EntityManager em;

    private void searchExternal(EntradaCaixa entradaCaixa) {
        if(entradaCaixa.getParceiro() != null) {
            entradaCaixa.setParceiro(parceiroService.listarById(entradaCaixa.getParceiro().getId()));
        }
        if(entradaCaixa.getAfiliado() != null) {
            entradaCaixa.setAfiliado(afiliadoService.listarById(entradaCaixa.getAfiliado().getId()));
        }
    }

    public List<EntradaCaixa> listar() {
        return repository.findAll();
    }

    public List<EntradaCaixa> listarByFiltro(Integer tipo, Integer mes, Integer ano, Integer page) {
        String query = "SELECT e FROM EntradaCaixa e ";
        String condicao = "WHERE";

        if (tipo != null) {
            query += condicao + " e.tipoEntradaCaixa = :tipo";
            condicao = " AND ";
        }
        if (ano != null) {
            query += condicao + " YEAR(e.dataPagamento) = :ano";
            condicao = " AND ";
        }
        if (mes != null) {
            query += condicao + " MONTH(e.dataPagamento) = :mes";
            condicao = " AND ";
        } else {
            query += condicao + " MONTH(e.dataPagamento) = :mes";
            condicao = " AND ";
        }
        if (mes == null && ano == null) {
            query += " AND MONTH(e.dataPagamento) = :mes AND YEAR(e.dataPagamento) = :year";
        }

        var q = em.createQuery(query, EntradaCaixa.class);

        if (tipo != null) {
            q.setParameter("tipo", TipoEntradaCaixa.toEnum(tipo));
        }
        if (ano != null) {
            q.setParameter("ano", ano);
        } else {
            q.setParameter("year", LocalDateTime.now().getYear());
        }
        if (mes != null) {
            q.setParameter("mes", mes);
        } else {
            q.setParameter("mes", LocalDateTime.now().getMonth().getValue());
        }
        q.setFirstResult(page * 25);
        q.setMaxResults(25);

        return q.getResultList();
    }

    public List<EntradaCaixa> listarByRelatorio(
            Integer ano,
            Integer mes,
            Integer tipoEntrada,
            Integer afiliado
    ) {
        String query = "SELECT e FROM EntradaCaixa e";
        String condicao = " WHERE ";

        if (ano != null) {
            query += condicao + " YEAR(e.dataPagamento) = :ano";
            condicao = " AND ";
        }
        if (mes != null) {
            query += condicao + " MONTH(e.dataPagamento) = :mes";
            condicao = " AND ";
        }
        if (tipoEntrada != null) {
            query += condicao + " e.tipoEntrada = :tipo";
            condicao = " AND ";
        }
        if (afiliado != null) {
            query += condicao + " e.afiliado = :afiliado";
            condicao = " AND ";
        }
        var q = em.createQuery(query, EntradaCaixa.class);
        if (mes != null) {
            q.setParameter("mes", mes);
        }
        if (tipoEntrada != null) {
            q.setParameter("tipo", TipoEntradaCaixa.toEnum(tipoEntrada));
        }
        if (ano != null) {
            q.setParameter("ano", ano);
        }
        if (afiliado != null) {
            q.setParameter("afiliado", afiliadoService.listarById(afiliado));
        }
        return q.getResultList();
    }


    public EntradaCaixa listarById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Entrada não encontrada"));
    }

    public EntradaCaixa salvar(EntradaCaixa entradaCaixa) {
        searchExternal(entradaCaixa);
        return repository.save(entradaCaixa);
    }

    public void deletar(Integer id) {
        repository.delete(listarById(id));
    }

    public EntradaCaixa alterar(Integer id, EntradaCaixa entradaCaixa) {
        EntradaCaixa obj = listarById(id);
        entradaCaixa.setId(id);
        return repository.save(entradaCaixa);
    }
}
