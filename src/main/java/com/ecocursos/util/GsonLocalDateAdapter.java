package com.ecocursos.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GsonLocalDateAdapter implements JsonSerializer<LocalDateTime>,JsonDeserializer<LocalDateTime>{

//        @Override
//        public LocalDate deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//            String ldtString = jsonElement.getAsString();
//            return LocalDate.parse(ldtString,DateTimeFormatter.ISO_LOCAL_DATE);
//
//        }
//
//        @Override
//        public JsonElement serialize(LocalDate localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
//            return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE));
//        }

    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String ldtString = json.getAsString();
        return LocalDateTime.parse(ldtString,DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
