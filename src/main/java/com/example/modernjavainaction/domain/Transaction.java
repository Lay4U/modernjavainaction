package com.example.modernjavainaction.domain;

public class Transaction {
    private final Trader trader;
    private final int year;
    private final int value;
    private final String referenceCode;

    public Transaction(Trader trader, int year, int value, String referenceCode) {
        this.trader = trader;
        this.year = year;
        this.value = value;
        this.referenceCode = referenceCode;
    }

    public Trader getTrader() {
        return this.trader;
    }

    public int getYear() {
        return this.year;
    }

    public int getValue() {
        return this.value;
    }

    public String getReferenceCode() {
        return this.referenceCode;
    }

    public String toString() {
        return "{" + this.trader + ", " +
                "year: " + this.year + ", " +
                "value:" + this.value + "}";
    }

}
