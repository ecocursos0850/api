package com.ecocursos.ecocursos.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecocursos.ecocursos.models.Assinatura;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Service
public class AssinaturaService {

    @Autowired
    private IuguService iuguService;

    public Object criarAssinatura(Assinatura assinatura) {
        JsonObject object = iuguService.save("subscriptions", obj(assinatura));
        return object;
    }

    public List<?> listarFaturasByCliente(String cliente) {
        JsonArray jsonObject = iuguService.getAll("subscriptions?customer_id=" + cliente).get("items").getAsJsonArray();
        List<Object> list = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        jsonObject.forEach(x -> {
            x.getAsJsonObject().get("recent_invoices").getAsJsonArray().forEach(object -> {
                map.put("id", object.getAsJsonObject().get("id").getAsString());
                map.put("due_date", object.getAsJsonObject().get("due_date").getAsString());
                map.put("status", object.getAsJsonObject().get("status").getAsString());
                map.put("total", object.getAsJsonObject().get("total").getAsString());
                map.put("secure_url", object.getAsJsonObject().get("secure_url").getAsString());
            });
            list.add(map);
        });
        return list;
    }

    private Map<String, Object> obj(Assinatura assinatura) {
        Map<String, Object> map = new HashMap<>();
        map.put("plan_identifier", assinatura.getIdentificador());
        map.put("customer_id", assinatura.getIdCliente());
        map.put("expires_at", assinatura.getDataExpiracao());
        map.put("only_on_charge_success", false);
        return map;
    }

}
