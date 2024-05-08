package com.ecocursos.ecocursos.services;

import java.time.LocalDate;
import java.util.List;

import com.ecocursos.ecocursos.models.Matricula;
import com.ecocursos.ecocursos.models.enums.StatusAvaliacaoMatricula;
import com.ecocursos.ecocursos.repositories.MatriculaRepository;
import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.exceptions.ErrorException;
import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.AlunoAvaliacao;
import com.ecocursos.ecocursos.repositories.AlunoAvaliacaoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlunoAvaliacaoService {
    
    private final AlunoAvaliacaoRepository repository;
    private final AlunoService alunoService;
    private final AvaliacaoService avaliacaoService;
    private final MatriculaService matriculaService;
    private final CertificadoService certificadoService;

    private void searchExternal(AlunoAvaliacao alunoAvaliacao) {
        alunoAvaliacao.setAvaliacao(avaliacaoService.listarById(alunoAvaliacao.getAvaliacao().getId()));
    }


    public List<AlunoAvaliacao> listar() {
        return repository.findAll();
    }

    public AlunoAvaliacao salvar(AlunoAvaliacao alunoSimulado) {
        if(repository.existsByAluno(alunoService.listarById(alunoSimulado.getAluno().getId())) && alunoSimulado.isAprovado()) {
            throw new ErrorException("Aluno já realizou a avaliação");
        }
        searchExternal(alunoSimulado);
        int dias = alunoSimulado.getAvaliacao().getCurso().getCargaHoraria() / 4;
        alunoSimulado.setDataLiberacao(LocalDate.now().plusDays(dias));
        alunoSimulado.setDataLimite(alunoSimulado.getDataLiberacao().plusDays(90));
        return repository.save(alunoSimulado);
    }

    public AlunoAvaliacao listarById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Simulado do aluno não foi encontrado"));
    }

    public AlunoAvaliacao listarByMatricula(Integer id) {
        return repository.findByMatricula(matriculaService.listarById(id));
    }

    public AlunoAvaliacao alterarByMatricula(Integer id, AlunoAvaliacao alunoAvaliacao) {
        Matricula matricula = matriculaService.listarById(id);
        AlunoAvaliacao alunoEncontradoByMatricula = listarByMatricula(id);
        alunoAvaliacao.setId(alunoEncontradoByMatricula.getId());
        alunoAvaliacao.setMatricula(matricula);
        alunoAvaliacao.setDataLiberacao(alunoEncontradoByMatricula.getDataLiberacao());
        alunoAvaliacao.setDataLimite(alunoEncontradoByMatricula.getDataLimite());
        if(alunoAvaliacao.isAprovado()) {
            matricula.setStatus(StatusAvaliacaoMatricula.APROVADO);
            certificadoService.salvarByMatricula(matricula);
            alunoAvaliacao.setFinalizada(true);
        } else {
            matricula.setStatus(StatusAvaliacaoMatricula.REPROVADO);
            alunoAvaliacao.setFinalizada(true);
        }
        matriculaService.create(matricula);
        return repository.save(alunoAvaliacao);
    }

    public void deletar(Integer id) {
        repository.delete(listarById(id));
    }

}
