package com.noetherlab.client.io.csv;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class CSVTools {

    public static final String DELIMITER = ",";
    public static final String DELIMITER_STRING = "\"";
    public static final String EXTENSION = ".csv";

    public static String escapeCSVString(String input) {
        return DELIMITER_STRING + input + DELIMITER_STRING;
    }


    public static String removeQuotes(String value) {
        if (value.startsWith(DELIMITER_STRING)) {
            value = value.substring(1, value.length());
        }

        if (value.endsWith(DELIMITER_STRING)) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    public static List<String> split(String line) {
        return split(line, CSVTools.DELIMITER);
    }

    public static List<String> split(String line, String delimiter) {
        return Lists.newArrayList(line.split(delimiter));
    }

    public static List<String> splitDelimiter(String line, String delimiter, String stringDelimiter) {

        List<String> chunks = new ArrayList<>();
        String currentChunk = "";
        boolean isEscaping = false;
        for(int i = 0; i < line.length(); ++i) {

            if(line.charAt(i) == delimiter.charAt(0) && !isEscaping) {
                chunks.add(currentChunk);
                currentChunk = "";
            } else if(line.charAt(i) == stringDelimiter.charAt(0)) {

                isEscaping = !isEscaping;

            } else {
                currentChunk += line.charAt(i);
            }
        }
        chunks.add(currentChunk);

        return chunks;
    }
}
