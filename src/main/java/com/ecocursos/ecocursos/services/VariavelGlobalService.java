package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.VariavelGlobal;
import com.ecocursos.ecocursos.repositories.VariavelGlobalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VariavelGlobalService {

    private final VariavelGlobalRepository repository;

    public List<VariavelGlobal> listar() {
        return repository.findAll();
    }

    public VariavelGlobal listarById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Variavel Global não encontrada"));
    }

    public VariavelGlobal listarByChave(String chave) {
        return repository.findByChave(chave);
    }

    public VariavelGlobal salvar(VariavelGlobal variavelGlobal) {
        return repository.save(variavelGlobal);
    }

    public VariavelGlobal alterar(Integer id, VariavelGlobal variavelGlobal) {
        listarById(id);
        variavelGlobal.setId(id);
        return repository.save(variavelGlobal);
    }

    public void deletar(Integer id) {
        repository.delete(listarById(id));
    }

}
