package com.ecocursos.ecocursos.repositories;

import java.util.List;

import com.ecocursos.ecocursos.models.Categoria;
import com.ecocursos.ecocursos.models.SubCategoria;
import com.ecocursos.ecocursos.models.dtos.CursoDTO;
import com.ecocursos.ecocursos.models.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ecocursos.ecocursos.models.Curso;

public interface CursoRepository extends JpaRepository<Curso, Integer>{

    List<Curso> findByTituloContainingIgnoreCase(String titulo);

    List<Curso> findAllByCategoriaAndStatus(Categoria categoria, Status status);

    List<Curso> findAllByStatus(Status status);

    List<Curso> findAllBySubCategoriaAndStatus(SubCategoria subCategoria, Status status);

    @Query(value = """
            SELECT  c.id as id, c.titulo as nome FROM curso c
            """, nativeQuery = true)
    List<CursoDTO> listarDTO();
}
