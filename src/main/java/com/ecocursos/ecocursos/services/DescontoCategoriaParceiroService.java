package com.ecocursos.ecocursos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.models.DescontoCategoriaParceiro;
import com.ecocursos.ecocursos.repositories.DescontoCategoriaParceiroRepository;

@Service
public class DescontoCategoriaParceiroService {

    @Autowired
    private DescontoCategoriaParceiroRepository repository;

    @Autowired
    private ParceiroService parceiroService;

    private void buscarExternos(DescontoCategoriaParceiro descontoCategoriaParceiro) {
        descontoCategoriaParceiro.setParceiro(parceiroService.listarById(descontoCategoriaParceiro.getParceiro().getId()));
    }

    public List<DescontoCategoriaParceiro> listarByParceiro(Integer idParceiro) {
        return repository.findAllByParceiro(parceiroService.listarById(idParceiro));
    }

    public DescontoCategoriaParceiro listarById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public void salvarTodosDescontos(List<DescontoCategoriaParceiro> descontoCategoriaParceiros) {
        descontoCategoriaParceiros.stream().forEach(x -> {
            repository.save(x);
        });
    }

}
