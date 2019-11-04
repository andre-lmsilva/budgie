package io.geekmind.budgie.model.entity;

public enum Currency {
    EUR("EUR", "Euros", "€"),
    BRL("BRL", "Brazilian Real", "R$");

    private final String currencyCode;
    private final String currencyName;
    private final String currencySymbol;

    Currency(String currencyCode, String currencyName, String currencySymbol) {
        this.currencyCode = currencyCode;
        this.currencyName = currencyName;
        this.currencySymbol = currencySymbol;
    }

    public String currencyCode() {
        return this.currencyCode;
    }

    public String currencyName() {
        return this.currencyName;
    }

    public String currencySymbol() {
        return this.currencySymbol;
    }

}
