package com.practise.zweet_fit_app.Modals;

import java.util.List;

public class CoinTransactionParentModal {
    String date;
    List<CoinTransactionChildModal> childModalList;

    public CoinTransactionParentModal() {
    }

    public CoinTransactionParentModal(String date, List<CoinTransactionChildModal> childModalList) {
        this.date = date;
        this.childModalList = childModalList;
    }

    public List<CoinTransactionChildModal> getChildModalList() {
        return childModalList;
    }

    public void setChildModalList(List<CoinTransactionChildModal> childModalList) {
        this.childModalList = childModalList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
