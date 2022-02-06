package com.practise.zweet_fit_app.Util;

public class Step_Item {
    private String date;
    private String data;
    private long timestamp;
    int steps;

    public Step_Item(String date, String steps, long timestamp) {
        this.date = date;
        this.data = steps;
        this.timestamp = timestamp;
    }

    public Step_Item() {

    }

    public Step_Item(String s, int nSteps) {
        this.date=s;
        this.steps=nSteps;
    }

    public String getDate() {
        return date;
    }

    public String getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        Step_Item temp = (Step_Item)obj;
        return this.date.equals(temp.getDate());
    }
}

