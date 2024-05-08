package com.ecocursos.ecocursos.services;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AlunoServiceTest {

    @Autowired
    private AlunoService alunoService;

    public void diminuirHorasDisponiveisCursoOnline() {

    }

    @Test
    public void testDataAniversarioAluno() {
        alunoService.verificarAniversarioAluno();
    }


}