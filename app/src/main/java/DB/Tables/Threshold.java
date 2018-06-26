package DB.Tables;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import DB.RewardType;

@Entity(tableName = "threshold_table")
public class Threshold {

    @PrimaryKey
    @NonNull
    private RewardType type;

    private int threshold;

    public Threshold(@NonNull RewardType type, int threshold) {
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

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

}

