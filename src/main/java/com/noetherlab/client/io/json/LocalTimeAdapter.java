package com.noetherlab.client.io.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter  extends TypeAdapter<LocalTime> {

    private final DateTimeFormatter formatter;

    public LocalTimeAdapter() {
        this.formatter = DateTimeFormatter.ISO_TIME;
    }

    public LocalTimeAdapter(String pattern) {
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    public LocalTimeAdapter(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void write(JsonWriter jsonWriter, LocalTime localDateTime) throws IOException {
        if(localDateTime != null) {
            jsonWriter.value(formatter.format(localDateTime));
        } else {
            jsonWriter.nullValue();
        }
    }

    @Override
    public LocalTime read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        String date = jsonReader.nextString();
        return LocalTime.parse(date, formatter);
    }
}
