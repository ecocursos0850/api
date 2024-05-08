package com.ecocursos.ecocursos.repositories;

import com.ecocursos.ecocursos.models.CategoriaSimulado;
import com.ecocursos.ecocursos.models.Simulado;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SimuladoRepository extends JpaRepository<Simulado, Integer> {

    List<Simulado> findAllByCategoriaSimulado(CategoriaSimulado listarById);
}
