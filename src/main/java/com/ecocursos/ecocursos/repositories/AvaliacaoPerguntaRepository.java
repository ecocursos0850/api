package com.ecocursos.ecocursos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecocursos.ecocursos.models.Avaliacao;
import com.ecocursos.ecocursos.models.AvaliacaoPergunta;

public interface AvaliacaoPerguntaRepository extends JpaRepository<AvaliacaoPergunta, Integer>{

    List<AvaliacaoPergunta> findAllByAvaliacao(Avaliacao avaliacao);
    
}
