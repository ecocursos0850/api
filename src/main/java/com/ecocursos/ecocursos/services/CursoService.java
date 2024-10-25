package com.ecocursos.ecocursos.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.exceptions.ErrorException;
import com.ecocursos.ecocursos.models.Categoria;
import com.ecocursos.ecocursos.models.Curso;
import com.ecocursos.ecocursos.models.SubCategoria;
import com.ecocursos.ecocursos.models.dtos.CursoDTO;
import com.ecocursos.ecocursos.models.enums.Status;
import com.ecocursos.ecocursos.models.enums.TipoCurso;
import com.ecocursos.ecocursos.repositories.CursoRepository;
import com.ecocursos.ecocursos.repositories.MaterialCursoRepository;

import jakarta.persistence.EntityManager;

@Service
public class CursoService {

    @Autowired
    private CursoRepository repository;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private SubCategoriaService subCategoriaService;

    @Autowired
    private MaterialCursoRepository materialCursoRepository;

    @Autowired
    private EntityManager em;

    public List<Curso> listar() {
        return repository.findAll();
    }

    public List<Curso> listarCursosPorFiltro(
            Integer tipoCurso,
            Categoria categoria,
            SubCategoria subCategoria,
            String nomeCurso,
            Integer page
    ) {
        String query = "select c from Curso c ";
        String condicao = "where";
        if (tipoCurso != null) {
            query += condicao + " c.tipoCurso = :tipoCurso";
            condicao = " and ";
        }
        if (categoria != null) {
            query += condicao + " c.categoria = :categoria";
            condicao = " and ";
        }
        if (subCategoria != null) {
            query += condicao + " c.subCategoria = :subCategoria";
            condicao = " and ";
        }
        if (nomeCurso != null) {
            query += condicao + " c.titulo like CONCAT(:nomeCurso ,'%')";
            condicao = " and ";
        }
        query += " ORDER BY c.status DESC ";

        var q = em.createQuery(query, Curso.class);

        if (tipoCurso != null) {
            q.setParameter("tipoCurso", TipoCurso.toEnum(tipoCurso));
        }
        if (categoria != null) {
            q.setParameter("categoria", categoria);
        }
        if (subCategoria != null) {
            q.setParameter("subCategoria", subCategoria);
        }
        if (nomeCurso != null) {
            q.setParameter("nomeCurso", nomeCurso);
        }

        q.setFirstResult(page * 25);
        q.setMaxResults(25);

        return q.getResultList();
    }

    public List<Curso> listarByFiltro(Integer status, Integer categoria) {
        String query = "SELECT c FROM Curso c ";
        String condicao = " WHERE ";

        if (status != null) {
            query += condicao + " c.status = :status";
            condicao = " AND ";
        }
        if (categoria != null) {
            query += condicao + " c.categoria = :categoria";
            condicao = " AND ";
        }
        var q = em.createQuery(query, Curso.class);

        if (status != null) {
            q.setParameter("status", Status.toEnum(status));
        }
        if (categoria != null) {
            q.setParameter("categoria", categoriaService.listarById(categoria));
        }
        return q.getResultList();
    }

    public List<Curso> listarByCategoria(Integer id) {
        return repository.findAllByCategoriaAndStatus(categoriaService.listarById(id), Status.ATIVO);
    }

    public List<CursoDTO> listarDTO() {
        return repository.listarDTO();
    }

    public Curso listarById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ErrorException("Erro ao buscar curso"));
    }

    public Curso salvar(Curso curso) {
        try {
            curso.setDataCadastro(LocalDateTime.now());
            return repository.save(curso);
        } catch(Exception e) {
            throw new ErrorException("Erro ao cadastrar curso");
        }
    }

    public Curso alterar(Integer id, Curso curso) {
        Curso cursoExistente = listarById(id);
        if (cursoExistente != null) {
            curso.setId(id);
            curso.setDataCadastro(cursoExistente.getDataCadastro());
            if (curso.getCapa() == null) {
                curso.setCapa(cursoExistente.getCapa());
            }
            return repository.saveAndFlush(curso);
        }
        return null;
    }

    public Curso alterarStatus(Integer id) {
        Curso curso = listarById(id);
        if (curso.getStatus() == Status.ATIVO) {
            curso.setStatus(Status.INATIVO);
        } else {
            curso.setStatus(Status.ATIVO);
        }
        return repository.save(curso);
    }

    public List<Curso> listarByStatus(Integer status) {
        try {
            return repository.findAllByStatus(Status.toEnum(status));
        } catch(Exception e) {
            throw new ErrorException("Erro ao listar curso");
        }
    }

    public void deletar(Integer id) {
        Curso curso = listarById(id);
        if (curso != null) {
            repository.delete(curso);
        }
    }

    public List<Curso> listarBySearch(String titulo) {
        return repository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Curso> listarByPagination(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dataCadastro"));
        return repository.findAll(pageable).toList();
    }

    public List<Curso> listarBySubcategoria(Integer id) {
        return repository.findAllBySubCategoriaAndStatus(subCategoriaService.listarById(id), Status.ATIVO);
    }
}
