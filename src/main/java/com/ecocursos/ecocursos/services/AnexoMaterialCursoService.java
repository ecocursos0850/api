package com.ecocursos.ecocursos.services;

import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ecocursos.ecocursos.exceptions.ObjectNotFoundException;
import com.ecocursos.ecocursos.models.AnexoMaterialCurso;
import com.ecocursos.ecocursos.models.enums.TipoMaterial;
import com.ecocursos.ecocursos.repositories.AnexoMaterialCursoRepository;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@Service
@RequiredArgsConstructor
public class AnexoMaterialCursoService {

    private final AnexoMaterialCursoRepository repository;
    private final FtpService ftpService;
    private final MaterialCursoService materialCursoService;

    public List<AnexoMaterialCurso> listar() {
        return repository.findAll();
    }

    public List<AnexoMaterialCurso> listarByCurso(Integer id) {
        return repository.findAllByMaterialCurso(materialCursoService.listarById(id));
    }

    public AnexoMaterialCurso listarById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException("Anexo do material do curso não encontrado"));
    }

    @SneakyThrows
    public void salvar(MultipartFile file, Integer id) {
        AnexoMaterialCurso anexoMaterialCurso = new AnexoMaterialCurso();
        anexoMaterialCurso.setMaterialCurso(materialCursoService.listarById(id));
        String arquivo = "";
        String nomeCodificado = Base64.getEncoder().encode(file.getName().getBytes()).toString();
        if(anexoMaterialCurso.getMaterialCurso().getTipoMaterial() == TipoMaterial.PDF) {
            anexoMaterialCurso.setCaminho("/public_html/arquivos/" + nomeCodificado + ".pdf");
            arquivo = "/public_html/arquivos/" + nomeCodificado + file.getContentType();
            ftpService.subirArquivo(arquivo, file);
            repository.save(anexoMaterialCurso);
        }
    }

    public void deletar(Integer id) {
        repository.delete(listarById(id));
    }

}
