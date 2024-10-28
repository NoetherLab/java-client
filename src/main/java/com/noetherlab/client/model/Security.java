package com.noetherlab.client.model;

import com.google.common.base.Joiner;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;

public class Security implements Comparable<Security> {

    final String exchangeMIC;
    final String symbol;

    String name;
    String country;
    String currency;
    String type;
    String ISIN;

    Integer CIK;

    Integer IBKRContractId;

    LocalDate startDate;
    LocalDate endDate;

    Set<String> tags = new HashSet<>();

    public static Security fromId(String key) {
        String[] s = key.split(":");
        return new Security(s[0], s[1]);
    }

    public String getId() {
        return getId(':');
    }

    public String getId(char c) {
        return getExchangeMIC() + c + getSymbol();
    }

    public Security(String MIC, String symbol) {
        this.exchangeMIC = MIC;
        this.symbol = symbol;
    }

    public String getExchangeMIC() {
        return exchangeMIC;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getISIN() {
        return ISIN;
    }

    public void setISIN(String ISIN) {
        this.ISIN = ISIN;
    }

    public Integer getCIK() {
        return CIK;
    }

    public void setCIK(Integer CIK) {
        this.CIK = CIK;
    }

    public Integer getIBKRContractId() {
        return IBKRContractId;
    }

    public void setIBKRContractId(Integer IBKRContractId) {
        this.IBKRContractId = IBKRContractId;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        if(startDate == null) {
            return LocalDate.MIN;
        }
        return startDate;
    }

    public LocalDate getEndDate() {
        if(endDate == null) {
            return LocalDate.MAX;
        }
        return endDate;
    }

    public boolean isLive(LocalDate d) {
        if(d.isEqual(getStartDate())) {
            return true;
        }
        return d.isAfter(getStartDate()) && d.isBefore(getEndDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Security security = (Security) o;
        return exchangeMIC.equals(security.exchangeMIC) && symbol.equals(security.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exchangeMIC, symbol);
    }

    public boolean equalsFull(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Security security = (Security) o;
        return exchangeMIC.equals(security.exchangeMIC) && symbol.equals(security.symbol) && Objects.equals(name, security.name) && Objects.equals(country, security.country) && Objects.equals(currency, security.currency) && Objects.equals(type, security.type) && Objects.equals(ISIN, security.ISIN) && Objects.equals(CIK, security.CIK) && Objects.equals(IBKRContractId, security.IBKRContractId);
    }

    public int hashCodeFull() {
        return Objects.hash(exchangeMIC, symbol, name, country, currency, type, ISIN, CIK, IBKRContractId);
    }

    @Override
    public int compareTo(Security o) {
        int MIComparison = exchangeMIC.compareTo(o.exchangeMIC);
        if(MIComparison == 0) {
            return symbol.compareTo(o.symbol);
        }
        return MIComparison;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", Security.class.getSimpleName() + "[", "]")
                .add("exchangeMIC='" + exchangeMIC + "'")
                .add("symbol='" + symbol + "'");

        if(name != null) {
            sj.add("name='" + name + "'");
        }
        if(country != null) {
            sj.add("country='" + country + "'");
        }
        if(currency != null) {
            sj.add("currency='" + currency + "'");
        }
        if(type != null) {
            sj.add("type='" + type + "'");
        }
        if(ISIN != null) {
            sj.add("ISIN='" + ISIN + "'");
        }
        if(CIK != null) {
            sj.add("CIK=" + CIK);
        }
        if(IBKRContractId != null) {
            sj.add("IBKRContractId=" + IBKRContractId);
        }
        if(tags != null && !tags.isEmpty()) {
            sj.add("Tags=" + Joiner.on(", ").join(tags));
        }
        return sj.toString();
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
}
