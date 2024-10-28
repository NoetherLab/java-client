package com.noetherlab.client.io.csv;

import com.google.common.collect.Table;
import com.noetherlab.client.SortedDataFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SortedDataFrameIO {

    public static final Logger logger = LoggerFactory.getLogger(SortedDataFrameIO.class);

    public static <R, C, V> String writeDataAsCSVString(SortedDataFrame<R, C, V> table) {
        Formatters<R, C, V> formatters = defaultFormatter(table);
        return writeDataAsCSVString(table, formatters);
    }
    public static <R, C, V> String writeDataAsCSVString(SortedDataFrame<R, C, V> table, Formatters<R, C, V> formatters) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PrintStream p = new PrintStream(byteArrayOutputStream);

        p.print(formatters.getRowFormatter().getFieldName());
        p.print(CSVTools.DELIMITER);
        p.print("Key");
        p.print(CSVTools.DELIMITER);
        p.print("Value");
        p.println();

        for(Table.Cell<R, C, V> cell : table.getData().cellSet()) {
            p.print(formatters.getRowFormatter().format(cell.getRowKey()));
            p.print(CSVTools.DELIMITER);
            p.print(cell.getColumnKey());
            p.print(CSVTools.DELIMITER);
            if(cell.getValue() != null) {
                p.print(formatters.getValFormatter().format(cell.getValue()));
            }
            p.println();
        }
        p.close();

        return byteArrayOutputStream.toString();
    }

    public interface RowFormatter<R> {
        String getFieldName();

        String format(R row);

        R parse(String s);
    }

    public interface ColFormatter<C> {
        String getFieldName();

        String format(C col);

        C parse(String s);
    }

    public interface ValFormatter<V> {
        String format(V v);

        V parse(String s);
    }

    public static class DateRowFormatter implements RowFormatter<LocalDate> {

        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE;

        @Override
        public String getFieldName() {
            return "DateTime";
        }

        @Override
        public String format(LocalDate row) {
            return row.format(FORMATTER);
        }

        @Override
        public LocalDate parse(String s) {
            return LocalDate.parse(s, FORMATTER);
        }
    }

    public static class DateTimeRowFormatter implements RowFormatter<LocalDateTime> {

        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

        @Override
        public String getFieldName() {
            return "DateTime";
        }

        @Override
        public String format(LocalDateTime row) {
            return row.format(FORMATTER);
        }

        @Override
        public LocalDateTime parse(String s) {
            return LocalDateTime.parse(s, FORMATTER);
        }
    }

    public static class StringRowFormatter implements RowFormatter<String> {

        @Override
        public String getFieldName() {
            return "Row";
        }

        @Override
        public String format(String row) {
            return row;
        }

        @Override
        public String parse(String s) {
            return s;
        }
    }

    public static class StringColFormatter implements ColFormatter<String> {

        @Override
        public String getFieldName() {
            return "Key";
        }

        @Override
        public String format(String col) {
            return col;
        }

        @Override
        public String parse(String s) {
            return s;
        }
    }

    public static class IntegerColFormatter implements ColFormatter<Integer> {

        @Override
        public String getFieldName() {
            return "Key";
        }

        @Override
        public String format(Integer col) {
            return col.toString();
        }

        @Override
        public Integer parse(String s) {
            return Integer.parseInt(s);
        }
    }

    public static class FloatValFormatter implements ValFormatter<Float> {

        @Override
        public String format(Float v) {
            return v.toString();
        }

        @Override
        public Float parse(String s) {
            return Float.parseFloat(s);
        }
    }


    public static class DoubleValFormatter implements ValFormatter<Double> {

        @Override
        public String format(Double v) {
            return v.toString();
        }

        @Override
        public Double parse(String s) {
            return Double.parseDouble(s);
        }
    }

    public static class IntegerValFormatter implements ValFormatter<Integer> {

        @Override
        public String format(Integer v) {
            return v.toString();
        }

        @Override
        public Integer parse(String s) {
            return Integer.parseInt(s);
        }
    }

    public static class StringValFormatter implements ValFormatter<String> {

        @Override
        public String format(String v) {
            return CSVTools.escapeCSVString(v);
        }

        @Override
        public String parse(String s) {
            return CSVTools.removeQuotes(s);
        }
    }

    public static class Formatters<R, C, V> {
        RowFormatter<R> rowFormatter;
        ColFormatter<C> colFormatter;
        ValFormatter<V> valFormatter;

        public RowFormatter<R> getRowFormatter() {
            return rowFormatter;
        }

        public ColFormatter<C> getColFormatter() {
            return colFormatter;
        }

        public ValFormatter<V> getValFormatter() {
            return valFormatter;
        }
    }

    public static  <R, C, V> Formatters<R, C, V> defaultFormatter(SortedDataFrame<R, C, V> table) {

        Formatters formatters = new Formatters();

        if( table.getRowClass().equals(LocalDateTime.class)) {
            formatters.rowFormatter = new DateTimeRowFormatter();
        } else if( table.getRowClass().equals(LocalDate.class)) {
            formatters.rowFormatter = new DateRowFormatter();
        } else if( table.getRowClass().equals(String.class)) {
            formatters.rowFormatter = new StringRowFormatter();
        }

        if(table.getColClass().equals(String.class)) {
            formatters.colFormatter = new StringColFormatter();
        } else if(table.getColClass().equals(Integer.class)) {
            formatters.colFormatter = new IntegerColFormatter();
        }

        if(table.getValueClass().equals(Float.class)) {
            formatters.valFormatter = new FloatValFormatter();
        } else if(table.getValueClass().equals(Double.class)) {
            formatters.valFormatter = new DoubleValFormatter();
        } else if(table.getValueClass().equals(Integer.class)) {
            formatters.valFormatter = new IntegerValFormatter();
        } else if(table.getValueClass().equals(String.class)) {
            formatters.valFormatter = new StringValFormatter();
        }

        return formatters;
    }

    public static <R, C, V> void writeCSV(SortedDataFrame<R, C, V> table,
                                          Path path, String prefix) throws FileNotFoundException {
        Formatters<R, C, V> formatters = defaultFormatter(table);
        writeCSV(table, formatters, path, prefix);
    }



    public static <R, C, V> void writeCSV(SortedDataFrame<R, C, V> table,
                                          Formatters<R, C, V> formatters,
                                          Path path, String prefix) throws FileNotFoundException {

        PrintStream p = new PrintStream(getDataPath(path, prefix).toFile());

        p.print(formatters.getRowFormatter().getFieldName());
        p.print(CSVTools.DELIMITER);
        p.print("Key");
        p.print(CSVTools.DELIMITER);
        p.print("Value");
        p.println();

        for(Table.Cell<R, C, V> cell : table.getData().cellSet()) {
            p.print(formatters.getRowFormatter().format(cell.getRowKey()));
            p.print(CSVTools.DELIMITER);
            p.print(cell.getColumnKey());
            p.print(CSVTools.DELIMITER);
            if(cell.getValue() != null) {
                p.print(formatters.getValFormatter().format(cell.getValue()));
            }
            p.println();
        }
        p.close();

        p = new PrintStream(getMetaPath(path, prefix).toFile());
        p.print("DataKey");
        p.print(CSVTools.DELIMITER);
        p.print("Key");
        p.print(CSVTools.DELIMITER);
        p.print("Value");
        p.println();

        for(Table.Cell<C, String, Object> cell : table.getMetaData().cellSet()) {
            p.print(cell.getRowKey());
            p.print(CSVTools.DELIMITER);
            p.print(cell.getColumnKey());
            p.print(CSVTools.DELIMITER);
            if(cell.getValue() != null) {
                p.print(CSVTools.escapeCSVString(cell.getValue().toString()));
            }
            p.println();
        }
        p.close();


        /*


        Set<String> columnsMeta = table.getMetaData().columnKeySet();
        p.print("Key");
        for(String c : columnsMeta) {
            p.print(CSVTools.DELIMITER);
            p.print(c);
        }
        p.println();


        for(Map.Entry<C, Map<String, Object>> rowMap : table.getMetaData().rowMap().entrySet()) {
            p.print(rowMap.getKey());
            Map<String, Object> row = rowMap.getValue();
            for(String c : columnsMeta) {
                p.print(CSVTools.DELIMITER);
                Object value = row.get(c);
                if(value != null) {
                    p.print(CSVTools.escapeCSVString(value.toString()));
                }
            }
            p.println();
        }*/

        p.close();

    }

    public static <R, C, V> void readCSV(SortedDataFrame<R, C, V> table,
                                         Formatters<R, C, V> formatters,
                                         Path path, String prefix) {

        try (InputStream isMeta = new FileInputStream(getMetaPath(path, prefix).toFile());
             InputStream isData = new FileInputStream(getDataPath(path, prefix).toFile()) ) {

            readCSV(table, formatters, isMeta, isData);

        } catch (IOException e) {
            logger.error("", e);
        }
    }

    public static <R, C, V> SortedDataFrame<R, C, V> readCSV(SortedDataFrame<R, C, V> table,
                                                             Path path, String prefix) {

        File metaFile = getMetaPath(path, prefix).toFile();
        File dataFile = getDataPath(path, prefix).toFile();

        if(!metaFile.exists() || !dataFile.exists()) {
            return null;
        }

        try (InputStream isMeta = new FileInputStream(metaFile);
             InputStream isData = new FileInputStream(dataFile)) {

            Formatters<R, C, V> formatters = defaultFormatter(table);
            readCSV(table, formatters, isMeta, isData);

            return table;

        } catch (IOException e) {
            logger.error("", e);
            return null;
        }
    }



    public static <R, C, V> void readCSV(SortedDataFrame<R, C, V> table,
                                         InputStream metaInputStream, InputStream dataInputStream) {

        Formatters<R, C, V> formatters = defaultFormatter(table);
        readCSV(table, formatters, metaInputStream, dataInputStream);
    }

    public static <R, C, V> void readCSV(SortedDataFrame<R, C, V> table,
                                         Formatters<R, C, V> formatters,
                                         InputStream metaInputStream, InputStream dataInputStream) {

        if(metaInputStream != null) {
            try (Scanner scanner = new Scanner(metaInputStream)) {
                Map<Integer, String> header = new HashMap<>();
                while (scanner.hasNextLine()) {
                    String[] chunks = scanner.nextLine().split(CSVTools.DELIMITER);
                    if (header.isEmpty()) {
                        for (int i = 1; i < chunks.length; ++i) {
                            header.put(i, chunks[i]);
                        }
                        continue;
                    }

                    for (int i = 1; i < chunks.length; ++i) {
                        table.getMetaData().put(
                                formatters.getColFormatter().parse(chunks[0]),
                                header.get(i),
                                chunks[i]);
                    }
                }
            }
        }

        try (Scanner scanner = new Scanner(dataInputStream)) {

            boolean header = false;
            while (scanner.hasNextLine()) {
                String[] chunks = scanner.nextLine().split(CSVTools.DELIMITER);
                if(!header) { header = true; continue;}

                R rowName = formatters.getRowFormatter().parse(chunks[0]);
                C colName =  formatters.getColFormatter().parse(chunks[1]);
                V value =  formatters.getValFormatter().parse(chunks[2]);

                table.getData().put(rowName, colName, value);
            }
        }

    }

    static Path getDataPath(Path path, String prefix) {
        return path.resolve(prefix + "_data" + CSVTools.EXTENSION);
    }

    static Path getMetaPath(Path path, String prefix) {
        return path.resolve(prefix + "_meta" + CSVTools.EXTENSION);
    }


}
