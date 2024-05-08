package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.exceptions.ErrorException;
import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.Desconto;
import com.ecocursos.ecocursos.repositories.DescontoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DescontoService {

    private final DescontoRepository repository;

    private void verificaGlobal(Desconto desconto) {
        if(desconto.getCursos().isEmpty()) {
            listar().forEach(x -> {
                if(x.isGlobal()) {
                    throw new ErrorException("Já possui um desconto global");
                } else {
                    desconto.setGlobal(true);
                }
            });
        }
    }

    public List<Desconto> listar() {
        return repository.findAll();
    }

    public Desconto listarById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Desconto não foi encontrado"));
    }

    public Desconto salvar(Desconto desconto) {
        verificaGlobal(desconto);
        return repository.save(desconto);
    }

    public Desconto alterar(Integer id, Desconto desconto) {
        listarById(id);
        desconto.setId(id);
        verificaGlobal(desconto);
        return repository.save(desconto);
    }

    public void deletar(Integer id) {
        repository.delete(listarById(id));
    }

}
