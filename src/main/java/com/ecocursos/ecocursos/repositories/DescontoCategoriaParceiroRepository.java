package com.ecocursos.ecocursos.repositories;

import com.ecocursos.ecocursos.models.DescontoCategoriaParceiro;
import com.ecocursos.ecocursos.models.Parceiro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DescontoCategoriaParceiroRepository extends JpaRepository<DescontoCategoriaParceiro, Integer> {
    List<DescontoCategoriaParceiro> findAllByParceiro(Parceiro parceiro);
}
