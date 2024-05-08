package com.ecocursos.ecocursos.repositories;

import com.ecocursos.ecocursos.models.Pergunta;
import com.ecocursos.ecocursos.models.Simulado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerguntaRepository extends JpaRepository<Pergunta, Integer> {
    List<Pergunta> findAllBySimulado(Simulado simulado);
}
