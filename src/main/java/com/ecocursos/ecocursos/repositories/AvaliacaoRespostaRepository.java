package com.ecocursos.ecocursos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecocursos.ecocursos.models.AvaliacaoPergunta;
import com.ecocursos.ecocursos.models.AvaliacaoResposta;

public interface AvaliacaoRespostaRepository extends JpaRepository<AvaliacaoResposta, Integer>{
    List<AvaliacaoResposta> findAllByPergunta(AvaliacaoPergunta pergunta);
}
