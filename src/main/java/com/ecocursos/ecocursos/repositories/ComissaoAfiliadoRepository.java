package com.ecocursos.ecocursos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecocursos.ecocursos.models.Afiliado;
import com.ecocursos.ecocursos.services.ComissaoAfiliado;

public interface ComissaoAfiliadoRepository extends JpaRepository<ComissaoAfiliado, Integer> {
    
    List<ComissaoAfiliado> findAllByAfiliado(Afiliado afiliado);

}
