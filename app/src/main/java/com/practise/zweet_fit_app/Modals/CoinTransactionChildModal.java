package com.practise.zweet_fit_app.Modals;

public class CoinTransactionChildModal {
    String source,coins;

    public CoinTransactionChildModal() {
    }

    public CoinTransactionChildModal(String source, String coins) {
        this.source = source;
        this.coins = coins;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCoins() {
        return coins;
    }

    public void setCoins(String coins) {
        this.coins = coins;
    }
}
