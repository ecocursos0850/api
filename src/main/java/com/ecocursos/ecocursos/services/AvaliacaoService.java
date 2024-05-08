package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.Avaliacao;
import com.ecocursos.ecocursos.models.Curso;
import com.ecocursos.ecocursos.repositories.AlunoAvaliacaoRepository;
import com.ecocursos.ecocursos.repositories.AvaliacaoRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {
    
    private final AvaliacaoRepository repository;
    private final CursoService cursoService;
    private final AlunoAvaliacaoRepository alunoAvaliacaoRepository;
    private final EntityManager em;

    public List<Avaliacao> listar() {
        return repository.findAll();
    }

    public List<Avaliacao> listarByFilter(String nome, Integer page) {
        String query = "SELECT a FROM Avaliacao a ";
        String condicao = " WHERE ";

        if (nome != null) {
            query += condicao + " a.titulo LIKE CONCAT('%',:nome, '%')";
            condicao = " AND ";
        }
        var q = em.createQuery(query, Avaliacao.class);
        if (nome != null) {
            q.setParameter("nome", nome);
        }
        q.setFirstResult(page * 25);
        q.setMaxResults(25);
        return q.getResultList();
    }

    public Avaliacao listarById(@PathVariable Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Avaliação não encontrada"));
    }

    public List<Avaliacao> listaByPagination(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).toList();
    }

    public Avaliacao salvar(Avaliacao avaliacao) {
        Avaliacao obj = repository.save(avaliacao);
        return obj;
    }


    public Avaliacao getByCurso(Integer id) {
        return repository.findByCurso(cursoService.listarById(id));
    }

    public Avaliacao alterar(Integer id, Avaliacao avaliacao) {
        listarById(id);
        avaliacao.setId(id);
        return repository.save(avaliacao);
    }

    public void  deletar(Integer id) {
        alunoAvaliacaoRepository.findAll().forEach(x -> {
            if (x.getAvaliacao() != null) {
                if(x.getAvaliacao().getId().equals(id)) {
                    alunoAvaliacaoRepository.delete(x);
                }
            }
        });
        repository.delete(listarById(id));
    }

}
