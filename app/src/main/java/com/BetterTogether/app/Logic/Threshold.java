package com.BetterTogether.app.Logic;

import DB.RewardType;

public class Threshold {

    private RewardType type;

    private int threshold;

    public Threshold(RewardType type, int threshold) {
        this.type = type;
        this.threshold = threshold;
    }

    public RewardType getType() {
        return type;
    }

    public void setType(RewardType type) {
        this.type = type;
    }

    public int getThreshold() {
        return threshold;
    }

}

