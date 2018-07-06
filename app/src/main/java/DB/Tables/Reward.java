package DB.Tables;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

import DB.RewardType;

@Entity(tableName = "reward_table")
public class Reward {

    @PrimaryKey
    private Date date;

    @NonNull
    private RewardType type;

    private boolean usedReward;

    public Reward(Date date, @NonNull RewardType type) {
        this.date = date;
        this.type = type;
        this.usedReward = false;
    }

    @Ignore
    public Reward(Date date, @NonNull RewardType type, boolean usedReward) {
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

    public void setType(@NonNull RewardType type) {
        this.type = type;
    }

}

