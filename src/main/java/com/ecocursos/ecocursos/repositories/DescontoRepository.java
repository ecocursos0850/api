package com.ecocursos.ecocursos.repositories;

import com.ecocursos.ecocursos.models.Desconto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DescontoRepository extends JpaRepository<Desconto, Integer> {
}
