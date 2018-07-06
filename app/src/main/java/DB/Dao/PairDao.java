package DB.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

import DB.RewardType;
import DB.Tables.Pair;


@Dao
public interface PairDao {


    @Query("SELECT * FROM pair_table WHERE date>=:startTime")
    List<Pair> getHistory(Date startTime);

    @Query("SELECT * FROM pair_table WHERE date>=(SELECT max(date) FROM reward_table WHERE type=:rewardType)")
    List<Pair> getPairsSinceLastReward(RewardType rewardType);

    @Query("SELECT COUNT(*) AS pairProgramUserTotal FROM pair_table WHERE person1 = :user OR person2 = :user")
    int getTimesPairProgrammed(String user);

    @Query("SELECT COUNT(*) AS pairProgramTotalFromDate FROM pair_table WHERE date >= :date")
    int getPairProgrammingTotalFromDate(Date date);

    @Query("SELECT * FROM pair_table WHERE person1 =:person OR person2 =:person")
    List<Pair> getPairProgrammingPairs(String person);

    @Query("SELECT * FROM pair_table WHERE date =:date")
    Pair getPair(Date date);

    @Insert
    long insertPair(Pair pair);

    @Update
    int updatePair(Pair pair);

    @Delete
    void deletePair(Pair pair);

}
