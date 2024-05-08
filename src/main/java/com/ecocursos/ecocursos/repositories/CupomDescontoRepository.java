package com.ecocursos.ecocursos.repositories;

import com.ecocursos.ecocursos.models.CupomDesconto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CupomDescontoRepository extends JpaRepository<CupomDesconto, Integer> {
    Optional<CupomDesconto> findByCodigo(String string);
}
