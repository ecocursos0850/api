package com.ecocursos.ecocursos.repositories;

import com.ecocursos.ecocursos.models.Documento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentoRepository extends JpaRepository<Documento, Long> {
}
