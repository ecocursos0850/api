package com.ecocursos.ecocursos.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.ecocursos.ecocursos.repositories.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.Afiliado;
import com.ecocursos.ecocursos.models.Curso;
import com.ecocursos.ecocursos.models.enums.StatusAfiliado;
import com.ecocursos.ecocursos.repositories.AfiliadoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AfiliadoService {
    
    private final AfiliadoRepository repository;
    private final ComissaoAfiliadoService comissaoAfiliadoService;
    private final CategoriaService categoriaService;
    private final CursoService cursoService;
    private final UserRepository userRepository;

    public List<Afiliado> listar(int page, int size) {
        Pageable list = PageRequest.of(page, size);
        return repository.findAll(list).toList();
    }

    public Afiliado listarById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Afiliado não encontrado"));
    }

    public boolean existsById(Integer id) {
        return repository.existsById(id);
    }

    public boolean existsByUsuario(Integer id) {
        return repository.existsByUsuario(userRepository.findById(id).get());
    }

    public Afiliado findByEmail(String email) {
        return repository.findByEmail(email).get();
    }

    public Afiliado salvar(Afiliado afiliado) {
        afiliado.setDataCadastro(LocalDate.now());
        Afiliado result = repository.save(afiliado);
        categoriaService.listar().stream().forEach(x -> {
            ComissaoAfiliado comissaoAfiliado = new ComissaoAfiliado();
            comissaoAfiliado.setAfiliado(result);
            comissaoAfiliado.setCategoria(x);
            comissaoAfiliado.setComissao(10D); //valor fixo 10% ?
            comissaoAfiliado.setDesconto(0D);
            comissaoAfiliadoService.salvar(comissaoAfiliado);
        });
        return result;
    }

    public Afiliado alterar(Integer id, Afiliado afiliado) {
        listarById(id);
        afiliado.setId(id);
        return repository.save(afiliado);
    }

    public Afiliado alterarStatus(Integer id, String statusAfiliado) {
        Afiliado afiliado = listarById(id);
        afiliado.setDataLiberacao(LocalDate.now());
        afiliado.setStatus(StatusAfiliado.toEnum(Integer.parseInt(statusAfiliado)));
        return repository.save(afiliado);
    }   

    public void deletar(Integer id) {
        repository.delete(listarById(id));
    }

    public Afiliado listarByNome(String nome) {
        return repository.findByNomeOrNomeFantasiaContainingAllIgnoreCase(nome);
    }

    public String salvarCursosPermitidos(Integer id,List<Curso> cursos) {
        List<Curso> cursosEncontrados = new ArrayList<>();
        Afiliado afiliadoExististente = listarById(id);
        cursos.stream().forEach(x -> {
            if(x.getId().equals(cursoService.listarById(x.getId()).getId())) {
                cursosEncontrados.add(cursoService.listarById(x.getId()));
            }
        });
        afiliadoExististente.getCursos().clear();
        afiliadoExististente.setCursos(cursosEncontrados);
        Afiliado result = repository.save(afiliadoExististente);
        return "Total de " + result.getCursos().size() + " adicionados";
    }

}
