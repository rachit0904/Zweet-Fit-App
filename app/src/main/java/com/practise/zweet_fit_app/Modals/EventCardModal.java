package com.practise.zweet_fit_app.Modals;

import java.util.List;

public class EventCardModal {
    String date;
    List<GrpEventsModal> grpEventsModalList;

    public EventCardModal(String date, List<GrpEventsModal> grpEventsModalList) {
        this.date = date;
        this.grpEventsModalList = grpEventsModalList;
    }

    public EventCardModal() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<GrpEventsModal> getGrpEventsModalList() {
        return grpEventsModalList;
    }

    public void setGrpEventsModalList(List<GrpEventsModal> grpEventsModalList) {
        this.grpEventsModalList = grpEventsModalList;
    }
}
