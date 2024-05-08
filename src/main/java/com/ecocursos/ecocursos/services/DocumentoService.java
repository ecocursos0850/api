package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.models.Documento;
import com.ecocursos.ecocursos.repositories.DocumentoRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class DocumentoService {

    private final DocumentoRepository
            documentRepository;
    public DocumentoService(DocumentoRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @SneakyThrows
    public Documento upload(MultipartFile file) {
        Documento document = new Documento();
        document.setNome(file.getOriginalFilename());
        document.setData(file.getBytes());
        document.setDataCriacao(LocalDateTime.now());
        return documentRepository.save(document);
    }

}
