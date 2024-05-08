package com.ecocursos.ecocursos.repositories;

import com.ecocursos.ecocursos.models.VariavelGlobal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface    VariavelGlobalRepository extends JpaRepository<VariavelGlobal, Integer> {
    VariavelGlobal findByChave(String chave);
}
