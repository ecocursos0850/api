package com.ecocursos.ecocursos.services;

import com.ecocursos.ecocursos.models.Plano;
import com.google.gson.JsonArray;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class PlanoService {

    private final IuguService iuguService;

    public List<Plano> listar() {
        List<Plano> planos = new ArrayList<>();
        try {
            JsonArray jsonObject = iuguService.getAll("plans").get("items").getAsJsonArray();
            jsonObject.forEach(x -> {
                Plano plano = new Plano();
                plano.setNome(x.getAsJsonObject().get("name").getAsString());
                plano.setIdentificador(x.getAsJsonObject().get("identifier").getAsString());
                planos.add(plano);
            });
        } catch (Exception e) {
            log.error("Erro ao buscar planos no IUGU");
        }
        return planos;
    }

}
