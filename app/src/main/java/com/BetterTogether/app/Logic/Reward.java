package com.BetterTogether.app.Logic;
import java.util.Date;
import DB.RewardType;

public class Reward {

    private Date date;

    private RewardType type;

    private boolean usedReward;

    public Reward(Date date, RewardType type) {
        this.date = date;
        this.type = type;
        this.usedReward = false;
    }

    public Reward(Date date, RewardType type, boolean usedReward) {
        this.date = date;
        this.type = type;
        this.usedReward = usedReward;
    }

    public boolean isUsedReward() {
        return usedReward;
    }

    public void setUsedReward(boolean usedReward) {
        this.usedReward = usedReward;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RewardType getType() {
        return type;
    }

    public void setType(RewardType type) {
        this.type = type;
    }

}

