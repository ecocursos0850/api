package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.exceptions.ErrorException;
import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.Aluno;
import com.ecocursos.ecocursos.models.CupomDesconto;
import com.ecocursos.ecocursos.models.enums.Status;
import com.ecocursos.ecocursos.repositories.CupomDescontoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CupomDescontoService {

    private final CupomDescontoRepository repository;

    private void validarStatus(CupomDesconto cupomDesconto) {
        try {
            if (cupomDesconto.getStatus().equals(Status.INATIVO)) {
                throw new ErrorException("Cupom de desconto está inválido");
            }
        } catch(Exception e) {
            throw new ErrorException(e.getMessage());
        }
    }

    private void validarValidade(CupomDesconto cupomDesconto) {
        try {
            if (LocalDate.now().isAfter(cupomDesconto.getDataValidade())) {
                throw new ErrorException("Cumpom de desconto está com a validade vencida");
            }
        } catch (Exception e) {
            throw new ErrorException(e.getMessage());
        }
    }

    public List<CupomDesconto> listar() {
        return repository.findAll();
    }

    public CupomDesconto gerarCupomDescontoAniversario(Aluno aluno) {
        CupomDesconto cupomDesconto = new CupomDesconto();
        cupomDesconto.setDataCadastro(LocalDate.now());
        cupomDesconto.setCodigo(String.format("ANIVERSARIO%s", aluno.getNome().split(" ")[0].toUpperCase()));
        cupomDesconto.setTitulo("Desconto de aniversario");
        cupomDesconto.setValor(30D);
        cupomDesconto.setStatus(Status.ATIVO);
        cupomDesconto.setDataValidade(cupomDesconto.getDataCadastro());
        cupomDesconto.setLimiteUso(Status.INATIVO);
        return repository.save(cupomDesconto);
    }

    public CupomDesconto listarById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Cupom de desconto não foi encontrado"));
    }

    public CupomDesconto salvar(CupomDesconto cupomDesconto) {
        cupomDesconto.setDataCadastro(LocalDate.now());
        return repository.save(cupomDesconto);
    }

    public CupomDesconto alterar(Integer id, CupomDesconto cupomDesconto) {
        listarById(id);
        cupomDesconto.setId(id);
        cupomDesconto.setDataCadastro(listarById(id).getDataCadastro());
        return repository.save(cupomDesconto);
    }

    public CupomDesconto alterarStatus(Integer id) {
        CupomDesconto cupomDesconto = listarById(id);
        cupomDesconto.setStatus(cupomDesconto.getStatus() == Status.ATIVO ? Status.INATIVO : Status.ATIVO);
        return repository.save(cupomDesconto);
    }

    public void deletar(Integer id) {
        repository.delete(listarById(id));
    }

    public CupomDesconto findByCodigo(String cupom) {
        try {
            CupomDesconto cupomDesconto = repository.findByCodigo(cupom).orElseThrow(() -> new ObjectNotFoundException("Cupom de desconto não encontrado"));
            validarValidade(cupomDesconto);
            validarStatus(cupomDesconto);
            return cupomDesconto;
        } catch (Exception e) {
            throw new ErrorException("Erro ao buscar cupom de desconto por código");
        }
    }

    
}