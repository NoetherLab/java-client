package com.noetherlab.client.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class ASubmission {

    String form;
    LocalDate filingDate;

    String accessionNumber;
    String fileNumber;

    LocalDateTime acceptanceDateTime;

    String url;
    String urlViewer;

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public LocalDate getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(LocalDate filingDate) {
        this.filingDate = filingDate;
    }

    public String getAccessionNumber() {
        return accessionNumber;
    }

    public void setAccessionNumber(String accessionNumber) {
        this.accessionNumber = accessionNumber;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public LocalDateTime getAcceptanceDateTime() {
        return acceptanceDateTime;
    }

    public void setAcceptanceDateTime(LocalDateTime acceptanceDateTime) {
        this.acceptanceDateTime = acceptanceDateTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlViewer() {
        return urlViewer;
    }

    public void setUrlViewer(String urlViewer) {
        this.urlViewer = urlViewer;
    }
}
