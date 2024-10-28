package com.noetherlab.client.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringJoiner;

public class SECSecurity {
    public static final Logger logger = LoggerFactory.getLogger(Security.class);

    public enum Type {
        COMMON_STOCK,
        AMERICAN_DEPOSITARY_SHARES,
        WARRANT,
        PREFERRED_STOCK,
        NOTES
    }

    public enum Features {
        CONVERTIBLE,
        REDEEMABLE,
        CUMULATIVE,
        PERPETUAL
    }


    final Set<String> classOfStockVariants = new LinkedHashSet<>();
    String classOfStock;
    String name;
    String tradingSymbol;
    String exchangeName;
    String act;
    Type type;

    LocalDate minDate;
    LocalDate maxDate;

    Set<Features> featuresSet = new HashSet<>();


    public String getClassOfStock() {
        return classOfStock;
    }

    public void setClassOfStock(String classOfStock) {
        this.classOfStock = classOfStock;
    }

    public Set<String> getClassOfStockVariants() {
        return classOfStockVariants;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getTradingSymbol() {
        return tradingSymbol;
    }

    public void setTradingSymbol(String tradingSymbol) {
        this.tradingSymbol = tradingSymbol;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LocalDate getMinDate() {
        return minDate;
    }

    public void setMinDate(LocalDate minDate) {
        this.minDate = minDate;
    }

    public LocalDate getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(LocalDate maxDate) {
        this.maxDate = maxDate;
    }

    public Set<Features> getFeaturesSet() {
        return featuresSet;
    }

    public boolean isMarketable() {
        return (getAct() == null || getAct().equals("12b")) && (getType() == Type.COMMON_STOCK || getType() == Type.AMERICAN_DEPOSITARY_SHARES) ;
    }

    //https://www.investopedia.com/terms/p/preferredstock.asp
    public void findType() {

        String name = getName().replaceAll(" +", " ");

        if(getName() != null) {
            if (
                    name.toLowerCase().contains("preferred stock") ||
                            getName().toLowerCase().contains("preferred shares") ||
                            getName().toLowerCase().contains("preference shares")) {

                type = Type.PREFERRED_STOCK;

                if(name.toLowerCase().contains("convertible")) {
                    featuresSet.add(Features.CONVERTIBLE);
                }
                if(name.toLowerCase().contains("perpetual")) {
                    featuresSet.add(Features.PERPETUAL);
                }
                if(name.toLowerCase().contains("redeemable")) {
                    featuresSet.add(Features.REDEEMABLE);
                }
                if(name.toLowerCase().contains("cumulative")) {
                    featuresSet.add(Features.CUMULATIVE);
                }
            } else if (
                    name.toLowerCase().contains("american depositary shares") ||
                            name.toLowerCase().contains("depositary shares") ||
                            name.toLowerCase().contains("depositary shrs") ) {
                type = Type.AMERICAN_DEPOSITARY_SHARES;
            } else if (name.toLowerCase().contains("warrant")) {
                type = Type.WARRANT;
            } else if (
                    name.toLowerCase().contains("common stock") ||
                            name.toLowerCase().contains("common shares") ||
                            name.toLowerCase().contains("ordinary shares") ||
                            name.toLowerCase().contains("ordinary share") ||
                            name.toLowerCase().contains("capital stock") ||
                            name.toLowerCase().contains("shares")

            ) {
                type = Type.COMMON_STOCK;
            } else if (name.toLowerCase().contains("notes") || name.toLowerCase().contains("debentures") || name.toLowerCase().contains("bonds")) {
                type = Type.NOTES;
            }
            //Ordinary Shares
        }

        if(type == null) {
            logger.warn("Unknown type for name " + getName());
        }
    }


    @Override
    public String toString() {
        return new StringJoiner(", ", Security.class.getSimpleName() + "[", "]")
                .add("classOfStockVariants=" + classOfStockVariants)
                .add("classOfStock='" + classOfStock + "'")
                .add("name='" + name + "'")
                .add("tradingSymbol='" + tradingSymbol + "'")
                .add("exchangeName='" + exchangeName + "'")
                .add("act='" + act + "'")
                .add("type=" + type)
                .add("minDate=" + minDate)
                .add("maxDate=" + maxDate)
                .add("featuresSet=" + featuresSet)
                .toString();
    }
}
