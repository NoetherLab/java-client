package com.noetherlab.client.model;

import com.noetherlab.client.SortedDataFrame;
import com.noetherlab.client.utils.DurationUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public class GeomBrownian extends Model {

    Double lastSt;

    //Linear model
    Double St;
    /**
     Percentage drift, [drift] = [%]/[T] = [%].[T]^{-1}
     Time unit = Year^{-1}
     */
    double drift;
    /**
     Percentage volatility, [volatility] = [%]/sqrt([T]) = [%].[T]^{-1/2}
     Time unit = Year^{-1/2}
     */
    double volatility;

    Double R2;

    Double lastZScore;

    public static GeomBrownian fromRateVol(double R, double volatility) {
        //R = exp(rate) - 1
        //rate = getDrift() - 0.5d * getVolatility() * getVolatility()
        double rate = Math.log(R + 1d);
        double drift = rate + 0.5d * volatility * volatility;

        GeomBrownian geomBrownian = new GeomBrownian();
        geomBrownian.setDrift(drift);
        geomBrownian.setVolatility(volatility);
        return geomBrownian;
    }

    public static GeomBrownian fromInstRateVol(double rate, double volatility) {
        //rate = getDrift() - 0.5d * getVolatility() * getVolatility()
        double drift = rate + 0.5d * volatility * volatility;

        GeomBrownian geomBrownian = new GeomBrownian();
        geomBrownian.setDrift(drift);
        geomBrownian.setVolatility(volatility);
        return geomBrownian;
    }

    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }

    public Double getSt() {
        return St;
    }

    public void setSt(double st) {
        this.St = st;
    }

    public double getDrift() {
        return drift;
    }

    public void setDrift(double drift) {
        this.drift = drift;
    }

    public double getVolatility() {
        return volatility;
    }

    public Double getRTrend() {
        return Math.exp(getRate()) - 1d;
    }

    public Double predictedValue(LocalDateTime t) {
        if(t == null) {
            return null;
        }

        double deltaT = DurationUtils.yearsBetween(getT(), t);
        return getSt() * Math.exp( getRate() * deltaT);
    }

    public void predict(Security security, SortedDataFrame<LocalDate, String, Float> ts) {

        for(Map.Entry<LocalDate, Map<String, Float>> e : ts.getData().rowMap().entrySet()) {
            String field = "AdjClose";
            if(!security.getCurrency().equals("USD")) {
                field += ".USD";
            }
            Float adjustedClosed = e.getValue().get(field);
            if(adjustedClosed != null) {
                ts.getData().put(e.getKey(), field + ".Pred", predictedValue(e.getKey().atStartOfDay()).floatValue());
            }
        }
    }

    public Double getRUpside() {
        Double lastPredicted = predictedValue(T);
        if(lastPredicted == null || lastSt ==null) {
            return null;
        }
        return (lastPredicted - lastSt)/lastSt;
    }

    public Double getRPotential() {
        Double Rupside = getRUpside();
        Double RTrend = getRTrend();

        if(Rupside == null || RTrend == null) {
            return null;
        }

        return (1 + Rupside) * (1 + RTrend) - 1;
    }

    public void setLastZScore(double lastZScore) {
        this.lastZScore = lastZScore;
    }

    public Double getLastZScore() {
        return lastZScore;
    }

    public Double getR2() {
        return R2;
    }

    public void setR2(double r2) {
        R2 = r2;
    }


    public Double getLastSt() {
        return lastSt;
    }

    public void setLastSt(double lastSt) {
        this.lastSt = lastSt;
    }

    public double getRate() {
        return getDrift() - 0.5d * getVolatility() * getVolatility();
    }

}
