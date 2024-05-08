package com.ecocursos.ecocursos.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.gson.JsonObject;

@SpringBootTest
public class AsaasServiceTest {
    
    @Autowired private AsaasService service;

    @Test
    void testIntegracaoAsaas() {
        JsonObject object = service.getAll("customers");
        assertEquals(true, object != null);
    }
}
