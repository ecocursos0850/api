package com.ecocursos.ecocursos.services;

import java.util.Arrays;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import com.ecocursos.ecocursos.exceptions.ErrorException;
import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.SneakyThrows;

@Service
public class AsaasService {

    private static final String token = "$aact_YTU5YTE0M2M2N2I4MTliNzk0YTI5N2U5MzdjNWZmNDQ6OjAwMDAwMDAwMDAwMDAzNTcwNTg6OiRhYWNoXzdjMTc0YTFmLTczZmQtNGY0Zi04Y2I1LTE3YzhlMTkwZDBjMg==";
    private static final String url = "https://api.asaas.com/v3";

    public JsonObject getAll(String url) {
        try {
            String list = WebClient.create().get()
                    .uri(this.url + "/" + url)
                    .headers(header -> {
                        header.set("access_token", token);
                        header.setContentType(MediaType.APPLICATION_JSON);
                    })
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            JsonElement element = JsonParser.parseString(list);
            JsonObject json = element.getAsJsonObject();
            return json;
        } catch (WebClientException e) {
            throw new ErrorException("Erro na integração com método de pagamento - GET");
        }
    }

    @SneakyThrows
    public JsonObject save(String url, Object object) {
        try {
            final Gson gson = Converters.registerLocalDateTime(new GsonBuilder()).create();
            System.out.println(gson.toJson(object));
            String webClient = WebClient.builder().build().post()
                    .uri(this.url + "/" + url)
                    .headers(header -> {
                        header.set("access_token", token);
                        header.set("User-Agent", "Ecocursos");
                        header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                        header.setContentType(MediaType.APPLICATION_JSON);
                    })
                    .body(BodyInserters.fromValue(gson.toJson(object)))
                    .retrieve()
                    .bodyToMono(String.class).block();
            JsonElement element = JsonParser.parseString(webClient);
            JsonObject json = element.getAsJsonObject();
            return json;
        } catch (Exception e) {
            System.out.println(e.getMessage()); 
            e.getMessage();
        }
        return null;
    }

    public JsonObject getBy(String url) {
        String object = WebClient.builder().build().get()
                .uri(this.url + "/" + url)
                .headers(header -> {
                    header.set("access_token", token);
                    header.set("User-Agent", "Ecocursos");
                    header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                    header.setContentType(MediaType.APPLICATION_JSON);
                })
                .retrieve().bodyToMono(String.class).block();
        JsonElement element = JsonParser.parseString(object);
        JsonObject json = element.getAsJsonObject();
        return json;
    }

    public JsonObject desativar(String url) {
        try {
            String list = WebClient.create().delete()
                    .uri(this.url + "/" + url)
                    .headers(header -> {
                        header.set("access_token", token);
                        header.set("User-Agent", "Ecocursos");
                        header.setContentType(MediaType.APPLICATION_JSON);
                    })
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            JsonElement element = JsonParser.parseString(list);
            JsonObject json = element.getAsJsonObject();
            return json;
        } catch (WebClientException e) {
            throw new ErrorException("Erro na integração com método de pagamento - DELETE");
        }
    }

    public JsonObject reativar(String url) {
        try {
            String list = WebClient.create().post()
                    .uri(this.url + "/" + url)
                    .headers(header -> {
                        header.set("access_token", token);
                        header.set("User-Agent", "Ecocursos");
                        header.setContentType(MediaType.APPLICATION_JSON);
                    })
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            JsonElement element = JsonParser.parseString(list);
            JsonObject json = element.getAsJsonObject();
            return json;
        } catch (WebClientException e) {
            throw new ErrorException("Erro na integração com método de pagamento - REATIVAÇÃOS - POST");
        }
    }

}
