package com.ecocursos.ecocursos.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.exceptions.ErrorException;
import com.ecocursos.ecocursos.models.Categoria;
import com.ecocursos.ecocursos.models.DescontoCategoriaParceiro;
import com.ecocursos.ecocursos.repositories.CategoriaRepository;

@Service
public class CategoriaService {
    
    @Autowired
    private CategoriaRepository repository;

    @Autowired
    private ParceiroService parceiroService;

    @Autowired
    private DescontoCategoriaParceiroService dcpService;

    public List<Categoria> listar() {
        List<Categoria> lista = repository.findAll();
        lista.stream().forEach(x -> x.setCursos(null));
        return lista;
    } 

    public Categoria salvar(Categoria categoria) {
        Categoria obj = repository.save(categoria);
        List<DescontoCategoriaParceiro> list = new ArrayList<>();
        parceiroService.listar().forEach(parceiro -> {
            DescontoCategoriaParceiro descontoCategoriaParceiro = new DescontoCategoriaParceiro();
            descontoCategoriaParceiro.setCategoria(obj);
            descontoCategoriaParceiro.setDesconto(5D);
            descontoCategoriaParceiro.setParceiro(parceiro);
            list.add(descontoCategoriaParceiro);
        });
        dcpService.salvarTodosDescontos(list);
        return repository.save(categoria);
    }

    public Categoria listarById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public Categoria alterar(Integer id, Categoria categoria) {
        Categoria categoriaExistente = listarById(id);
        if (categoriaExistente != null) {
            categoria.setId(categoriaExistente.getId());
            return repository.save(categoria);
        }
        return null;
    }

    public void deletar(Integer id) {
        Categoria categoria = listarById(id);
        if (categoria != null && categoria.getCursos().isEmpty()) {
            repository.delete(categoria);
        } else {
            throw new ErrorException("A categoria tem cursos cadastrados");
        }
    }

}
