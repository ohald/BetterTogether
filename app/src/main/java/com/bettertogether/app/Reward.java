package com.bettertogether.app;
import db.RewardType;

public class Reward {

    private String date;

    private RewardType type;

    private boolean usedReward;

    public Reward(String date, RewardType type) {
        this.date = date;
        this.type = type;
        this.usedReward = false;
    }


    public boolean isUsedReward() {
        return usedReward;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RewardType getType() {
        return type;
    }

    public void setType(RewardType type) {
        this.type = type;
    }

}

