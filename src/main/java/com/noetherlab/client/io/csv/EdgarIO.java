package com.noetherlab.client.io.csv;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.noetherlab.client.model.SECSecurity;
import com.noetherlab.client.model.Submission;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EdgarIO {

    public static final Logger logger = LoggerFactory.getLogger(EdgarIO.class);

    public static Collection<SECSecurity> securitiesFromCSV(String string) {
        try {
            return securitiesFromCSV(new ByteArrayInputStream(string.getBytes()));
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    public static Collection<SECSecurity> securitiesFromCSV(InputStream is) {
        Collection<SECSecurity> securities = new ArrayList<>();
        try (CSVReader br = new CSVReader(new InputStreamReader(is))) {
            String[] chunks;
            Map<String, Integer> header = new HashMap<>();
            while ((chunks = br.readNext()) != null)  {
                if(header.isEmpty()) {
                    for(int i = 0; i < chunks.length; ++i) {
                        header.put(chunks[i], i);
                    }
                } else {
                    SECSecurity security = new SECSecurity();
                    security.setClassOfStock(chunks[header.get("ClassOfStock")]);
                    security.getClassOfStockVariants().addAll(Splitter.on(", ").trimResults().splitToList(CSVTools.removeQuotes(chunks[header.get("ClassOfStock.Variants")])));
                    security.setAct(chunks[header.get("Act")]);
                    security.setExchangeName(chunks[header.get("Exchange.MIC")]);
                    security.setTradingSymbol(chunks[header.get("Symbol")]);
                    security.setName(chunks[header.get("Name")]);
                    security.setType(SECSecurity.Type.valueOf(chunks[header.get("Type")]));
                    security.setMinDate(LocalDate.parse(chunks[header.get("MinDate")], DateTimeFormatter.ISO_DATE));
                    security.setMaxDate(LocalDate.parse(chunks[header.get("MaxDate")], DateTimeFormatter.ISO_DATE));

                    for(String fs : Splitter.on(",").trimResults().omitEmptyStrings().splitToList(CSVTools.removeQuotes(chunks[header.get("FeatureSet")]))) {
                        security.getFeaturesSet().add(SECSecurity.Features.valueOf(fs));
                    }


                    securities.add(security);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }

        return securities;

    }

    public static void securitiesToCSV(Collection<SECSecurity> securities, Path p) throws FileNotFoundException {

        try (PrintStream sSec = new PrintStream(p.toFile())) {

            sSec.print("ClassOfStock");
            sSec.print(CSVTools.DELIMITER);
            sSec.print("ClassOfStock.Variants");
            sSec.print(CSVTools.DELIMITER);
            sSec.print("Act");
            sSec.print(CSVTools.DELIMITER);
            sSec.print("Exchange.MIC");
            sSec.print(CSVTools.DELIMITER);
            sSec.print("Symbol");
            sSec.print(CSVTools.DELIMITER);
            sSec.print("Name");
            sSec.print(CSVTools.DELIMITER);
            sSec.print("Type");
            sSec.print(CSVTools.DELIMITER);
            sSec.print("MinDate");
            sSec.print(CSVTools.DELIMITER);
            sSec.print("MaxDate");
            sSec.print(CSVTools.DELIMITER);
            sSec.print("FeatureSet");
            sSec.println();

            for(SECSecurity sec : securities) {
                if(sec.getClassOfStock() != null) {
                    sSec.print(sec.getClassOfStock());
                }
                sSec.print(CSVTools.DELIMITER);
                sSec.print(CSVTools.escapeCSVString(Joiner.on(", ").skipNulls().join(sec.getClassOfStockVariants())));
                sSec.print(CSVTools.DELIMITER);
                sSec.print(sec.getAct());
                sSec.print(CSVTools.DELIMITER);
                if(sec.getExchangeName() != null) {
                    sSec.print(sec.getExchangeName());
                }
                sSec.print(CSVTools.DELIMITER);
                if(sec.getTradingSymbol() != null) {
                    sSec.print(sec.getTradingSymbol());
                }
                sSec.print(CSVTools.DELIMITER);
                sSec.print(CSVTools.escapeCSVString(sec.getName()));
                sSec.print(CSVTools.DELIMITER);
                if(sec.getType() != null) {
                    sSec.print(sec.getType());
                }
                sSec.print(CSVTools.DELIMITER);
                if(sec.getMinDate() != null) {
                    sSec.print(sec.getMinDate().format(DateTimeFormatter.ISO_DATE));
                }
                sSec.print(CSVTools.DELIMITER);
                if(sec.getMaxDate() != null) {
                    sSec.print(sec.getMaxDate().format(DateTimeFormatter.ISO_DATE));
                }
                sSec.print(CSVTools.DELIMITER);
                sSec.print(CSVTools.escapeCSVString(Joiner.on(",").join(sec.getFeaturesSet())));
                sSec.println();

            }
        }
    }
}

