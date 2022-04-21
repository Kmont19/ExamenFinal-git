package com.example.examenfinal.services.proxy;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateDeserializer implements JsonSerializer<LocalDateTime>,JsonDeserializer<LocalDateTime> {
    private  final  ThreadLocal<DateTimeFormatter> formatter =
            ThreadLocal.withInitial( () ->
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    Locale.US)
                    .withZone( ZoneOffset.UTC));


    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type typeOF, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            if (jsonElement == null || jsonElement.isJsonNull())
                return LocalDateTime.now();
            return LocalDateTime.parse(jsonElement.getAsString(), formatter.get());
        }catch (Exception e){
            return   LocalDateTime.now();
        }


    }
    @Override
    public synchronized JsonElement serialize(LocalDateTime date, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(date.format(formatter.get()));
    }
}
