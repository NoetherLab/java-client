package com.noetherlab.client.io.csv;

import com.noetherlab.client.model.Announcement;
import com.noetherlab.client.model.Submission;
import com.opencsv.CSVReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SubmissionsIO {

    public static final Logger logger = LoggerFactory.getLogger(SubmissionsIO.class);

    public static Collection<Submission> submissionsFromCSV(String string) {
        try {
            return submissionsFromCSV(new ByteArrayInputStream(string.getBytes()));
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }
    public static Collection<Submission> submissionsFromCSV(Path p) {
        try {
            return submissionsFromCSV(new FileInputStream(p.toFile()));
        } catch (Exception e) {
            logger.error("", e);
            return null;
        }
    }

    public static Collection<Submission> submissionsFromCSV(InputStream is) {
        Collection<Submission> submissions = new ArrayList<>();
        try (CSVReader br = new CSVReader(new InputStreamReader(is))) {
            String[] chunks;
            Map<String, Integer> header = new HashMap<>();
            while ((chunks = br.readNext()) != null)  {
                if(header.isEmpty()) {
                    for(int i = 0; i < chunks.length; ++i) {
                        header.put(chunks[i], i);
                    }
                } else {
                    Submission submission = new Submission();

                    submission.setAccessionNumber(chunks[header.get("AccessionNumber")]);
                    String filingDate = chunks[header.get("FilingDate")].trim();
                    if(!filingDate.isEmpty()) {
                        submission.setFilingDate(LocalDate.parse(filingDate, DateTimeFormatter.ISO_DATE));
                    }
                    String reportDate = chunks[header.get("ReportDate")].trim();
                    if(!reportDate.isEmpty()) {
                        submission.setReportDate(LocalDate.parse(reportDate, DateTimeFormatter.ISO_DATE));
                    }
                    String acceptanceDateTime = chunks[header.get("AcceptanceDateTime")];
                    if(!acceptanceDateTime.isEmpty()) {
                        submission.setAcceptanceDateTime(LocalDateTime.parse(acceptanceDateTime, DateTimeFormatter.ISO_DATE_TIME));
                    }
                    submission.setAct(chunks[header.get("Act")]);
                    submission.setForm(chunks[header.get("Form")]);
                    submission.setFileNumber(chunks[header.get("FileNumber")]);
                    submission.setFilmNumber(chunks[header.get("FilmNumber")]);
                    submission.setItems(CSVTools.removeQuotes(chunks[header.get("Items")]));
                    submission.setSize(Integer.parseInt(chunks[header.get("Size")]));
                    String XBRL = chunks[header.get("XBRL")];
                    if(!XBRL.isEmpty()) {
                        submission.setXBRL(Boolean.parseBoolean(XBRL));
                    }
                    String inlineXBRL = chunks[header.get("InlineXBRL")];
                    if(!inlineXBRL.isEmpty()) {
                        submission.setInlineXBRL(Boolean.parseBoolean(inlineXBRL));
                    }
                    submission.setPrimaryDocument(CSVTools.removeQuotes(chunks[header.get("PrimaryDocument")]));
                    submission.setPrimaryDocDescription(CSVTools.removeQuotes(chunks[header.get("PrimaryDocDescription")]));

                    submissions.add(submission);
                }
            }
        } catch (Exception e) {
            logger.error("", e);
        }

        return submissions;
    }

    public static Collection<Announcement> announcementsFromCSV(String s) {
        return announcementsFromCSV(new StringReader(s));
    }

    public static Collection<Announcement> announcementsFromCSV(Reader reader) {
        Collection<Announcement> announcements = new ArrayList<>();

        try (CSVReader br = new CSVReader(reader)) {
            String[] chunks;
            Map<String, Integer> header = new HashMap<>();
            while ((chunks = br.readNext()) != null) {
                if (header.isEmpty()) {
                    for (int i = 0; i < chunks.length; ++i) {
                        header.put(chunks[i], i);
                    }
                } else {
                    Announcement announcement = new Announcement();

                    announcement.setAccessionNumber(chunks[header.get("AccessionNumber")]);

                    String filingDate = chunks[header.get("FilingDate")].trim();
                    if (!filingDate.isEmpty()) {
                        announcement.setFilingDate(LocalDate.parse(filingDate, DateTimeFormatter.ISO_DATE));
                    }



                    String reportDate = chunks[header.get("ReportDate")].trim();
                    if (!reportDate.isEmpty()) {
                        announcement.setReportDate(LocalDate.parse(reportDate, DateTimeFormatter.ISO_DATE));
                    }
                    String acceptanceDateTime = chunks[header.get("AcceptanceDateTime")];
                    if (!acceptanceDateTime.isEmpty()) {
                        announcement.setAcceptanceDateTime(LocalDateTime.parse(acceptanceDateTime, DateTimeFormatter.ISO_DATE_TIME));
                    }
                    announcement.setForm(chunks[header.get("Form")]);
                    announcement.setFileNumber(chunks[header.get("FileNumber")]);
                    announcement.setVersion(Integer.valueOf(chunks[header.get("Version")]));
                    announcement.setSectionId(chunks[header.get("Section.Id")]);
                    announcement.setItemId(chunks[header.get("Item.Id")]);
                    announcement.setSectionDesc(chunks[header.get("Section.Desc")]);
                    announcement.setItemDesc(chunks[header.get("Item.Desc")]);
                    announcement.setUrl(chunks[header.get("URL")]);
                    announcement.setUrlViewer(chunks[header.get("URL.Viewer")]);

                    announcements.add(announcement);
                }
            }

        } catch (Exception e) {
            logger.error("", e);
        }

        return announcements;
    }

    public static void submissionsToCSV(Map<Integer, Collection<Submission>> submissions, OutputStream os) {
        PrintStream s = new PrintStream(os);
        printHeader(s);

        for(Map.Entry<Integer, Collection<Submission>> k : submissions.entrySet()) {
            for(Submission submission : k.getValue()) {
                printSubmission(s, k.getKey(),  submission);
            }
        }
        s.close();
    }

    public static void submissionsToCSV(Map<Integer, Collection<Submission>> submissions, Path p) throws FileNotFoundException {
        PrintStream s = new PrintStream(p.toFile());
        printHeader(s);

        for(Map.Entry<Integer, Collection<Submission>> k : submissions.entrySet()) {
            for(Submission submission : k.getValue()) {
                printSubmission(s, k.getKey(),  submission);
            }
        }
        s.close();
    }

    public static void submissionsToCSV(Integer cik, Collection<Submission> submissions, Path p) throws FileNotFoundException {
        PrintStream s = new PrintStream(p.toFile());
        printHeader(s);
        printSubmission(s, cik, submissions);
        s.close();
    }

    public static void printHeader(PrintStream s) {
        s.print("CIK");
        s.print(CSVTools.DELIMITER);
        s.print("AccessionNumber");
        s.print(CSVTools.DELIMITER);
        s.print("FilingDate");
        s.print(CSVTools.DELIMITER);
        s.print("ReportDate");
        s.print(CSVTools.DELIMITER);
        s.print("AcceptanceDateTime");
        s.print(CSVTools.DELIMITER);
        s.print("Act");
        s.print(CSVTools.DELIMITER);
        s.print("Form");
        s.print(CSVTools.DELIMITER);
        s.print("FileNumber");
        s.print(CSVTools.DELIMITER);
        s.print("FilmNumber");
        s.print(CSVTools.DELIMITER);
        s.print("Items");
        s.print(CSVTools.DELIMITER);
        s.print("Size");
        s.print(CSVTools.DELIMITER);
        s.print("XBRL");
        s.print(CSVTools.DELIMITER);
        s.print("InlineXBRL");
        s.print(CSVTools.DELIMITER);
        s.print("PrimaryDocument");
        s.print(CSVTools.DELIMITER);
        s.print("PrimaryDocDescription");
        s.print(CSVTools.DELIMITER);
        s.print("Type");
        s.print(CSVTools.DELIMITER);
        s.print("isAmendment");
        s.print(CSVTools.DELIMITER);
        s.print("URL");
        s.print(CSVTools.DELIMITER);
        s.print("URL.Viewer");
        s.print(CSVTools.DELIMITER);
        s.print("Local.Need");
        s.print(CSVTools.DELIMITER);
        s.print("Local.Has");
        s.println();
    }

    public static void printSubmission(PrintStream s, Integer cik, Collection<Submission> submissions) {
        for (Submission submission : submissions) {
            printSubmission(s, cik, submission);
        }
    }

    public static void printSubmission(PrintStream s, Integer cik,  Submission submission) {
        if(cik != null) {
            s.print(cik);
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getAccessionNumber() != null) {
            s.print(submission.getAccessionNumber());
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getFilingDate() != null) {
            s.print(submission.getFilingDate());
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getReportDate() != null) {
            s.print(submission.getReportDate());
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getAcceptanceDateTime() != null) {
            s.print(submission.getAcceptanceDateTime());
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getAct() != null) {
            s.print(submission.getAct());
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getForm() != null) {
            s.print(submission.getForm());
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getFileNumber() != null) {
            s.print(submission.getFileNumber());
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getFilmNumber() != null) {
            s.print(submission.getFilmNumber());
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getItems() != null) {
            s.print(CSVTools.escapeCSVString(submission.getItems()));
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getSize() != null) {
            s.print(submission.getSize());
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getXBRL() != null) {
            s.print(submission.getXBRL());
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getInlineXBRL() != null) {
            s.print(submission.getInlineXBRL());
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getPrimaryDocDescription() != null) {
            s.print(CSVTools.escapeCSVString(submission.getPrimaryDocument()));
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getPrimaryDocDescription() != null) {
            s.print(CSVTools.escapeCSVString(submission.getPrimaryDocDescription()));
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getType() != null) {
            s.print(submission.getType().name());
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getType() != null) {
            s.print(submission.getAmendment());
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getUrl() != null) {
            s.print(CSVTools.escapeCSVString(submission.getUrl()));
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getUrl() != null) {
            s.print(CSVTools.escapeCSVString(submission.getUrlViewer()));
        }

        s.print(CSVTools.DELIMITER);
        if(submission.getUrl() != null) {
            //TODO:
        }
        s.print(CSVTools.DELIMITER);
        if(submission.getUrl() != null) {
            //TODO:
        }

        s.println();
    }

    public static void announcementsToCSV(Map<Integer, Collection<Announcement>> announcements, OutputStream os) {
        PrintStream s = new PrintStream(os);
        printHeaderAnnouncement(s);

        for(Map.Entry<Integer, Collection<Announcement>> k : announcements.entrySet()) {
            for(Announcement announcement : k.getValue()) {
                printAnnouncement(s, k.getKey(),  announcement);
            }
        }
        s.close();
    }

    private static void printAnnouncement(PrintStream s, Integer cik, Announcement announcement) {
        if(cik != null) {
            s.print(cik);
        }
        s.print(CSVTools.DELIMITER);
        if(announcement.getAccessionNumber() != null) {
            s.print(announcement.getAccessionNumber());
        }
        s.print(CSVTools.DELIMITER);
        if(announcement.getFilingDate() != null) {
            s.print(announcement.getFilingDate());
        }
        s.print(CSVTools.DELIMITER);
        if(announcement.getReportDate() != null) {
            s.print(announcement.getReportDate());
        }
        s.print(CSVTools.DELIMITER);
        if(announcement.getAcceptanceDateTime() != null) {
            s.print(announcement.getAcceptanceDateTime());
        }
        s.print(CSVTools.DELIMITER);
        if(announcement.getForm() != null) {
            s.print(announcement.getForm());
        }
        s.print(CSVTools.DELIMITER);
        if(announcement.getFileNumber() != null) {
            s.print(announcement.getFileNumber());
        }
        s.print(CSVTools.DELIMITER);
        if(announcement.getVersion() != null) {
            s.print(announcement.getVersion());
        }
        s.print(CSVTools.DELIMITER);
        if(announcement.getSectionId() != null) {
            s.print(announcement.getSectionId());
        }
        s.print(CSVTools.DELIMITER);
        if(announcement.getItemId() != null) {
            s.print(announcement.getItemId());
        }
        s.print(CSVTools.DELIMITER);
        if(announcement.getSectionDesc() != null) {
            s.print(CSVTools.escapeCSVString(announcement.getSectionDesc()));
        }
        s.print(CSVTools.DELIMITER);
        if(announcement.getItemDesc() != null) {
            s.print(CSVTools.escapeCSVString(announcement.getItemDesc()));
        }
        s.print(CSVTools.DELIMITER);
        if(announcement.getUrl() != null) {
            s.print(CSVTools.escapeCSVString(announcement.getUrl()));
        }
        s.print(CSVTools.DELIMITER);
        if(announcement.getUrl() != null) {
            s.print(CSVTools.escapeCSVString(announcement.getUrlViewer()));
        }
        s.println();
    }

    private static void printHeaderAnnouncement(PrintStream s) {
        s.print("CIK");
        s.print(CSVTools.DELIMITER);
        s.print("AccessionNumber");
        s.print(CSVTools.DELIMITER);
        s.print("FilingDate");
        s.print(CSVTools.DELIMITER);
        s.print("ReportDate");
        s.print(CSVTools.DELIMITER);
        s.print("AcceptanceDateTime");
        s.print(CSVTools.DELIMITER);
        s.print("Form");
        s.print(CSVTools.DELIMITER);
        s.print("FileNumber");
        s.print(CSVTools.DELIMITER);
        s.print("Version");
        s.print(CSVTools.DELIMITER);
        s.print("Section.Id");
        s.print(CSVTools.DELIMITER);
        s.print("Item.Id");
        s.print(CSVTools.DELIMITER);
        s.print("Section.Desc");
        s.print(CSVTools.DELIMITER);
        s.print("Item.Desc");
        s.print(CSVTools.DELIMITER);
        s.print("URL");
        s.print(CSVTools.DELIMITER);
        s.print("URL.Viewer");
        s.println();
    }

}
