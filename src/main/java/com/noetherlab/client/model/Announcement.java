package com.noetherlab.client.model;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class Announcement  extends ASubmission {

    //https://en.wikipedia.org/wiki/Form_8-K#cite_note-3
    public static final Map<String, String> SECTIONS_V2 = new LinkedHashMap<>();
    public static final Map<String, String> ITEM_DESCRIPTION_V1 = new LinkedHashMap<>();
    public static final Map<String, String> ITEM_DESCRIPTION_V2 = new LinkedHashMap<>();
    static {
        ITEM_DESCRIPTION_V1.put("1", "Changes in Control of Registrant");
        ITEM_DESCRIPTION_V1.put("2", "Acquisition or Disposition of Assets");
        ITEM_DESCRIPTION_V1.put("3", "Bankruptcy or Receivership");
        ITEM_DESCRIPTION_V1.put("4", "Changes in Registrant's Certifying Accountant");
        ITEM_DESCRIPTION_V1.put("5", "Other Events");
        ITEM_DESCRIPTION_V1.put("6", "Resignation of Registrant's Directors");
        ITEM_DESCRIPTION_V1.put("7", "Financial Statements and Exhibits");
        ITEM_DESCRIPTION_V1.put("8", "Change in Fiscal Year");
        ITEM_DESCRIPTION_V1.put("9", "Regulation FD Disclosure");
        ITEM_DESCRIPTION_V1.put("10", "Amendments to the Registrant's Code of Ethics");
        ITEM_DESCRIPTION_V1.put("11", "Temporary Suspension of Trading Under Registrant's Employee Benefit Plans");
        ITEM_DESCRIPTION_V1.put("12", "Results of Operations and Financial Condition");

        SECTIONS_V2.put("1", "Registrant's Business and Operations");
        SECTIONS_V2.put("2", "Financial Information");
        SECTIONS_V2.put("3", "Securities and Trading Markets");
        SECTIONS_V2.put("4", "Matters Related to Accountants and Financial Statements");
        SECTIONS_V2.put("5", "Corporate Governance and Management");
        SECTIONS_V2.put("6", "Asset-Backed Securities");
        SECTIONS_V2.put("7", "Regulation FD");
        SECTIONS_V2.put("8", "Other Events");
        SECTIONS_V2.put("9", "Financial Statements and Exhibits");

        ITEM_DESCRIPTION_V2.put("1.01", "Entry into a Material Definitive Agreement");
        ITEM_DESCRIPTION_V2.put("1.02", "Termination of a Material Definitive Agreement");
        ITEM_DESCRIPTION_V2.put("1.03", "Bankruptcy or Receivership");
        ITEM_DESCRIPTION_V2.put("1.04", "Mine Safety - Reporting of Shutdowns and Patterns of Violations");
        ITEM_DESCRIPTION_V2.put("1.05", "Material Cybersecurity Incidents");
        ITEM_DESCRIPTION_V2.put("2.01", "Completion of Acquisition or Disposition of Assets");
        ITEM_DESCRIPTION_V2.put("2.02", "Results of Operations and Financial Condition");
        ITEM_DESCRIPTION_V2.put("2.03", "Creation of a Direct Financial Obligation or an Obligation under an Off-Balance Sheet Arrangement of a Registrant");
        ITEM_DESCRIPTION_V2.put("2.04", "Triggering Events That Accelerate or Increase a Direct Financial Obligation or an Obligation under an Off-Balance Sheet Arrangement");
        ITEM_DESCRIPTION_V2.put("2.05", "Costs Associated with Exit or Disposal Activities");
        ITEM_DESCRIPTION_V2.put("2.06", "Material Impairments");
        ITEM_DESCRIPTION_V2.put("3.01", "Notice of Delisting or Failure to Satisfy a Continued Listing Rule or Standard; Transfer of Listing");
        ITEM_DESCRIPTION_V2.put("3.02", "Unregistered Sales of Equity Securities");
        ITEM_DESCRIPTION_V2.put("3.03", "Material Modification to Rights of Security Holders");
        ITEM_DESCRIPTION_V2.put("4.01", "Changes in Registrant's Certifying Accountant");
        ITEM_DESCRIPTION_V2.put("4.02", "Non-Reliance on Previously Issued Financial Statements or a Related Audit Report or Completed Interim Review");
        ITEM_DESCRIPTION_V2.put("5.01", "Changes in Control of Registrant");
        ITEM_DESCRIPTION_V2.put("5.02", "Departure of Directors or Certain Officers; Election of Directors; Appointment of Certain Officers; Compensatory Arrangements of Certain Officers");
        ITEM_DESCRIPTION_V2.put("5.03", "Amendments to Articles of Incorporation or Bylaws; Change in Fiscal Year");
        ITEM_DESCRIPTION_V2.put("5.04", "Temporary Suspension of Trading Under Registrant's Employee Benefit Plans");
        ITEM_DESCRIPTION_V2.put("5.05", "Amendment to Registrant's Code of Ethics, or Waiver of a Provision of the Code of Ethics");
        ITEM_DESCRIPTION_V2.put("5.06", "Change in Shell Company Status");
        ITEM_DESCRIPTION_V2.put("5.07", "Submission of Matters to a Vote of Security Holders");
        ITEM_DESCRIPTION_V2.put("5.08", "Shareholder Director Nominations");
        ITEM_DESCRIPTION_V2.put("6.01", "ABS Informational and Computational Material");
        ITEM_DESCRIPTION_V2.put("6.02", "Change of Servicer or Trustee");
        ITEM_DESCRIPTION_V2.put("6.03", "Change in Credit Enhancement or Other External Support");
        ITEM_DESCRIPTION_V2.put("6.04", "Failure to Make a Required Distribution");
        ITEM_DESCRIPTION_V2.put("6.05", "Securities Act Updating Disclosure");
        ITEM_DESCRIPTION_V2.put("7.01", "Regulation FD Disclosure");
        ITEM_DESCRIPTION_V2.put("8.01", "Other Events");
        ITEM_DESCRIPTION_V2.put("9.01", "Financial Statements and Exhibits");
    }

    LocalDate reportDate;

    Integer version;

    String sectionId;
    String itemId;

    String sectionDesc;
    String itemDesc;

    String url;

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }


    public String getSectionDesc() {
        return sectionDesc;
    }

    public void setSectionDesc(String sectionDesc) {
        this.sectionDesc = sectionDesc;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
