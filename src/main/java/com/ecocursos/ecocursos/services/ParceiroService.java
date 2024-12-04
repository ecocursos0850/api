package com.ecocursos.ecocursos.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ecocursos.ecocursos.models.Aluno;
import com.ecocursos.ecocursos.models.CpfParceiro;
import com.ecocursos.ecocursos.models.enums.Status;
import com.ecocursos.ecocursos.repositories.AlunoRepository;
import com.ecocursos.ecocursos.repositories.CpfParceiroRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.models.DescontoCategoriaParceiro;
import com.ecocursos.ecocursos.models.Parceiro;
import com.ecocursos.ecocursos.models.dtos.ParceiroDTO;
import com.ecocursos.ecocursos.repositories.ParceiroRepository;

@Service
public class ParceiroService {
    
    @Autowired
    private ParceiroRepository repository;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CpfParceiroRepository cpfParceiroRepository;

    @Autowired
    private DescontoCategoriaParceiroService descontoCategoriaParceiroService;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private EntityManager em;

    private void criarDescontosCategoria(Parceiro parceiro) {
        List<DescontoCategoriaParceiro> list = new ArrayList<>();
        categoriaService.listar().stream().forEach(x -> {
            DescontoCategoriaParceiro descontoCategoriaParceiro = new DescontoCategoriaParceiro();
            descontoCategoriaParceiro.setCategoria(x);
            descontoCategoriaParceiro.setDesconto(0D);
            descontoCategoriaParceiro.setParceiro(parceiro);
            list.add(descontoCategoriaParceiro);
        });
        descontoCategoriaParceiroService.salvarTodosDescontos(list);
    }

    public List<Parceiro> listar() {
        return repository.findAll();
    }

    public List<CpfParceiro> listarByRelatorio(Integer parceiro) {
        String query = "SELECT c FROM CpfParceiro c WHERE c.parceiro.id = :parceiro";
        var q = em.createQuery(query, CpfParceiro.class);
        q.setParameter("parceiro", parceiro);
        return q.getResultList();
    }

    public List<ParceiroDTO> listarDTO() {
        List<ParceiroDTO> list = repository.findAll().stream().map(x -> ParceiroDTO.toDTO(x)).toList();
        return list;
    }

    public List<Parceiro> listarByNome(String nome) {
        String query = "SELECT p FROM Parceiro p WHERE p.nome ILIKE CONCAT('%',:nome,'%')";
        var list = em.createQuery(query, Parceiro.class);
        list.setParameter("nome", nome);
        return list.getResultList();
    }

    public List<Parceiro> listarBySearch(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Parceiro> list = repository.listarByFirstAtivos(pageable);
        list.forEach(x -> {
            x.setTotalAlunos(alunoRepository.countByParceiro(x));
            x.setTotalCpfs(cpfParceiroRepository.countByParceiro(x));
        });
        return list;
    }

    public Parceiro listarById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Parceiro salvar(Parceiro parceiro) {
        parceiro.setDataCadastro(LocalDateTime.now());
        Parceiro result = repository.save(parceiro);
        criarDescontosCategoria(result);
        return result;
    }

    public Parceiro alterar(Integer id, Parceiro parceiro) {
        Parceiro parceiroExistente = listarById(id);
        if (parceiroExistente != null) {
            parceiro.setId(id);
            parceiro.setDataCadastro(parceiroExistente.getDataCadastro());
            return repository.save(parceiro);
        }
        return null;
    }

    public void deletar(Integer id) {
        Parceiro parceiro = listarById(id);
        if (parceiro != null) {
            repository.delete(parceiro);
        }
    }

    public Parceiro alterarStatus(Integer id) {
        Parceiro parceiroExistente = listarById(id);
        parceiroExistente.setStatus(parceiroExistente.getStatus() == Status.ATIVO ? Status.INATIVO : Status.ATIVO);
        return repository.save(parceiroExistente);
    }
}
