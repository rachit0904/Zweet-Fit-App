package com.practise.zweet_fit_app.Modals;

public class StatRecordModal {
    String date,steps;
    Boolean streakAchieved;

    public StatRecordModal(String date, String steps, Boolean streakAchieved) {
        this.date = date;
        this.steps = steps;
        this.streakAchieved = streakAchieved;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public Boolean getStreakAchieved() {
        return streakAchieved;
    }

    public void setStreakAchieved(Boolean streakAchieved) {
        this.streakAchieved = streakAchieved;
    }
}
