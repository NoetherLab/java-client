package com.noetherlab.client.model;


/*
https://www.investopedia.com/terms/s/sec-form-s-8.asp
 */

import java.util.Set;

public enum SubmissionType {

    Periodic_Financials,
    Announcement,
    CapTable_Insider,
    CapTable_Large,
    CapTable_Employee,
    //https://www.13dmonitor.com/ActivistGlossary.aspx
    CapTable_Investors_Active,
    CapTable_Investors_Passive,
    Assets,
    IPO;


    public final static Set<String> Periodic_Financials_Forms = Set.of("10-Q", "10-K");
    public final static Set<String> CapTable_Insider_Forms = Set.of("3", "4");

}
