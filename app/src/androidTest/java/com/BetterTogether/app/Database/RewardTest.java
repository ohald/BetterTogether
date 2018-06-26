package com.BetterTogether.app.Database;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

import DB.Dao.RewardDao;
import DB.RewardType;
import DB.SQLiteDB;
import DB.Tables.Reward;
import DB.Tables.Threshold;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class RewardTest {

    private RewardDao mRewardDao;
    private SQLiteDB mDb;

    private Date d = new Date();
    private Reward cakeReward  = new Reward(d, RewardType.CAKE);
    private Reward pizzaReward = new Reward(new Date(d.getTime()-1000), RewardType.PIZZA);

    private int threshold = 100;
    private int updatedThreshold = threshold + 10;
    private Threshold cakeThreshold = new Threshold(RewardType.CAKE, threshold);


    @Before
    public void createDbWithEntries() {
        Context context = InstrumentationRegistry.getTargetContext();
        mDb = Room.inMemoryDatabaseBuilder(context, SQLiteDB.class).build();
        mRewardDao = mDb.rewardDao();

        addEntriesToDB();
    }

    private void addEntriesToDB(){
        mRewardDao.addReward(pizzaReward);
        mRewardDao.addReward(cakeReward);
        mRewardDao.addThreshold(cakeThreshold);
    }

    @After
    public void closeDb(){
        mDb.close();
    }

    @Test
    public void whenTwoEntriesAreAddedToDBNumberOfEntriesEqualsTwo(){
        assertThat(2, equalTo(mRewardDao.getNumberOfRewards()));
    }

    @Test
    public void getLastRewardDateFindsTheNewestDate(){
      assertThat(d, equalTo(mRewardDao.getLastRewardDate()));
    }

    @Test
    public void getLastRewardTypeFindsTheTypeOfMostRecentReward(){
        assertThat(RewardType.CAKE, equalTo(mRewardDao.getLastRewardType()));
    }

    @Test
    public void canGetThresholdValueFromDatabaseByRewardType(){
        assertThat(threshold, equalTo(mRewardDao.getThreshold(RewardType.CAKE)));
    }

    private void updateThresholdForCakeReward(){
        mRewardDao.setThreshold(new Threshold(RewardType.CAKE, updatedThreshold));
    }

    @Test
    public void canUpdateExistingThresholdInDatabase(){
        updateThresholdForCakeReward();
        assertThat(updatedThreshold, equalTo(mRewardDao.getThreshold(RewardType.CAKE)));
    }

    @Test
    public void updatingThresholdGivesSameAmountOfEntriesInEntity(){
        int numEntries = mRewardDao.getNumberOfRewards();
        updateThresholdForCakeReward();
        assertThat(numEntries, equalTo(mRewardDao.getNumberOfRewards()));
    }

    private int allUnusedRewards(){
        return mRewardDao.numberOfUnusedRewards(RewardType.CAKE) + mRewardDao.numberOfUnusedRewards(RewardType.PIZZA);
    }

    @Test
    public void usingOneRewardRemovesOneRewardFromUnusedCount(){
        int unused = mRewardDao.numberOfUnusedRewards(RewardType.CAKE) + mRewardDao.numberOfUnusedRewards(RewardType.PIZZA);
        cakeReward.setUsedReward(true);
        mRewardDao.updateReward(cakeReward);

        assertThat(unused-1, equalTo(allUnusedRewards()));

    }

    @Test
    public void unusedRewardsInDatabaseEqualsTotalAmountOfEntriesWhenNoRewardsAreUsed(){
        //no rewards are set to used
        assertThat(mRewardDao.getNumberOfRewards(), equalTo((allUnusedRewards())));
    }



}
