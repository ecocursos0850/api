package com.ecocursos.ecocursos.repositories;

import com.ecocursos.ecocursos.models.Categoria;
import com.ecocursos.ecocursos.models.SubCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Integer> {

    List<SubCategoria> findAllByCategoria(Categoria categoria);

}
