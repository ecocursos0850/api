    package com.ecocursos.ecocursos.services;

    import java.util.Arrays;
import java.util.Base64;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.SneakyThrows;

    @Component
    public class IuguService {

        private static final String TOKEN = "2B78254AE7B3F2D57489B93992FD9A75AFE6C690932018FF7E20C54FD7A11B60";
        private static final String TOKEN_PROD = "2D67DA310F9AD1F74F2AA066403F72C710EE2ADC3F9C531EA42360A3C80342D7";

        private static final String TOKEN_CRIPTO = Base64.getEncoder().encodeToString(TOKEN_PROD.getBytes());
        private static final String URL = "https://api.iugu.com/v1/";

        @SneakyThrows
        public JsonObject getAll(String url) {
            String list = WebClient.builder().build().get()
            .uri(URL + "/" + url)
            .headers(header -> {
                header.setBasicAuth(TOKEN_CRIPTO);
                header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                header.setContentType(MediaType.APPLICATION_JSON);
            })
            .retrieve().bodyToMono(String.class).block();
            JsonElement element = JsonParser.parseString(list);
            JsonObject jsonObject = element.getAsJsonObject();
            JsonObject json = jsonObject.getAsJsonObject();
            return json;
        }

        public JsonObject getBy(String url) {
            String object = WebClient.builder().build().get()
                    .uri(URL + "/" + url)
                    .headers(header -> {
                        header.setBasicAuth(TOKEN_CRIPTO);
                        header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                        header.setContentType(MediaType.APPLICATION_JSON);
                    })
                    .retrieve().bodyToMono(String.class).block();
            JsonElement element = JsonParser.parseString(object);
            JsonObject json = element.getAsJsonObject();
            return json;
        }

        @SneakyThrows
        public JsonObject save(String url, Object object) {
            try {
                final Gson gson = Converters.registerLocalDateTime(new GsonBuilder()).create();
                String webClient = WebClient.builder().build().post()
                .uri(URL + url)
                .headers(header -> {
                    header.setBasicAuth(TOKEN_CRIPTO);
                    header.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                    header.setContentType(MediaType.APPLICATION_JSON);
                })
                .body(BodyInserters.fromValue(gson.toJson(object)))
                .retrieve()
                .bodyToMono(String.class).block();
                JsonElement element = JsonParser.parseString(webClient);
                JsonObject json = element.getAsJsonObject();
                return json;
            } catch(Exception e) {
                System.out.println(e.getMessage());
                e.getMessage();
            }
            return null;
        }
        
        
    }
