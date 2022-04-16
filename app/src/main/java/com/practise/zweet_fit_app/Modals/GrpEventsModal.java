package com.practise.zweet_fit_app.Modals;

public class GrpEventsModal {
    String gId,title,minP,maxP,levelUp,entryCoins,dur,target,participants,status,type;

    public GrpEventsModal(String gId, String title, String minP, String maxP, String levelUp, String entryCoins, String dur, String target, String participants, String status, String type) {
        this.gId = gId;
        this.title = title;
        this.minP = minP;
        this.maxP = maxP;
        this.levelUp = levelUp;
        this.entryCoins = entryCoins;
        this.dur = dur;
        this.target = target;
        this.participants = participants;
        this.status = status;
        this.type = type;
    }

    public GrpEventsModal() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMinP() {
        return minP;
    }

    public void setMinP(String minP) {
        this.minP = minP;
    }

    public String getMaxP() {
        return maxP;
    }

    public void setMaxP(String maxP) {
        this.maxP = maxP;
    }

    public String getLevelUp() {
        return levelUp;
    }

    public void setLevelUp(String levelUp) {
        this.levelUp = levelUp;
    }

    public String getEntryCoins() {
        return entryCoins;
    }

    public void setEntryCoins(String entryCoins) {
        this.entryCoins = entryCoins;
    }

    public String getDur() {
        return dur;
    }

    public void setDur(String dur) {
        this.dur = dur;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
    }
}
