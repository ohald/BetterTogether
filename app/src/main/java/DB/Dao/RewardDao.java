package DB.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;

import DB.RewardType;
import DB.Tables.Reward;
import DB.Tables.Threshold;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface RewardDao {

    @Query("SELECT threshold FROM threshold_table WHERE type=:type")
    int getThreshold(RewardType type);

    @Query("SELECT date FROM reward_table WHERE date = (SELECT max(date) FROM reward_table)")
    Date getLastRewardDate();

    @Query("SELECT type FROM reward_table WHERE date = (SELECT max (date) FROM reward_table)")
    RewardType getLastRewardType();

    @Query("SELECT COUNT(*) FROM reward_table")
    int getNumberOfRewards();

    //0 equals false
    @Query("SELECT COUNT(*) FROM reward_table WHERE type = :type AND usedReward = 0")
    int numberOfUnusedRewards(RewardType type);

    @Update
    void updateReward(Reward reward);

    @Insert
    void addReward(Reward reset);

    @Insert(onConflict = REPLACE)
    void addThreshold(Threshold newThreshold);

    @Update
    void setThreshold(Threshold newThreshold);

}
