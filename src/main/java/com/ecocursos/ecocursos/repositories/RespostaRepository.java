package com.ecocursos.ecocursos.repositories;

import com.ecocursos.ecocursos.models.Pergunta;
import com.ecocursos.ecocursos.models.Resposta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RespostaRepository extends JpaRepository<Resposta, Integer> {
    List<Resposta> findAllByPergunta(Pergunta listarById);
}
