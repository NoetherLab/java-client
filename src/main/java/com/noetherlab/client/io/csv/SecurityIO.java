package com.noetherlab.client.io.csv;

import com.google.common.base.Joiner;
import com.noetherlab.client.model.Security;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class SecurityIO {

    public static final Logger logger = LoggerFactory.getLogger(SecurityIO.class);

    public static void toCSV(Collection<Security> securities, File f) throws FileNotFoundException {
        toCSV(securities, new FileOutputStream(f));
    }

    public static void toCSV(Collection<Security> securities, OutputStream f) {
        PrintStream s = new PrintStream(f);

        s.print("Security.Id");
        s.print(CSVTools.DELIMITER);
        s.print("ExchangeMIC");
        s.print(CSVTools.DELIMITER);
        s.print("Symbol");
        s.print(CSVTools.DELIMITER);
        s.print("Name");
        s.print(CSVTools.DELIMITER);
        s.print("Country.Alpha3");
        s.print(CSVTools.DELIMITER);
        s.print("Currency");
        s.print(CSVTools.DELIMITER);
        s.print("Type");
        s.print(CSVTools.DELIMITER);
        s.print("ISIN");
        s.print(CSVTools.DELIMITER);
        s.print("CIK");
        s.print(CSVTools.DELIMITER);
        s.print("Tags");
        s.println();

        for(Security sec : securities) {

            if(sec == null) {
                s.print(CSVTools.DELIMITER);
                s.print(CSVTools.DELIMITER);
                s.print(CSVTools.DELIMITER);
                s.print(CSVTools.DELIMITER);
                s.print(CSVTools.DELIMITER);
                s.print(CSVTools.DELIMITER);
                s.print(CSVTools.DELIMITER);
                s.print(CSVTools.DELIMITER);
                s.print(CSVTools.DELIMITER);
                s.println();
                continue;
            }

            if(sec.getId() != null) {
                s.print(sec.getId());
            }
            s.print(CSVTools.DELIMITER);
            if(sec.getExchangeMIC() != null) {
                s.print(sec.getExchangeMIC());
            }
            s.print(CSVTools.DELIMITER);
            if(sec.getSymbol() != null) {
                s.print(sec.getSymbol());
            }
            s.print(CSVTools.DELIMITER);
            if(sec.getName() != null) {
                s.print(CSVTools.escapeCSVString(sec.getName()));
            }
            s.print(CSVTools.DELIMITER);
            if(sec.getCountry() != null) {
                s.print(sec.getCountry());
            }
            s.print(CSVTools.DELIMITER);
            if(sec.getCurrency() != null) {
                s.print(sec.getCurrency() );
            }
            s.print(CSVTools.DELIMITER);
            if(sec.getType() != null) {
                s.print(sec.getType() );
            }
            s.print(CSVTools.DELIMITER);
            if(sec.getISIN() != null) {
                s.print(sec.getISIN());
            }
            s.print(CSVTools.DELIMITER);
            if(sec.getCIK() != null) {
                s.print(sec.getCIK());
            }
            s.print(CSVTools.DELIMITER);
            if(sec.getTags() != null && !sec.getTags().isEmpty()) {
                s.print(CSVTools.escapeCSVString(Joiner.on(", ").join(sec.getTags())));
            }
            s.println();
        }

        s.close();
    }

    public static Collection<Security> fromCSV(Reader r) {
        List<Security> securities = new ArrayList<>();
        HashMap<String, Integer> header = new HashMap<>();

        try (CSVReader csvReader = new CSVReader(r)) {
            String[] chunks;
            while ((chunks = csvReader.readNext()) != null)  {
                if (header.isEmpty()) {
                    for (int i = 0; i < chunks.length; ++i) {
                        header.put(chunks[i], i);
                    }
                    continue;
                }
                Security security = parseSecurity(chunks, header);
                securities.add(security);
            }
        } catch (Exception e) {
            logger.error("", e);
        }

        return securities;
    }

    public static Collection<Security> fromCSV(String s) {
        return fromCSV(new StringReader(s));
    }


    static Security parseSecurity(String[] chunks, Map<String, Integer> header) {
        Security security = new Security(chunks[header.get("ExchangeMIC")], chunks[header.get("Symbol")]);
        security.setName(CSVTools.removeQuotes(chunks[header.get("Name")]).isEmpty() ? null : CSVTools.removeQuotes(chunks[header.get("Name")]) );
        security.setCountry(chunks[header.get("Country.Alpha3")].isEmpty() ? null : chunks[header.get("Country.Alpha3")]);
        security.setCurrency(chunks[header.get("Currency")].isEmpty() ? null : chunks[header.get("Currency")]);
        security.setType(chunks[header.get("Type")].isEmpty() ? null : chunks[header.get("Type")]);
        security.setISIN(chunks[header.get("ISIN")].isEmpty() ? null : chunks[header.get("ISIN")]);
        security.setCIK(chunks[header.get("CIK")].isEmpty() ? null : Integer.parseInt(chunks[header.get("CIK")]));
        return security;
    }

}
