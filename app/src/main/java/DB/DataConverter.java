package DB;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;


public class DataConverter {

    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }

    @TypeConverter
    public String fromRewardType(RewardType rewardType) {
        if (rewardType == null)
            return null;
        return rewardType.toString();
    }

    @TypeConverter
    public RewardType toRewardType(String value) {
        if (value == null)
            return null;
        switch (value) {
            case "cake":
                return RewardType.CAKE;
            case "pizza":
                return RewardType.PIZZA;
            //maybe not use this as default??
            default:
                return null;
        }
    }


}
