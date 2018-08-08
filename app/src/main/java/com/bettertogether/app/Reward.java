package com.bettertogether.app;
import db.RewardType;

public class Reward {

    private RewardType type;

    private boolean usedReward;

    public Reward(RewardType type) {
        this.type = type;
        this.usedReward = false;
    }

    public boolean isUsedReward() {
        return usedReward;
    }

    public RewardType getType() {
        return type;
    }

    public void setType(RewardType type) {
        this.type = type;
    }

}

