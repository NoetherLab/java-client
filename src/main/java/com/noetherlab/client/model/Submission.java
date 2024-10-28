package com.noetherlab.client.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.StringJoiner;

public class Submission extends ASubmission implements Comparable<Submission> {

    LocalDate reportDate;

    String act;

    String filmNumber;

    String items;

    Integer size;

    Boolean isXBRL;
    Boolean isInlineXBRL;

    String primaryDocument;

    String primaryDocDescription;

    SubmissionType type;
    Boolean isAmendment;

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public LocalDate getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(LocalDate filingDate) {
        this.filingDate = filingDate;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public LocalDateTime getAcceptanceDateTime() {
        return acceptanceDateTime;
    }

    public void setAcceptanceDateTime(LocalDateTime acceptanceDateTime) {
        this.acceptanceDateTime = acceptanceDateTime;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public String getFilmNumber() {
        return filmNumber;
    }

    public void setFilmNumber(String filmNumber) {
        this.filmNumber = filmNumber;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Boolean getXBRL() {
        return isXBRL;
    }

    public void setXBRL(Boolean XBRL) {
        isXBRL = XBRL;
    }

    public Boolean getInlineXBRL() {
        return isInlineXBRL;
    }

    public void setInlineXBRL(Boolean inlineXBRL) {
        isInlineXBRL = inlineXBRL;
    }

    public String getPrimaryDocument() {
        return primaryDocument;
    }

    public void setPrimaryDocument(String primaryDocument) {
        this.primaryDocument = primaryDocument;
    }

    public String getPrimaryDocDescription() {
        return primaryDocDescription;
    }

    public void setPrimaryDocDescription(String primaryDocDescription) {
        this.primaryDocDescription = primaryDocDescription;
    }

    public SubmissionType getType() {
        return type;
    }

    public void setType(SubmissionType type) {
        this.type = type;
    }

    public Boolean getAmendment() {
        return isAmendment;
    }

    public void setAmendment(Boolean amendment) {
        isAmendment = amendment;
    }

    @Override
    public int compareTo(Submission o) {
        return getReportDate().compareTo(o.getReportDate());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Submission.class.getSimpleName() + "[", "]")
                .add("accessionNumber='" + accessionNumber + "'")
                .add("filingDate=" + filingDate)
                .add("reportDate=" + reportDate)
                .add("acceptanceDateTime=" + acceptanceDateTime)
                .add("act='" + act + "'")
                .add("form='" + form + "'")
                .add("fileNumber='" + fileNumber + "'")
                .add("filmNumber='" + filmNumber + "'")
                .add("items='" + items + "'")
                .add("size=" + size)
                .add("isXBRL=" + isXBRL)
                .add("isInlineXBRL=" + isInlineXBRL)
                .add("primaryDocument='" + primaryDocument + "'")
                .add("primaryDocDescription='" + primaryDocDescription + "'")
                .toString();
    }
}