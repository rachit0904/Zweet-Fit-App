package com.practise.zweet_fit_app.Modals;

public class InviteCardModal {
    String eId,eventName,inviteFrom;

    public InviteCardModal(String eId, String eventName, String inviteFrom) {
        this.eId = eId;
        this.eventName = eventName;
        this.inviteFrom = inviteFrom;
    }

    public String geteId() {
        return eId;
    }

    public void seteId(String eId) {
        this.eId = eId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getInviteFrom() {
        return inviteFrom;
    }

    public void setInviteFrom(String inviteFrom) {
        this.inviteFrom = inviteFrom;
    }
}
