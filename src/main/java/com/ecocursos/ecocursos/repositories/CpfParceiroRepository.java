package com.ecocursos.ecocursos.repositories;

import com.ecocursos.ecocursos.models.CpfParceiro;
import com.ecocursos.ecocursos.models.Parceiro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CpfParceiroRepository extends JpaRepository<CpfParceiro, Integer> {
    boolean existsByCpf(String cpf);

    CpfParceiro findAllByCpf(String cpf);

    List<CpfParceiro> findAllByParceiro(Parceiro parceiro);

    Integer countByParceiro(Parceiro parceiro);
}
