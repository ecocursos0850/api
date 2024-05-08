package com.ecocursos.ecocursos.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.repositories.AfiliadoRepository;
import com.ecocursos.ecocursos.repositories.ComissaoAfiliadoRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class ComissaoAfiliadoService {
    
    private final ComissaoAfiliadoRepository repository;
    private final AfiliadoRepository afiliadoRepository;

    public List<ComissaoAfiliado> listarByAfiliado(Integer id) {
        return repository.findAllByAfiliado(afiliadoRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Afiliado não encontrado")));
    }

    public ComissaoAfiliado salvar(ComissaoAfiliado comissaoAfiliado) {
        return repository.save(comissaoAfiliado);
    }

    public ComissaoAfiliado alterar(Integer id, ComissaoAfiliado comissaoAfiliado) {
        repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Comissão do afiliado não foi encontrada"));
        comissaoAfiliado.setId(id);
        return repository.save(comissaoAfiliado);
    };

}
