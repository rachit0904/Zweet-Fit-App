package com.practise.zweet_fit_app.Modals;

public class YearlyRecordModal {
    String monthName;
    String steps,cal,dist;

    public YearlyRecordModal() {
    }

    public YearlyRecordModal(String monthName, String steps, String cal, String dist) {
        this.monthName = monthName;
        this.steps = steps;
        this.cal = cal;
        this.dist = dist;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getCal() {
        return cal;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }

    public String getDist() {
        return dist;
    }

    public void setDist(String dist) {
        this.dist = dist;
    }
}
