package com.noetherlab.client.utils;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public class DurationUtils {

    public static final double DAYS_IN_YEARS = 365.25d;


    public static double yearsBetween(Temporal t1, Temporal t2) {
        long sec = ChronoUnit.SECONDS.between(t1, t2);
        return  (double) sec / 3600 / 24 / DAYS_IN_YEARS;
    }

}
