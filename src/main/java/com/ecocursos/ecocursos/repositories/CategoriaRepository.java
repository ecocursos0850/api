package com.ecocursos.ecocursos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecocursos.ecocursos.models.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer>{
    
}
