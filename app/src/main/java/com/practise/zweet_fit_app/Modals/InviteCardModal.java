package com.practise.zweet_fit_app.Modals;

public class InviteCardModal {
    String eId,eventName,inviteFrom,rid,sid;

    public InviteCardModal(String eId, String eventName, String inviteFrom, String rid, String sid) {
        this.eId = eId;
        this.eventName = eventName;
        this.inviteFrom = inviteFrom;
        this.rid = rid;
        this.sid = sid;
    }

    public InviteCardModal() {

    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
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
