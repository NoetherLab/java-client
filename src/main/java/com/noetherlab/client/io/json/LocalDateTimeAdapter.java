package com.noetherlab.client.io.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime>  {
    private final DateTimeFormatter formatter;

    public LocalDateTimeAdapter() {
        this.formatter = DateTimeFormatter.ISO_DATE_TIME;
    }

    public LocalDateTimeAdapter(String pattern) {
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    public LocalDateTimeAdapter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if(localDateTime != null) {
            jsonWriter.value(formatter.format(localDateTime));
        } else {
            jsonWriter.nullValue();
        }
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        String date = jsonReader.nextString();
        return LocalDateTime.parse(date, formatter);
    }

}
