package com.practise.zweet_fit_app.Modals;

public class GrpLeaderboardModal {
    String gId,name,rank,steps,coins,participantlvl,dp;

    public GrpLeaderboardModal(String gId, String name, String rank, String steps, String coins, String participantlvl, String dp) {
        this.gId = gId;
        this.name = name;
        this.rank = rank;
        this.steps = steps;
        this.coins = coins;
        this.participantlvl = participantlvl;
        this.dp = dp;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getCoins() { return coins; }

    public void setCoins(String coins) { this.coins = coins; }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getParticipantlvl() {
        return participantlvl;
    }

    public void setParticipantlvl(String participantlvl) {
        this.participantlvl = participantlvl;
    }
}
